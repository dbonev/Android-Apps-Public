package com.bubblelake.maia;

import java.util.List;
import java.util.Random;

public class JokeOfTheDayProvider extends StringProvider {
	public final static String URL = "http://jokes4all.net/rss/140010213/jokes.xml";

	public JokeOfTheDayProvider(){		
		this.setDisplayName(SettingsManager.getInstance(null).getString(R.string.jokeOfTheDay));
	}
	
	@Override
	public String getString(MainTTS context) {
		List<Message> jokes = new FeedParser(true).parse(getUrl());
		
		Random randomGenerator = new Random();
		int random = randomGenerator.nextInt(jokes.size());
		Message joke = jokes.get(random);
		
		String res = context.getResources().getString(R.string.hereIsAJoke) + process(joke.getDescription());
		return res;
	}
	
	@Override
	public boolean canDuplicate(){
		return true;
	}
	
	private String getUrl(){
		return URL;
	}	
	
	private String process(String description) {
		
		String res = description.replace("<br>", "\n");
		return res;
	}			
}
