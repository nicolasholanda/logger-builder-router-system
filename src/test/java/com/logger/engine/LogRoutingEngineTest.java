package com.logger.engine;

import com.logger.backend.LoggerBackend;
import com.logger.backend.LoggerBackendRegistry;
import com.logger.destination.DestinationType;
import com.logger.destination.LogDestination;
import com.logger.entry.EntryStatus;
import com.logger.entry.LogEntry;
import com.logger.entry.LogEntryRepository;
import com.logger.route.LogLevel;
import com.logger.route.LogRoute;
import com.logger.route.LogRouteRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogRoutingEngineTest {

    @Mock
    private LogRouteRepository routeRepository;

    @Mock
    private LogEntryRepository entryRepository;

    @Mock
    private LoggerBackendRegistry registry;

    @Mock
    private LoggerBackend backend;

    @InjectMocks
    private LogRoutingEngine engine;

    @Test
    void dispatch_syncDestination_sendsAndSavesAsSent() {
        LogDestination dest = destination(false);
        when(routeRepository.findByLevelsContaining(LogLevel.INFO)).thenReturn(List.of(route(Set.of(LogLevel.INFO), Set.of(dest))));
        when(entryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(registry.get(DestinationType.CONSOLE)).thenReturn(backend);

        engine.dispatch(LogLevel.INFO, "hello", "test");

        verify(backend).send(any());
        ArgumentCaptor<LogEntry> captor = ArgumentCaptor.forClass(LogEntry.class);
        verify(entryRepository, times(2)).save(captor.capture());
        assertEquals(EntryStatus.SENT, captor.getAllValues().get(1).getStatus());
    }

    @Test
    void dispatch_asyncDestination_sendsAndSavesAsSent() throws InterruptedException {
        LogDestination dest = destination(true);
        when(routeRepository.findByLevelsContaining(LogLevel.INFO)).thenReturn(List.of(route(Set.of(LogLevel.INFO), Set.of(dest))));
        when(entryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(registry.get(DestinationType.CONSOLE)).thenReturn(backend);

        engine.dispatch(LogLevel.INFO, "async", "test");

        Thread.sleep(200);
        verify(backend).send(any());
    }

    @Test
    void dispatch_noMatchingRoute_doesNotSaveEntry() {
        when(routeRepository.findByLevelsContaining(LogLevel.ERROR)).thenReturn(List.of());

        engine.dispatch(LogLevel.ERROR, "error", "test");

        verify(entryRepository, never()).save(any());
    }

    @Test
    void dispatch_backendThrows_savesAsFailed() {
        LogDestination dest = destination(false);
        when(routeRepository.findByLevelsContaining(LogLevel.WARN)).thenReturn(List.of(route(Set.of(LogLevel.WARN), Set.of(dest))));
        when(entryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(registry.get(DestinationType.CONSOLE)).thenReturn(backend);
        doThrow(new RuntimeException("send failed")).when(backend).send(any());

        engine.dispatch(LogLevel.WARN, "warn", "test");

        ArgumentCaptor<LogEntry> captor = ArgumentCaptor.forClass(LogEntry.class);
        verify(entryRepository, times(2)).save(captor.capture());
        assertEquals(EntryStatus.FAILED, captor.getAllValues().get(1).getStatus());
    }

    @Test
    void dispatch_disabledDestination_skipped() {
        LogDestination dest = destination(false);
        dest.setEnabled(false);
        when(routeRepository.findByLevelsContaining(LogLevel.INFO)).thenReturn(List.of(route(Set.of(LogLevel.INFO), Set.of(dest))));

        engine.dispatch(LogLevel.INFO, "hello", "test");

        verify(entryRepository, never()).save(any());
    }

    private LogDestination destination(boolean async) {
        LogDestination d = new LogDestination();
        d.setName("Console");
        d.setType(DestinationType.CONSOLE);
        d.setAsync(async);
        d.setEnabled(true);
        return d;
    }

    private LogRoute route(Set<LogLevel> levels, Set<LogDestination> destinations) {
        LogRoute r = new LogRoute();
        r.setName("Route");
        r.setLevels(levels);
        r.setDestinations(destinations);
        return r;
    }
}
