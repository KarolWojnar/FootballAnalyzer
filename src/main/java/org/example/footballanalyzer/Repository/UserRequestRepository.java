package org.example.footballanalyzer.Repository;


import org.example.footballanalyzer.Data.Entity.UserEntity;
import org.example.footballanalyzer.Data.Entity.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM UserRequest ur WHERE ur.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    @Transactional
    void deleteByRequestStatus(UserRequest.RequestStatus requestStatus);

    List<UserRequest> findAllByUser(UserEntity user);
}
