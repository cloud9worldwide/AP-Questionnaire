package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud9worldwide.questionnaire.data.AddressData;
import com.cloud9worldwide.questionnaire.data.ContactData;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddCustomerENActivity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    ImageButton btnMenu, btnSend, btnBack, btnEN, btnTH;

    questionniare_delegate delegate;
    RelativeLayout footer;
    TextView project_name,question_title;
    RelativeLayout root_view;
    static PopupWindow popup, popupAddMobile;

    EditText txtHomeId, txtMoo, txtBuilding, txtFloor, txtRoom, txtSoi, txtRoad, txtProvince;
    EditText txtDistrict, txtSubDistrict,txtPostcode;
    EditText txtWork, txtWorkDistrict, txtWorkProvince, txtRoadWork;

    TextView lblHomeId, lblMoo, lblBuilding, lblFloor, lblRoom, lblSoi, lblRoad, lblProvince;
    TextView lblDistrict, lblSubDistrict, lblPostcode;
    TextView lblWork, lblWorkDistrict, txt_header, lblWorkProvince, lblWorkRoad;

    ContactData new_customer;
    int statusFin;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_en);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setObject();
    }

    private void getCustomerInfo(){
        AddressData home = new_customer.getAddress();
        AddressData work = new_customer.getAddressWork();

        if(!home.getHouseId().trim().equals("null")){
            txtHomeId.setText(home.getHouseId().toString());
        }
        if(!home.getMoo().trim().equals("null")){
            txtMoo.setText(home.getMoo().toString());
        }
        if(!home.getVillage().trim().equals("null")){
            txtBuilding.setText(home.getVillage().toString());
        }
        if(!home.getFloor().trim().equals("null")){
            txtFloor.setText(home.getFloor().toString());
        }
        if(!home.getRoom().trim().equals("null")){
            txtRoom.setText(home.getRoom().toString());
        }
        if(!home.getSoi().trim().equals("null")){
            txtSoi.setText(home.getSoi().toString());
        }
        if(!home.getRoad().trim().equals("null")){
            txtRoad.setText(home.getRoad().toString());
        }
        if(!(home.getProvince().toString().equals("Please select") && home.getProvince().toString().equals("กรุณาเลือก"))){
            txtProvince.setText(home.getProvince().toString());
        }

        if(!(home.getDistrict().toString().equals("Please select") && home.getDistrict().toString().equals("กรุณาเลือก"))){
            txtDistrict.setText(home.getDistrict().toString());
        }

        if(!(home.getSubdistrict().toString().equals("Please select") && home.getSubdistrict().toString().equals("กรุณาเลือก"))){
            txtSubDistrict.setText(home.getSubdistrict().toString());
        }

        if(!home.getPostalcode().trim().equals("null")){
            txtPostcode.setText(home.getPostalcode().toString());
        }
        if(!work.getVillage().trim().equals("null")){
            txtWork.setText(work.getVillage().toString());
        }
        if(!work.getDistrict().trim().equals("null")){
            txtWorkDistrict.setText(work.getDistrict().toString());
        }
        if(!work.getProvince().trim().equals("null")){
            txtWorkProvince.setText(work.getProvince().toString());
        }
        if(!work.getRoad().trim().equals("null")){
            txtRoadWork.setText(work.getRoad().toString());
        }
    }

    private void setObject(){
        delegate = (questionniare_delegate) getApplicationContext();
        ctx = this;
        statusFin=0;
        new_customer = delegate.customer_selected;
        footer = (RelativeLayout) findViewById(R.id.footer);
        project_name = (TextView) findViewById(R.id.project_name);
        project_name.setText(delegate.project.getName());
        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);

        question_title = (TextView) findViewById(R.id.question_title);
        question_title.setTextSize(25);
        question_title.setTypeface(delegate.font_type);
        question_title.setGravity(Gravity.CENTER);

        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_header.setTypeface(delegate.font_type);
        txt_header.setTextSize(35);

        root_view = (RelativeLayout) findViewById(R.id.root_view);
        root_view.setOnClickListener(this);
        popup = new PopupWindow(this);
        popupAddMobile = new PopupWindow(this);

        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        btnMenu.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        btnEN = (ImageButton) findViewById(R.id.btnEN);
        btnTH = (ImageButton) findViewById(R.id.btnTH);
        btnEN.setOnClickListener(this);
        btnTH.setOnClickListener(this);

        lblHomeId = (TextView) findViewById(R.id.lblHomeID);
        lblMoo = (TextView) findViewById(R.id.lbl_add_customer_moo);
        lblBuilding = (TextView) findViewById(R.id.lbl_add_customer_building);
        lblFloor = (TextView) findViewById(R.id.lbl_add_customer_floor);
        lblRoom = (TextView) findViewById(R.id.lbl_add_customer_room);
        lblSoi = (TextView) findViewById(R.id.lbl_add_customer_soi);
        lblProvince = (TextView) findViewById(R.id.lblProvince);
        lblRoad = (TextView) findViewById(R.id.lbl_add_customer_road);

        lblDistrict = (TextView) findViewById(R.id.lbl_add_customer_district);
        lblSubDistrict = (TextView) findViewById(R.id.lbl_add_customer_sub_district);
        lblPostcode = (TextView) findViewById(R.id.lblPostcode);

        lblWork = (TextView) findViewById(R.id.lblWork);
        lblWorkDistrict = (TextView) findViewById(R.id.lbl_add_customer_district_work);
        lblWorkProvince = (TextView) findViewById(R.id.lbl_add_customer_province_work);
        lblWorkRoad = (TextView) findViewById(R.id.lbl_add_customer_road_work);

        txtHomeId = (EditText) findViewById(R.id.txtHomeID);
        txtMoo = (EditText) findViewById(R.id.txtMoo);
        txtBuilding = (EditText) findViewById(R.id.txtBuilding);
        txtFloor =(EditText) findViewById(R.id.txtFloor);
        txtRoom = (EditText) findViewById(R.id.txtRoom);
        txtSoi = (EditText) findViewById(R.id.txtSoi);
        txtRoad = (EditText) findViewById(R.id.txtRoad);
        txtProvince = (EditText) findViewById(R.id.txtProvince);
        txtDistrict = (EditText) findViewById(R.id.txtDistrict);
        txtSubDistrict = (EditText) findViewById(R.id.txtSubDistrict);
        txtPostcode = (EditText) findViewById(R.id.txtPostcode);
        txtWork = (EditText) findViewById(R.id.txtWork);
        txtWorkDistrict = (EditText) findViewById(R.id.txtDistrictWork);
        txtWorkProvince = (EditText) findViewById(R.id.txtProvinceWork);
        txtRoadWork = (EditText) findViewById(R.id.txtRoadWork);

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
            changeLanguege();
        }
    }

    private void changeLanguege(){

        txt_header.setText(R.string.please_fill_completed);
        question_title.setText(R.string.title_activity_customer_look_info);

        project_name.setText(delegate.project.getName());
        if(delegate.service.getLg().equals("en")){
            btnEN.setImageResource(R.drawable.btn_en_);
            btnTH.setImageResource(R.drawable.btn_th);
            lblBuilding.setTextSize(20);
            lblRoom.setTextSize(17);
        } else {
            btnEN.setImageResource(R.drawable.btn_en);
            btnTH.setImageResource(R.drawable.btn_th_);
            lblBuilding.setTextSize(25);
            lblRoom.setTextSize(25);
        }

        lblHomeId.setText(R.string.add_customer_homeid);
        lblMoo.setText(R.string.add_customer_moo);
        lblBuilding.setText(R.string.add_customer_building);
        lblFloor.setText(R.string.add_customer_floor);
        lblRoom.setText(R.string.add_customer_room);
        lblSoi.setText(R.string.add_customer_soi);
        lblProvince.setText(R.string.add_customer_province);
        lblRoad.setText(R.string.add_customer_road);

        lblDistrict.setText(R.string.add_customer_district);
        lblSubDistrict.setText(R.string.add_customer_sub_district);
        lblPostcode.setText(R.string.add_customer_postcode);

        lblWork.setText(R.string.add_customer_work);
        lblWorkDistrict.setText(R.string.add_customer_district);
        lblWorkRoad.setText(R.string.add_customer_road);

        txtHomeId.setHint(R.string.add_customer_homeid);
        txtMoo.setHint(R.string.add_customer_moo);
        txtBuilding.setHint(R.string.add_customer_building);
        txtFloor.setHint(R.string.add_customer_floor);
        txtRoom.setHint(R.string.add_customer_room);
        txtSoi.setHint(R.string.add_customer_soi);
        txtRoad.setHint(R.string.add_customer_road);

        txtPostcode.setText(R.string.add_customer_postcode);
        lblWorkProvince.setText(R.string.add_customer_province);

        txtWork = (EditText) findViewById(R.id.txtWork);
        txtWorkDistrict = (EditText) findViewById(R.id.txtDistrictWork);
        txtWorkProvince = (EditText) findViewById(R.id.txtProvinceWork);
        getCustomerInfo();
    }

    private void setFont(){
        lblHomeId.setTextSize(25);
        lblHomeId.setTypeface(delegate.font_type);
        lblMoo.setTextSize(25);
        lblMoo.setTypeface(delegate.font_type);

        lblBuilding.setTypeface(delegate.font_type);

        lblFloor.setTextSize(25);
        lblFloor.setTypeface(delegate.font_type);

        lblRoom.setTypeface(delegate.font_type);

        lblSoi.setTextSize(25);
        lblSoi.setTypeface(delegate.font_type);
        lblProvince.setTextSize(25);
        lblProvince.setTypeface(delegate.font_type);
        lblRoad.setTextSize(25);
        lblRoad.setTypeface(delegate.font_type);
        lblDistrict.setTextSize(25);
        lblDistrict.setTypeface(delegate.font_type);
        lblSubDistrict.setTextSize(25);
        lblSubDistrict.setTypeface(delegate.font_type);
        lblPostcode.setTextSize(25);
        lblPostcode.setTypeface(delegate.font_type);
        lblWork.setTextSize(25);
        lblWork.setTypeface(delegate.font_type);
        lblWorkDistrict.setTextSize(25);
        lblWorkDistrict.setTypeface(delegate.font_type);
        lblWorkProvince.setTextSize(25);
        lblWorkProvince.setTypeface(delegate.font_type);
        lblWorkRoad.setTextSize(25);
        lblWorkRoad.setTypeface(delegate.font_type);

        txtHomeId.setTextSize(25);
        txtHomeId.setTypeface(delegate.font_type);
        txtMoo.setTextSize(25);
        txtMoo.setTypeface(delegate.font_type);
        txtBuilding.setTextSize(25);
        txtBuilding.setTypeface(delegate.font_type);
        txtFloor.setTextSize(25);
        txtFloor.setTypeface(delegate.font_type);
        txtRoom.setTextSize(25);
        txtRoom.setTypeface(delegate.font_type);
        txtSoi.setTextSize(25);
        txtSoi.setTypeface(delegate.font_type);
        txtRoad.setTextSize(25);
        txtRoad.setTypeface(delegate.font_type);
        txtProvince.setTextSize(25);
        txtProvince.setTypeface(delegate.font_type);
        txtDistrict.setTextSize(25);
        txtDistrict.setTypeface(delegate.font_type);
        txtSubDistrict.setTextSize(25);
        txtSubDistrict.setTypeface(delegate.font_type);
        txtWork.setTextSize(25);
        txtWork.setTypeface(delegate.font_type);
        txtWorkDistrict.setTextSize(25);
        txtWorkDistrict.setTypeface(delegate.font_type);
        txtWorkProvince.setTextSize(25);
        txtWorkProvince.setTypeface(delegate.font_type);
        txtRoadWork.setTextSize(25);
        txtRoadWork.setTypeface(delegate.font_type);
        txtPostcode.setTextSize(25);
        txtPostcode.setTypeface(delegate.font_type);

    }

    private void setKeyListener() {
        txtHomeId.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtHomeId.clearFocus();
                    txtMoo.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtMoo.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtMoo.clearFocus();
                    txtBuilding.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtBuilding.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtBuilding.clearFocus();
                    txtFloor.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtFloor.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtFloor.clearFocus();
                    txtRoom.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtRoom.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtRoom.clearFocus();
                    txtSoi.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtSoi.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtSoi.clearFocus();
                    txtRoad.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtRoad.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtRoad.clearFocus();
                    txtProvince.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtProvince.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtProvince.clearFocus();
                    txtDistrict.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtDistrict.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtDistrict.clearFocus();
                    txtSubDistrict.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtSubDistrict.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtSubDistrict.clearFocus();
                    txtPostcode.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtPostcode.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtPostcode.clearFocus();
                    txtWork.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtWork.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtWork.clearFocus();
                    txtRoadWork.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtRoadWork.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtRoadWork.clearFocus();
                    txtWorkDistrict.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtWorkDistrict.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    txtWorkDistrict.clearFocus();
                    txtWorkProvince.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtWorkProvince.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtWorkProvince.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_customer_en, menu);
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

    private void saveToNewCustomer(){
        AddressData home = new_customer.getAddress();
        AddressData work = new_customer.getAddressWork();

        if(txtHomeId.getText().toString().length()>0 && !txtHomeId.getText().equals("null")){
            home.setHouseId(txtHomeId.getText().toString());
        }else{
            home.setHouseId("");
        }
        if(txtMoo.getText().toString().length()>0 && !txtMoo.getText().equals("null")){
            home.setMoo(txtMoo.getText().toString());
        }else{
            home.setMoo("");
        }
        if(txtBuilding.getText().toString().length()>0 && !txtBuilding.getText().equals("null")){
            home.setVillage(txtBuilding.getText().toString());
        }else{
            home.setVillage("");
        }
        if(txtFloor.getText().toString().length()>0 && !txtFloor.getText().equals("null")){
            home.setFloor(txtFloor.getText().toString());
        }else{
            home.setFloor("");
        }
        if(txtRoom.getText().toString().length()>0 && !txtRoom.getText().equals("null")){
            home.setRoom(txtRoom.getText().toString());
        }else{
            home.setRoom("");
        }
        if(txtSoi.getText().toString().length()>0 && !txtSoi.getText().equals("null")){
            home.setSoi(txtSoi.getText().toString());
        }else{
            home.setSoi("");
        }
        if(txtRoad.getText().toString().length()>0 && !txtRoad.getText().equals("null")){
            home.setRoad(txtRoad.getText().toString());
        }else{
            home.setRoad("");
        }
        if(txtProvince.getText().toString().length()>0 && !txtProvince.getText().equals("null")){
            home.setProvince(txtProvince.getText().toString());
        }else{
            home.setProvince("");
        }

        if(txtDistrict.getText().toString().length()>0 && !txtDistrict.getText().equals("null")){
            home.setDistrict(txtDistrict.getText().toString());
        }else{
            home.setDistrict("");
        }
        if(txtSubDistrict.getText().toString().length()>0 && !txtSubDistrict.getText().equals("null")){
            home.setSubdistrict(txtSubDistrict.getText().toString());
        }else{
            home.setSubdistrict("");
        }
        if(txtPostcode.getText().toString().length()>0 && !txtPostcode.getText().equals("null")){
            home.setPostalcode(txtPostcode.getText().toString());
        }else{
            home.setPostalcode("");
        }


        if(txtWork.getText().toString().length()>0 && !txtWork.getText().equals("null")){
            work.setVillage(txtWork.getText().toString());
        }else{
            work.setVillage("");
        }
        if(txtRoadWork.getText().toString().length() > 0 && !txtRoadWork.getText().equals("null")){
            work.setRoad(txtRoadWork.getText().toString());
        } else {
            work.setRoad("");
        }

        if(txtWorkDistrict.getText().toString().length()>0 && !txtWorkDistrict.getText().equals("null")){
            work.setDistrict(txtWorkDistrict.getText().toString());
        }else{
            work.setDistrict("");
        }
        if(txtWorkProvince.getText().toString().length()>0 && !txtWorkProvince.getText().equals("null")){
            work.setProvince(txtWorkProvince.getText().toString());
        }else{
            work.setProvince("");
        }
    }

    private void packData(){
        saveToNewCustomer();

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
                if(new_customer.getContactId().equals("-1")){
                    Intent i = new Intent(ctx, CustomerInfomationActivity.class);
                    i.putExtra("customerIndex", delegate.service.globals.getContactId());
                    startActivityForResult(i, 0);
                } else {
                    statusFin =1;
                    onBackPressed();
                }
            }
        };
        Runnable background = new Runnable() {
            @Override
            public void run() {
                // This is the delay
                if (delegate.service.globals.getContactId().equals("-1")) {
                    delegate.service.saveContact(new_customer, delegate.project.getId(),delegate.questionnaire_selected.getId());
                    delegate.opp = true;
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

    public void onClick(View v) {
        if (popup.isShowing()) {
            popup.dismiss();
        }

        if(v.getId() == R.id.btnSend){
            if(!delegate.service.isOnline() && !new_customer.getContactId().equals("-1")) {
                //offline mode
                AlertDialog alertDialog1 = new AlertDialog.Builder(
                        ctx).create();
                alertDialog1.setTitle(getString(R.string.alert_warning));
                alertDialog1.setMessage(getString(R.string.is_offine));
                alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                alertDialog1.show();
            } else {
                packData();
            }
        } else if(v.getId() == R.id.btnBack){
            if(!delegate.service.isOnline() && !new_customer.getContactId().equals("-1")) {
                //offline mode
                AlertDialog alertDialog1 = new AlertDialog.Builder(
                        ctx).create();
                alertDialog1.setTitle(getString(R.string.alert_warning));
                alertDialog1.setMessage(getString(R.string.is_offine));
                alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                alertDialog1.show();
            } else {
                onBackPressed();
            }
        } else if(v.getId() == R.id.btnMenu){
            showPopup(this);
        } else if(v.getId() == R.id.btnEN){
            if (popup.isShowing()) {
                popup.dismiss();
            } else {
                saveToNewCustomer();
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
                saveToNewCustomer();
                if (!delegate.service.getLg().equals("th")) {
                    delegate.service.setLg("th");
                    delegate.setLocale("th");
                    changeLanguege();
                }
            }
        }
    }

    public void onBackPressed() {
        if(!delegate.service.isOnline() && !new_customer.getContactId().equals("-1")) {
            this.setResult(5);
        } else if(statusFin==1){
            this.setResult(4);
        } else {
            this.setResult(3);
        }
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2 || resultCode == 0 || resultCode == 1 || resultCode == 5) {
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
