package com.example.serviceimpl;

import java.security.SecureRandom;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.data.Captcha;
import com.example.repository.CaptchaRepo;
import com.example.service.CaptchaService;

@Component
public class CaptchaServiceImpl implements CaptchaService{
	
	@Autowired
	private CaptchaRepo captchaRepo;
	
	private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
    private static final SecureRandom RANDOM = new SecureRandom();

	@Override
	public Captcha createCaptcha(Captcha captcha) {
		System.out.println(" Capatcha Service :  " + captcha.getIpAddress());
		try {
			
			captcha.setCaptcha(getRandomString(6));
		    System.out.println(captcha.getCaptcha());
		    return captchaRepo.save(captcha);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println( " Error occured while creating captcha :" + e.getMessage());
			return null;
		}
	}
	
	private String getRandomString(int count) {
		StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
		return sb.toString();
	}

	@Override
	public Captcha get(String ipAddress) {
		List<Captcha> captchaList =  captchaRepo.findByIpAddress(ipAddress);
		if(captchaList != null && captchaList.size() > 0) {
			Captcha captcha =  captchaList.get(captchaList.size()-1);
			return captcha;
		}
		return null;
	}
}
