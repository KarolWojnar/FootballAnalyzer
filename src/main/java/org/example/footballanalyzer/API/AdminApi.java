package org.example.footballanalyzer.API;


import jdk.jfr.Description;
import org.example.footballanalyzer.Data.Dto.UserEntityEditData;
import org.example.footballanalyzer.Data.Entity.UserRequest;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@Description("Api for admin.")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*")
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
public interface AdminApi {
    @PostMapping("/fixtures/save-all-by-league-season")
    ResponseEntity<?> saveAllByLeagueSeason(@RequestParam Long league, @RequestParam Long season) throws IOException, InterruptedException, JSONException, ParseException;

    @GetMapping("/fixtures/collect")
    ResponseEntity<?> collectFixtures();

    @PostMapping("/leaguesFromCountry/{country}")
    ResponseEntity<?> findPossibleLeagues(@PathVariable String country);

    @PostMapping("/clubsFromLeague/{leagueId}")
    ResponseEntity<?> findPossibleClubs(@PathVariable Long leagueId);

    @GetMapping("/users")
    ResponseEntity<?> getAllUsers();

    @PatchMapping("/users/{id}")
    ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserEntityEditData user);

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

    @PostMapping("/requests/{id}/status")
    ResponseEntity<?> approveRequest(@PathVariable Long id, @RequestBody UserRequest.RequestStatus status);
}
