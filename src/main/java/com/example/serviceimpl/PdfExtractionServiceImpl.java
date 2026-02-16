package com.example.serviceimpl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.data.PdfDocument;
import com.example.repository.PdfDocumentRepository;
import com.example.service.PdfExtractionService;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PdfExtractionServiceImpl implements PdfExtractionService {

	@Autowired
	private PdfDocumentRepository pdfDocumentRepository;

	@Override
	public PdfDocument extractPdfFromUrl(String url) throws Exception {
		PdfDocument pdfDocument = new PdfDocument();
		pdfDocument.setUrl(url);

		try {
			// Download PDF from URL
			URL pdfUrl = new URL(url);
			String fileName = extractFileNameFromUrl(url);
			pdfDocument.setFileName(fileName);

			// Load PDF document
			try (InputStream inputStream = pdfUrl.openStream();
			     PDDocument document = PDDocument.load(inputStream)) {

				// Extract text content
				PDFTextStripper stripper = new PDFTextStripper();
				String content = stripper.getText(document);

				// Set document properties
				pdfDocument.setContent(content);
				pdfDocument.setPageCount(document.getNumberOfPages());
				pdfDocument.setStatus("SUCCESS");
				pdfDocument.setExtractedAt(LocalDateTime.now());

			}

			// Save to MongoDB
			return pdfDocumentRepository.save(pdfDocument);

		} catch (Exception e) {
			pdfDocument.setStatus("FAILED");
			pdfDocument.setErrorMessage(e.getMessage());
			pdfDocumentRepository.save(pdfDocument);
			throw new Exception("Failed to extract PDF content: " + e.getMessage(), e);
		}
	}

	@Override
	public PdfDocument getPdfDocumentById(String id) {
		Optional<PdfDocument> pdfDocument = pdfDocumentRepository.findById(id);
		return pdfDocument.orElse(null);
	}

	@Override
	public List<PdfDocument> getAllPdfDocuments() {
		return pdfDocumentRepository.findAllByOrderByExtractedAtDesc();
	}

	@Override
	public PdfDocument getPdfDocumentByUrl(String url) {
		Optional<PdfDocument> pdfDocument = pdfDocumentRepository.findByUrl(url);
		return pdfDocument.orElse(null);
	}

	@Override
	public void deletePdfDocument(String id) {
		pdfDocumentRepository.deleteById(id);
	}

	private String extractFileNameFromUrl(String url) {
		String[] parts = url.split("/");
		if (parts.length > 0) {
			return parts[parts.length - 1];
		}
		return "unknown.pdf";
	}
}
