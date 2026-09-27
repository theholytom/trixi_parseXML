package cz.trixi.parsexml.job;

import cz.trixi.parsexml.persistence.entity.Municipality;
import cz.trixi.parsexml.persistence.entity.ParsingRun;
import cz.trixi.parsexml.persistence.repository.MunicipalityRepository;
import org.springframework.stereotype.Service;

@Service
public class MunicipalityService {

    private final MunicipalityRepository repository;

    public MunicipalityService(MunicipalityRepository repository) {
        this.repository = repository;
    }

    public Municipality findById(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
