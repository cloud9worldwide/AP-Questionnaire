package com.cloud9worldwide.questionnaire.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cloud9 on 4/11/14.
 */
public class SaveAnswerData {
    private String value;
    private String freetxt;
    private boolean hassubquestion = false;

    public SaveAnswerData(String value, String freetxt) {
        this.value = value;
        if(freetxt ==null){
            freetxt = "";
        }
        this.freetxt = freetxt;
    }

    public boolean isHassubquestion() {
        return hassubquestion;
    }

    public void setHassubquestion(boolean hassubquestion) {
        this.hassubquestion = hassubquestion;
    }

    public String getValue() {
        return value;
    }

    public String getFreetxt() {
        return freetxt;
    }

    public JSONObject toJsonObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("value",this.value);
            obj.put("freetxt",this.freetxt);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  obj;
    }
    public String toString(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("value",this.value);
            obj.put("freetxt",this.freetxt);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  obj.toString();
    }
}
