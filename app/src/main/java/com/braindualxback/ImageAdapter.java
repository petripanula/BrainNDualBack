package com.braindualxback;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.util.LruCache;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.Toast;


/**
 * Created by ppanula on 15.9.2015.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private int pics;
    private int picture_size;

    public ImageAdapter(Context c, int pictures, int pic_size) {
        mContext = c;
        pics = pictures;
        picture_size = pic_size;

    }


    //Number of elemens show in gridview...
    public int getCount() {

        return pics;
        //return Pictures.MEMORY_IDS.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        String newString;
        int in;

        if(MainActivity.ENABLE2_LOGS) Log.d(MainActivity.TAG, "public View getView. position: " + position);

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(picture_size, picture_size));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ///imageView.setPadding(6, 6, 6, 6);
            imageView.setPadding(1, 1, 1, 1);

            if  (MainActivity.mImageViews[position] == null) {
                if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "imageView.setId: " + position);
                MainActivity.mImageViews[position] = imageView;
            }

            imageView.setId(position);
        } else {
            imageView = (ImageView) convertView;
        }

        //Log.d(BabyMain.TAG, "getResources().getResourceEntryName: " + imageView.getResources().getResourceEntryName(Pictures.MEMORY_IDS[position]));

        imageView.setImageBitmap(decodeSampledBitmapFromResource(imageView.getResources(), MainActivity.NewArray[position], picture_size, picture_size));

        //imageView.setImageResource(MemoryGameActivity.NewArray[position]);
        int color = Color.parseColor("#FFFFFF");
        imageView.setColorFilter(color);

        return imageView;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /*
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };
    */
}