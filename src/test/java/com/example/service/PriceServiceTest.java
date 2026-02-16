package com.example.service;

import com.example.data.PriceInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for PriceService interface.
 * Tests interface contract and method signatures.
 * Note: Implementation testing is done in PriceServiceImplTest.
 */
@DisplayName("PriceService Interface Tests")
class PriceServiceTest {

    /**
     * This test verifies that the PriceService interface exists
     * and has the expected method signatures.
     */
    @Test
    @DisplayName("Should verify PriceService interface exists")
    void testPriceServiceInterface_Exists() {
        assertNotNull(PriceService.class);
    }

    @Test
    @DisplayName("Should have savePriceInfo method")
    void testPriceServiceInterface_HasSavePriceInfoMethod() throws NoSuchMethodException {
        assertNotNull(PriceService.class.getMethod("savePriceInfo", PriceInfo.class));
    }

    @Test
    @DisplayName("Should have extractPricesFromPdf method")
    void testPriceServiceInterface_HasExtractPricesFromPdfMethod() throws NoSuchMethodException {
        assertNotNull(PriceService.class.getMethod("extractPricesFromPdf", MultipartFile.class));
    }

    @Test
    @DisplayName("Should have getPriceInfoById method")
    void testPriceServiceInterface_HasGetPriceInfoByIdMethod() throws NoSuchMethodException {
        assertNotNull(PriceService.class.getMethod("getPriceInfoById", String.class));
    }

    @Test
    @DisplayName("Should have getAllPriceInfo method")
    void testPriceServiceInterface_HasGetAllPriceInfoMethod() throws NoSuchMethodException {
        assertNotNull(PriceService.class.getMethod("getAllPriceInfo"));
    }

    @Test
    @DisplayName("Should have findByProductName method")
    void testPriceServiceInterface_HasFindByProductNameMethod() throws NoSuchMethodException {
        assertNotNull(PriceService.class.getMethod("findByProductName", String.class));
    }

    @Test
    @DisplayName("Should have findByProductCode method")
    void testPriceServiceInterface_HasFindByProductCodeMethod() throws NoSuchMethodException {
        assertNotNull(PriceService.class.getMethod("findByProductCode", String.class));
    }

    @Test
    @DisplayName("Should have findByCategory method")
    void testPriceServiceInterface_HasFindByCategoryMethod() throws NoSuchMethodException {
        assertNotNull(PriceService.class.getMethod("findByCategory", String.class));
    }

    @Test
    @DisplayName("Should have findBySupplier method")
    void testPriceServiceInterface_HasFindBySupplierMethod() throws NoSuchMethodException {
        assertNotNull(PriceService.class.getMethod("findBySupplier", String.class));
    }

    @Test
    @DisplayName("Should have findByPriceRange method")
    void testPriceServiceInterface_HasFindByPriceRangeMethod() throws NoSuchMethodException {
        assertNotNull(PriceService.class.getMethod("findByPriceRange", BigDecimal.class, BigDecimal.class));
    }

    @Test
    @DisplayName("Should have searchByKeyword method")
    void testPriceServiceInterface_HasSearchByKeywordMethod() throws NoSuchMethodException {
        assertNotNull(PriceService.class.getMethod("searchByKeyword", String.class));
    }

    @Test
    @DisplayName("Should have deletePriceInfo method")
    void testPriceServiceInterface_HasDeletePriceInfoMethod() throws NoSuchMethodException {
        assertNotNull(PriceService.class.getMethod("deletePriceInfo", String.class));
    }

    @Test
    @DisplayName("Should have updatePriceInfo method")
    void testPriceServiceInterface_HasUpdatePriceInfoMethod() throws NoSuchMethodException {
        assertNotNull(PriceService.class.getMethod("updatePriceInfo", String.class, PriceInfo.class));
    }

    @Test
    @DisplayName("Should verify savePriceInfo returns PriceInfo")
    void testSavePriceInfoMethod_ReturnType() throws NoSuchMethodException {
        assertEquals(PriceInfo.class,
                PriceService.class.getMethod("savePriceInfo", PriceInfo.class).getReturnType());
    }

    @Test
    @DisplayName("Should verify extractPricesFromPdf returns List")
    void testExtractPricesFromPdfMethod_ReturnType() throws NoSuchMethodException {
        assertEquals(List.class,
                PriceService.class.getMethod("extractPricesFromPdf", MultipartFile.class).getReturnType());
    }

    @Test
    @DisplayName("Should verify getPriceInfoById returns PriceInfo")
    void testGetPriceInfoByIdMethod_ReturnType() throws NoSuchMethodException {
        assertEquals(PriceInfo.class,
                PriceService.class.getMethod("getPriceInfoById", String.class).getReturnType());
    }

    @Test
    @DisplayName("Should verify getAllPriceInfo returns List")
    void testGetAllPriceInfoMethod_ReturnType() throws NoSuchMethodException {
        assertEquals(List.class,
                PriceService.class.getMethod("getAllPriceInfo").getReturnType());
    }

