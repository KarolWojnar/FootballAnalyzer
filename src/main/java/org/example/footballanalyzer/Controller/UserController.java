package org.example.footballanalyzer.Controller;

import org.example.footballanalyzer.API.UserApi;
import org.example.footballanalyzer.Data.Dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UserController implements UserApi {
    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return null;
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long id) {
        return null;
    }
}
