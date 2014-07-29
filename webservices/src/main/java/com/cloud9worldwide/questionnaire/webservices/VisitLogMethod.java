package com.cloud9worldwide.questionnaire.webservices;

import android.content.Context;

import com.cloud9worldwide.questionnaire.task.VisitLogTask;

/**
 * Created by cloud9 on 7/29/14.
 */
public class VisitLogMethod {
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
        VisitLogTask task = new VisitLogTask(_context);
        try {
            task.execute(params);
            return task.get();
        } catch (Exception e){
            task.cancel(true);
            throw new ApiException("Problem connecting to the server " + e.getMessage(), e);
        }
    }
}
