package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud9worldwide.questionnaire.data.ContactSearchData;

import java.util.ArrayList;


public class CustomerListActivity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total = 5;
    ImageButton btn_menu, btn_send, btn_back, btnEN, btnTH;
    LinearLayout linearLayout, content_view;
    questionniare_delegate delegate;
    ArrayList<ContactSearchData> customer_list;
    TextView project_name;
    static PopupWindow popup;
    ImageView img_background;
    RelativeLayout root_view;

    TextView question_title;

    ImageButton btnAdd,btnBack;
    private Context ctx;
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        setImage();
    }

    private void setImage(){
        setObject();
        img_background = (ImageView) findViewById(R.id.img_background);
        delegate.imageLoader.display(delegate.project.getBackgroundUrl(),
                String.valueOf(img_background.getWidth()),
                String.valueOf(img_background.getHeight()),
                img_background,delegate.imgDefault);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        ctx = this;
    }

    private void setObject() {
        delegate = (questionniare_delegate)getApplicationContext();
        delegate.isBack = 9;
        customer_list = delegate.getCustomer_list();
        total = customer_list.size();

        content_view = (LinearLayout) findViewById(R.id.AP_content);
        content_view.removeAllViews();

        project_name = (TextView) findViewById(R.id.project_name);
        project_name.setText(delegate.project.getName());
        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);

        question_title = (TextView) findViewById(R.id.question_title);
        question_title.setTypeface(delegate.font_type);
        question_title.setTextSize(25);

        btn_menu = (ImageButton) findViewById(R.id.btnMenu);
        btn_menu.setOnClickListener(this);

        root_view = (RelativeLayout) findViewById(R.id.root_view);
        root_view.setOnClickListener(this);

        popup = new PopupWindow(this);
        btnAdd =(ImageButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnBack =(ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnEN = (ImageButton) findViewById(R.id.btnEN);
        btnTH = (ImageButton) findViewById(R.id.btnTH);
        btnEN.setOnClickListener(this);
        btnTH.setOnClickListener(this);

        changeLanguege();

    }

    private void changeLanguege(){

        if(delegate.service.getLg().equals("en")){
            btnEN.setImageResource(R.drawable.btn_en_);
            btnTH.setImageResource(R.drawable.btn_th);
            btnAdd.setImageResource(R.drawable.btn_en_add_customer);
        } else {
            btnEN.setImageResource(R.drawable.btn_en);
            btnTH.setImageResource(R.drawable.btn_th_);
            btnAdd.setImageResource(R.drawable.btn_th_add_customer);
        }
        project_name.setText(delegate.project.getName());
        question_title.setText(R.string.title_activity_customer_list);
        content_view.removeAllViews();
        setTableLayout();
    }

    private void setTableLayout(){
        LinearLayout.LayoutParams lp;
        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,delegate.dpToPx(50));
        lp.weight = 1;
        lp.gravity = Gravity.CENTER_VERTICAL;
        lp.setMargins(delegate.dpToPx(20), 0, delegate.dpToPx(20), 0);
        LinearLayout.LayoutParams lp2;
        lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,delegate.dpToPx(50));
        lp2.weight = 1;
        lp2.gravity = Gravity.CENTER_VERTICAL;
        lp2.setMargins(delegate.dpToPx(20), 0, delegate.dpToPx(20), 0);

        //set question
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        TextView header_name = new TextView(this);
        header_name.setText(R.string.header_list_name);
        header_name.setLayoutParams(lp);
        header_name.setTypeface(delegate.font_type, Typeface.BOLD);
        header_name.setTextSize(20);
        header_name.setGravity(Gravity.CENTER);
        linearLayout.addView(header_name);
        TextView header_surname = new TextView(this);
        header_surname.setText(R.string.header_list_surname);
        header_surname.setTypeface(delegate.font_type, Typeface.BOLD);
        header_surname.setTextSize(20);
        header_surname.setGravity(Gravity.CENTER);
        header_surname.setLayoutParams(lp);
        linearLayout.addView(header_surname);
        TextView header_unitnumber = new TextView(this);
        header_unitnumber.setText(R.string.header_list_unitnumber);
        header_unitnumber.setTypeface(delegate.font_type, Typeface.BOLD);
        header_unitnumber.setTextSize(20);
        header_unitnumber.setGravity(Gravity.CENTER);
        header_unitnumber.setLayoutParams(lp);
        linearLayout.addView(header_unitnumber);
