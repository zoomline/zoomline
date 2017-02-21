package com.zeitraub.calendar.data.calendar;

import java.util.ArrayList;

import com.zeitraub.calendar.data.event.Event;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;

/**
 * Access to local calendar database
 * 
 * @author Torsten
 *
 */
public class LocalCalendar {
    
    private ArrayList<Event> mResult = new ArrayList<Event>();
    private Cursor mCursor = null;
	private StringBuilder mSelectionBuilder = new StringBuilder();
	private Uri.Builder mUriBuilder;
    
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_TITLE_INDEX = 1;
	private static final int PROJECTION_CALENDARID_INDEX = 2;
	private static final int PROJECTION_DESCRIPTION_INDEX = 3;
	private static final int PROJECTION_DTSTART_INDEX = 4;
	private static final int PROJECTION_DTEND_INDEX = 5;
	private static final int PROJECTION_TIMEZONE_INDEX = 6;
	private static final int PROJECTION_ENDTIMEZONE_INDEX = 7;	
	private static final int PROJECTION_LOCATION_INDEX = 8;
	private static final int PROJECTION_EVENTCOLOR_INDEX = 9;
	private static final int PROJECTION_DURATION_INDEX = 10;
	private static final int PROJECTION_ALLDAY_INDEX = 11;
	private static final int PROJECTION_CALENDAR_DISPLAY_NAME_INDEX = 12;	
	private static final int PROJECTION_EVENTID_INDEX = 13;
	private static final int PROJECTION_RRULE_INDEX = 14;
	private static final int PROJECTION_RDATE_INDEX = 15;
	private static final int PROJECTION_EXRUE_INDEX = 16;	
	private static final int PROJECTION_EXDATE_INDEX = 17;	
	private static final int PROJECTION_STARTDAY_INDEX = 18;	
	private static final int PROJECTION_STARTMINUTE_INDEX = 19;	
	private static final int PROJECTION_BEGIN_INDEX = 20;	
	private static final int PROJECTION_END_INDEX = 21;	
	private static final int PROJECTION_ENDDAY_INDEX = 22;	
	private static final int PROJECTION_ENDMINUTE_INDEX = 23;
	private static final int PROJECTION_CALENDARCOLOR_INDEX = 24;
	
	public static final String[] PROJECTION_EVENTS = new String[] {
		Events._ID,
		Events.TITLE,
		Events.CALENDAR_ID,
		Events.DESCRIPTION,
		Events.DTSTART,
		Events.DTEND,
		Events.EVENT_TIMEZONE,
		Events.EVENT_END_TIMEZONE,	
		Events.EVENT_LOCATION,
		Events.EVENT_COLOR,
		Events.DURATION,
		Events.ALL_DAY,
		Events.CALENDAR_DISPLAY_NAME               
	};
	
	public static final String[] PROJECTION_INSTANCES = new String[] {
		Events._ID,
		Events.TITLE,
		Events.CALENDAR_ID,
		Events.DESCRIPTION,
		Events.DTSTART,
		Events.DTEND,
		Events.EVENT_TIMEZONE,
		Events.EVENT_END_TIMEZONE,	
		Events.EVENT_LOCATION,
		Events.EVENT_COLOR,
		Events.DURATION,
		Events.ALL_DAY,
		Events.CALENDAR_DISPLAY_NAME,
		Instances.EVENT_ID,
		Instances.RRULE,
		Instances.RDATE,
		Instances.EXRULE,	
		Instances.EXDATE,	
		Instances.START_DAY,	
		Instances.START_MINUTE,	
		Instances.BEGIN,
		Instances.END,	
		Instances.END_DAY,	
		Instances.END_MINUTE,
		Events.CALENDAR_COLOR
	};

	public LocalCalendar() {}

