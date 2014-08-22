package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.cloud9worldwide.questionnaire.data.QuestionTypeData;

import java.util.ArrayList;


public class FlagActivity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    ImageButton btn_menu, btn_Flag_EN, btn_Flag_TH, btnEN, btnTH;
    questionniare_delegate delegate;
    TextView project_name, question_title, lblFlagTH,lblFlagEN;
    ImageView img_background;
    static PopupWindow popup;
    RelativeLayout root_view;
    LinearLayout content_view,linearLayout;
    int total;
    ArrayList<QuestionTypeData> data;

    private void setImage(){
        setObject();
//        View rootView = getWindow().getDecorView().getRootView();
//        Bitmap imageBitmap = delegate.readImageFileOnSD(delegate.project.getBackgroundUrl(),0, 0);
//        Drawable imageDraw =  new BitmapDrawable(imageBitmap);
//        rootView.setBackground(imageDraw);
        img_background = (ImageView) findViewById(R.id.img_background);
        delegate.imageLoader.display(delegate.project.getBackgroundUrl(),
                String.valueOf(img_background.getWidth()),
                String.valueOf(img_background.getHeight()),
                img_background,
                delegate.imgDefault);

    }

    private void setObject(){
        delegate = (questionniare_delegate)getApplicationContext();

        btn_menu = (ImageButton) findViewById(R.id.btnMenu);
        btn_Flag_EN = (ImageButton) findViewById(R.id.btnFlagEN);
        btn_Flag_TH = (ImageButton) findViewById(R.id.btnFlagTH);
        btnEN = (ImageButton) findViewById(R.id.btnEN);
        btnTH = (ImageButton) findViewById(R.id.btnTH);

        btn_menu.setOnClickListener(this);
        btn_Flag_EN.setOnClickListener(this);
        btn_Flag_TH.setOnClickListener(this);
        btnEN.setOnClickListener(this);
        btnTH.setOnClickListener(this);

        project_name = (TextView) findViewById(R.id.project_name);
        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);

        question_title = (TextView) findViewById(R.id.question_title);
        question_title.setTypeface(delegate.font_type);
        question_title.setTextSize(35);

        lblFlagTH = (TextView) findViewById(R.id.lblFlagTH);
        lblFlagTH.setTypeface(delegate.font_type);
        lblFlagTH.setTextSize(30);

        lblFlagEN = (TextView) findViewById(R.id.lblFlagEN);
        lblFlagEN.setTypeface(delegate.font_type);
        lblFlagEN.setTextSize(30);

        root_view = (RelativeLayout) findViewById(R.id.root_view);
        root_view.setOnClickListener(this);
        popup = new PopupWindow(this);
        changeLanguege();

    }

    private void changeLanguege() {
        project_name.setText(delegate.project.getName());
        if (delegate.service.getLg().equals("en")) {
            btnEN.setImageResource(R.drawable.btn_en_);
            btnTH.setImageResource(R.drawable.btn_th);
        } else {
            btnEN.setImageResource(R.drawable.btn_en);
            btnTH.setImageResource(R.drawable.btn_th_);
        }
        question_title.setText(R.string.title_flag);
        lblFlagTH.setText(R.string.flag_name_TH);
        lblFlagEN.setText(R.string.flag_name_EN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag);
        setImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.flag, menu);
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
                setResult(2);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnMenu) {
            if (popup.isShowing()) {
                popup.dismiss();
            } else {
                showPopup(this);
            }
        } else if (v.getId() == R.id.root_view) {
            if (popup.isShowing()) {
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
        } else if(v.getId() == R.id.btnFlagEN){
            delegate.service.setLg("en");
            delegate.setLocale("en");
            startQuestion();
        } else if(v.getId() == R.id.btnFlagTH){
            delegate.service.setLg("th");
            delegate.setLocale("th");
            startQuestion();
        }
    }
    private void startQuestion(){
        delegate.initQuestions();

        Intent newPage = new Intent();
        if(delegate.QM.get_question().getQuestionType().equals("1")){
            newPage = new Intent(this, Display01Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("2")){
            newPage = new Intent(this, Display02Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("3")){
            newPage = new Intent(this, Display03Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("4")){
            newPage = new Intent(this, Display04Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("5")){
            newPage = new Intent(this, Display05Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("6")){
            newPage = new Intent(this, Display06Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("7")){
            newPage = new Intent(this, Display07Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("8")){
            newPage = new Intent(this, Display08Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("9")){
            newPage = new Intent(this, Display09Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("10")){
            newPage = new Intent(this, Display10Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("11")){
            newPage = new Intent(this, Display11Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("12")){
            newPage = new Intent(this, Display12Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("13")){
            newPage = new Intent(this, Display13Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("14")){
            newPage = new Intent(this, Display14Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("15")){
            newPage = new Intent(this, Display15Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("16")){
            newPage = new Intent(this, Display16Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("17")){
            newPage = new Intent(this, Display17Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("18")){
            newPage = new Intent(this, Display18Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("19")){
            newPage = new Intent(this, Display19Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("20")){
            newPage = new Intent(this, Display20Activity.class);
        } else if(delegate.QM.get_question().getQuestionType().equals("21")){
            newPage = new Intent(this, Display21Activity.class);
        }

        final Intent _newPage = newPage;
        if(delegate.service.isOnline()) {
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Please wait");
            progress.setMessage("Loading....");
            progress.setCancelable(false);
            progress.show();

            final Handler uiHandler = new Handler();
            final Runnable onUi = new Runnable() {
                @Override
                public void run() {
                    // this will run on the main UI thread
                    progress.dismiss();
                    //startActivityForResult(_newPage,0);
                    delegate.nextQuestionPage(_newPage);
                }
            };
            Runnable background = new Runnable() {
                @Override
                public void run() {
                    delegate.getQuestionnaireHistory();
                    //delay
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {

                    }
                    uiHandler.post(onUi);
                }
            };
            new Thread(background).start();

        }else{
            startActivityForResult(_newPage,0);
        }
    }
}

