package org.example.footballanalyzer.API;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/coach")
public interface CoachApi {
    @GetMapping("/stats/team")
    ResponseEntity<?> getStatsTeamCoach(@RequestParam String teamName,
                                               @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                               @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                               @RequestParam(value = "rounding", required = false, defaultValue = "week") String rounding);

    @GetMapping("/stats/players")
    ResponseEntity<?> getStatsPlayers(@RequestParam String teamName);

    @GetMapping("/futureMatches")
    ResponseEntity<?> futureMatches(@RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(value = "page") int page);

    @GetMapping("all-teams")
    ResponseEntity<?> getAllTeams();
}