	/**
	 * Loads all instances from calendars within an time interval
	 */
	public ArrayList<Event> getAllInstancesByCalendarId(Context context, ArrayList<String> calendarIds, String startTime, String endTime) {

	    mResult.clear();
	    mCursor = null;
	    mSelectionBuilder.setLength(0);

	    for (int i = 0; i < calendarIds.size(); i++) {
			if (i == calendarIds.size()-1) {
			    mSelectionBuilder.append(CalendarContract.Events.CALENDAR_ID + " = ?");
			} else {
			    mSelectionBuilder.append(CalendarContract.Events.CALENDAR_ID + " = ? OR ");
			}
		}
		
		mUriBuilder = Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(mUriBuilder, Long.valueOf(startTime));
		ContentUris.appendId(mUriBuilder, Long.valueOf(endTime));

		mCursor = context.getContentResolver().query(mUriBuilder.build(), PROJECTION_INSTANCES, mSelectionBuilder.toString(), calendarIds.toArray(new String[]{}), null);
		
		try {
            if (mCursor!=null && mCursor.moveToFirst()) {
                   while (mCursor.moveToNext()) {
                        mResult.add(new Event(  mCursor.getLong(PROJECTION_ID_INDEX),
                                                mCursor.getString(PROJECTION_TITLE_INDEX),
                                                mCursor.getString(PROJECTION_CALENDARID_INDEX),
                                                mCursor.getString(PROJECTION_DESCRIPTION_INDEX),
                                                mCursor.getLong(PROJECTION_DTSTART_INDEX),
                                                mCursor.getLong(PROJECTION_DTEND_INDEX),
                                                mCursor.getString(PROJECTION_TIMEZONE_INDEX),
                                                mCursor.getString(PROJECTION_ENDTIMEZONE_INDEX),
                                                mCursor.getString(PROJECTION_LOCATION_INDEX),
                                                mCursor.getInt(PROJECTION_EVENTCOLOR_INDEX),
                                                mCursor.getLong(PROJECTION_DURATION_INDEX),
                                                mCursor.getString(PROJECTION_ALLDAY_INDEX),
                                                mCursor.getString(PROJECTION_CALENDAR_DISPLAY_NAME_INDEX),
                                                mCursor.getLong(PROJECTION_EVENTID_INDEX),
                                                mCursor.getString(PROJECTION_RRULE_INDEX),
                                                mCursor.getLong(PROJECTION_RDATE_INDEX),
                                                mCursor.getString(PROJECTION_EXRUE_INDEX),  
                                                mCursor.getLong(PROJECTION_EXDATE_INDEX),   
                                                mCursor.getLong(PROJECTION_STARTDAY_INDEX), 
                                                mCursor.getLong(PROJECTION_STARTMINUTE_INDEX),  
                                                mCursor.getLong(PROJECTION_BEGIN_INDEX),
                                                mCursor.getLong(PROJECTION_END_INDEX),  
                                                mCursor.getLong(PROJECTION_ENDDAY_INDEX),   
                                                mCursor.getLong(PROJECTION_ENDMINUTE_INDEX),
                                                mCursor.getInt(PROJECTION_CALENDARCOLOR_INDEX)      
                                            ));
                    } 
            }
        } catch (Exception e) {
            mCursor.close();
        }
		mCursor.close();
		return mResult;
	}
	
	/**
	 * Returns all calendars
	 */
	public ArrayList<String> getCalendars(Context context) {
        ArrayList<String> result = new ArrayList<String>();
	    ContentResolver cr;
        Cursor cursor;
        String[] projection = new String[] { CalendarContract.Calendars._ID };
        String selection = "(" + CalendarContract.Calendars.VISIBLE + " = ?)";
        String[] selectionArgs = new String[] { "1" };
        cr = context.getContentResolver();
        cursor = cr.query(CalendarContract.Calendars.CONTENT_URI, projection, selection, selectionArgs, null);
        while (cursor.moveToNext()) {
            result.add(cursor.getString(0));            
        }
        return result;
	}
	
}