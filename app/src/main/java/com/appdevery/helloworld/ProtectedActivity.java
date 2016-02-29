package com.appdevery.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.appdevery.helloworld.services.AuthService;

/**
 * Created by robert on 28/2/2016.
 */
public abstract class ProtectedActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!authService.isAuthenticated())
        {
            Intent intent = new Intent(this, LoginActivity.class);

            startActivity(intent);
        }
    }
}
