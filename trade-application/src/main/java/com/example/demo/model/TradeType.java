package com.example.demo.model;

public class TradeType{
public enum Type {
    BUY,
    SELL;
};
private Type tradeType;

public TradeType(Type tradeType) {
	super();
	this.tradeType = tradeType;
}
public Type getTradeType() {
	return tradeType;
}
public void setTradeType(Type tradeType) {
	this.tradeType = tradeType;
}

}
