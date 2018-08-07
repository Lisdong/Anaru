package com.example.lrd.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.lrd.R;
import com.example.lrd.model.ToolbarHelper;
import com.example.lrd.ui.base.BaseActivity;
import com.github.lzyzsd.jsbridge.BridgeWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By LRD
 * on 2018/8/3  notes：webView加载HTML5
 */
public class WebViewActivity extends BaseActivity {
	@BindView(R.id.web_fl)
	FrameLayout mFl;
	@BindView(R.id.error_web)
	LinearLayout mErrorLl;
	@BindView(R.id.web_progressbar)
	ProgressBar mProgressbar;
	private BridgeWebView mWebView;
	private ToolbarHelper mToolbar;

	@Override
	protected int getContentView() {
		return R.layout.web_layout;
	}

	@Override
	protected void initToolbar(ToolbarHelper toolbar) {
		super.initToolbar(toolbar);
		mToolbar = toolbar;
	}

	@Override
	public void init() {
		initView();
	}

	private void initView() {
		String url = getIntent().getStringExtra("URL");

		mWebView = new BridgeWebView(getApplicationContext());
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		mFl.addView(mWebView,params);

		//设置进度条
		mProgressbar.setMax(100);//设置加载进度最大值

		//设置
		setSettings();

		// 设置setWebChromeClient对象
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				mToolbar.setTitle(title);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				mProgressbar.setProgress(newProgress);
			}
		});

		//设置此方法可在WebView中打开链接，反之用浏览器打开
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
					mWebView.getSettings().setLoadsImagesAutomatically(true);
				}
				mProgressbar.setVisibility(View.GONE);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mProgressbar.setVisibility(View.VISIBLE);
				mErrorLl.setVisibility(View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("http:") || url.startsWith("https:")) {
					view.loadUrl(url);
					return false;
				}
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}

			@Override
			public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
				super.onReceivedError(view, request, error);
				mErrorLl.setVisibility(View.VISIBLE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				//此方法适配低版本机型
				mErrorLl.setVisibility(View.VISIBLE);
			}
		});

		mWebView.setDownloadListener((String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong)-> {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.setData(Uri.parse(paramAnonymousString1));
				startActivity(intent);
		});
		if (!TextUtils.isEmpty(url)){
			mWebView.loadUrl(url);
		}else {
			mWebView.loadUrl("https://github.com/LRDDYR");
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setSettings() {
		WebSettings settings = mWebView.getSettings();//获取set对象
		settings.setJavaScriptEnabled(true);//支持js
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//设置缓存方式,不使用缓存，只从网络获取数据。
		settings.setDefaultTextEncodingName("utf-8");//设置默认编码
		settings.setUseWideViewPort(false);//将图片调整到适合webview的大小
		settings.setSupportZoom(true);//支持缩放
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
		//settings.supportMultipleWindows();//多窗口
		settings.setAllowFileAccess(true);//设置可以访问文件
		settings.setNeedInitialFocus(true);//当webview调用requestFocus时为webview设置节点
		settings.setBuiltInZoomControls(false);//设置支持缩放按钮
		settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
		settings.setLoadWithOverviewMode(true);//缩放至屏幕的大小
		settings.setLoadsImagesAutomatically(true);//支持自动加载图片
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 处理返回键，在webview界面，按下返回键，不退出程序
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView != null && mWebView.canGoBack()) {
				mWebView.goBack();
				return true;
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		if( mWebView!=null) {
			ViewParent parent = mWebView.getParent();
			if (parent != null) {
				((ViewGroup) parent).removeView(mWebView);
			}

			mWebView.stopLoading();
			// 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
			mWebView.getSettings().setJavaScriptEnabled(false);
			mWebView.clearHistory();
			mWebView.clearView();
			mWebView.removeAllViews();
			mWebView.destroy();

		}
		super.onDestroy();
	}
}
