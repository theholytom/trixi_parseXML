package cz.trixi.parsexml.api.dto;

import cz.trixi.parsexml.persistence.entity.ParsingRun;
import cz.trixi.parsexml.persistence.entity.enums.RunStatus;

import java.time.Instant;

public record RunResponse(
        Long id,
        RunStatus status,
        Instant startedAt,
        Instant finishedAt
) {

    public static RunResponse from(ParsingRun run) {
        return new RunResponse(run.getId(), run.getStatus(), run.getStartedAt(),
                run.getFinishedAt());
    }

}