//        TextView header_contact_id = new TextView(this);
//        header_contact_id.setText(R.string.header_list_contactID);
//        header_contact_id.setTypeface(delegate.font_type, Typeface.BOLD);
//        header_contact_id.setTextSize(20);
//        header_contact_id.setGravity(Gravity.CENTER);
//        header_contact_id.setLayoutParams(lp);
//        linearLayout.addView(header_contact_id);
        TextView header_time = new TextView(this);
        header_time.setText(R.string.header_list_lastVisit);
        header_time.setTypeface(delegate.font_type, Typeface.BOLD);
        header_time.setTextSize(20);
        header_time.setGravity(Gravity.CENTER);
        header_time.setLayoutParams(lp);
        linearLayout.addView(header_time);
        TextView header_opatulity = new TextView(this);
        header_opatulity.setText(R.string.header_list_opp);
        header_opatulity.setTypeface(delegate.font_type, Typeface.BOLD);
        header_opatulity.setTextSize(20);
        header_opatulity.setGravity(Gravity.CENTER);
        header_opatulity.setLayoutParams(lp2);
        linearLayout.addView(header_opatulity);

        content_view.addView(linearLayout);

        linearLayout = new LinearLayout(this);

        int column =1;

        for(int i =0, c = 0, r = 0; i < total; i++, c++){
            ContactSearchData obj = customer_list.get(i);
            LinearLayout btn = new LinearLayout(this);

            TextView name = new TextView(this);
            name.setText(obj.getFname());
            name.setTypeface(delegate.font_type);
            name.setTextSize(25);
            name.setGravity(Gravity.CENTER_VERTICAL);
            name.setLayoutParams(lp);
            btn.addView(name);

            TextView surname = new TextView(this);
            surname.setText(obj.getLname());
            surname.setTypeface(delegate.font_type);
            surname.setGravity(Gravity.CENTER_VERTICAL);
            surname.setTextSize(25);
            surname.setLayoutParams(lp);
            btn.addView(surname);

            TextView unitnumber = new TextView(this);
            unitnumber.setText(obj.getUnitNumber());
            unitnumber.setTypeface(delegate.font_type);
            unitnumber.setTextSize(20);
            unitnumber.setGravity(Gravity.CENTER_VERTICAL);
            unitnumber.setLayoutParams(lp);
            btn.addView(unitnumber);

//            TextView contact_id = new TextView(this);
//            contact_id.setText(obj.getContactId());
//            contact_id.setTypeface(delegate.font_type);
//            contact_id.setGravity(Gravity.CENTER_VERTICAL);
//            contact_id.setTextSize(20);
//            contact_id.setLayoutParams(lp);
//            btn.addView(contact_id);

            TextView time = new TextView(this);
            time.setText(obj.getLastVisit());
            time.setTypeface(delegate.font_type);
            time.setGravity(Gravity.CENTER);
            time.setTextSize(18);
            time.setLayoutParams(lp);
            btn.addView(time);

            TextView opatulity = new TextView(this);
            String op = "NO";
            if(obj.getIsOpporpunity()){
                op = "YES";
            }
            opatulity.setText(op);
            opatulity.setTypeface(delegate.font_type);
            opatulity.setGravity(Gravity.CENTER);
            opatulity.setTextSize(20);
            opatulity.setLayoutParams(lp2);
            btn.addView(opatulity);

            btn.setTag(i);
            btn.setOnClickListener(this);

            content_view.addView(btn);
        }
        content_view.addView(linearLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_list, menu);
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
        if(v.getId() == R.id.btnMenu){
            if(popup.isShowing()){
                popup.dismiss();
            } else {
                showPopup(this);
            }
        } else if(v.getId() == R.id.root_view){
            if(popup.isShowing()){
                popup.dismiss();
            }
        } else if (v.getId() == R.id.btnAdd) {
            delegate.customer_selected = null;
            startActivityForResult(new Intent(this, AddCustomerOneActivity.class),0);
        } else if(v.getId() == R.id.btnBack){
            onBackPressed();
        }
        else if(v.getId() == R.id.btnEN){
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
        else {
            if(popup.isShowing()){
                popup.dismiss();
            } else {
                int index = Integer.parseInt(v.getTag().toString());
                ContactSearchData obj = customer_list.get(index);

                delegate.service.globals.setContactId(obj.getContactId());
                delegate.service.globals.setIsCustomerLocal(false);

                Intent i = new Intent(this, CustomerInfomationActivity.class);
                Log.e(TAG , "customer index : "+obj.getContactId());
                startActivityForResult(i, 0);
            }
        }
    }

    @Override
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
//                setResult(2);
//                finish();
                Intent i = new Intent(ctx, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }
}