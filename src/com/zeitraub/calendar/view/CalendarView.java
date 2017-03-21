package com.zeitraub.calendar.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import com.zeitraub.calendar.MainActivity;
import com.zeitraub.calendar.control.EventsHandler;
import com.zeitraub.calendar.data.event.Event;
import com.zeitraub.calendar.view.animations.Animation;
import com.zeitraub.calendar.view.elements.BackgroundElement;
import com.zeitraub.calendar.view.elements.EventElement;
import com.zeitraub.calendar.view.elements.EventViewElement;
import com.zeitraub.calendar.view.elements.MenuElement;
import com.zeitraub.calendar.view.elements.MenuElement.MenuAction;
import com.zeitraub.calendar.view.elements.TimeElement;
import com.zeitraub.calendar.view.preferences.AnimationPreferences;
import com.zeitraub.calendar.view.preferences.TemporaryPreferences;
import com.zeitraub.calendar.view.preferences.TimePreferences;
import com.zeitraub.calendar.view.preferences.VisualPreferences;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Calendar view handles user interaction
 * 
 * @author Torsten
 *
 */
public class CalendarView extends SurfaceView implements SurfaceHolder.Callback, OnScaleGestureListener, Runnable {

    // context
    private Context mContext;
    
    // surfaceview basics
    private Canvas mCanvas;
    private Thread mThread;
    private SurfaceHolder mHolder;
    private volatile boolean mIsRunning = false;
    
    // gestures
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    
    // fling for momentum scrolling
    private Interpolator mAnimationInterpolator;
    private Scroller mAnimationScroller;
    private Animation mAnimationFling;
    private long mAnimationFlingOffsetY;
    private long mAnimationFlingCurrentMillis;
    private float mAnimationFlingCurrentVelocity;
    private float mAnimationFlingStartY;
    private float mAnimationFlingEndY;
    private float mAnimationFlingDuration;
    private float mAnimationFlingVelocity;
    private volatile boolean mAnimationFlingIsRunning;

    // reset animation
    private Animation mAnimationResetTopValue;
    private Animation mAnimationResetBottomValue;
    private long mAnimationResetMillisTopEnd;
    private long mAnimationResetMillisBottomEnd;
    private long mAnimationResetTimeStart;
    private long mAnimationResetTimeNow;
    private volatile boolean mAnimationResetIsRunning;
    private long mViewSpanResetMillis;
    
    // double tap animation
    private Animation mAnimationDoubleTapTopValue;
    private Animation mAnimationDoubleTapBottomValue;
    private long mAnimationDoubleTapMillisTopEnd;
    private long mAnimationDoubleTapMillisBottomEnd;
    private long mAnimationDoubleTapTimeStart;
    private long mAnimationDoubleTapTimeNow;
    private volatile boolean mAnimationDoubleTapIsRunning;
    
    // double tap animation
    private Animation mAnimationTodayTopValue;
    private Animation mAnimationTodayBottomValue;
    private long mAnimationTodayTimeStart;
    private long mAnimationTodayTimeNow;
    private volatile boolean mAnimationTodayIsRunning;
    
    // menu animation
    private Animation mAnimationMenu;
    private float mAnimationMenuRatio;
    private volatile boolean mAnimationMenuIsRunning;
    
    // dimensions of the surfaceview
    private int mViewHeightPixel;
    private int mViewWidthPixel;

    // screen density
    private float mScreenDensity; 
    
    // view-elements
    private BackgroundElement mBackgroundElement;
    private TimeElement mTimeElement;
    private EventElement mEventElement;
    private MenuElement mMenuElement;
    
    // view rendering
    private float mRenderStageRatio;
    private float mRenderIndex;
    private float mPxOffset_HourHalf;
    private float mPxOffset_Hour;
    private float mPxOffset_Day;
    private float mPxOffset_Week;
    private Calendar mCalendarHourHalf;
    private Calendar mCalendarHour;
    private Calendar mCalendarDay;
    private Calendar mCalendarWeek;
    private Calendar mCalendarMonth;
    private Calendar mCalendarYear;
    private int pxOffsetOriginDay; 
    private int pxOffsetOriginWeek;
    private int pxOffsetOriginMonth;
    private int pxOffsetOriginYear;
        
    // calendars
    private Calendar mCalendar;
    private Calendar mCalendarToday;
    private Calendar mCalendarSelected;
    private Calendar mCalendarCreateEvent;
    private TimeZone mTimeZoneUTC;
    
    // millis view properties
    private long mViewTopMillis;
    private long mViewBottomMillis;
    private long mViewSpanMillis;
    
    // diffrent touch-modes
    private int mTouchMode;
    private final static int TOUCH_MODE_NONE = 0;
    private final static int TOUCH_MODE_TAP = 100;
    private final static int TOUCH_MODE_TAP_DOUBLE = 200;
    private final static int TOUCH_MODE_LONG_PRESS = 300;
    private final static int TOUCH_MODE_SCROLL = 400;
    private final static int TOUCH_MODE_FLING = 500;
    private final static int TOUCH_MODE_SCALE = 600;
    
    // properties for scale-calculation
    private double mViewScaleSpanDeltaMillis;
    private double mViewScaleStageMultiplier;
    private double mViewScaleSpanFocalPointPercentage;
    private double mViewScaleTempTopMillis;
    private double mViewScaleTempBottomMillis;
    private double mViewScaleTempSpanMillis;
    
    // multiplier to convert millis to pixel  
    private float mViewMultiplierMillisToPixel;
    
    // event lists 
    private ArrayList<Event> mEventsAllDay;
    private ArrayList<Event> mEventsDayGraph;
    private ArrayList<Event> mEventsRow = new ArrayList<Event>();
    private ArrayList<EventViewElement> mEventsOnView = new ArrayList<EventViewElement>();
    
    // properties #TODO #DELETE
    private boolean isGenerated = false;
    private long startTIME = 0;
    private MenuAction mMenuTapAction = MenuElement.MenuAction.None;
    private int mTouchEventPointerCount;
        
    public CalendarView(Context context, BackgroundElement backgroundElement, TimeElement timeElement, EventElement eventElement, MenuElement menuElement, int selectedEventId, long selectedEventBegin) {
        super(context);
        
        // surfaceview basics
        mContext = context;
        getHolder().addCallback(this);
        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.RGBA_8888);

        // gestures
        mGestureDetector = new GestureDetector(context, new CalendarGestureDetector());
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        
        // double tap animators
        mAnimationDoubleTapTopValue = new Animation();
        mAnimationDoubleTapBottomValue = new Animation();
        
        // today animators
        mAnimationTodayTopValue = new Animation();
        mAnimationTodayBottomValue = new Animation();
        
        // reset animators
        mAnimationResetTopValue = new Animation();
        mAnimationResetBottomValue = new Animation();
        
        // menu animator
        mAnimationMenu = new Animation();
        mAnimationMenuRatio = 0;
        
        // fling for momentum scrolling
        mAnimationInterpolator = new DecelerateInterpolator(AnimationPreferences.FLING_DECELERATE_MULTIPLIER);
        mAnimationScroller = new Scroller(mContext);
        mAnimationScroller.setFriction(ViewConfiguration.getScrollFriction() * AnimationPreferences.FLING_FRICTION_MULTIPLIER);
        mAnimationFling = new Animation();
        
        // view-elements
        mBackgroundElement = backgroundElement;
        mTimeElement = timeElement;
        mEventElement = eventElement;
        mMenuElement = menuElement;
        
        // dimesnions of the view
        mViewHeightPixel = getHeight();
        mViewWidthPixel = getWidth();
        
        // screen density
        mScreenDensity = getResources().getDisplayMetrics().density;
        TemporaryPreferences.screenDensity = mScreenDensity;
        
        mTimeZoneUTC = TimeZone.getTimeZone("UTC");
        
        mCalendar = GregorianCalendar.getInstance(mTimeZoneUTC, Locale.getDefault());
        mCalendar.setTimeInMillis(System.currentTimeMillis());      
        mCalendar.set(Calendar.MILLISECOND, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        
        mCalendarToday = GregorianCalendar.getInstance(mTimeZoneUTC, Locale.getDefault());
        mCalendarToday.setTimeInMillis(System.currentTimeMillis());    
        mCalendarToday.set(Calendar.MILLISECOND, 0);
        mCalendarToday.set(Calendar.SECOND, 0);
        mCalendarToday.set(Calendar.MINUTE, 0);
        mCalendarToday.set(Calendar.HOUR_OF_DAY, 0);
        TemporaryPreferences.dayNowTopMillis = mCalendarToday.getTimeInMillis();
        TemporaryPreferences.yearNow = mCalendarToday.get(Calendar.YEAR);
        TemporaryPreferences.dayNow = mCalendarToday.get(Calendar.DAY_OF_YEAR);
        mCalendarToday.set(Calendar.HOUR_OF_DAY, 12);
        TemporaryPreferences.hour12NowMillis = mCalendarToday.getTimeInMillis();
        
        mCalendarHourHalf = GregorianCalendar.getInstance(mTimeZoneUTC, Locale.getDefault());
        mCalendarHour = GregorianCalendar.getInstance(mTimeZoneUTC, Locale.getDefault());
        mCalendarDay = GregorianCalendar.getInstance(mTimeZoneUTC, Locale.getDefault());
        mCalendarWeek = GregorianCalendar.getInstance(mTimeZoneUTC, Locale.getDefault());
        mCalendarMonth = GregorianCalendar.getInstance(mTimeZoneUTC, Locale.getDefault());
        mCalendarYear = GregorianCalendar.getInstance(mTimeZoneUTC, Locale.getDefault());
        
        mCalendarSelected = mCalendarToday;
        TemporaryPreferences.selectedDayInScalaMillis = mCalendarSelected.getTimeInMillis();
        TemporaryPreferences.selectedWeekDayInScalaIndex = mCalendarSelected.get(Calendar.DAY_OF_WEEK);
        
        mCalendarCreateEvent = GregorianCalendar.getInstance(mTimeZoneUTC, Locale.getDefault());
        
        // starting point if view created or changed
        if (selectedEventId != -1 && selectedEventBegin != -1) {
            mViewTopMillis = selectedEventBegin+(TimeZone.getDefault().getRawOffset()+TimeZone.getDefault().getDSTSavings())-(TimePreferences.DURATION_MILLIS_DAY_01/2);
            mViewBottomMillis = selectedEventBegin+(TimeZone.getDefault().getRawOffset()+TimeZone.getDefault().getDSTSavings())+(TimePreferences.DURATION_MILLIS_DAY_01/2);
            mViewHeightPixel = TemporaryPreferences.viewHeight;
            mViewWidthPixel = TemporaryPreferences.viewWidth;
        } else if (TemporaryPreferences.viewTopMillis > 0) {
            mViewTopMillis = TemporaryPreferences.viewTopMillis;
            mViewBottomMillis = TemporaryPreferences.viewBottomMillis;
            mViewHeightPixel = TemporaryPreferences.viewHeight;
            mViewWidthPixel = TemporaryPreferences.viewWidth;
        } else {
            mViewTopMillis = mCalendar.getTimeInMillis();
            mViewBottomMillis = mViewTopMillis + TimePreferences.DURATION_MILLIS_INITIAL;
        }
        mViewSpanMillis = mViewBottomMillis - mViewTopMillis;
        
        // initial touch mode
        mTouchMode = TOUCH_MODE_NONE;
        
    }

