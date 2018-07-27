package com.example.lrd.update;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import com.example.lrd.R;
import com.example.lrd.ui.MyApplication;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.DecimalFormat;

/**
 *  * @Title:DownAPKService.java
 * @Description:专用下载APK文件Service工具类,通知栏显示进度,下载完成震动提示,并自动打开安装界面(配合xUtils快速开发框架)
 *
 * 需要添加权限：
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.VIBRATE" />
 *
 * 需要在<application></application>标签下注册服务<service android:name=".update.DownAPKService"/>
 *
 * 可以在142行代码：builder.setSmallIcon(R.drawable.ic_launcher);中修改自己应用的图标
 * Created by LRD on 2017/9/22.
 */
public  class DownAPKService extends Service{
    private final int NotificationID = 0x10000;
    private NotificationManager mNotificationManager = null;
    private NotificationCompat.Builder builder;
//    private HttpHandler<File> mDownLoadHelper;

    // 文件下载路径
    private String APK_url = "";
    // 文件保存路径(如果有SD卡就保存SD卡,如果没有SD卡就保存到手机包名下的路径)
    private String APK_dir = "";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //进行EventBus的注册
        //EventBus.getDefault().register(this);
        initAPKDir();// 创建保存路径
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //System.out.println("onStartCommand");
        // 接收Intent传来的参数:
        APK_url = intent.getStringExtra("apk_url");

        DownFile(APK_url,APK_dir + "lrd.apk");
       // DownFile(<span style="font-family: Arial, Helvetica, sans-serif;">APK_url </span>, APK_dir + "Club.apk");

        return super.onStartCommand(intent, flags, startId);
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
            APK_dir = getApplicationContext().getFilesDir().getAbsolutePath() + "/Club/download/";// 保存到app的包名路径下
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

    private void DownFile(final String file_url, String target_name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkGo.<File>get(file_url).tag(this)
                        .execute(new FileCallback(APK_dir,"abc.apk"){
                            @Override
                            public void onStart(Request<File, ? extends Request> request) {
                                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                    NotificationChannel mChannel = new NotificationChannel("1","Channel1", NotificationManager.IMPORTANCE_DEFAULT);
                                    mChannel.enableLights(true); //是否在桌面icon右上角展示小红点
                                    mChannel.setLightColor(Color.GREEN); //小红点颜色
                                    mChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                                    mNotificationManager.createNotificationChannel(mChannel);
                                    builder = new NotificationCompat.Builder(getApplicationContext(),"1");
                                    //icon title text必须包含，不然影响桌面图标小红点的展示
                                    builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                                            .setContentTitle("标题")
                                            .setContentText("正文")
                                            .setNumber(3); //久按桌面图标时允许的此条通知的数量
                                }else {
                                    builder = new NotificationCompat.Builder(getApplicationContext(),null);
                                    builder.setSmallIcon(R.mipmap.ic_launcher);
                                    builder.setTicker("正在下载新版本");
                                    builder.setContentTitle(getApplicationName());
                                    builder.setContentText("正在下载,请稍后...");
                                    builder.setNumber(0);
                                    builder.setAutoCancel(true);
                                }
                                mNotificationManager.notify(NotificationID, builder.build());
                            }

                            @Override
                            public void onSuccess(Response<File> response) {
                                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                                String apkPath = response.body().getPath();
                                //----------------------------------------------
                                Uri uri = null;
                                if (Build.VERSION.SDK_INT >= 24) {
                                    uri = FileProvider.getUriForFile(DownAPKService.this, MyApplication.provider, new File(apkPath));
                                    installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                }else{
                                    uri = Uri.fromFile(new File(apkPath));
                                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                }
                                //----------------------------------------------
                                installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                                PendingIntent mPendingIntent = PendingIntent.getActivity(DownAPKService.this, 0, installIntent, 0);
                                builder.setContentText("下载完成,请点击安装");
                                builder.setContentIntent(mPendingIntent);
                                mNotificationManager.notify(NotificationID, builder.build());
                                // 震动提示
                                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(100L);// 参数是震动时间(long类型)
                                stopSelf();
                                startActivity(installIntent);// 下载完成之后自动弹出安装界面
                                mNotificationManager.cancel(NotificationID);
                            }

                            @Override
                            public void downloadProgress(Progress progress) {
                                int x = Long.valueOf(progress.currentSize).intValue();
                                int totalS = Long.valueOf(progress.totalSize).intValue();
                                builder.setProgress(totalS, x, false);
                                builder.setContentInfo(getPercent(x, totalS));
                                mNotificationManager.notify(NotificationID, builder.build());
                            }

                            @Override
                            public void onError(Response<File> response) {
                                mNotificationManager.cancel(NotificationID);
//                        Toast.makeText(getApplicationContext(), "下载失败，请检查网络！", Toast.LENGTH_SHORT).show();
                            }

                        });
            }
        }).start();

    }

    /**
     *
     * @param x
     *            当前值
     * @param total
     *            总值
     * [url=home.php?mod=space&uid=7300]@return[/url] 当前百分比
     * @Description:返回百分之值
     */
    private String getPercent(int x, int total) {
        String result = "";// 接受百分比的值
        double x_double = x * 1.0;
        double tempresult = x_double / total;
        // 百分比格式，后面不足2位的用0补齐 ##.00%
        DecimalFormat df1 = new DecimalFormat("0.00%");
        result = df1.format(tempresult);
        return result;
    }

    /**
     * @return
     * @Description:获取当前应用的名称
     */
    private String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mNotificationManager.deleteNotificationChannel("1");
        }
    }

}
