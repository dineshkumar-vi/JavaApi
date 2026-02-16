package com.example.serviceimpl;

import com.example.data.PriceInfo;
import com.example.repository.PriceInfoRepository;
import com.example.util.PdfPriceExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
 * Comprehensive unit tests for PriceServiceImpl.
 * Tests all service methods with various scenarios including edge cases and error handling.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PriceServiceImpl Tests")
class PriceServiceImplTest {

    @Mock
    private PriceInfoRepository priceInfoRepository;

    @Mock
    private PdfPriceExtractor pdfPriceExtractor;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private PriceServiceImpl priceService;

    private PriceInfo samplePriceInfo;
    private PriceInfo anotherPriceInfo;

    @BeforeEach
    void setUp() {
        samplePriceInfo = new PriceInfo();
        samplePriceInfo.setId("1");
        samplePriceInfo.setProductName("Laptop");
        samplePriceInfo.setProductCode("LAP-001");
        samplePriceInfo.setPrice(new BigDecimal("999.99"));
        samplePriceInfo.setCurrency("USD");
        samplePriceInfo.setCategory("Electronics");
        samplePriceInfo.setSupplier("Tech Corp");
        samplePriceInfo.setExtractedDate(LocalDateTime.now());

        anotherPriceInfo = new PriceInfo();
        anotherPriceInfo.setId("2");
        anotherPriceInfo.setProductName("Mouse");
        anotherPriceInfo.setProductCode("MOU-001");
        anotherPriceInfo.setPrice(new BigDecimal("29.99"));
        anotherPriceInfo.setCurrency("USD");
    }

    // savePriceInfo Tests
    @Test
    @DisplayName("Should save price info successfully")
    void testSavePriceInfo_Success() {
        when(priceInfoRepository.save(any(PriceInfo.class))).thenReturn(samplePriceInfo);

        PriceInfo result = priceService.savePriceInfo(samplePriceInfo);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Laptop", result.getProductName());
        verify(priceInfoRepository, times(1)).save(samplePriceInfo);
    }

    @Test
    @DisplayName("Should handle saving price info with minimal data")
    void testSavePriceInfo_MinimalData() {
        PriceInfo minimalInfo = new PriceInfo();
        minimalInfo.setPrice(new BigDecimal("50.00"));

        when(priceInfoRepository.save(any(PriceInfo.class))).thenReturn(minimalInfo);

        PriceInfo result = priceService.savePriceInfo(minimalInfo);

        assertNotNull(result);
        verify(priceInfoRepository, times(1)).save(minimalInfo);
    }

    @Test
    @DisplayName("Should handle saving null price info")
    void testSavePriceInfo_Null() {
        when(priceInfoRepository.save(null)).thenReturn(null);

        PriceInfo result = priceService.savePriceInfo(null);

        assertNull(result);
        verify(priceInfoRepository, times(1)).save(null);
    }

    // extractPricesFromPdf Tests
    @Test
    @DisplayName("Should extract prices from PDF successfully")
    void testExtractPricesFromPdf_Success() throws Exception {
        List<PriceInfo> extractedPrices = Arrays.asList(samplePriceInfo, anotherPriceInfo);
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("prices.pdf");
        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(pdfPriceExtractor.extractPricesFromPdf(any(InputStream.class), anyString()))
                .thenReturn(extractedPrices);
        when(priceInfoRepository.saveAll(anyList())).thenReturn(extractedPrices);

        List<PriceInfo> result = priceService.extractPricesFromPdf(multipartFile);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(multipartFile, times(1)).getInputStream();
        verify(pdfPriceExtractor, times(1)).extractPricesFromPdf(any(InputStream.class), eq("prices.pdf"));
        verify(priceInfoRepository, times(1)).saveAll(extractedPrices);
    }

