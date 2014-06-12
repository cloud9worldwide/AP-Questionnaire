package com.cloud9worldwide.questionnaire.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cloud9 on 4/11/14.
 */
public class QuestionnaireAnswerData {
    private String customerId;
    private Boolean iscustomerLocal;
    private String projectId;
    private String questionnaireId;
    private String timedevice;
    private ArrayList<QuestionAnswerData> answers;
    private ArrayList<QuestionAnswerData> staffanswers;

    private long saveQuestionnaireId = -1;

    public long getSaveQuestionnaireId() {
        return saveQuestionnaireId;
    }

    public void setSaveQuestionnaireId(long saveQuestionnaireId) {
        this.saveQuestionnaireId = saveQuestionnaireId;
    }

    public QuestionnaireAnswerData() {
        Date _date = new Date();
        String pattern = "yyyyMMddHHmmss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        this.timedevice = format.format(_date);

        this.answers = new ArrayList<QuestionAnswerData>();
        this.staffanswers = new ArrayList<QuestionAnswerData>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Boolean getIscustomerLocal() {
        return iscustomerLocal;
    }

    public void setIscustomerLocal(Boolean iscustomerLocal) {
        this.iscustomerLocal = iscustomerLocal;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public ArrayList<QuestionAnswerData> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<QuestionAnswerData> answers) {
        this.answers = answers;
    }

    public ArrayList<QuestionAnswerData> getStaffanswers() {
        return staffanswers;
    }

    public void setStaffanswers(ArrayList<QuestionAnswerData> staffanswers) {
        this.staffanswers = staffanswers;
    }

    public String getTimedevice() {
        return timedevice;
    }

    public void setTimedevice(String timedevice) {
        this.timedevice = timedevice;
    }

    public void addAnswer(QuestionAnswerData answer){
        if(this.answers != null){
            this.answers.add(answer);
        }
    }
    public void addStaffAnswer(QuestionAnswerData answer){
        if(this.staffanswers != null){
            this.staffanswers.add(answer);
        }
    }


    public JSONObject toJsonObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("customerId",this.customerId);
            obj.put("iscustomerLocal",this.iscustomerLocal);
            obj.put("projectId",this.projectId);
            obj.put("questionnaireId",this.questionnaireId);
            obj.put("timedevice",this.timedevice);


            JSONArray obj_arr = new JSONArray();
            for (int i = 0; i < this.answers.size() ; i++) {
                QuestionAnswerData ans = this.answers.get(i);
                obj_arr.put(i,ans.toJsonObject());
            }
            obj.put("answers",obj_arr);

            JSONArray obj_arr2 = new JSONArray();
            for (int i = 0; i < this.staffanswers.size() ; i++) {
                QuestionAnswerData ans = this.staffanswers.get(i);
                obj_arr2.put(i,ans.toJsonObject());
            }
            obj.put("staffanswers",obj_arr2);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  obj;
    }
    public String toString(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("customerId",this.customerId);
            obj.put("iscustomerLocal",this.iscustomerLocal);
            obj.put("projectId",this.projectId);
            obj.put("questionnaireId",this.questionnaireId);
            obj.put("timedevice",this.timedevice);


            JSONArray obj_arr = new JSONArray();
            for (int i = 0; i < this.answers.size() ; i++) {
                QuestionAnswerData ans = this.answers.get(i);
                obj_arr.put(i,ans.toJsonObject());
            }
            obj.put("answers",obj_arr);

            JSONArray obj_arr2 = new JSONArray();
            for (int i = 0; i < this.staffanswers.size() ; i++) {
                QuestionAnswerData ans = this.staffanswers.get(i);
                obj_arr2.put(i,ans.toJsonObject());
            }
            obj.put("staffanswers",obj_arr2);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  obj.toString();
    }
}
