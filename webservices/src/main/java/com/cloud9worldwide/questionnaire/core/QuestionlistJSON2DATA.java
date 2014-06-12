package com.cloud9worldwide.questionnaire.core;

import com.cloud9worldwide.questionnaire.data.QuestionTypeData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cloud9 on 4/4/14.
 */
public class QuestionlistJSON2DATA {
    public static synchronized ArrayList<QuestionTypeData> parseData(JSONArray _questionlist){
        ArrayList<QuestionTypeData> _result = new ArrayList<QuestionTypeData>();
        //Extract question data & find url image for download to local
        try {
            for (int i = 0; i < _questionlist.length(); i++) {

                JSONObject _question = _questionlist.getJSONObject(i);
                QuestionTypeData _qTypeData = new QuestionTypeData(_question);
                if(_qTypeData.getQuestion() != null && _qTypeData.getAnswers() != null)
                    _result.add(_qTypeData);
            }

            //return  _result;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return _result;
    }
}
