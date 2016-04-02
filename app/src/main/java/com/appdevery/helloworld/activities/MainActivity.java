package com.appdevery.helloworld.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appdevery.helloworld.R;
import com.appdevery.helloworld.tasks.UserTasks;
import com.appdevery.helloworld.utils.ActionResponse;
import com.appdevery.helloworld.utils.TaskListener;

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

                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });

        Button buttonMe = (Button)findViewById(R.id.buttonMe);

        buttonMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserTasks.GetCurrentUser userTask = new UserTasks.GetCurrentUser(userService, new TaskListener<ActionResponse>() {
                    @Override
                    public void onFinished(final ActionResponse response) {
                        if(response.isSuccessful())
                        {
                        }
                    }
                });

                userTask.execute();
            }
        });

    }
}
