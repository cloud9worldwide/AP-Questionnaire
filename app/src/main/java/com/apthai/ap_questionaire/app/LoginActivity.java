package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoginActivity extends Activity implements OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    ImageView imgLogo;
    EditText txtUsername, txtPassword,txtIP;
    ImageButton btnLogin;
    TextView btnForgot,txtError,btnSyncGEO;
    questionniare_delegate delegate;
    private static Context ctx;
    private static boolean txtfocus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ctx = this;
        delegate = (questionniare_delegate)getApplicationContext();

        if(delegate.service.getLg().equals("en")){
            delegate.setLocale("en");
        } else {
            delegate.setLocale("th");
        }

        setObject();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String nowDate = sdf.format(c.getTime());

        if(delegate.service.getLoginStatus() ){
            if(delegate.service.globals.getDateLastLogin().equals(nowDate)){
                Intent i = new Intent(this, ProjectsActivity.class);
                startActivityForResult(i,0);
            } else {
                delegate.service.Logout();
            }

        }
        startAnimateLogo();
//        txtUsername.setText("admin");
//        txtPassword.setText("password");

    }




    private void setObject(){
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (ImageButton) findViewById(R.id.btnLogin);
        btnForgot = (TextView) findViewById(R.id.btnForgot);
        txtIP = (EditText) findViewById(R.id.txtIP);
        btnSyncGEO = (TextView) findViewById(R.id.btnSyncGEO);

        txtError = (TextView) findViewById(R.id.txtError);
        txtError.setText("");
        txtError.setTypeface(delegate.font_type);
        txtError.setTextSize(25);

        txtIP.setTypeface(delegate.font_type);
        txtIP.setTextSize(30);
        txtIP.setVisibility(View.GONE);
        btnSyncGEO.setTypeface(delegate.font_type);
        btnSyncGEO.setTextSize(30);
        btnSyncGEO.setVisibility(View.GONE);
        btnSyncGEO.setOnClickListener(this);

        txtUsername.setTextSize(30);
        txtUsername.setTypeface(delegate.font_type);
        txtPassword.setTextSize(30);
        txtPassword.setTypeface(delegate.font_type);
        txtUsername.setHint(R.string.username);
        txtPassword.setHint(R.string.password);

        btnForgot.setTypeface(delegate.font_type);
        btnForgot.setTextSize(30);

        txtPassword.setAlpha(0);
        txtUsername.setAlpha(0);

        btnLogin.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(this);

        btnForgot.setAlpha(0);
        btnForgot.setOnClickListener(this);

        /*
        txtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        */
        txtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        txtIP.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    if(keyCode ==KeyEvent.KEYCODE_ENTER){
                        delegate.service.setWebserviceUrl(txtIP.getText().toString().trim());
                        hideConfig();
                    }
                    return true;
                }
                return false;
            }
        });


        if(delegate.service.getLg().equals("en")){
            btnLogin.setImageResource(R.drawable.login_btn_);
        } else {
            btnLogin.setImageResource(R.drawable.btn_th_login);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

    private void showConfig(){
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtPassword.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(txtUsername.getWindowToken(), 0);

            txtIP.setVisibility(View.VISIBLE);
            btnSyncGEO.setVisibility(View.VISIBLE);
            txtIP.setText(delegate.service.getWebserviceUrl());

    }

    private void hideConfig(){


        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtPassword.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(txtUsername.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(txtIP.getWindowToken(), 0);

        txtIP.setVisibility(View.GONE);
        btnSyncGEO.setVisibility(View.GONE);
        txtIP.setText(delegate.service.getWebserviceUrl());
    }

    public void onClick(View v) {


        if(v.getId() == R.id.btnLogin && txtUsername.getText().toString().equals("apqtn") && txtPassword.getText().toString().equals("ntqpa")){
            if(txtIP.isShown()) {
                hideConfig();
            } else {
                showConfig();
            }

        } else if (v.getId() == R.id.btnLogin && txtUsername.getText().length() > 0 && txtPassword.getText().length() > 0) {
            hideConfig();

            final ProgressDialog ringProgressDialog = ProgressDialog.show(ctx, "Please wait ...", "Login...", true);
            ringProgressDialog.setCancelable(false);

            final Handler uiHandler = new Handler(); // anything posted to this handler will run on the UI Thread
            System.out.println("LoadingScreenActivity  screen started");

            final  Runnable onUi = new Runnable() {
                @Override
                public void run() {
                    // this will run on the main UI thread
                    txtError.setText(delegate.service.getLoginMessage());
                }
            };

            Runnable background = new Runnable() {
                @Override
                public void run() {
                    if (delegate.service.Login(txtUsername.getText().toString() , txtPassword.getText().toString()) == true) {
                        if (delegate.service.checkUpdateQuestionnaire()) {
                            //ringProgressDialog.setMessage("Downloading...Questionnaire Data");
                            delegate.service.updateQuestionnaire(ringProgressDialog);
                            delegate.service.startDownloadImages(ringProgressDialog);
                            //ringProgressDialog.setMessage("Downloading...All images Data");

                            try {
                                Thread.sleep(3000);
                            }catch (Exception e) {
                            }
                            ringProgressDialog.dismiss();

                            startActivityForResult(new Intent(LoginActivity.this , ProjectsActivity.class),0);
                        } else {
                            delegate.service.startDownloadImages(ringProgressDialog);
                            try {
                                Thread.sleep(3000);
                            }catch (Exception e){

                            }
                            ringProgressDialog.dismiss();
                            startActivityForResult(new Intent(LoginActivity.this , ProjectsActivity.class),0);
                        }
                    } else {
                        ringProgressDialog.dismiss();
                        uiHandler.post( onUi );
                    }
                }
            };

            new Thread( background ).start();


        }
        else if(v.getId() == R.id.btnForgot){
            startActivityForResult(new Intent(this, ResetPasswordActivity.class),0);
        } else if(v.getId() == R.id.btnSyncGEO){
            hideConfig();
            delegate.service.sync_geo_data(this);

        }
    }

    private void startAnimateLogo(){

        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, delegate.dpToPx(-175));
        anim.setFillAfter(true);
        anim.setDuration(1000);

        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                SystemClock.sleep(2000);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                txtPassword.setAlpha(1);
                txtUsername.setAlpha(1);
                btnForgot.setAlpha(1);
                btnLogin.setVisibility(View.VISIBLE);
                Animation fadeInAnimation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.animate);
                fadeInAnimation.setFillAfter(true);
                fadeInAnimation.setDuration(2500);

                txtUsername.startAnimation(fadeInAnimation);
                txtPassword.startAnimation(fadeInAnimation);
                btnLogin.startAnimation(fadeInAnimation);
                btnForgot.startAnimation(fadeInAnimation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imgLogo.startAnimation(anim);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==4){
            this.setResult(resultCode);
            finish();
        }
    }

    public void onBackPressed() {
        delegate.service.Logout();
        this.setResult(4);
        finish();
    }



}
