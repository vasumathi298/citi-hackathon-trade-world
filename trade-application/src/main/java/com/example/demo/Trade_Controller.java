package com.example.demo;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Trade;
import com.example.demo.model.TradeState;
import com.example.demo.model.TradeType;
import com.example.demo.repository.Trade_Repository;

import yahoofinance.YahooFinance;




public class Trade_Controller {
	@Autowired
	private Trade_Repository repository;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<Trade> getAllTrades() {
	  return repository.findAll();
	}
	
	@RequestMapping(value = "/addTrades", method = RequestMethod.GET)
	public void addTrades() throws IOException {
//	  Trade trade1 = new Trade(new ObjectId(), new Date(), TradeState.State.CREATED, TradeType.Type.BUY, "SBIN", 50, 1000.0);
//	  repository.save(trade1);
//	  Trade trade2 = new Trade(new ObjectId(), new Date(), TradeState.State.CREATED, TradeType.Type.BUY, "RELIANCE", 2, 2000.0);
//	  repository.save(trade2);
//	  Trade trade3 = new Trade(new ObjectId(), new Date(), TradeState.State.CREATED, TradeType.Type.BUY, "HDFCBANK", 10, 3000.0);
//	  repository.save(trade3);
		
		String[] symbol = new String[] {"AIG", "U11.SI", "G07.SI"};
		 double amount [] = new double[3];
		 for (int i=0; i<symbol.length; i++) {
		  amount[i] = YahooFinance.get(symbol[i]).getQuote().getPrice().doubleValue();
		  }
		 // double amount = YahooFinance.get("BAC").getQuote().getPrice().doubleValue();
		  Trade trade1 = new Trade(new ObjectId(), new Date(), TradeState.State.CREATED, TradeType.Type.BUY, symbol[0], 50, amount[0]);
		  repository.save(trade1);
		  Trade trade2 = new Trade(new ObjectId(), new Date(), TradeState.State.CREATED, TradeType.Type.BUY, symbol[1], 2, amount[1]);
		  repository.save(trade2);
		  Trade trade3 = new Trade(new ObjectId(), new Date(), TradeState.State.CREATED, TradeType.Type.BUY, symbol[2], 10, amount[2]);
		  repository.save(trade3);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Trade getTradeById(@PathVariable("id") ObjectId id) {
	  return repository.findBy_id(id);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void modifyTradeById(@PathVariable("id") ObjectId id, @Valid @RequestBody Trade trade) {
	  trade.set_id(id);
	  Trade trade1 = getTradeById(id);
	  trade.setPrice(trade1.getPrice());
	  trade.setTicker(trade1.getTicker());
	  repository.save(trade);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<Object> createTrade(@Valid @RequestBody Trade trade) throws IOException {
	    trade.set_id(ObjectId.get());
		trade.setCreated(new Date());
		trade.setState(TradeState.State.CREATED);
		//trade.setType(TradeType.Type.BUY);
	    String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "HSBC", "JPM", "WFC", "BAC", "SAN", "RC", "GS", "LYG", "USB", "UBS", "MS", "AXP", "S23.SI", "HSBC", "D05.SI"};
	    double price [] = new double[symbols.length];
	    int flag = 50;
	    for (int i=0; i<symbols.length; i++) {
	       price[i] = YahooFinance.get(symbols[i]).getQuote().getPrice().doubleValue();
	  	    }
	    for(int j=0;j<symbols.length; j++) { 
		    if(trade.getTicker().equals(symbols[j])) {
		    	trade.setPrice(price[j]);
			    flag = 1;
			    repository.save(trade);
			    break;
		    }
		    else {
			    flag = 0;
		    }
	    }
	    if (flag == 0) {
		    System.out.println("Invalid Ticker");
		    return new ResponseEntity<>("Invalid Ticker!! Trade not created", HttpStatus.FORBIDDEN);
	    }
	    else {
	    	return new ResponseEntity<>("Trade created successfully !!", HttpStatus.OK);
	    }
	  }

	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteTrade(@PathVariable ObjectId id) {
	  repository.delete(repository.findBy_id(id));
	}
	
	@RequestMapping(value = "/{id}/updateStatus", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateStatus(@PathVariable ObjectId id, @RequestParam String status) {
	   Trade trade = repository.findBy_id(id);
	   if(trade.getState().equals(TradeState.State.CREATED))
	   {
		   trade.setState(TradeState.State.valueOf(status));
		   repository.save(trade);
		   return new ResponseEntity<>("Trade status updated successfully", HttpStatus.OK);
	   }
	   else if(trade.getState().equals(TradeState.State.PROCESSING))
	   {
		   if(!status.equals("CREATED"))
		   {
			   trade.setState(TradeState.State.valueOf(status));
			   repository.save(trade);
			   return new ResponseEntity<>("Trade status updated successfully", HttpStatus.OK);
		   }
		   else
		   {
			   return new ResponseEntity<>("Invalid operation", HttpStatus.FORBIDDEN);
		   }
	   }
	   else if(trade.getState().equals(TradeState.State.FILLED))
	   {
		   if(!(status.equals("CREATED") || status.equals("PENDING")))
		   {
			   trade.setState(TradeState.State.valueOf(status));
			   repository.save(trade);
			   return new ResponseEntity<>("Trade status updated successfully", HttpStatus.OK);
		   }
		   else
		   {
			   return new ResponseEntity<>("Invalid operation", HttpStatus.FORBIDDEN);
		   }
	   }
	   else if(trade.getState().equals(TradeState.State.FILLED))
	   {
		   if(!(status.equals("CREATED") || status.equals("PROCESSING") || status.equals("FILLED")))
		   {
			   trade.setState(TradeState.State.valueOf(status));
			   repository.save(trade);
			   return new ResponseEntity<>("Trade status updated successfully", HttpStatus.OK);
		   }
		   else
		   {
			   return new ResponseEntity<>("Invalid operation", HttpStatus.FORBIDDEN);
		   }
	   }
	   else {
		   return new ResponseEntity<>("Invalid operation", HttpStatus.FORBIDDEN);
	   }
//	   repository.save(trade);
//	   return trade;
	}


}
