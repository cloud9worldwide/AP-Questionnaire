package com.apthai.ap_questionaire.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;

import com.cloud9worldwide.questionnaire.core.CoreEngine;
import com.cloud9worldwide.questionnaire.core.TCImageLoader;
import com.cloud9worldwide.questionnaire.data.ContactData;
import com.cloud9worldwide.questionnaire.data.ContactSearchData;
import com.cloud9worldwide.questionnaire.data.ProjectData;
import com.cloud9worldwide.questionnaire.data.QuestionAnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionTypeData;
import com.cloud9worldwide.questionnaire.data.QuestionnaireData;
import com.cloud9worldwide.questionnaire.data.SaveAnswerData;
import com.cloud9worldwide.questionnaire.questionmagement.QuestionManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by koy on 08/04/2014.
 */

public class questionniare_delegate extends Application {

    public CoreEngine service;
    private ArrayList<ContactSearchData> customer_list = new ArrayList<ContactSearchData>();
    public ContactData customer_selected;
    private String questionnaire_selected_id, questionnaire_time;
    public ProjectData project;
    public int index_question;
    public  ArrayList<QuestionTypeData> questions;
    ArrayList<QuestionAnswerData> _answers,_staff_answers;

    public QuestionTypeData dataSubQuestion;

    public long timesleep = 100;

    TCImageLoader imageLoader;
    int imgDefault, imgDefaultQuestion, imgDefaultIcon,imgDefaultIconSelect;

    public QuestionnaireData questionnaire_selected;

    Typeface font_type;

    public QuestionManagement QM;
    public Context ctx;
    public void saveUserNamePassword(Context context){
        SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.commit();
    }

    public  synchronized  boolean testja()
    {
        return  false;
    }

//    public void addAnswers(QuestionAnswerData answerData){
//        if(isCustomerMode){
//            _answers.add(answerData);
//            Log.e("print",_answers.toString());
//        } else {
//            _staff_answers.add(answerData);
//            Log.e("print",_staff_answers.toString());
//        }
//    }
    private void printAnswer(QuestionAnswerData answerData){
        ArrayList<SaveAnswerData> save  = new ArrayList<SaveAnswerData>();
        save = answerData.getAnswer();
        for(int j=0;j<save.size();j++){
            Log.e("print ans", "order "+ j + " : " +save.get(j).getValue());
        }
    }
    private void printAllAnswer(){
        for(int f =0; f<_answers.size(); f++){
            ArrayList<SaveAnswerData> save  = _answers.get(f).getAnswer();
            for(int j=0;j<save.size();j++){
                Log.e("print ans", "order "+ f + " : " +save.get(j).getValue());
            }
        }
    }
    private void printAllStaffAnswer(){
        for(int f =0; f<_staff_answers.size(); f++){
            ArrayList<SaveAnswerData> save  = _staff_answers.get(f).getAnswer();
            for (int j=0;j<save.size();j++) {
                Log.e("staff ans", "order "+ f + " : " + save.get(j).getValue());
            }
        }
    }
    //keep
    //service.sync_save_questionnaire()

    public void sendAnswer(){
        if(QM.pack_staff_question_ans_data()){
            service.saveQuestionnaireData(QM.get_questionnaire_ans_data());
        }else {
            //cannot pack staff
        }

//        QuestionnaireAnswerData answer_data = new QuestionnaireAnswerData();
//        answer_data.setCustomerId(String.valueOf(service.globals.getContactId()));
//        answer_data.setIscustomerLocal(service.globals.getIsCustomerLocal());
//        answer_data.setProjectId(project.getId());
//        answer_data.setQuestionnaireId(questionnaire_selected_id);
//        answer_data.setAnswers(_answers);
//        answer_data.setStaffanswers(_staff_answers);
//
//        Log.e("print",answer_data.toString());
//        service.saveQuestionnaireData(answer_data);
        service.globals.setContactId("-1");
    }

