#!/bin/bash

# Sample PDF Extraction API Requests
# Make this file executable: chmod +x sample-pdf-requests.sh

BASE_URL="http://localhost:8080/api/pdf"

echo "======================================"
echo "PDF Extraction API - Sample Requests"
echo "======================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 1. Extract PDF from URL
echo -e "${BLUE}1. Extract PDF from URL${NC}"
echo "POST $BASE_URL/extract"
curl -X POST "$BASE_URL/extract" \
  -H "Content-Type: application/json" \
  -d '{
    "url": "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
  }' | json_pp || cat
echo ""
echo ""

# Wait a moment for extraction to complete
sleep 2

# 2. Get all PDF documents
echo -e "${BLUE}2. Get all PDF documents${NC}"
echo "GET $BASE_URL"
curl -X GET "$BASE_URL" | json_pp || cat
echo ""
echo ""

# 3. Search PDF by URL
echo -e "${BLUE}3. Search PDF by URL${NC}"
echo "GET $BASE_URL/search"
curl -X GET "$BASE_URL/search?url=https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf" | json_pp || cat
echo ""
echo ""

# 4. Extract another PDF
echo -e "${BLUE}4. Extract another PDF (sample)${NC}"
echo "POST $BASE_URL/extract"
curl -X POST "$BASE_URL/extract" \
  -H "Content-Type: application/json" \
  -d '{
    "url": "https://www.africau.edu/images/default/sample.pdf"
  }' | json_pp || cat
echo ""
echo ""

# 5. Get all PDFs again (should show 2 documents)
echo -e "${BLUE}5. Get all PDFs (should show multiple documents)${NC}"
echo "GET $BASE_URL"
curl -X GET "$BASE_URL" | json_pp || cat
echo ""
echo ""

# 6. Get PDF by ID (you'll need to replace {id} with actual ID from previous responses)
echo -e "${BLUE}6. Get PDF by ID${NC}"
echo "Note: Replace {id} with actual document ID from previous responses"
echo "GET $BASE_URL/{id}"
echo "Example: curl -X GET \"$BASE_URL/65d1234567890abcdef12345\""
echo ""
echo ""

# 7. Delete PDF by ID (you'll need to replace {id} with actual ID)
echo -e "${BLUE}7. Delete PDF by ID${NC}"
echo "Note: Replace {id} with actual document ID"
echo "DELETE $BASE_URL/{id}"
echo "Example: curl -X DELETE \"$BASE_URL/65d1234567890abcdef12345\""
echo ""
echo ""

# 8. Test with invalid URL
echo -e "${BLUE}8. Test error handling - Empty URL${NC}"
echo "POST $BASE_URL/extract"
curl -X POST "$BASE_URL/extract" \
  -H "Content-Type: application/json" \
  -d '{
    "url": ""
  }' | json_pp || cat
echo ""
echo ""

# 9. Test with non-existent ID
echo -e "${BLUE}9. Test error handling - Non-existent ID${NC}"
echo "GET $BASE_URL/nonexistent123"
curl -X GET "$BASE_URL/nonexistent123" | json_pp || cat
echo ""
echo ""

echo -e "${GREEN}======================================"
echo "Sample requests completed!"
echo "======================================${NC}"
echo ""
echo "Tips:"
echo "- Replace {id} placeholders with actual document IDs from responses"
echo "- Use 'json_pp' or 'jq' for pretty-printed JSON output"
echo "- Check $BASE_URL to see all extracted PDFs"
echo "- Refer to PDF_EXTRACTION_API.md for complete API documentation"
