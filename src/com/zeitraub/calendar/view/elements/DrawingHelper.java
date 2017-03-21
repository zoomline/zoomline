package com.zeitraub.calendar.view.elements;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Helper class for drawing 
 * 
 * @author Torsten
 *
 */
public class DrawingHelper {

    public enum TextAlignVertical { Top, Middle, Baseline, Bottom };

    private static boolean mIsLocalized = false;
    private static Locale mLocale = Locale.getDefault();
    
    private static Calendar mCalendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"), mLocale);
    private static Calendar mCalendarTime = GregorianCalendar.getInstance(TimeZone.getDefault(), mLocale);
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("", mLocale);
    private static StringBuilder mStringBuilder = new StringBuilder();

    
    private static String mPoint = ".";
    private static String mPoints = ":";
    private static String mSpace = " ";
    
    private static float sSizeTmp;
    private static int sAlphaTmp;
    private static Rect sRectangleTmp = new Rect();
    
    private static String mMonthNON = "";
    private static String mMonthJAN = "JAN";
    private static String mMonthFEB = "FEB";
    private static String mMonthMAR = "MÄR";
    private static String mMonthAPR = "APR";
    private static String mMonthMAI = "MAI";
    private static String mMonthJUN = "JUN";
    private static String mMonthJUL = "JUL";
    private static String mMonthAUG = "AUG";
    private static String mMonthSEP = "SEP";
    private static String mMonthOKT = "OKT";
    private static String mMonthNOV = "NOV";
    private static String mMonthDEZ = "DEZ";
    private static String mWeekDayNON = "";
    private static String mWeekDayMON = "Mo";
    private static String mWeekDayDIE = "Di";
    private static String mWeekDayMIT = "Mi";
    private static String mWeekDayDON = "Do";
    private static String mWeekDayFRE = "Fr";
    private static String mWeekDaySAM = "Sa";
    private static String mWeekDaySON = "So";
    
    public DrawingHelper() {}
    
    public static float getTextHeight(Paint paint) {
        return (Math.abs(paint.getFontMetrics().top)+paint.getFontMetrics().bottom);
    }

    public static int dipToPixel(int dip, float screenDensity) {
        return Math.round(dip * screenDensity / 160);
    }
    
    public static Paint setFontSize(Paint paint, float textSize, float screenDensity) {
        paint.setTextSize(textSize * (screenDensity / 160));
        return paint;
    }
    
