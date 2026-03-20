package com.logger.destination;

import java.util.List;
import java.util.Optional;
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
class LogDestinationServiceTest {

    @Mock
    private LogDestinationRepository repository;

    @InjectMocks
    private LogDestinationService service;

    @Test
    void findAll_returnsAll() {
        LogDestination d = destination();
        when(repository.findAll()).thenReturn(List.of(d));

        List<LogDestination> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals(d, result.get(0));
    }

    @Test
    void findById_found() {
        LogDestination d = destination();
        when(repository.findById(1L)).thenReturn(Optional.of(d));

        LogDestination result = service.findById(1L);

        assertEquals(d, result);
    }

    @Test
    void findById_notFound_throws() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.findById(99L));
    }

    @Test
    void create_savesAndReturns() {
        LogDestination d = destination();
        when(repository.save(d)).thenReturn(d);

        LogDestination result = service.create(d);

        assertEquals(d, result);
        verify(repository).save(d);
    }

    @Test
    void update_updatesFields() {
        LogDestination existing = destination();
        existing.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        LogDestination updated = new LogDestination();
        updated.setName("Updated");
        updated.setType(DestinationType.ELK);
        updated.setConfig("{\"host\":\"elk\"}");
        updated.setAsync(true);
        updated.setEnabled(false);

        LogDestination result = service.update(1L, updated);

        assertEquals("Updated", result.getName());
        assertEquals(DestinationType.ELK, result.getType());
        verify(repository).save(existing);
    }

    @Test
    void delete_callsRepository() {
        LogDestination d = destination();
        d.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(d));

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_notFound_throws() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.delete(99L));
    }

    private LogDestination destination() {
        LogDestination d = new LogDestination();
        d.setName("Console");
        d.setType(DestinationType.CONSOLE);
        d.setConfig(null);
        d.setAsync(false);
        d.setEnabled(true);
        return d;
    }
}
