package com.appdevery.helloworld.utils;

/**
 * Created by robert on 3/3/2016.
 */
public interface TaskListener<ActionResponse> {
    public void onFinished(ActionResponse response);
}
