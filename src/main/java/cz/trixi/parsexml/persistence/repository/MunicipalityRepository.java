package cz.trixi.parsexml.persistence.repository;

import cz.trixi.parsexml.persistence.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {

}
