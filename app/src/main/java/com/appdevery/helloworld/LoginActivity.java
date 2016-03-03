package com.appdevery.helloworld;

import com.appdevery.helloworld.tasks.LoginTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appdevery.helloworld.utils.Response;
import com.appdevery.helloworld.utils.TaskListener;

public class LoginActivity extends BaseActivity {
    private static final String LOG_TAG = "LoginActivity";
    private String errorMessage;

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

                LoginTask loginTask = new LoginTask(authService, new TaskListener<Response>() {
                    @Override
                    public void onFinished(Response response) {
                        if(response.isSuccessful())
                        {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Context context = getApplicationContext();
                            String message = response.getFirstError();
                            if(message == null) {
                                message = "Unable to log in user.";
                            }
                            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });

                loginTask.execute(username, password);
            }
        });
    }
}
