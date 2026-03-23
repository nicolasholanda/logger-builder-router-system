package com.logger.route;

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
@RequestMapping("/api/routes")
public class LogRouteController {

    private final LogRouteService service;

    public LogRouteController(LogRouteService service) {
        this.service = service;
    }

    @GetMapping
    public List<LogRoute> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public LogRoute findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogRoute create(@RequestBody LogRoute route) {
        return service.create(route);
    }

    @PutMapping("/{id}")
    public LogRoute update(@PathVariable Long id, @RequestBody LogRoute route) {
        return service.update(id, route);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
