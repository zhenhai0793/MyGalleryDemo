package com.ahai.demo.gallery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import net.qiujuer.genius.blur.StackBlur;

import nsouth.jonas.android.R;

/**
 * Created by zhenhai.fzh on 17/3/10.
 */

public class MyGalleryActivity extends Activity {

    String TAG = "MyGalleryActivity";
    Gallery mGallery;
    ImageView mImageView;
    int[] mImageArray = new int[]{
            R.drawable.image1,
//            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image7,
            R.drawable.image8,
            R.drawable.image9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_gallery_activity);
        mGallery = (Gallery) findViewById(R.id.gallery);
        mGallery.setAdapter(new GalleryAdapter());
        mImageView = (ImageView) findViewById(R.id.main_bg);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image2);
        Bitmap blurBitmap = StackBlur.blur(bitmap, 30, false);
        mImageView.setImageBitmap(blurBitmap);
    }

    class GalleryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mImageArray.length;
        }

        @Override
        public Object getItem(int position) {
            return mImageArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "getView.parent: " + parent.getClass().getName());
            Log.d(TAG, "parent.width:"+parent.getWidth()+", parent.height:"+parent.getHeight());
            View rootView = LayoutInflater.from(getBaseContext()).inflate(R.layout.my_gallery_item, null);
            rootView.setLayoutParams(new Gallery.LayoutParams(parent.getWidth()-120, parent.getHeight()));
            ImageView imageView = (ImageView)rootView.findViewById(R.id.image_view);
            imageView.setImageResource(mImageArray[position]);
            return rootView;
        }

//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            Log.d(TAG, "getView.parent: " + parent.getClass().getName());
//            Log.d(TAG, "parent.width:"+parent.getWidth()+", parent.height:"+parent.getHeight());
//            ImageView imageView = new ImageView(getBaseContext());
//            imageView.setLayoutParams(new Gallery.LayoutParams(parent.getWidth()-120, parent.getHeight()));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setImageResource(mImageArray[position]);
//            return imageView;
//        }
    }
}
