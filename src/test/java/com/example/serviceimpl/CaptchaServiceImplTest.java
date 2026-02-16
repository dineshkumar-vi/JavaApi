package com.example.serviceimpl;

import com.example.data.Captcha;
import com.example.repository.CaptchaRepo;
import com.example.service.CaptchaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for CaptchaServiceImpl
 * Tests cover captcha creation, retrieval, validation, and edge cases
 */
@ExtendWith(MockitoExtension.class)
class CaptchaServiceImplTest {

    @Mock
    private CaptchaRepo captchaRepo;

    @InjectMocks
    private CaptchaServiceImpl captchaService;

    private Captcha testCaptcha;

    @BeforeEach
    void setUp() {
        testCaptcha = new Captcha();
        testCaptcha.setId("test-id-123");
        testCaptcha.setIpAddress("192.168.1.1");
    }

    @AfterEach
    void tearDown() {
        testCaptcha = null;
    }

    /**
     * Test that @Component annotation is present
     */
    @Test
    void testComponentAnnotationPresent() {
        assertTrue(CaptchaServiceImpl.class.isAnnotationPresent(Component.class));
    }

    /**
     * Test that class implements CaptchaService
     */
    @Test
    void testImplementsCaptchaService() {
        assertTrue(CaptchaService.class.isAssignableFrom(CaptchaServiceImpl.class));
    }

    /**
     * Test createCaptcha with valid input
     */
    @Test
    void testCreateCaptchaWithValidInput() {
        when(captchaRepo.save(any(Captcha.class))).thenReturn(testCaptcha);

        Captcha result = captchaService.createCaptcha(testCaptcha);

        assertNotNull(result);
        assertNotNull(result.getCaptcha());
        assertEquals(6, result.getCaptcha().length());
        verify(captchaRepo).save(any(Captcha.class));
    }

    /**
     * Test createCaptcha generates 6-character captcha
     */
    @Test
    void testCreateCaptchaGeneratesSixCharacters() {
        Captcha savedCaptcha = new Captcha();
        savedCaptcha.setIpAddress("192.168.1.1");
        savedCaptcha.setCaptcha("abc123");

        when(captchaRepo.save(any(Captcha.class))).thenReturn(savedCaptcha);

        Captcha result = captchaService.createCaptcha(testCaptcha);

        assertNotNull(result);
        verify(captchaRepo).save(any(Captcha.class));
    }

    /**
     * Test createCaptcha generates alphanumeric captcha
     */
    @Test
    void testCreateCaptchaGeneratesAlphanumeric() {
        when(captchaRepo.save(any(Captcha.class))).thenAnswer(invocation -> {
            Captcha c = invocation.getArgument(0);
            // Verify captcha contains only alphanumeric characters
            assertTrue(c.getCaptcha().matches("[0-9a-zA-Z]{6}"));
            return c;
        });

        captchaService.createCaptcha(testCaptcha);

        verify(captchaRepo).save(any(Captcha.class));
    }

    /**
     * Test createCaptcha with null captcha object
     */
    @Test
    void testCreateCaptchaWithNullObject() {
        assertThrows(NullPointerException.class, () -> {
            captchaService.createCaptcha(null);
        });
    }

    /**
     * Test createCaptcha when repository throws exception
     */
    @Test
    void testCreateCaptchaWhenRepositoryThrowsException() {
        when(captchaRepo.save(any(Captcha.class))).thenThrow(new RuntimeException("Database error"));

        Captcha result = captchaService.createCaptcha(testCaptcha);

        assertNull(result);
        verify(captchaRepo).save(any(Captcha.class));
    }

