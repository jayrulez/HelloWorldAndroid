package com.appdevery.helloworld.services;

import android.content.Context;

import com.appdevery.helloworld.utils.ApiClient;

/**
 * Created by robert on 29/2/2016.
 */
public abstract class BaseService {
    protected Context context;
    protected ApiClient apiClient;

    public BaseService(Context context)
    {
        this.context = context;
        apiClient = new ApiClient(this.context);
    }
}
