package com.example.lrd.call;

import android.view.View;

/**
 * Created By LRD
 * on 2018/7/26  notes：
 */
public interface IToolBar {
	void setTitle(String title);
	void onBackListener(View.OnClickListener listener);
	void setRightText(String str);
}