    @Test
    @DisplayName("Should throw exception when PDF file is null")
    void testExtractPricesFromPdf_NullFile() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            priceService.extractPricesFromPdf(null);
        });

        assertEquals("PDF file is required", exception.getMessage());
        verify(pdfPriceExtractor, never()).extractPricesFromPdf(any(), any());
    }

    @Test
    @DisplayName("Should throw exception when PDF file is empty")
    void testExtractPricesFromPdf_EmptyFile() {
        when(multipartFile.isEmpty()).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            priceService.extractPricesFromPdf(multipartFile);
        });

        assertEquals("PDF file is required", exception.getMessage());
        verify(pdfPriceExtractor, never()).extractPricesFromPdf(any(), any());
    }

    @Test
    @DisplayName("Should handle exception during PDF extraction")
    void testExtractPricesFromPdf_ExtractionException() throws Exception {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("corrupt.pdf");
        when(multipartFile.getInputStream()).thenThrow(new RuntimeException("Corrupt PDF"));

        assertThrows(Exception.class, () -> {
            priceService.extractPricesFromPdf(multipartFile);
        });

        verify(priceInfoRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Should handle empty extraction results")
    void testExtractPricesFromPdf_EmptyResults() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("empty.pdf");
        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(pdfPriceExtractor.extractPricesFromPdf(any(InputStream.class), anyString()))
                .thenReturn(Collections.emptyList());
        when(priceInfoRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        List<PriceInfo> result = priceService.extractPricesFromPdf(multipartFile);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(priceInfoRepository, times(1)).saveAll(Collections.emptyList());
    }

    // getPriceInfoById Tests
    @Test
    @DisplayName("Should get price info by id successfully")
    void testGetPriceInfoById_Success() {
        when(priceInfoRepository.findById("1")).thenReturn(Optional.of(samplePriceInfo));

        PriceInfo result = priceService.getPriceInfoById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Laptop", result.getProductName());
        verify(priceInfoRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Should throw exception when price info not found by id")
    void testGetPriceInfoById_NotFound() {
        when(priceInfoRepository.findById("999")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            priceService.getPriceInfoById("999");
        });

        assertTrue(exception.getMessage().contains("PriceInfo not found with id: 999"));
        verify(priceInfoRepository, times(1)).findById("999");
    }

    @Test
    @DisplayName("Should handle null id in getPriceInfoById")
    void testGetPriceInfoById_NullId() {
        when(priceInfoRepository.findById(null)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            priceService.getPriceInfoById(null);
        });

        verify(priceInfoRepository, times(1)).findById(null);
    }

    // getAllPriceInfo Tests
    @Test
    @DisplayName("Should get all price info successfully")
    void testGetAllPriceInfo_Success() {
        List<PriceInfo> allPriceInfo = Arrays.asList(samplePriceInfo, anotherPriceInfo);
        when(priceInfoRepository.findAll()).thenReturn(allPriceInfo);

        List<PriceInfo> result = priceService.getAllPriceInfo();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(priceInfoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no price info exists")
    void testGetAllPriceInfo_EmptyList() {
        when(priceInfoRepository.findAll()).thenReturn(Collections.emptyList());

        List<PriceInfo> result = priceService.getAllPriceInfo();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(priceInfoRepository, times(1)).findAll();
    }

    // findByProductName Tests
    @Test
    @DisplayName("Should find price info by product name successfully")
    void testFindByProductName_Success() {
        when(priceInfoRepository.findByProductName("Laptop"))
                .thenReturn(Collections.singletonList(samplePriceInfo));

        List<PriceInfo> result = priceService.findByProductName("Laptop");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getProductName());
        verify(priceInfoRepository, times(1)).findByProductName("Laptop");
    }

    @Test
    @DisplayName("Should return empty list when product name not found")
    void testFindByProductName_NotFound() {
        when(priceInfoRepository.findByProductName("NonExistent")).thenReturn(Collections.emptyList());

        List<PriceInfo> result = priceService.findByProductName("NonExistent");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(priceInfoRepository, times(1)).findByProductName("NonExistent");
    }

    @Test
    @DisplayName("Should handle null product name")
    void testFindByProductName_Null() {
        when(priceInfoRepository.findByProductName(null)).thenReturn(Collections.emptyList());

        List<PriceInfo> result = priceService.findByProductName(null);

        assertNotNull(result);
        verify(priceInfoRepository, times(1)).findByProductName(null);
    }

    // findByProductCode Tests
    @Test
    @DisplayName("Should find price info by product code successfully")
    void testFindByProductCode_Success() {
        when(priceInfoRepository.findByProductCode("LAP-001"))
                .thenReturn(Collections.singletonList(samplePriceInfo));

        List<PriceInfo> result = priceService.findByProductCode("LAP-001");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("LAP-001", result.get(0).getProductCode());
        verify(priceInfoRepository, times(1)).findByProductCode("LAP-001");
    }

    @Test
    @DisplayName("Should return empty list when product code not found")
    void testFindByProductCode_NotFound() {
        when(priceInfoRepository.findByProductCode("INVALID")).thenReturn(Collections.emptyList());

        List<PriceInfo> result = priceService.findByProductCode("INVALID");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(priceInfoRepository, times(1)).findByProductCode("INVALID");
    }

    // findByCategory Tests
    @Test
    @DisplayName("Should find price info by category successfully")
    void testFindByCategory_Success() {
        when(priceInfoRepository.findByCategory("Electronics"))
                .thenReturn(Collections.singletonList(samplePriceInfo));

        List<PriceInfo> result = priceService.findByCategory("Electronics");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getCategory());
        verify(priceInfoRepository, times(1)).findByCategory("Electronics");
    }

    @Test
    @DisplayName("Should return empty list when category not found")
    void testFindByCategory_NotFound() {
        when(priceInfoRepository.findByCategory("Unknown")).thenReturn(Collections.emptyList());

        List<PriceInfo> result = priceService.findByCategory("Unknown");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(priceInfoRepository, times(1)).findByCategory("Unknown");
    }

    // findBySupplier Tests
    @Test
    @DisplayName("Should find price info by supplier successfully")
    void testFindBySupplier_Success() {
        when(priceInfoRepository.findBySupplier("Tech Corp"))
                .thenReturn(Collections.singletonList(samplePriceInfo));

        List<PriceInfo> result = priceService.findBySupplier("Tech Corp");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tech Corp", result.get(0).getSupplier());
        verify(priceInfoRepository, times(1)).findBySupplier("Tech Corp");
    }

    @Test
    @DisplayName("Should return empty list when supplier not found")
    void testFindBySupplier_NotFound() {
        when(priceInfoRepository.findBySupplier("Unknown Supplier")).thenReturn(Collections.emptyList());

        List<PriceInfo> result = priceService.findBySupplier("Unknown Supplier");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(priceInfoRepository, times(1)).findBySupplier("Unknown Supplier");
    }

    // findByPriceRange Tests
    @Test
    @DisplayName("Should find price info by price range successfully")
    void testFindByPriceRange_Success() {
        BigDecimal minPrice = new BigDecimal("20.00");
        BigDecimal maxPrice = new BigDecimal("50.00");

        when(priceInfoRepository.findByPriceBetween(minPrice, maxPrice))
                .thenReturn(Collections.singletonList(anotherPriceInfo));

        List<PriceInfo> result = priceService.findByPriceRange(minPrice, maxPrice);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(priceInfoRepository, times(1)).findByPriceBetween(minPrice, maxPrice);
    }

    @Test
    @DisplayName("Should return empty list when no prices in range")
    void testFindByPriceRange_NoResults() {
        BigDecimal minPrice = new BigDecimal("5000.00");
        BigDecimal maxPrice = new BigDecimal("10000.00");

        when(priceInfoRepository.findByPriceBetween(minPrice, maxPrice))
                .thenReturn(Collections.emptyList());

        List<PriceInfo> result = priceService.findByPriceRange(minPrice, maxPrice);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(priceInfoRepository, times(1)).findByPriceBetween(minPrice, maxPrice);
    }

    @Test
    @DisplayName("Should handle null price range values")
    void testFindByPriceRange_NullValues() {
        when(priceInfoRepository.findByPriceBetween(null, null)).thenReturn(Collections.emptyList());

        List<PriceInfo> result = priceService.findByPriceRange(null, null);

        assertNotNull(result);
        verify(priceInfoRepository, times(1)).findByPriceBetween(null, null);
    }

    // searchByKeyword Tests
    @Test
    @DisplayName("Should search by keyword successfully")
    void testSearchByKeyword_Success() {
        when(priceInfoRepository.findByProductNameContaining("Lap"))
                .thenReturn(Collections.singletonList(samplePriceInfo));

        List<PriceInfo> result = priceService.searchByKeyword("Lap");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getProductName().contains("Lap"));
        verify(priceInfoRepository, times(1)).findByProductNameContaining("Lap");
    }

    @Test
    @DisplayName("Should return empty list when keyword not found")
    void testSearchByKeyword_NotFound() {
        when(priceInfoRepository.findByProductNameContaining("xyz")).thenReturn(Collections.emptyList());

        List<PriceInfo> result = priceService.searchByKeyword("xyz");

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(priceInfoRepository, times(1)).findByProductNameContaining("xyz");
    }

    @Test
    @DisplayName("Should handle null keyword")
    void testSearchByKeyword_Null() {
        when(priceInfoRepository.findByProductNameContaining(null)).thenReturn(Collections.emptyList());

        List<PriceInfo> result = priceService.searchByKeyword(null);

        assertNotNull(result);
        verify(priceInfoRepository, times(1)).findByProductNameContaining(null);
    }

    // deletePriceInfo Tests
    @Test
    @DisplayName("Should delete price info successfully")
    void testDeletePriceInfo_Success() {
        doNothing().when(priceInfoRepository).deleteById("1");

        priceService.deletePriceInfo("1");

        verify(priceInfoRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Should handle delete with null id")
    void testDeletePriceInfo_NullId() {
        doNothing().when(priceInfoRepository).deleteById(null);

        priceService.deletePriceInfo(null);

        verify(priceInfoRepository, times(1)).deleteById(null);
    }

    // updatePriceInfo Tests
    @Test
    @DisplayName("Should update all fields of price info successfully")
    void testUpdatePriceInfo_AllFields() {
        PriceInfo updateData = new PriceInfo();
        updateData.setProductName("Updated Laptop");
        updateData.setProductCode("LAP-002");
        updateData.setPrice(new BigDecimal("1099.99"));
        updateData.setCurrency("EUR");
        updateData.setDescription("Updated description");
        updateData.setCategory("Premium Electronics");
        updateData.setSupplier("New Supplier");

        when(priceInfoRepository.findById("1")).thenReturn(Optional.of(samplePriceInfo));
        when(priceInfoRepository.save(any(PriceInfo.class))).thenReturn(samplePriceInfo);

        PriceInfo result = priceService.updatePriceInfo("1", updateData);

        assertNotNull(result);
        assertEquals("Updated Laptop", samplePriceInfo.getProductName());
        assertEquals("LAP-002", samplePriceInfo.getProductCode());
        assertEquals(new BigDecimal("1099.99"), samplePriceInfo.getPrice());
        assertEquals("EUR", samplePriceInfo.getCurrency());
        assertEquals("Updated description", samplePriceInfo.getDescription());
        assertEquals("Premium Electronics", samplePriceInfo.getCategory());
        assertEquals("New Supplier", samplePriceInfo.getSupplier());
        verify(priceInfoRepository, times(1)).save(samplePriceInfo);
    }

    @Test
    @DisplayName("Should update only provided fields")
    void testUpdatePriceInfo_PartialUpdate() {
        PriceInfo updateData = new PriceInfo();
        updateData.setProductName("Updated Laptop");
        updateData.setPrice(new BigDecimal("1099.99"));

        when(priceInfoRepository.findById("1")).thenReturn(Optional.of(samplePriceInfo));
        when(priceInfoRepository.save(any(PriceInfo.class))).thenReturn(samplePriceInfo);

        PriceInfo result = priceService.updatePriceInfo("1", updateData);

        assertNotNull(result);
        assertEquals("Updated Laptop", samplePriceInfo.getProductName());
        assertEquals(new BigDecimal("1099.99"), samplePriceInfo.getPrice());
        assertEquals("LAP-001", samplePriceInfo.getProductCode()); // Unchanged
        verify(priceInfoRepository, times(1)).save(samplePriceInfo);
    }

    @Test
    @DisplayName("Should not update fields with null values")
    void testUpdatePriceInfo_NullFields() {
        PriceInfo updateData = new PriceInfo();

        when(priceInfoRepository.findById("1")).thenReturn(Optional.of(samplePriceInfo));
        when(priceInfoRepository.save(any(PriceInfo.class))).thenReturn(samplePriceInfo);

        PriceInfo result = priceService.updatePriceInfo("1", updateData);

        assertNotNull(result);
        assertEquals("Laptop", samplePriceInfo.getProductName());
        assertEquals("LAP-001", samplePriceInfo.getProductCode());
        verify(priceInfoRepository, times(1)).save(samplePriceInfo);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent price info")
    void testUpdatePriceInfo_NotFound() {
        PriceInfo updateData = new PriceInfo();
        updateData.setProductName("Updated");

        when(priceInfoRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            priceService.updatePriceInfo("999", updateData);
        });

        verify(priceInfoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle update with null id")
    void testUpdatePriceInfo_NullId() {
        PriceInfo updateData = new PriceInfo();

        when(priceInfoRepository.findById(null)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            priceService.updatePriceInfo(null, updateData);
        });

        verify(priceInfoRepository, never()).save(any());
    }
}
