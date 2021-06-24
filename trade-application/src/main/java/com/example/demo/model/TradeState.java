package com.example.demo.model;


public class TradeState{
	public enum State {
	    CREATED,
	    PROCESSING,
	    FILLED,
	    REJECTED;
	};
	private State tradeStatus;
	public TradeState(State tradeStatus) {
		super();
		this.tradeStatus = tradeStatus;
	}
	public State getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(State tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	
}



