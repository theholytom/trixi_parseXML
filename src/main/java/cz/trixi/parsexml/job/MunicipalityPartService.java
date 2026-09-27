package cz.trixi.parsexml.job;

import cz.trixi.parsexml.persistence.entity.Municipality;
import cz.trixi.parsexml.persistence.entity.MunicipalityPart;
import cz.trixi.parsexml.persistence.repository.MunicipalityPartRepository;
import org.springframework.stereotype.Service;

@Service
public class MunicipalityPartService {

    private final MunicipalityPartRepository repository;

    public MunicipalityPartService(MunicipalityPartRepository repository) {
        this.repository = repository;
    }

    public MunicipalityPart findById(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
