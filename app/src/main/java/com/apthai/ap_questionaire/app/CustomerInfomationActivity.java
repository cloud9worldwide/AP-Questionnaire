package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
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
import android.widget.Toast;

import com.cloud9worldwide.questionnaire.data.ContactData;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CustomerInfomationActivity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total = 5;
    ImageButton btn_menu, btn_next, btn_back, btn_reVisit, btnEN, btnTH, btnEdit;
    LinearLayout linearLayout, content_view;
    questionniare_delegate delegate;
    String customerIndex;
    ContactData customer_info;
    TextView project_name;
    TextView txtFirstName, txtLastName, txtAddress, txtMobile, txtTel, txtEmail;
    static PopupWindow popup;
    RelativeLayout root_view;
    ImageView img_background;

    TextView question_title,title;
    TextView lbl_Fname , lbl_Lname, lbl_address, lbl_mobile, lbl_tel, lbl_email;
    Context ctx;

    boolean loadready = false;
    private void setImage(){
        img_background = (ImageView) findViewById(R.id.img_background);

        delegate.imageLoader.display(delegate.project.getBackgroundUrl(),
                String.valueOf(img_background.getWidth()),
                String.valueOf(img_background.getHeight()),
                img_background,R.drawable.logo_ap);

    }

    private synchronized void loadData(){
        delegate = (questionniare_delegate)getApplicationContext();
        customerIndex = delegate.service.globals.getContactId();
        ctx =this;

//        customer_info = delegate.service.getContactInfo("eiei");

//        customer_info = delegate.service.getContactInfo(customerIndex);
//        if(customer_info !=null) {
//            customer_info.setContactId(delegate.service.globals.getContactId());
//        }
//        setObject();
//        setImage();

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
                setImage();
                loadready = true;
            }
        };
        final  Runnable onUi2 = new Runnable() {
            @Override
            public void run() {
                // this will run on the main UI thread
                progress.dismiss();
                new AlertDialog.Builder(CustomerInfomationActivity.this)
                        .setTitle("Connection Lost")
                        .setMessage("Please enter customer info again.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(1);
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        };
        Runnable background = new Runnable() {
            @Override
            public void run() {
                // This is the delay
                customer_info = delegate.service.getContactInfo(customerIndex);
                if(customer_info !=null){
                    customer_info.setContactId(delegate.service.globals.getContactId());
                    try {
                        Thread.sleep(2000);
                    }catch (Exception e){

                    }
                    uiHandler.post( onUi );
                }else{
                    uiHandler.post( onUi2 );
                }
            }
        };
        new Thread( background ).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_infomation);


        //cut
        loadData();
    }

    private void setData(){

        lbl_Fname.setText(R.string.add_customer_name);
        lbl_Lname.setText(R.string.add_customer_surname);
        lbl_address.setText(R.string.address);
        lbl_mobile.setText(R.string.add_customer_mobilenumber);
        lbl_tel.setText(R.string.telephone);
        lbl_email.setText(R.string.add_customer_email);

        project_name.setText(delegate.project.getName());
        String strAddress = "";

        if(customer_info.getAddress().getHouseId().trim().length()!=0 && !customer_info.getAddress().getHouseId().trim().equals("null")){
            strAddress = getResources().getString(R.string.add_customer_homeid)+ " " + customer_info.getAddress().getHouseId();
        }

        if(customer_info.getAddress().getMoo().trim().length()!=0 && !customer_info.getAddress().getMoo().trim().equals("null")){
            if(strAddress.length() >0){
                strAddress = strAddress + " ";
            }
            strAddress = strAddress + getResources().getString(R.string.add_customer_moo) +" " + customer_info.getAddress().getMoo();
        }

        if(customer_info.getAddress().getVillage().trim().length()!=0 && !customer_info.getAddress().getVillage().trim().equals("null")){
            if(strAddress.length() >0){
                strAddress = strAddress + " ";
            }
            strAddress = strAddress + getResources().getString(R.string.add_customer_building) +" " + customer_info.getAddress().getVillage();
        }

        if(customer_info.getAddress().getSoi().trim().length()!=0 && !customer_info.getAddress().getSoi().trim().equals("null")){
            if(strAddress.length() >0){
                strAddress = strAddress + " ";
            }
            strAddress = strAddress + getResources().getString(R.string.add_customer_soi) +" " + customer_info.getAddress().getSoi();
        }

        if(customer_info.getAddress().getRoad().trim().length()!=0 && !customer_info.getAddress().getRoad().trim().equals("null")){
            if(strAddress.length() >0){
                strAddress = strAddress + " ";
            }
            strAddress = strAddress + getResources().getString(R.string.add_customer_road) +" " + customer_info.getAddress().getRoad();
        }

        if(customer_info.getAddress().getSubdistrict().trim().length()!=0 && !customer_info.getAddress().getSubdistrict().trim().equals("null")){
            if(strAddress.length() >0){
                strAddress = strAddress + " ";
            }
            strAddress = strAddress + " " + getResources().getString(R.string.add_customer_sub_district) +" " + customer_info.getAddress().getSubdistrict();
        }

        if(customer_info.getAddress().getDistrict().trim().length()!=0 && !customer_info.getAddress().getDistrict().trim().equals("null")){
            if(strAddress.length() >0){
                strAddress = strAddress + " ";
            }
            strAddress = strAddress + " " + getResources().getString(R.string.add_customer_district) +" " + customer_info.getAddress().getDistrict();
        }

        if(customer_info.getAddress().getProvince().trim().length()!=0 && !customer_info.getAddress().getProvince().trim().equals("null")){
            if(strAddress.length() >0){
                strAddress = strAddress + " ";
            }
            strAddress = strAddress + getResources().getString(R.string.add_customer_province) +" " + customer_info.getAddress().getProvince();
        }

        if(customer_info.getAddress().getPostalcode().trim().length()!=0 && !customer_info.getAddress().getPostalcode().trim().equals("null")){
            if(strAddress.length() >0){
                strAddress = strAddress + " ";
            }
            strAddress = strAddress + getResources().getString(R.string.add_customer_postcode) +" " + customer_info.getAddress().getPostalcode();
        }

        txtAddress.setText(strAddress);

    }

    private void setObject() {

        if(customer_info == null){
            onBackPressed();
        } else {
            content_view = (LinearLayout) findViewById(R.id.AP_content);

            project_name = (TextView) findViewById(R.id.project_name);
            project_name.setTextSize(30);
            project_name.setTypeface(delegate.font_type);
            project_name.setGravity(Gravity.CENTER);

            txtFirstName = (TextView) findViewById(R.id.txtFirstName);
            txtFirstName.setText(customer_info.getFname());
            txtFirstName.setTypeface(delegate.font_type);
            txtFirstName.setTextSize(25);
            txtLastName = (TextView) findViewById(R.id.txtLastName);
            txtLastName.setText(customer_info.getLname());
            txtLastName.setTypeface(delegate.font_type);
            txtLastName.setTextSize(25);
            txtAddress = (TextView) findViewById(R.id.txtAddress);
            txtAddress.setTypeface(delegate.font_type);
            txtAddress.setTextSize(25);
            txtMobile = (TextView) findViewById(R.id.txtMobile);
            String allMobile = "";
            if(customer_info.getMobiles() !=null){
                for(int i = 0; i< customer_info.getMobiles().size(); i++){
                    if(customer_info.getMobiles().get(i).length() !=0){
                        if(!customer_info.getMobiles().get(i).equals("null")){
                            if(!allMobile.equals("")){
                                allMobile = allMobile + ", ";
                            }
                            allMobile = allMobile + numberformat(customer_info.getMobiles().get(i));
                        }
                    }
                }
            }
            if(allMobile.equals("")){
                allMobile = "-";
            }
            txtMobile.setText(allMobile);
            txtMobile.setTypeface(delegate.font_type);
            txtMobile.setTextSize(25);
            txtTel = (TextView) findViewById(R.id.txtTel);
            String allTels = "";
            if(customer_info.getTels() !=null){
                for(int i = 0; i< customer_info.getTels().size(); i++){
                    if(customer_info.getTels().get(i).length() !=0){
                        if(!customer_info.getTels().get(i).equals("null")) {
                            if (!allTels.equals("")) {
                                allTels = allTels + ", ";
                            }
                            allTels = allTels + numberformat(customer_info.getTels().get(i));
                        }
                    }
                }
            }
            if(allTels.equals("")){
                allTels = "-";
            }
            txtTel.setText(allTels);
            txtTel.setTypeface(delegate.font_type);
            txtTel.setTextSize(25);
            txtEmail = (TextView) findViewById(R.id.txtEmail);
            if(customer_info.getEmail().toString().trim().length()!=0 && !customer_info.getEmail().equals("null")){
                txtEmail.setText(customer_info.getEmail());
            } else {
                txtEmail.setText("-");
            }


            txtEmail.setTypeface(delegate.font_type);
            txtEmail.setTextSize(25);
            btnEdit = (ImageButton) findViewById(R.id.btnEdit);
            btnEdit.setOnClickListener(this);

            btn_next = (ImageButton) findViewById(R.id.btnNext);
            btn_next.setOnClickListener(this);

            btn_menu = (ImageButton) findViewById(R.id.btnMenu);
            btn_menu.setOnClickListener(this);

            btn_back = (ImageButton) findViewById(R.id.btnBack);
            btn_back.setOnClickListener(this);

            btn_reVisit = (ImageButton) findViewById(R.id.btnReVisit);
            btn_reVisit.setOnClickListener(this);

            popup = new PopupWindow(this);

            root_view = (RelativeLayout) findViewById(R.id.root_view);
            root_view.setOnClickListener(this);

            question_title = (TextView) findViewById(R.id.question_title);
            question_title.setTypeface(delegate.font_type);
            question_title.setTextSize(25);

            title = (TextView) findViewById(R.id.title);
            title.setTypeface(delegate.font_type);
            title.setTextSize(30);

            lbl_Fname = (TextView) findViewById(R.id.lbl_Fname);
            lbl_Lname = (TextView) findViewById(R.id.lbl_Lname);
            lbl_address = (TextView) findViewById(R.id.lbl_address);
            lbl_mobile = (TextView) findViewById(R.id.lbl_mobile);
            lbl_tel = (TextView) findViewById(R.id.lbl_tel);
            lbl_email = (TextView) findViewById(R.id.lbl_email);

            lbl_Fname.setTypeface(delegate.font_type);
            lbl_Lname.setTypeface(delegate.font_type);
            lbl_address.setTypeface(delegate.font_type);
            lbl_mobile.setTypeface(delegate.font_type);
            lbl_tel.setTypeface(delegate.font_type);
            lbl_email.setTypeface(delegate.font_type);

            lbl_Fname.setTextSize(25);
            lbl_Lname.setTextSize(25);
            lbl_address.setTextSize(25);
            lbl_mobile.setTextSize(25);
            lbl_tel.setTextSize(25);
            lbl_email.setTextSize(25);
        }

        if(delegate.service.isOnline()) {
            btnEdit.setVisibility(View.VISIBLE);
            btn_reVisit.setVisibility(View.VISIBLE);
        } else {
            btnEdit.setVisibility(View.INVISIBLE);
            btn_reVisit.setVisibility(View.INVISIBLE);
        }

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
            btnEdit.setImageResource(R.drawable.btn_en_edit_profile);
        } else {
            btnEN.setImageResource(R.drawable.btn_en);
            btnTH.setImageResource(R.drawable.btn_th_);
            btnEdit.setImageResource(R.drawable.btn_th_edit_profile);
        }
        question_title.setText(R.string.title_activity_customer_list);
        title.setText(R.string.title_customer_info);
        setData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_infomation, menu);
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
        if(v.getId() == R.id.btnNext){
            if(popup.isShowing()){
                popup.dismiss();
            } else {

                delegate.customer_selected = customer_info;



                final ProgressDialog progress10 = new ProgressDialog(this);
                progress10.setTitle("Please wait");
                progress10.setMessage("Loading....");
                progress10.setCancelable(false);
                progress10.show();

                final Handler uiHandler = new Handler();
                final  Runnable onUi10 = new Runnable() {
                    @Override
                    public void run() {
                        // this will run on the main UI thread
                        progress10.dismiss();

                        startActivityForResult(new Intent(ctx, FlagActivity.class),0);
                    }
                };
                Runnable background = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(delegate.timesleep);
                        }catch (Exception e){
                        }
                        uiHandler.post( onUi10 );
                    }
                };
                new Thread( background ).start();


            }
        } else if(v.getId() == R.id.btnBack){
            if(popup.isShowing()){
                popup.dismiss();
            } else {
                onBackPressed();
            }
        } else if(v.getId() ==R.id.btnReVisit){
            if(popup.isShowing()){
                popup.dismiss();
            } else {
                //method revisit

                final ProgressDialog progress = new ProgressDialog(this);
                progress.setTitle("Please wait");
                progress.setMessage("Loading....");
                progress.setCancelable(false);
                progress.show();

                final Handler uiHandler = new Handler();
                final  Runnable onUi11 = new Runnable() {
                    @Override
                    public void run() {
                        // this will run on the main UI thread
                        progress.dismiss();
                        delegate.customer_selected = customer_info;
                        delegate.StampVisitLog();
                        startActivityForResult(new Intent(ctx, CustomerFinishedAnswerActivity.class),0);

                    }
                };
                Runnable background = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(delegate.timesleep);
                        }catch (Exception e){
                        }
                        uiHandler.post( onUi11 );
                    }
                };
                new Thread( background ).start();

            }
        } else if(v.getId() == R.id.btnMenu){
            if(popup.isShowing()){
                popup.dismiss();
            } else {
                showPopup(this);
            }
        } else if(v.getId() == R.id.btnEdit) {
            if(popup.isShowing()){
                popup.dismiss();
            } else {

                if(delegate.service.isOnline()){

                final ProgressDialog progress = new ProgressDialog(this);
                progress.setTitle("Please wait");
                progress.setMessage("Loading....");
                progress.setCancelable(false);
                progress.show();

                final Handler uiHandler = new Handler();
                final  Runnable onUi12 = new Runnable() {
                    @Override
                    public void run() {
                        // this will run on the main UI thread
                        progress.dismiss();
                        delegate.customer_selected = customer_info;
                        startActivityForResult(new Intent(ctx, AddCustomerOneActivity.class),0);

                    }
                };
                Runnable background = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(delegate.timesleep);
                        }catch (Exception e){
                        }
                        uiHandler.post( onUi12 );
                    }
                };
                new Thread( background ).start();


                } else {
                    Toast.makeText(this, R.string.is_offine, Toast.LENGTH_SHORT).show();
                }
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

    public void onBackPressed() {
        this.setResult(3);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode ==2 || resultCode == 0|| resultCode == 1){
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

    public String numberformat(String phone){
        if(phone.length()==10){
            //mobile mode
            return PhoneNumberUtils.formatNumber(phone);
        } else if(phone.length()==9){
            //home mode
            return phone.substring(0,2) +"-"+phone.substring(2,5)+"-"+phone.substring(5,9);
        }
        //do not anythings
        return phone;
    }
}
