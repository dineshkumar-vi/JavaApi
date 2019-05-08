package com.example.controllor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Boolean> create(@RequestBody User user) {
		if(user == null || user.getUserName() == null || user.getPassword() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		boolean isUserAvilable = userService.checkUser(user);
		return new ResponseEntity<Boolean>(isUserAvilable, HttpStatus.OK);
	}

}
