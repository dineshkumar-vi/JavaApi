package com.example.repository;

import com.example.data.PdfDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@SpringJUnitConfig
public class PdfDocumentRepositoryTest {

	@Autowired
	private PdfDocumentRepository pdfDocumentRepository;

	@Test
	public void testSavePdfDocument() {
		PdfDocument pdfDocument = new PdfDocument();
		pdfDocument.setUrl("http://example.com/test.pdf");
		pdfDocument.setContent("Test content");
		pdfDocument.setFileName("test.pdf");
		pdfDocument.setPageCount(3);
		pdfDocument.setStatus("SUCCESS");

		PdfDocument saved = pdfDocumentRepository.save(pdfDocument);

		assertNotNull(saved.getId());
		assertEquals("http://example.com/test.pdf", saved.getUrl());
	}

	@Test
	public void testFindByUrl() {
		PdfDocument pdfDocument = new PdfDocument();
		pdfDocument.setUrl("http://example.com/unique.pdf");
		pdfDocument.setContent("Unique content");
		pdfDocument.setStatus("SUCCESS");

		pdfDocumentRepository.save(pdfDocument);

		Optional<PdfDocument> found = pdfDocumentRepository.findByUrl("http://example.com/unique.pdf");

		assertTrue(found.isPresent());
		assertEquals("Unique content", found.get().getContent());
	}

	@Test
	public void testFindByStatus() {
		PdfDocument pdfDocument1 = new PdfDocument();
		pdfDocument1.setUrl("http://example.com/success1.pdf");
		pdfDocument1.setStatus("SUCCESS");
		pdfDocumentRepository.save(pdfDocument1);

		PdfDocument pdfDocument2 = new PdfDocument();
		pdfDocument2.setUrl("http://example.com/success2.pdf");
		pdfDocument2.setStatus("SUCCESS");
		pdfDocumentRepository.save(pdfDocument2);

		List<PdfDocument> successDocs = pdfDocumentRepository.findByStatus("SUCCESS");

		assertTrue(successDocs.size() >= 2);
	}
}
