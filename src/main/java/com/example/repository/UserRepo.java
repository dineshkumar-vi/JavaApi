package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.data.User;

public interface UserRepo extends MongoRepository<User, String>{
	
	public User findByUserNameAndPassword(String userName, String password);

}
