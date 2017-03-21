package com.zeitraub.calendar.view.elements;

/**
 * Event element with layout data
 * 
 * @author Torsten
 *
 */
public class EventViewElement {

    public long id;
    public long begin;
    public long end;
    public int top;
    public int left;
    public int bottom;
    public int right;
    public String title;
    public int color;
    public long unique;
    public boolean isInstance;
    public String calendar;
    
    public EventViewElement(long _id, long _begin, long _end, int _left, int _top, int _bottom, int _right, String _title, int _color, long _unique, boolean _isInstance, String _calendar) {
       id = _id;
       begin = _begin;
       end = _end;
       top = _top;
       left = _left;
       bottom = _bottom;
       right = _right;
       title = _title;
       color = _color;
       unique = _unique;
       isInstance = _isInstance;
       calendar = _calendar;
    }
   
}
