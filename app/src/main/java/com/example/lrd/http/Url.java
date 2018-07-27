package com.example.lrd.http;

/**
 * Created By LRD
 * on 2018/6/28
 */
public class Url {
    private static final String BASE_URL;
    private static final String BASE_HOU;//后管

    static {
        switch (Environment.ENVIRONMENT) {
            case Environment.$Environment.ENVIRONMENT_INTERNAL:         //内网 开发
                BASE_URL = "";
                BASE_HOU = "http://10.16.80.67:9085/";
                break;
            case Environment.$Environment.ENVIRONMENT_EXTERNAL:          //外网 开发
                BASE_URL = "";
                BASE_HOU = "http://61.181.71.46:9085/";
                break;
            default:
                BASE_URL = "";
                BASE_HOU = "";
                break;
        }
    }

    public static final String UPDATE_APK = BASE_HOU + "clients/versionupdate";  //更新apk

    public static final String TRANS_GET_TAB_DATA = BASE_HOU + "clients/nav";  //获取导航标签数据
}