    public void initQuestions(){
        QM = new QuestionManagement(service,project,questionnaire_selected);
        questions = service.getQuestionnaireData(questionnaire_selected_id, questionnaire_time);
        QM.InitQuestionListData(questions);
    }
    public void initQuestionsStaff(){
        QM.pack_question_ans_data();
        questions = service.getStaffQuestionnaireData(questionnaire_selected_id, questionnaire_time);
        QM.InitStaffQuestionListData(questions);
    }
    public Spanned getTitleSequence(){
        Integer show_index = QM.getCurQuestionIndex() +1;
        return Html.fromHtml("QUESTION <b>" + show_index + "</b> OF <b>" + QM.get_questions().size() + "</b>");
    }
    public String getPercent() {
        double percent = ((QM.getCurQuestionIndex() +0.0)/ QM.get_questions().size()) *100.0;
        return (int)percent +"%";
    }
    public int getMax(){
        return QM.get_questions().size();
    }
    public int getProcessed(){
        return QM.getCurQuestionIndex() ;
    }

    public QuestionTypeData getQuestions() {
        if(questions.size() == index_question){
            return null;
        }
        return questions.get(index_question);
    }

    public void nextIndex_question(){
        index_question++;
        Log.e("s question", index_question + "");
    }
    public boolean backIndex_question(){
        if(index_question==0){
            return false;
        } else {
            index_question--;
            _answers.remove(index_question);
            return true;
        }


    }

    public void setIndex_question(int index){
        index_question = index;
    }
    public int getIndex_question(){
        return index_question;
    }

    public void setQuestionnaire_selected_id(String id){
        questionnaire_selected_id =id;
    }
    public String getQuestionnaire_selected_id(){
        return questionnaire_selected_id;
    }
    public void setQuestionnaire_time(String time){
        questionnaire_time=time;
    }
    public String getQuestionnaire_time(){
        return questionnaire_time;
    }
    public void setCustomer_list(ArrayList<ContactSearchData> customer_list) {
        this.customer_list = customer_list;
    }

