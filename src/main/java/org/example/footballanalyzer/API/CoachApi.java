package org.example.footballanalyzer.API;

import org.example.footballanalyzer.Data.DateReturn;
import org.example.footballanalyzer.Data.DateReturnRounding;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("/api/coach")
public interface CoachApi {
    @PostMapping("/stats/team")
    @PreAuthorize("hasAnyAuthority('TRENER', 'ANALITYK', 'ADMIN')")
    ResponseEntity<?> getStatsTeamCoach(@RequestBody DateReturnRounding date);

    @PostMapping("/stats/players")
    @PreAuthorize("hasAnyAuthority('TRENER', 'ANALITYK', 'ADMIN')")
    ResponseEntity<?> getStatsPlayers(@RequestBody DateReturn datesReturn);

    @GetMapping("/futureMatches")
    ResponseEntity<?> futureMatches(@RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(value = "page") int page, @RequestParam(required = false) Long leagueId);

    @GetMapping("/all-teams")
    ResponseEntity<?> getAllTeams();

    @GetMapping("/all-leagues")
    ResponseEntity<?> getAllLeagues();

    @GetMapping("/roles")
    ResponseEntity<?> getRoles();
}
