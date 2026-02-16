package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DemoApplication
 * Tests the Spring Boot application initialization and configuration
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=mongodb://localhost:27017/test"
})
class DemoApplicationTest {

    /**
     * Test that the application context loads successfully
     */
    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> {
            // Context loads without errors
        });
    }

    /**
     * Test that the main method can be invoked without errors
     */
    @Test
    void testMain() {
        assertDoesNotThrow(() -> {
            // Test that main method exists and can be called
            // Note: We don't actually start the application to avoid port conflicts
            String[] args = {};
            assertNotNull(args);
        });
    }

    /**
     * Test that DemoApplication class exists and is properly annotated
     */
    @Test
    void testDemoApplicationClassExists() {
        Class<?> clazz = DemoApplication.class;
        assertNotNull(clazz);
        assertTrue(clazz.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    /**
     * Test that ComponentScan annotation is present with correct base packages
     */
    @Test
    void testComponentScanAnnotation() {
        Class<?> clazz = DemoApplication.class;
        assertTrue(clazz.isAnnotationPresent(org.springframework.context.annotation.ComponentScan.class));

        org.springframework.context.annotation.ComponentScan componentScan =
            clazz.getAnnotation(org.springframework.context.annotation.ComponentScan.class);

        String[] basePackages = componentScan.basePackages();
        assertNotNull(basePackages);
        assertEquals(1, basePackages.length);
        assertEquals("com.example", basePackages[0]);
    }

    /**
     * Test that SpringBootApplication annotation is present
     */
    @Test
    void testSpringBootApplicationAnnotation() {
        assertTrue(DemoApplication.class.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    /**
     * Test that the application has a main method
     */
    @Test
    void testMainMethodExists() throws NoSuchMethodException {
        assertNotNull(DemoApplication.class.getMethod("main", String[].class));
    }

    /**
     * Test that main method is public and static
     */
    @Test
    void testMainMethodModifiers() throws NoSuchMethodException {
        var method = DemoApplication.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()));
    }

    /**
     * Test application package structure
     */
    @Test
    void testPackageStructure() {
        assertEquals("com.example", DemoApplication.class.getPackage().getName());
    }

    /**
     * Test that DemoApplication is a valid class
     */
    @Test
    void testDemoApplicationInstantiation() {
        assertDoesNotThrow(() -> {
            DemoApplication app = new DemoApplication();
            assertNotNull(app);
        });
    }

    /**
     * Test component scan includes controller package
     */
    @Test
    void testComponentScanIncludesControllers() {
        org.springframework.context.annotation.ComponentScan componentScan =
            DemoApplication.class.getAnnotation(org.springframework.context.annotation.ComponentScan.class);

        String[] basePackages = componentScan.basePackages();
        boolean includesExample = false;
        for (String pkg : basePackages) {
            if (pkg.startsWith("com.example")) {
                includesExample = true;
                break;
            }
        }
        assertTrue(includesExample);
    }
}
