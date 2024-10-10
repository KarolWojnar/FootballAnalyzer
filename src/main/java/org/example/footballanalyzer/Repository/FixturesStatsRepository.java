package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.Fixture;
import org.example.footballanalyzer.Data.Entity.FixturesStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixturesStatsRepository extends JpaRepository<FixturesStats, Long> {
    List<FixturesStats> getFixturesStatsByFixture(Fixture fixture);
}
