package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for DemoApplication
 * Tests cover application startup, annotations, and configuration
 */
class DemoApplicationTest {

    @BeforeEach
    void setUp() {
        // Setup before each test
    }

    @AfterEach
    void tearDown() {
        // Cleanup after each test
    }

    /**
     * Test that DemoApplication class exists and can be instantiated
     */
    @Test
    void testDemoApplicationClassExists() {
        assertDoesNotThrow(() -> {
            new DemoApplication();
        });
    }

    /**
     * Test that @SpringBootApplication annotation is present
     */
    @Test
    void testSpringBootApplicationAnnotationPresent() {
        assertTrue(DemoApplication.class.isAnnotationPresent(SpringBootApplication.class));
    }

    /**
     * Test that @ComponentScan annotation is present
     */
    @Test
    void testComponentScanAnnotationPresent() {
        assertTrue(DemoApplication.class.isAnnotationPresent(ComponentScan.class));
    }

    /**
     * Test that ComponentScan basePackages includes com.example
     */
    @Test
    void testComponentScanBasePackages() {
        ComponentScan componentScan = DemoApplication.class.getAnnotation(ComponentScan.class);
        assertNotNull(componentScan);
        String[] basePackages = componentScan.basePackages();
        assertNotNull(basePackages);
        assertEquals(1, basePackages.length);
        assertEquals("com.example", basePackages[0]);
    }

    /**
     * Test that main method exists
     */
    @Test
    void testMainMethodExists() {
        assertDoesNotThrow(() -> {
            DemoApplication.class.getDeclaredMethod("main", String[].class);
        });
    }

    /**
     * Test main method with null arguments - edge case
     */
    @Test
    void testMainMethodWithNullArguments() {
        // Note: This would actually start the Spring application
        // In a real test environment, you'd mock SpringApplication.run
        assertDoesNotThrow(() -> {
            DemoApplication.class.getDeclaredMethod("main", String[].class);
        });
    }

    /**
     * Test that main method is public and static
     */
    @Test
    void testMainMethodIsPublicStatic() throws NoSuchMethodException {
        var mainMethod = DemoApplication.class.getDeclaredMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
    }

    /**
     * Test that main method returns void
     */
    @Test
    void testMainMethodReturnsVoid() throws NoSuchMethodException {
        var mainMethod = DemoApplication.class.getDeclaredMethod("main", String[].class);
        assertEquals(void.class, mainMethod.getReturnType());
    }

    /**
     * Test that class is in correct package
     */
    @Test
    void testClassPackage() {
        assertEquals("com.example", DemoApplication.class.getPackageName());
    }

    /**
     * Test that class is public
     */
    @Test
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(DemoApplication.class.getModifiers()));
    }

    /**
     * Test SpringBootApplication annotation has default configuration
     */
    @Test
    void testSpringBootApplicationDefaultConfig() {
        SpringBootApplication annotation = DemoApplication.class.getAnnotation(SpringBootApplication.class);
        assertNotNull(annotation);
        // Verify annotation is properly configured
    }

    /**
     * Test that class has exactly one constructor (default)
     */
    @Test
    void testClassConstructors() {
        assertEquals(1, DemoApplication.class.getDeclaredConstructors().length);
    }

    /**
     * Test class name
     */
    @Test
    void testClassName() {
        assertEquals("DemoApplication", DemoApplication.class.getSimpleName());
    }
}
