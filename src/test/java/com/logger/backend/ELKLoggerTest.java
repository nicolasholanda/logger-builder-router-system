package com.logger.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logger.destination.LogDestination;
import com.logger.entry.LogEntry;
import com.logger.route.LogLevel;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ELKLoggerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ELKLogger logger;

    @Test
    void send_postsToUrl() throws Exception {
        String url = "http://elk:9200/logs";
        String config = new ObjectMapper().writeValueAsString(Map.of("url", url));

        LogDestination dest = new LogDestination();
        dest.setConfig(config);

        LogEntry entry = new LogEntry();
        entry.setLevel(LogLevel.ERROR);
        entry.setMessage("error occurred");
        entry.setTimestamp(Instant.now());
        entry.setDestination(dest);

        logger.send(entry);

        verify(restTemplate).postForEntity(eq(url), eq(entry), eq(String.class));
    }
}
