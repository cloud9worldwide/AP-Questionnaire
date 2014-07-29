package com.cloud9worldwide.questionnaire.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cloud9 on 3/26/14.
 */
public class QuestionnaireData {
    private String id;
    private String type;
    private String logoUrl;
    private String timeStamp;
    private String lg;

    public QuestionnaireData(String _id,String _type,String _logoUrl,String _timeStamp){
        this.id = _id;
        this.type = _type;
        this.logoUrl = _logoUrl;
        this.timeStamp = _timeStamp;
        this.lg = "th";
    }
    public QuestionnaireData(String _id,String _type,String _logoUrl,String _timeStamp, String _lg){
        this.id = _id;
        this.type = _type;
        this.logoUrl = _logoUrl;
        this.timeStamp = _timeStamp;
        this.lg = _lg;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getLg() {
        return lg;
    }

    public void setLg(String lg) {
        this.lg = lg;
    }

    public JSONObject toJsonObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",this.id);
            obj.put("type",this.type);
            obj.put("logoUrl",this.logoUrl);
            obj.put("timeStamp",this.timeStamp);
            obj.put("lg",this.lg);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  obj;
    }
    public String toString(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",this.id);
            obj.put("type",this.type);
            obj.put("logoUrl",this.logoUrl);
            obj.put("timeStamp",this.timeStamp);
            obj.put("lg",this.lg);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  obj.toString();
    }
}
