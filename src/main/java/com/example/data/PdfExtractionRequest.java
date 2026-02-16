package com.example.data;

public class PdfExtractionRequest {

	private String url;

	public PdfExtractionRequest() {
	}

	public PdfExtractionRequest(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
