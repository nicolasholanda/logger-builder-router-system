package com.logger.log;

import com.logger.entry.EntryStatus;
import com.logger.entry.LogEntry;
import com.logger.entry.LogEntryRepository;
import com.logger.engine.LogRoutingEngine;
import com.logger.route.LogLevel;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LogController {

    private final LogRoutingEngine engine;
    private final LogEntryRepository entryRepository;

    public LogController(LogRoutingEngine engine, LogEntryRepository entryRepository) {
        this.engine = engine;
        this.entryRepository = entryRepository;
    }

    @PostMapping("/log")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void log(@RequestBody LogRequest request) {
        engine.dispatch(request.getLevel(), request.getMessage(), request.getSource());
    }

    @GetMapping("/logs")
    public List<LogEntry> getLogs(@RequestParam(required = false) LogLevel level,
                                   @RequestParam(required = false) Long destinationId,
                                   @RequestParam(required = false) EntryStatus status) {
        if (level != null) {
            return entryRepository.findByLevel(level);
        }
        if (status != null) {
            return entryRepository.findByStatus(status);
        }
        if (destinationId != null) {
            return entryRepository.findByDestinationId(destinationId);
        }
        return entryRepository.findAll();
    }
}
