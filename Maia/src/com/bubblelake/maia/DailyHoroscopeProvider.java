package com.bubblelake.maia;

import java.util.List;

public class DailyHoroscopeProvider extends StringProvider {
	public final static String HOROSCOPE_URL = "http://www.astrology.com/horoscopes/daily-horoscope.rss";
//	public final static String HOROSCOPE_URL_DH = "http://www.dailyhoroscopes.com/index.php?option=com_eventscalrss&feed=RSS2.0&no_html=1";
	public final static String HOROSCOPE_URL_DH = "http://feeds.feedburner.com/dayhoroscope";
	public final static String ASTROLOGY_COM = "Astrology.com";
	public final static String ASTROSAGE_COM = "AstroSage.com";
	
	public DailyHoroscopeProvider(){		
		this.setDisplayName(SettingsManager.getInstance(null).getString(R.string.dailyHoroscope));
	}
	
	@Override
	public String getString(MainTTS context) {
		if (this.extra1 == null || this.extra1.trim().equals("")){
			return context.getResources().getString(R.string.dailyHoroscopeConfigure);
		} else {
			List<Message> signs = new FeedParser(true).parse(getUrl());
			StringBuilder result = new StringBuilder();
			for (Message sign : signs){
				if (sign.getTitle().toLowerCase().startsWith(this.extra1.toLowerCase())){
					result.append(process(sign.getDescription()));
				}
			}
			String temp = result.toString();
			temp = temp.replace("(", "").replace(")", "");
			return String.format(context.getString(R.string.dailyHoroscopePrelude), getExtra2()) + " " + temp;
		}
	}
	
	private String getUrl(){
		if (getExtra2().equals(ASTROLOGY_COM)){
			return HOROSCOPE_URL;
		} else {
			return HOROSCOPE_URL_DH;
		}
	}
	
	@Override
	public String getExtra2(){
		if (this.extra2 == null || this.extra2.equals("")){
			return ASTROLOGY_COM;
		}
		
		return this.extra2;
	}
	
	private String process(String description) {
		
		int eopIndex = getEndIndex(description);
		
		if (eopIndex >= 0){
			return replace(description.substring(0, eopIndex));
		} else {
			return replace(description);
		}
	}
	
	private String replace(String source){
		return source.replace("--", "-").replace("AstroSage.com,", "");
	}
	
	private int getEndIndex(String description){
		if (getExtra2().equals(ASTROLOGY_COM)){
			return description.indexOf("</p>");
		} else {
			return description.indexOf("<p>");
		}
	}

	@Override
	public boolean canConfigureAdd(){
		return true;
	}
	
	@Override
	public boolean canConfigureUpdate(){
		return true;
	}
	
	@Override
	public Class getConfigureActivityClass(){
		return DailyHoroscopeConfigureActivity.class;
	}
	
	@Override
	public String getDisplayName(){
		String suffix = "";
		if (this.extra1 != null && !this.extra1.trim().equals("")){
			suffix = " (" + this.extra1 + " / " + getExtra2() + ") ";
		} else {
			suffix = " " + SettingsManager.getInstance(null).getString(R.string.clickToSetYourSign) + " ";
		}
		return this.displayName + suffix;
	}

}
