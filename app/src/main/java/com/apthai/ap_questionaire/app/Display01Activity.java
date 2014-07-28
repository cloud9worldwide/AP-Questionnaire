package com.apthai.ap_questionaire.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud9worldwide.questionnaire.data.AnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionAnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionTypeData;
import com.cloud9worldwide.questionnaire.data.SaveAnswerData;

import java.util.ArrayList;
import java.util.Calendar;

public class Display01Activity extends Activity implements OnClickListener {

    final String TAG = this.getClass().getSimpleName();
    int total;
    Button btn_settings, btn_home;
    LinearLayout linearLayout, content_view;
    QuestionTypeData data;
    questionniare_delegate delegate;
    TextView project_name;
    ImageButton btn_back,btnNext;
    ArrayList<SaveAnswerData> answer;
    ImageView img_background;

    TextView navigatorBar;
    TextView txt_process;
    Drawable thumb;
    RelativeLayout footer;
    int mYear, mMonth, mDay;

    private Context ctx;
    private QuestionAnswerData checkAnswer = null;

    private void setImage(){
        View rootView = getWindow().getDecorView().getRootView();
        Bitmap imageBitmap = delegate.readImageFileOnSD(delegate.project.getBackgroundUrl(),0, 0);
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
        setContentView(R.layout.activity_display01);
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
                setTableLayout();
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

        btn_back = (ImageButton) findViewById(R.id.btnBack);
        btn_back.setOnClickListener(this);

        content_view = (LinearLayout)this.findViewById(R.id.AP_content);
        content_view.removeAllViews();

        project_name = (TextView) findViewById(R.id.project_name);
        project_name.setText(delegate.project.getName());
        project_name.setTextSize(30);
        project_name.setTypeface(delegate.font_type);
        project_name.setGravity(Gravity.CENTER);

        total = data.getAnswers().size();
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
        question.setPadding(0, delegate.dpToPx(20), 0, delegate.dpToPx(20));
        question.setGravity(Gravity.CENTER);
        question.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.1f));
        linearLayout.addView(question);
        content_view.addView(linearLayout);

        linearLayout = new LinearLayout(this);

        int column =1;

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

            lp = new LinearLayout.LayoutParams(delegate.dpToPx(50), delegate.dpToPx(50));
            lp.gravity =Gravity.CENTER_VERTICAL;

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
            name.setPadding(delegate.dpToPx(20), delegate.dpToPx(15), 0, 0);
            name.setTag(98);
            name.setGravity(Gravity.CENTER_VERTICAL);

            btn.addView(image);
            name.setHeight(delegate.dpToPx(45));
            btn.addView(name);

            if(data.getAnswers().get(i).getIsFreeTxt()){
                if(data.getAnswers().get(i).getFreeTxtType().length()!=0) {
                    int textType = Integer.parseInt(data.getAnswers().get(i).getFreeTxtType());
                    final int indexAnswer = i;
                    final TextView addDate;
                    final EditText addEdit;
                    addDate = new TextView (this);
                    addEdit = new EditText(this);
                    addDate.setTag(97);
                    addEdit.setTag(97);

                    int widthTextBox = delegate.dpToPx(240);
                    int heightTextBox = delegate.dpToPx(40);

                    if(textType ==4){
                        addDate.setPadding(delegate.dpToPx(15), 0, 0, 0);

                        if(getFreeText.length()>0){
                            addDate.setText(getFreeText);
                        }
                        addDate.setTypeface(delegate.font_type);
                        addDate.setGravity(Gravity.CENTER_VERTICAL);
                        addDate.setBackgroundResource(R.drawable.box_login);
                        addDate.setTextSize(25);
                        lp = new LinearLayout.LayoutParams(widthTextBox, heightTextBox);
                        addDate.setLayoutParams(lp);

                    } else {

                        addEdit.setPadding(delegate.dpToPx(15), 0, 0, 0);
                        addEdit.setTag(97);
                        if(getFreeText.length()>0){
                            addEdit.setText(getFreeText);
                        }

                        if(data.getAnswers().get(i).getFreeTxtType().length()!=0) {
                            int maxChar = Integer.parseInt(data.getAnswers().get(i).getFreeTxtMaxChar());
                            if (textType == 1) {
                                addEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                                if(maxChar !=0){
                                    InputFilter[] FilterArray = new InputFilter[1];
                                    FilterArray[0] = new InputFilter.LengthFilter(maxChar);
                                    addEdit.setFilters(FilterArray);
                                }
                            } else if (textType == 2) {
                                if(maxChar !=0){
                                    InputFilter[] FilterArray = new InputFilter[1];
                                    FilterArray[0] = new InputFilter.LengthFilter(maxChar);
                                    addEdit.setFilters(FilterArray);
                                }
                            } else if (textType == 3) {
                                addEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                            }

                            addEdit.setWidth(widthTextBox);
                            addEdit.setHeight(heightTextBox);
                            addEdit.setTypeface(delegate.font_type);
                            addEdit.setBackgroundResource(R.drawable.box_login);
                            addEdit.setTextSize(25);
                            addEdit.setSingleLine();
                            addEdit.setHint(R.string.Please_enter_txtbox_in_question);
                            if (textType != 4) {
                                addEdit.addTextChangedListener(new TextWatcher() {
                                    public void afterTextChanged(Editable s) {
                                    }

                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        AnswerData selected = data.getAnswers().get(indexAnswer);
                                        if (s.length() != 0) {
                                            image.setImageResource(R.drawable.checkbox_selected);
                                            SaveAnswerData _ans = new SaveAnswerData(String.valueOf(selected.getId()), addEdit.getText().toString());
                                            boolean isSeleted = true;
                                            int index = 0;

                                            for (int j = 0; j < answer.size(); j++) {
                                                Log.e(TAG, "selected : " + selected.getId() + ", " + answer.get(j).getValue());
                                                if (selected.getId() == Integer.parseInt(answer.get(j).getValue())) {
                                                    isSeleted = false;
                                                    index = j;
                                                }
                                            }
                                            if (isSeleted) {
                                                answer.add(_ans);
                                            } else {
                                                answer.set(index, _ans);
                                            }
                                        } else {
                                            boolean isSeleted = false;
                                            int index = 0;
                                            for (int j = 0; j < answer.size(); j++) {
                                                if (selected.getId() == Integer.parseInt(answer.get(j).getValue())) {
                                                    isSeleted = true;
                                                    index = j;
                                                }
                                            }
                                            if (isSeleted) {
                                                answer.remove(index);
                                            }
                                            image.setImageResource(R.drawable.checkbox_unselect);
                                        }
                                    }
                                });
                            }
                        }
                    }
                    //out

                    if(data.getAnswers().get(i).getIsFreeTxt()) {
                        LinearLayout btn2 = new LinearLayout(this);
                        btn2.setOrientation(LinearLayout.VERTICAL);
                        TextView txtError = new TextView(this);
                        txtError.setText(data.getAnswers().get(i).getValidateTxt());
                        txtError.setTextSize(15);
                        txtError.setTextColor(Color.RED);
                        txtError.setTypeface(delegate.font_type);
                        txtError.setGravity(Gravity.CENTER_VERTICAL);
                        txtError.setHeight(delegate.dpToPx(15));
                        btn2.addView(txtError);
                        if(textType ==4){
                            btn2.addView(addDate);
                        } else {
                            btn2.addView(addEdit);
                        }
                        lp = new LinearLayout.LayoutParams(widthTextBox, delegate.dpToPx(55));
                        lp.gravity = Gravity.CENTER_VERTICAL;
                        lp.setMargins(delegate.dpToPx(20), delegate.dpToPx(5), 0, delegate.dpToPx(5));
                        btn2.setLayoutParams(lp);
                        btn.addView(btn2);
                    }
                }
            }

            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, delegate.dpToPx(55));
            lp.gravity = Gravity.CENTER_VERTICAL;
            lp.weight = 1;
            lp.setMargins(delegate.dpToPx(20), delegate.dpToPx(10), 0, delegate.dpToPx(10));
            btn.setLayoutParams(lp);
            linearLayout.addView(btn);
            //for beautiful
            if(i==total-1  && total % column !=0){
                for (int addcolum = 0;addcolum<column-(total % column);addcolum++){
                    LinearLayout btn2 = new LinearLayout(this);
                    lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, delegate.dpToPx(50));
                    lp.gravity = Gravity.CENTER_VERTICAL;

                    lp.weight = 1;
                    lp.setMargins(delegate.dpToPx(20), delegate.dpToPx(10), 0, delegate.dpToPx(10));
                    btn2.setLayoutParams(lp);
                    linearLayout.addView(btn2);
                }
            }
        }
        content_view.addView(linearLayout);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display01, menu);
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
                }
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
            final int indexSelected =Integer.parseInt(parent.getTag().toString());
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
                        if(Integer.parseInt(data.getAnswers().get(indexSelected).getFreeTxtType()) ==4){
                            showCalendar(selected.getId());
                        }
                    } else {
                        image.setImageResource(R.drawable.checkbox_unselect);
                        answer.remove(index);
                        content_view.removeAllViews();
                        setTableLayout();
                    }
                } else {
                    image.setImageResource(R.drawable.checkbox_selected);
                    SaveAnswerData _ans = new SaveAnswerData(String.valueOf(selected.getId()),"");
                    answer.add(_ans);
                    if(Integer.parseInt(data.getAnswers().get(indexSelected).getFreeTxtType()) ==4){
                        showCalendar(selected.getId());
                    }
                }
                break;
            }
        }
    }

    public void nextPage(){
        String error_msg = delegate.validate(answer,data.getAnswers());
        if(error_msg.equals("NO")){
            delegate.QM.save_answer(answer);
            delegate.nextQuestionPage(delegate.nextPage(this));
        } else {
            Toast.makeText(this, error_msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        if(delegate.checkPressBack(answer)){
            delegate.backQuestionpage(this);
        }else{
            Toast.makeText(this, "Cannot Back", Toast.LENGTH_SHORT).show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode ==2 || resultCode == 0 || resultCode == 1){
            this.setResult(resultCode);
            finish();
        }
    }

    public void showCalendar(final int indexCalendar){
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog mDatePicker = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                mDay = selectedday;
                mMonth = selectedmonth + 1;
                mYear = selectedyear;
                for(int j=0;j<answer.size();j++){
                    if(indexCalendar == Integer.parseInt(answer.get(j).getValue())){
                        answer.set(j,new SaveAnswerData(String.valueOf(indexCalendar),mYear + "-" + mMonth + "-" + mDay));
                        content_view.removeAllViews();
                        setTableLayout();
                        break;
                    }
                }
                Log.e("DATE",mYear + "-" + mMonth + "-" + mDay);
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        for(int j=0;j<answer.size();j++) {
                            if (indexCalendar == Integer.parseInt(answer.get(j).getValue())) {
                                if(answer.get(j).getFreetxt().length()==0){
                                    answer.remove(j);
                                }
                                content_view.removeAllViews();
                                setTableLayout();
                                break;
                            }
                        }
                    }
                });
            }
        });
        mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }
}
