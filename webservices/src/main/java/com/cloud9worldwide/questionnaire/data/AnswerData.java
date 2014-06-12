package com.cloud9worldwide.questionnaire.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cloud9 on 3/17/14.
 *
 *

 "id"			: 4011,
 "title" 		: "Answer Title...",
 "isFreeTxt" 	: false/true,
 "iconActive"	: "url_icon",
 "iconInActive"	: "url_icon",
 "image"         : "url_image",
 "itemOrder"		: 1
 */
public class AnswerData {
    private int id;
    private String title;
    private Boolean isFreeTxt;
    private String iconActiveUrl;
    private String iconInActiveUrl;
    private String imageUrl;
    public Integer itemOrder;

    private String FreeTxtType;
    private String FreeTxtMaxChar;
    private boolean hassubquestion;
    private int subquestion_id = -1;

    public String getFreeTxtMaxChar() {
        return FreeTxtMaxChar;
    }
    public void setFreeTxtMaxChar(String freeTxtMaxChar) {
        FreeTxtMaxChar = freeTxtMaxChar;
    }
    public int getSubquestion_id() {
        return subquestion_id;
    }
    public void setSubquestion_id(int subquestion_id) {
        this.subquestion_id = subquestion_id;
    }

    public String getFreeTxtType() {
        return FreeTxtType;
    }

    public void setFreeTxtType(String freeTxtType) {
        FreeTxtType = freeTxtType;
    }

    public AnswerData(JSONObject _answer){
        try {
            this.id = Integer.parseInt(_answer.getString("id"));
            this.title = _answer.getString("title");
            this.isFreeTxt = Boolean.parseBoolean(_answer.getString("isFreeTxt"));
            this.iconActiveUrl = _answer.getString("iconActive");
            this.iconInActiveUrl = _answer.getString("iconInActive");
            this.imageUrl = _answer.getString("image");
            this.itemOrder = Integer.parseInt(_answer.getString("itemOrder"));

            //check freetext type
            if(this.isFreeTxt && _answer.has("FreeTxtType")){
                this.FreeTxtType = _answer.getString("FreeTxtType");
            }else{
                this.FreeTxtType = "0";
            }

            //check freetext type
            if(this.isFreeTxt && _answer.has("FreeTxtMaxChar")){
                this.FreeTxtMaxChar = _answer.getString("FreeTxtMaxChar");
            }else{
                this.FreeTxtMaxChar = "0";
            }

            //check has sub question
            if(_answer.has("hassubquestion") && _answer.has("subquestion_id")){
                this.hassubquestion = Boolean.parseBoolean(_answer.getString("hassubquestion"));
                if(this.hassubquestion){
                    this.subquestion_id = Integer.parseInt(_answer.getString("subquestion_id"));
                }
            }else{
                this.hassubquestion = false;
                this.subquestion_id = -1;
            }
        }catch (JSONException e){
            this.id = -1;
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getIsFreeTxt() {
        return isFreeTxt;
    }

    public String getIconActiveUrl() {
        return iconActiveUrl;
    }

    public String getIconInActiveUrl() {
        return iconInActiveUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String toString(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",this.id);
            obj.put("title",this.title);
            obj.put("isFreeTxt",this.isFreeTxt);
            obj.put("FreeTxtType",this.FreeTxtType);
            obj.put("FreeTxtMaxChar",this.FreeTxtMaxChar);
            obj.put("iconActiveUrl",this.iconActiveUrl);
            obj.put("iconInActiveUrl",this.iconInActiveUrl);
            obj.put("imageUrl",this.imageUrl);
            obj.put("itemOrder",this.itemOrder);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return obj.toString();
    }
    public JSONObject toJsonObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",this.id);
            obj.put("title",this.title);
            obj.put("isFreeTxt",this.isFreeTxt);
            obj.put("FreeTxtType",this.FreeTxtType);
            obj.put("FreeTxtMaxChar",this.FreeTxtMaxChar);
            obj.put("iconActiveUrl",this.iconActiveUrl);
            obj.put("iconInActiveUrl",this.iconInActiveUrl);
            obj.put("imageUrl",this.imageUrl);
            obj.put("itemOrder",this.itemOrder);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return obj;
    }
}
