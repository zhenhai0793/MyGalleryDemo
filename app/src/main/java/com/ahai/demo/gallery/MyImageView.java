package com.ahai.demo.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by zhenhai.fzh on 17/3/10.
 */

public class MyImageView extends ImageView {

    private String TAG = "MyImageView";

    private int lastX;
    private int lastY;
    private int firstY;
    private float firstRawX;
    private float firstRawY;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取到手指处的横坐标和纵坐标
        int x = (int) event.getX();
        int y = (int) event.getY();
        float rawX = event.getRawX();
        float rawY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                lastX = x;
                lastY = y;
                firstY = y;
                firstRawX = rawX;
                firstRawY = rawY;

                break;

            case MotionEvent.ACTION_MOVE: {
                //计算移动的距离
//                int offX = x - lastX;
                int offX = 0;
                int offY = y - lastY;
                //调用layout方法来重新放置它的位置
                layout(getLeft() + offX, getTop() + offY,
                        getRight() + offX, getBottom() + offY);

                updateMyAlpha(rawY);

                break;
            }

            case MotionEvent.ACTION_UP: {
                int offX = 0;
                int offY = (int)(firstRawY - rawY);
                //调用layout方法来重新放置它的位置
//                layout(getLeft() + offX, getTop() + offY,
//                        getRight() + offX, getBottom() + offY);
                ((View)getParent()).scrollBy(0, -offY);
                updateMyAlpha(firstRawY);
                break;
            }
        }
        return true;
//        return super.onTouchEvent(event);
    }

    private void updateMyAlpha(float curRawY) {
        if(curRawY > firstRawY) return;
        float height = getHeight();
        float moveHeight = Math.abs(curRawY - firstRawY);
        int factor = 1;
        float alpha = 1-moveHeight/height*factor;
        Log.d(TAG, "height:"+height+", firstRawY:"+firstRawY+", curRawY:"+curRawY+", moveHeight:"+moveHeight+", alpha:"+alpha);
        setAlpha(alpha);
    }
}
