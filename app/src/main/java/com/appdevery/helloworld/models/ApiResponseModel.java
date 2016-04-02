package com.appdevery.helloworld.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by robert on 2/4/2016.
 */
public class ApiResponseModel<T> {
    @SerializedName("success")
    private Boolean Success;

    @SerializedName("data")
    T Data;

    public void setSuccess(Boolean success)
    {
        this.Success = success;
    }

    public Boolean getSuccess()
    {
        return this.Success;
    }

    public void setData(T data)
    {
        this.Data = data;
    }

    public T getData()
    {
        return this.Data;
    }

    public ApiResponseModel()
    {
    }
}
