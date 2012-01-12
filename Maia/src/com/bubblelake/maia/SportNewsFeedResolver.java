package com.bubblelake.maia;

import java.util.ArrayList;
import java.util.List;

public class SportNewsFeedResolver extends NewsFeedResolver {
	public static final String CNN_NAME = "CNN Sport";
	public static final String CNN_FEED = "http://rss.cnn.com/rss/edition_sport.rss";
	public static final int CNN_LOGO = R.drawable.cnn;
	
	public static final String ESPN_NAME = "ESPN";
	public static final String ESPN_FEED = "http://sports.espn.go.com/espn/rss/news";
	public static final int ESPN_LOGO = R.drawable.espn;
	
	public static final String BBC_NAME = "BBC Sport";
	public static final String BBC_FEED = "http://newsrss.bbc.co.uk/rss/sportonline_uk_edition/front_page/rss.xml";
	public static final int BBC_LOGO = R.drawable.bbc_sport;
	
	
	@Override
	public List<NewsFeedItem> getFeedItems() {
		ArrayList<NewsFeedItem> result = new ArrayList<NewsFeedItem>();
		result.add(getCnnItem());
		result.add(getEspnItem());
		result.add(getBbcItem());
		return result;
	}

	private NewsFeedItem getCnnItem(){
		NewsFeedItem item = new NewsFeedItem();
		item.setName(CNN_NAME);
		item.setFeedUrl(CNN_FEED);
		item.setLogoId(CNN_LOGO);
		return item;
	}
	
	private NewsFeedItem getEspnItem(){
		NewsFeedItem item = new NewsFeedItem();
		item.setName(ESPN_NAME);
		item.setFeedUrl(ESPN_FEED);
		item.setLogoId(ESPN_LOGO);
		return item;
	}
	
	
	private NewsFeedItem getBbcItem(){
		NewsFeedItem item = new NewsFeedItem();
		item.setName(BBC_NAME);
		item.setFeedUrl(BBC_FEED);
		item.setLogoId(BBC_LOGO);
		return item;
	}
}
