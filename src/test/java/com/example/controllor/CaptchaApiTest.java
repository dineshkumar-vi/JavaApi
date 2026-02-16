package com.example.controllor;

import com.example.data.Captcha;
import com.example.service.CaptchaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for CaptchaApi
 * Tests cover REST endpoints, request validation, and response handling
 */
@ExtendWith(MockitoExtension.class)
class CaptchaApiTest {

    @Mock
    private CaptchaService captchaService;

    @InjectMocks
    private CaptchaApi captchaApi;

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
     * Test that @RestController annotation is present
     */
    @Test
    void testRestControllerAnnotationPresent() {
        assertTrue(CaptchaApi.class.isAnnotationPresent(RestController.class));
    }

    /**
     * Test that @RequestMapping annotation is present with correct path
     */
    @Test
    void testRequestMappingAnnotationPresent() {
        assertTrue(CaptchaApi.class.isAnnotationPresent(RequestMapping.class));
        RequestMapping mapping = CaptchaApi.class.getAnnotation(RequestMapping.class);
        assertArrayEquals(new String[]{"/captcha"}, mapping.path());
    }

    /**
     * Test create endpoint with valid captcha
     */
    @Test
    void testCreateWithValidCaptcha() {
        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(testCaptcha);

        ResponseEntity<Captcha> response = captchaApi.create(testCaptcha);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("abc123", response.getBody().getCaptcha());
        verify(captchaService).createCaptcha(any(Captcha.class));
    }

    /**
     * Test create endpoint with null captcha
     */
    @Test
    void testCreateWithNullCaptcha() {
        ResponseEntity<Captcha> response = captchaApi.create(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(captchaService, never()).createCaptcha(any());
    }

    /**
     * Test create endpoint with null IP address
     */
    @Test
    void testCreateWithNullIpAddress() {
        testCaptcha.setIpAddress(null);

        ResponseEntity<Captcha> response = captchaApi.create(testCaptcha);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(captchaService, never()).createCaptcha(any());
    }

    /**
     * Test create endpoint with empty IP address
     */
    @Test
    void testCreateWithEmptyIpAddress() {
        testCaptcha.setIpAddress("");
        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(testCaptcha);

        ResponseEntity<Captcha> response = captchaApi.create(testCaptcha);

        // Empty string is not null, so it should be accepted
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(captchaService).createCaptcha(any(Captcha.class));
    }

    /**
     * Test create endpoint when service returns null
     */
    @Test
    void testCreateWhenServiceReturnsNull() {
        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(null);

        ResponseEntity<Captcha> response = captchaApi.create(testCaptcha);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(captchaService).createCaptcha(any(Captcha.class));
    }

    /**
     * Test get endpoint with valid parameters
     */
    @Test
    void testGetWithValidParameters() {
        when(captchaService.get("abc123", "192.168.1.1")).thenReturn(testCaptcha);

        ResponseEntity<Captcha> response = captchaApi.get("abc123", "192.168.1.1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("abc123", response.getBody().getCaptcha());
        verify(captchaService).get("abc123", "192.168.1.1");
    }

    /**
     * Test get endpoint with null IP address
     */
    @Test
    void testGetWithNullIpAddress() {
        ResponseEntity<Captcha> response = captchaApi.get("abc123", null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(captchaService, never()).get(anyString(), any());
    }

    /**
     * Test get endpoint with null captcha
     */
    @Test
    void testGetWithNullCaptcha() {
        when(captchaService.get(null, "192.168.1.1")).thenReturn(null);

        ResponseEntity<Captcha> response = captchaApi.get(null, "192.168.1.1");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(captchaService).get(null, "192.168.1.1");
    }

    /**
     * Test get endpoint when service returns null
     */
    @Test
    void testGetWhenServiceReturnsNull() {
        when(captchaService.get("invalid", "192.168.1.1")).thenReturn(null);

        ResponseEntity<Captcha> response = captchaApi.get("invalid", "192.168.1.1");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(captchaService).get("invalid", "192.168.1.1");
    }

    /**
     * Test get endpoint with empty captcha string
     */
    @Test
    void testGetWithEmptyCaptcha() {
        when(captchaService.get("", "192.168.1.1")).thenReturn(null);

        ResponseEntity<Captcha> response = captchaApi.get("", "192.168.1.1");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(captchaService).get("", "192.168.1.1");
    }

    /**
     * Test get endpoint with empty IP address
     */
    @Test
    void testGetWithEmptyIpAddress() {
        when(captchaService.get("abc123", "")).thenReturn(testCaptcha);

        ResponseEntity<Captcha> response = captchaApi.get("abc123", "");

        // Empty string is not null, so it should proceed to service
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(captchaService).get("abc123", "");
    }

    /**
     * Test that create method has @PostMapping annotation
     */
    @Test
    void testCreateMethodHasPostMapping() throws NoSuchMethodException {
        var method = CaptchaApi.class.getDeclaredMethod("create", Captcha.class);
        assertTrue(method.isAnnotationPresent(PostMapping.class));
    }

    /**
     * Test that get method has @GetMapping annotation
     */
    @Test
    void testGetMethodHasGetMapping() throws NoSuchMethodException {
        var method = CaptchaApi.class.getDeclaredMethod("get", String.class, String.class);
        assertTrue(method.isAnnotationPresent(GetMapping.class));
    }

    /**
     * Test create with special characters in IP address
     */
    @Test
    void testCreateWithSpecialCharactersInIpAddress() {
        testCaptcha.setIpAddress("192.168.1.1:8080");
        when(captchaService.createCaptcha(any(Captcha.class))).thenReturn(testCaptcha);

        ResponseEntity<Captcha> response = captchaApi.create(testCaptcha);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(captchaService).createCaptcha(any(Captcha.class));
    }

    /**
     * Test get with IPv6 address
     */
    @Test
    void testGetWithIpv6Address() {
        String ipv6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        when(captchaService.get("abc123", ipv6)).thenReturn(testCaptcha);

        ResponseEntity<Captcha> response = captchaApi.get("abc123", ipv6);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(captchaService).get("abc123", ipv6);
    }

    /**
     * Test that class is public
     */
    @Test
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(CaptchaApi.class.getModifiers()));
    }

    /**
     * Test package name
     */
    @Test
    void testPackageName() {
        assertEquals("com.example.controllor", CaptchaApi.class.getPackageName());
    }

    /**
     * Test get with very long captcha string
     */
    @Test
    void testGetWithLongCaptcha() {
        String longCaptcha = "a".repeat(1000);
        when(captchaService.get(longCaptcha, "192.168.1.1")).thenReturn(null);

        ResponseEntity<Captcha> response = captchaApi.get(longCaptcha, "192.168.1.1");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(captchaService).get(longCaptcha, "192.168.1.1");
    }
}
