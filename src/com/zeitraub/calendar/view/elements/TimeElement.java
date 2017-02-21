package com.zeitraub.calendar.view.elements;

import java.util.Calendar;

import com.zeitraub.calendar.view.preferences.VisualPreferences;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * Draws ui elements at specified location
 * 
 * @author Torsten
 *
 */
public class TimeElement {
    
    private String mDayAsString = "";
        
    public TimeElement() {}

    public void drawYearElement(Canvas canvas, long offset, int year, int alpha, float ratio) {
        DrawingHelper.drawText(canvas, VisualPreferences.timeElement_Year_Paint, getYearAsString(year), VisualPreferences.TIME_ELEMENT_SCALE_YEAR_PADDING_PIXEL, offset+VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL/5f, Align.CENTER, DrawingHelper.TextAlignVertical.Middle, alpha, ratio);       
    }

    public void drawMonthElement(Canvas canvas, long offset, int month, int alpha, float ratio) {
        DrawingHelper.drawText(canvas, VisualPreferences.timeElement_Month_Paint, DrawingHelper.getMonthMMM(month), VisualPreferences.TIME_ELEMENT_SCALE_MONTH_PADDING_PIXEL, offset+VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL/5f, Align.CENTER, DrawingHelper.TextAlignVertical.Middle, alpha, ratio);
    }

    public void drawWeekDayElement(Canvas canvas, long offset, int weekday, int alpha, float ratio) {
        DrawingHelper.drawText(canvas, VisualPreferences.timeElement_WeekDay_Paint, DrawingHelper.getWeekDayDD(weekday), VisualPreferences.TIME_ELEMENT_SCALE_WEEKDAY_PADDING_PIXEL, offset+VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL/5f, Align.LEFT, DrawingHelper.TextAlignVertical.Middle, alpha, ratio);
    }

    public void drawSelectedDayElement(Canvas canvas, long offset, int weekday, float elementHeightPixel, int alpha, boolean today) {
        DrawingHelper.drawRectangleAlpha(canvas, VisualPreferences.timeElement_Selected_Day_Paint, offset, 0, offset+elementHeightPixel, VisualPreferences.VIEW_WIDTH_PIXEL, 80);
    }
     
    public void drawSelectedWeekDayElement(Canvas canvas, long offset, int weekday, float elementHeightPixel, int alpha, boolean today) {
        DrawingHelper.drawRectangleAlpha(canvas, VisualPreferences.timeElement_Selected_WeekDay_Paint, offset, 0, offset+elementHeightPixel, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL, 50);
    }
    
    public void drawWeekDayMiddleElement(Canvas canvas, long offset, int weekday, float elementHeightPixel, int alpha, float ratio) {
        DrawingHelper.drawText(canvas, VisualPreferences.timeElement_WeekDay_Paint, DrawingHelper.getWeekDayDD(weekday), VisualPreferences.TIME_ELEMENT_SCALE_WEEKDAY_PADDING_PIXEL, offset+(0.5f*elementHeightPixel), Align.LEFT, DrawingHelper.TextAlignVertical.Middle, alpha, ratio);
    }
    
    public void drawDayElement(Canvas canvas, long offset, int day, int weekday, int alpha, float ratio) {
        if (weekday == Calendar.SATURDAY) {
            DrawingHelper.drawText(canvas, VisualPreferences.timeElement_Saturday_Paint, getDayAsString(day), VisualPreferences.TIME_ELEMENT_SCALE_DAY_PADDING_PIXEL, offset+VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL/5f, Align.CENTER, DrawingHelper.TextAlignVertical.Middle, alpha, ratio);
        } else if (weekday == Calendar.SUNDAY) {
            DrawingHelper.drawText(canvas, VisualPreferences.timeElement_Sunday_Paint, getDayAsString(day), VisualPreferences.TIME_ELEMENT_SCALE_DAY_PADDING_PIXEL, offset+VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL/5f, Align.CENTER, DrawingHelper.TextAlignVertical.Middle, alpha, ratio);
        } else {
            DrawingHelper.drawText(canvas, VisualPreferences.timeElement_Day_Paint, getDayAsString(day), VisualPreferences.TIME_ELEMENT_SCALE_DAY_PADDING_PIXEL, offset+VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL/5f, Align.CENTER, DrawingHelper.TextAlignVertical.Middle, alpha, ratio);
        }
    }

