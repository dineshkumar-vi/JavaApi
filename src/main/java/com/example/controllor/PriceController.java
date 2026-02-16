package com.example.controllor;

import com.example.data.PriceInfo;
import com.example.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prices")
@CrossOrigin(origins = "*")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @PostMapping("/upload-pdf")
    public ResponseEntity<?> uploadPdfAndExtractPrices(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("File is empty"));
            }

            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.badRequest().body(createErrorResponse("Only PDF files are supported"));
            }

            List<PriceInfo> extractedPrices = priceService.extractPricesFromPdf(file);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "PDF processed successfully");
            response.put("extractedCount", extractedPrices.size());
            response.put("prices", extractedPrices);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error processing PDF: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPriceInfo(@RequestBody PriceInfo priceInfo) {
        try {
            PriceInfo saved = priceService.savePriceInfo(priceInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error creating price info: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPriceInfo() {
        try {
            List<PriceInfo> priceInfoList = priceService.getAllPriceInfo();
            return ResponseEntity.ok(priceInfoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving price info: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPriceInfoById(@PathVariable String id) {
        try {
            PriceInfo priceInfo = priceService.getPriceInfoById(id);
            return ResponseEntity.ok(priceInfo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error retrieving price info: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePriceInfo(@PathVariable String id, @RequestBody PriceInfo priceInfo) {
        try {
            PriceInfo updated = priceService.updatePriceInfo(id, priceInfo);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error updating price info: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePriceInfo(@PathVariable String id) {
        try {
            priceService.deletePriceInfo(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Price info deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error deleting price info: " + e.getMessage()));
        }
    }

    @GetMapping("/search/product-name")
    public ResponseEntity<?> searchByProductName(@RequestParam String name) {
        try {
            List<PriceInfo> results = priceService.findByProductName(name);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error searching by product name: " + e.getMessage()));
        }
    }

    @GetMapping("/search/product-code")
    public ResponseEntity<?> searchByProductCode(@RequestParam String code) {
        try {
            List<PriceInfo> results = priceService.findByProductCode(code);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error searching by product code: " + e.getMessage()));
        }
    }

    @GetMapping("/search/category")
    public ResponseEntity<?> searchByCategory(@RequestParam String category) {
        try {
            List<PriceInfo> results = priceService.findByCategory(category);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error searching by category: " + e.getMessage()));
        }
    }

    @GetMapping("/search/supplier")
    public ResponseEntity<?> searchBySupplier(@RequestParam String supplier) {
        try {
            List<PriceInfo> results = priceService.findBySupplier(supplier);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error searching by supplier: " + e.getMessage()));
        }
    }

    @GetMapping("/search/price-range")
    public ResponseEntity<?> searchByPriceRange(@RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice) {
        try {
            List<PriceInfo> results = priceService.findByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error searching by price range: " + e.getMessage()));
        }
    }

    @GetMapping("/search/keyword")
    public ResponseEntity<?> searchByKeyword(@RequestParam String keyword) {
        try {
            List<PriceInfo> results = priceService.searchByKeyword(keyword);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error searching by keyword: " + e.getMessage()));
        }
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", message);
        return error;
    }
}
