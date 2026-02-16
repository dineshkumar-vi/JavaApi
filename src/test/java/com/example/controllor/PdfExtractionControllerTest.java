package com.example.controllor;

import com.example.data.PdfDocument;
import com.example.service.PdfExtractionService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PdfExtractionControllerTest {

	@Mock
	private PdfExtractionService pdfExtractionService;

	@InjectMocks
	private PdfExtractionController pdfExtractionController;

	private Gson gson;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		gson = new Gson();
	}

	@Test
	public void testExtractPdfFromUrl_Success() throws Exception {
		Map<String, String> request = new HashMap<>();
		request.put("url", "http://example.com/test.pdf");

		PdfDocument pdfDocument = new PdfDocument();
		pdfDocument.setId("123");
		pdfDocument.setUrl("http://example.com/test.pdf");
		pdfDocument.setStatus("SUCCESS");

		when(pdfExtractionService.getPdfDocumentByUrl(anyString())).thenReturn(null);
		when(pdfExtractionService.extractPdfFromUrl(anyString())).thenReturn(pdfDocument);

		ResponseEntity<?> response = pdfExtractionController.extractPdfFromUrl(request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		verify(pdfExtractionService, times(1)).extractPdfFromUrl("http://example.com/test.pdf");
	}

	@Test
	public void testExtractPdfFromUrl_EmptyUrl() {
		Map<String, String> request = new HashMap<>();
		request.put("url", "");

		ResponseEntity<?> response = pdfExtractionController.extractPdfFromUrl(request);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(pdfExtractionService, never()).extractPdfFromUrl(anyString());
	}

	@Test
	public void testExtractPdfFromUrl_AlreadyExists() {
		Map<String, String> request = new HashMap<>();
		request.put("url", "http://example.com/existing.pdf");

		PdfDocument existingDocument = new PdfDocument();
		existingDocument.setId("456");
		existingDocument.setUrl("http://example.com/existing.pdf");
		existingDocument.setStatus("SUCCESS");

		when(pdfExtractionService.getPdfDocumentByUrl("http://example.com/existing.pdf"))
				.thenReturn(existingDocument);

		ResponseEntity<?> response = pdfExtractionController.extractPdfFromUrl(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(pdfExtractionService, never()).extractPdfFromUrl(anyString());
	}

	@Test
	public void testExtractPdfFromUrl_Exception() throws Exception {
		Map<String, String> request = new HashMap<>();
		request.put("url", "http://example.com/error.pdf");

		when(pdfExtractionService.getPdfDocumentByUrl(anyString())).thenReturn(null);
		when(pdfExtractionService.extractPdfFromUrl(anyString()))
				.thenThrow(new Exception("Network error"));

		ResponseEntity<?> response = pdfExtractionController.extractPdfFromUrl(request);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	public void testGetPdfDocumentById_Found() {
		PdfDocument pdfDocument = new PdfDocument();
		pdfDocument.setId("123");

		when(pdfExtractionService.getPdfDocumentById("123")).thenReturn(pdfDocument);

		ResponseEntity<?> response = pdfExtractionController.getPdfDocumentById("123");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(pdfExtractionService, times(1)).getPdfDocumentById("123");
	}

	@Test
	public void testGetPdfDocumentById_NotFound() {
		when(pdfExtractionService.getPdfDocumentById("999")).thenReturn(null);

		ResponseEntity<?> response = pdfExtractionController.getPdfDocumentById("999");

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void testGetAllPdfDocuments() {
		PdfDocument doc1 = new PdfDocument();
		doc1.setId("1");
		PdfDocument doc2 = new PdfDocument();
		doc2.setId("2");

		when(pdfExtractionService.getAllPdfDocuments()).thenReturn(Arrays.asList(doc1, doc2));

		ResponseEntity<List<PdfDocument>> response = pdfExtractionController.getAllPdfDocuments();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
	}

	@Test
	public void testGetPdfDocumentByUrl_Found() {
		PdfDocument pdfDocument = new PdfDocument();
		pdfDocument.setUrl("http://example.com/test.pdf");

		when(pdfExtractionService.getPdfDocumentByUrl("http://example.com/test.pdf"))
				.thenReturn(pdfDocument);

		ResponseEntity<?> response = pdfExtractionController
				.getPdfDocumentByUrl("http://example.com/test.pdf");

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetPdfDocumentByUrl_NotFound() {
		when(pdfExtractionService.getPdfDocumentByUrl(anyString())).thenReturn(null);

		ResponseEntity<?> response = pdfExtractionController
				.getPdfDocumentByUrl("http://example.com/notfound.pdf");

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void testDeletePdfDocument_Success() {
		PdfDocument pdfDocument = new PdfDocument();
		pdfDocument.setId("123");

		when(pdfExtractionService.getPdfDocumentById("123")).thenReturn(pdfDocument);
		doNothing().when(pdfExtractionService).deletePdfDocument("123");

		ResponseEntity<?> response = pdfExtractionController.deletePdfDocument("123");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(pdfExtractionService, times(1)).deletePdfDocument("123");
	}

	@Test
	public void testDeletePdfDocument_NotFound() {
		when(pdfExtractionService.getPdfDocumentById("999")).thenReturn(null);

		ResponseEntity<?> response = pdfExtractionController.deletePdfDocument("999");

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		verify(pdfExtractionService, never()).deletePdfDocument(anyString());
	}
}
