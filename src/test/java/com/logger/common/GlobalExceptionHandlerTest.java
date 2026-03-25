package com.logger.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logger.destination.DestinationType;
import com.logger.destination.LogDestination;
import com.logger.destination.LogDestinationController;
import com.logger.destination.LogDestinationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LogDestinationController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LogDestinationService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void runtimeException_returns404() throws Exception {
        when(service.findById(99L)).thenThrow(new RuntimeException("LogDestination not found: 99"));

        mockMvc.perform(get("/api/destinations/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("LogDestination not found: 99"));
    }

    @Test
    void validationFailure_returns400() throws Exception {
        LogDestination invalid = new LogDestination();
        invalid.setType(DestinationType.CONSOLE);

        mockMvc.perform(post("/api/destinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
