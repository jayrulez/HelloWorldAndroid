package com.appdevery.helloworld.services;

import com.appdevery.*;
import com.appdevery.helloworld.R;
import com.appdevery.helloworld.services.Exception.AuthenticationException;
import com.appdevery.helloworld.utils.ApiClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.prefs.Preferences;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;


/**
 * Created by robert on 27/2/2016.
 */
public class AuthService extends BaseService {
    private static final String LOG_TAG = "AuthService";
    private static final String AUTH_PREFERENCES = "Auth";
    private static final String PREFERENCE_KEY_ACCESS_TOKEN = "access_token";
    private static final String PREFERENCE_KEY_REFRESH_TOKEN = "refresh_token";
    private static final String PREFERENCE_KEY_IDENTITY = "identity";

    public AuthService(Context context)
    {
        super(context);

        Log.d(LOG_TAG, "AuthService created.");
    }

    private SharedPreferences getAuthPreferences()
    {
        return context.getSharedPreferences(AuthService.AUTH_PREFERENCES, Context.MODE_PRIVATE);
    }

    public boolean authenticate(String username, String password) throws AuthenticationException
    {
        try
        {
            RequestBody requestBody = new FormBody.Builder()
                    .add("grant_type", "password")
                    .add("username", username.toLowerCase())
                    .add("password", password)
                    .add("client_id", context.getString(R.string.client_id))
                    .add("client_secret", context.getString(R.string.client_secret))
                    .build();

            Response response = ApiClient.post("api/oauth2/token", requestBody);

            if(response.isSuccessful())
            {
                String responseText = response.body().string();

                Log.d(LOG_TAG, "Response text: " + responseText);

                JSONObject jsonData = new JSONObject(responseText);

                String accessToken = jsonData.getString("access_token");
                String refreshToken = jsonData.getString("refresh_token");

                Editor editor = this.getAuthPreferences().edit();
                editor.putString(AuthService.PREFERENCE_KEY_ACCESS_TOKEN, accessToken);
                editor.putString(AuthService.PREFERENCE_KEY_REFRESH_TOKEN, refreshToken);
                editor.putString(AuthService.PREFERENCE_KEY_IDENTITY, username);

                return editor.commit();
            }else{
                response.body().close();
                Log.d(LOG_TAG, "Response Code: " + response.code());
                if(response.code() == 400)
                {
                    throw new AuthenticationException("Incorrect username or password.");
                }else{
                    throw new AuthenticationException("Unable to log you in at this time. Try again later.");
                }
            }
        }
        catch(IOException e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
            throw new AuthenticationException("Internet access is not available.");
        }catch(JSONException e)
        {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
            throw new AuthenticationException("Unable to login user.");
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
