package com.appdevery.helloworld.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appdevery.helloworld.R;
import com.appdevery.helloworld.tasks.LoginTask;
import com.appdevery.helloworld.tasks.SignUpTask;
import com.appdevery.helloworld.utils.ActionResponse;
import com.appdevery.helloworld.utils.TaskListener;

public class SignUpActivity extends BaseActivity {
    private static final String LOG_TAG = "SignUpActivity";
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextEmailAddress;
    EditText editTextPassword;
    private String errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button buttonSignUp = (Button)findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextFirstName = (EditText)findViewById(R.id.signUpFirstName);
                String firstName = editTextFirstName.getText().toString();

                editTextLastName = (EditText)findViewById(R.id.signUpLastName);
                String lastName = editTextLastName.getText().toString();

                editTextEmailAddress = (EditText)findViewById(R.id.signUpEmailAddress);
                final String emailAddress = editTextEmailAddress.getText().toString();

                editTextPassword = (EditText)findViewById(R.id.signUpPassword);
                String password = editTextPassword.getText().toString();

                Log.d(LOG_TAG, "FirstName: " + firstName);
                Log.d(LOG_TAG, "LastName: " + lastName);
                Log.d(LOG_TAG, "EmailAddress: " + emailAddress);
                Log.d(LOG_TAG, "Password: " + password);

                SignUpTask signUpTask = new SignUpTask(authService, new TaskListener<ActionResponse>() {
                    @Override
                    public void onFinished(final ActionResponse response) {
                        if(response.isSuccessful())
                        {
                            String username = editTextEmailAddress.getText().toString();
                            String password = editTextPassword.getText().toString();

                            LoginTask loginTask = new LoginTask(authService, new TaskListener<ActionResponse>() {
                                @Override
                                public void onFinished(ActionResponse response) {
                                    if(response.isSuccessful())
                                    {
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }else{

                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        intent.putExtra("username", SignUpActivity.this.editTextEmailAddress.getText().toString());
                                        intent.putExtra("password", SignUpActivity.this.editTextPassword.getText().toString());

                                        startActivity(intent);
                                    }
                                }
                            });

                            loginTask.execute(emailAddress, password);
                        }else{
                            Context context = getApplicationContext();
                            String message = response.getFirstError();
                            if(message == null) {
                                message = "Unable to sign up user.";
                            }
                            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
                signUpTask.execute(firstName, lastName, emailAddress, "", "", password);
            }
        });
    }
}
