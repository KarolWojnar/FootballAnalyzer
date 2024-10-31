package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.ResetOperations;
import org.example.footballanalyzer.Data.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResetOperationsRepository extends JpaRepository<ResetOperations, Long> {
    @Modifying
    void deleteAllByUser(UserEntity user);

    Optional<ResetOperations> findByUuid(String uuid);

    @Query(nativeQuery = true, value = "SELECT * FROM resetoperations WHERE createdate < current_timestamp - INTERVAL '15 minutes'")
    List<ResetOperations> findExpiredOperations();
}
