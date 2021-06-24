package com.example.demo.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import com.example.demo.model.TradeState.State;
import com.example.demo.model.TradeType.Type;


@Component
public class Trade {

    @Id
    private ObjectId _id;
    private Date created = new Date(System.currentTimeMillis());
    private State state = TradeState.State.CREATED;
    private Type type = TradeType.Type.BUY;
    private String ticker;
    private int quantity;
    private double price;
    
    public Trade() {}
    public Trade(ObjectId _id, Date created, State state, Type type, String ticker, int quantity, double price) {
		super();
		this._id = _id;
		this.created = created;
		this.state = state;
		this.type = type;
		this.ticker = ticker;
		this.quantity = quantity;
		this.price = price;
	}

	public String get_id() { return _id.toHexString(); }
	
	public void set_id(ObjectId _id) { this._id = _id; }
	
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
