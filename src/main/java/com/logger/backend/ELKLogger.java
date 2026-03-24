package com.logger.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logger.entry.LogEntry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ELKLogger implements LoggerBackend {

    private final RestTemplate restTemplate;

    public ELKLogger(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void send(LogEntry entry) {
        try {
            String url = new ObjectMapper().readTree(entry.getDestination().getConfig()).get("url").asText();
            restTemplate.postForEntity(url, entry, String.class);
        } catch (Exception e) {
            throw new RuntimeException("ELKLogger failed to send log entry", e);
        }
    }
}
