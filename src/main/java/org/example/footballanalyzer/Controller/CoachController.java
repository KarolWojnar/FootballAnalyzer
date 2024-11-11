package org.example.footballanalyzer.Controller;

import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.API.CoachApi;
import org.example.footballanalyzer.Data.Code;
import org.example.footballanalyzer.Data.DateReturn;
import org.example.footballanalyzer.Data.DateReturnRounding;
import org.example.footballanalyzer.Data.Entity.AuthResponse;
import org.example.footballanalyzer.Service.FootballService;
import org.example.footballanalyzer.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class CoachController implements CoachApi {

    private final FootballService footballService;
    private final UserService userService;

    @Override
    public ResponseEntity<?> getStatsTeamCoach(DateReturnRounding date) {
        try {
            return footballService.getStatsTeamCoach(date.getStartDate(), date.getEndDate(), date.getRounding());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(401).body(Code.T1);
        }
    }

    @Override
    public ResponseEntity<?> getStatsPlayers(DateReturn dataReturn) {
        try {
            return footballService.getPlayerStatsByTeam(dataReturn.getStartDate(), dataReturn.getEndDate());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(401).body(new AuthResponse(Code.T1));
        }
    }

    @Override
    public ResponseEntity<?> futureMatches(LocalDate date, int page, @Nullable Long leagueId) {
        return footballService.closestMatches(date, page, leagueId);
    }

    @Override
    public ResponseEntity<?> getAllTeams() {
        return footballService.getAllTeams();
    }

    @Override
    public ResponseEntity<?> getAllLeagues() {
        return footballService.getAllLeagues();
    }

    @Override
    public ResponseEntity<?> getRoles() {
        return userService.getRoles();
    }
}
