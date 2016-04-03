package com.appdevery.helloworld.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.appdevery.helloworld.R;
import com.appdevery.helloworld.models.ApiResponseModel;
import com.appdevery.helloworld.models.TokenModel;
import com.appdevery.helloworld.services.BaseService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by robert on 28/2/2016.
 */
public class ApiClient {
    private static final String KEY_AUTHORIZATION = "Authorization";
    private static final String LOG_TAG = "ApiClient";
    private static final String BASE_URL = "http://api.appdevery.com/app_dev.php/v1/";
    private Context context;
    private OkHttpClient httpClient;

    public static final String PARAM_CLIENT_ID = "client_id";
    public static final String PARAM_CLIENT_SECRET = "client_secret";
    public static final String PARAM_GRANT_TYPE = "grant_type";
    public static final String PARAM_REFRESH_TOKEN = "refresh_token";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";

    public static final String ENDPOINT_TOKEN = "public/login";

    public ApiClient(Context context)
    {
        this.context = context;
        httpClient = createHttpClient();
    }

    private OkHttpClient getInternalClient()
    {
        return new OkHttpClient.Builder().build();
    }

    private OkHttpClient createHttpClient()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest    = chain.request();
                        String authorizationHeader = originalRequest.headers().get(KEY_AUTHORIZATION);

                        if(authorizationHeader != null && !authorizationHeader.isEmpty())
                        {
                            Log.d(LOG_TAG, "Authorization header present.");
                            return chain.proceed(originalRequest);
                        }else{
                            Log.d(LOG_TAG, "Authorization header is not present.");

                            SharedPreferences authPreferences = context.getApplicationContext().getSharedPreferences(PreferenceKeys.AUTH_PREFERENCES, Context.MODE_PRIVATE);

                            if(authPreferences != null) {
                                String accessToken = authPreferences.getString(PreferenceKeys.PREFERENCE_KEY_ACCESS_TOKEN, null);
                                String refreshToken = authPreferences.getString(PreferenceKeys.PREFERENCE_KEY_REFRESH_TOKEN, null);

                                if (accessToken != null && !accessToken.isEmpty()) {

                                    Log.d(LOG_TAG, "Adding authorization header: Authorization Bearer " + accessToken);

                                    Request modifiedRequest = originalRequest.newBuilder()
                                            .addHeader(KEY_AUTHORIZATION, "Bearer " + accessToken)
                                            .build();

                                    Response response = chain.proceed(modifiedRequest);

                                    Log.d(LOG_TAG, "Response code: " + response.code());

                                    if(response.code() == 401)
                                    {
                                        Log.d(LOG_TAG, "Unauthorized, token may have expired.");

                                        if(refreshToken != null && !refreshToken.isEmpty())
                                        {
                                            //refresh the token and try again
                                            FormBody.Builder builder = new FormBody.Builder();

                                            builder.add(ApiClient.PARAM_CLIENT_ID, context.getString(R.string.client_id));
                                            builder.add(ApiClient.PARAM_CLIENT_SECRET, context.getString(R.string.client_secret));
                                            builder.add(ApiClient.PARAM_REFRESH_TOKEN, refreshToken);
                                            builder.add(ApiClient.PARAM_GRANT_TYPE, "refresh_token");

                                            RequestBody requestBody = builder.build();

                                            Request refreshTokenRequest = new Request.Builder()
                                                    .url(getAbsoluteUrl(ApiClient.ENDPOINT_TOKEN))
                                                    .post(requestBody)
                                                    .build();

                                            try {
                                                Log.d(LOG_TAG, "Refreshing token.");

                                                Response refreshTokenResponse = getInternalClient().newCall(refreshTokenRequest).execute();

                                                Gson gson = new Gson();

                                                if(refreshTokenResponse.isSuccessful())
                                                {
                                                    String responseText = refreshTokenResponse.body().string();

                                                    ApiResponseModel<TokenModel> apiResponse = gson.fromJson(responseText, new TypeToken<ApiResponseModel<TokenModel>>() {} .getType());

                                                    accessToken = apiResponse.getData().getAccessToken();
                                                    refreshToken = apiResponse.getData().getRefreshToken();

                                                    SharedPreferences.Editor editor = authPreferences.edit();

                                                    editor.putString(PreferenceKeys.PREFERENCE_KEY_ACCESS_TOKEN, accessToken);
                                                    editor.putString(PreferenceKeys.PREFERENCE_KEY_REFRESH_TOKEN, refreshToken);

                                                    editor.commit();

                                                    modifiedRequest = modifiedRequest.newBuilder()
                                                            .addHeader(KEY_AUTHORIZATION, "Bearer " + accessToken)
                                                            .build();

                                                    Log.d(LOG_TAG, "New access token: " + accessToken);
                                                    return chain.proceed(modifiedRequest);
                                                }else{
                                                    Log.d(LOG_TAG, "Response body: " + refreshTokenResponse.body().string());
                                                    Log.d(LOG_TAG, "Request to refresh token failed.");
                                                    Log.d(LOG_TAG, "Response code: " + refreshTokenResponse.code());
                                                }
                                            }catch(Exception e)
                                            {
                                                Log.d(LOG_TAG, "Request to refresh token failed with exception: " + e.getMessage());
                                            }

                                        }else{
                                            Log.d(LOG_TAG, "No refresh token was found.");
                                        }
                                    }else{
                                        return response;
                                    }
                                }else{
                                    Log.d(LOG_TAG, "No access token was found.");
                                }
                            }

                            Log.d(LOG_TAG, "Proceeding without authorization.");

                            return chain.proceed(originalRequest);
                        }
                    }
                })
                .build();

        return client;
    }

    public Response post(String url, HashMap<String, String> formBody) throws IOException
    {
        FormBody.Builder builder = new FormBody.Builder();

        if(formBody != null)
        {
            for(Map.Entry<String, String> entry : formBody.entrySet())
            {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = builder.build();

        url = getAbsoluteUrl(url);

        Log.d(LOG_TAG, "POST request to: " + url + ", params: " + requestBody != null? requestBody.toString() : "");

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        return httpClient.newCall(request).execute();
    }

    public Response get(String url, HashMap<String, String> queryParams) throws IOException
    {
        url = getAbsoluteUrl(url);

        HttpUrl httpUrl = HttpUrl.parse(url);

        HttpUrl.Builder builder = httpUrl.newBuilder();
        if(queryParams != null)
        {
            for(Map.Entry<String, String> entry : queryParams.entrySet())
            {
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        httpUrl = builder.build();
        url = httpUrl.toString();

        Log.d(LOG_TAG, "GET request to: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        return httpClient.newCall(request).execute();
    }

    private String getAbsoluteUrl(String relativeUrl)
    {
        return BASE_URL + relativeUrl;
    }
}
