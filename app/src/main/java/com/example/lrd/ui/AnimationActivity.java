package com.example.lrd.ui;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.lrd.R;
import com.example.lrd.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created By LRD
 * on 2018/7/25  notes：动画
 */
public class AnimationActivity extends BaseActivity {
	@BindView(R.id.btn_ll)
	LinearLayout btnLl;
	@BindView(R.id.popMenu)
	LinearLayout popMenu;
	private int currentClick = -1;

	@Override
	protected int getContentView() {
		return R.layout.animation_layout;
	}

	@Override
	public void init() {

	}

	@OnClick(R.id.btn_ll)
	public void onViewClicked() {
		if (currentClick == -1){
			popMenu.setVisibility(View.VISIBLE);
			popMenu.setAnimation(AnimationUtils.loadAnimation(this,R.anim.dd_menu_in));
			currentClick = 0;
		}else {
			popMenu.setVisibility(View.GONE);
			popMenu.setAnimation(AnimationUtils.loadAnimation(this,R.anim.dd_menu_out));
			currentClick = -1;
		}
	}
}
