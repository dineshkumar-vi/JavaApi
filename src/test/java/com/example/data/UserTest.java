package com.example.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for User data model
 * Tests getters, setters, and data integrity
 */
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    /**
     * Test User object instantiation
     */
    @Test
    void testUserInstantiation() {
        assertNotNull(user);
    }

    /**
     * Test setting and getting IP address
     */
    @Test
    void testSetAndGetIpAddress() {
        String ipAddress = "192.168.1.1";
        user.setIpAddress(ipAddress);
        assertEquals(ipAddress, user.getIpAddress());
    }

    /**
     * Test setting and getting username
     */
    @Test
    void testSetAndGetUserName() {
        String username = "testuser";
        user.setUserName(username);
        assertEquals(username, user.getUserName());
    }

    /**
     * Test setting and getting password
     */
    @Test
    void testSetAndGetPassword() {
        String password = "password123";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    /**
     * Test setting and getting captcha
     */
    @Test
    void testSetAndGetCaptcha() {
        String captcha = "ABC123";
        user.setCaptcha(captcha);
        assertEquals(captcha, user.getCaptcha());
    }

    /**
     * Test default values (should be null)
     */
    @Test
    void testDefaultValues() {
        User newUser = new User();
        assertNull(newUser.getIpAddress());
        assertNull(newUser.getUserName());
        assertNull(newUser.getPassword());
        assertNull(newUser.getCaptcha());
    }

    /**
     * Test setting null IP address
     */
    @Test
    void testSetNullIpAddress() {
        user.setIpAddress("192.168.1.1");
        user.setIpAddress(null);
        assertNull(user.getIpAddress());
    }

    /**
     * Test setting null username
     */
    @Test
    void testSetNullUserName() {
        user.setUserName("testuser");
        user.setUserName(null);
        assertNull(user.getUserName());
    }

    /**
     * Test setting null password
     */
    @Test
    void testSetNullPassword() {
        user.setPassword("password123");
        user.setPassword(null);
        assertNull(user.getPassword());
    }

    /**
     * Test setting null captcha
     */
    @Test
    void testSetNullCaptcha() {
        user.setCaptcha("ABC123");
        user.setCaptcha(null);
        assertNull(user.getCaptcha());
    }

    /**
     * Test setting empty string values
     */
    @Test
    void testSetEmptyStrings() {
        user.setIpAddress("");
        user.setUserName("");
        user.setPassword("");
        user.setCaptcha("");

        assertEquals("", user.getIpAddress());
        assertEquals("", user.getUserName());
        assertEquals("", user.getPassword());
        assertEquals("", user.getCaptcha());
    }

    /**
     * Test setting all fields
     */
    @Test
    void testSetAllFields() {
        user.setIpAddress("10.0.0.1");
        user.setUserName("john.doe");
        user.setPassword("securePass456");
        user.setCaptcha("XYZ789");

        assertEquals("10.0.0.1", user.getIpAddress());
        assertEquals("john.doe", user.getUserName());
        assertEquals("securePass456", user.getPassword());
        assertEquals("XYZ789", user.getCaptcha());
    }

    /**
     * Test overwriting existing values
     */
    @Test
    void testOverwriteValues() {
        user.setUserName("firstUser");
        assertEquals("firstUser", user.getUserName());

        user.setUserName("secondUser");
        assertEquals("secondUser", user.getUserName());
    }

    /**
     * Test IP address with various formats
     */
    @Test
    void testVariousIpAddressFormats() {
        user.setIpAddress("127.0.0.1");
        assertEquals("127.0.0.1", user.getIpAddress());

        user.setIpAddress("255.255.255.255");
        assertEquals("255.255.255.255", user.getIpAddress());

        user.setIpAddress("0.0.0.0");
        assertEquals("0.0.0.0", user.getIpAddress());
    }

    /**
     * Test username with special characters
     */
    @Test
    void testUsernameWithSpecialCharacters() {
        user.setUserName("user@example.com");
        assertEquals("user@example.com", user.getUserName());

        user.setUserName("user_name-123");
        assertEquals("user_name-123", user.getUserName());
    }

    /**
     * Test password with special characters
     */
    @Test
    void testPasswordWithSpecialCharacters() {
        String complexPassword = "P@ssw0rd!#$%";
        user.setPassword(complexPassword);
        assertEquals(complexPassword, user.getPassword());
    }

    /**
     * Test long values in fields
     */
    @Test
    void testLongValues() {
        String longUsername = "a".repeat(100);
        String longPassword = "b".repeat(200);
        String longCaptcha = "c".repeat(50);

        user.setUserName(longUsername);
        user.setPassword(longPassword);
        user.setCaptcha(longCaptcha);

        assertEquals(longUsername, user.getUserName());
        assertEquals(longPassword, user.getPassword());
        assertEquals(longCaptcha, user.getCaptcha());
    }

    /**
     * Test captcha with alphanumeric values
     */
    @Test
    void testCaptchaAlphanumeric() {
        user.setCaptcha("aB3cD4");
        assertEquals("aB3cD4", user.getCaptcha());
    }

    /**
     * Test User class is in correct package
     */
    @Test
    void testPackage() {
        assertEquals("com.example.data", User.class.getPackage().getName());
    }

    /**
     * Test that User class has all required methods
     */
    @Test
    void testRequiredMethodsExist() throws NoSuchMethodException {
        assertNotNull(User.class.getMethod("getIpAddress"));
        assertNotNull(User.class.getMethod("setIpAddress", String.class));
        assertNotNull(User.class.getMethod("getUserName"));
        assertNotNull(User.class.getMethod("setUserName", String.class));
        assertNotNull(User.class.getMethod("getPassword"));
        assertNotNull(User.class.getMethod("setPassword", String.class));
        assertNotNull(User.class.getMethod("getCaptcha"));
        assertNotNull(User.class.getMethod("setCaptcha", String.class));
    }

    /**
     * Test multiple User objects are independent
     */
    @Test
    void testMultipleUsersIndependent() {
        User user1 = new User();
        User user2 = new User();

        user1.setUserName("user1");
        user2.setUserName("user2");

        assertEquals("user1", user1.getUserName());
        assertEquals("user2", user2.getUserName());
        assertNotEquals(user1.getUserName(), user2.getUserName());
    }
}
