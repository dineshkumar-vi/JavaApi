package com.example.serviceimpl;

import com.example.data.PdfDocument;
import com.example.repository.PdfDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PdfExtractionServiceImplTest {

	@Mock
	private PdfDocumentRepository pdfDocumentRepository;

	@InjectMocks
	private PdfExtractionServiceImpl pdfExtractionService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetPdfDocumentById() {
		PdfDocument pdfDocument = new PdfDocument();
		pdfDocument.setId("123");
		pdfDocument.setUrl("http://example.com/test.pdf");

		when(pdfDocumentRepository.findById("123")).thenReturn(Optional.of(pdfDocument));

		PdfDocument result = pdfExtractionService.getPdfDocumentById("123");

		assertNotNull(result);
		assertEquals("123", result.getId());
		verify(pdfDocumentRepository, times(1)).findById("123");
	}

	@Test
	public void testGetPdfDocumentByIdNotFound() {
		when(pdfDocumentRepository.findById("999")).thenReturn(Optional.empty());

		PdfDocument result = pdfExtractionService.getPdfDocumentById("999");

		assertNull(result);
		verify(pdfDocumentRepository, times(1)).findById("999");
	}

	@Test
	public void testGetAllPdfDocuments() {
		PdfDocument doc1 = new PdfDocument();
		doc1.setId("1");
		PdfDocument doc2 = new PdfDocument();
		doc2.setId("2");

		when(pdfDocumentRepository.findAllByOrderByExtractedAtDesc())
				.thenReturn(Arrays.asList(doc1, doc2));

		List<PdfDocument> result = pdfExtractionService.getAllPdfDocuments();

		assertEquals(2, result.size());
		verify(pdfDocumentRepository, times(1)).findAllByOrderByExtractedAtDesc();
	}

	@Test
	public void testGetPdfDocumentByUrl() {
		PdfDocument pdfDocument = new PdfDocument();
		pdfDocument.setUrl("http://example.com/test.pdf");

		when(pdfDocumentRepository.findByUrl("http://example.com/test.pdf"))
				.thenReturn(Optional.of(pdfDocument));

		PdfDocument result = pdfExtractionService.getPdfDocumentByUrl("http://example.com/test.pdf");

		assertNotNull(result);
		assertEquals("http://example.com/test.pdf", result.getUrl());
		verify(pdfDocumentRepository, times(1)).findByUrl("http://example.com/test.pdf");
	}

	@Test
	public void testDeletePdfDocument() {
		doNothing().when(pdfDocumentRepository).deleteById("123");

		pdfExtractionService.deletePdfDocument("123");

		verify(pdfDocumentRepository, times(1)).deleteById("123");
	}
}
