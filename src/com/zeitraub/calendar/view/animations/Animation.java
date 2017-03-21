package com.zeitraub.calendar.view.animations;

/**
 * Custom animation
 * 
 * @author Torsten
 *
 */
public class Animation {

    private long mStartLongValue;
    private double mStartFloatValue;
    
    private long mEndLongValue;
    private double mEndFloatValue;
    
    private long mDuration;
    private long mTimeStart;
    
    private boolean mIsInvert;
    
    public Animation() {
        mStartLongValue = 0;
        mEndLongValue = 0;
        mDuration = 0;
        mIsInvert = false;
    }
    
    public void prepareAnimation(long startValue, long endValue, long duration, boolean invert) {
        mStartLongValue = startValue;
        mEndLongValue = endValue;
        mDuration = duration;
        mIsInvert = invert;
    }
    
    public void prepareAnimation(double startValue, double endValue, long duration, boolean invert) {
        mStartFloatValue = startValue;
        mEndFloatValue = endValue;
        mDuration = duration;
        mIsInvert = invert;
    }
    
    public void prepareAnimation(double startValue, double endValue, double duration, boolean invert) {
        mStartFloatValue = startValue;
        mEndFloatValue = endValue;
        mDuration = (long) duration;
        mIsInvert = invert;
    }
    
    public void start(long timeNow) {
        mTimeStart = timeNow;
    }
    
    public long getCurrentLongValue(long currentMillis) {
        if (currentMillis > (mTimeStart + mDuration)) {
            return mEndLongValue;
        }
        return (mStartLongValue + (((mEndLongValue - mStartLongValue) / mDuration) * (currentMillis - mTimeStart)));
    }
    
    public double getCurrentFloatValue(long currentMillis) {
        if (currentMillis > (mTimeStart + mDuration)) {
            return mEndFloatValue;
        }
        return (mStartFloatValue + (((mEndFloatValue - mStartFloatValue) / mDuration) * (currentMillis - mTimeStart)));
    }
    
    public double getTimePassed(long currentMillis) {
        if (currentMillis > (mTimeStart + mDuration)) {
            return mDuration;
        }
        return (mDuration - (currentMillis - mTimeStart));
    }
    
    public double getCurrentPercentageValue(long currentMillis) {
        if (currentMillis > (mTimeStart + mDuration)) {
            if (mIsInvert) {
                return 0f;  
            } 
            return 1f;
        }
        if (mIsInvert) {
            return 1f - (1f / mDuration *(currentMillis-mTimeStart));  
        } 
        return (1f / mDuration *(currentMillis-mTimeStart));
    }
    
    public boolean isRunning(long currentMillis) {
        return (currentMillis <= (mTimeStart + mDuration));
    }
    
}
