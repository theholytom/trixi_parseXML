package cz.trixi.parsexml.api.dto;

import cz.trixi.parsexml.persistence.entity.enums.RunStatus;

import java.time.Instant;

public record RunTriggerResponse(Long id, RunStatus status, Instant startedAt) {
}
