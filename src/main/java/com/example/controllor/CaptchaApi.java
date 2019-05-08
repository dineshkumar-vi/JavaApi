package com.example.controllor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.data.Captcha;
import com.example.service.CaptchaService;

@RestController
@RequestMapping(path="/captcha")
public class CaptchaApi {
	
	
	@Autowired
	CaptchaService captchaService;
	
	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Captcha> create(@RequestBody Captcha captcha) {
		if(captcha == null || captcha.getIpAddress() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Captcha response = captchaService.createCaptcha(captcha);
		return new ResponseEntity<Captcha>(response, HttpStatus.OK);
	}
	
	
	@GetMapping(produces= "application/json")
	public ResponseEntity<Captcha> get(@RequestParam String ipAddress) {
		if(ipAddress == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Captcha response = captchaService.get(ipAddress);
		return new ResponseEntity<Captcha>(response, HttpStatus.OK);
	}

}
