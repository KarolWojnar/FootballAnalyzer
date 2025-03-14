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

    @PostMapping("/stats/opponent")
    @PreAuthorize("hasAnyAuthority('TRENER', 'ADMIN')")
    ResponseEntity<?> getStatsTeamOpponent(@RequestBody DateReturnRounding date);

    @PostMapping("/stats/{playerId}")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<?> getStatsPlayer(@RequestBody DateReturn date, @PathVariable Long playerId);

    @PostMapping("/stats/players")
    @PreAuthorize("hasAnyAuthority('TRENER', 'ANALITYK', 'ADMIN')")
    ResponseEntity<?> getStatsPlayers(@RequestBody DateReturn datesReturn);

    @GetMapping("/futureMatches")
    ResponseEntity<?> futureMatches(@RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                    @RequestParam(value = "page") int page, @RequestParam(required = false) Long leagueId,
                                    @RequestParam(required = false) String teamName);

    @GetMapping("/all-teams")
    ResponseEntity<?> getAllTeams();

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<?> getUser();

    @GetMapping("/staff")
    @PreAuthorize("hasAnyAuthority('TRENER', 'ADMIN')")
    ResponseEntity<?> getStaff();

    @GetMapping("/requests")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<?> getRequests();

    @PatchMapping("/requests/{id}")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<?> changeStatus(@PathVariable Long id, @RequestBody String status);

    @PatchMapping("/staff/{id}")
    @PreAuthorize("hasAnyAuthority('TRENER', 'ADMIN')")
    ResponseEntity<?> setAsCoach(@PathVariable Long id);

    @DeleteMapping("/staff/{id}")
    @PreAuthorize("hasAnyAuthority('TRENER', 'ADMIN')")
    ResponseEntity<?> removeFromTeam(@PathVariable Long id);

    @GetMapping("/all-leagues")
    ResponseEntity<?> getAllLeagues();

    @GetMapping("/roles")
    ResponseEntity<?> getRoles();
}
