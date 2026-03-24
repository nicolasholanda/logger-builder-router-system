package com.logger.backend;

import com.logger.entry.LogEntry;

public interface LoggerBackend {

    void send(LogEntry entry);
}
