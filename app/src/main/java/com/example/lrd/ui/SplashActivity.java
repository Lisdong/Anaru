package com.example.lrd.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.lrd.R;
import com.example.lrd.app.Constant;
import com.example.lrd.bean.TabDataBean;
import com.example.lrd.bean.VersionBean;
import com.example.lrd.call.ImageCallback;
import com.example.lrd.call.RequestBeanCallback;
import com.example.lrd.http.Url;
import com.example.lrd.ui.base.BaseActivity;
import com.example.lrd.update.DownAPKForce;
import com.example.lrd.update.DownloadSignatureService;
import com.example.lrd.utils.BitmapUtils;
import com.example.lrd.utils.DeviceUtils;
import com.example.lrd.utils.HttpManager;
import com.example.lrd.utils.SharedPreferencesUtils;
import com.example.lrd.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created By LRD
 * on 2018/6/28
 */
public class SplashActivity extends BaseActivity {
    private static final int RC_CAMERA_AND_WIFI = 100;
    @BindView(R.id.splash_first_img)
    ImageView mFirstImg;
    @BindView(R.id.tv_time)
    TextView mTime;
    @BindView(R.id.splash_btn_skip)
    LinearLayout mBtnSkip;
    Handler mHandler = new Handler();
    private int recLen = 5;

    @Override
    protected int getContentView() {
        return R.layout.splash_layout;
    }

    @Override
    protected boolean isShowStatusBarHeight() {
        return false;
    }

