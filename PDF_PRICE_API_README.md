# PDF Price Extraction API

## Overview
This API provides endpoints to upload PDF files, extract price information, and manage price data in MongoDB.

## Features

### PDF Processing
- Upload PDF files (up to 10MB)
- Automatic extraction of:
  - Product prices
  - Currency symbols (USD, EUR, GBP, JPY, INR)
  - Product codes/SKU
  - Product names
  - Page numbers
  - Raw text content

### Price Management
- Create, Read, Update, Delete (CRUD) operations
- Search and filter capabilities:
  - By product name
  - By product code
  - By category
  - By supplier
  - By price range
  - By keyword

## API Endpoints

### Upload PDF and Extract Prices
```http
POST /api/prices/upload-pdf
Content-Type: multipart/form-data

Parameters:
- file: PDF file (required)

Response:
{
  "success": true,
  "message": "PDF processed successfully",
  "extractedCount": 15,
  "prices": [
    {
      "id": "65a1b2c3d4e5f6g7h8i9j0k1",
      "productName": "Product Name",
      "productCode": "SKU-12345",
      "price": 99.99,
      "currency": "USD",
      "pdfFileName": "pricelist.pdf",
      "pageNumber": 1,
      "extractedDate": "2024-01-20T10:30:00"
    }
  ]
}
```

### Create Price Info Manually
```http
POST /api/prices
Content-Type: application/json

Request Body:
{
  "productName": "Sample Product",
  "productCode": "SKU-001",
  "price": 149.99,
  "currency": "USD",
  "description": "Product description",
  "category": "Electronics",
  "supplier": "ABC Supplier"
}

Response: 201 Created
{
  "id": "65a1b2c3d4e5f6g7h8i9j0k1",
  "productName": "Sample Product",
  ...
}
```

### Get All Price Info
```http
GET /api/prices

Response: 200 OK
[
  {
    "id": "65a1b2c3d4e5f6g7h8i9j0k1",
    "productName": "Product 1",
    "price": 99.99,
    ...
  },
  ...
]
```

### Get Price Info by ID
```http
GET /api/prices/{id}

Response: 200 OK
{
  "id": "65a1b2c3d4e5f6g7h8i9j0k1",
  "productName": "Product Name",
  ...
}
```

### Update Price Info
```http
PUT /api/prices/{id}
Content-Type: application/json

Request Body:
{
  "price": 199.99,
  "description": "Updated description"
}

Response: 200 OK
{
  "id": "65a1b2c3d4e5f6g7h8i9j0k1",
  "price": 199.99,
  ...
}
```

### Delete Price Info
```http
DELETE /api/prices/{id}

Response: 200 OK
{
  "success": true,
  "message": "Price info deleted successfully"
}
```

### Search by Product Name
```http
GET /api/prices/search/product-name?name=ProductName

Response: 200 OK
[...]
```

### Search by Product Code
```http
GET /api/prices/search/product-code?code=SKU-001

Response: 200 OK
[...]
```

### Search by Category
```http
GET /api/prices/search/category?category=Electronics

Response: 200 OK
[...]
```

### Search by Supplier
```http
GET /api/prices/search/supplier?supplier=ABC%20Supplier

Response: 200 OK
[...]
```

### Search by Price Range
```http
GET /api/prices/search/price-range?minPrice=10.00&maxPrice=100.00

Response: 200 OK
[...]
```

### Search by Keyword
```http
GET /api/prices/search/keyword?keyword=laptop

Response: 200 OK
[...]
```

## Data Model

### PriceInfo Entity
```json
{
  "id": "string",
  "productName": "string",
  "productCode": "string",
  "price": "BigDecimal",
  "currency": "string",
  "description": "string",
  "category": "string",
  "supplier": "string",
  "extractedDate": "LocalDateTime",
  "pdfFileName": "string",
  "pageNumber": "integer",
  "rawText": "string"
}
```

## PDF Price Extraction Logic

### Supported Patterns

#### Price Patterns
- $99.99
- £49.99
- €79.99
- ¥1,000
- ₹999.00
- 1,234.56

#### Currency Symbols
- $ → USD
- £ → GBP
- € → EUR
- ¥ → JPY
- ₹ → INR

#### Product Code Patterns
- SKU: ABC-123
- Code: XYZ789
- Item: 12345
- Product #: PRD-001

#### Product Name Patterns
- Product: Product Name
- Item: Item Name
- Name: Name Here

### Extraction Process
1. PDF is loaded using Apache PDFBox
2. Each page is processed separately
3. Text is extracted line by line
4. Regex patterns identify price, currency, codes, and names
5. Related information on adjacent lines is combined
6. Extracted data is saved to MongoDB

## Testing with cURL

### Upload a PDF
```bash
curl -X POST http://localhost:8080/api/prices/upload-pdf \
  -F "file=@/path/to/pricelist.pdf"
```

### Get All Prices
```bash
curl http://localhost:8080/api/prices
```

### Create Price Manually
```bash
curl -X POST http://localhost:8080/api/prices \
  -H "Content-Type: application/json" \
  -d '{
    "productName": "Test Product",
    "productCode": "TEST-001",
    "price": 99.99,
    "currency": "USD"
  }'
```

### Search by Price Range
```bash
curl "http://localhost:8080/api/prices/search/price-range?minPrice=10.00&maxPrice=100.00"
```

## Configuration

### application.properties
```properties
# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/demo
```

### Adjust Maximum File Size
To allow larger PDF files, modify in application.properties:
```properties
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

## Error Handling

### Common Errors

#### File Too Large
```json
{
  "success": false,
  "error": "Maximum upload size exceeded"
}
```

#### Invalid File Type
```json
{
  "success": false,
  "error": "Only PDF files are supported"
}
```

#### Empty File
```json
{
  "success": false,
  "error": "File is empty"
}
```

#### Price Not Found
```json
{
  "success": false,
  "error": "PriceInfo not found with id: 123"
}
```

## Best Practices

### PDF Format Recommendations
For best extraction results:
1. Use text-based PDFs (not scanned images)
2. Maintain consistent formatting
3. Keep price near product information
4. Use clear product codes/SKUs
5. Include currency symbols

### Performance Tips
1. Process large PDFs in batches
2. Index MongoDB fields used in searches
3. Cache frequently accessed data
4. Limit result set sizes with pagination

## Future Enhancements

Potential improvements:
- OCR support for scanned PDFs
- Batch processing of multiple PDFs
- Advanced price history tracking
- Export to CSV/Excel
- Price comparison analytics
- Notification for price changes
- Image-based price extraction
- Multi-language support
