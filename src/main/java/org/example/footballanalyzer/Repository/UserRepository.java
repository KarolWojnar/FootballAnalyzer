package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.Role;
import org.example.footballanalyzer.Data.Entity.Team;
import org.example.footballanalyzer.Data.Entity.UserEntity;
import org.example.footballanalyzer.Data.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findFirstByTeamAndRole(Team team, Role role);

    Optional<UserEntity> findFirstByEmailOrLogin(String email, String login);

    Optional<UserEntity> findByUuid(String uuid);

    @Query(nativeQuery = true, value = "SELECT * FROM users where login=?1 and islock=false and isenabled=true")
    Optional<UserEntity> findByLoginAndLockAndEnabled(String login);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.isLocked = ?2 where u.uuid = ?1")
    void unlockUser(String uuid, boolean isLocked);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.team.id = ?2 where u.id = ?1")
    void updateTeam(Long id, Long teamId);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.coachConfirmPdf = ?2 where u.id = ?1")
    void uploadFile(long id, byte[] teamId);

    Optional<UserEntity> findFirstByTeamAndRole_RoleName(Team team, RoleName roleName);

    List<UserEntity> findAllByTeam(Team team);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.role.id = ?2 where u.id = ?1")
    void updateRole(Long id, Long roleId);
}
