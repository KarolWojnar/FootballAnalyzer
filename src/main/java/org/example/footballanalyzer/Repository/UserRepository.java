package org.example.footballanalyzer.Repository;

import org.example.footballanalyzer.Data.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByLoginOrEmail(String login, String email);
    Optional<User> findByLogin(String login);
}
