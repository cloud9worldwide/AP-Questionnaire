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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud9worldwide.questionnaire.data.AnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionAnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionTypeData;
import com.cloud9worldwide.questionnaire.data.SaveAnswerData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Display18Activity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total;
    Button btn_settings, btn_home;
    LinearLayout linearLayout, content_view;
    QuestionTypeData data;
    questionniare_delegate delegate;
    TextView project_name, txt_question;
    int selected =0;
    ArrayList<SaveAnswerData> answer = new ArrayList<SaveAnswerData>();
    ImageButton btnNext, btnBack, btnEN, btnTH;
    static PopupWindow popup;
    ImageView img_background;

    TextView navigatorBar;
    TextView txt_process;
    Drawable thumb;
    RelativeLayout footer;

    boolean isParent;

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
        setContentView(R.layout.activity_display18);
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
                isParent = delegate.QM.get_question().isParent_question();
                setObject();
                setTableLayout();
                setImage();
                if(delegate.dataSubQuestion == null){
                    setNavigator();
                } else {
                    isParent = delegate.dataSubQuestion.isParent_question();
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

        content_view = (LinearLayout)this.findViewById(R.id.AP_content);
        content_view.removeAllViews();
    }

    private void setTableLayout(){
        linearLayout = new LinearLayout(this);
        int column =4;
        for(int i =0, c = 0; i < total; i++, c++){
            if(c == column){
                c = 0;
                content_view.addView(linearLayout);
                linearLayout = new LinearLayout(this);
            }
            LinearLayout btn = new LinearLayout(this);
            btn.setOrientation(LinearLayout.VERTICAL);

            ImageView image = new ImageView(this);
            image.setTag(99);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(delegate.sizeImage,delegate.sizeImage);
            imageLayout.gravity = Gravity.CENTER;
            image.setLayoutParams(imageLayout);

            btn.setTag(i);
            btn.setOnClickListener(this);
            boolean isSelected = false;

            Log.e(TAG, "answer : "+ answer.toString());

            for(int j=0;j<answer.size();j++) {
                if(Integer.parseInt(answer.get(j).getValue()) == data.getAnswers().get(i).getId()){
                    isSelected = true;
                }
            }

            if(isSelected){
                if(data.getAnswers().get(i).getIconActiveUrl().length()==0 || delegate.readImageFileOnSDFileName(data.getAnswers().get(i).getIconActiveUrl())==null){
                    image.setImageResource(delegate.imgDefaultIconSelect);
                } else {
                    image.setImageURI(delegate.readImageFileOnSDFileName(data.getAnswers().get(i).getIconActiveUrl()));
                }
            } else {
                if(data.getAnswers().get(i).getIconInActiveUrl().length()==0 || delegate.readImageFileOnSDFileName(data.getAnswers().get(i).getIconInActiveUrl())==null){
                    image.setImageResource(delegate.imgDefaultIcon);
                } else {
                    Log.e("image url",data.getAnswers().get(i).getIconInActiveUrl());
                    image.setImageURI(delegate.readImageFileOnSDFileName(data.getAnswers().get(i).getIconInActiveUrl()));
                }
            }

            TextView name = new TextView(this);
            name.setText(data.getAnswers().get(i).getTitle().toString());
            name.setTypeface(delegate.font_type);
            name.setPadding(0, delegate.dpToPx(20), 0, delegate.dpToPx(20));
            name.setGravity(Gravity.CENTER);
            name.setTextSize(25);
            name.setTag(98);

            btn.addView(image);
            name.setHeight(delegate.heightDescriptionUnderImage);
            btn.addView(name);
            LinearLayout.LayoutParams lp;
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            lp.setMargins(delegate.dpToPx(10), delegate.dpToPx(10), delegate.dpToPx(10), delegate.dpToPx(10));

            btn.setLayoutParams(lp);

            linearLayout.addView(btn);
        }
        content_view.addView(linearLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display18, menu);
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
                delegate.RemoveQuestionHistory(delegate.dataSubQuestion.getQuestion().getId().toString());
                delegate.QM.save_answer(answer, delegate.dataSubQuestion.getQuestion().getId());

                delegate.skip_save_subans = false;
                onBackPressed();
            } else {
                //normal mode
                delegate.dataSubQuestion = null;
                nextPage();
            }
            btnNext.setEnabled(true);
        } else if (v.getId() == R.id.btnBack){

            if(delegate.dataSubQuestion !=null) {
                delegate.skip_save_subans = true;
            }

            onBackPressed();
        } else {

            //delegate.QM.save_answer(answer);
            LinearLayout btn = (LinearLayout) v;
            int count = btn.getChildCount();
            View obj = null;
            for(int i=0; i<count; i++) {
                obj = btn.getChildAt(i);
                int indexSelected =Integer.parseInt(v.getTag().toString());
                String tag = obj.getTag().toString();
                if(tag.equals("99")){
                    AnswerData selected = data.getAnswers().get(indexSelected);
                    ImageView image = (ImageView) obj;
                    if(answer.size()>0){
                        boolean isSeleted = true;
                        int index=0;
                        for(int j=0;j<answer.size();j++){
                            if(selected.getId() == Integer.parseInt(answer.get(j).getValue())){
                                isSeleted = false;
                                index = j;
                            }
                        }
                        if(isSeleted){
                            if(data.getAnswers().get(indexSelected).getIconActiveUrl().length()==0 || delegate.readImageFileOnSDFileName(data.getAnswers().get(indexSelected).getIconActiveUrl())==null){
                                image.setImageResource(delegate.imgDefaultIconSelect);
                            } else {
                                image.setImageURI(delegate.readImageFileOnSDFileName(data.getAnswers().get(indexSelected).getIconActiveUrl()));
                            }
                            SaveAnswerData _ans = new SaveAnswerData(String.valueOf(selected.getId()),null);
                            //answer.add(_ans);
                            if(isParent){
                                parentSelected(indexSelected);
                            }else{
                                answer.add(_ans);
                            }
                        } else {
                            if(data.getAnswers().get(indexSelected).getIconInActiveUrl().length()==0 || delegate.readImageFileOnSDFileName(data.getAnswers().get(indexSelected).getIconInActiveUrl())==null){
                                image.setImageResource(delegate.imgDefaultIcon);
                            } else {
                                image.setImageURI(delegate.readImageFileOnSDFileName(data.getAnswers().get(indexSelected).getIconInActiveUrl()));
                            }

                            //answer.remove(index);
                            if(data.getParent_question_id() > 0){
                                //is sub question, will save @ save_answer(answers, quesiton inx)


//                                delegate.RemoveQuestionHistory(data.getQuestion().getId().toString());
//                                delegate.QM.save_answer(answer,data.getQuestion().getId());
                            }else{
                                // not is sub question
                                delegate.RemoveQuestionHistory(data.getQuestion().getId().toString());
                                delegate.QM.save_answer(answer);
                            }


                            if(isParent){
                                parentSelected(indexSelected);
                            }else{
                                answer.remove(index);
                            }

                        }
                    } else {
                        if(data.getAnswers().get(i).getIconActiveUrl().length()==0 || delegate.readImageFileOnSDFileName(data.getAnswers().get(indexSelected).getIconActiveUrl())==null){
                            image.setImageResource(delegate.imgDefaultIconSelect);
                        } else {
                            image.setImageURI(delegate.readImageFileOnSDFileName(data.getAnswers().get(indexSelected).getIconActiveUrl()));
                        }
                        SaveAnswerData _ans = new SaveAnswerData(String.valueOf(selected.getId()),null);
                        //answer.add(_ans);
                        if(isParent){
                            parentSelected(indexSelected);
                        }else{
                            answer.add(_ans);
                        }
                    }
                }
            }
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
        } else {
            delegate.showAlert(this, getString(R.string.cannot_back), getString(R.string.alert_warning));
        }
    }

    public void parentSelected(int indexSelected){

        QuestionTypeData dataSubQuestion =  delegate.QM.get_sub_question(data.getAnswers().get(indexSelected).getId());
        delegate.QM.save_answer(answer);
        delegate.dataSubQuestion = dataSubQuestion;
        Intent newPage = new Intent();

            if(dataSubQuestion.getQuestionType().equals("1")){
                newPage = new Intent(this, Display01Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("2")){
                newPage = new Intent(this, Display02Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("3")){
                newPage = new Intent(this, Display03Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("4")){
                newPage = new Intent(this, Display04Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("5")){
                newPage = new Intent(this, Display05Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("6")){
                newPage = new Intent(this, Display06Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("7")){
                newPage = new Intent(this, Display07Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("8")){
                newPage = new Intent(this, Display08Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("9")){
                newPage = new Intent(this, Display09Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("10")){
                newPage = new Intent(this, Display10Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("11")){
                newPage = new Intent(this, Display11Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("12")){
                newPage = new Intent(this, Display12Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("13")){
                newPage = new Intent(this, Display13Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("14")){
                newPage = new Intent(this, Display14Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("15")){
                newPage = new Intent(this, Display15Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("16")){
                newPage = new Intent(this, Display16Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("17")){
                newPage = new Intent(this, Display17Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("18")){
                newPage = new Intent(this, Display18Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("19")){
                newPage = new Intent(this, Display19Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("20")){
                newPage = new Intent(this, Display20Activity.class);
            } else if(dataSubQuestion.getQuestionType().equals("21")){
                newPage = new Intent(this, Display21Activity.class);
            }
            delegate.nextQuestionPage(newPage);
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
