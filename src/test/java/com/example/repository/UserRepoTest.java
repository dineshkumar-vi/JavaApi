package com.example.repository;

import com.example.data.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserRepo
 * Tests MongoDB repository operations for User entity
 */
@DataMongoTest
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=mongodb://localhost:27017/test"
})
class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        if (userRepo != null) {
            userRepo.deleteAll();
        }

        // Create test data
        testUser1 = new User();
        testUser1.setUserName("testuser");
        testUser1.setPassword("password123");
        testUser1.setIpAddress("192.168.1.1");
        testUser1.setCaptcha("ABC123");

        testUser2 = new User();
        testUser2.setUserName("anotheruser");
        testUser2.setPassword("pass456");
        testUser2.setIpAddress("192.168.1.2");
        testUser2.setCaptcha("XYZ789");
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        if (userRepo != null) {
            userRepo.deleteAll();
        }
    }

    /**
     * Test finding user by username and password - success case
     */
    @Test
    void testFindByUserNameAndPassword_Success() {
        if (userRepo == null) {
            return; // Skip test if repository is not available
        }

        userRepo.save(testUser1);

        User found = userRepo.findByUserNameAndPassword("testuser", "password123");

        assertNotNull(found);
        assertEquals("testuser", found.getUserName());
        assertEquals("password123", found.getPassword());
    }

    /**
     * Test finding user by username and password - user not found
     */
    @Test
    void testFindByUserNameAndPassword_NotFound() {
        if (userRepo == null) {
            return;
        }

        userRepo.save(testUser1);

        User found = userRepo.findByUserNameAndPassword("nonexistent", "password123");

        assertNull(found);
    }

    /**
     * Test finding user with wrong password
     */
    @Test
    void testFindByUserNameAndPassword_WrongPassword() {
        if (userRepo == null) {
            return;
        }

        userRepo.save(testUser1);

        User found = userRepo.findByUserNameAndPassword("testuser", "wrongpassword");

        assertNull(found);
    }

    /**
     * Test finding user with wrong username
     */
    @Test
    void testFindByUserNameAndPassword_WrongUsername() {
        if (userRepo == null) {
            return;
        }

        userRepo.save(testUser1);

        User found = userRepo.findByUserNameAndPassword("wronguser", "password123");

        assertNull(found);
    }

    /**
     * Test finding user with null username
     */
    @Test
    void testFindByUserNameAndPassword_NullUsername() {
        if (userRepo == null) {
            return;
        }

        userRepo.save(testUser1);

        User found = userRepo.findByUserNameAndPassword(null, "password123");

        assertNull(found);
    }

    /**
     * Test finding user with null password
     */
    @Test
    void testFindByUserNameAndPassword_NullPassword() {
        if (userRepo == null) {
            return;
        }

        userRepo.save(testUser1);

        User found = userRepo.findByUserNameAndPassword("testuser", null);

        assertNull(found);
    }

    /**
     * Test finding user with empty username
     */
    @Test
    void testFindByUserNameAndPassword_EmptyUsername() {
        if (userRepo == null) {
            return;
        }

        userRepo.save(testUser1);

        User found = userRepo.findByUserNameAndPassword("", "password123");

        assertNull(found);
    }

    /**
     * Test finding user with empty password
     */
    @Test
    void testFindByUserNameAndPassword_EmptyPassword() {
        if (userRepo == null) {
            return;
        }

        userRepo.save(testUser1);

        User found = userRepo.findByUserNameAndPassword("testuser", "");

        assertNull(found);
    }

    /**
     * Test saving a user to the repository
     */
    @Test
    void testSaveUser() {
        if (userRepo == null) {
            return;
        }

        User saved = userRepo.save(testUser1);

        assertNotNull(saved);
        assertEquals("testuser", saved.getUserName());
        assertEquals("password123", saved.getPassword());
    }

    /**
     * Test finding all users
     */
    @Test
    void testFindAll() {
        if (userRepo == null) {
            return;
        }

        userRepo.save(testUser1);
        userRepo.save(testUser2);

        List<User> all = userRepo.findAll();

        assertNotNull(all);
        assertEquals(2, all.size());
    }

    /**
     * Test case sensitivity of username search
     */
    @Test
    void testFindByUserNameAndPassword_CaseSensitiveUsername() {
        if (userRepo == null) {
            return;
        }

        userRepo.save(testUser1);

        User found = userRepo.findByUserNameAndPassword("TESTUSER", "password123");

        assertNull(found); // Should not find uppercase version
    }

    /**
     * Test case sensitivity of password search
     */
    @Test
    void testFindByUserNameAndPassword_CaseSensitivePassword() {
        if (userRepo == null) {
            return;
        }

        userRepo.save(testUser1);

        User found = userRepo.findByUserNameAndPassword("testuser", "PASSWORD123");

        assertNull(found); // Should not find uppercase version
    }

    /**
     * Test that multiple users can be saved
     */
    @Test
    void testSaveMultipleUsers() {
        if (userRepo == null) {
            return;
        }

        userRepo.save(testUser1);
        userRepo.save(testUser2);

        User found1 = userRepo.findByUserNameAndPassword("testuser", "password123");
        User found2 = userRepo.findByUserNameAndPassword("anotheruser", "pass456");

        assertNotNull(found1);
        assertNotNull(found2);
        assertEquals("testuser", found1.getUserName());
        assertEquals("anotheruser", found2.getUserName());
    }

    /**
     * Test updating a user
     */
    @Test
    void testUpdateUser() {
        if (userRepo == null) {
            return;
        }

        User saved = userRepo.save(testUser1);
        saved.setPassword("newpassword");
        userRepo.save(saved);

        User found = userRepo.findByUserNameAndPassword("testuser", "newpassword");

        assertNotNull(found);
        assertEquals("newpassword", found.getPassword());
    }

    /**
     * Test that UserRepo extends MongoRepository
     */
    @Test
    void testRepositoryInheritance() {
        assertTrue(org.springframework.data.mongodb.repository.MongoRepository.class
            .isAssignableFrom(UserRepo.class));
    }
}
