package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;


public class MenuActivity extends Activity {

    RelativeLayout menu_home, menu_settings, menu_logout;
    Activity parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setObject();
    }
    private void setObject(){
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        menu_settings = (RelativeLayout) findViewById(R.id.menu_settings);
        menu_logout = (RelativeLayout) findViewById(R.id.menu_logout);

//        menu_home.setOnClickListener(parentView);
//        menu_settings.setOnClickListener(parentView);
    }

//    @Override
//    public void onClick(View v) {
//
//    }



}
