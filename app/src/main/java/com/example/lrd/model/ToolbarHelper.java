package com.example.lrd.model;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.lrd.R;
import com.example.lrd.call.IToolBar;

/**
 * Created By LRD
 * on 2018/7/26  notes：
 */
public class ToolbarHelper implements IToolBar {
	private Toolbar mToolbar;

	public ToolbarHelper( Toolbar toolbar){
		this.mToolbar = toolbar;
	}
	@Override
	public void setTitle(String str){
		TextView title = mToolbar.findViewById(R.id.toolbar_title);
		title.setText(str);
	}

	@Override
	public void onBackListener(View.OnClickListener listener) {
		mToolbar.setNavigationOnClickListener(listener);
	}

	@Override
	public void setRightText(String str) {

	}
}
