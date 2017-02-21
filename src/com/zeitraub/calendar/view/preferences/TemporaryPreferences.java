package com.zeitraub.calendar.view.preferences;

import java.util.ArrayList;
import java.util.TimeZone;

import com.zeitraub.calendar.view.elements.EventViewElement;

public class TemporaryPreferences {
     
    public static float screenDensity;
    
    public static int viewWidth;
    public static int viewHeight;
    
    public static long dayNowTopMillis;
    public static long hour12NowMillis;
    public static int yearNow;
    public static int dayNow;
    
    public static volatile long viewTopMillis;
    public static volatile long viewBottomMillis;
    
    public static volatile ArrayList<String> calendarIds = new ArrayList<String>();

    public static volatile ArrayList<Long> daysInScalaList = new ArrayList<Long>();
    public static volatile long selectedDayInScalaMillis = 0;
    public static volatile long selectedWeekDayInScalaIndex = 0;
    public static volatile boolean selectedDayIsSelected = false;
    
    public static TimeZone timezone;

    public static boolean isEventSelected = false;
    public static EventViewElement eventSelected = null;
    
    public static boolean isVisibleEventDetails = false;
    public static EventViewElement visibleEventDetails = null;

    public static float detailTop = -1;
    public static float detailLeft = -1;
    public static float detailBottom = -1;
    public static float detailRight = -1;
    
    
    public TemporaryPreferences() {
    }
    
}

