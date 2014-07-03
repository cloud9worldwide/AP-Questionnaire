package com.apthai.ap_questionaire.app;

import android.app.Activity;
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
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;


public class AddCustomerTHActivity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    ImageButton btnMenu, btnSend, btnBack;

    questionniare_delegate delegate;
    RelativeLayout footer;
    TextView project_name,question_title,txt_header;
    String txtPromp;
    RelativeLayout root_view;
    static PopupWindow popup, popupAddMobile;

    EditText txtHomeId, txtMoo, txtBuilding, txtFloor, txtRoom, txtSoi, txtRoad;
    EditText txtWork, txtWorkDistrict;
    Spinner ddlProvince, ddlDistrict, ddlSubDistrict;

    TextView lblHomeId, lblMoo, lblBuilding, lblFloor, lblRoom, lblSoi, lblRoad, lblProvince;
    TextView lblDistrict, lblSubDistrict, lblPostcode;
    TextView lblWork, lblWorkDistrict, txtPostcode;

    ContactData new_customer;
    private Context ctx;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_th);
        setObject();
    }

    private void setObject() {
        delegate = (questionniare_delegate) getApplicationContext();
        ctx = this;
        new_customer = delegate.customer_selected;
        status= 0;

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
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        btnMenu.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnBack.setOnClickListener(this);

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

        txtHomeId = (EditText) findViewById(R.id.txtHomeID);
        txtMoo = (EditText) findViewById(R.id.txtMoo);
        txtBuilding = (EditText) findViewById(R.id.txtBuilding);
        txtFloor =(EditText) findViewById(R.id.txtFloor);
        txtRoom = (EditText) findViewById(R.id.txtRoom);
        txtSoi = (EditText) findViewById(R.id.txtSoi);
        txtRoad = (EditText) findViewById(R.id.txtRoad);
        ddlProvince = (Spinner) findViewById(R.id.ddlProvince);
        ddlDistrict = (Spinner) findViewById(R.id.ddlDistrict);
        ddlSubDistrict = (Spinner) findViewById(R.id.ddlSubDistrict);
        txtPostcode = (TextView) findViewById(R.id.txtPostcode2);
        txtWork = (EditText) findViewById(R.id.txtWork);
        txtWorkDistrict = (EditText) findViewById(R.id.txtDistrictWork);
        setFont();
        setItemSpinner();
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

        getCustomerInfo();

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
                    txtWorkDistrict.requestFocus();
                    return true;
                }
                return false;
            }
        });
        txtWorkDistrict.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtWorkDistrict.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    private void getCustomerInfo(){

        if(delegate.customer_selected.getContactId().equals("")){

        } else {
            AddressData home = new_customer.getAddress();
            AddressData work = new_customer.getAddressWork();

            txtHomeId.setText(home.getHouseId().toString());
            txtMoo.setText(home.getMoo().toString());
            txtBuilding.setText(home.getVillage().toString());
            txtFloor.setText(home.getFloor().toString());
            txtRoom.setText(home.getRoom().toString());
            txtSoi.setText(home.getSoi().toString());
            txtRoad.setText(home.getRoad().toString());
            txtPostcode.setText(home.getPostalcode().toString());
            txtWork.setText(work.getVillage().toString());
            txtWorkDistrict.setText(work.getDistrict().toString());

            ArrayList<ValTextData> list;
            list= delegate.service.getProvinces();
            String indexSelect="999";

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getText().equals(home.getProvince().toString())) {
                    indexSelect = list.get(i).getValue().toString();
                    Log.e("list.get(i)",list.get(i).getValue());
                    ddlProvince.setSelection(i);

                    break;
                }
            }
            Log.e("indexSelect",indexSelect);
