package com.bubblelake.maia;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedResolverTop extends NewsFeedResolver {
	public static final String CNN_NAME = "CNN";
	public static final String CNN_FEED = "http://rss.cnn.com/rss/edition.rss";
	public static final int CNN_LOGO = R.drawable.cnn;
	
	public static final String BBC_NAME = "BBC";
	public static final String BBC_FEED = "http://feeds.bbci.co.uk/news/rss.xml";
	public static final int BBC_LOGO = R.drawable.bbc_news_logo;
	
	public static final String REUTERS_NAME = "Reuters";
	public static final String REUTERS_FEED = "http://feeds.reuters.com/reuters/topNews";
	public static final int REUTERS_LOGO = R.drawable.reuters;
	
	@Override
	public List<NewsFeedItem> getFeedItems(){		
		ArrayList<NewsFeedItem> result = new ArrayList<NewsFeedItem>();
		result.add(getCnnItem());	
		result.add(getBBCItem());	
		result.add(getReutersItem());
		return result;
	}
	
	private NewsFeedItem getCnnItem(){
		NewsFeedItem item = new NewsFeedItem();
		item.setName(CNN_NAME);
		item.setFeedUrl(CNN_FEED);
		item.setLogoId(CNN_LOGO);
		return item;
	}
	
	private NewsFeedItem getBBCItem(){
		NewsFeedItem item = new NewsFeedItem();
		item.setName(BBC_NAME);
		item.setFeedUrl(BBC_FEED);
		item.setLogoId(BBC_LOGO);
		return item;
	}
	
	private NewsFeedItem getReutersItem(){
		NewsFeedItem item = new NewsFeedItem();
		item.setName(REUTERS_NAME);
		item.setFeedUrl(REUTERS_FEED);
		item.setLogoId(REUTERS_LOGO);
		return item;
	}
		
}
