package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
}
