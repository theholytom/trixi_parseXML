package cz.trixi.parsexml.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Table(name = "municipality")
@Entity
@Getter
@NoArgsConstructor
public class Municipality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "municipality_id")
    private Long id;

    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "municipality", cascade = CascadeType.ALL)
    private List<MunicipalityPart> parts;

}
