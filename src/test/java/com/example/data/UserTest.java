package com.example.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for User data class
 * Tests cover all getters, setters, and edge cases
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
     * Test User class instantiation
     */
    @Test
    void testUserInstantiation() {
        assertNotNull(user);
    }

    /**
     * Test ipAddress getter and setter with valid value
     */
    @Test
    void testIpAddressGetterAndSetterWithValidValue() {
        String ipAddress = "192.168.1.1";
        user.setIpAddress(ipAddress);
        assertEquals(ipAddress, user.getIpAddress());
    }

    /**
     * Test ipAddress with null value
     */
    @Test
    void testIpAddressWithNullValue() {
        user.setIpAddress(null);
        assertNull(user.getIpAddress());
    }

    /**
     * Test ipAddress with empty string
     */
    @Test
    void testIpAddressWithEmptyString() {
        user.setIpAddress("");
        assertEquals("", user.getIpAddress());
    }

    /**
     * Test ipAddress with IPv6 address
     */
    @Test
    void testIpAddressWithIpv6() {
        String ipv6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        user.setIpAddress(ipv6);
        assertEquals(ipv6, user.getIpAddress());
    }

    /**
     * Test userName getter and setter with valid value
     */
    @Test
    void testUserNameGetterAndSetterWithValidValue() {
        String userName = "testuser";
        user.setUserName(userName);
        assertEquals(userName, user.getUserName());
    }

    /**
     * Test userName with null value
     */
    @Test
    void testUserNameWithNullValue() {
        user.setUserName(null);
        assertNull(user.getUserName());
    }

    /**
     * Test userName with empty string
     */
    @Test
    void testUserNameWithEmptyString() {
        user.setUserName("");
        assertEquals("", user.getUserName());
    }

    /**
     * Test userName with special characters
     */
    @Test
    void testUserNameWithSpecialCharacters() {
        String userName = "test@user.com";
        user.setUserName(userName);
        assertEquals(userName, user.getUserName());
    }

    /**
     * Test userName with very long string
     */
    @Test
    void testUserNameWithLongString() {
        String longUserName = "a".repeat(1000);
        user.setUserName(longUserName);
        assertEquals(longUserName, user.getUserName());
    }

    /**
     * Test password getter and setter with valid value
     */
    @Test
    void testPasswordGetterAndSetterWithValidValue() {
        String password = "testpass123";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    /**
     * Test password with null value
     */
    @Test
    void testPasswordWithNullValue() {
        user.setPassword(null);
        assertNull(user.getPassword());
    }

    /**
     * Test password with empty string
     */
    @Test
    void testPasswordWithEmptyString() {
        user.setPassword("");
        assertEquals("", user.getPassword());
    }

    /**
     * Test password with special characters
     */
    @Test
    void testPasswordWithSpecialCharacters() {
        String password = "P@ssw0rd!#$%";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    /**
     * Test password with very long string
     */
    @Test
    void testPasswordWithLongString() {
        String longPassword = "p".repeat(1000);
        user.setPassword(longPassword);
        assertEquals(longPassword, user.getPassword());
    }

    /**
     * Test captcha getter and setter with valid value
     */
    @Test
    void testCaptchaGetterAndSetterWithValidValue() {
        String captcha = "abc123";
        user.setCaptcha(captcha);
        assertEquals(captcha, user.getCaptcha());
    }

    /**
     * Test captcha with null value
     */
    @Test
    void testCaptchaWithNullValue() {
        user.setCaptcha(null);
        assertNull(user.getCaptcha());
    }

    /**
     * Test captcha with empty string
     */
    @Test
    void testCaptchaWithEmptyString() {
        user.setCaptcha("");
        assertEquals("", user.getCaptcha());
    }

    /**
     * Test captcha with alphanumeric characters
     */
    @Test
    void testCaptchaWithAlphanumeric() {
        String captcha = "AbC123XyZ";
        user.setCaptcha(captcha);
        assertEquals(captcha, user.getCaptcha());
    }

    /**
     * Test that all fields are initially null
     */
    @Test
    void testAllFieldsInitiallyNull() {
        User newUser = new User();
        assertNull(newUser.getIpAddress());
        assertNull(newUser.getUserName());
        assertNull(newUser.getPassword());
        assertNull(newUser.getCaptcha());
    }

    /**
     * Test setting all fields at once
     */
    @Test
    void testSettingAllFields() {
        user.setIpAddress("192.168.1.1");
        user.setUserName("testuser");
        user.setPassword("testpass123");
        user.setCaptcha("abc123");

        assertEquals("192.168.1.1", user.getIpAddress());
        assertEquals("testuser", user.getUserName());
        assertEquals("testpass123", user.getPassword());
        assertEquals("abc123", user.getCaptcha());
    }

    /**
     * Test overwriting existing values
     */
    @Test
    void testOverwritingExistingValues() {
        user.setUserName("olduser");
        assertEquals("olduser", user.getUserName());

        user.setUserName("newuser");
        assertEquals("newuser", user.getUserName());
    }

    /**
     * Test that class is public
     */
    @Test
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(User.class.getModifiers()));
    }

    /**
     * Test package name
     */
    @Test
    void testPackageName() {
        assertEquals("com.example.data", User.class.getPackageName());
    }

    /**
     * Test class name
     */
    @Test
    void testClassName() {
        assertEquals("User", User.class.getSimpleName());
    }

    /**
     * Test that User has exactly 4 private fields
     */
    @Test
    void testUserHasFourPrivateFields() {
        assertEquals(4, User.class.getDeclaredFields().length);
    }

    /**
     * Test userName with whitespace
     */
    @Test
    void testUserNameWithWhitespace() {
        String userName = "  test user  ";
        user.setUserName(userName);
        assertEquals(userName, user.getUserName());
    }

    /**
     * Test password with whitespace
     */
    @Test
    void testPasswordWithWhitespace() {
        String password = "  test pass  ";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    /**
     * Test setting same value multiple times
     */
    @Test
    void testSettingSameValueMultipleTimes() {
        user.setUserName("testuser");
        user.setUserName("testuser");
        user.setUserName("testuser");
        assertEquals("testuser", user.getUserName());
    }

    /**
     * Test chaining setters (verifying they work independently)
     */
    @Test
    void testSettersWorkIndependently() {
        user.setUserName("user1");
        user.setPassword("pass1");
        user.setIpAddress("192.168.1.1");
        user.setCaptcha("cap1");

        assertEquals("user1", user.getUserName());
        assertEquals("pass1", user.getPassword());
        assertEquals("192.168.1.1", user.getIpAddress());
        assertEquals("cap1", user.getCaptcha());
    }
}
