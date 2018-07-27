package com.example.lrd.call;

import org.json.JSONObject;

/**
 * Created By LRD
 * on 2018/6/29
 */
public interface CryptoInterface {
    /**
     * 对请求报文进行加密操作
     */
    JSONObject encryptRequest(String url, JSONObject requestBody);

    /**
     * 对返回报文进行解密操作
     */
    String decryptResponse(String url, String responseBody);
}
