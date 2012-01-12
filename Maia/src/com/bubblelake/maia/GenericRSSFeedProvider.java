package com.bubblelake.maia;

import java.util.ArrayList;
import java.util.List;
import java.lang.Class;
import android.content.Context;
import android.content.Intent;

public class GenericRSSFeedProvider extends StringProvider {
	String type;
	
	public GenericRSSFeedProvider(){
		
	}
	
	public GenericRSSFeedProvider(String type){	
		this();
		this.type = type;
		
		String concrete = "";
		if (this.extra1 != null){
			this.setDisplayName(this.extra1);
		} else {
			if (type.equals(PredefinedNewsConfigureActivity.NEWS_TYPE_TECH)){			
				this.setDisplayName(SettingsManager.getInstance(null).getString(R.string.techNews));
			} else if (type.equals(PredefinedNewsConfigureActivity.NEWS_TYPE_TOP)){
				this.setDisplayName(SettingsManager.getInstance(null).getString(R.string.currentTopNews));
			} else if (type.equals(PredefinedNewsConfigureActivity.NEWS_TYPE_SPORT)){
				this.setDisplayName(SettingsManager.getInstance(null).getString(R.string.topSportNews));
			}
			else {
				this.setDisplayName(SettingsManager.getInstance(null).getString(R.string.newsProvider));
			}
		}
		
	}
	
	private void refresh_display_name(){
		if (this.extra1 != null){
			this.setDisplayName(this.extra1);
		}
	}
	
	@Override
	public void setExtra2(String value){
		super.setExtra2(value);
		refresh_display_name();
	}
	
	@Override
	public boolean hasExtra1(){
		return true;	
	}
	
	@Override
	public String getExtra1Label(Context context){
		return context.getResources().getString(R.string.newsSourceName);
	}
	
	@Override
	public boolean hasExtra2(){
		return true;	
	}
	
	@Override
	public String getExtra2Label(Context context){
		return context.getResources().getString(R.string.newsSource);
	}
	
	@Override
	public boolean canConfigureUpdate(){
		return true;
	}
	
	@Override
	public String[] getExtendedString(MainTTS context){
		String feed = this.extra2;
		if (feed == null || feed.trim().equals("")){
			return new String[0];
		}
		
		ArrayList<String> result = new ArrayList<String>();
		
		boolean mustParseDescriptions = mustParseDescriptions();
		List<Message> news = new FeedParser(mustParseDescriptions).parse(feed);
		StringBuilder sb = new StringBuilder();
		int storiesMAx = getStoriesMax();
		String top = storiesMAx >= 0 ? " top " + storiesMAx + " " : "";
		sb.append(String.format(context.getResources().getString(R.string.currentNewsFeed), top, this.extra1));
		sb.append(". ");
		int counter = 1;
		result.add(sb.toString());
		
		for (Message m : news){	
			sb = new StringBuilder();
			
			String heading = m.getStrippedTitle();
			heading = heading.trim().replace("--", "-");
			if (NewsFeedResolverTech.TMN_NAME.equals(this.extra1)){
				heading = strip_tmn_artifacts(heading);
			}
			sb.append(heading);
			if (!heading.trim().endsWith(".")){
				sb.append(". ");
			}
			
			if (mustParseDescriptions){
				String description = m.getDescription();
				if (description != null){
					description = processDescription(description);
					sb.append(description);
					if (!description.endsWith(".")){
						sb.append(". ");
					}
				}
			}
			
			result.add(sb.toString());
			
			if (storiesMAx >= 0 && counter++ >= storiesMAx){
				break;
			}
		}		
		return toStringArray(result);
	}
	
	private String processDescription(String rawDescription){
		String description = rawDescription.trim().replace("--", "-");
		int ltIndex = description.indexOf("<");
		if (ltIndex >= 0){
			description = description.substring(0, ltIndex);
		}
		
		if (NewsFeedResolverTech.TMN_NAME.equals(this.extra1) 
				|| NewsFeedResolverTech.SCOBLE_NAME.equals(this.extra1)){
			int lsb = description.indexOf("[");
			if (lsb >= 0){
				description = description.substring(0, lsb);
			}
			
			description = strip_tmn_artifacts(description);
		}
		
		return description;
	}

	private String strip_tmn_artifacts(String description) {
		description = description.replace("&#8217;", "'").replace("&#038;", "&").replace("&#8230;", "...").replace("&#8220;", "'").replace("&#8221;", "'");
		return description;
	}
	
	@Override
	public String getString(MainTTS context) {
		return makeString(getExtendedString(context));
	}
	
	public static String makeString(String[] array){
		String[] temp = array;
		StringBuilder sb = new StringBuilder();
		for (String s:temp){
			sb.append(s);
		}
		return sb.toString();
	}
	
	private boolean mustParseDescriptions(){
		if ("CNN".equals(this.extra1) 
				|| "BBC".equals(this.extra1) 
				|| SportNewsFeedResolver.CNN_NAME.equals(this.extra1)
				|| SportNewsFeedResolver.BBC_NAME.equals(this.extra1)
				|| NewsFeedResolverTech.TMN_NAME.equals(this.extra1)
				|| NewsFeedResolverTech.SCOBLE_NAME.equals(this.extra1)){
			return true;
		} else {
			return false;
		}
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
	public Class getConfigureActivityClass(){
		return PredefinedNewsConfigureActivity.class;
	}
	
	@Override
	public Class getConfigureUpdateActivityClass(){
		return NewsConfigureActivity.class;
	}
	
	@Override
	public boolean canConfigureAdd(){
		return true;
	}

	@Override
	public void addAdditionalInfo(Intent intent){
		intent.putExtra("type", this.type);
	}
}
