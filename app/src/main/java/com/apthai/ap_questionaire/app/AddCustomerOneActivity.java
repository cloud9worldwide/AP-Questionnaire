package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class AddCustomerOneActivity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    ImageButton btnMenu, btnNext, btnBack;
    Spinner ddlPrefix, ddlCountry, ddlNationality;

    questionniare_delegate delegate;
    RelativeLayout footer;
    TextView project_name,question_title;
    String txtPromp;
    RelativeLayout root_view;
    static PopupWindow popup, popupAddMobile;

    ImageButton btnAddMobile, btnAddPhone;
    EditText txtPrefix, txtFirstName, txtLastName, txtNickname, txtEmail;
    TextView datePicker;

    ArrayList<String> mobile_list, phone_list;
    int mYear, mMonth, mDay;

    EditText home1, home2, home3, home4, home5, home6, home7, home8, home9;
    EditText mobile1, mobile2, mobile3, mobile4, mobile5, mobile6, mobile7, mobile8, mobile9;

    TextView txt_header,lblPrefix,lbl_add_customer_name, lbl_add_customer_surname, lblNickname,
            lbl_add_customer_birthday, lblEmail,lbl_add_customer_tel_home_BKK_line1, lbl_add_customer_tel_home_line2,
            lbl_digit_tel_home_BKK1, lbl_digit_tel_home_BKK2, lbl_add_customer_tel_home_other_line1,
            lbl_add_customer_tel_home_other_line2, lbl_digit_tel_home_other1, lbl_add_customer_mobile,
            lbl_digit_mobile1, lblWork, lbl_add_customer_district_work, lblCountry, lblHomeID, lbl_add_customer_moo,
            lbl_add_customer_building, lbl_add_customer_floor, lbl_add_customer_room, lbl_add_customer_soi,
            lbl_add_customer_road, lblProvince, lbl_add_customer_district, lbl_add_customer_sub_district,
            lblPostcode;

    TextView lbl_add_customer_phone1, lbl_add_customer_phone2, lbl_add_customer_phone3, lbl_add_customer_phone4;
    TextView lbl_add_customer_mobile1, lbl_add_customer_mobile2, lbl_add_customer_mobile3, lbl_add_customer_mobile4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_one);

        setObject();
    }
    private void setObject() {
        delegate = (questionniare_delegate) getApplicationContext();

        footer = (RelativeLayout) findViewById(R.id.footer);

        project_name = (TextView) findViewById(R.id.project_name);
        project_name.setText(delegate.project.getName());
        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);

        txtPromp = "กรุณาเลือก";

        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_header.setTypeface(delegate.font_type);
        txt_header.setTextSize(35);

        root_view = (RelativeLayout) findViewById(R.id.root_view);
        root_view.setOnClickListener(this);
        popup = new PopupWindow(this);
        popupAddMobile = new PopupWindow(this);

        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        ddlPrefix = (Spinner) findViewById(R.id.ddlPrefix);
        txtPrefix = (EditText) findViewById(R.id.txt_Prefix);
        txtFirstName = (EditText) findViewById(R.id.txt_FirstName);
        txtLastName = (EditText) findViewById(R.id.txt_LastName);
        txtNickname = (EditText) findViewById(R.id.txtNickname);
        datePicker = (TextView) findViewById(R.id.datePicker);
        txtEmail = (EditText) findViewById(R.id.txt_Email);
        ddlCountry = (Spinner) findViewById(R.id.ddlOtherCountry);
        ddlNationality = (Spinner) findViewById(R.id.ddlNationality);

        btnAddMobile = (ImageButton) findViewById(R.id.btnAddMobiles);
        btnAddPhone = (ImageButton) findViewById(R.id.btnAddPhone);

        btnAddMobile.setOnClickListener(this);
        btnAddPhone.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        lbl_add_customer_phone1 = (TextView) findViewById(R.id.lbl_add_customer_phone1);
        lbl_add_customer_phone2 = (TextView) findViewById(R.id.lbl_add_customer_phone2);
        lbl_add_customer_phone3 = (TextView) findViewById(R.id.lbl_add_customer_phone3);
        lbl_add_customer_phone4 = (TextView) findViewById(R.id.lbl_add_customer_phone4);

        lbl_add_customer_mobile1 = (TextView) findViewById(R.id.lbl_add_customer_mobile1);
        lbl_add_customer_mobile2 = (TextView) findViewById(R.id.lbl_add_customer_mobile2);
        lbl_add_customer_mobile3 = (TextView) findViewById(R.id.lbl_add_customer_mobile3);
        lbl_add_customer_mobile4 = (TextView) findViewById(R.id.lbl_add_customer_mobile4);

        mobile_list = new ArrayList<String>();
        mobile_list.add("");
        mobile_list.add("");
        mobile_list.add("");
        mobile_list.add("");
        mobile_list.add("");

        phone_list = new ArrayList<String>();
        phone_list.add("");
        phone_list.add("");
        phone_list.add("");
        phone_list.add("");
        setItemSpinner();
        setBirthDay();
        setObjectTelephone();
        setFont();

