package org.example.footballanalyzer.Controller;

import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.API.CoachApi;
import org.example.footballanalyzer.Service.FootballService;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CoachController implements CoachApi {

    private final FootballService footballService;

    @Override
    public ResponseEntity<?> getStatsTeamCoach(String teamName, LocalDate startDate, LocalDate endDate, String rounding) {
        return footballService.getStatsTeamCoach(teamName, startDate, endDate, rounding);
    }
}
