package com.logger.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logger.entry.LogEntry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.springframework.stereotype.Component;

@Component
public class FileLogger implements LoggerBackend {

    @Override
    public void send(LogEntry entry) {
        try {
            String path = new ObjectMapper().readTree(entry.getDestination().getConfig()).get("path").asText();
            String line = String.format("[%s] [%s] %s%n", entry.getTimestamp(), entry.getLevel(), entry.getMessage());
            Files.writeString(Path.of(path), line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("FileLogger failed to write log entry", e);
        }
    }
}
