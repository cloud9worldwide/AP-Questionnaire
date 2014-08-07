package com.cloud9worldwide.questionnaire.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.cloud9worldwide.questionnaire.webservices.WebserviceHelper;

/**
 * Created by cloud9 on 8/7/14.
 */
public class DistrictAPITask extends AsyncTask<String, Integer, String> {
    private Context context;
    private ProgressDialog progDialog;
    private static final String debugTag = "DistrictAPITask";
    private String command = "district";

    public DistrictAPITask(Context _context) {
        super();
        this.context = _context;
    }
    protected void onPreExecute() {
        super.onPreExecute();
        //progDialog = ProgressDialog.show(this.context.getApplicationContext(), "LOGIN", "Waiting...", true, false);
    }
    protected String doInBackground(String... params) {
        Boolean _error = false;
        String _error_msg = null;
        String response = null;
        try {
            String _webserviceUrl = params[0];
            command = params[1];
            WebserviceHelper client = new WebserviceHelper(_webserviceUrl);
            client.AddParam("cmd", command);
            try {
                client.Execute(WebserviceHelper.RequestMethod.POST);
            } catch (Exception e) {
                _error = true;
                _error_msg = e.getMessage();
            }
            response = client.getResponse();
        } catch (Exception e) {
            _error_msg = e.getMessage();
        }
        if(!_error && response != null){
            return response;
        }else {
            return null;
        }
    }
    protected void onPostExecute(String result)
    {
        //progDialog.dismiss();
    }
}
