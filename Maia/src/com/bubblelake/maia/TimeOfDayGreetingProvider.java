package com.bubblelake.maia;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;

public class TimeOfDayGreetingProvider extends StringProvider {
	public TimeOfDayGreetingProvider(){
		this.setDisplayName(SettingsManager.getInstance(null).getString(R.string.timeOfDay));
	}
	
	public static String getTimeOfDayGreeting(Context context){
		
		Calendar calendar = new GregorianCalendar();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour > 4 && hour <= 12){
			return context.getResources().getString(R.string.goodMorning);
		} else if (hour > 12 && hour <= 17){
			return context.getResources().getString(R.string.goodAfternoon);
		} else if (hour <= 23){
			return context.getResources().getString(R.string.goodEvening);
		} else {
			return context.getResources().getString(R.string.hi);
		}
	}

	@Override
	public String getString(MainTTS context) {
		return getTimeOfDayGreeting(context);
	}
	
	@Override
	public String finishWith(MainTTS context){
		return ", ";
	}
	
	@Override
	public boolean skipPleaseWait(){
		return true;
	}
}