    public ArrayList<ContactSearchData> getCustomer_list() {
        return customer_list;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        service = new CoreEngine(this);
        imageLoader = new TCImageLoader(this);
        imgDefault = R.drawable.space;
        imgDefaultQuestion = R.drawable.no_image_question;
        imgDefaultIcon = R.drawable.no_image_icon;
        imgDefaultIconSelect = R.drawable.no_image_icon_selected;
        font_type = Typeface.createFromAsset(getAssets(),
                "fonts/DB_Ozone_X.otf");

        ctx = this;
        if(questionnaire_selected !=null){
            QM = new QuestionManagement(service,project,questionnaire_selected);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public Uri readImageFileOnSDFileName(String rFileName){
        File root = new File(Environment.getExternalStorageDirectory(), "Questionnaire");
        if (!root.exists()) {
            return null;
//            return Uri.parse("android.resource://your.package.here/drawable-xhdpi/no_image_icon.png");
        }
        File imgPath = new File(root,"downloadimgs/");
        String fileName = this.md5(rFileName);

        Uri temp =Uri.parse(imgPath.getAbsolutePath()+ "/" + fileName + ".jpg");
        File checkFile = new File(temp.getPath());
        if (!checkFile.exists()) {
            return null;
        }
        if(checkFile.exists()){
            Log.e("is found", "yes");
        } else {
            Log.e("is found", "no");
        }

        return temp;
    }
    public Bitmap readImageFileOnSD(String rFileName, int width, int height){
        File root = new File(Environment.getExternalStorageDirectory(), "Questionnaire");
        if (!root.exists()) {
            return null;
        }
        File imgPath = new File(root,"downloadimgs");
        String fileName = this.md5(rFileName);
        File imgFile = new File(imgPath, fileName+".jpg");
        if(imgFile.canRead()){
            Bitmap bitmap = decodeFile(imgFile,width,height);
//            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return bitmap;
        }
        return null;
    }
    private Bitmap decodeFile(File f, int width, int height){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            o.inDither=false;                     //Disable Dithering mode
            o.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
            o.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
            o.inTempStorage=new byte[32 * 1024];

            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=width && o.outHeight/scale/2>=height)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    public Intent nextPage (Context context){
        Intent newPage = new Intent();

        if(QM.move_next()){
            if(QM.get_question().getQuestionType().equals("1")){
                newPage = new Intent(this, Display01Activity.class);
            } else if(QM.get_question().getQuestionType().equals("2")){
                newPage = new Intent(this, Display02Activity.class);
            } else if(QM.get_question().getQuestionType().equals("3")){
                newPage = new Intent(this, Display03Activity.class);
            } else if(QM.get_question().getQuestionType().equals("4")){
                newPage = new Intent(this, Display04Activity.class);
            } else if(QM.get_question().getQuestionType().equals("5")){
                newPage = new Intent(this, Display05Activity.class);
            } else if(QM.get_question().getQuestionType().equals("6")){
                newPage = new Intent(this, Display06Activity.class);
            } else if(QM.get_question().getQuestionType().equals("7")){
                newPage = new Intent(this, Display07Activity.class);
            } else if(QM.get_question().getQuestionType().equals("8")){
                newPage = new Intent(this, Display08Activity.class);
            } else if(QM.get_question().getQuestionType().equals("9")){
                newPage = new Intent(this, Display09Activity.class);
            } else if(QM.get_question().getQuestionType().equals("10")){
                newPage = new Intent(this, Display10Activity.class);
            } else if(QM.get_question().getQuestionType().equals("11")){
                newPage = new Intent(this, Display11Activity.class);
            } else if(QM.get_question().getQuestionType().equals("12")){
                newPage = new Intent(this, Display12Activity.class);
            } else if(QM.get_question().getQuestionType().equals("13")){
                newPage = new Intent(this, Display13Activity.class);
            } else if(QM.get_question().getQuestionType().equals("14")){
                newPage = new Intent(this, Display14Activity.class);
            } else if(QM.get_question().getQuestionType().equals("15")){
                newPage = new Intent(this, Display15Activity.class);
            } else if(QM.get_question().getQuestionType().equals("16")){
                newPage = new Intent(this, Display16Activity.class);
            } else if(QM.get_question().getQuestionType().equals("17")){
                newPage = new Intent(this, Display17Activity.class);
            } else if(QM.get_question().getQuestionType().equals("18")){
                newPage = new Intent(this, Display18Activity.class);
            } else if(QM.get_question().getQuestionType().equals("19")){
                newPage = new Intent(this, Display19Activity.class);
            } else if(QM.get_question().getQuestionType().equals("20")){
                newPage = new Intent(this, Display20Activity.class);
            } else if(QM.get_question().getQuestionType().equals("21")){
                newPage = new Intent(this, Display21Activity.class);
            }
        } else {
            newPage = new Intent(this, CustomerFinishedAnswerActivity.class);
        }



        return newPage;
    }

    public synchronized ArrayList<SaveAnswerData> getHistory(){
        ArrayList<SaveAnswerData> answer = new ArrayList<SaveAnswerData>();
        if(this.AllHistoryAnswer != null) {
            String _q_id = QM.get_question().getQuestion().getId().toString();
            for (int i = 0; i < this.AllHistoryAnswer.size(); i++) {
                QuestionAnswerData q_ans = this.AllHistoryAnswer.get(i);

                if (q_ans.getQuestionId().equals(_q_id)) {
                    answer = q_ans.getAnswer();
                }
            }
        }
        return answer;
    }
    private ArrayList<QuestionAnswerData> AllHistoryAnswer = null;
    public synchronized ArrayList<QuestionAnswerData> getQuestionnaireHistory(){
        ArrayList<QuestionAnswerData> questionnaireAnswer = new ArrayList<QuestionAnswerData>();
        if(service.isOnline() && !service.globals.getIsCustomerLocal()){
            questionnaireAnswer =  service.getQuestionnaireAnswerHistory(String.valueOf(this.getQuestionnaire_selected_id()));
        }
        AllHistoryAnswer = questionnaireAnswer;
        return questionnaireAnswer;
    }



    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }
}
