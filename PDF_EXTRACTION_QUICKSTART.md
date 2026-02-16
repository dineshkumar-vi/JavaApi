# PDF Extraction Feature - Quick Start Guide

## What Was Created

A complete REST API for extracting text content from PDF files at given URLs and storing them in MongoDB.

## Files Created

### Main Source Files (10 files)
1. **Controller Layer**
   - `PdfExtractionController.java` - REST API endpoints

2. **Service Layer**
   - `PdfExtractionService.java` - Service interface
   - `PdfExtractionServiceImpl.java` - Service implementation

3. **Data/Model Layer**
   - `PdfDocument.java` - MongoDB entity for storing PDF content
   - `PdfExtractionRequest.java` - DTO for API requests

4. **Repository Layer**
   - `PdfDocumentRepository.java` - MongoDB repository interface

5. **Test Files** (4 test classes)
   - `PdfExtractionControllerTest.java` - Controller tests
   - `PdfExtractionServiceImplTest.java` - Service tests
   - `PdfDocumentRepositoryTest.java` - Repository tests
   - `PdfDocumentTest.java` - Entity tests

6. **Configuration**
   - `build.gradle` - Updated with Apache PDFBox dependency

7. **Documentation**
   - `PDF_EXTRACTION_API.md` - Complete API documentation
   - `PDF_EXTRACTION_QUICKSTART.md` - This file

## Prerequisites

1. Java 25 installed
2. MongoDB running on `localhost:27017`
3. Database name: `demo` (as configured in application.properties)

## Build and Run

### Using Gradle

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## Quick Test

### 1. Extract PDF Content

```bash
curl -X POST http://localhost:8080/api/pdf/extract \
  -H "Content-Type: application/json" \
  -d '{"url": "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"}'
```

### 2. Get All Extracted PDFs

```bash
curl http://localhost:8080/api/pdf
```

### 3. Get PDF by ID (replace {id} with actual ID from step 1)

```bash
curl http://localhost:8080/api/pdf/{id}
```

## API Endpoints Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/pdf/extract` | Extract PDF content from URL |
| GET | `/api/pdf` | Get all PDF documents |
| GET | `/api/pdf/{id}` | Get PDF document by ID |
| GET | `/api/pdf/search?url={url}` | Search PDF by URL |
| DELETE | `/api/pdf/{id}` | Delete PDF document |

## Features

- Extracts text from PDF files at given URLs
- Stores content in MongoDB with metadata
- Prevents duplicate extraction (checks if URL already processed)
- Comprehensive error handling
- Full test coverage
- RESTful API design
- CORS enabled

## MongoDB Collection Structure

```javascript
{
  _id: ObjectId("..."),
  url: "http://example.com/document.pdf",
  content: "Full text content extracted from PDF...",
  fileName: "document.pdf",
  pageCount: 10,
  extractedAt: ISODate("2024-02-15T10:30:00Z"),
  status: "SUCCESS",
  errorMessage: null
}
```

## Dependencies Added

- **Apache PDFBox 3.0.3** - For PDF text extraction

## Testing

Run all tests:
```bash
./gradlew test
```

Run specific test:
```bash
./gradlew test --tests PdfExtractionControllerTest
```

## Troubleshooting

### MongoDB Connection Error
Ensure MongoDB is running:
```bash
# Check MongoDB status
brew services list | grep mongodb

# Start MongoDB (macOS)
brew services start mongodb-community
```

### PDF Download Error
- Ensure the PDF URL is accessible
- Check if the URL requires authentication
- Verify network connectivity

### Out of Memory Error
For large PDFs, increase JVM memory:
```bash
./gradlew bootRun -Dorg.gradle.jvmargs="-Xmx2g"
```

## Project Structure

```
JavaApi/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── controllor/
│   │   │   │   └── PdfExtractionController.java
│   │   │   ├── service/
│   │   │   │   └── PdfExtractionService.java
│   │   │   ├── serviceimpl/
│   │   │   │   └── PdfExtractionServiceImpl.java
│   │   │   ├── repository/
│   │   │   │   └── PdfDocumentRepository.java
│   │   │   └── data/
│   │   │       ├── PdfDocument.java
│   │   │       └── PdfExtractionRequest.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/
│           ├── controllor/
│           ├── service/
│           ├── serviceimpl/
│           ├── repository/
│           └── data/
├── build.gradle
├── PDF_EXTRACTION_API.md
└── PDF_EXTRACTION_QUICKSTART.md
```

## Next Steps

1. Customize MongoDB connection in `application.properties`
2. Run tests to verify installation: `./gradlew test`
3. Start the application: `./gradlew bootRun`
4. Test with the provided cURL commands
5. Integrate with your frontend application

## Support

For detailed API documentation, refer to `PDF_EXTRACTION_API.md`
