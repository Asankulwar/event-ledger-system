package com.eventledger.accountservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AccountServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/health"))
               .andExpect(status().isOk());
    }

    @Test
    void testApplyTransaction() throws Exception {
        String txnJson = """
            {
              "amount": 200.0,
              "type": "CREDIT"
            }
        """;

        mockMvc.perform(post("/accounts/acc-001/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(txnJson))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.balance").value(200.0));
    }

    @Test
    void testGetBalance() throws Exception {
        mockMvc.perform(get("/accounts/acc-001/balance"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.balance").exists());
    }
}
