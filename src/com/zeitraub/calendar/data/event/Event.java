package com.zeitraub.calendar.data.event;

import java.util.TimeZone;

import com.zeitraub.calendar.view.elements.DrawingHelper;

/**
 * Event object
 * 
 * @author Torsten
 *
 */
public class Event {

	public long id;
	public String title;
	public String calendarId;
	public String description;
	public Long DTStart;
	public Long DTEnd;
	public String timeZone;
	public String endTimeZone;
	public String location;
	public int color;
	public int calendarColor;
	public Long duration;
	public String allDay;
	public String organizer;
	public long eventId;
	public String RRule;
	public Long RDate;
	public String ExRule;
	public Long ExDate;
	public Long begin;
	public Long end;
	public Long unionBegin;
	public Long unionEnd;
	public int unionOffsetUTC;
	public int left;
	public int top;
	public int right;
	public int bottom;
	public String startTime;
	public int arrangeableEventPositionAllDay = 0;
	
	public Event(long pId, String pTitle, String pCalendarId,
			String pDescription, Long pDTStart, Long pDTEnd, String pTimeZone,
			String pEndTimeZone, String pLocation, int pColor,
			Long pDuration, String pAllDay, String pOrganizer, long pEventId,
			String pRRule, Long pRDate, String pExRule, Long pExDate,
			Long pStartDay, Long pStartMinute, Long pBegin, Long pEnd,
			Long pEndDay, Long pEndMinute, int pCalendarColor) {
    
		id = pId;
		title = pTitle;
		calendarId = pCalendarId;
		description = pDescription;
		DTStart = pDTStart;
		DTEnd = pDTEnd;
		timeZone = pTimeZone;
		endTimeZone = pEndTimeZone;
		location = pLocation;
		color = pColor;
		calendarColor = pCalendarColor;
		duration = pDuration;
		allDay = pAllDay;
		organizer = pOrganizer;
		eventId = pEventId;
		RRule = pRRule;
		RDate = pRDate;
		ExRule = pExRule;
		ExDate = pExDate;
		begin = pBegin;
		end = pEnd;

        startTime = DrawingHelper.getTimeHHMM(begin);
        
		unionBegin = begin+TimeZone.getTimeZone(timeZone).getOffset(begin);
		unionEnd = end+TimeZone.getTimeZone(timeZone).getOffset(begin);
		unionOffsetUTC = TimeZone.getTimeZone(timeZone).getOffset(begin);
		duration = unionEnd-unionBegin;
		
		
		left = -1;
		top = -1;
		right = -1;
		bottom = -1;

	}
	
}