    private static void setLocalizedStrings() {
        
        mCalendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"), mLocale);
        dateFormatter.applyPattern("MMM");
        
        mCalendar.set(Calendar.MONTH, 0);
        mMonthJAN = dateFormatter.format(mCalendar.getTime()).toUpperCase(mLocale).substring(0, 3);
        mCalendar.set(Calendar.MONTH, 1);
        mMonthFEB = dateFormatter.format(mCalendar.getTime()).toUpperCase(mLocale).substring(0, 3);
        mCalendar.set(Calendar.MONTH, 2);
        mMonthMAR = dateFormatter.format(mCalendar.getTime()).toUpperCase(mLocale).substring(0, 3);
        mCalendar.set(Calendar.MONTH, 3);
        mMonthAPR = dateFormatter.format(mCalendar.getTime()).toUpperCase(mLocale).substring(0, 3);
        mCalendar.set(Calendar.MONTH, 4);
        mMonthMAI = dateFormatter.format(mCalendar.getTime()).toUpperCase(mLocale).substring(0, 3);
        mCalendar.set(Calendar.MONTH, 5);
        mMonthJUN = dateFormatter.format(mCalendar.getTime()).toUpperCase(mLocale).substring(0, 3);
        mCalendar.set(Calendar.MONTH, 6);
        mMonthJUL = dateFormatter.format(mCalendar.getTime()).toUpperCase(mLocale).substring(0, 3);
        mCalendar.set(Calendar.MONTH, 7);
        mMonthAUG = dateFormatter.format(mCalendar.getTime()).toUpperCase(mLocale).substring(0, 3);
        mCalendar.set(Calendar.MONTH, 8);
        mMonthSEP = dateFormatter.format(mCalendar.getTime()).toUpperCase(mLocale).substring(0, 3);
        mCalendar.set(Calendar.MONTH, 9);
        mMonthOKT = dateFormatter.format(mCalendar.getTime()).toUpperCase(mLocale).substring(0, 3);
        mCalendar.set(Calendar.MONTH, 10);
        mMonthNOV = dateFormatter.format(mCalendar.getTime()).toUpperCase(mLocale).substring(0, 3);
        mCalendar.set(Calendar.MONTH, 11);
        mMonthDEZ = dateFormatter.format(mCalendar.getTime()).toUpperCase(mLocale).substring(0, 3);


        dateFormatter.applyPattern("EEE");
        mCalendar.set(Calendar.DAY_OF_WEEK, 1);
        mWeekDayMON = dateFormatter.format(mCalendar.getTime()).substring(0, 2);
        mCalendar.set(Calendar.DAY_OF_WEEK, 2);
        mWeekDayDIE = dateFormatter.format(mCalendar.getTime()).substring(0, 2);
        mCalendar.set(Calendar.DAY_OF_WEEK, 3);
        mWeekDayMIT = dateFormatter.format(mCalendar.getTime()).substring(0, 2);
        mCalendar.set(Calendar.DAY_OF_WEEK, 4);
        mWeekDayDON = dateFormatter.format(mCalendar.getTime()).substring(0, 2);
        mCalendar.set(Calendar.DAY_OF_WEEK, 5);
        mWeekDayFRE = dateFormatter.format(mCalendar.getTime()).substring(0, 2);
        mCalendar.set(Calendar.DAY_OF_WEEK, 6);
        mWeekDaySAM = dateFormatter.format(mCalendar.getTime()).substring(0, 2);
        mCalendar.set(Calendar.DAY_OF_WEEK, 0);
        mWeekDaySON = dateFormatter.format(mCalendar.getTime()).substring(0, 2);
        
        mIsLocalized = true;
    }
    
    public static String getMonthMMM(int month){
        if (!mIsLocalized || mLocale != Locale.getDefault()) {
            setLocalizedStrings();
        }
        switch (month) {
            case 0:
                return mMonthJAN;
            case 1:
                return mMonthFEB;
            case 2:
                return mMonthMAR;
            case 3:
                return mMonthAPR;
            case 4:
                return mMonthMAI;
            case 5:
                return mMonthJUN;
            case 6:
                return mMonthJUL;
            case 7:
                return mMonthAUG;
            case 8:
                return mMonthSEP;
            case 9:
                return mMonthOKT;
            case 10:
                return mMonthNOV;
            case 11:
                return mMonthDEZ;
        }
        return mMonthNON;
    }
    
    public static String getYearYY(int year){
        mStringBuilder.setLength(0);
        return mStringBuilder.append(year).substring(2, 4);
    }
    
    public static String getDayDD(int day){
        mStringBuilder.setLength(0);
        if (day < 10) {
            return mStringBuilder.append(0).append(day).toString(); 
        } else {
            return mStringBuilder.append(day).toString(); 
        }
    }
    
    public static String getWeekWW(int week){
        mStringBuilder.setLength(0);
        if (week < 10) {
            return mStringBuilder.append(0).append(week).toString(); 
        } else {
            return mStringBuilder.append(week).toString(); 
        }
    }
    
    public static String getWeekDayDD(int weekday){
        if (!mIsLocalized || mLocale != Locale.getDefault()) {
            setLocalizedStrings();
        }
        switch (weekday) {
            case 1:
                return mWeekDayMON;
            case 2:
                return mWeekDayDIE;
            case 3:
                return mWeekDayMIT;
            case 4:
                return mWeekDayDON;
            case 5:
                return mWeekDayFRE;
            case 6:
                return mWeekDaySAM;
            case 7:
                return mWeekDaySON;
        }
        return mWeekDayNON;
    }
    
