package com.bubblelake.maia;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;

public class CalendarAppointmentsProvider extends StringProvider {
	public static final int NEXT_HOURS_CAL = 24;

	public CalendarAppointmentsProvider(){
		this.setDisplayName(SettingsManager.getInstance(null).getString(R.string.calendarAppointments));
	}
	
	@Override
	public String[] getExtendedString(MainTTS context) {	
		ArrayList<String> result = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		Resources r = context.getResources();		
		List<CalendarEventInfo> events = CalendarEventsExtractor.readCalendar(context);
		
		if (events == null || events.size() == 0){
			sb.append(String.format(r.getString(R.string.noEvents), NEXT_HOURS_CAL));
			
			result.add(sb.toString());
			
		} else {
			int rawSize = events.size();
			int maxCount = rawSize > 5 ? 5 : rawSize;
			
			String prelude = "";
			
			if (rawSize == 1){
				prelude = String.format(r.getString(R.string.eventsInCalendarSingle), NEXT_HOURS_CAL);
			} else {
				if (rawSize > maxCount){
					prelude = String.format(r.getString(R.string.eventsInCalendarConstrained), NEXT_HOURS_CAL, rawSize, maxCount);
				} else {
					prelude = String.format(r.getString(R.string.eventsInCalendar), NEXT_HOURS_CAL, rawSize);
				}
			}
			
			sb.append(prelude);
			sb.append(" ");
			
			result.add(sb.toString());			
			
			int counter = 0;
			for(CalendarEventInfo info : events){
				sb = new StringBuilder();
				if (counter++ >= maxCount){
					break;
				}
				String eventTemplate = r.getString(R.string.eventTemplate);
				int hour = info.getBegin().getHours();
				int minute = info.getBegin().getMinutes();
				String timeString = CurrentTimeProvider.getTimeString(info.getBegin());
				String toAppend = String.format(eventTemplate, info.getTitle(), timeString);
				sb.append(toAppend);
				sb.append(". ");
				result.add(sb.toString());
			}
		}
				
		return toStringArray(result);
	}
	
	@Override
	public String getString(MainTTS context) {		
		return GenericRSSFeedProvider.makeString(getExtendedString(context));		
	}

}
