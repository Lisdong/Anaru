package com.example.lrd.http;

/**
 * Created By LRD
 * on 2018/6/28
 */
public interface RequestCallback {
    void onSuccess(String response);

    void onError(String response);
}
