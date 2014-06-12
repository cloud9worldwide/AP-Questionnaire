package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends Activity implements OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    ImageView imgLogo;
    EditText txtUsername, txtPassword;
    ImageButton btnLogin;
    TextView btnForgot,txtError;
    questionniare_delegate delegate;
    private static Context ctx;
    private static boolean txtfocus = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx = this;
        delegate = (questionniare_delegate)getApplicationContext();

        setObject();
        startAnimateLogo();
//        txtUsername.setText("admin");
//        txtPassword.setText("password");

    }
    private void setObject(){
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        //txtUsername.setInputType(0);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        //txtPassword.setInputType(0);
        btnLogin = (ImageButton) findViewById(R.id.btnLogin);
        btnForgot = (TextView) findViewById(R.id.btnForgot);

        txtError = (TextView) findViewById(R.id.txtError);
        txtError.setText("");
        txtError.setTypeface(delegate.font_type);
        txtError.setTextSize(25);

        txtUsername.setTypeface(delegate.font_type);
        txtUsername.setTextSize(30);
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

    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin && txtUsername.getText().length() > 0 && txtPassword.getText().length() > 0) {

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
                    // This is the delay
                    if (delegate.service.Login(txtUsername.getText().toString() , txtPassword.getText().toString()) == true) {
                        if (delegate.service.checkUpdateQuestionnaire()) {
                            //ringProgressDialog.setMessage("Downloading...Questionnaire Data");
                            delegate.service.updateQuestionnaire(ringProgressDialog);
                            //ringProgressDialog.setMessage("Downloading...All images Data");
                            delegate.service.startDownloadImages(ringProgressDialog);
                            try {
                                Thread.sleep(3000);
                            }catch (Exception e) {
                            }
                            ringProgressDialog.dismiss();
                            startActivityForResult(new Intent(LoginActivity.this , ProjectsActivity.class),0);
                        } else {
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
                    //
                }
            };

            new Thread( background ).start();


        }
        else if(v.getId() == R.id.btnForgot){
            startActivityForResult(new Intent(this, ResetPasswordActivity.class),0);
        }
    }
    private void startAnimateLogo(){
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -200);
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
