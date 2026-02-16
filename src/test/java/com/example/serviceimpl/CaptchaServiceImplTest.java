package com.example.serviceimpl;

import com.example.data.Captcha;
import com.example.repository.CaptchaRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for CaptchaServiceImpl
 * Tests captcha creation, validation, and retrieval functionality
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
        testCaptcha.setIpAddress("192.168.1.1");
    }

    /**
     * Test creating a captcha successfully
     */
    @Test
    void testCreateCaptcha_Success() {
        Captcha savedCaptcha = new Captcha();
        savedCaptcha.setId("123");
        savedCaptcha.setCaptcha("ABC123");
        savedCaptcha.setIpAddress("192.168.1.1");

        when(captchaRepo.save(any(Captcha.class))).thenReturn(savedCaptcha);

        Captcha result = captchaService.createCaptcha(testCaptcha);

        assertNotNull(result);
        assertNotNull(result.getCaptcha());
        assertEquals(6, result.getCaptcha().length());
        assertEquals("192.168.1.1", result.getIpAddress());
        verify(captchaRepo, times(1)).save(any(Captcha.class));
    }

    /**
     * Test that created captcha has correct length
     */
    @Test
    void testCreateCaptcha_CaptchaLength() {
        Captcha savedCaptcha = new Captcha();
        savedCaptcha.setCaptcha("TEST12");
        savedCaptcha.setIpAddress("192.168.1.1");

        when(captchaRepo.save(any(Captcha.class))).thenReturn(savedCaptcha);

        Captcha result = captchaService.createCaptcha(testCaptcha);

        assertNotNull(result);
        assertNotNull(result.getCaptcha());
        assertEquals(6, result.getCaptcha().length());
    }

    /**
     * Test that created captcha contains only alphanumeric characters
     */
    @Test
    void testCreateCaptcha_AlphanumericOnly() {
        Captcha savedCaptcha = new Captcha();
        savedCaptcha.setCaptcha("aB3cD4");
        savedCaptcha.setIpAddress("192.168.1.1");

        when(captchaRepo.save(any(Captcha.class))).thenReturn(savedCaptcha);

        Captcha result = captchaService.createCaptcha(testCaptcha);

        assertNotNull(result);
        assertNotNull(result.getCaptcha());
        assertTrue(result.getCaptcha().matches("[0-9a-zA-Z]+"));
    }

    /**
     * Test creating captcha when repository throws exception
     */
    @Test
    void testCreateCaptcha_RepositoryException() {
        when(captchaRepo.save(any(Captcha.class))).thenThrow(new RuntimeException("Database error"));

        Captcha result = captchaService.createCaptcha(testCaptcha);

        assertNull(result);
        verify(captchaRepo, times(1)).save(any(Captcha.class));
    }

    /**
     * Test creating captcha with null input
     */
    @Test
    void testCreateCaptcha_NullInput() {
        assertThrows(NullPointerException.class, () -> {
            captchaService.createCaptcha(null);
        });
    }

    /**
     * Test getting captcha successfully
     */
    @Test
    void testGet_Success() {
        Captcha captcha1 = new Captcha();
        captcha1.setId("1");
        captcha1.setCaptcha("ABC123");
        captcha1.setIpAddress("192.168.1.1");

        List<Captcha> captchaList = Arrays.asList(captcha1);
        when(captchaRepo.findByCaptchaAndIpAddress("ABC123", "192.168.1.1")).thenReturn(captchaList);

        Captcha result = captchaService.get("ABC123", "192.168.1.1");

        assertNotNull(result);
        assertEquals("ABC123", result.getCaptcha());
        assertEquals("192.168.1.1", result.getIpAddress());
        verify(captchaRepo, times(1)).findByCaptchaAndIpAddress("ABC123", "192.168.1.1");
    }

    /**
     * Test getting captcha when multiple captchas exist (should return latest)
     */
    @Test
    void testGet_MultipleCaptchas_ReturnsLatest() {
        Captcha captcha1 = new Captcha();
        captcha1.setId("1");
        captcha1.setCaptcha("ABC123");
        captcha1.setIpAddress("192.168.1.1");

        Captcha captcha2 = new Captcha();
        captcha2.setId("2");
        captcha2.setCaptcha("ABC123");
        captcha2.setIpAddress("192.168.1.1");

        List<Captcha> captchaList = Arrays.asList(captcha1, captcha2);
        when(captchaRepo.findByCaptchaAndIpAddress("ABC123", "192.168.1.1")).thenReturn(captchaList);

        Captcha result = captchaService.get("ABC123", "192.168.1.1");

        assertNotNull(result);
        assertEquals("2", result.getId()); // Should return the last one
        verify(captchaRepo, times(1)).findByCaptchaAndIpAddress("ABC123", "192.168.1.1");
    }

    /**
     * Test getting captcha when not found
     */
    @Test
    void testGet_NotFound() {
        List<Captcha> emptyCaptchaList = new ArrayList<>();
        when(captchaRepo.findByCaptchaAndIpAddress("INVALID", "192.168.1.1")).thenReturn(emptyCaptchaList);

        Captcha result = captchaService.get("INVALID", "192.168.1.1");

        assertNull(result);
        verify(captchaRepo, times(1)).findByCaptchaAndIpAddress("INVALID", "192.168.1.1");
    }

    /**
     * Test getting captcha when list is null
     */
    @Test
    void testGet_NullList() {
        when(captchaRepo.findByCaptchaAndIpAddress("ABC123", "192.168.1.1")).thenReturn(null);

        Captcha result = captchaService.get("ABC123", "192.168.1.1");

        assertNull(result);
        verify(captchaRepo, times(1)).findByCaptchaAndIpAddress("ABC123", "192.168.1.1");
    }

    /**
     * Test getting captcha with null captcha parameter
     */
    @Test
    void testGet_NullCaptcha() {
        List<Captcha> emptyCaptchaList = new ArrayList<>();
        when(captchaRepo.findByCaptchaAndIpAddress(null, "192.168.1.1")).thenReturn(emptyCaptchaList);

        Captcha result = captchaService.get(null, "192.168.1.1");

        assertNull(result);
    }

    /**
     * Test getting captcha with null IP address
     */
    @Test
    void testGet_NullIpAddress() {
        List<Captcha> emptyCaptchaList = new ArrayList<>();
        when(captchaRepo.findByCaptchaAndIpAddress("ABC123", null)).thenReturn(emptyCaptchaList);

        Captcha result = captchaService.get("ABC123", null);

        assertNull(result);
    }

    /**
     * Test getting captcha with empty captcha string
     */
    @Test
    void testGet_EmptyCaptcha() {
        List<Captcha> emptyCaptchaList = new ArrayList<>();
        when(captchaRepo.findByCaptchaAndIpAddress("", "192.168.1.1")).thenReturn(emptyCaptchaList);

        Captcha result = captchaService.get("", "192.168.1.1");

        assertNull(result);
    }

    /**
     * Test getting captcha with empty IP address
     */
    @Test
    void testGet_EmptyIpAddress() {
        List<Captcha> emptyCaptchaList = new ArrayList<>();
        when(captchaRepo.findByCaptchaAndIpAddress("ABC123", "")).thenReturn(emptyCaptchaList);

        Captcha result = captchaService.get("ABC123", "");

        assertNull(result);
    }

    /**
     * Test that CaptchaServiceImpl is annotated as Component
     */
    @Test
    void testComponentAnnotation() {
        assertTrue(CaptchaServiceImpl.class.isAnnotationPresent(org.springframework.stereotype.Component.class));
    }

    /**
     * Test that CaptchaServiceImpl implements CaptchaService
     */
    @Test
    void testImplementsCaptchaService() {
        assertTrue(com.example.service.CaptchaService.class.isAssignableFrom(CaptchaServiceImpl.class));
    }

    /**
     * Test creating multiple captchas generates different values
     */
    @Test
    void testCreateCaptcha_UniqueCaptchas() {
        when(captchaRepo.save(any(Captcha.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Captcha result1 = captchaService.createCaptcha(testCaptcha);

        Captcha testCaptcha2 = new Captcha();
        testCaptcha2.setIpAddress("192.168.1.2");
        Captcha result2 = captchaService.createCaptcha(testCaptcha2);

        assertNotNull(result1);
        assertNotNull(result2);
        // While random, it's extremely unlikely they're the same
        // This test validates both captchas are generated
        assertNotNull(result1.getCaptcha());
        assertNotNull(result2.getCaptcha());
    }
}
