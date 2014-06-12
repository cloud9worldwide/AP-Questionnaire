package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class Display03Activity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total;
    Button btn_settings, btn_home;
    LinearLayout linearLayout, content_view;
    QuestionTypeData data;
    questionniare_delegate delegate;
    TextView question_title,project_name;
    int selected =0;
    ArrayList<SaveAnswerData> answer;
    ImageButton btnNext,  btnBack;
    static PopupWindow popup;
    ImageView img_background;

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

        //setObject();
        //setTableLayout();
        /*
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
        setContentView(R.layout.activity_display03);

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
                setTableLayout();

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
                    Log.e("Ans",answer.toString());
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
        //data = delegate.QM.get_question();
        //Log.e("answer", data.getAnswers().toString());

        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        content_view = (LinearLayout)this.findViewById(R.id.AP_content);
        content_view.removeAllViews();

        question_title = (TextView) findViewById(R.id.question_title);
        question_title.setText(delegate.getTitleSequence());
        question_title.setTypeface(delegate.font_type);
        question_title.setTextSize(20);

        project_name = (TextView) findViewById(R.id.project_name);
        project_name.setText(delegate.project.getName());
        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);

        total = data.getAnswers().size();


        /*
        QuestionAnswerData checkAnswer;
        checkAnswer = delegate.QM.get_sub_answer(data.getQuestion().getId());
        if(checkAnswer==null){
            answer = delegate.getHistory();
        } else {
            answer = checkAnswer.getAnswer();
        }
        */
    }
    private void setTableLayout(){
        //set question
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        TextView question = new TextView(this);
        question.setText(data.getQuestion().getTitle());
        question.setTextSize(35);
        question.setTypeface(delegate.font_type);
        question.setPadding(0,delegate.pxToDp(20),0,delegate.pxToDp(20));
        question.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));
        linearLayout.addView(question);
        content_view.addView(linearLayout);

        linearLayout = new LinearLayout(this);

        int column =3;

        for(int i =0, c = 0, r = 0; i < total; i++, c++){
            if(c == column){
                c = 0;
                r++;
                content_view.addView(linearLayout);
                linearLayout = new LinearLayout(this);
            }

            LinearLayout.LayoutParams lp;

            LinearLayout btn = new LinearLayout(this);
            btn.setOrientation(LinearLayout.HORIZONTAL);

            final ImageView image = new ImageView(this);
            image.setTag(99);
            lp = new LinearLayout.LayoutParams(delegate.pxToDp(50), delegate.pxToDp(50));
            image.setLayoutParams(lp);
            btn.setTag(i);

            btn.setOnClickListener(this);
            boolean isSelected = false;
            String getFreeText = "";
            for(int j=0;j<answer.size();j++) {
                if(Integer.parseInt(answer.get(j).getValue()) == data.getAnswers().get(i).getId()){
                    isSelected = true;
                    getFreeText = answer.get(j).getFreetxt();
                }
            }
            if(isSelected){
                image.setImageResource(R.drawable.checkbox_selected);
            } else {
                image.setImageResource(R.drawable.checkbox_unselect);
            }

            TextView name = new TextView(this);
            name.setText(data.getAnswers().get(i).getTitle().toString());
            name.setTextSize(30);
            name.setTypeface(delegate.font_type);
            name.setPadding(delegate.pxToDp(20), 0, 0, 0);
            name.setTag(98);
            name.setGravity(Gravity.CENTER_VERTICAL);

            btn.addView(image);
            name.setHeight(delegate.pxToDp(45));
            btn.addView(name);

            if(data.getAnswers().get(i).getIsFreeTxt()){
                final EditText addEdit = new EditText(this);
                final int indexAnswer = i;
                addEdit.setPadding(delegate.pxToDp(20), 0, 0, 0);
                addEdit.setTag(97);

                if(getFreeText.length()>0){
                    addEdit.setText(getFreeText);
                }
                addEdit.setTypeface(delegate.font_type);
                addEdit.setBackgroundResource(R.drawable.box_login);
                addEdit.setTextSize(25);
                addEdit.setSingleLine();
                addEdit.setHint(R.string.Please_enter_txtbox_in_question);
                addEdit.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {}
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.length() != 0) {
                            image.setImageResource(R.drawable.checkbox_selected);
                            AnswerData selected = data.getAnswers().get(indexAnswer);
                            SaveAnswerData _ans = new SaveAnswerData(String.valueOf(selected.getId()) , addEdit.getText().toString());

                            boolean isSeleted = true;
                            int index=0;

                            for(int j=0;j<answer.size();j++){
                                Log.e(TAG, "selected : " + selected.getId() + ", " +answer.get(j).getValue());
                                if(selected.getId() == Integer.parseInt(answer.get(j).getValue())){
                                    isSeleted = false;
                                    index = j;
                                }
                            }
                            if(isSeleted) {
                                answer.add(_ans);
                            } else {
                                answer.set(index,_ans);
                            }
                        }
                    }
                });
                btn.addView(addEdit);
            }
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, delegate.pxToDp(50));
            lp.gravity = Gravity.CENTER_VERTICAL;

            lp.weight = 1;
            lp.setMargins(delegate.pxToDp(20), delegate.pxToDp(10), 0, delegate.pxToDp(10));

            btn.setLayoutParams(lp);
            linearLayout.addView(btn);
            if(i == total-1 && (total%column ==2||total%column==3)){
                if(total%column==2){
                    btn = new LinearLayout(this);
                    lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    lp.weight = 1;
                    lp.setMargins(delegate.pxToDp(20), delegate.pxToDp(10), delegate.pxToDp(20), delegate.pxToDp(10));
                    btn.setLayoutParams(lp);
                    linearLayout.addView(btn);
                } else {
                    btn = new LinearLayout(this);
                    lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    lp.weight = 1;
                    lp.setMargins(delegate.pxToDp(20), delegate.pxToDp(10), delegate.pxToDp(20), delegate.pxToDp(10));
                    btn.setLayoutParams(lp);
                    linearLayout.addView(btn);
                    btn = new LinearLayout(this);
                    lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    lp.weight = 1;
                    lp.setMargins(delegate.pxToDp(20), delegate.pxToDp(10), delegate.pxToDp(20), delegate.pxToDp(10));
                    btn.setLayoutParams(lp);
                    linearLayout.addView(btn);
                }
            }
        }
        content_view.addView(linearLayout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display03, menu);
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
            seletedAnswer(v, "");
        }
    }
    public void seletedAnswer(View parent, String freeText){
        LinearLayout btn = (LinearLayout) parent;
        int count = btn.getChildCount();
        View obj = null;
        for(int i=0; i<count; i++) {
            obj = btn.getChildAt(i);
            int indexSelected =Integer.parseInt(parent.getTag().toString());
            Log.e(TAG, "index : " + indexSelected);
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
                        image.setImageResource(R.drawable.checkbox_selected);
                        SaveAnswerData _ans = new SaveAnswerData(String.valueOf(selected.getId()),freeText);
                        answer.add(_ans);
                    } else {
                        image.setImageResource(R.drawable.checkbox_unselect);
                        answer.remove(index);
                    }
                } else {
                    image.setImageResource(R.drawable.checkbox_selected);
                    SaveAnswerData _ans = new SaveAnswerData(String.valueOf(selected.getId()),null);
                    answer.add(_ans);
                }
            }
        }
    }

    public void nextPage(){
        delegate.QM.save_answer(answer);
        startActivityForResult(delegate.nextPage(this),0);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode ==2 || resultCode == 0|| resultCode == 1){
            this.setResult(resultCode);
            finish();
        }
    }

    @Override
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
