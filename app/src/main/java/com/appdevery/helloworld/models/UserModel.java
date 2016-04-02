package com.appdevery.helloworld.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by robert on 1/4/2016.
 */
public class UserModel {
    @SerializedName("id")
    private int Id;

    @SerializedName("username")
    private String Username;

    public int getId()
    {
        return this.Id;
    }

    public void setId(int id)
    {
        this.Id = id;
    }

    public String getUsername()
    {
        return this.Username;
    }

    public void setUsername(String username)
    {
        this.Username = username;
    }
}
