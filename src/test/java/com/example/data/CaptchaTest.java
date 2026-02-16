package com.example.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for Captcha data model
 * Tests cover getters, setters, MongoDB annotations, and data integrity
 */
class CaptchaTest {

    private Captcha captcha;

    @BeforeEach
    void setUp() {
        captcha = new Captcha();
    }

    @AfterEach
    void tearDown() {
        captcha = null;
    }

    /**
     * Test Captcha class can be instantiated
     */
    @Test
    void testCaptchaInstantiation() {
        assertNotNull(captcha);
    }

    /**
     * Test setting and getting ID
     */
    @Test
    void testSetAndGetId() {
        captcha.setId("123");
        assertEquals("123", captcha.getId());
    }

    /**
     * Test setting and getting captcha text
     */
    @Test
    void testSetAndGetCaptcha() {
        captcha.setCaptcha("ABC123");
        assertEquals("ABC123", captcha.getCaptcha());
    }

    /**
     * Test setting and getting IP address
     */
    @Test
    void testSetAndGetIpAddress() {
        captcha.setIpAddress("192.168.1.1");
        assertEquals("192.168.1.1", captcha.getIpAddress());
    }

    /**
     * Test all fields initially null
     */
    @Test
    void testAllFieldsInitiallyNull() {
        Captcha newCaptcha = new Captcha();
        assertNull(newCaptcha.getId());
        assertNull(newCaptcha.getCaptcha());
        assertNull(newCaptcha.getIpAddress());
    }

    /**
     * Test setting ID to null
     */
    @Test
    void testSetIdToNull() {
        captcha.setId("123");
        captcha.setId(null);
        assertNull(captcha.getId());
    }

    /**
     * Test setting captcha to null
     */
    @Test
    void testSetCaptchaToNull() {
        captcha.setCaptcha("ABC123");
        captcha.setCaptcha(null);
        assertNull(captcha.getCaptcha());
    }

    /**
     * Test setting IP address to null
     */
    @Test
    void testSetIpAddressToNull() {
        captcha.setIpAddress("192.168.1.1");
        captcha.setIpAddress(null);
        assertNull(captcha.getIpAddress());
    }

    /**
     * Test setting empty ID
     */
    @Test
    void testSetEmptyId() {
        captcha.setId("");
        assertEquals("", captcha.getId());
    }

    /**
     * Test setting empty captcha
     */
    @Test
    void testSetEmptyCaptcha() {
        captcha.setCaptcha("");
        assertEquals("", captcha.getCaptcha());
    }

    /**
     * Test setting empty IP address
     */
    @Test
    void testSetEmptyIpAddress() {
        captcha.setIpAddress("");
        assertEquals("", captcha.getIpAddress());
    }

    /**
     * Test setting all fields at once
     */
    @Test
    void testSetAllFields() {
        captcha.setId("123");
        captcha.setCaptcha("ABC123");
        captcha.setIpAddress("192.168.1.1");

        assertEquals("123", captcha.getId());
        assertEquals("ABC123", captcha.getCaptcha());
        assertEquals("192.168.1.1", captcha.getIpAddress());
    }

    /**
     * Test toString method
     */
    @Test
    void testToString() {
        captcha.setId("123");
        captcha.setCaptcha("ABC123");
        captcha.setIpAddress("192.168.1.1");

        String result = captcha.toString();
        assertNotNull(result);
        assertTrue(result.contains("123"));
        assertTrue(result.contains("ABC123"));
        assertTrue(result.contains("192.168.1.1"));
    }

    /**
     * Test toString with null fields
     */
    @Test
    void testToStringWithNullFields() {
        String result = captcha.toString();
        assertNotNull(result);
        assertTrue(result.contains("null"));
    }

    /**
     * Test toString format
     */
    @Test
    void testToStringFormat() {
        captcha.setId("123");
        captcha.setCaptcha("ABC123");
        captcha.setIpAddress("192.168.1.1");

        String expected = "Captcha[id=123, captcha='ABC123', ipAddress='192.168.1.1']";
        assertEquals(expected, captcha.toString());
    }

    /**
     * Test MongoDB Document annotation is present
     */
    @Test
    void testDocumentAnnotationPresent() {
        assertTrue(Captcha.class.isAnnotationPresent(
            org.springframework.data.mongodb.core.mapping.Document.class));
    }

    /**
     * Test MongoDB Document collection name
     */
    @Test
    void testDocumentCollectionName() {
        org.springframework.data.mongodb.core.mapping.Document annotation =
            Captcha.class.getAnnotation(
                org.springframework.data.mongodb.core.mapping.Document.class);
        assertNotNull(annotation);
        assertEquals("captcha", annotation.collection());
    }

