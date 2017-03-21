
package com.zeitraub.calendar.view.preferences;

import java.util.ArrayList;
import java.util.HashSet;

import com.zeitraub.calendar.MainActivity;

import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

/**
 * Fragment to display app preferences 
 * 
 * @author Torsten
 *
 */
public class PreferencesFragment extends android.preference.PreferenceFragment {
    
    private static SharedPreferences mSharedPreferences;
    
    private EditTextPreference mTime_DURATION_MILLIS_INITIAL;
    private SwitchPreference mVisual_VIEW_INFO;
    private EditTextPreference mApplication_APP_INFO_VERSION;
    private SwitchPreference mApplication_FIRST_EXECUTION;
    private boolean mFirstExecution = true;
    private CalendarListPreference mCalendar_LIST;
    
    private ArrayList<String> mCalendarIdsTmp = new ArrayList<String>();
    
    public static final String DOUBLE_TAP_ANIMATION_DURATION = "DOUBLE_TAP_ANIMATION_DURATION";
    public static final String TODAY_ANIMATION_DURATION = "TODAY_ANIMATION_DURATION";
    public static final String FLING_DECELERATE_MULTIPLIER = "FLING_DECELERATE_MULTIPLIER";
    public static final String FLING_FRICTION_MULTIPLIER = "FLING_FRICTION_MULTIPLIER";
    public static final String FLING_DURATION_MULTIPLIER = "FLING_DURATION_MULTIPLIER";
    public static final String FLING_VELOCITY_ANIMATION_MULTIPLIER = "FLING_VELOCITY_ANIMATION_MULTIPLIER";
    public static final String FLING_VELOCITY_CORE_MULTIPLIER = "FLING_VELOCITY_CORE_MULTIPLIER";
    public static final String DOUBLE_TAP_PINCH_MULTIPLIER = "DOUBLE_TAP_PINCH_MULTIPLIER";
    public static final String DOUBLE_TAP_SPREAD_MULTIPLIER = "DOUBLE_TAP_SPREAD_MULTIPLIER";
    public static final String SCALE_MULTIPLIER = "SCALE_MULTIPLIER";

    public static final String DURATION_MILLIS_INITIAL = "DURATION_MILLIS_INITIAL";
    public static final String DURATION_MILLIS_MIN = "DURATION_MILLIS_MIN";
    public static final String DURATION_MILLIS_MAX = "DURATION_MILLIS_MAX";

    public static final String ANTI_ALIASING = "ANTI_ALIASING";
    public static final String VIEW_INFORMATION = "VIEW_INFORMATION";
    public static final String FIRST_EXECUTION = "FIRST_EXECUTION";

    public static final String TIME_ELEMENT_DAY_GRADIENT_STEP = "TIME_ELEMENT_DAY_GRADIENT_STEP";
    public static final String TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS = "TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS";
    public static final String TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION = "TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION";
    public static final String TIME_ELEMENT_DAY_GRADIENT_BASE_HUE = "TIME_ELEMENT_DAY_GRADIENT_BASE_HUE";
    
    public static final String CALENDAR_LIST = "CALENDAR_LIST";
    
    private PreferenceCategory mCategoryAnimation;
    private PreferenceCategory mCategoryTime;
    private PreferenceCategory mCategoryVisual;
    private PreferenceCategory mCategoryCalendar;
    private PreferenceCategory mCategoryApplication;
    
    private SharedPreferences.OnSharedPreferenceChangeListener mSharedPreferenceChangeListener;
    
    private PreferenceScreen mPreferencesScreen;
    boolean mCalendarAccessGrandet = false;

    private MainActivity mMainActivity;
    
