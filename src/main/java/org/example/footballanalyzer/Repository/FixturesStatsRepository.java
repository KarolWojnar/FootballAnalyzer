package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Dto.PlayerStatsDto;
import org.example.footballanalyzer.Data.Entity.Fixture;
import org.example.footballanalyzer.Data.Entity.FixturesStats;
import org.example.footballanalyzer.Data.Entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixturesStatsRepository extends JpaRepository<FixturesStats, Long> {
    List<FixturesStats> getFixturesStatsByFixture(Fixture fixture);

    @Query("SELECT new org.example.footballanalyzer.Data.Dto.PlayerStatsDto(" +
            "f.fixture.date, f.position, f.player.name, f.minutes, f.rating, f.offsides, f.shotsTotal, " +
            "f.shotsOnGoal, f.goalsTotal, f.goalsConceded, f.assists, f.saves, f.passesTotal, " +
            "f.passesKey, f.passesAccuracy, f.tacklesTotal, f.tacklesBlocks, f.tacklesInterceptions, " +
            "f.duelsTotal, f.duelsWon, f.dribblesAttempts, f.dribblesSuccess, f.foulsDrawn, " +
            "f.foulsCommitted, f.cardsYellow, f.cardsRed, f.penaltyWon, f.penaltyCommitted, " +
            "f.penaltyScored, f.penaltySaved, f.penaltyMissed) " +
            "FROM FixturesStats f WHERE f.player IN :players AND f.minutes > 0")
    List<PlayerStatsDto> findAllPlayerStatsByPlayers(List<Player> players);
}
