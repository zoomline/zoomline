package com.zeitraub.calendar.view.elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import com.zeitraub.calendar.view.preferences.VisualPreferences;

import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Path;

/**
 * Draws the main menu
 * 
 * @author Torsten
 *
 */
public class MenuElement {
    
    private static Path mArrowPath;
    
    private static boolean isOpened = false;
    private ArrayList<Region> mMenuRegions = new ArrayList<Region>(); 
    private int mRegionsIndex;
    
    public enum MenuAction { None, TapClosedMenu, TapOpenedMenu, TapTodaySubMenu, TapAddEventSubMenu, TapEmptyTopSubMenu, TapResetSubMenu, TapOptionsSubMenu };
    public enum MenuType { None, ClosedMenu, OpenedMenu, AddEventSubMenu, TodaySubMenu, OptionsSubMenu, EmptyTopSubMenu, ResetSubMenu };
    
    private static Calendar mCalendarTemp = GregorianCalendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    
    public MenuElement() {
        
        // path for arrow
        mArrowPath = new Path();
        mArrowPath.setFillType(Path.FillType.EVEN_ODD);
        mArrowPath.moveTo(VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL - VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL - VisualPreferences.MENU_ELEMENT_MAIN_ARROW_LONG_PIXEL + VisualPreferences.MENU_ELEMENT_MAIN_ARROW_SHORT_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL);
        mArrowPath.lineTo(VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL - VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL + VisualPreferences.MENU_ELEMENT_MAIN_ARROW_OFFSET_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL - (VisualPreferences.MENU_ELEMENT_MAIN_ARROW_LONG_PIXEL*0.5f));
        mArrowPath.lineTo(VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL - VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL + VisualPreferences.MENU_ELEMENT_MAIN_ARROW_OFFSET_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL + (VisualPreferences.MENU_ELEMENT_MAIN_ARROW_LONG_PIXEL*0.5f));
        mArrowPath.lineTo(VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL - VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL - VisualPreferences.MENU_ELEMENT_MAIN_ARROW_LONG_PIXEL + VisualPreferences.MENU_ELEMENT_MAIN_ARROW_SHORT_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL);
        mArrowPath.close();
    }

    private Region getRegionFromCenterAndRadius(MenuType t, int x, int y, int r) {
        // create new region
        return new Region(t, y-r, x-r, y+r, x+r);
    }
    
