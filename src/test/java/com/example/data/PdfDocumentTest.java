package com.example.data;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PdfDocumentTest {

	@Test
	public void testPdfDocumentCreation() {
		PdfDocument pdfDocument = new PdfDocument();
		assertNotNull(pdfDocument);
		assertEquals("PENDING", pdfDocument.getStatus());
		assertNotNull(pdfDocument.getExtractedAt());
	}

	@Test
	public void testPdfDocumentGettersAndSetters() {
		PdfDocument pdfDocument = new PdfDocument();

		pdfDocument.setId("123");
		pdfDocument.setUrl("http://example.com/test.pdf");
		pdfDocument.setContent("Test content");
		pdfDocument.setFileName("test.pdf");
		pdfDocument.setPageCount(5);
		pdfDocument.setStatus("SUCCESS");
		pdfDocument.setErrorMessage(null);

		assertEquals("123", pdfDocument.getId());
		assertEquals("http://example.com/test.pdf", pdfDocument.getUrl());
		assertEquals("Test content", pdfDocument.getContent());
		assertEquals("test.pdf", pdfDocument.getFileName());
		assertEquals(5, pdfDocument.getPageCount());
		assertEquals("SUCCESS", pdfDocument.getStatus());
		assertNull(pdfDocument.getErrorMessage());
	}

	@Test
	public void testPdfDocumentWithError() {
		PdfDocument pdfDocument = new PdfDocument();
		pdfDocument.setStatus("FAILED");
		pdfDocument.setErrorMessage("Connection timeout");

		assertEquals("FAILED", pdfDocument.getStatus());
		assertEquals("Connection timeout", pdfDocument.getErrorMessage());
	}
}
