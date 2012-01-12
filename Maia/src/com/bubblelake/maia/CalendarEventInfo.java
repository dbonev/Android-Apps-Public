package com.bubblelake.maia;

import java.util.Date;

public class CalendarEventInfo {
	private String title;
	private Date begin;
	private Date end;
	private boolean allDay;
	private String calendarName;
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setBegin(Date begin) {
		this.begin = begin;
	}
	public Date getBegin() {
		return begin;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public Date getEnd() {
		return end;
	}
	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}
	public boolean isAllDay() {
		return allDay;
	}
	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}
	public String getCalendarName() {
		return calendarName;
	}
}
