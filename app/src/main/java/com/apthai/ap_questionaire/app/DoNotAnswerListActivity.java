package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud9worldwide.questionnaire.data.QuestionTypeData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DoNotAnswerListActivity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    ImageButton btn_menu, btn_next, btn_back;
    questionniare_delegate delegate;
    TextView project_name,question_title;
    ImageView img_background;
    static PopupWindow popup;
    RelativeLayout root_view;
    LinearLayout content_view,linearLayout;
    int total;
    ArrayList<QuestionTypeData> data;

    private void setImage(){
        setObject();
        img_background = (ImageView) findViewById(R.id.img_background);
        if(delegate.project.getBackgroundUrl().trim().length()!=0) {
            delegate.imageLoader.display(delegate.project.getBackgroundUrl().trim(),
                    String.valueOf(img_background.getWidth()),
                    String.valueOf(img_background.getHeight()),
                    img_background, R.drawable.space);
        }
    }
    private void setObject(){
        delegate = (questionniare_delegate)getApplicationContext();

        btn_menu = (ImageButton) findViewById(R.id.btnMenu);
        btn_next = (ImageButton) findViewById(R.id.btnNext);
        btn_back = (ImageButton) findViewById(R.id.btnBack);

        btn_menu.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        project_name = (TextView) findViewById(R.id.project_name);
        project_name.setText(delegate.project.getName());
        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);

        question_title = (TextView) findViewById(R.id.question_title);
        question_title.setTypeface(delegate.font_type);
        question_title.setTextSize(35);
        question_title.setText(R.string.title_dont_answer);

        root_view = (RelativeLayout) findViewById(R.id.root_view);
        root_view.setOnClickListener(this);
        popup = new PopupWindow(this);

        content_view = (LinearLayout)this.findViewById(R.id.AP_content);
        content_view.removeAllViews();

        data = delegate.QM.CheckQuestionNotAns();
        total = data.size();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_not_answer_list);
        delegate = (questionniare_delegate)getApplicationContext();
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Please wait");
        progress.setMessage("Loading....");
        progress.setCancelable(false);
        progress.show();

        final Handler uiHandler = new Handler();
        final  Runnable onUi = new Runnable() {
            @Override
            public void run() {
                // this will run on the main UI thread
                progress.dismiss();
                setImage();
                setTableLayout();

            }
        };
        Runnable background = new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(delegate.timesleep);
                }catch (Exception e){

                }
                uiHandler.post( onUi );
            }
        };
        new Thread( background ).start();
    }

    private void setTableLayout(){

        for(int i =0, c = 0; i < total; i++, c++) {
            LinearLayout.LayoutParams lp;
            TextView questionTitle = new TextView(this);
            String txtShown = "(" + data.get(i).getQuestionOrder() + ") " + data.get(i).getQuestion().getTitle();
            questionTitle.setText(txtShown);
            questionTitle.setTextSize(30);
            questionTitle.setTypeface(delegate.font_type);
            questionTitle.setGravity(Gravity.CENTER);
            questionTitle.setTag(i);
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, delegate.dpToPx(55));
            lp.gravity = Gravity.CENTER_VERTICAL;
            lp.weight = 1;
            lp.setMargins(delegate.dpToPx(20), delegate.dpToPx(10), 0, delegate.dpToPx(10));
            questionTitle.setLayoutParams(lp);
            questionTitle.setOnClickListener(this);
            content_view.addView(questionTitle);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.do_not_answer_list, menu);
        return true;
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
        }
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
        if(v.getId() == R.id.btnMenu){
            if (popup.isShowing()) {
                popup.dismiss();
            } else {
                showPopup(this);
            }
        } else if(v.getId() == R.id.btnNext){
            delegate.initQuestionsStaff();
            nextPage();
        }
        else if(v.getId() == R.id.root_view){
            if (popup.isShowing()) {
                popup.dismiss();
            }
        }

        else {
            final int indexSelected =Integer.parseInt(v.getTag().toString());
            Log.e("indexSelected", indexSelected +"");
            delegate.QM.redo_question_not_ansAtIndex(indexSelected);
            QuestionTypeData Test = delegate.QM.get_question();
            Log.e("Test ",Test.toString());
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
                delegate.isBack = 0;
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
                delegate.isBack = 2;
                finish();
            }
        });
    }

    public void nextPage(){
        QuestionTypeData data = delegate.getQuestions();
        if(data == null){
            startActivityForResult(new Intent(this, CustomerFinishedAnswerActivity.class),0);
        } else {
            Intent intent = delegate.getCurentQuestionIntent();
            delegate.nextQuestionPage(intent);
        }
    }

    @Override
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
}
