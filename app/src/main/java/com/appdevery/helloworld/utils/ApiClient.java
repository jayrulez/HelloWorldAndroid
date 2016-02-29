package com.appdevery.helloworld.utils;

import android.util.Log;

import com.loopj.android.http.*;
/**
 * Created by robert on 28/2/2016.
 */
public class ApiClient {
    private static final String LOG_TAG = "ApiClient";
    private static final String BASE_URL = "http://localhost:8000/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        url = getAbsoluteUrl(url);
        Log.d(LOG_TAG, "GET request to: " + url);
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        url = getAbsoluteUrl(url);
        Log.d(LOG_TAG, "POST request to: " + url);
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl)
    {
        return BASE_URL + relativeUrl;
    }
}
