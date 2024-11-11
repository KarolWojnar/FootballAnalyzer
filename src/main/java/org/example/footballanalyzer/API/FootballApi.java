package org.example.footballanalyzer.API;

import jdk.jfr.Description;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.ParseException;

@Description("Football API to save new data")
@RequestMapping("/api/save")
public interface FootballApi {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/fixtures/save-all-by-league-season")
    ResponseEntity<?> saveAllByLeagueSeason(@RequestParam Long league, @RequestParam Long season) throws IOException, InterruptedException, JSONException, ParseException;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/fixtures/collect")
    ResponseEntity<?> collectFixtures();



}