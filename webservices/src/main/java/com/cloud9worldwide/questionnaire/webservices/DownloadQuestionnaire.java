package com.cloud9worldwide.questionnaire.webservices;

import android.app.ProgressDialog;
import android.util.Log;

import com.cloud9worldwide.questionnaire.data.QuestionnaireData;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cloud9 on 4/3/14.
 */
public class DownloadQuestionnaire {
    public static class ApiException extends Exception {
        private static final long serialVersionUID = 1L;
        public ApiException (String msg)
        {
            super (msg);
        }

        public ApiException (String msg, Throwable thr)
        {
            super (msg, thr);
        }
    }
    public static synchronized ArrayList<String> download(ProgressDialog _pDialog,String _webserviceUrl,String _tokenaccess,ArrayList<QuestionnaireData> _allquestionnaire) throws ApiException {
        String command = "getquestionnaire";
        Boolean _error = false;
        String _error_msg = null;
        ArrayList<String>  response = new ArrayList<String>();
        //ArrayList<QuestionnaireData> _all_questionnaire = _allquestionnaire;

        int SIZE = _allquestionnaire.size();
        for (int i = 0; i < _allquestionnaire.size(); i++) {

            String text = "Download questionnaire["+(i+1)+ "/" + SIZE + "]";
            Log.d("Core", text);
            //_pDialog.setMessage(text);

            try {
                JSONObject json = new JSONObject();
                json.put("questionnaireid",_allquestionnaire.get(i).getId());
                json.put("tokenaccess",_tokenaccess);
                WebserviceHelper client = new WebserviceHelper(_webserviceUrl);
                client.AddParam("cmd", command);
                client.AddParam("data", json.toString());
                try {
                    client.Execute(WebserviceHelper.RequestMethod.POST);
                } catch (Exception e) {
                    _error = true;
                    _error_msg = e.getMessage();
                }
                response.add(client.getResponse());
            } catch (Exception e) {
                _error_msg = e.getMessage();
            }
        }


        return response;
        /*
        DownloadQuestionnaireAPITask _task = new DownloadQuestionnaireAPITask(_pDialog,_webserviceUrl,_tokenaccess);
        try {
            _task.execute(_allquestionnaire);
            ArrayList<String> result = _task.get();
            return result;
        } catch (Exception e){
            _task.cancel(true);
            throw new ApiException("Problem connecting to the server " + e.getMessage(), e);
        }
        */
    }
}
