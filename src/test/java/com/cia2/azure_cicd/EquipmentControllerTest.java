
package com.cia2.azure_cicd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHomeEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetAllEquipment() throws Exception {
        mockMvc.perform(get("/api/equipment"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Laptop")))
                .andExpect(content().string(containsString("Projector")))
                .andExpect(content().string(containsString("Tablet")));
    }

    @Test
    void testVersionEndpoint() throws Exception {
        mockMvc.perform(get("/api/version"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1.0")));
    }

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
