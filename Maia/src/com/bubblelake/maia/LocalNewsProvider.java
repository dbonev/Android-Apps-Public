package com.bubblelake.maia;

import java.util.List;

public class LocalNewsProvider extends StringProvider{
	public LocalNewsProvider(){
		this.setDisplayName(SettingsManager.getInstance(null).getString(R.string.localNews));
	}
	@Override
	public String getString(MainTTS context) {
		CityNameProvider cnp = new CityNameProvider();
		String city = cnp.getString(context);
		String country = cnp.getCountryName(context);
		String url = String.format("http://news.google.com/news?pz=1&cf=all&ned=us&hl=en&q=local+news+%s,+%s&cf=all&output=rss&num=5", city, country);
		List<Message> newsItems = new FeedParser().parse(url);
		
		StringBuilder sb = new StringBuilder();
		sb.append(context.getResources().getString(R.string.headlines));
		sb.append(" ");
		int storiesMAx = getStoriesMax();
		int counter = 1;
		for (Message m : newsItems){
			sb.append(m.getStrippedTitle());
			sb.append(". ");
			
			if (storiesMAx >= 0 && counter++ >= storiesMAx){
				break;
			}
		}
		
		return sb.toString();
	}
	
	private int getStoriesMax(){
		try {
			int res = Integer.parseInt(this.extra3);
			return res;
		} catch (Exception ex){
			return -1;
		}
	}
	
	@Override
	public Class getConfigureUpdateActivityClass(){
		return NewsConfigureActivity.class;
	}
	
	@Override
	public boolean canConfigureUpdate(){
		return true;
	}

}
