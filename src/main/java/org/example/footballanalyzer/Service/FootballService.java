package org.example.footballanalyzer.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Config.ApiKeyManager;
import org.example.footballanalyzer.Data.Code;
import org.example.footballanalyzer.Data.Dto.GroupRecord;
import org.example.footballanalyzer.Data.Dto.LeagueDto;
import org.example.footballanalyzer.Data.Dto.PlayerStatsDto;
import org.example.footballanalyzer.Data.Dto.TeamSelectDto;
import org.example.footballanalyzer.Data.Entity.*;
import org.example.footballanalyzer.Repository.*;
import org.example.footballanalyzer.Service.Util.DataUtil;
import org.example.footballanalyzer.Service.Util.FootballApiUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    private final HttpServletRequest request;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void scheduleStatsFromApi() throws IOException, InterruptedException, JSONException {

        List<Fixture> fixtures = fixtureRepository.findAllByDateBeforeAndIsCountedOrderByDate(new Date(), false);
        for (Fixture fixture : fixtures) {
            log.info("Fixture date: {}", fixture.getDate());
            log.info("Fixture counted: {}", fixture.isCounted());
            saveStatsFixtureByPlayer(fixture);
            dataUtil.setFixtureAsCounted(fixture.getId());
        }
        log.info("{} stats counted for {}", fixtures.size(), new Date());
    }

    @Scheduled(cron = "0 5 * * * *")
    public void scheduleCollectStats() {
        List<Fixture> fixtures = fixtureRepository.findAllCompleted();
        log.info("Collected fixtures: {}", fixtures.size());
        fixtures.forEach(
                this::saveCollectedFixture
        );
    }

    public ResponseEntity<?> collectFixtures() {
        List<Fixture> fixtures = fixtureRepository.findAllCompleted();
        fixtures.forEach(
                this::saveCollectedFixture
        );
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }

    public ResponseEntity<?> saveAllByLeagueSeason(Long league, Long season) throws IOException, InterruptedException, JSONException, ParseException {
        int attempts = 0;

        while (attempts < apiKeyManager.getApiKeysLength()) {
            HttpResponse<String> responseFixturesByLeagueAndSeason = footballApiUtil.getFixturesByLeagueAndSeason(league, season, apiKeyManager.getApiKey());

            if (responseFixturesByLeagueAndSeason.statusCode() == 429) {
                apiKeyManager.switchToNextApiKey();
                attempts++;
            } else if (responseFixturesByLeagueAndSeason.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(responseFixturesByLeagueAndSeason.body());
                if (jsonResponse.has("response")) {
                    JSONArray fixtures = jsonResponse.getJSONArray("response");
                    return saveFixtures(fixtures);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<Error>(HttpStatus.CONFLICT);
    }

    public ResponseEntity<?> saveFixtures(JSONArray fixtures) throws JSONException, ParseException, IOException, InterruptedException {
        if (fixtures.isEmpty()) {
            return ResponseEntity.status(404).body(new AuthResponse(Code.C2));
        }
        League league = getLeague(fixtures.getJSONObject(0).getJSONObject("league"));
        for (int i = 0; i < fixtures.length(); i++) {
            JSONObject jsonFixture = fixtures.getJSONObject(i);
            JSONObject teams = jsonFixture.getJSONObject("teams");
            Team homeTeam = getTeam(teams.getJSONObject("home"), league);
            Team awayTeam = getTeam(teams.getJSONObject("away"), league);
            Fixture fixture = getFixture(jsonFixture, homeTeam, awayTeam);
            if (fixture.getAwayGoals() != -1 && fixture.getHomeGoals() != -1 && !fixture.isCounted()) {
                saveStatsFixtureByPlayer(fixture);
                dataUtil.setFixtureAsCounted(fixture.getId());
            }
        }
        return ResponseEntity.status(200).body(new AuthResponse(Code.SUCCESS));
    }

    private Fixture getFixture(JSONObject fixture, Team homeTeam, Team awayTeam) throws JSONException, ParseException {
        Optional<Fixture> optionalFixture = fixtureRepository
                .findByFixtureId(fixture.getJSONObject("fixture").getLong("id"));

        if (optionalFixture.isPresent()) {
            Fixture existingFixture = optionalFixture.get();
            if (existingFixture.getAwayGoals() != -1) {
                return existingFixture;
            }
            fixtureRepository.delete(existingFixture);
        }

        return dataUtil.saveFixture(fixture, homeTeam, awayTeam);
    }

    public Team getTeam(JSONObject team, League league) throws JSONException {
        Team retrievedTeam;
        Optional<Team> optionalTeam = teamRepository.findByTeamId(team.getLong("id"));
        if (optionalTeam.isEmpty()) {
            retrievedTeam = dataUtil.saveTeam(team, league);
        } else {
            retrievedTeam = dataUtil.addTeamToLeague(optionalTeam.get(), league);
        }

        return retrievedTeam;
    }

    public League getLeague(JSONObject league) throws JSONException {
        Optional<League> optionalLeague = leagueRepository.findByName(league.getString("name"));
        return optionalLeague.orElseGet(() -> dataUtil.saveLeague(league));
    }

    private void saveStatsFixtureByPlayer(Fixture fixture) throws IOException, InterruptedException {
        int attempts = 0;

        while (attempts < apiKeyManager.getApiKeysLength()) {
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
            Team team = teamRepository.findByTeamId(jsonTeam.getLong("id")).orElse(null);
            JSONArray players = playerStat.getJSONArray("players");
            for (int j = 0; j < players.length(); j++) {
                JSONObject jsonPlayer = players.getJSONObject(j);
                Player player = getPlayer(jsonPlayer.getJSONObject("player"), jsonTeam);
                savePlayerStats(player, fixture, jsonPlayer.getJSONArray("statistics"), team);
            }
        }
        log.info("Saved players stats for fixture: {}", fixture.getFixtureId());
    }

    private Player getPlayer(JSONObject jsonPlayer, JSONObject jsonTeam) {
        Optional<Player> optionalPlayer = playerRepository.findByPlayerId(jsonPlayer.getLong("id"));
        return optionalPlayer.orElseGet(() -> dataUtil.savePlayer(jsonPlayer, jsonTeam));
    }

    private void savePlayerStats(Player player, Fixture fixture, JSONArray statistics, Team team) {
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
        dataUtil.savePlayerStats(player, fixture, team, offsides, games, shots, goals, passes, tackles, duels, dribbles, fouls, cards, penalty);
    }

    public void saveCollectedFixture(Fixture fixture) {
        List<FixturesStats> playersStatsFixture = fixturesStatsRepository.getFixturesStatsByFixture(fixture);
        if (playersStatsFixture.isEmpty()) {
            return;
        }
        List<FixturesStats> playersHome = playersStatsFixture.stream()
                .filter(stat -> stat.getTeam().getName().equals(fixture.getHomeTeam().getName()))
                .toList();

        List<FixturesStats> playersAway = playersStatsFixture.stream()
                .filter(stat -> stat.getTeam().getName().equals(fixture.getAwayTeam().getName()))
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

    public ResponseEntity<?> getStatsPlayer(LocalDate startDate, LocalDate endDate, Long playerId) {
        String userTeam = request.getUserPrincipal().getName();
        Team team = findTeam(userTeam);

        HashMap<String, Object> ratings = new HashMap<>(populatePlayerAndTeam(startDate, endDate, team, playerId));

        return ResponseEntity.ok(ratings);
    }

    private List<GroupRecord> groupRatingsPlayers(List<FixturesStats> playersStats, List<Fixture> teamStats) {
        Map<String, Double> maxValues = ratingService.initializeMaxValues();
        Map<String, Double> sumValues = ratingService.initializeSumValues();
        int fixturesCount = teamStats.size();

        for (FixturesStats player : playersStats) {
            ratingService.updateMaxValuesPlayers(maxValues, player);
            ratingService.updateSumValuesPlayers(sumValues, player);
        }

        ratingService.normalizeSums(sumValues, fixturesCount);

        double[] weights = ratingService.calculateWeights(sumValues.values().stream().mapToDouble(Double::doubleValue).toArray());

        return calculateStatsPlayers(weights, playersStats, maxValues);
    }

    private List<GroupRecord> calculateStatsPlayers(double[] weights, List<FixturesStats> playersStats, Map<String, Double> maxValues) {
        List<GroupRecord> records = new ArrayList<>();

        for (FixturesStats teamStats : playersStats) {
            GroupRecord record = new GroupRecord(
                    teamStats.getPlayer().getPlayerId(),
                    teamStats.getTeam().getName(),
                    teamStats.getFixture().getDate(),
                    ratingService.setAttackingPlayers(teamStats, maxValues, weights),
                    ratingService.setDefendingPlayers(teamStats, maxValues, weights),
                    ratingService.setAgressionPlayers(teamStats, maxValues, weights),
                    ratingService.setCreativityPlayers(teamStats, maxValues, weights)
            );
            records.add(record);
        }
        return records;
    }

    private List<GroupRecord> calculateStats(double[] weights, List<FixtureStatsTeam> teamStatsList, Map<String, Double> maxValues) {
        List<GroupRecord> records = new ArrayList<>();

        for (FixtureStatsTeam teamStats : teamStatsList) {
            GroupRecord record = new GroupRecord(
                    0,
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

    private Map<String, Object> populatePlayerAndTeam(LocalDate startDate, LocalDate endDate, Team team, long playerId) {
        Date dateStart = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endStart = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Fixture> teamStats = fixtureRepository.findAllByDateBetweenAndIsCollectedAndIsCounted(dateStart, endStart, true, true);

        List<FixturesStats> playersStats = fixturesStatsRepository.findAllByTeamAndFixtureIn(team, teamStats);

        List<GroupRecord> groupedStats = groupRatingsPlayers(playersStats, teamStats);

        List<GroupRecord> player = groupedStats.stream().filter(record -> record.playerId() == playerId).toList();

        Player player2 = playerRepository.findByPlayerId(playerId).orElse(null);

        Map<String, Object> ratings = new HashMap<>();

        ratings.putAll(ratingService.getAvgOfList("allTeamsRating", groupedStats, team.getName()));
        ratings.putAll(ratingService.getAvgOfList("teamRating", player, player2 != null ? player2.getName() : null));

        return ratings;

    }

    private Map<String, Object> populateRatingsAndPlayers(LocalDate startDate, LocalDate endDate, String rounding, boolean compareToAll, String... teamNames) {
        Date dateStart = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endStart = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Fixture> teamStats;
        if (compareToAll) {
            teamStats = fixtureRepository.findAllByDateBetweenAndIsCollectedAndIsCounted(dateStart, endStart, true, true);
        } else {
            Team teamLeague = teamRepository.findByName(teamNames[0]).orElse(null);
            if (teamLeague == null) {
                throw new IllegalArgumentException("Team not found");
            }
            League league = leagueRepository.findFirstByTeams_Id(teamLeague.getId()).orElse(null);

            teamStats = fixtureRepository.findAllByDateBetweenAndIsCollectedAndIsCountedAndLeague(dateStart, endStart, true, true, league);
        }
        List<FixtureStatsTeam> teamStatsList = fixtureStatsTeamRepository.findAllByFixtureInAndMinutesGreaterThan(teamStats, 0);
        List<GroupRecord> groupedStats = groupRatings(teamStatsList);

        boolean isOpponent = teamNames.length > 1;

        List<GroupRecord> coachTeam = groupedStats.stream().filter(record -> record.team().equals(teamNames[0])).toList();
        if (coachTeam.isEmpty()) {
            return new HashMap<>();
        }

        List<GroupRecord> opponentTeam = null;

        if (isOpponent) {
            opponentTeam = groupedStats.stream().filter(record -> record.team().equals(teamNames[1])).toList();
        }

        Map<String, Object> ratings = new HashMap<>();

        ratings.putAll(ratingService.getAvgOfList("allTeamsRating", isOpponent ? opponentTeam : groupedStats, isOpponent ? opponentTeam.get(0).team() : null));
        ratings.putAll(ratingService.getAvgOfList("teamRating", coachTeam, coachTeam.get(0).team()));

        ratings.putAll(ratingService.getAvgByDates("allTeamsForm", isOpponent ? opponentTeam : groupedStats, rounding, startDate, endDate, isOpponent ? opponentTeam.get(0).team() : null));
        ratings.putAll(ratingService.getAvgByDates("teamForm", coachTeam, rounding, startDate, endDate, coachTeam.get(0).team()));

        return ratings;
    }

    public ResponseEntity<?> getStatsTeamCoach(LocalDate startDate, LocalDate endDate, String rounding, boolean compareToAll) {
        String username = request.getUserPrincipal().getName();

        Team team = findTeam(username);

        HashMap<String, Object> ratings = new HashMap<>(populateRatingsAndPlayers(startDate, endDate, rounding, compareToAll, team.getName()));

        if (ratings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ratings);
    }

    public ResponseEntity<?> getStatsOpponentCoach(LocalDate startDate, LocalDate endDate, String rounding) {
        String username = request.getUserPrincipal().getName();

        Team team = findTeam(username);

        var today = new Date();

        Fixture nextFixture = fixtureRepository.findNextFixture(team.getId(), today).orElse(null);
        if (nextFixture == null) {
            return ResponseEntity.notFound().build();
        }

        Team opponent;
        if (nextFixture.getHomeTeam().getName().equals(team.getName())) {
            opponent = nextFixture.getAwayTeam();
        } else {
            opponent = nextFixture.getHomeTeam();
        }

        if (opponent == null) {
            return ResponseEntity.notFound().build();
        }

        String[] teams = {team.getName(), opponent.getName()};

        HashMap<String, Object> ratings = new HashMap<>(populateRatingsAndPlayers(startDate, endDate, rounding, true, teams));
        if (ratings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ratings);
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


    public ResponseEntity<?> getPlayerStatsByTeam(LocalDate startDate, LocalDate endDate) {
        String username = request.getUserPrincipal().getName();
        Team coachTeam = findTeam(username);
        List<PlayerStatsDto> playerList = dataUtil.findAllPlayersStatsByTeamAndDate(coachTeam, startDate, endDate);
        if (playerList.isEmpty()) {
            return ResponseEntity.badRequest().body(new AuthResponse(Code.T2));
        }
        return ResponseEntity.ok(playerList);
    }

    private Team findTeam(String username) throws UsernameNotFoundException {
        Optional<Team> team = userRepository.findByLogin(username).map(UserEntity::getTeam);
        if (team.isEmpty()) {
            throw new UsernameNotFoundException("User not found " + username);
        }
        return team.get();
    }

    public ResponseEntity<?> closestMatches(LocalDate startDate, int page, Long leagueId) {
        Date dateStart = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return dataUtil.closestMatches(dateStart, page, leagueId);
    }

    public ResponseEntity<?> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        if (teams.isEmpty()) return ResponseEntity.noContent().build();
        List<TeamSelectDto> teamSelectDtos = teams.stream()
                .map(team -> new TeamSelectDto(team.getId(), team.getName())).toList();
        return ResponseEntity.ok(teamSelectDtos);
    }

    public ResponseEntity<?> getAllLeagues() {
        List<LeagueDto> leagues = leagueRepository.findAll().stream().map(
                league -> LeagueDto.builder()
                        .leagueId(league.getId())
                        .logo(league.getLogo())
                        .name(league.getName())
                        .build()).toList();
        return ResponseEntity.ok(leagues);
    }

    public ResponseEntity<?> findPossibleLeagues(String country) throws IOException, InterruptedException {
        int attempts = 0;

        while (attempts < apiKeyManager.getApiKeysLength()) {
            HttpResponse<String> responseLeaguesByCountry = footballApiUtil.getLeaguesByCountry(country, apiKeyManager.getApiKey());
            if (responseLeaguesByCountry.statusCode() == 429) {
                apiKeyManager.switchToNextApiKey();
                attempts++;
            } else if (responseLeaguesByCountry.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(responseLeaguesByCountry.body());
                if (jsonResponse.has("response")) {
                    return ResponseEntity.ok(allLeaguesFromCountry(jsonResponse.getJSONArray("response")));

                } else {
                    throw new UsernameNotFoundException("Not found");
                }
            } else {
                throw new UsernameNotFoundException("Not found");
            }
        }
        throw new UsernameNotFoundException("Not found");
    }

    private List<LeagueDto> allLeaguesFromCountry(JSONArray response) {
        List<LeagueDto> leagues = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject leagueObject = response.getJSONObject(i);
            JSONArray seasonsArray = leagueObject.getJSONArray("seasons");
            for (int j = 0; j < seasonsArray.length(); j++) {
                JSONObject currenSeason = seasonsArray.getJSONObject(j);
                if (currenSeason.getBoolean("current")) {
                    boolean hasStatisticsFixtures = currenSeason.getJSONObject("coverage")
                            .getJSONObject("fixtures").optBoolean("statistics_fixtures", false);
                    boolean hasStatisticsPlayers = currenSeason.getJSONObject("coverage")
                            .getJSONObject("fixtures").optBoolean("statistics_players", false);
                    if (hasStatisticsFixtures && hasStatisticsPlayers) {
                        leagues.add(LeagueDto.builder()
                                .leagueId(leagueObject.getJSONObject("league").getLong("id"))
                                .name(leagueObject.getJSONObject("league").getString("name"))
                                .logo(leagueObject.getJSONObject("league").getString("logo"))
                                .build());
                    }
                }
            }
        }
        if (leagues.isEmpty()) {
            throw new UsernameNotFoundException("Not found");
        }
        return leagues;
    }

    public ResponseEntity<?> findPossibleClubs(Long leagueId) throws IOException, InterruptedException {
        int attempts = 0;

        while (attempts < apiKeyManager.getApiKeysLength()) {
            HttpResponse<String> responseClubs = footballApiUtil.getClubsFromLeague(leagueId, apiKeyManager.getApiKey());
            if (responseClubs.statusCode() == 429) {
                apiKeyManager.switchToNextApiKey();
                attempts++;
            } else if (responseClubs.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(responseClubs.body());
                if (jsonResponse.has("response")) {
                    return ResponseEntity.ok(allTeamsByLeague(jsonResponse.getJSONArray("response")));

                } else {
                    throw new UsernameNotFoundException("Not found");
                }
            } else {
                throw new UsernameNotFoundException("Not found");
            }
        }
        throw new UsernameNotFoundException("Not found");
    }

    private List<TeamSelectDto> allTeamsByLeague(JSONArray response) {
        List<TeamSelectDto> teams = new ArrayList<>();

        for (int i = 0; i < response.length(); i++) {
            JSONObject teamObject = response.getJSONObject(i).getJSONObject("team");
            teams.add(new TeamSelectDto(teamObject.getLong("id"), teamObject.getString("name")));
        }

        if (teams.isEmpty()) {
            throw new UsernameNotFoundException("Not found");
        }
        return teams;
    }
}
