package org.example.footballanalyzer.Service.Util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Data.Dto.FixturesDto;
import org.example.footballanalyzer.Data.Dto.PlayerStatsDto;
import org.example.footballanalyzer.Data.Entity.*;
import org.example.footballanalyzer.Repository.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataUtil {

    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final FixtureRepository fixtureRepository;
    private final PlayerRepository playerRepository;
    private final FixturesStatsRepository fixturesStatsRepository;
    private final FixtureStatsTeamRepository fixtureStatsTeamRepository;

    public League saveLeague(JSONObject league) throws JSONException {
        League newLeague = new League();

        newLeague.setLeagueId(league.getLong("id"));
        newLeague.setCountry(league.getString("country"));
        newLeague.setName(league.getString("name"));
        newLeague.setLogo(league.getString("logo"));

        leagueRepository.save(newLeague);

        log.info("Saved new league: {}", newLeague.getName());
        return newLeague;
    }

    public Team saveTeam(JSONObject team, League league) throws JSONException {
        Team newTeam = new Team();

        newTeam.setTeamId(team.getLong("id"));
        newTeam.setName(team.getString("name"));
        newTeam.setLogo(team.getString("logo"));
        newTeam.setLeague(league);

        teamRepository.save(newTeam);
        log.info("Saved new team: {}", newTeam.getName());

        return newTeam;
    }

    public Fixture saveFixture(JSONObject fixture, Team homeTeam, Team awayTeam) throws JSONException, ParseException {
        Fixture newFixture = new Fixture();
        JSONObject fixtureInfo = fixture.getJSONObject("fixture");
        JSONObject fixtureResult = fixture.getJSONObject("goals");
        newFixture.setFixtureId(fixtureInfo.getLong("id"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = formatter.parse(fixtureInfo.getString("date"));
        newFixture.setDate(date);
        newFixture.setSeason(fixture.getJSONObject("league").getInt("season"));
        newFixture.setHomeTeam(homeTeam);
        newFixture.setAwayTeam(awayTeam);
        newFixture.setAwayGoals(fixtureResult.optInt("away", -1));
        newFixture.setHomeGoals(fixtureResult.optInt("home", -1));

        fixtureRepository.save(newFixture);
        log.info("Saved new fixture: {}", newFixture.getFixtureId());

        return newFixture;
    }

    public Player savePlayer(JSONObject player, JSONObject team) {
        Player newPlayer = new Player();
        newPlayer.setPlayerId(player.getLong("id"));
        newPlayer.setName(player.getString("name"));
        newPlayer.setPhoto(player.getString("photo"));

        Optional<Team> optionalTeam = teamRepository.findByTeamId(team.getLong("id"));
        optionalTeam.ifPresent(newPlayer::setTeam);

        playerRepository.save(newPlayer);
        log.info("Saved new player: {}", newPlayer.getName());

        return newPlayer;
    }

    public void savePlayerStats(Player player, Fixture fixture, int offsides, JSONObject games, JSONObject shots,
                                JSONObject goals, JSONObject passes, JSONObject tackles, JSONObject duels,
                                JSONObject dribbles, JSONObject fouls, JSONObject cards, JSONObject penalty) {
        FixturesStats newFixturesStats = new FixturesStats();
        newFixturesStats.setPlayer(player);
        newFixturesStats.setFixture(fixture);
        newFixturesStats.setOffsides(offsides);
        newFixturesStats.setMinutes(games.optInt("minutes", 0));
        newFixturesStats.setPosition(games.optString("position", "N/A"));
        newFixturesStats.setRating(games.optDouble("rating", 0.0));
        newFixturesStats.setShotsTotal(shots.optInt("total", 0));
        newFixturesStats.setShotsOnGoal(shots.optInt("on", 0));
        newFixturesStats.setGoalsConceded(goals.optInt("conceded", 0));
        newFixturesStats.setGoalsTotal(goals.optInt("total", 0));
        newFixturesStats.setAssists(goals.optInt("assists", 0));
        newFixturesStats.setSaves(goals.optInt("saves", 0));
        newFixturesStats.setPassesTotal(passes.optInt("total", 0));
        newFixturesStats.setPassesKey(passes.optInt("key", 0));
        newFixturesStats.setPassesAccuracy(passes.optInt("accuracy", 0));
        newFixturesStats.setTacklesTotal(tackles.optInt("total", 0));
        newFixturesStats.setTacklesBlocks(tackles.optInt("blocks", 0));
        newFixturesStats.setTacklesInterceptions(tackles.optInt("interceptions", 0));
        newFixturesStats.setDuelsTotal(duels.optInt("total", 0));
        newFixturesStats.setDuelsWon(duels.optInt("won", 0));
        newFixturesStats.setDribblesAttempts(dribbles.optInt("attempts", 0));
        newFixturesStats.setDribblesSuccess(dribbles.optInt("success", 0));
        newFixturesStats.setFoulsCommitted(fouls.optInt("committed", 0));
        newFixturesStats.setFoulsDrawn(fouls.optInt("drawn", 0));
        newFixturesStats.setCardsYellow(cards.optInt("yellow", 0));
        newFixturesStats.setCardsRed(cards.optInt("red", 0));
        newFixturesStats.setPenaltyWon(penalty.optInt("won", 0));
        newFixturesStats.setPenaltyCommitted(penalty.optInt("commited", 0));
        newFixturesStats.setPenaltyScored(penalty.optInt("scored", 0));
        newFixturesStats.setPenaltyMissed(penalty.optInt("missed", 0));
        newFixturesStats.setPenaltySaved(penalty.optInt("saved", 0));

        fixturesStatsRepository.save(newFixturesStats);

    }

    public void collectStatsAndSave(Fixture fixture, FixtureStatsTeam teamStats) {
        fixtureStatsTeamRepository.save(teamStats);

        Optional<Fixture> optionalFixture = fixtureRepository.findByFixtureId(fixture.getFixtureId());

        if (optionalFixture.isPresent()) {
            Fixture existingFixture = optionalFixture.get();
            existingFixture.setCounted(true);
            fixtureRepository.save(existingFixture);
        }
    }

    public List<PlayerStatsDto> findAllPlayersStatsByTeam(Team team) {
        List<Player> playersFromTeam = playerRepository.findAllByTeam(team);

        return fixturesStatsRepository.findAllPlayerStatsByPlayers(playersFromTeam);
    }

    public ResponseEntity<?> closestMatches(Date startDate, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Fixture> fixtures = fixtureRepository.findAllByDateAfterOrderByDateAsc(startDate, pageable);
        if (fixtures.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<FixturesDto> fixturesDtos = new ArrayList<>();

        for (Fixture fixture : fixtures) {
            FixturesDto fixturesDto = new FixturesDto();
            fixturesDto.setDate(fixture.getDate());
            fixturesDto.setHomeTeam(fixture.getHomeTeam().getName());
            fixturesDto.setAwayTeam(fixture.getAwayTeam().getName());
            fixturesDtos.add(fixturesDto);
        }
        return ResponseEntity.ok(fixturesDtos);
    }

    public void setFixtureAsCounted(long fixtureId) {
        Optional<Fixture> optionalFixture = fixtureRepository.findById(fixtureId);
        if (optionalFixture.isPresent()) {
            Fixture fixture = optionalFixture.get();
            fixtureRepository.updateFixture(fixture.getId());
        }
        log.info("Fixture with id: {} marked as counted", fixtureId);
    }
}
