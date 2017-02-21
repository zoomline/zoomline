package com.zeitraub.calendar.view.preferences;

import com.zeitraub.calendar.view.elements.DrawingHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.TextPaint;

/**
 * Preferences for ui elements
 * 
 * @author Torsten
 *
 */
public class VisualPreferences {

    // ANTI ALIASING
    public static boolean ANTI_ALIASING = true;
    public static boolean VIEW_INFORMATION = false;
        
    // DIP
    private static int TIME_ELEMENT_CALENDAR_WIDTH = 8000;
    
    private static int TIME_ELEMENT_SCALE_LINE_WIDTH = 800;
    private static int TIME_ELEMENT_SCALE_LINE_PADDING = 8000;
    public static int TIME_ELEMENT_SCALE_SHADOW_WIDTH_PIXEL = 7;
    
    private static int TIME_ELEMENT_SCALE_HOUR_PADDING = 4000;
    private static int TIME_ELEMENT_SCALE_WEEKDAY_PADDING = 1000;
    private static int TIME_ELEMENT_SCALE_DAY_PADDING = 5250;
    private static int TIME_ELEMENT_SCALE_WEEK_PADDING = 4000;
    private static int TIME_ELEMENT_SCALE_MONTH_PADDING = 4000;
    private static int TIME_ELEMENT_SCALE_YEAR_PADDING = 4000;
    
    private static int TIME_ELEMENT_TEXTSIZE_LARGER = 3000;
    private static int TIME_ELEMENT_TEXTSIZE_LARGE = 2500;
    private static int TIME_ELEMENT_TEXTSIZE_MEDIUM = 2250;
    private static int TIME_ELEMENT_TEXTSIZE_SMALL = 2000;
    private static int TIME_ELEMENT_TEXTSIZE_SMALLER = 1750;
    private static int TIME_ELEMENT_TEXTSIZE_SMALLEST = 1250;
    
    private static int MENU_ELEMENT_MAIN_RADIUS = 6500;
    private static int MENU_ELEMENT_MAIN_RADIUS_PADDING = 2000;
    
    private static int MENU_ELEMENT_MAIN_ARROW_LONG = 2000;
    private static int MENU_ELEMENT_MAIN_ARROW_SHORT = 1000;
    private static int MENU_ELEMENT_MAIN_ARROW_OFFSET = 250;
    
    private static int MENU_ELEMENT_MAIN_SUB_RADIUS = 4000;
    private static int MENU_ELEMENT_MAIN_SUB_RADIUS_PADDING = 2000;
    
    private static int EVENT_ELEMENT_ALLDAY_WIDTH = 1500;
    private static int EVENT_ELEMENT_LABEL_PADDING_LEFT = 50;
    private static int EVENT_ELEMENT_LABEL_PADDING_RIGHT = 550;
    private static int EVENT_ELEMENT_LABEL_MARGIN_LEFT = 250;
    private static int EVENT_ELEMENT_LABEL_MARGIN_RIGHT = 250;
    private static int EVENT_ELEMENT_LABEL_MARGIN_TOP = 250;
    private static int EVENT_ELEMENT_LABEL_MARGIN_BOTTOM = 250;
    private static int EVENT_ELEMENT_LABEL_COLOR_WIDTH = 500;
    
    // PIXEL
    public static int VIEW_WIDTH_PIXEL;
    public static int VIEW_HEIGHT_PIXEL;
    
    public static float SCREEN_DENSITY;
    
    public static int EVENT_ELEMENT_WIDTH_PIXEL;
    public static int EVENT_ELEMENT_PADDING_PIXEL;
    public static int EVENT_ELEMENT_ALLDAY_WIDTH_PIXEL;
    
    public static int TIME_ELEMENT_CALENDAR_WIDTH_PIXEL;
    
    public static int TIME_ELEMENT_SCALE_LINE_WIDTH_PIXEL;
    public static int TIME_ELEMENT_SCALE_LINE_PADDING_PIXEL;
    
    public static int TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL;
    public static int TIME_ELEMENT_TEXTSIZE_LARGE_PIXEL;
    public static int TIME_ELEMENT_TEXTSIZE_MEDIUM_PIXEL;
    public static int TIME_ELEMENT_TEXTSIZE_SMALL_PIXEL;
    public static int TIME_ELEMENT_TEXTSIZE_SMALLER_PIXEL;
    public static int TIME_ELEMENT_TEXTSIZE_SMALLEST_PIXEL;

