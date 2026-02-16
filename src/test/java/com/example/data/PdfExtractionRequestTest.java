package com.example.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for PdfExtractionRequest class
 * Tests constructors, getters, and setters
 */
class PdfExtractionRequestTest {

    private PdfExtractionRequest request;
    private static final String VALID_URL = "https://example.com/document.pdf";

    @BeforeEach
    void setUp() {
        request = new PdfExtractionRequest();
    }

    // Constructor Tests

    @Test
    void testDefaultConstructor() {
        // Test that default constructor creates an instance with null url
        assertNotNull(request);
        assertNull(request.getUrl());
    }

    @Test
    void testParameterizedConstructor_withValidUrl() {
        // Test constructor with valid URL
        PdfExtractionRequest requestWithUrl = new PdfExtractionRequest(VALID_URL);

        assertNotNull(requestWithUrl);
        assertEquals(VALID_URL, requestWithUrl.getUrl());
    }

    @Test
    void testParameterizedConstructor_withNullUrl() {
        // Test constructor accepts null URL
        PdfExtractionRequest requestWithNull = new PdfExtractionRequest(null);

        assertNotNull(requestWithNull);
        assertNull(requestWithNull.getUrl());
    }

    @Test
    void testParameterizedConstructor_withEmptyUrl() {
        // Test constructor with empty string
        PdfExtractionRequest requestWithEmpty = new PdfExtractionRequest("");

        assertNotNull(requestWithEmpty);
        assertEquals("", requestWithEmpty.getUrl());
    }

    // Getter Tests

    @Test
    void testGetUrl_afterDefaultConstruction() {
        // Test getUrl returns null after default construction
        assertNull(request.getUrl());
    }

    @Test
    void testGetUrl_afterSettingValue() {
        // Test getUrl returns the set value
        request.setUrl(VALID_URL);
        assertEquals(VALID_URL, request.getUrl());
    }

    // Setter Tests

    @Test
    void testSetUrl_withValidUrl() {
        // Test setting a valid URL
        request.setUrl(VALID_URL);
        assertEquals(VALID_URL, request.getUrl());
    }

    @Test
    void testSetUrl_withNullUrl() {
        // Test setting null URL
        request.setUrl(VALID_URL);
        request.setUrl(null);
        assertNull(request.getUrl());
    }

    @Test
    void testSetUrl_withEmptyUrl() {
        // Test setting empty string URL
        request.setUrl("");
        assertEquals("", request.getUrl());
    }

    @Test
    void testSetUrl_withWhitespaceUrl() {
        // Test setting whitespace URL
        String whitespaceUrl = "   ";
        request.setUrl(whitespaceUrl);
        assertEquals(whitespaceUrl, request.getUrl());
    }

    @Test
    void testSetUrl_withSpecialCharacters() {
        // Test URL with special characters
        String specialUrl = "https://example.com/file%20with%20spaces.pdf?param=value&other=123";
        request.setUrl(specialUrl);
        assertEquals(specialUrl, request.getUrl());
    }

    @Test
    void testSetUrl_multipleTimes() {
        // Test that URL can be updated multiple times
        String url1 = "https://example.com/file1.pdf";
        String url2 = "https://example.com/file2.pdf";
        String url3 = "https://example.com/file3.pdf";

        request.setUrl(url1);
        assertEquals(url1, request.getUrl());

        request.setUrl(url2);
        assertEquals(url2, request.getUrl());

        request.setUrl(url3);
        assertEquals(url3, request.getUrl());
    }

    // Edge Case Tests

    @Test
    void testSetUrl_withVeryLongUrl() {
        // Test with extremely long URL
        StringBuilder longUrl = new StringBuilder("https://example.com/");
        for (int i = 0; i < 1000; i++) {
            longUrl.append("very-long-path-segment-");
        }
        longUrl.append("document.pdf");

        String longUrlString = longUrl.toString();
        request.setUrl(longUrlString);
        assertEquals(longUrlString, request.getUrl());
    }

    @Test
    void testSetUrl_withInvalidProtocol() {
        // Test with non-standard protocol (setter should accept any string)
        String invalidUrl = "ftp://example.com/document.pdf";
        request.setUrl(invalidUrl);
        assertEquals(invalidUrl, request.getUrl());
    }

    @Test
    void testSetUrl_withRelativePath() {
        // Test with relative path
        String relativePath = "/documents/file.pdf";
        request.setUrl(relativePath);
        assertEquals(relativePath, request.getUrl());
    }

    @Test
    void testSetUrl_withLocalFilePath() {
        // Test with local file path
        String localPath = "file:///home/user/documents/file.pdf";
        request.setUrl(localPath);
        assertEquals(localPath, request.getUrl());
    }

    // Object State Tests

    @Test
    void testMultipleInstances_areIndependent() {
        // Test that multiple instances don't interfere with each other
        PdfExtractionRequest request1 = new PdfExtractionRequest("url1.pdf");
        PdfExtractionRequest request2 = new PdfExtractionRequest("url2.pdf");

        assertEquals("url1.pdf", request1.getUrl());
        assertEquals("url2.pdf", request2.getUrl());

        request1.setUrl("modified1.pdf");
        assertEquals("modified1.pdf", request1.getUrl());
        assertEquals("url2.pdf", request2.getUrl()); // Should not be affected
    }
}
