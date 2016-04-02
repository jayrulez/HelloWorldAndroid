package com.appdevery.helloworld.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by robert on 1/4/2016.
 */
public class TokenModel {
    @SerializedName("access_token")
    private String AccessToken;

    @SerializedName("refresh_token")
    private String RefreshToken;

    public void setAccessToken(String accessToken)
    {
        this.AccessToken = accessToken;
    }

    public String getAccessToken()
    {
        return this.AccessToken;
    }

    public void setRefreshToken(String refreshToken)
    {
        this.RefreshToken = refreshToken;
    }

    public String getRefreshToken()
    {
        return this.RefreshToken;
    }
}
