package com.example.service;

import com.example.data.Captcha;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CaptchaService interface
 * Tests the service interface contract and method signatures
 */
class CaptchaServiceTest {

    /**
     * Test that CaptchaService interface exists
     */
    @Test
    void testCaptchaServiceInterfaceExists() {
        assertNotNull(CaptchaService.class);
    }

    /**
     * Test that CaptchaService is an interface
     */
    @Test
    void testCaptchaServiceIsInterface() {
        assertTrue(CaptchaService.class.isInterface());
    }

    /**
     * Test that CaptchaService has Service annotation
     */
    @Test
    void testServiceAnnotation() {
        assertTrue(CaptchaService.class.isAnnotationPresent(org.springframework.stereotype.Service.class));
    }

    /**
     * Test that createCaptcha method exists
     */
    @Test
    void testCreateCaptchaMethodExists() throws NoSuchMethodException {
        assertNotNull(CaptchaService.class.getMethod("createCaptcha", Captcha.class));
    }

    /**
     * Test that createCaptcha method returns Captcha
     */
    @Test
    void testCreateCaptchaReturnType() throws NoSuchMethodException {
        assertEquals(Captcha.class,
            CaptchaService.class.getMethod("createCaptcha", Captcha.class).getReturnType());
    }

    /**
     * Test that createCaptcha method accepts Captcha parameter
     */
    @Test
    void testCreateCaptchaParameters() throws NoSuchMethodException {
        var method = CaptchaService.class.getMethod("createCaptcha", Captcha.class);
        assertEquals(1, method.getParameterCount());
        assertEquals(Captcha.class, method.getParameterTypes()[0]);
    }

    /**
     * Test that get method exists
     */
    @Test
    void testGetMethodExists() throws NoSuchMethodException {
        assertNotNull(CaptchaService.class.getMethod("get", String.class, String.class));
    }

    /**
     * Test that get method returns Captcha
     */
    @Test
    void testGetMethodReturnType() throws NoSuchMethodException {
        assertEquals(Captcha.class,
            CaptchaService.class.getMethod("get", String.class, String.class).getReturnType());
    }

    /**
     * Test that get method accepts two String parameters
     */
    @Test
    void testGetMethodParameters() throws NoSuchMethodException {
        var method = CaptchaService.class.getMethod("get", String.class, String.class);
        assertEquals(2, method.getParameterCount());
        assertEquals(String.class, method.getParameterTypes()[0]);
        assertEquals(String.class, method.getParameterTypes()[1]);
    }

    /**
     * Test that CaptchaService has exactly 2 methods
     */
    @Test
    void testNumberOfMethods() {
        assertEquals(2, CaptchaService.class.getDeclaredMethods().length);
    }

    /**
     * Test that CaptchaService is in correct package
     */
    @Test
    void testPackage() {
        assertEquals("com.example.service", CaptchaService.class.getPackage().getName());
    }

    /**
     * Test that createCaptcha method is public
     */
    @Test
    void testCreateCaptchaMethodModifiers() throws NoSuchMethodException {
        var method = CaptchaService.class.getMethod("createCaptcha", Captcha.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isAbstract(method.getModifiers()));
    }

    /**
     * Test that get method is public
     */
    @Test
    void testGetMethodModifiers() throws NoSuchMethodException {
        var method = CaptchaService.class.getMethod("get", String.class, String.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isAbstract(method.getModifiers()));
    }

    /**
     * Test that interface has no declared fields
     */
    @Test
    void testNoDeclaredFields() {
        assertEquals(0, CaptchaService.class.getDeclaredFields().length);
    }

    /**
     * Test that interface is not an enum
     */
    @Test
    void testNotEnum() {
        assertFalse(CaptchaService.class.isEnum());
    }

    /**
     * Test that interface is not an annotation
     */
    @Test
    void testNotAnnotation() {
        assertFalse(CaptchaService.class.isAnnotation());
    }
}
