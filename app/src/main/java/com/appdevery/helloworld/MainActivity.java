package com.appdevery.helloworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appdevery.helloworld.services.AuthService;

public class MainActivity extends ProtectedActivity {
    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLogout = (Button)findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authService.logout();
                startActivity(new Intent(MainActivity.this, SplashScreenActivity.class));
                MainActivity.this.finish();
            }
        });
    }
}
