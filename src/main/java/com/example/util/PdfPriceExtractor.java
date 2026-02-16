package com.example.util;

import com.example.data.PriceInfo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PdfPriceExtractor {

    // Regex patterns for price extraction
    private static final Pattern PRICE_PATTERN = Pattern.compile(
            "(?:[$£€¥₹]\\s*)?([0-9]{1,3}(?:,?[0-9]{3})*(?:\\.[0-9]{2})?)"
    );

    private static final Pattern CURRENCY_PATTERN = Pattern.compile(
            "(USD|EUR|GBP|JPY|INR|\\$|£|€|¥|₹)"
    );

    private static final Pattern PRODUCT_CODE_PATTERN = Pattern.compile(
            "(?:SKU|Code|Item|Product\\s*#?)\\s*:?\\s*([A-Z0-9-]+)",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern PRODUCT_NAME_PATTERN = Pattern.compile(
            "(?:Product|Item|Name)\\s*:?\\s*([A-Za-z0-9\\s\\-]+)",
            Pattern.CASE_INSENSITIVE
    );

    public List<PriceInfo> extractPricesFromPdf(InputStream pdfInputStream, String fileName) throws Exception {
        List<PriceInfo> priceInfoList = new ArrayList<>();

        try (PDDocument document = PDDocument.load(pdfInputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            int numberOfPages = document.getNumberOfPages();

            for (int page = 1; page <= numberOfPages; page++) {
                stripper.setStartPage(page);
                stripper.setEndPage(page);
                String pageText = stripper.getText(document);

                List<PriceInfo> pageData = extractPricesFromText(pageText, fileName, page);
                priceInfoList.addAll(pageData);
            }
        }

        return priceInfoList;
    }

    private List<PriceInfo> extractPricesFromText(String text, String fileName, int pageNumber) {
        List<PriceInfo> priceInfoList = new ArrayList<>();

        // Split text into lines for better parsing
        String[] lines = text.split("\\r?\\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty()) {
                continue;
            }

            // Check if line contains price information
            Matcher priceMatcher = PRICE_PATTERN.matcher(line);

            while (priceMatcher.find()) {
                PriceInfo priceInfo = new PriceInfo();

                // Extract price
                String priceStr = priceMatcher.group(1).replace(",", "");
                try {
                    BigDecimal price = new BigDecimal(priceStr);
                    priceInfo.setPrice(price);
                } catch (NumberFormatException e) {
                    continue; // Skip if price is not valid
                }

                // Extract currency
                String currency = extractCurrency(line);
                priceInfo.setCurrency(currency);

                // Extract product code
                String productCode = extractProductCode(line);
                if (productCode == null && i > 0) {
                    // Try previous line
                    productCode = extractProductCode(lines[i - 1]);
                }
                priceInfo.setProductCode(productCode);

                // Extract product name
                String productName = extractProductName(line);
                if (productName == null && i > 0) {
                    // Try previous line
                    productName = extractProductName(lines[i - 1]);
                }
                if (productName == null && line.length() > 10) {
                    // Use the line itself as product name if it's descriptive enough
                    productName = line.substring(0, Math.min(100, line.length()));
                }
                priceInfo.setProductName(productName);

                // Set metadata
                priceInfo.setPdfFileName(fileName);
                priceInfo.setPageNumber(pageNumber);
                priceInfo.setRawText(line);
                priceInfo.setExtractedDate(LocalDateTime.now());

                // Add to list if we have at least a price
                if (priceInfo.getPrice() != null) {
                    priceInfoList.add(priceInfo);
                }
            }
        }

        return priceInfoList;
    }

    private String extractCurrency(String text) {
        Matcher matcher = CURRENCY_PATTERN.matcher(text);
        if (matcher.find()) {
            String currency = matcher.group(1);
            // Convert symbols to currency codes
            switch (currency) {
                case "$":
                    return "USD";
                case "£":
                    return "GBP";
                case "€":
                    return "EUR";
                case "¥":
                    return "JPY";
                case "₹":
                    return "INR";
                default:
                    return currency;
            }
        }
        return "USD"; // Default currency
    }

    private String extractProductCode(String text) {
        Matcher matcher = PRODUCT_CODE_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private String extractProductName(String text) {
        Matcher matcher = PRODUCT_NAME_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
}
