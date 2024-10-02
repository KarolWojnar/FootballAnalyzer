package org.example.footballanalyzer.Service.Util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RequiredArgsConstructor
@Component
public class FootballApiUtil {

    private final String defaultUrl = "https://api-football-beta.p.rapidapi.com/";

     public HttpResponse<String> getFixturesByLeagueAndSeason(Long league, Long season, String apiKey) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(defaultUrl + "fixtures?season=" + season + "&league=" + league))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "api-football-beta.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}
