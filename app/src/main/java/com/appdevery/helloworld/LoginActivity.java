package com.appdevery.helloworld;

import com.appdevery.helloworld.services.Exception.AuthenticationException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.appdevery.helloworld.services.AuthService;

public class LoginActivity extends BaseActivity {
    private static final String LOG_TAG = "LoginActivity";

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

        TextView textViewLoginErrorMessage = (TextView)findViewById(R.id.textView_login_error);

        Log.d(LOG_TAG, "Username: " + username);
        Log.d(LOG_TAG, "Password: " + password);

        try
        {
            if(authService.authenticate(username, password))
            {
                startActivity(new Intent(this, MainActivity.class));

                finish();
            }else{
                textViewLoginErrorMessage.setText("Unable to login user.");
            }
        }catch(AuthenticationException e)
        {
            textViewLoginErrorMessage.setText(e.getMessage());
        }
    }
}
