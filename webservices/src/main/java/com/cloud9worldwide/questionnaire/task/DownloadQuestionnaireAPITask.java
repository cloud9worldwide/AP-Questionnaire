package com.cloud9worldwide.questionnaire.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cloud9worldwide.questionnaire.data.QuestionnaireData;
import com.cloud9worldwide.questionnaire.webservices.WebserviceHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cloud9 on 4/3/14.
 */
public class DownloadQuestionnaireAPITask extends AsyncTask <ArrayList<QuestionnaireData>, Integer,ArrayList<String>> {
    private Context context;
    private String webserviceUrl;
    private String tokenaccess;

    private ProgressDialog progDialog;
    private static final String debugTag = "APITask";
    private static final String command = "getquestionnaire";

    private int SIZE = 0;

    public DownloadQuestionnaireAPITask(ProgressDialog _pDialog,String _webserviceUrl,String _tokenaccess) {
        super();
        this.progDialog = _pDialog;
        this.webserviceUrl = _webserviceUrl;
        this.tokenaccess = _tokenaccess;
    }
    protected void onPreExecute() {
        super.onPreExecute();
        //progDialog = ProgressDialog.show(this.context.getApplicationContext(), "LOGIN", "Donwloading...", true, false);
    }
    protected void onPostExecute(ArrayList<String> result)
    {
        //progDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        String text = "Download questionnaire["+values[0]+ "/" + SIZE + "]";
        Log.d("Core",text);
        progDialog.setMessage(text);
        super.onProgressUpdate(values);
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<QuestionnaireData>... arrayLists) {
        Boolean _error = false;
        String _error_msg = null;
        ArrayList<String>  response = new ArrayList<String>();
        ArrayList<QuestionnaireData> _all_questionnaire = new ArrayList<QuestionnaireData>();
        _all_questionnaire = arrayLists[0];

        SIZE = _all_questionnaire.size();
        for (int i = 0; i < _all_questionnaire.size(); i++) {
            publishProgress(i);
            //Log.d("DowloadQuestionnaire","QuestionnaireId :: "+_all_questionnaire.get(i).getId());
            try {
                //String _data ="";
                JSONObject json = new JSONObject();
                json.put("questionnaireid",_all_questionnaire.get(i).getId());
                json.put("tokenaccess",this.tokenaccess);
                WebserviceHelper client = new WebserviceHelper(this.webserviceUrl);
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


        publishProgress(_all_questionnaire.size());
        return response;
    }
}
