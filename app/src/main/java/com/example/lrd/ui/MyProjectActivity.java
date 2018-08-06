package com.example.lrd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.lrd.R;
import com.example.lrd.bean.MainBean;
import com.example.lrd.model.ToolbarHelper;
import com.example.lrd.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By LRD
 * on 2018/8/3  notes：
 */
public class MyProjectActivity extends BaseActivity {
	@BindView(R.id.recyclerView_my)
	RecyclerView mRecyclerView;
	private MAdapter mAdapter;

	@Override
	protected int getContentView() {
		return R.layout.project_layout;
	}

	@Override
	protected void initToolbar(ToolbarHelper toolbar) {
		super.initToolbar(toolbar);
		toolbar.setTitle("点击跳转网页");
	}

	@Override
	public void init() {
		initView();
		initListener();
	}

	private void initView() {
		ArrayList<MainBean> mStr = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			MainBean mainBean = new MainBean();
			if (i == 0)mainBean.setName("百度一下");
			else if (i == 1)mainBean.setName("我的GitHub");
			else if (i == 2)mainBean.setName("Yexingshuai的GitHub");
			else if (i == 3)mainBean.setName("GitHub-UI项目集合");
			else if (i == 4)mainBean.setName("免费看电影");
			else if (i == 5)mainBean.setName("18岁以下勿进（需VPN）");
			else mainBean.setName("招商请联系微信号LD007D");
			mStr.add(mainBean);
		}

		//RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
		mRecyclerView.setLayoutManager(gridLayoutManager);
		mAdapter = new MAdapter(R.layout.item_main, mStr);
		mRecyclerView.setAdapter(mAdapter);
	}

	private void initListener() {
		mAdapter.setOnItemClickListener((BaseQuickAdapter adapter, View view, int position)-> {
			Intent intent = new Intent();
			intent.setClass(this,WebViewActivity.class);
			if (position == 0)intent.putExtra("URL","https://www.baidu.com");
			if (position == 1)intent.putExtra("URL","https://github.com/LRDDYR");
			if (position == 2)intent.putExtra("URL","https://github.com/Yexingshuai");
			if (position == 3)intent.putExtra("URL","https://github.com/opendigg/awesome-github-android-ui/blob/master/README.md");
			if (position == 4)intent.putExtra("URL","http://www.0855tv.com");
			if (position == 5)intent.putExtra("URL","https://www.xvideos.com");

			startActivity(intent);
		});
	}

	private class MAdapter extends BaseQuickAdapter<MainBean,BaseViewHolder>{

		private MAdapter(int layoutResId, @Nullable List<MainBean> data) {
			super(layoutResId, data);
		}

		@Override
		protected void convert(BaseViewHolder helper, MainBean item) {
			helper.setText(R.id.item_main_text,item.getName());
		}
	}
}
