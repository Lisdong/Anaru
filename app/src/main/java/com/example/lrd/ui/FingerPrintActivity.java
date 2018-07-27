package com.example.lrd.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.lrd.R;
import com.example.lrd.model.ToolbarHelper;
import com.example.lrd.ui.base.BaseActivity;
import com.example.lrd.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created By LRD
 * on 2018/7/18
 */
public class FingerPrintActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_fingerprint)
    Button mBtn;
    @BindView(R.id.text_success)
    TextView mText;
    private FingerprintManager.AuthenticationCallback mSelfCancelled;
    private FingerprintManager mFingerManager;
    private CancellationSignal mCancellationSignal;

    @Override
    protected int getContentView() {
        return R.layout.fingerprint_layout;
    }

    @Override
    public void init() {
        initView();
        //initListener();
    }

    @Override
    protected void initToolbar(ToolbarHelper toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle("指纹验证");
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initListener() {
        mSelfCancelled = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                //多次指纹密码验证错误后，进入此方法；并且，不可再验（短时间）
                //errorCode是失败的次数
                ToastUtil.getInstance(FingerPrintActivity.this).showToast("尝试次数过多，请稍后重试");
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                //指纹验证失败，可再验，可能手指过脏，或者移动过快等原因。
                ToastUtil.getInstance(FingerPrintActivity.this).showToast("指纹验证失败，可再验，可能手指过脏，或者移动过快等原因");
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                //指纹密码验证成功
                ToastUtil.getInstance(FingerPrintActivity.this).showToast("成功");
                mText.setVisibility(View.VISIBLE);
                mCancellationSignal.cancel();
            }

            @Override
            public void onAuthenticationFailed() {
                //指纹验证失败，指纹识别失败，可再验，错误原因为：该指纹不是系统录入的指纹。
                ToastUtil.getInstance(FingerPrintActivity.this).showToast("指纹验证失败，指纹识别失败，可再验，错误原因为：该指纹不是系统录入的指纹");
            }
        };
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = new String[]{Manifest.permission.USE_FINGERPRINT,};
            if (EasyPermissions.hasPermissions(this, perms)) {
                //ToastUtil.getInstance(this).showToast("获取权限成功");
                boolean b = checkFingerPrint();
                if (b){
                    initListener();
                    mCancellationSignal = new CancellationSignal();
                    mFingerManager.authenticate(null, mCancellationSignal, 0, mSelfCancelled, null);
                }
            } else {
                EasyPermissions.requestPermissions(this, "指纹读取权限",
                        100, perms);
            }
        } else {
            new MaterialDialog.Builder(this).title("提示").content("当前手机版本不支持指纹登录")
                    .positiveText("退出")
                    .cancelable(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    }).show();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkFingerPrint() {
        mFingerManager = getFingerprintManager(this);
        if (!mFingerManager.isHardwareDetected()) {
            ToastUtil.getInstance(this).showToast("没有指纹识别模块");
            return false;
        }
        if (!mFingerManager.hasEnrolledFingerprints()) {
            ToastUtil.getInstance(this).showToast("没有指纹录入");
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private FingerprintManager getFingerprintManager(Context context) {
        FingerprintManager fingerprintManager = null;
        try {
            fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        } catch (Throwable e) {
            Logger.d("have not class FingerprintManager");
        }
        return fingerprintManager;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mCancellationSignal.cancel();
    }
}
