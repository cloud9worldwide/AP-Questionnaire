package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloud9worldwide.questionnaire.data.AddressData;
import com.cloud9worldwide.questionnaire.data.ContactData;
import com.cloud9worldwide.questionnaire.data.ValTextData;

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
    EditText mobile1, mobile2, mobile3, mobile4, mobile5, mobile6, mobile7, mobile8, mobile9,mobile10;

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
    TextView lbl_prefix_extra1,lbl_prefix_extra2;

    ContactData new_customer;
    ArrayList<String> listfix;
    int indexPrefix, indexNationality, indexCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_one);

//        setObject();
        listfix = new ArrayList<String>();
        listfix.add("คุณ");
        listfix.add("นาย");
        listfix.add("นาง");
        listfix.add("นางสาว");
        listfix.add("อื่นๆ");
        indexPrefix= -1;
        indexNationality= -1;
        indexCountry= -1;
        delegate = (questionniare_delegate) getApplicationContext();

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Please wait");
        progress.setMessage("Loading customer information.");
        progress.show();

        final Handler uiHandler = new Handler();
        final  Runnable onUi = new Runnable() {
            @Override
            public void run() {
                // this will run on the main UI thread
                progress.dismiss();
                setObject();
                getCustomerInfo();
            }
        };
        Runnable background = new Runnable() {
            @Override
            public void run() {
                // This is the delay
                if(delegate.customer_selected !=null){
                    new_customer = delegate.customer_selected;
                    getCustomerInfoLong();
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e){

                }
                uiHandler.post( onUi );
            }
        };
        new Thread( background ).start();

    }
    private void getCustomerInfoLong(){


        for (int i = 0; i < listfix.size(); i++) {

            if (listfix.get(i).equals(new_customer.getPrefix())) {

                indexPrefix = i;
                break;
            }
        }
        ArrayList<ValTextData> list =delegate.service.getNationality();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toString().equals(new_customer.getNationality())) {
                indexNationality =i;
                break;
            }
        }
        list =delegate.service.getCountry();
        for (int i = 0; i < delegate.service.getCountry().size(); i++) {
            if (list.get(i).toString().equals(new_customer.getAddress().getCountry())) {
                indexCountry = i;
                break;
            }
        }
    }
    private void getCustomerInfo(){
        if(delegate.customer_selected ==null){
            AddressData home = new AddressData("", "", "", "", "", "", "", "", "", "", "", "");
            AddressData work = new AddressData("", "", "", "", "", "", "", "", "", "", "", "");
            new_customer = new ContactData("", "", "", "", "", "", null, null,null);
            new_customer.setAddress(home);
            new_customer.setAddressWork(work);
        } else {

            txtPrefix.setText(new_customer.getPrefix().toString());
            if(indexPrefix != -1){
                ddlPrefix.setSelection(indexPrefix);
            }

            txtFirstName.setText(new_customer.getFname());
            txtLastName.setText(new_customer.getLname());
            txtNickname.setText(new_customer.getNickname());
            txtEmail.setText(new_customer.getEmail());
            datePicker.setText(new_customer.getBirthdate());


            if(indexNationality != -1){
                ddlNationality.setSelection(indexNationality);
            }
            if(indexCountry != -1){
                ddlCountry.setSelection(indexCountry);
            }
            mobile_list = new_customer.getMobiles();

            String mobile = mobile_list.get(0);
            mobile = mobile.replace("-", "");
            switch (mobile.length()) {
                case 10:
                    mobile10.setText(mobile.substring(9, 10));
                case 9:
                    mobile9.setText(mobile.substring(8, 9));
                case 8:
                    mobile8.setText(mobile.substring(7, 8));
                case 7:
                    mobile7.setText(mobile.substring(6, 7));
                case 6:
                    mobile6.setText(mobile.substring(5, 6));
                case 5:
                    mobile5.setText(mobile.substring(4, 5));
                case 4:
                    mobile4.setText(mobile.substring(3, 4));
                case 3:
                    mobile3.setText(mobile.substring(2, 3));
                case 2:
                    mobile2.setText(mobile.substring(1, 2));
                case 1:
                    mobile1.setText(mobile.substring(0, 1));
            }

            phone_list = new_customer.getTels();
            String tel = phone_list.get(0);
            tel = tel.replace("-", "");
            int length = tel.length();
            switch (length) {
                case 9:
                    home9.setText(tel.substring(8, 9));
                case 8:
                    home8.setText(tel.substring(7, 8));
                case 7:
                    home7.setText(tel.substring(6, 7));
                case 6:
                    home6.setText(tel.substring(5, 6));
                case 5:
                    home5.setText(tel.substring(4, 5));
                case 4:
                    home4.setText(tel.substring(3, 4));
                case 3:
                    home3.setText(tel.substring(2, 3));
                case 2:
                    home2.setText(tel.substring(1, 2));
                case 1:
                    home1.setText(tel.substring(0, 1));
            }
            updateMobile();

        }
    }
    private void setObject() {
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
        lbl_add_customer_mobile1.setText("");
        lbl_add_customer_mobile2.setText("");
        lbl_add_customer_mobile3.setText("");
        lbl_add_customer_mobile4.setText("");

        phone_list = new ArrayList<String>();
        phone_list.add("");
        phone_list.add("");
        phone_list.add("");
        phone_list.add("");
        phone_list.add("");
        lbl_add_customer_phone1.setText("");
        lbl_add_customer_phone2.setText("");
        lbl_add_customer_phone3.setText("");
        lbl_add_customer_phone4.setText("");

        lbl_prefix_extra1  = (TextView) findViewById(R.id.lbl_prefix_extra1);
        lbl_prefix_extra2 = (TextView) findViewById(R.id.lbl_prefix_extra2);

        setItemSpinner();
        setBirthDay();
        setObjectTelephone();
        setFont();

        setKeyListener();

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
    private void setKeyListener(){
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
                    txtNickname.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txtNickname.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    txtNickname.clearFocus();
                    txtEmail.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtEmail.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtEmail.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        //test
        mobile1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP){
                    Log.e("ACTION_UP",keyCode+"");
                    mobile1.clearFocus();
                    mobile2.requestFocus();
                    return true;
                }
                return false;
            }
        });

        mobile2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.e("keyCode",keyCode+"");
                if (event.getAction() == KeyEvent.ACTION_UP){
                    Log.e("ACTION_UP",keyCode+"");
                    mobile2.clearFocus();
                    mobile3.requestFocus();
                    return true;
                }
                return false;
            }
        });

        mobile3.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    mobile3.clearFocus();
                    mobile4.requestFocus();
                    return true;
                }
                return false;
            }
        });

        mobile4.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    mobile4.clearFocus();
                    mobile5.requestFocus();
                    return true;
                }
                return false;
            }
        });

        mobile5.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    mobile5.clearFocus();
                    mobile6.requestFocus();
                    return true;
                }
                return false;
            }
        });

        mobile6.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    mobile6.clearFocus();
                    mobile7.requestFocus();
                    return true;
                }
                return false;
            }
        });

        mobile7.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    mobile7.clearFocus();
                    mobile8.requestFocus();
                    return true;
                }
                return false;
            }
        });

        mobile8.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    mobile8.clearFocus();
                    mobile9.requestFocus();
                    return true;
                }
                return false;
            }
        });

        mobile9.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mobile9.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        home1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    home1.clearFocus();
                    home2.requestFocus();
                    return true;
                }
                return false;
            }
        });


        home2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.e(TAG,"keyCode"+keyCode);
                if(keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP ){
                    home2.clearFocus();
                    home1.requestFocus();
                } else if (event.getAction() == KeyEvent.ACTION_UP){
                    home2.clearFocus();
                    home3.requestFocus();
                    return true;
                }
                return false;
            }
        });

        home3.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP ){
                    home3.clearFocus();
                    home2.requestFocus();
                } else if (event.getAction() == KeyEvent.ACTION_UP){
                    home3.clearFocus();
                    home4.requestFocus();
                    return true;
                }
                return false;
            }
        });

        home4.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    home4.clearFocus();
                    home5.requestFocus();
                    return true;
                }
                return false;
            }
        });

        home5.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    home5.clearFocus();
                    home6.requestFocus();
                    return true;
                }
                return false;
            }
        });

        home6.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    home6.clearFocus();
                    home7.requestFocus();
                    return true;
                }
                return false;
            }
        });

        home7.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    home7.clearFocus();
                    home8.requestFocus();
                    return true;
                }
                return false;
            }
        });

        home8.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    home8.clearFocus();
                    home9.requestFocus();
                    return true;
                }
                return false;
            }
        });

        home9.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    if (event.getAction() == KeyEvent.ACTION_UP){
                        InputMethodManager imm = (InputMethodManager)getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(home9.getWindowToken(), 0);
                        return true;
                    }
                    return true;
                }
                return false;
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
        mobile10 = (EditText) findViewById(R.id.txt_digit_mobile10);

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
        ArrayList<ValTextData> list;
        ArrayAdapter<ValTextData> dataAdapter;

        ArrayAdapter<String> dataAdapterfix;

        //PREFIX
        dataAdapterfix = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listfix);
        dataAdapterfix.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlPrefix.setAdapter(dataAdapterfix);
        ddlPrefix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(arg2 == listfix.size()-1){
                    //koy
                    lbl_prefix_extra1.setVisibility(View.VISIBLE);
                    lbl_prefix_extra2.setVisibility(View.VISIBLE);
                    txtPrefix.setVisibility(View.VISIBLE);
                } else {
                    lbl_prefix_extra1.setVisibility(View.GONE);
                    lbl_prefix_extra2.setVisibility(View.GONE);
                    txtPrefix.setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //COUNTRY
        list = delegate.service.getCountry();
        dataAdapter = new ArrayAdapter<ValTextData>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlCountry.setAdapter(dataAdapter);

        //NATIONALITY
        list = delegate.service.getNationality();
        dataAdapter = new ArrayAdapter<ValTextData>(this,
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

        AddressData home = new_customer.getAddress();
        AddressData work = new_customer.getAddressWork();

        //mobile index0
        if (mobile1.getText().toString().length() > 0 && mobile2.getText().toString().length() > 0
                && mobile3.getText().toString().length() > 0 && mobile4.getText().toString().length() > 0
                && mobile5.getText().toString().length() > 0 && mobile6.getText().toString().length() > 0
                && mobile7.getText().toString().length() > 0 && mobile8.getText().toString().length() > 0
                && mobile9.getText().toString().length() > 0 && mobile10.getText().toString().length() > 0) {
            String txtMobileinPage =  mobile1.getText().toString() + mobile2.getText().toString() + mobile3.getText().toString()
                    + mobile4.getText().toString() + mobile5.getText().toString() + mobile6.getText().toString() +
                    mobile7.getText().toString() + mobile8.getText().toString() + mobile9.getText().toString()+ mobile10.getText().toString();
            mobile_list.set(0, txtMobileinPage);
        }
        //home index0
        if (home1.getText().toString().length() > 0 && home2.getText().toString().length() > 0
                && home3.getText().toString().length() > 0 && home4.getText().toString().length() > 0
                && home5.getText().toString().length() > 0 && home6.getText().toString().length() > 0
                && home7.getText().toString().length() > 0 && home8.getText().toString().length() > 0
                && home9.getText().toString().length() > 0) {
            String txtPhone = home1.getText().toString() + home2.getText().toString() + home3.getText().toString()
                    + home4.getText().toString() + home5.getText().toString() + home6.getText().toString() +
                    home7.getText().toString() + home8.getText().toString() + home9.getText().toString();
            phone_list.set(0, txtPhone);
        }

        if(!ddlCountry.getSelectedItem().toString().equals(txtPromp)){
            home.setCountry(ddlCountry.getSelectedItem().toString());
        } else {
            home.setCountry("");
        }
        if(!ddlNationality.getSelectedItem().toString().equals(txtPromp)){
            new_customer.setNationality(ddlNationality.getSelectedItem().toString());
        } else {
            new_customer.setNationality("");
        }

        if(txtPrefix.getVisibility() == View.VISIBLE){
            new_customer.setPrefix_vip(txtPrefix.getText().toString());
        } else {
            new_customer.setPrefix_vip("");
        }
        new_customer.setPrefix(ddlPrefix.getSelectedItem().toString());

        new_customer.setFname(txtFirstName.getText().toString());
        new_customer.setLname(txtLastName.getText().toString());
        new_customer.setNickname(txtNickname.getText().toString());

        new_customer.setEmail(txtEmail.getText().toString());
        new_customer.setTels(phone_list);
        new_customer.setMobiles(mobile_list);
        new_customer.setBirthdate(datePicker.getText().toString());
        new_customer.setAddress(home);
        new_customer.setAddressWork(work);

        delegate.customer_selected = new_customer;
    }

    public void onClick(View v) {
        if (popup.isShowing()) {
            popup.dismiss();
        }
        if(popupAddMobile.isShowing()){
            popupAddMobile.dismiss();
        }

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
            if(ddlCountry.getSelectedItem().toString().equals("Thailand")){
                startActivityForResult(new Intent(this, AddCustomerTHActivity.class),0);
            } else {
                startActivityForResult(new Intent(this, AddCustomerENActivity.class),0);
            }
        } else if (v.getId() == R.id.btnAddMobiles){
            showPopupAddMobile(this,"mobile",99);
        } else if(v.getId() == R.id.lbl_add_customer_mobile1){
            if(mobile_list.get(1).length()!=0) {
                showPopupAddMobile(this, "mobile", 1);
            }
        } else if(v.getId() == R.id.lbl_add_customer_mobile2){
            if(mobile_list.get(2).length()!=0){
                showPopupAddMobile(this,"mobile",2);
            }
        } else if(v.getId() == R.id.lbl_add_customer_mobile3){
            if(mobile_list.get(3).length()!=0){
                showPopupAddMobile(this,"mobile",3);
            }
        } else if(v.getId() == R.id.lbl_add_customer_mobile4){
            if(mobile_list.get(4).length()!=0){
                showPopupAddMobile(this,"mobile",4);
            }
        } else if (v.getId() == R.id.btnAddPhone){
            showPopupAddMobile(this,"phone",99);
        } else if(v.getId() == R.id.lbl_add_customer_phone1){
            if(phone_list.get(1).length()!=0){
                showPopupAddMobile(this,"phone",1);
            }
        } else if(v.getId() == R.id.lbl_add_customer_phone2){
            if(phone_list.get(2).length()!=0){
                showPopupAddMobile(this,"phone",2);
            }
        } else if(v.getId() == R.id.lbl_add_customer_phone3){
            if(phone_list.get(3).length()!=0){
                showPopupAddMobile(this,"phone",3);
            }
        } else if(v.getId() == R.id.lbl_add_customer_phone4){
            if(phone_list.get(4).length()!=0){
                 showPopupAddMobile(this,"phone",4);
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
    public void showPopupAddMobile(final Activity context, final String type, final int index) {
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup_mobile);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.activity_add_moblie, viewGroup);
        popupAddMobile = new PopupWindow(context);
        popupAddMobile.setFocusable(true);
        popupAddMobile.setContentView(layout);
        popupAddMobile.setWidth(delegate.pxToDp(500));
        popupAddMobile.setHeight(delegate.pxToDp(150));
        popupAddMobile.setBackgroundDrawable(null);
        popupAddMobile.showAtLocation(layout, Gravity.CENTER, 0, 0);
        View view_instance = (View)layout.findViewById(R.id.popup_mobile);

        final EditText m00 = (EditText) layout.findViewById(R.id.txt_digit_mobile0);
        final EditText m01 = (EditText) layout.findViewById(R.id.txt_digit_mobile1);
        final EditText m02 = (EditText) layout.findViewById(R.id.txt_digit_mobile2);
        final EditText m03 = (EditText) layout.findViewById(R.id.txt_digit_mobile3);
        final EditText m04 = (EditText) layout.findViewById(R.id.txt_digit_mobile4);
        final EditText m05 = (EditText) layout.findViewById(R.id.txt_digit_mobile5);
        final EditText m06 = (EditText) layout.findViewById(R.id.txt_digit_mobile6);
        final EditText m07 = (EditText) layout.findViewById(R.id.txt_digit_mobile7);
        final EditText m08 = (EditText) layout.findViewById(R.id.txt_digit_mobile8);
        final EditText m09 = (EditText) layout.findViewById(R.id.txt_digit_mobile9);

        final TextView lbl = (TextView) layout.findViewById(R.id.lbl_add_customer_mobile);

        lbl.setTypeface(delegate.font_type);
        lbl.setTextSize(25);

        final TextView lbl_error = (TextView) layout.findViewById(R.id.error_add_mobile);
        lbl_error.setTypeface(delegate.font_type);
        lbl_error.setTextSize(25);

        final Button btn_ok = (Button) layout.findViewById(R.id.btn_ok);
        final Button btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
        final Button btn_delete = (Button) layout.findViewById(R.id.btn_delete);
        btn_delete.setTypeface(delegate.font_type);
        btn_delete.setTextSize(25);
        btn_ok.setTypeface(delegate.font_type);
        btn_ok.setTextSize(25);
        btn_cancel.setTypeface(delegate.font_type);
        btn_cancel.setTextSize(25);
        if(index != 99){
            btn_delete.setVisibility(View.VISIBLE);
        } else {
            btn_delete.setVisibility(View.GONE);
        }


        //add on condition
        if(type.equals("mobile")){
            lbl.setText(getString(R.string.popup_mobile));
            m00.setVisibility(View.VISIBLE);
        } else {
            lbl.setText(getString(R.string.popup_phone));
            m00.setVisibility(View.INVISIBLE);
        }

        if(index != 99){
            //edit mode
            String txtmobile ="";

            if(type.equals("mobile")){
                //mobile mode
                txtmobile = mobile_list.get(index);
                txtmobile = txtmobile.replace("-", "");
                switch (txtmobile.length()) {
                    case 10:
                        m09.setText(txtmobile.substring(9, 10));
                    case 9:
                        m08.setText(txtmobile.substring(8, 9));
                    case 8:
                        m07.setText(txtmobile.substring(7, 8));
                    case 7:
                        m06.setText(txtmobile.substring(6, 7));
                    case 6:
                        m05.setText(txtmobile.substring(5, 6));
                    case 5:
                        m04.setText(txtmobile.substring(4, 5));
                    case 4:
                        m03.setText(txtmobile.substring(3, 4));
                    case 3:
                        m02.setText(txtmobile.substring(2, 3));
                    case 2:
                        m01.setText(txtmobile.substring(1, 2));
                    case 1:
                        m00.setText(txtmobile.substring(0, 1));
                }
            } else {
                //phone modes
                txtmobile = phone_list.get(index);
                txtmobile = txtmobile.replace("-", "");
                switch (txtmobile.length()) {
                    case 9:
                        m09.setText(txtmobile.substring(8, 9));
                    case 8:
                        m08.setText(txtmobile.substring(7, 8));
                    case 7:
                        m07.setText(txtmobile.substring(6, 7));
                    case 6:
                        m06.setText(txtmobile.substring(5, 6));
                    case 5:
                        m05.setText(txtmobile.substring(4, 5));
                    case 4:
                        m04.setText(txtmobile.substring(3, 4));
                    case 3:
                        m03.setText(txtmobile.substring(2, 3));
                    case 2:
                        m02.setText(txtmobile.substring(1, 2));
                    case 1:
                        m01.setText(txtmobile.substring(0, 1));
                }
            }
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m01.getText().length()!=0 && m02.getText().length()!=0 && m03.getText().length()!=0 &&
                        m04.getText().length()!=0 && m05.getText().length()!=0 && m06.getText().length()!=0 &&
                        m07.getText().length()!=0 && m08.getText().length()!=0 && m09.getText().length()!=0) {
                    if(type.equals("mobile") && m01.getText().length()==0){
                        lbl_error.setVisibility(View.VISIBLE);
                    } else {
                        InputMethodManager imm = (InputMethodManager)getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(m09.getWindowToken(), 0);
                        lbl_error.setVisibility(View.INVISIBLE);
                        String string_phone = m01.getText().toString() + m02.getText().toString() +
                                m03.getText().toString() + m04.getText().toString() +
                                m05.getText().toString() + m06.getText().toString() +
                                m07.getText().toString() + m08.getText().toString() + m09.getText().toString();
                        if(type.equals("mobile")){
                            string_phone = m00.getText().toString() +string_phone;
                        }
                        addMobileinPage(string_phone,type);
                        popupAddMobile.dismiss();
                    }
                } else {
                    lbl_error.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddMobile.dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_list.set(index,"");
                if(type.equals("mobile")){
                    mobile_list.remove(index);
                    mobile_list.add("");
                } else {
                    phone_list.remove(index);
                    phone_list.add("");
                }

                if(type.equals("mobile")){
                    btnAddMobile.setVisibility(View.VISIBLE);
                } else {
                    btnAddPhone.setVisibility(View.VISIBLE);
                }
                updateMobile();

                popupAddMobile.dismiss();
            }
        });

        m01.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    m01.clearFocus();
                    m02.requestFocus();
                    return true;
                }
                return false;
            }
        });
        m02.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    m02.clearFocus();
                    m03.requestFocus();
                    return true;
                }
                return false;
            }
        });

        m03.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    m03.clearFocus();
                    m04.requestFocus();
                    return true;
                }
                return false;
            }
        });

        m04.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    m04.clearFocus();
                    m05.requestFocus();
                    return true;
                }
                return false;
            }
        });

        m05.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    m05.clearFocus();
                    m06.requestFocus();
                    return true;
                }
                return false;
            }
        });

        m06.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    m06.clearFocus();
                    m07.requestFocus();
                    return true;
                }
                return false;
            }
        });

        m07.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    m07.clearFocus();
                    m08.requestFocus();
                    return true;
                }
                return false;
            }
        });

        m08.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    m08.clearFocus();
                    m09.requestFocus();
                    return true;
                }
                return false;
            }
        });

        m09.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    if(m01.getText().length()!=0 && m02.getText().length()!=0 && m03.getText().length()!=0 &&
                            m04.getText().length()!=0 && m05.getText().length()!=0 && m06.getText().length()!=0 &&
                            m07.getText().length()!=0 && m08.getText().length()!=0 && m09.getText().length()!=0) {
                        if(type.equals("mobile") && m01.getText().length()==0){
                            lbl_error.setVisibility(View.VISIBLE);
                        } else {
                            InputMethodManager imm = (InputMethodManager)getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(m09.getWindowToken(), 0);
                            lbl_error.setVisibility(View.INVISIBLE);
                            String string_phone = m01.getText().toString() + m02.getText().toString() +
                                    m03.getText().toString() + m04.getText().toString() +
                                    m05.getText().toString() + m06.getText().toString() +
                                    m07.getText().toString() + m08.getText().toString() + m09.getText().toString();
                            if(type.equals("mobile")){
                                string_phone = m00.getText().toString() +string_phone;
                            }
                            addMobileinPage(string_phone,type);
                            popupAddMobile.dismiss();
                        }
                    } else {
                        lbl_error.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });
    }
    public void addMobileinPage(String mobile_number , String type){
        boolean statusAdd =false;
        if(type.equals("mobile")){
            int index=0;
            for (int i =1;i<mobile_list.size();i++){
                if(mobile_list.get(i).length()==0){
                    statusAdd = true;
                    index = i;
                    break;
                }
            }
            if(statusAdd){
                mobile_list.set(index, mobile_number);
            }
        } else {
            //phone mode
            int index=0;
            for (int i =1;i<phone_list.size();i++){
                if(phone_list.get(i).length()==0){
                    statusAdd = true;
                    index = i;
                    break;
                }
            }
            if(statusAdd){
                phone_list.set(index, mobile_number);
            }
        }
        updateMobile();

    }
    public void updateMobile(){
        boolean statusAdd =false;
        Log.e("mobile_list", mobile_list.toString());
        Log.e("phone_list", phone_list.toString());

        statusAdd = true;
        for (int i =1;i<phone_list.size();i++){
            if(phone_list.get(i).length()==0){
                statusAdd = false;
            }
        }
        if(statusAdd){
            btnAddPhone.setVisibility(View.INVISIBLE);
        }
        statusAdd = true;
        for (int i =1;i<mobile_list.size();i++){
            if(mobile_list.get(i).length()==0){
                statusAdd = false;
            }
        }
        if(statusAdd){
            btnAddMobile.setVisibility(View.INVISIBLE);
        }

        if(mobile_list.size()>1){
            lbl_add_customer_mobile1.setText(mobile_list.get(1));
            lbl_add_customer_mobile1.setOnClickListener(this);
        }
        if(mobile_list.size()>2){
            lbl_add_customer_mobile2.setText(mobile_list.get(2));
            lbl_add_customer_mobile2.setOnClickListener(this);
        }
        if(mobile_list.size()>3){
            lbl_add_customer_mobile3.setText(mobile_list.get(3));
            lbl_add_customer_mobile3.setOnClickListener(this);
        }
        if(mobile_list.size()>4){
            lbl_add_customer_mobile4.setText(mobile_list.get(4));
            lbl_add_customer_mobile4.setOnClickListener(this);
        }

        if(phone_list.size()>1){
            lbl_add_customer_phone1.setText(phone_list.get(1));
            lbl_add_customer_phone1.setOnClickListener(this);
        }
        if(phone_list.size()>2){
            lbl_add_customer_phone2.setText(phone_list.get(2));
            lbl_add_customer_phone2.setOnClickListener(this);
        }
        if(phone_list.size()>3){
            lbl_add_customer_phone3.setText(phone_list.get(3));
            lbl_add_customer_phone3.setOnClickListener(this);
        }
        if(phone_list.size()>4){
            lbl_add_customer_phone4.setText(phone_list.get(4));
            lbl_add_customer_phone4.setOnClickListener(this);
        }
    }
}
