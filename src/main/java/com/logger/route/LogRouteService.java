package com.logger.route;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LogRouteService {

    private final LogRouteRepository repository;

    public LogRouteService(LogRouteRepository repository) {
        this.repository = repository;
    }

    public List<LogRoute> findAll() {
        return repository.findAll();
    }

    public LogRoute findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("LogRoute not found: " + id));
    }

    public LogRoute create(LogRoute route) {
        return repository.save(route);
    }

    public LogRoute update(Long id, LogRoute updated) {
        LogRoute existing = findById(id);
        existing.setName(updated.getName());
        existing.setLevels(updated.getLevels());
        existing.setDestinations(updated.getDestinations());
        return repository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}
