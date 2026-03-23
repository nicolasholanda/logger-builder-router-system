package com.logger.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
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

@WebMvcTest(LogRouteController.class)
class LogRouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LogRouteService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAll_returns200() throws Exception {
        when(service.findAll()).thenReturn(List.of(route()));

        mockMvc.perform(get("/api/routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Default Route"));
    }

    @Test
    void getById_returns200() throws Exception {
        when(service.findById(1L)).thenReturn(route());

        mockMvc.perform(get("/api/routes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Default Route"));
    }

    @Test
    void create_returns201() throws Exception {
        LogRoute r = route();
        when(service.create(any())).thenReturn(r);

        mockMvc.perform(post("/api/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Default Route"));
    }

    @Test
    void update_returns200() throws Exception {
        LogRoute r = route();
        when(service.update(eq(1L), any())).thenReturn(r);

        mockMvc.perform(put("/api/routes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Default Route"));
    }

    @Test
    void delete_returns204() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/routes/1"))
                .andExpect(status().isNoContent());
    }

    private LogRoute route() {
        LogRoute r = new LogRoute();
        r.setName("Default Route");
        r.setLevels(Set.of(LogLevel.INFO));
        r.setDestinations(Set.of());
        return r;
    }
}