    public static int TIME_ELEMENT_SCALE_HOUR_PADDING_PIXEL;
    public static int TIME_ELEMENT_SCALE_WEEKDAY_PADDING_PIXEL;
    public static int TIME_ELEMENT_SCALE_DAY_PADDING_PIXEL;
    public static int TIME_ELEMENT_SCALE_WEEK_PADDING_PIXEL;
    public static int TIME_ELEMENT_SCALE_MONTH_PADDING_PIXEL;
    public static int TIME_ELEMENT_SCALE_YEAR_PADDING_PIXEL;
    
    public static int MENU_ELEMENT_MAIN_POSX_PIXEL;
    public static int MENU_ELEMENT_MAIN_POSY_PIXEL;
    public static int MENU_ELEMENT_MAIN_RADIUS_PIXEL;
    public static int MENU_ELEMENT_MAIN_RADIUS_PADDING_PIXEL;
    
    public static int MENU_ELEMENT_MAIN_ARROW_LONG_PIXEL;
    public static int MENU_ELEMENT_MAIN_ARROW_SHORT_PIXEL;
    public static int MENU_ELEMENT_MAIN_ARROW_OFFSET_PIXEL;
    
    public static int MENU_ELEMENT_MAIN_SUB_RADIUS_PIXEL;
    public static int MENU_ELEMENT_MAIN_SUB_RADIUS_PADDING_PIXEL;

    public static int EVENT_ELEMENT_LABEL_PADDING_LEFT_PIXEL;
    public static int EVENT_ELEMENT_LABEL_PADDING_RIGHT_PIXEL;
    public static int EVENT_ELEMENT_LABEL_MARGIN_LEFT_PIXEL;
    public static int EVENT_ELEMENT_LABEL_MARGIN_RIGHT_PIXEL;
    public static int EVENT_ELEMENT_LABEL_MARGIN_TOP_PIXEL;
    public static int EVENT_ELEMENT_LABEL_MARGIN_BOTTOM_PIXEL;
    public static int EVENT_ELEMENT_LABEL_COLOR_WIDTH_PIXEL;
    
    // GRADIENT
    public static float TIME_ELEMENT_DAY_GRADIENT_BASE_HUE = 0f; // 0 - 360
    public static float TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION = 0f; // 0 - 1
    public static float TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS = 0.975f; // 0 - 1
    public static float TIME_ELEMENT_DAY_GRADIENT_STEP = 0.03f;
    
    // PAINT
    public static int TIME_ELEMENT_SCALE_LINE_1_COLOR = Color.argb(255, 33,33,33); // #212121 Grey 900
    public static Paint timeElement_Scale_Line_1_Paint; 
    
    public static int TIME_ELEMENT_SCALE_SEPERATOR_COLOR = Color.HSVToColor(255, new float[]{ 200f, 0.25f , 0.85f});
    public static Paint timeElement_Scale_Seperator_Paint; 

    public static int TIME_ELEMENT_SCALE_SEPERATOR_LIGHT_COLOR = Color.HSVToColor(100, new float[]{ 200f, 0.15f , 0.85f});
    public static Paint timeElement_Scale_Seperator_Light_Paint; 
    
    public static int TIME_ELEMENT_YEAR_COLOR = Color.argb(255, 69,90,100); // #455A64 Blue Grey 700
    public static Paint timeElement_Year_Paint;
    
    public static int TIME_ELEMENT_MONTH_COLOR = Color.argb(255, 69,90,100); // #455A64 Blue Grey 700
    public static Paint timeElement_Month_Paint;
    
    public static int TIME_ELEMENT_WEEKDAY_COLOR = Color.argb(255, 69,90,100); // #455A64 Blue Grey 700
    public static Paint timeElement_WeekDay_Paint;
    
    public static int TIME_ELEMENT_DAY_COLOR = Color.argb(255, 69,90,100); // #455A64 Blue Grey 700
    public static Paint timeElement_Day_Paint;

    public static int TIME_ELEMENT_HOUR_COLOR = Color.argb(255, 69,90,100); // #455A64 Blue Grey 700
    public static Paint timeElement_Hour_Paint;

    public static int TIME_ELEMENT_TODAY_COLOR = Color.HSVToColor(255, new float[]{ 38, 1.0f , 1.0f}); // #FFD180 Orange A100
    public static Paint timeElement_Today_Paint;
    
    public static int TIME_ELEMENT_SELECTED_DAY_COLOR = Color.HSVToColor(255, new float[]{ 200f,0.31f,0.55f}); // DB 500
    public static Paint timeElement_Selected_Day_Paint;
    
