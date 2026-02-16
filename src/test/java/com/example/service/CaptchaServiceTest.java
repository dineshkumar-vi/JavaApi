package com.example.service;

import com.example.data.Captcha;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for CaptchaService interface
 * Tests verify interface contract and method signatures
 */
class CaptchaServiceTest {

    private Class<CaptchaService> serviceInterface;

    @BeforeEach
    void setUp() {
        serviceInterface = CaptchaService.class;
    }

    /**
     * Test that CaptchaService interface exists
     */
    @Test
    void testInterfaceExists() {
        assertNotNull(serviceInterface);
        assertTrue(serviceInterface.isInterface());
    }

    /**
     * Test that CaptchaService is annotated with @Service
     */
    @Test
    void testServiceAnnotationPresent() {
        assertTrue(serviceInterface.isAnnotationPresent(
            org.springframework.stereotype.Service.class));
    }

    /**
     * Test that createCaptcha method exists
     */
    @Test
    void testCreateCaptchaMethodExists() throws NoSuchMethodException {
        assertNotNull(serviceInterface.getMethod("createCaptcha", Captcha.class));
    }

    /**
     * Test that createCaptcha method has correct return type
     */
    @Test
    void testCreateCaptchaReturnType() throws NoSuchMethodException {
        assertEquals(Captcha.class,
            serviceInterface.getMethod("createCaptcha", Captcha.class).getReturnType());
    }

    /**
     * Test that createCaptcha method has correct parameter types
     */
    @Test
    void testCreateCaptchaParameterTypes() throws NoSuchMethodException {
        Class<?>[] paramTypes = serviceInterface.getMethod("createCaptcha", Captcha.class)
            .getParameterTypes();
        assertEquals(1, paramTypes.length);
        assertEquals(Captcha.class, paramTypes[0]);
    }

    /**
     * Test that get method exists
     */
    @Test
    void testGetMethodExists() throws NoSuchMethodException {
        assertNotNull(serviceInterface.getMethod("get", String.class, String.class));
    }

    /**
     * Test that get method has correct return type
     */
    @Test
    void testGetMethodReturnType() throws NoSuchMethodException {
        assertEquals(Captcha.class,
            serviceInterface.getMethod("get", String.class, String.class).getReturnType());
    }

    /**
     * Test that get method has correct parameter types
     */
    @Test
    void testGetMethodParameterTypes() throws NoSuchMethodException {
        Class<?>[] paramTypes = serviceInterface.getMethod("get", String.class, String.class)
            .getParameterTypes();
        assertEquals(2, paramTypes.length);
        assertEquals(String.class, paramTypes[0]);
        assertEquals(String.class, paramTypes[1]);
    }

    /**
     * Test that interface is in correct package
     */
    @Test
    void testInterfacePackage() {
        assertEquals("com.example.service", serviceInterface.getPackageName());
    }

    /**
     * Test that interface has exactly 2 methods
     */
    @Test
    void testInterfaceMethodCount() {
        assertEquals(2, serviceInterface.getDeclaredMethods().length);
    }

    /**
     * Test that interface is public
     */
    @Test
    void testInterfaceIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(serviceInterface.getModifiers()));
    }

    /**
     * Test that createCaptcha method name is correct
     */
    @Test
    void testCreateCaptchaMethodName() throws NoSuchMethodException {
        assertEquals("createCaptcha",
            serviceInterface.getMethod("createCaptcha", Captcha.class).getName());
    }

    /**
     * Test that get method name is correct
     */
    @Test
    void testGetMethodName() throws NoSuchMethodException {
        assertEquals("get",
            serviceInterface.getMethod("get", String.class, String.class).getName());
    }

    /**
     * Test that interface can be used as a type
     */
    @Test
    void testInterfaceAsType() {
        CaptchaService service = null;
        assertNull(service);
        assertTrue(CaptchaService.class.isAssignableFrom(CaptchaService.class));
    }

    /**
     * Test that Service annotation is present
     */
    @Test
    void testServiceAnnotation() {
        org.springframework.stereotype.Service annotation =
            serviceInterface.getAnnotation(org.springframework.stereotype.Service.class);
        assertNotNull(annotation);
    }

    /**
     * Test interface has no default methods
     */
    @Test
    void testNoDefaultMethods() {
        long defaultMethodCount = java.util.Arrays.stream(serviceInterface.getMethods())
            .filter(java.lang.reflect.Method::isDefault)
            .count();
        assertEquals(0, defaultMethodCount);
    }

    /**
     * Test interface naming convention
     */
    @Test
    void testInterfaceNamingConvention() {
        String interfaceName = serviceInterface.getSimpleName();
        assertTrue(interfaceName.endsWith("Service"));
    }
}
