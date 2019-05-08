package com.example.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "captcha")
public class Captcha {
	
	@Id
	private String id;
	
	private String captcha;
	
	private String ipAddress;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	@Override
    public String toString() {
        return String.format(
                "Captcha[id=%s, captcha='%s', ipAddress='%s']",
                id, captcha, ipAddress);
    }

}
