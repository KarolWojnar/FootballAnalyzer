package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.FixtureStatsTeam;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FixtureStatsTeamRepository extends JpaRepository<FixtureStatsTeam, Long> {
}
