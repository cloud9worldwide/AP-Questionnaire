package com.cloud9worldwide.questionnaire.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cloud9 on 3/26/14.
 */
public class ProjectData {
    private String id;
    private String name;
    private String logoUrl;
    private String backgroundUrl;

    private ArrayList<QuestionnaireData> questionnaireList;

    public ProjectData(String _id,String _name,String _logoUrl,String _backgroundUrl){
        this.id = _id;
        this.name = _name;
        this.logoUrl = _logoUrl;
        this.backgroundUrl = _backgroundUrl;

        this.questionnaireList = new ArrayList<QuestionnaireData>();
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public ArrayList<QuestionnaireData> getQuestionnaireList() {
        return questionnaireList;
    }

    public void addQuestionnaire(QuestionnaireData questionnaire){
        this.questionnaireList.add(questionnaire);
    }
    public String toString(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",this.id);
            obj.put("name",this.name);
            obj.put("logoUrl",this.logoUrl);
            obj.put("backgroundUrl",this.backgroundUrl);

            JSONArray obj_arr = new JSONArray();
            for (int i = 0; i < this.questionnaireList.size(); i++) {
                QuestionnaireData q = this.questionnaireList.get(i);
                obj_arr.put(i,q.toJsonObject());
            }
            obj.put("questionnaireList",obj_arr.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj.toString();
    }
    public JSONObject toJsonObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",this.id);
            obj.put("name",this.name);
            obj.put("logoUrl",this.logoUrl);
            obj.put("backgroundUrl",this.backgroundUrl);

            JSONArray obj_arr = new JSONArray();
            for (int i = 0; i < this.questionnaireList.size(); i++) {
                QuestionnaireData q = this.questionnaireList.get(i);
                obj_arr.put(i,q.toJsonObject());
            }
            obj.put("questionnaireList",obj_arr.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj;
    }
}
