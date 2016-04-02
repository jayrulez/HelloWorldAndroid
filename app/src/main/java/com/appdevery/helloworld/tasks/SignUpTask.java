package com.appdevery.helloworld.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.appdevery.helloworld.services.AuthService;
import com.appdevery.helloworld.services.Exception.AuthenticationException;
import com.appdevery.helloworld.utils.ActionResponse;
import com.appdevery.helloworld.utils.TaskListener;

/**
 * Created by robert on 3/3/2016.
 */
public class SignUpTask extends AsyncTask<String, String, ActionResponse>
{
    private final TaskListener<ActionResponse> taskListener;
    private AuthService authService;

    public SignUpTask(AuthService authService, TaskListener<ActionResponse> taskListener)
    {
        this.authService = authService;
        this.taskListener = taskListener;
    }

    @Override
    protected ActionResponse doInBackground(String... params)
    {
        ActionResponse response = new ActionResponse<Boolean>();
        try
        {
            if(authService.register(params[0], params[1], params[2], params[3], params[4], params[5]))
            {
                response.setResult(true);
            }else{
                response.addError("Unable to sign up user.");
                response.setResult(false);
            }
            return response;
        }catch(AuthenticationException e)
        {
            response.addError(e.getMessage());
            response.setResult(false);
            return response;
        }
    }

    @Override
    protected void onPostExecute(ActionResponse response) {
        super.onPostExecute(response);

        if(taskListener != null)
        {
            taskListener.onFinished(response);
        }
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(String... text) {
    }
}
