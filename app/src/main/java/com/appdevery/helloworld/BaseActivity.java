package com.appdevery.helloworld;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.appdevery.helloworld.services.AuthService;

/**
 * Created by robert on 28/2/2016.
 */
public class BaseActivity extends AppCompatActivity {
    protected AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authService = new AuthService(this);
    }
}