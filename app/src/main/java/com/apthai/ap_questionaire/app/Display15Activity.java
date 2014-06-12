package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;


public class Display15Activity extends Activity implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total;
    Button btn_settings, btn_home;
    LinearLayout linearLayout, content_view;
    QuestionTypeData data;
    questionniare_delegate delegate;
    TextView question_title, project_name, txt_question, txtResult;
    int selected =0;
    ArrayList<SaveAnswerData> answer;
    ImageButton btnNext,btn_plus,btn_minus, btnBack;
    static PopupWindow popup;
    ImageView img_background,img_question;
    ArrayList<String> choich;
    int indexChoich;

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
        img_question = (ImageView) findViewById(R.id.img_question);

        img_question.setImageURI(delegate.readImageFileOnSDFileName(data.getQuestion().getImageUrl()));

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
        setContentView(R.layout.activity_display15);
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
                //setTableLayout();
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
        question_title.setTypeface(delegate.font_type);
        question_title.setTextSize(20);

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

        txtResult = (TextView) findViewById(R.id.txtResult);
        txtResult.setTextSize(30);
        txtResult.setTypeface(delegate.font_type);

        btn_plus = (ImageButton) findViewById(R.id.btn_plus);
        btn_plus.setOnClickListener(this);
        btn_minus = (ImageButton) findViewById(R.id.btn_minus);
        btn_minus.setOnClickListener(this);

        choich = new ArrayList<String>();

        indexChoich = 0;

        for(int i =0; i < total; i++) {
            if(answer.size() ==1){
                if(Integer.parseInt(answer.get(0).getValue()) == data.getAnswers().get(i).getId()){
                    indexChoich = i;
                }
            }
            choich.add(data.getAnswers().get(i).getTitle().toString());
        }

        txtResult.setText(choich.get(indexChoich));
        if(indexChoich==0){
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
            AnswerData selected = data.getAnswers().get(indexChoich);
            SaveAnswerData _ans = new SaveAnswerData(String.valueOf(selected.getId()),null);
            answer.add(_ans);

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

    public void showPopup(final Activity context) {
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.activity_menu, viewGroup);
        popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(delegate.pxToDp(300));
        popup.setHeight(delegate.pxToDp(300));
        popup.setBackgroundDrawable(null);
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, delegate.pxToDp(40), delegate.pxToDp(120));
        View view_instance = (View)layout.findViewById(R.id.popup);
        final RelativeLayout home = (RelativeLayout) layout.findViewById(R.id.menu_home);
        final RelativeLayout settings = (RelativeLayout) layout.findViewById(R.id.menu_settings);
        final RelativeLayout logout = (RelativeLayout) layout.findViewById(R.id.menu_logout);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(getResources().getColor(R.color.ORANGE));
                settings.setBackgroundColor(getResources().getColor(R.color.WHITE));
                logout.setBackgroundColor(getResources().getColor(R.color.WHITE));
                setResult(0);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(getResources().getColor(R.color.WHITE));
                settings.setBackgroundColor(getResources().getColor(R.color.ORANGE));
                logout.setBackgroundColor(getResources().getColor(R.color.WHITE));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(getResources().getColor(R.color.WHITE));
                settings.setBackgroundColor(getResources().getColor(R.color.WHITE));
                logout.setBackgroundColor(getResources().getColor(R.color.ORANGE));
                delegate.service.Logout();
                setResult(2);
                finish();
            }
        });
    }

}
