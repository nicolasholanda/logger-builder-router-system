package com.logger.backend;

import com.logger.entry.LogEntry;
import org.springframework.stereotype.Component;

@Component
public class ConsoleLogger implements LoggerBackend {

    @Override
    public void send(LogEntry entry) {
        System.out.printf("[%s] [%s] %s%n", entry.getTimestamp(), entry.getLevel(), entry.getMessage());
    }
}
