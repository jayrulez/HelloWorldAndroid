package com.appdevery.helloworld.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.appdevery.helloworld.services.AuthService;
import com.appdevery.helloworld.services.Exception.AuthenticationException;
import com.appdevery.helloworld.utils.Response;
import com.appdevery.helloworld.utils.TaskListener;

/**
 * Created by robert on 3/3/2016.
 */
public class SignUpTask extends AsyncTask<String, String, Response>
{
    private final TaskListener<Response> taskListener;
    private String errorMessage = null;
    private Context context;

    public SignUpTask(Context context, TaskListener<Response> taskListener)
    {
        this.context = context;
        this.taskListener = taskListener;
    }

    @Override
    protected Response doInBackground(String... params)
    {
        Response response = new Response<Boolean>();
        try
        {
            AuthService authService = new AuthService(context);
            if(authService.register(params[0], params[1]))
            {
                response.setResult(true);
            }else{
                response.addError("Unable to sign up user.");
                response.setResult(false);
            }
            return response;
        }catch(AuthenticationException e)
        {
            errorMessage = e.getMessage();

            response.addError(e.getMessage());
            response.setResult(false);
            return response;
        }
    }

    @Override
    protected void onPostExecute(Response response) {
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
