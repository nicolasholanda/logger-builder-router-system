package com.logger.engine;

import com.logger.backend.LoggerBackendRegistry;
import com.logger.destination.LogDestination;
import com.logger.entry.EntryStatus;
import com.logger.entry.LogEntry;
import com.logger.entry.LogEntryRepository;
import com.logger.route.LogLevel;
import com.logger.route.LogRoute;
import com.logger.route.LogRouteRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LogRoutingEngine {

    private final LogRouteRepository routeRepository;
    private final LogEntryRepository entryRepository;
    private final LoggerBackendRegistry registry;

    public LogRoutingEngine(LogRouteRepository routeRepository,
                            LogEntryRepository entryRepository,
                            LoggerBackendRegistry registry) {
        this.routeRepository = routeRepository;
        this.entryRepository = entryRepository;
        this.registry = registry;
    }

    public void dispatch(LogLevel level, String message, String source) {
        List<LogRoute> routes = routeRepository.findByLevelsContaining(level);

        for (LogRoute route : routes) {
            for (LogDestination destination : route.getDestinations()) {
                if (!destination.isEnabled()) {
                    continue;
                }
                LogEntry entry = buildEntry(level, message, source, destination);
                entryRepository.save(entry);

                if (destination.isAsync()) {
                    Thread.ofVirtual().start(() -> sendAndUpdate(entry, destination));
                } else {
                    sendAndUpdate(entry, destination);
                }
            }
        }
    }

    private LogEntry buildEntry(LogLevel level, String message, String source, LogDestination destination) {
        LogEntry entry = new LogEntry();
        entry.setLevel(level);
        entry.setMessage(message);
        entry.setSource(source);
        entry.setDestination(destination);
        entry.setTimestamp(Instant.now());
        entry.setStatus(EntryStatus.PENDING);
        return entry;
    }

    private void sendAndUpdate(LogEntry entry, LogDestination destination) {
        try {
            registry.get(destination.getType()).send(entry);
            entry.setStatus(EntryStatus.SENT);
        } catch (Exception e) {
            entry.setStatus(EntryStatus.FAILED);
        }
        entryRepository.save(entry);
    }
}
