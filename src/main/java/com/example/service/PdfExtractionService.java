package com.example.service;

import org.springframework.stereotype.Service;

import com.example.data.PdfDocument;

import java.util.List;

@Service
public interface PdfExtractionService {

	PdfDocument extractPdfFromUrl(String url) throws Exception;

	PdfDocument getPdfDocumentById(String id);

	List<PdfDocument> getAllPdfDocuments();

	PdfDocument getPdfDocumentByUrl(String url);

	void deletePdfDocument(String id);
}
