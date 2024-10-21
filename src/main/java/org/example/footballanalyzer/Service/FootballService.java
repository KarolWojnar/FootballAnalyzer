package org.example.footballanalyzer.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Config.ApiKeyManager;
import org.example.footballanalyzer.Data.Dto.GroupRecord;
import org.example.footballanalyzer.Data.Entity.*;
import org.example.footballanalyzer.Repository.*;
import org.example.footballanalyzer.Service.Util.DataUtil;
import org.example.footballanalyzer.Service.Util.FootballApiUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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
    private final RatingService ratingService;

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
            saveStatsFixtureByPlayer(fixture);
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

    private void saveStatsFixtureByPlayer(Fixture fixture) throws IOException, InterruptedException {
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

        teamStats.setTeam(players.get(0).getPlayer().getTeam());
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

        dataUtil.collectStatsAndSave(fixture, teamStats);
    }

    public ResponseEntity<?> getStatsTeamCoach(String teamName, LocalDate startDate, LocalDate endDate, String rounding) {
        Optional<Long> teamId = teamRepository.findByName(teamName).map(Team::getId);

        if (teamId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>(populateRatingsAndPlayers(teamName, startDate, endDate, rounding));
        return ResponseEntity.ok(response);
    }

    private Map<String,Object> populateRatingsAndPlayers(String teamName, LocalDate startDate, LocalDate endDate, String rounding) {
        Date dateStart = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endStart = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Fixture> teamStats = fixtureRepository.findAllByDateBetween(dateStart, endStart);
        List<FixtureStatsTeam> teamStatsList = fixtureStatsTeamRepository.findAllByFixtureInAndMinutesGreaterThan(teamStats, 0);
        List<GroupRecord> groupedStats = groupRatings(teamStatsList);
        List<GroupRecord> coachTeam = groupedStats.stream().filter(record -> record.team().equals(teamName)).toList();
        Map<String, Object> ratings = new HashMap<>();

        ratings.putAll(ratingService.getAvgOfList("allTeamsRating", groupedStats));
        ratings.putAll(ratingService.getAvgOfList("teamRating", coachTeam));

        ratings.putAll(ratingService.getAvgByDates("allTeamsForm", groupedStats, rounding, startDate, endDate));
        ratings.putAll(ratingService.getAvgByDates("teamForm", coachTeam, rounding, startDate, endDate));

        return ratings;
    }

    private List<GroupRecord> groupRatings(List<FixtureStatsTeam> teamStatsList) {
        Map<String, Double> maxValues = ratingService.initializeMaxValues();
        Map<String, Double> sumValues = ratingService.initializeSumValues();

        int fixturesCount = teamStatsList.size();
        
        for (FixtureStatsTeam fixture : teamStatsList) {
            ratingService.updateMaxValues(maxValues, fixture);
            ratingService.updateSumValues(sumValues, fixture);
        }

        ratingService.normalizeSums(sumValues, fixturesCount);
        double[] weights = ratingService.calculateWeights(sumValues.values().stream().mapToDouble(Double::doubleValue).toArray());

        return calculateStats(weights, teamStatsList, maxValues);
    }

    private List<GroupRecord> calculateStats(double[] weights, List<FixtureStatsTeam> teamStatsList, Map<String, Double> maxValues) {
        List<GroupRecord> records = new ArrayList<>();

        for (FixtureStatsTeam teamStats : teamStatsList) {
            GroupRecord record = new GroupRecord(
                    teamStats.getTeam().getName(),
                    teamStats.getFixture().getDate(),
                    ratingService.setAttacking(teamStats, maxValues, weights),
                    ratingService.setDefending(teamStats, maxValues, weights),
                    ratingService.setAgression(teamStats, maxValues, weights),
                    ratingService.setCreativity(teamStats, maxValues, weights)
            );
            records.add(record);
        }
        return records;
    }


    public ResponseEntity<?> getPlayerStatsByTeam(String teamName) {
        Optional<Team> optionalTeam = teamRepository.findByName(teamName);
        if (optionalTeam.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Team team = optionalTeam.get();
        return ResponseEntity.ok(dataUtil.findAllPlayersStatsByTeam(team));
    }

    public ResponseEntity<?> closestMatches(LocalDate startDate, int page) {
        Date dateStart = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return dataUtil.closestMatches(dateStart, page);
    }
}
