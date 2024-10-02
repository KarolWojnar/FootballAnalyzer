package org.example.footballanalyzer.Data.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long leagueId;
    private String name;
    private String country;
    private String logo;

    @OneToMany
    (mappedBy = "league")
    private Set<Team> teams;
}
