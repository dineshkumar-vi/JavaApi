# Quick Start Guide

## What Was Done

Your JavaApi project has been successfully modernized with:

1. ✅ **Gradle Conversion**: Migrated from Maven to Gradle 8.5
2. ✅ **Java 21**: Updated from Java 1.8 to Java 21
3. ✅ **Spring Boot 3.2.2**: Updated from Spring Boot 2.1.4
4. ✅ **PDF Price Extraction API**: Complete REST API for extracting and managing price data from PDFs

## Quick Start (3 Steps)

### Step 1: Verify Java 21 is Installed
```bash
java -version
# Should show: java version "21" or higher
```

If not installed, download from: https://adoptium.net/

### Step 2: Build the Project
```bash
cd /Users/dineshkumar/Documents/auto-code-agent/JavaApi
./gradlew build
```

### Step 3: Run the Application
```bash
./gradlew bootRun
```

The application will start at: http://localhost:8080

## Test the New PDF API

### 1. Create a Test PDF with Price Data
Save this as `test-prices.txt` (or use any PDF with prices):
```
Product Catalog 2024

Product: Laptop Computer
SKU: LAP-001
Price: $1,299.99

Product: Wireless Mouse
SKU: MOU-002
Price: $29.99

Product: USB-C Cable
SKU: CAB-003
Price: $15.50
```

Convert to PDF using any tool, or use an existing price list PDF.

### 2. Upload PDF to Extract Prices
```bash
curl -X POST http://localhost:8080/api/prices/upload-pdf \
  -F "file=@test-prices.pdf"
```

Response:
```json
{
  "success": true,
  "message": "PDF processed successfully",
  "extractedCount": 3,
  "prices": [...]
}
```

### 3. Retrieve All Prices
```bash
curl http://localhost:8080/api/prices
```

### 4. Search by Price Range
```bash
curl "http://localhost:8080/api/prices/search/price-range?minPrice=10&maxPrice=50"
```

## API Endpoints Available

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/prices/upload-pdf` | Upload PDF and extract prices |
| POST | `/api/prices` | Create price manually |
| GET | `/api/prices` | Get all prices |
| GET | `/api/prices/{id}` | Get price by ID |
| PUT | `/api/prices/{id}` | Update price |
| DELETE | `/api/prices/{id}` | Delete price |
| GET | `/api/prices/search/product-name?name=X` | Search by name |
| GET | `/api/prices/search/product-code?code=X` | Search by code |
| GET | `/api/prices/search/category?category=X` | Search by category |
| GET | `/api/prices/search/supplier?supplier=X` | Search by supplier |
| GET | `/api/prices/search/price-range?minPrice=X&maxPrice=Y` | Search by range |
| GET | `/api/prices/search/keyword?keyword=X` | Search by keyword |

## Project Structure

```
JavaApi/
├── build.gradle              # Gradle build config (NEW)
├── gradlew                   # Gradle wrapper (NEW)
├── src/
│   └── main/
│       ├── java/com/example/
│       │   ├── controllor/
│       │   │   └── PriceController.java (NEW)
│       │   ├── data/
│       │   │   └── PriceInfo.java (NEW)
│       │   ├── repository/
│       │   │   └── PriceInfoRepository.java (NEW)
│       │   ├── service/
│       │   │   └── PriceService.java (NEW)
│       │   ├── serviceimpl/
│       │   │   └── PriceServiceImpl.java (NEW)
│       │   └── util/
│       │       └── PdfPriceExtractor.java (NEW)
│       └── resources/
│           └── application.properties (UPDATED)
```

## Technologies Now Used

- **Java 21** (latest LTS)
- **Spring Boot 3.2.2** (latest stable)
- **Gradle 8.5** (build tool)
- **Apache PDFBox 3.0.1** (PDF processing)
- **MongoDB** (database)

## Documentation

For detailed information, see:

- **GRADLE_MIGRATION_GUIDE.md** - Gradle migration details
- **PDF_PRICE_API_README.md** - Complete API documentation
- **IMPLEMENTATION_SUMMARY.md** - Full implementation details

## Troubleshooting

### Build Fails
```bash
# Clean and rebuild
./gradlew clean build
```

### MongoDB Connection Error
```bash
# Check MongoDB is running
mongod --version

# Start MongoDB (macOS with Homebrew)
brew services start mongodb-community

# Start MongoDB (Linux)
sudo systemctl start mongod
```

### Port Already in Use
Edit `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Permission Denied on gradlew
```bash
chmod +x gradlew
```

## What's Next?

1. ✅ Test the PDF upload API with your own PDF files
2. ✅ Explore the search and filter capabilities
3. ✅ Add authentication/authorization if needed
4. ✅ Deploy to production environment
5. ✅ Add monitoring and logging

## Need Help?

- Check **GRADLE_MIGRATION_GUIDE.md** for Gradle-specific issues
- Check **PDF_PRICE_API_README.md** for API usage examples
- Check **IMPLEMENTATION_SUMMARY.md** for architecture details

## Success Checklist

- [ ] Java 21 installed
- [ ] MongoDB running
- [ ] Project builds successfully (`./gradlew build`)
- [ ] Application starts (`./gradlew bootRun`)
- [ ] Can access http://localhost:8080
- [ ] Can upload PDF file
- [ ] Prices extracted and saved
- [ ] Can query prices via API

---

**Status**: 🎉 Ready to Use!
**Version**: 1.0.0
**Last Updated**: 2024-01-20
