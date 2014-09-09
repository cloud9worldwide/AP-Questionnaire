package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud9worldwide.questionnaire.data.ContactSearchData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class CustomerLookUpActivity extends Activity implements OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    ImageButton btn_menu, btnEN, btnTH, btn_send, btn_back;
    questionniare_delegate delegate;
    EditText txtFirstName,txtLastName,txtTel;
    TextView project_name,question_title;
    ImageView img_background;
    static PopupWindow popup;
    RelativeLayout root_view;

    TextView lbl_firstname, lbl_lastname, lbl_tel;
    private Context ctx;

    private void setImage(){
        setObject();

        View rootView = getWindow().getDecorView().getRootView();
//        rootView.setBackground(this.getResources().getDrawable(R.drawable.img_love_mom_app_bg));

        img_background = (ImageView) findViewById(R.id.img_background);
        img_background.setVisibility(View.GONE);

        Bitmap imageBitmap = delegate.imageLoader.display2(delegate.project.getBackgroundUrl());
        if (imageBitmap == null) {
            imageBitmap = delegate.readImageFileOnSD(delegate.project.getBackgroundUrl(),0, 0);
        }
        Drawable imageDraw =  new BitmapDrawable(imageBitmap);
//        Bitmap imageBitmap = delegate.readImageFileOnSD(delegate.project.getBackgroundUrl(),0, 0);
//        Drawable imageDraw =  new BitmapDrawable(imageBitmap);

        rootView.setBackground(imageDraw);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_look_up);
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setImage();
        ctx = this;
    }

    private void setObject(){
        delegate = (questionniare_delegate)getApplicationContext();

        btn_menu = (ImageButton) findViewById(R.id.btnMenu);
        btn_send = (ImageButton) findViewById(R.id.btnSend);
        btn_back = (ImageButton) findViewById(R.id.btnBack);

        btn_menu.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        txtFirstName = (EditText) findViewById(R.id.txt_FirstName);
        txtLastName = (EditText) findViewById(R.id.txt_LastName);
        txtTel = (EditText) findViewById(R.id.txt_Tel);

        txtFirstName.setTypeface(delegate.font_type);
        txtLastName.setTypeface(delegate.font_type);
        txtTel.setTypeface(delegate.font_type);

        txtFirstName.setTextSize(25);
        txtLastName.setTextSize(25);
        txtTel.setTextSize(25);

        project_name = (TextView) findViewById(R.id.project_name);
        project_name.setText(delegate.project.getName());
        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);

        question_title = (TextView) findViewById(R.id.question_title);
        question_title.setTypeface(delegate.font_type);
        question_title.setTextSize(25);

        lbl_firstname = (TextView) findViewById(R.id.lbl_customer_lookup_name);
        lbl_lastname = (TextView) findViewById(R.id.lbl_customer_lookup_surname);
        lbl_tel = (TextView) findViewById(R.id.lbl_customer_lookup_tel);

        lbl_firstname.setTypeface(delegate.font_type);
        lbl_lastname.setTypeface(delegate.font_type);
        lbl_tel.setTypeface(delegate.font_type);

        lbl_firstname.setTextSize(25);
        lbl_lastname.setTextSize(25);
        lbl_tel.setTextSize(25);

        root_view = (RelativeLayout) findViewById(R.id.root_view);
        root_view.setOnClickListener(this);
        popup = new PopupWindow(this);

        txtFirstName.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    txtFirstName.clearFocus();
                    txtLastName.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtLastName.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    txtLastName.clearFocus();
                    txtTel.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtTel.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    txtTel.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtTel.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        btnEN = (ImageButton) findViewById(R.id.btnEN);
        btnTH = (ImageButton) findViewById(R.id.btnTH);
        btnEN.setOnClickListener(this);
        btnTH.setOnClickListener(this);

    }

    private void changeLanguege(){

        question_title.setText(R.string.title_activity_customer_look_up);
        project_name.setText(delegate.project.getName());

        if(delegate.service.getLg().equals("en")){
            btnEN.setImageResource(R.drawable.btn_en_);
            btnTH.setImageResource(R.drawable.btn_th);

        } else {
            btnEN.setImageResource(R.drawable.btn_en);
            btnTH.setImageResource(R.drawable.btn_th_);
        }
        txtFirstName.setHint(R.string.customer_look_up_hint_name);
        txtLastName.setHint(R.string.customer_look_up_hint_surname);
        txtTel.setHint(R.string.customer_look_up_hint_tel);
        txtFirstName.setText("");
        txtLastName.setText("");
        txtTel.setText("");

        lbl_firstname.setText(R.string.customer_look_up_hint_name);
        lbl_lastname.setText(R.string.customer_look_up_hint_surname);
        lbl_tel.setText(R.string.customer_look_up_hint_tel);

    }

    protected void onResume() {
        super.onResume();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String nowDate = sdf.format(c.getTime());

        if(!delegate.service.globals.getDateLastLogin().equals(nowDate)){
            delegate.service.Logout();
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else {
            changeLanguege();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_look_up, menu);
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

        if(v.getId() == R.id.btnSend) {
            btn_send.setEnabled(false);
            hideKeyboard();
            if(popup.isShowing()){
                popup.dismiss();
            } else {
                if (txtFirstName.getText().toString().trim().length() == 0 && txtLastName.getText().toString().trim().length() == 0 && txtTel.getText().toString().length() == 0) {
                    startActivityForResult(new Intent(ctx, NotFoundCustomerActivity.class), 0);
                } else {
                    final ProgressDialog progress = new ProgressDialog(this);
                    progress.setTitle("Please wait");
                    progress.setMessage("Searching....");
                    progress.show();

                    final Handler uiHandler = new Handler();
                    final Runnable onUi = new Runnable() {
                        @Override
                        public void run() {
                            // this will run on the main UI thread
                            progress.dismiss();
                            btn_send.setEnabled(true);
                            if (delegate.getCustomer_list().size() > 0) {
                                startActivityForResult(new Intent(ctx, CustomerListActivity.class), 0);
                            } else {
                                startActivityForResult(new Intent(ctx, NotFoundCustomerActivity.class), 0);
                            }
                        }
                    };
                    Runnable background = new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<ContactSearchData> searchData = delegate.service.searchContact(txtFirstName.getText().toString().trim(), txtLastName.getText().toString().trim(), txtTel.getText().toString());
                            delegate.setCustomer_list(new ArrayList<ContactSearchData>());
                            if (searchData != null) {
                                if (searchData.size() != 0) {
                                    delegate.setCustomer_list(searchData);
                                }
                            }
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {

                            }
                            uiHandler.post(onUi);
                        }
                    };
                    new Thread(background).start();
                }
            }
        } else if(v.getId() == R.id.btnBack){
            if(popup.isShowing()){
                popup.dismiss();
            } else {
                onBackPressed();
            }
        } else if(v.getId() == R.id.btnMenu){
            if(popup.isShowing()){
                popup.dismiss();
            } else {
                showPopup(this);
            }
        } else if(v.getId() == R.id.root_view){
            if(popup.isShowing()){
                popup.dismiss();
            }
        } else if(v.getId() == R.id.btnEN){
            if (popup.isShowing()) {
                popup.dismiss();
            } else {
                if (!delegate.service.getLg().equals("en")) {
                    delegate.service.setLg("en");
                    delegate.setLocale("en");
                    changeLanguege();
                }
            }
        } else if(v.getId() == R.id.btnTH){
            if (popup.isShowing()) {
                popup.dismiss();
            } else {
                if (!delegate.service.getLg().equals("th")) {
                    delegate.service.setLg("th");
                    delegate.setLocale("th");
                    changeLanguege();
                }
            }
        }
    }
    public void onBackPressed() {
        this.setResult(3);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode ==2 || resultCode == 0 || resultCode == 1){
            this.setResult(resultCode);
            finish();
        }
    }

    public void showPopup(final Activity context) {
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.activity_menu, viewGroup);
        popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(delegate.dpToPx(175));
        popup.setHeight(delegate.dpToPx(80));
        popup.setBackgroundDrawable(null);

        ImageButton v = (ImageButton)findViewById(R.id.btnMenu);
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, 0, (int)v.getY()+delegate.dpToPx(70));

        View view_instance = (View)layout.findViewById(R.id.popup);
        final RelativeLayout home = (RelativeLayout) layout.findViewById(R.id.menu_home);
        final RelativeLayout logout = (RelativeLayout) layout.findViewById(R.id.menu_logout);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(getResources().getColor(R.color.ORANGE));
                logout.setBackgroundColor(getResources().getColor(R.color.WHITE));
                setResult(0);
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(getResources().getColor(R.color.WHITE));
                logout.setBackgroundColor(getResources().getColor(R.color.ORANGE));
                delegate.service.Logout();
                Intent i = new Intent(ctx, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtTel.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(txtLastName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(txtFirstName.getWindowToken(), 0);
    }
}
