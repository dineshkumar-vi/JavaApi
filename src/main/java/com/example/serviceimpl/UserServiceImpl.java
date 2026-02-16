package com.example.serviceimpl;

import java.nio.charset.StandardCharsets;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.data.Captcha;
import com.example.data.User;
import com.example.repository.UserRepo;
import com.example.service.UserService;
import com.google.gson.Gson;

@Component
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepo userRepo;

	@Override
	public User checkUser(User user) {
	  try {
		User userFromDb = userRepo.findByUserNameAndPassword(user.getUserName(), user.getPassword());
		if(userFromDb.getUserName() != null) {
			return userFromDb;
		 }
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	
	@Override
	public boolean validateCaptcha(String captcha, String ipAddress) {
		
		try( CloseableHttpClient httpClient = HttpClients.createDefault(); ){
			HttpGet httpGet = new HttpGet("http://localhost:8080/captcha?captcha="+captcha+"&ipAddress="+ipAddress);
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			String response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
			if(response != null) {
				Gson g = new Gson();
				Captcha p = g.fromJson(response, Captcha.class);
				if(p.getCaptcha() != null) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
