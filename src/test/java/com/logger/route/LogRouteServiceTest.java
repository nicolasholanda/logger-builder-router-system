package com.logger.route;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogRouteServiceTest {

    @Mock
    private LogRouteRepository repository;

    @InjectMocks
    private LogRouteService service;

    @Test
    void findAll_returnsAll() {
        LogRoute r = route();
        when(repository.findAll()).thenReturn(List.of(r));

        List<LogRoute> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals(r, result.get(0));
    }

    @Test
    void findById_found() {
        LogRoute r = route();
        when(repository.findById(1L)).thenReturn(Optional.of(r));

        LogRoute result = service.findById(1L);

        assertEquals(r, result);
    }

    @Test
    void findById_notFound_throws() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.findById(99L));
    }

    @Test
    void create_savesAndReturns() {
        LogRoute r = route();
        when(repository.save(r)).thenReturn(r);

        LogRoute result = service.create(r);

        assertEquals(r, result);
        verify(repository).save(r);
    }

    @Test
    void update_updatesFields() {
        LogRoute existing = route();
        existing.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        LogRoute updated = new LogRoute();
        updated.setName("Updated Route");
        updated.setLevels(Set.of(LogLevel.ERROR));
        updated.setDestinations(Set.of());

        LogRoute result = service.update(1L, updated);

        assertEquals("Updated Route", result.getName());
        assertEquals(Set.of(LogLevel.ERROR), result.getLevels());
        verify(repository).save(existing);
    }

    @Test
    void delete_callsRepository() {
        LogRoute r = route();
        r.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(r));

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_notFound_throws() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.delete(99L));
    }

    private LogRoute route() {
        LogRoute r = new LogRoute();
        r.setName("Default Route");
        r.setLevels(Set.of(LogLevel.INFO, LogLevel.WARN));
        r.setDestinations(Set.of());
        return r;
    }
}
