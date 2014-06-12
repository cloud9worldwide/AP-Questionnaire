package com.cloud9worldwide.questionnaire.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by cloud9 on 4/4/14.
 */
public class DownloadImagesTask extends AsyncTask<ArrayList<String>, Integer,String> {
    private Context context;
    private ProgressDialog progDialog;
    private static final String debugTag = "APITask";
    private int SIZE = 0;
    public DownloadImagesTask(ProgressDialog _pDailog) {
        this.progDialog = _pDailog;
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

    public boolean saveImage(String fullURL) {
        try {

            URL url = new URL(fullURL.toLowerCase());
            try {
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath().replace("//","/"), url.getQuery(), url.getRef());
                url = uri.toURL();
            }catch (URISyntaxException ee){
                ee.printStackTrace();
            }


            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            //c.setDoOutput(true);
            c.connect();

            File root = new File(Environment.getExternalStorageDirectory(), "Questionnaire");
            if (!root.exists()) {
                root.mkdirs();
            }
            File imgPath = new File(root,"downloadimgs");
            if (!imgPath.exists()) {
                imgPath.mkdirs();
            }
            String fileName = this.md5(fullURL);
            File outputFile = new File(imgPath, fileName+".jpg");
            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = null;
            if (c.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
            }else{
                Log.e(debugTag,"GET FAIL : "+c.getResponseCode());
            }


//            InputStream is = c.getInputStream();
//            byte[] buffer = new byte[1024];
//            int len1 = 0;
//            while ((len1 = is.read(buffer)) != -1) {
//                fos.write(buffer, 0, len1);
//            }
//            fos.close();
//            is.close();
            // Toast.makeText(this, "Downloaded Successfully", 600).show();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    @Override
    protected String doInBackground(ArrayList<String>... data) {
        ArrayList<String> imgUrls = data[0];
        this.SIZE = imgUrls.size();
        for (int i = 0; i < SIZE; i++) {
            this.saveImage(imgUrls.get(i));
            publishProgress(i);
        }
        publishProgress(SIZE);
        return null;
    }

    @Override
    protected void onPreExecute() {
        //progDialog = ProgressDialog.show(this.context.getApplicationContext(), "DOWNLOAD QUESTIONNAIRE DATA", "Donwloading...", true, false);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        //progDialog.dismiss();
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        String text = "Download image ["+values[0]+ "/" + SIZE + "]";
        Log.d("Core", text);
        progDialog.setMessage(text);
        super.onProgressUpdate(values);
    }
}
