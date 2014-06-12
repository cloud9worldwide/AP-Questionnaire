package com.cloud9worldwide.questionnaire.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.cloud9worldwide.questionnaire.webservices.WebserviceHelper;

/**
 * Created by cloud9 on 3/12/14.
 */
public class ForgotpasswordAPITask extends AsyncTask<String, Integer, String> {
    private Context context;
    private ProgressDialog progDialog;
    private static final String debugTag    = "ForgotpasswordAPITask";
    private static final String command     = "forgotpassword";

    public ForgotpasswordAPITask(Context _context) {
        super();
        this.context = _context;
    }
    protected void onPreExecute() {
        super.onPreExecute();
        //progDialog = ProgressDialog.show(this.context.getApplicationContext(), "FORGOT PASSWORD", "Waiting...", true, false);
    }
    protected String doInBackground(String... params) {
        Boolean _error = false;
        String _error_msg = null;
        String response = null;
        try {
            String _webserviceUrl = params[0];
            String _data = params[1];
            WebserviceHelper client = new WebserviceHelper(_webserviceUrl);
            client.AddParam("cmd", command);

            String str1 = "\\\"";
            String str2 = "\"";
            _data = _data.replaceAll(str1,str2);

            client.AddParam("data", _data);
            try {
                client.Execute(WebserviceHelper.RequestMethod.POST);
            } catch (Exception e) {
                _error_msg = e.getMessage();
            }
            response = client.getResponse();
        } catch (Exception e) {
            _error_msg = e.getMessage();
        }
        if(!_error){
            return response;
        }else {
            return "{\"status\":false,\"reuslt\":{\"message\":\""+_error_msg+"\"}}";
        }
    }
    protected void onPostExecute(String result)
    {
        //progDialog.dismiss();
    }
}
