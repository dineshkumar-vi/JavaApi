package com.example.serviceimpl;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
			String response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
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
