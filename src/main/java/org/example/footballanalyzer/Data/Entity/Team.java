package org.example.footballanalyzer.Data.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long teamId;
    private String name;
    private String logo;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private Set<Player> players;

    @OneToMany(mappedBy = "homeTeam")
    private Set<Fixture> homeFixtures;

    @OneToMany(mappedBy = "awayTeam")
    private Set<Fixture> awayFixtures;

    @OneToOne
    (mappedBy = "team")
    private User user;

    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;
}
