package org.example.footballanalyzer.Data.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Entity
public class Fixture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fixtureId;

    private Date date;
    private int season;

    private int homeGoals;
    private int awayGoals;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @OneToMany(mappedBy = "fixture", cascade = CascadeType.ALL)
    private Set<FixturesStats> fixtureStats;

    private boolean isCounted = false;
}
