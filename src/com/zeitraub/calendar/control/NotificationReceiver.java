package com.zeitraub.calendar.control;

import com.zeitraub.calendar.MainActivity;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.provider.CalendarContract.CalendarAlerts;

/**
 * Receives notifications and opens event
 * 
 * @author Torsten
 *
 */
public class NotificationReceiver extends BroadcastReceiver {
 
    public static final String ALERT_EVENT_ID = "ALERT_EVENT_ID";
    public static final String ALERT_EVENT_BEGIN = "ALERT_EVENT_BEGIN";
    
    private long mAlarmTime;
    private int mAlarmEventId;
    private long mAlarmEventBegin;
    
    private Cursor mCursor;
    private ContentResolver mContentResolver;
    private String[] mProjection = new String[] { CalendarAlerts.EVENT_ID, CalendarAlerts.BEGIN };
    private String mSelection;
    private String[] mSelectionArgs;
    private Intent mMainActivityIntent;
        
    @Override
    public void onReceive(Context context, Intent intent) {
        mAlarmTime = intent.getExtras().getLong("alarmTime");
        mContentResolver = context.getContentResolver();
        mSelection = CalendarContract.CalendarAlerts.ALARM_TIME + " = ?";
        mSelectionArgs = new String[] { mAlarmTime+"" };
        mCursor = mContentResolver.query(CalendarContract.CalendarAlerts.CONTENT_URI, mProjection, mSelection, mSelectionArgs, null);
        mAlarmEventId = -1;
        while (mCursor.moveToNext()) {
            mAlarmEventId = mCursor.getInt(0);
            mAlarmEventBegin = mCursor.getLong(1);
        }
        mCursor.close();
        if (mAlarmEventId != -1) {
            mMainActivityIntent = new Intent(context, MainActivity.class);
            mMainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mMainActivityIntent.putExtra(ALERT_EVENT_ID, mAlarmEventId);
            mMainActivityIntent.putExtra(ALERT_EVENT_BEGIN, mAlarmEventBegin);
            context.startActivity(mMainActivityIntent);    
        }
    }
}
       
        
        
        