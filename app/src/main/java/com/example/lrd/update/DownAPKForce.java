package com.example.lrd.update;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.lrd.ui.MyApplication;
import com.example.lrd.ui.base.BaseActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by LRD on 2017/9/23.
 * 强制更新
 */

public class DownAPKForce {
    private BaseActivity activity;
    // 文件下载路径
    private String APK_url = "";
    // 文件保存路径(如果有SD卡就保存SD卡,如果没有SD卡就保存到手机包名下的路径)
    private String APK_dir = "";
    private static DownAPKForce downAPKForce;

    public static DownAPKForce getDownAPKForce(){
        if (downAPKForce == null){
            downAPKForce = new DownAPKForce();
        }
        return downAPKForce;
    }
    private DownAPKForce(){}

    public void DownFile(final BaseActivity activity, String file_url, String target_name) {
        this.activity = activity;
        initAPKDir();// 创建保存路径
        OkGo.<File>get(file_url).tag("downForce").execute(new FileCallback(){
                    private MaterialDialog mDialog;
                    int totalSize = 0;
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        //boolean showMinMax = true;
                        mDialog = new MaterialDialog.Builder(activity)
                                .title("下载更新")
                                .content("正在下载中···")
                                .negativeText("退出下载")
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        OkGo.getInstance().cancelTag("downForce");
                                        activity.finish();
                                    }
                                })
                                .progress(false, totalSize, true)
                                .cancelable(false)
                                .show();
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        mDialog.setContent("下载完成！");
                        mDialog.dismiss();
                        Intent installIntent = new Intent(Intent.ACTION_VIEW);
                        String apkPath = response.body().getPath();
                        //----------------------------------------------
                        Uri uri = null;
                        if (Build.VERSION.SDK_INT >= 24) {
                            uri = FileProvider.getUriForFile(activity, MyApplication.provider, new File(apkPath));
                            installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }else{
                            uri = Uri.fromFile(new File(apkPath));
                            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        //----------------------------------------------
                        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                        // 震动提示
                        Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(100L);// 参数是震动时间(long类型)
                        activity.startActivity(installIntent);// 下载完成之后自动弹出安装界面
                        activity.finish();
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        int currentSize = Long.valueOf(progress.currentSize).intValue() ;
                        int totalSize = Long.valueOf(progress.totalSize).intValue() ;
                        int i = currentSize / 1024 /1024 ;
                        int i1 = totalSize / 1024 /1024;
                        mDialog.setMaxProgress(i1);
                        mDialog.setProgress(i );
                    }

                    @Override
                    public void onError(Response<File> response) {
                        mDialog.setContent("下载失败！");
                    }

                });
    }

    private void initAPKDir() {
        /**
         * 创建路径的时候一定要用[/],不能使用[\],但是创建文件夹加文件的时候可以使用[\].
         * [/]符号是Linux系统路径分隔符,而[\]是windows系统路径分隔符 Android内核是Linux.
         */
        if (isHasSdcard())// 判断是否插入SD卡
        {
            APK_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Club/download/";// 保存到SD卡路径下
        }
        else{
            APK_dir = activity.getApplicationContext().getFilesDir().getAbsolutePath() + "/Club/download/";// 保存到app的包名路径下
        }
        File destDir = new File(APK_dir);
        if (!destDir.exists()) {// 判断文件夹是否存在
            destDir.mkdirs();
        }
    }
    /**
     *
     * @Description:判断是否插入SD卡
     */
    private boolean isHasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
