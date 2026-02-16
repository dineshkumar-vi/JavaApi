package com.example.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "price_info")
public class PriceInfo {

    @Id
    private String id;

    private String productName;

    private String productCode;

    private BigDecimal price;

    private String currency;

    private String description;

    private String category;

    private String supplier;

    private LocalDateTime extractedDate;

    private String pdfFileName;

    private Integer pageNumber;

    private String rawText;

    public PriceInfo() {
        this.extractedDate = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public LocalDateTime getExtractedDate() {
        return extractedDate;
    }

    public void setExtractedDate(LocalDateTime extractedDate) {
        this.extractedDate = extractedDate;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    @Override
    public String toString() {
        return "PriceInfo{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", category='" + category + '\'' +
                ", supplier='" + supplier + '\'' +
                ", extractedDate=" + extractedDate +
                ", pdfFileName='" + pdfFileName + '\'' +
                ", pageNumber=" + pageNumber +
                '}';
    }
}
