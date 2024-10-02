package org.example.footballanalyzer.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyManager {
    @Value("${api.keys}")
    private String[] apiKeys;
    public int getApiKeysLength() {
        return apiKeys.length;
    }

    private int currentKeyIndex = 0;

    public synchronized String getApiKey() {
        return apiKeys[currentKeyIndex];
    }

    public synchronized void switchToNextApiKey() {
        currentKeyIndex = (currentKeyIndex + 1) % apiKeys.length;
    }
}
