package org.example.footballanalyzer.API;


import jdk.jfr.Description;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@Description("Api for admin.")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*")
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/admin")
public interface AdminApi {

    @GetMapping("/fixtures/save-all-by-league-season/{league}/{season}")
    ResponseEntity<?> saveAllByLeagueSeason(@PathVariable Long league, @PathVariable Long season) throws IOException, InterruptedException, JSONException, ParseException;

    @GetMapping("/fixtures/collect")
    ResponseEntity<?> collectFixtures();

    @GetMapping("/leaguesFromCountry/{country}")
    ResponseEntity<?> findPossibleLeagues(@PathVariable String country);

    @GetMapping("/clubsFromLeague/{leagueId}")
    ResponseEntity<?> findPossibleClubs(@PathVariable Long leagueId);

    @GetMapping("/users")
    ResponseEntity<?> getAllUsers();

    @DeleteMapping("/users/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id);

    @PatchMapping("/users/{id}/role")
    ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestParam String role);

    @PatchMapping("/users/{id}/team/{teamId}")
    ResponseEntity<?> updateUserTeam(@PathVariable Long id, @PathVariable Long teamId);

    @PatchMapping("users/{id}/changeLock")
    ResponseEntity<?> unlockUser(@PathVariable Long id, @RequestParam boolean setLocked);

    @GetMapping("/requests")
    ResponseEntity<?> getAllRequests();

    @DeleteMapping("/requests/{id}")
    ResponseEntity<?> deleteRequest(@PathVariable Long id);
}
