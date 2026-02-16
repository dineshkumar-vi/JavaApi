package com.example.controllor;

import com.example.data.User;
import com.example.service.UserService;
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
 * Comprehensive unit tests for LoginApi
 * Tests cover login endpoint, validation, and authentication flow
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginApi.class)
class LoginApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserName("testuser");
        testUser.setPassword("password123");
        testUser.setIpAddress("192.168.1.1");
        testUser.setCaptcha("ABC123");
    }

    /**
     * Test POST /login with valid credentials and captcha - success
     */
    @Test
    void testLogin_Success() throws Exception {
        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Validated successfully"));

        verify(userService, times(1)).validateCaptcha("ABC123", "192.168.1.1");
        verify(userService, times(1)).checkUser(any(User.class));
    }

    /**
     * Test POST /login with invalid captcha
     */
    @Test
    void testLogin_InvalidCaptcha() throws Exception {
        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(false);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Captcha , Please enter correct captcha"));

        verify(userService, times(1)).validateCaptcha("ABC123", "192.168.1.1");
        verify(userService, never()).checkUser(any(User.class));
    }

    /**
     * Test POST /login with valid captcha but invalid credentials
     */
    @Test
    void testLogin_InvalidCredentials() throws Exception {
        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(null);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid username and password"));

        verify(userService, times(1)).validateCaptcha("ABC123", "192.168.1.1");
        verify(userService, times(1)).checkUser(any(User.class));
    }

    /**
     * Test POST /login with null user - bad request
     */
    @Test
    void testLogin_NullUser() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).validateCaptcha(anyString(), anyString());
        verify(userService, never()).checkUser(any(User.class));
    }

    /**
     * Test POST /login with null username - bad request
     */
    @Test
    void testLogin_NullUsername() throws Exception {
        testUser.setUserName(null);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).validateCaptcha(anyString(), anyString());
    }

    /**
     * Test POST /login with null password - bad request
     */
    @Test
    void testLogin_NullPassword() throws Exception {
        testUser.setPassword(null);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).validateCaptcha(anyString(), anyString());
    }

    /**
     * Test POST /login with empty username - bad request
     */
    @Test
    void testLogin_EmptyUsername() throws Exception {
        testUser.setUserName("");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test POST /login with empty password - bad request
     */
    @Test
    void testLogin_EmptyPassword() throws Exception {
        testUser.setPassword("");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test POST /login with missing captcha field
     */
    @Test
    void testLogin_MissingCaptcha() throws Exception {
        testUser.setCaptcha(null);

        when(userService.validateCaptcha(null, "192.168.1.1")).thenReturn(false);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Captcha , Please enter correct captcha"));
    }

    /**
     * Test POST /login with missing IP address field
     */
    @Test
    void testLogin_MissingIpAddress() throws Exception {
        testUser.setIpAddress(null);

        when(userService.validateCaptcha("ABC123", null)).thenReturn(false);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test POST /login with invalid JSON
     */
    @Test
    void testLogin_InvalidJson() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test POST /login with missing content type
     */
    @Test
    void testLogin_MissingContentType() throws Exception {
        mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isUnsupportedMediaType());
    }

    /**
     * Test POST /login CORS configuration
     */
    @Test
    void testLogin_CorsEnabled() throws Exception {
        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/login")
                .header("Origin", "http://localhost:4200")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());
    }

    /**
     * Test POST /login with special characters in username
     */
    @Test
    void testLogin_SpecialCharactersUsername() throws Exception {
        testUser.setUserName("test@user.com");

        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());
    }

    /**
     * Test POST /login with special characters in password
     */
    @Test
    void testLogin_SpecialCharactersPassword() throws Exception {
        testUser.setPassword("P@ssw0rd!#$");

        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());
    }

    /**
     * Test POST /login with IPv6 address
     */
    @Test
    void testLogin_IPv6Address() throws Exception {
        String ipv6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        testUser.setIpAddress(ipv6);

        when(userService.validateCaptcha("ABC123", ipv6)).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());
    }

    /**
     * Test POST /login validates captcha before checking user
     */
    @Test
    void testLogin_CaptchaValidatedFirst() throws Exception {
        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(false);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).validateCaptcha("ABC123", "192.168.1.1");
        verify(userService, never()).checkUser(any(User.class));
    }

    /**
     * Test POST /login with very long username
     */
    @Test
    void testLogin_LongUsername() throws Exception {
        testUser.setUserName("a".repeat(1000));

        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());
    }

    /**
     * Test POST /login with very long password
     */
    @Test
    void testLogin_LongPassword() throws Exception {
        testUser.setPassword("p".repeat(1000));

        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());
    }

    /**
     * Test POST /login response content type
     */
    @Test
    void testLogin_ResponseContentType() throws Exception {
        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    /**
     * Test POST /login with whitespace in credentials
     */
    @Test
    void testLogin_WhitespaceInCredentials() throws Exception {
        testUser.setUserName(" testuser ");
        testUser.setPassword(" password123 ");

        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());
    }
}
