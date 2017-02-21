
package com.zeitraub.calendar;

import java.util.GregorianCalendar;

import com.zeitraub.calendar.control.CalendarObserver;
import com.zeitraub.calendar.control.EventsHandler;
import com.zeitraub.calendar.control.NotificationReceiver;
import com.zeitraub.calendar.view.CalendarView;
import com.zeitraub.calendar.view.elements.BackgroundElement;
import com.zeitraub.calendar.view.elements.EventElement;
import com.zeitraub.calendar.view.elements.MenuElement;
import com.zeitraub.calendar.view.elements.TimeElement;
import com.zeitraub.calendar.view.preferences.PreferencesFragment;
import com.zeitraub.calendar.view.preferences.TemporaryPreferences;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

/**
 * Main app class
 * 
 * @author Torsten
 *
 */
public class MainActivity extends Activity {
 
    public static Context context;
    
    private FrameLayout mFrameLayout;
    private CalendarView mCalendarView;
    private PreferencesFragment mSettingFragment;
    private FragmentTransaction mFragmentTransaction;
    
    private static final String PERMISSION_WRITE_CALENDAR = Manifest.permission.READ_CALENDAR;
    private static final int PERMISSION_REQUEST_CODE = 1;
    
    private Uri mEventUri = CalendarContract.Events.CONTENT_URI;
    private CalendarObserver mCalendarObserver;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        Intent intent = this.getIntent();
        int selectedEventId = intent.getIntExtra(NotificationReceiver.ALERT_EVENT_ID,-1);
        long selectedEventBegin = intent.getLongExtra(NotificationReceiver.ALERT_EVENT_BEGIN,-1l);
                
        context = this;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTheme(android.R.style.Theme_DeviceDefault_Light_DarkActionBar);

        mCalendarView = new CalendarView(this, new BackgroundElement(), new TimeElement(), new EventElement(), new MenuElement(), selectedEventId, selectedEventBegin);
        
        mFrameLayout = new FrameLayout(this); 
        LayoutParams frameLayoutParams = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mFrameLayout.setLayoutParams(frameLayoutParams);
        mFrameLayout.setId(0x1234);
        mFrameLayout.addView(mCalendarView);
        
        setContentView(mFrameLayout);
        
        mCalendarObserver = new CalendarObserver(null,this);
        
        getContentResolver().registerContentObserver(mEventUri, true, mCalendarObserver);
        
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission(PERMISSION_WRITE_CALENDAR);
        } else {
            addSettingsFragment(true);
        }
        
    }
    
    @Override
    protected void onDestroy() {
        getContentResolver().unregisterContentObserver(mCalendarObserver);
        super.onDestroy();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        mCalendarView.pause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mCalendarView.resume();
    }
    
    @SuppressLint("NewApi")
    public void checkPermission(String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            addSettingsFragment(true);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addSettingsFragment(true);
                } else {
                    addSettingsFragment(false);
                }
                break;
            default: 
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    
    private void addSettingsFragment(boolean permission) {
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mSettingFragment = new PreferencesFragment(permission, this);
        mFragmentTransaction.add(mFrameLayout.getId(), mSettingFragment, "settings");
        mFragmentTransaction.hide(mSettingFragment);
        mFragmentTransaction.commit();
    }
    
    public void setSettingsFragmentVisible(boolean visible){
        mFragmentTransaction = getFragmentManager().beginTransaction();
        if (visible) {
            mFragmentTransaction.show(mSettingFragment);
            mCalendarView.setBackgroundColor(Color.WHITE);
        } else {
            mFragmentTransaction.hide(mSettingFragment);
            mCalendarView.setBackgroundColor(Color.TRANSPARENT);
        }
        mFragmentTransaction.commit();
    }
 
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && !mSettingFragment.isHidden()) {
            setSettingsFragmentVisible(false);
            reloadEvents();
            return true;
        }
        super.dispatchKeyEvent(event);
        return false;
    }
    
    public void refreshCalendarView() {
        mCalendarView.resume();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999) {
            // nothing todo
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    public void reloadEvents() {
        new AsyncEventLoader().execute("");
    }
    
    private class AsyncEventLoader extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            EventsHandler.getInstance().clearEventList();
            if (TemporaryPreferences.calendarIds.size() > 0) {
                EventsHandler.getInstance().loadCalendarEvents(MainActivity.context, TemporaryPreferences.calendarIds, new GregorianCalendar(2015, 1, 1).getTimeInMillis()+"", new GregorianCalendar(2018, 1, 1).getTimeInMillis()+"");    
            } else {
                EventsHandler.getInstance().clearEventList();
            }
            refreshCalendarView();
            return null;
        }
        
    }
    
    
}