package com.ahai.demo.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * Created by zhenhai.fzh on 17/3/11.
 */

public class MyGallery extends Gallery {

    private String TAG = "MyGallery";

    public MyGallery(Context context) {
        super(context);
    }

    public MyGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = super.onInterceptTouchEvent(ev);
        Log.d(TAG, "onInterceptTouchEvent result:"+result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        Log.d(TAG, "onTouchEvent result:"+result);
        return result;
    }
}
