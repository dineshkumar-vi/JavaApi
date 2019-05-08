package com.example.service;

import org.springframework.stereotype.Service;

import com.example.data.Captcha;

@Service
public interface CaptchaService {
	
	Captcha createCaptcha(Captcha captcha);
	
	Captcha get(String ipAddress);

}
