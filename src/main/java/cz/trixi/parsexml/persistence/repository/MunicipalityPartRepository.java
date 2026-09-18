package cz.trixi.parsexml.persistence.repository;

import cz.trixi.parsexml.persistence.entity.MunicipalityPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MunicipalityPartRepository extends JpaRepository<MunicipalityPart, Long> {
}
