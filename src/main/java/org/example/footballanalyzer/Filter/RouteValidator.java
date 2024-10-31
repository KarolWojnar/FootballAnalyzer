package org.example.footballanalyzer.Filter;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/api/users/register",
            "/api/users/login",
            "/api/users/validate",
            "/api/coach/futureMatches",
            "/api/coach/all-teams",
            "/api/user/reset-password",
            "/api/user/active"
    );
}
