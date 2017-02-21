package com.zeitraub.calendar.control;

import com.zeitraub.calendar.MainActivity;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

/**
 * Reloads events on calendar changes 
 * 
 * @author Torsten
 *
 */
public class CalendarObserver extends ContentObserver {

    MainActivity mMainActivity;
    
    public CalendarObserver(Handler handler, MainActivity activity) {
        super(handler);
        mMainActivity = activity;
    }

    @Override
    public void onChange(boolean changed) {
        reloadEvents();
        super.onChange(changed);
    }
    
    @Override
    public void onChange(boolean changed, Uri uri) {
        reloadEvents();
        super.onChange(changed, uri);
    }
    
    private void reloadEvents() {
        mMainActivity.reloadEvents();
    }
    
}
