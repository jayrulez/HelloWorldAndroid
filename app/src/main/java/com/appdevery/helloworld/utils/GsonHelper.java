package com.appdevery.helloworld.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by robert on 2/4/2016.
 */
public class GsonHelper {
    public static <T> T deserialize(String json)
    {
        Gson gson = new Gson();
        T result = gson.fromJson(json, new TypeToken<T>() {} .getType());

        return result;
    }
}
