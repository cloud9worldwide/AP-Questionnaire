package com.cloud9worldwide.questionnaire.webservices;

import android.content.Context;

/**
 * Created by cloud9 on 3/11/14.
 */

public class LoginMethod {

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
        final String command = "login";
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
            return "{\"status\":false,\"result\":{\"message\":\""+_error_msg+"\"}}";
        }



        /*
        LoginAPITask loginTask = new LoginAPITask(_context);
        try {
            loginTask.execute(params);
            return loginTask.get();
        } catch (Exception e){
            loginTask.cancel(true);
            throw new ApiException("Problem connecting to the server " + e.getMessage(), e);
        }
        */
    }
}
