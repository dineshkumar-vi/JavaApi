package com.example.serviceimpl;

import com.example.data.PriceInfo;
import com.example.repository.PriceInfoRepository;
import com.example.service.PriceService;
import com.example.util.PdfPriceExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PriceServiceImpl implements PriceService {

    @Autowired
    private PriceInfoRepository priceInfoRepository;

    @Autowired
    private PdfPriceExtractor pdfPriceExtractor;

    @Override
    public PriceInfo savePriceInfo(PriceInfo priceInfo) {
        return priceInfoRepository.save(priceInfo);
    }

    @Override
    public List<PriceInfo> extractPricesFromPdf(MultipartFile pdfFile) throws Exception {
        if (pdfFile == null || pdfFile.isEmpty()) {
            throw new IllegalArgumentException("PDF file is required");
        }

        String fileName = pdfFile.getOriginalFilename();
        List<PriceInfo> extractedPrices = pdfPriceExtractor.extractPricesFromPdf(
                pdfFile.getInputStream(),
                fileName
        );

        return priceInfoRepository.saveAll(extractedPrices);
    }

    @Override
    public PriceInfo getPriceInfoById(String id) {
        return priceInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PriceInfo not found with id: " + id));
    }

    @Override
    public List<PriceInfo> getAllPriceInfo() {
        return priceInfoRepository.findAll();
    }

    @Override
    public List<PriceInfo> findByProductName(String productName) {
        return priceInfoRepository.findByProductName(productName);
    }

    @Override
    public List<PriceInfo> findByProductCode(String productCode) {
        return priceInfoRepository.findByProductCode(productCode);
    }

    @Override
    public List<PriceInfo> findByCategory(String category) {
        return priceInfoRepository.findByCategory(category);
    }

    @Override
    public List<PriceInfo> findBySupplier(String supplier) {
        return priceInfoRepository.findBySupplier(supplier);
    }

    @Override
    public List<PriceInfo> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return priceInfoRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<PriceInfo> searchByKeyword(String keyword) {
        return priceInfoRepository.findByProductNameContaining(keyword);
    }

    @Override
    public void deletePriceInfo(String id) {
        priceInfoRepository.deleteById(id);
    }

    @Override
    public PriceInfo updatePriceInfo(String id, PriceInfo priceInfo) {
        PriceInfo existing = getPriceInfoById(id);

        if (priceInfo.getProductName() != null) {
            existing.setProductName(priceInfo.getProductName());
        }
        if (priceInfo.getProductCode() != null) {
            existing.setProductCode(priceInfo.getProductCode());
        }
        if (priceInfo.getPrice() != null) {
            existing.setPrice(priceInfo.getPrice());
        }
        if (priceInfo.getCurrency() != null) {
            existing.setCurrency(priceInfo.getCurrency());
        }
        if (priceInfo.getDescription() != null) {
            existing.setDescription(priceInfo.getDescription());
        }
        if (priceInfo.getCategory() != null) {
            existing.setCategory(priceInfo.getCategory());
        }
        if (priceInfo.getSupplier() != null) {
            existing.setSupplier(priceInfo.getSupplier());
        }

        return priceInfoRepository.save(existing);
    }
}
