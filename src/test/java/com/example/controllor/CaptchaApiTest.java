package com.example.controllor;

import com.example.data.Captcha;
import com.example.service.CaptchaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for CaptchaApi
 * Tests REST API endpoints for captcha creation and retrieval
 */
@WebMvcTest(CaptchaApi.class)
class CaptchaApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaptchaService captchaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Captcha testCaptcha;
    private Captcha responseCaptcha;

    @BeforeEach
    void setUp() {
        testCaptcha = new Captcha();
        testCaptcha.setIpAddress("192.168.1.1");

        responseCaptcha = new Captcha();
        responseCaptcha.setId("123");
        responseCaptcha.setCaptcha("ABC123");
        responseCaptcha.setIpAddress("192.168.1.1");
    }

    /**
     * Test creating captcha successfully
     */
    @Test
    void testCreate_Success() throws Exception {
        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(responseCaptcha);

        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCaptcha)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.captcha").value("ABC123"))
                .andExpect(jsonPath("$.ipAddress").value("192.168.1.1"));

        verify(captchaService, times(1)).createCaptcha(any(Captcha.class));
    }

    /**
     * Test creating captcha with null request body
     */
    @Test
    void testCreate_NullRequestBody() throws Exception {
        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());

        verify(captchaService, never()).createCaptcha(any(Captcha.class));
    }

    /**
     * Test creating captcha with null IP address
     */
    @Test
    void testCreate_NullIpAddress() throws Exception {
        Captcha captchaWithNullIp = new Captcha();
        captchaWithNullIp.setIpAddress(null);

        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(captchaWithNullIp)))
                .andExpect(status().isBadRequest());

        verify(captchaService, never()).createCaptcha(any(Captcha.class));
    }

    /**
     * Test creating captcha with empty IP address
     */
    @Test
    void testCreate_EmptyIpAddress() throws Exception {
        Captcha captchaWithEmptyIp = new Captcha();
        captchaWithEmptyIp.setIpAddress("");

        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(responseCaptcha);

        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(captchaWithEmptyIp)))
                .andExpect(status().isOk());
    }

    /**
     * Test creating captcha when service returns null
     */
    @Test
    void testCreate_ServiceReturnsNull() throws Exception {
        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(null);

        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCaptcha)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(captchaService, times(1)).createCaptcha(any(Captcha.class));
    }

    /**
     * Test getting captcha successfully
     */
    @Test
    void testGet_Success() throws Exception {
        when(captchaService.get("ABC123", "192.168.1.1")).thenReturn(responseCaptcha);

        mockMvc.perform(get("/captcha")
                .param("captcha", "ABC123")
                .param("ipAddress", "192.168.1.1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.captcha").value("ABC123"))
                .andExpect(jsonPath("$.ipAddress").value("192.168.1.1"));

        verify(captchaService, times(1)).get("ABC123", "192.168.1.1");
    }

    /**
     * Test getting captcha with null IP address
     */
    @Test
    void testGet_NullIpAddress() throws Exception {
        mockMvc.perform(get("/captcha")
                .param("captcha", "ABC123"))
                .andExpect(status().isBadRequest());

        verify(captchaService, never()).get(anyString(), anyString());
    }

    /**
     * Test getting captcha when not found
     */
    @Test
    void testGet_NotFound() throws Exception {
        when(captchaService.get("INVALID", "192.168.1.1")).thenReturn(null);

        mockMvc.perform(get("/captcha")
                .param("captcha", "INVALID")
                .param("ipAddress", "192.168.1.1"))
                .andExpect(status().isBadRequest());

        verify(captchaService, times(1)).get("INVALID", "192.168.1.1");
    }

    /**
     * Test getting captcha with empty captcha parameter
     */
    @Test
    void testGet_EmptyCaptcha() throws Exception {
        when(captchaService.get("", "192.168.1.1")).thenReturn(null);

        mockMvc.perform(get("/captcha")
                .param("captcha", "")
                .param("ipAddress", "192.168.1.1"))
                .andExpect(status().isBadRequest());

        verify(captchaService, times(1)).get("", "192.168.1.1");
    }

    /**
     * Test getting captcha with empty IP address
     */
    @Test
    void testGet_EmptyIpAddress() throws Exception {
        mockMvc.perform(get("/captcha")
                .param("captcha", "ABC123")
                .param("ipAddress", ""))
                .andExpect(status().isBadRequest());

        verify(captchaService, never()).get(anyString(), anyString());
    }

    /**
     * Test CORS configuration on create endpoint
     */
    @Test
    void testCreate_CorsEnabled() throws Exception {
        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(responseCaptcha);

        mockMvc.perform(post("/captcha")
                .header("Origin", "http://localhost:4200")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCaptcha)))
                .andExpect(status().isOk());
    }

    /**
     * Test creating captcha with malformed JSON
     */
    @Test
    void testCreate_MalformedJson() throws Exception {
        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                .andExpect(status().isBadRequest());

        verify(captchaService, never()).createCaptcha(any(Captcha.class));
    }

    /**
     * Test getting captcha missing both parameters
     */
    @Test
    void testGet_MissingParameters() throws Exception {
        mockMvc.perform(get("/captcha"))
                .andExpect(status().isBadRequest());

        verify(captchaService, never()).get(anyString(), anyString());
    }

    /**
     * Test creating captcha with valid IP address formats
     */
    @Test
    void testCreate_VariousIpAddressFormats() throws Exception {
        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(responseCaptcha);

        // Test IPv4
        testCaptcha.setIpAddress("10.0.0.1");
        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCaptcha)))
                .andExpect(status().isOk());

        // Test localhost
        testCaptcha.setIpAddress("127.0.0.1");
        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCaptcha)))
                .andExpect(status().isOk());
    }

    /**
     * Test that controller is properly mapped to /captcha path
     */
    @Test
    void testControllerRequestMapping() throws Exception {
        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(responseCaptcha);

        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCaptcha)))
                .andExpect(status().isOk());
    }

    /**
     * Test getting captcha with special characters in captcha
     */
    @Test
    void testGet_SpecialCharactersInCaptcha() throws Exception {
        when(captchaService.get("ABC@123", "192.168.1.1")).thenReturn(null);

        mockMvc.perform(get("/captcha")
                .param("captcha", "ABC@123")
                .param("ipAddress", "192.168.1.1"))
                .andExpect(status().isBadRequest());
    }
}
