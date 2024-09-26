package org.example.footballanalyzer.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.example.footballanalyzer.Data.Entity.User;
import org.example.footballanalyzer.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        log.info("Users: {}", users);

        return ResponseEntity.ok(users.stream().map(
                user ->
                        new UserDTO(
                                user.getId(),
                                user.getLogin(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getEmail(),
                                user.getRole().getRoleName().name()
                        )

        ).collect(Collectors.toUnmodifiableList()));
    }

    public ResponseEntity<UserDTO> getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        return ResponseEntity.ok(new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().getRoleName().name()
        ));
    }
}
