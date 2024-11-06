package org.example.footballanalyzer.API;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/coach")
public interface CoachApi {
    @GetMapping("/stats/team")
    @PreAuthorize("hasRole('COACH')")
    ResponseEntity<?> getStatsTeamCoach(@RequestParam String teamName,
                                               @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                               @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                               @RequestParam(value = "rounding", required = false, defaultValue = "week") String rounding);

    @GetMapping("/stats/players")
    @PreAuthorize("hasRole('COACH')")
    ResponseEntity<?> getStatsPlayers(@RequestParam String teamName);

    @GetMapping("/futureMatches")
    ResponseEntity<?> futureMatches(@RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(value = "page") int page, @RequestParam(required = false) Long leagueId);

    @GetMapping("/all-teams")
    ResponseEntity<?> getAllTeams();

    @GetMapping("/all-leagues")
    ResponseEntity<?> getAllLeagues();

    @GetMapping("/roles")
    ResponseEntity<?> getRoles();
}
