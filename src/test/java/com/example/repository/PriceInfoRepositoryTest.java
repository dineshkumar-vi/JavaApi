package com.example.repository;

import com.example.data.PriceInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for PriceInfoRepository.
 * Tests all query methods with various scenarios including edge cases.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PriceInfoRepository Tests")
class PriceInfoRepositoryTest {

    @Mock
    private PriceInfoRepository repository;

    private PriceInfo samplePriceInfo1;
    private PriceInfo samplePriceInfo2;
    private PriceInfo samplePriceInfo3;

    @BeforeEach
    void setUp() {
        // Create sample data
        samplePriceInfo1 = new PriceInfo();
        samplePriceInfo1.setId("1");
        samplePriceInfo1.setProductName("Laptop");
        samplePriceInfo1.setProductCode("LAP-001");
        samplePriceInfo1.setPrice(new BigDecimal("999.99"));
        samplePriceInfo1.setCurrency("USD");
        samplePriceInfo1.setCategory("Electronics");
        samplePriceInfo1.setSupplier("Tech Corp");
        samplePriceInfo1.setPdfFileName("prices.pdf");
        samplePriceInfo1.setExtractedDate(LocalDateTime.now());

        samplePriceInfo2 = new PriceInfo();
        samplePriceInfo2.setId("2");
        samplePriceInfo2.setProductName("Mouse");
        samplePriceInfo2.setProductCode("MOU-001");
        samplePriceInfo2.setPrice(new BigDecimal("29.99"));
        samplePriceInfo2.setCurrency("USD");
        samplePriceInfo2.setCategory("Accessories");
        samplePriceInfo2.setSupplier("Tech Corp");
        samplePriceInfo2.setPdfFileName("accessories.pdf");
        samplePriceInfo2.setExtractedDate(LocalDateTime.now());

        samplePriceInfo3 = new PriceInfo();
        samplePriceInfo3.setId("3");
        samplePriceInfo3.setProductName("Keyboard");
        samplePriceInfo3.setProductCode("KEY-001");
        samplePriceInfo3.setPrice(new BigDecimal("79.99"));
        samplePriceInfo3.setCurrency("USD");
        samplePriceInfo3.setCategory("Accessories");
        samplePriceInfo3.setSupplier("Office Supplies Inc");
        samplePriceInfo3.setPdfFileName("prices.pdf");
        samplePriceInfo3.setExtractedDate(LocalDateTime.now());
    }

    // Save Tests
    @Test
    @DisplayName("Should save price info successfully")
    void testSave_Success() {
        when(repository.save(any(PriceInfo.class))).thenReturn(samplePriceInfo1);

        PriceInfo result = repository.save(samplePriceInfo1);

        assertNotNull(result);
        assertEquals(samplePriceInfo1.getId(), result.getId());
        assertEquals(samplePriceInfo1.getProductName(), result.getProductName());
        verify(repository, times(1)).save(samplePriceInfo1);
    }

    @Test
    @DisplayName("Should save multiple price info objects")
    void testSaveAll_Success() {
        List<PriceInfo> priceInfoList = Arrays.asList(samplePriceInfo1, samplePriceInfo2);
        when(repository.saveAll(anyList())).thenReturn(priceInfoList);

        List<PriceInfo> result = repository.saveAll(priceInfoList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).saveAll(priceInfoList);
    }

    // FindById Tests
    @Test
    @DisplayName("Should find price info by id successfully")
    void testFindById_Success() {
        when(repository.findById("1")).thenReturn(Optional.of(samplePriceInfo1));

        Optional<PriceInfo> result = repository.findById("1");

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        assertEquals("Laptop", result.get().getProductName());
        verify(repository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Should return empty when price info not found by id")
    void testFindById_NotFound() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        Optional<PriceInfo> result = repository.findById("999");

        assertFalse(result.isPresent());
        verify(repository, times(1)).findById("999");
    }

    @Test
    @DisplayName("Should handle null id in findById")
    void testFindById_WithNullId() {
        when(repository.findById(null)).thenReturn(Optional.empty());

        Optional<PriceInfo> result = repository.findById(null);

        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(null);
    }

    // FindAll Tests
    @Test
    @DisplayName("Should find all price info successfully")
    void testFindAll_Success() {
        List<PriceInfo> allPriceInfo = Arrays.asList(samplePriceInfo1, samplePriceInfo2, samplePriceInfo3);
        when(repository.findAll()).thenReturn(allPriceInfo);

        List<PriceInfo> result = repository.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no price info exists")
    void testFindAll_EmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository, times(1)).findAll();
    }

