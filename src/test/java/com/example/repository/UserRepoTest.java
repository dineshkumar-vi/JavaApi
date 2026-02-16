package com.example.repository;

import com.example.data.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

/**
 * Comprehensive unit tests for UserRepo
 * Tests cover MongoDB repository operations and custom user queries
 */
@ExtendWith(SpringExtension.class)
@DataMongoTest
class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        userRepo.deleteAll();

        // Create test user
        testUser = new User();
        testUser.setUserName("testuser");
        testUser.setPassword("password123");
        testUser.setIpAddress("192.168.1.1");
        testUser.setCaptcha("ABC123");
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        userRepo.deleteAll();
    }

    /**
     * Test that repository is autowired successfully
     */
    @Test
    void testRepositoryIsNotNull() {
        assertNotNull(userRepo);
    }

    /**
     * Test saving a user entity
     */
    @Test
    void testSaveUser() {
        User savedUser = userRepo.save(testUser);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUserName());
        assertEquals("password123", savedUser.getPassword());
        assertEquals("192.168.1.1", savedUser.getIpAddress());
    }

    /**
     * Test finding user by username and password - success case
     */
    @Test
    void testFindByUserNameAndPassword_Success() {
        userRepo.save(testUser);

        User foundUser = userRepo.findByUserNameAndPassword("testuser", "password123");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUserName());
        assertEquals("password123", foundUser.getPassword());
    }

    /**
     * Test finding user by username and password - user not found
     */
    @Test
    void testFindByUserNameAndPassword_NotFound() {
        userRepo.save(testUser);

        User foundUser = userRepo.findByUserNameAndPassword("wronguser", "password123");

        assertNull(foundUser);
    }

    /**
     * Test finding user with wrong password
     */
    @Test
    void testFindByUserNameAndPassword_WrongPassword() {
        userRepo.save(testUser);

        User foundUser = userRepo.findByUserNameAndPassword("testuser", "wrongpassword");

        assertNull(foundUser);
    }

    /**
     * Test finding user with null username
     */
    @Test
    void testFindByUserNameAndPassword_NullUserName() {
        userRepo.save(testUser);

        User foundUser = userRepo.findByUserNameAndPassword(null, "password123");

        assertNull(foundUser);
    }

    /**
     * Test finding user with null password
     */
    @Test
    void testFindByUserNameAndPassword_NullPassword() {
        userRepo.save(testUser);

        User foundUser = userRepo.findByUserNameAndPassword("testuser", null);

        assertNull(foundUser);
    }

    /**
     * Test finding user with empty username
     */
    @Test
    void testFindByUserNameAndPassword_EmptyUserName() {
        userRepo.save(testUser);

        User foundUser = userRepo.findByUserNameAndPassword("", "password123");

        assertNull(foundUser);
    }

    /**
     * Test finding user with empty password
     */
    @Test
    void testFindByUserNameAndPassword_EmptyPassword() {
        userRepo.save(testUser);

        User foundUser = userRepo.findByUserNameAndPassword("testuser", "");

        assertNull(foundUser);
    }

    /**
     * Test case sensitivity of username
     */
    @Test
    void testFindByUserNameAndPassword_CaseSensitiveUserName() {
        userRepo.save(testUser);

        User foundUser = userRepo.findByUserNameAndPassword("TESTUSER", "password123");

        assertNull(foundUser); // Should be case-sensitive
    }

    /**
     * Test case sensitivity of password
     */
    @Test
    void testFindByUserNameAndPassword_CaseSensitivePassword() {
        userRepo.save(testUser);

        User foundUser = userRepo.findByUserNameAndPassword("testuser", "PASSWORD123");

        assertNull(foundUser); // Should be case-sensitive
    }

    /**
     * Test save and retrieve with all fields populated
     */
    @Test
    void testSaveAndRetrieveWithAllFields() {
        User savedUser = userRepo.save(testUser);

        User foundUser = userRepo.findByUserNameAndPassword("testuser", "password123");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUserName());
        assertEquals("password123", foundUser.getPassword());
        assertEquals("192.168.1.1", foundUser.getIpAddress());
        assertEquals("ABC123", foundUser.getCaptcha());
    }

    /**
     * Test count method
     */
    @Test
    void testCount() {
        assertEquals(0, userRepo.count());

        userRepo.save(testUser);
        assertEquals(1, userRepo.count());
    }

    /**
     * Test find all method
     */
    @Test
    void testFindAll() {
        userRepo.save(testUser);

        User anotherUser = new User();
        anotherUser.setUserName("user2");
        anotherUser.setPassword("pass2");
        userRepo.save(anotherUser);

        List<User> allUsers = userRepo.findAll();

        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
    }

    /**
     * Test delete user
     */
    @Test
    void testDeleteUser() {
        User savedUser = userRepo.save(testUser);

        userRepo.delete(savedUser);

        User foundUser = userRepo.findByUserNameAndPassword("testuser", "password123");
        assertNull(foundUser);
    }

    /**
     * Test update user
     */
    @Test
    void testUpdateUser() {
        User savedUser = userRepo.save(testUser);

        savedUser.setPassword("newpassword");
        userRepo.save(savedUser);

        User foundUser = userRepo.findByUserNameAndPassword("testuser", "newpassword");
        assertNotNull(foundUser);
        assertEquals("newpassword", foundUser.getPassword());
    }

    /**
     * Test multiple users with same password
     */
    @Test
    void testMultipleUsersWithSamePassword() {
        userRepo.save(testUser);

        User anotherUser = new User();
        anotherUser.setUserName("user2");
        anotherUser.setPassword("password123");
        userRepo.save(anotherUser);

        User foundUser1 = userRepo.findByUserNameAndPassword("testuser", "password123");
        User foundUser2 = userRepo.findByUserNameAndPassword("user2", "password123");

        assertNotNull(foundUser1);
        assertNotNull(foundUser2);
        assertNotEquals(foundUser1.getUserName(), foundUser2.getUserName());
    }

    /**
     * Test saving user with minimal fields
     */
    @Test
    void testSaveUserWithMinimalFields() {
        User minimalUser = new User();
        minimalUser.setUserName("minimal");
        minimalUser.setPassword("pass");

        User savedUser = userRepo.save(minimalUser);

        assertNotNull(savedUser);
        assertEquals("minimal", savedUser.getUserName());
    }

    /**
     * Test special characters in username
     */
    @Test
    void testSpecialCharactersInUserName() {
        testUser.setUserName("test@user.com");
        userRepo.save(testUser);

        User foundUser = userRepo.findByUserNameAndPassword("test@user.com", "password123");

        assertNotNull(foundUser);
        assertEquals("test@user.com", foundUser.getUserName());
    }

    /**
     * Test special characters in password
     */
    @Test
    void testSpecialCharactersInPassword() {
        testUser.setPassword("P@ssw0rd!#$");
        userRepo.save(testUser);

        User foundUser = userRepo.findByUserNameAndPassword("testuser", "P@ssw0rd!#$");

        assertNotNull(foundUser);
        assertEquals("P@ssw0rd!#$", foundUser.getPassword());
    }
}
