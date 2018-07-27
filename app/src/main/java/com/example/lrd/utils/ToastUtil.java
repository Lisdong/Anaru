package com.example.lrd.utils;

import android.content.Context;
import android.widget.Toast;
/**
 * Created by LRD on 2017/4/24.
 */

public class ToastUtil {
    private Toast mToast;
    private static ToastUtil mToastUtil;

    private ToastUtil(Context context) {
        mToast = Toast.makeText(context.getApplicationContext(), null, Toast.LENGTH_SHORT);
    }

    public static synchronized ToastUtil getInstance(Context context) {
        if (null == mToastUtil) {
            mToastUtil = new ToastUtil(context);
        }
        return mToastUtil;
    }
    /**
     * 显示toast
     *
     * @param toastMsg
     */
    public void showToast(int toastMsg) {
        mToast.setText(toastMsg);
        mToast.show();
    }

    /**
     * 显示toast
     *
     * @param toastMsg
     */
    public void showToast(String toastMsg) {
        mToast.setText(toastMsg);
        mToast.show();
    }

    /**
     * 取消toast，在activity的destory方法中调用
     */
    public void destroy() {
        if (null != mToast) {
            mToast.cancel();
            mToast = null;
        }
        mToastUtil = null;
    }
}