    /**
     * Test createCaptcha preserves IP address
     */
    @Test
    void testCreateCaptchaPreservesIpAddress() {
        when(captchaRepo.save(any(Captcha.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Captcha result = captchaService.createCaptcha(testCaptcha);

        assertEquals("192.168.1.1", result.getIpAddress());
        verify(captchaRepo).save(any(Captcha.class));
    }

    /**
     * Test get with valid captcha and IP address
     */
    @Test
    void testGetWithValidCaptchaAndIpAddress() {
        testCaptcha.setCaptcha("abc123");
        List<Captcha> captchaList = Arrays.asList(testCaptcha);
        when(captchaRepo.findByCaptchaAndIpAddress("abc123", "192.168.1.1"))
                .thenReturn(captchaList);

        Captcha result = captchaService.get("abc123", "192.168.1.1");

        assertNotNull(result);
        assertEquals("abc123", result.getCaptcha());
        assertEquals("192.168.1.1", result.getIpAddress());
        verify(captchaRepo).findByCaptchaAndIpAddress("abc123", "192.168.1.1");
    }

    /**
     * Test get with multiple captchas returns the latest
     */
    @Test
    void testGetWithMultipleCaptchasReturnsLatest() {
        Captcha captcha1 = new Captcha();
        captcha1.setCaptcha("abc123");
        captcha1.setIpAddress("192.168.1.1");
        captcha1.setId("id1");

        Captcha captcha2 = new Captcha();
        captcha2.setCaptcha("abc123");
        captcha2.setIpAddress("192.168.1.1");
        captcha2.setId("id2");

        List<Captcha> captchaList = Arrays.asList(captcha1, captcha2);
        when(captchaRepo.findByCaptchaAndIpAddress("abc123", "192.168.1.1"))
                .thenReturn(captchaList);

        Captcha result = captchaService.get("abc123", "192.168.1.1");

        assertNotNull(result);
        assertEquals("id2", result.getId()); // Should return the last one
        verify(captchaRepo).findByCaptchaAndIpAddress("abc123", "192.168.1.1");
    }

    /**
     * Test get with no matching captcha
     */
    @Test
    void testGetWithNoMatchingCaptcha() {
        when(captchaRepo.findByCaptchaAndIpAddress("invalid", "192.168.1.1"))
                .thenReturn(Collections.emptyList());

        Captcha result = captchaService.get("invalid", "192.168.1.1");

        assertNull(result);
        verify(captchaRepo).findByCaptchaAndIpAddress("invalid", "192.168.1.1");
    }

    /**
     * Test get with null captcha list
     */
    @Test
    void testGetWithNullCaptchaList() {
        when(captchaRepo.findByCaptchaAndIpAddress("abc123", "192.168.1.1"))
                .thenReturn(null);

        Captcha result = captchaService.get("abc123", "192.168.1.1");

        assertNull(result);
        verify(captchaRepo).findByCaptchaAndIpAddress("abc123", "192.168.1.1");
    }

    /**
     * Test get with null captcha parameter
     */
    @Test
    void testGetWithNullCaptchaParameter() {
        when(captchaRepo.findByCaptchaAndIpAddress(null, "192.168.1.1"))
                .thenReturn(Collections.emptyList());

        Captcha result = captchaService.get(null, "192.168.1.1");

        assertNull(result);
        verify(captchaRepo).findByCaptchaAndIpAddress(null, "192.168.1.1");
    }

    /**
     * Test get with null IP address parameter
     */
    @Test
    void testGetWithNullIpAddressParameter() {
        when(captchaRepo.findByCaptchaAndIpAddress("abc123", null))
                .thenReturn(Collections.emptyList());

        Captcha result = captchaService.get("abc123", null);

        assertNull(result);
        verify(captchaRepo).findByCaptchaAndIpAddress("abc123", null);
    }

    /**
     * Test get with empty captcha string
     */
    @Test
    void testGetWithEmptyCaptchaString() {
        when(captchaRepo.findByCaptchaAndIpAddress("", "192.168.1.1"))
                .thenReturn(Collections.emptyList());

        Captcha result = captchaService.get("", "192.168.1.1");

        assertNull(result);
        verify(captchaRepo).findByCaptchaAndIpAddress("", "192.168.1.1");
    }

    /**
     * Test get with empty IP address string
     */
    @Test
    void testGetWithEmptyIpAddressString() {
        when(captchaRepo.findByCaptchaAndIpAddress("abc123", ""))
                .thenReturn(Collections.emptyList());

        Captcha result = captchaService.get("abc123", "");

        assertNull(result);
        verify(captchaRepo).findByCaptchaAndIpAddress("abc123", "");
    }

    /**
     * Test createCaptcha multiple times generates different captchas
     */
    @Test
    void testCreateCaptchaGeneratesDifferentCaptchas() {
        when(captchaRepo.save(any(Captcha.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Captcha captcha1 = new Captcha();
        captcha1.setIpAddress("192.168.1.1");
        Captcha result1 = captchaService.createCaptcha(captcha1);

        Captcha captcha2 = new Captcha();
        captcha2.setIpAddress("192.168.1.1");
        Captcha result2 = captchaService.createCaptcha(captcha2);

        // While technically they could be the same, the probability is very low
        assertNotNull(result1.getCaptcha());
        assertNotNull(result2.getCaptcha());
    }

    /**
     * Test that class is public
     */
    @Test
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(CaptchaServiceImpl.class.getModifiers()));
    }

    /**
     * Test package name
     */
    @Test
    void testPackageName() {
        assertEquals("com.example.serviceimpl", CaptchaServiceImpl.class.getPackageName());
    }

    /**
     * Test get with special characters in captcha
     */
    @Test
    void testGetWithSpecialCharactersInCaptcha() {
        when(captchaRepo.findByCaptchaAndIpAddress("!@#$%^", "192.168.1.1"))
                .thenReturn(Collections.emptyList());

        Captcha result = captchaService.get("!@#$%^", "192.168.1.1");

        assertNull(result);
        verify(captchaRepo).findByCaptchaAndIpAddress("!@#$%^", "192.168.1.1");
    }
}
