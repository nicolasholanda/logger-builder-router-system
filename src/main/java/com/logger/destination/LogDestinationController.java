package com.logger.destination;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/destinations")
public class LogDestinationController {

    private final LogDestinationService service;

    public LogDestinationController(LogDestinationService service) {
        this.service = service;
    }

    @GetMapping
    public List<LogDestination> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public LogDestination findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogDestination create(@RequestBody LogDestination destination) {
        return service.create(destination);
    }

    @PutMapping("/{id}")
    public LogDestination update(@PathVariable Long id, @RequestBody LogDestination destination) {
        return service.update(id, destination);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
