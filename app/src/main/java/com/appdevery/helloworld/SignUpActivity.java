package com.appdevery.helloworld;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appdevery.helloworld.services.Exception.AuthenticationException;

public class SignUpActivity extends BaseActivity {
    private static final String LOG_TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button buttonSignUp = (Button)findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextUsername = (EditText)findViewById(R.id.signUpUsername);
                String username = editTextUsername.getText().toString();

                EditText editTextPassword = (EditText)findViewById(R.id.signUpPassword);
                String password = editTextPassword.getText().toString();

                Log.d(LOG_TAG, "Username: " + username);
                Log.d(LOG_TAG, "Password: " + password);

                SignUpTask signUpTask = new SignUpTask();
                signUpTask.execute(username, password);
            }
        });
    }

    private class SignUpTask extends AsyncTask<String, String, Boolean>
    {
        private String errorMessage = null;

        @Override
        protected Boolean doInBackground(String... params)
        {
            try
            {
                return authService.register(params[0], params[1]);
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
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));

                SignUpActivity.this.finish();
            }else{
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, errorMessage != null ? errorMessage : "Unable to sign up user.", Toast.LENGTH_SHORT);
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
