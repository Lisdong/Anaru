package com.example.lrd.call;

import com.lzy.okgo.model.Progress;

import java.io.File;

/**
 * Created By LRD
 * on 2018/7/10
 */
public interface MyFileCallback {
    void onSuccess(File file);
    void onProgress(Progress progress);
    void onError(String error);
}