    public static String getTimeHHMM(long millis) {
        mCalendarTime.setTimeInMillis(millis);
        mStringBuilder.setLength(0);
        if (mCalendarTime.get(Calendar.HOUR_OF_DAY) < 10) {
            mStringBuilder.append(0).append(mCalendarTime.get(Calendar.HOUR_OF_DAY)).toString(); 
        } else {
            mStringBuilder.append(mCalendarTime.get(Calendar.HOUR_OF_DAY)).toString(); 
        }
        mStringBuilder.append(mPoints);
        if (mCalendarTime.get(Calendar.MINUTE) < 10) {
            mStringBuilder.append(0).append(mCalendarTime.get(Calendar.MINUTE)).toString(); 
        } else {
            mStringBuilder.append(mCalendarTime.get(Calendar.MINUTE)).toString(); 
        }
        return mStringBuilder.toString();
    }
    
    public static String getTimeHHMMEDDMMYYYY(long millis) { //HH:mm  E  dd.MM.yyyy
        mCalendarTime.setTimeInMillis(millis);
        mStringBuilder.setLength(0);
        if (mCalendarTime.get(Calendar.HOUR_OF_DAY) < 10) {
            mStringBuilder.append(0).append(mCalendarTime.get(Calendar.HOUR_OF_DAY)); 
        } else {
            mStringBuilder.append(mCalendarTime.get(Calendar.HOUR_OF_DAY)); 
        }
        mStringBuilder.append(mPoints);
        if (mCalendarTime.get(Calendar.MINUTE) < 10) {
            mStringBuilder.append(0).append(mCalendarTime.get(Calendar.MINUTE)); 
        } else {
            mStringBuilder.append(mCalendarTime.get(Calendar.MINUTE)); 
        }
        mStringBuilder.append(mSpace).append(mSpace);
        mStringBuilder.append(getWeekDayDD(mCalendarTime.get(Calendar.DAY_OF_WEEK)));
        mStringBuilder.append(mSpace).append(mSpace);
        if (mCalendarTime.get(Calendar.DAY_OF_MONTH) < 10) {
            mStringBuilder.append(0).append(mCalendarTime.get(Calendar.DAY_OF_MONTH)); 
        } else {
            mStringBuilder.append(mCalendarTime.get(Calendar.DAY_OF_MONTH)); 
        }
        mStringBuilder.append(mPoint);
        if (mCalendarTime.get(Calendar.MONTH) < 10) {
            mStringBuilder.append(0).append(mCalendarTime.get(Calendar.MONTH)); 
        } else {
            mStringBuilder.append(mCalendarTime.get(Calendar.MONTH)); 
        }
        mStringBuilder.append(mPoint);
        mStringBuilder.append(mCalendarTime.get(Calendar.YEAR));
        return mStringBuilder.toString();
    }
    
    public static void drawLineAlpha(Canvas canvas, Paint paint, float left, float top, float right, float bottom, int alpha) {
        sAlphaTmp = paint.getAlpha();
        paint.setAlpha(alpha);
        canvas.drawLine(left, top, right, bottom, paint);
        paint.setAlpha(sAlphaTmp);
    }
    
    public static void drawRectangleAlpha(Canvas canvas, Paint paint, float top, float left, float bottom, float right, int alpha) {
        sAlphaTmp = paint.getAlpha();
        paint.setAlpha(alpha);
        canvas.drawRect(left, top, right, bottom, paint);
        paint.setAlpha(sAlphaTmp);
    }
    
    public static void drawText(Canvas canvas, Paint paint, String text, float x, float y, Paint.Align alignHorizontal, TextAlignVertical alignVertical, int alpha, float ratio) { 
        
        sAlphaTmp = paint.getAlpha();
        sSizeTmp = paint.getTextSize();
        
        paint.setAlpha(alpha);
        paint.setTextSize(paint.getTextSize()*ratio);
        
        sRectangleTmp.setEmpty();
        paint.getTextBounds("[", 0, 1, sRectangleTmp);         
       
        switch (alignVertical) { 
            
            case Top:
                y = y-sRectangleTmp.top;
                break;
            
            case Middle: 
                y = y - sRectangleTmp.top-sRectangleTmp.height()/2; 
                break; 
                
            case Baseline:  
                break; 
                
            case Bottom: 
                y = y-(sRectangleTmp.height()+sRectangleTmp.top); 
                break; 
                
        } 
        
        paint.setTextAlign(alignHorizontal);
        
        canvas.drawText(text, x, y, paint);
        
        paint.setTextSize(sSizeTmp);
        paint.setAlpha(sAlphaTmp);
    }
    
}