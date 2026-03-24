package com.logger.backend;

import com.logger.entry.LogEntry;
import com.logger.route.LogLevel;
import java.time.Instant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ConsoleLoggerTest {

    private final ConsoleLogger logger = new ConsoleLogger();

    @Test
    void send_doesNotThrow() {
        LogEntry entry = new LogEntry();
        entry.setLevel(LogLevel.INFO);
        entry.setMessage("hello");
        entry.setTimestamp(Instant.now());

        assertDoesNotThrow(() -> logger.send(entry));
    }
}