    public static int TIME_ELEMENT_SELECTED_WEEKDAY_COLOR = Color.HSVToColor(255, new float[]{ 200f,0.31f,0.55f}); // DB 500
    public static Paint timeElement_Selected_WeekDay_Paint;
    
    public static int TIME_ELEMENT_MONTH_BACKGROUND_COLOR = Color.HSVToColor(255, new float[]{ 198f, 0.06f , 0.86f});
    public static Paint timeElement_Month_Background_Paint;
    
    public static int TIME_ELEMENT_WEEK_EVEN_BACKGROUND_COLOR = Color.HSVToColor(255, new float[]{ 204f, 0.02f , 0.97f});
    public static Paint timeElement_Week_Even_Background_Paint;
    
    public static int TIME_ELEMENT_WEEK_Odd_BACKGROUND_COLOR = Color.HSVToColor(255, new float[]{ 204f, 0.02f , 1.0f});
    public static Paint timeElement_Week_Odd_Background_Paint;
    
    public static Paint timeElement_Month_Shadow_Paint;
    
    public static int EVENT_ELEMENT_EVENT_BORDER_COLOR = Color.HSVToColor(255, new float[]{ 204f, 0.02f , 1.0f});
    
    
    public static Paint timeElement_Monday_Gradient_Paint;
    public static Paint timeElement_Tuesday_Gradient_Paint;
    public static Paint timeElement_Wednesday_Gradient_Paint;
    public static Paint timeElement_Thursday_Gradient_Paint;
    public static Paint timeElement_Friday_Gradient_Paint;
    public static Paint timeElement_Saturday_Gradient_Paint;
    public static Paint timeElement_Sunday_Gradient_Paint;

