package cz.trixi.parsexml.api;

import cz.trixi.parsexml.api.dto.MunicipalityPartResponse;
import cz.trixi.parsexml.api.dto.MunicipalityResponse;
import cz.trixi.parsexml.api.dto.PageResponse;
import cz.trixi.parsexml.job.MunicipalityPartService;
import cz.trixi.parsexml.persistence.repository.MunicipalityPartRepository;
import cz.trixi.parsexml.persistence.repository.MunicipalityRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("municipality_parts")
public class MunicipalityPartController {

    private final MunicipalityPartRepository repository;
    private final MunicipalityPartService service;

    public MunicipalityPartController(MunicipalityPartRepository repository, MunicipalityPartService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    public PageResponse<MunicipalityPartResponse> listParts(@PageableDefault(size = 20) Pageable pageable) {
        return PageResponse.from(repository.findAll(pageable).map(MunicipalityPartResponse::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MunicipalityPartResponse> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(MunicipalityPartResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
