package org.example.footballanalyzer.Repository;

import jakarta.transaction.Transactional;
import org.example.footballanalyzer.Data.Entity.Fixture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    Page<Fixture> findAllByDateAfterOrderByDateAsc(Date startDate, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Fixture f set f.isCounted = true where f.id = ?1")
    void updateFixture(Long id);
}
