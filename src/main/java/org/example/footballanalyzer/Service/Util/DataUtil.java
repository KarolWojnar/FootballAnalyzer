package org.example.footballanalyzer.Service.Util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Data.Entity.Fixture;
import org.example.footballanalyzer.Data.Entity.League;
import org.example.footballanalyzer.Data.Entity.Team;
import org.example.footballanalyzer.Repository.FixtureRepository;
import org.example.footballanalyzer.Repository.LeagueRepository;
import org.example.footballanalyzer.Repository.TeamRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataUtil {

    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final FixtureRepository fixtureRepository;

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
        newFixture.setAwayGoals(fixtureResult.getInt("away"));
        newFixture.setHomeGoals(fixtureResult.getInt("home"));

        fixtureRepository.save(newFixture);
        log.info("Saved new fixture: {}", newFixture.getFixtureId());

        return newFixture;
    }
}
