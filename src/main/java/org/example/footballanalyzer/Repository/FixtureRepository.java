package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.Fixture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FixtureRepository extends JpaRepository<Fixture, Long> {
    Optional<Fixture> findByFixtureId(Long fixtureId);

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    @Query("SELECT f FROM Fixture f WHERE NOT f.isCounted AND f.awayGoals != -1")
    List<Fixture> findAllCompleted();

    List<Fixture> findAllByDateBetween(Date startDate, Date endDate);
}
