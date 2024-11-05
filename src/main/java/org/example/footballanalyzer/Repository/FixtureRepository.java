package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.Fixture;
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
    Page<Fixture> findAllByDateAfterOrderByDateAsc(Date startDate, Pageable pageable);

    Page<Fixture> findAllByAwayTeam_League_IdAndHomeTeam_League_IdAndDateAfterOrderByDateAsc(Long league1, Long league2, Date startDate, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Fixture f set f.isCounted = true where f.id = ?1")
    void setFixtureAsCounted(Long id);

    @Modifying
    @Transactional
    @Query("update Fixture f set f.isCollected = true where f.id = ?1")
    void setFixtureAsCollected(Long id);
}
