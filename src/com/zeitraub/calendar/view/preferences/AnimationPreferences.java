package com.zeitraub.calendar.view.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Preferences for animations
 * 
 * @author Torsten
 *
 */
public class AnimationPreferences {

    public static float SCALE_MULTIPLIER = 4.0f;
    
    public static float DOUBLE_TAP_SPREAD_MULTIPLIER = 0.4f;
    public static float DOUBLE_TAP_PINCH_MULTIPLIER = 14f;
    
    public static float FLING_VELOCITY_CORE_MULTIPLIER = 0.1f;
    public static float FLING_VELOCITY_ANIMATION_MULTIPLIER = 0.025f;
    public static float FLING_DURATION_MULTIPLIER = 2.0f;
    public static float FLING_FRICTION_MULTIPLIER = 0.5f;
    public static  float FLING_DECELERATE_MULTIPLIER = 0.5f;
    
    public static long DOUBLE_TAP_ANIMATION_DURATION = 300;
    public static long TODAY_ANIMATION_DURATION = 300;
    
    public AnimationPreferences(Context context) {
        loadSharedPreferences(PreferenceManager.getDefaultSharedPreferences(context));
    }
    
    public static void loadSharedPreferences(SharedPreferences mSharedPreferences) {

        SCALE_MULTIPLIER = Float.valueOf(mSharedPreferences.getString(PreferencesFragment.SCALE_MULTIPLIER, "4.0"));
        DOUBLE_TAP_SPREAD_MULTIPLIER = Float.valueOf(mSharedPreferences.getString(PreferencesFragment.DOUBLE_TAP_SPREAD_MULTIPLIER, "0.4"));
        DOUBLE_TAP_PINCH_MULTIPLIER = Float.valueOf(mSharedPreferences.getString(PreferencesFragment.DOUBLE_TAP_PINCH_MULTIPLIER, "14"));
        
        FLING_DECELERATE_MULTIPLIER = Float.valueOf(mSharedPreferences.getString(PreferencesFragment.FLING_DECELERATE_MULTIPLIER, "0.5"));
        FLING_FRICTION_MULTIPLIER = Float.valueOf(mSharedPreferences.getString(PreferencesFragment.FLING_FRICTION_MULTIPLIER, "0.5"));
        FLING_DURATION_MULTIPLIER = Float.valueOf(mSharedPreferences.getString(PreferencesFragment.FLING_DURATION_MULTIPLIER, "2.0"));
        FLING_VELOCITY_ANIMATION_MULTIPLIER = Float.valueOf(mSharedPreferences.getString(PreferencesFragment.FLING_VELOCITY_ANIMATION_MULTIPLIER, "0.025"));
        FLING_VELOCITY_CORE_MULTIPLIER = Float.valueOf(mSharedPreferences.getString(PreferencesFragment.FLING_VELOCITY_CORE_MULTIPLIER, "0.1"));
                
        DOUBLE_TAP_ANIMATION_DURATION = Long.valueOf(mSharedPreferences.getString(PreferencesFragment.DOUBLE_TAP_ANIMATION_DURATION, "300"));
        TODAY_ANIMATION_DURATION = Long.valueOf(mSharedPreferences.getString(PreferencesFragment.TODAY_ANIMATION_DURATION, "300"));
        
    }
    
}
