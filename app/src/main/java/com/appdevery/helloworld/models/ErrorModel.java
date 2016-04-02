package com.appdevery.helloworld.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by robert on 2/4/2016.
 */
public class ErrorModel {
    @SerializedName("error")
    private String Error;

    @SerializedName("error_description")
    private String ErrorDescription;

    public void setError(String error)
    {
        this.Error = error;
    }

    public void setErrorDescription(String errorDescription)
    {
        this.ErrorDescription = errorDescription;
    }

    public String getError()
    {
        return this.Error;
    }

    public String getErrorDescription()
    {
        return this.ErrorDescription;
    }
}
