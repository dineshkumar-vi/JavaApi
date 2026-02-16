package com.example.controllor;

import com.example.data.PriceInfo;
import com.example.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for PriceController.
 * Tests all REST endpoints with various scenarios including success cases and error handling.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PriceController Tests")
class PriceControllerTest {

    @Mock
    private PriceService priceService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private PriceController priceController;

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

    // uploadPdfAndExtractPrices Tests
    @Test
    @DisplayName("Should upload PDF and extract prices successfully")
    void testUploadPdfAndExtractPrices_Success() throws Exception {
        List<PriceInfo> extractedPrices = Arrays.asList(samplePriceInfo, anotherPriceInfo);

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getContentType()).thenReturn("application/pdf");
        when(priceService.extractPricesFromPdf(multipartFile)).thenReturn(extractedPrices);

        ResponseEntity<?> response = priceController.uploadPdfAndExtractPrices(multipartFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertEquals("PDF processed successfully", responseBody.get("message"));
        assertEquals(2, responseBody.get("extractedCount"));

        verify(priceService, times(1)).extractPricesFromPdf(multipartFile);
    }

    @Test
    @DisplayName("Should return bad request when file is empty")
    void testUploadPdfAndExtractPrices_EmptyFile() {
        when(multipartFile.isEmpty()).thenReturn(true);

        ResponseEntity<?> response = priceController.uploadPdfAndExtractPrices(multipartFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertEquals("File is empty", responseBody.get("error"));

        verify(priceService, never()).extractPricesFromPdf(any());
    }

    @Test
    @DisplayName("Should return bad request when file is not PDF")
    void testUploadPdfAndExtractPrices_NotPdfFile() {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getContentType()).thenReturn("application/json");

        ResponseEntity<?> response = priceController.uploadPdfAndExtractPrices(multipartFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertEquals("Only PDF files are supported", responseBody.get("error"));

        verify(priceService, never()).extractPricesFromPdf(any());
    }

    @Test
    @DisplayName("Should handle exception during PDF processing")
    void testUploadPdfAndExtractPrices_Exception() throws Exception {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getContentType()).thenReturn("application/pdf");
        when(priceService.extractPricesFromPdf(multipartFile))
                .thenThrow(new RuntimeException("PDF processing error"));

        ResponseEntity<?> response = priceController.uploadPdfAndExtractPrices(multipartFile);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertTrue(((String) responseBody.get("error")).contains("Error processing PDF"));
    }

