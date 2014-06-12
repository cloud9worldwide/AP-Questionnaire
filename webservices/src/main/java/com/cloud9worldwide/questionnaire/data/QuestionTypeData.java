package com.cloud9worldwide.questionnaire.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by cloud9 on 3/14/14.
 */
public class QuestionTypeData {
    private String questionType;
    private int parent_question_id;
    private QuestionData question = null;
    private ArrayList<AnswerData> answers = null;
    private boolean parent_question = false;

    public boolean isParent_question() {
        return parent_question;
    }

    public void setParent_question(boolean parent_question) {
        this.parent_question = parent_question;
    }

    public int getParent_question_id() {
        return parent_question_id;
    }

    public void setParent_question_id(int parent_question_id) {
        this.parent_question_id = parent_question_id;
    }

    public QuestionTypeData(JSONObject _jsondata) throws JSONException{
        try {
            //Log.d("Core",_jsondata.toString());
            this.questionType = _jsondata.getString("displayTemplate");

            if(_jsondata.has("parent_question_id"))
                this.parent_question_id = Integer.parseInt(_jsondata.getString("parent_question_id"));
            else
                this.parent_question_id = -1;

            JSONObject _question_obj = _jsondata.getJSONObject("question");
            JSONArray _answers_arr = _jsondata.getJSONArray("answer");

            this.question = new QuestionData(
                    _question_obj.getInt("id"),
                    _question_obj.getString("title"),
                    _question_obj.getString("remark"),
                    _question_obj.getString("image"));

            answers = new ArrayList<AnswerData>();
            for (int i = 0; i < _answers_arr.length(); i++) {
                //Log.d("Core",_answers_arr.getJSONObject(i).toString());
                AnswerData _answer = new AnswerData(_answers_arr.getJSONObject(i));
                answers.add(_answer);
            }
            if(answers.size() > 1){
                Collections.sort(answers, new Comparator<AnswerData>() {
                    @Override
                    public int compare(AnswerData answer1, AnswerData answer2) {
                        return answer1.itemOrder.compareTo(answer2.itemOrder);
                    }
                });
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public QuestionData getQuestion() {
        return question;
    }

    public void setQuestion(QuestionData question) {
        this.question = question;
    }

    public ArrayList<AnswerData> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<AnswerData> answers) {
        this.answers = answers;
    }

    public Integer answers_length(){
        return this.answers.size();
    }

    public JSONObject toJsonObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("questionType",this.questionType);
            obj.put("question",this.question.toJsonObject());
            JSONArray obj_arr = new JSONArray();
            for (int i = 0; i < this.answers.size() ; i++) {
                AnswerData ans = this.answers.get(i);
                obj_arr.put(i,ans.toJsonObject());
            }
            obj.put("answers",obj_arr);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  obj;
    }
    public String toString(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("questionType",this.questionType);
            obj.put("question",this.question.toJsonObject());
            JSONArray obj_arr = new JSONArray();
            for (int i = 0; i < this.answers.size() ; i++) {
                AnswerData ans = this.answers.get(i);
                obj_arr.put(i,ans.toJsonObject());
            }
            obj.put("answers",obj_arr);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  obj.toString();
    }
}
