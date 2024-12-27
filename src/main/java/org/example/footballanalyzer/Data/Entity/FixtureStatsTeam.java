package org.example.footballanalyzer.Data.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class FixtureStatsTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fixture_id")
    private Fixture fixture;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private double minutes;
    private double rating;
    private double offsides;
    private double shotsTotal;
    private double shotsOnGoal;
    private double goalsTotal;
    private double goalsConceded;
    private double assists;
    private double saves;
    private double passesTotal;
    private double passesKey;
    private double passesAccuracy;
    private double tacklesTotal;
    private double tacklesBlocks;
    private double tacklesInterceptions;
    private double duelsTotal;
    private double duelsWon;
    private double dribblesAttempts;
    private double dribblesSuccess;
    private double foulsDrawn;
    private double foulsCommitted;
    private double cardsYellow;
    private double cardsRed;

}
