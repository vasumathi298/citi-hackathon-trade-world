package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Trade;
import com.example.demo.model.TradeState;
import com.example.demo.model.User;
import com.example.demo.repository.Trade_Repository;
import com.example.demo.repository.User_Repository;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class Portfolio_Controller {
	@Autowired
	private User_Repository repository;
	
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void signup(@Valid @RequestBody User user) {
		repository.save(user);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public User login(@Valid @RequestBody User user) {
		List<User> users = getUsers();
		for(User u: users)
		{
			
			if(user.getUsername().equals(u.getUsername()))
			{
				if(user.getPassword().equals(u.getPassword()))
				{
					return getUserById(new ObjectId(u.get_id()));
				}
				else
				{
					System.out.println("Password incorrect");
				}
			}
			else
			{
				System.out.println("User doesn't exist");
			}
		}
		return new User("","");
	}
	
	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	public List<User> getUsers() {
		return repository.findAll();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public User getUserById(@PathVariable("id") ObjectId id) {
		return repository.findBy_id(id); 
	}
	
	@RequestMapping(value = "/{id}/trades", method = RequestMethod.GET)
	public List<Trade> getUserTrades(@PathVariable("id") ObjectId id) {
		User user = repository.findBy_id(id); 
		//System.out.println("in getusertrades");
		return user.getTrades();
	}
	
	@RequestMapping(value = "/{id}/trades", method = RequestMethod.POST)
	public ResponseEntity<Object> createTrade(@PathVariable("id") ObjectId id, @Valid @RequestBody Trade trade )
			throws IOException{
		trade.set_id(ObjectId.get());
		trade.setCreated(new Date());
		trade.setState(TradeState.State.CREATED);
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
			    User user = repository.findBy_id(id); 
			    List<Trade> trades = user.getTrades();
			    if(trades == null)
			    	trades = new ArrayList<Trade>();
			    //System.out.println(trade.getTicker());
			    trades.add(trade);
			    user.setTrades(trades);
			    repository.save(user);
			    break;
		    }
		    else {
			    flag = 0;
		    }
	    }
	    if (flag == 0) {
		    System.out.println("Invalid Ticker");
		    return new ResponseEntity<>("Invalid Ticker! Trade not created", HttpStatus.FORBIDDEN);
	    }
	    else {
	    	return new ResponseEntity<>("Trade created successfully !!", HttpStatus.OK);
	    }
	}
	
	@RequestMapping(value = "/{id}/trades/{tradeid}", method = RequestMethod.DELETE)
	public void deleteTradeById(@PathVariable ObjectId id, @PathVariable ObjectId tradeid) {
		User user = repository.findBy_id(id); 
		List<Trade> trades = user.getTrades();
		List<Trade> trades1 = trades;
		for(Trade trade: trades)
		{
			System.out.println(trade.get_id());
			System.out.println(tradeid);
			if(trade.get_id().equals(tradeid.toString()))
			{
				trades1.remove(trade);
				user.setTrades(trades1);
				System.out.println("HI");
				repository.save(user);
				break;
			}
		}
	}
	
	@RequestMapping(value = "/{id}/trades/{tradeid}", method = RequestMethod.GET)
	public Trade getTradeById(@PathVariable("id") ObjectId id, @PathVariable ObjectId tradeid) {
	  System.out.println("in gettradebyid");
	  User user = repository.findBy_id(id); 
	  List<Trade> trades = user.getTrades();
	  for(Trade trade: trades)
	  {
		  if(trade.get_id().equals(tradeid.toString()))
		  {
			  System.out.println("matching");
			  return trade;
		  }
		  else
		  {
			  System.out.println("not matching "+trade.get_id()+" "+tradeid.toString());
		  }
	  }
	  return new Trade();
	}
	
	@RequestMapping(value = "/{id}/trades/{tradeid}", method = RequestMethod.PUT)
	public void modifyTradeById(@PathVariable("id") ObjectId id, @PathVariable ObjectId tradeid,
			@Valid @RequestBody Trade trade) {
	  User user = repository.findBy_id(id); 
	  List<Trade> trades = user.getTrades();
	  for(Trade t: trades)
	  {
		  if(t.get_id().equals(tradeid.toString()) && t.getState().equals(TradeState.State.CREATED))
		  {
			 t.setQuantity(trade.getQuantity());
		  }
	  }
	  user.setTrades(trades);
	  repository.save(user);
	}
	
	@RequestMapping(value = "/{id}/tradeAdvice/{tradeid}", method = RequestMethod.GET)
	public String getTradeAdvice(@PathVariable("id") ObjectId id, @PathVariable ObjectId tradeid) throws IOException {
		User user = repository.findBy_id(id); 
		List<Trade> trades = user.getTrades();
		for(Trade t: trades)
		  {
			  if(t.get_id().equals(tradeid.toString()))
			  {
				  String tradeticker = t.getTicker();
					Calendar from = Calendar.getInstance();
					Calendar to = Calendar.getInstance();
					from.add(Calendar.WEEK_OF_MONTH, -1);
					
					Stock ticker = YahooFinance.get(tradeticker);
					List<HistoricalQuote> tickerHistQuotes = ticker.getHistory(from, to, Interval.DAILY);
					double previous_value = tickerHistQuotes.get(0).getClose().doubleValue();
					double current_value = tickerHistQuotes.get(tickerHistQuotes.size()-1).getClose().doubleValue();
					double current_value_1 = tickerHistQuotes.get(tickerHistQuotes.size()-2).getClose().doubleValue();
					
					double sum = 0.0;
					for(int j=0;j<tickerHistQuotes.size(); j++) { 
						sum = sum+tickerHistQuotes.get(j).getClose().doubleValue();
					}	
					sum = sum/tickerHistQuotes.size();
					
					double no0_1 = current_value * 0.001;
					double current_value0_1_high = current_value + no0_1;
					double current_value0_1_less = current_value - no0_1;
					
					if(sum >= current_value0_1_less && sum <=current_value0_1_high) {
						return "The price of this stock has been quite constant for the past few days."
								+ " Hence we suggest you to SELL this stock!";
					}
					else if((current_value < current_value_1) && (sum < previous_value)) {
						return "The price of this stock has been decreasing for the past few days."
								+ " Hence we suggest you to BUY this stock!";
					}
					else  {
						return "The price of this stock is more likely to increase further in the next few days."
								+ " Hence we suggest you to HOLD this stock for now!";
					}
			  }
		  }
		return "";
	}
}
