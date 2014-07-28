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
import com.cloud9worldwide.questionnaire.data.AnswerData;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public ArrayList<QuestionTypeData> questions;

    public QuestionTypeData dataSubQuestion;
    public boolean skip_save_subans = false;

    public long timesleep = 100;

    TCImageLoader imageLoader;
    int imgDefault, imgDefaultQuestion, imgDefaultIcon, imgDefaultIconSelect;

    public QuestionnaireData questionnaire_selected;

    Typeface font_type;

    public QuestionManagement QM;
    public Context ctx;

    public int sizeImage,sizeImage19;
    public int isBack;

    public void saveUserNamePassword(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.commit();
    }



    public void sendAnswer() {
        if (QM.pack_staff_question_ans_data()) {
            service.saveQuestionnaireData(QM.get_questionnaire_ans_data());
        } else {
            //cannot pack staff
        }
        service.globals.setContactId("-1");
    }

    public void initQuestions() {
        AllHistoryAnswer = new ArrayList<QuestionAnswerData>();
        QM = new QuestionManagement(service, project, questionnaire_selected);
        questions = service.getQuestionnaireData(questionnaire_selected_id, questionnaire_time);
        QM.InitQuestionListData(questions);
    }

    public void initQuestionsStaff() {
        QM.pack_question_ans_data();
        questions = service.getStaffQuestionnaireData(questionnaire_selected_id, questionnaire_time);
        QM.InitStaffQuestionListData(questions);
    }

    public Spanned getTitleSequence() {
        Integer show_index = QM.getCurQuestionIndex() + 1;
        return Html.fromHtml("QUESTION <b>" + show_index + "</b> OF <b>" + QM.get_questions().size() + "</b>");
    }

    public String getPercent() {
        double percent = ((QM.getCurQuestionIndex() + 0.0) / QM.get_questions().size()) * 100.0;
        return (int) percent + "%";
    }

    public int getMax() {
        return QM.get_questions().size();
    }

    public int getProcessed() {
        return QM.getCurQuestionIndex();
    }

    public QuestionTypeData getQuestions() {
        if (questions.size() == index_question) {
            return null;
        }
        return questions.get(index_question);
    }


    public void setQuestionnaire_selected_id(String id) {
        questionnaire_selected_id = id;
    }

    public String getQuestionnaire_selected_id() {
        return questionnaire_selected_id;
    }

    public void setQuestionnaire_time(String time) {
        questionnaire_time = time;
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
        sizeImage = dpToPx(120);
        sizeImage19 = dpToPx(200);

        ctx = this;
        if (questionnaire_selected != null) {
            QM = new QuestionManagement(service, project, questionnaire_selected);
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

    public Uri readImageFileOnSDFileName(String rFileName) {
        File root = new File(Environment.getExternalStorageDirectory(), "Questionnaire");
        if (!root.exists()) {
            return null;
//            return Uri.parse("android.resource://your.package.here/drawable-xhdpi/no_image_icon.png");
        }
        File imgPath = new File(root, "downloadimgs/");
        String fileName = this.md5(rFileName);

        Uri temp = Uri.parse(imgPath.getAbsolutePath() + "/" + fileName + ".jpg");
        File checkFile = new File(temp.getPath());
        if (!checkFile.exists()) {
            return null;
        }
        if (checkFile.exists()) {
            Log.e("is found", "yes");
        } else {
            Log.e("is found", "no");
        }

        return temp;
    }

    public Bitmap readImageFileOnSD(String rFileName, int width, int height) {
        File root = new File(Environment.getExternalStorageDirectory(), "Questionnaire");
        if (!root.exists()) {
            return null;
        }
        File imgPath = new File(root, "downloadimgs");
        String fileName = this.md5(rFileName);
        File imgFile = new File(imgPath, fileName + ".jpg");
        if (imgFile.canRead()) {
            Bitmap bitmap = decodeFile(imgFile, width, height);
//            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return bitmap;
        }
        return null;
    }

    private Bitmap decodeFile(File f, int width, int height) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            o.inDither = false;                     //Disable Dithering mode
            o.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
            o.inInputShareable = true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
            o.inTempStorage = new byte[32 * 1024];

            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            Log.e("scale ",scale +"");
            if(width !=0 && height !=0){
                while (o.outWidth / scale / 2 >= width && o.outHeight / scale / 2 >= height){
                    scale *= 2;
                    Log.e("scale ",scale +"");
                }
            }



            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public Intent nextPage(Context context) {
        Intent newPage = new Intent();

        if (QM.move_next()) {
            if (QM.get_question().getQuestionType().equals("1")) {
                newPage = new Intent(this, Display01Activity.class);
            } else if (QM.get_question().getQuestionType().equals("2")) {
                newPage = new Intent(this, Display02Activity.class);
            } else if (QM.get_question().getQuestionType().equals("3")) {
                newPage = new Intent(this, Display03Activity.class);
            } else if (QM.get_question().getQuestionType().equals("4")) {
                newPage = new Intent(this, Display04Activity.class);
            } else if (QM.get_question().getQuestionType().equals("5")) {
                newPage = new Intent(this, Display05Activity.class);
            } else if (QM.get_question().getQuestionType().equals("6")) {
                newPage = new Intent(this, Display06Activity.class);
            } else if (QM.get_question().getQuestionType().equals("7")) {
                newPage = new Intent(this, Display07Activity.class);
            } else if (QM.get_question().getQuestionType().equals("8")) {
                newPage = new Intent(this, Display08Activity.class);
            } else if (QM.get_question().getQuestionType().equals("9")) {
                newPage = new Intent(this, Display09Activity.class);
            } else if (QM.get_question().getQuestionType().equals("10")) {
                newPage = new Intent(this, Display10Activity.class);
            } else if (QM.get_question().getQuestionType().equals("11")) {
                newPage = new Intent(this, Display11Activity.class);
            } else if (QM.get_question().getQuestionType().equals("12")) {
                newPage = new Intent(this, Display12Activity.class);
            } else if (QM.get_question().getQuestionType().equals("13")) {
                newPage = new Intent(this, Display13Activity.class);
            } else if (QM.get_question().getQuestionType().equals("14")) {
                newPage = new Intent(this, Display14Activity.class);
            } else if (QM.get_question().getQuestionType().equals("15")) {
                newPage = new Intent(this, Display15Activity.class);
            } else if (QM.get_question().getQuestionType().equals("16")) {
                newPage = new Intent(this, Display16Activity.class);
            } else if (QM.get_question().getQuestionType().equals("17")) {
                newPage = new Intent(this, Display17Activity.class);
            } else if (QM.get_question().getQuestionType().equals("18")) {
                newPage = new Intent(this, Display18Activity.class);
            } else if (QM.get_question().getQuestionType().equals("19")) {
                newPage = new Intent(this, Display19Activity.class);
            } else if (QM.get_question().getQuestionType().equals("20")) {
                newPage = new Intent(this, Display20Activity.class);
            } else if (QM.get_question().getQuestionType().equals("21")) {
                newPage = new Intent(this, Display21Activity.class);
            }
        } else {
            newPage = new Intent(this, CustomerFinishedAnswerActivity.class);
        }


        return newPage;
    }

    public synchronized ArrayList<SaveAnswerData> getHistory() {
        ArrayList<SaveAnswerData> answer = new ArrayList<SaveAnswerData>();
        if (this.AllHistoryAnswer != null) {
            String _q_id;
            if (QM.get_question().isParent_question() && dataSubQuestion != null) {
                _q_id = dataSubQuestion.getQuestion().getId().toString();
            } else {
                _q_id = QM.get_question().getQuestion().getId().toString();
            }
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

    public synchronized ArrayList<QuestionAnswerData> getQuestionnaireHistory() {
        ArrayList<QuestionAnswerData> questionnaireAnswer = new ArrayList<QuestionAnswerData>();
        if (service.isOnline() && !service.globals.getIsCustomerLocal()) {
            questionnaireAnswer = service.getQuestionnaireAnswerHistory(String.valueOf(this.getQuestionnaire_selected_id()));
        }
        AllHistoryAnswer = questionnaireAnswer;
        Log.e("AllHistoryAnswer",AllHistoryAnswer.toString());
        return questionnaireAnswer;
    }

    public synchronized boolean RemoveQuestionHistory(String _q_id) {
        if (this.AllHistoryAnswer != null) {
            for (int i = 0; i < this.AllHistoryAnswer.size(); i++) {
                QuestionAnswerData q_ans = this.AllHistoryAnswer.get(i);
                if (q_ans.getQuestionId().equals(_q_id)) {
                    this.AllHistoryAnswer.remove(i);
                }
            }
        }
        return true;
    }

    public int dpToPx(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        int px = (int) (dp * scale + 0.5f);
        return px;
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public void nextQuestionPage(Intent intent) {
        //QuestionTypeData question = this.QM.get_question();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        /*
        if(!question.isParent_question()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        }
        */
        startActivity(intent);
    }

    public Intent getCurentQuestionIntent() {
        Intent newPage = new Intent();
        if (QM.get_question().getQuestionType().equals("1")) {
            newPage = new Intent(this, Display01Activity.class);
        } else if (QM.get_question().getQuestionType().equals("2")) {
            newPage = new Intent(this, Display02Activity.class);
        } else if (QM.get_question().getQuestionType().equals("3")) {
            newPage = new Intent(this, Display03Activity.class);
        } else if (QM.get_question().getQuestionType().equals("4")) {
            newPage = new Intent(this, Display04Activity.class);
        } else if (QM.get_question().getQuestionType().equals("5")) {
            newPage = new Intent(this, Display05Activity.class);
        } else if (QM.get_question().getQuestionType().equals("6")) {
            newPage = new Intent(this, Display06Activity.class);
        } else if (QM.get_question().getQuestionType().equals("7")) {
            newPage = new Intent(this, Display07Activity.class);
        } else if (QM.get_question().getQuestionType().equals("8")) {
            newPage = new Intent(this, Display08Activity.class);
        } else if (QM.get_question().getQuestionType().equals("9")) {
            newPage = new Intent(this, Display09Activity.class);
        } else if (QM.get_question().getQuestionType().equals("10")) {
            newPage = new Intent(this, Display10Activity.class);
        } else if (QM.get_question().getQuestionType().equals("11")) {
            newPage = new Intent(this, Display11Activity.class);
        } else if (QM.get_question().getQuestionType().equals("12")) {
            newPage = new Intent(this, Display12Activity.class);
        } else if (QM.get_question().getQuestionType().equals("13")) {
            newPage = new Intent(this, Display13Activity.class);
        } else if (QM.get_question().getQuestionType().equals("14")) {
            newPage = new Intent(this, Display14Activity.class);
        } else if (QM.get_question().getQuestionType().equals("15")) {
            newPage = new Intent(this, Display15Activity.class);
        } else if (QM.get_question().getQuestionType().equals("16")) {
            newPage = new Intent(this, Display16Activity.class);
        } else if (QM.get_question().getQuestionType().equals("17")) {
            newPage = new Intent(this, Display17Activity.class);
        } else if (QM.get_question().getQuestionType().equals("18")) {
            newPage = new Intent(this, Display18Activity.class);
        } else if (QM.get_question().getQuestionType().equals("19")) {
            newPage = new Intent(this, Display19Activity.class);
        } else if (QM.get_question().getQuestionType().equals("20")) {
            newPage = new Intent(this, Display20Activity.class);
        } else if (QM.get_question().getQuestionType().equals("21")) {
            newPage = new Intent(this, Display21Activity.class);
        }
        return newPage;
    }

    public void backQuestionpage(Context ctx) {

        if (dataSubQuestion != null) {
            //is sub question

            dataSubQuestion = null;
            Intent i = getCurentQuestionIntent();
            nextQuestionPage(i);
            //startActivity(i);
        } else {
            QM.move_back();
            Intent i = getCurentQuestionIntent();
            nextQuestionPage(i);
            //startActivity(i);
        }
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean checkPressBack(ArrayList<SaveAnswerData> _ans) {
        if (dataSubQuestion != null) {
            //isSub question
            if(skip_save_subans)
                return true;

            QuestionTypeData parent_question = QM.get_question();
            QuestionTypeData sub_question = this.dataSubQuestion;
            if (parent_question.isParent_question() && parent_question.getQuestion().getId() == sub_question.getParent_question_id()) {
                if (_ans.size() > 0) {

                    ArrayList<AnswerData> all_parent_ans = parent_question.getAnswers();
                    for (int i = 0; i < all_parent_ans.size(); i++) {
                        AnswerData parent_ans = all_parent_ans.get(i);
                        if (parent_ans.getSubquestion_id() == sub_question.getQuestion().getId()) {
                            QuestionAnswerData checkAnswer = QM.get_answer();

                            ArrayList<SaveAnswerData> old_answer = new ArrayList<SaveAnswerData>();
                            if (checkAnswer != null) {
                                old_answer = checkAnswer.getAnswer();
                            }
                            ArrayList<SaveAnswerData> answer = new ArrayList<SaveAnswerData>();
                            SaveAnswerData new_ans = new SaveAnswerData(String.valueOf(parent_ans.getId()), "");
                            answer.add(new_ans);
                            for (int j = 0; j < old_answer.size(); j++) {
                                SaveAnswerData o_ans = old_answer.get(j);
                                if (!o_ans.getValue().equals(new_ans.getValue())) {
                                    answer.add(o_ans);
                                }
                            }
                            QM.save_answer(answer);
                        }
                    }
                } else {
                    //remove answer
                    ArrayList<AnswerData> all_parent_ans = parent_question.getAnswers();
                    for (int i = 0; i < all_parent_ans.size(); i++) {
                        AnswerData parent_ans = all_parent_ans.get(i);
                        if (parent_ans.getSubquestion_id() == sub_question.getQuestion().getId()) {
                            QuestionAnswerData checkAnswer = QM.get_answer();
                            ArrayList<SaveAnswerData> old_answer = new ArrayList<SaveAnswerData>();
                            if (checkAnswer != null) {
                                old_answer = checkAnswer.getAnswer();
                            }

                            ArrayList<SaveAnswerData> answer = new ArrayList<SaveAnswerData>();
                            SaveAnswerData new_ans = new SaveAnswerData(String.valueOf(parent_ans.getId()), "");
                            //answer.add(new_ans);
                            for (int j = 0; j < old_answer.size(); j++) {
                                SaveAnswerData o_ans = old_answer.get(j);
                                if (!o_ans.getValue().equals(new_ans.getValue())) {
                                    answer.add(o_ans);
                                }
                            }
                            QM.save_answer(answer);
                        }
                    }
                }
            }

            return true;
        } else {
            if (this.QM.getCurQuestionIndex() > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    //getFreeTxtType 1:number, 2:string, 3:EMAIL, 4=Date;
    //MaxChar work case 1 and 2 only;
    public String validate(ArrayList<SaveAnswerData> answer, ArrayList<AnswerData> choice) {
        String error_msg = "NO";

        for (int i = 0; i < answer.size(); i++) {
            for (int j = 0; j < choice.size(); j++) {
                if (answer.get(i).getValue().toString().equals(String.valueOf(choice.get(j).getId()))) {
                    if (choice.get(j).getIsFreeTxt()) {
                        if (choice.get(j).getFreeTxtType().toString().equals("1")) {
                            if (answer.get(i).getFreetxt().toString().length() == 0) {
                                error_msg = "Please ans";
                            }
                        } else if (choice.get(j).getFreeTxtType().toString().equals("2")) {
                            if (answer.get(i).getFreetxt().toString().length() == 0) {
                                error_msg = "Please ans";
                            }
                        } else if (choice.get(j).getFreeTxtType().toString().equals("3")) {
                            if (emailValidator(answer.get(i).getFreetxt().toString()) || answer.get(i).getFreetxt().toString().length() == 0) {
                                error_msg = getString(R.string.email_not_correct);
                            }
                        } else if (choice.get(j).getFreeTxtType().toString().equals("4")) {
                            if (answer.get(i).getFreetxt().toString().length() == 0) {
                                error_msg = "Please ans";
                            }
                        }
                    }
                }
            }
        }
        return error_msg;
    }
}
