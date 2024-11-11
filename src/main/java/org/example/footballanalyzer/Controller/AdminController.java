package org.example.footballanalyzer.Controller;

import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.API.AdminApi;
import org.example.footballanalyzer.Data.Code;
import org.example.footballanalyzer.Data.Dto.UserEntityEditData;
import org.example.footballanalyzer.Data.Entity.AuthResponse;
import org.example.footballanalyzer.Data.Entity.UserRequest;
import org.example.footballanalyzer.Service.FootballService;
import org.example.footballanalyzer.Service.UserService;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {

    private final FootballService footballService;
    private final UserService userService;

    @Override
    public ResponseEntity<?> saveAllByLeagueSeason(Long league, Long season) throws IOException, InterruptedException, JSONException, ParseException {
        return footballService.saveAllByLeagueSeason(league, season);
    }

    @Override
    public ResponseEntity<?> collectFixtures() {
        return footballService.collectFixtures();
    }

    @Override
    public ResponseEntity<?> findPossibleLeagues(String country) {
        try {
            return footballService.findPossibleLeagues(country);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(new AuthResponse(Code.C1));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<?> findPossibleClubs(Long leagueId) {
        try {
            return footballService.findPossibleClubs(leagueId);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(new AuthResponse(Code.C2));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<?> updateUser(Long id, UserEntityEditData user) {
        try {
            return ResponseEntity.status(200).body(userService.updateUser(id, user));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(new AuthResponse(Code.A2));
        } catch (FileAlreadyExistsException e) {
            return ResponseEntity.status(400).body(new AuthResponse(Code.A4));
        }
    }

    @Override
    public ResponseEntity<?> deleteUser(Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(200).body(new AuthResponse(Code.SUCCESS));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(new AuthResponse(Code.ERROR));
        }
    }

    @Override
    public ResponseEntity<?> updateUserRole(Long id, String role) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateUserTeam(Long id, Long teamId) {
        try {
            return ResponseEntity.status(200).body(userService.updateUserTeam(id, teamId));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(new AuthResponse(Code.A2));
        } catch (FileAlreadyExistsException e) {
            return ResponseEntity.status(400).body(new AuthResponse(Code.T1));
        } catch (ExceptionInInitializerError e) {
            return ResponseEntity.status(400).body(new AuthResponse(Code.T3));
        }
    }

    @Override
    public ResponseEntity<?> unlockUser(Long id, boolean setLocked) {
        try {
            userService.unlockUser(id, setLocked);
            return ResponseEntity.status(200).body(new AuthResponse(Code.SUCCESS));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(new AuthResponse(Code.A2));
        }
    }

    @Override
    public ResponseEntity<?> getAllRequests() {
        try {
            return ResponseEntity.status(200).body(userService.getAllRequests());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(new AuthResponse(Code.R3));
        }
    }

    @Override
    public ResponseEntity<?> approveRequest(Long id, UserRequest.RequestStatus status) {
        return null;
    }
}
