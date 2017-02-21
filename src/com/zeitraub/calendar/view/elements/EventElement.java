package com.zeitraub.calendar.view.elements;

import java.util.ArrayList;
import java.util.TimeZone;

import com.zeitraub.calendar.R;
import com.zeitraub.calendar.data.event.Event;
import com.zeitraub.calendar.view.preferences.TemporaryPreferences;
import com.zeitraub.calendar.view.preferences.TimePreferences;
import com.zeitraub.calendar.view.preferences.VisualPreferences;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;

/**
 * Event element with drawing routine
 * 
 * @author Torsten
 *
 */
public class EventElement {

    private Paint mPaintEventGraph;
    private float mMultiplierGraph; 
    private int mOffsetLeftGraph;
    private int mOffsetRightGraph;
    
    private Paint mPaintEventAllDay;
    private int mLeftAllDay;
    private int mTopAllDay;
    private int mRrightAllDay;
    private int mBottomAllDay;
        
    private TextPaint mPaintEventText;
    private Paint mPaintEventBackground;
    private Paint mPaintEventBackgroundSelected;
    private Paint mPaintEventColor;
    private int mEventsPaddingLeft;
    private int mEventPaddingLeft;
    private int mEventPaddingRight;
    private int mEventMarginLeft;
    private int mEventMarginRight;
    private int mEventMarginTop;
    private int mEventMarginBottom;
    private int mEventColorWidth;
    private int mEventEventTextLength;
    private int mEventEventsTextLength;
    private int mEventEventsRectangleLength;
    private int mEventTextTooLongDelta;
    private boolean mEventTextIsTooLong;

    private boolean mEventExactReviousIsAllDay;
    private int mEventExactReviousIsAllDayEndPx;
    private int mEventExactMillisOffsetBegin;
    private int mEventExactMillisOffsetBeginPx;
    private int mEventExactMillisOffsetEnd;
    private int mEventExactMillisOffsetEndPx;
    private int mEventExactMillisOffsetMiddlePx;

    private ArrayList<EventViewElement> mEventsOnView = new ArrayList<EventViewElement>();
    
    public EventElement() {
       
        mPaintEventGraph = new Paint();
        mPaintEventGraph.setAntiAlias(true);
        mPaintEventAllDay = new Paint();
        mPaintEventAllDay.setAntiAlias(true);
        mPaintEventAllDay.setStyle(Style.FILL);
        mPaintEventText = new TextPaint();
        mPaintEventText.setAntiAlias(true);
        mPaintEventBackground = new Paint();
        mPaintEventBackground.setAntiAlias(true);
        mPaintEventBackground.setStyle(Style.FILL);
        mPaintEventBackgroundSelected = new Paint();
        mPaintEventBackgroundSelected.setAntiAlias(true);
        mPaintEventBackgroundSelected.setStyle(Style.FILL);
        mPaintEventColor = new Paint();
        mPaintEventColor.setAntiAlias(true);
        mPaintEventColor.setStyle(Style.FILL);
        
        mPaintEventBackground.setColor(Color.HSVToColor(255, new float[]{200f,0.05f,0.93f}));
        mPaintEventBackgroundSelected.setColor(Color.HSVToColor(255, new float[]{200f,0.15f,0.93f}));
                
    }
    
    /**
     * Draws event details pop-up
     */
    public void drawEventDetail(Canvas canvas, EventViewElement eVent, long mViewTopMillis, long mViewBottomMillis, float mViewMultiplierMillisToPixel, Context context) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(eVent.color);
        int margin = DrawingHelper.dipToPixel(2500,VisualPreferences.SCREEN_DENSITY);
        int padding = margin/5;
        
        int arrowTopX = Math.round(eVent.left + (VisualPreferences.EVENT_ELEMENT_LABEL_COLOR_WIDTH_PIXEL/2));
        int arrowRightX = Math.round(arrowTopX+(margin*0.75f));
        int arrowLeftX = Math.round(arrowTopX-(margin*0.75f));
        
        int arrowTopY = Math.round(eVent.bottom);
        int arrowRightY = arrowTopY+margin;
        int arrowLeftY = arrowTopY+margin;
        
        Path mArrowPath = new Path();
        mArrowPath.setFillType(Path.FillType.EVEN_ODD);
        mArrowPath.moveTo(arrowTopX, arrowTopY);
        mArrowPath.lineTo(arrowRightX, arrowRightY);
        mArrowPath.lineTo(arrowLeftX, arrowLeftY);
        mArrowPath.lineTo(arrowTopX, arrowTopY);
        mArrowPath.close();
        