//            if(!indexSelect.equals("999")){
//
//                list= delegate.service.getDistrictByProvince(indexSelect);
//                Log.e("list",list.toString());
//
//                Log.e("server dis",new_customer.getAddress().getDistrict());
//                Log.e("getText",list.get(0).getText());
//                indexSelect="999";
//                for (int i = 0; i < list.size(); i++) {
//                    if (list.get(i).getText().equals(new_customer.getAddress().getDistrict())) {
//                        indexSelect = list.get(i).getValue();
//                        ddlDistrict.setSelection(i);
//                        setddlSubDistrict(indexSelect);
//                        break;
//                    }
//                }
//
//                if(!indexSelect.equals("999")){
//                    list = delegate.service.getSubDistrictByDistrict(indexSelect);
//                    for (int i = 0; i < list.size(); i++) {
//                        if (list.get(i).getText().equals(new_customer.getAddress().getSubdistrict())) {
//                            ddlSubDistrict.setSelection(i);
//                            break;
//                        }
//                    }
//                }
//            }
        }
    }

    private synchronized void setItemSpinner(){
        ArrayList<ValTextData> list;
        ArrayAdapter<ValTextData> dataAdapter;

        final ArrayList<String> listfix;
        ArrayAdapter<String> dataAdapterfix;

        //COUNTRY
        final ArrayList<ValTextData> province = delegate.service.getProvinces();
        provinceAdapter _provinceAdapter = new provinceAdapter(this, R.layout.dropdownlist, province);

        ddlProvince.setSelection(0);
        ddlProvince.setAdapter(_provinceAdapter);

        ddlProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public synchronized void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(status==0){
                    status=1;
                }
                setddlDistrict(province.get(arg2).getValue());
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    public synchronized void setddlDistrict(final String provinceID) {
        final ArrayList<ValTextData> district = delegate.service.getDistrictByProvince(provinceID);
        provinceAdapter _provinceAdapter = new provinceAdapter(this, R.layout.dropdownlist, district);
        ddlDistrict.setAdapter(_provinceAdapter);
        ddlDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public synchronized void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(status==1){
                    status=2;
                    ArrayList<ValTextData> list;
                    list= delegate.service.getDistrictByProvince(provinceID);
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getText().equals(new_customer.getAddress().getDistrict())) {
                            ddlDistrict.setSelection(i);
                            break;
                        }
                    }
                }
                setddlSubDistrict(district.get(arg2).getValue());
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public synchronized void setddlSubDistrict(final String districtID) {
        final ArrayList<ValTextData> subDistrict = delegate.service.getSubDistrictByDistrict(districtID);
        provinceAdapter _provinceAdapter = new provinceAdapter(this, R.layout.dropdownlist, subDistrict);
        ddlSubDistrict.setAdapter(_provinceAdapter);
        ddlSubDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public synchronized void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(status==2){
                    status=3;
                    ArrayList<ValTextData> list;
                    list= delegate.service.getSubDistrictByDistrict(districtID);
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getText().equals(new_customer.getAddress().getSubdistrict())) {
                            ddlSubDistrict.setSelection(i);
                            break;
                        }
                    }
                }
                txtPostcode.setText(subDistrict.get(arg2).getText2());
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void setFont(){
        lblHomeId.setTextSize(25);
        lblHomeId.setTypeface(delegate.font_type);
        lblMoo.setTextSize(25);
        lblMoo.setTypeface(delegate.font_type);
        lblBuilding.setTextSize(25);
        lblBuilding.setTypeface(delegate.font_type);
        lblFloor.setTextSize(25);
        lblFloor.setTypeface(delegate.font_type);
        lblRoom.setTextSize(25);
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
        txtWork.setTextSize(25);
        txtWork.setTypeface(delegate.font_type);
        txtWorkDistrict.setTextSize(25);
        txtWorkDistrict.setTypeface(delegate.font_type);
        txtPostcode.setTextSize(25);
        txtPostcode.setTypeface(delegate.font_type);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_customer_th, menu);
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
        if (popup.isShowing()) {
            popup.dismiss();
        }
        if(v.getId() == R.id.btnSend){
            if(validate()){
                packData();
            } else {
                Toast.makeText(this, "Please enter request fill.", Toast.LENGTH_SHORT).show();
            }
        } else if(v.getId() == R.id.btnBack){
            onBackPressed();
        } else if(v.getId() == R.id.btnMenu){
            showPopup(this);
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

    private boolean validate() {
        boolean status = true;

        if(txtHomeId.getText().toString().length() ==0){
            lblHomeId.setText(Html.fromHtml(getString(R.string.add_customer_homeid) + "<font color=\"#FF0000\"> *</font>"));
            status = false;
        } else {
            lblHomeId.setText(getString(R.string.add_customer_homeid));
        }
        if(!ddlProvince.getSelectedItem().toString().equals(txtPromp)){
            lblProvince.setText(Html.fromHtml(getString(R.string.add_customer_province) + "<font color=\"#FF0000\"> *</font>"));
            status = false;
        }else{
            lblProvince.setText(getString(R.string.add_customer_province));
        }
        if(!ddlDistrict.getSelectedItem().toString().equals(txtPromp)){
            lblDistrict.setText(Html.fromHtml(getString(R.string.add_customer_district) + "<font color=\"#FF0000\"> *</font>"));
            status = false;
        }else{
            lblDistrict.setText(getString(R.string.add_customer_district));
        }
        if(!ddlSubDistrict.getSelectedItem().toString().equals(txtPromp)){
            lblSubDistrict.setText(Html.fromHtml(getString(R.string.add_customer_sub_district) + "<font color=\"#FF0000\"> *</font>"));
            status = false;
        }else{
            lblSubDistrict.setText(getString(R.string.add_customer_sub_district));
        }

        return status;
    }

    private void packData(){
        AddressData home = new_customer.getAddress();
        AddressData work = new_customer.getAddressWork();

        if(txtHomeId.getText().toString().length()>0){
            home.setHouseId(txtHomeId.getText().toString());
        }else{
            home.setHouseId("");
        }
        if(txtMoo.getText().toString().length()>0){
            home.setMoo(txtMoo.getText().toString());
        }else{
            home.setMoo("");
        }
        if(txtBuilding.getText().toString().length()>0){
            home.setVillage(txtBuilding.getText().toString());
        }else{
            home.setVillage("");
        }
        if(txtFloor.getText().toString().length()>0){
            home.setFloor(txtFloor.getText().toString());
        }else{
            home.setFloor("");
        }
        if(txtRoom.getText().toString().length()>0){
            home.setRoom(txtRoom.getText().toString());
        }else{
            home.setRoom("");
        }
        if(txtSoi.getText().toString().length()>0){
            home.setSoi(txtSoi.getText().toString());
        }else{
            home.setSoi("");
        }
        if(txtRoad.getText().toString().length()>0){
            home.setRoad(txtRoad.getText().toString());
        }else{
            home.setRoad("");
        }
        if(!ddlProvince.getSelectedItem().toString().equals(txtPromp)){
            home.setProvince(ddlProvince.getSelectedItem().toString());
        }else{
            home.setProvince("");
        }

        if(!ddlDistrict.getSelectedItem().toString().equals(txtPromp)){
            home.setDistrict(ddlDistrict.getSelectedItem().toString());
        }else{
            home.setDistrict("");
        }
        if(!ddlSubDistrict.getSelectedItem().toString().equals(txtPromp)){
            home.setSubdistrict(ddlSubDistrict.getSelectedItem().toString());
        }else{
            home.setSubdistrict("");
        }
        if(txtPostcode.getText().toString().length()>0){
            home.setPostalcode(txtPostcode.getText().toString());
        }else{
            home.setPostalcode("");
        }

        if(txtWork.getText().toString().length()>0){
            work.setVillage(txtWork.getText().toString());
        }else{
            work.setVillage("");
        }
        if(txtWorkDistrict.getText().toString().length()>0){
            work.setDistrict(txtWorkDistrict.getText().toString());
        }else{
            work.setDistrict("");
        }

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
                } else {
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
