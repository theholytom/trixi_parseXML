package cz.trixi.parsexml.persistence.repository;

import cz.trixi.parsexml.persistence.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {
}
