package cz.trixi.parsexml.persistence.repository;

import cz.trixi.parsexml.persistence.entity.ParsingRun;
import cz.trixi.parsexml.persistence.entity.enums.RunStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParsingRunRepository extends JpaRepository<ParsingRun, Long> {

    @Query("""
			SELECT r FROM ParsingRun r
			WHERE (:status IS NULL OR r.status = :status)
			ORDER BY r.startedAt DESC
			""")
    Page<ParsingRun> search(@Param("status") RunStatus status, Pageable pageable);
}