    public static int TIME_ELEMENT_MONDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*1)});
    public static int TIME_ELEMENT_TUESDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*2)});
    public static int TIME_ELEMENT_WEDNESDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*3)});
    public static int TIME_ELEMENT_THURSDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*4)});
    public static int TIME_ELEMENT_FRIDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*5)});
    public static int TIME_ELEMENT_SATURDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*6)});
    public static int TIME_ELEMENT_SUNDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*7)});
     
        
    public static int TIME_ELEMENT_SATURDAY_COLOR = Color.argb(255, 120,144,156); // #FF6E40 Deep Orange 300
    public static Paint timeElement_Saturday_Paint;
    
    public static int TIME_ELEMENT_SUNDAY_COLOR = Color.argb(255, 230,74,25); // #FF6E40 Deep Orange 700
    public static Paint timeElement_Sunday_Paint;
    
    
    public static int EVENT_ELEMENT_EVENT_TEXT_COLOR = Color.argb(255, 69,90,100); // #455A64 Blue Grey 700
    public static TextPaint eventElement_Event_Text_Paint;
    
    
    public static int MENU_ELEMENT_MAIN_COLOR = Color.HSVToColor(255, new float[]{ 200f, 0.11f , 0.77f});
    public static Paint mainElement_Main_Paint;

    public static int MENU_ELEMENT_MAIN_SUB_COLOR = Color.HSVToColor(255, new float[]{ 198, 0.06f , 0.86f});
    public static Paint mainElement_Main_Sub_Paint;

    public static int MENU_ELEMENT_MAIN_SUB_ADD_COLOR = Color.HSVToColor(255, new float[]{ 14, 0.5f , 1.0f}); // Deep Orange A100 *
    public static Paint mainElement_Main_Sub_Add_Paint;
    
    public static int MENU_ELEMENT_MAIN_SUB_LINE_COLOR = Color.HSVToColor(255, new float[]{ 14, 0.5f , 1.0f}); // Deep Orange A100 *
    public static Paint mainElement_Main_Sub_Line_Paint;
    
    public static int MENU_ELEMENT_MAIN_SUB_TODAY_COLOR = Color.HSVToColor(255, new float[]{ 38, 0.5f , 1.0f}); // Orange A100 *
    public static Paint mainElement_Main_Sub_Today_Paint;

    public static int MENU_ELEMENT_MAIN_SUB_WEEK_LABEL_COLOR = Color.HSVToColor(255, new float[]{200f,0.30f,0.31f}); // Blue Grey 800 *
    public static Paint mainElement_Main_Sub_Week_Label_Paint; 
    
    public static int MENU_ELEMENT_MAIN_SUB_LABEL_COLOR = Color.HSVToColor(255, new float[]{200f,0.30f,0.31f}); // Blue Grey 800 *
    public static Paint mainElement_Main_Sub_Label_Paint;
    
    public static int MENU_ELEMENT_MAIN_YEAR_COLOR = Color.HSVToColor(255, new float[]{200f,0.30f,0.31f}); // Blue Grey 800 *
    public static Paint mainElement_Main_Year_Paint;
    
    public static int MENU_ELEMENT_MAIN_WEEK_COLOR = Color.HSVToColor(255, new float[]{200f,0.30f,0.31f}); // Blue Grey 800 *
    public static Paint mainElement_Main_Week_Paint;
    
    public static int MENU_ELEMENT_MAIN_MONTH_COLOR = Color.HSVToColor(255, new float[]{200f,0.30f,0.31f}); // Blue Grey 800 *
    public static Paint mainElement_Main_Month_Paint;

    public static int MENU_ELEMENT_MAIN_TODAY_COLOR = Color.HSVToColor(255, new float[]{200f,0.30f,0.31f}); // Blue Grey 800 *
    public static Paint mainElement_Main_Today_Paint;
    
    public static int VIEW_BACKGROUND_COLOR = Color.HSVToColor(255, new float[]{ 204f, 0.02f , 0.99f});
    
    public static Paint view_background_scale_shadow_paint;

    public static void generatePaintObjects(int viewHeight, int viewWidth, float screenDesity, Context mContext) {
        loadSharedPreferences(PreferenceManager.getDefaultSharedPreferences(mContext));
        generatePaintObjects(viewHeight, viewWidth, screenDesity);
    }

    

    public static void generatePaintObjects(int viewHeight, int viewWidth, float screenDesity) {
        
        VIEW_WIDTH_PIXEL = viewWidth;
        VIEW_HEIGHT_PIXEL = viewHeight;

        SCREEN_DENSITY = screenDesity;
        
        TIME_ELEMENT_CALENDAR_WIDTH_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_CALENDAR_WIDTH, SCREEN_DENSITY);
        TIME_ELEMENT_SCALE_LINE_WIDTH_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_SCALE_LINE_WIDTH, SCREEN_DENSITY);
        TIME_ELEMENT_SCALE_LINE_PADDING_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_SCALE_LINE_PADDING, SCREEN_DENSITY);
        TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_TEXTSIZE_LARGER, SCREEN_DENSITY);
        TIME_ELEMENT_TEXTSIZE_LARGE_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_TEXTSIZE_LARGE, SCREEN_DENSITY);
        TIME_ELEMENT_TEXTSIZE_MEDIUM_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_TEXTSIZE_MEDIUM, SCREEN_DENSITY);
        TIME_ELEMENT_TEXTSIZE_SMALL_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_TEXTSIZE_SMALL, SCREEN_DENSITY);
        TIME_ELEMENT_TEXTSIZE_SMALLER_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_TEXTSIZE_SMALLER, SCREEN_DENSITY);
        TIME_ELEMENT_TEXTSIZE_SMALLEST_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_TEXTSIZE_SMALLEST, SCREEN_DENSITY);
        TIME_ELEMENT_SCALE_HOUR_PADDING_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_SCALE_HOUR_PADDING, SCREEN_DENSITY);
        TIME_ELEMENT_SCALE_WEEKDAY_PADDING_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_SCALE_WEEKDAY_PADDING, SCREEN_DENSITY);
        TIME_ELEMENT_SCALE_DAY_PADDING_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_SCALE_DAY_PADDING, SCREEN_DENSITY);
        TIME_ELEMENT_SCALE_WEEK_PADDING_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_SCALE_WEEK_PADDING, SCREEN_DENSITY);
        TIME_ELEMENT_SCALE_MONTH_PADDING_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_SCALE_MONTH_PADDING, SCREEN_DENSITY);
        TIME_ELEMENT_SCALE_YEAR_PADDING_PIXEL = DrawingHelper.dipToPixel(TIME_ELEMENT_SCALE_YEAR_PADDING, SCREEN_DENSITY);
        
        EVENT_ELEMENT_ALLDAY_WIDTH_PIXEL = DrawingHelper.dipToPixel(EVENT_ELEMENT_ALLDAY_WIDTH, SCREEN_DENSITY);
        EVENT_ELEMENT_PADDING_PIXEL = TIME_ELEMENT_CALENDAR_WIDTH_PIXEL+TIME_ELEMENT_SCALE_SHADOW_WIDTH_PIXEL;
        EVENT_ELEMENT_WIDTH_PIXEL = VIEW_WIDTH_PIXEL-EVENT_ELEMENT_PADDING_PIXEL;
        
        EVENT_ELEMENT_LABEL_PADDING_LEFT_PIXEL = DrawingHelper.dipToPixel(EVENT_ELEMENT_LABEL_PADDING_LEFT, SCREEN_DENSITY);
        EVENT_ELEMENT_LABEL_PADDING_RIGHT_PIXEL = DrawingHelper.dipToPixel(EVENT_ELEMENT_LABEL_PADDING_RIGHT, SCREEN_DENSITY);
        EVENT_ELEMENT_LABEL_MARGIN_LEFT_PIXEL = DrawingHelper.dipToPixel(EVENT_ELEMENT_LABEL_MARGIN_LEFT, SCREEN_DENSITY);
        EVENT_ELEMENT_LABEL_MARGIN_RIGHT_PIXEL = DrawingHelper.dipToPixel(EVENT_ELEMENT_LABEL_MARGIN_RIGHT, SCREEN_DENSITY);
        EVENT_ELEMENT_LABEL_MARGIN_TOP_PIXEL = DrawingHelper.dipToPixel(EVENT_ELEMENT_LABEL_MARGIN_TOP, SCREEN_DENSITY);
        EVENT_ELEMENT_LABEL_MARGIN_BOTTOM_PIXEL = DrawingHelper.dipToPixel(EVENT_ELEMENT_LABEL_MARGIN_BOTTOM, SCREEN_DENSITY);
        EVENT_ELEMENT_LABEL_COLOR_WIDTH_PIXEL = DrawingHelper.dipToPixel(EVENT_ELEMENT_LABEL_COLOR_WIDTH, SCREEN_DENSITY);
        
        MENU_ELEMENT_MAIN_POSX_PIXEL = viewWidth;
        MENU_ELEMENT_MAIN_POSY_PIXEL = viewHeight/2;
        MENU_ELEMENT_MAIN_RADIUS_PIXEL = DrawingHelper.dipToPixel(MENU_ELEMENT_MAIN_RADIUS, SCREEN_DENSITY);
        MENU_ELEMENT_MAIN_RADIUS_PADDING_PIXEL = DrawingHelper.dipToPixel(MENU_ELEMENT_MAIN_RADIUS_PADDING, SCREEN_DENSITY);
        MENU_ELEMENT_MAIN_ARROW_LONG_PIXEL = DrawingHelper.dipToPixel(MENU_ELEMENT_MAIN_ARROW_LONG, SCREEN_DENSITY);
        MENU_ELEMENT_MAIN_ARROW_OFFSET_PIXEL = DrawingHelper.dipToPixel(MENU_ELEMENT_MAIN_ARROW_OFFSET, SCREEN_DENSITY);
        MENU_ELEMENT_MAIN_ARROW_SHORT_PIXEL = DrawingHelper.dipToPixel(MENU_ELEMENT_MAIN_ARROW_SHORT, SCREEN_DENSITY);
        MENU_ELEMENT_MAIN_SUB_RADIUS_PIXEL = DrawingHelper.dipToPixel(MENU_ELEMENT_MAIN_SUB_RADIUS, SCREEN_DENSITY);
        MENU_ELEMENT_MAIN_SUB_RADIUS_PADDING_PIXEL = DrawingHelper.dipToPixel(MENU_ELEMENT_MAIN_SUB_RADIUS_PADDING, SCREEN_DENSITY);
        
        TIME_ELEMENT_MONDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*1)});
        TIME_ELEMENT_TUESDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*2)});
        TIME_ELEMENT_WEDNESDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*3)});
        TIME_ELEMENT_THURSDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*4)});
        TIME_ELEMENT_FRIDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*5)});
        TIME_ELEMENT_SATURDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*6)});
        TIME_ELEMENT_SUNDAY_GRADIENT_COLOR = Color.HSVToColor(255, new float[]{TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS-(TIME_ELEMENT_DAY_GRADIENT_STEP*7)});
        
        timeElement_Scale_Line_1_Paint = new Paint();
        timeElement_Scale_Line_1_Paint.setStyle(Style.STROKE);
        timeElement_Scale_Line_1_Paint.setStrokeWidth(1f);
        timeElement_Scale_Line_1_Paint.setColor(TIME_ELEMENT_SCALE_LINE_1_COLOR);
        timeElement_Scale_Line_1_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Scale_Seperator_Paint = new Paint();
        timeElement_Scale_Seperator_Paint.setStyle(Style.STROKE);
        timeElement_Scale_Seperator_Paint.setStrokeWidth(1f);
        timeElement_Scale_Seperator_Paint.setColor(TIME_ELEMENT_SCALE_SEPERATOR_COLOR);
        timeElement_Scale_Seperator_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Scale_Seperator_Light_Paint = new Paint();
        timeElement_Scale_Seperator_Light_Paint.setStyle(Style.STROKE);
        timeElement_Scale_Seperator_Light_Paint.setStrokeWidth(1f);
        timeElement_Scale_Seperator_Light_Paint.setColor(TIME_ELEMENT_SCALE_SEPERATOR_LIGHT_COLOR);
        timeElement_Scale_Seperator_Light_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Year_Paint = new Paint();
        timeElement_Year_Paint.setColor(TIME_ELEMENT_YEAR_COLOR);
        timeElement_Year_Paint.setTypeface(Typeface.DEFAULT_BOLD);
        timeElement_Year_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_MEDIUM_PIXEL);
        timeElement_Year_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Month_Paint = new Paint();
        timeElement_Month_Paint.setColor(TIME_ELEMENT_MONTH_COLOR);
        timeElement_Month_Paint.setTypeface(Typeface.DEFAULT_BOLD);
        timeElement_Month_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_MEDIUM_PIXEL);
        timeElement_Month_Paint.setAntiAlias(ANTI_ALIASING);

        timeElement_WeekDay_Paint = new Paint();
        timeElement_WeekDay_Paint.setColor(TIME_ELEMENT_WEEKDAY_COLOR);
        timeElement_WeekDay_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_SMALLER_PIXEL);
        timeElement_WeekDay_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Day_Paint = new Paint();
        timeElement_Day_Paint.setColor(TIME_ELEMENT_DAY_COLOR);
        timeElement_Day_Paint.setTypeface(Typeface.DEFAULT_BOLD);
        timeElement_Day_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_MEDIUM_PIXEL);
        timeElement_Day_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Hour_Paint = new Paint();
        timeElement_Hour_Paint.setColor(TIME_ELEMENT_HOUR_COLOR);
        timeElement_Hour_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_SMALLER_PIXEL);
        timeElement_Hour_Paint.setAntiAlias(ANTI_ALIASING);

        
        timeElement_Today_Paint = new Paint();
        timeElement_Today_Paint.setStyle(Paint.Style.FILL);
        timeElement_Today_Paint.setColor(TIME_ELEMENT_TODAY_COLOR);
        timeElement_Today_Paint.setAntiAlias(ANTI_ALIASING);

        timeElement_Selected_Day_Paint = new Paint();
        timeElement_Selected_Day_Paint.setStyle(Paint.Style.FILL);
        timeElement_Selected_Day_Paint.setColor(TIME_ELEMENT_SELECTED_DAY_COLOR);
        timeElement_Selected_Day_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Selected_WeekDay_Paint = new Paint();
        timeElement_Selected_WeekDay_Paint.setStyle(Paint.Style.FILL);
        timeElement_Selected_WeekDay_Paint.setColor(TIME_ELEMENT_SELECTED_WEEKDAY_COLOR);
        timeElement_Selected_WeekDay_Paint.setAntiAlias(ANTI_ALIASING);

        timeElement_Month_Background_Paint = new Paint();
        timeElement_Month_Background_Paint.setStyle(Paint.Style.FILL);
        timeElement_Month_Background_Paint.setColor(TIME_ELEMENT_MONTH_BACKGROUND_COLOR);
        timeElement_Month_Background_Paint.setAntiAlias(ANTI_ALIASING);

        timeElement_Week_Even_Background_Paint = new Paint();
        timeElement_Week_Even_Background_Paint.setStyle(Paint.Style.FILL);
        timeElement_Week_Even_Background_Paint.setColor(TIME_ELEMENT_WEEK_EVEN_BACKGROUND_COLOR);
        timeElement_Week_Even_Background_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Week_Odd_Background_Paint = new Paint();
        timeElement_Week_Odd_Background_Paint.setStyle(Paint.Style.FILL);
        timeElement_Week_Odd_Background_Paint.setColor(TIME_ELEMENT_WEEK_Odd_BACKGROUND_COLOR);
        timeElement_Week_Odd_Background_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Month_Shadow_Paint = new Paint();
        timeElement_Month_Shadow_Paint.setStyle(Paint.Style.FILL);
        timeElement_Month_Shadow_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Monday_Gradient_Paint = new Paint();
        timeElement_Monday_Gradient_Paint.setDither(true);
        timeElement_Monday_Gradient_Paint.setStyle(Paint.Style.FILL);
        timeElement_Monday_Gradient_Paint.setColor(TIME_ELEMENT_MONDAY_GRADIENT_COLOR);
        timeElement_Monday_Gradient_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Tuesday_Gradient_Paint = new Paint();
        timeElement_Tuesday_Gradient_Paint.setDither(true);
        timeElement_Tuesday_Gradient_Paint.setStyle(Paint.Style.FILL);
        timeElement_Tuesday_Gradient_Paint.setColor(TIME_ELEMENT_TUESDAY_GRADIENT_COLOR);
        timeElement_Tuesday_Gradient_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Wednesday_Gradient_Paint = new Paint();
        timeElement_Wednesday_Gradient_Paint.setStyle(Paint.Style.FILL);
        timeElement_Wednesday_Gradient_Paint.setColor(TIME_ELEMENT_WEDNESDAY_GRADIENT_COLOR);
        timeElement_Wednesday_Gradient_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Thursday_Gradient_Paint = new Paint();
        timeElement_Thursday_Gradient_Paint.setStyle(Paint.Style.FILL);
        timeElement_Thursday_Gradient_Paint.setColor(TIME_ELEMENT_THURSDAY_GRADIENT_COLOR);
        timeElement_Thursday_Gradient_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Friday_Gradient_Paint = new Paint();
        timeElement_Friday_Gradient_Paint.setStyle(Paint.Style.FILL);
        timeElement_Friday_Gradient_Paint.setColor(TIME_ELEMENT_FRIDAY_GRADIENT_COLOR);
        timeElement_Friday_Gradient_Paint.setAntiAlias(ANTI_ALIASING);
    
        timeElement_Saturday_Gradient_Paint = new Paint();
        timeElement_Saturday_Gradient_Paint.setStyle(Paint.Style.FILL);
        timeElement_Saturday_Gradient_Paint.setColor(TIME_ELEMENT_SATURDAY_GRADIENT_COLOR);
        timeElement_Saturday_Gradient_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Sunday_Gradient_Paint = new Paint();
        timeElement_Sunday_Gradient_Paint.setStyle(Paint.Style.FILL);
        timeElement_Sunday_Gradient_Paint.setColor(TIME_ELEMENT_SUNDAY_GRADIENT_COLOR);
        timeElement_Sunday_Gradient_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Saturday_Paint = new Paint();
        timeElement_Saturday_Paint.setTypeface(Typeface.DEFAULT_BOLD);
        timeElement_Saturday_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_MEDIUM_PIXEL);
        timeElement_Saturday_Paint.setColor(TIME_ELEMENT_SATURDAY_COLOR);
        timeElement_Saturday_Paint.setAntiAlias(ANTI_ALIASING);
        
        timeElement_Sunday_Paint = new Paint();
        timeElement_Sunday_Paint.setTypeface(Typeface.DEFAULT_BOLD);
        timeElement_Sunday_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_MEDIUM_PIXEL);
        timeElement_Sunday_Paint.setColor(TIME_ELEMENT_SUNDAY_COLOR);
        timeElement_Sunday_Paint.setAntiAlias(ANTI_ALIASING);

        
        eventElement_Event_Text_Paint = new TextPaint();
        eventElement_Event_Text_Paint.setTypeface(Typeface.DEFAULT);
        eventElement_Event_Text_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_SMALLER_PIXEL);
        eventElement_Event_Text_Paint.setColor(EVENT_ELEMENT_EVENT_TEXT_COLOR);
        eventElement_Event_Text_Paint.setAntiAlias(ANTI_ALIASING);
        
        
        mainElement_Main_Paint = new Paint();
        mainElement_Main_Paint.setAntiAlias(ANTI_ALIASING);
        mainElement_Main_Paint.setColor(MENU_ELEMENT_MAIN_COLOR);
        
        mainElement_Main_Sub_Paint = new Paint();
        mainElement_Main_Sub_Paint.setAntiAlias(ANTI_ALIASING);
        mainElement_Main_Sub_Paint.setColor(MENU_ELEMENT_MAIN_SUB_COLOR);
        
        mainElement_Main_Sub_Add_Paint = new Paint();
        mainElement_Main_Sub_Add_Paint.setAntiAlias(ANTI_ALIASING);
        mainElement_Main_Sub_Add_Paint.setColor(MENU_ELEMENT_MAIN_SUB_LINE_COLOR);
        
        mainElement_Main_Sub_Line_Paint = new Paint();
        mainElement_Main_Sub_Line_Paint.setAntiAlias(ANTI_ALIASING);
        mainElement_Main_Sub_Line_Paint.setColor(MENU_ELEMENT_MAIN_SUB_LINE_COLOR);
        mainElement_Main_Sub_Line_Paint.setStyle(Style.STROKE);
        mainElement_Main_Sub_Line_Paint.setStrokeWidth(1);
        
        mainElement_Main_Sub_Today_Paint = new Paint();
        mainElement_Main_Sub_Today_Paint.setAntiAlias(ANTI_ALIASING);
        mainElement_Main_Sub_Today_Paint.setColor(MENU_ELEMENT_MAIN_SUB_TODAY_COLOR);
        
        mainElement_Main_Year_Paint = new Paint();
        mainElement_Main_Year_Paint.setAntiAlias(ANTI_ALIASING);
        mainElement_Main_Year_Paint.setColor(MENU_ELEMENT_MAIN_YEAR_COLOR);
        mainElement_Main_Year_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_LARGE_PIXEL);
        
        mainElement_Main_Week_Paint = new Paint();
        mainElement_Main_Week_Paint.setAntiAlias(ANTI_ALIASING);
        mainElement_Main_Week_Paint.setColor(MENU_ELEMENT_MAIN_WEEK_COLOR);
        mainElement_Main_Week_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_LARGE_PIXEL);
        
        mainElement_Main_Sub_Week_Label_Paint = new Paint();
        mainElement_Main_Sub_Week_Label_Paint.setAntiAlias(ANTI_ALIASING);
        mainElement_Main_Sub_Week_Label_Paint.setColor(MENU_ELEMENT_MAIN_WEEK_COLOR);
        mainElement_Main_Sub_Week_Label_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_SMALLEST_PIXEL);

        mainElement_Main_Sub_Label_Paint = new Paint();
        mainElement_Main_Sub_Label_Paint.setAntiAlias(ANTI_ALIASING);
        mainElement_Main_Sub_Label_Paint.setColor(MENU_ELEMENT_MAIN_WEEK_COLOR);
        mainElement_Main_Sub_Label_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_MEDIUM_PIXEL);
        
        mainElement_Main_Month_Paint = new Paint();
        mainElement_Main_Month_Paint.setAntiAlias(ANTI_ALIASING);
        mainElement_Main_Month_Paint.setColor(MENU_ELEMENT_MAIN_MONTH_COLOR);
        mainElement_Main_Month_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_LARGE_PIXEL);
        
        mainElement_Main_Today_Paint = new Paint();
        mainElement_Main_Today_Paint.setAntiAlias(ANTI_ALIASING);
        mainElement_Main_Today_Paint.setColor(MENU_ELEMENT_MAIN_TODAY_COLOR);
        mainElement_Main_Today_Paint.setTextSize(TIME_ELEMENT_TEXTSIZE_LARGE_PIXEL);
                
        view_background_scale_shadow_paint = new Paint();
        view_background_scale_shadow_paint.setAntiAlias(ANTI_ALIASING);
                
    }
    
    public static void loadSharedPreferences(SharedPreferences mSharedPreferences) {
        
//        ANTI_ALIASING = mSharedPreferences.getBoolean(PreferencesFragment.ANTI_ALIASING, true);
        VIEW_INFORMATION = mSharedPreferences.getBoolean(PreferencesFragment.VIEW_INFORMATION, false);

        TIME_ELEMENT_DAY_GRADIENT_BASE_HUE = Float.valueOf(mSharedPreferences.getString(PreferencesFragment.TIME_ELEMENT_DAY_GRADIENT_BASE_HUE, "0"));
        TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION = Float.valueOf(mSharedPreferences.getString(PreferencesFragment.TIME_ELEMENT_DAY_GRADIENT_BASE_SATURATION, "0"));
        TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS = Float.valueOf(mSharedPreferences.getString(PreferencesFragment.TIME_ELEMENT_DAY_GRADIENT_BASE_BRIGHTNESS, "0.975"));
        TIME_ELEMENT_DAY_GRADIENT_STEP = Float.valueOf(mSharedPreferences.getString(PreferencesFragment.TIME_ELEMENT_DAY_GRADIENT_STEP, "0.03"));
        
        generatePaintObjects(VIEW_HEIGHT_PIXEL, VIEW_WIDTH_PIXEL, SCREEN_DENSITY);
        
    }
    
}
