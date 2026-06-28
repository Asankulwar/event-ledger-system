package com.eventledger.eventgateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EventGatewayIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/events/health"))
               .andExpect(status().isOk())
               .andExpect(content().string("OK"));
    }

    @Test
    void testSubmitEvent() throws Exception {
        String eventJson = """
            {
              "id": "evt-123",
              "accountId": "acc-001",
              "amount": 100.0,
              "eventTimestamp": "2026-06-28T23:59:00"
            }
        """;

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value("evt-123"))
               .andExpect(jsonPath("$.accountId").value("acc-001"))
               .andExpect(jsonPath("$.amount").value(100.0));
    }
}
