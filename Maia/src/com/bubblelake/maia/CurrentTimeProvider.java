package com.bubblelake.maia;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.res.Resources;

public class CurrentTimeProvider extends StringProvider {
	public CurrentTimeProvider(){
		this.setDisplayName(SettingsManager.getInstance(null).getString(R.string.currentTime));
	}
	
	@Override
	public String getString(MainTTS context) {
		Calendar calendar = new GregorianCalendar();
		String template = context.getResources().getString(R.string.currentTimeTemplate);
		
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		
		int amPm = calendar.get(Calendar.AM_PM);
		
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		
		String amPmString = amPm == Calendar.AM ? "AM" : "PM";
		String monthString = getMonthString(month, context.getResources());
		String dayOfWeekString = getDayString(dayOfWeek, context.getResources());
		String dayOfMonthString = DateStringConverter.getDateString(day, context.getResources());
		if (dayOfMonthString.equals("")){
			dayOfMonthString = new Integer(day).toString();
		}
		
		if (hour == 0){
			hour = 12;
		}
		String result = String.format(template, hour, minute, amPmString, dayOfWeekString, dayOfMonthString, monthString);
		return result;
	}
	
	@Override
	public boolean skipPleaseWait(){
		return true;
	}
	
	public static String getTimeString(Date date){
		Calendar calendar = new GregorianCalendar(date.getYear(), date.getMonth(),  date.getDay(), date.getHours(), date.getMinutes(), 0);
		String amPmString = calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
		
		String result = String.format("%s %s %s", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), amPmString);
		return result;
	}
	
	@Override
	public int getFootPrint(){
		return 3;
	}
	
	private String getDayString(int dayOfWeek, Resources r){
		switch(dayOfWeek){
		case Calendar.MONDAY:
			return r.getString(R.string.monday);
		case Calendar.TUESDAY:
			return r.getString(R.string.tuesday);
		case Calendar.WEDNESDAY:
			return r.getString(R.string.wednesday);
		case Calendar.THURSDAY:
			return r.getString(R.string.thursday);
		case Calendar.FRIDAY:
			return r.getString(R.string.friday);
		case Calendar.SATURDAY:
			return r.getString(R.string.saturday);
		case Calendar.SUNDAY:
			return r.getString(R.string.sunday);
		default:
			return "";
		}
	}

	private String getMonthString(int month, Resources r) {
		switch(month){
		case Calendar.JANUARY:
			return r.getString(R.string.january);
		case Calendar.FEBRUARY:
			return r.getString(R.string.february);
		case Calendar.MARCH:
			return r.getString(R.string.march);
		case Calendar.APRIL:
			return r.getString(R.string.april);
		case Calendar.MAY:
			return r.getString(R.string.may);
		case Calendar.JUNE:
			return r.getString(R.string.june);
		case Calendar.JULY:
			return r.getString(R.string.july);
		case Calendar.AUGUST:
			return r.getString(R.string.august);
		case Calendar.SEPTEMBER:
			return r.getString(R.string.september);
		case Calendar.OCTOBER:
			return r.getString(R.string.october);
		case Calendar.NOVEMBER:
			return r.getString(R.string.november);
		case Calendar.DECEMBER:
			return r.getString(R.string.december);
		default:
			return "";
		}
	}

}
