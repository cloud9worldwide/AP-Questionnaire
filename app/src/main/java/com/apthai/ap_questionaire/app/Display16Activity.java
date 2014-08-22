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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud9worldwide.questionnaire.data.AnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionAnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionTypeData;
import com.cloud9worldwide.questionnaire.data.SaveAnswerData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Display16Activity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total;
    Button btn_settings, btn_home;
    LinearLayout linearLayout, content_view;
    QuestionTypeData data;
    questionniare_delegate delegate;
    TextView project_name, txt_question, txtResult;
    int selected =0;
    ArrayList<SaveAnswerData> answer;
    ImageButton btnNext,btn_plus,btn_minus,btnBack, btnEN, btnTH;
    static PopupWindow popup;
    ImageView img_background;
    ArrayList<String> choich;
    int indexChoich;
    SeekBar mySeekBar;
    TextView lbl_min, lbl_middle, lbl_max;

    TextView navigatorBar;
    TextView txt_process;
    Drawable thumb;
    RelativeLayout footer;

    LinearLayout pack;
    ProgressBar progressBar;

    private Context ctx;
    private QuestionAnswerData checkAnswer = null;

    private void setImage(){
        img_background = (ImageView) findViewById(R.id.img_background);
        delegate.imageLoader.display(delegate.project.getBackgroundUrl(),
                String.valueOf(img_background.getWidth()),
                String.valueOf(img_background.getHeight()),
                img_background,
                delegate.imgDefault);

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
        setContentView(R.layout.activity_display16);
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
        pack = (LinearLayout) findViewById(R.id.pack);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

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

        choich = new ArrayList<String>();

        int middle = total/2;

        indexChoich = -1;

        if(answer.size()==0){
            txtResult.setText(getResources().getString(R.string.default_display_16));
            for(int i =0; i < total; i++) {
                choich.add(data.getAnswers().get(i).getTitle().toString());            }
        } else {
            for(int i =0; i < total; i++) {
                if(answer.size() ==1 && !answer.get(0).getValue().equals("-1")){
                    if(Integer.parseInt(answer.get(0).getValue()) == data.getAnswers().get(i).getId()){
                        indexChoich = i;
                    }
                }
                choich.add(data.getAnswers().get(i).getTitle().toString());
            }
            if(indexChoich == -1){
                txtResult.setText(getResources().getString(R.string.default_display_16));
            } else {
                txtResult.setText(choich.get(indexChoich));
            }

        }

        lbl_min = (TextView) findViewById(R.id.min_rank);
        lbl_middle = (TextView) findViewById(R.id.middle_rank);
        lbl_max = (TextView) findViewById(R.id.max_rank);
        lbl_min.setText(choich.get(0));
        lbl_middle.setText(choich.get(middle));
        lbl_max.setText(choich.get(total-1));

        lbl_min.setTextSize(25);
        lbl_min.setTypeface(delegate.font_type);
        lbl_middle.setTextSize(25);
        lbl_middle.setTypeface(delegate.font_type);
        lbl_max.setTextSize(25);
        lbl_max.setTypeface(delegate.font_type);

        mySeekBar = (SeekBar) findViewById(R.id.seekBar);
        mySeekBar.setMax(total-1);
        mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                indexChoich = progress;
                txtResult.setText(choich.get(progress));
            }
        });
        if(indexChoich !=-1){
            mySeekBar.setProgress(indexChoich);
        }


        pack.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display16, menu);
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
            btn_minus.setEnabled(true);
            btn_minus.setImageResource(R.drawable.btn_minus);
        }
    }

    public void nextPage(){
        delegate.QM.save_answer(answer);
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
