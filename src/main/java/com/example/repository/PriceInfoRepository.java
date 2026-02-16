package com.example.repository;

import com.example.data.PriceInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PriceInfoRepository extends MongoRepository<PriceInfo, String> {

    List<PriceInfo> findByProductName(String productName);

    List<PriceInfo> findByProductCode(String productCode);

    List<PriceInfo> findByCategory(String category);

    List<PriceInfo> findBySupplier(String supplier);

    List<PriceInfo> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<PriceInfo> findByPdfFileName(String pdfFileName);

    List<PriceInfo> findByProductNameContaining(String keyword);
}
