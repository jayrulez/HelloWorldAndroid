package com.appdevery.helloworld.services;
import com.appdevery.helloworld.utils.PreferenceKeys;
import com.appdevery.helloworld.R;
import com.appdevery.helloworld.services.Exception.AuthenticationException;
import com.appdevery.helloworld.utils.ApiClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
    public AuthService(Context context)
    {
        super(context);

        Log.d(LOG_TAG, "AuthService created.");
    }

    protected SharedPreferences getAuthPreferences()
    {
        return context.getApplicationContext().getSharedPreferences(PreferenceKeys.AUTH_PREFERENCES, Context.MODE_PRIVATE);
    }

    public Response getToken(HashMap<String, String> credentials) throws IOException
    {
        return apiClient.post("public/login", credentials);
    }

    public boolean authenticate(String username, String password) throws AuthenticationException
    {
        try
        {   HashMap<String, String> formBody = new HashMap<String, String>();
            formBody.put("username", username.toLowerCase());
            formBody.put("password", password);
            formBody.put("client_id", context.getString(R.string.client_id));
            formBody.put("client_secret", context.getString(R.string.client_secret));
            formBody.put("grant_type", "password");

            Response response = getToken(formBody);

            Log.d(LOG_TAG, "Response Code: " + response.code());
            Log.d(LOG_TAG, "Request Url: " + response.request().url());

            if(response.isSuccessful())
            {
                String responseText = response.body().string();

                Log.d(LOG_TAG, "Response text: " + responseText);

                JSONObject jsonData = new JSONObject(responseText);

                jsonData = jsonData.getJSONObject("data");

                String accessToken = jsonData.getString("access_token");
                String refreshToken = jsonData.getString("refresh_token");

                Editor editor = this.getAuthPreferences().edit();
                editor.putString(PreferenceKeys.PREFERENCE_KEY_ACCESS_TOKEN, accessToken);
                editor.putString(PreferenceKeys.PREFERENCE_KEY_REFRESH_TOKEN, refreshToken);
                editor.putString(PreferenceKeys.PREFERENCE_KEY_IDENTITY, username);

                return editor.commit();
            }else{
                response.body().close();
                if(response.code() == 400)
                {
                    throw new AuthenticationException("Incorrect username or password.");
                }else{
                    throw new AuthenticationException("Unable to login at this time. Try again later.");
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

    public boolean register(String firstName, String lastName, String emailAddress, String countryCode, String mobileNumber, String password) throws AuthenticationException
    {
        try
        {
            HashMap<String, String> formBody = new HashMap<String, String>();
            formBody.put("first_name", firstName);
            formBody.put("last_name", lastName);
            formBody.put("email_address", emailAddress);
            formBody.put("country_code", countryCode);
            formBody.put("mobile_number", mobileNumber);
            formBody.put("password", password);
            formBody.put("client_id", context.getString(R.string.client_id));
            formBody.put("client_secret", context.getString(R.string.client_secret));
            formBody.put("grant_type", "client_credentials");

            Response response = apiClient.post("public/register", formBody);

            if(response.isSuccessful())
            {
                String responseText = response.body().string();

                Log.d(LOG_TAG, "Response text: " + responseText);

                JSONObject jsonData = new JSONObject(responseText);
                Boolean success = jsonData.getBoolean("success");

                if(success)
                {
                    jsonData = jsonData.getJSONObject("data");

                    try
                    {
                        String user = jsonData.getString("username");
                        return true;
                    }catch (JSONException e)
                    {
                        throw new AuthenticationException("Unable to parse response from server.");
                    }
                }else{
                    jsonData = jsonData.getJSONObject("data");

                    try
                    {
                        String error = jsonData.getString("error_description");
                        throw new AuthenticationException(error);
                    }catch (JSONException e)
                    {
                        throw new AuthenticationException("Unable to parse response from server.");
                    }
                }
            }else{
                response.body().close();
                Log.d(LOG_TAG, "Response Code: " + response.code());
                Log.d(LOG_TAG, "Request Url: " + response.request().url());
                throw new AuthenticationException("Unable to sign up at this time. Try again later.");
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
        String accessToken = this.getAuthPreferences().getString(PreferenceKeys.PREFERENCE_KEY_ACCESS_TOKEN, null);

        if(accessToken != null)
        {
            return true;
        }

        return false;
    }
}
