package com.example.service;

import com.example.data.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for UserService interface
 * Tests verify interface contract and method signatures
 */
class UserServiceTest {

    private Class<UserService> serviceInterface;

    @BeforeEach
    void setUp() {
        serviceInterface = UserService.class;
    }

    /**
     * Test that UserService interface exists
     */
    @Test
    void testInterfaceExists() {
        assertNotNull(serviceInterface);
        assertTrue(serviceInterface.isInterface());
    }

    /**
     * Test that UserService is annotated with @Service
     */
    @Test
    void testServiceAnnotationPresent() {
        assertTrue(serviceInterface.isAnnotationPresent(
            org.springframework.stereotype.Service.class));
    }

    /**
     * Test that checkUser method exists
     */
    @Test
    void testCheckUserMethodExists() throws NoSuchMethodException {
        assertNotNull(serviceInterface.getMethod("checkUser", User.class));
    }

    /**
     * Test that checkUser method has correct return type
     */
    @Test
    void testCheckUserReturnType() throws NoSuchMethodException {
        assertEquals(User.class,
            serviceInterface.getMethod("checkUser", User.class).getReturnType());
    }

    /**
     * Test that checkUser method has correct parameter types
     */
    @Test
    void testCheckUserParameterTypes() throws NoSuchMethodException {
        Class<?>[] paramTypes = serviceInterface.getMethod("checkUser", User.class)
            .getParameterTypes();
        assertEquals(1, paramTypes.length);
        assertEquals(User.class, paramTypes[0]);
    }

    /**
     * Test that validateCaptcha method exists
     */
    @Test
    void testValidateCaptchaMethodExists() throws NoSuchMethodException {
        assertNotNull(serviceInterface.getMethod("validateCaptcha", String.class, String.class));
    }

    /**
     * Test that validateCaptcha method has correct return type
     */
    @Test
    void testValidateCaptchaReturnType() throws NoSuchMethodException {
        assertEquals(boolean.class,
            serviceInterface.getMethod("validateCaptcha", String.class, String.class)
                .getReturnType());
    }

    /**
     * Test that validateCaptcha method has correct parameter types
     */
    @Test
    void testValidateCaptchaParameterTypes() throws NoSuchMethodException {
        Class<?>[] paramTypes = serviceInterface.getMethod("validateCaptcha", String.class, String.class)
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
     * Test that checkUser method name is correct
     */
    @Test
    void testCheckUserMethodName() throws NoSuchMethodException {
        assertEquals("checkUser",
            serviceInterface.getMethod("checkUser", User.class).getName());
    }

    /**
     * Test that validateCaptcha method name is correct
     */
    @Test
    void testValidateCaptchaMethodName() throws NoSuchMethodException {
        assertEquals("validateCaptcha",
            serviceInterface.getMethod("validateCaptcha", String.class, String.class).getName());
    }

    /**
     * Test that interface can be used as a type
     */
    @Test
    void testInterfaceAsType() {
        UserService service = null;
        assertNull(service);
        assertTrue(UserService.class.isAssignableFrom(UserService.class));
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

    /**
     * Test validateCaptcha returns primitive boolean
     */
    @Test
    void testValidateCaptchaReturnsPrimitiveBoolean() throws NoSuchMethodException {
        Class<?> returnType = serviceInterface.getMethod("validateCaptcha", String.class, String.class)
            .getReturnType();
        assertTrue(returnType.isPrimitive());
        assertEquals(boolean.class, returnType);
    }

    /**
     * Test checkUser parameter is not primitive
     */
    @Test
    void testCheckUserParameterIsNotPrimitive() throws NoSuchMethodException {
        Class<?>[] paramTypes = serviceInterface.getMethod("checkUser", User.class)
            .getParameterTypes();
        assertFalse(paramTypes[0].isPrimitive());
    }

    /**
     * Test validateCaptcha parameters are not primitive
     */
    @Test
    void testValidateCaptchaParametersAreNotPrimitive() throws NoSuchMethodException {
        Class<?>[] paramTypes = serviceInterface.getMethod("validateCaptcha", String.class, String.class)
            .getParameterTypes();
        assertFalse(paramTypes[0].isPrimitive());
        assertFalse(paramTypes[1].isPrimitive());
    }
}
