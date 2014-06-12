package com.cloud9worldwide.questionnaire.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cloud9 on 5/2/14.
 */
public class ValTextData {
    private String value;
    private String text;
    private String text2 = null;
    public ValTextData(String _val,String _txt) {
        this.value = _val;
        this.text = _txt;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String toString(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("value",this.value);
            obj.put("text",this.text);
            if(this.text2 != null){
                obj.put("text2",this.text2);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return text;
    }
}
