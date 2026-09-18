package cz.trixi.parsexml.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Table(name = "municipality_part")
@Entity
@Getter
@NoArgsConstructor
public class MunicipalityPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "municipality_part_id")
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;


    @ManyToOne
    @JoinColumn(name = "municipality_code", referencedColumnName = "municipality_id")
    private Municipality municipality;
}
