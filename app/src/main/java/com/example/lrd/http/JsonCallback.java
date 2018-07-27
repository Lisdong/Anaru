package com.example.lrd.http;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.Reader;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * Created By LRD
 * on 2018/6/28
 */
public abstract class JsonCallback<T> extends AbsCallback<T>{

    private Type mType;
    private Class<T> mClazz;

    public JsonCallback(Type type) {
        this.mType = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.mClazz = clazz;
    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) return null;
        T data = null;
        Reader reader = body.charStream();
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(reader);
        if (mType != null) data = gson.fromJson(jsonReader,mType);
        if (mClazz != null) data = gson.fromJson(jsonReader,mClazz);
        return data;
    }
}
