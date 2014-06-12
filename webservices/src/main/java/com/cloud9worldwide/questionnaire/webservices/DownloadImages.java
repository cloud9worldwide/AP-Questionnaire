package com.cloud9worldwide.questionnaire.webservices;

import android.app.ProgressDialog;
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
public class DownloadImages {
    private final  String debugTag = "DownloadImages";
    public static class ApiException extends Exception {
        private static final long serialVersionUID = 1L;
        public ApiException (String msg)
        {
            super (msg);
        }

        public ApiException (String msg, Throwable thr)
        {
            super (msg, thr);
        }
    }
    public static synchronized void download(ProgressDialog _pDailog,ArrayList<String> _imageUrls) throws ApiException {
        ArrayList<String> imgUrls = _imageUrls;
        int SIZE = imgUrls.size();
        for (int i = 0; i < SIZE; i++) {
            saveImage(imgUrls.get(i));
            String text = "Download image ["+(i+1)+ "/" + SIZE + "]";
            Log.d("Core", text);
            //_pDailog.setMessage(text);
        }
        /*
        DownloadImagesTask _task = new DownloadImagesTask(_pDailog);
        try {
            _task.execute(_imageUrls);
            _task.get();
        } catch (Exception e){
            _task.cancel(true);
            throw new ApiException("Problem connecting to the server " + e.getMessage(), e);
        }
        */
    }

    public static String md5(String s) {
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

    public static boolean saveImage(String fullURL) {
        final  String debugTag = "DownloadImages";
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
            String fileName = md5(fullURL);
            InputStream is = null;
            if (c.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                File outputFile = new File(imgPath, fileName+".jpg");
                FileOutputStream fos = new FileOutputStream(outputFile);
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
            }else{
                Log.e(debugTag, "GET FAIL : " + c.getResponseCode());
            }

            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
