# PDF Extraction API Documentation

## Overview
This API provides endpoints to extract text content from PDF files at given URLs and store them in MongoDB.

## Technology Stack
- Spring Boot 3.3.6
- MongoDB
- Apache PDFBox 3.0.3
- Java 25

## Features
- Extract text content from PDF files via URL
- Store extracted content in MongoDB
- Retrieve PDF documents by ID or URL
- List all extracted PDF documents
- Delete PDF documents
- Duplicate detection (prevents re-extraction of same URL)
- Error handling with detailed messages

## API Endpoints

### 1. Extract PDF Content
**POST** `/api/pdf/extract`

Extracts text content from a PDF at the given URL and stores it in MongoDB.

**Request Body:**
```json
{
  "url": "http://example.com/document.pdf"
}
```

**Response (Success - 201 Created):**
```json
{
  "message": "PDF content extracted successfully",
  "document": {
    "id": "507f1f77bcf86cd799439011",
    "url": "http://example.com/document.pdf",
    "content": "Extracted text content...",
    "fileName": "document.pdf",
    "pageCount": 10,
    "extractedAt": "2024-02-15T10:30:00",
    "status": "SUCCESS",
    "errorMessage": null
  }
}
```

**Response (Already Exists - 200 OK):**
```json
{
  "message": "PDF already extracted",
  "document": {
    "id": "507f1f77bcf86cd799439011",
    ...
  }
}
```

**Response (Error - 400 Bad Request):**
```json
{
  "error": "URL is required",
  "message": "Please provide a valid PDF URL"
}
```

**Response (Error - 500 Internal Server Error):**
```json
{
  "error": "Extraction failed",
  "message": "Failed to extract PDF content: Connection timeout"
}
```

### 2. Get PDF Document by ID
**GET** `/api/pdf/{id}`

Retrieves a PDF document by its MongoDB ID.

**Response (Success - 200 OK):**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "url": "http://example.com/document.pdf",
  "content": "Extracted text content...",
  "fileName": "document.pdf",
  "pageCount": 10,
  "extractedAt": "2024-02-15T10:30:00",
  "status": "SUCCESS",
  "errorMessage": null
}
```

**Response (Not Found - 404 Not Found):**
```json
{
  "error": "Not found",
  "message": "PDF document not found with ID: 507f1f77bcf86cd799439011"
}
```

### 3. Get All PDF Documents
**GET** `/api/pdf`

Retrieves all PDF documents, ordered by extraction date (newest first).

**Response (Success - 200 OK):**
```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "url": "http://example.com/document1.pdf",
    "content": "Content 1...",
    ...
  },
  {
    "id": "507f1f77bcf86cd799439012",
    "url": "http://example.com/document2.pdf",
    "content": "Content 2...",
    ...
  }
]
```

### 4. Search PDF Document by URL
**GET** `/api/pdf/search?url={url}`

Searches for a PDF document by its original URL.

**Query Parameters:**
- `url` (required): The PDF URL to search for

**Response (Success - 200 OK):**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "url": "http://example.com/document.pdf",
  ...
}
```

**Response (Not Found - 404 Not Found):**
```json
{
  "error": "Not found",
  "message": "No PDF document found for URL: http://example.com/document.pdf"
}
```

### 5. Delete PDF Document
**DELETE** `/api/pdf/{id}`

Deletes a PDF document by its ID.

**Response (Success - 200 OK):**
```json
{
  "message": "PDF document deleted successfully",
  "id": "507f1f77bcf86cd799439011"
}
```

**Response (Not Found - 404 Not Found):**
```json
{
  "error": "Not found",
  "message": "PDF document not found with ID: 507f1f77bcf86cd799439011"
}
```

## Data Model

### PdfDocument
```java
{
  id: String                    // MongoDB document ID
  url: String                   // Original PDF URL
  content: String               // Extracted text content
  fileName: String              // File name extracted from URL
  pageCount: Integer            // Number of pages in PDF
  extractedAt: LocalDateTime    // Timestamp of extraction
  status: String                // SUCCESS, FAILED, or PENDING
  errorMessage: String          // Error message if status is FAILED
}
```

## MongoDB Collection
- **Collection Name:** `pdf_documents`
- **Database:** As configured in `application.properties`

## Configuration

Ensure MongoDB connection is configured in `application.properties` or `application.yml`:

```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=your_database_name
```

## Error Handling

The API handles various error scenarios:
- Invalid or missing URL
- Network connection failures
- Invalid PDF files
- MongoDB connection issues
- General extraction errors

All errors return appropriate HTTP status codes and descriptive error messages.

## Example Usage

### Using cURL

```bash
# Extract PDF content
curl -X POST http://localhost:8080/api/pdf/extract \
  -H "Content-Type: application/json" \
  -d '{"url": "http://example.com/sample.pdf"}'

# Get all PDF documents
curl http://localhost:8080/api/pdf

# Get PDF by ID
curl http://localhost:8080/api/pdf/507f1f77bcf86cd799439011

# Search by URL
curl "http://localhost:8080/api/pdf/search?url=http://example.com/sample.pdf"

# Delete PDF document
curl -X DELETE http://localhost:8080/api/pdf/507f1f77bcf86cd799439011
```

### Using JavaScript/Fetch

```javascript
// Extract PDF content
fetch('http://localhost:8080/api/pdf/extract', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    url: 'http://example.com/sample.pdf'
  })
})
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(error => console.error('Error:', error));
```

## Testing

Run tests using Gradle:
```bash
./gradlew test
```

Test files are located in:
- `src/test/java/com/example/controllor/PdfExtractionControllerTest.java`
- `src/test/java/com/example/serviceimpl/PdfExtractionServiceImplTest.java`
- `src/test/java/com/example/repository/PdfDocumentRepositoryTest.java`
- `src/test/java/com/example/data/PdfDocumentTest.java`

## Notes
- The API supports CORS with origins set to "*" (configure appropriately for production)
- Large PDF files may take longer to process
- Ensure sufficient memory allocation for large PDF processing
- The extracted content is stored as plain text without formatting
