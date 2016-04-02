package com.appdevery.helloworld.utils;

import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by robert on 3/3/2016.
 */
public class ActionResponse<T> {
    private T result;
    private ArrayList<Error> errors;

    public ActionResponse()
    {
        errors = new ArrayList<Error>();
    }

    public void addError(String message)
    {
        Error error = new Error(message);

        errors.add(error);
    }

    public String getFirstError()
    {
        if(!errors.isEmpty())
        {
            return errors.get(0).getMessage();
        }

        return null;
    }

    public Boolean isSuccessful()
    {
        return errors == null || errors.isEmpty();
    }

    public void setResult(T result)
    {
        this.result = result;
    }

    public T GetResult()
    {
        return result;
    }
}
