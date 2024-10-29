package org.example.footballanalyzer.Controller;

import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.API.CoachApi;
import org.example.footballanalyzer.Service.FootballService;
import org.example.footballanalyzer.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class CoachController implements CoachApi {

    private final FootballService footballService;
    private final UserService userService;

    @Override
    public ResponseEntity<?> getStatsTeamCoach(String teamName, LocalDate startDate, LocalDate endDate, String rounding) {
        return footballService.getStatsTeamCoach(teamName, startDate, endDate, rounding);
    }

    @Override
    public ResponseEntity<?> getStatsPlayers(String teamName) {
        return footballService.getPlayerStatsByTeam(teamName);
    }

    @Override
    public ResponseEntity<?> futureMatches(LocalDate date, int page) {
        return footballService.closestMatches(date, page);
    }

    @Override
    public ResponseEntity<?> getAllTeams() {
        return footballService.getAllTeams();
    }

    @Override
    public ResponseEntity<?> getRoles() {
        return userService.getRoles();
    }
}
