package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.FixturesStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixturesStatsRepository extends JpaRepository<FixturesStats, Long> {
}