    @Test
    @DisplayName("Should verify findByProductName returns List")
    void testFindByProductNameMethod_ReturnType() throws NoSuchMethodException {
        assertEquals(List.class,
                PriceService.class.getMethod("findByProductName", String.class).getReturnType());
    }

    @Test
    @DisplayName("Should verify findByProductCode returns List")
    void testFindByProductCodeMethod_ReturnType() throws NoSuchMethodException {
        assertEquals(List.class,
                PriceService.class.getMethod("findByProductCode", String.class).getReturnType());
    }

    @Test
    @DisplayName("Should verify findByCategory returns List")
    void testFindByCategoryMethod_ReturnType() throws NoSuchMethodException {
        assertEquals(List.class,
                PriceService.class.getMethod("findByCategory", String.class).getReturnType());
    }

    @Test
    @DisplayName("Should verify findBySupplier returns List")
    void testFindBySupplierMethod_ReturnType() throws NoSuchMethodException {
        assertEquals(List.class,
                PriceService.class.getMethod("findBySupplier", String.class).getReturnType());
    }

    @Test
    @DisplayName("Should verify findByPriceRange returns List")
    void testFindByPriceRangeMethod_ReturnType() throws NoSuchMethodException {
        assertEquals(List.class,
                PriceService.class.getMethod("findByPriceRange", BigDecimal.class, BigDecimal.class).getReturnType());
    }

    @Test
    @DisplayName("Should verify searchByKeyword returns List")
    void testSearchByKeywordMethod_ReturnType() throws NoSuchMethodException {
        assertEquals(List.class,
                PriceService.class.getMethod("searchByKeyword", String.class).getReturnType());
    }

    @Test
    @DisplayName("Should verify deletePriceInfo returns void")
    void testDeletePriceInfoMethod_ReturnType() throws NoSuchMethodException {
        assertEquals(void.class,
                PriceService.class.getMethod("deletePriceInfo", String.class).getReturnType());
    }

    @Test
    @DisplayName("Should verify updatePriceInfo returns PriceInfo")
    void testUpdatePriceInfoMethod_ReturnType() throws NoSuchMethodException {
        assertEquals(PriceInfo.class,
                PriceService.class.getMethod("updatePriceInfo", String.class, PriceInfo.class).getReturnType());
    }

    @Test
    @DisplayName("Should verify extractPricesFromPdf declares Exception")
    void testExtractPricesFromPdfMethod_DeclaresException() throws NoSuchMethodException {
        Class<?>[] exceptions = PriceService.class
                .getMethod("extractPricesFromPdf", MultipartFile.class)
                .getExceptionTypes();

        assertTrue(exceptions.length > 0);
        assertEquals(Exception.class, exceptions[0]);
    }

    @Test
    @DisplayName("Should verify PriceService is an interface")
    void testPriceService_IsInterface() {
        assertTrue(PriceService.class.isInterface());
    }

    @Test
    @DisplayName("Should verify interface has exactly 12 methods")
    void testPriceServiceInterface_MethodCount() {
        // Count all declared methods in the interface
        int methodCount = PriceService.class.getDeclaredMethods().length;
        assertEquals(12, methodCount, "PriceService should have exactly 12 methods");
    }

    @Test
    @DisplayName("Should verify savePriceInfo has one parameter")
    void testSavePriceInfoMethod_ParameterCount() throws NoSuchMethodException {
        assertEquals(1,
                PriceService.class.getMethod("savePriceInfo", PriceInfo.class).getParameterCount());
    }

    @Test
    @DisplayName("Should verify extractPricesFromPdf has one parameter")
    void testExtractPricesFromPdfMethod_ParameterCount() throws NoSuchMethodException {
        assertEquals(1,
                PriceService.class.getMethod("extractPricesFromPdf", MultipartFile.class).getParameterCount());
    }

    @Test
    @DisplayName("Should verify getPriceInfoById has one parameter")
    void testGetPriceInfoByIdMethod_ParameterCount() throws NoSuchMethodException {
        assertEquals(1,
                PriceService.class.getMethod("getPriceInfoById", String.class).getParameterCount());
    }

    @Test
    @DisplayName("Should verify getAllPriceInfo has no parameters")
    void testGetAllPriceInfoMethod_ParameterCount() throws NoSuchMethodException {
        assertEquals(0,
                PriceService.class.getMethod("getAllPriceInfo").getParameterCount());
    }

    @Test
    @DisplayName("Should verify findByPriceRange has two parameters")
    void testFindByPriceRangeMethod_ParameterCount() throws NoSuchMethodException {
        assertEquals(2,
                PriceService.class.getMethod("findByPriceRange", BigDecimal.class, BigDecimal.class).getParameterCount());
    }

    @Test
    @DisplayName("Should verify updatePriceInfo has two parameters")
    void testUpdatePriceInfoMethod_ParameterCount() throws NoSuchMethodException {
        assertEquals(2,
                PriceService.class.getMethod("updatePriceInfo", String.class, PriceInfo.class).getParameterCount());
    }

    @Test
    @DisplayName("Should verify interface is public")
    void testPriceServiceInterface_IsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(PriceService.class.getModifiers()));
    }
}
