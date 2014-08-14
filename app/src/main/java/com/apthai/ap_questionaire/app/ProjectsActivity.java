package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud9worldwide.questionnaire.data.ProjectData;

import java.util.ArrayList;


public class ProjectsActivity extends Activity implements OnClickListener {

    final String TAG = "eiei";
    int total = 1;
    Button btn_settings, btn_home;
    LinearLayout linearLayout, content_view;
    questionniare_delegate delegate;
    ArrayList<ProjectData> list_projectdata;
    static PopupWindow popup;
    ImageButton btnMenu, btnEN, btnTH;
    RelativeLayout page;
    TextView project_name;
    TextView lbl_title;

    Context mCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        mCtx = this;

        delegate = (questionniare_delegate)getApplicationContext();

        if(delegate.service.getLg().equals("en")){
            delegate.setLocale("en");
        } else {
            delegate.setLocale("th");
        }

//        Button sycn_geo = (Button)findViewById(R.id.sync_geo);
//        sycn_geo.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("debug","click");
//                delegate.service.sync_geo_data(mCtx);
//            }
//        });

        if(delegate.service.isOnline()){
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Please wait");
            progress.setMessage("Sync local data to server.");
            progress.setCancelable(false);
            progress.show();

            final Handler uiHandler = new Handler();
            final  Runnable onUi = new Runnable() {
                @Override
                public void run() {
                    // this will run on the main UI thread
                    progress.dismiss();
                    list_projectdata = delegate.service.getProjects();
                    total = list_projectdata.size();
                    setObject();
                }
            };
            Runnable background = new Runnable() {
                @Override
                public void run() {
                    // This is the delay
                    delegate.service.sync_save_questionnaire2(progress);
                    uiHandler.post( onUi );
                }
            };
            new Thread( background ).start();

        } else {
            list_projectdata = delegate.service.getProjects();
            total = list_projectdata.size();
            setObject();
        }

    }

    private void setObject(){
        popup = new PopupWindow(this);

        content_view = (LinearLayout)this.findViewById(R.id.AP_content);
        content_view.removeAllViews();

        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);

        page = (RelativeLayout) findViewById(R.id.root_view);
        page.setOnClickListener(this);

        project_name = (TextView) findViewById(R.id.project_name);
        project_name.setText("AP PROJECTS");
        project_name.setTypeface(delegate.font_type);

        project_name.setTextSize(30);
        project_name.setGravity(Gravity.CENTER);

        lbl_title = (TextView) findViewById(R.id.lbl_title);
        lbl_title.setTypeface(delegate.font_type);
        lbl_title.setTextSize(25);

        btnEN = (ImageButton) findViewById(R.id.btnEN);
        btnTH = (ImageButton) findViewById(R.id.btnTH);
        btnEN.setOnClickListener(this);
        btnTH.setOnClickListener(this);
        changeLanguege();

    }
    private void changeLanguege(){

        lbl_title.setText(R.string.YOUR_PROJECT);
        if(delegate.service.getLg().equals("en")){
            btnEN.setImageResource(R.drawable.btn_en_);
            btnTH.setImageResource(R.drawable.btn_th);
        } else {
            btnEN.setImageResource(R.drawable.btn_en);
            btnTH.setImageResource(R.drawable.btn_th_);
        }
        content_view.removeAllViews();
        list_projectdata = delegate.service.getProjects();
        setTableLayout();
    }


    private void setTableLayout(){
        linearLayout = new LinearLayout(this);
//        int icon_size = delegate.pxToDp(400);
        int icon_size = 0;
        int icon_size_for_demo = delegate.pxToDp(150);

        int column =4 ;
//        if(total >10){
//            column = 4;
//        } else if (total >4){
//            column = 3;
//        } else if(total >1){
//            column =2;
//        } else {
//            column =1;
//        }
        int row = (total / column);
        if(total%column != 0){
            row++;
        }

        for(int i =0, c = 0, r = 0; i < total; i++, c++){
            ProjectData obj = list_projectdata.get(i);
//            int imageWidth = delegate.dpToPx(100), imageHeight = delegate.dpToPx(50);
            int imageWidth = 0, imageHeight = 0;
            int imageWidth2 = delegate.dpToPx(200), imageHeight2 = delegate.dpToPx(100);

            if(c == column){
                c = 0;
                r++;
                content_view.addView(linearLayout);
                linearLayout = new LinearLayout(this);
            }

            LinearLayout btn = new LinearLayout(this);
            btn.setOrientation(LinearLayout.VERTICAL);

            ImageView image = new ImageView(this);
            btn.setTag(i);

            btn.setOnClickListener(this);

            Bitmap bmp = delegate.readImageFileOnSD(obj.getLogoUrl(), imageWidth, imageHeight);
            if(bmp !=null) {
                image.setImageBitmap(bmp);
                LinearLayout.LayoutParams lpImage =new LinearLayout.LayoutParams(imageWidth2,imageHeight2);
                lpImage.gravity = Gravity.CENTER;
                image.setLayoutParams(lpImage);
            }
            bmp = null;

            btn.addView(image);
//            if(i%2 ==0){
//                btn.setBackgroundColor(Color.RED);
//            } else {
//                btn.setBackgroundColor(Color.GRAY);
//            }

            LinearLayout.LayoutParams lp;

            lp = new LinearLayout.LayoutParams(icon_size,LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            lp.setMargins(delegate.dpToPx(20),delegate.dpToPx(20), delegate.dpToPx(20), delegate.dpToPx(20));
            lp.gravity = Gravity.CENTER;

            btn.setLayoutParams(lp);
            linearLayout.addView(btn);

        }
        content_view.addView(linearLayout);
    }

    public void onClick(View v) {

        if(v.getId() == R.id.btnMenu){
            if (popup.isShowing()) {
                popup.dismiss();
            } else {
                showPopup(this);
            }

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

        else if(v.getId() == R.id.root_view) {
            if (popup.isShowing()) {
                popup.dismiss();
            }
        } else {
            if (popup.isShowing()) {
                popup.dismiss();
            } else {
                Intent i = new Intent(this, QuestionniareActivity.class);
                delegate.project = list_projectdata.get(Integer.parseInt(v.getTag().toString()));
                i.putExtra("projectIndex", v.getTag().toString());
                startActivityForResult(i, 0);
            }
        }
    }
    public void onBackPressed() {
        delegate.service.Logout();
        this.setResult(4);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode==2){
            this.setResult(resultCode);
            finish();
        }
    }

    protected void onResume() {
        super.onResume();
        Log.e("Resume", "Resume");
//        if(delegate.isBack == 2 || delegate.isBack == 0 || delegate.isBack == 1){
//            this.setResult(delegate.isBack);
//            delegate.isBack = 9;
//            finish();
//        }

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
//                home.setBackgroundColor(getResources().getColor(R.color.ORANGE));
//                settings.setBackgroundColor(getResources().getColor(R.color.WHITE));
//                logout.setBackgroundColor(getResources().getColor(R.color.WHITE));
//                setResult(0);
//                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(getResources().getColor(R.color.WHITE));
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
