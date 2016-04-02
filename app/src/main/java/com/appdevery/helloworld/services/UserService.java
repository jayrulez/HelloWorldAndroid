package com.appdevery.helloworld.services;

import android.content.Context;
import android.util.Log;

import com.appdevery.helloworld.models.UserModel;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by robert on 2/4/2016.
 */
public class UserService  extends BaseService {
    private static final String LOG_TAG = "UserService";

    public UserService(Context context)
    {
        super(context);

        Log.d(LOG_TAG, "UserService created.");
    }

    public UserModel getCurrentUser()
    {
        UserModel model = new UserModel();

        try
        {
            Response response = apiClient.get("users/me", null);
        }catch(IOException e)
        {
            Log.d(LOG_TAG, e.getMessage());
        }

        return model;
    }
}
