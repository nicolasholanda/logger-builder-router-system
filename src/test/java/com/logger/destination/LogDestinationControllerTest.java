package com.logger.destination;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LogDestinationController.class)
class LogDestinationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LogDestinationService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAll_returns200() throws Exception {
        when(service.findAll()).thenReturn(List.of(destination()));

        mockMvc.perform(get("/api/destinations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Console"));
    }

    @Test
    void getById_returns200() throws Exception {
        when(service.findById(1L)).thenReturn(destination());

        mockMvc.perform(get("/api/destinations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("CONSOLE"));
    }

    @Test
    void create_returns201() throws Exception {
        LogDestination d = destination();
        when(service.create(any())).thenReturn(d);

        mockMvc.perform(post("/api/destinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(d)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Console"));
    }

    @Test
    void update_returns200() throws Exception {
        LogDestination d = destination();
        when(service.update(eq(1L), any())).thenReturn(d);

        mockMvc.perform(put("/api/destinations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(d)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void delete_returns204() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/destinations/1"))
                .andExpect(status().isNoContent());
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
