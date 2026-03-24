package com.logger.backend;

import com.logger.destination.DestinationType;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class LoggerBackendRegistry {

    private final Map<DestinationType, LoggerBackend> backends;

    public LoggerBackendRegistry(ConsoleLogger consoleLogger, FileLogger fileLogger, ELKLogger elkLogger) {
        backends = new EnumMap<>(DestinationType.class);
        backends.put(DestinationType.CONSOLE, consoleLogger);
        backends.put(DestinationType.FILE, fileLogger);
        backends.put(DestinationType.ELK, elkLogger);
    }

    public LoggerBackend get(DestinationType type) {
        LoggerBackend backend = backends.get(type);
        if (backend == null) {
            throw new RuntimeException("No backend registered for type: " + type);
        }
        return backend;
    }
}