    @Override
    public void init() {
        //去除顶部栏
        //getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //...
            ToastUtil.getInstance(this).showToast("获取权限成功");
            checkVersion();
        } else {
            //...
            EasyPermissions.requestPermissions(this, "应用程序需要文件读取权限",
                    RC_CAMERA_AND_WIFI, perms);
        }
        //获取图片的宽高
    }
    //更新
    private void checkVersion() {
        Map<String, String> params = new HashMap<>();
        params.put("clienttype", "0");
        params.put("wsid", "");
        params.put("clientversionid", DeviceUtils.getVersionCode(this));
        HttpManager.getInstance().requestGet(Url.UPDATE_APK, VersionBean.class, params, new RequestBeanCallback<VersionBean>() {
            @Override
            public void onSuccess(VersionBean bean) {
                String clientUpdate = bean.getClientUpdate();
                String forceUpdate = bean.getForceUpdate();
                final String clientVersionURL = bean.getClientVersionURL();
                if ("1".equals(clientUpdate)){
                    //不更新
                    ToastUtil.getInstance(SplashActivity.this).showToast("不更新");
                    getTabDataFromServer();
                    return;
                }if ("-0".equals(forceUpdate)){//原为0
                    //强制更新
                    new MaterialDialog.Builder(SplashActivity.this)
                            .title("有一些必要的更新")
                            .positiveText("更新")
                            .negativeText("退出")
                            .cancelable(false)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    DownAPKForce.getDownAPKForce().DownFile(activity,clientVersionURL,"");
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }
                            }).show();
                }else if ("0".equals(forceUpdate)){//原为1
                    //不强制更新
                    new MaterialDialog.Builder(SplashActivity.this)
                            .title("有新版本了，是否更新？")
                            .positiveText("更新")
                            .negativeText("不更新")
                            .cancelable(false)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent(activity, DownloadSignatureService.class);
                                        intent.putExtra("apk_url",clientVersionURL);
                                        activity.startService(intent);
                                        getTabDataFromServer();
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    getTabDataFromServer();
                                }
                            }).show();
                }else {
                    ToastUtil.getInstance(SplashActivity.this).showToast("没有更新Flag");
                    getTabDataFromServer();
                }
            }

            @Override
            public void onError(String error) {
                ToastUtil.getInstance(SplashActivity.this).showToast("联网更新失败");
                getTabDataFromServer();
            }
        });
    }

    //获取数据
    private void getTabDataFromServer() {
        Map<String, String> params = new HashMap<>();
        params.put("wsid", "");
        HttpManager.getInstance().requestPost(Url.TRANS_GET_TAB_DATA, TabDataBean.class, params, new RequestBeanCallback<TabDataBean>() {
            @Override
            public void onSuccess(TabDataBean bean) {
                String splashImgUrl = bean.getSplashImgUrl();
                downloadImage(splashImgUrl);
            }

            @Override
            public void onError(String error) {
                ToastUtil.getInstance(SplashActivity.this).showToast("数据获取失败"+error);
                imageError();
            }
        });
    }

    //下载广告图片
    private void downloadImage(String splashImgUrl) {
        HttpManager.getInstance().downImage("http://61.181.71.46:9087"+splashImgUrl, new ImageCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                //保存图片到本地
                try {
                    BitmapUtils.saveBitmap(SplashActivity.this,bitmap,"Anaru首页广告.png");
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtil.getInstance(SplashActivity.this).showToast("广告保存异常"+e.getMessage());
                }
                setBitmap(bitmap);
            }

            @Override
            public void onError(String error) {
                ToastUtil.getInstance(SplashActivity.this).showToast("广告下载失败"+error);
                imageError();
            }
        });
    }

    private void imageError() {
        String path = DeviceUtils.createDir(activity,"Image/"+"Anaru首页广告.png");
        if (DeviceUtils.fileIsExists(path)){
            File file = new File(path);
            if (file.exists()) {
                //Bitmap bm = BitmapFactory.decodeFile(path);
                int viewHeight = DeviceUtils.getViewHeight(mFirstImg, true);
                int viewWeight = DeviceUtils.getViewHeight(mFirstImg, false);
                Logger.d(viewWeight+"----11----"+viewHeight);
                Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(path, viewWeight, viewHeight);
                setBitmap(bitmap);
            } else {
                //"hjz","文件不存在");
            }
        }else {
            boolean isFirst = SharedPreferencesUtils.getBoolean(SplashActivity.this, Constant.IS_FIRST, true);
            if(isFirst){
                startActivity(WelcomeActivity.class);
                SharedPreferencesUtils.saveBoolean(SplashActivity.this, Constant.IS_FIRST,false);
            }else {
                startActivity(MainActivity.class);
            }
            finish();
        }
    }

    private void setBitmap(Bitmap bitmap) {
        mFirstImg.setImageBitmap(bitmap);
        mFirstImg.setVisibility(View.VISIBLE);
        mBtnSkip.setVisibility(View.VISIBLE);
        ObjectAnimator oa_ad = ObjectAnimator.ofFloat(mFirstImg, "alpha", 0f, 1f);
        ObjectAnimator oa_skip = ObjectAnimator.ofFloat(mBtnSkip, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1500);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                //tv_skip.startAnimal(3000);
            }
        });
        animatorSet.playTogether(oa_ad, oa_skip);
        animatorSet.start();
        mHandler.postDelayed(mRunnabler,2000);
    }

    Runnable mRunnabler = new Runnable() {
        @Override
        public void run() {
            if (recLen > 1){
                recLen--;
                mTime.setText(String.valueOf(recLen));
                mHandler.postDelayed(this, 1000);
            }else {
                boolean isFirst = SharedPreferencesUtils.getBoolean(SplashActivity.this, Constant.IS_FIRST, true);
                if(isFirst){
                    startActivity(WelcomeActivity.class);
                    SharedPreferencesUtils.saveBoolean(SplashActivity.this, Constant.IS_FIRST,false);
                }else {
                    startActivity(MainActivity.class);
                }
                finish();
            }
        }
    };

    @OnClick({R.id.splash_first_img, R.id.splash_btn_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.splash_first_img:
                ToastUtil.getInstance(this).showToast("Http");
                break;
            case R.id.splash_btn_skip:
                recLen = 0;
                mHandler.removeCallbacks(mRunnabler);
                boolean isFirst = SharedPreferencesUtils.getBoolean(this, Constant.IS_FIRST, true);
                if(isFirst){
                    startActivity(WelcomeActivity.class);
                    SharedPreferencesUtils.saveBoolean(this, Constant.IS_FIRST,false);
                }else {
                    startActivity(MainActivity.class);
                }
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
    }

    //同意授权
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);

    }

    //
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        //若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。这时候，需要跳转到设置界面去，让用户手动开启。
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(SplashActivity.this)
                    .setTitle("提示")
                    .setPositiveButton("设置")
                    .setRationale("去权限设置界面打开权限")
                    .setRequestCode(RC_SETTINGS_SCREEN)
                    .build()
                    .show();
        }else {
            new MaterialDialog.Builder(this).content("拒绝权限")
                    .positiveText("确定")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            android.os.Process.killProcess(android.os.Process.myPid());   //获取PID
                            System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
                        }
                    }).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SETTINGS_SCREEN) {
            // 从app设置返回应用界面
            init();
        }
    }
}
