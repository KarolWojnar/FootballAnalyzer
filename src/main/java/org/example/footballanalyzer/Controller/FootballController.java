package org.example.footballanalyzer.Controller;

import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.API.FootballApi;
import org.example.footballanalyzer.Service.FootballService;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class FootballController implements FootballApi {

    private final FootballService footballService;

    @Override
    public ResponseEntity<?> saveAllByLeagueSeason(Long league, Long season) throws IOException, InterruptedException, JSONException, ParseException {
        return footballService.saveAllByLeagueSeason(league, season);
    }

    @Override
    public ResponseEntity<?> collectFixtures() {
        return footballService.collectFixtures();
    }
}
