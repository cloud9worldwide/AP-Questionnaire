package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud9worldwide.questionnaire.data.AnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionAnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionTypeData;
import com.cloud9worldwide.questionnaire.data.SaveAnswerData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Display15Activity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total;
    Button btn_settings, btn_home;
    LinearLayout linearLayout, content_view;
    QuestionTypeData data;
    questionniare_delegate delegate;
    TextView project_name, txt_question, txtResult;
    int selected =0;
    ArrayList<SaveAnswerData> answer;
    ImageButton btnNext,btn_plus,btn_minus, btnBack, btnEN, btnTH;
    static PopupWindow popup;
    ImageView img_background,img_question;
    ArrayList<String> choich;
    int indexChoich;

    TextView navigatorBar;
    TextView txt_process;
    Drawable thumb;
    RelativeLayout footer;

    private Context ctx;
    private QuestionAnswerData checkAnswer = null;

    private void setImage(){
        img_background = (ImageView) findViewById(R.id.img_background);
        delegate.imageLoader.display(delegate.project.getBackgroundUrl(),
                String.valueOf(img_background.getWidth()),
                String.valueOf(img_background.getHeight()),
                img_background,
                delegate.imgDefault);

        img_question = (ImageView) findViewById(R.id.img_question);
        img_question.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if(data.getQuestion().getImageUrl().length()==0 || delegate.readImageFileOnSDFileName(data.getQuestion().getImageUrl())==null){
            img_question.setImageResource(delegate.imgDefaultQuestion);
        } else {
            img_question.setImageURI(delegate.readImageFileOnSDFileName(data.getQuestion().getImageUrl()));
        }
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
        setContentView(R.layout.activity_display15);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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

        total = data.getAnswers().size();

        txt_question = (TextView) findViewById(R.id.lbl_question);
        txt_question.setText(data.getQuestion().getTitle());
        txt_question.setTextSize(35);
        txt_question.setTypeface(delegate.font_type);
        txt_question.setPadding(0, delegate.dpToPx(20), 0, delegate.dpToPx(20));

        txtResult = (TextView) findViewById(R.id.txtResult);
        txtResult.setTextSize(30);
        txtResult.setTypeface(delegate.font_type);

        btn_plus = (ImageButton) findViewById(R.id.btn_plus);
        btn_plus.setOnClickListener(this);
        btn_minus = (ImageButton) findViewById(R.id.btn_minus);
        btn_minus.setOnClickListener(this);

        choich = new ArrayList<String>();

        indexChoich = -1;

        for(int i =0; i < total; i++) {
            if(answer.size() ==1){
                if(Integer.parseInt(answer.get(0).getValue()) == data.getAnswers().get(i).getId()){
                    indexChoich = i;
                }
            }
            choich.add(data.getAnswers().get(i).getTitle().toString());
        }

        if(indexChoich == -1){
            txtResult.setText(getResources().getString(R.string.default_display_14_15));
        } else {
            txtResult.setText(choich.get(indexChoich));
        }

        if(indexChoich <= 0){
            btn_minus.setEnabled(false);
            btn_minus.setImageResource(R.drawable.btn_minus_no_active);
        } else {
            btn_minus.setEnabled(true);
            btn_minus.setImageResource(R.drawable.btn_minus);
        }
        if(indexChoich==total-1){
            btn_plus.setEnabled(false);
            btn_plus.setImageResource(R.drawable.btn_plus_no_active);
        } else {
            btn_plus.setEnabled(true);
            btn_plus.setImageResource(R.drawable.btn_plus);
        }


        txtResult.setTextSize(30);
        txtResult.setTypeface(delegate.font_type);

        LinearLayout layoutprogressBar = (LinearLayout) findViewById(R.id.layoutprogressBar);
        layoutprogressBar.setVisibility(View.GONE);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        LinearLayout layoutReal = (LinearLayout) findViewById(R.id.layoutReal);
        layoutReal.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display15, menu);
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
            SaveAnswerData _ans;
            if(indexChoich ==-1){
                _ans = new SaveAnswerData("-1",null);
            } else {
                AnswerData selected = data.getAnswers().get(indexChoich);
                _ans = new SaveAnswerData(String.valueOf(selected.getId()),null);
            }
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
        } else if (v.getId() == R.id.btn_minus){
            if(indexChoich!=0){
                indexChoich--;
            }
            txtResult.setText(choich.get(indexChoich));
            if(indexChoich==0){
                btn_minus.setEnabled(false);
                btn_minus.setImageResource(R.drawable.btn_minus_no_active);
            }
            btn_plus.setEnabled(true);
            btn_plus.setImageResource(R.drawable.btn_plus);
        } else if (v.getId() == R.id.btn_plus){
            indexChoich++;
            txtResult.setText(choich.get(indexChoich));
            if(indexChoich==total-1){
                btn_plus.setEnabled(false);
                btn_plus.setImageResource(R.drawable.btn_plus_no_active);
            }
            if(indexChoich !=0){
                btn_minus.setEnabled(true);
                btn_minus.setImageResource(R.drawable.btn_minus);
            }
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
            Toast.makeText(this,R.string.cannot_back, Toast.LENGTH_SHORT).show();
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
        }
    }
}
