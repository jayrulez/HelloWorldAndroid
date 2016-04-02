package com.appdevery.helloworld.activities;

import com.appdevery.helloworld.R;
import com.appdevery.helloworld.tasks.UserTasks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appdevery.helloworld.utils.ActionResponse;
import com.appdevery.helloworld.utils.TaskListener;

public class LoginActivity extends BaseActivity {
    private static final String LOG_TAG = "LoginActivity";
    EditText editTextUsername;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText)findViewById(R.id.loginUsername);
        editTextPassword = (EditText)findViewById(R.id.loginPassword);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            String username = extras.getString("username", null);
            if(username != null)
            {
                editTextUsername.setText(username);
            }

            String password = extras.getString("password", null);
            if (password != null)
            {
                editTextPassword.setText(password);
            }
        }

        Button buttonLogin = (Button)findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                Log.d(LOG_TAG, "Username: " + username);
                Log.d(LOG_TAG, "Password: " + password);

                UserTasks.Login loginTask = new UserTasks.Login(authService, new TaskListener<ActionResponse>() {
                    @Override
                    public void onFinished(ActionResponse response) {
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
