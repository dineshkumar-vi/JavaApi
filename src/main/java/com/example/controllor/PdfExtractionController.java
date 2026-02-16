package com.example.controllor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.data.PdfDocument;
import com.example.service.PdfExtractionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/pdf")
@CrossOrigin(origins = "*")
public class PdfExtractionController {

	@Autowired
	private PdfExtractionService pdfExtractionService;

	/**
	 * Extract content from PDF at given URL and store in MongoDB
	 * @param request Map containing 'url' key with PDF URL value
	 * @return ResponseEntity with extraction result
	 */
	@PostMapping(value = "/extract", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> extractPdfFromUrl(@RequestBody Map<String, String> request) {
		String url = request.get("url");

		if (url == null || url.trim().isEmpty()) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "URL is required");
			errorResponse.put("message", "Please provide a valid PDF URL");
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		try {
			// Check if PDF already exists
			PdfDocument existingDocument = pdfExtractionService.getPdfDocumentByUrl(url);
			if (existingDocument != null && "SUCCESS".equals(existingDocument.getStatus())) {
				Map<String, Object> response = new HashMap<>();
				response.put("message", "PDF already extracted");
				response.put("document", existingDocument);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

			// Extract PDF content
			PdfDocument pdfDocument = pdfExtractionService.extractPdfFromUrl(url);

			Map<String, Object> response = new HashMap<>();
			response.put("message", "PDF content extracted successfully");
			response.put("document", pdfDocument);

			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "Extraction failed");
			errorResponse.put("message", e.getMessage());
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Get PDF document by ID
	 * @param id Document ID
	 * @return ResponseEntity with PDF document
	 */
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<?> getPdfDocumentById(@PathVariable String id) {
		PdfDocument pdfDocument = pdfExtractionService.getPdfDocumentById(id);

		if (pdfDocument == null) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "Not found");
			errorResponse.put("message", "PDF document not found with ID: " + id);
			return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(pdfDocument, HttpStatus.OK);
	}

	/**
	 * Get all PDF documents
	 * @return ResponseEntity with list of all PDF documents
	 */
	@GetMapping(produces = "application/json")
	public ResponseEntity<List<PdfDocument>> getAllPdfDocuments() {
		List<PdfDocument> documents = pdfExtractionService.getAllPdfDocuments();
		return new ResponseEntity<>(documents, HttpStatus.OK);
	}

	/**
	 * Search PDF document by URL
	 * @param url PDF URL to search
	 * @return ResponseEntity with PDF document
	 */
	@GetMapping(value = "/search", produces = "application/json")
	public ResponseEntity<?> getPdfDocumentByUrl(@RequestParam String url) {
		if (url == null || url.trim().isEmpty()) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "URL parameter is required");
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		PdfDocument pdfDocument = pdfExtractionService.getPdfDocumentByUrl(url);

		if (pdfDocument == null) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "Not found");
			errorResponse.put("message", "No PDF document found for URL: " + url);
			return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(pdfDocument, HttpStatus.OK);
	}

	/**
	 * Delete PDF document by ID
	 * @param id Document ID
	 * @return ResponseEntity with deletion status
	 */
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<?> deletePdfDocument(@PathVariable String id) {
		PdfDocument pdfDocument = pdfExtractionService.getPdfDocumentById(id);

		if (pdfDocument == null) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "Not found");
			errorResponse.put("message", "PDF document not found with ID: " + id);
			return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		}

		pdfExtractionService.deletePdfDocument(id);

		Map<String, String> response = new HashMap<>();
		response.put("message", "PDF document deleted successfully");
		response.put("id", id);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
