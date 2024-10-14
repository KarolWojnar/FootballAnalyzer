package org.example.footballanalyzer.Data.Dto;

import java.util.Map;

public record RatingRecord(
        String team,
        Map<String, Double> rating
) {
}
