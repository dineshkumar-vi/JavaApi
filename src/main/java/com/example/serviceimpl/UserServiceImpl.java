package com.example.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.data.User;
import com.example.repository.UserRepo;
import com.example.service.UserService;

@Component
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepo userRepo;

	@Override
	public User checkUser(User user) {
		User userFromDb = userRepo.findByUserNameAndPassword(user.getUserName(), user.getPassword());
		if(userFromDb.getUserName() != null) {
			return userFromDb;
		}
		return null;
	}

}
