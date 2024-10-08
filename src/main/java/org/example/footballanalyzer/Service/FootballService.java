package org.example.footballanalyzer.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Config.ApiKeyManager;
import org.example.footballanalyzer.Data.Entity.Fixture;
import org.example.footballanalyzer.Data.Entity.League;
import org.example.footballanalyzer.Data.Entity.Player;
import org.example.footballanalyzer.Data.Entity.Team;
import org.example.footballanalyzer.Repository.FixtureRepository;
import org.example.footballanalyzer.Repository.LeagueRepository;
import org.example.footballanalyzer.Repository.PlayerRepository;
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
    private final PlayerRepository playerRepository;

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

    private void saveFixtures(JSONArray fixtures) throws JSONException, ParseException, IOException, InterruptedException {
        League league = getLeague(fixtures.getJSONObject(0).getJSONObject("league"));
        for (int i = 0; i < fixtures.length(); i++) {
            JSONObject jsonFixture = fixtures.getJSONObject(i);
            JSONObject teams = jsonFixture.getJSONObject("teams");
            Team homeTeam = getTeam(teams.getJSONObject("home"), league);
            Team awayTeam = getTeam(teams.getJSONObject("away"), league);
            Fixture fixture = getFixture(jsonFixture, homeTeam, awayTeam);

            if (fixture.getAwayGoals() == -1 || fixture.getHomeGoals() == -1) {
                continue;
            }

            saveStatsFixureByPlayer(fixture);
            log.info("Saved new fixture: {}", fixture.getFixtureId());
        }

    }

    private Fixture getFixture(JSONObject fixture, Team homeTeam, Team awayTeam) throws JSONException, ParseException {
        Optional<Fixture> optionalFixture = fixtureRepository
                .findByFixtureId(fixture.getJSONObject("fixture").getLong("id"));

        if (optionalFixture.isPresent()) {
            Fixture existingFixture = optionalFixture.get();
            if (existingFixture.getAwayGoals() != -1 && existingFixture.getHomeGoals() != -1) {
                return existingFixture;
            }
            fixtureRepository.delete(existingFixture);
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

    private void saveStatsFixureByPlayer(Fixture fixture) throws IOException, InterruptedException {
        int attempts = 0;

        while(attempts < apiKeyManager.getApiKeysLength()) {
            HttpResponse<String> responsePlayerStats = footballApiUtil.getPlayersStatsByFixture(fixture.getFixtureId(), apiKeyManager.getApiKey());

            if (responsePlayerStats.statusCode() == 429) {
                apiKeyManager.switchToNextApiKey();
                attempts++;
            } else if (responsePlayerStats.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(responsePlayerStats.body());

                if (jsonResponse.has("response")) {
                    JSONArray playerStats = jsonResponse.getJSONArray("response");
                    saveStats(playerStats, fixture);
                    return;
                } else {
                    throw new IOException("Fixture not found");
                }
            } else {
                throw new IOException("Unexpected response status: " + responsePlayerStats.statusCode());
            }
        }

        throw new IOException("Failed to retrieve data from API after trying all API keys.");
    }

    private void saveStats(JSONArray playerStats, Fixture fixture) {
        for (int i = 0; i < playerStats.length(); i++) {
            JSONObject playerStat = playerStats.getJSONObject(i);
            JSONObject jsonTeam = playerStat.getJSONObject("team");
            JSONArray players = playerStat.getJSONArray("players");
            for (int j = 0; j < players.length(); j++) {
                JSONObject jsonPlayer = players.getJSONObject(j);
                Player player = getPlayer(jsonPlayer.getJSONObject("player"), jsonTeam);
                savePlayerStats(player, fixture, jsonPlayer.getJSONArray("statistics"));
            }
            log.info("Saved players stats for fixture: {}", fixture.getFixtureId());
        }
    }

    private Player getPlayer(JSONObject jsonPlayer, JSONObject jsonTeam) {
        Optional<Player> optionalPlayer = playerRepository.findByPlayerId(jsonPlayer.getLong("id"));
        return optionalPlayer.orElseGet(() -> dataUtil.savePlayer(jsonPlayer, jsonTeam));
    }

    private void savePlayerStats(Player player, Fixture fixture, JSONArray statistics) {
        JSONObject jsonStatistics = statistics.getJSONObject(0);
        JSONObject games = jsonStatistics.getJSONObject("games");
        int offsides = jsonStatistics.optInt("offsides");
        JSONObject shots = jsonStatistics.getJSONObject("shots");
        JSONObject goals = jsonStatistics.getJSONObject("goals");
        JSONObject passes = jsonStatistics.getJSONObject("passes");
        JSONObject tackles = jsonStatistics.getJSONObject("tackles");
        JSONObject duels = jsonStatistics.getJSONObject("duels");
        JSONObject dribbles = jsonStatistics.getJSONObject("dribbles");
        JSONObject fouls = jsonStatistics.getJSONObject("fouls");
        JSONObject cards = jsonStatistics.getJSONObject("cards");
        JSONObject penalty = jsonStatistics.getJSONObject("penalty");
        dataUtil.savePlayerStats(player, fixture, offsides, games, shots, goals, passes, tackles, duels, dribbles, fouls, cards, penalty);
    }
}
