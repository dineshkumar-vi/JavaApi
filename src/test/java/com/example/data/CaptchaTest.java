package com.example.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Captcha data model
 * Tests getters, setters, MongoDB annotations, and data integrity
 */
class CaptchaTest {

    private Captcha captcha;

    @BeforeEach
    void setUp() {
        captcha = new Captcha();
    }

    /**
     * Test Captcha object instantiation
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
        String id = "123abc";
        captcha.setId(id);
        assertEquals(id, captcha.getId());
    }

    /**
     * Test setting and getting captcha value
     */
    @Test
    void testSetAndGetCaptcha() {
        String captchaValue = "ABC123";
        captcha.setCaptcha(captchaValue);
        assertEquals(captchaValue, captcha.getCaptcha());
    }

    /**
     * Test setting and getting IP address
     */
    @Test
    void testSetAndGetIpAddress() {
        String ipAddress = "192.168.1.1";
        captcha.setIpAddress(ipAddress);
        assertEquals(ipAddress, captcha.getIpAddress());
    }

    /**
     * Test default values (should be null)
     */
    @Test
    void testDefaultValues() {
        Captcha newCaptcha = new Captcha();
        assertNull(newCaptcha.getId());
        assertNull(newCaptcha.getCaptcha());
        assertNull(newCaptcha.getIpAddress());
    }

    /**
     * Test setting null ID
     */
    @Test
    void testSetNullId() {
        captcha.setId("123");
        captcha.setId(null);
        assertNull(captcha.getId());
    }

    /**
     * Test setting null captcha
     */
    @Test
    void testSetNullCaptcha() {
        captcha.setCaptcha("ABC123");
        captcha.setCaptcha(null);
        assertNull(captcha.getCaptcha());
    }

    /**
     * Test setting null IP address
     */
    @Test
    void testSetNullIpAddress() {
        captcha.setIpAddress("192.168.1.1");
        captcha.setIpAddress(null);
        assertNull(captcha.getIpAddress());
    }

    /**
     * Test setting empty string values
     */
    @Test
    void testSetEmptyStrings() {
        captcha.setId("");
        captcha.setCaptcha("");
        captcha.setIpAddress("");

        assertEquals("", captcha.getId());
        assertEquals("", captcha.getCaptcha());
        assertEquals("", captcha.getIpAddress());
    }

    /**
     * Test setting all fields
     */
    @Test
    void testSetAllFields() {
        captcha.setId("456def");
        captcha.setCaptcha("XYZ789");
        captcha.setIpAddress("10.0.0.1");

        assertEquals("456def", captcha.getId());
        assertEquals("XYZ789", captcha.getCaptcha());
        assertEquals("10.0.0.1", captcha.getIpAddress());
    }

    /**
     * Test overwriting existing values
     */
    @Test
    void testOverwriteValues() {
        captcha.setCaptcha("FIRST");
        assertEquals("FIRST", captcha.getCaptcha());

        captcha.setCaptcha("SECOND");
        assertEquals("SECOND", captcha.getCaptcha());
    }

    /**
     * Test toString method
     */
    @Test
    void testToString() {
        captcha.setId("123");
        captcha.setCaptcha("ABC123");
        captcha.setIpAddress("192.168.1.1");

        String expected = "Captcha[id=123, captcha='ABC123', ipAddress='192.168.1.1']";
        assertEquals(expected, captcha.toString());
    }

    /**
     * Test toString with null values
     */
    @Test
    void testToStringWithNullValues() {
        String result = captcha.toString();
        assertTrue(result.contains("id=null"));
        assertTrue(result.contains("captcha='null'"));
        assertTrue(result.contains("ipAddress='null'"));
    }

    /**
     * Test Document annotation
     */
    @Test
    void testDocumentAnnotation() {
        assertTrue(Captcha.class.isAnnotationPresent(Document.class));
        Document document = Captcha.class.getAnnotation(Document.class);
        assertEquals("captcha", document.collection());
    }

    /**
     * Test Id annotation on id field
     */
    @Test
    void testIdAnnotation() throws NoSuchFieldException {
        var field = Captcha.class.getDeclaredField("id");
        assertTrue(field.isAnnotationPresent(org.springframework.data.annotation.Id.class));
    }

