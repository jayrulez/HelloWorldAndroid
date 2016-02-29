package com.appdevery.helloworld;

import com.appdevery.helloworld.services.Exception.AuthenticationException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.appdevery.helloworld.services.AuthService;

public class LoginActivity extends BaseActivity {
    private static final String LOG_TAG = "LoginActivity";
    private TextView textViewLoginErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view)
    {
        EditText editTextUsername = (EditText)findViewById(R.id.login_username);
        String username = editTextUsername.getText().toString();

        EditText editTextPassword = (EditText)findViewById(R.id.login_password);
        String password = editTextPassword.getText().toString();

        textViewLoginErrorMessage = (TextView)findViewById(R.id.textView_login_error);

        Log.d(LOG_TAG, "Username: " + username);
        Log.d(LOG_TAG, "Password: " + password);

        LoginTask loginTask = new LoginTask();
        loginTask.execute(username, password);
    }

    private class LoginTask extends AsyncTask<String, String, Boolean>
    {
        private String errorMessage = null;

        @Override
        protected Boolean doInBackground(String... params)
        {
            try
            {
                return authService.authenticate(params[0], params[1]);
            }catch(AuthenticationException e)
            {
                errorMessage = e.getMessage();

                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                finish();
            }else{
                textViewLoginErrorMessage.setText(errorMessage != null ? errorMessage : "Unable to login user.");
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }
}