    public PreferencesFragment(boolean calendarAccessGranted, MainActivity mainActivity) {
        mCalendarAccessGrandet = calendarAccessGranted;
        mMainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        
        mPreferencesScreen = getPreferenceManager().createPreferenceScreen(getActivity());
        setPreferenceScreen(mPreferencesScreen);
        
        mCategoryAnimation = new PreferenceCategory(mPreferencesScreen.getContext());
        mCategoryAnimation.setTitle("Animation");
        
        mCategoryTime = new PreferenceCategory(mPreferencesScreen.getContext());
        mCategoryTime.setTitle("Time");
        
        mCategoryVisual = new PreferenceCategory(mPreferencesScreen.getContext());
        mCategoryVisual.setTitle("User Interface");
        
        mCategoryCalendar = new PreferenceCategory(mPreferencesScreen.getContext());
        mCategoryCalendar.setTitle("Calendar");

        mCategoryApplication = new PreferenceCategory(mPreferencesScreen.getContext());
        mCategoryApplication.setTitle("Application");

        mPreferencesScreen.addPreference(mCategoryCalendar);
        mPreferencesScreen.addPreference(mCategoryTime);
        mPreferencesScreen.addPreference(mCategoryVisual);
        mPreferencesScreen.addPreference(mCategoryApplication);
        
        mTime_DURATION_MILLIS_INITIAL = new EditTextPreference(getActivity());
        mTime_DURATION_MILLIS_INITIAL.setDefaultValue("604800000");
        mTime_DURATION_MILLIS_INITIAL.setKey(DURATION_MILLIS_INITIAL);
        mTime_DURATION_MILLIS_INITIAL.setSelectable(true);
        mTime_DURATION_MILLIS_INITIAL.setTitle("Timespan / startup");
        mTime_DURATION_MILLIS_INITIAL.setSummary("period of time shown on startup");
        mCategoryTime.addPreference(mTime_DURATION_MILLIS_INITIAL);
        
        mVisual_VIEW_INFO = new SwitchPreference(getActivity());
        mVisual_VIEW_INFO.setKey(VIEW_INFORMATION);
        mVisual_VIEW_INFO.setSelectable(true);
        mVisual_VIEW_INFO.setTitle("View developer information");
        mVisual_VIEW_INFO.setSummary("zoom ratio | render duration");
        mCategoryVisual.addPreference(mVisual_VIEW_INFO);
        
        mApplication_APP_INFO_VERSION = new EditTextPreference(getActivity());
        mApplication_APP_INFO_VERSION.setKey("VERSION");
        mApplication_APP_INFO_VERSION.setSelectable(false);
        try {
            mApplication_APP_INFO_VERSION.setTitle("Version: "+mPreferencesScreen.getContext().getPackageManager().getPackageInfo(mPreferencesScreen.getContext().getPackageName(), 0).versionName);
        } catch (NameNotFoundException e) {}
        mApplication_APP_INFO_VERSION.setSummary("Date: 29.04.2016");
        mCategoryApplication.addPreference(mApplication_APP_INFO_VERSION);
        
        mApplication_FIRST_EXECUTION = new SwitchPreference(getActivity());
        mApplication_FIRST_EXECUTION.setDefaultValue(true);
        mApplication_FIRST_EXECUTION.setKey(FIRST_EXECUTION);
        mApplication_FIRST_EXECUTION.setSelectable(false);
        
        if (mCalendarAccessGrandet) {
            mCalendar_LIST = new CalendarListPreference(mPreferencesScreen.getContext());
            mCalendar_LIST.setKey("CALENDAR_LIST");
            mCalendar_LIST.setSelectable(true);
            mCalendar_LIST.setTitle("Select calendars");
            mCalendar_LIST.setSummary("choose from available calendars");
            mCategoryCalendar.addPreference(mCalendar_LIST);
        }

        setSelectedCalendars();
                
        mSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                AnimationPreferences.loadSharedPreferences(prefs);
                VisualPreferences.loadSharedPreferences(prefs);
                TimePreferences.loadSharedPreferences(prefs);
                if (key.equals(CALENDAR_LIST)) {
                    setSelectedCalendars();
                }
            }
        };
        
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mSharedPreferenceChangeListener);
        
    }
    
    private void setSelectedCalendars() {
        if (mSharedPreferences.getBoolean(FIRST_EXECUTION, true) && mFirstExecution && mCalendar_LIST.getEntryValues().length > 0) {
            mCalendarIdsTmp.clear();
            for (CharSequence s : mCalendar_LIST.getEntryValues()) {
                try {
                    mCalendarIdsTmp.add(Integer.valueOf((String) s).toString());
                } catch (NumberFormatException e) {
                    // no id
                }
            }mSharedPreferences.edit().putStringSet(CALENDAR_LIST, new HashSet<String>(mCalendarIdsTmp)).apply();
            mSharedPreferences.edit().putBoolean(FIRST_EXECUTION, false).apply();
            mCalendar_LIST.update(new HashSet<String>(mCalendarIdsTmp));
            mFirstExecution = false;
            setSelectedCalendars();
        } else {
            mCalendarIdsTmp.clear();
            for (String s : mCalendar_LIST.getValues()) {
                try {
                    mCalendarIdsTmp.add(Integer.valueOf(s).toString());
                } catch (NumberFormatException e) {
                    // no id
                }
            }    
        }
        TemporaryPreferences.calendarIds = mCalendarIdsTmp;
        mMainActivity.reloadEvents();        
    }
    
}
