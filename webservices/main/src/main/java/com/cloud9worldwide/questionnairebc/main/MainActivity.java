package com.cloud9worldwide.questionnairebc.main;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cloud9worldwide.questionnaire.core.CoreEngine;
import com.cloud9worldwide.questionnaire.data.ProjectData;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    private static final String debugTag = "mainActivity";

    private ArrayList<ProjectData> _pdata;
    private static CoreEngine coreEngine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coreEngine = new CoreEngine(this);
        _pdata = new ArrayList<ProjectData>();
        if(coreEngine.Login("usertest","11111","udidtest") == true){
            Log.d(debugTag, coreEngine.getTokenAccess());
            Log.d(debugTag, "_pdata ::"+_pdata.size());
            _pdata = coreEngine.getProjects();
            Log.d(debugTag, "_pdata ::"+_pdata.get(0).getName());
        }else{
            Log.d(debugTag,coreEngine.getLoginMessage());
            //Log.d(debugTag, "Login fail");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
