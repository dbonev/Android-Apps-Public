package com.bubblelake.maia;

import java.util.List;

import android.content.res.Resources;
import android.location.Location;

public class WeatherProvider extends StringProvider {
	public WeatherProvider(){
		this.setDisplayName(SettingsManager.getInstance(null).getString(R.string.weather));
	}
	public static String unit = "c";
	@Override
	public String getString(MainTTS context) {
		if (this.extra1 != null){
			unit = this.extra1.toLowerCase();
			if (!unit.equals("c") && !unit.equals("f")){
				unit = "c";
			}
		}
		Location l = context.getLocation();
		if (l == null){
			return "";
		}
		String woeid = WoeidResolver.getWoeidYahoo(l.getLatitude(), l.getLongitude());
		if (woeid == null || woeid.equals("")){
			return "";
		}
		
		String url = String.format("http://weather.yahooapis.com/forecastrss?w=%s&u=%s", woeid, unit);
		List<Message> weatherMessages = new FeedParser().parse(url);
		
		if (weatherMessages.size() > 0){
			Message first = weatherMessages.get(0);
			
			CityNameProvider cnp = new CityNameProvider();
			String cityName = cnp.getString(context);
			if (cityName != null && !cityName.trim().equals("")){
				cityName = " in " + cityName;
			}
			String celsFahr = getScaleString(first.getTemperatureUnit(), context.getResources());
			String template = context.getResources().getString(R.string.weatherTemplate);
			String result= String.format(template, cityName, first.getTemperature(), celsFahr, first.getDescription());
			return result;
		}
		
		return "";
		
//		String webPage = PageDownloader.getPage(url);
//		if (webPage == null || webPage.length() < 50){
//			return "";
//		}
//		else {
//			return webPage.substring(0, 50);
//		}
	}
	
	@Override
	public int getFootPrint(){
		return 3;
	}
	
	@Override
	public boolean canConfigureUpdate(){
		return true;
	}
	
	@Override
	public Class getConfigureActivityClass(){
		return WeatherConfigureActivity.class;
	}
	
	@Override
	public boolean canDuplicate(){
		return false;
	}
	
	private String getScaleString(String temperatureUnit, Resources r) {
		if (temperatureUnit.equals("c")){
			return  r.getString(R.string.celsius);
		} else {
			return r.getString(R.string.fahrenheit);
			
		}
	}

}
