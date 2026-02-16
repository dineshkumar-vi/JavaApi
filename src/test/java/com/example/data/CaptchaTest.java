package com.example.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for Captcha data class
 * Tests cover all getters, setters, annotations, and edge cases
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
     * Test Captcha class instantiation
     */
    @Test
    void testCaptchaInstantiation() {
        assertNotNull(captcha);
    }

    /**
     * Test that @Document annotation is present
     */
    @Test
    void testDocumentAnnotationPresent() {
        assertTrue(Captcha.class.isAnnotationPresent(Document.class));
    }

    /**
     * Test that @Document collection name is correct
     */
    @Test
    void testDocumentCollectionName() {
        Document document = Captcha.class.getAnnotation(Document.class);
        assertNotNull(document);
        assertEquals("captcha", document.collection());
    }

    /**
     * Test id getter and setter with valid value
     */
    @Test
    void testIdGetterAndSetterWithValidValue() {
        String id = "test-id-123";
        captcha.setId(id);
        assertEquals(id, captcha.getId());
    }

    /**
     * Test id with null value
     */
    @Test
    void testIdWithNullValue() {
        captcha.setId(null);
        assertNull(captcha.getId());
    }

    /**
     * Test id with empty string
     */
    @Test
    void testIdWithEmptyString() {
        captcha.setId("");
        assertEquals("", captcha.getId());
    }

    /**
     * Test captcha getter and setter with valid value
     */
    @Test
    void testCaptchaGetterAndSetterWithValidValue() {
        String captchaValue = "abc123";
        captcha.setCaptcha(captchaValue);
        assertEquals(captchaValue, captcha.getCaptcha());
    }

    /**
     * Test captcha with null value
     */
    @Test
    void testCaptchaWithNullValue() {
        captcha.setCaptcha(null);
        assertNull(captcha.getCaptcha());
    }

    /**
     * Test captcha with empty string
     */
    @Test
    void testCaptchaWithEmptyString() {
        captcha.setCaptcha("");
        assertEquals("", captcha.getCaptcha());
    }

    /**
     * Test captcha with alphanumeric characters
     */
    @Test
    void testCaptchaWithAlphanumeric() {
        String captchaValue = "AbC123XyZ";
        captcha.setCaptcha(captchaValue);
        assertEquals(captchaValue, captcha.getCaptcha());
    }

    /**
     * Test captcha with 6 characters (typical length)
     */
    @Test
    void testCaptchaWithSixCharacters() {
        String captchaValue = "abc123";
        captcha.setCaptcha(captchaValue);
        assertEquals(6, captcha.getCaptcha().length());
    }

    /**
     * Test ipAddress getter and setter with valid value
     */
    @Test
    void testIpAddressGetterAndSetterWithValidValue() {
        String ipAddress = "192.168.1.1";
        captcha.setIpAddress(ipAddress);
        assertEquals(ipAddress, captcha.getIpAddress());
    }

    /**
     * Test ipAddress with null value
     */
    @Test
    void testIpAddressWithNullValue() {
        captcha.setIpAddress(null);
        assertNull(captcha.getIpAddress());
    }

    /**
     * Test ipAddress with empty string
     */
    @Test
    void testIpAddressWithEmptyString() {
        captcha.setIpAddress("");
        assertEquals("", captcha.getIpAddress());
    }

    /**
     * Test ipAddress with IPv6 address
     */
    @Test
    void testIpAddressWithIpv6() {
        String ipv6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        captcha.setIpAddress(ipv6);
        assertEquals(ipv6, captcha.getIpAddress());
    }

    /**
     * Test toString method with all fields set
     */
    @Test
    void testToStringWithAllFieldsSet() {
        captcha.setId("test-id-123");
        captcha.setCaptcha("abc123");
        captcha.setIpAddress("192.168.1.1");

        String result = captcha.toString();

        assertNotNull(result);
        assertTrue(result.contains("test-id-123"));
        assertTrue(result.contains("abc123"));
        assertTrue(result.contains("192.168.1.1"));
    }

    /**
     * Test toString method with null fields
     */
    @Test
    void testToStringWithNullFields() {
        String result = captcha.toString();

        assertNotNull(result);
        assertTrue(result.contains("null"));
    }

    /**
     * Test toString method format
     */
    @Test
    void testToStringFormat() {
        captcha.setId("id1");
        captcha.setCaptcha("cap1");
        captcha.setIpAddress("ip1");

        String result = captcha.toString();

        assertTrue(result.startsWith("Captcha["));
        assertTrue(result.contains("id="));
        assertTrue(result.contains("captcha="));
        assertTrue(result.contains("ipAddress="));
    }

    /**
     * Test that all fields are initially null
     */
    @Test
    void testAllFieldsInitiallyNull() {
        Captcha newCaptcha = new Captcha();
        assertNull(newCaptcha.getId());
        assertNull(newCaptcha.getCaptcha());
        assertNull(newCaptcha.getIpAddress());
    }

    /**
     * Test setting all fields at once
     */
    @Test
    void testSettingAllFields() {
        captcha.setId("test-id-123");
        captcha.setCaptcha("abc123");
        captcha.setIpAddress("192.168.1.1");

        assertEquals("test-id-123", captcha.getId());
        assertEquals("abc123", captcha.getCaptcha());
        assertEquals("192.168.1.1", captcha.getIpAddress());
    }

    /**
     * Test overwriting existing values
     */
    @Test
    void testOverwritingExistingValues() {
        captcha.setCaptcha("old123");
        assertEquals("old123", captcha.getCaptcha());

        captcha.setCaptcha("new456");
        assertEquals("new456", captcha.getCaptcha());
    }

    /**
     * Test that @Id annotation is present on id field
     */
    @Test
    void testIdFieldHasIdAnnotation() throws NoSuchFieldException {
        var idField = Captcha.class.getDeclaredField("id");
        assertTrue(idField.isAnnotationPresent(Id.class));
    }

    /**
     * Test that @Field annotation is present on captcha field
     */
    @Test
    void testCaptchaFieldHasFieldAnnotation() throws NoSuchFieldException {
        var captchaField = Captcha.class.getDeclaredField("captcha");
        assertTrue(captchaField.isAnnotationPresent(Field.class));

        Field fieldAnnotation = captchaField.getAnnotation(Field.class);
        assertEquals("captcha", fieldAnnotation.value());
    }

    /**
     * Test that @Field annotation is present on ipAddress field
     */
    @Test
    void testIpAddressFieldHasFieldAnnotation() throws NoSuchFieldException {
        var ipAddressField = Captcha.class.getDeclaredField("ipAddress");
        assertTrue(ipAddressField.isAnnotationPresent(Field.class));

        Field fieldAnnotation = ipAddressField.getAnnotation(Field.class);
        assertEquals("ipAddress", fieldAnnotation.value());
    }

    /**
     * Test that class is public
     */
    @Test
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(Captcha.class.getModifiers()));
    }

    /**
     * Test package name
     */
    @Test
    void testPackageName() {
        assertEquals("com.example.data", Captcha.class.getPackageName());
    }

    /**
     * Test class name
     */
    @Test
    void testClassName() {
        assertEquals("Captcha", Captcha.class.getSimpleName());
    }

    /**
     * Test that Captcha has exactly 3 private fields
     */
    @Test
    void testCaptchaHasThreePrivateFields() {
        assertEquals(3, Captcha.class.getDeclaredFields().length);
    }

    /**
     * Test captcha with special characters
     */
    @Test
    void testCaptchaWithSpecialCharacters() {
        String captchaValue = "!@#$%^";
        captcha.setCaptcha(captchaValue);
        assertEquals(captchaValue, captcha.getCaptcha());
    }

    /**
     * Test id with very long string
     */
    @Test
    void testIdWithLongString() {
        String longId = "a".repeat(1000);
        captcha.setId(longId);
        assertEquals(longId, captcha.getId());
    }

    /**
     * Test setting same value multiple times
     */
    @Test
    void testSettingSameValueMultipleTimes() {
        captcha.setCaptcha("abc123");
        captcha.setCaptcha("abc123");
        captcha.setCaptcha("abc123");
        assertEquals("abc123", captcha.getCaptcha());
    }

    /**
     * Test setters work independently
     */
    @Test
    void testSettersWorkIndependently() {
        captcha.setId("id1");
        captcha.setCaptcha("cap1");
        captcha.setIpAddress("ip1");

        assertEquals("id1", captcha.getId());
        assertEquals("cap1", captcha.getCaptcha());
        assertEquals("ip1", captcha.getIpAddress());
    }

    /**
     * Test toString with empty strings
     */
    @Test
    void testToStringWithEmptyStrings() {
        captcha.setId("");
        captcha.setCaptcha("");
        captcha.setIpAddress("");

        String result = captcha.toString();

        assertNotNull(result);
        assertTrue(result.contains("id="));
    }
}
