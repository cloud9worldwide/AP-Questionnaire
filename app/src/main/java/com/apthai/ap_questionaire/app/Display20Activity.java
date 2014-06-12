package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud9worldwide.questionnaire.data.AnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionAnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionTypeData;
import com.cloud9worldwide.questionnaire.data.SaveAnswerData;

import java.util.ArrayList;


public class Display20Activity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total;
    Button btn_settings, btn_home;
    LinearLayout content_view;
    QuestionTypeData data;
    questionniare_delegate delegate;
    TextView question_title, project_name, txt_question;
    int selected =0;
    ArrayList<SaveAnswerData> answer;
    ImageButton btnNext, btn_left, btn_right, btnBack;
    static PopupWindow popup;
    ImageView img_background;
    int pictureWidth, pictureHeight;

    HorizontalScrollView scrollPicture;
    int maxScrollX;

    SeekBar navigatorBar;
    TextView txt_process;
    Drawable thumb;
    RelativeLayout footer;

    private Context ctx;
    private QuestionAnswerData checkAnswer = null;


    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            if(delegate ==null){
                setImage();
            }
        }
    }

    private void setImage(){
        delegate = (questionniare_delegate)getApplicationContext();

        img_background = (ImageView) findViewById(R.id.img_background);
        delegate.imageLoader.display(delegate.project.getBackgroundUrl(),
                String.valueOf(img_background.getWidth()),
                String.valueOf(img_background.getHeight()),
                img_background,
                delegate.imgDefault);

        /*
        setObject();
        setPicture();

        if(delegate.dataSubQuestion ==null){
            setNavigator();
        } else {
            question_title.setText("คำถามย่อย");
            navigatorBar = (SeekBar) findViewById(R.id.navigatorBar);
            navigatorBar.setVisibility(View.GONE);
        }
        */
    }
    public void setNavigator(){
        navigatorBar = (SeekBar) findViewById(R.id.navigatorBar);
        navigatorBar.setMax(delegate.getMax());
        navigatorBar.setProgress(0);
        navigatorBar.setProgress(delegate.getProcessed());
        navigatorBar.setEnabled(false);
        navigatorBar.setVisibility(View.VISIBLE);
        thumb = getResources().getDrawable(R.drawable.icon_navigator);
        thumb.setBounds(new Rect(0,0, thumb.getIntrinsicWidth(),thumb.getIntrinsicHeight()));
        navigatorBar.setThumb(thumb);

        txt_process = new TextView(this);
        txt_process.setText(delegate.getPercent());

        txt_process.setWidth(thumb.getIntrinsicWidth());
        txt_process.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins((thumb.getBounds().left + (int) navigatorBar.getX()) -8 , (int)navigatorBar.getY()+5, 0, 0);
        txt_process.setLayoutParams(params);
        footer = (RelativeLayout) findViewById(R.id.footer);
        footer.addView(txt_process);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display20);

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
                setPicture();
                if(delegate.dataSubQuestion ==null){
                    setNavigator();
                } else {
                    question_title.setText("คำถามย่อย");
                    navigatorBar = (SeekBar) findViewById(R.id.navigatorBar);
                    navigatorBar.setVisibility(View.GONE);
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
                        answer = delegate.getHistory();
                    }else{
                        answer = checkAnswer.getAnswer();
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
        /*
        data = delegate.QM.get_question();
        QuestionAnswerData checkAnswer;
        checkAnswer = delegate.QM.get_sub_answer(data.getQuestion().getId());
        if(checkAnswer==null){
            answer = delegate.getHistory();
        } else {
            answer = checkAnswer.getAnswer();
        }
        */
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        question_title = (TextView) findViewById(R.id.question_title);
        question_title.setText(delegate.getTitleSequence());
        question_title.setTextSize(20);
        question_title.setTypeface(delegate.font_type);

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
        txt_question.setPadding(0, delegate.pxToDp(20), 0, delegate.pxToDp(20));

        btn_left = (ImageButton) findViewById(R.id.btnLeft);
//        btn_left.setOnClickListener(this);
        btn_right = (ImageButton) findViewById(R.id.btnRight);
//        btn_right.setOnClickListener(this);

        content_view = (LinearLayout) findViewById(R.id.picture);
        content_view.removeAllViews();

        pictureWidth = delegate.pxToDp(250);
        pictureHeight =delegate.pxToDp(250);

        scrollPicture = (HorizontalScrollView) findViewById(R.id.scrollPicture);
//        scrollPicture.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                // TODO Auto-generated method stub
//
//                int scrollX = view.getScrollX();
//                int scrollY = view.getScrollY();
//
//                Log.e(TAG, scrollX +"");
//                Log.e(TAG, maxScrollX+"");
//
//                if(scrollX ==0){
//                    btn_left.setVisibility(View.INVISIBLE);
//                } else {
//                    btn_left.setVisibility(View.VISIBLE);
//                }
//
//                if(scrollX == maxScrollX){
//                    Log.e("MaxRight", "YES");
//                } else {
//                    Log.e("MaxRight", "NO");
//
//                }
//
//                return false;
//            }
//
//        });
    }


    private void setPicture() {
        for(int i =0; i < total; i++){
            LinearLayout btn = new LinearLayout(this);
            btn.setOrientation(LinearLayout.HORIZONTAL);
            btn.setGravity(Gravity.CENTER);

            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            btn.setTag(i);

            btn.setOnClickListener(this);
            boolean isSelected = false;

            for(int j=0;j<answer.size();j++) {
                if(Integer.parseInt(answer.get(j).getValue()) == data.getAnswers().get(i).getId()){
                    isSelected = true;
                }
            }

            if(data.getAnswers().get(i).getIconActiveUrl().length()==0){
                image.setImageResource(delegate.imgDefaultQuestion);
            } else {
                image.setImageURI(delegate.readImageFileOnSDFileName(data.getAnswers().get(i).getImageUrl()));
            }

            if(isSelected){
                btn.setBackgroundColor(Color.rgb(247,156,49));
            } else {
                btn.setBackgroundColor(Color.TRANSPARENT);
            }

            btn.addView(image);
            btn.setPadding(delegate.pxToDp(10),delegate.pxToDp(10),delegate.pxToDp(10),delegate.pxToDp(10));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pictureWidth, pictureHeight);
            lp.setMargins(delegate.pxToDp(20), delegate.pxToDp(10), delegate.pxToDp(20), delegate.pxToDp(10));
            btn.setLayoutParams(lp);
            content_view.addView(btn);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display20, menu);
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
            if(delegate.dataSubQuestion !=null){
                //sub question mode
                if(answer.size()!=0){
                    delegate.QM.save_answer(answer, delegate.dataSubQuestion.getQuestion().getId());
                    delegate.dataSubQuestion = null;
                }
                this.setResult(3);
                finish();
            } else {
                //normal mode
                nextPage();
            }
            btnNext.setEnabled(true);
        } else if (v.getId() == R.id.btnBack){
            onBackPressed();
        } else {
            LinearLayout btn = (LinearLayout) v;
            int indexSelected =Integer.parseInt(v.getTag().toString());
            AnswerData selected = data.getAnswers().get(indexSelected);

            if(answer.size()>0){
                boolean isSeleted = true;
                int index=0;
                for (int j=0;j<answer.size();j++) {
                    if(selected.getId() == Integer.parseInt(answer.get(j).getValue())) {
                        isSeleted = false;
                        index = j;
                    }
                }
                if(isSeleted){
                    btn.setBackgroundColor(Color.rgb(247, 156, 49));
                    SaveAnswerData _ans = new SaveAnswerData(String.valueOf(selected.getId()),null);
                    answer.add(_ans);
                } else {
                    btn.setBackgroundColor(Color.TRANSPARENT);
                    answer.remove(index);
                }
            } else {
                btn.setBackgroundColor(Color.rgb(247,156,49));
                SaveAnswerData _ans = new SaveAnswerData(String.valueOf(selected.getId()),null);
                answer.add(_ans);
            }
        }
    }

    public void nextPage(){
        delegate.QM.save_answer(answer);
        startActivityForResult(delegate.nextPage(this),0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode ==2 || resultCode == 0|| resultCode == 1){
            this.setResult(resultCode);
            finish();
        }
    }

    public void onBackPressed() {
        if(delegate.dataSubQuestion ==null){
            if(delegate.QM.move_back()){
                this.setResult(3);
                finish();
            } else {
                Toast.makeText(this, "Cannot Back", Toast.LENGTH_LONG).show();
            }
        } else {
            // back sub question
            this.setResult(3);
            finish();
        }
    }

}