    /**
     * Test Field annotation on captcha field
     */
    @Test
    void testCaptchaFieldAnnotation() throws NoSuchFieldException {
        var field = Captcha.class.getDeclaredField("captcha");
        assertTrue(field.isAnnotationPresent(Field.class));
        Field fieldAnnotation = field.getAnnotation(Field.class);
        assertEquals("captcha", fieldAnnotation.value());
    }

    /**
     * Test Field annotation on ipAddress field
     */
    @Test
    void testIpAddressFieldAnnotation() throws NoSuchFieldException {
        var field = Captcha.class.getDeclaredField("ipAddress");
        assertTrue(field.isAnnotationPresent(Field.class));
        Field fieldAnnotation = field.getAnnotation(Field.class);
        assertEquals("ipAddress", fieldAnnotation.value());
    }

    /**
     * Test captcha with alphanumeric values
     */
    @Test
    void testCaptchaAlphanumeric() {
        captcha.setCaptcha("aB3cD4");
        assertEquals("aB3cD4", captcha.getCaptcha());
    }

    /**
     * Test IP address with various formats
     */
    @Test
    void testVariousIpAddressFormats() {
        captcha.setIpAddress("127.0.0.1");
        assertEquals("127.0.0.1", captcha.getIpAddress());

        captcha.setIpAddress("255.255.255.255");
        assertEquals("255.255.255.255", captcha.getIpAddress());

        captcha.setIpAddress("0.0.0.0");
        assertEquals("0.0.0.0", captcha.getIpAddress());
    }

    /**
     * Test long ID values
     */
    @Test
    void testLongIdValue() {
        String longId = "a".repeat(100);
        captcha.setId(longId);
        assertEquals(longId, captcha.getId());
    }

    /**
     * Test Captcha class is in correct package
     */
    @Test
    void testPackage() {
        assertEquals("com.example.data", Captcha.class.getPackage().getName());
    }

    /**
     * Test that Captcha class has all required methods
     */
    @Test
    void testRequiredMethodsExist() throws NoSuchMethodException {
        assertNotNull(Captcha.class.getMethod("getId"));
        assertNotNull(Captcha.class.getMethod("setId", String.class));
        assertNotNull(Captcha.class.getMethod("getCaptcha"));
        assertNotNull(Captcha.class.getMethod("setCaptcha", String.class));
        assertNotNull(Captcha.class.getMethod("getIpAddress"));
        assertNotNull(Captcha.class.getMethod("setIpAddress", String.class));
        assertNotNull(Captcha.class.getMethod("toString"));
    }

    /**
     * Test multiple Captcha objects are independent
     */
    @Test
    void testMultipleCaptchasIndependent() {
        Captcha captcha1 = new Captcha();
        Captcha captcha2 = new Captcha();

        captcha1.setCaptcha("ABC123");
        captcha2.setCaptcha("XYZ789");

        assertEquals("ABC123", captcha1.getCaptcha());
        assertEquals("XYZ789", captcha2.getCaptcha());
        assertNotEquals(captcha1.getCaptcha(), captcha2.getCaptcha());
    }

    /**
     * Test toString format consistency
     */
    @Test
    void testToStringFormat() {
        captcha.setId("test123");
        captcha.setCaptcha("CAP456");
        captcha.setIpAddress("1.2.3.4");

        String result = captcha.toString();
        assertTrue(result.startsWith("Captcha["));
        assertTrue(result.contains("id="));
        assertTrue(result.contains("captcha='"));
        assertTrue(result.contains("ipAddress='"));
        assertTrue(result.endsWith("]"));
    }

    /**
     * Test field types
     */
    @Test
    void testFieldTypes() throws NoSuchFieldException {
        assertEquals(String.class, Captcha.class.getDeclaredField("id").getType());
        assertEquals(String.class, Captcha.class.getDeclaredField("captcha").getType());
        assertEquals(String.class, Captcha.class.getDeclaredField("ipAddress").getType());
    }
}
