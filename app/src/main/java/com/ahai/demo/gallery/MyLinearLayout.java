package com.ahai.demo.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by zhenhai.fzh on 17/3/11.
 */

public class MyLinearLayout extends LinearLayout {

    private String TAG = "MyLinearLayout";

    private int downX;
    private int downY;
    private float downRawX;
    private float downRawY;
    private float upRawY;
    private Scroller mScroller;
    private long moveTime;
    private long upTime;
    private float moveY;
    private float upY;
    private boolean isMove;
    private int dragOrientation;

    public MyLinearLayout(Context context) {
        super(context);
        mScroller = new Scroller(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        float rawX = event.getRawX();
        float rawY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downX = x;
                downY = y;
                downRawX = rawX;
                downRawY = rawY;

                moveTime = event.getEventTime();
                moveY = event.getY();

                isMove = false;

                Log.d(TAG, "action_down isMove:"+isMove);

                break;

            case MotionEvent.ACTION_MOVE: {

                moveTime = event.getEventTime();
                moveY = event.getY();

                if(!isMove) {
                    float dx = Math.abs(rawX - downRawX);
                    float dy = Math.abs(rawY - downRawY);
                    Log.d(TAG, "beginMove dx:" + dx+", dy:"+dy);
                    if(dx == 0 && dy == 0) {
                        break;
                    } else {
                        isMove = true;
                        if (dx > dy) {
                            dragOrientation = HORIZONTAL;
                            break;
                        } else {
                            dragOrientation = VERTICAL;
                        }
                    }
                }

                Log.d(TAG, "action_move isMove:"+isMove+", dragOrientation:"+dragOrientation);

                int offX = 0;
                int offY = y - downY;

                layout(getLeft() + offX, getTop() + offY, getRight() + offX, getBottom() + offY);

                updateMyAlpha(rawY);

                break;
            }

            case MotionEvent.ACTION_UP: {

                upRawY = rawY;
                int offY = (int)(downRawY - upRawY);

                if(offY < 0) {
                    // 向上滑回
                    scrollBack(offY);
                } else {
                    upTime = event.getEventTime();
                    upY = event.getY();

                    long dt = upTime - moveTime;
                    float dy = upY - moveY;
                    float vy = dy / dt;
                    Log.d(TAG, "fling dt:"+dt+", dy:"+dy+", vy:"+vy+", bottomY:"+getBottom());

                    if(vy > 0.1) {
                        // 向上滑出
                        View parent = (View) getParent();
                        mScroller.startScroll(parent.getScrollX(), parent.getScrollY(), 0, getBottom(), 500);
                    } else {
                        // 向下滑回
                        scrollBack(offY);
                    }
                }
                break;
            }
        }

//        boolean result = !isMove || dragOrientation == VERTICAL;
        boolean result = isMove && dragOrientation == VERTICAL;
        Log.d(TAG, "onTouchEvent result:"+result);
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = super.onInterceptTouchEvent(ev);
        Log.d(TAG, "onInterceptTouchEvent result:"+result);
        return result;
    }

    private void scrollBack(int offY) {
        View parent = (View) getParent();
        mScroller.startScroll(parent.getScrollX(), parent.getScrollY(), 0, -offY, 500);
        updateMyAlpha(downRawY);
    }

    private void updateMyAlpha(float curRawY) {
        if(curRawY > downRawY) return;
        float height = getHeight();
        float moveHeight = Math.abs(curRawY - downRawY);
        int factor = 1;
        float alpha = 1-moveHeight/height*factor;
        Log.d(TAG, "height:"+height+", downRawY:"+downRawY+", curRawY:"+curRawY+", moveHeight:"+moveHeight+", alpha:"+alpha);
        setAlpha(alpha);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()) {
            ((View)getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        }
        invalidate();
    }
}
