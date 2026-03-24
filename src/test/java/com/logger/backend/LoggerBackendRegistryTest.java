package com.logger.backend;

import com.logger.destination.DestinationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LoggerBackendRegistryTest {

    @Mock
    private ConsoleLogger consoleLogger;

    @Mock
    private FileLogger fileLogger;

    @Mock
    private ELKLogger elkLogger;

    private LoggerBackendRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new LoggerBackendRegistry(consoleLogger, fileLogger, elkLogger);
    }

    @Test
    void console_resolvesCorrectBackend() {
        assertEquals(consoleLogger, registry.get(DestinationType.CONSOLE));
    }

    @Test
    void file_resolvesCorrectBackend() {
        assertEquals(fileLogger, registry.get(DestinationType.FILE));
    }

    @Test
    void elk_resolvesCorrectBackend() {
        assertEquals(elkLogger, registry.get(DestinationType.ELK));
    }
}
