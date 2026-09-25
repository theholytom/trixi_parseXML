package cz.trixi.parsexml.persistence.entity;

import cz.trixi.parsexml.persistence.entity.enums.RunStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Table(name = "parsing_run")
@Entity
@Getter
@NoArgsConstructor
public class ParsingRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parsing_run_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RunStatus status;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    public static ParsingRun starting(Instant startedAt) {
        ParsingRun newRun = new ParsingRun();
        newRun.startedAt = startedAt;
        newRun.status = RunStatus.RUNNING;
        return newRun;
    }
}
