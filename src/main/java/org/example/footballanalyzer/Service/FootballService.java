package org.example.footballanalyzer.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Config.ApiKeyManager;
import org.example.footballanalyzer.Data.Entity.*;
import org.example.footballanalyzer.Repository.*;
import org.example.footballanalyzer.Service.Util.DataUtil;
import org.example.footballanalyzer.Service.Util.FootballApiUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.util.List;
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
    private final FixturesStatsRepository fixturesStatsRepository;
    private final FixtureStatsTeamRepository fixtureStatsTeamRepository;

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
        }
        log.info("Saved players stats for fixture: {}", fixture.getFixtureId());
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

    public void collectFixtures() {
        List<Fixture> fixtures = fixtureRepository.findAllCompleted();
        fixtures.forEach(
                this::saveCollectedFixture
        );
    }

    @Transactional
    public void saveCollectedFixture(Fixture fixture) {
        List<FixturesStats> playersStatsFixture = fixturesStatsRepository.getFixturesStatsByFixture(fixture);
        List<FixturesStats> playersHome = playersStatsFixture.stream()
                .filter(stat -> stat.getPlayer().getTeam().getName().equals(fixture.getHomeTeam().getName()))
                .toList();

        List<FixturesStats> playersAway = playersStatsFixture.stream()
                .filter(stat -> stat.getPlayer().getTeam().getName().equals(fixture.getAwayTeam().getName()))
                .toList();

        collectStatsAndSave(playersHome, fixture);
        collectStatsAndSave(playersAway, fixture);
    }

    private void collectStatsAndSave(List<FixturesStats> players, Fixture fixture) {

        FixtureStatsTeam teamStats = new FixtureStatsTeam();

        int playersCount = players.size();

        teamStats.setFixture(fixture);
        teamStats.setAssists(players.stream().mapToDouble(FixturesStats::getAssists).sum() / playersCount);
        teamStats.setCardsRed(players.stream().mapToDouble(FixturesStats::getCardsRed).sum() / playersCount);
        teamStats.setCardsYellow(players.stream().mapToDouble(FixturesStats::getCardsYellow).sum() / playersCount);
        teamStats.setDribblesAttempts(players.stream().mapToDouble(FixturesStats::getDribblesAttempts).sum() / playersCount);
        teamStats.setDribblesSuccess(players.stream().mapToDouble(FixturesStats::getDribblesSuccess).sum() / playersCount);
        teamStats.setDuelsTotal(players.stream().mapToDouble(FixturesStats::getDuelsTotal).sum() / playersCount);
        teamStats.setDuelsWon(players.stream().mapToDouble(FixturesStats::getDuelsWon).sum() / playersCount);
        teamStats.setFoulsCommitted(players.stream().mapToDouble(FixturesStats::getFoulsCommitted).sum() / playersCount);
        teamStats.setFoulsDrawn(players.stream().mapToDouble(FixturesStats::getFoulsDrawn).sum() / playersCount);
        teamStats.setGoalsConceded(players.stream().mapToDouble(FixturesStats::getGoalsConceded).sum() / playersCount);
        teamStats.setGoalsTotal(players.stream().mapToDouble(FixturesStats::getGoalsTotal).sum() / playersCount);
        teamStats.setPassesAccuracy(players.stream().mapToDouble(FixturesStats::getPassesAccuracy).sum() / playersCount);
        teamStats.setPassesKey(players.stream().mapToDouble(FixturesStats::getPassesKey).sum() / playersCount);
        teamStats.setPassesTotal(players.stream().mapToDouble(FixturesStats::getPassesTotal).sum() / playersCount);
        teamStats.setShotsOnGoal(players.stream().mapToDouble(FixturesStats::getShotsOnGoal).sum() / playersCount);
        teamStats.setOffsides(players.stream().mapToDouble(FixturesStats::getOffsides).sum() / playersCount);
        teamStats.setTacklesBlocks(players.stream().mapToDouble(FixturesStats::getTacklesBlocks).sum() / playersCount);
        teamStats.setTacklesInterceptions(players.stream().mapToDouble(FixturesStats::getTacklesInterceptions).sum() / playersCount);
        teamStats.setTacklesTotal(players.stream().mapToDouble(FixturesStats::getTacklesTotal).sum() / playersCount);
        teamStats.setPenaltyCommitted(players.stream().mapToDouble(FixturesStats::getPenaltyCommitted).sum() / playersCount);
        teamStats.setPenaltyMissed(players.stream().mapToDouble(FixturesStats::getPenaltyMissed).sum() / playersCount);
        teamStats.setPenaltySaved(players.stream().mapToDouble(FixturesStats::getPenaltySaved).sum() / playersCount);
        teamStats.setPenaltyScored(players.stream().mapToDouble(FixturesStats::getPenaltyScored).sum() / playersCount);
        teamStats.setPenaltyWon(players.stream().mapToDouble(FixturesStats::getPenaltyWon).sum() / playersCount);
        teamStats.setSaves(players.stream().mapToDouble(FixturesStats::getSaves).sum() / playersCount);
        teamStats.setShotsTotal(players.stream().mapToDouble(FixturesStats::getShotsTotal).sum() / playersCount);
        teamStats.setMinutes(players.stream().mapToDouble(FixturesStats::getMinutes).sum() / playersCount);

        fixtureStatsTeamRepository.save(teamStats);
    }
}
