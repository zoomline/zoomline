package com.zeitraub.calendar.view.elements;

import com.zeitraub.calendar.view.elements.MenuElement.MenuType;

/**
 * Region element on screen 
 * 
 * @author Torsten
 *
 */
public class Region {

    public MenuType mType;
    public int mTop;
    public int mBottom;
    public int mLeft;
    public int mRight;
    
    public Region(MenuType type, int top, int left, int bottom, int right) {
        mType = type;
        mTop = top;
        mBottom = bottom;
        mLeft = left;
        mRight = right;    
    }
    
    public boolean isInsideRegion(float x, float y) {
        return (y >= mTop && y <= mBottom && x >= mLeft && x <= mRight);
    }
}
