package com.example.serviceimpl;

import com.example.data.Captcha;
import com.example.data.User;
import com.example.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for UserServiceImpl
 * Tests cover user authentication and captcha validation
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserName("testuser");
        testUser.setPassword("password123");
        testUser.setIpAddress("192.168.1.1");
        testUser.setCaptcha("ABC123");
    }

    @AfterEach
    void tearDown() {
        reset(userRepo);
    }

    /**
     * Test check user with valid credentials - success
     */
    @Test
    void testCheckUser_Success() {
        User dbUser = new User();
        dbUser.setUserName("testuser");
        dbUser.setPassword("password123");

        when(userRepo.findByUserNameAndPassword("testuser", "password123"))
            .thenReturn(dbUser);

        User result = userService.checkUser(testUser);

        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
        verify(userRepo, times(1)).findByUserNameAndPassword("testuser", "password123");
    }

    /**
     * Test check user with invalid credentials - not found
     */
    @Test
    void testCheckUser_NotFound() {
        when(userRepo.findByUserNameAndPassword(anyString(), anyString()))
            .thenReturn(null);

        User result = userService.checkUser(testUser);

        assertNull(result);
    }

    /**
     * Test check user when username doesn't exist
     */
    @Test
    void testCheckUser_InvalidUsername() {
        when(userRepo.findByUserNameAndPassword("wronguser", "password123"))
            .thenReturn(null);

        User wrongUser = new User();
        wrongUser.setUserName("wronguser");
        wrongUser.setPassword("password123");

        User result = userService.checkUser(wrongUser);

        assertNull(result);
    }

    /**
     * Test check user when password is incorrect
     */
    @Test
    void testCheckUser_InvalidPassword() {
        when(userRepo.findByUserNameAndPassword("testuser", "wrongpassword"))
            .thenReturn(null);

        User wrongPasswordUser = new User();
        wrongPasswordUser.setUserName("testuser");
        wrongPasswordUser.setPassword("wrongpassword");

        User result = userService.checkUser(wrongPasswordUser);

        assertNull(result);
    }

    /**
     * Test check user when repository throws exception
     */
    @Test
    void testCheckUser_RepositoryException() {
        when(userRepo.findByUserNameAndPassword(anyString(), anyString()))
            .thenThrow(new RuntimeException("Database error"));

        User result = userService.checkUser(testUser);

        assertNull(result);
    }

    /**
     * Test check user when user from DB has null username
     */
    @Test
    void testCheckUser_DbUserWithNullUsername() {
        User dbUser = new User();
        dbUser.setUserName(null);

        when(userRepo.findByUserNameAndPassword("testuser", "password123"))
            .thenReturn(dbUser);

        User result = userService.checkUser(testUser);

        assertNull(result);
    }

    /**
     * Test check user with null input user
     */
    @Test
    void testCheckUser_NullInput() {
        assertThrows(NullPointerException.class, () -> {
            userService.checkUser(null);
        });
    }

    /**
     * Test check user with null username
     */
    @Test
    void testCheckUser_NullUsername() {
        testUser.setUserName(null);

        when(userRepo.findByUserNameAndPassword(null, "password123"))
            .thenReturn(null);

        User result = userService.checkUser(testUser);

        assertNull(result);
    }

    /**
     * Test check user with null password
     */
    @Test
    void testCheckUser_NullPassword() {
        testUser.setPassword(null);

        when(userRepo.findByUserNameAndPassword("testuser", null))
            .thenReturn(null);

        User result = userService.checkUser(testUser);

        assertNull(result);
    }

    /**
     * Test check user with empty username
     */
    @Test
    void testCheckUser_EmptyUsername() {
        testUser.setUserName("");

        when(userRepo.findByUserNameAndPassword("", "password123"))
            .thenReturn(null);

        User result = userService.checkUser(testUser);

        assertNull(result);
    }

    /**
     * Test check user with empty password
     */
    @Test
    void testCheckUser_EmptyPassword() {
        testUser.setPassword("");

        when(userRepo.findByUserNameAndPassword("testuser", ""))
            .thenReturn(null);

        User result = userService.checkUser(testUser);

        assertNull(result);
    }

    /**
     * Test check user with special characters in username
     */
    @Test
    void testCheckUser_SpecialCharactersUsername() {
        testUser.setUserName("test@user.com");

        User dbUser = new User();
        dbUser.setUserName("test@user.com");
        dbUser.setPassword("password123");

        when(userRepo.findByUserNameAndPassword("test@user.com", "password123"))
            .thenReturn(dbUser);

        User result = userService.checkUser(testUser);

        assertNotNull(result);
        assertEquals("test@user.com", result.getUserName());
    }

    /**
     * Test check user with special characters in password
     */
    @Test
    void testCheckUser_SpecialCharactersPassword() {
        testUser.setPassword("P@ssw0rd!#$");

        User dbUser = new User();
        dbUser.setUserName("testuser");
        dbUser.setPassword("P@ssw0rd!#$");

        when(userRepo.findByUserNameAndPassword("testuser", "P@ssw0rd!#$"))
            .thenReturn(dbUser);

        User result = userService.checkUser(testUser);

        assertNotNull(result);
        assertEquals("P@ssw0rd!#$", result.getPassword());
    }

    /**
     * Test check user verifies case sensitivity
     */
    @Test
    void testCheckUser_CaseSensitive() {
        when(userRepo.findByUserNameAndPassword("TESTUSER", "password123"))
            .thenReturn(null);

        User upperCaseUser = new User();
        upperCaseUser.setUserName("TESTUSER");
        upperCaseUser.setPassword("password123");

        User result = userService.checkUser(upperCaseUser);

        assertNull(result);
    }

    /**
     * Test check user with whitespace in username
     */
    @Test
    void testCheckUser_WhitespaceUsername() {
        testUser.setUserName(" testuser ");

        when(userRepo.findByUserNameAndPassword(" testuser ", "password123"))
            .thenReturn(null);

        User result = userService.checkUser(testUser);

        assertNull(result);
    }

    /**
     * Test check user with very long username
     */
    @Test
    void testCheckUser_LongUsername() {
        String longUsername = "a".repeat(1000);
        testUser.setUserName(longUsername);

        when(userRepo.findByUserNameAndPassword(longUsername, "password123"))
            .thenReturn(null);

        User result = userService.checkUser(testUser);

        assertNull(result);
    }

    /**
     * Test check user with very long password
     */
    @Test
    void testCheckUser_LongPassword() {
        String longPassword = "p".repeat(1000);
        testUser.setPassword(longPassword);

        when(userRepo.findByUserNameAndPassword("testuser", longPassword))
            .thenReturn(null);

        User result = userService.checkUser(testUser);

        assertNull(result);
    }

    /**
     * Test validate captcha method exists and returns boolean
     * Note: This method makes HTTP calls which are hard to test without integration tests
     */
    @Test
    void testValidateCaptcha_MethodExists() {
        // Verify the method signature exists
        assertDoesNotThrow(() -> {
            userService.validateCaptcha("ABC123", "192.168.1.1");
        });
    }

    /**
     * Test validate captcha with null captcha parameter
     */
    @Test
    void testValidateCaptcha_NullCaptcha() {
        boolean result = userService.validateCaptcha(null, "192.168.1.1");
        assertFalse(result);
    }

    /**
     * Test validate captcha with null IP address parameter
     */
    @Test
    void testValidateCaptcha_NullIpAddress() {
        boolean result = userService.validateCaptcha("ABC123", null);
        assertFalse(result);
    }

    /**
     * Test validate captcha with empty captcha string
     */
    @Test
    void testValidateCaptcha_EmptyCaptcha() {
        boolean result = userService.validateCaptcha("", "192.168.1.1");
        assertFalse(result);
    }

    /**
     * Test validate captcha with empty IP address string
     */
    @Test
    void testValidateCaptcha_EmptyIpAddress() {
        boolean result = userService.validateCaptcha("ABC123", "");
        assertFalse(result);
    }
}
