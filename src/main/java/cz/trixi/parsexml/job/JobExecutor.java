package cz.trixi.parsexml.job;

import cz.trixi.parsexml.persistence.entity.Municipality;
import cz.trixi.parsexml.persistence.entity.MunicipalityPart;
import cz.trixi.parsexml.persistence.entity.ParsingRun;
import cz.trixi.parsexml.persistence.repository.MunicipalityRepository;
import cz.trixi.parsexml.persistence.repository.ParsingRunRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class JobExecutor {

    private static final Logger log = LoggerFactory.getLogger(JobExecutor.class);
    private final UrlSourceConnector connector;
    private final MunicipalityRepository municipalityRepository;
    private final ParsingRunRepository runRepository;

    public JobExecutor(UrlSourceConnector connector, MunicipalityRepository repository, ParsingRunRepository runRepository) {
        this.connector = connector;
        this.municipalityRepository = repository;
        this.runRepository = runRepository;
    }

    @Async("parsingExecutor")
    public void execute(Long runId) {
        log.info("Starting execute() for runId={}", runId);

        byte[] zipBytes;

        try {
            zipBytes = connector.callApiEndpoint();
            log.info("Downloaded ZIP payload for runId={}, size={} bytes", runId, zipBytes.length);
        } catch (Exception e) {
            log.error("Failed downloading ZIP payload for runId={}", runId, e);
            finishWithFailure(runId, Instant.now());
            throw new RuntimeException(e.getMessage(), e.getCause());
        }

        XMLInputFactory factory = XMLInputFactory.newFactory();

        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);

        Municipality toAdd = new Municipality();
        List<MunicipalityPart> parts = new ArrayList<>();


        try (ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry;
            while ((entry = zipStream.getNextEntry()) != null) {
                String name = entry.getName().toLowerCase(Locale.ROOT);
                if (!name.endsWith(".xml")) {
                    log.debug("Skipping non-XML ZIP entry '{}' for runId={}", entry.getName(), runId);
                    continue;
                }
                log.info("Processing XML ZIP entry '{}' for runId={}", entry.getName(), runId);

                XMLStreamReader reader = factory.createXMLStreamReader(zipStream, StandardCharsets.UTF_8.name());
                try {

                    boolean inObec = false;
                    boolean inCastObce = false;
                    boolean inCoiObec = false;

                    String castObceKod = null;
                    String castObceNazev = null;

                    while (reader.hasNext()) {
                        int event = reader.next();

                        if (event == XMLStreamConstants.START_ELEMENT) {
                            String elementName = reader.getLocalName();

                            switch (elementName) {
                                case "Obec" -> {
                                    if (inCastObce) {
                                        inCoiObec = true;
                                    } else {
                                        inObec = true;
                                    }
                                }
                                case "CastObce" -> inCastObce = true;
                                case "Kod" -> {
                                    String kodValue = reader.getElementText();
                                    if (inObec && !inCastObce && reader.getPrefix().equals("obi")) {
                                        toAdd.setCode(kodValue);
                                    } else if (inCastObce) {
                                        if (!inCoiObec) {
                                            castObceKod = kodValue;
                                        }
                                    }
                                }
                                case "Nazev" -> {
                                    String nazevValue = reader.getElementText();
                                    if (inObec && !inCastObce) {
                                        toAdd.setName(nazevValue);
                                    } else if (inCastObce && !inCoiObec) {
                                        castObceNazev = nazevValue;
                                    }
                                }
                            }
                        } else if (event == XMLStreamConstants.END_ELEMENT) {
                            String elementName = reader.getLocalName();

                            switch (elementName) {
                                case "Obec" -> {
                                    if (inCoiObec) {
                                        inCoiObec = false;
                                    } else if (inObec) {
                                        inObec = false;
                                    }
                                }
                                case "CastObce" -> {
                                    inCastObce = false;
                                    MunicipalityPart tmp = new MunicipalityPart();
                                    tmp.setCode(castObceKod);
                                    tmp.setName(castObceNazev);
                                    tmp.setMunicipality(toAdd);
                                    parts.add(tmp);

                                    castObceKod = null;
                                    castObceNazev = null;
                                }
                                case "CastiObci" -> {
                                    reader.close();
                                    toAdd.setParts(parts);

                                    municipalityRepository.findByCode(toAdd.getCode())
                                            .map(existing -> {
                                                log.info("Updating existing municipality code={} for runId={}", existing.getCode(), runId);
                                                existing.setName(toAdd.getName());
                                                existing.setParts(toAdd.getParts());
                                                for (MunicipalityPart part : existing.getParts()) {
                                                    part.setMunicipality(existing);
                                                }
                                                return municipalityRepository.save(existing);
                                            }).orElseGet(() -> {
                                                log.info("Creating new municipality code={} for runId={}", toAdd.getCode(), runId);
                                                return municipalityRepository.save(toAdd);
                                            });

                                    finishSuccessfully(runId, Instant.now());
                                    log.info("Finished execute() successfully for runId={}", runId);
                                    return;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("Failed while parsing/storing data for runId={}", runId, e);
                    finishWithFailure(runId, Instant.now());
                    throw new RuntimeException("Failed to complete execute() method", e);
                }
            }
            log.error("No XML file found in ZIP for runId={}", runId);
            finishWithFailure(runId, Instant.now());
            throw new IllegalStateException("No XML file found in ZIP");
        } catch (IOException | XMLStreamException e) {
            log.error("Failed to parse XML from ZIP payload for runId={}", runId, e);
            finishWithFailure(runId, Instant.now());
            throw new IllegalStateException("Failed to parse XML from ZIP payload", e);
        }
    }

    private void finishSuccessfully(Long runId, Instant finishedAt) {
        runRepository.findById(runId).ifPresent(run -> {
            run.succeed(finishedAt);
            runRepository.save(run);
        });
    }

    private void finishWithFailure(Long runId, Instant finishedAt) {
        runRepository.findById(runId).ifPresent(run -> {
            run.fail(finishedAt);
            runRepository.save(run);
        });
    }
}
