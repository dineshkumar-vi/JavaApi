package com.example.controllor;

import com.example.data.Captcha;
import com.example.service.CaptchaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Comprehensive unit tests for CaptchaApi
 * Tests cover HTTP endpoints, request validation, and response handling
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(CaptchaApi.class)
class CaptchaApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaptchaService captchaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Captcha testCaptcha;

    @BeforeEach
    void setUp() {
        testCaptcha = new Captcha();
        testCaptcha.setId("123");
        testCaptcha.setCaptcha("ABC123");
        testCaptcha.setIpAddress("192.168.1.1");
    }

    /**
     * Test POST /captcha with valid captcha - success
     */
    @Test
    void testCreate_Success() throws Exception {
        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(testCaptcha);

        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCaptcha)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.captcha").value("ABC123"))
                .andExpect(jsonPath("$.ipAddress").value("192.168.1.1"));

        verify(captchaService, times(1)).createCaptcha(any(Captcha.class));
    }

    /**
     * Test POST /captcha with null captcha - bad request
     */
    @Test
    void testCreate_NullCaptcha() throws Exception {
        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());

        verify(captchaService, never()).createCaptcha(any(Captcha.class));
    }

    /**
     * Test POST /captcha with null IP address - bad request
     */
    @Test
    void testCreate_NullIpAddress() throws Exception {
        Captcha captcha = new Captcha();
        captcha.setIpAddress(null);

        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(captcha)))
                .andExpect(status().isBadRequest());

        verify(captchaService, never()).createCaptcha(any(Captcha.class));
    }

    /**
     * Test POST /captcha with empty IP address - bad request
     */
    @Test
    void testCreate_EmptyIpAddress() throws Exception {
        Captcha captcha = new Captcha();
        captcha.setIpAddress("");

        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(testCaptcha);

        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(captcha)))
                .andExpect(status().isOk());
    }

    /**
     * Test POST /captcha with invalid JSON
     */
    @Test
    void testCreate_InvalidJson() throws Exception {
        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test POST /captcha with missing content type
     */
    @Test
    void testCreate_MissingContentType() throws Exception {
        mockMvc.perform(post("/captcha")
                .content(objectMapper.writeValueAsString(testCaptcha)))
                .andExpect(status().isUnsupportedMediaType());
    }

    /**
     * Test POST /captcha returns generated captcha
     */
    @Test
    void testCreate_ReturnsGeneratedCaptcha() throws Exception {
        Captcha generatedCaptcha = new Captcha();
        generatedCaptcha.setCaptcha("XYZ789");
        generatedCaptcha.setIpAddress("192.168.1.1");

        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(generatedCaptcha);

        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCaptcha)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.captcha").value("XYZ789"));
    }

    /**
     * Test GET /captcha with valid parameters - success
     */
    @Test
    void testGet_Success() throws Exception {
        when(captchaService.get("ABC123", "192.168.1.1")).thenReturn(testCaptcha);

        mockMvc.perform(get("/captcha")
                .param("captcha", "ABC123")
                .param("ipAddress", "192.168.1.1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.captcha").value("ABC123"))
                .andExpect(jsonPath("$.ipAddress").value("192.168.1.1"));

        verify(captchaService, times(1)).get("ABC123", "192.168.1.1");
    }

    /**
     * Test GET /captcha with null IP address - bad request
     */
    @Test
    void testGet_NullIpAddress() throws Exception {
        mockMvc.perform(get("/captcha")
                .param("captcha", "ABC123"))
                .andExpect(status().isBadRequest());

        verify(captchaService, never()).get(anyString(), anyString());
    }

    /**
     * Test GET /captcha when captcha not found - bad request
     */
    @Test
    void testGet_NotFound() throws Exception {
        when(captchaService.get("ABC123", "192.168.1.1")).thenReturn(null);

        mockMvc.perform(get("/captcha")
                .param("captcha", "ABC123")
                .param("ipAddress", "192.168.1.1"))
                .andExpect(status().isBadRequest());

        verify(captchaService, times(1)).get("ABC123", "192.168.1.1");
    }

    /**
     * Test GET /captcha without captcha parameter
     */
    @Test
    void testGet_MissingCaptchaParameter() throws Exception {
        mockMvc.perform(get("/captcha")
                .param("ipAddress", "192.168.1.1"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test GET /captcha without any parameters
     */
    @Test
    void testGet_MissingAllParameters() throws Exception {
        mockMvc.perform(get("/captcha"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test GET /captcha with empty captcha parameter
     */
    @Test
    void testGet_EmptyCaptcha() throws Exception {
        when(captchaService.get("", "192.168.1.1")).thenReturn(null);

        mockMvc.perform(get("/captcha")
                .param("captcha", "")
                .param("ipAddress", "192.168.1.1"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test GET /captcha with empty IP address parameter
     */
    @Test
    void testGet_EmptyIpAddress() throws Exception {
        mockMvc.perform(get("/captcha")
                .param("captcha", "ABC123")
                .param("ipAddress", ""))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test GET /captcha with special characters in parameters
     */
    @Test
    void testGet_SpecialCharacters() throws Exception {
        when(captchaService.get("A@B#C$", "192.168.1.1")).thenReturn(testCaptcha);

        mockMvc.perform(get("/captcha")
                .param("captcha", "A@B#C$")
                .param("ipAddress", "192.168.1.1"))
                .andExpect(status().isOk());
    }

    /**
     * Test POST /captcha CORS configuration
     */
    @Test
    void testCreate_CorsEnabled() throws Exception {
        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(testCaptcha);

        mockMvc.perform(post("/captcha")
                .header("Origin", "http://localhost:4200")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCaptcha)))
                .andExpect(status().isOk());
    }

    /**
     * Test POST /captcha with valid IPv6 address
     */
    @Test
    void testCreate_IPv6Address() throws Exception {
        testCaptcha.setIpAddress("2001:0db8:85a3:0000:0000:8a2e:0370:7334");
        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(testCaptcha);

        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCaptcha)))
                .andExpect(status().isOk());
    }

    /**
     * Test GET /captcha with IPv6 address
     */
    @Test
    void testGet_IPv6Address() throws Exception {
        String ipv6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        testCaptcha.setIpAddress(ipv6);
        when(captchaService.get("ABC123", ipv6)).thenReturn(testCaptcha);

        mockMvc.perform(get("/captcha")
                .param("captcha", "ABC123")
                .param("ipAddress", ipv6))
                .andExpect(status().isOk());
    }

    /**
     * Test POST /captcha response contains all fields
     */
    @Test
    void testCreate_ResponseContainsAllFields() throws Exception {
        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(testCaptcha);

        mockMvc.perform(post("/captcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCaptcha)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.captcha").value("ABC123"))
                .andExpect(jsonPath("$.ipAddress").value("192.168.1.1"));
    }

    /**
     * Test GET /captcha response contains all fields
     */
    @Test
    void testGet_ResponseContainsAllFields() throws Exception {
        when(captchaService.get("ABC123", "192.168.1.1")).thenReturn(testCaptcha);

        mockMvc.perform(get("/captcha")
                .param("captcha", "ABC123")
                .param("ipAddress", "192.168.1.1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.captcha").value("ABC123"))
                .andExpect(jsonPath("$.ipAddress").value("192.168.1.1"));
    }
}
