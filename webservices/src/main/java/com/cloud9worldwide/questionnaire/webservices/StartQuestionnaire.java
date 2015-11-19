package com.cloud9worldwide.questionnaire.webservices;

import android.content.Context;

import com.cloud9worldwide.questionnaire.task.StartQuestionnaireTask;

/**
 * Created by koy on 20/11/15.
 */
public class StartQuestionnaire {
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
        StartQuestionnaireTask task = new StartQuestionnaireTask(_context);
        try {
            task.execute(params);
            return task.get();
        } catch (Exception e){
            task.cancel(true);
            throw new ApiException("Problem connecting to the server " + e.getMessage(), e);
        }
    }
}
