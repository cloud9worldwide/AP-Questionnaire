package com.cloud9worldwide.questionnaire.webservices;

import android.content.Context;
import android.util.Log;

/**
 * Created by koy on 27/02/15.
 */
public class NewVersion {
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
    public static synchronized String execute(Context _context, String... params) throws ApiException {
        Boolean _error = false;
        String _error_msg = null;
        String response = null;
        try {
            String _webserviceUrl = params[0];
            WebserviceHelper client = new WebserviceHelper(_webserviceUrl);
            try {
                client.Execute(WebserviceHelper.RequestMethod.GET);
            } catch (Exception e) {
                _error = true;
                _error_msg = e.getMessage();
            }
            response = client.getResponse();
        } catch (Exception e) {
            _error_msg = e.getMessage();
        }

        if(!_error ){
            if(response != null) {
                Log.e("YES",response);
                return response;
            } else {
                return "{\"status\":false,\"result\":{\"message\":\""+_error_msg+"\"}}";
            }
        } else {
            return "{\"status\":false,\"result\":{\"message\":\""+_error_msg+"\"}}";
        }
    }
}
