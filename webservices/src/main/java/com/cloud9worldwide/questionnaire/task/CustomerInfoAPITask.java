package com.cloud9worldwide.questionnaire.task;

import android.content.Context;
import android.os.AsyncTask;

import com.cloud9worldwide.questionnaire.webservices.WebserviceHelper;

/**
 * Created by cloud9 on 4/9/14.
 */
public class CustomerInfoAPITask extends AsyncTask<String, Integer, String> {
    private Context context;
    private static final String debugTag = "getcustomerprofileAPITask";
    private static final String command = "getcustomerprofile";

    public CustomerInfoAPITask(Context context) {
        this.context = context;
    }

    @Override
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
            return "{\"status\":false,\"reuslt\":{\"message\":\""+_error_msg+"\"}}";
        }
    }
}
