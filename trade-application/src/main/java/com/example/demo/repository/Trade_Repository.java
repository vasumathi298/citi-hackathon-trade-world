package com.example.demo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.model.Trade;

public interface Trade_Repository extends MongoRepository<Trade, String>{
	Trade findBy_id(ObjectId _id);
}



