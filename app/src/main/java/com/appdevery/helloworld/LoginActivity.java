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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appdevery.helloworld.services.AuthService;

public class LoginActivity extends BaseActivity {
    private static final String LOG_TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button buttonLogin = (Button)findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextUsername = (EditText)findViewById(R.id.loginUsername);
                String username = editTextUsername.getText().toString();

                EditText editTextPassword = (EditText)findViewById(R.id.loginPassword);
                String password = editTextPassword.getText().toString();

                Log.d(LOG_TAG, "Username: " + username);
                Log.d(LOG_TAG, "Password: " + password);

                LoginTask loginTask = new LoginTask();
                loginTask.execute(username, password);
            }
        });
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

                LoginActivity.this.finish();
            }else{
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, errorMessage != null ? errorMessage : "Unable to login user.", Toast.LENGTH_SHORT);
                toast.show();
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
