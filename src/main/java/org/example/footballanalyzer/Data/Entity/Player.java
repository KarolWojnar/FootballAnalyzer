package org.example.footballanalyzer.Data.Entity;

import lombok.Data;
import org.apache.commons.lang3.builder.HashCodeExclude;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long playerId;
    private String name;
    private String photo;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "player")
    @HashCodeExclude
    private Set<FixturesStats> fixtureStats;
}
