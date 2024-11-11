package org.example.footballanalyzer.Service.Util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Calendar;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class FootballApiUtil {

    private final String defaultUrl = "https://api-football-beta.p.rapidapi.com/";
    private final String rapidHost = "x-rapidapi-host";
    private final String rapidKey = "x-rapidapi-key";

     public HttpResponse<String> getFixturesByLeagueAndSeason(Long league, Long season, String apiKey) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(defaultUrl + "fixtures?season=" + season + "&league=" + league))
                .header(rapidKey, apiKey)
                .header(rapidHost, "api-football-beta.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getPlayersStatsByFixture(Long fixtureId, String apiKey) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(defaultUrl + "fixtures/players?fixture=" + fixtureId))
                .header(rapidKey, apiKey)
                .header(rapidHost, "api-football-beta.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getLeaguesByCountry(String country, String apiKey) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(defaultUrl + "leagues?search=" + country))
                .header(rapidKey, apiKey)
                .header(rapidHost, "api-football-beta.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getClubsFromLeague(Long leagueId, String apiKey) throws IOException, InterruptedException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(defaultUrl + "teams?league=" + leagueId + "&season=" + cal.get(Calendar.YEAR)))
                .header(rapidKey, apiKey)
                .header(rapidHost, "api-football-beta.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}
