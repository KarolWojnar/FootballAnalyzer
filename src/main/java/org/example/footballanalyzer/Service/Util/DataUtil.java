package org.example.footballanalyzer.Service.Util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Data.Code;
import org.example.footballanalyzer.Data.Dto.FixturesDto;
import org.example.footballanalyzer.Data.Dto.PlayerStatsDto;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Data.Dto.UserRequestDto;
import org.example.footballanalyzer.Data.Entity.*;
import org.example.footballanalyzer.Repository.*;
import org.example.footballanalyzer.Service.EmailService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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
    private final UserRepository userRepository;
    private final UserRequestRepository userRequestRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public League saveLeague(JSONObject league) throws JSONException {
        League newLeague = new League();

        newLeague.setLeagueId(league.getLong("id"));
        newLeague.setCountry(league.getString("country"));
        newLeague.setName(league.getString("name"));
        newLeague.setLogo(league.getString("logo"));

        newLeague = leagueRepository.save(newLeague);

        log.info("Saved new league: {}", newLeague.getName());
        return newLeague;
    }

    @Transactional
    public Team addTeamToLeague(Team team, League league) {
        findLeague(team, league);
        return team;
    }

    @Transactional
    public Team saveTeam(JSONObject team, League league) throws JSONException {
        Team newTeam = new Team();
        newTeam.setTeamId(team.getLong("id"));
        newTeam.setName(team.getString("name"));
        newTeam.setLogo(team.getString("logo"));

        newTeam = teamRepository.save(newTeam);

        findLeague(newTeam, league);

        log.info("Saved new team: {}", newTeam.getName());
        return newTeam;
    }

    @Transactional
    public void findLeague(Team team, League league) {
        Optional<League> optionalLeague = leagueRepository.findByLeagueId(league.getLeagueId());
        Optional<Team> optionalTeam = teamRepository.findByTeamId(team.getTeamId());
        if (optionalLeague.isPresent() && optionalTeam.isPresent()) {
            league = optionalLeague.get();
            team = optionalTeam.get();
            if (!league.getTeams().contains(team) && !team.getLeagues().contains(league)) {
                team.getLeagues().add(league);
                Team team2 = teamRepository.save(team);
                league.getTeams().add(team2);
                leagueRepository.save(league);
            }
        }
    }

    public Fixture saveFixture(JSONObject fixture, Team homeTeam, Team awayTeam) throws JSONException, ParseException {
        Fixture newFixture = new Fixture();
        League league = leagueRepository.findByLeagueId(fixture.getJSONObject("league").getLong("id")).orElseThrow();
        JSONObject fixtureInfo = fixture.getJSONObject("fixture");
        JSONObject fixtureResult = fixture.getJSONObject("goals");
        newFixture.setFixtureId(fixtureInfo.getLong("id"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = formatter.parse(fixtureInfo.getString("date"));
        newFixture.setDate(date);
        newFixture.setLeague(league);
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
        newPlayer.setName(player.optString("name", null));
        newPlayer.setPhoto(player.optString("photo", null));

        Optional<Team> optionalTeam = teamRepository.findByTeamId(team.getLong("id"));
        optionalTeam.ifPresent(newPlayer::setTeam);

        playerRepository.save(newPlayer);
        log.info("Saved new player: {}", newPlayer.getName());

        return newPlayer;
    }

    @Transactional
    public void savePlayerStats(Player player, Fixture fixture, Team team, int offsides, JSONObject games, JSONObject shots,
                                JSONObject goals, JSONObject passes, JSONObject tackles, JSONObject duels,
                                JSONObject dribbles, JSONObject fouls, JSONObject cards, JSONObject penalty) {
        FixturesStats newFixturesStats = new FixturesStats();
        newFixturesStats.setPlayer(player);
        newFixturesStats.setFixture(fixture);
        newFixturesStats.setOffsides(offsides);
        newFixturesStats.setTeam(team);
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
            existingFixture.setCollected(true);
            fixtureRepository.setFixtureAsCollected(existingFixture.getId());
        }
    }

    public List<PlayerStatsDto> findAllPlayersStatsByTeamAndDate(Team team, LocalDate startDate, LocalDate endDate) {

        Date dateStart = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateEnd = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return fixturesStatsRepository.findAllPlayerStatsByTeam(team, dateStart, dateEnd);
    }

    public ResponseEntity<?> closestMatches(Date startDate, int page, Long leagueId, String teamName) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Fixture> fixtures;
        if (leagueId != null) {
            fixtures = fixtureRepository.findAllByLeague_IdAndDateAfterOrderByDateAsc(leagueId, startDate, pageable);
            if (teamName != null) {
                fixtures = fixtureRepository.findAllByTeamAndName(leagueId, startDate, teamName, pageable);
            }
        } else {
            fixtures = fixtureRepository.findAllByDateAfterOrderByDateAsc(startDate, pageable);
            if (teamName != null) {
                fixtures = fixtureRepository.findAllByTeam(startDate, teamName, pageable);
            }
        }
        if (fixtures.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HashMap<String, Object> response = new HashMap<>();
        List<FixturesDto> fixturesDtos = new ArrayList<>();

        for (Fixture fixture : fixtures) {
            FixturesDto fixturesDto = new FixturesDto();
            fixturesDto.setDate(fixture.getDate());
            fixturesDto.setHomeTeam(fixture.getHomeTeam().getName());
            fixturesDto.setLogoHome(fixture.getHomeTeam().getLogo());
            fixturesDto.setAwayTeam(fixture.getAwayTeam().getName());
            fixturesDto.setLeagueId(fixture.getLeague().getId());
            fixturesDto.setLogoAway(fixture.getAwayTeam().getLogo());
            fixturesDtos.add(fixturesDto);
        }
        response.put("fixtures", fixturesDtos);
        response.put("emelents", fixtures.getTotalElements());

        return ResponseEntity.ok(response);

    }

    public void setFixtureAsCounted(long fixtureId) {
        Optional<Fixture> optionalFixture = fixtureRepository.findById(fixtureId);
        if (optionalFixture.isPresent()) {
            Fixture fixture = optionalFixture.get();
            fixtureRepository.setFixtureAsCounted(fixture.getId());
        }
    }

    public ResponseEntity<?> saveNewRequest(UserRequestDto userRequest, String requestData, String login) {
        UserRequest newUserRequest = new UserRequest();
        log.info("Saving new request: {}", login);
        Optional<UserEntity> optionalUser = userRepository.findByLogin(login);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        UserEntity user = optionalUser.get();
        newUserRequest.setRequestData(requestData);
        newUserRequest.setRequestStatus(userRequest.getRequestStatus());
        newUserRequest.setRequestType(userRequest.getRequestType());
        newUserRequest.setUser(user);
        newUserRequest.setCreateDate(new Timestamp(System.currentTimeMillis()).toString());
        userRequestRepository.save(newUserRequest);
        log.info("Saved new request: {}", newUserRequest.getRequestType());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<AuthResponse> saveUserToDb(UserDTO user, Optional<Team> optionalTeam) {
        Optional<Role> role = roleRepository.findById(user.getRoleId());
        if (role.isEmpty()) {
            return ResponseEntity.badRequest().body(new AuthResponse(Code.R1));
        }
        UserEntity newUser = UserEntity.builder()
                .login(user.getLogin())
                .uuid(UUID.randomUUID().toString())
                .team(optionalTeam.orElse(null))
                .role(role.get())
                .coachConfirmPdf(user.getCoachConfirmPdf())
                .password(passwordEncoder.encode(user.getPassword()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
        userRepository.saveAndFlush(newUser);
        emailService.sendActivation(newUser);
        return ResponseEntity.ok().body(new AuthResponse(Code.SUCCESS));
    }
}
