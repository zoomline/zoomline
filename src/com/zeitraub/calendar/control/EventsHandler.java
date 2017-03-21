package com.zeitraub.calendar.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.zeitraub.calendar.data.calendar.LocalCalendar;
import com.zeitraub.calendar.data.event.Event;
import com.zeitraub.calendar.view.preferences.TimePreferences;

import android.content.Context;

/**
 * Handles event layout data
 * 
 * @author Torsten
 *
 */
public class EventsHandler {

    private static EventsHandler sInstance;
    
    private LocalCalendar mLocalCalendar = new LocalCalendar();
    
    private ArrayList<Event> mEvents = new ArrayList<Event>();
    private ArrayList<Event> mEventsOrigin = new ArrayList<Event>();
    private ArrayList<Event> mEventsAllDay = new ArrayList<Event>();
    private ArrayList<Event> mEventsLongerDay = new ArrayList<Event>();
    private ArrayList<Event> mEventsDayGraph = new ArrayList<Event>();
    private ArrayList<Event> mEventsRow = new ArrayList<Event>();
    private static ArrayList<Event> mResultEventsRow = new ArrayList<Event>();
    private static ArrayList<Event> mResultEventsDayGraph = new ArrayList<Event>();
    private static ArrayList<Event> mResultEventsLongerDay = new ArrayList<Event>();
    private static ArrayList<Event> mResultEventsAllDay = new ArrayList<Event>();
    
    private int mLoopIndex = 0;
    private int mLoopIndex2 = 0;
    private int mLoopPosition = 0;
    private int mLoopCount = 0;
	
	private int mIndex = 0;

	
	public EventsHandler() {}

	public static EventsHandler getInstance() {
		if (EventsHandler.sInstance == null) {
			EventsHandler.sInstance = new EventsHandler();
		}
		return EventsHandler.sInstance;
	}

	/**
	 * Loads all instances from: calendars,  within an time interval, sorted by starting time
	 */
	public void loadCalendarEvents(Context pContext, ArrayList<String> calendarIds, String pStartTime, String pEndTime) {
	    mEventsOrigin = mLocalCalendar.getAllInstancesByCalendarId(pContext, calendarIds, pStartTime, pEndTime);
		Collections.sort(mEventsOrigin, new Comparator<Event>() {
			public int compare(Event e1, Event e2) {
				return e1.unionBegin.compareTo(e2.unionBegin);
			}
		});
	}

	/**
	 * Calculate layout data within an time interval
	 */
    public void getEventsSpan(long startTime, long endTime) {
        mEvents.clear();
        if (mEventsOrigin != null) {
            for (mLoopIndex = 0 ; mLoopIndex < mEventsOrigin.size(); mLoopIndex++) {
                if (mEventsOrigin.get(mLoopIndex).unionEnd > startTime && mEventsOrigin.get(mLoopIndex).unionBegin < endTime) {
                    mEvents.add(mEventsOrigin.get(mLoopIndex));
                } else if (mEventsOrigin.get(mLoopIndex).unionBegin > endTime) {
                    break;
                }
            }
        }
        setArrangableAllDay();
        setArrangableLongerDay();
        setArrangableDayGraph();
        setArrangableRow();
    }
	
    /**
     * Calculate layout data for allday events
     */
    private void setArrangableAllDay() {
        mEventsAllDay.clear();
        if (mEvents != null && !mEvents.isEmpty()) {
            for (Event event : mEvents) {
                if (event.allDay.equals("1")) {
                    mEventsAllDay.add(event);
                }
            }
            if (mEventsAllDay != null && !mEventsAllDay.isEmpty()) {
                mLoopPosition = 0;
                mLoopCount = 0;
                for (mLoopIndex = 0; mLoopIndex < mEventsAllDay.size(); mLoopIndex++, mLoopPosition++, mLoopCount++) {
                    for (mLoopIndex2 = 0; mLoopIndex2 < mLoopCount; mLoopIndex2++) {
                        if (mEventsAllDay.get(mLoopIndex - mLoopIndex2 - 1).unionEnd <= mEventsAllDay.get(mLoopIndex).unionBegin) {
                            mLoopPosition--;
                        } else {
                            break;
                        }
                    }
                    mEventsAllDay.get(mLoopIndex).arrangeableEventPositionAllDay = mLoopPosition;
                    mLoopCount = mLoopPosition;
                }
            }
        }
    }
	
    /**
     * Calculate layout data for events which are longer then one day
     */
    private void setArrangableLongerDay() {
        mEventsLongerDay.clear();
        if (mEvents != null && !mEvents.isEmpty()) {
            for (Event event : mEvents) {
                if (event.duration >= TimePreferences.DURATION_MILLIS_DAY_01) {
                    mEventsLongerDay.add(event);
                }
            }
            if (mEventsLongerDay != null && !mEventsLongerDay.isEmpty()) {
                mLoopPosition = 0;
                mLoopCount = 0;
                for (mLoopIndex = 0; mLoopIndex < mEventsLongerDay.size(); mLoopIndex++, mLoopPosition++, mLoopCount++) {
                    for (mLoopIndex2 = 0; mLoopIndex2 < mLoopCount; mLoopIndex2++) {
                        if (mEventsLongerDay.get(mLoopIndex - mLoopIndex2 - 1).unionEnd <= mEventsLongerDay.get(mLoopIndex).unionBegin) {
                            mLoopPosition--;
                        } else {
                            break;
                        }
                    }
                    mEventsLongerDay.get(mLoopIndex).arrangeableEventPositionAllDay = mLoopPosition;
                    mLoopCount = mLoopPosition;
                }
            }
        }
    }
	
