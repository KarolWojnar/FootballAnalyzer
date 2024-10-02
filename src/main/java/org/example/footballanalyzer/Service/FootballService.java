package org.example.footballanalyzer.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Config.ApiKeyManager;
import org.example.footballanalyzer.Data.Entity.Fixture;
import org.example.footballanalyzer.Data.Entity.League;
import org.example.footballanalyzer.Data.Entity.Team;
import org.example.footballanalyzer.Repository.FixtureRepository;
import org.example.footballanalyzer.Repository.LeagueRepository;
import org.example.footballanalyzer.Repository.TeamRepository;
import org.example.footballanalyzer.Service.Util.DataUtil;
import org.example.footballanalyzer.Service.Util.FootballApiUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FootballService {

    private final ApiKeyManager apiKeyManager;
    private final TeamRepository teamRepository;
    private final FootballApiUtil footballApiUtil;
    private final LeagueRepository leagueRepository;
    private final DataUtil dataUtil;
    private final FixtureRepository fixtureRepository;

    public void saveAllByLeagueSeason(Long league, Long season) throws IOException, InterruptedException, JSONException, ParseException {
        int attempts = 0;

        while(attempts < apiKeyManager.getApiKeysLength()) {
            HttpResponse<String> responseFixturesByLeagueAndSeason = footballApiUtil.getFixturesByLeagueAndSeason(league, season, apiKeyManager.getApiKey());

            if (responseFixturesByLeagueAndSeason.statusCode() == 429) {
                apiKeyManager.switchToNextApiKey();
                attempts++;
            } else if (responseFixturesByLeagueAndSeason.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(responseFixturesByLeagueAndSeason.body());

                if (jsonResponse.has("response")) {
                    JSONArray fixtures = jsonResponse.getJSONArray("response");
                    saveFixtures(fixtures);
                    return;
                } else {
                    throw new IOException("Fixture not found");
                }
            } else {
                throw new IOException("Unexpected response status: " + responseFixturesByLeagueAndSeason.statusCode());
            }
        }

        throw new IOException("Failed to retrieve data from API after trying all API keys.");
    }

    private void saveFixtures(JSONArray fixtures) throws JSONException, ParseException {
        League league = getLeague(fixtures.getJSONObject(0).getJSONObject("league"));
        for (int i = 0; i < fixtures.length(); i++) {
            JSONObject jsonFixture = fixtures.getJSONObject(i);
            JSONObject teams = jsonFixture.getJSONObject("teams");
            Team homeTeam = getTeam(teams.getJSONObject("home"), league);
            Team awayTeam = getTeam(teams.getJSONObject("away"), league);
            Fixture fixture = getFixture(jsonFixture, homeTeam, awayTeam);
            log.info("Saved new fixture: {}", fixture.getFixtureId());

            //todo: add player stats on fixture
        }

    }

    private Fixture getFixture(JSONObject fixture, Team homeTeam, Team awayTeam) throws JSONException, ParseException {
        Optional<Fixture> optionalFixture = fixtureRepository
                .findByFixtureId(fixture.getJSONObject("fixture").getLong("id"));

        if (optionalFixture.isPresent()) {
            return optionalFixture.get();
        }

        return dataUtil.saveFixture(fixture, homeTeam, awayTeam);
    }

    private Team getTeam(JSONObject team, League league) throws JSONException {
        Optional<Team> optionalTeam = teamRepository.findByName(team.getString("name"));
        return optionalTeam.orElseGet(() -> dataUtil.saveTeam(team, league));

    }

    private League getLeague(JSONObject league) throws JSONException {
        Optional<League> optionalLeague = leagueRepository.findByName(league.getString("name"));
        return optionalLeague.orElseGet(() -> dataUtil.saveLeague(league));
    }
}
