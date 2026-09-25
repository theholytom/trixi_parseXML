package cz.trixi.parsexml.api;

import cz.trixi.parsexml.api.dto.PageResponse;
import cz.trixi.parsexml.api.dto.RunResponse;
import cz.trixi.parsexml.api.dto.RunTriggerResponse;
import cz.trixi.parsexml.job.ParsingRunService;
import cz.trixi.parsexml.persistence.entity.ParsingRun;
import cz.trixi.parsexml.persistence.entity.enums.RunStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/tasks")
public class ParsingTaskController {

    private final ParsingRunService service;


    public ParsingTaskController(ParsingRunService service) {
        this.service = service;
    }

    @PostMapping("/run")
    public ResponseEntity<RunTriggerResponse> triggerRun() {
        ParsingRun run = service.triggerRun();
        RunTriggerResponse body = new RunTriggerResponse(run.getId(), run.getStatus(), run.getStartedAt());
        return ResponseEntity.accepted()
                .location(URI.create("/api/v1/task/runs/" + run.getId()))
                .body(body);
    }

    @GetMapping("/runs/{id}")
    public RunResponse getRunById(@PathVariable Long id) {
        return RunResponse.from(service.findById(id));
    }
    
    @GetMapping("/runs")
    public PageResponse<RunResponse> listRuns(
            @RequestParam(required = false) RunStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        return PageResponse.from(service.searchRuns(status, pageable));
    }
}
