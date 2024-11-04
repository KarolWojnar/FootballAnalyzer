package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.Team;
import org.example.footballanalyzer.Data.Entity.UserEntity;
import org.example.footballanalyzer.Data.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByTeamAndRole_RoleName(Team team, RoleName role);

    Optional<UserEntity> findFirstByEmailOrLogin(String email, String login);

    Optional<UserEntity> findByUuid(String uuid);

    @Query(nativeQuery = true, value = "SELECT * FROM users where login=?1 and islock=false and isenabled=true")
    Optional<UserEntity> findByLoginAndLockAndEnabled(String login);
}
