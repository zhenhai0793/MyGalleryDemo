package com.ahai.demo.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Scroller;

/**
 * Created by zhenhai.fzh on 17/3/10.
 */

public class MyImageView extends ImageView {

    private String TAG = "MyImageView";

    private int downY;
    private float downRawY;
    private float upRawY;
    private Scroller mScroller;
    private long moveTime;
    private long upTime;
    private float moveY;
    private float upY;

    public MyImageView(Context context) {
        super(context);
        mScroller = new Scroller(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        float rawY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downY = y;
                downRawY = rawY;

                moveTime = event.getEventTime();
                moveY = event.getY();

                break;

            case MotionEvent.ACTION_MOVE: {

                moveTime = event.getEventTime();
                moveY = event.getY();

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
        return true;
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
