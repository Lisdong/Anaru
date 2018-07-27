package com.example.lrd.ui.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.lrd.R;
import com.example.lrd.model.ToolbarHelper;
import com.example.lrd.utils.StatusBarUtil;
import com.example.lrd.utils.ToastUtil;

import java.util.List;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created By LRD
 * on 2018/6/28
 */
public abstract class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public BaseActivity activity;
    protected final int RC_SETTINGS_SCREEN = 100;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        this.activity = this;
        setContentView(getContentView());

        ButterKnife.bind(this);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // 默认不显示原生标题
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            initToolbar(new ToolbarHelper(toolbar));
        }
        init();
    }

    /**
     * [获取布局文件]
     */
    protected abstract int getContentView();
    /**
     * [初始化控件]
     */
    public abstract void init();

    /**
     * [页面跳转]
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this,clz));
    }

    /**
     * [携带数据的页面跳转]
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    /**
     * [含有Bundle通过Class打开编辑界面]
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //同意权限
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //ToastUtil.getInstance(this).showToast("您拒绝了权限");
        //若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。这时候，需要跳转到设置界面去，让用户手动开启。
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("提示")
                    .setPositiveButton("设置")
                    .setNegativeButton("取消")
                    .setRationale("去权限设置界面打开权限")
                    .setRequestCode(RC_SETTINGS_SCREEN)
                    .build()
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtil.getInstance(this).destroy();
        activity = null;
    }
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0以上系统
            Window window = getWindow();
            //是否显示状态栏高度
            if (isShowStatusBarHeight()){
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.setStatusBarColor(Color.WHITE);//顶部栏
            }else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
            window.setNavigationBarColor(Color.TRANSPARENT);//底部栏
        } else {
            //6.0以下系统
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //是否显示状态栏高度
            if (isShowStatusBarHeight()){
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            boolean a = StatusBarUtil.FlymeSetStatusBarLightMode(window, true);//魅族
            boolean b = StatusBarUtil.MIUISetStatusBarLightMode(window, true);//小米
            if (a || b) {
                if (isShowStatusBarHeight()){
                    window.setStatusBarColor(Color.WHITE);//顶部栏
                }else {
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
                window.setNavigationBarColor(Color.TRANSPARENT);
                int i = StatusBarUtil.StatusBarLightMode(this);
                StatusBarUtil.StatusBarLightMode(this, i);
            } else {
                if (isShowStatusBarHeight()){
                    window.setStatusBarColor(Color.WHITE);//顶部栏
                }else {
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
                window.setNavigationBarColor(Color.TRANSPARENT);
            }
        }
    }

    /**
     * 是否显示顶部栏
     * @return 默认true
     */
    protected boolean isShowStatusBarHeight() {
        return true;
    }

    protected void initToolbar(ToolbarHelper toolbar){
        setToolbarBackBtn(toolbar);
    }

    protected void setToolbarBackBtn(ToolbarHelper toolbar){
        toolbar.onBackListener((v)->{
            finish();
        });
    }
}
