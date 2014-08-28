package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud9worldwide.questionnaire.data.ProjectData;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class NotFoundCustomerActivity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total = 5;
    ImageButton btn_menu, btnAdd, btn_back, btnEN, btnTH;
    LinearLayout linearLayout, content_view;
    static PopupWindow popup;
    questionniare_delegate delegate;
    TextView project_name;
    ImageView img_background;
    ProjectData project_data;

    TextView lblRed, lblBlack;


    private void setImage(){
        delegate = (questionniare_delegate)getApplicationContext();
        setObject();
        img_background = (ImageView) findViewById(R.id.img_background);
        delegate.imageLoader.display(delegate.project.getBackgroundUrl(),
                String.valueOf(img_background.getWidth()),
                String.valueOf(img_background.getHeight()),
                img_background,R.drawable.logo_ap);
    }

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_found_customer);
        delegate = (questionniare_delegate)getApplicationContext();
        project_data = delegate.project;
        delegate.customer_selected =null;
        setImage();
    }

    private void setObject(){

        btn_menu = (ImageButton) findViewById(R.id.btnMenu);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btn_back = (ImageButton) findViewById(R.id.btnBack);

        lblRed = (TextView) findViewById(R.id.question);
        lblRed.setTextSize(30);
        lblRed.setTypeface(delegate.font_type);

        lblBlack = (TextView) findViewById(R.id.description);
        lblBlack.setTextSize(30);
        lblBlack.setTypeface(delegate.font_type);

        btn_menu.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        project_name = (TextView) findViewById(R.id.project_name);

        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);
        popup = new PopupWindow(this);

        btnEN = (ImageButton) findViewById(R.id.btnEN);
        btnTH = (ImageButton) findViewById(R.id.btnTH);
        btnEN.setOnClickListener(this);
        btnTH.setOnClickListener(this);
    }

    private void changeLanguege(){

        project_name.setText(delegate.project.getName());
        if(delegate.service.getLg().equals("en")){
            btnEN.setImageResource(R.drawable.btn_en_);
            btnTH.setImageResource(R.drawable.btn_th);
            btnAdd.setImageResource(R.drawable.btn_en_add_customer);
        } else {
            btnEN.setImageResource(R.drawable.btn_en);
            btnTH.setImageResource(R.drawable.btn_th_);
            btnAdd.setImageResource(R.drawable.btn_th_add_customer);
        }

        lblRed.setText(R.string.not_found_customer_header);
        lblBlack.setText(R.string.not_found_customer_detail);
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
        getMenuInflater().inflate(R.menu.not_found_customer, menu);
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
        if(v.getId() == R.id.btnAdd){
            startActivityForResult(new Intent(this, AddCustomerOneActivity.class),0);
        } else if(v.getId() == R.id.btnBack){
            onBackPressed();
        } else if(v.getId() == R.id.btnMenu){
            if(popup.isShowing()){
                popup.dismiss();
            } else {
                showPopup(this);
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
                setResult(2);
                finish();
            }
        });
    }


}
