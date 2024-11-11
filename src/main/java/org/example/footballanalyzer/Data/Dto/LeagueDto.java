package org.example.footballanalyzer.Data.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeagueDto {
    private Long leagueId;
    private String name;
    private String logo;
}
