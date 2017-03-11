package com.ahai.demo.gallery;

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
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import net.qiujuer.genius.blur.StackBlur;

import java.util.LinkedList;

import nsouth.jonas.android.R;

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
        void onItemRemoved();
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "getView.parent: " + parent.getClass().getName());
            Log.d(TAG, "parent.width:"+parent.getWidth()+", parent.height:"+parent.getHeight());
            View rootView = LayoutInflater.from(getBaseContext()).inflate(R.layout.my_gallery_item, null);
            rootView.setLayoutParams(new Gallery.LayoutParams(parent.getWidth()-120, parent.getHeight()));
            ImageView imageView = (ImageView)rootView.findViewById(R.id.image_view);
            imageView.setImageResource(list.get(position));
            MyLinearLayout myLinearLayout = (MyLinearLayout)rootView.findViewById(R.id.my_linear_layout);
            myLinearLayout.setOnItemListener(new OnItemListener() {
                @Override
                public void onItemRemoved() {
                    list.remove(position);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    }, 500);
                }
            });
            return rootView;
        }
    }
}
