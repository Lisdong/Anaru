package com.example.lrd.update;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import com.example.lrd.ui.MyApplication;
import com.example.lrd.utils.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * Created By LRD
 * on 2018/7/11
 */
public class DownloadSignatureService extends Service {
    // 文件下载路径
    private String APK_url = "";
    // 文件保存路径(如果有SD卡就保存SD卡,如果没有SD卡就保存到手机包名下的路径)
    private String APK_dir = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("--------->onCreate: ");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Logger.d("--------->onStart: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("--------->onStartCommand: ");

        APK_url = intent.getStringExtra("apk_url");
        initAPKDir();
        boolean b = fileIsExists(APK_dir + "lrd.apk");
        if (b){//如果存在文件，直接安装
            installApk(new File(APK_dir + "lrd.apk"));
        }else {//没有，直接下载
            DownFile(APK_url, "lrd.apk");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Logger.d("--------->onDestroy: ");
        super.onDestroy();

    }

    private void DownFile(final String apk_url, final String appName) {
        OkGo.<File>get(apk_url).tag("downForce").execute(new FileCallback(APK_dir,appName){
            @Override
            public void onSuccess(Response<File> response) {
                installApk(response.body());
            }

            @Override
            public void onError(Response<File> response) {
                ToastUtil.getInstance(DownloadSignatureService.this).showToast("下载失败！");
            }
        });
    }

    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(this, MyApplication.provider, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        getApplication().startActivity(intent);
        stopSelf();
    }
    /**
     * 创建路径的时候一定要用[/],不能使用[\],但是创建文件夹加文件的时候可以使用[\].
     * [/]符号是Linux系统路径分隔符,而[\]是windows系统路径分隔符 Android内核是Linux.
     */
    private void initAPKDir() {
        if (isHasSdcard()) {
            APK_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lrd/download/";// 保存到SD卡路径下
        } else{
            APK_dir = getApplicationContext().getFilesDir().getAbsolutePath() + "/lrd/download/";// 保存到app的包名路径下
        }
        File destDir = new File(APK_dir);
        if (!destDir.exists()) {// 判断文件夹是否存在
            destDir.mkdirs();
        }
    }
    // 判断是否插入SD卡
    private boolean isHasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    //判断文件是否存在
    public boolean fileIsExists(String strFile) {
        try {
            File f=new File(strFile);
            if(!f.exists()) {
                return false;
            }
        }catch (Exception e) {
            return false;
        }
        return true;
    }
}