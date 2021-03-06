package com.ahai.demo.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
//import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
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

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        int left = child.getLeft();
        int paddingLeft = child.getPaddingLeft();
        int paddingRight = child.getPaddingRight();
        int right = child.getRight();
        int width = child.getWidth();
        String id = "@"+child.hashCode();
        boolean bool = super.getChildStaticTransformation(child, t);
        Log.d(TAG, "ChildInfo id:"+id+", left:"+left+", right:"+right
                +", width:"+width+", paddingLeft:"+paddingLeft+", paddingRight:"+paddingRight+", bool:"+bool);
        return bool;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG, "onLayout changed:"+changed+", l:"+l+", r:"+r+", t:"+t+", b:"+b);
    }

    /*private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }*/

    /*@Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int kEvent;
        if (isScrollingLeft(e1, e2)) {
            // Check if scrolling left
            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            // Otherwise scrolling right
            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
        }

        onKeyDown(kEvent, null);
        return true;
    }*/
}
