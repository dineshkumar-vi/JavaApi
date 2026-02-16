package com.example.controllor;

import com.example.data.User;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for LoginApi
 * Tests cover login endpoint, validation, authentication, and edge cases
 */
@ExtendWith(MockitoExtension.class)
class LoginApiTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginApi loginApi;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserName("testuser");
        testUser.setPassword("testpass123");
        testUser.setIpAddress("192.168.1.1");
        testUser.setCaptcha("abc123");
    }

    @AfterEach
    void tearDown() {
        testUser = null;
    }

    /**
     * Test that @RestController annotation is present
     */
    @Test
    void testRestControllerAnnotationPresent() {
        assertTrue(LoginApi.class.isAnnotationPresent(RestController.class));
    }

    /**
     * Test that @RequestMapping annotation is present with correct path
     */
    @Test
    void testRequestMappingAnnotationPresent() {
        assertTrue(LoginApi.class.isAnnotationPresent(RequestMapping.class));
        RequestMapping mapping = LoginApi.class.getAnnotation(RequestMapping.class);
        assertArrayEquals(new String[]{"/login"}, mapping.path());
    }

    /**
     * Test successful login with valid credentials and captcha
     */
    @Test
    void testSuccessfulLoginWithValidCredentialsAndCaptcha() {
        User dbUser = new User();
        dbUser.setUserName("testuser");
        dbUser.setPassword("testpass123");

        when(userService.validateCaptcha("abc123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(dbUser);

        ResponseEntity<String> response = loginApi.create(testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User Validated successfully", response.getBody());
        verify(userService).validateCaptcha("abc123", "192.168.1.1");
        verify(userService).checkUser(any(User.class));
    }

    /**
     * Test login with invalid captcha
     */
    @Test
    void testLoginWithInvalidCaptcha() {
        when(userService.validateCaptcha("abc123", "192.168.1.1")).thenReturn(false);

        ResponseEntity<String> response = loginApi.create(testUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid Captcha , Please enter correct captcha", response.getBody());
        verify(userService).validateCaptcha("abc123", "192.168.1.1");
        verify(userService, never()).checkUser(any());
    }

    /**
     * Test login with valid captcha but invalid credentials
     */
    @Test
    void testLoginWithValidCaptchaButInvalidCredentials() {
        when(userService.validateCaptcha("abc123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(null);

        ResponseEntity<String> response = loginApi.create(testUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid username and password", response.getBody());
        verify(userService).validateCaptcha("abc123", "192.168.1.1");
        verify(userService).checkUser(any(User.class));
    }

    /**
     * Test login with null user
     */
    @Test
    void testLoginWithNullUser() {
        ResponseEntity<String> response = loginApi.create(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).validateCaptcha(anyString(), anyString());
        verify(userService, never()).checkUser(any());
    }

    /**
     * Test login with null username
     */
    @Test
    void testLoginWithNullUsername() {
        testUser.setUserName(null);

        ResponseEntity<String> response = loginApi.create(testUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).validateCaptcha(anyString(), anyString());
        verify(userService, never()).checkUser(any());
    }

    /**
     * Test login with null password
     */
    @Test
    void testLoginWithNullPassword() {
        testUser.setPassword(null);

        ResponseEntity<String> response = loginApi.create(testUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).validateCaptcha(anyString(), anyString());
        verify(userService, never()).checkUser(any());
    }

    /**
     * Test login with empty username
     */
    @Test
    void testLoginWithEmptyUsername() {
        testUser.setUserName("");
        when(userService.validateCaptcha("abc123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(null);

        ResponseEntity<String> response = loginApi.create(testUser);

        // Empty string is not null, so validation proceeds
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService).validateCaptcha("abc123", "192.168.1.1");
    }

    /**
     * Test login with empty password
     */
    @Test
    void testLoginWithEmptyPassword() {
        testUser.setPassword("");
        when(userService.validateCaptcha("abc123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(null);

        ResponseEntity<String> response = loginApi.create(testUser);

        // Empty string is not null, so validation proceeds
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService).validateCaptcha("abc123", "192.168.1.1");
    }

    /**
     * Test login with null captcha
     */
    @Test
    void testLoginWithNullCaptcha() {
        testUser.setCaptcha(null);
        when(userService.validateCaptcha(null, "192.168.1.1")).thenReturn(false);

        ResponseEntity<String> response = loginApi.create(testUser);

        // Captcha validation should fail
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService).validateCaptcha(null, "192.168.1.1");
    }

    /**
     * Test login with null IP address
     */
    @Test
    void testLoginWithNullIpAddress() {
        testUser.setIpAddress(null);
        when(userService.validateCaptcha("abc123", null)).thenReturn(false);

        ResponseEntity<String> response = loginApi.create(testUser);

        // Captcha validation should fail
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService).validateCaptcha("abc123", null);
    }

    /**
     * Test that create method has @PostMapping annotation
     */
    @Test
    void testCreateMethodHasPostMapping() throws NoSuchMethodException {
        var method = LoginApi.class.getDeclaredMethod("create", User.class);
        assertTrue(method.isAnnotationPresent(PostMapping.class));
    }

    /**
     * Test that create method has @CrossOrigin annotation
     */
    @Test
    void testCreateMethodHasCrossOrigin() throws NoSuchMethodException {
        var method = LoginApi.class.getDeclaredMethod("create", User.class);
        assertTrue(method.isAnnotationPresent(CrossOrigin.class));
    }

    /**
     * Test login with special characters in username
     */
    @Test
    void testLoginWithSpecialCharactersInUsername() {
        testUser.setUserName("test@user.com");
        User dbUser = new User();
        dbUser.setUserName("test@user.com");

        when(userService.validateCaptcha("abc123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(dbUser);

        ResponseEntity<String> response = loginApi.create(testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).checkUser(any(User.class));
    }

    /**
     * Test login with special characters in password
     */
    @Test
    void testLoginWithSpecialCharactersInPassword() {
        testUser.setPassword("P@ssw0rd!");
        User dbUser = new User();
        dbUser.setUserName("testuser");

        when(userService.validateCaptcha("abc123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(dbUser);

        ResponseEntity<String> response = loginApi.create(testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).checkUser(any(User.class));
    }

    /**
     * Test that class is public
     */
    @Test
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(LoginApi.class.getModifiers()));
    }

    /**
     * Test package name
     */
    @Test
    void testPackageName() {
        assertEquals("com.example.controllor", LoginApi.class.getPackageName());
    }

    /**
     * Test login with very long username
     */
    @Test
    void testLoginWithLongUsername() {
        String longUsername = "a".repeat(1000);
        testUser.setUserName(longUsername);
        when(userService.validateCaptcha("abc123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(null);

        ResponseEntity<String> response = loginApi.create(testUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService).validateCaptcha("abc123", "192.168.1.1");
    }

    /**
     * Test login with IPv6 address
     */
    @Test
    void testLoginWithIpv6Address() {
        testUser.setIpAddress("2001:0db8:85a3:0000:0000:8a2e:0370:7334");
        User dbUser = new User();
        dbUser.setUserName("testuser");

        when(userService.validateCaptcha(anyString(), anyString())).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(dbUser);

        ResponseEntity<String> response = loginApi.create(testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test successful login response message
     */
    @Test
    void testSuccessfulLoginResponseMessage() {
        User dbUser = new User();
        dbUser.setUserName("testuser");

        when(userService.validateCaptcha("abc123", "192.168.1.1")).thenReturn(true);
        when(userService.checkUser(any(User.class))).thenReturn(dbUser);

        ResponseEntity<String> response = loginApi.create(testUser);

        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Validated successfully"));
    }

    /**
     * Test invalid captcha response message
     */
    @Test
    void testInvalidCaptchaResponseMessage() {
        when(userService.validateCaptcha("abc123", "192.168.1.1")).thenReturn(false);

        ResponseEntity<String> response = loginApi.create(testUser);

        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Invalid Captcha"));
    }
}
