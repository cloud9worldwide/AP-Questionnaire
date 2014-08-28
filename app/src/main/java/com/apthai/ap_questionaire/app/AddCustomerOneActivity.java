package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
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
import android.widget.Toast;

import com.cloud9worldwide.questionnaire.data.AddressData;
import com.cloud9worldwide.questionnaire.data.ContactData;
import com.cloud9worldwide.questionnaire.data.ValTextData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AddCustomerOneActivity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    ImageButton btnMenu, btnNext, btnBack, btnEN, btnTH;
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

    TextView txt_header;
    TextView lbl_add_customer_phone1, lbl_add_customer_phone2, lbl_add_customer_phone3, lbl_add_customer_phone4;
    TextView lbl_add_customer_mobile1, lbl_add_customer_mobile2, lbl_add_customer_mobile3, lbl_add_customer_mobile4;

    ContactData new_customer;
    ArrayList<String> listfix;
    int indexPrefix, indexNationality, indexCountry;

    TextView lblPrefix,lbl_prefix_extra2,lbl_add_customer_name,lbl_add_customer_surname,lblNickname;
    TextView lblEmail,lbl_add_customer_mobile_line1,lbl_add_customer_mobile_line2,lbl_add_customer_tel_home_other_line1;
    TextView lbl_add_customer_tel_home_other_line2,lbl_add_customer_birthday,lblCountry,lblNationality;
    String focus_mobile_number;

    Context ctx;
    boolean loadready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_one);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        AddressData home = new AddressData("", "", "", "", "", "", "", "", "", "Thailand", "", "");
        AddressData work = new AddressData("", "", "", "", "", "", "", "", "", "", "", "");
        new_customer = new ContactData("คุณ", "", "", "", "", "", null, null,null);
        new_customer.setAddress(home);
        new_customer.setNationality("Thailand");
        new_customer.setAddressWork(work);

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
                loadready = true;
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
            AddressData home = new AddressData("", "", "", "", "", "", "", "", "", "Thailand", "", "");
            AddressData work = new AddressData("", "", "", "", "", "", "", "", "", "", "", "");
            new_customer = new ContactData("คุณ", "", "", "", "", "", null, null,null);
            new_customer.setAddress(home);
            new_customer.setNationality("Thailand");
            new_customer.setAddressWork(work);
        } else {

            txtPrefix.setText(new_customer.getPrefix_vip().toString());
            if(indexPrefix != -1){
                ddlPrefix.setSelection(indexPrefix);
            }

            if(!new_customer.getFname().trim().equals("null")){
                txtFirstName.setText(new_customer.getFname());
            }
            if(!new_customer.getLname().trim().equals("null")){
                txtLastName.setText(new_customer.getLname());
            }
            if(!new_customer.getNickname().trim().equals("null")){
                txtNickname.setText(new_customer.getNickname());
            }
            if(!new_customer.getEmail().trim().equals("null")){
                txtEmail.setText(new_customer.getEmail());
            }
            if(!new_customer.getBirthdate().trim().equals("null")){
                datePicker.setText(new_customer.getBirthdate());
            }

            Log.e("indexNationality indexCountry",indexNationality +" "+ indexCountry);
            if(indexNationality != -1){
                ddlNationality.setSelection(indexNationality);
            }
            if(indexCountry != -1){
                ddlCountry.setSelection(indexCountry);
            }
            mobile_list = new_customer.getMobiles();
            for(int i = 0; i<mobile_list.size();i++){
                if(mobile_list.get(i).equals("null")){
                    mobile_list.set(i,"");
                }
            }

            String mobile = mobile_list.get(0);
            mobile = mobile.replace("-", "");

            switch (mobile.length()) {
                case 10: mobile10.setText(mobile.substring(9, 10));
                case 9: mobile9.setText(mobile.substring(8, 9));
                case 8: mobile8.setText(mobile.substring(7, 8));
                case 7: mobile7.setText(mobile.substring(6, 7));
                case 6: mobile6.setText(mobile.substring(5, 6));
                case 5: mobile5.setText(mobile.substring(4, 5));
                case 4: mobile4.setText(mobile.substring(3, 4));
                case 3: mobile3.setText(mobile.substring(2, 3));
                case 2: mobile2.setText(mobile.substring(1, 2));
                case 1: mobile1.setText(mobile.substring(0, 1));
            }

            phone_list = new_customer.getTels();
            for(int i = 0; i<phone_list.size();i++){
                if(phone_list.get(i).equals("null")){
                    phone_list.set(i,"");
                }
            }


            String tel = phone_list.get(0);
            tel = tel.replace("-", "");
            int length = tel.length();
            switch (length) {
                case 9: home9.setText(tel.substring(8, 9));
                case 8: home8.setText(tel.substring(7, 8));
                case 7: home7.setText(tel.substring(6, 7));
                case 6: home6.setText(tel.substring(5, 6));
                case 5: home5.setText(tel.substring(4, 5));
                case 4: home4.setText(tel.substring(3, 4));
                case 3: home3.setText(tel.substring(2, 3));
                case 2: home2.setText(tel.substring(1, 2));
                case 1: home1.setText(tel.substring(0, 1));
            }
            updateMobile();
        }
        txtFirstName.requestFocus();
    }

    private void setObject() {
        ctx = this;
        focus_mobile_number ="";
        footer = (RelativeLayout) findViewById(R.id.footer);

        project_name = (TextView) findViewById(R.id.project_name);
        project_name.setText(delegate.project.getName());
        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);

        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_header.setTypeface(delegate.font_type);
        txt_header.setTextSize(35);

        question_title = (TextView) findViewById(R.id.question_title);
        question_title.setTypeface(delegate.font_type);
        question_title.setTextSize(30);

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

        btnEN = (ImageButton) findViewById(R.id.btnEN);
        btnTH = (ImageButton) findViewById(R.id.btnTH);
        btnEN.setOnClickListener(this);
        btnTH.setOnClickListener(this);

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
            if(loadready)
            changeLanguege();
        }
    }

    private void changeLanguege(){

        project_name.setText(delegate.project.getName());
        if(delegate.service.getLg().equals("en")){
            btnEN.setImageResource(R.drawable.btn_en_);
            btnTH.setImageResource(R.drawable.btn_th);
        } else {
            btnEN.setImageResource(R.drawable.btn_en);
            btnTH.setImageResource(R.drawable.btn_th_);
        }
        question_title.setText(R.string.title_activity_customer_look_info);
        txt_header.setText(R.string.please_fill_completed);

        txtPrefix.setHint(R.string.add_customer_prefix_extra);
        txtFirstName.setHint(R.string.add_customer_name);
        txtLastName.setHint(R.string.add_customer_surname);
        txtNickname.setHint(R.string.add_customer_nickname);
        txtEmail.setHint(R.string.add_customer_email);

        lblPrefix.setText(R.string.add_customer_prefix);
        lbl_prefix_extra2.setText(R.string.add_customer_prefix_extra);
        lbl_add_customer_name.setText(R.string.add_customer_name);
        lbl_add_customer_surname.setText(R.string.add_customer_surname);
        lblNickname.setText(R.string.add_customer_nickname);
        lblEmail.setText(R.string.add_customer_email);
        lbl_add_customer_mobile_line1.setText(R.string.add_customer_mobile_line1);
        lbl_add_customer_mobile_line2.setText(R.string.add_customer_mobile_line2);
        lbl_add_customer_tel_home_other_line1.setText(R.string.add_customer_tel_home_BKK_line1);
        lbl_add_customer_tel_home_other_line2.setText(R.string.add_customer_tel_home_BKK_line2);
        lbl_add_customer_birthday.setText(R.string.add_customer_birthday);
        lblCountry.setText(R.string.add_customer_other_country);
        lblNationality.setText(R.string.add_customer_nationality);

    }

    private void setKeyListener(){

        txtPrefix.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    txtPrefix.clearFocus();
                    txtFirstName.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txtFirstName.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.e("keyCode",keyCode+"");
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
                Log.e("keyCode",keyCode+"");
                if ((keyCode == KeyEvent.KEYCODE_TAB ||keyCode == KeyEvent.KEYCODE_ENTER) && event.getAction() == KeyEvent.ACTION_UP){
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

                Log.e("keyCode1",keyCode+"");
                if (event.getAction() == KeyEvent.ACTION_UP){
                    if(keyCode ==KeyEvent.KEYCODE_DEL){
                        //delete
                    } else {
                        mobile1.clearFocus();
                        mobile2.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });

        mobile2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.e("keyCode2",keyCode+"");
                if (event.getAction() == KeyEvent.ACTION_UP){
                    if(keyCode ==KeyEvent.KEYCODE_DEL){
                        mobile1.requestFocus();
                        mobile2.clearFocus();
                    } else {
                        mobile2.clearFocus();
                        mobile3.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });

        mobile3.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP){

                    if(keyCode ==KeyEvent.KEYCODE_DEL){
                        mobile2.requestFocus();
                        mobile3.clearFocus();
                    } else {
                        mobile3.clearFocus();
                        mobile4.requestFocus();
                    }


                    return true;
                }
                return false;
            }
        });

        mobile4.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){

                    if(keyCode ==KeyEvent.KEYCODE_DEL){
                        mobile3.requestFocus();
                        mobile4.clearFocus();
                    } else {
                        mobile4.clearFocus();
                        mobile5.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });

        mobile5.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){

                    if(keyCode ==KeyEvent.KEYCODE_DEL){
                        mobile4.requestFocus();
                        mobile5.clearFocus();
                    } else {
                        mobile5.clearFocus();
                        mobile6.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });

        mobile6.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){

                    if(keyCode ==KeyEvent.KEYCODE_DEL){
                        mobile5.requestFocus();
                        mobile6.clearFocus();
                    } else {
                        mobile6.clearFocus();
                        mobile7.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });

        mobile7.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){

                    if(keyCode ==KeyEvent.KEYCODE_DEL){
                        mobile6.requestFocus();
                        mobile7.clearFocus();
                    } else {
                        mobile7.clearFocus();
                        mobile8.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });

        mobile8.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){

                    if(keyCode == KeyEvent.KEYCODE_DEL){
                        mobile7.requestFocus();
                        mobile8.clearFocus();
                    } else {
                        mobile8.clearFocus();
                        mobile9.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });

        mobile9.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){

                    if(keyCode == KeyEvent.KEYCODE_DEL){
                        mobile8.requestFocus();
                        mobile9.clearFocus();
                    } else {
                        mobile9.clearFocus();
                        mobile10.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });
        mobile10.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    if(keyCode ==KeyEvent.KEYCODE_DEL){
                        mobile9.requestFocus();
                        mobile10.clearFocus();
                    } else {
                        InputMethodManager imm = (InputMethodManager)getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mobile10.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });

        home1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    if(keyCode ==KeyEvent.KEYCODE_DEL){

                    } else {
                        home1.clearFocus();
                        home2.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });


        home2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                 if (event.getAction() == KeyEvent.ACTION_UP){
                     if(keyCode == KeyEvent.KEYCODE_DEL ){
                         home1.requestFocus();
                         home2.clearFocus();
                     } else{
                         home2.clearFocus();
                         home3.requestFocus();
                     }
                    return true;
                }
                return false;
            }
        });

        home3.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){

                    if(keyCode == KeyEvent.KEYCODE_DEL ){
                        home2.requestFocus();
                        home3.clearFocus();
                    } else {
                        home3.clearFocus();
                        home4.requestFocus();
                    }
                        return true;
                }
                return false;
            }
        });

        home4.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    if(keyCode == KeyEvent.KEYCODE_DEL ){
                        home3.requestFocus();
                        home4.clearFocus();
                    } else{
                        home4.clearFocus();
                        home5.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });

        home5.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    if(keyCode == KeyEvent.KEYCODE_DEL ){
                        home4.requestFocus();
                        home5.clearFocus();
                    } else{
                        home5.clearFocus();
                        home6.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });

        home6.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    if(keyCode == KeyEvent.KEYCODE_DEL ){
                        home5.requestFocus();
                        home6.clearFocus();
                    } else{
                        home6.clearFocus();
                        home7.requestFocus();
                    }


                    return true;
                }
                return false;
            }
        });

        home7.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    if(keyCode == KeyEvent.KEYCODE_DEL ){
                        home6.requestFocus();
                        home7.clearFocus();
                    } else{
                        home7.clearFocus();
                        home8.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });

        home8.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    if(keyCode == KeyEvent.KEYCODE_DEL ){
                        home7.requestFocus();
                        home8.clearFocus();
                    } else{
                        home8.clearFocus();
                        home9.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });

        home9.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    if(keyCode == KeyEvent.KEYCODE_DEL ){
                        home8.requestFocus();
                        home9.clearFocus();
                    } else{
                        InputMethodManager imm = (InputMethodManager)getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(home9.getWindowToken(), 0);
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

        lblPrefix = (TextView) findViewById(R.id.lblPrefix);
        lbl_prefix_extra2 = (TextView) findViewById(R.id.lbl_prefix_extra2);
        lbl_add_customer_name = (TextView) findViewById(R.id.lbl_add_customer_name);
        lbl_add_customer_surname = (TextView) findViewById(R.id.lbl_add_customer_surname);
        lblNickname = (TextView) findViewById(R.id.lblNickname);
        lblEmail = (TextView) findViewById(R.id.lblEmail);
        lbl_add_customer_mobile_line1 = (TextView) findViewById(R.id.lbl_add_customer_mobile_line1);
        lbl_add_customer_mobile_line2 = (TextView) findViewById(R.id.lbl_add_customer_mobile_line2);
        lbl_add_customer_tel_home_other_line1 = (TextView) findViewById(R.id.lbl_add_customer_tel_home_other_line1);
        lbl_add_customer_tel_home_other_line2 = (TextView) findViewById(R.id.lbl_add_customer_tel_home_other_line2);
        lbl_add_customer_birthday = (TextView) findViewById(R.id.lbl_add_customer_birthday);
        lblCountry = (TextView) findViewById(R.id.lblCountry);
        lblNationality = (TextView) findViewById(R.id.lblNationality);

        lblPrefix.setTypeface(delegate.font_type);
        lblPrefix.setTextSize(25);
        lbl_prefix_extra2.setTypeface(delegate.font_type);
        lbl_prefix_extra2.setTextSize(25);
        lbl_add_customer_name.setTypeface(delegate.font_type);
        lbl_add_customer_name.setTextSize(25);
        lbl_add_customer_surname.setTypeface(delegate.font_type);
        lbl_add_customer_surname.setTextSize(25);
        lblNickname.setTypeface(delegate.font_type);
        lblNickname.setTextSize(25);
        lblEmail.setTypeface(delegate.font_type);
        lblEmail.setTextSize(25);
        lbl_add_customer_mobile_line1.setTypeface(delegate.font_type);
        lbl_add_customer_mobile_line1.setTextSize(20);
        lbl_add_customer_mobile_line2.setTypeface(delegate.font_type);
        lbl_add_customer_mobile_line2.setTextSize(15);
        lbl_add_customer_tel_home_other_line1.setTypeface(delegate.font_type);
        lbl_add_customer_tel_home_other_line1.setTextSize(20);
        lbl_add_customer_tel_home_other_line2.setTypeface(delegate.font_type);
        lbl_add_customer_tel_home_other_line2.setTextSize(15);
        lbl_add_customer_birthday.setTypeface(delegate.font_type);
        lbl_add_customer_birthday.setTextSize(25);
        lblCountry.setTypeface(delegate.font_type);
        lblCountry.setTextSize(25);
        lblNationality.setTypeface(delegate.font_type);
        lblNationality.setTextSize(25);
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
                    lbl_prefix_extra2.setVisibility(View.VISIBLE);
                    txtPrefix.setVisibility(View.VISIBLE);
                } else {
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

        home.setCountry(ddlCountry.getSelectedItem().toString());
        new_customer.setNationality(ddlNationality.getSelectedItem().toString());

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
    private boolean validate() {
        boolean status = true;

        if(txtFirstName.getText().toString().length() ==0){
            lbl_add_customer_name.setText(Html.fromHtml(getString(R.string.add_customer_name) +"<font color=\"#FF0000\"> *</font>"));
            status = false;
        } else {
            lbl_add_customer_name.setText(getString(R.string.add_customer_name));
        }
        if(txtLastName.getText().toString().length() ==0){
            lbl_add_customer_surname.setText(Html.fromHtml(getString(R.string.add_customer_surname) +"<font color=\"#FF0000\"> *</font>"));
            status = false;
        } else {
            lbl_add_customer_surname.setText(getString(R.string.add_customer_surname));
        }

        if (mobile1.getText().toString().length() > 0 && mobile2.getText().toString().length() > 0
                && mobile3.getText().toString().length() > 0 && mobile4.getText().toString().length() > 0
                && mobile5.getText().toString().length() > 0 && mobile6.getText().toString().length() > 0
                && mobile7.getText().toString().length() > 0 && mobile8.getText().toString().length() > 0
                && mobile9.getText().toString().length() > 0 && mobile10.getText().toString().length() > 0) {
            lbl_add_customer_mobile_line1.setText(getString(R.string.add_customer_mobile_line1));
        } else {
            status = false;
            lbl_add_customer_mobile_line1.setText(Html.fromHtml(getString(R.string.add_customer_mobile_line1) +"<font color=\"#FF0000\"> *</font>"));
        }

        if(txtEmail.getText().toString().length() !=0){
            if(delegate.emailValidator(txtEmail.getText().toString())){
                lblEmail.setText(Html.fromHtml(getString(R.string.add_customer_email)));
            } else {
                lblEmail.setText(Html.fromHtml(getString(R.string.add_customer_email) +"<font color=\"#FF0000\"> *</font>"));
                status = false;
            }
        } else {
            lblEmail.setText(Html.fromHtml(getString(R.string.add_customer_email)));
        }

        return status;
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
            if(validate()){
                packData();
                if(ddlCountry.getSelectedItem().toString().equals("Thailand")){
                    startActivityForResult(new Intent(this, AddCustomerTHActivity.class),0);
                } else {
                    startActivityForResult(new Intent(this, AddCustomerENActivity.class),0);
                }
            } else {
                Toast.makeText(this, R.string.error_customer_one, Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.btnAddMobiles){
//            showPopupAddMobile(this,"mobile",99);
            showPopupAddMobile2(this,"mobile",99);
        } else if(v.getId() == R.id.lbl_add_customer_mobile1){
            if(mobile_list.get(1).length()!=0) {
                showPopupAddMobile2(this, "mobile", 1);
//                showPopupAddMobile(this, "mobile", 1);
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
//            showPopupAddMobile(this,"phone",99);
            showPopupAddMobile2(this,"phone",99);
        } else if(v.getId() == R.id.lbl_add_customer_phone1){
            if(phone_list.get(1).length()!=0){
                showPopupAddMobile2(this,"phone",1);
//                showPopupAddMobile(this,"phone",1);
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
        delegate.customer_selected = null;
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
    public void showPopupAddMobile(final Activity context, final String type, final int index) {
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup_mobile);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.activity_add_moblie, viewGroup);
        popupAddMobile = new PopupWindow(context);
        popupAddMobile.setFocusable(true);
        popupAddMobile.setContentView(layout);
        popupAddMobile.setWidth(delegate.dpToPx(500));
        popupAddMobile.setHeight(delegate.dpToPx(150));
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
                        lbl_error.setText(R.string.error_add_phone);
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
                    lbl_error.setText(R.string.error_add_phone);
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
        m00.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    m00.clearFocus();
                    m01.requestFocus();
                    return true;
                }
                return false;
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
                            lbl_error.setText(R.string.error_add_phone);
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
                        lbl_error.setText(R.string.error_add_phone);
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
            lbl_add_customer_mobile1.setText(numberformat(mobile_list.get(1)));
            lbl_add_customer_mobile1.setOnClickListener(this);
        }
        if(mobile_list.size()>2){
            lbl_add_customer_mobile2.setText(numberformat(mobile_list.get(2)));
            lbl_add_customer_mobile2.setOnClickListener(this);
        }
        if(mobile_list.size()>3){
            lbl_add_customer_mobile3.setText(numberformat(mobile_list.get(3)));
            lbl_add_customer_mobile3.setOnClickListener(this);
        }
        if(mobile_list.size()>4){
            lbl_add_customer_mobile4.setText(numberformat(mobile_list.get(4)));
            lbl_add_customer_mobile4.setOnClickListener(this);
        }

        if(phone_list.size()>1){
            lbl_add_customer_phone1.setText(numberformat(phone_list.get(1)));
            lbl_add_customer_phone1.setOnClickListener(this);
        }
        if(phone_list.size()>2){
            lbl_add_customer_phone2.setText(numberformat(phone_list.get(2)));
            lbl_add_customer_phone2.setOnClickListener(this);
        }
        if(phone_list.size()>3){
            lbl_add_customer_phone3.setText(numberformat(phone_list.get(3)));
            lbl_add_customer_phone3.setOnClickListener(this);
        }
        if(phone_list.size()>4){
            lbl_add_customer_phone4.setText(numberformat(phone_list.get(4)));
            lbl_add_customer_phone4.setOnClickListener(this);
        }
    }
    public String numberformat(String phone){
        if(phone.length()==10){
            //mobile mode
            return phone.substring(0,3) +"-"+phone.substring(3,6)+"-"+phone.substring(6,10);
        } else if(phone.length()==9){
            //home mode
            if(phone.substring(0,2).equals("02")){
                return phone.substring(0,2) +"-"+phone.substring(2,5)+"-"+phone.substring(5,9);
            }
            return phone.substring(0,3) +"-"+phone.substring(3,6)+"-"+phone.substring(6,9);
        }
        //do not anythings
        return phone;
    }
    public void showPopupAddMobile2(final Activity context, final String type, final int index) {
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup_mobile);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.activity_add_moblie2, viewGroup);
        popupAddMobile = new PopupWindow(context);
        popupAddMobile.setFocusable(true);
        popupAddMobile.setContentView(layout);
        popupAddMobile.setWidth(delegate.dpToPx(500));
        popupAddMobile.setHeight(delegate.dpToPx(150));
        popupAddMobile.setBackgroundDrawable(null);
        popupAddMobile.showAtLocation(layout, Gravity.CENTER, 0, 0);
        View view_instance = (View) layout.findViewById(R.id.popup_mobile);
        focus_mobile_number ="";

        final EditText txt_digit_mobile = (EditText) layout.findViewById(R.id.txt_digit_mobile);

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
        if (index != 99) {
            btn_delete.setVisibility(View.VISIBLE);
        } else {
            btn_delete.setVisibility(View.GONE);
        }
        //add on condition
        if (type.equals("mobile")) {
            lbl.setText(getString(R.string.popup_mobile));
        } else {
            lbl.setText(getString(R.string.popup_phone));
        }

        if (index != 99) {
            //edit mode
            String txtmobile = "";

            if (type.equals("mobile")) {
                //mobile mode
                txtmobile = mobile_list.get(index);
                txtmobile = txtmobile.replace("-", "");
                if(txtmobile.length() ==10){
                    String new_format_mobile = "";
                    for(int i = 0; i<txtmobile.length(); i++){
                        if(i==2 || i==5){
                            new_format_mobile = new_format_mobile + txtmobile.substring(i,i+1) + "  -  ";
                        } else if(i==9){
                            new_format_mobile = new_format_mobile + txtmobile.substring(i,i+1);
                        } else {
                            new_format_mobile = new_format_mobile + txtmobile.substring(i,i+1)+"  ";
                        }
                    }
                    txt_digit_mobile.setText(new_format_mobile);
                }
            } else {
                //phone modes
                txtmobile = phone_list.get(index);
                txtmobile = txtmobile.replace("-", "");
                if(txtmobile.length() ==9){
                    String new_format_mobile = "";
                    for(int i = 0; i<txtmobile.length(); i++){
                        if(i==1 || i==4){
                            new_format_mobile = new_format_mobile + txtmobile.substring(i,i+1) + "  -  ";
                        } else if(i==8){
                            new_format_mobile = new_format_mobile + txtmobile.substring(i,i+1);
                        } else {
                            new_format_mobile = new_format_mobile + txtmobile.substring(i,i+1)+"  ";
                        }
                    }
                    txt_digit_mobile.setText(new_format_mobile);
                }
            }
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((type.equals("mobile") && txt_digit_mobile.getText().length()==34) || (type.equals("phone") && txt_digit_mobile.getText().length()==31)){
                        InputMethodManager imm = (InputMethodManager)getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(txt_digit_mobile.getWindowToken(), 0);
                        lbl_error.setVisibility(View.INVISIBLE);
                        String string_phone = txt_digit_mobile.getText().toString().replace(" ", "").replace("-","");
                    if(index !=99){
                        //edit
                        if(type.equals("mobile")){
                            mobile_list.set(index, string_phone);
                        } else {
                            phone_list.set(index, string_phone);
                        }
                    } else {
                        addMobileinPage(string_phone,type);
                    }

                    updateMobile();
                        popupAddMobile.dismiss();


                } else {
                    lbl_error.setText(R.string.error_add_phone);
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
                mobile_list.set(index, "");
                if (type.equals("mobile")) {
                    mobile_list.remove(index);
                    mobile_list.add("");
                } else {
                    phone_list.remove(index);
                    phone_list.add("");
                }

                if (type.equals("mobile")) {
                    btnAddMobile.setVisibility(View.VISIBLE);
                } else {
                    btnAddPhone.setVisibility(View.VISIBLE);
                }
                updateMobile();

                popupAddMobile.dismiss();
            }
        });
        txt_digit_mobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(txt_digit_mobile.getText().length() !=0){
                    txt_digit_mobile.setSelection(txt_digit_mobile.getText().length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(start+1== txt_digit_mobile.getText().length()|| start== txt_digit_mobile.getText().length() ){
                    String check;
                    if(type.equals("mobile") && txt_digit_mobile.getText().length()<=34){
                        if(focus_mobile_number.length() > txt_digit_mobile.getText().length()) {
                            if (txt_digit_mobile.getText().toString().length() == 34) {
                                focus_mobile_number = focus_mobile_number.substring(0, focus_mobile_number.length() - 1);
                                txt_digit_mobile.setText(focus_mobile_number);
                            } else if (txt_digit_mobile.getText().toString().length() == 33) {
                                focus_mobile_number = txt_digit_mobile.getText().toString();
                            } else {
                                if (focus_mobile_number.length() == 12 || focus_mobile_number.length() == 24) {
                                    focus_mobile_number = focus_mobile_number.substring(0, focus_mobile_number.length() - 6);
                                } else {
                                    focus_mobile_number = focus_mobile_number.substring(0, focus_mobile_number.length() - 3);
                                }
                                txt_digit_mobile.setText(focus_mobile_number);
                            }

                        } else {
                            if(txt_digit_mobile.getText().length()<2){
                                check ="";
                            } else {
                                check = txt_digit_mobile.getText().charAt(txt_digit_mobile.getText().toString().length()-1)+"";
                            }
                            if (!check.equals(" ") && txt_digit_mobile.getText().toString().length() != 34 &&txt_digit_mobile.getText().length()!=0) {
                                if (focus_mobile_number.length() == 6 || focus_mobile_number.length() == 18) {
                                    focus_mobile_number = txt_digit_mobile.getText().toString() + "  -  ";
                                } else {
                                    focus_mobile_number = txt_digit_mobile.getText().toString() + "  ";
                                }
                                txt_digit_mobile.setText(focus_mobile_number);
                            } else if (txt_digit_mobile.getText().toString().length() == 34) {
                                focus_mobile_number = txt_digit_mobile.getText().toString();
                            }
                        }
                    } else if(type.equals("phone") && txt_digit_mobile.getText().length()<=31){
                        if(focus_mobile_number.length() > txt_digit_mobile.getText().length()) {
                            if (txt_digit_mobile.getText().toString().length() == 31) {
                                focus_mobile_number = focus_mobile_number.substring(0, focus_mobile_number.length() - 1);
                                txt_digit_mobile.setText(focus_mobile_number);
                            } else if(txt_digit_mobile.getText().toString().length() == 30) {
                                focus_mobile_number = txt_digit_mobile.getText().toString();
                            } else {
                                if (focus_mobile_number.length() == 9 || focus_mobile_number.length() == 21) {
                                    focus_mobile_number = focus_mobile_number.substring(0, focus_mobile_number.length() - 6);
                                } else {
                                    focus_mobile_number = focus_mobile_number.substring(0, focus_mobile_number.length() - 3);
                                }
                                txt_digit_mobile.setText(focus_mobile_number);
                            }

                        } else {
                            if(txt_digit_mobile.getText().length()<2){
                                check ="";
                            } else {
                                check = txt_digit_mobile.getText().charAt(txt_digit_mobile.getText().toString().length()-1)+"";
                            }
                            if (!check.equals(" ") && txt_digit_mobile.getText().toString().length() != 31 &&txt_digit_mobile.getText().length()!=0) {
                                if (focus_mobile_number.length() == 3 || focus_mobile_number.length() == 15) {
                                    focus_mobile_number = txt_digit_mobile.getText().toString() + "  -  ";
                                } else if(focus_mobile_number.equals("0  2")){

                                } else {
                                    focus_mobile_number = txt_digit_mobile.getText().toString() + "  ";
                                }
                                txt_digit_mobile.setText(focus_mobile_number);
                            } else if (txt_digit_mobile.getText().toString().length() == 31) {
                                focus_mobile_number = txt_digit_mobile.getText().toString();
                            }
                        }
                    } else {
                        txt_digit_mobile.setText(focus_mobile_number);
                    }

                } else {
                    if(focus_mobile_number.length()!=0 && !txt_digit_mobile.getText().toString().equals(focus_mobile_number)){
                        txt_digit_mobile.setText(focus_mobile_number);
                    }
                }
            }
        });
    }
}
