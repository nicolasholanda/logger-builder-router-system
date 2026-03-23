package com.logger.entry;

import com.logger.route.LogLevel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {

    List<LogEntry> findByLevel(LogLevel level);

    List<LogEntry> findByStatus(EntryStatus status);

    List<LogEntry> findByDestinationId(Long destinationId);
}
