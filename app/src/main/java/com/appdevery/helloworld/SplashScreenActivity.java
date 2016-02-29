package com.appdevery.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
            startActivity(new Intent(this, MainActivity.class));
        }else{
            Log.d(LOG_TAG, "Unauthenticated");
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish();
    }
}
