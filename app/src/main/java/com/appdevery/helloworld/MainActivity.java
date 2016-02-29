package com.appdevery.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.appdevery.helloworld.services.AuthService;

public class MainActivity extends ProtectedActivity {
    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void logout(View view)
    {
        authService.logout();

        startActivity(new Intent(this, SplashScreenActivity.class));
    }
}
