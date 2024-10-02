package org.example.footballanalyzer.API;

import jdk.jfr.Description;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/users")
@Description("API for managing users")
public interface UserApi {
    @GetMapping
    ResponseEntity<List<UserDTO>> getAllUsers();

    @GetMapping("/{id}")
    ResponseEntity<UserDTO> getUserById(@PathVariable Long id);
}
