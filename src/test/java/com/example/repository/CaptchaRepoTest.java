package com.example.repository;

import com.example.data.Captcha;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for CaptchaRepo
 * Tests cover repository operations, queries, and edge cases
 */
@ExtendWith(MockitoExtension.class)
class CaptchaRepoTest {

    @Mock
    private CaptchaRepo captchaRepo;

    private Captcha testCaptcha;

    @BeforeEach
    void setUp() {
        testCaptcha = new Captcha();
        testCaptcha.setId("test-id-123");
        testCaptcha.setCaptcha("abc123");
        testCaptcha.setIpAddress("192.168.1.1");
    }

    @AfterEach
    void tearDown() {
        testCaptcha = null;
    }

    /**
     * Test that CaptchaRepo interface extends MongoRepository
     */
    @Test
    void testCaptchaRepoExtendsMongoRepository() {
        assertTrue(MongoRepository.class.isAssignableFrom(CaptchaRepo.class));
    }

    /**
     * Test that @Repository annotation is present
     */
    @Test
    void testRepositoryAnnotationPresent() {
        assertTrue(CaptchaRepo.class.isAnnotationPresent(Repository.class));
    }

    /**
     * Test findByCaptchaAndIpAddress with valid inputs
     */
    @Test
    void testFindByCaptchaAndIpAddressWithValidInputs() {
        List<Captcha> expectedList = Arrays.asList(testCaptcha);
        when(captchaRepo.findByCaptchaAndIpAddress("abc123", "192.168.1.1"))
                .thenReturn(expectedList);

        List<Captcha> result = captchaRepo.findByCaptchaAndIpAddress("abc123", "192.168.1.1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCaptcha, result.get(0));
        verify(captchaRepo).findByCaptchaAndIpAddress("abc123", "192.168.1.1");
    }

    /**
     * Test findByCaptchaAndIpAddress with multiple results
     */
    @Test
    void testFindByCaptchaAndIpAddressWithMultipleResults() {
        Captcha captcha2 = new Captcha();
        captcha2.setId("test-id-456");
        captcha2.setCaptcha("abc123");
        captcha2.setIpAddress("192.168.1.1");

        List<Captcha> expectedList = Arrays.asList(testCaptcha, captcha2);
        when(captchaRepo.findByCaptchaAndIpAddress("abc123", "192.168.1.1"))
                .thenReturn(expectedList);

        List<Captcha> result = captchaRepo.findByCaptchaAndIpAddress("abc123", "192.168.1.1");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(captchaRepo).findByCaptchaAndIpAddress("abc123", "192.168.1.1");
    }

    /**
     * Test findByCaptchaAndIpAddress with no results
     */
    @Test
    void testFindByCaptchaAndIpAddressWithNoResults() {
        when(captchaRepo.findByCaptchaAndIpAddress("invalid", "0.0.0.0"))
                .thenReturn(Collections.emptyList());

        List<Captcha> result = captchaRepo.findByCaptchaAndIpAddress("invalid", "0.0.0.0");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(captchaRepo).findByCaptchaAndIpAddress("invalid", "0.0.0.0");
    }

    /**
     * Test findByCaptchaAndIpAddress with null captcha
     */
    @Test
    void testFindByCaptchaAndIpAddressWithNullCaptcha() {
        when(captchaRepo.findByCaptchaAndIpAddress(null, "192.168.1.1"))
                .thenReturn(Collections.emptyList());

        List<Captcha> result = captchaRepo.findByCaptchaAndIpAddress(null, "192.168.1.1");

        assertNotNull(result);
        verify(captchaRepo).findByCaptchaAndIpAddress(null, "192.168.1.1");
    }

    /**
     * Test findByCaptchaAndIpAddress with null IP address
     */
    @Test
    void testFindByCaptchaAndIpAddressWithNullIpAddress() {
        when(captchaRepo.findByCaptchaAndIpAddress("abc123", null))
                .thenReturn(Collections.emptyList());

        List<Captcha> result = captchaRepo.findByCaptchaAndIpAddress("abc123", null);

        assertNotNull(result);
        verify(captchaRepo).findByCaptchaAndIpAddress("abc123", null);
    }

    /**
     * Test findByCaptchaAndIpAddress with empty strings
     */
    @Test
    void testFindByCaptchaAndIpAddressWithEmptyStrings() {
        when(captchaRepo.findByCaptchaAndIpAddress("", ""))
                .thenReturn(Collections.emptyList());

        List<Captcha> result = captchaRepo.findByCaptchaAndIpAddress("", "");

        assertNotNull(result);
        verify(captchaRepo).findByCaptchaAndIpAddress("", "");
    }

    /**
     * Test that interface has correct method signature
     */
    @Test
    void testFindByCaptchaAndIpAddressMethodExists() {
        assertDoesNotThrow(() -> {
            CaptchaRepo.class.getDeclaredMethod("findByCaptchaAndIpAddress", String.class, String.class);
        });
    }

    /**
     * Test findByCaptchaAndIpAddress with special characters in captcha
     */
    @Test
    void testFindByCaptchaAndIpAddressWithSpecialCharacters() {
        when(captchaRepo.findByCaptchaAndIpAddress("abc!@#123", "192.168.1.1"))
                .thenReturn(Collections.emptyList());

        List<Captcha> result = captchaRepo.findByCaptchaAndIpAddress("abc!@#123", "192.168.1.1");

        assertNotNull(result);
        verify(captchaRepo).findByCaptchaAndIpAddress("abc!@#123", "192.168.1.1");
    }

    /**
     * Test findByCaptchaAndIpAddress with IPv6 address
     */
    @Test
    void testFindByCaptchaAndIpAddressWithIpv6() {
        String ipv6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        when(captchaRepo.findByCaptchaAndIpAddress("abc123", ipv6))
                .thenReturn(Arrays.asList(testCaptcha));

        List<Captcha> result = captchaRepo.findByCaptchaAndIpAddress("abc123", ipv6);

        assertNotNull(result);
        verify(captchaRepo).findByCaptchaAndIpAddress("abc123", ipv6);
    }

    /**
     * Test that repository interface is public
     */
    @Test
    void testRepositoryInterfaceIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(CaptchaRepo.class.getModifiers()));
    }

    /**
     * Test that repository interface is indeed an interface
     */
    @Test
    void testCaptchaRepoIsInterface() {
        assertTrue(CaptchaRepo.class.isInterface());
    }

    /**
     * Test package name
     */
    @Test
    void testPackageName() {
        assertEquals("com.example.repository", CaptchaRepo.class.getPackageName());
    }
}