    /**
     * Calculate layout data for graph view
     */
    private void setArrangableDayGraph() {
        mEventsDayGraph.clear();
        if (mEvents != null && !mEvents.isEmpty()) {
            for (Event event : mEvents) {
                if (event.duration < TimePreferences.DURATION_MILLIS_DAY_01) {
                    mEventsDayGraph.add(event);
                }
            }
            Collections.sort(mEventsDayGraph, new Comparator<Event>() {
                public int compare(Event e1, Event e2) {
                    return e1.unionBegin.compareTo(e2.unionBegin);
                }
            });
        }
    }
	
    /**
     * Calculate layout data for row view
     */
    private void setArrangableRow() {
        mEventsRow.clear();
        if (mEvents != null && !mEvents.isEmpty()) {
            for (Event event : mEvents) {
                if (event.allDay.equals("0")) {
                    mEventsRow.add(event);
                }
            }
            Collections.sort(mEventsRow, new Comparator<Event>() {
                public int compare(Event e1, Event e2) {
                    return e1.unionBegin.compareTo(e2.unionBegin);
                }
            });
        }
    }
	
    /**
     * Select all visable all day events within an time interval
     */
    public ArrayList<Event> getEventsAllDay(long startTime, long endTime) {
        mResultEventsAllDay.clear();
        if (mEventsAllDay != null) {
            for (mLoopIndex = 0; mLoopIndex < mEventsAllDay.size(); mLoopIndex++) {
                if (startTime <= mEventsAllDay.get(mLoopIndex).unionBegin && startTime + TimePreferences.DURATION_MILLIS_DAY_01 >= mEventsAllDay.get(mLoopIndex).unionEnd && mEventsAllDay.get(mLoopIndex).allDay.equals("1")) {
                    mResultEventsAllDay.add(mEventsAllDay.get(mLoopIndex));
                } else if (startTime >= mEventsAllDay.get(mLoopIndex).unionBegin && startTime + TimePreferences.DURATION_MILLIS_DAY_01 <= mEventsAllDay.get(mLoopIndex).unionEnd) {
                    if (TimePreferences.DURATION_MILLIS_DAY_01 < (mEventsAllDay.get(mLoopIndex).unionEnd - mEventsAllDay.get(mLoopIndex).unionBegin)) {
                        mResultEventsAllDay.add(mEventsAllDay.get(mLoopIndex));
                    }
                } else if (endTime < mEventsAllDay.get(mLoopIndex).unionBegin) {
                    break;
                }
            }
        }
        return mResultEventsAllDay;
    }
	
    /**
     * Select all visable all events which are longer then one day within an time interval
     */
    public ArrayList<Event> getEventsLongerDay(long startTime, long endTime) {
        mResultEventsLongerDay.clear();
        if (mEventsLongerDay != null) {
            for (mLoopIndex = 0; mLoopIndex < mEventsLongerDay.size(); mLoopIndex++) {
                if (startTime <= mEventsLongerDay.get(mLoopIndex).unionEnd && endTime >= mEventsLongerDay.get(mLoopIndex).unionBegin) {
                    mResultEventsLongerDay.add(mEventsLongerDay.get(mLoopIndex));
                } else if (endTime < mEventsLongerDay.get(mLoopIndex).unionBegin) {
                    break;
                }
            }
        }
        return mResultEventsLongerDay;
    }
    
    /**
     * Select all visable events for graph view within an time interval
     */
    public ArrayList<Event> getEventsDayGraph(long startTime, long endTime) {
        mResultEventsDayGraph.clear();
        if (mEventsDayGraph != null) {
            for (mLoopIndex = 0; mLoopIndex < mEventsDayGraph.size(); mLoopIndex++) {
                if (startTime <= mEventsDayGraph.get(mLoopIndex).unionEnd && endTime >= mEventsDayGraph.get(mLoopIndex).unionBegin) {
                    mResultEventsDayGraph.add(mEventsDayGraph.get(mLoopIndex));
                } else if (endTime < mEventsDayGraph.get(mLoopIndex).unionBegin) {
                    break;
                }
            }
        }        
        return mResultEventsDayGraph;
    }
    
    /**
     * Select all visable events for row view within an time interval
     */
    public ArrayList<Event> getEventsRow(long startTime, long endTime) {
        mResultEventsRow.clear();
        if (mEventsRow != null) {
            for (mIndex = 0; mIndex < mEventsRow.size(); mIndex++) {
                if (mEventsRow.get(mIndex).unionBegin >= startTime && mEventsRow.get(mIndex).unionBegin < endTime) {
                    mResultEventsRow.add(mEventsRow.get(mIndex));
                } else if (endTime <= mEventsRow.get(mIndex).unionBegin) {
                    break;
                }
            }
        }        
        return mResultEventsRow;
    }
    
    /**
     * Clears current layout data 
     */
    public void clearEventList() {
        mEvents.clear();
    }
    
    /**
     * Returns an event by alarm time
     */
    public Event getEventByAlarmTime(long alarmTime) {
        if (mEvents != null) {
            for (Event event : mEvents) {
                if (event.begin == alarmTime) {
                    return event;
                }
            }
        }
        return null;
    }
}