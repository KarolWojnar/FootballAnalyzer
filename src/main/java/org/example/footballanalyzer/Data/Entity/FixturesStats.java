package org.example.footballanalyzer.Data.Entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class FixturesStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "fixture_id")
    private Fixture fixture;

    private int minutes;
    private String position;
    private double rating;
    private int offsides;
    private int shotsTotal;
    private int shotsOnGoal;
    private int goalsTotal;
    private int goalsConceded;
    private int assists;
    private int saves;
    private int passesTotal;
    private int passesKey;
    private double passesAccuracy;
    private int tacklesTotal;
    private int tacklesBlocks;
    private int tacklesInterceptions;
    private int duelsTotal;
    private int duelsWon;
    private int dribblesAttempts;
    private int dribblesSuccess;
    private int foulsDrawn;
    private int foulsCommitted;
    private int cardsYellow;
    private int cardsRed;
    private int penaltyWon;
    private int penaltyCommitted;
    private int penaltyScored;
    private int penaltySaved;
    private int penaltyMissed;
}
