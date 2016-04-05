package com.appdevery.helloworld.services;
import com.appdevery.helloworld.models.ApiResponseModel;
import com.appdevery.helloworld.models.ErrorModel;
import com.appdevery.helloworld.models.TokenModel;
import com.appdevery.helloworld.models.UserModel;
import com.appdevery.helloworld.utils.GsonHelper;
import com.appdevery.helloworld.utils.PreferenceKeys;
import com.appdevery.helloworld.R;
import com.appdevery.helloworld.services.Exception.AuthenticationException;
import com.appdevery.helloworld.utils.ApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
        return apiClient.post(ApiClient.ENDPOINT_TOKEN, credentials);
    }

    public boolean authenticate(String username, String password) throws AuthenticationException
    {
        try
        {   HashMap<String, String> formBody = new HashMap<String, String>();
            formBody.put(ApiClient.PARAM_USERNAME, username.toLowerCase());
            formBody.put(ApiClient.PARAM_PASSWORD, password);
            formBody.put(ApiClient.PARAM_CLIENT_ID, context.getString(R.string.client_id));
            formBody.put(ApiClient.PARAM_CLIENT_SECRET, context.getString(R.string.client_secret));
            formBody.put(ApiClient.PARAM_GRANT_TYPE, "password");

            Response response = getToken(formBody);
            String responseText = response.body().string();

            Log.d(LOG_TAG, "Response Text: " + responseText);
            Log.d(LOG_TAG, "Response Code: " + response.code());
            Log.d(LOG_TAG, "Request Url: " + response.request().url());

            Gson gson = new Gson();

            if(response.isSuccessful())
            {
                ApiResponseModel<TokenModel> apiResponse = gson.fromJson(responseText, new TypeToken<ApiResponseModel<TokenModel>>() {} .getType());

                String accessToken = apiResponse.getData().getAccessToken();
                String refreshToken = apiResponse.getData().getRefreshToken();

                Editor editor = this.getAuthPreferences().edit();
                editor.putString(PreferenceKeys.PREFERENCE_KEY_ACCESS_TOKEN, accessToken);
                editor.putString(PreferenceKeys.PREFERENCE_KEY_REFRESH_TOKEN, refreshToken);

                return editor.commit();
            }else{
                ApiResponseModel<ErrorModel> apiResponse = gson.fromJson(responseText, new TypeToken<ApiResponseModel<ErrorModel>>() {} .getType());

                Log.d(LOG_TAG, apiResponse.getData().getErrorDescription());

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
        }
    }

    public boolean register(String firstName, String lastName, String emailAddress, String countryCode, String mobileNumber, String password) throws AuthenticationException
    {
        try
        {
            HashMap<String, String> formBody = new HashMap<String, String>();
            formBody.put("first_name", firstName);
            formBody.put("last_name", lastName);
            if(emailAddress != null && !emailAddress.isEmpty())
            {
                formBody.put("email_address", emailAddress);
            }

            if(countryCode != null && !countryCode.isEmpty() && mobileNumber != null && !mobileNumber.isEmpty())
            {
                formBody.put("country_code", countryCode);
                formBody.put("mobile_number", mobileNumber);
            }

            formBody.put("password", password);
            formBody.put("client_id", context.getString(R.string.client_id));
            formBody.put("client_secret", context.getString(R.string.client_secret));
            formBody.put("grant_type", "client_credentials");

            Response response = apiClient.post("public/register", formBody);
            String responseText = response.body().string();

            Log.d(LOG_TAG, "Request Url: " + response.request().url());
            Log.d(LOG_TAG, "Response Text: " + responseText);
            Log.d(LOG_TAG, "Response Code: " + response.code());

            Gson gson = new Gson();

            if(response.isSuccessful())
            {
                ApiResponseModel<UserModel> apiResponse = gson.fromJson(responseText, new TypeToken<ApiResponseModel<UserModel>>() {} .getType());

                if(apiResponse.getSuccess())
                {
                    Log.d(LOG_TAG, "Registration was successful.");

                    return true;
                }else{
                    ApiResponseModel<ErrorModel> errorResponse = gson.fromJson(responseText, new TypeToken<ApiResponseModel<ErrorModel>>() {} .getType());

                    Log.d(LOG_TAG, errorResponse.getData().getErrorDescription());

                    throw new AuthenticationException(errorResponse.getData().getErrorDescription());
                }
            }else{
                ApiResponseModel<ErrorModel> errorResponse = gson.fromJson(responseText, new TypeToken<ApiResponseModel<ErrorModel>>() {} .getType());
                response.body().close();

                Log.d(LOG_TAG, errorResponse.getData().getErrorDescription());

                throw new AuthenticationException("Unable to sign up at this time. Try again later.");
            }
        }
        catch(IOException e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
            throw new AuthenticationException("Internet access is not available.");
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
