package org.example.footballanalyzer.Controller;

import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.API.FootballApi;
import org.example.footballanalyzer.Service.FootballService;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FootballController implements FootballApi {

    private final FootballService footballService;

    @Override
    public ResponseEntity<?> saveAllByLeagueSeason(Long league, int season) throws IOException, InterruptedException, JSONException {
        footballService.saveAllByLeagueSeason(league, season);
        return ResponseEntity.ok().build();
    }
}
