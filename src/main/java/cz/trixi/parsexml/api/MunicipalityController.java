package cz.trixi.parsexml.api;

import cz.trixi.parsexml.api.dto.MunicipalityResponse;
import cz.trixi.parsexml.api.dto.PageResponse;
import cz.trixi.parsexml.persistence.repository.MunicipalityRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/municipalities")
public class MunicipalityController {

    private final MunicipalityRepository repository;

    public MunicipalityController(MunicipalityRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public PageResponse<MunicipalityResponse> listMunicipalities(@PageableDefault(size = 20) Pageable pageable) {
        return PageResponse.from(repository.findAll(pageable).map(MunicipalityResponse::from));
    }
}
