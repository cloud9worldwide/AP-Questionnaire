package com.cloud9worldwide.questionnaire.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cloud9 on 3/14/14.
 */
public class QuestionData {
    private Integer id;
    private String title;
    private String remark;
    private String imageUrl;

    public QuestionData(Integer id,String title,String remark,String imageUrl){
        super();
        this.id = id;
        this.title = title;
        this.remark = remark;
        this.imageUrl = imageUrl;
    }
    public Integer getId(){
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }
    public String getRemark(){
        return this.remark;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public JSONObject toJsonObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",this.id);
            obj.put("title",this.title);
            obj.put("remark",this.remark);
            obj.put("imageUrl",this.imageUrl);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  obj;
    }
    public String toString(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",this.id);
            obj.put("title",this.title);
            obj.put("remark",this.remark);
            obj.put("imageUrl",this.imageUrl);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  obj.toString();
    }
}
