package com.example.controllor;

import com.example.data.User;
import com.example.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for LoginApi
 * Tests REST API endpoints for user login and authentication
 */
@WebMvcTest(LoginApi.class)
class LoginApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private User dbUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserName("testuser");
        testUser.setPassword("password123");
        testUser.setIpAddress("192.168.1.1");
        testUser.setCaptcha("ABC123");

        dbUser = new User();
        dbUser.setUserName("testuser");
        dbUser.setPassword("password123");
    }

    /**
     * Test successful login with valid credentials and captcha
     */
    @Test
    void testCreate_SuccessfulLogin() throws Exception {
        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(dbUser);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Validated successfully"));

        verify(userService, times(1)).validateCaptcha("ABC123", "192.168.1.1");
        verify(userService, times(1)).checkUser(any(User.class));
    }

    /**
     * Test login with invalid captcha
     */
    @Test
    void testCreate_InvalidCaptcha() throws Exception {
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
     * Test login with invalid username and password
     */
    @Test
    void testCreate_InvalidCredentials() throws Exception {
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
     * Test login with null request body
     */
    @Test
    void testCreate_NullRequestBody() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).validateCaptcha(anyString(), anyString());
        verify(userService, never()).checkUser(any(User.class));
    }

    /**
     * Test login with null username
     */
    @Test
    void testCreate_NullUsername() throws Exception {
        testUser.setUserName(null);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).validateCaptcha(anyString(), anyString());
        verify(userService, never()).checkUser(any(User.class));
    }

    /**
     * Test login with null password
     */
    @Test
    void testCreate_NullPassword() throws Exception {
        testUser.setPassword(null);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).validateCaptcha(anyString(), anyString());
        verify(userService, never()).checkUser(any(User.class));
    }

    /**
     * Test login with empty username
     */
    @Test
    void testCreate_EmptyUsername() throws Exception {
        testUser.setUserName("");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).validateCaptcha(anyString(), anyString());
        verify(userService, never()).checkUser(any(User.class));
    }

    /**
     * Test login with empty password
     */
    @Test
    void testCreate_EmptyPassword() throws Exception {
        testUser.setPassword("");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).validateCaptcha(anyString(), anyString());
        verify(userService, never()).checkUser(any(User.class));
    }

    /**
     * Test login with valid captcha but user not found
     */
    @Test
    void testCreate_ValidCaptchaUserNotFound() throws Exception {
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
     * Test CORS configuration
     */
    @Test
    void testCreate_CorsEnabled() throws Exception {
        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(dbUser);

        mockMvc.perform(post("/login")
                .header("Origin", "http://localhost:4200")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());
    }

    /**
     * Test login with malformed JSON
     */
    @Test
    void testCreate_MalformedJson() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).validateCaptcha(anyString(), anyString());
        verify(userService, never()).checkUser(any(User.class));
    }

    /**
     * Test login workflow with all validations passing
     */
    @Test
    void testCreate_CompleteWorkflow() throws Exception {
        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(dbUser);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("User Validated successfully"));

        verify(userService, times(1)).validateCaptcha("ABC123", "192.168.1.1");
        verify(userService, times(1)).checkUser(any(User.class));
    }

    /**
     * Test login with missing captcha
     */
    @Test
    void testCreate_MissingCaptcha() throws Exception {
        testUser.setCaptcha(null);

        when(userService.validateCaptcha(null, "192.168.1.1")).thenReturn(false);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Captcha , Please enter correct captcha"));
    }

    /**
     * Test login with missing IP address
     */
    @Test
    void testCreate_MissingIpAddress() throws Exception {
        testUser.setIpAddress(null);

        when(userService.validateCaptcha("ABC123", null)).thenReturn(false);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Captcha , Please enter correct captcha"));
    }

    /**
     * Test that controller is properly mapped to /login path
     */
    @Test
    void testControllerRequestMapping() throws Exception {
        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(dbUser);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());
    }

    /**
     * Test login with wrong credentials after valid captcha
     */
    @Test
    void testCreate_WrongPasswordValidCaptcha() throws Exception {
        when(userService.validateCaptcha("ABC123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(null);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid username and password"));
    }
}
