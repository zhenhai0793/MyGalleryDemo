package com.ahai.demo.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.ahai.demo.mycinema.R;

public class MyFlingActivity extends Activity {

    String TAG = "MyFlingActivity";

    GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fling);
        initGestureDetector();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private void initGestureDetector() {
        mGestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float minMove = 60;         //最小滑动距离
                float minVelocity = 0;      //最小滑动速度
                float beginX = e1.getX();
                float endX = e2.getX();
                float beginY = e1.getY();
                float endY = e2.getY();

                if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity) {   //左滑
                    Log.d(TAG, "左滑速度:" + velocityX);
                } else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity) {   //右滑
                    Log.d(TAG, "右滑速度:" + velocityX);
                } else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) {   //上滑
                    Log.d(TAG, "上滑速度:" + velocityY);
                } else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) {   //下滑
                    Log.d(TAG, "下滑速度:" + velocityY);
                }
                return false;
            }
        });
    }
}
