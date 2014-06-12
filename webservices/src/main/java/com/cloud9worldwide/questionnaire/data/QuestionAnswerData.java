package com.cloud9worldwide.questionnaire.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cloud9 on 4/11/14.
 */
public class QuestionAnswerData {
    private String questionId;
    private boolean isDefault = true;
    private ArrayList<SaveAnswerData> answer;

    public QuestionAnswerData(String questionId, ArrayList<SaveAnswerData> answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public void setAnswer(ArrayList<SaveAnswerData> answer) {
        this.answer = answer;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getQuestionId() {
        return questionId;
    }

    public ArrayList<SaveAnswerData> getAnswer() {
        return answer;
    }

    public JSONObject toJsonObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("questionId",this.questionId);
            JSONArray obj_arr = new JSONArray();
            for (int i = 0; i < this.answer.size(); i++) {
                JSONObject ans = this.answer.get(i).toJsonObject();
                obj_arr.put(i,ans);
            }
            obj.put("answer",obj_arr);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  obj;
    }
    public String toString(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("questionId",this.questionId);
            JSONArray obj_arr = new JSONArray();
            for (int i = 0; i < this.answer.size(); i++) {
                JSONObject ans = this.answer.get(i).toJsonObject();
                obj_arr.put(i,ans);
            }
            obj.put("answer",obj_arr);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  obj.toString();
    }
}