//        setKeyListener();

        final View activityRootView = findViewById(R.id.root_view);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    footer.setVisibility(View.GONE);
                } else {
                    footer.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void setFont(){
        txtPrefix = (EditText) findViewById(R.id.txt_Prefix);
        txtFirstName = (EditText) findViewById(R.id.txt_FirstName);
        txtLastName = (EditText) findViewById(R.id.txt_LastName);
        txtNickname = (EditText) findViewById(R.id.txtNickname);
        datePicker = (TextView) findViewById(R.id.datePicker);
        txtEmail = (EditText) findViewById(R.id.txt_Email);

        txtPrefix.setTypeface(delegate.font_type);
        txtPrefix.setTextSize(25);
        txtFirstName.setTypeface(delegate.font_type);
        txtFirstName.setTextSize(25);
        txtLastName.setTypeface(delegate.font_type);
        txtLastName.setTextSize(25);
        txtNickname.setTypeface(delegate.font_type);
        txtNickname.setTextSize(25);
        datePicker.setTypeface(delegate.font_type);
        datePicker.setTextSize(25);
        txtEmail.setTypeface(delegate.font_type);
        txtEmail.setTextSize(25);

    }
    private void setObjectTelephone(){

        home1 = (EditText) findViewById(R.id.txt_digit_tel_home1);
        home2 = (EditText) findViewById(R.id.txt_digit_tel_home2);
        home3 = (EditText) findViewById(R.id.txt_digit_tel_home3);
        home4 = (EditText) findViewById(R.id.txt_digit_tel_home4);
        home5 = (EditText) findViewById(R.id.txt_digit_tel_home5);
        home6 = (EditText) findViewById(R.id.txt_digit_tel_home6);
        home7 = (EditText) findViewById(R.id.txt_digit_tel_home7);
        home8 = (EditText) findViewById(R.id.txt_digit_tel_home8);
        home9 = (EditText) findViewById(R.id.txt_digit_tel_home9);

        mobile1 = (EditText) findViewById(R.id.txt_digit_mobile1);
        mobile2 = (EditText) findViewById(R.id.txt_digit_mobile2);
        mobile3 = (EditText) findViewById(R.id.txt_digit_mobile3);
        mobile4 = (EditText) findViewById(R.id.txt_digit_mobile4);
        mobile5 = (EditText) findViewById(R.id.txt_digit_mobile5);
        mobile6 = (EditText) findViewById(R.id.txt_digit_mobile6);
        mobile7 = (EditText) findViewById(R.id.txt_digit_mobile7);
        mobile8 = (EditText) findViewById(R.id.txt_digit_mobile8);
        mobile9 = (EditText) findViewById(R.id.txt_digit_mobile9);

    }
    private void setBirthDay(){
        datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();

                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(AddCustomerOneActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        mDay = selectedday;
                        mMonth = selectedmonth + 1;
                        mYear = selectedyear;
                        datePicker.setText(mYear + "-" + mMonth + "-" + mDay);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
    }
    private void setItemSpinner(){
        ArrayList<String> list;
        ArrayAdapter<String> dataAdapter;

        //PREFIX
        list = new ArrayList<String>();
        list.add("คุณ");
        list.add("อื่นๆ");
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlPrefix.setAdapter(dataAdapter);

        //COUNTRY
        list = new ArrayList<String>();
        list.add("ไทย");
        list.add("อังกฤษ");
        list.add("จีน");
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlCountry.setAdapter(dataAdapter);

        //NATIONALITY
        list = new ArrayList<String>();
        list.add("ไทย");
        list.add("อังกฤษ");
        list.add("จีน");
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlNationality.setAdapter(dataAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_customer_one, menu);
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
    private void packData(){

    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnBack) {
            onBackPressed();
        } else if (v.getId() == R.id.btnMenu) {
            if (popup.isShowing()) {
                popup.dismiss();
            } else {
                showPopup(this);
            }
        } else if (v.getId() == R.id.root_view) {
            if (popup.isShowing()) {
                popup.dismiss();
            }
            if(popupAddMobile.isShowing()){
                popupAddMobile.dismiss();
            }
        } else if(v.getId() == R.id.btnNext){
            packData();
            if(ddlCountry.getSelectedItem().equals("ไทย")){
                startActivityForResult(new Intent(this, AddCustomerTHActivity.class),0);
            } else {
                startActivityForResult(new Intent(this, AddCustomerENActivity.class),0);
            }
        }
    }
    public void onBackPressed() {
        this.setResult(3);
        finish();
    }

    public void showPopup(final Activity context) {
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.activity_menu, viewGroup);
        popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(delegate.pxToDp(180));
        popup.setHeight(delegate.pxToDp(118));
        popup.setBackgroundDrawable(null);

        ImageButton v = (ImageButton)findViewById(R.id.btnMenu);
        popup.showAtLocation(layout, Gravity.NO_GRAVITY,0,(int)v.getY()+delegate.dpToPx(50));

        //popup.showAtLocation(layout, Gravity.NO_GRAVITY, 0, 70);
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
