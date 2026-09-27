package cz.trixi.parsexml.job;

import cz.trixi.parsexml.persistence.entity.Municipality;
import cz.trixi.parsexml.persistence.entity.ParsingRun;
import cz.trixi.parsexml.persistence.repository.MunicipalityRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class MunicipalityService {

    private final MunicipalityRepository repository;

    public MunicipalityService(MunicipalityRepository repository) {
        this.repository = repository;
    }

    public Optional<Municipality> findById(Long id) {
        return repository.findById(id);
    }
}
