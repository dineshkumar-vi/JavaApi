package com.example.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.data.Captcha;

@Repository
public interface CaptchaRepo extends MongoRepository<Captcha, String>{
	
	// Find the Captch by ip address
	public List<Captcha> findByIpAddress(String ipAddress);
	
	
}
