package com.appdevery.helloworld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by robert on 28/2/2016.
 */
public abstract class ProtectedActivity extends BaseActivity {
    private static final String LOG_TAG = "BaseActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!authService.isAuthenticated())
        {
            Log.d(LOG_TAG, "Unauthenticated, starting SplashActivity");

            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }
}
