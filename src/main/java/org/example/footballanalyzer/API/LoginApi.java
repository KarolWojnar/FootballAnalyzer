package org.example.footballanalyzer.API;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping
public interface LoginApi {
    @GetMapping("/hello")
    ResponseEntity<String> hello(@RequestParam String name, @RequestParam String password);
}
