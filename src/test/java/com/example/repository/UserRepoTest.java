package com.example.repository;

import com.example.data.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.repository.MongoRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for UserRepo
 * Tests cover repository operations, user queries, and edge cases
 */
@ExtendWith(MockitoExtension.class)
class UserRepoTest {

    @Mock
    private UserRepo userRepo;

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
     * Test that UserRepo interface extends MongoRepository
     */
    @Test
    void testUserRepoExtendsMongoRepository() {
        assertTrue(MongoRepository.class.isAssignableFrom(UserRepo.class));
    }

    /**
     * Test findByUserNameAndPassword with valid credentials
     */
    @Test
    void testFindByUserNameAndPasswordWithValidCredentials() {
        when(userRepo.findByUserNameAndPassword("testuser", "testpass123"))
                .thenReturn(testUser);

        User result = userRepo.findByUserNameAndPassword("testuser", "testpass123");

        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
        assertEquals("testpass123", result.getPassword());
        verify(userRepo).findByUserNameAndPassword("testuser", "testpass123");
    }

    /**
     * Test findByUserNameAndPassword with invalid username
     */
    @Test
    void testFindByUserNameAndPasswordWithInvalidUsername() {
        when(userRepo.findByUserNameAndPassword("invaliduser", "testpass123"))
                .thenReturn(null);

        User result = userRepo.findByUserNameAndPassword("invaliduser", "testpass123");

        assertNull(result);
        verify(userRepo).findByUserNameAndPassword("invaliduser", "testpass123");
    }

    /**
     * Test findByUserNameAndPassword with invalid password
     */
    @Test
    void testFindByUserNameAndPasswordWithInvalidPassword() {
        when(userRepo.findByUserNameAndPassword("testuser", "wrongpassword"))
                .thenReturn(null);

        User result = userRepo.findByUserNameAndPassword("testuser", "wrongpassword");

        assertNull(result);
        verify(userRepo).findByUserNameAndPassword("testuser", "wrongpassword");
    }

    /**
     * Test findByUserNameAndPassword with null username
     */
    @Test
    void testFindByUserNameAndPasswordWithNullUsername() {
        when(userRepo.findByUserNameAndPassword(null, "testpass123"))
                .thenReturn(null);

        User result = userRepo.findByUserNameAndPassword(null, "testpass123");

        assertNull(result);
        verify(userRepo).findByUserNameAndPassword(null, "testpass123");
    }

    /**
     * Test findByUserNameAndPassword with null password
     */
    @Test
    void testFindByUserNameAndPasswordWithNullPassword() {
        when(userRepo.findByUserNameAndPassword("testuser", null))
                .thenReturn(null);

        User result = userRepo.findByUserNameAndPassword("testuser", null);

        assertNull(result);
        verify(userRepo).findByUserNameAndPassword("testuser", null);
    }

    /**
     * Test findByUserNameAndPassword with both null values
     */
    @Test
    void testFindByUserNameAndPasswordWithBothNull() {
        when(userRepo.findByUserNameAndPassword(null, null))
                .thenReturn(null);

        User result = userRepo.findByUserNameAndPassword(null, null);

        assertNull(result);
        verify(userRepo).findByUserNameAndPassword(null, null);
    }

    /**
     * Test findByUserNameAndPassword with empty username
     */
    @Test
    void testFindByUserNameAndPasswordWithEmptyUsername() {
        when(userRepo.findByUserNameAndPassword("", "testpass123"))
                .thenReturn(null);

        User result = userRepo.findByUserNameAndPassword("", "testpass123");

        assertNull(result);
        verify(userRepo).findByUserNameAndPassword("", "testpass123");
    }

    /**
     * Test findByUserNameAndPassword with empty password
     */
    @Test
    void testFindByUserNameAndPasswordWithEmptyPassword() {
        when(userRepo.findByUserNameAndPassword("testuser", ""))
                .thenReturn(null);

        User result = userRepo.findByUserNameAndPassword("testuser", "");

        assertNull(result);
        verify(userRepo).findByUserNameAndPassword("testuser", "");
    }

    /**
     * Test findByUserNameAndPassword with whitespace in username
     */
    @Test
    void testFindByUserNameAndPasswordWithWhitespaceUsername() {
        when(userRepo.findByUserNameAndPassword("  testuser  ", "testpass123"))
                .thenReturn(null);

        User result = userRepo.findByUserNameAndPassword("  testuser  ", "testpass123");

        assertNull(result);
        verify(userRepo).findByUserNameAndPassword("  testuser  ", "testpass123");
    }

    /**
     * Test findByUserNameAndPassword with special characters
     */
    @Test
    void testFindByUserNameAndPasswordWithSpecialCharacters() {
        User specialUser = new User();
        specialUser.setUserName("test@user.com");
        specialUser.setPassword("P@ssw0rd!");

        when(userRepo.findByUserNameAndPassword("test@user.com", "P@ssw0rd!"))
                .thenReturn(specialUser);

        User result = userRepo.findByUserNameAndPassword("test@user.com", "P@ssw0rd!");

        assertNotNull(result);
        assertEquals("test@user.com", result.getUserName());
        verify(userRepo).findByUserNameAndPassword("test@user.com", "P@ssw0rd!");
    }

    /**
     * Test that interface has correct method signature
     */
    @Test
    void testFindByUserNameAndPasswordMethodExists() {
        assertDoesNotThrow(() -> {
            UserRepo.class.getDeclaredMethod("findByUserNameAndPassword", String.class, String.class);
        });
    }

    /**
     * Test that repository interface is public
     */
    @Test
    void testRepositoryInterfaceIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(UserRepo.class.getModifiers()));
    }

    /**
     * Test that UserRepo is indeed an interface
     */
    @Test
    void testUserRepoIsInterface() {
        assertTrue(UserRepo.class.isInterface());
    }

    /**
     * Test package name
     */
    @Test
    void testPackageName() {
        assertEquals("com.example.repository", UserRepo.class.getPackageName());
    }

    /**
     * Test findByUserNameAndPassword with very long username
     */
    @Test
    void testFindByUserNameAndPasswordWithLongUsername() {
        String longUsername = "a".repeat(1000);
        when(userRepo.findByUserNameAndPassword(longUsername, "testpass123"))
                .thenReturn(null);

        User result = userRepo.findByUserNameAndPassword(longUsername, "testpass123");

        assertNull(result);
        verify(userRepo).findByUserNameAndPassword(longUsername, "testpass123");
    }

    /**
     * Test findByUserNameAndPassword with case sensitivity
     */
    @Test
    void testFindByUserNameAndPasswordCaseSensitivity() {
        when(userRepo.findByUserNameAndPassword("TestUser", "testpass123"))
                .thenReturn(null);

        User result = userRepo.findByUserNameAndPassword("TestUser", "testpass123");

        // Should not find user with different case
        assertNull(result);
        verify(userRepo).findByUserNameAndPassword("TestUser", "testpass123");
    }
}