    public MenuAction getMenuAction(float x, float y) {
        // add regions if necessary 
        if (mMenuRegions.isEmpty()) {
            mMenuRegions.add(getRegionFromCenterAndRadius(MenuType.ClosedMenu, VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL));
            mMenuRegions.add(getRegionFromCenterAndRadius(MenuType.OpenedMenu, VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL));
            mMenuRegions.add(getRegionFromCenterAndRadius(MenuType.AddEventSubMenu, (int) (VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.75f)), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL));
            mMenuRegions.add(getRegionFromCenterAndRadius(MenuType.OptionsSubMenu, (int) (VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*0.7f)), (int) (VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL+(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.75f)), VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL));
            mMenuRegions.add(getRegionFromCenterAndRadius(MenuType.TodaySubMenu, (int) (VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*0.7f)), (int) (VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.75f)), VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL));
            mMenuRegions.add(getRegionFromCenterAndRadius(MenuType.EmptyTopSubMenu, (int) (VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.15f)), (int) (VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.35f)), VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL));
            mMenuRegions.add(getRegionFromCenterAndRadius(MenuType.ResetSubMenu, (int) (VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.15f)), (int) (VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL+(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.35f)), VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL));
        }
        // return menu action 
        for (mRegionsIndex = 0; mRegionsIndex < mMenuRegions.size(); mRegionsIndex++) {
            if (mMenuRegions.get(mRegionsIndex).isInsideRegion(x, y)) {
                if (isOpened && mMenuRegions.get(mRegionsIndex).mType == MenuType.OpenedMenu) {
                    isOpened = false;
                    return MenuAction.TapOpenedMenu;
                } else if (!isOpened && mMenuRegions.get(mRegionsIndex).mType == MenuType.ClosedMenu) {
                    isOpened = true;
                    return MenuAction.TapClosedMenu;
                } else if (isOpened && mMenuRegions.get(mRegionsIndex).mType == MenuType.AddEventSubMenu) {
                    return MenuAction.TapAddEventSubMenu;
                } else if (isOpened && mMenuRegions.get(mRegionsIndex).mType == MenuType.OptionsSubMenu) {
                    return MenuAction.TapOptionsSubMenu;
                } else if (isOpened && mMenuRegions.get(mRegionsIndex).mType == MenuType.TodaySubMenu) {
                    return MenuAction.TapTodaySubMenu;
                } else if (isOpened && mMenuRegions.get(mRegionsIndex).mType == MenuType.EmptyTopSubMenu) {
                    return MenuAction.TapEmptyTopSubMenu;
                } else if (isOpened && mMenuRegions.get(mRegionsIndex).mType == MenuType.ResetSubMenu) {
                    return MenuAction.TapResetSubMenu;
                }                
            }
        }
        return MenuAction.None;
    }
    
    public static void drawMainMenu(Canvas canvas, int viewHeight, int viewWidth, long currentMillis, float ratioMenu) {
        // arrow
        canvas.drawPath(mArrowPath, VisualPreferences.mainElement_Main_Paint);
        // main menu
        canvas.drawCircle(VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*ratioMenu), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL, VisualPreferences.mainElement_Main_Paint);
        // time from middle of the view
        mCalendarTemp.setTimeInMillis(currentMillis);
        // draw text for menu
        DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Month_Paint, DrawingHelper.getMonthMMM(mCalendarTemp.get(Calendar.MONTH)), VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL/2)-((VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*0.5f)*ratioMenu), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL, Align.CENTER, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Week_Paint, DrawingHelper.getDayDD(mCalendarTemp.get(Calendar.DAY_OF_MONTH)), (float) (VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL/6))-((VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*0.65f)*ratioMenu), (float) (VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL - (VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*0.55)), Align.RIGHT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Year_Paint, "'"+DrawingHelper.getYearYY(mCalendarTemp.get(Calendar.YEAR)), (float) (VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL/6))-((VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*0.65f)*ratioMenu), (float) (VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL + (VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*0.55)), Align.RIGHT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
    }
 
    
    public static void drawMainMenuSub(Canvas canvas, int viewHeight, int viewWidth, float screenDensity, long currentMillis, float subMenuRatio) {
        // red line
        canvas.drawLine(VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-((VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL)*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL, VisualPreferences.mainElement_Main_Sub_Line_Paint);
        // middle
        canvas.drawCircle(VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.75f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_SUB_RADIUS_PIXEL, VisualPreferences.mainElement_Main_Sub_Add_Paint);
        // border middle
        canvas.drawCircle(VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.75f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL, VisualPreferences.MENU_ELEMENT_MAIN_SUB_RADIUS_PIXEL, VisualPreferences.mainElement_Main_Sub_Line_Paint);
        // left / top
        canvas.drawCircle(VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.15f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.35f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_SUB_RADIUS_PIXEL, VisualPreferences.mainElement_Main_Sub_Paint);
        // left / bottom
        canvas.drawCircle(VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.15f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL+(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.35f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_SUB_RADIUS_PIXEL, VisualPreferences.mainElement_Main_Sub_Paint);
        // right / top
        canvas.drawCircle(VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*0.7f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.75f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_SUB_RADIUS_PIXEL, VisualPreferences.mainElement_Main_Sub_Today_Paint);
        // right / bottom
        canvas.drawCircle(VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*0.7f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL+(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.75f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_SUB_RADIUS_PIXEL, VisualPreferences.mainElement_Main_Sub_Paint);
        // today/now
        mCalendarTemp.setTimeInMillis(System.currentTimeMillis());
        // draw text for sub menu
        DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Today_Paint, "+", VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.75f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL, Align.CENTER, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Today_Paint, DrawingHelper.getDayDD(mCalendarTemp.get(Calendar.DAY_OF_MONTH)), VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*0.7f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.75f*subMenuRatio), Align.CENTER, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        // time from middle of the view
        mCalendarTemp.setTimeInMillis(currentMillis);
        if (Locale.getDefault().getDisplayLanguage().equals("Deutsch")) {
            DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Sub_Week_Label_Paint, "KW", VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.15f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.1f*subMenuRatio), Align.CENTER, DrawingHelper.TextAlignVertical.Middle, 255, 1);    
        } else {
            DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Sub_Week_Label_Paint, "week", VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.15f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.1f*subMenuRatio), Align.CENTER, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        }
        
        DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Week_Paint, DrawingHelper.getWeekWW(mCalendarTemp.get(Calendar.WEEK_OF_YEAR)), VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.15f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.45f*subMenuRatio), Align.CENTER, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Sub_Label_Paint, "...", VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*0.7f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL+(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.75f*subMenuRatio), Align.CENTER, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Week_Paint, "__", VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.15f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL+(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.05f*subMenuRatio), Align.CENTER, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Week_Paint, "__", VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.15f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL+(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.15f*subMenuRatio), Align.CENTER, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Week_Paint, "__", VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.15f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL+(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.25f*subMenuRatio), Align.CENTER, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Week_Paint, "__", VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.15f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL+(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.35f*subMenuRatio), Align.CENTER, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        DrawingHelper.drawText(canvas, VisualPreferences.mainElement_Main_Week_Paint, "__", VisualPreferences.MENU_ELEMENT_MAIN_POSX_PIXEL-(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*2.15f*subMenuRatio), VisualPreferences.MENU_ELEMENT_MAIN_POSY_PIXEL+(VisualPreferences.MENU_ELEMENT_MAIN_RADIUS_PIXEL*1.45f*subMenuRatio), Align.CENTER, DrawingHelper.TextAlignVertical.Middle, 255, 1);
    }
    
}
