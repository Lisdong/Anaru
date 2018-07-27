package com.example.lrd.call;

import android.graphics.Bitmap;

/**
 * Created By LRD
 * on 2018/7/2
 */
public interface ImageCallback {
    void onSuccess(Bitmap bitmap);
    void onError(String error);
}
