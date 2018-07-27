package com.example.lrd.http;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.example.lrd.ui.MyApplication;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;

/**
 * Created By LRD
 * on 2018/6/29
 */
public class Environment {
    /**
     * 当前安装包环境是否是Release版本
     */
    public static final boolean IS_RELEASE_VERSION;


    /**
     * 当前安装包环境渠道版本
     */
    public static final int CHANNEL;

    /**
     * 当前安装包环境版本
     */
    public static final int ENVIRONMENT;

    /**
     * 当前安装包版本名称
     */
    public static final String APP_VERSION_NAME;

    /**
     * 当前系统版本号
     */
    public static final int SYSTEM_VERSION_CODE;


    /**
     * 当前安装包版本号
     */
    public static final int APP_VERSION_CODE;

    /**
     * 状态栏高度
     */
    public static final int STATUS_BAR_HEIGHT;


    /**
     * 当前设备品牌机型号
     */
    public static final String MODEL;

    /**
     * fileProvider
     */
    public static final String FILE_PROVIDER_AUTHORITY = "com......fileProvider";


    static {
        PackageManager packageManager = MyApplication.getInstance().getPackageManager();
        ApplicationInfo appInfo = null;
        try {
            appInfo = packageManager.getApplicationInfo(MyApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
        } catch (Exception ignore) {

        }
        if (appInfo != null) {
            IS_RELEASE_VERSION = appInfo.metaData.getBoolean("RELEASE_VERSION");
            CHANNEL = appInfo.metaData.getInt("CHANNEL");
            ENVIRONMENT = appInfo.metaData.getInt("ENVIRONMENT");
        } else {
            IS_RELEASE_VERSION = false;
            CHANNEL = $Channel.CHANNEL_OFFICIAL;
            ENVIRONMENT = $Environment.ENVIRONMENT_EXTERNAL;//....
        }
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
        } catch (Exception ignore) {
        }
        if (packageInfo != null) {
            APP_VERSION_CODE = packageInfo.versionCode;
            APP_VERSION_NAME = packageInfo.versionName;
        } else {
            APP_VERSION_CODE = 0;
            APP_VERSION_NAME = "0.0.0";
        }
        SYSTEM_VERSION_CODE = Build.VERSION.SDK_INT;
        MODEL = Build.BRAND + " " + android.os.Build.MODEL;
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int resId = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = MyApplication.getInstance().getResources().getDimensionPixelSize(resId);
        } catch (Exception ignored) {

        }
        STATUS_BAR_HEIGHT = statusBarHeight;
        Logger.d("当前环境为："+ENVIRONMENT +"    (0: 87测试，1: 85开发)");
    }

    public interface $Channel {
        int CHANNEL_OFFICIAL = 0;
    }

    public interface $Environment {
        int ENVIRONMENT_EXTERNAL = 0;
        int ENVIRONMENT_INTERNAL = 1;
    }
}
