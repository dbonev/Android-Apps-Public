package com.bubblelake.maia;

import java.util.List;
import java.util.ArrayList;
 
public class NewsFeedResolverTech extends NewsFeedResolver {
	public static final String TECHCRUNCH_NAME = "TechCrunch";
	public static final String TECHCRUNCH_FEED = "http://feeds.feedburner.com/TechCrunch";
	public static final int TECHCRUNCH_LOGO = R.drawable.techcrunch;
	
	public static final String TECHMEME_NAME = "Techmeme";
	public static final String TECHMEME_FEED = "http://www.techmeme.com/feed.xml";
	public static final int TECHMEME_LOGO = R.drawable.techmeme;
	
	public static final String ENGADGET_NAME = "Engadget";
	public static final String ENGADGET_FEED = "http://www.engadget.com/rss.xml";
	public static final int ENGADGET_LOGO = R.drawable.engadget;
	
	public static final String TMN_NAME = "This Is My Next";
	public static final String TMN_FEED = "http://thisismynext.com/feed/";
	public static final int TMN_LOGO = R.drawable.thisismynext;
	
	public static final String CNET_NAME = "CNET";
	public static final String CNET_FEED = "http://feeds.feedburner.com/cnet/NnTv";
	public static final int CNET_LOGO = R.drawable.cnet;
	
	public static final String SCOBLE_NAME = "Scobleizer";
	public static final String SCOBLE_FEED = "http://scobleizer.com/feed/";
	public static final int SCOBLE_LOGO = R.drawable.scobleizer;
	
	@Override
	public List<NewsFeedItem> getFeedItems(){		
		ArrayList<NewsFeedItem> result = new ArrayList<NewsFeedItem>();
		result.add(getTechCrunchItem());
		result.add(getTechmemeItem());
		result.add(getEngadgetItem());
		result.add(getTMNItem());
		result.add(getCNETItem());
		result.add(getScobleItem());
		return result;
	}
	
	private NewsFeedItem getTechCrunchItem(){
		NewsFeedItem item = new NewsFeedItem();
		item.setName(TECHCRUNCH_NAME);
		item.setFeedUrl(TECHCRUNCH_FEED);
		item.setLogoId(TECHCRUNCH_LOGO);
		return item;
	}
	
	private NewsFeedItem getTechmemeItem(){
		NewsFeedItem item = new NewsFeedItem();
		item.setName(TECHMEME_NAME);
		item.setFeedUrl(TECHMEME_FEED);
		item.setLogoId(TECHMEME_LOGO);
		return item;
	}
	
	private NewsFeedItem getEngadgetItem(){
		NewsFeedItem item = new NewsFeedItem();
		item.setName(ENGADGET_NAME);
		item.setFeedUrl(ENGADGET_FEED);		
		item.setLogoId(ENGADGET_LOGO);
		return item;
	}
	
	private NewsFeedItem getTMNItem(){
		NewsFeedItem item = new NewsFeedItem();
		item.setName(TMN_NAME);
		item.setFeedUrl(TMN_FEED);		
		item.setLogoId(TMN_LOGO);
		return item;
	}
	
	private NewsFeedItem getCNETItem(){
		NewsFeedItem item = new NewsFeedItem();
		item.setName(CNET_NAME);
		item.setFeedUrl(CNET_FEED);		
		item.setLogoId(CNET_LOGO);
		return item;
	}
	
	private NewsFeedItem getScobleItem(){
		NewsFeedItem item = new NewsFeedItem();
		item.setName(SCOBLE_NAME);
		item.setFeedUrl(SCOBLE_FEED);		
		item.setLogoId(SCOBLE_LOGO);
		return item;
	}
}