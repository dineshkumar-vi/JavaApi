package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.data.PdfDocument;

import java.util.List;
import java.util.Optional;

@Repository
public interface PdfDocumentRepository extends MongoRepository<PdfDocument, String> {

	Optional<PdfDocument> findByUrl(String url);

	List<PdfDocument> findByStatus(String status);

	List<PdfDocument> findAllByOrderByExtractedAtDesc();
}
