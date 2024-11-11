package org.example.footballanalyzer.Data.Entity;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsExclude;

import javax.persistence.*;
import java.util.HashSet;
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
    @EqualsExclude
    private Set<Player> players = new HashSet<>();

    @OneToMany(mappedBy = "homeTeam")
    private Set<Fixture> homeFixtures = new HashSet<>();

    @OneToMany(mappedBy = "awayTeam")
    private Set<Fixture> awayFixtures = new HashSet<>();

    @OneToMany(mappedBy = "team")
    private Set<FixtureStatsTeam> team = new HashSet<>();

    @OneToMany(mappedBy = "team")
    private Set<UserEntity> user = new HashSet<>();

    @ManyToMany(mappedBy = "teams")
    private Set<League> leagues = new HashSet<>();

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return teamId.equals(team.id);
    }

}
