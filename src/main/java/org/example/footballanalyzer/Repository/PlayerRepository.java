package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.Player;
import org.example.footballanalyzer.Data.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByPlayerId(Long playerId);
    List<Player> findAllByTeam(Team team);
}