    @Test
    @DisplayName("Should handle null file upload")
    void testUploadPdfAndExtractPrices_NullFile() {
        ResponseEntity<?> response = priceController.uploadPdfAndExtractPrices(null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(priceService, never()).extractPricesFromPdf(any());
    }

    // createPriceInfo Tests
    @Test
    @DisplayName("Should create price info successfully")
    void testCreatePriceInfo_Success() {
        when(priceService.savePriceInfo(any(PriceInfo.class))).thenReturn(samplePriceInfo);

        ResponseEntity<?> response = priceController.createPriceInfo(samplePriceInfo);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(samplePriceInfo, response.getBody());

        verify(priceService, times(1)).savePriceInfo(samplePriceInfo);
    }

    @Test
    @DisplayName("Should handle exception during price info creation")
    void testCreatePriceInfo_Exception() {
        when(priceService.savePriceInfo(any(PriceInfo.class)))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = priceController.createPriceInfo(samplePriceInfo);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertTrue(((String) responseBody.get("error")).contains("Error creating price info"));
    }

    @Test
    @DisplayName("Should handle null price info creation")
    void testCreatePriceInfo_Null() {
        when(priceService.savePriceInfo(null)).thenReturn(null);

        ResponseEntity<?> response = priceController.createPriceInfo(null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(priceService, times(1)).savePriceInfo(null);
    }

    // getAllPriceInfo Tests
    @Test
    @DisplayName("Should get all price info successfully")
    void testGetAllPriceInfo_Success() {
        List<PriceInfo> allPriceInfo = Arrays.asList(samplePriceInfo, anotherPriceInfo);
        when(priceService.getAllPriceInfo()).thenReturn(allPriceInfo);

        ResponseEntity<?> response = priceController.getAllPriceInfo();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        List<PriceInfo> responseBody = (List<PriceInfo>) response.getBody();
        assertEquals(2, responseBody.size());

        verify(priceService, times(1)).getAllPriceInfo();
    }

    @Test
    @DisplayName("Should return empty list when no price info exists")
    void testGetAllPriceInfo_EmptyList() {
        when(priceService.getAllPriceInfo()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = priceController.getAllPriceInfo();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        List<PriceInfo> responseBody = (List<PriceInfo>) response.getBody();
        assertEquals(0, responseBody.size());
    }

    @Test
    @DisplayName("Should handle exception when getting all price info")
    void testGetAllPriceInfo_Exception() {
        when(priceService.getAllPriceInfo()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = priceController.getAllPriceInfo();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
    }

    // getPriceInfoById Tests
    @Test
    @DisplayName("Should get price info by id successfully")
    void testGetPriceInfoById_Success() {
        when(priceService.getPriceInfoById("1")).thenReturn(samplePriceInfo);

        ResponseEntity<?> response = priceController.getPriceInfoById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(samplePriceInfo, response.getBody());

        verify(priceService, times(1)).getPriceInfoById("1");
    }

    @Test
    @DisplayName("Should return not found when price info does not exist")
    void testGetPriceInfoById_NotFound() {
        when(priceService.getPriceInfoById("999"))
                .thenThrow(new RuntimeException("PriceInfo not found with id: 999"));

        ResponseEntity<?> response = priceController.getPriceInfoById("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
    }

    @Test
    @DisplayName("Should handle general exception when getting by id")
    void testGetPriceInfoById_GeneralException() {
        when(priceService.getPriceInfoById("1")).thenThrow(new NullPointerException("Null error"));

        ResponseEntity<?> response = priceController.getPriceInfoById("1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // updatePriceInfo Tests
    @Test
    @DisplayName("Should update price info successfully")
    void testUpdatePriceInfo_Success() {
        PriceInfo updateData = new PriceInfo();
        updateData.setProductName("Updated Laptop");

        when(priceService.updatePriceInfo("1", updateData)).thenReturn(samplePriceInfo);

        ResponseEntity<?> response = priceController.updatePriceInfo("1", updateData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(priceService, times(1)).updatePriceInfo("1", updateData);
    }

    @Test
    @DisplayName("Should return not found when updating non-existent price info")
    void testUpdatePriceInfo_NotFound() {
        PriceInfo updateData = new PriceInfo();

        when(priceService.updatePriceInfo("999", updateData))
                .thenThrow(new RuntimeException("PriceInfo not found with id: 999"));

        ResponseEntity<?> response = priceController.updatePriceInfo("999", updateData);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
    }

    @Test
    @DisplayName("Should handle exception during update")
    void testUpdatePriceInfo_Exception() {
        PriceInfo updateData = new PriceInfo();

        when(priceService.updatePriceInfo("1", updateData))
                .thenThrow(new NullPointerException("Null error"));

        ResponseEntity<?> response = priceController.updatePriceInfo("1", updateData);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // deletePriceInfo Tests
    @Test
    @DisplayName("Should delete price info successfully")
    void testDeletePriceInfo_Success() {
        doNothing().when(priceService).deletePriceInfo("1");

        ResponseEntity<?> response = priceController.deletePriceInfo("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertEquals("Price info deleted successfully", responseBody.get("message"));

        verify(priceService, times(1)).deletePriceInfo("1");
    }

    @Test
    @DisplayName("Should handle exception during delete")
    void testDeletePriceInfo_Exception() {
        doThrow(new RuntimeException("Database error")).when(priceService).deletePriceInfo("1");

        ResponseEntity<?> response = priceController.deletePriceInfo("1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
    }

    // searchByProductName Tests
    @Test
    @DisplayName("Should search by product name successfully")
    void testSearchByProductName_Success() {
        when(priceService.findByProductName("Laptop"))
                .thenReturn(Collections.singletonList(samplePriceInfo));

        ResponseEntity<?> response = priceController.searchByProductName("Laptop");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        List<PriceInfo> responseBody = (List<PriceInfo>) response.getBody();
        assertEquals(1, responseBody.size());

        verify(priceService, times(1)).findByProductName("Laptop");
    }

    @Test
    @DisplayName("Should handle exception when searching by product name")
    void testSearchByProductName_Exception() {
        when(priceService.findByProductName("Laptop")).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = priceController.searchByProductName("Laptop");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // searchByProductCode Tests
    @Test
    @DisplayName("Should search by product code successfully")
    void testSearchByProductCode_Success() {
        when(priceService.findByProductCode("LAP-001"))
                .thenReturn(Collections.singletonList(samplePriceInfo));

        ResponseEntity<?> response = priceController.searchByProductCode("LAP-001");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        List<PriceInfo> responseBody = (List<PriceInfo>) response.getBody();
        assertEquals(1, responseBody.size());

        verify(priceService, times(1)).findByProductCode("LAP-001");
    }

    @Test
    @DisplayName("Should handle exception when searching by product code")
    void testSearchByProductCode_Exception() {
        when(priceService.findByProductCode("LAP-001")).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = priceController.searchByProductCode("LAP-001");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // searchByCategory Tests
    @Test
    @DisplayName("Should search by category successfully")
    void testSearchByCategory_Success() {
        when(priceService.findByCategory("Electronics"))
                .thenReturn(Collections.singletonList(samplePriceInfo));

        ResponseEntity<?> response = priceController.searchByCategory("Electronics");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        List<PriceInfo> responseBody = (List<PriceInfo>) response.getBody();
        assertEquals(1, responseBody.size());

        verify(priceService, times(1)).findByCategory("Electronics");
    }

    @Test
    @DisplayName("Should handle exception when searching by category")
    void testSearchByCategory_Exception() {
        when(priceService.findByCategory("Electronics")).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = priceController.searchByCategory("Electronics");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // searchBySupplier Tests
    @Test
    @DisplayName("Should search by supplier successfully")
    void testSearchBySupplier_Success() {
        when(priceService.findBySupplier("Tech Corp"))
                .thenReturn(Collections.singletonList(samplePriceInfo));

        ResponseEntity<?> response = priceController.searchBySupplier("Tech Corp");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        List<PriceInfo> responseBody = (List<PriceInfo>) response.getBody();
        assertEquals(1, responseBody.size());

        verify(priceService, times(1)).findBySupplier("Tech Corp");
    }

    @Test
    @DisplayName("Should handle exception when searching by supplier")
    void testSearchBySupplier_Exception() {
        when(priceService.findBySupplier("Tech Corp")).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = priceController.searchBySupplier("Tech Corp");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // searchByPriceRange Tests
    @Test
    @DisplayName("Should search by price range successfully")
    void testSearchByPriceRange_Success() {
        BigDecimal minPrice = new BigDecimal("20.00");
        BigDecimal maxPrice = new BigDecimal("50.00");

        when(priceService.findByPriceRange(minPrice, maxPrice))
                .thenReturn(Collections.singletonList(anotherPriceInfo));

        ResponseEntity<?> response = priceController.searchByPriceRange(minPrice, maxPrice);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        List<PriceInfo> responseBody = (List<PriceInfo>) response.getBody();
        assertEquals(1, responseBody.size());

        verify(priceService, times(1)).findByPriceRange(minPrice, maxPrice);
    }

    @Test
    @DisplayName("Should handle exception when searching by price range")
    void testSearchByPriceRange_Exception() {
        BigDecimal minPrice = new BigDecimal("20.00");
        BigDecimal maxPrice = new BigDecimal("50.00");

        when(priceService.findByPriceRange(minPrice, maxPrice))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = priceController.searchByPriceRange(minPrice, maxPrice);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // searchByKeyword Tests
    @Test
    @DisplayName("Should search by keyword successfully")
    void testSearchByKeyword_Success() {
        when(priceService.searchByKeyword("Lap"))
                .thenReturn(Collections.singletonList(samplePriceInfo));

        ResponseEntity<?> response = priceController.searchByKeyword("Lap");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        List<PriceInfo> responseBody = (List<PriceInfo>) response.getBody();
        assertEquals(1, responseBody.size());

        verify(priceService, times(1)).searchByKeyword("Lap");
    }

    @Test
    @DisplayName("Should handle exception when searching by keyword")
    void testSearchByKeyword_Exception() {
        when(priceService.searchByKeyword("Lap")).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = priceController.searchByKeyword("Lap");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return empty list for searches with no results")
    void testSearch_NoResults() {
        when(priceService.searchByKeyword("xyz")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = priceController.searchByKeyword("xyz");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        List<PriceInfo> responseBody = (List<PriceInfo>) response.getBody();
        assertEquals(0, responseBody.size());
    }
}
