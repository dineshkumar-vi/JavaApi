package com.example.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.data.User;
import com.example.repository.CaptchaRepo;
import com.example.repository.UserRepo;
import com.example.service.UserService;

public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepo userRepo;

	@Override
	public boolean checkUser(User user) {
		User userFromDb = userRepo.findByUserNameAndPassword(user.getUserName(), user.getPassword());
		if(userFromDb.getUserName() != null) {
			return true;
		}
		return false;
	}

}
