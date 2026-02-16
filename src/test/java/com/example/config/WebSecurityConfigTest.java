package com.example.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoginEndpointIsAccessible() throws Exception {
        mockMvc.perform(post("/login")
                .contentType("application/json")
                .content("{\"userName\":\"test\",\"password\":\"test\"}"))
                .andExpect(status().isBadRequest()); // Bad request due to missing captcha, but accessible
    }

    @Test
    public void testCaptchaEndpointIsAccessible() throws Exception {
        mockMvc.perform(get("/captcha"))
                .andExpect(status().isNotFound()); // 404 if endpoint doesn't exist, but accessible
    }

    @Test
    public void testPricesEndpointIsAccessible() throws Exception {
        mockMvc.perform(get("/prices"))
                .andExpect(status().isNotFound()); // 404 if endpoint doesn't exist, but accessible
    }

    @Test
    public void testCorsConfiguration() throws Exception {
        mockMvc.perform(options("/login")
                .header("Origin", "http://localhost:4200")
                .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }
}
