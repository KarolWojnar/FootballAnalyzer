package org.example.footballanalyzer.Service;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public String login(String username, String password) {
        return username.equals(password) ? "hello " + username : "wrong credentials";
    }
}
