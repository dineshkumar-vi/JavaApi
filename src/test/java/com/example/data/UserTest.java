package com.example.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for User data model
 * Tests cover getters, setters, and data integrity
 */
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    /**
     * Test User class can be instantiated
     */
    @Test
    void testUserInstantiation() {
        assertNotNull(user);
    }

    /**
     * Test setting and getting username
     */
    @Test
    void testSetAndGetUserName() {
        user.setUserName("testuser");
        assertEquals("testuser", user.getUserName());
    }

    /**
     * Test setting and getting password
     */
    @Test
    void testSetAndGetPassword() {
        user.setPassword("password123");
        assertEquals("password123", user.getPassword());
    }

    /**
     * Test setting and getting IP address
     */
    @Test
    void testSetAndGetIpAddress() {
        user.setIpAddress("192.168.1.1");
        assertEquals("192.168.1.1", user.getIpAddress());
    }

    /**
     * Test setting and getting captcha
     */
    @Test
    void testSetAndGetCaptcha() {
        user.setCaptcha("ABC123");
        assertEquals("ABC123", user.getCaptcha());
    }

    /**
     * Test all fields initially null
     */
    @Test
    void testAllFieldsInitiallyNull() {
        User newUser = new User();
        assertNull(newUser.getUserName());
        assertNull(newUser.getPassword());
        assertNull(newUser.getIpAddress());
        assertNull(newUser.getCaptcha());
    }

    /**
     * Test setting username to null
     */
    @Test
    void testSetUserNameToNull() {
        user.setUserName("testuser");
        user.setUserName(null);
        assertNull(user.getUserName());
    }

    /**
     * Test setting password to null
     */
    @Test
    void testSetPasswordToNull() {
        user.setPassword("password123");
        user.setPassword(null);
        assertNull(user.getPassword());
    }

    /**
     * Test setting IP address to null
     */
    @Test
    void testSetIpAddressToNull() {
        user.setIpAddress("192.168.1.1");
        user.setIpAddress(null);
        assertNull(user.getIpAddress());
    }

    /**
     * Test setting captcha to null
     */
    @Test
    void testSetCaptchaToNull() {
        user.setCaptcha("ABC123");
        user.setCaptcha(null);
        assertNull(user.getCaptcha());
    }

    /**
     * Test setting empty username
     */
    @Test
    void testSetEmptyUserName() {
        user.setUserName("");
        assertEquals("", user.getUserName());
    }

    /**
     * Test setting empty password
     */
    @Test
    void testSetEmptyPassword() {
        user.setPassword("");
        assertEquals("", user.getPassword());
    }

    /**
     * Test setting empty IP address
     */
    @Test
    void testSetEmptyIpAddress() {
        user.setIpAddress("");
        assertEquals("", user.getIpAddress());
    }

    /**
     * Test setting empty captcha
     */
    @Test
    void testSetEmptyCaptcha() {
        user.setCaptcha("");
        assertEquals("", user.getCaptcha());
    }

    /**
     * Test setting all fields at once
     */
    @Test
    void testSetAllFields() {
        user.setUserName("testuser");
        user.setPassword("password123");
        user.setIpAddress("192.168.1.1");
        user.setCaptcha("ABC123");

        assertEquals("testuser", user.getUserName());
        assertEquals("password123", user.getPassword());
        assertEquals("192.168.1.1", user.getIpAddress());
        assertEquals("ABC123", user.getCaptcha());
    }

    /**
     * Test username with special characters
     */
    @Test
    void testUserNameWithSpecialCharacters() {
        user.setUserName("test@user.com");
        assertEquals("test@user.com", user.getUserName());
    }

    /**
     * Test password with special characters
     */
    @Test
    void testPasswordWithSpecialCharacters() {
        user.setPassword("P@ssw0rd!#$");
        assertEquals("P@ssw0rd!#$", user.getPassword());
    }

    /**
     * Test IPv4 address format
     */
    @Test
    void testIPv4Address() {
        user.setIpAddress("192.168.1.1");
        assertEquals("192.168.1.1", user.getIpAddress());
    }

    /**
     * Test IPv6 address format
     */
    @Test
    void testIPv6Address() {
        user.setIpAddress("2001:0db8:85a3:0000:0000:8a2e:0370:7334");
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", user.getIpAddress());
    }

    /**
     * Test captcha with alphanumeric characters
     */
    @Test
    void testCaptchaAlphanumeric() {
        user.setCaptcha("aB3Xy9");
        assertEquals("aB3Xy9", user.getCaptcha());
    }

    /**
     * Test updating username multiple times
     */
    @Test
    void testUpdateUserNameMultipleTimes() {
        user.setUserName("user1");
        assertEquals("user1", user.getUserName());

        user.setUserName("user2");
        assertEquals("user2", user.getUserName());

        user.setUserName("user3");
        assertEquals("user3", user.getUserName());
    }

    /**
     * Test updating password multiple times
     */
    @Test
    void testUpdatePasswordMultipleTimes() {
        user.setPassword("pass1");
        assertEquals("pass1", user.getPassword());

        user.setPassword("pass2");
        assertEquals("pass2", user.getPassword());
    }

    /**
     * Test very long username
     */
    @Test
    void testVeryLongUserName() {
        String longUsername = "a".repeat(1000);
        user.setUserName(longUsername);
        assertEquals(longUsername, user.getUserName());
        assertEquals(1000, user.getUserName().length());
    }

    /**
     * Test very long password
     */
    @Test
    void testVeryLongPassword() {
        String longPassword = "p".repeat(1000);
        user.setPassword(longPassword);
        assertEquals(longPassword, user.getPassword());
        assertEquals(1000, user.getPassword().length());
    }

    /**
     * Test username with whitespace
     */
    @Test
    void testUserNameWithWhitespace() {
        user.setUserName(" test user ");
        assertEquals(" test user ", user.getUserName());
    }

    /**
     * Test password with whitespace
     */
    @Test
    void testPasswordWithWhitespace() {
        user.setPassword(" pass word ");
        assertEquals(" pass word ", user.getPassword());
    }

    /**
     * Test User class is in correct package
     */
    @Test
    void testUserPackage() {
        assertEquals("com.example.data", User.class.getPackageName());
    }

    /**
     * Test User class is public
     */
    @Test
    void testUserClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(User.class.getModifiers()));
    }

    /**
     * Test User has default constructor
     */
    @Test
    void testUserHasDefaultConstructor() {
        assertDoesNotThrow(() -> User.class.getDeclaredConstructor());
    }
}
