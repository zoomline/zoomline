package com.zeitraub.calendar.view.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Preferences for time intervals
 * 
 * @author Torsten
 *
 */
public class TimePreferences {

    public static final long DURATION_MILLIS_MINUTE_01 = 60000l;
    public static final long DURATION_MILLIS_MINUTE_15 = 900000l;
    public static final long DURATION_MILLIS_MINUTE_30 = 1800000l;
    public static final long DURATION_MILLIS_HOUR_01 = 3600000l;
    public static final long DURATION_MILLIS_HOUR_03 = 10800000l;
    public static final long DURATION_MILLIS_HOUR_06 = 21600000l;
    public static final long DURATION_MILLIS_HOUR_12 = 43200000l;
    public static final long DURATION_MILLIS_DAY_01 = 86400000l;
    public static final long DURATION_MILLIS_WEEK_01 = 604800000l;
    public static final long DURATION_MILLIS_MONTH_01 = 2592000000l;
    public static final long DURATION_MILLIS_YEAR_01 = 31536000000l;
    
    public static long DURATION_MILLIS_MIN = 43200000l;
    public static long DURATION_MILLIS_MAX = 63072000000l;
    public static long DURATION_MILLIS_INITIAL = 604800000l;
    
    public TimePreferences(Context context) {
        loadSharedPreferences(PreferenceManager.getDefaultSharedPreferences(context));
    }
    
    public static void loadSharedPreferences(SharedPreferences mSharedPreferences) {
        DURATION_MILLIS_MIN = Long.valueOf(mSharedPreferences.getString(PreferencesFragment.DURATION_MILLIS_MIN, "43200000"));
        DURATION_MILLIS_MAX = Long.valueOf(mSharedPreferences.getString(PreferencesFragment.DURATION_MILLIS_MAX, "63072000000"));
        DURATION_MILLIS_INITIAL = Long.valueOf(mSharedPreferences.getString(PreferencesFragment.DURATION_MILLIS_INITIAL, "604800000"));
    }
    
}
