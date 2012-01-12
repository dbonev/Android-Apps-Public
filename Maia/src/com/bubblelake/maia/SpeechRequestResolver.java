package com.bubblelake.maia;

import java.util.ArrayList;

public class SpeechRequestResolver {
	public static final String CNN = "CNN";
	public static final String NEWS = "NEWS";
	public static final String SPORT = "SPORT";
	public static final String BBC = "BBC";
	public static final String JOKE = "joke";
	public static final String JOKES = "jokes";
	public static final String FUN = "fun";
	public static final String CALENDAR = "calendar";
	public static final String APPOINTMENT = "appointment";
	public static final String APPOINTMENTS = "appointments";
	public static final String WEATHER = "weather";
	public static final String HOROSCOPE = "horoscope";
	public static final String STOCK = "stock";
	public static final String STOCKS = "stocks";
	public static final String QUOTES = "quotes";
	public static final String TECHNOLOGY = "technology";
	public static final String LOCAL = "local";
	public static final String TIME = "time";
	
	public static Greeting GetTempGreeting(ArrayList<String> matches){
		Greeting result =  new Greeting();
		StringBuilder sb = new StringBuilder();
		for (String m : matches){
			sb.append(m);
			sb.append(" ");
		}
		StringProvider provider = get_provider(sb.toString());
		if (provider != null){
			result.getProviders().add(provider);
		}
		
		return result;
	}

	private static StringProvider get_provider(String m) {
		if (m == null){
			return get_default_provider();
		}
		else if (m.toUpperCase().contains(CNN)){
			return SettingsManager.createCNNProvider("10");
		}
		else if (m.toLowerCase().contains(JOKE) || m.toLowerCase().contains(JOKES) || m.toLowerCase().contains(FUN)){
			return new JokeOfTheDayProvider();
		}			
		else if (m.toLowerCase().contains(CALENDAR) 
				|| m.toLowerCase().contains(APPOINTMENT) 
				|| m.toLowerCase().contains(APPOINTMENTS)){
			return new CalendarAppointmentsProvider();
		}
		else if (m.toLowerCase().contains(WEATHER)){
			return new WeatherProvider();
		}
		else if (m.toUpperCase().contains(BBC)){
			return SettingsManager.createBBCProvider("10");
		}
		else if (m.toUpperCase().contains(NEWS)){
			if (m.toUpperCase().contains(SPORT)){
				return SettingsManager.createCNNSportProvider("10");
			} 
			else if (m.toLowerCase().contains(TECHNOLOGY)){
				return SettingsManager.createTechMemeProvider("10");
			} else if (m.toLowerCase().contains(LOCAL)){
				LocalNewsProvider res = new LocalNewsProvider();
				res.setExtra1("10");
				return res;
			}
			else {
				return SettingsManager.createCNNProvider("10");
			}
		}
		if (m.toLowerCase().contains(TECHNOLOGY)){
			return SettingsManager.createTechMemeProvider("10");
		}
		else if (m.toLowerCase().contains(HOROSCOPE)){
			Class c = DailyHoroscopeProvider.class;
			//try to find existing first
			for (Greeting g : GreetingsFactory.getGreetings(null)){
				for (StringProvider sp : g.getProviders()){					
					if (c.isInstance(sp)){
						return sp;
					}
				}
			}
		}		
		else if (m.toLowerCase().contains(STOCK) || m.toLowerCase().contains(QUOTES) || m.toLowerCase().contains(STOCKS)){
			Class c = StockQuoteProvider.class;
			//try to find existing first
			for (Greeting g : GreetingsFactory.getGreetings(null)){
				for (StringProvider sp : g.getProviders()){					
					if (c.isInstance(sp)){
						return sp;
					}
				}
			}
			return new StockQuoteProvider("AAPL, GOOG, MSFT, ^IXIC, ^DJI");
		}
		else if (m.toLowerCase().contains(TIME)){
			return new CurrentTimeProvider();
		}
		
		return get_default_provider();
	}

	private static StringProvider get_default_provider() {		
		SalutationProvider sp = new SalutationProvider();
		sp.setExtra1("I didn't really understand what you mean.");		
		return sp;
	}
}
