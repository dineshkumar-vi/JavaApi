package com.example.service;

import com.example.data.PriceInfo;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface PriceService {

    PriceInfo savePriceInfo(PriceInfo priceInfo);

    List<PriceInfo> extractPricesFromPdf(MultipartFile pdfFile) throws Exception;

    PriceInfo getPriceInfoById(String id);

    List<PriceInfo> getAllPriceInfo();

    List<PriceInfo> findByProductName(String productName);

    List<PriceInfo> findByProductCode(String productCode);

    List<PriceInfo> findByCategory(String category);

    List<PriceInfo> findBySupplier(String supplier);

    List<PriceInfo> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    List<PriceInfo> searchByKeyword(String keyword);

    void deletePriceInfo(String id);

    PriceInfo updatePriceInfo(String id, PriceInfo priceInfo);
}
