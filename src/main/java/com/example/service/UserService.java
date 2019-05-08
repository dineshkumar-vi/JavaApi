package com.example.service;

import org.springframework.stereotype.Service;

import com.example.data.User;

@Service
public interface UserService {
	
	User checkUser(User user);

}