    /*
     * 
     *  SurfaceView
     *  
     */
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        
        // set view dimensions
        mViewHeightPixel = height;
        mViewWidthPixel = width;
        
        // save view dimensions
        TemporaryPreferences.viewHeight = height;
        TemporaryPreferences.viewWidth = width;
        
        // save today
        mCalendarToday.setTimeInMillis(System.currentTimeMillis());    
        mCalendarToday.set(Calendar.MILLISECOND, 0);
        mCalendarToday.set(Calendar.SECOND, 0);
        mCalendarToday.set(Calendar.MINUTE, 0);
        mCalendarToday.set(Calendar.HOUR_OF_DAY, 0);
        TemporaryPreferences.dayNowTopMillis = mCalendarToday.getTimeInMillis();
        TemporaryPreferences.yearNow = mCalendarToday.get(Calendar.YEAR);
        TemporaryPreferences.dayNow = mCalendarToday.get(Calendar.DAY_OF_YEAR);
        mCalendarToday.set(Calendar.HOUR_OF_DAY, 12);
        TemporaryPreferences.hour12NowMillis = mCalendarToday.getTimeInMillis();
       
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {       
        //nothing to do here
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {       
        //nothing to do here
    }

    /*
     * 
     *  Thread
     *  
     */
    
    @Override
    public void run() {
        while (mIsRunning) { 
            if (!mHolder.getSurface().isValid()) {
                continue;
            }
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                synchronized (mHolder) {
                    // calculates values for drawing
                    update();
                    // draw the view
                    render(mCanvas);
                    // stop thread if it's no animation
                    mIsRunning = isAnimation();
                  }
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }
    
    public void pause() {
        mIsRunning = false;
        while (true) {
            try {
                mThread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void resume() {
        mIsRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }
    
    /*
     * 
     *  Draw
     *  
     */
    
    private void update(){ 
        
        // calculate animation values
        animation();
        
        // save view properties
        TemporaryPreferences.viewTopMillis = mViewTopMillis;
        TemporaryPreferences.viewBottomMillis = mViewBottomMillis;
        
        // calculate multiplier to convert millis to pixel
        mViewMultiplierMillisToPixel = 1f * mViewHeightPixel / mViewSpanMillis;  
        
        // set calendar with millis of view-top
        mCalendar.setTimeInMillis(mViewTopMillis);
                
        // DELETE TODO
        if (!isGenerated) {
            VisualPreferences.generatePaintObjects(mViewHeightPixel, mViewWidthPixel, mScreenDensity, mContext);
            mMenuElement = new MenuElement();
            isGenerated = true;
        }
        
        
    }
    
    private boolean isAnimation() {
        return (mAnimationDoubleTapIsRunning || mAnimationFlingIsRunning || mAnimationMenuIsRunning || mAnimationTodayIsRunning || mAnimationResetIsRunning);
    }

    private void animation() {

        // calculate values for double-tap animation
        if (mTouchMode == TOUCH_MODE_TAP_DOUBLE && mAnimationDoubleTapIsRunning) {
            
            mAnimationDoubleTapTimeNow = System.currentTimeMillis();
            
            // check if animations are still running
            if (mAnimationDoubleTapTopValue.isRunning(mAnimationDoubleTapTimeNow) && mAnimationDoubleTapBottomValue.isRunning(mAnimationDoubleTapTimeNow)) {
                mViewTopMillis = mAnimationDoubleTapTopValue.getCurrentLongValue(mAnimationDoubleTapTimeNow);
                mViewBottomMillis = mAnimationDoubleTapBottomValue.getCurrentLongValue(mAnimationDoubleTapTimeNow);
                mViewSpanMillis = mViewBottomMillis - mViewTopMillis;
                mAnimationDoubleTapIsRunning = true;
            } else {
                mViewTopMillis = mAnimationDoubleTapTopValue.getCurrentLongValue(mAnimationDoubleTapTimeNow);
                mViewBottomMillis = mAnimationDoubleTapBottomValue.getCurrentLongValue(mAnimationDoubleTapTimeNow);
                mViewSpanMillis = mViewBottomMillis - mViewTopMillis;
                mAnimationDoubleTapIsRunning = false;
                mTouchMode = TOUCH_MODE_NONE;
            }
        }
        
        // calculate values for today animation
        if (mTouchMode == TOUCH_MODE_TAP && mAnimationTodayIsRunning) {
            
            mAnimationTodayTimeNow = System.currentTimeMillis();
            
            // check if animations are still running
            if (mAnimationTodayTopValue.isRunning(mAnimationTodayTimeNow) && mAnimationTodayBottomValue.isRunning(mAnimationTodayTimeNow)) {
                mViewTopMillis = mAnimationTodayTopValue.getCurrentLongValue(mAnimationTodayTimeNow);
                mViewBottomMillis = mAnimationTodayBottomValue.getCurrentLongValue(mAnimationTodayTimeNow);
                mViewSpanMillis = mViewBottomMillis - mViewTopMillis;
                mAnimationTodayIsRunning = true;
            } else {
                mViewTopMillis = mAnimationTodayTopValue.getCurrentLongValue(mAnimationTodayTimeNow);
                mViewBottomMillis = mAnimationTodayBottomValue.getCurrentLongValue(mAnimationTodayTimeNow);
                mViewSpanMillis = mViewBottomMillis - mViewTopMillis;
                mAnimationTodayIsRunning = false;
                mTouchMode = TOUCH_MODE_NONE;
            }
        }
        
        // calculate values for reset animation
        if (mTouchMode == TOUCH_MODE_TAP && mAnimationResetIsRunning) {
            
            mAnimationResetTimeNow = System.currentTimeMillis();
            
            // check if animations are still running
            if (mAnimationResetTopValue.isRunning(mAnimationResetTimeNow) && mAnimationResetBottomValue.isRunning(mAnimationResetTimeNow)) {
                mViewTopMillis = mAnimationResetTopValue.getCurrentLongValue(mAnimationResetTimeNow);
                mViewBottomMillis = mAnimationResetBottomValue.getCurrentLongValue(mAnimationResetTimeNow);
                mViewSpanMillis = mViewBottomMillis - mViewTopMillis;
                mAnimationResetIsRunning = true;
            } else {
                mViewTopMillis = mAnimationResetTopValue.getCurrentLongValue(mAnimationResetTimeNow);
                mViewBottomMillis = mAnimationResetBottomValue.getCurrentLongValue(mAnimationResetTimeNow);
                mViewSpanMillis = mViewBottomMillis - mViewTopMillis;
                mAnimationResetIsRunning = false;
                mTouchMode = TOUCH_MODE_NONE;
            }
        }
        
        // calculate values for fling animation
        if (mTouchMode == TOUCH_MODE_FLING && mAnimationFlingIsRunning) {

            mAnimationFlingCurrentMillis = System.currentTimeMillis(); 
            
            // check if animations are still running
            if (mAnimationFling.isRunning(mAnimationFlingCurrentMillis)) {
                
                // calculate offset
                mAnimationFlingCurrentVelocity = mAnimationFlingVelocity - (mAnimationFlingVelocity * mAnimationInterpolator.getInterpolation((float) mAnimationFling.getCurrentPercentageValue(mAnimationFlingCurrentMillis)));
                mAnimationFlingOffsetY = Math.round((mAnimationFlingCurrentVelocity/1000*mAnimationFling.getTimePassed(mAnimationFlingCurrentMillis)*mViewSpanMillis / mViewHeightPixel));    

                // set new view dimensions
                mViewTopMillis -= mAnimationFlingOffsetY;
                mViewBottomMillis -= mAnimationFlingOffsetY;
            
                mAnimationFlingIsRunning = true;
            } else {
                mAnimationFlingIsRunning = false;
                mTouchMode = TOUCH_MODE_NONE;
            }
      
        }        
                
    }
    
    private void renderEventsAllDay(Canvas canvas) {
        if (!TemporaryPreferences.calendarIds.isEmpty()) {
            mEventsAllDay = EventsHandler.getInstance().getEventsLongerDay(mViewTopMillis, mViewBottomMillis);
            for (Event event : mEventsAllDay) {
                mEventElement.drawEventAllDay(canvas, event, mViewTopMillis, mViewMultiplierMillisToPixel, event.arrangeableEventPositionAllDay);    
            }
        }
    }
    
    private void renderEventsDayGraph(Canvas canvas, long beginMillis, long endMillis, float offset, float height, int width) {
        if (!TemporaryPreferences.calendarIds.isEmpty()) {
            mEventsDayGraph = EventsHandler.getInstance().getEventsDayGraph(beginMillis, endMillis);
            for (Event event : mEventsDayGraph) {
                mEventElement.drawEventGraph(canvas, event, offset, height, beginMillis, endMillis);
            }
        }
    }
    
    private void renderEventsRow(Canvas canvas, long beginMillis, long endMillis, float offset, float height) {
        if (!TemporaryPreferences.calendarIds.isEmpty()) {
            mEventsRow.clear();
            mEventsRow = EventsHandler.getInstance().getEventsRow(beginMillis, endMillis);
            mEventsOnView.addAll(mEventElement.drawEventRow(canvas, mEventsRow, offset, height));
        }
    }

    private void renderEventsRowAllDay(Canvas canvas, long beginMillis, long endMillis, float offset, float height) {
        if (!TemporaryPreferences.calendarIds.isEmpty()) {
            mEventsRow.clear();
            mEventsRow = EventsHandler.getInstance().getEventsAllDay(beginMillis, endMillis);
            mEventsRow.addAll(EventsHandler.getInstance().getEventsRow(beginMillis, endMillis));
            mEventsOnView.addAll(mEventElement.drawEventRowAllDay(canvas, mEventsRow, offset, height));
        }
    }
    
    private void renderEventsExact(Canvas canvas, long beginMillis, long endMillis, float offset, float height, float multiplier) {
        if (!TemporaryPreferences.calendarIds.isEmpty()) {
            mEventsRow.clear();
            mEventsRow = EventsHandler.getInstance().getEventsAllDay(beginMillis, endMillis);
            mEventsRow.addAll(EventsHandler.getInstance().getEventsRow(beginMillis, endMillis));
            mEventsOnView.addAll(mEventElement.drawEventExact(canvas, mEventsRow, offset, height, beginMillis, multiplier));
        }
    }
    
    
    
    private void render(Canvas canvas) {
        
        mEventsOnView.clear();
        
        // clear visible days storage
        TemporaryPreferences.daysInScalaList.clear();
        
        // draw Background
        mBackgroundElement.drawBackground(canvas);
        
        // stage ratio
        mRenderStageRatio = (100f/(TimePreferences.DURATION_MILLIS_MAX-TimePreferences.DURATION_MILLIS_MIN)*(mViewSpanMillis));

        // Debug
        startTIME = System.currentTimeMillis();
        
        mPxOffset_HourHalf = TimePreferences.DURATION_MILLIS_HOUR_01 / 2 * mViewMultiplierMillisToPixel;
        mPxOffset_Hour = TimePreferences.DURATION_MILLIS_HOUR_01 * mViewMultiplierMillisToPixel;
        mPxOffset_Day = TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel;
        mPxOffset_Week = TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel;
        
        mCalendar.set(Calendar.MILLISECOND, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.add(Calendar.DAY_OF_YEAR, -1); 
        mCalendarHourHalf.setTimeInMillis(mCalendar.getTimeInMillis());
        mCalendarHour.setTimeInMillis(mCalendar.getTimeInMillis());
        mCalendarDay.setTimeInMillis(mCalendar.getTimeInMillis());
        pxOffsetOriginDay = Math.round((mCalendarDay.getTimeInMillis() - mViewTopMillis) * mViewMultiplierMillisToPixel);
        
        mCalendar.set(Calendar.DAY_OF_WEEK, 0);
        mCalendar.add(Calendar.WEEK_OF_YEAR, -1);
        mCalendar.add(Calendar.DAY_OF_YEAR, 2);
        mCalendarWeek.setTimeInMillis(mCalendar.getTimeInMillis());
        pxOffsetOriginWeek = Math.round((mCalendarWeek.getTimeInMillis() - mViewTopMillis) * mViewMultiplierMillisToPixel);
        
        mCalendar.set(Calendar.DAY_OF_MONTH, 0);
        mCalendar.add(Calendar.MONTH, -1);  
        mCalendarMonth.setTimeInMillis(mCalendar.getTimeInMillis());
        pxOffsetOriginMonth = Math.round((mCalendarMonth.getTimeInMillis() - mViewTopMillis) * mViewMultiplierMillisToPixel);
        
        mCalendar.set(Calendar.DAY_OF_YEAR, 0);
        mCalendar.add(Calendar.DAY_OF_YEAR, -1); 
        mCalendarYear.setTimeInMillis(mCalendar.getTimeInMillis());
        pxOffsetOriginYear = Math.round((mCalendarYear.getTimeInMillis() - mViewTopMillis) * mViewMultiplierMillisToPixel);
                
        EventsHandler.getInstance().getEventsSpan(mViewTopMillis-TimePreferences.DURATION_MILLIS_DAY_01, mViewBottomMillis+TimePreferences.DURATION_MILLIS_DAY_01);
        
        // 
        if (mRenderStageRatio < 0.15f) {
            for (mRenderIndex = pxOffsetOriginWeek;mRenderIndex < mViewHeightPixel;mRenderIndex+=mPxOffset_Week) {
                if (mCalendarWeek.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                    mTimeElement.drawWeekEvenElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                } else {
                    mTimeElement.drawWeekOddElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                }   
                mCalendarWeek.add(Calendar.WEEK_OF_YEAR, 1);
            }
            if (mViewTopMillis <= (TemporaryPreferences.dayNowTopMillis + TimePreferences.DURATION_MILLIS_DAY_01) && mViewBottomMillis >= TemporaryPreferences.dayNowTopMillis) {
                int pxOffsetToday = Math.round((TemporaryPreferences.dayNowTopMillis - mViewTopMillis) * mViewMultiplierMillisToPixel);
                mTimeElement.drawDayTodayElementBackground(canvas, pxOffsetToday, 0, pxOffsetToday + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
            }
            for (mRenderIndex = pxOffsetOriginDay;mRenderIndex < mViewHeightPixel;mRenderIndex+=mPxOffset_Day) {
                if (mCalendarDay.get(Calendar.DAY_OF_MONTH) != 1) {
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarDay.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarDay.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarDay.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_MONTH), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                    mTimeElement.drawWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                }
                renderEventsExact(canvas, mCalendarDay.getTimeInMillis(), mCalendarDay.getTimeInMillis()+TimePreferences.DURATION_MILLIS_DAY_01, mRenderIndex, mPxOffset_Day, mViewMultiplierMillisToPixel);
                mCalendarDay.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginMonth;mRenderIndex < mViewHeightPixel;mRenderIndex+=mPxOffset_Day) {
                if (mCalendarMonth.get(Calendar.DAY_OF_MONTH) == 1 && mCalendarMonth.get(Calendar.DAY_OF_YEAR) != 1) {
                    mTimeElement.drawFirstDayOfMonthElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarMonth.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarMonth));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarMonth.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarMonth));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarMonth.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawMonthElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.MONTH), 255, 1);   
                    mCalendarMonth.add(Calendar.DAY_OF_YEAR, 27);
                   mRenderIndex+=(mPxOffset_Day*27);
                }
                mCalendarMonth.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginYear;mRenderIndex < mViewHeightPixel;mRenderIndex+=mPxOffset_Day) {
                if (mCalendarYear.get(Calendar.DAY_OF_YEAR) == 1) {
                    mTimeElement.drawFirstDayOfYearElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarYear.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarYear));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarYear.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarYear));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarYear.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawYearElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.YEAR), 255, 1);
                    mCalendarYear.add(Calendar.DAY_OF_YEAR, 364);
                   mRenderIndex+=(mPxOffset_Day*364);
                }
                mCalendarYear.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginDay;mRenderIndex < mViewHeightPixel;mRenderIndex+=mPxOffset_HourHalf) {
                if (mCalendarHourHalf.get(Calendar.MINUTE) == 30) {
                    mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 0.5f, 255);    
                }
                mCalendarHourHalf.add(Calendar.MINUTE, 30);
            }
            for (mRenderIndex = pxOffsetOriginDay;mRenderIndex < mViewHeightPixel;mRenderIndex+=mPxOffset_Hour) {
                if (mCalendarHour.get(Calendar.HOUR_OF_DAY) != 0) {
                    mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255);
                    mTimeElement.drawHourElement(canvas, Math.round(mRenderIndex), mCalendarHour.get(Calendar.HOUR_OF_DAY), 255, 1);    
                }
                mCalendarHour.add(Calendar.HOUR_OF_DAY, 1);
            }
            
            
        //    
        } else if (mRenderStageRatio < 0.4f) {
            for (mRenderIndex = pxOffsetOriginWeek;mRenderIndex < mViewHeightPixel;mRenderIndex+=mPxOffset_Week) {
                if (mCalendarWeek.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                    mTimeElement.drawWeekEvenElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                } else {
                    mTimeElement.drawWeekOddElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                }   
                mCalendarWeek.add(Calendar.WEEK_OF_YEAR, 1);
            }
            if (mViewTopMillis <= (TemporaryPreferences.dayNowTopMillis + TimePreferences.DURATION_MILLIS_DAY_01) && mViewBottomMillis >= TemporaryPreferences.dayNowTopMillis) {
                int pxOffsetToday = Math.round((TemporaryPreferences.dayNowTopMillis - mViewTopMillis) * mViewMultiplierMillisToPixel);
                mTimeElement.drawDayTodayElementBackground(canvas, pxOffsetToday, 0, pxOffsetToday + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
            }
            for (mRenderIndex = pxOffsetOriginDay; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarDay.get(Calendar.DAY_OF_MONTH) != 1) {
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarDay.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarDay.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarDay.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_MONTH), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                    mTimeElement.drawWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                }
                mCalendarDay.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginMonth; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarMonth.get(Calendar.DAY_OF_MONTH) == 1 && mCalendarMonth.get(Calendar.DAY_OF_YEAR) != 1) {
                    mTimeElement.drawFirstDayOfMonthElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarMonth.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarMonth));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarMonth.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarMonth));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarMonth.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawMonthElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.MONTH), 255, 1);   
                    mCalendarMonth.add(Calendar.DAY_OF_YEAR, 27);
                    mRenderIndex+=(mPxOffset_Day*27);
                }
                mCalendarMonth.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginYear; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarYear.get(Calendar.DAY_OF_YEAR) == 1) {
                    mTimeElement.drawFirstDayOfYearElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarYear.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarYear));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarYear.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarYear));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarYear.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawYearElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.YEAR), 255, 1);
                    mCalendarYear.add(Calendar.DAY_OF_YEAR, 364);
                    mRenderIndex+=(mPxOffset_Day*364);
                }
                mCalendarYear.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginDay; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Hour) {
                if (mCalendarHour.get(Calendar.HOUR_OF_DAY) != 0) {
                    if (mCalendarHour.get(Calendar.HOUR_OF_DAY) % 3 == 0) {
                        mTimeElement.drawSeperatorEventsLight(canvas, Math.round(mRenderIndex), 1);
                        mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255);
                        mTimeElement.drawHourElement(canvas, Math.round(mRenderIndex), mCalendarHour.get(Calendar.HOUR_OF_DAY), 255, 1);
                        renderEventsRow(canvas, mCalendarHour.getTimeInMillis(), mCalendarHour.getTimeInMillis()+TimePreferences.DURATION_MILLIS_HOUR_03, mRenderIndex, mPxOffset_Hour*3);
                    }
                } else {
                    renderEventsRowAllDay(canvas, mCalendarHour.getTimeInMillis(), mCalendarHour.getTimeInMillis()+TimePreferences.DURATION_MILLIS_HOUR_03, mRenderIndex, mPxOffset_Hour*3);
                }
                mCalendarHour.add(Calendar.HOUR_OF_DAY, 1);
            }
            
            
        //    
        } else if (mRenderStageRatio < 0.8f) {
            for (mRenderIndex = pxOffsetOriginWeek; mRenderIndex <mViewHeightPixel; mRenderIndex+=mPxOffset_Week) {
                if (mCalendarWeek.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                    mTimeElement.drawWeekEvenElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                } else {
                    mTimeElement.drawWeekOddElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                }   
                mCalendarWeek.add(Calendar.WEEK_OF_YEAR, 1);
            }
            if (mViewTopMillis <= (TemporaryPreferences.dayNowTopMillis + TimePreferences.DURATION_MILLIS_DAY_01) && mViewBottomMillis >= TemporaryPreferences.dayNowTopMillis) {
                int pxOffsetToday = Math.round((TemporaryPreferences.dayNowTopMillis - mViewTopMillis) * mViewMultiplierMillisToPixel);
                mTimeElement.drawDayTodayElementBackground(canvas, pxOffsetToday, 0, pxOffsetToday + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
            }
            for (mRenderIndex = pxOffsetOriginDay; mRenderIndex <mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarDay.get(Calendar.DAY_OF_MONTH) != 1) {
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarDay.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarDay.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarDay.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_MONTH), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                    mTimeElement.drawWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                }
                mCalendarDay.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginMonth; mRenderIndex <mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarMonth.get(Calendar.DAY_OF_MONTH) == 1 && mCalendarMonth.get(Calendar.DAY_OF_YEAR) != 1) {
                    mTimeElement.drawFirstDayOfMonthElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarMonth.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarMonth));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarMonth.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarMonth));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarMonth.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawMonthElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.MONTH), 255, 1);   
                    mCalendarMonth.add(Calendar.DAY_OF_YEAR, 27);
                    mRenderIndex+=(mPxOffset_Day*27);
                }
                mCalendarMonth.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginYear; mRenderIndex <mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarYear.get(Calendar.DAY_OF_YEAR) == 1) {
                    mTimeElement.drawFirstDayOfYearElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarYear.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarYear));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarYear.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarYear));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarYear.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawYearElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.YEAR), 255, 1);
                    mCalendarYear.add(Calendar.DAY_OF_YEAR, 364);
                    mRenderIndex+=(mPxOffset_Day*364);
                }
                mCalendarYear.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginDay; mRenderIndex <mViewHeightPixel; mRenderIndex+=mPxOffset_Hour) {
                if (mCalendarHour.get(Calendar.HOUR_OF_DAY) != 0) {
                    if (mCalendarHour.get(Calendar.HOUR_OF_DAY) % 6 == 0) {
                        mTimeElement.drawSeperatorEventsLight(canvas, Math.round(mRenderIndex), 1);
                        mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255);
                        mTimeElement.drawHourElement(canvas, Math.round(mRenderIndex), mCalendarHour.get(Calendar.HOUR_OF_DAY), 255, 1);
                        renderEventsRow(canvas, mCalendarHour.getTimeInMillis(), mCalendarHour.getTimeInMillis()+TimePreferences.DURATION_MILLIS_HOUR_06, mRenderIndex, mPxOffset_Hour*6);
                    } else {
//                        mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 0.5f);
                    }
                } else {
                    renderEventsRowAllDay(canvas, mCalendarHour.getTimeInMillis(), mCalendarHour.getTimeInMillis()+TimePreferences.DURATION_MILLIS_HOUR_06, mRenderIndex, mPxOffset_Hour*6);
                }
                mCalendarHour.add(Calendar.HOUR_OF_DAY, 1);
            }
            
            
        //    
        } else if (mRenderStageRatio < 1.6f) {
            for (mRenderIndex = pxOffsetOriginWeek; mRenderIndex <mViewHeightPixel; mRenderIndex+=mPxOffset_Week) {
                if (mCalendarWeek.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                    mTimeElement.drawWeekEvenElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                } else {
                    mTimeElement.drawWeekOddElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                }   
                mCalendarWeek.add(Calendar.WEEK_OF_YEAR, 1);
            }
            if (mViewTopMillis <= (TemporaryPreferences.dayNowTopMillis + TimePreferences.DURATION_MILLIS_DAY_01) && mViewBottomMillis >= TemporaryPreferences.dayNowTopMillis) {
                int pxOffsetToday = Math.round((TemporaryPreferences.dayNowTopMillis - mViewTopMillis) * mViewMultiplierMillisToPixel);
                mTimeElement.drawDayTodayElementBackground(canvas, pxOffsetToday, 0, pxOffsetToday + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
            }
            for (mRenderIndex = pxOffsetOriginDay; mRenderIndex <mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarDay.get(Calendar.DAY_OF_MONTH) != 1) {
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarDay.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarDay.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarDay.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_MONTH), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                    mTimeElement.drawWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                }
                mCalendarDay.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginMonth; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarMonth.get(Calendar.DAY_OF_MONTH) == 1 && mCalendarMonth.get(Calendar.DAY_OF_YEAR) != 1) {
                    mTimeElement.drawFirstDayOfMonthElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarMonth.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarMonth));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarMonth.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarMonth));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarMonth.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawMonthElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.MONTH), 255, 1);   
                    mCalendarMonth.add(Calendar.DAY_OF_YEAR, 27);
                    mRenderIndex+=(mPxOffset_Day*27);
                }
                mCalendarMonth.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (float mRenderIndex = pxOffsetOriginYear; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarYear.get(Calendar.DAY_OF_YEAR) == 1) {
                    mTimeElement.drawFirstDayOfYearElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarYear.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarYear));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarYear.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarYear));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarYear.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawYearElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.YEAR), 255, 1);
                    mCalendarYear.add(Calendar.DAY_OF_YEAR, 364);
                    mRenderIndex+=(mPxOffset_Day*364);
                }
                mCalendarYear.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginDay; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Hour) {
                if (mCalendarHour.get(Calendar.HOUR_OF_DAY) != 0) {
                    if (mCalendarHour.get(Calendar.HOUR_OF_DAY) % 12 == 0) {
                        mTimeElement.drawSeperatorEventsLight(canvas, Math.round(mRenderIndex), 1);
                        mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255);
                        mTimeElement.drawHourElement(canvas, Math.round(mRenderIndex), mCalendarHour.get(Calendar.HOUR_OF_DAY), 255, 1);
                        renderEventsRow(canvas, mCalendarHour.getTimeInMillis(), mCalendarHour.getTimeInMillis()+TimePreferences.DURATION_MILLIS_HOUR_12, mRenderIndex, mPxOffset_Hour*12);
                    } else if (mCalendarHour.get(Calendar.HOUR_OF_DAY) % 6 == 0) {
//                        mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f);
                    }
                } else {
                    renderEventsRowAllDay(canvas, mCalendarHour.getTimeInMillis(), mCalendarHour.getTimeInMillis()+TimePreferences.DURATION_MILLIS_HOUR_12, mRenderIndex, mPxOffset_Hour*12);
                }
                mCalendarHour.add(Calendar.HOUR_OF_DAY, 1);
            }
            
            
        //
        } else if (mRenderStageRatio < 4f) {
            for (mRenderIndex = pxOffsetOriginWeek; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Week) {
                if (mCalendarWeek.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                    mTimeElement.drawWeekEvenElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                } else {
                    mTimeElement.drawWeekOddElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                }   
                mCalendarWeek.add(Calendar.WEEK_OF_YEAR, 1);
            }
            if (mViewTopMillis <= (TemporaryPreferences.dayNowTopMillis + TimePreferences.DURATION_MILLIS_DAY_01) && mViewBottomMillis >= TemporaryPreferences.dayNowTopMillis) {
                int pxOffsetToday = Math.round((TemporaryPreferences.dayNowTopMillis - mViewTopMillis) * mViewMultiplierMillisToPixel);
                mTimeElement.drawDayTodayElementBackground(canvas, pxOffsetToday, 0, pxOffsetToday + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
            }
            for (mRenderIndex = pxOffsetOriginDay; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarDay.get(Calendar.DAY_OF_MONTH) != 1) {
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarDay.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarDay.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarDay.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_MONTH), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                    mTimeElement.drawWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                }
                renderEventsRowAllDay(canvas, mCalendarDay.getTimeInMillis(), mCalendarDay.getTimeInMillis()+TimePreferences.DURATION_MILLIS_DAY_01, mRenderIndex, mPxOffset_Day);
                mCalendarDay.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginMonth; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarMonth.get(Calendar.DAY_OF_MONTH) == 1 && mCalendarMonth.get(Calendar.DAY_OF_YEAR) != 1) {
                    mTimeElement.drawFirstDayOfMonthElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarMonth.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarMonth));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarMonth.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarMonth));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarMonth.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawMonthElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.MONTH), 255, 1);   
                    mCalendarMonth.add(Calendar.DAY_OF_YEAR, 27);
                    mRenderIndex+=(mPxOffset_Day*27);
                }
                mCalendarMonth.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginYear; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarYear.get(Calendar.DAY_OF_YEAR) == 1) {
                    mTimeElement.drawFirstDayOfYearElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarYear.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarYear));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarYear.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarYear));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarYear.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawYearElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.YEAR), 255, 1);
                    mCalendarYear.add(Calendar.DAY_OF_YEAR, 364);
                    mRenderIndex+=(mPxOffset_Day*364);
                }
                mCalendarYear.add(Calendar.DAY_OF_YEAR, 1);
            }
            
        
        //
        } else if (mRenderStageRatio < 5.5f) {
            for (mRenderIndex = pxOffsetOriginWeek; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Week) {
                if (mCalendarWeek.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                    mTimeElement.drawWeekEvenElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                } else {
                    mTimeElement.drawWeekOddElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                }   
                mCalendarWeek.add(Calendar.WEEK_OF_YEAR, 1);
            }
            if (mViewTopMillis <= (TemporaryPreferences.dayNowTopMillis + TimePreferences.DURATION_MILLIS_DAY_01) && mViewBottomMillis >= TemporaryPreferences.dayNowTopMillis) {
                int pxOffsetToday = Math.round((TemporaryPreferences.dayNowTopMillis - mViewTopMillis) * mViewMultiplierMillisToPixel);
                mTimeElement.drawDayTodayElementBackground(canvas, pxOffsetToday, 0, pxOffsetToday + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
            }
            for (mRenderIndex = pxOffsetOriginDay; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarDay.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                    mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarDay.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                    mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                }
                TemporaryPreferences.daysInScalaList.add(mCalendarDay.getTimeInMillis());
                if (mCalendarDay.get(Calendar.DAY_OF_MONTH) != 1) { 
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);    
                      if (mCalendarDay.get(Calendar.DAY_OF_MONTH) % 2 != 0){ 
                          mTimeElement.drawDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_MONTH), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255-Math.round((255/100)*(100/(5.5f-4f)*(mRenderStageRatio-4f))), 1);
                          mTimeElement.drawWeekDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255-Math.round((255/100)*(100/(5.5f-4f)*(mRenderStageRatio-4f))), 1);
                      } else {
                          mTimeElement.drawDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_MONTH), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                          mTimeElement.drawWeekDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, 1);
                      }
                    }
                renderEventsDayGraph(canvas, mCalendarDay.getTimeInMillis(), mCalendarDay.getTimeInMillis()+TimePreferences.DURATION_MILLIS_DAY_01, mRenderIndex, mPxOffset_Day, 800);
                mCalendarDay.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginMonth; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarMonth.get(Calendar.DAY_OF_MONTH) == 1 && mCalendarMonth.get(Calendar.DAY_OF_YEAR) != 1) {
                    mTimeElement.drawFirstDayOfMonthElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL, mViewWidthPixel, 255);
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarDay.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarDay.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarMonth.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawMonthElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.MONTH), 255, 1);   
                    mCalendarMonth.add(Calendar.DAY_OF_YEAR, 27);
                    mRenderIndex+=(mPxOffset_Day*27);
                }
                mCalendarMonth.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginYear; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarYear.get(Calendar.DAY_OF_YEAR) == 1) {
                    mTimeElement.drawFirstDayOfYearElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL, mViewWidthPixel, 255);
                    if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarYear.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarYear));
                    } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarYear.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                        mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarYear));
                    }
                    TemporaryPreferences.daysInScalaList.add(mCalendarYear.getTimeInMillis());
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawYearElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.YEAR), 255, 1);
                    mCalendarYear.add(Calendar.DAY_OF_YEAR, 364);
                    mRenderIndex+=(mPxOffset_Day*364);
                }
                mCalendarYear.add(Calendar.DAY_OF_YEAR, 1);
            }
            renderEventsAllDay(canvas);
            
            
            
            
        //
        } else if (mRenderStageRatio < 12f) {
            for (mRenderIndex = pxOffsetOriginWeek; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Week) {
                if (mCalendarWeek.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                    mTimeElement.drawWeekEvenElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                } else {
                    mTimeElement.drawWeekOddElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                }   
                mCalendarWeek.add(Calendar.WEEK_OF_YEAR, 1);
            }
            if (mViewTopMillis <= (TemporaryPreferences.dayNowTopMillis + TimePreferences.DURATION_MILLIS_DAY_01) && mViewBottomMillis >= TemporaryPreferences.dayNowTopMillis) {
                int pxOffsetToday = Math.round((TemporaryPreferences.dayNowTopMillis - mViewTopMillis) * mViewMultiplierMillisToPixel);
                mTimeElement.drawDayTodayElementBackground(canvas, pxOffsetToday, 0, pxOffsetToday + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
            }
            for (mRenderIndex = pxOffsetOriginDay; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarDay.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                    mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarDay.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                    mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                }
                if (mCalendarDay.get(Calendar.DAY_OF_MONTH) != 1 && mCalendarDay.get(Calendar.DAY_OF_MONTH) % 2 == 0 && mCalendarDay.get(Calendar.DAY_OF_MONTH) % 4 != 0) {
                    mTimeElement.drawSeperatorEvents(canvas, Math.round(mRenderIndex), 255-Math.round((255/100)*(100/(22-5.5f)*(mRenderStageRatio-5.5f))));
                    mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255);
                    mTimeElement.drawDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_MONTH), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255-Math.round((255/100)*(100/(12-5.5f)*(mRenderStageRatio-5.5f))), 1);
                    mTimeElement.drawWeekDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255-Math.round((255/100)*(100/(12-5.5f)*(mRenderStageRatio-5.5f))), 1);
                } else if (mCalendarDay.get(Calendar.DAY_OF_MONTH) != 1 && !(mCalendarDay.get(Calendar.DAY_OF_MONTH) % 2 != 0)) {
                    mTimeElement.drawSeperatorEvents(canvas, Math.round(mRenderIndex), 255-Math.round((255/100)*(100/(22-5.5f)*(mRenderStageRatio-5.5f))));
                    mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255);
                    mTimeElement.drawDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_MONTH), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                    mTimeElement.drawWeekDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, 1);
                } else {
                    mTimeElement.drawSeperatorEvents(canvas, Math.round(mRenderIndex), 255-Math.round((255/100)*(100/(22-5.5f)*(mRenderStageRatio-5.5f))));
                    mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255);
                }
                renderEventsDayGraph(canvas, mCalendarDay.getTimeInMillis(), mCalendarDay.getTimeInMillis()+TimePreferences.DURATION_MILLIS_DAY_01, mRenderIndex, mPxOffset_Day, 800);
                mCalendarDay.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginMonth; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarMonth.get(Calendar.DAY_OF_MONTH) == 1 && mCalendarMonth.get(Calendar.DAY_OF_YEAR) != 1) {
                    mTimeElement.drawFirstDayOfMonthElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL, mViewWidthPixel, 255);
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawMonthElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.MONTH), 255, 1);   
                    mCalendarMonth.add(Calendar.DAY_OF_YEAR, 27);
                    mRenderIndex+=(mPxOffset_Day*27);
                }
                mCalendarMonth.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginYear; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarYear.get(Calendar.DAY_OF_YEAR) == 1) {
                    mTimeElement.drawFirstDayOfYearElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL, mViewWidthPixel, 255);
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawYearElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.YEAR), 255, 1);
                    mCalendarYear.add(Calendar.DAY_OF_YEAR, 364);
                    mRenderIndex+=(mPxOffset_Day*364);
                }
                mCalendarYear.add(Calendar.DAY_OF_YEAR, 1);
            }
            renderEventsAllDay(canvas);
            
            
        //    
        } else if (mRenderStageRatio < 22f) {
            for (mRenderIndex = pxOffsetOriginWeek; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Week) {
                if (mCalendarWeek.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                    mTimeElement.drawWeekEvenElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                } else {
                    mTimeElement.drawWeekOddElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                }   
                mCalendarWeek.add(Calendar.WEEK_OF_YEAR, 1);
            }
            if (mViewTopMillis <= (TemporaryPreferences.dayNowTopMillis + TimePreferences.DURATION_MILLIS_DAY_01) && mViewBottomMillis >= TemporaryPreferences.dayNowTopMillis) {
                int pxOffsetToday = Math.round((TemporaryPreferences.dayNowTopMillis - mViewTopMillis) * mViewMultiplierMillisToPixel);
                mTimeElement.drawDayTodayElementBackground(canvas, pxOffsetToday, 0, pxOffsetToday + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
            }
            for (mRenderIndex = pxOffsetOriginDay; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarDay.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                    mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarDay.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                    mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                }
                if (mCalendarDay.get(Calendar.DAY_OF_MONTH) != 1 && mCalendarDay.get(Calendar.DAY_OF_MONTH) % 2 == 0 && mCalendarDay.get(Calendar.DAY_OF_MONTH) % 4 == 0 && mCalendarDay.get(Calendar.DAY_OF_MONTH) % 8 != 0) {
                    mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255);
                    mTimeElement.drawDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_MONTH), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255-Math.round((255/100)*(100/(22-12f)*(mRenderStageRatio-12f))), 1);
                    mTimeElement.drawWeekDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255-Math.round((255/100)*(100/(22-12f)*(mRenderStageRatio-12f))), 1);
                } else if (mCalendarDay.get(Calendar.DAY_OF_MONTH) != 1 && !(mCalendarDay.get(Calendar.DAY_OF_MONTH) % 2 != 0) && !(mCalendarDay.get(Calendar.DAY_OF_MONTH) % 4 != 0)) {
                    mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255);
                    mTimeElement.drawDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_MONTH), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                    mTimeElement.drawWeekDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, 1);
                } else {
                    mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255);
                }
                mTimeElement.drawSeperatorEvents(canvas, Math.round(mRenderIndex), 255-Math.round((255/100)*(100/(22-5.5f)*(mRenderStageRatio-5.5f))));
                renderEventsDayGraph(canvas, mCalendarDay.getTimeInMillis(), mCalendarDay.getTimeInMillis()+TimePreferences.DURATION_MILLIS_DAY_01, mRenderIndex, mPxOffset_Day, 800);
                mCalendarDay.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginMonth; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarMonth.get(Calendar.DAY_OF_MONTH) == 1 && mCalendarMonth.get(Calendar.DAY_OF_YEAR) != 1) {
                    mTimeElement.drawFirstDayOfMonthElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL, mViewWidthPixel, 255);
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawMonthElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.MONTH), 255, 1);   
                    mCalendarMonth.add(Calendar.DAY_OF_YEAR, 27);
                    mRenderIndex+=(mPxOffset_Day*27);
                }
                mCalendarMonth.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginYear; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarYear.get(Calendar.DAY_OF_YEAR) == 1) {
                    mTimeElement.drawFirstDayOfYearElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL, mViewWidthPixel, 255);
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawYearElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.YEAR), 255, 1);
                    mCalendarYear.add(Calendar.DAY_OF_YEAR, 364);
                    mRenderIndex+=(mPxOffset_Day*364);
                }
                mCalendarYear.add(Calendar.DAY_OF_YEAR, 1);
            }
            renderEventsAllDay(canvas);
            
         
        //    
        } else if (mRenderStageRatio < 50f) {
            for (mRenderIndex = pxOffsetOriginWeek; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Week) {
                if (mCalendarWeek.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                    mTimeElement.drawWeekEvenElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                } else {
                    mTimeElement.drawWeekOddElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                }   
                mCalendarWeek.add(Calendar.WEEK_OF_YEAR, 1);
            }
            if (mViewTopMillis <= (TemporaryPreferences.dayNowTopMillis + TimePreferences.DURATION_MILLIS_DAY_01) && mViewBottomMillis >= TemporaryPreferences.dayNowTopMillis) {
                int pxOffsetToday = Math.round((TemporaryPreferences.dayNowTopMillis - mViewTopMillis) * mViewMultiplierMillisToPixel);
                mTimeElement.drawDayTodayElementBackground(canvas, pxOffsetToday, 0, pxOffsetToday + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
            }
            for (mRenderIndex = pxOffsetOriginDay; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarDay.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                    mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarDay.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                    mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                }
                if (mCalendarDay.get(Calendar.DAY_OF_MONTH) != 1 && mCalendarDay.get(Calendar.DAY_OF_MONTH) % 8 == 0) {
                    mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255);
                    mTimeElement.drawDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_MONTH), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255-Math.round((255/100)*(100/(50-22f)*(mRenderStageRatio-22f))), 1);
                    mTimeElement.drawWeekDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255-Math.round((255/100)*(100/(50-22f)*(mRenderStageRatio-22f))), 1);
                } else if (mCalendarDay.get(Calendar.DAY_OF_MONTH) != 1 && !(mCalendarDay.get(Calendar.DAY_OF_MONTH) % 2 != 0) && !(mCalendarDay.get(Calendar.DAY_OF_MONTH) % 4 != 0) && !(mCalendarDay.get(Calendar.DAY_OF_MONTH) % 8 != 0)) {
                    mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255);
                    mTimeElement.drawDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_MONTH), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), mCalendarDay.get(Calendar.DAY_OF_WEEK), 255, 1);
                    mTimeElement.drawWeekDayMiddleElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, 1);
                } else {
                    mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255);
                }

                renderEventsDayGraph(canvas, mCalendarDay.getTimeInMillis(), mCalendarDay.getTimeInMillis()+TimePreferences.DURATION_MILLIS_DAY_01, mRenderIndex, mPxOffset_Day, 800);
                mCalendarDay.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginMonth; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarMonth.get(Calendar.DAY_OF_MONTH) == 1 && mCalendarMonth.get(Calendar.DAY_OF_YEAR) != 1) {
                    mTimeElement.drawFirstDayOfMonthElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL, mViewWidthPixel, 255);
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawMonthElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.MONTH), 255, 1);   
                    mCalendarMonth.add(Calendar.DAY_OF_YEAR, 27);
                    mRenderIndex+=(mPxOffset_Day*27);
                }
                mCalendarMonth.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginYear; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarYear.get(Calendar.DAY_OF_YEAR) == 1) {
                    mTimeElement.drawFirstDayOfYearElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL, mViewWidthPixel, 255);
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawYearElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.YEAR), 255, 1);
                    mCalendarYear.add(Calendar.DAY_OF_YEAR, 364);
                    mRenderIndex+=(mPxOffset_Day*364);
                }
                mCalendarYear.add(Calendar.DAY_OF_YEAR, 1);
            }
            renderEventsAllDay(canvas);
            
         
        //    
        } else if (mRenderStageRatio < 60f) {
            for (mRenderIndex = pxOffsetOriginWeek; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Week) {
                if (mCalendarWeek.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                    mTimeElement.drawWeekEvenElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                } else {
                    mTimeElement.drawWeekOddElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                }   
                mCalendarWeek.add(Calendar.WEEK_OF_YEAR, 1);
            }
            if (mViewTopMillis <= (TemporaryPreferences.dayNowTopMillis + TimePreferences.DURATION_MILLIS_DAY_01) && mViewBottomMillis >= TemporaryPreferences.dayNowTopMillis) {
                int pxOffsetToday = Math.round((TemporaryPreferences.dayNowTopMillis - mViewTopMillis) * mViewMultiplierMillisToPixel);
                mTimeElement.drawDayTodayElementBackground(canvas, pxOffsetToday, 0, pxOffsetToday + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
            }
            for (mRenderIndex = pxOffsetOriginDay; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarDay.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                    mTimeElement.drawSelectedDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                } else if (TemporaryPreferences.selectedWeekDayInScalaIndex == mCalendarDay.get(Calendar.DAY_OF_WEEK) && TemporaryPreferences.selectedDayIsSelected) {
                    mTimeElement.drawSelectedWeekDayElement(canvas, Math.round(mRenderIndex), mCalendarDay.get(Calendar.DAY_OF_WEEK), (float) (TimePreferences.DURATION_MILLIS_DAY_01*mViewMultiplierMillisToPixel), 255, isToday(mCalendarDay));
                }
                mTimeElement.drawScalaLine(canvas, Math.round(mRenderIndex), 1f, 255-Math.round((255/100)*(100/(60-50f)*(mRenderStageRatio-50f))));
                renderEventsDayGraph(canvas, mCalendarDay.getTimeInMillis(), mCalendarDay.getTimeInMillis()+TimePreferences.DURATION_MILLIS_DAY_01, mRenderIndex, mPxOffset_Day, 800);
                mCalendarDay.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginMonth; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarMonth.get(Calendar.DAY_OF_MONTH) == 1 && mCalendarMonth.get(Calendar.DAY_OF_YEAR) != 1) {
                    mTimeElement.drawFirstDayOfMonthElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL, mViewWidthPixel, 255);
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawMonthElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.MONTH), 255, 1);   
                    mCalendarMonth.add(Calendar.DAY_OF_YEAR, 27);
                    mRenderIndex+=(mPxOffset_Day*27);
                }
                mCalendarMonth.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginYear; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarYear.get(Calendar.DAY_OF_YEAR) == 1) {
                    mTimeElement.drawFirstDayOfYearElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL, mViewWidthPixel, 255);
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawYearElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.YEAR), 255, 1);
                    mCalendarYear.add(Calendar.DAY_OF_YEAR, 364);
                    mRenderIndex+=(mPxOffset_Day*364);
                }
                mCalendarYear.add(Calendar.DAY_OF_YEAR, 1);
            }
            renderEventsAllDay(canvas);
            
         
        //    
        } else {
            for (mRenderIndex = pxOffsetOriginWeek; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Week) {
                if (mCalendarWeek.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                    mTimeElement.drawWeekEvenElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                } else {
                    mTimeElement.drawWeekOddElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + Math.round(TimePreferences.DURATION_MILLIS_WEEK_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
                }   
                mCalendarWeek.add(Calendar.WEEK_OF_YEAR, 1);
            }
            if (mViewTopMillis <= (TemporaryPreferences.dayNowTopMillis + TimePreferences.DURATION_MILLIS_DAY_01) && mViewBottomMillis >= TemporaryPreferences.dayNowTopMillis) {
                int pxOffsetToday = Math.round((TemporaryPreferences.dayNowTopMillis - mViewTopMillis) * mViewMultiplierMillisToPixel);
                mTimeElement.drawDayTodayElementBackground(canvas, pxOffsetToday, 0, pxOffsetToday + (int) (TimePreferences.DURATION_MILLIS_DAY_01 * mViewMultiplierMillisToPixel), mViewWidthPixel, 255);
            }
            for (mRenderIndex = pxOffsetOriginMonth; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarMonth.get(Calendar.DAY_OF_MONTH) == 1 && mCalendarMonth.get(Calendar.DAY_OF_YEAR) != 1) {
                    mTimeElement.drawFirstDayOfMonthElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL, mViewWidthPixel, 255);
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawMonthElement(canvas, Math.round(mRenderIndex), mCalendarMonth.get(Calendar.MONTH), 255, 1);   
                    mCalendarMonth.add(Calendar.DAY_OF_YEAR, 27);
                    mRenderIndex+=(mPxOffset_Day*27);
                }
                mCalendarMonth.add(Calendar.DAY_OF_YEAR, 1);
            }
            for (mRenderIndex = pxOffsetOriginYear; mRenderIndex < mViewHeightPixel; mRenderIndex+=mPxOffset_Day) {
                if (mCalendarYear.get(Calendar.DAY_OF_YEAR) == 1) {
                    mTimeElement.drawFirstDayOfYearElementBackground(canvas, Math.round(mRenderIndex), 0, Math.round(mRenderIndex) + VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL, mViewWidthPixel, 255);
                    mTimeElement.drawSeperatorFull(canvas, Math.round(mRenderIndex), 1f);
                    mTimeElement.drawYearElement(canvas, Math.round(mRenderIndex), mCalendarYear.get(Calendar.YEAR), 255, 1);
                    mCalendarYear.add(Calendar.DAY_OF_YEAR, 364);
                    mRenderIndex+=(mPxOffset_Day*364);
                }
                mCalendarYear.add(Calendar.DAY_OF_YEAR, 1);
            }
            renderEventsAllDay(canvas);
        }
        

        
        if (TemporaryPreferences.visibleEventDetails != null && TemporaryPreferences.isVisibleEventDetails) {
            for (EventViewElement eventTmp : mEventsOnView) {
                if (TemporaryPreferences.eventSelected.unique == eventTmp.unique) {
                    TemporaryPreferences.eventSelected = eventTmp;
                }
                if (TemporaryPreferences.visibleEventDetails.unique == eventTmp.unique) {
                    TemporaryPreferences.visibleEventDetails = eventTmp;
                    mEventElement.drawEventDetail(canvas, TemporaryPreferences.visibleEventDetails, mViewTopMillis, mViewBottomMillis, mViewMultiplierMillisToPixel, mContext);
                    break;
                }
            }
        }
        
       if (VisualPreferences.VIEW_INFORMATION) {
           Paint p = new Paint(); 
           p.setColor(Color.BLACK);
           p.setTextSize(10*mScreenDensity);
           canvas.drawRect(mViewWidthPixel-(80*mScreenDensity), mViewHeightPixel-(15*mScreenDensity), mViewWidthPixel, mViewHeightPixel, p);
           p.setColor(Color.RED);
           canvas.drawText(String.format("%05.2f", mRenderStageRatio)+"% | "+(System.currentTimeMillis()-startTIME)+"ms", mViewWidthPixel-(75*mScreenDensity), mViewHeightPixel-(4*mScreenDensity), p);           
       }

       if (mAnimationMenu.isRunning(System.currentTimeMillis())) {
            mAnimationMenuRatio = (float) mAnimationMenu.getCurrentPercentageValue(System.currentTimeMillis());
        } else {     
            if (mAnimationMenuRatio > 0.5) {
                mAnimationMenuRatio = 1;
            } else {
                mAnimationMenuRatio = 0;
            }
            mAnimationMenuIsRunning = false;
        } 
        
        MenuElement.drawMainMenuSub(canvas, mViewHeightPixel, mViewWidthPixel, mScreenDensity, mViewTopMillis+(mViewSpanMillis/2), mAnimationMenuRatio);
        MenuElement.drawMainMenu(canvas, mViewHeightPixel, mViewWidthPixel, mViewTopMillis+(mViewSpanMillis/2),mAnimationMenuRatio);
        
    }

    /*
     * 
     *  Misc
     *  
     */
    
    private boolean isToday(Calendar calendar) {
        return (calendar.get(Calendar.DAY_OF_YEAR) == TemporaryPreferences.dayNow && calendar.get(Calendar.YEAR) == TemporaryPreferences.yearNow);
    }
    
    private long getMillisFromPixel(double pixelPosition_Y) {
        return (long) (((pixelPosition_Y / mViewHeightPixel) * mViewSpanMillis) + mViewTopMillis);
    }
    
    /*
     * 
     *  Touch-Events
     *  
     */
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
          
        mTouchEventPointerCount = event.getPointerCount();

        if (mTouchEventPointerCount>1 && mTouchMode != TOUCH_MODE_TAP_DOUBLE) {
            // check for scale gesture
            mScaleGestureDetector.onTouchEvent(event);
        }
        
        // pass non-scale event to non-scale gesture detector
        if (mTouchMode != TOUCH_MODE_SCALE) {
            mGestureDetector.onTouchEvent(event);
        }
    
        return true;
    }
    
    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        if (mTouchMode == TOUCH_MODE_SCALE) {

            // set multiplier for scale 
            mViewScaleStageMultiplier = AnimationPreferences.SCALE_MULTIPLIER;
           
            // calculate delta and position
            mViewScaleSpanDeltaMillis = (mViewSpanMillis * (1d / mViewHeightPixel * (detector.getCurrentSpanY()-detector.getPreviousSpanY()))) * mViewScaleStageMultiplier;
            mViewScaleSpanFocalPointPercentage = 1d / mViewHeightPixel * detector.getFocusY();

            // calculate new view dimensions
            mViewScaleTempTopMillis = mViewTopMillis + Math.round(mViewScaleSpanDeltaMillis*mViewScaleSpanFocalPointPercentage);
            mViewScaleTempBottomMillis = mViewBottomMillis - Math.round(mViewScaleSpanDeltaMillis*(1-mViewScaleSpanFocalPointPercentage));
            mViewScaleTempSpanMillis = mViewScaleTempBottomMillis - mViewScaleTempTopMillis;
            
            // if in range set new view dimensions
            if (mViewScaleTempSpanMillis >= TimePreferences.DURATION_MILLIS_MIN &&
                mViewScaleTempSpanMillis <= TimePreferences.DURATION_MILLIS_MAX) {
                mViewSpanMillis = (long) mViewScaleTempSpanMillis;
                mViewTopMillis = (long) mViewScaleTempTopMillis;
                mViewBottomMillis = (long) mViewScaleTempBottomMillis;
                          
                // run thread
                mIsRunning = true;
                mThread.run();
            
            }
            
            return true;    
            
        } else {
            return false;
        }
        
        
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        // set touch-mode
        mTouchMode = TOUCH_MODE_SCALE;            
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        // set touch-mode
        mTouchMode = TOUCH_MODE_NONE;
    }
    
    class CalendarGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            // check if spread is possible and it is no menu tap
            if (mMenuElement.getMenuAction(e.getX(), e.getY()) == MenuAction.None && mViewSpanMillis >= TimePreferences.DURATION_MILLIS_MIN && !mAnimationDoubleTapIsRunning && !mAnimationTodayIsRunning && !mAnimationResetIsRunning) {
                if (mViewSpanMillis > TimePreferences.DURATION_MILLIS_MIN) {
                    // check if desired span is in stage range
                    if ((mViewSpanMillis*AnimationPreferences.DOUBLE_TAP_SPREAD_MULTIPLIER) > TimePreferences.DURATION_MILLIS_MIN) {
                        mAnimationDoubleTapMillisTopEnd = (long) (getMillisFromPixel(e.getY())-(mViewSpanMillis*AnimationPreferences.DOUBLE_TAP_SPREAD_MULTIPLIER/2));
                        mAnimationDoubleTapMillisBottomEnd = (long) (getMillisFromPixel(e.getY())+(mViewSpanMillis*AnimationPreferences.DOUBLE_TAP_SPREAD_MULTIPLIER/2));
                    } else {
                        mAnimationDoubleTapMillisTopEnd = getMillisFromPixel(e.getY())-(TimePreferences.DURATION_MILLIS_MIN/2);
                        mAnimationDoubleTapMillisBottomEnd = mAnimationDoubleTapMillisTopEnd + TimePreferences.DURATION_MILLIS_MIN;
                    }
                } else {
                    mAnimationDoubleTapMillisTopEnd = (long) (getMillisFromPixel(e.getY())-((TimePreferences.DURATION_MILLIS_MIN*AnimationPreferences.DOUBLE_TAP_PINCH_MULTIPLIER)/2));
                    mAnimationDoubleTapMillisBottomEnd = (long) (getMillisFromPixel(e.getY())+((TimePreferences.DURATION_MILLIS_MIN*AnimationPreferences.DOUBLE_TAP_PINCH_MULTIPLIER)/2));                    
                }
                
                // create animations
                mAnimationDoubleTapTopValue.prepareAnimation(mViewTopMillis, mAnimationDoubleTapMillisTopEnd, AnimationPreferences.DOUBLE_TAP_ANIMATION_DURATION, false);
                mAnimationDoubleTapBottomValue.prepareAnimation(mViewBottomMillis, mAnimationDoubleTapMillisBottomEnd, AnimationPreferences.DOUBLE_TAP_ANIMATION_DURATION, false);
                
                // start animations
                mAnimationDoubleTapTimeStart = System.currentTimeMillis();
                mAnimationDoubleTapTopValue.start(mAnimationDoubleTapTimeStart);
                mAnimationDoubleTapBottomValue.start(mAnimationDoubleTapTimeStart);
                mAnimationDoubleTapIsRunning = true;
                
                // set touch-mode
                mTouchMode = TOUCH_MODE_TAP_DOUBLE;

                // run thread
                mIsRunning = true;
                mThread.run();
                
            }
          
            return true;
        }
        
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // let scroller calculate values for animation
            mAnimationScroller.fling(0, (int) e2.getY(), 0, (int) (velocityY * AnimationPreferences.FLING_VELOCITY_CORE_MULTIPLIER), 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            
            // prpare animation
            mAnimationFlingVelocity = velocityY * AnimationPreferences.FLING_VELOCITY_ANIMATION_MULTIPLIER;
            mAnimationFlingStartY = mAnimationScroller.getStartY();
            mAnimationFlingDuration = mAnimationScroller.getDuration() * AnimationPreferences.FLING_DURATION_MULTIPLIER;
            mAnimationFlingEndY = mAnimationScroller.getFinalY();
            mAnimationFling.prepareAnimation(mAnimationFlingStartY, mAnimationFlingEndY, mAnimationFlingDuration, false);
            
            // start animation
            mAnimationFling.start(System.currentTimeMillis());
            mAnimationFlingIsRunning = true;
            
            // set touch-mode
            mTouchMode = TOUCH_MODE_FLING;
            
            // run thread
            resume();
                        
            return true;
        }
        
        @Override
        public void onLongPress(MotionEvent e) {
            
            if (mMenuElement.getMenuAction(e.getX(), e.getY()) == MenuAction.None && e.getX() > VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL) {
                boolean isEvent = false;
                for (EventViewElement event : mEventsOnView) {
                    if (e.getY() > event.top && e.getY() <= event.bottom && e.getX() <= event.right && e.getX() >= event.left) {
                        isEvent = true;
                        Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, event.id);
                        Intent intent = new Intent(Intent.ACTION_VIEW).setData(uri);
                        if (event.isInstance) {
                            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.begin-TimeZone.getTimeZone(TimeZone.getDefault().getID()).getOffset(event.begin));
                            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.end-TimeZone.getTimeZone(TimeZone.getDefault().getID()).getOffset(event.begin));
                        }
                        MainActivity ma = ((MainActivity) mContext);
                        ma.startActivityForResult(intent, 999);
                        break;
                    }
                }
                if (!isEvent) {
                    Intent intent = new Intent(Intent.ACTION_INSERT, Events.CONTENT_URI); 
                    long cent = getMillisFromPixel(e.getY());
                    mCalendarCreateEvent.setTimeInMillis(cent);
                    mCalendarCreateEvent.set(Calendar.SECOND, 0);
                    mCalendarCreateEvent.set(Calendar.MILLISECOND, 0);
                    int modulo = mCalendarCreateEvent.get(Calendar.MINUTE) % 30;
                    if (modulo > 0) {
                        mCalendarCreateEvent.add(Calendar.MINUTE, -modulo);
                    }
                    cent = mCalendarCreateEvent.getTimeInMillis() - TimeZone.getTimeZone(TimeZone.getDefault().getID()).getOffset(cent);
                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cent);
                    MainActivity ma = ((MainActivity) mContext);
                    ma.startActivityForResult(intent, 666);                    
                }
                
                // set touch-mode
                mTouchMode = TOUCH_MODE_LONG_PRESS;
                
                // run thread
                resume();

            }
            
            super.onLongPress(e);
        }
        
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if (mTouchEventPointerCount < 2) {

                // calculate new view boundaries
                mViewTopMillis += Math.round(distanceY) * mViewSpanMillis / mViewHeightPixel;
                mViewBottomMillis += Math.round(distanceY) * mViewSpanMillis / mViewHeightPixel;
                
                // run thread
//                resume();
                mIsRunning = true;
                mThread.run();

                // set touch-mode
                mTouchMode = TOUCH_MODE_SCROLL;
                
                return true;
                
            } else {
                
                return false;
            }
            
        }
                
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            // set touch-mode
            mTouchMode = TOUCH_MODE_TAP;

            // run thread
            resume();
            
            return true;
        }
        
        @Override
        public boolean onDown(MotionEvent e) {

            // stop animation on fling modus
            if (mTouchMode == TOUCH_MODE_FLING) {
                mAnimationFlingIsRunning = false;
            }
            
            return true;
        }
        
        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            // set touch-mode
            mTouchMode = TOUCH_MODE_TAP;
            
            mMenuTapAction = mMenuElement.getMenuAction(e.getX(), e.getY());
            
            if (mMenuTapAction == MenuAction.None) {
                
                if (e.getX() < VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL) {
                    long x = mViewTopMillis + Math.round(e.getY() / mViewHeightPixel * mViewSpanMillis);
                    Collections.sort(TemporaryPreferences.daysInScalaList);
                    for (int i = 0; i < TemporaryPreferences.daysInScalaList.size(); i++) {
                        if (x < TemporaryPreferences.daysInScalaList.get(i)) {
                            mCalendarSelected.setTimeInMillis(TemporaryPreferences.daysInScalaList.get(i-1));
                            if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarSelected.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                                TemporaryPreferences.selectedDayIsSelected = false;
                            } else {
                                TemporaryPreferences.selectedDayIsSelected = true;
                                TemporaryPreferences.selectedDayInScalaMillis = mCalendarSelected.getTimeInMillis();
                                TemporaryPreferences.selectedWeekDayInScalaIndex = mCalendarSelected.get(Calendar.DAY_OF_WEEK);
                            }
                            break;
                        } else if (i == TemporaryPreferences.daysInScalaList.size()-1) {
                            mCalendarSelected.setTimeInMillis(TemporaryPreferences.daysInScalaList.get(i));
                            if (TemporaryPreferences.selectedDayInScalaMillis == mCalendarSelected.getTimeInMillis() && TemporaryPreferences.selectedDayIsSelected) {
                                TemporaryPreferences.selectedDayIsSelected = false;
                            } else {
                                TemporaryPreferences.selectedDayIsSelected = true;
                                TemporaryPreferences.selectedDayInScalaMillis = mCalendarSelected.getTimeInMillis();
                                TemporaryPreferences.selectedWeekDayInScalaIndex = mCalendarSelected.get(Calendar.DAY_OF_WEEK);
                            }
                        }
                    }
                } 
                
                if (e.getX() > VisualPreferences.TIME_ELEMENT_CALENDAR_WIDTH_PIXEL && mRenderStageRatio < 5.5f) {
                    if (!TemporaryPreferences.isVisibleEventDetails) {
                        if (TemporaryPreferences.eventSelected != null && !(e.getY() > TemporaryPreferences.eventSelected.top && e.getY() <= TemporaryPreferences.eventSelected.bottom && e.getX() <= TemporaryPreferences.eventSelected.right && e.getX() >= TemporaryPreferences.eventSelected.left)) {
                            for (EventViewElement event : mEventsOnView) {
                                if (e.getY() > event.top && e.getY() <= event.bottom && e.getX() <= event.right && e.getX() >= event.left) {
                                    TemporaryPreferences.isEventSelected = true;
                                    TemporaryPreferences.eventSelected = event;
                                    break;
                                } else {
                                    TemporaryPreferences.isEventSelected = false;
                                    TemporaryPreferences.eventSelected = null;
                                }
                            }
                        } else {
                            for (EventViewElement event : mEventsOnView) {
                                if (e.getY() > event.top && e.getY() <= event.bottom && e.getX() <= event.right && e.getX() >= event.left) {
                                    if (TemporaryPreferences.eventSelected != null && TemporaryPreferences.eventSelected.unique == event.unique) {
                                        TemporaryPreferences.isVisibleEventDetails = true;
                                        TemporaryPreferences.visibleEventDetails = event;
                                    } else {
                                        TemporaryPreferences.isEventSelected = true;
                                        TemporaryPreferences.eventSelected = event;
                                    }
                                    break;
                                }
                            }    
                        }
                    } else if (!(e.getY() > TemporaryPreferences.detailTop && e.getY() <= TemporaryPreferences.detailBottom && e.getX() <= TemporaryPreferences.detailRight && e.getX() >= TemporaryPreferences.detailLeft)) {
                        TemporaryPreferences.isVisibleEventDetails = false;
                        TemporaryPreferences.visibleEventDetails = null;
                        TemporaryPreferences.isEventSelected = false;
                        TemporaryPreferences.detailTop = -1;
                        TemporaryPreferences.detailLeft = -1;
                        TemporaryPreferences.detailBottom = -1;
                        TemporaryPreferences.detailRight = -1;
                    } else if (e.getY() > TemporaryPreferences.detailTop && e.getY() <= TemporaryPreferences.detailBottom && e.getX() <= TemporaryPreferences.detailRight && e.getX() >= TemporaryPreferences.detailLeft) {
                        int top = Math.round(TemporaryPreferences.detailBottom-((TemporaryPreferences.detailBottom-TemporaryPreferences.detailTop)/3));
                        int left = Math.round(TemporaryPreferences.detailRight-((TemporaryPreferences.detailBottom-TemporaryPreferences.detailTop)/3));
                        int right = Math.round(TemporaryPreferences.detailRight);
                        int bottom = Math.round(TemporaryPreferences.detailBottom);
                        if (e.getY() > top && e.getY() <= bottom && e.getX() <= right && e.getX() >= left) {
                            Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, TemporaryPreferences.visibleEventDetails.id);
                            Intent intent = new Intent(Intent.ACTION_VIEW).setData(uri);
                            if (TemporaryPreferences.visibleEventDetails.isInstance) {
                                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, TemporaryPreferences.visibleEventDetails.begin-TimeZone.getTimeZone(TimeZone.getDefault().getID()).getOffset(TemporaryPreferences.visibleEventDetails.begin));
                                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, TemporaryPreferences.visibleEventDetails.end-TimeZone.getTimeZone(TimeZone.getDefault().getID()).getOffset(TemporaryPreferences.visibleEventDetails.begin));
                            }
                            MainActivity ma = ((MainActivity) mContext);
                            ma.startActivityForResult(intent, 999);                            
                        }
                    }
                }
                
            } else {
                
                switch (mMenuTapAction) {
                    case TapClosedMenu:
                        // prepare animation
                        mAnimationMenu.prepareAnimation(0, 500, 150, false);
                        // start animation
                        mAnimationMenu.start(System.currentTimeMillis());
                        mAnimationMenuIsRunning = true;
                        break;
               
                    case TapOpenedMenu:
                        // prepare animation
                        mAnimationMenu.prepareAnimation(0, 150, 150, true);
                        // start animation
                        mAnimationMenu.start(System.currentTimeMillis());
                        mAnimationMenuIsRunning = true;
                        break;
                
                    case TapAddEventSubMenu:
                        Log.d("debug", "TapAddEventSubMenu");
                        Intent intent = new Intent(Intent.ACTION_INSERT, Events.CONTENT_URI);
                        long cent = TemporaryPreferences.viewTopMillis+(mViewSpanMillis/2);
                        mCalendarCreateEvent.setTimeInMillis(cent);
                        mCalendarCreateEvent.set(Calendar.SECOND, 0);
                        mCalendarCreateEvent.set(Calendar.MILLISECOND, 0);
                        int modulo = mCalendarCreateEvent.get(Calendar.MINUTE) % 30;
                        if (modulo > 0) {
                            mCalendarCreateEvent.add(Calendar.MINUTE, -modulo);
                        }
                        cent = mCalendarCreateEvent.getTimeInMillis() - TimeZone.getTimeZone(TimeZone.getDefault().getID()).getOffset(cent);
                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cent);
                        MainActivity ma = ((MainActivity) mContext);
                        ma.startActivityForResult(intent, 666);
                        break;
                        
                    case TapTodaySubMenu:

                        if (!mAnimationTodayIsRunning && (mViewTopMillis+(mViewSpanMillis/2)) != TemporaryPreferences.hour12NowMillis) {
                            // create animations
                            mAnimationTodayTopValue.prepareAnimation(mViewTopMillis,TemporaryPreferences.hour12NowMillis-(mViewSpanMillis/2), AnimationPreferences.TODAY_ANIMATION_DURATION, false);
                            mAnimationTodayBottomValue.prepareAnimation(mViewBottomMillis, TemporaryPreferences.hour12NowMillis+(mViewSpanMillis/2), AnimationPreferences.TODAY_ANIMATION_DURATION, false);
                            
                            // start animations
                            mAnimationTodayTimeStart = System.currentTimeMillis();
                            mAnimationTodayTopValue.start(mAnimationTodayTimeStart);
                            mAnimationTodayBottomValue.start(mAnimationTodayTimeStart);
                            mAnimationTodayIsRunning = true;
                        } else if (!mAnimationTodayIsRunning && (mViewTopMillis+(mViewSpanMillis/2)) == TemporaryPreferences.hour12NowMillis) {
                            // create animations
                            mAnimationTodayTopValue.prepareAnimation(mViewTopMillis, TemporaryPreferences.dayNowTopMillis, AnimationPreferences.TODAY_ANIMATION_DURATION, false);
                            mAnimationTodayBottomValue.prepareAnimation(mViewBottomMillis, TemporaryPreferences.dayNowTopMillis+TimePreferences.DURATION_MILLIS_DAY_01, AnimationPreferences.TODAY_ANIMATION_DURATION, false);
                            
                            // start animations
                            mAnimationTodayTimeStart = System.currentTimeMillis();
                            mAnimationTodayTopValue.start(mAnimationTodayTimeStart);
                            mAnimationTodayBottomValue.start(mAnimationTodayTimeStart);
                            mAnimationTodayIsRunning = true;
                        }
                        break;
                        
                    case TapOptionsSubMenu:
                        MainActivity a = (MainActivity) mContext;
                        a.setSettingsFragmentVisible(true);
                        break;
                        
                    case TapEmptyTopSubMenu:
                        Log.d("debug", "TapEmptyTopSubMenu");
                        break;
                        
                    case TapResetSubMenu:
                        
                        mViewSpanResetMillis = Math.round((TimePreferences.DURATION_MILLIS_MAX-TimePreferences.DURATION_MILLIS_MIN) / 100d * 3.99999d);
                        mAnimationResetMillisTopEnd = (long) (mViewTopMillis + (mViewSpanMillis*0.5f) - (mViewSpanResetMillis*0.5f));
                        mAnimationResetMillisBottomEnd = (long) (mViewTopMillis + (mViewSpanMillis*0.5f) + (mViewSpanResetMillis*0.5f));
                                           
                        // create animations
                        mAnimationResetTopValue.prepareAnimation(mViewTopMillis, mAnimationResetMillisTopEnd, AnimationPreferences.DOUBLE_TAP_ANIMATION_DURATION, false);
                        mAnimationResetBottomValue.prepareAnimation(mViewBottomMillis, mAnimationResetMillisBottomEnd, AnimationPreferences.DOUBLE_TAP_ANIMATION_DURATION, false);
                        
                        // start animations
                        mAnimationResetTimeStart = System.currentTimeMillis();
                        mAnimationResetTopValue.start(mAnimationResetTimeStart);
                        mAnimationResetBottomValue.start(mAnimationResetTimeStart);
                        mAnimationResetIsRunning = true;
                        
                        break;
                         
                    default:
                        break;
                };
                
            }
            
            
            
            
            
            // run thread
            resume();
            
            return true;
        }
        
    }
}