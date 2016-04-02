package com.appdevery.helloworld.utils;

import com.google.gson.Gson;

/**
 * Created by robert on 2/4/2016.
 */
public class GsonHelper<T> {
    private Class<T> type;

    public GsonHelper(Class<T> type)
    {
        this.type = type;
    }

    public T deserialize(String json)
    {
        Gson gson = new Gson();

        T result = gson.fromJson(json, this.type);

        return result;
    }
}