    // FindByProductName Tests
    @Test
    @DisplayName("Should find price info by product name successfully")
    void testFindByProductName_Success() {
        when(repository.findByProductName("Laptop")).thenReturn(Collections.singletonList(samplePriceInfo1));

        List<PriceInfo> result = repository.findByProductName("Laptop");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getProductName());
        verify(repository, times(1)).findByProductName("Laptop");
    }

    @Test
    @DisplayName("Should return empty list when product name not found")
    void testFindByProductName_NotFound() {
        when(repository.findByProductName("NonExistent")).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findByProductName("NonExistent");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository, times(1)).findByProductName("NonExistent");
    }

    @Test
    @DisplayName("Should handle null product name")
    void testFindByProductName_WithNull() {
        when(repository.findByProductName(null)).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findByProductName(null);

        assertNotNull(result);
        verify(repository, times(1)).findByProductName(null);
    }

    @Test
    @DisplayName("Should handle empty product name")
    void testFindByProductName_WithEmptyString() {
        when(repository.findByProductName("")).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findByProductName("");

        assertNotNull(result);
        verify(repository, times(1)).findByProductName("");
    }

    // FindByProductCode Tests
    @Test
    @DisplayName("Should find price info by product code successfully")
    void testFindByProductCode_Success() {
        when(repository.findByProductCode("LAP-001")).thenReturn(Collections.singletonList(samplePriceInfo1));

        List<PriceInfo> result = repository.findByProductCode("LAP-001");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("LAP-001", result.get(0).getProductCode());
        verify(repository, times(1)).findByProductCode("LAP-001");
    }

    @Test
    @DisplayName("Should return empty list when product code not found")
    void testFindByProductCode_NotFound() {
        when(repository.findByProductCode("INVALID")).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findByProductCode("INVALID");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository, times(1)).findByProductCode("INVALID");
    }

    @Test
    @DisplayName("Should handle null product code")
    void testFindByProductCode_WithNull() {
        when(repository.findByProductCode(null)).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findByProductCode(null);

        assertNotNull(result);
        verify(repository, times(1)).findByProductCode(null);
    }

    // FindByCategory Tests
    @Test
    @DisplayName("Should find price info by category successfully")
    void testFindByCategory_Success() {
        List<PriceInfo> accessories = Arrays.asList(samplePriceInfo2, samplePriceInfo3);
        when(repository.findByCategory("Accessories")).thenReturn(accessories);

        List<PriceInfo> result = repository.findByCategory("Accessories");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> "Accessories".equals(p.getCategory())));
        verify(repository, times(1)).findByCategory("Accessories");
    }

    @Test
    @DisplayName("Should return empty list when category not found")
    void testFindByCategory_NotFound() {
        when(repository.findByCategory("NonExistentCategory")).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findByCategory("NonExistentCategory");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository, times(1)).findByCategory("NonExistentCategory");
    }

    @Test
    @DisplayName("Should handle null category")
    void testFindByCategory_WithNull() {
        when(repository.findByCategory(null)).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findByCategory(null);

        assertNotNull(result);
        verify(repository, times(1)).findByCategory(null);
    }

    // FindBySupplier Tests
    @Test
    @DisplayName("Should find price info by supplier successfully")
    void testFindBySupplier_Success() {
        List<PriceInfo> techCorpProducts = Arrays.asList(samplePriceInfo1, samplePriceInfo2);
        when(repository.findBySupplier("Tech Corp")).thenReturn(techCorpProducts);

        List<PriceInfo> result = repository.findBySupplier("Tech Corp");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> "Tech Corp".equals(p.getSupplier())));
        verify(repository, times(1)).findBySupplier("Tech Corp");
    }

    @Test
    @DisplayName("Should return empty list when supplier not found")
    void testFindBySupplier_NotFound() {
        when(repository.findBySupplier("Unknown Supplier")).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findBySupplier("Unknown Supplier");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository, times(1)).findBySupplier("Unknown Supplier");
    }

    @Test
    @DisplayName("Should handle null supplier")
    void testFindBySupplier_WithNull() {
        when(repository.findBySupplier(null)).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findBySupplier(null);

        assertNotNull(result);
        verify(repository, times(1)).findBySupplier(null);
    }

    // FindByPriceBetween Tests
    @Test
    @DisplayName("Should find price info by price range successfully")
    void testFindByPriceBetween_Success() {
        BigDecimal minPrice = new BigDecimal("20.00");
        BigDecimal maxPrice = new BigDecimal("100.00");
        List<PriceInfo> pricesInRange = Arrays.asList(samplePriceInfo2, samplePriceInfo3);

        when(repository.findByPriceBetween(minPrice, maxPrice)).thenReturn(pricesInRange);

        List<PriceInfo> result = repository.findByPriceBetween(minPrice, maxPrice);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findByPriceBetween(minPrice, maxPrice);
    }

    @Test
    @DisplayName("Should return empty list when no prices in range")
    void testFindByPriceBetween_NoResults() {
        BigDecimal minPrice = new BigDecimal("5000.00");
        BigDecimal maxPrice = new BigDecimal("10000.00");

        when(repository.findByPriceBetween(minPrice, maxPrice)).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findByPriceBetween(minPrice, maxPrice);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository, times(1)).findByPriceBetween(minPrice, maxPrice);
    }

    @Test
    @DisplayName("Should handle zero price range")
    void testFindByPriceBetween_ZeroRange() {
        BigDecimal price = new BigDecimal("29.99");

        when(repository.findByPriceBetween(price, price)).thenReturn(Collections.singletonList(samplePriceInfo2));

        List<PriceInfo> result = repository.findByPriceBetween(price, price);

        assertNotNull(result);
        verify(repository, times(1)).findByPriceBetween(price, price);
    }

    @Test
    @DisplayName("Should handle inverted price range")
    void testFindByPriceBetween_InvertedRange() {
        BigDecimal minPrice = new BigDecimal("100.00");
        BigDecimal maxPrice = new BigDecimal("20.00");

        when(repository.findByPriceBetween(minPrice, maxPrice)).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findByPriceBetween(minPrice, maxPrice);

        assertNotNull(result);
        verify(repository, times(1)).findByPriceBetween(minPrice, maxPrice);
    }

    // FindByPdfFileName Tests
    @Test
    @DisplayName("Should find price info by PDF file name successfully")
    void testFindByPdfFileName_Success() {
        List<PriceInfo> fromPricesPdf = Arrays.asList(samplePriceInfo1, samplePriceInfo3);
        when(repository.findByPdfFileName("prices.pdf")).thenReturn(fromPricesPdf);

        List<PriceInfo> result = repository.findByPdfFileName("prices.pdf");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> "prices.pdf".equals(p.getPdfFileName())));
        verify(repository, times(1)).findByPdfFileName("prices.pdf");
    }

    @Test
    @DisplayName("Should return empty list when PDF file name not found")
    void testFindByPdfFileName_NotFound() {
        when(repository.findByPdfFileName("nonexistent.pdf")).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findByPdfFileName("nonexistent.pdf");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository, times(1)).findByPdfFileName("nonexistent.pdf");
    }

    @Test
    @DisplayName("Should handle null PDF file name")
    void testFindByPdfFileName_WithNull() {
        when(repository.findByPdfFileName(null)).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findByPdfFileName(null);

        assertNotNull(result);
        verify(repository, times(1)).findByPdfFileName(null);
    }

    // FindByProductNameContaining Tests
    @Test
    @DisplayName("Should find price info by keyword in product name successfully")
    void testFindByProductNameContaining_Success() {
        when(repository.findByProductNameContaining("Key")).thenReturn(Collections.singletonList(samplePriceInfo3));

        List<PriceInfo> result = repository.findByProductNameContaining("Key");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getProductName().contains("Key"));
        verify(repository, times(1)).findByProductNameContaining("Key");
    }

    @Test
    @DisplayName("Should return empty list when keyword not found")
    void testFindByProductNameContaining_NotFound() {
        when(repository.findByProductNameContaining("xyz")).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findByProductNameContaining("xyz");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository, times(1)).findByProductNameContaining("xyz");
    }

    @Test
    @DisplayName("Should handle empty keyword")
    void testFindByProductNameContaining_EmptyKeyword() {
        List<PriceInfo> allPriceInfo = Arrays.asList(samplePriceInfo1, samplePriceInfo2, samplePriceInfo3);
        when(repository.findByProductNameContaining("")).thenReturn(allPriceInfo);

        List<PriceInfo> result = repository.findByProductNameContaining("");

        assertNotNull(result);
        verify(repository, times(1)).findByProductNameContaining("");
    }

    @Test
    @DisplayName("Should handle null keyword")
    void testFindByProductNameContaining_WithNull() {
        when(repository.findByProductNameContaining(null)).thenReturn(Collections.emptyList());

        List<PriceInfo> result = repository.findByProductNameContaining(null);

        assertNotNull(result);
        verify(repository, times(1)).findByProductNameContaining(null);
    }

    // Delete Tests
    @Test
    @DisplayName("Should delete price info by id successfully")
    void testDeleteById_Success() {
        doNothing().when(repository).deleteById("1");

        repository.deleteById("1");

        verify(repository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Should handle delete with null id")
    void testDeleteById_WithNull() {
        doNothing().when(repository).deleteById(null);

        repository.deleteById(null);

        verify(repository, times(1)).deleteById(null);
    }

    @Test
    @DisplayName("Should delete all price info successfully")
    void testDeleteAll_Success() {
        doNothing().when(repository).deleteAll();

        repository.deleteAll();

        verify(repository, times(1)).deleteAll();
    }

    // Count Tests
    @Test
    @DisplayName("Should count all price info successfully")
    void testCount_Success() {
        when(repository.count()).thenReturn(3L);

        long result = repository.count();

        assertEquals(3L, result);
        verify(repository, times(1)).count();
    }

    @Test
    @DisplayName("Should return zero when no price info exists")
    void testCount_ZeroResults() {
        when(repository.count()).thenReturn(0L);

        long result = repository.count();

        assertEquals(0L, result);
        verify(repository, times(1)).count();
    }

    // ExistsById Tests
    @Test
    @DisplayName("Should return true when price info exists by id")
    void testExistsById_True() {
        when(repository.existsById("1")).thenReturn(true);

        boolean result = repository.existsById("1");

        assertTrue(result);
        verify(repository, times(1)).existsById("1");
    }

    @Test
    @DisplayName("Should return false when price info does not exist by id")
    void testExistsById_False() {
        when(repository.existsById("999")).thenReturn(false);

        boolean result = repository.existsById("999");

        assertFalse(result);
        verify(repository, times(1)).existsById("999");
    }
}
