package com.appdevery.helloworld.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.appdevery.helloworld.R;
import com.appdevery.helloworld.services.BaseService;

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

    public ApiClient(Context context)
    {
        this.context = context;
        httpClient = createHttpClient();
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
                        }

                        Response response = chain.proceed(originalRequest);

                        if(response.code() == 400)
                        {
                            Log.d(LOG_TAG, "Unauthorized.");

                            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PreferenceKeys.AUTH_PREFERENCES, Context.MODE_PRIVATE);

                            if(preferences != null) {
                                String bearerToken = preferences.getString(PreferenceKeys.PREFERENCE_KEY_ACCESS_TOKEN, null);

                                if (bearerToken != null && !bearerToken.isEmpty()) {

                                    Log.d(LOG_TAG, "Adding authorization header: Authorization Bearer " + bearerToken);

                                    Request modifiedRequest = originalRequest.newBuilder()
                                            .addHeader(KEY_AUTHORIZATION, "Bearer " + bearerToken)
                                            .build();

                                    response = chain.proceed(modifiedRequest);
                                }else{
                                    Log.d(LOG_TAG, "No access token was found..");
                                }
                            }
                        }

                        return response;
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
