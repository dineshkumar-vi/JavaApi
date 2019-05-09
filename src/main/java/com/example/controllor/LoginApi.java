package com.example.controllor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.data.User;
import com.example.service.UserService;

@RestController
@RequestMapping(path="/login")
public class LoginApi {
	
	@Autowired
	UserService userService;
	

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> create(@RequestBody User user) {
		if(user == null || user.getUserName() == null || user.getPassword() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// Validate the captch and ipaddress
		boolean validateCaptcha = userService.validateCaptcha(user.getCaptcha(), user.getIpAddress());
		if(validateCaptcha) {
			User dbuser = userService.checkUser(user);
			if(dbuser != null) {
				return new ResponseEntity<String>("User Validated successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Invalid username and password", HttpStatus.BAD_REQUEST);
			}
			
		} else {
			return new ResponseEntity<String>("Invalid Captcha , Please enter correct captcha", HttpStatus.BAD_REQUEST);
		}

	}

}
