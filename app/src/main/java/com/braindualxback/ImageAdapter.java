package com.braindualxback;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.GridView;

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

        if(MainActivity.ENABLE2_LOGS) Log.d(MainActivity.TAG, "public View getView. position: " + position);

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(picture_size, picture_size));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);

            if  (MainActivity.mImageViews[position] == null) {
                if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "imageView.setId: " + position);

                //imageView.setImageBitmap(decodeSampledBitmapFromResource(imageView.getResources(), MainActivity.NewArray[0], picture_size, picture_size));
                //imageView.setImageResource(MainActivity.NewArray[0]);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inDither = false;
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inSampleSize = 3;

                Bitmap icon = BitmapFactory.decodeResource(parent.getResources(),
                        MainActivity.NewArray[0],options);

                imageView.setImageBitmap(icon);

                MainActivity.mImageViews[position] = imageView;
            }else{
                if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "(MainActivity.mImageViews[position] != null?? position: " + position);
            }

            imageView.setId(position);
        } else {
            imageView = (ImageView) convertView;
        }

        int color = Color.parseColor(MainActivity.ImageFilterColour);
        imageView.setColorFilter(color);
        //imageView.setAnimation();

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

}