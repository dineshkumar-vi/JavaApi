package com.example.serviceimpl;

import com.example.data.Captcha;
import com.example.data.User;
import com.example.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class for UserServiceImpl
 * Tests user authentication and captcha validation functionality
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userService;

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
     * Test checking user successfully
     */
    @Test
    void testCheckUser_Success() {
        when(userRepo.findByUserNameAndPassword("testuser", "password123")).thenReturn(dbUser);

        User result = userService.checkUser(testUser);

        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
        assertEquals("password123", result.getPassword());
        verify(userRepo, times(1)).findByUserNameAndPassword("testuser", "password123");
    }

    /**
     * Test checking user when user not found
     */
    @Test
    void testCheckUser_NotFound() {
        when(userRepo.findByUserNameAndPassword("testuser", "password123")).thenReturn(null);

        User result = userService.checkUser(testUser);

        assertNull(result);
        verify(userRepo, times(1)).findByUserNameAndPassword("testuser", "password123");
    }

    /**
     * Test checking user when username is null in database result
     */
    @Test
    void testCheckUser_NullUsername() {
        User nullUsernameUser = new User();
        nullUsernameUser.setUserName(null);
        nullUsernameUser.setPassword("password123");

        when(userRepo.findByUserNameAndPassword("testuser", "password123")).thenReturn(nullUsernameUser);

        User result = userService.checkUser(testUser);

        assertNull(result);
    }

    /**
     * Test checking user when repository throws exception
     */
    @Test
    void testCheckUser_RepositoryException() {
        when(userRepo.findByUserNameAndPassword(anyString(), anyString()))
            .thenThrow(new RuntimeException("Database error"));

        User result = userService.checkUser(testUser);

        assertNull(result);
        verify(userRepo, times(1)).findByUserNameAndPassword("testuser", "password123");
    }

    /**
     * Test checking user with null input
     */
    @Test
    void testCheckUser_NullInput() {
        assertThrows(NullPointerException.class, () -> {
            userService.checkUser(null);
        });
    }

    /**
     * Test checking user with wrong password
     */
    @Test
    void testCheckUser_WrongPassword() {
        when(userRepo.findByUserNameAndPassword("testuser", "wrongpassword")).thenReturn(null);

        User wrongPasswordUser = new User();
        wrongPasswordUser.setUserName("testuser");
        wrongPasswordUser.setPassword("wrongpassword");

        User result = userService.checkUser(wrongPasswordUser);

        assertNull(result);
    }

    /**
     * Test checking user with wrong username
     */
    @Test
    void testCheckUser_WrongUsername() {
        when(userRepo.findByUserNameAndPassword("wronguser", "password123")).thenReturn(null);

        User wrongUsernameUser = new User();
        wrongUsernameUser.setUserName("wronguser");
        wrongUsernameUser.setPassword("password123");

        User result = userService.checkUser(wrongUsernameUser);

        assertNull(result);
    }

    /**
     * Test checking user with empty username
     */
    @Test
    void testCheckUser_EmptyUsername() {
        when(userRepo.findByUserNameAndPassword("", "password123")).thenReturn(null);

        User emptyUsernameUser = new User();
        emptyUsernameUser.setUserName("");
        emptyUsernameUser.setPassword("password123");

        User result = userService.checkUser(emptyUsernameUser);

        assertNull(result);
    }

    /**
     * Test checking user with empty password
     */
    @Test
    void testCheckUser_EmptyPassword() {
        when(userRepo.findByUserNameAndPassword("testuser", "")).thenReturn(null);

        User emptyPasswordUser = new User();
        emptyPasswordUser.setUserName("testuser");
        emptyPasswordUser.setPassword("");

        User result = userService.checkUser(emptyPasswordUser);

        assertNull(result);
    }

    /**
     * Test that validateCaptcha returns false
     * Note: This method makes HTTP calls which are difficult to mock without additional setup
     * In a real scenario, you'd want to refactor to inject HttpClient for better testability
     */
    @Test
    void testValidateCaptcha_Integration() {
        // This is an integration test that would require a running server
        // For unit testing, we're verifying the method exists and handles errors
        boolean result = userService.validateCaptcha("ABC123", "192.168.1.1");

        // Without a running server, this will return false
        assertFalse(result);
    }

    /**
     * Test validateCaptcha with null captcha
     */
    @Test
    void testValidateCaptcha_NullCaptcha() {
        boolean result = userService.validateCaptcha(null, "192.168.1.1");

        assertFalse(result);
    }

    /**
     * Test validateCaptcha with null IP address
     */
    @Test
    void testValidateCaptcha_NullIpAddress() {
        boolean result = userService.validateCaptcha("ABC123", null);

        assertFalse(result);
    }

    /**
     * Test validateCaptcha with empty captcha
     */
    @Test
    void testValidateCaptcha_EmptyCaptcha() {
        boolean result = userService.validateCaptcha("", "192.168.1.1");

        assertFalse(result);
    }

    /**
     * Test validateCaptcha with empty IP address
     */
    @Test
    void testValidateCaptcha_EmptyIpAddress() {
        boolean result = userService.validateCaptcha("ABC123", "");

        assertFalse(result);
    }

    /**
     * Test that UserServiceImpl is annotated as Component
     */
    @Test
    void testComponentAnnotation() {
        assertTrue(UserServiceImpl.class.isAnnotationPresent(org.springframework.stereotype.Component.class));
    }

    /**
     * Test that UserServiceImpl implements UserService
     */
    @Test
    void testImplementsUserService() {
        assertTrue(com.example.service.UserService.class.isAssignableFrom(UserServiceImpl.class));
    }

    /**
     * Test user validation workflow
     */
    @Test
    void testUserValidationWorkflow() {
        // This test validates the typical workflow
        when(userRepo.findByUserNameAndPassword("testuser", "password123")).thenReturn(dbUser);

        User result = userService.checkUser(testUser);

        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
    }

    /**
     * Test that checkUser handles database timeout gracefully
     */
    @Test
    void testCheckUser_DatabaseTimeout() {
        when(userRepo.findByUserNameAndPassword(anyString(), anyString()))
            .thenThrow(new RuntimeException("Connection timeout"));

        User result = userService.checkUser(testUser);

        assertNull(result);
    }
}
