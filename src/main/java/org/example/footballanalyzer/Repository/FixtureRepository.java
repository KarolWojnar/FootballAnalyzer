package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.Fixture;
import org.example.footballanalyzer.Data.Entity.League;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FixtureRepository extends JpaRepository<Fixture, Long> {
    Optional<Fixture> findByFixtureId(Long fixtureId);

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    @Query("SELECT f FROM Fixture f WHERE f.isCollected = false AND f.isCounted = true AND f.awayGoals != -1")
    List<Fixture> findAllCompleted();

    List<Fixture> findAllByDateBetweenAndIsCollectedAndIsCounted(Date startDate, Date endDate, boolean collected, boolean counted);

    List<Fixture> findAllByDateBetweenAndIsCollectedAndIsCountedAndLeague(Date startDate, Date endDate, boolean collected, boolean counted, League league);
    Page<Fixture> findAllByDateAfterOrderByDateAsc(Date startDate, Pageable pageable);

    Page<Fixture> findAllByLeague_IdAndDateAfterOrderByDateAsc(Long league1, Date startDate, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Fixture f set f.isCounted = true where f.id = ?1")
    void setFixtureAsCounted(Long id);

    @Modifying
    @Transactional
    @Query("update Fixture f set f.isCollected = true where f.id = ?1")
    void setFixtureAsCollected(Long id);

    List<Fixture> findAllByDateBeforeAndIsCountedOrderByDate(Date date, boolean counted);

    @Query(nativeQuery = true, value = "SELECT * FROM Fixture f WHERE (f.away_team_id = ?1 OR f.home_team_id = ?1) AND f.date > ?2 LIMIT 1")
    Optional<Fixture> findNextFixture(long team, Date today);

    @Query("SELECT f FROM Fixture f WHERE f.date > ?2 AND f.league.id = ?1 AND (f.homeTeam.name LIKE %?3% or f.awayTeam.name LIKE %?3%) ORDER BY f.date")
    Page<Fixture> findAllByTeamAndName(Long leagueId, Date startDate, String teamName, Pageable pageable);

    @Query("SELECT f FROM Fixture f WHERE f.date > ?1 AND (f.homeTeam.name LIKE %?2% or f.awayTeam.name LIKE %?2%) ORDER BY f.date")
    Page<Fixture> findAllByTeam(Date startDate, String teamName, Pageable pageable);
}
