package cz.trixi.parsexml.api;

import cz.trixi.parsexml.api.dto.MunicipalityResponse;
import cz.trixi.parsexml.api.dto.PageResponse;
import cz.trixi.parsexml.job.MunicipalityService;
import cz.trixi.parsexml.persistence.repository.MunicipalityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/municipalities")
public class MunicipalityController {

    private static final Logger log = LoggerFactory.getLogger(MunicipalityController.class);
    private final MunicipalityRepository repository;
    private final MunicipalityService service;

    public MunicipalityController(MunicipalityRepository repository, MunicipalityService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    public PageResponse<MunicipalityResponse> listMunicipalities(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Endpoint call: GET /municipalities (page={}, size={})", pageable.getPageNumber(), pageable.getPageSize());
        return PageResponse.from(repository.findAll(pageable).map(MunicipalityResponse::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MunicipalityResponse> findById(@PathVariable Long id) {
        log.info("Endpoint call: GET /municipalities/{} (id={})", id, id);
        return service.findById(id)
                .map(MunicipalityResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
