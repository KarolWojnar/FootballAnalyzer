package org.example.footballanalyzer.Data.Dto;

import lombok.Data;

import java.util.Date;

@Data
public class FixturesDto {
    private Date date;
    private String homeTeam;
    private String awayTeam;
    private String logoHome;
    private String logoAway;
    private Long leagueId;
}
