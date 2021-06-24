package com.example.demo.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class User {
	@Id
    private ObjectId _id;
	private String username;
	private String password;
	private List<Trade> trades;
	
	public User()
	{
		
	}
	public User(ObjectId _id, String username, String password, List<Trade> trades) {
		super();
		this._id = _id;
		this.username = username;
		this.password = password;
		this.trades = trades;
	}
	public User(String username, String password) {
		
		this.username = username;
		this.password = password;
		this.trades = trades;
	}
	public String get_id() {
		return _id.toHexString(); 
	}
	public void set_id(ObjectId _id) {
		this._id = _id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Trade> getTrades() {
		return trades;
	}
	public void setTrades(List<Trade> trades) {
		this.trades = trades;
	}
	
}
