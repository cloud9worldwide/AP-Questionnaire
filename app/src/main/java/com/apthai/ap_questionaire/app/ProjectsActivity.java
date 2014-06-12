package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
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
    ImageButton btnMenu;
    RelativeLayout page;
    TextView project_name;
    TextView lbl_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        delegate = (questionniare_delegate)getApplicationContext();


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
                    setTableLayout();
                }
            };
            Runnable background = new Runnable() {
                @Override
                public void run() {
                    // This is the delay
                    delegate.service.sync_save_questionnaire(progress);
                    uiHandler.post( onUi );
                }
            };
            new Thread( background ).start();

        }else {
            list_projectdata = delegate.service.getProjects();
            total = list_projectdata.size();
            setObject();
            setTableLayout();
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

    }


    private void setTableLayout(){
        linearLayout = new LinearLayout(this);
        int icon_size = delegate.pxToDp(85);
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
            int imageWidth = delegate.pxToDp(100), imageHeight = delegate.pxToDp(50);

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
            }

            bmp = null;
            btn.addView(image);

            LinearLayout.LayoutParams lp;

            lp = new LinearLayout.LayoutParams(icon_size,LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            lp.setMargins(delegate.pxToDp(20),delegate.pxToDp( 20), delegate.pxToDp(20), delegate.pxToDp(20));
            lp.gravity = Gravity.CENTER;

            btn.setLayoutParams(lp);
//            btn.setBackgroundColor(Color.GRAY);
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

        } else if(v.getId() == R.id.root_view) {
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
        //Log.e("debug",String.valueOf(v.getX()+delegate.dpToPx(50))+" , "+String.valueOf(v.getY()+delegate.dpToPx(50)));
        popup.showAtLocation(layout, Gravity.NO_GRAVITY,0,(int)v.getY()+delegate.dpToPx(50));
        //popup.showAtLocation(layout, Gravity.NO_GRAVITY, 0, 70);

        View view_instance = (View)layout.findViewById(R.id.popup);
        final RelativeLayout home = (RelativeLayout) layout.findViewById(R.id.menu_home);
        final RelativeLayout settings = (RelativeLayout) layout.findViewById(R.id.menu_settings);
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
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                home.setBackgroundColor(getResources().getColor(R.color.WHITE));
//                settings.setBackgroundColor(getResources().getColor(R.color.ORANGE));
//                logout.setBackgroundColor(getResources().getColor(R.color.WHITE));
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
