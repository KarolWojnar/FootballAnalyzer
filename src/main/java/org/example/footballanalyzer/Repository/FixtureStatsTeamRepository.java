package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.Fixture;
import org.example.footballanalyzer.Data.Entity.FixtureStatsTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FixtureStatsTeamRepository extends JpaRepository<FixtureStatsTeam, Long> {

    List<FixtureStatsTeam> findAllByFixtureInAndMinutesGreaterThan(List<Fixture> fixture, double minutes);
}
