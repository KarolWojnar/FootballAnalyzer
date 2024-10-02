package org.example.footballanalyzer.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Config.ApiKeyManager;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class FootballService {

    private final ApiKeyManager apiKeyManager;

    public void saveAllByLeagueSeason(Long league, int season) throws IOException, InterruptedException, JSONException {
        int attempts = 0;

        while(attempts < apiKeyManager.getApiKeysLength()) {
            HttpResponse<String> responseFixturesByLeagueAndSeason = getFixturesByLeagueAndSeason(league, season);

            if (responseFixturesByLeagueAndSeason.statusCode() == 429) {
                apiKeyManager.switchToNextApiKey();
                attempts++;
            } else if (responseFixturesByLeagueAndSeason.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(responseFixturesByLeagueAndSeason.body());

                if (jsonResponse.has("response")) {
                    //todo: save teams to database
                    return;
                } else {
                    throw new IOException("Fixture not found");
                }
            } else {
                throw new IOException("Unexpected response status: " + responseFixturesByLeagueAndSeason.statusCode());
            }
        }

        throw new IOException("Failed to retrieve data from API after trying all API keys.");
    }

    private HttpResponse<String> getFixturesByLeagueAndSeason(Long league, int season) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api-football-beta.p.rapidapi.com/fixtures?season=" + season + "&league=" + league))
                .header("x-rapidapi-key", apiKeyManager.getApiKey())
                .header("x-rapidapi-host", "api-football-beta.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}