        int boxheight = DrawingHelper.dipToPixel(22000,VisualPreferences.SCREEN_DENSITY);
        
        canvas.drawRect(0+margin, eVent.bottom+margin, VisualPreferences.VIEW_WIDTH_PIXEL-margin, eVent.bottom+boxheight-margin, p);
    

        p.setColor(Color.HSVToColor(255, new float[]{ 203f, 0.32f , 0.22f}));

        canvas.drawPath(mArrowPath, p);
        canvas.drawRect(0+margin+padding, eVent.bottom+margin, VisualPreferences.VIEW_WIDTH_PIXEL-margin, eVent.bottom+boxheight-margin, p);
        
        int titelPadX = DrawingHelper.dipToPixel(1250,VisualPreferences.SCREEN_DENSITY);
        int titelPadY = DrawingHelper.dipToPixel(2500,VisualPreferences.SCREEN_DENSITY);
        int lineSpace = DrawingHelper.dipToPixel(1250,VisualPreferences.SCREEN_DENSITY);;
        
        TextPaint pt = new TextPaint();
        pt.setAntiAlias(true);
        pt.setTextSize(VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL);
        pt.setColor(Color.HSVToColor(255, new float[]{ 201f, 0.06f , 0.87f}));
        
        DrawingHelper.drawText(canvas, pt, eVent.title, 0+margin+padding+titelPadX, eVent.bottom+margin+padding+titelPadY, Align.LEFT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        
        titelPadY += lineSpace+VisualPreferences.TIME_ELEMENT_TEXTSIZE_LARGER_PIXEL;
        
        pt.setTextSize(VisualPreferences.TIME_ELEMENT_TEXTSIZE_MEDIUM_PIXEL);
        DrawingHelper.drawText(canvas, pt, DrawingHelper.getTimeHHMMEDDMMYYYY(eVent.begin-TimeZone.getTimeZone(TimeZone.getDefault().getID()).getOffset(eVent.begin)), 0+margin+padding+titelPadX, eVent.bottom+margin+padding+titelPadY, Align.LEFT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        
        titelPadY += lineSpace+VisualPreferences.TIME_ELEMENT_TEXTSIZE_MEDIUM_PIXEL;
        
        pt.setTextSize(VisualPreferences.TIME_ELEMENT_TEXTSIZE_MEDIUM_PIXEL);
        DrawingHelper.drawText(canvas, pt, DrawingHelper.getTimeHHMMEDDMMYYYY(eVent.end-TimeZone.getTimeZone(TimeZone.getDefault().getID()).getOffset(eVent.end)), 0+margin+padding+titelPadX, eVent.bottom+margin+padding+titelPadY, Align.LEFT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        
        titelPadY += lineSpace+VisualPreferences.TIME_ELEMENT_TEXTSIZE_MEDIUM_PIXEL;
        
        DrawingHelper.drawText(canvas, pt, eVent.calendar, 0+margin+padding+titelPadX, eVent.bottom+margin+padding+titelPadY, Align.LEFT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
        
        TemporaryPreferences.detailTop = eVent.bottom+margin+padding;
        TemporaryPreferences.detailLeft = 0+margin+padding;
        TemporaryPreferences.detailBottom = eVent.bottom+boxheight-margin-padding;
        TemporaryPreferences.detailRight = VisualPreferences.VIEW_WIDTH_PIXEL-margin-padding;
        
        @SuppressWarnings("deprecation")
        Drawable edit = context.getResources().getDrawable(R.drawable.edit);

        int top = Math.round(TemporaryPreferences.detailBottom-((TemporaryPreferences.detailBottom-TemporaryPreferences.detailTop)/3));
        int left = Math.round(TemporaryPreferences.detailRight-((TemporaryPreferences.detailBottom-TemporaryPreferences.detailTop)/3));
        int right = Math.round(TemporaryPreferences.detailRight);
        int bottom = Math.round(TemporaryPreferences.detailBottom);
        
        edit.setBounds(left, top, right, bottom);
        edit.draw(canvas);
    }
    
    
    /**
     * Draws all day event
     */
    public void drawEventAllDay(Canvas canvas, Event event, long mViewTopMillis, float mViewMultiplierMillisToPixel, int position) {
        
        mPaintEventAllDay.setStyle(Style.FILL);
        if (event.color != 0) {
            mPaintEventAllDay.setColor(event.color);
        } else {
            mPaintEventAllDay.setColor(event.calendarColor);
        }
        
        mLeftAllDay = VisualPreferences.EVENT_ELEMENT_PADDING_PIXEL;
        mLeftAllDay += position*VisualPreferences.EVENT_ELEMENT_ALLDAY_WIDTH_PIXEL/3;
        
        mRrightAllDay = VisualPreferences.EVENT_ELEMENT_PADDING_PIXEL;
        mRrightAllDay += (position+1)*VisualPreferences.EVENT_ELEMENT_ALLDAY_WIDTH_PIXEL/3;
        
        mTopAllDay = Math.round((event.unionBegin - mViewTopMillis) * mViewMultiplierMillisToPixel);
        mBottomAllDay = Math.round((event.unionEnd - mViewTopMillis) * mViewMultiplierMillisToPixel);
        
        canvas.drawRect(mLeftAllDay, mTopAllDay, mRrightAllDay, mBottomAllDay, mPaintEventAllDay);
        
        mPaintEventAllDay.setStyle(Style.STROKE);
        mPaintEventAllDay.setStrokeWidth(1f);
        mPaintEventAllDay.setColor(VisualPreferences.EVENT_ELEMENT_EVENT_BORDER_COLOR);
        canvas.drawRect(mLeftAllDay, mTopAllDay, mRrightAllDay, mBottomAllDay, mPaintEventAllDay);
    }

    /**
     * Draws event in graph view
     */
    public void drawEventGraph(Canvas canvas, Event event, float offset, float height, long beginMillis, long endMillis) {

        mPaintEventGraph.setStyle(Style.FILL);
        if (event.color != 0) {
            mPaintEventGraph.setColor(event.color);
        } else {
            mPaintEventGraph.setColor(event.calendarColor);
        }

        mMultiplierGraph = 1f * VisualPreferences.EVENT_ELEMENT_WIDTH_PIXEL / TimePreferences.DURATION_MILLIS_DAY_01;
       
        if (event.unionBegin > beginMillis) {
            mOffsetLeftGraph = Math.round((event.unionBegin - beginMillis) * mMultiplierGraph);
        } else {
            mOffsetLeftGraph = 0;
        }
       
        if (event.unionEnd < endMillis) {
            mOffsetRightGraph = Math.round((event.unionEnd - beginMillis) * mMultiplierGraph);
        } else {
            mOffsetRightGraph = Math.round(TimePreferences.DURATION_MILLIS_DAY_01 * mMultiplierGraph);
        }
       
        canvas.drawRect(VisualPreferences.EVENT_ELEMENT_PADDING_PIXEL+mOffsetLeftGraph, offset, VisualPreferences.EVENT_ELEMENT_PADDING_PIXEL+mOffsetRightGraph, offset+height, mPaintEventGraph);
        
        mPaintEventGraph.setStyle(Style.STROKE);
        mPaintEventGraph.setColor(VisualPreferences.EVENT_ELEMENT_EVENT_BORDER_COLOR);
        canvas.drawRect(VisualPreferences.EVENT_ELEMENT_PADDING_PIXEL+mOffsetLeftGraph, offset, VisualPreferences.EVENT_ELEMENT_PADDING_PIXEL+mOffsetRightGraph, offset+height, mPaintEventGraph);
    }

    /**
     * Draws event in row view
     */
    public ArrayList<EventViewElement> drawEventRowAllDay(Canvas canvas, ArrayList<Event> events, float offsetFloat, float heightFloat) {

        int offset = Math.round(offsetFloat);
        
        int height = Math.round(heightFloat);
        
        mEventsOnView.clear();
        
        mEventPaddingLeft = VisualPreferences.EVENT_ELEMENT_LABEL_PADDING_LEFT_PIXEL;
        mEventPaddingRight = VisualPreferences.EVENT_ELEMENT_LABEL_PADDING_RIGHT_PIXEL;
        mEventMarginLeft = VisualPreferences.EVENT_ELEMENT_LABEL_MARGIN_LEFT_PIXEL;
        mEventMarginRight = VisualPreferences.EVENT_ELEMENT_LABEL_MARGIN_RIGHT_PIXEL;
        mEventMarginTop = VisualPreferences.EVENT_ELEMENT_LABEL_MARGIN_TOP_PIXEL;
        mEventMarginBottom = VisualPreferences.EVENT_ELEMENT_LABEL_MARGIN_BOTTOM_PIXEL;
        mEventColorWidth = VisualPreferences.EVENT_ELEMENT_LABEL_COLOR_WIDTH_PIXEL;

        mPaintEventText = VisualPreferences.eventElement_Event_Text_Paint;
        
        mEventsPaddingLeft = VisualPreferences.EVENT_ELEMENT_PADDING_PIXEL;
        mEventEventsRectangleLength = 0;
        mEventEventsTextLength = 0;
        mEventTextTooLongDelta = 0;
        mEventTextIsTooLong = false;
        
        for (Event event : events) {
            if (event.allDay.equals("1")) {
                mEventEventsTextLength += getTextWidth(event.title);
//                Log.d("debug", "a: "+mPaintEventText.measureText(event.title)+" b: "+getTextWidth(event.title));
                mEventEventsRectangleLength +=getTextWidth(event.title)+mEventPaddingLeft+mEventPaddingRight+mEventMarginLeft+mEventMarginRight+mEventColorWidth;                
            } else {
                mEventEventsTextLength += getTextWidth("00:00"+" "+event.title);
//                Log.d("debug", "a: "+mPaintEventText.measureText("00:00"+" "+event.title)+" b: "+getTextWidth("00:00"+" "+event.title));
                mEventEventsRectangleLength += getTextWidth("00:00"+" "+event.title)+mEventPaddingLeft+mEventPaddingRight+mEventMarginLeft+mEventMarginRight+mEventColorWidth;
            }
            
        }
        
        if (mEventEventsRectangleLength > VisualPreferences.EVENT_ELEMENT_WIDTH_PIXEL) {
            mEventTextTooLongDelta = mEventEventsRectangleLength - VisualPreferences.EVENT_ELEMENT_WIDTH_PIXEL; 
            mEventTextIsTooLong = true;
        }
        
        if (mEventTextIsTooLong) {
            mEventEventTextLength = 0;
            for (Event event : events) {
                if (event.color != 0) {
                    mPaintEventColor.setColor(event.color);
                } else {
                    mPaintEventColor.setColor(event.calendarColor);
                }
                
                mEventsPaddingLeft += mEventMarginLeft;
                mEventEventTextLength = (mEventEventsTextLength-mEventTextTooLongDelta)/events.size();
                if (TemporaryPreferences.eventSelected != null && TemporaryPreferences.eventSelected.unique == event.id) {
                    canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventTextLength+mEventPaddingRight+mEventMarginRight, offset+height-mEventMarginBottom, mPaintEventBackgroundSelected);
                } else {
                    canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventTextLength+mEventPaddingRight+mEventMarginRight, offset+height-mEventMarginBottom, mPaintEventBackground);    
                }
                canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth, offset+height-mEventMarginBottom, mPaintEventColor);
                event.left = mEventsPaddingLeft;
                event.top = offset+mEventMarginTop;
                event.right = mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventTextLength+mEventPaddingRight+mEventMarginRight;
                event.bottom = offset+height-mEventMarginBottom;
                mEventsOnView.add(new EventViewElement(event.eventId, event.unionBegin, event.unionEnd, event.left, event.top, event.bottom, event.right, event.title, mPaintEventColor.getColor(), event.id, (event.RRule != null), event.organizer));
                if (event.allDay.equals("1")) {
                    DrawingHelper.drawText(canvas, mPaintEventText, TextUtils.ellipsize(event.title, mPaintEventText, mEventEventTextLength, TextUtils.TruncateAt.END).toString(), mEventsPaddingLeft+mEventMarginLeft+mEventColorWidth+mEventPaddingLeft, offset+(height/2), Align.LEFT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
                } else {
                    DrawingHelper.drawText(canvas, mPaintEventText, TextUtils.ellipsize(event.startTime+" "+event.title, mPaintEventText, mEventEventTextLength, TextUtils.TruncateAt.END).toString(), mEventsPaddingLeft+mEventMarginLeft+mEventColorWidth+mEventPaddingLeft, offset+(height/2), Align.LEFT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
                }
                mEventsPaddingLeft += mEventColorWidth+mEventPaddingLeft+mEventEventTextLength+mEventPaddingRight+mEventMarginRight;
            }
        } else {
            mEventEventsRectangleLength = 0;
            for (Event event : events) {
                if (event.color != 0) {
                    mPaintEventColor.setColor(event.color);
                } else {
                    mPaintEventColor.setColor(event.calendarColor);
                }
                
                mEventsPaddingLeft += mEventMarginLeft;
                if (event.allDay.equals("1")) {
                    mEventEventsRectangleLength = getTextWidth(event.title);
                } else {
                    mEventEventsRectangleLength = getTextWidth("00:00"+" "+event.title);
                }
                if (TemporaryPreferences.eventSelected != null && TemporaryPreferences.eventSelected.unique == event.id) {
                    canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight, offset+height-mEventMarginBottom, mPaintEventBackgroundSelected);
                } else {
                    canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight, offset+height-mEventMarginBottom, mPaintEventBackground);    
                }
                canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth, offset+height-mEventMarginBottom, mPaintEventColor);
                event.left = mEventsPaddingLeft;
                event.top = offset+mEventMarginTop;
                event.right = mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight;
                event.bottom = offset+height-mEventMarginBottom;
                mEventsOnView.add(new EventViewElement(event.eventId, event.unionBegin, event.unionEnd, event.left, event.top, event.bottom, event.right, event.title, mPaintEventColor.getColor(), event.id, (event.RRule != null), event.organizer));
                if (event.allDay.equals("1")) {
                    DrawingHelper.drawText(canvas, mPaintEventText, event.title, mEventsPaddingLeft+mEventMarginLeft+mEventColorWidth+mEventPaddingLeft, offset+(height/2), Align.LEFT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
                } else {
                    DrawingHelper.drawText(canvas, mPaintEventText, event.startTime+" "+event.title, mEventsPaddingLeft+mEventMarginLeft+mEventColorWidth+mEventPaddingLeft, offset+(height/2), Align.LEFT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
                }
                mEventsPaddingLeft += mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight;
            }
        }      
        
        return mEventsOnView;
        
    }
    
    
    /**
     * Returns events for row view
     */
    public ArrayList<EventViewElement> drawEventRow(Canvas canvas, ArrayList<Event> events, float offsetFloat, float heightFloat) {

        int offset = Math.round(offsetFloat);
        
        int height = Math.round(heightFloat);
        
        mEventsOnView.clear();
        
        mEventPaddingLeft = VisualPreferences.EVENT_ELEMENT_LABEL_PADDING_LEFT_PIXEL;
        mEventPaddingRight = VisualPreferences.EVENT_ELEMENT_LABEL_PADDING_RIGHT_PIXEL;
        mEventMarginLeft = VisualPreferences.EVENT_ELEMENT_LABEL_MARGIN_LEFT_PIXEL;
        mEventMarginRight = VisualPreferences.EVENT_ELEMENT_LABEL_MARGIN_RIGHT_PIXEL;
        mEventMarginTop = VisualPreferences.EVENT_ELEMENT_LABEL_MARGIN_TOP_PIXEL;
        mEventMarginBottom = VisualPreferences.EVENT_ELEMENT_LABEL_MARGIN_BOTTOM_PIXEL;
        mEventColorWidth = VisualPreferences.EVENT_ELEMENT_LABEL_COLOR_WIDTH_PIXEL;

        mPaintEventText = VisualPreferences.eventElement_Event_Text_Paint;
        
        mEventsPaddingLeft = VisualPreferences.EVENT_ELEMENT_PADDING_PIXEL;
        mEventEventsRectangleLength = 0;
        mEventEventsTextLength = 0;
        mEventTextTooLongDelta = 0;
        mEventTextIsTooLong = false;
        
        for (Event Event : events) {
            mEventEventsTextLength += getTextWidth("00:00"+" "+Event.title);
            mEventEventsRectangleLength += getTextWidth("00:00"+" "+Event.title)+mEventPaddingLeft+mEventPaddingRight+mEventMarginLeft+mEventMarginRight+mEventColorWidth;
        }
        
        if (mEventEventsRectangleLength > VisualPreferences.EVENT_ELEMENT_WIDTH_PIXEL) {
            mEventTextTooLongDelta = mEventEventsRectangleLength - VisualPreferences.EVENT_ELEMENT_WIDTH_PIXEL; 
            mEventTextIsTooLong = true;
        }
        
        if (mEventTextIsTooLong) {
            mEventEventTextLength = 0;
            for (Event event : events) {
                if (event.color != 0) {
                    mPaintEventColor.setColor(event.color);
                } else {
                    mPaintEventColor.setColor(event.calendarColor);
                }
                
                mEventsPaddingLeft += mEventMarginLeft;
                mEventEventTextLength = (mEventEventsTextLength-mEventTextTooLongDelta)/events.size();
                if (TemporaryPreferences.eventSelected != null && TemporaryPreferences.eventSelected.unique == event.id) {
                    canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventTextLength+mEventPaddingRight+mEventMarginRight, offset+height-mEventMarginBottom, mPaintEventBackgroundSelected);
                } else {
                    canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventTextLength+mEventPaddingRight+mEventMarginRight, offset+height-mEventMarginBottom, mPaintEventBackground);    
                }
                canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth, offset+height-mEventMarginBottom, mPaintEventColor);
                event.left = mEventsPaddingLeft;
                event.top = offset+mEventMarginTop;
                event.right = mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventTextLength+mEventPaddingRight+mEventMarginRight;
                event.bottom = offset+height-mEventMarginBottom;
                mEventsOnView.add(new EventViewElement(event.eventId, event.unionBegin, event.unionEnd, event.left, event.top, event.bottom, event.right, event.title, mPaintEventColor.getColor(), event.id, (event.RRule != null), event.organizer));
                DrawingHelper.drawText(canvas, mPaintEventText, TextUtils.ellipsize(event.startTime+" "+event.title, mPaintEventText, mEventEventTextLength, TextUtils.TruncateAt.END).toString(), mEventsPaddingLeft+mEventMarginLeft+mEventColorWidth+mEventPaddingLeft, offset+(height/2), Align.LEFT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
                mEventsPaddingLeft += mEventColorWidth+mEventPaddingLeft+mEventEventTextLength+mEventPaddingRight+mEventMarginRight;
            }
        } else {
            mEventEventsRectangleLength = 0;
            for (Event event : events) {
                if (event.color != 0) {
                    mPaintEventColor.setColor(event.color);
                } else {
                    mPaintEventColor.setColor(event.calendarColor);
                }

                mEventsPaddingLeft += mEventMarginLeft;
                mEventEventsRectangleLength = getTextWidth("00:00"+" "+event.title);
                if (TemporaryPreferences.eventSelected != null && TemporaryPreferences.eventSelected.unique == event.id) {
                    canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight, offset+height-mEventMarginBottom, mPaintEventBackgroundSelected);
                } else {
                    canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight, offset+height-mEventMarginBottom, mPaintEventBackground);    
                }
                canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth, offset+height-mEventMarginBottom, mPaintEventColor);
                event.left = mEventsPaddingLeft;
                event.top = offset+mEventMarginTop;
                event.right = mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight;
                event.bottom = offset+height-mEventMarginBottom;
                mEventsOnView.add(new EventViewElement(event.eventId, event.unionBegin, event.unionEnd, event.left, event.top, event.bottom, event.right, event.title, mPaintEventColor.getColor(), event.id, (event.RRule != null), event.organizer));
                DrawingHelper.drawText(canvas, mPaintEventText, event.startTime+" "+event.title, mEventsPaddingLeft+mEventMarginLeft+mEventColorWidth+mEventPaddingLeft, offset+(height/2), Align.LEFT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
                mEventsPaddingLeft += mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight;
            }
        }   
        
        return mEventsOnView;
    }
    
    /**
     * Returns events for exact view
     */
    public ArrayList<EventViewElement> drawEventExact(Canvas canvas, ArrayList<Event> events, float offsetFloat, float heightFloat, long beginMillis, float multiplier) {
        
        int offset = Math.round(offsetFloat);
        
        int height = Math.round(heightFloat);
        
        mEventsOnView.clear();
        
        mEventExactReviousIsAllDay = false;
        mEventExactReviousIsAllDayEndPx = 0;
        
        mEventExactMillisOffsetBegin = 0;
        mEventExactMillisOffsetBeginPx = 0;
        mEventExactMillisOffsetEnd = 0;
        mEventExactMillisOffsetEndPx = 0;
        mEventExactMillisOffsetMiddlePx = 0;
        
        mEventPaddingLeft = VisualPreferences.EVENT_ELEMENT_LABEL_PADDING_LEFT_PIXEL;
        mEventPaddingRight = VisualPreferences.EVENT_ELEMENT_LABEL_PADDING_RIGHT_PIXEL;
        mEventMarginLeft = VisualPreferences.EVENT_ELEMENT_LABEL_MARGIN_LEFT_PIXEL;
        mEventMarginRight = VisualPreferences.EVENT_ELEMENT_LABEL_MARGIN_RIGHT_PIXEL;
        mEventMarginTop = 0;
        mEventMarginBottom = 0;
        mEventColorWidth = VisualPreferences.EVENT_ELEMENT_LABEL_COLOR_WIDTH_PIXEL;

        mPaintEventText = VisualPreferences.eventElement_Event_Text_Paint;
        
        mEventsPaddingLeft = VisualPreferences.EVENT_ELEMENT_PADDING_PIXEL;
        mEventEventsRectangleLength = 0;
        mEventEventsTextLength = 0;
        mEventTextTooLongDelta = 0;
        mEventTextIsTooLong = false;
        
        for (Event event : events) {
            if (event.allDay.equals("1")) {
                mEventEventsTextLength += getTextWidth(event.title);
                mEventEventsRectangleLength += getTextWidth(event.title)+mEventPaddingLeft+mEventPaddingRight+mEventMarginLeft+mEventMarginRight+mEventColorWidth;                
            } else {
                mEventEventsTextLength += getTextWidth("00:00"+" "+event.title);
                mEventEventsRectangleLength += getTextWidth("00:00"+" "+event.title)+mEventPaddingLeft+mEventPaddingRight+mEventMarginLeft+mEventMarginRight+mEventColorWidth;
            }
            
        }
        
        if (mEventEventsRectangleLength > VisualPreferences.EVENT_ELEMENT_WIDTH_PIXEL) {
            mEventTextTooLongDelta = mEventEventsRectangleLength - VisualPreferences.EVENT_ELEMENT_WIDTH_PIXEL; 
            mEventTextIsTooLong = true;
        }
        
        for (Event event : events) {
            
            mEventExactMillisOffsetBegin = Math.round(event.unionBegin - beginMillis);
            mEventExactMillisOffsetBeginPx = Math.round(mEventExactMillisOffsetBegin * multiplier);
            
            Log.d("debug", event.title+" "+mEventExactReviousIsAllDay+" "+mEventExactMillisOffsetBeginPx+" "+mEventExactMillisOffsetEndPx);
            
           
            if ((mEventExactReviousIsAllDay && event.allDay.equals("0") && (offset+mEventExactMillisOffsetBeginPx+mEventMarginTop) > mEventExactReviousIsAllDayEndPx) || mEventExactMillisOffsetBeginPx > mEventExactMillisOffsetEndPx) {
                mEventsPaddingLeft = VisualPreferences.EVENT_ELEMENT_PADDING_PIXEL;
            }
            
            mEventExactMillisOffsetEnd = Math.round(event.unionEnd - beginMillis);
            mEventExactMillisOffsetEndPx = Math.round(mEventExactMillisOffsetEnd * multiplier);
            
            mEventExactMillisOffsetMiddlePx = Math.round((mEventExactMillisOffsetEndPx - mEventExactMillisOffsetBeginPx) / 2);
            
            if (event.color != 0) {
                mPaintEventColor.setColor(event.color);
            } else {
                mPaintEventColor.setColor(event.calendarColor);
            }
            
            
            mEventsPaddingLeft += mEventMarginLeft;
            mPaintEventBackground.setAlpha(0);
            if (event.allDay.equals("1")) {
                mEventExactReviousIsAllDay = true;
                mEventEventsRectangleLength = getTextWidth(event.title);
                if (TemporaryPreferences.eventSelected != null && TemporaryPreferences.eventSelected.unique == event.id) {
                    canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight, offset+(TimePreferences.DURATION_MILLIS_HOUR_01*multiplier)-mEventMarginBottom, mPaintEventBackgroundSelected);
                } else {
                    canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight, offset+(TimePreferences.DURATION_MILLIS_HOUR_01*multiplier)-mEventMarginBottom, mPaintEventBackground);    
                }
                canvas.drawRect(mEventsPaddingLeft, offset+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth, offset+(TimePreferences.DURATION_MILLIS_HOUR_01*multiplier)-mEventMarginBottom, mPaintEventColor);
                event.left = mEventsPaddingLeft;
                event.top = offset+mEventMarginTop;
                event.right = mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight;
                event.bottom = (int) (offset+(TimePreferences.DURATION_MILLIS_HOUR_01*multiplier)-mEventMarginBottom);
                mEventExactReviousIsAllDayEndPx = Math.round(event.bottom);
                mEventsOnView.add(new EventViewElement(event.eventId, event.unionBegin, event.unionEnd, event.left, event.top, event.bottom, event.right, event.title, mPaintEventColor.getColor(), event.id, (event.RRule != null), event.organizer));
                DrawingHelper.drawText(canvas, mPaintEventText, event.title, mEventsPaddingLeft+mEventMarginLeft+mEventColorWidth+mEventPaddingLeft, offset+(TimePreferences.DURATION_MILLIS_HOUR_01*multiplier/2), Align.LEFT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
                
            } else {
                mEventExactReviousIsAllDay = false;
                mEventEventsRectangleLength = getTextWidth("00:00"+" "+event.title);
                if (height < (offset+mEventExactMillisOffsetEndPx-mEventMarginBottom)) {
                    if (TemporaryPreferences.eventSelected != null && TemporaryPreferences.eventSelected.unique == event.id) {
                        canvas.drawRect(mEventsPaddingLeft, offset+mEventExactMillisOffsetBeginPx+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight, offset+height-mEventMarginBottom, mPaintEventBackgroundSelected);
                    } else {
                        canvas.drawRect(mEventsPaddingLeft, offset+mEventExactMillisOffsetBeginPx+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight, offset+height-mEventMarginBottom, mPaintEventBackground);    
                    }
                    canvas.drawRect(mEventsPaddingLeft, offset+mEventExactMillisOffsetBeginPx+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth, offset+height-mEventMarginBottom, mPaintEventColor);
                    event.left = mEventsPaddingLeft;
                    event.top = offset+mEventExactMillisOffsetBeginPx+mEventMarginTop;
                    event.right = mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight;
                    event.bottom = offset+height-mEventMarginBottom;
                    mEventExactReviousIsAllDayEndPx = Math.round(event.bottom);
                    mEventsOnView.add(new EventViewElement(event.eventId, event.unionBegin, event.unionEnd, event.left, event.top, event.bottom, event.right, event.title, mPaintEventColor.getColor(), event.id, (event.RRule != null), event.organizer));
                    DrawingHelper.drawText(canvas, mPaintEventText, event.startTime+" "+event.title, mEventsPaddingLeft+mEventMarginLeft+mEventColorWidth+mEventPaddingLeft, offset+mEventExactMillisOffsetBeginPx+((height-mEventExactMillisOffsetBeginPx)/2), Align.LEFT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
                    DrawingHelper.drawText(canvas, mPaintEventText, "...", mEventsPaddingLeft+mEventMarginLeft+mEventColorWidth+mEventPaddingLeft, offset+mEventExactMillisOffsetBeginPx+((height-mEventExactMillisOffsetBeginPx)/1.15f), Align.LEFT, DrawingHelper.TextAlignVertical.Top, 255, 1);
                } else {
                    if (TemporaryPreferences.eventSelected != null && TemporaryPreferences.eventSelected.unique == event.id) {
                        canvas.drawRect(mEventsPaddingLeft, offset+mEventExactMillisOffsetBeginPx+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight, offset+mEventExactMillisOffsetEndPx-mEventMarginBottom, mPaintEventBackgroundSelected);
                    } else {
                        canvas.drawRect(mEventsPaddingLeft, offset+mEventExactMillisOffsetBeginPx+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight, offset+mEventExactMillisOffsetEndPx-mEventMarginBottom, mPaintEventBackground);    
                    }
                    canvas.drawRect(mEventsPaddingLeft, offset+mEventExactMillisOffsetBeginPx+mEventMarginTop, mEventsPaddingLeft+mEventColorWidth, offset+mEventExactMillisOffsetEndPx-mEventMarginBottom, mPaintEventColor);
                    event.left = mEventsPaddingLeft;
                    event.top = offset+mEventExactMillisOffsetBeginPx+mEventMarginTop;
                    event.right = mEventsPaddingLeft+mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight;
                    event.bottom = offset+mEventExactMillisOffsetEndPx-mEventMarginBottom;
                    mEventsOnView.add(new EventViewElement(event.eventId, event.unionBegin, event.unionEnd, event.left, event.top, event.bottom, event.right, event.title, mPaintEventColor.getColor(), event.id, (event.RRule != null), event.organizer));
                    DrawingHelper.drawText(canvas, mPaintEventText, event.startTime+" "+event.title, mEventsPaddingLeft+mEventMarginLeft+mEventColorWidth+mEventPaddingLeft, offset+mEventExactMillisOffsetBeginPx+mEventExactMillisOffsetMiddlePx, Align.LEFT, DrawingHelper.TextAlignVertical.Middle, 255, 1);
                }
            }
            mEventsPaddingLeft += mEventColorWidth+mEventPaddingLeft+mEventEventsRectangleLength+mEventPaddingRight+mEventMarginRight;
            mPaintEventBackground.setAlpha(255);
        }

        return mEventsOnView;
    }
    
    Rect bounds = new Rect();
    Paint paint = new Paint();
    
    public int getTextWidth(String text) {
        bounds.setEmpty();
        paint = VisualPreferences.eventElement_Event_Text_Paint;
        paint.getTextBounds(text, 0, text.length(), bounds);
        return (bounds.width());
    }
    
}
