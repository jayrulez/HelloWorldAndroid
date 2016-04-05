package com.appdevery.helloworld.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.appdevery.helloworld.services.AuthService;
import com.appdevery.helloworld.services.UserService;

/**
 * Created by robert on 4/4/2016.
 */
public class BaseFragmentActivity extends FragmentActivity {
    protected AuthService authService;
    protected UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authService = new AuthService(this);
        userService = new UserService(this);
    }
}
