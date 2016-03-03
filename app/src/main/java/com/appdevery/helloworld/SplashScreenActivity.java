package com.appdevery.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.appdevery.helloworld.services.AuthService;

public class SplashScreenActivity extends BaseActivity {
    private static final String LOG_TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(authService.isAuthenticated())
        {
            Log.d(LOG_TAG, "Authenticated");

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }

        Button buttonStartLogin = (Button)findViewById(R.id.buttonStartLogin);

        buttonStartLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            }
        });

        Button buttonStartSignUp = (Button)findViewById(R.id.buttonStartSignUp);

        buttonStartSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreenActivity.this, SignUpActivity.class));
            }
        });

    }
}
