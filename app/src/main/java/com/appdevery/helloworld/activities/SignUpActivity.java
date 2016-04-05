package com.appdevery.helloworld.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.appdevery.helloworld.R;
import com.appdevery.helloworld.fragments.CountryCodePickerFragment;
import com.appdevery.helloworld.listeners.CountryPickerListener;
import com.appdevery.helloworld.tasks.UserTasks;
import com.appdevery.helloworld.utils.ActionResponse;
import com.appdevery.helloworld.utils.TaskListener;

public class SignUpActivity extends BaseFragmentActivity {
    private static final String LOG_TAG = "SignUpActivity";
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextEmailAddress;
    EditText editTextCountryCode;
    EditText editTextMobileNumber;
    EditText editTextPassword;
    private String errorMessage;
    final static int MODE_MOBILE_SIGNUP = 1;
    final static int MODE_EMAIL_SIGNUP = 2;
    int SignupMode = MODE_MOBILE_SIGNUP;
    Button toggleSignupModeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextFirstName = (EditText)findViewById(R.id.signUpFirstName);
        editTextLastName = (EditText)findViewById(R.id.signUpLastName);
        editTextEmailAddress = (EditText)findViewById(R.id.signUpEmailAddress);
        editTextCountryCode = (EditText)findViewById(R.id.signUpCountryCode);
        editTextMobileNumber = (EditText)findViewById(R.id.signUpMobileNumber);
        editTextPassword = (EditText)findViewById(R.id.signUpPassword);

        toggleSignupModeButton = (Button)findViewById(R.id.toggleSignUpMode);

        toggleFields(SignupMode);

        toggleSignupModeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                SignupMode = SignupMode == MODE_EMAIL_SIGNUP ? MODE_MOBILE_SIGNUP : MODE_EMAIL_SIGNUP;
                toggleFields(SignupMode);
            }
        });

        final CountryCodePickerFragment picker = new CountryCodePickerFragment();
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode) {
                editTextCountryCode.setText(dialCode);
                Toast.makeText(
                        SignUpActivity.this,
                        "Country Name: " + name + " - Code: " + code
                                + " - Currency: "
                                + CountryCodePickerFragment.getCurrencyCode(code) + " - Dial Code: " + dialCode,
                        Toast.LENGTH_SHORT).show();
                picker.dismiss();
            }
        });
        picker.show(getSupportFragmentManager(), "COUNTRY_CODE_PICKER");

        Button buttonSignUp = (Button)findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                final String emailAddress = editTextEmailAddress.getText().toString();
                final String countryCode = editTextCountryCode.getText().toString();
                final String mobileNumber = editTextMobileNumber.getText().toString();
                String password = editTextPassword.getText().toString();

                Log.d(LOG_TAG, "FirstName: " + firstName);
                Log.d(LOG_TAG, "LastName: " + lastName);
                Log.d(LOG_TAG, "EmailAddress: " + emailAddress);
                Log.d(LOG_TAG, "CountryCode: " + countryCode);
                Log.d(LOG_TAG, "MobileNumber: " + mobileNumber);
                Log.d(LOG_TAG, "Password: " + password);

                UserTasks.SignUp signUpTask = new UserTasks.SignUp(authService, new TaskListener<ActionResponse>() {
                    @Override
                    public void onFinished(final ActionResponse response) {
                        if(response.isSuccessful())
                        {
                            String username = editTextEmailAddress.getText().toString();
                            String password = editTextPassword.getText().toString();

                            UserTasks.Login loginTask = new UserTasks.Login(authService, new TaskListener<ActionResponse>() {
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
                signUpTask.execute(firstName, lastName, emailAddress, countryCode, mobileNumber, password);
            }
        });
    }

    public void toggleFields(int mode)
    {
        if(mode != MODE_MOBILE_SIGNUP)
        {
            editTextCountryCode.setVisibility(View.GONE);
            editTextMobileNumber.setVisibility(View.GONE);

            toggleSignupModeButton.setText("Sign up with mobile number");
        }else{
            editTextCountryCode.setVisibility(View.VISIBLE);
            editTextMobileNumber.setVisibility(View.VISIBLE);
        }

        if(mode != MODE_EMAIL_SIGNUP)
        {
            editTextEmailAddress.setVisibility(View.GONE);

            toggleSignupModeButton.setText("Sign up with email address");
        }else{
            editTextEmailAddress.setVisibility(View.VISIBLE);
        }
    }
}
