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


public class NotFoundCustomerActivity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total = 5;
    ImageButton btn_menu, btnAdd, btn_back;
    LinearLayout linearLayout, content_view;
    static PopupWindow popup;
    questionniare_delegate delegate;
    TextView project_name;
    ImageView img_background;
    ProjectData project_data;

    TextView lblRed, lblBlack;

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setImage();
    }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_found_customer);
        delegate = (questionniare_delegate)getApplicationContext();
        project_data = delegate.project;
        delegate.customer_selected =null;
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
        project_name.setText(delegate.project.getName());
        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);
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
        popup.setWidth(180);
        popup.setHeight(118);
        popup.setBackgroundDrawable(null);
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, 0, 70);
        View view_instance = (View)layout.findViewById(R.id.popup);
        final RelativeLayout home = (RelativeLayout) layout.findViewById(R.id.menu_home);
        final RelativeLayout settings = (RelativeLayout) layout.findViewById(R.id.menu_settings);
        final RelativeLayout logout = (RelativeLayout) layout.findViewById(R.id.menu_logout);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(getResources().getColor(R.color.ORANGE));
                settings.setBackgroundColor(getResources().getColor(R.color.WHITE));
                logout.setBackgroundColor(getResources().getColor(R.color.WHITE));
                if (popup.isShowing()) {
                    popup.dismiss();
                }
                setResult(0);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(getResources().getColor(R.color.WHITE));
                settings.setBackgroundColor(getResources().getColor(R.color.ORANGE));
                logout.setBackgroundColor(getResources().getColor(R.color.WHITE));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(getResources().getColor(R.color.WHITE));
                settings.setBackgroundColor(getResources().getColor(R.color.WHITE));
                logout.setBackgroundColor(getResources().getColor(R.color.ORANGE));
                if (popup.isShowing()) {
                    popup.dismiss();
                }
                delegate.service.Logout();
                setResult(2);
                finish();
            }
        });
    }
}
