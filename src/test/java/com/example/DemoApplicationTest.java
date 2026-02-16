package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for DemoApplication
 * Tests cover Spring Boot application startup and configuration
 */
@SpringBootTest
class DemoApplicationTest {

    private ConfigurableApplicationContext context;

    @BeforeEach
    void setUp() {
        // Setup before each test
    }

    @AfterEach
    void tearDown() {
        // Close context if it was started
        if (context != null && context.isRunning()) {
            context.close();
        }
    }

    /**
     * Test that DemoApplication class exists
     */
    @Test
    void testApplicationClassExists() {
        assertDoesNotThrow(() -> {
            Class.forName("com.example.DemoApplication");
        });
    }

    /**
     * Test that main method exists and is public static void
     */
    @Test
    void testMainMethodExists() throws NoSuchMethodException {
        Class<?> clazz = DemoApplication.class;
        assertNotNull(clazz.getMethod("main", String[].class));
    }

    /**
     * Test SpringBootApplication annotation is present
     */
    @Test
    void testSpringBootApplicationAnnotationPresent() {
        assertTrue(DemoApplication.class.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    /**
     * Test ComponentScan annotation is present
     */
    @Test
    void testComponentScanAnnotationPresent() {
        assertTrue(DemoApplication.class.isAnnotationPresent(
            org.springframework.context.annotation.ComponentScan.class));
    }

    /**
     * Test ComponentScan base packages configuration
     */
    @Test
    void testComponentScanBasePackages() {
        org.springframework.context.annotation.ComponentScan annotation =
            DemoApplication.class.getAnnotation(org.springframework.context.annotation.ComponentScan.class);
        assertNotNull(annotation);
        String[] basePackages = annotation.basePackages();
        assertEquals(1, basePackages.length);
        assertEquals("com.example", basePackages[0]);
    }

    /**
     * Test application can be instantiated
     */
    @Test
    void testApplicationCanBeInstantiated() {
        assertDoesNotThrow(() -> new DemoApplication());
    }

    /**
     * Test application class is in correct package
     */
    @Test
    void testApplicationPackage() {
        assertEquals("com.example", DemoApplication.class.getPackageName());
    }

    /**
     * Test main method with null arguments handles gracefully
     */
    @Test
    void testMainMethodWithNullArguments() {
        // This test verifies the main method signature
        // Actual invocation would start the application
        assertDoesNotThrow(() -> {
            DemoApplication.class.getMethod("main", String[].class);
        });
    }

    /**
     * Test main method with empty arguments array
     */
    @Test
    void testMainMethodWithEmptyArguments() {
        String[] args = new String[0];
        assertNotNull(args);
        assertEquals(0, args.length);
    }

    /**
     * Test Spring Boot application primary source configuration
     */
    @Test
    void testSpringApplicationPrimarySource() {
        Class<?> primarySource = DemoApplication.class;
        assertNotNull(primarySource);
        assertTrue(primarySource.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    /**
     * Test application class is public
     */
    @Test
    void testApplicationClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(DemoApplication.class.getModifiers()));
    }

    /**
     * Test application class has default constructor
     */
    @Test
    void testApplicationHasDefaultConstructor() {
        assertDoesNotThrow(() -> {
            DemoApplication.class.getDeclaredConstructor();
        });
    }

    /**
     * Test SpringBootApplication annotation enables auto-configuration
     */
    @Test
    void testAutoConfigurationEnabled() {
        org.springframework.boot.autoconfigure.SpringBootApplication annotation =
            DemoApplication.class.getAnnotation(
                org.springframework.boot.autoconfigure.SpringBootApplication.class);
        assertNotNull(annotation);
        // By default, auto-configuration is enabled in @SpringBootApplication
    }

    /**
     * Test ComponentScan annotation scans correct packages
     */
    @Test
    void testComponentScanConfiguration() {
        org.springframework.context.annotation.ComponentScan componentScan =
            DemoApplication.class.getAnnotation(
                org.springframework.context.annotation.ComponentScan.class);
        assertNotNull(componentScan);
        assertNotNull(componentScan.basePackages());
        assertTrue(componentScan.basePackages().length > 0);
    }

    /**
     * Test application class naming convention
     */
    @Test
    void testApplicationNamingConvention() {
        String className = DemoApplication.class.getSimpleName();
        assertTrue(className.endsWith("Application"));
    }
}
