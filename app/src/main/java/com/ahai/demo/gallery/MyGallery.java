package com.ahai.demo.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;

/**
 * Created by zhenhai.fzh on 17/3/11.
 */

public class MyGallery extends Gallery {

    private String TAG = "MyGallery";

    private float downRawX;
    private float downRawY;
    private boolean isMove;
    private boolean bIntercept;

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
    public boolean onInterceptTouchEvent(MotionEvent event) {

        float rawX = event.getRawX();
        float rawY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downRawX = rawX;
                downRawY = rawY;

                isMove = false;
                bIntercept = false;

                onTouchEvent(event);

                Log.d(TAG, "action_down isMove:"+isMove+", bIntercept:"+bIntercept);

                break;

            case MotionEvent.ACTION_MOVE: {

                if(!isMove) {
                    float dx = Math.abs(rawX - downRawX);
                    float dy = Math.abs(rawY - downRawY);
                    Log.d(TAG, "beginMove dx:" + dx+", dy:"+dy);
                    if(dx == 0 && dy == 0) {
                        break;
                    } else {
                        isMove = true;
                        if (dx > dy) {
                            Log.d(TAG, "dx > dy onInterceptTouchEvent result:true");
                            bIntercept = true;
                        }
                    }
                }

                Log.d(TAG, "action_move isMove:"+isMove+", bIntercept:"+bIntercept);

                break;
            }

            case MotionEvent.ACTION_UP: {

                break;
            }
        }
//        boolean result = super.onInterceptTouchEvent(event);
//        Log.d(TAG, "onInterceptTouchEvent result:"+result);
        return bIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "action_down";
                break;
            case MotionEvent.ACTION_MOVE:
                action = "action_move";
                break;
            case MotionEvent.ACTION_UP:
                action = "action_up";
                break;
        }
        Log.d(TAG, "onTouchEvent result:"+result+", type:"+action);
        return result;
    }
}
