package com.logger.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logger.engine.LogRoutingEngine;
import com.logger.entry.EntryStatus;
import com.logger.entry.LogEntry;
import com.logger.entry.LogEntryRepository;
import com.logger.route.LogLevel;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LogController.class)
class LogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LogRoutingEngine engine;

    @MockitoBean
    private LogEntryRepository entryRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void postLog_returns204() throws Exception {
        doNothing().when(engine).dispatch(any(), any(), any());

        LogRequest request = new LogRequest();
        request.setLevel(LogLevel.INFO);
        request.setMessage("hello");
        request.setSource("test");

        mockMvc.perform(post("/api/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(engine).dispatch(eq(LogLevel.INFO), eq("hello"), eq("test"));
    }

    @Test
    void getLogs_noParams_returnsAll() throws Exception {
        when(entryRepository.findAll()).thenReturn(List.of(entry(LogLevel.INFO, EntryStatus.SENT)));

        mockMvc.perform(get("/api/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].level").value("INFO"));
    }

    @Test
    void getLogs_filterByLevel_returnsFiltered() throws Exception {
        when(entryRepository.findByLevel(LogLevel.ERROR)).thenReturn(List.of(entry(LogLevel.ERROR, EntryStatus.SENT)));

        mockMvc.perform(get("/api/logs").param("level", "ERROR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].level").value("ERROR"));
    }

    @Test
    void getLogs_filterByStatus_returnsFiltered() throws Exception {
        when(entryRepository.findByStatus(EntryStatus.FAILED)).thenReturn(List.of(entry(LogLevel.WARN, EntryStatus.FAILED)));

        mockMvc.perform(get("/api/logs").param("status", "FAILED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("FAILED"));
    }

    @Test
    void getLogs_filterByDestinationId_returnsFiltered() throws Exception {
        when(entryRepository.findByDestinationId(1L)).thenReturn(List.of(entry(LogLevel.DEBUG, EntryStatus.SENT)));

        mockMvc.perform(get("/api/logs").param("destinationId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].level").value("DEBUG"));
    }

    private LogEntry entry(LogLevel level, EntryStatus status) {
        LogEntry e = new LogEntry();
        e.setLevel(level);
        e.setMessage("msg");
        e.setStatus(status);
        return e;
    }
}