    public void drawDayMiddleElement(Canvas canvas, long offset, int day, float elementHeightPixel, int weekday, int alpha, float ratio) {
        if (weekday == Calendar.SATURDAY) {
            DrawingHelper.drawText(canvas, VisualPreferences.timeElement_Saturday_Paint, getDayAsString(day), VisualPreferences.TIME_ELEMENT_SCALE_DAY_PADDING_PIXEL, offset+(0.5f*elementHeightPixel), Align.CENTER, DrawingHelper.TextAlignVertical.Middle, alpha, ratio);
        } else if (weekday == Calendar.SUNDAY) {
            DrawingHelper.drawText(canvas, VisualPreferences.timeElement_Sunday_Paint, getDayAsString(day), VisualPreferences.TIME_ELEMENT_SCALE_DAY_PADDING_PIXEL, offset+(0.5f*elementHeightPixel), Align.CENTER, DrawingHelper.TextAlignVertical.Middle, alpha, ratio);
        } else {
            DrawingHelper.drawText(canvas, VisualPreferences.timeElement_Day_Paint, getDayAsString(day), VisualPreferences.TIME_ELEMENT_SCALE_DAY_PADDING_PIXEL, offset+(0.5f*elementHeightPixel), Align.CENTER, DrawingHelper.TextAlignVertical.Middle, alpha, ratio);
        }
    }

    public void drawHourElement(Canvas canvas, long offset, int hour, int alpha, float ratio) {
        DrawingHelper.drawText(canvas, VisualPreferences.timeElement_Hour_Paint, getHourAsString(hour), VisualPreferences.TIME_ELEMENT_SCALE_HOUR_PADDING_PIXEL, offset, Align.CENTER, DrawingHelper.TextAlignVertical.Middle, alpha, ratio);
    }

    public void drawScalaLine(Canvas canvas, long offset, float ratio, int alpha) {
//        canvas.drawLine(VisualPreferences.TIME_ELEMENT_SCALE_LINE_PADDING_PIXEL, offset, VisualPreferences.TIME_ELEMENT_SCALE_LINE_PADDING_PIXEL - (VisualPreferences.TIME_ELEMENT_SCALE_LINE_WIDTH_PIXEL*ratio), offset, VisualPreferences.timeElement_Scale_Line_1_Paint);
        DrawingHelper.drawLineAlpha(canvas, VisualPreferences.timeElement_Scale_Line_1_Paint, VisualPreferences.TIME_ELEMENT_SCALE_LINE_PADDING_PIXEL, offset, VisualPreferences.TIME_ELEMENT_SCALE_LINE_PADDING_PIXEL - (VisualPreferences.TIME_ELEMENT_SCALE_LINE_WIDTH_PIXEL*ratio), offset, alpha);
    }

    public void drawSeperatorFull(Canvas canvas, long offset, float ratio) {
        canvas.drawLine(0, offset, VisualPreferences.VIEW_WIDTH_PIXEL, offset, VisualPreferences.timeElement_Scale_Seperator_Paint);
    }
   
    public void drawSeperatorEvents(Canvas canvas, long offset, int alpha) {
        DrawingHelper.drawLineAlpha(canvas, VisualPreferences.timeElement_Scale_Seperator_Light_Paint,VisualPreferences.EVENT_ELEMENT_PADDING_PIXEL, offset, VisualPreferences.VIEW_WIDTH_PIXEL, offset,  alpha);
    }

    public void drawSeperatorEventsLight(Canvas canvas, long offset, float ratio) {
        canvas.drawLine(VisualPreferences.EVENT_ELEMENT_PADDING_PIXEL, offset, VisualPreferences.VIEW_WIDTH_PIXEL, offset, VisualPreferences.timeElement_Scale_Seperator_Light_Paint);
    }
    
