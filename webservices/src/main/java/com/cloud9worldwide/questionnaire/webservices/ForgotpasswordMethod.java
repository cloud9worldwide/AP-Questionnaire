package com.cloud9worldwide.questionnaire.webservices;

import android.app.Activity;
import android.content.Context;

import com.cloud9worldwide.questionnaire.task.ForgotpasswordAPITask;

/**
 * Created by cloud9 on 3/12/14.
 */
public class ForgotpasswordMethod {
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
        ForgotpasswordAPITask forgotTask = new ForgotpasswordAPITask(_context);
        try {
            forgotTask.execute(params);
            return forgotTask.get();
        } catch (Exception e){
            forgotTask.cancel(true);
            throw new ApiException("Problem connecting to the server " + e.getMessage(), e);
        }
    }
}
