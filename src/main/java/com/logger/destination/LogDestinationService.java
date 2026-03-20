package com.logger.destination;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LogDestinationService {

    private final LogDestinationRepository repository;

    public LogDestinationService(LogDestinationRepository repository) {
        this.repository = repository;
    }

    public List<LogDestination> findAll() {
        return repository.findAll();
    }

    public LogDestination findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("LogDestination not found: " + id));
    }

    public LogDestination create(LogDestination destination) {
        return repository.save(destination);
    }

    public LogDestination update(Long id, LogDestination updated) {
        LogDestination existing = findById(id);
        existing.setName(updated.getName());
        existing.setType(updated.getType());
        existing.setConfig(updated.getConfig());
        existing.setAsync(updated.isAsync());
        existing.setEnabled(updated.isEnabled());
        return repository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}
