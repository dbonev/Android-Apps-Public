package com.bubblelake.maia;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateUtils;

public class CalendarEventsExtractor {

	public static List<CalendarEventInfo> readCalendar(Context context) {
		List<CalendarEventInfo> result = new ArrayList<CalendarEventInfo>();
		ContentResolver contentResolver = context.getContentResolver();
		HashMap<String, String> idNameMap = new HashMap<String, String>();

		// Fetch a list of all calendars synced with the device, their display names and whether the
		// user has them selected for display.
		
		final Cursor cursor = contentResolver.query(Uri.parse(get_calendar_uri()),
				(new String[] { "_id", "displayName", "selected" }), null, null, null);
		// For a full list of available columns see http://tinyurl.com/yfbg76w

		HashSet<String> calendarIds = new HashSet<String>();
		
		if (cursor == null){
			return null;
		}
		while (cursor.moveToNext()) {

			final String _id = cursor.getString(0);
			final String displayName = cursor.getString(1);
			if (displayName.toLowerCase().contains("weather")){
				continue;
			}
			final Boolean selected = !cursor.getString(2).equals("0");
						
			calendarIds.add(_id);
			idNameMap.put(_id, displayName);
		}
		
		// For each calendar, display all the events from the previous week to the end of next week.		
		for (String id : calendarIds) {
			Uri.Builder builder = Uri.parse(get_events_uri()).buildUpon();
			long now = new Date().getTime();
			ContentUris.appendId(builder, now);//- DateUtils.WEEK_IN_MILLIS);
			ContentUris.appendId(builder, now + DateUtils.DAY_IN_MILLIS );

			Cursor eventCursor = contentResolver.query(builder.build(),
					new String[] { "title", "begin", "end", "allDay"}, "Calendars._id=" + id,
					null, "startDay ASC, startMinute ASC"); 
			// For a full list of available columns see http://tinyurl.com/yfbg76w

			if (eventCursor == null){
				return null;
			}
			while (eventCursor.moveToNext()) {
				final String title = eventCursor.getString(0);
				final Date begin = new Date(eventCursor.getLong(1));
				final Date end = new Date(eventCursor.getLong(2));
				final Boolean allDay = !eventCursor.getString(3).equals("0");
				
				CalendarEventInfo info = new CalendarEventInfo();
				info.setTitle(title);
				info.setBegin(begin);
				info.setEnd(end);
				info.setAllDay(allDay);
				info.setCalendarName(idNameMap.get(id));
				
				result.add(info);
			}
		}
		
		return result;
	}
	
	private static String get_calendar_uri(){
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 8){
		    return "content://com.android.calendar/calendars";
		} else{
		    return "content://calendar/calendars";
		}
	}
	private static String get_events_uri(){
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 8){
		    return "content://com.android.calendar/instances/when";
		} else{
		    return "content://calendar/instances/when";
		}
	}
}