package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;


public class SplashActivity extends Activity {

    questionniare_delegate delegate;
    ImageView img;
    Integer duration;

    @Override
    protected void onResume() {
        super.onResume();
        if(duration !=500){
            duration =0;
        }
        img = (ImageView) findViewById(R.id.imgLogo);
        delegate = (questionniare_delegate)getApplicationContext();

        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
        anim.setFillAfter(true);
        anim.setDuration(duration);

        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                SystemClock.sleep(2000);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPreferences preferences = SplashActivity.this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

                String isInit = preferences.getString("isInitProvince","NO");
                Log.e("Splash ", isInit);
                if(isInit.equals("NO")){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("isInitProvince", "YES");
                    editor.commit();

                    Log.e("Splash ","Init ja");
                    delegate.service.initProvincesData(SplashActivity.this);
                    delegate.service.initDistrictData(SplashActivity.this);
                    delegate.service.initSubDistrictData(SplashActivity.this);
                    delegate.service.initCountryData(SplashActivity.this);
                    delegate.service.initNationalityData(SplashActivity.this);
                }

                if(delegate.service.getLoginStatus()){
                    Intent i = new Intent(SplashActivity.this, ProjectsActivity.class);
                    startActivityForResult(i,0);
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivityForResult(i,0);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img.startAnimation(anim);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        duration =500;

        delegate = (questionniare_delegate)getApplicationContext();







        //text 2 postcode

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==4){
            this.setResult(resultCode);
            finish();
        }
    }

}
