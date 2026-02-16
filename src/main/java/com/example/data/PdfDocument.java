package com.example.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "pdf_documents")
public class PdfDocument {

	@Id
	private String id;

	private String url;

	private String content;

	private String fileName;

	private Integer pageCount;

	private LocalDateTime extractedAt;

	private String status;

	private String errorMessage;

	public PdfDocument() {
		this.extractedAt = LocalDateTime.now();
		this.status = "PENDING";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public LocalDateTime getExtractedAt() {
		return extractedAt;
	}

	public void setExtractedAt(LocalDateTime extractedAt) {
		this.extractedAt = extractedAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
