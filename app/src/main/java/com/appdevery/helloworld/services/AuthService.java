package com.appdevery.helloworld.services;

import com.appdevery.*;
import com.appdevery.helloworld.R;
import com.appdevery.helloworld.services.Exception.AuthenticationException;
import com.appdevery.helloworld.utils.ApiClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import org.json.JSONObject;

import java.util.prefs.Preferences;

import cz.msebera.android.httpclient.Header;

/**
 * Created by robert on 27/2/2016.
 */
public class AuthService {
    private static final String LOG_TAG = "AuthService";
    private static final String AUTH_PREFERENCES = "Auth";
    private static final String PREFERENCE_KEY_ACCESS_TOKEN = "access_token";
    private static final String PREFERENCE_KEY_IDENTITY = "identity";

    private Context context;

    public AuthService(Context context)
    {
        this.context = context;
        Log.d(LOG_TAG, "AuthService created.");
    }

    private SharedPreferences getAuthPreferences()
    {
        return this.context.getSharedPreferences(AuthService.AUTH_PREFERENCES, Context.MODE_PRIVATE);
    }

    public boolean authenticate(String username, String password) throws AuthenticationException
    {
        try
        {
            RequestParams params = new RequestParams();
            params.put("grant_type", "password");
            params.put("username", username.toLowerCase());
            params.put("password", password);
            params.put("client_id", R.string.client_id);
            params.put("client_secret", R.string.client_secret);

            ApiClient.get("api/oauth2/token", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(LOG_TAG, response.toString());
                    // If the response is JSONObject instead of expected JSONArray
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(LOG_TAG, "Status Code: " + statusCode + "\nMessage: " + responseString);
                }

                @Override
                public  void onFailure(int statusCode, Header[] header, Throwable throwable, JSONObject jsonObject)
                {
                    Log.d(LOG_TAG, "Status Code: " + statusCode + "\nMessage: " + jsonObject.toString());
                }
            });
        }catch(Exception e)
        {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
        }

        if(username.toLowerCase().equals("admin") && password.equals("admin"))
        {
            Editor editor = this.getAuthPreferences().edit();
            editor.putString(AuthService.PREFERENCE_KEY_ACCESS_TOKEN, "token");
            editor.putString(AuthService.PREFERENCE_KEY_IDENTITY, username);

            return editor.commit();
        }else {
            throw new AuthenticationException("Username or password is incorrect.");
        }
    }

    public void logout()
    {
        Log.d(LOG_TAG, "Logging out.");

        Editor editor = this.getAuthPreferences().edit();

        editor.clear();

        editor.commit();
    }

    public boolean isAuthenticated()
    {
        String accessToken = this.getAuthPreferences().getString(AuthService.PREFERENCE_KEY_ACCESS_TOKEN, null);

        if(accessToken != null)
        {
            return true;
        }

        return false;
    }
}
