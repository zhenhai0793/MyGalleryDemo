package com.ahai.demo.gallery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import net.qiujuer.genius.blur.StackBlur;

import java.util.LinkedList;

import com.ahai.demo.mycinema.R;

/**
 * Created by zhenhai.fzh on 17/3/10.
 */

public class MyGalleryActivity extends Activity {

    String TAG = "MyGalleryActivity";
    Gallery mGallery;
    GalleryAdapter mGalleryAdapter;
    ImageView mImageView;
    LinkedList<Integer> list = new LinkedList();
    Handler mHandler = new Handler();

    public void initList() {
        list.add(R.drawable.image1);
        list.add(R.drawable.image3);
        list.add(R.drawable.image4);
        list.add(R.drawable.image5);
        list.add(R.drawable.image6);
        list.add(R.drawable.image7);
        list.add(R.drawable.image8);
        list.add(R.drawable.image9);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();
        setContentView(R.layout.my_gallery_activity);
        mGallery = (Gallery) findViewById(R.id.gallery);
        mGalleryAdapter = new GalleryAdapter();
        mGallery.setAdapter(mGalleryAdapter);
        mImageView = (ImageView) findViewById(R.id.main_bg);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image2);
        Bitmap blurBitmap = StackBlur.blur(bitmap, 30, false);
        mImageView.setImageBitmap(blurBitmap);
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged hasFocus:"+hasFocus);
        mGalleryAdapter.notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onItemRemoved(); // 下滑删除item
        void onItemJumpDetail(); // 上滑item跳转到详情页
    }

    private Animation makeTranslateAnimation(float fromX, float toX, float fromY, float toY, boolean fillAfter) {
        Animation animation = new TranslateAnimation(fromX, toX, fromY, toY);
        animation.setDuration(1000);
        animation.setFillEnabled(true);
        animation.setFillAfter(fillAfter);
        animation.setInterpolator(new LinearInterpolator());
        return animation;
    }

    class GalleryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private void setLeftRightValue() {

        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            Log.d(TAG, "getView parent: " + parent.getClass().getName());
            Log.d(TAG, "parent.width:" + parent.getWidth() + ", parent.height:" + parent.getHeight());
            if(convertView == null) {
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.my_gallery_item, null);
            }
            final View rootView = convertView;
            rootView.setLayoutParams(new Gallery.LayoutParams(parent.getWidth() - 520, parent.getHeight()));
            ImageView imageView = (ImageView)rootView.findViewById(R.id.image_view);
            imageView.setImageResource(list.get(position));
            Log.d(TAG, "getView left:"+rootView.getLeft()+", right:"+rootView.getRight()+", width:"+rootView.getWidth());
            MyLinearLayout myLinearLayout = (MyLinearLayout)rootView.findViewById(R.id.my_linear_layout);
            myLinearLayout.setOnItemListener(new OnItemListener() {
                @Override
                public void onItemRemoved() {
                    // 方法一:简单实现删除之后0.5秒刷新adapter
                    /*{
                        list.remove(position);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        }, 500);
                    }*/
                    // 方法二:使用ValueAnimator实现平滑移动
                    /*{
                        int start = parent.getWidth()-120;
                        int end = 0;
                        ValueAnimator animator = ValueAnimator.ofInt(start, end);
                        animator.addUpdateListener(
                                new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        int value = (Integer) valueAnimator.getAnimatedValue();
                                        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
                                        Log.d(TAG, "onAnimationUpdate oldWidth:"+layoutParams.width+", newWidth:"+value);
                                        layoutParams.width = value;
                                        {
                                            rootView.setLeft(60);
                                            rootView.setRight(60+value);

                                            View firstChild = mGallery.getChildAt(0);
                                            int right = rootView.getLeft();
                                            firstChild.setLeft(right - firstChild.getWidth());
                                            firstChild.setRight(right);

                                            View lastChild = mGallery.getChildAt(2);
                                            int left = rootView.getRight();
                                            lastChild.setLeft(left);
                                            lastChild.setRight(left + lastChild.getWidth());
                                        }
                                        rootView.setLayoutParams(layoutParams);
                                        {
                                            int numChildren = mGallery.getChildCount();
                                            for(int i=0;i<numChildren;i++) {
                                                View child = mGallery.getChildAt(i);
                                                Log.d(TAG, "child("+i+"/"+numChildren+") left:"+child.getLeft()+", right:"+child.getRight()+", width:"+child.getWidth());
                                            }
                                        }
                                    }
                                }
                        );
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                //list.remove(position);
                                //notifyDataSetChanged();
                            }
                        });
                        animator.start();
                    }*/
                    // 方法三:调用gallery.setSelection方法
                    /*{
                        Gallery gallery = (Gallery) parent;
                        if(position != getCount() - 1) {
                            gallery.setSelection(position + 1);
                        } else {
                            if(position != 0) {
                                gallery.setSelection(position - 1);
                            }
                        }
                        list.remove(position);
                        notifyDataSetChanged();
                    }*/
                    // 方法四:右边的item往左平移
                    {
                        Log.d(TAG, "getView rootView "+rootView.getClass().getSimpleName()+"@"+rootView.hashCode());
                        int numChildren = mGallery.getChildCount();
                        int nextIndex = -1;
                        View nextChild = null;
                        for(int i=0;i<numChildren;i++) {
                            View child = mGallery.getChildAt(i);
                            Log.d(TAG, "child("+i+"/"+numChildren+") "+child.getClass().getSimpleName()+"@"+child.hashCode());
                            if(child.hashCode() == rootView.hashCode()) {
                                if(i < numChildren - 1) {
                                    nextIndex = i + 1;
                                } else if(i > 0) {
                                    nextIndex = i - 1;
                                } else {
                                    nextIndex = -1;
                                }
                                Log.d(TAG, "nextIndex:"+nextIndex);
                                if(nextIndex != -1) {
                                    nextChild = mGallery.getChildAt(nextIndex);
                                    Log.d(TAG, "nextChild:"+child.getClass().getSimpleName()+"@"+child.hashCode());
                                }
                                break;
                            }
                        }
                        if(nextChild != null) {
                            int[] loc = new int[2];
                            rootView.getLocationOnScreen(loc);
                            int xEnd = loc[0];
                            nextChild.getLocationOnScreen(loc);
                            int xStart = loc[0];
                            Log.d(TAG, "xEnd:"+xEnd+", xStart:"+xStart);
                            int toXDelta = xEnd - xStart;
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
                             nextChild.startAnimation(anim);

                            // 方法五:只能在父布局(即gallery item内部)滑动
//                            MyLinearLayout myLinearLayout = (MyLinearLayout)nextChild.findViewById(R.id.my_linear_layout);
//                            myLinearLayout.doScrollX(xStart - xEnd);
                        }
                    }
                }

                @Override
                public void onItemJumpDetail() {
                    Gallery.LayoutParams layoutParams = (Gallery.LayoutParams) rootView.getLayoutParams();
                    if(layoutParams.width != parent.getWidth()) {
                        layoutParams.width += 120;
                        rootView.setLayoutParams(layoutParams);
                    }
                }
            });
            return rootView;
        }
    }
}
