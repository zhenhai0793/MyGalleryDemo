package com.ahai.demo.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.ahai.demo.mycinema.R;

/**
 * Created by zhenhai.fzh on 17/3/14.
 */

public class MyAnimActivity extends Activity implements View.OnClickListener {

    String TAG = "MyAnimActivity";

    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_anim_activity);
        imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setImageResource(R.drawable.image2);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button) {
            startAnim();
        }
    }

    private void startAnim() {
        Log.d(TAG, "startAnim");
        int xEnd = imageView.getLeft();
        int xStart = imageView.getRight();
        int toXDelta = xEnd - xStart;
        Log.d(TAG, "xStart:"+xStart+", xEnd:"+xEnd+", toXDelta:"+toXDelta);
        Animation anim = makeTranslateAnimation(0, toXDelta, 0, 0, true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "onAnimationEnd");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, "onAnimationRepeat");
            }
        });
        imageView.startAnimation(anim);
    }

    private Animation makeTranslateAnimation(float fromX, float toX, float fromY, float toY, boolean fillAfter) {
        Animation animation = new TranslateAnimation(fromX, toX, fromY, toY);
        animation.setDuration(500);
        animation.setFillEnabled(true);
        animation.setFillAfter(fillAfter);
        animation.setInterpolator(new LinearInterpolator());
        return animation;
    }
}