    public void drawDayTodayElementBackground(Canvas canvas, int top, int left, int bottom, int right, int alpha) {
        DrawingHelper.drawRectangleAlpha(canvas, VisualPreferences.timeElement_Today_Paint, top, left, bottom, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL, 124);
        DrawingHelper.drawRectangleAlpha(canvas, VisualPreferences.timeElement_Today_Paint, top, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL, bottom, VisualPreferences.VIEW_WIDTH_PIXEL, 40);
    }

    public void drawFirstDayOfMonthElementBackground(Canvas canvas, int top, int left, int bottom, int right, int alpha) {
        DrawingHelper.drawRectangleAlpha(canvas, VisualPreferences.timeElement_Month_Background_Paint, top, left, bottom, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, 255);

        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(230, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(205, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(180, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(160, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(140, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(120, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(100, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(85, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(70, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(55, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(40, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(30, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(20, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(10, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
    }

    public void drawFirstDayOfYearElementBackground(Canvas canvas, int top, int left, int bottom, int right, int alpha) {
        DrawingHelper.drawRectangleAlpha(canvas, VisualPreferences.timeElement_Month_Background_Paint, top, left, bottom, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, 255);
        
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(230, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(205, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(180, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(160, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(140, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(120, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(100, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(85, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(70, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(55, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(40, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(30, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(20, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
        VisualPreferences.timeElement_Month_Shadow_Paint.setColor(Color.HSVToColor(10, new float[]{ 198f, 0.06f , 0.86f}));
        canvas.drawLine(left, bottom++, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, bottom, VisualPreferences.timeElement_Month_Shadow_Paint);
    }

    public void drawWeekEvenElementBackground(Canvas canvas, int top, int left, int bottom, int right, int alpha) {
        VisualPreferences.timeElement_Week_Even_Background_Paint.setColor(Color.HSVToColor(255, new float[]{ 204f, 0.02f , 0.97f}));
        DrawingHelper.drawRectangleAlpha(canvas, VisualPreferences.timeElement_Week_Even_Background_Paint, top, left, bottom, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, 255);
        VisualPreferences.timeElement_Week_Even_Background_Paint.setColor(Color.HSVToColor(255, new float[]{ 204f, 0.02f , 0.95f}));
        DrawingHelper.drawRectangleAlpha(canvas, VisualPreferences.timeElement_Week_Even_Background_Paint, top, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+VisualPreferences.TIME_ELEMENT_SCALE_SHADOW_WIDTH_PIXEL, bottom, VisualPreferences.VIEW_WIDTH_PIXEL, 255);
       
    }

    public void drawWeekOddElementBackground(Canvas canvas, float top, float left, float bottom, float right, int alpha) {
        VisualPreferences.timeElement_Week_Odd_Background_Paint.setColor(Color.HSVToColor(255, new float[]{ 204f, 0.02f , 0.98f}));
        DrawingHelper.drawRectangleAlpha(canvas, VisualPreferences.timeElement_Week_Odd_Background_Paint, top, left, bottom, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, 255);
        VisualPreferences.timeElement_Week_Odd_Background_Paint.setColor(Color.HSVToColor(255, new float[]{ 204f, 0.02f , 1.0f}));
        DrawingHelper.drawRectangleAlpha(canvas, VisualPreferences.timeElement_Week_Odd_Background_Paint, top, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+VisualPreferences.TIME_ELEMENT_SCALE_SHADOW_WIDTH_PIXEL, bottom, VisualPreferences.VIEW_WIDTH_PIXEL, 255);
       
    }
    
    private String getYearAsString(int year) {
        return String.valueOf(year);
    }

    private String getDayAsString(int day) {
        if (day < 10) {
            mDayAsString = "0"+String.valueOf(day);
        } else {
            mDayAsString = String.valueOf(day);
        }
        return mDayAsString;
    }
    
    private String getHourAsString(int hour) {
        return String.valueOf(hour)+":00";
    }

}
