package com.appdevery.helloworld.services;

import android.content.Context;

/**
 * Created by robert on 29/2/2016.
 */
public abstract class BaseService {
    protected Context context;

    public BaseService(Context context)
    {
        this.context = context;
    }
}
