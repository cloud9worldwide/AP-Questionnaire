package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud9worldwide.questionnaire.data.QuestionAnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionTypeData;
import com.cloud9worldwide.questionnaire.data.SaveAnswerData;

import java.util.ArrayList;


public class Display21Activity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total;
    Button btn_settings, btn_home;
    LinearLayout content_view;
    QuestionTypeData data;
    questionniare_delegate delegate;
    TextView project_name, txt_question;
    int selected =0;
    ArrayList<SaveAnswerData> answer;
    ImageButton btnNext, btnBack, btnEN, btnTH;
    static PopupWindow popup;
    ImageView img_background;
    EditText txtbox;

    TextView navigatorBar;
    TextView txt_process;
    Drawable thumb;
    RelativeLayout footer;

    private Context ctx;
    private QuestionAnswerData checkAnswer = null;

    private void setImage(){
//        img_background = (ImageView) findViewById(R.id.img_background);
//        delegate.imageLoader.display(delegate.project.getBackgroundUrl(),
//                String.valueOf(img_background.getWidth()),
//                String.valueOf(img_background.getHeight()),
//                img_background,
//                delegate.imgDefault);

//        View rootView = getWindow().getDecorView().getRootView();
//        Bitmap imageBitmap = delegate.readImageFileOnSD(delegate.project.getBackgroundUrl(),0, 0);
//        Drawable imageDraw =  new BitmapDrawable(imageBitmap);
//        rootView.setBackground(imageDraw);



        View rootView = getWindow().getDecorView().getRootView();
        Bitmap imageBitmap = delegate.imageLoader.display2(delegate.project.getBackgroundUrl());
        if (imageBitmap == null) {
            imageBitmap = delegate.readImageFileOnSD(delegate.project.getBackgroundUrl(),0, 0);
        }
        Drawable imageDraw =  new BitmapDrawable(imageBitmap);
        rootView.setBackground(imageDraw);
    }

    public void setNavigator(){
        navigatorBar = (TextView) findViewById(R.id.navigatorBar);
        navigatorBar.setText(delegate.getTitleSequence());
        navigatorBar.setTypeface(delegate.font_type);
        navigatorBar.setTextSize(20);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display21);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        delegate = (questionniare_delegate)getApplicationContext();
        ctx = this;

        if(delegate.dataSubQuestion !=null){
            data = delegate.dataSubQuestion;
        } else{
            data = delegate.QM.get_question();
        }

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
                setObject();
                setImage();
                if(delegate.dataSubQuestion ==null){
                    setNavigator();
                } else {
                    navigatorBar = (TextView) findViewById(R.id.navigatorBar);
                    navigatorBar.setText("คำถามย่อย");
                }
            }
        };
        Runnable background = new Runnable() {
            @Override
            public void run() {

                if(!data.isParent_question() && data.getParent_question_id() < 0){//not have sub question && not is sub question
                    checkAnswer = delegate.QM.get_answer();
                    if(checkAnswer == null){
                        answer = delegate.getHistory();
                    }else{
                        answer = checkAnswer.getAnswer();
                    }
                    Log.e("Ans", answer.toString());
                }else {
                    //is parent question
                    if(data.getParent_question_id() > 0){
                        // is sub question
                        checkAnswer = delegate.QM.get_sub_answer(data.getQuestion().getId());
                    }else{
                        // is parent question
                        checkAnswer = delegate.QM.get_answer();
                    }

                    if(checkAnswer == null){
                        answer = (ArrayList<SaveAnswerData>) delegate.getHistory().clone();
                    } else {
                        answer = (ArrayList<SaveAnswerData>) checkAnswer.getAnswer().clone();
                    }
                }

                //delay
                try {
                    Thread.sleep(delegate.timesleep);
                }catch (Exception e){

                }
                uiHandler.post( onUi );
            }
        };
        new Thread( background ).start();
    }

    private void setObject(){
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        project_name = (TextView) findViewById(R.id.project_name);
        project_name.setText(delegate.project.getName());
        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);

        txt_question = (TextView) findViewById(R.id.lbl_question);
        txt_question.setText(data.getQuestion().getTitle());
        txt_question.setTextSize(35);
        txt_question.setTypeface(delegate.font_type);
        txt_question.setPadding(0, delegate.dpToPx(20), 0, delegate.dpToPx(20));

        txtbox = (EditText) findViewById(R.id.txtbox);
        txtbox.setTypeface(delegate.font_type);
        txtbox.setTextSize(30);

        if(answer.size()!=0){
            if(!answer.get(0).getFreetxt().equals("")){
                txtbox.setText(answer.get(0).getFreetxt());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display21, menu);
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
            btnNext.setEnabled(false);
            answer.clear();
            SaveAnswerData _ans = new SaveAnswerData(String.valueOf(data.getAnswers().get(0).getId()), txtbox.getText().toString());
            answer.add(_ans);
            if(delegate.dataSubQuestion !=null){
                //sub question mode
                delegate.RemoveQuestionHistory(delegate.dataSubQuestion.getQuestion().getId().toString());
                delegate.QM.save_answer(answer, delegate.dataSubQuestion.getQuestion().getId());

                delegate.skip_save_subans = false;
                onBackPressed();
            } else {
                //normal mode
                nextPage();
            }
            btnNext.setEnabled(true);
        } else if (v.getId() == R.id.btnBack){
            if(delegate.dataSubQuestion !=null) {
                delegate.skip_save_subans = true;
            }
            onBackPressed();
        }
    }

    public void nextPage(){
        delegate.QM.save_answer(answer);
        //startActivityForResult(delegate.nextPage(this),0);
        delegate.nextQuestionPage(delegate.nextPage(this));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode ==2 || resultCode == 0|| resultCode == 1){
            this.setResult(resultCode);
            finish();
        }
    }

    public void onBackPressed() {
        if(delegate.checkPressBack(answer)){
            delegate.backQuestionpage(this);
        }else{
            Toast.makeText(this, R.string.cannot_back, Toast.LENGTH_SHORT).show();
        }
    }

}
