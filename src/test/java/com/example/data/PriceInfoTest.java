package com.example.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for PriceInfo data model.
 * Tests all getters, setters, constructors, and business logic.
 */
@DisplayName("PriceInfo Data Model Tests")
class PriceInfoTest {

    private PriceInfo priceInfo;

    @BeforeEach
    void setUp() {
        priceInfo = new PriceInfo();
    }

    // Constructor Tests
    @Test
    @DisplayName("Should initialize with current timestamp when created")
    void testConstructor_ShouldInitializeExtractedDate() {
        PriceInfo newPriceInfo = new PriceInfo();

        assertNotNull(newPriceInfo.getExtractedDate());
        assertTrue(newPriceInfo.getExtractedDate().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(newPriceInfo.getExtractedDate().isAfter(LocalDateTime.now().minusSeconds(5)));
    }

    // Id Tests
    @Test
    @DisplayName("Should set and get id correctly")
    void testSetAndGetId() {
        String expectedId = "123456";
        priceInfo.setId(expectedId);

        assertEquals(expectedId, priceInfo.getId());
    }

    @Test
    @DisplayName("Should handle null id")
    void testSetId_WithNull() {
        priceInfo.setId(null);

        assertNull(priceInfo.getId());
    }

    // Product Name Tests
    @Test
    @DisplayName("Should set and get product name correctly")
    void testSetAndGetProductName() {
        String expectedName = "Laptop Computer";
        priceInfo.setProductName(expectedName);

        assertEquals(expectedName, priceInfo.getProductName());
    }

    @Test
    @DisplayName("Should handle null product name")
    void testSetProductName_WithNull() {
        priceInfo.setProductName(null);

        assertNull(priceInfo.getProductName());
    }

    @Test
    @DisplayName("Should handle empty product name")
    void testSetProductName_WithEmptyString() {
        priceInfo.setProductName("");

        assertEquals("", priceInfo.getProductName());
    }

    @Test
    @DisplayName("Should handle long product name")
    void testSetProductName_WithLongString() {
        String longName = "A".repeat(1000);
        priceInfo.setProductName(longName);

        assertEquals(longName, priceInfo.getProductName());
    }

    // Product Code Tests
    @Test
    @DisplayName("Should set and get product code correctly")
    void testSetAndGetProductCode() {
        String expectedCode = "SKU-12345";
        priceInfo.setProductCode(expectedCode);

        assertEquals(expectedCode, priceInfo.getProductCode());
    }

    @Test
    @DisplayName("Should handle null product code")
    void testSetProductCode_WithNull() {
        priceInfo.setProductCode(null);

        assertNull(priceInfo.getProductCode());
    }

    // Price Tests
    @Test
    @DisplayName("Should set and get price correctly")
    void testSetAndGetPrice() {
        BigDecimal expectedPrice = new BigDecimal("99.99");
        priceInfo.setPrice(expectedPrice);

        assertEquals(expectedPrice, priceInfo.getPrice());
    }

    @Test
    @DisplayName("Should handle null price")
    void testSetPrice_WithNull() {
        priceInfo.setPrice(null);

        assertNull(priceInfo.getPrice());
    }

    @Test
    @DisplayName("Should handle zero price")
    void testSetPrice_WithZero() {
        BigDecimal zeroPrice = BigDecimal.ZERO;
        priceInfo.setPrice(zeroPrice);

        assertEquals(BigDecimal.ZERO, priceInfo.getPrice());
    }

    @Test
    @DisplayName("Should handle negative price")
    void testSetPrice_WithNegative() {
        BigDecimal negativePrice = new BigDecimal("-10.50");
        priceInfo.setPrice(negativePrice);

        assertEquals(negativePrice, priceInfo.getPrice());
    }

    @Test
    @DisplayName("Should handle large price")
    void testSetPrice_WithLargeValue() {
        BigDecimal largePrice = new BigDecimal("999999999.99");
        priceInfo.setPrice(largePrice);

        assertEquals(largePrice, priceInfo.getPrice());
    }

    @Test
    @DisplayName("Should handle price with many decimal places")
    void testSetPrice_WithManyDecimals() {
        BigDecimal precisePrice = new BigDecimal("99.999999");
        priceInfo.setPrice(precisePrice);

        assertEquals(precisePrice, priceInfo.getPrice());
    }

    // Currency Tests
    @Test
    @DisplayName("Should set and get currency correctly")
    void testSetAndGetCurrency() {
        String expectedCurrency = "USD";
        priceInfo.setCurrency(expectedCurrency);

        assertEquals(expectedCurrency, priceInfo.getCurrency());
    }

    @Test
    @DisplayName("Should handle null currency")
    void testSetCurrency_WithNull() {
        priceInfo.setCurrency(null);

        assertNull(priceInfo.getCurrency());
    }

    @Test
    @DisplayName("Should handle various currency codes")
    void testSetCurrency_WithVariousCodes() {
        String[] currencies = {"USD", "EUR", "GBP", "JPY", "INR"};

        for (String currency : currencies) {
            priceInfo.setCurrency(currency);
            assertEquals(currency, priceInfo.getCurrency());
        }
    }

    // Description Tests
    @Test
    @DisplayName("Should set and get description correctly")
    void testSetAndGetDescription() {
        String expectedDescription = "High-performance laptop with 16GB RAM";
        priceInfo.setDescription(expectedDescription);

        assertEquals(expectedDescription, priceInfo.getDescription());
    }

    @Test
    @DisplayName("Should handle null description")
    void testSetDescription_WithNull() {
        priceInfo.setDescription(null);

        assertNull(priceInfo.getDescription());
    }

    @Test
    @DisplayName("Should handle empty description")
    void testSetDescription_WithEmptyString() {
        priceInfo.setDescription("");

        assertEquals("", priceInfo.getDescription());
    }

    // Category Tests
    @Test
    @DisplayName("Should set and get category correctly")
    void testSetAndGetCategory() {
        String expectedCategory = "Electronics";
        priceInfo.setCategory(expectedCategory);

        assertEquals(expectedCategory, priceInfo.getCategory());
    }

    @Test
    @DisplayName("Should handle null category")
    void testSetCategory_WithNull() {
        priceInfo.setCategory(null);

        assertNull(priceInfo.getCategory());
    }

    // Supplier Tests
    @Test
    @DisplayName("Should set and get supplier correctly")
    void testSetAndGetSupplier() {
        String expectedSupplier = "Tech Suppliers Inc.";
        priceInfo.setSupplier(expectedSupplier);

        assertEquals(expectedSupplier, priceInfo.getSupplier());
    }

    @Test
    @DisplayName("Should handle null supplier")
    void testSetSupplier_WithNull() {
        priceInfo.setSupplier(null);

        assertNull(priceInfo.getSupplier());
    }

    // Extracted Date Tests
    @Test
    @DisplayName("Should set and get extracted date correctly")
    void testSetAndGetExtractedDate() {
        LocalDateTime expectedDate = LocalDateTime.of(2023, 6, 15, 10, 30);
        priceInfo.setExtractedDate(expectedDate);

        assertEquals(expectedDate, priceInfo.getExtractedDate());
    }

    @Test
    @DisplayName("Should handle null extracted date")
    void testSetExtractedDate_WithNull() {
        priceInfo.setExtractedDate(null);

        assertNull(priceInfo.getExtractedDate());
    }

    // PDF File Name Tests
    @Test
    @DisplayName("Should set and get PDF file name correctly")
    void testSetAndGetPdfFileName() {
        String expectedFileName = "price_list_2023.pdf";
        priceInfo.setPdfFileName(expectedFileName);

        assertEquals(expectedFileName, priceInfo.getPdfFileName());
    }

    @Test
    @DisplayName("Should handle null PDF file name")
    void testSetPdfFileName_WithNull() {
        priceInfo.setPdfFileName(null);

        assertNull(priceInfo.getPdfFileName());
    }

    // Page Number Tests
    @Test
    @DisplayName("Should set and get page number correctly")
    void testSetAndGetPageNumber() {
        Integer expectedPageNumber = 5;
        priceInfo.setPageNumber(expectedPageNumber);

        assertEquals(expectedPageNumber, priceInfo.getPageNumber());
    }

    @Test
    @DisplayName("Should handle null page number")
    void testSetPageNumber_WithNull() {
        priceInfo.setPageNumber(null);

        assertNull(priceInfo.getPageNumber());
    }

    @Test
    @DisplayName("Should handle zero page number")
    void testSetPageNumber_WithZero() {
        priceInfo.setPageNumber(0);

        assertEquals(0, priceInfo.getPageNumber());
    }

    @Test
    @DisplayName("Should handle negative page number")
    void testSetPageNumber_WithNegative() {
        priceInfo.setPageNumber(-1);

        assertEquals(-1, priceInfo.getPageNumber());
    }

    // Raw Text Tests
    @Test
    @DisplayName("Should set and get raw text correctly")
    void testSetAndGetRawText() {
        String expectedRawText = "Product: Laptop Price: $999.99";
        priceInfo.setRawText(expectedRawText);

        assertEquals(expectedRawText, priceInfo.getRawText());
    }

    @Test
    @DisplayName("Should handle null raw text")
    void testSetRawText_WithNull() {
        priceInfo.setRawText(null);

        assertNull(priceInfo.getRawText());
    }

    @Test
    @DisplayName("Should handle empty raw text")
    void testSetRawText_WithEmptyString() {
        priceInfo.setRawText("");

        assertEquals("", priceInfo.getRawText());
    }

    // ToString Tests
    @Test
    @DisplayName("Should generate toString with all fields populated")
    void testToString_WithAllFields() {
        priceInfo.setId("123");
        priceInfo.setProductName("Laptop");
        priceInfo.setProductCode("SKU-001");
        priceInfo.setPrice(new BigDecimal("999.99"));
        priceInfo.setCurrency("USD");
        priceInfo.setCategory("Electronics");
        priceInfo.setSupplier("Tech Corp");
        priceInfo.setPdfFileName("prices.pdf");
        priceInfo.setPageNumber(1);

        String result = priceInfo.toString();

        assertNotNull(result);
        assertTrue(result.contains("123"));
        assertTrue(result.contains("Laptop"));
        assertTrue(result.contains("SKU-001"));
        assertTrue(result.contains("999.99"));
        assertTrue(result.contains("USD"));
        assertTrue(result.contains("Electronics"));
        assertTrue(result.contains("Tech Corp"));
        assertTrue(result.contains("prices.pdf"));
        assertTrue(result.contains("1"));
    }

    @Test
    @DisplayName("Should generate toString with null fields")
    void testToString_WithNullFields() {
        String result = priceInfo.toString();

        assertNotNull(result);
        assertTrue(result.contains("PriceInfo{"));
    }

    // Integration Tests
    @Test
    @DisplayName("Should create complete price info object")
    void testCompleteObject_Integration() {
        PriceInfo completeInfo = new PriceInfo();
        completeInfo.setId("ABC123");
        completeInfo.setProductName("Premium Laptop");
        completeInfo.setProductCode("LAPTOP-001");
        completeInfo.setPrice(new BigDecimal("1299.99"));
        completeInfo.setCurrency("USD");
        completeInfo.setDescription("High-end gaming laptop");
        completeInfo.setCategory("Electronics");
        completeInfo.setSupplier("Tech Suppliers Inc.");
        completeInfo.setPdfFileName("catalog_2023.pdf");
        completeInfo.setPageNumber(15);
        completeInfo.setRawText("Premium Laptop - LAPTOP-001 - $1299.99");

        assertEquals("ABC123", completeInfo.getId());
        assertEquals("Premium Laptop", completeInfo.getProductName());
        assertEquals("LAPTOP-001", completeInfo.getProductCode());
        assertEquals(new BigDecimal("1299.99"), completeInfo.getPrice());
        assertEquals("USD", completeInfo.getCurrency());
        assertEquals("High-end gaming laptop", completeInfo.getDescription());
        assertEquals("Electronics", completeInfo.getCategory());
        assertEquals("Tech Suppliers Inc.", completeInfo.getSupplier());
        assertEquals("catalog_2023.pdf", completeInfo.getPdfFileName());
        assertEquals(15, completeInfo.getPageNumber());
        assertEquals("Premium Laptop - LAPTOP-001 - $1299.99", completeInfo.getRawText());
        assertNotNull(completeInfo.getExtractedDate());
    }

    @Test
    @DisplayName("Should handle object with minimal fields")
    void testMinimalObject_Integration() {
        PriceInfo minimalInfo = new PriceInfo();
        minimalInfo.setPrice(new BigDecimal("50.00"));

        assertNull(minimalInfo.getId());
        assertNull(minimalInfo.getProductName());
        assertNull(minimalInfo.getProductCode());
        assertEquals(new BigDecimal("50.00"), minimalInfo.getPrice());
        assertNull(minimalInfo.getCurrency());
        assertNull(minimalInfo.getDescription());
        assertNull(minimalInfo.getCategory());
        assertNull(minimalInfo.getSupplier());
        assertNotNull(minimalInfo.getExtractedDate());
        assertNull(minimalInfo.getPdfFileName());
        assertNull(minimalInfo.getPageNumber());
        assertNull(minimalInfo.getRawText());
    }
}
