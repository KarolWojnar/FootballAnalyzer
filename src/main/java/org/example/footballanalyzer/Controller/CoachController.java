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
    public ResponseEntity<?> getStatsTeamOpponent(DateReturnRounding date) {
        try {
            return footballService.getStatsOpponentCoach(date.getStartDate(), date.getEndDate(), date.getRounding());
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
    public ResponseEntity<?> getUser() {
        try {
            return userService.getUser();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(new AuthResponse(Code.ERROR));
        }
    }

    @Override
    public ResponseEntity<?> getStaff() {
        try {
            return userService.getStaff();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(new AuthResponse(Code.S1));
        }
    }

    @Override
    public ResponseEntity<?> changeStatus(Long id, String status) {
        try {
            userService.setAsResolved(id, status);
            return ResponseEntity.status(200).body(new AuthResponse(Code.SUCCESS));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(new AuthResponse(Code.ERROR));
        }
    }

    @Override
    public ResponseEntity<?> getRequests() {
        try {
            return userService.getRequests();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(new AuthResponse(Code.R3));
        }
    }

    @Override
    public ResponseEntity<?> setAsCoach(Long id) {
        return userService.setAsCoach(id);
    }

    @Override
    public ResponseEntity<?> removeFromTeam(Long id) {
        try {
            return userService.removeFromTeam(id);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(new AuthResponse(Code.S1));
        }
    }

    @Override
    public ResponseEntity<?> getAllLeagues() {
        return footballService.getAllLeagues();
    }

    @Override
    public ResponseEntity<?> getRoles() {
        return userService.getRoles(false);
    }
}
