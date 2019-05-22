package com.example.corbi.dartevent;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
    private boolean enabled;

    public CustomViewPager(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        enabled = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(enabled){
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        if(enabled){
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    public void setPagingEnabled(boolean enabled){
        this.enabled = enabled;
    }
}
