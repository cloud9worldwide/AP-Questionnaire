package com.cloud9worldwide.questionnaire.core;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by cloud9 on 4/2/14.
 */
public class QuestionnaireFS {
    private final Context mCtx;
    public QuestionnaireFS(Context _context) {
        this.mCtx = _context;
    }

    /**
     * before writing files also check whether your SDCard is Mounted & your external storage state is writable
     * - Environment.getExternalStorageState()
     * @param sFileName
     * @param sBody
     */
    public void generateFileOnSD(String sFileName, String sBody){
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "Questionnaire");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            //Toast.makeText(this.mCtx, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public String readFileOnSD(String rFileName){
        String _json_str = "";
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Questionnaire");
            if (!root.exists()) {
                return null;
            }
            File jsonfile = new File(root, rFileName);
            if(jsonfile.canRead()){
                StringBuilder text = new StringBuilder();
                BufferedReader br = new BufferedReader(new FileReader(jsonfile));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                _json_str = text.toString();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return _json_str;
    }
    public boolean checkFileIsExists(String sFileName){
        File root = new File(Environment.getExternalStorageDirectory(), "Questionnaire");
        if (!root.exists()) {
            return false;
        }
        File _file = new File(root, sFileName);
        if(_file.exists()){
            return true;
        }
        return false;
    }
}
