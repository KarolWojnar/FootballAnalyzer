package org.example.footballanalyzer.Data.Dto;

import lombok.Data;

import java.util.Date;

@Data
public class PlayerStatsDto {
    private Date date;
    private String position;
    private String player;
    private int minutes;
    double rating;
    private int offsides;
    private int shotsTotal;
    private int shotsOnGoal;
    private int goalsTotal;
    private int goalsConceded;
    private int assists;
    private int saves;
    private int passesTotal;
    private int passesKey;
    double passesAccuracy;
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

    public PlayerStatsDto(Date date, String position, String player, int minutes,
                          double rating, int offsides, int shotsTotal, int shotsOnGoal,
                          int goalsTotal, int goalsConceded, int assists, int saves,
                          int passesTotal, int passesKey, double passesAccuracy,
                          int tacklesTotal, int tacklesBlocks, int tacklesInterceptions,
                          int duelsTotal, int duelsWon, int dribblesAttempts,
                          int dribblesSuccess, int foulsDrawn, int foulsCommitted, int cardsYellow,
                          int cardsRed, int penaltyWon, int penaltyCommitted, int penaltyScored,
                          int penaltySaved, int penaltyMissed
                          ) {
        this.date = date;
        this.position = position;
        this.penaltyMissed = penaltyMissed;
        this.penaltySaved = penaltySaved;
        this.penaltyScored = penaltyScored;
        this.penaltyCommitted = penaltyCommitted;
        this.penaltyWon = penaltyWon;
        this.cardsRed = cardsRed;
        this.cardsYellow = cardsYellow;
        this.foulsCommitted = foulsCommitted;
        this.foulsDrawn = foulsDrawn;
        this.dribblesSuccess = dribblesSuccess;
        this.dribblesAttempts = dribblesAttempts;
        this.duelsWon = duelsWon;
        this.duelsTotal = duelsTotal;
        this.tacklesInterceptions = tacklesInterceptions;
        this.tacklesBlocks = tacklesBlocks;
        this.tacklesTotal = tacklesTotal;
        this.passesAccuracy = passesAccuracy;
        this.passesKey = passesKey;
        this.passesTotal = passesTotal;
        this.saves = saves;
        this.assists = assists;
        this.goalsConceded = goalsConceded;
        this.goalsTotal = goalsTotal;
        this.shotsOnGoal = shotsOnGoal;
        this.shotsTotal = shotsTotal;
        this.offsides = offsides;
        this.rating = rating;
        this.minutes = minutes;
        this.player = player;
    }

    private int penaltyScored;
    private int penaltySaved;
    private int penaltyMissed;
}
