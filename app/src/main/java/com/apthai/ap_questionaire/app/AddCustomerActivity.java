package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloud9worldwide.questionnaire.data.AddressData;
import com.cloud9worldwide.questionnaire.data.ContactData;
import com.cloud9worldwide.questionnaire.data.ValTextData;

import java.util.ArrayList;
import java.util.Calendar;

public class AddCustomerActivity extends Activity implements OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total = 5;
    ImageButton btn_menu, btnSend, btn_back;
    Spinner ddlOtherCountry, ddlProvince, ddlDistrict, ddlSubDistrict;
    EditText txtPrefix, txtFirstName, txtLastName, txtNickname, txtEmail;
    EditText txtWork, txtDistrictWork;
    TextView txtPostcode,datePicker;
    ContactData new_customer;

    EditText home1, home2, home3, home4, home5, home6, home7;
    EditText other1, other2, other3, other4, other5, other6, other7, other8;
    EditText mobile1, mobile2, mobile3, mobile4, mobile5, mobile6, mobile7, mobile8, mobile9;

    EditText txtHomeID, txtMoo, txtBuilding, txtFloor, txtRoom, txtSoi, txtRoad;

    TextView project_name,question_title;
    RelativeLayout root_view;
    static PopupWindow popup;

    int mYear, mMonth, mDay;
    questionniare_delegate delegate;

    ContactData customer_selected;
    String txtPromp;

    Button btnAddMobile;
    static PopupWindow popupAddMobile;
    LinearLayout addMobiles;
    ArrayList<String> mobile_list;

    RelativeLayout footer;

    int status;

    //koy
    TextView txt_header,lblPrefix,lbl_add_customer_name, lbl_add_customer_surname, lblNickname,
            lbl_add_customer_birthday, lblEmail,lbl_add_customer_tel_home_BKK_line1, lbl_add_customer_tel_home_line2,
            lbl_digit_tel_home_BKK1, lbl_digit_tel_home_BKK2, lbl_add_customer_tel_home_other_line1,
            lbl_add_customer_tel_home_other_line2, lbl_digit_tel_home_other1, lbl_add_customer_mobile,
            lbl_digit_mobile1, lblWork, lbl_add_customer_district_work, lblCountry, lblHomeID, lbl_add_customer_moo,
            lbl_add_customer_building, lbl_add_customer_floor, lbl_add_customer_room, lbl_add_customer_soi,
            lbl_add_customer_road, lblProvince, lbl_add_customer_district, lbl_add_customer_sub_district,
            lblPostcode;

    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        ctx = this;
        setObject();
        status =8;

        if (delegate.customer_selected != null) {
            customer_selected = delegate.customer_selected;
            txtPrefix.setText(customer_selected.getPrefix());
            txtFirstName.setText(customer_selected.getFname());
            txtLastName.setText(customer_selected.getLname());
            txtNickname.setText(customer_selected.getNickname());
            txtEmail.setText(customer_selected.getEmail());
            txtWork.setText(customer_selected.getAddressWork().getVillage());
            txtDistrictWork.setText(customer_selected.getAddressWork().getDistrict());


            datePicker.setText(customer_selected.getBirthdate());
            String tel = customer_selected.getAddress().getTel();
            tel = tel.replace("-", "");
            int length = tel.length();
            switch (length) {
                case 9:
                    home7.setText(tel.substring(8, 9));
                case 8:
                    home6.setText(tel.substring(7, 8));
                case 7:
                    home5.setText(tel.substring(6, 7));
                case 6:
                    home4.setText(tel.substring(5, 6));
                case 5:
                    home3.setText(tel.substring(4, 5));
                case 4:
                    home2.setText(tel.substring(3, 4));
                case 3:
                    home1.setText(tel.substring(2, 3));
            }
            String other = customer_selected.getAddress().getTelExt();
            other = other.replace("-", "");
            length = other.length();
            switch (length) {
                case 9:
                    other8.setText(other.substring(8, 9));
                case 8:
                    other7.setText(other.substring(7, 8));
                case 7:
                    other6.setText(other.substring(6, 7));
                case 6:
                    other5.setText(other.substring(5, 6));
                case 5:
                    other4.setText(other.substring(4, 5));
                case 4:
                    other3.setText(other.substring(3, 4));
                case 3:
                    other2.setText(other.substring(2, 3));
                case 2:
                    other1.setText(other.substring(1, 2));
            }
            mobile_list = customer_selected.getMobiles();

            String mobile = customer_selected.getMobiles().get(0);
            mobile = mobile.replace("-", "");
            switch (mobile.length()) {
                case 10:
                    mobile9.setText(mobile.substring(9, 10));
                case 9:
                    mobile8.setText(mobile.substring(8, 9));
                case 8:
                    mobile7.setText(mobile.substring(7, 8));
                case 7:
                    mobile6.setText(mobile.substring(6, 7));
                case 6:
                    mobile5.setText(mobile.substring(5, 6));
                case 5:
                    mobile4.setText(mobile.substring(4, 5));
                case 4:
                    mobile3.setText(mobile.substring(3, 4));
                case 3:
                    mobile2.setText(mobile.substring(2, 3));
                case 2:
                    mobile1.setText(mobile.substring(1, 2));
            }
            updateMobile2();

            boolean statusAdd = true;
            Log.e("mobile",mobile_list.toString());
            for (int i =1;i<mobile_list.size();i++){
                if(mobile_list.get(i).length()==0){
                    statusAdd = false;
                }
            }
            if(statusAdd){
                btnAddMobile.setVisibility(View.GONE);
            }

            txtHomeID.setText(customer_selected.getAddress().getHouseId());
            txtMoo.setText(customer_selected.getAddress().getMoo());
            txtBuilding.setText(customer_selected.getAddress().getVillage());
            txtSoi.setText(customer_selected.getAddress().getSoi());
            txtRoad.setText(customer_selected.getAddress().getRoad());

            status = 0;
            int indexProvince = 300;
            for (int i = 0; i < delegate.service.getProvinces().size(); i++) {
                if (delegate.service.getProvinces().get(i).getText().equals(customer_selected.getAddress().getProvince())) {
                    indexProvince = i;
                    break;
                }
            }
            if (indexProvince != 300) {
                ddlProvince.setSelection(indexProvince);
                status++;
            }

        } else {

//            txtFirstName.setText("");
//            txtLastName.setText("");
//            txtNickname.setText("");
//            txtEmail.setText("");
//            txtWork.setText("");
//            txtDistrictWork.setText("");
//
//            home1.setText("");
//            home2.setText("4");
//            home3.setText("5");
//            home4.setText("1");
//            home5.setText("8");
//            home6.setText("8");
//            home7.setText("1");
//
//            other1.setText("4");
//            other2.setText("4");
//            other3.setText("5");
//            other4.setText("1");
//            other5.setText("1");
//            other6.setText("7");
//            other7.setText("2");
//            other8.setText("2");
//
//            mobile1.setText("9");
//            mobile2.setText("0");
//            mobile3.setText("9");
//            mobile4.setText("1");
//            mobile5.setText("8");
//            mobile6.setText("7");
//            mobile7.setText("8");
//            mobile8.setText("9");
//            mobile9.setText("3");
//            txtHomeID.setText("4138");
//            txtMoo.setText("5");
//            txtBuilding.setText("MaePraFatima");
//            txtSoi.setText("Soi");
//            txtRoad.setText("Tanoon");
        }
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

        btn_menu = (ImageButton) findViewById(R.id.btnMenu);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btn_back = (ImageButton) findViewById(R.id.btnBack);
        btnAddMobile = (Button) findViewById(R.id.btnAddMobile);

        txtPrefix = (EditText) findViewById(R.id.txt_Prefix);
        txtFirstName = (EditText) findViewById(R.id.txt_FirstName);
        txtLastName = (EditText) findViewById(R.id.txt_LastName);
        txtNickname = (EditText) findViewById(R.id.txtNickname);
        datePicker = (TextView) findViewById(R.id.datePicker);
        txtEmail = (EditText) findViewById(R.id.txt_Email);
        txtWork = (EditText) findViewById(R.id.txtWork);
        txtDistrictWork = (EditText) findViewById(R.id.txtDistrictWork);
        ddlOtherCountry = (Spinner) findViewById(R.id.ddlOtherCountry);
        ddlProvince = (Spinner) findViewById(R.id.ddlProvince);
        ddlDistrict = (Spinner) findViewById(R.id.ddlDistrict);
        ddlSubDistrict = (Spinner) findViewById(R.id.ddlSubDistrict);
        txtPostcode = (TextView) findViewById(R.id.txtPostcode);
        addMobiles = (LinearLayout) findViewById(R.id.addMobiles);

        txtHomeID = (EditText) findViewById(R.id.txtHomeID);
        txtMoo = (EditText) findViewById(R.id.txtMoo);
        txtBuilding = (EditText) findViewById(R.id.txtBuilding);
        txtFloor = (EditText) findViewById(R.id.txtFloor);
        txtRoom = (EditText) findViewById(R.id.txtRoom);
        txtSoi = (EditText) findViewById(R.id.txtSoi);
        txtRoad = (EditText) findViewById(R.id.txtRoad);
        ddlProvince.setPrompt(txtPromp);
        ddlDistrict.setPrompt(txtPromp);
        ddlSubDistrict.setPrompt(txtPromp);

        setObjectTelephone();

        btnAddMobile.setOnClickListener(this);
        btn_menu.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        addItemPrefix();

        setKeyListener();
        mobile_list = new ArrayList<String>();
        mobile_list.add("");
        mobile_list.add("");
        mobile_list.add("");
        mobile_list.add("");
        setFont();

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
        txtFirstName.setTypeface(delegate.font_type);
        txtLastName.setTypeface(delegate.font_type);
        txtNickname.setTypeface(delegate.font_type);
        datePicker.setTypeface(delegate.font_type);
        txtEmail.setTypeface(delegate.font_type);
        txtWork.setTypeface(delegate.font_type);
        txtDistrictWork.setTypeface(delegate.font_type);
        txtPostcode.setTypeface(delegate.font_type);

        txtHomeID.setTypeface(delegate.font_type);
        txtMoo.setTypeface(delegate.font_type);
        txtBuilding.setTypeface(delegate.font_type);
        txtFloor.setTypeface(delegate.font_type);
        txtRoom.setTypeface(delegate.font_type);
        txtSoi.setTypeface(delegate.font_type);
        txtRoad.setTypeface(delegate.font_type);

        txtFirstName.setTextSize(25);
        txtLastName.setTextSize(25);
        txtNickname.setTextSize(25);
        datePicker.setTextSize(25);
        txtEmail.setTextSize(25);
        txtWork.setTextSize(25);
        txtDistrictWork.setTextSize(25);
        txtPostcode.setTextSize(25);

        txtHomeID.setTextSize(25);
        txtMoo.setTextSize(25);
        txtBuilding.setTextSize(25);
        txtFloor.setTextSize(25);
        txtRoom.setTextSize(25);
        txtSoi.setTextSize(25);
        txtRoad.setTextSize(25);

        lblPrefix = (TextView) findViewById(R.id.lblPrefix);
        lblPrefix.setTextSize(25);
        lblPrefix.setTypeface(delegate.font_type);

        lbl_add_customer_name = (TextView) findViewById(R.id.lbl_add_customer_name);
        lbl_add_customer_name.setTextSize(25);
        lbl_add_customer_name.setTypeface(delegate.font_type);

        lbl_add_customer_surname = (TextView) findViewById(R.id.lbl_add_customer_surname);
        lbl_add_customer_surname.setTextSize(25);
        lbl_add_customer_surname.setTypeface(delegate.font_type);

        lblNickname = (TextView) findViewById(R.id.lblNickname);
        lblNickname.setTextSize(25);
        lblNickname.setTypeface(delegate.font_type);

        lbl_add_customer_birthday = (TextView)findViewById(R.id.lbl_add_customer_birthday);
        lbl_add_customer_birthday.setTextSize(25);
        lbl_add_customer_birthday.setTypeface(delegate.font_type);

        lblEmail = (TextView) findViewById(R.id.lblEmail);
        lblEmail.setTextSize(25);
        lblEmail.setTypeface(delegate.font_type);

        lbl_add_customer_tel_home_BKK_line1 = (TextView)findViewById(R.id.lbl_add_customer_tel_home_BKK_line1);
        lbl_add_customer_tel_home_BKK_line1.setTextSize(20);
        lbl_add_customer_tel_home_BKK_line1.setTypeface(delegate.font_type);

        lbl_add_customer_tel_home_line2 = (TextView) findViewById(R.id.lbl_add_customer_tel_home_line2);
        lbl_add_customer_tel_home_line2.setTextSize(15);
        lbl_add_customer_tel_home_line2.setTypeface(delegate.font_type);

        lbl_digit_tel_home_BKK1 = (TextView) findViewById(R.id.lbl_digit_tel_home_BKK1);
        lbl_digit_tel_home_BKK1.setTextSize(25);
        lbl_digit_tel_home_BKK1.setTypeface(delegate.font_type);

        lbl_digit_tel_home_BKK2 = (TextView) findViewById(R.id.lbl_digit_tel_home_BKK2);
        lbl_digit_tel_home_BKK2.setTextSize(25);
        lbl_digit_tel_home_BKK2.setTypeface(delegate.font_type);

        lbl_add_customer_tel_home_other_line1 = (TextView) findViewById(R.id.lbl_add_customer_tel_home_other_line1);
        lbl_add_customer_tel_home_other_line1.setTextSize(20);
        lbl_add_customer_tel_home_other_line1.setTypeface(delegate.font_type);

        lbl_add_customer_tel_home_other_line2 = (TextView) findViewById(R.id.lbl_add_customer_tel_home_other_line2);
        lbl_add_customer_tel_home_other_line2.setTextSize(15);
        lbl_add_customer_tel_home_other_line2.setTypeface(delegate.font_type);

        lbl_digit_tel_home_other1 = (TextView) findViewById(R.id.lbl_digit_tel_home_other1);
        lbl_digit_tel_home_other1.setTextSize(25);
        lbl_digit_tel_home_other1.setTypeface(delegate.font_type);

        lbl_add_customer_mobile = (TextView) findViewById(R.id.lbl_add_customer_mobile);
        lbl_add_customer_mobile.setTextSize(20);
        lbl_add_customer_mobile.setTypeface(delegate.font_type);

        btnAddMobile = (Button) findViewById(R.id.btnAddMobile);
        btnAddMobile.setTextSize(15);
        btnAddMobile.setTypeface(delegate.font_type);

        lbl_digit_mobile1 = (TextView) findViewById(R.id.lbl_digit_mobile1);
        lbl_digit_mobile1.setTextSize(25);
        lbl_digit_mobile1.setTypeface(delegate.font_type);

        lblWork = (TextView) findViewById(R.id.lblWork);
        lblWork.setTextSize(25);
        lblWork.setTypeface(delegate.font_type);

        lbl_add_customer_district_work = (TextView) findViewById(R.id.lbl_add_customer_district_work);
        lbl_add_customer_district_work.setTextSize(25);
        lbl_add_customer_district_work.setTypeface(delegate.font_type);

        lblCountry = (TextView) findViewById(R.id.lblCountry);
        lblCountry.setTextSize(25);
        lblCountry.setTypeface(delegate.font_type);

        lblHomeID = (TextView) findViewById(R.id.lblHomeID);
        lblHomeID.setTextSize(25);
        lblHomeID.setTypeface(delegate.font_type);

        lbl_add_customer_moo = (TextView) findViewById(R.id.lbl_add_customer_moo);
        lbl_add_customer_moo.setTextSize(25);
        lbl_add_customer_moo.setTypeface(delegate.font_type);

        lbl_add_customer_building = (TextView) findViewById(R.id.lbl_add_customer_building);
        lbl_add_customer_building.setTextSize(25);
        lbl_add_customer_building.setTypeface(delegate.font_type);

        lbl_add_customer_floor = (TextView) findViewById(R.id.lbl_add_customer_floor);
        lbl_add_customer_floor.setTextSize(25);
        lbl_add_customer_floor.setTypeface(delegate.font_type);

        lbl_add_customer_room = (TextView) findViewById(R.id.lbl_add_customer_room);
        lbl_add_customer_room.setTextSize(25);
        lbl_add_customer_room.setTypeface(delegate.font_type);

        lbl_add_customer_soi = (TextView) findViewById(R.id.lbl_add_customer_soi);
        lbl_add_customer_soi.setTextSize(25);
        lbl_add_customer_soi.setTypeface(delegate.font_type);

        lbl_add_customer_road = (TextView) findViewById(R.id.lbl_add_customer_road);
        lbl_add_customer_road.setTextSize(25);
        lbl_add_customer_road.setTypeface(delegate.font_type);

        lblProvince = (TextView) findViewById(R.id.lblProvince);
        lblProvince.setTextSize(25);
        lblProvince.setTypeface(delegate.font_type);

        lbl_add_customer_district = (TextView) findViewById(R.id.lbl_add_customer_district);
        lbl_add_customer_district.setTextSize(25);
        lbl_add_customer_district.setTypeface(delegate.font_type);

        lbl_add_customer_sub_district = (TextView) findViewById(R.id.lbl_add_customer_sub_district);
        lbl_add_customer_sub_district.setTextSize(25);
        lbl_add_customer_sub_district.setTypeface(delegate.font_type);

        lblPostcode = (TextView) findViewById(R.id.lblPostcode);
        lblPostcode.setTextSize(25);
        lblPostcode.setTypeface(delegate.font_type);


        //koy

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

                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtNickname.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        txtEmail.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    txtEmail.clearFocus();
                    home1.requestFocus();
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
                    other1.requestFocus();
                    return true;
                }
                return false;
            }
        });

        other1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    other1.clearFocus();
                    other2.requestFocus();
                    return true;
                }
                return false;
            }
        });

        other2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    other2.clearFocus();
                    other3.requestFocus();
                    return true;
                }
                return false;
            }
        });

        other3.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    other3.clearFocus();
                    other4.requestFocus();
                    return true;
                }
                return false;
            }
        });

        other4.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    other4.clearFocus();
                    other5.requestFocus();
                    return true;
                }
                return false;
            }
        });
        other5.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    other5.clearFocus();
                    other6.requestFocus();
                    return true;
                }
                return false;
            }
        });

        other6.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    other6.clearFocus();
                    other7.requestFocus();
                    return true;
                }
                return false;
            }
        });

        other7.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    other7.clearFocus();
                    other8.requestFocus();
                    return true;
                }
                return false;
            }
        });

        other8.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    other8.clearFocus();
                    mobile1.requestFocus();
                    return true;
                }
                return false;
            }
        });

        mobile1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                if (event.getAction() == KeyEvent.ACTION_UP){
                    mobile1.clearFocus();
                    mobile2.requestFocus();
                    return true;
                }
                return false;
            }
        });

        mobile2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
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
                    mobile9.clearFocus();
                    txtWork.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txtWork.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    txtWork.clearFocus();
                    txtDistrictWork.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txtDistrictWork.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtDistrictWork.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        txtHomeID.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    txtHomeID.clearFocus();
                    txtMoo.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtMoo.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    txtMoo.clearFocus();
                    txtBuilding.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txtBuilding.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    txtBuilding.clearFocus();
                    txtFloor.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txtFloor.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    txtFloor.clearFocus();
                    txtRoom.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtRoom.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    txtRoom.clearFocus();
                    txtSoi.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtSoi.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    txtSoi.clearFocus();
                    txtRoad.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtRoad.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtRoad.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    private void setObjectTelephone() {

        home1 = (EditText) findViewById(R.id.txt_digit_tel_home_BKK1);
        home2 = (EditText) findViewById(R.id.txt_digit_tel_home_BKK2);
        home3 = (EditText) findViewById(R.id.txt_digit_tel_home_BKK3);
        home4 = (EditText) findViewById(R.id.txt_digit_tel_home_BKK4);
        home5 = (EditText) findViewById(R.id.txt_digit_tel_home_BKK5);
        home6 = (EditText) findViewById(R.id.txt_digit_tel_home_BKK6);
        home7 = (EditText) findViewById(R.id.txt_digit_tel_home_BKK7);

        other1 = (EditText) findViewById(R.id.txt_digit_tel_home_other1);
        other2 = (EditText) findViewById(R.id.txt_digit_tel_home_other2);
        other3 = (EditText) findViewById(R.id.txt_digit_tel_home_other3);
        other4 = (EditText) findViewById(R.id.txt_digit_tel_home_other4);
        other5 = (EditText) findViewById(R.id.txt_digit_tel_home_other5);
        other6 = (EditText) findViewById(R.id.txt_digit_tel_home_other6);
        other7 = (EditText) findViewById(R.id.txt_digit_tel_home_other7);
        other8 = (EditText) findViewById(R.id.txt_digit_tel_home_other8);

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

    private void addItemPrefix() {

        ArrayList<String> list;
        ArrayAdapter<String> dataAdapter;

        datePicker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();

                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(AddCustomerActivity.this, new OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        mDay = selectedday;
                        mMonth = selectedmonth + 1;
                        mYear = selectedyear;

                        datePicker.setText(mYear + "-" + mMonth + "-" + mDay);
//                        datePicker.setText(mDay + "-" +   mMonth  + "-" +  mYear);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        list = new ArrayList<String>();
        list.add("ไทย");
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlOtherCountry.setAdapter(dataAdapter);

        final ArrayList<ValTextData> province = delegate.service.getProvinces();
        provinceAdapter _provinceAdapter = new provinceAdapter(this, R.layout.dropdownlist, province);

        ddlProvince.setSelection(0);
        ddlProvince.setAdapter(_provinceAdapter);

        ddlProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.e("province", province.get(arg2).toString());
                setddlDistrict(province.get(arg2).getValue());
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void setddlDistrict(String provinceID) {
        final ArrayList<ValTextData> district = delegate.service.getDistrictByProvince(provinceID);
        provinceAdapter _provinceAdapter = new provinceAdapter(this, R.layout.dropdownlist, district);
        ddlDistrict.setAdapter(_provinceAdapter);
        ddlDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                setddlSubDistrict(district.get(arg2).getValue());
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if (status == 1) {
            for (int i = 0; i < delegate.service.getDistrictByProvince(provinceID).size(); i++) {
                Log.e(delegate.service.getDistrictByProvince(provinceID).get(i).getText(), "my : " +customer_selected.getAddress().getDistrict());
                if (delegate.service.getDistrictByProvince(provinceID).get(i).getText().equals(customer_selected.getAddress().getDistrict())) {
                    ddlDistrict.setSelection(i);
                    status++;
                    break;
                }
            }
        }
    }

    public void setddlSubDistrict(String districtID) {
        final ArrayList<ValTextData> subDistrict = delegate.service.getSubDistrictByDistrict(districtID);
        provinceAdapter _provinceAdapter = new provinceAdapter(this, R.layout.dropdownlist, subDistrict);
        ddlSubDistrict.setAdapter(_provinceAdapter);
        ddlSubDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                txtPostcode.setText(subDistrict.get(arg2).getText2());
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        if (status == 2) {
            for (int i = 0; i < delegate.service.getSubDistrictByDistrict(districtID).size(); i++) {
                if (delegate.service.getSubDistrictByDistrict(districtID).get(i).getText().equals(customer_selected.getAddress().getSubdistrict())) {
                    ddlSubDistrict.setSelection(i);
                    status++;
                    break;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_customer, menu);
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

    private void sendData() {
        AddressData work = new AddressData("", "", "", "", "", "", "", "", "", "", "", "");
        AddressData home = new AddressData("", "", "", "", "", "", "", "", "", "", "", "");


        work.setVillage(txtWork.getText().toString());
        work.setDistrict(txtDistrictWork.getText().toString());

        new_customer = new ContactData("", "", "", "", "", "", null, null,null);

        if (mobile1.getText().toString().length() > 0 && mobile2.getText().toString().length() > 0
                && mobile3.getText().toString().length() > 0 && mobile4.getText().toString().length() > 0
                && mobile5.getText().toString().length() > 0 && mobile6.getText().toString().length() > 0
                && mobile7.getText().toString().length() > 0 && mobile8.getText().toString().length() > 0
                && mobile9.getText().toString().length() > 0) {
            String txtMobileinPage = "0" + mobile1.getText().toString() + mobile2.getText().toString() + mobile3.getText().toString()
                    + mobile4.getText().toString() + mobile5.getText().toString() + mobile6.getText().toString() +
                    mobile7.getText().toString() + mobile8.getText().toString() + mobile9.getText().toString();
            mobile_list.set(0, txtMobileinPage);

        } else {
            //validate invalid mobile
        }
        String home_phone = "02" + home1.getText().toString() + home2.getText().toString() + home3.getText().toString()
                + home4.getText().toString() + home5.getText().toString() + home6.getText().toString() +
                home7.getText().toString();
        String ext_home_phone = "0" + other1.getText().toString() + other2.getText().toString() + other3.getText().toString()
                + other4.getText().toString() + other5.getText().toString() + other6.getText().toString() +
                other7.getText().toString() + other8.getText().toString();

        //       ไม่มี txtFloor, txtRoom

        home.setHouseId(txtHomeID.getText().toString());
        home.setMoo(txtMoo.getText().toString());
        home.setVillage(txtBuilding.getText().toString());
        home.setSoi(txtSoi.getText().toString());
        home.setRoad(txtRoad.getText().toString());

        //ddl
        if(!ddlProvince.getSelectedItem().toString().equals(txtPromp)){
            home.setProvince(ddlProvince.getSelectedItem().toString());
        } else {
            home.setProvince("");
        }

        if(!ddlDistrict.getSelectedItem().toString().equals(txtPromp)){
            home.setDistrict(ddlDistrict.getSelectedItem().toString());
        } else {
            home.setDistrict("");
        }

        if(!ddlSubDistrict.getSelectedItem().toString().equals(txtPromp)){
            home.setSubdistrict(ddlSubDistrict.getSelectedItem().toString());
        } else {
            home.setSubdistrict("");
        }

        home.setPostalcode(txtPostcode.getText().toString());
        home.setCountry(ddlOtherCountry.getSelectedItem().toString());

        if(!home_phone.equals("02")){
            home.setTel(home_phone);
        } else {
            home.setTel("");
        }

        if(!home_phone.equals("0")){
            home.setTelExt(ext_home_phone);
        } else {
            home.setTelExt("");
        }

        new_customer.setPrefix(txtPrefix.getText().toString());
        if (txtFirstName.getText().toString().length() > 0) {
            new_customer.setFname(txtFirstName.getText().toString());
        } else {
            //validate invalid txtFirstName
            new_customer.setFname("");
        }
        if (txtLastName.getText().toString().length() > 0) {
            new_customer.setLname(txtLastName.getText().toString());
        } else {
            //validate invalid txtFirstName
            new_customer.setLname("");
        }
        Log.e(TAG,mobile_list.toString());
        new_customer.setMobiles(mobile_list);
        new_customer.setAddressWork(work);
        new_customer.setAddress(home);
        new_customer.setBirthdate(datePicker.getText().toString());
        new_customer.setEmail(txtEmail.getText().toString());
        new_customer.setNickname(txtNickname.getText().toString());
        Log.e("customer ", delegate.service.globals.getContactId());
        if (delegate.service.globals.getContactId() != "-1") {
            new_customer.setContactId(delegate.service.globals.getContactId());
        }

        Log.e("cmd", new_customer.toString());

    }
    public boolean validate(){
        boolean status = true;

        if(txtFirstName.getText().toString().length() ==0){
            lbl_add_customer_name.setText(Html.fromHtml("ชื่อ <font color =#FF0000>*</font>"));
            status =  false;
        }

        if(txtLastName.getText().toString().length() ==0){
            lbl_add_customer_surname.setText(Html.fromHtml("นามสกุล <font color =#FF0000>*</font>"));
            status = false;
        }

        if(txtEmail.getText().toString().length() ==0){
            lblEmail.setText(Html.fromHtml("อีเมล <font color =#FF0000>*</font>"));
            status = false;
        }

        return status;
    }

    public void onClick(View v) {

        if (v.getId() == R.id.btnSend) {
            if(validate()){
                sendData();

                final ProgressDialog progress = new ProgressDialog(this);
                progress.setTitle("Please wait");
                progress.setMessage("Saving customer information.");
                progress.show();

                final Handler uiHandler = new Handler();
                final  Runnable onUi = new Runnable() {
                    @Override
                    public void run() {
                        // this will run on the main UI thread
                        progress.dismiss();
                        Intent i = new Intent(ctx, CustomerInfomationActivity.class);
                        i.putExtra("customerIndex", delegate.service.globals.getContactId());
                        startActivityForResult(i, 0);
                    }
                };
                Runnable background = new Runnable() {
                    @Override
                    public void run() {
                        // This is the delay
                        if (delegate.service.globals.getContactId() == "-1") {
                            delegate.service.saveContact(new_customer);
                        }else{
                            delegate.service.updateContact(new_customer);
                        }
                        try {
                            Thread.sleep(2000);
                        }catch (Exception e){

                        }
                        uiHandler.post( onUi );
                    }
                };
                new Thread( background ).start();
            }
        } else if (v.getId() == R.id.btnBack) {
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
        } else if (v.getId() == R.id.btnAddMobile) {

            if(popupAddMobile.isShowing()){
                popupAddMobile.dismiss();
            } else {
                showPopupAddMobile(this);
            }
        } else if(v.getId() == R.id.popup_mobile) {

            if(popupAddMobile.isShowing()){
                popupAddMobile.dismiss();
            }
        } else {
            if(Integer.parseInt(v.getTag().toString()) >100){
                updatePopupAddMobile(this, Integer.parseInt(v.getTag().toString())-100);

            }
        }
    }

    public void onBackPressed() {
        this.setResult(3);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2 || resultCode == 0 || resultCode == 1) {
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
    public void showPopupAddMobile(final Activity context) {
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
        final TextView lbl0 = (TextView) layout.findViewById(R.id.lbl_digit_mobile1);

        lbl.setTypeface(delegate.font_type);
        lbl.setTextSize(25);
        lbl0.setTypeface(delegate.font_type);
        lbl0.setTextSize(25);

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
        btn_delete.setVisibility(View.GONE);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m01.getText().length()!=0 && m02.getText().length()!=0 && m03.getText().length()!=0 &&
                        m04.getText().length()!=0 && m05.getText().length()!=0 && m06.getText().length()!=0 &&
                        m07.getText().length()!=0 && m08.getText().length()!=0 && m09.getText().length()!=0){
                    addMobileinPage( m01.getText().toString() + m02.getText().toString() +
                            m03.getText().toString() + m04.getText().toString() +
                            m05.getText().toString() + m06.getText().toString() +
                            m07.getText().toString() + m08.getText().toString() + m09.getText().toString());
                    popupAddMobile.dismiss();
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(m09.getWindowToken(), 0);
                    lbl_error.setVisibility(View.INVISIBLE);
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
                            m07.getText().length()!=0 && m08.getText().length()!=0 && m09.getText().length()!=0){
                        addMobileinPage( m01.getText().toString() + m02.getText().toString() +
                                m03.getText().toString() + m04.getText().toString() +
                                m05.getText().toString() + m06.getText().toString() +
                                m07.getText().toString() + m08.getText().toString() + m09.getText().toString());
                        popupAddMobile.dismiss();
                        InputMethodManager imm = (InputMethodManager)getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(m09.getWindowToken(), 0);
                        lbl_error.setVisibility(View.INVISIBLE);
                    } else {
                        lbl_error.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });
    }
    public void addMobileinPage(String mobile_number){

        boolean statusAdd =false;
        int index=0;

        for (int i =1;i<mobile_list.size();i++){
            if(mobile_list.get(i).length()==0){
                statusAdd = true;
                index = i;
                break;
            }
        }
        if(statusAdd){
            mobile_list.set(index,"0"+mobile_number);
        }
        statusAdd = true;

        for (int i =1;i<mobile_list.size();i++){
            if(mobile_list.get(i).length()==0){
                statusAdd = false;
            }
        }
        if(statusAdd){
            btnAddMobile.setVisibility(View.GONE);
        }
        updateMobile2();

    }
    public void updateMobile2(){
        String txt_mobile= "";
        if(mobile_list.size()>1){
            addMobiles.removeAllViews();
            for (int i =1;i<mobile_list.size();i++){
                txt_mobile = mobile_list.get(i);

                TextView newMobile = new TextView(this);
                newMobile.setOnClickListener(this);
                int mobileTAG =100 +i;
                newMobile.setTag(mobileTAG);
                newMobile.setText(txt_mobile);

                newMobile.setTextSize(20);
                newMobile.setPadding(delegate.pxToDp(10),delegate.pxToDp(10),delegate.pxToDp(10),delegate.pxToDp(10));

                addMobiles.addView(newMobile);
            }

        } else if (mobile_list.size()==1) {
//            updateMobile();
        }
    }

    public void updateMobile(){

        String txtmobile = mobile_list.get(0);
        txtmobile = txtmobile.replace("-", "");
        txtmobile = txtmobile.replace(" ", "");
        txtmobile = txtmobile.replace(",", "");

        switch (txtmobile.length()) {
            case 10:
                mobile9.setText(txtmobile.substring(9, 10));
            case 9:
                mobile8.setText(txtmobile.substring(8, 9));
            case 8:
                mobile7.setText(txtmobile.substring(7, 8));
            case 7:
                mobile6.setText(txtmobile.substring(6, 7));
            case 6:
                mobile5.setText(txtmobile.substring(5, 6));
            case 5:
                mobile4.setText(txtmobile.substring(4, 5));
            case 4:
                mobile3.setText(txtmobile.substring(3, 4));
            case 3:
                mobile2.setText(txtmobile.substring(2, 3));
            case 2:
                mobile1.setText(txtmobile.substring(1, 2));
        }
    }

    public void updatePopupAddMobile(final Activity context,final int index) {
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
        final TextView lbl0 = (TextView) layout.findViewById(R.id.lbl_digit_mobile1);

        lbl.setTypeface(delegate.font_type);
        lbl.setTextSize(25);
        lbl0.setTypeface(delegate.font_type);
        lbl0.setTextSize(25);

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
        btn_delete.setVisibility(View.VISIBLE);

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

                    addMobileinPage( m01.getText().toString() + m02.getText().toString() +
                            m03.getText().toString() + m04.getText().toString() +
                            m05.getText().toString() + m06.getText().toString() +
                            m07.getText().toString() + m08.getText().toString() + m09.getText().toString());
                    popupAddMobile.dismiss();
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(m09.getWindowToken(), 0);
                }
                return false;
            }
        });

        String txtmobile = mobile_list.get(index);
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
        }
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m01.getText().length()!=0 && m02.getText().length()!=0 && m03.getText().length()!=0 &&
                        m04.getText().length()!=0 && m05.getText().length()!=0 && m06.getText().length()!=0 &&
                        m07.getText().length()!=0 && m08.getText().length()!=0 && m09.getText().length()!=0){
                    addMobileinPage( m01.getText().toString() + m02.getText().toString() +
                            m03.getText().toString() + m04.getText().toString() +
                            m05.getText().toString() + m06.getText().toString() +
                            m07.getText().toString() + m08.getText().toString() + m09.getText().toString());
                    popupAddMobile.dismiss();
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(m09.getWindowToken(), 0);
                    lbl_error.setVisibility(View.INVISIBLE);
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
                updateMobile2();
                btnAddMobile.setVisibility(View.VISIBLE);
                popupAddMobile.dismiss();
            }
        });
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        final int proposedheight = View.MeasureSpec.getSize(heightMeasureSpec);
//        final int actualHeight = getHeight();
//
//        if (actualHeight > proposedheight){
//            // Keyboard is shown
//        } else {
//            // Keyboard is hidden
//        }
//
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        Log.e(TAG, newConfig.toString());
//        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {//something
//        } else {//something}
//        }
//    }

}