    /**
     * Test ID field has @Id annotation
     */
    @Test
    void testIdFieldAnnotation() throws NoSuchFieldException {
        assertTrue(Captcha.class.getDeclaredField("id").isAnnotationPresent(
            org.springframework.data.annotation.Id.class));
    }

    /**
     * Test captcha field has @Field annotation
     */
    @Test
    void testCaptchaFieldAnnotation() throws NoSuchFieldException {
        assertTrue(Captcha.class.getDeclaredField("captcha").isAnnotationPresent(
            org.springframework.data.mongodb.core.mapping.Field.class));
    }

    /**
     * Test captcha field @Field annotation value
     */
    @Test
    void testCaptchaFieldAnnotationValue() throws NoSuchFieldException {
        org.springframework.data.mongodb.core.mapping.Field annotation =
            Captcha.class.getDeclaredField("captcha").getAnnotation(
                org.springframework.data.mongodb.core.mapping.Field.class);
        assertNotNull(annotation);
        assertEquals("captcha", annotation.value());
    }

    /**
     * Test ipAddress field has @Field annotation
     */
    @Test
    void testIpAddressFieldAnnotation() throws NoSuchFieldException {
        assertTrue(Captcha.class.getDeclaredField("ipAddress").isAnnotationPresent(
            org.springframework.data.mongodb.core.mapping.Field.class));
    }

    /**
     * Test ipAddress field @Field annotation value
     */
    @Test
    void testIpAddressFieldAnnotationValue() throws NoSuchFieldException {
        org.springframework.data.mongodb.core.mapping.Field annotation =
            Captcha.class.getDeclaredField("ipAddress").getAnnotation(
                org.springframework.data.mongodb.core.mapping.Field.class);
        assertNotNull(annotation);
        assertEquals("ipAddress", annotation.value());
    }

    /**
     * Test captcha with alphanumeric characters
     */
    @Test
    void testCaptchaAlphanumeric() {
        captcha.setCaptcha("aB3Xy9");
        assertEquals("aB3Xy9", captcha.getCaptcha());
    }

    /**
     * Test captcha with 6 characters (typical length)
     */
    @Test
    void testCaptchaSixCharacters() {
        captcha.setCaptcha("AbC123");
        assertEquals("AbC123", captcha.getCaptcha());
        assertEquals(6, captcha.getCaptcha().length());
    }

    /**
     * Test IPv4 address format
     */
    @Test
    void testIPv4Address() {
        captcha.setIpAddress("192.168.1.1");
        assertEquals("192.168.1.1", captcha.getIpAddress());
    }

    /**
     * Test IPv6 address format
     */
    @Test
    void testIPv6Address() {
        captcha.setIpAddress("2001:0db8:85a3:0000:0000:8a2e:0370:7334");
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", captcha.getIpAddress());
    }

    /**
     * Test updating captcha text multiple times
     */
    @Test
    void testUpdateCaptchaMultipleTimes() {
        captcha.setCaptcha("ABC123");
        assertEquals("ABC123", captcha.getCaptcha());

        captcha.setCaptcha("XYZ789");
        assertEquals("XYZ789", captcha.getCaptcha());

        captcha.setCaptcha("DEF456");
        assertEquals("DEF456", captcha.getCaptcha());
    }

    /**
     * Test updating IP address multiple times
     */
    @Test
    void testUpdateIpAddressMultipleTimes() {
        captcha.setIpAddress("192.168.1.1");
        assertEquals("192.168.1.1", captcha.getIpAddress());

        captcha.setIpAddress("10.0.0.1");
        assertEquals("10.0.0.1", captcha.getIpAddress());
    }

    /**
     * Test very long ID
     */
    @Test
    void testVeryLongId() {
        String longId = "a".repeat(100);
        captcha.setId(longId);
        assertEquals(longId, captcha.getId());
        assertEquals(100, captcha.getId().length());
    }

    /**
     * Test Captcha class is in correct package
     */
    @Test
    void testCaptchaPackage() {
        assertEquals("com.example.data", Captcha.class.getPackageName());
    }

    /**
     * Test Captcha class is public
     */
    @Test
    void testCaptchaClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(Captcha.class.getModifiers()));
    }

    /**
     * Test Captcha has default constructor
     */
    @Test
    void testCaptchaHasDefaultConstructor() {
        assertDoesNotThrow(() -> Captcha.class.getDeclaredConstructor());
    }

    /**
     * Test captcha with special characters
     */
    @Test
    void testCaptchaWithSpecialCharacters() {
        captcha.setCaptcha("A@B#C$");
        assertEquals("A@B#C$", captcha.getCaptcha());
    }

    /**
     * Test ID with MongoDB ObjectId format
     */
    @Test
    void testIdWithObjectIdFormat() {
        captcha.setId("507f1f77bcf86cd799439011");
        assertEquals("507f1f77bcf86cd799439011", captcha.getId());
        assertEquals(24, captcha.getId().length());
    }
}
