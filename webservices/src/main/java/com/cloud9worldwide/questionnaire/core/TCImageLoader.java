package com.cloud9worldwide.questionnaire.core;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cloud9 on 5/8/14.
 */
public class TCImageLoader implements ComponentCallbacks2 {
    private TCLruCache cache;

    public TCImageLoader(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass() * 1024 * 1024;
        cache = new TCLruCache(memoryClass);
    }

    public void display(String url,String width,String height, ImageView imageview, int defaultresource) {
        imageview.setImageResource(defaultresource);
        Bitmap image = cache.get(url);
        if (image != null) {
            imageview.setImageBitmap(image);
        }
        else {
            new SetImageTask(imageview).execute(url,width,height);
        }
    }

    private class TCLruCache extends LruCache<String, Bitmap> {

        public TCLruCache(int maxSize) {
            super(maxSize);
        }
    }

    private class SetImageTask extends AsyncTask<String, Void, Integer> {
        private ImageView imageview;
        private Bitmap bmp;

        public SetImageTask(ImageView imageview) {
            this.imageview = imageview;
        }
        public String md5(String s) {
            try {
                // Create MD5 Hash
                MessageDigest digest = java.security.MessageDigest
                        .getInstance("MD5");
                digest.update(s.getBytes());
                byte messageDigest[] = digest.digest();
                // Create Hex String
                StringBuffer hexString = new StringBuffer();
                for (int i = 0; i < messageDigest.length; i++)
                    hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
                return hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }
        @Override
        protected Integer doInBackground(String... params) {
            String img_url = params[0];
            int w = Integer.parseInt(params[1]);
            int h = Integer.parseInt(params[2]);

            File root = new File(Environment.getExternalStorageDirectory(), "Questionnaire");
            if (!root.exists()) {
                return  null;
            }
            File imgPath = new File(root,"downloadimgs");
            if (!imgPath.exists()) {
                return null;
            }
            String fileName = this.md5(img_url);
            File imgFile = new File(imgPath, fileName+".jpg");

            try {
                //bmp = getBitmapFromURL(img_url);
                bmp = getBitmapFromFile(imgFile.getAbsolutePath(),w,h);
                if (bmp != null) {
                    cache.put(img_url, bmp);
                }
                else {
                    return 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                imageview.setImageBitmap(bmp);
            }
            super.onPostExecute(result);
        }

        private Bitmap getBitmapFromURL(String src) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection
                        = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        private Bitmap getBitmapFromFile(String filepath,int w,int h){
            Bitmap bitmap = null;
            BitmapFactory.Options bmOptions= new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds= true;
            BitmapFactory.decodeFile(filepath, bmOptions);
            int photoW= bmOptions.outWidth;
            int photoH= bmOptions.outHeight;

// Determine how much to scale down the image.
            int scaleFactor= (int) Math.max(1.0, Math.min((double) photoW / (double)w, (double)photoH / (double)h));    //1, 2, 3, 4, 5, 6, ...
            scaleFactor= (int) Math.pow(2.0, Math.floor(Math.log((double) scaleFactor) / Math.log(2.0)));               //1, 2, 4, 8, ...

// Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds= false;
            bmOptions.inSampleSize= scaleFactor;
            bmOptions.inPurgeable= true;

            do
            {
                try
                {
                    Log.d("tag", "scaleFactor: " + scaleFactor);
                    scaleFactor*= 2;
                    bitmap= BitmapFactory.decodeFile(filepath, bmOptions);
                }
                catch(OutOfMemoryError e)
                {
                    bmOptions.inSampleSize= scaleFactor;
                    Log.d("tag", "OutOfMemoryError: " + e.toString());
                }
            }
            while(bitmap == null && scaleFactor <= 256);

            if(bitmap == null)
                return null;
            return bitmap;
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

    @Override
    public void onLowMemory() {
    }


    @Override
    public void onTrimMemory(int level) {
        if (level >= TRIM_MEMORY_MODERATE) {
            cache.evictAll();
        }
        else if (level >= TRIM_MEMORY_BACKGROUND) {
            //cache.trimToSize(cache.size() / 2);

        }
    }
}