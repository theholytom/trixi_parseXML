package cz.trixi.parsexml.job;

import cz.trixi.parsexml.api.dto.RunResponse;
import cz.trixi.parsexml.persistence.entity.ParsingRun;
import cz.trixi.parsexml.persistence.entity.enums.RunStatus;
import cz.trixi.parsexml.persistence.repository.ParsingRunRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ParsingRunService {

    private final JobExecutor executor;
    private final ParsingRunRepository repository;


    public ParsingRunService(JobExecutor executor, ParsingRunRepository repository) {
        this.executor = executor;
        this.repository = repository;
    }

    public ParsingRun triggerRun() {
        ParsingRun run = repository.save(ParsingRun.starting(Instant.now()));
        executor.execute(run.getId());
        return repository.findById(run.getId()).orElseThrow();
    }

    public ParsingRun findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Page<RunResponse> searchRuns(RunStatus status, Pageable pageable) {
        return repository.search(status, pageable).map(RunResponse::from);
    }
}
