package org.example.footballanalyzer.Repository;


import org.example.footballanalyzer.Data.Entity.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
}
