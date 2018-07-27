package com.example.lrd.call;

/**
 * Created By LRD
 * on 2018/6/29
 */
public interface RequestBeanCallback<T>{
    void onSuccess(T bean);

    void onError(String error);
}
