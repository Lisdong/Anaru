package com.example.lrd.utils;


import android.graphics.Bitmap;

import com.example.lrd.call.CryptoInterface;
import com.example.lrd.call.ImageCallback;
import com.example.lrd.call.MyFileCallback;
import com.example.lrd.call.RequestBeanCallback;
import com.example.lrd.http.RequestCallback;
import com.example.lrd.ui.MyApplication;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * Created By LRD
 * on 2018/6/28
 */
public class HttpManager {

    private volatile static HttpManager httpManager;
    private CryptoInterface cryptoInterface = null;//加密接口

    public static HttpManager getInstance(){
        if(httpManager == null){
            synchronized(HttpManager.class){
                if(httpManager == null){
                    httpManager = new HttpManager();
                }
            }
        }
        return httpManager;
    }

    private HttpManager(){}

    public void setCryptoInterface(CryptoInterface cryptoInterface) {
        this.cryptoInterface = cryptoInterface;
    }

    public void requestPostString(String url, Map<String, String> params, final RequestCallback callback){
        if (!DeviceUtils.isOpenNetwork(MyApplication.getInstance())){
            callback.onError("无法连接网络");
            return;
        }
        OkGo.<String>get(url).params(params).execute(new AbsCallback<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public String convertResponse(okhttp3.Response response) throws Throwable {
                ResponseBody body = response.body();
                if (body == null) return null;
                //以字符串的形式获得网络
                //result = body.string();
                //以字符流的形式获得网络
                //body.charStream();

                //以字节的形式获得网络
                // body.bytes();
                //以字节流的形式获得网络
                //body.byteStream();
                return body.string();
            }

            @Override
            public void onError(Response<String> response) {
                callback.onSuccess(response.body());
            }
        });
    }

    /**
     * post请求
     * @param url url
     * @param clazz javaBean
     * @param params 参数
     * @param callback 回调
     * @param <T> 泛型
     */
    public <T> void requestPost(String url, final Class clazz, Map<String, String> params, final RequestBeanCallback<T> callback){
        if (!DeviceUtils.isOpenNetwork(MyApplication.getInstance())){
            callback.onError("无法连接网络");
            return;
        }
        if (params == null) {
            params = new HashMap<>();
        }
        OkGo.<T>get(url).params(params).execute(new AbsCallback<T>() {
            @Override
            public void onSuccess(Response<T> response) {
                T body = response.body();
                callback.onSuccess(body);
            }

            @Override
            public T convertResponse(okhttp3.Response response) throws Throwable {
                ResponseBody body = response.body();
                if (body == null) return null;
                T data = null;
                Reader reader = body.charStream();
                Gson gson = new Gson();
                JsonReader jsonReader = new JsonReader(reader);
                if (clazz != null) data = gson.fromJson(jsonReader,clazz);
                return data;
            }

            @Override
            public void onError(Response<T> response) {
                String message = response.message();
                callback.onError(message);
            }
        });
    }

    /**
     * post请求
     * @param url url
     * @param type javaBean
     * @param params 参数
     * @param callback 回调
     * @param <T> 泛型
     */
    public <T> void requestPost(String url, final Type type, Map<String, String> params, final RequestBeanCallback<T> callback){
        if (!DeviceUtils.isOpenNetwork(MyApplication.getInstance())){
            callback.onError("无法连接网络");
            return;
        }
        if (params == null) {
            params = new HashMap<>();
        }
        OkGo.<T>get(url).params(params).execute(new AbsCallback<T>() {
            @Override
            public void onSuccess(Response<T> response) {
                T body = response.body();
                callback.onSuccess(body);
            }

            @Override
            public T convertResponse(okhttp3.Response response) throws Throwable {
                ResponseBody body = response.body();
                if (body == null) return null;
                T data = null;
                Reader reader = body.charStream();
                Gson gson = new Gson();
                JsonReader jsonReader = new JsonReader(reader);
                if (type != null) data = gson.fromJson(jsonReader,type);
                return data;
            }

            @Override
            public void onError(Response<T> response) {
                String message = response.message();
                callback.onError(message);
            }
        });
    }

    /**
     * get请求
     * @param url url
     * @param clazz javaBean
     * @param params 参数
     * @param callback 回调
     * @param <T> javaBean泛型
     */
    public <T> void requestGet(String url, final Class clazz, Map<String, String> params, final RequestBeanCallback<T> callback){
        if (!DeviceUtils.isOpenNetwork(MyApplication.getInstance())){
            callback.onError("无法连接网络");
            return;
        }
        if (params == null) {
            params = new HashMap<>();
        }
        OkGo.<T>get(url).params(params).execute(new AbsCallback<T>() {
            @Override
            public void onSuccess(Response<T> response) {
                T body = response.body();
                callback.onSuccess(body);
            }

            @Override
            public T convertResponse(okhttp3.Response response) throws Throwable {
                ResponseBody body = response.body();
                if (body == null) return null;
                T data = null;
                Reader reader = body.charStream();
                Gson gson = new Gson();
                JsonReader jsonReader = new JsonReader(reader);
                if (clazz != null) data = gson.fromJson(jsonReader,clazz);
                return data;
            }

            @Override
            public void onError(Response<T> response) {
                String message = response.message();
                callback.onError(message);
            }
        });
    }
    /**
     * get请求
     * @param url url
     * @param type 类型
     * @param params 参数
     * @param callback 回调
     * @param <T> 泛型
     */
    public <T> void requestGet(String url, final Type type, Map<String, String> params, final RequestBeanCallback<T> callback){
        if (!DeviceUtils.isOpenNetwork(MyApplication.getInstance())){
            callback.onError("无法连接网络");
            return;
        }
        if (params == null) {
            params = new HashMap<>();
        }
        OkGo.<T>get(url).params(params).execute(new AbsCallback<T>() {
            @Override
            public void onSuccess(Response<T> response) {
                T body = response.body();
                callback.onSuccess(body);
            }

            @Override
            public T convertResponse(okhttp3.Response response) throws Throwable {
                ResponseBody body = response.body();
                if (body == null) return null;
                T data = null;
                Reader reader = body.charStream();
                Gson gson = new Gson();
                JsonReader jsonReader = new JsonReader(reader);
                if (type != null) data = gson.fromJson(jsonReader,type);
                return data;
            }

            @Override
            public void onError(Response<T> response) {
                String message = response.message();
                callback.onError(message);
            }
        });
    }

    /**
     * 文件下载
     * @param url 地址
     * @param params 参数
     * @param callback 回调
     */
    public void downFile(String url, Map<String,String> params, final MyFileCallback callback){
        if (!DeviceUtils.isOpenNetwork(MyApplication.getInstance())){
            callback.onError("无法连接网络");
            return;
        }
        if (params == null){
            params = new HashMap<>();
        }
        OkGo.<File>get(url)
                .params(params)
                .execute(new FileCallback() {
                    @Override
                    public void onSuccess(Response<File> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        callback.onProgress(progress);
                    }

                    @Override
                    public void onError(Response<File> response) {
                       callback.onError(response.message());
                    }
                });
    }

    //下载图片
    public void downImage(String imageUrl,final ImageCallback callback){
        OkGo.<Bitmap>get(imageUrl).execute(new BitmapCallback() {
            @Override
            public void onSuccess(Response<Bitmap> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onError(Response<Bitmap> response) {
                callback.onError(response.message());
            }
        });
    }

    //加密
    public <T> void requestEncryptGetBean(String url, final Class clazz, Map<String, String> params, final RequestBeanCallback<T> callback){
        if (!DeviceUtils.isOpenNetwork(MyApplication.getInstance())){
            callback.onError("无法连接网络");
            return;
        }
        if (params == null) {
            params = new HashMap<>();
        } else {
            //对请求报文进行加密处理
            if (cryptoInterface != null) {
                JSONObject jsonObject = cryptoInterface.encryptRequest(url, mapToJson(params));
                params = jsonToMap(jsonObject);
            }
        }
        final String requestUrl = url;
        OkGo.<T>get(url).params(params).execute(new AbsCallback<T>() {
            @Override
            public void onSuccess(Response<T> response) {
                T body = response.body();
                callback.onSuccess(body);
            }

            @Override
            public T convertResponse(okhttp3.Response response) throws Throwable {
                ResponseBody body = response.body();
                if (body == null) return null;
                T data = null;
                Gson gson = new Gson();
                if (cryptoInterface != null) {
                    //对返回报文进行解密处理
                    String responseStr = body.string();
                    responseStr = cryptoInterface.decryptResponse(requestUrl, responseStr);
                    byte[] bytes = responseStr.getBytes("utf-8");
                    InputStream inputStream = new ByteArrayInputStream(bytes);
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    JsonReader jsonReader = new JsonReader(inputStreamReader);
                    if (clazz != null) data = gson.fromJson(jsonReader,clazz);
                }else {
                    Reader responseStr = body.charStream();
                    JsonReader jsonReader = new JsonReader(responseStr);
                    if (clazz != null) data = gson.fromJson(jsonReader,clazz);
                }
                return data;
            }

            @Override
            public void onError(Response<T> response) {
                String message = response.message();
                callback.onError(message);
            }
        });
    }

    private void checkResponseBody(Response<String> response, RequestCallback callback) {
    }

    /**
     * map转json格式
     */
    private JSONObject mapToJson(Map<String, String> map) {
        JSONObject jsonObject = new JSONObject();
        if (map == null) {
            return jsonObject;
        }
        for (String key : map.keySet()) {
            try {
                if (map.get(key).startsWith("[") && map.get(key).endsWith("]")) {
                    JSONArray jsonArray = new JSONArray(map.get(key));
                    jsonObject.put(key, jsonArray);
                } else if (map.get(key).startsWith("{") && map.get(key).endsWith("}")) {
                    JSONObject obj = new JSONObject(map.get(key));
                    jsonObject.put(key, obj);
                } else {
                    jsonObject.put(key, map.get(key));
                }

            } catch (JSONException e) {

            }
        }
        return jsonObject;
    }

    /**
     * json格式转map
     */
    private Map<String, String> jsonToMap(JSONObject json) {
        Map<String, String> map = new HashMap<String, String>();
        if (json == null) {
            return map;
        }
        Iterator iterator = json.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            map.put(key, json.optString(key));
        }
        return map;
    }
}
