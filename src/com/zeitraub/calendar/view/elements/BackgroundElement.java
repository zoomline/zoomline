package com.zeitraub.calendar.view.elements;

import com.zeitraub.calendar.view.preferences.VisualPreferences;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Draws bachground graphics
 * 
 * @author Torsten
 *
 */
public class BackgroundElement {

    private int mColor01 = Color.HSVToColor(255, new float[]{ 204f, 0.02f , 0.73f});
    private int mColor02 = Color.HSVToColor(255, new float[]{ 204f, 0.02f , 0.79f});
    private int mColor03 = Color.HSVToColor(255, new float[]{ 204f, 0.02f , 0.83f});
    private int mColor04 = Color.HSVToColor(255, new float[]{ 204f, 0.02f , 0.86f});
    private int mColor05 = Color.HSVToColor(255, new float[]{ 204f, 0.02f , 0.89f});
    private int mColor06 = Color.HSVToColor(255, new float[]{ 204f, 0.02f , 0.91f});
    private int mColor07 = Color.HSVToColor(255, new float[]{ 204f, 0.02f , 0.93f});
    private int mColor08 = Color.HSVToColor(255, new float[]{ 204f, 0.02f , 0.94f});
    
    public BackgroundElement() {
    }
    
    public void drawBackground(Canvas canvas) {
        
        VisualPreferences.view_background_scale_shadow_paint.setColor(mColor01);
        canvas.drawLine(VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL, 0, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+1, VisualPreferences.VIEW_HEIGHT_PIXEL, VisualPreferences.view_background_scale_shadow_paint);
        VisualPreferences.view_background_scale_shadow_paint.setColor(mColor02);
        canvas.drawLine(VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+1, 0, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, VisualPreferences.VIEW_HEIGHT_PIXEL, VisualPreferences.view_background_scale_shadow_paint);
        VisualPreferences.view_background_scale_shadow_paint.setColor(mColor03);
        canvas.drawLine(VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+2, 0, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+3, VisualPreferences.VIEW_HEIGHT_PIXEL, VisualPreferences.view_background_scale_shadow_paint);
        VisualPreferences.view_background_scale_shadow_paint.setColor(mColor04);
        canvas.drawLine(VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+3, 0, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+4, VisualPreferences.VIEW_HEIGHT_PIXEL, VisualPreferences.view_background_scale_shadow_paint);
        VisualPreferences.view_background_scale_shadow_paint.setColor(mColor05);
        canvas.drawLine(VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+4, 0, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+5, VisualPreferences.VIEW_HEIGHT_PIXEL, VisualPreferences.view_background_scale_shadow_paint);
        VisualPreferences.view_background_scale_shadow_paint.setColor(mColor06);
        canvas.drawLine(VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+5, 0, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+6, VisualPreferences.VIEW_HEIGHT_PIXEL, VisualPreferences.view_background_scale_shadow_paint);
        VisualPreferences.view_background_scale_shadow_paint.setColor(mColor07);
        canvas.drawLine(VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+6, 0, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+7, VisualPreferences.VIEW_HEIGHT_PIXEL, VisualPreferences.view_background_scale_shadow_paint);
        VisualPreferences.view_background_scale_shadow_paint.setColor(mColor08);
        canvas.drawLine(VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+6, 0, VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+7, VisualPreferences.VIEW_HEIGHT_PIXEL, VisualPreferences.view_background_scale_shadow_paint);
        
    }
    
}
