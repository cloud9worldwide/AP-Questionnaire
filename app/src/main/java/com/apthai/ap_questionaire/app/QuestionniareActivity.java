package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.cloud9worldwide.questionnaire.data.QuestionnaireData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class QuestionniareActivity extends Activity implements OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total = 1;
    Button btn_settings, btn_home;
    LinearLayout linearLayout, content_view;
    questionniare_delegate delegate;
    String projectIndex;
    ArrayList<QuestionnaireData> questionList;
    ImageView img_logo_project, img_background;
    TextView project_name;
    ImageButton btnMenu, btnEN, btnTH;
    RelativeLayout root_view;
    static PopupWindow popup;
    ProjectData project_data;
    TextView lbl_title, lblFullName;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionniare);
        ctx = this;

        setImage();
    }


    private void setImage() {
        delegate = (questionniare_delegate)getApplicationContext();
        project_data = delegate.project;
        img_background = (ImageView) findViewById(R.id.img_background);
        img_logo_project = (ImageView)findViewById(R.id.img_logo_project);
        setObject();
    }

    private void setObject(){
        popup = new PopupWindow(this);

        content_view = (LinearLayout)this.findViewById(R.id.AP_content);
        content_view.removeAllViews();

        project_name = (TextView) findViewById(R.id.project_name);
        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);

        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);

        root_view = (RelativeLayout) findViewById(R.id.root_view);
        root_view.setOnClickListener(this);

        lbl_title = (TextView) findViewById(R.id.lbl_title);
        lbl_title.setTypeface(delegate.font_type);
        lbl_title.setTextSize(35);

        lblFullName= (TextView) findViewById(R.id.lblFullName);
        lblFullName.setTypeface(delegate.font_type);
        lblFullName.setTextSize(25);
        lblFullName.setText("User : " + delegate.service.getfullName());
        lblFullName.setTextColor(getResources().getColor(R.color.staffname));

        btnEN = (ImageButton) findViewById(R.id.btnEN);
        btnTH = (ImageButton) findViewById(R.id.btnTH);
        btnEN.setOnClickListener(this);
        btnTH.setOnClickListener(this);

    }

    private void changeLanguege(){

        if(delegate.project.getBackgroundUrl().length()!=0) {
            delegate.imageLoader.display(delegate.project.getBackgroundUrl(),
                    String.valueOf(img_background.getWidth()),
                    String.valueOf(img_background.getHeight()),
                    img_background, R.drawable.space);
        }

        lbl_title.setText(R.string.Please_select_questionnaire);
        if(delegate.service.getLg().equals("en")){
            btnEN.setImageResource(R.drawable.btn_en_);
            btnTH.setImageResource(R.drawable.btn_th);
        } else {
            btnEN.setImageResource(R.drawable.btn_en);
            btnTH.setImageResource(R.drawable.btn_th_);
        }

        if (delegate.project.getLogoUrl().trim().length() > 0){
            Bitmap bmp = delegate.readImageFileOnSD(delegate.project.getLogoUrl().trim(), delegate.dpToPx(200), delegate.dpToPx(100));
            if(bmp !=null) {
                img_logo_project.setImageBitmap(bmp);
                LinearLayout.LayoutParams lpImage =new LinearLayout.LayoutParams(delegate.dpToPx(200), delegate.dpToPx(100));
                lpImage.gravity = Gravity.CENTER;
                img_logo_project.setLayoutParams(lpImage);
            } else {
                img_logo_project.setImageResource(R.drawable.logo_ap);
            }
            bmp = null;
        }else {
            img_logo_project.setImageResource(R.drawable.logo_ap);
        }


        project_name.setText(delegate.project.getName());

        content_view.removeAllViews();
        questionList = delegate.project.getQuestionnaireList();
        total= questionList.size();

        setTableLayout();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.questionniare, menu);
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

    private void setTableLayout(){
        linearLayout = new LinearLayout(this);
        int icon_size = delegate.dpToPx(0);

        int column =4 ;

        int row = (total / column);
        if(total%column != 0){
            row++;
        }

        for(int i =0, c = 0, r = 0; i < total; i++, c++){
            QuestionnaireData obj = questionList.get(i);
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
//            Bitmap bmp = delegate.readImageFileOnSD(obj.getLogoUrl(),imageWidth,imageHeight);
//            image.setImageBitmap(bmp);
//            bmp = null;

            if (obj.getLogoUrl().trim().length() > 0) {
                Bitmap bmp = delegate.readImageFileOnSD(obj.getLogoUrl().trim(), imageWidth, imageHeight);
                if(bmp !=null) {
                    image.setImageBitmap(bmp);
                } else {
                    image.setImageResource(R.drawable.logo_ap);
                }
                LinearLayout.LayoutParams lpImage =new LinearLayout.LayoutParams(imageWidth2,imageHeight2);
                lpImage.gravity = Gravity.CENTER;
                image.setLayoutParams(lpImage);
                bmp = null;
            }else {
                image.setImageResource(R.drawable.logo_ap);
            }


            TextView name = new TextView(this);
//            name.setText(obj.get);
            name.setWidth(imageWidth);
            name.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

            btn.addView(image);
            btn.addView(name);
            LinearLayout.LayoutParams lp;
            lp = new LinearLayout.LayoutParams(icon_size,LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            lp.setMargins(delegate.dpToPx(20), delegate.dpToPx(20), delegate.dpToPx(20), delegate.dpToPx(20));

            btn.setLayoutParams(lp);
            linearLayout.addView(btn);
        }
        content_view.addView(linearLayout);
    }

    public void onClick(View v) {

        if(v.getId() == R.id.btnMenu){
            if (popup.isShowing()) {
                popup.dismiss();
            }else {
                showPopup(this);
            }


        } else if(v.getId() == R.id.root_view) {
            if (popup.isShowing()) {
                popup.dismiss();
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
        else {
            if (popup.isShowing()) {
                popup.dismiss();
            } else {
                Intent i = new Intent(this, CustomerLookUpActivity.class);
                delegate.questionnaire_selected = questionList.get(Integer.parseInt(v.getTag().toString()));
                delegate.setQuestionnaire_time(questionList.get(Integer.parseInt(v.getTag().toString())).getTimeStamp());

                startActivityForResult(i, 0);
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG,  "resultCode : " + resultCode);

        if(resultCode == 0 || resultCode == 2) {
            this.setResult(resultCode);
            finish();
        } else if(resultCode ==1){
            //stay here
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
                if (popup.isShowing()) {
                    popup.dismiss();
                }
                setResult(0);
                finish();
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
//                setResult(2);
//                finish();
                Intent i = new Intent(ctx , LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }
}
