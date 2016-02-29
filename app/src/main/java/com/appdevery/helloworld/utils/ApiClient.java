package com.appdevery.helloworld.utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by robert on 28/2/2016.
 */
public class ApiClient {
    private static final String LOG_TAG = "ApiClient";
    private static final String BASE_URL = "http://api.appdevery.com/";

    private static OkHttpClient httpClient = new OkHttpClient();

    public static Response post(String url, RequestBody requestBody) throws IOException
    {
        url = getAbsoluteUrl(url);

        Log.d(LOG_TAG, "POST request to: " + url + ", params: " + requestBody != null? requestBody.toString() : "");

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        return httpClient.newCall(request).execute();
    }

    public static Response get(String url) throws IOException
    {
        url = getAbsoluteUrl(url);

        Log.d(LOG_TAG, "GET request to: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        return httpClient.newCall(request).execute();
    }

    private static String getAbsoluteUrl(String relativeUrl)
    {
        return BASE_URL + relativeUrl;
    }
}
