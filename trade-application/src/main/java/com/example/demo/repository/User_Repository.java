package com.example.demo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.User;

public interface User_Repository extends MongoRepository<User, String>{
	User findBy_id(ObjectId _id);
}
