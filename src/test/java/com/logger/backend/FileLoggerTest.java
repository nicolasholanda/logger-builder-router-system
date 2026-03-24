package com.logger.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logger.destination.LogDestination;
import com.logger.entry.LogEntry;
import com.logger.route.LogLevel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FileLoggerTest {

    private final FileLogger logger = new FileLogger();

    @Test
    void send_writesToFile() throws Exception {
        Path tempFile = Files.createTempFile("log", ".txt");
        tempFile.toFile().deleteOnExit();

        String config = new ObjectMapper().writeValueAsString(Map.of("path", tempFile.toString()));

        LogDestination dest = new LogDestination();
        dest.setConfig(config);

        LogEntry entry = new LogEntry();
        entry.setLevel(LogLevel.INFO);
        entry.setMessage("test message");
        entry.setTimestamp(Instant.now());
        entry.setDestination(dest);

        logger.send(entry);

        String content = Files.readString(tempFile);
        assertTrue(content.contains("test message"));
    }
}
