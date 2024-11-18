package org.example.footballanalyzer.Data.Dto;

import java.util.Date;

public record GroupRecord (
        long playerId,
        String team,
        Date date,
        double attacking,
        double defending,
        double aggression,
        double creativity
){ }
