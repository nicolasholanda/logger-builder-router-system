package com.logger.entry;

import com.logger.destination.DestinationType;
import com.logger.destination.LogDestination;
import com.logger.route.LogLevel;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class LogEntryRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private LogEntryRepository repository;

    @Test
    void findByLevel_returnsMatching() {
        em.persist(entry(LogLevel.INFO, EntryStatus.SENT, null));
        em.persist(entry(LogLevel.ERROR, EntryStatus.SENT, null));
        em.flush();

        List<LogEntry> result = repository.findByLevel(LogLevel.INFO);

        assertEquals(1, result.size());
        assertEquals(LogLevel.INFO, result.get(0).getLevel());
    }

    @Test
    void findByStatus_returnsMatching() {
        em.persist(entry(LogLevel.INFO, EntryStatus.PENDING, null));
        em.persist(entry(LogLevel.WARN, EntryStatus.SENT, null));
        em.flush();

        List<LogEntry> result = repository.findByStatus(EntryStatus.PENDING);

        assertEquals(1, result.size());
        assertEquals(EntryStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    void findByDestinationId_returnsMatching() {
        LogDestination dest = new LogDestination();
        dest.setName("Console");
        dest.setType(DestinationType.CONSOLE);
        dest.setAsync(false);
        dest.setEnabled(true);
        em.persist(dest);

        em.persist(entry(LogLevel.INFO, EntryStatus.SENT, dest));
        em.persist(entry(LogLevel.ERROR, EntryStatus.SENT, null));
        em.flush();

        List<LogEntry> result = repository.findByDestinationId(dest.getId());

        assertEquals(1, result.size());
        assertEquals(dest.getId(), result.get(0).getDestination().getId());
    }

    private LogEntry entry(LogLevel level, EntryStatus status, LogDestination destination) {
        LogEntry e = new LogEntry();
        e.setLevel(level);
        e.setMessage("Test message");
        e.setSource("test");
        e.setDestination(destination);
        e.setTimestamp(Instant.now());
        e.setStatus(status);
        return e;
    }
}
