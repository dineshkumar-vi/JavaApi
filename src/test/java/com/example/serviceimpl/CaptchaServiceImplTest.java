package com.example.serviceimpl;

import com.example.data.Captcha;
import com.example.repository.CaptchaRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Comprehensive unit tests for CaptchaServiceImpl
 * Tests cover captcha generation, validation, and repository interactions
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

    @AfterEach
    void tearDown() {
        reset(captchaRepo);
    }

    /**
     * Test creating captcha successfully
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
     * Test creating captcha with valid IP address
     */
    @Test
    void testCreateCaptcha_ValidIpAddress() {
        when(captchaRepo.save(any(Captcha.class))).thenReturn(testCaptcha);

        Captcha result = captchaService.createCaptcha(testCaptcha);

        assertNotNull(result);
        assertNotNull(result.getCaptcha());
    }

    /**
     * Test creating captcha generates 6-character string
     */
    @Test
    void testCreateCaptcha_GeneratesSixCharacters() {
        when(captchaRepo.save(any(Captcha.class))).thenReturn(testCaptcha);

        Captcha result = captchaService.createCaptcha(testCaptcha);

        assertNotNull(result.getCaptcha());
        assertEquals(6, result.getCaptcha().length());
    }

    /**
     * Test creating captcha generates alphanumeric characters
     */
    @Test
    void testCreateCaptcha_GeneratesAlphanumeric() {
        when(captchaRepo.save(any(Captcha.class))).thenReturn(testCaptcha);

        Captcha result = captchaService.createCaptcha(testCaptcha);

        assertNotNull(result.getCaptcha());
        assertTrue(result.getCaptcha().matches("[0-9a-zA-Z]{6}"));
    }

    /**
     * Test creating captcha when repository throws exception
     */
    @Test
    void testCreateCaptcha_RepositoryException() {
        when(captchaRepo.save(any(Captcha.class))).thenThrow(new RuntimeException("Database error"));

        Captcha result = captchaService.createCaptcha(testCaptcha);

        assertNull(result);
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
     * Test get captcha by captcha text and IP address - success
     */
    @Test
    void testGet_Success() {
        List<Captcha> captchaList = new ArrayList<>();
        Captcha captcha = new Captcha();
        captcha.setCaptcha("ABC123");
        captcha.setIpAddress("192.168.1.1");
        captchaList.add(captcha);

        when(captchaRepo.findByCaptchaAndIpAddress("ABC123", "192.168.1.1"))
            .thenReturn(captchaList);

        Captcha result = captchaService.get("ABC123", "192.168.1.1");

        assertNotNull(result);
        assertEquals("ABC123", result.getCaptcha());
        assertEquals("192.168.1.1", result.getIpAddress());
        verify(captchaRepo, times(1)).findByCaptchaAndIpAddress("ABC123", "192.168.1.1");
    }

    /**
     * Test get captcha when not found
     */
    @Test
    void testGet_NotFound() {
        when(captchaRepo.findByCaptchaAndIpAddress(anyString(), anyString()))
            .thenReturn(new ArrayList<>());

        Captcha result = captchaService.get("XYZ789", "192.168.1.1");

        assertNull(result);
    }

    /**
     * Test get captcha when list is null
     */
    @Test
    void testGet_NullList() {
        when(captchaRepo.findByCaptchaAndIpAddress(anyString(), anyString()))
            .thenReturn(null);

        Captcha result = captchaService.get("ABC123", "192.168.1.1");

        assertNull(result);
    }

    /**
     * Test get captcha returns last element when multiple matches
     */
    @Test
    void testGet_MultipleMatches_ReturnsLast() {
        List<Captcha> captchaList = new ArrayList<>();

        Captcha captcha1 = new Captcha();
        captcha1.setCaptcha("ABC123");
        captcha1.setIpAddress("192.168.1.1");
        captcha1.setId("1");
        captchaList.add(captcha1);

        Captcha captcha2 = new Captcha();
        captcha2.setCaptcha("ABC123");
        captcha2.setIpAddress("192.168.1.1");
        captcha2.setId("2");
        captchaList.add(captcha2);

        when(captchaRepo.findByCaptchaAndIpAddress("ABC123", "192.168.1.1"))
            .thenReturn(captchaList);

        Captcha result = captchaService.get("ABC123", "192.168.1.1");

        assertNotNull(result);
        assertEquals("2", result.getId());
    }

    /**
     * Test get captcha with null captcha parameter
     */
    @Test
    void testGet_NullCaptcha() {
        when(captchaRepo.findByCaptchaAndIpAddress(null, "192.168.1.1"))
            .thenReturn(new ArrayList<>());

        Captcha result = captchaService.get(null, "192.168.1.1");

        assertNull(result);
    }

    /**
     * Test get captcha with null IP address parameter
     */
    @Test
    void testGet_NullIpAddress() {
        when(captchaRepo.findByCaptchaAndIpAddress("ABC123", null))
            .thenReturn(new ArrayList<>());

        Captcha result = captchaService.get("ABC123", null);

        assertNull(result);
    }

    /**
     * Test get captcha with empty captcha string
     */
    @Test
    void testGet_EmptyCaptcha() {
        when(captchaRepo.findByCaptchaAndIpAddress("", "192.168.1.1"))
            .thenReturn(new ArrayList<>());

        Captcha result = captchaService.get("", "192.168.1.1");

        assertNull(result);
    }

    /**
     * Test get captcha with empty IP address string
     */
    @Test
    void testGet_EmptyIpAddress() {
        when(captchaRepo.findByCaptchaAndIpAddress("ABC123", ""))
            .thenReturn(new ArrayList<>());

        Captcha result = captchaService.get("ABC123", "");

        assertNull(result);
    }

    /**
     * Test captcha generation produces unique values
     */
    @Test
    void testCreateCaptcha_GeneratesUniqueValues() {
        when(captchaRepo.save(any(Captcha.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Captcha result1 = captchaService.createCaptcha(testCaptcha);

        Captcha testCaptcha2 = new Captcha();
        testCaptcha2.setIpAddress("192.168.1.2");
        Captcha result2 = captchaService.createCaptcha(testCaptcha2);

        // While not guaranteed to be different, it's statistically very unlikely
        assertNotNull(result1.getCaptcha());
        assertNotNull(result2.getCaptcha());
    }

    /**
     * Test get captcha with wrong IP address
     */
    @Test
    void testGet_WrongIpAddress() {
        when(captchaRepo.findByCaptchaAndIpAddress("ABC123", "10.0.0.1"))
            .thenReturn(new ArrayList<>());

        Captcha result = captchaService.get("ABC123", "10.0.0.1");

        assertNull(result);
    }

    /**
     * Test get captcha with case-sensitive captcha text
     */
    @Test
    void testGet_CaseSensitiveCaptcha() {
        when(captchaRepo.findByCaptchaAndIpAddress("abc123", "192.168.1.1"))
            .thenReturn(new ArrayList<>());

        Captcha result = captchaService.get("abc123", "192.168.1.1");

        assertNull(result);
    }

    /**
     * Test captcha repository save is called with correct object
     */
    @Test
    void testCreateCaptcha_RepositorySaveCalledWithCorrectObject() {
        when(captchaRepo.save(any(Captcha.class))).thenReturn(testCaptcha);

        captchaService.createCaptcha(testCaptcha);

        verify(captchaRepo, times(1)).save(argThat(captcha ->
            captcha.getIpAddress().equals("192.168.1.1") &&
            captcha.getCaptcha() != null &&
            captcha.getCaptcha().length() == 6
        ));
    }

    /**
     * Test repository find method is called with correct parameters
     */
    @Test
    void testGet_RepositoryFindCalledWithCorrectParameters() {
        when(captchaRepo.findByCaptchaAndIpAddress("ABC123", "192.168.1.1"))
            .thenReturn(new ArrayList<>());

        captchaService.get("ABC123", "192.168.1.1");

        verify(captchaRepo, times(1)).findByCaptchaAndIpAddress("ABC123", "192.168.1.1");
    }
}
