package com.example.lrd.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lrd.R;
import com.example.lrd.adapter.AllAdapter;
import com.example.lrd.adapter.DistanceAdapter;
import com.example.lrd.adapter.SellAdapter;
import com.example.lrd.model.ToolbarHelper;
import com.example.lrd.ui.base.BaseActivity;
import com.example.lrd.views.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created By LRD
 * on 2018/7/24
 */
public class DropDownMenuActivity extends BaseActivity implements AdapterView.OnItemClickListener {
	@BindView(R.id.my_menu)
	DropDownMenu mMenu;
	private String headers[] = {"全部","销量","距离","大牌"};
	private List<View> popViews = new ArrayList<>();
	private String all[] = {"不限","好的","坏的","全的","傻得","活的",};
	private String sell[] = {"不限","最高","最低"};
	private String distance[] = {"不限","最远","最近"};
	private AllAdapter mAllAdapter;
	private SellAdapter mSellAdapter;
	private DistanceAdapter mDistanceAdapter;

	@Override
	protected int getContentView() {
		return R.layout.dropmenu_layout;
	}

	@Override
	protected void initToolbar(ToolbarHelper toolbar) {
		super.initToolbar(toolbar);
		toolbar.setTitle("DropDownMenu");
	}

	@Override
	public void init() {
		initData();
		initView();
	}

	@SuppressLint("ResourceType")
	private void initData() {
		ListView allList = new ListView(this);
		mAllAdapter = new AllAdapter(this, Arrays.asList(all));
		allList.setDividerHeight(0);
		allList.setId(0);
		allList.setAdapter(mAllAdapter);

		ListView sellList = new ListView(this);
		mSellAdapter = new SellAdapter(this, Arrays.asList(sell));
		sellList.setDividerHeight(0);
		sellList.setId(1);
		sellList.setAdapter(mSellAdapter);

		ListView distanceList = new ListView(this);
		mDistanceAdapter = new DistanceAdapter(this, Arrays.asList(distance));
		distanceList.setDividerHeight(0);
		distanceList.setId(2);
		distanceList.setAdapter(mDistanceAdapter);

		allList.setOnItemClickListener(this);
		sellList.setOnItemClickListener(this);
		distanceList.setOnItemClickListener(this);

		View viewF = LayoutInflater.from(this).inflate(R.layout.dropmenu_four_layout, null);
		viewF.setId(3);


		popViews.add(allList);
		popViews.add(sellList);
		popViews.add(distanceList);
		popViews.add(viewF);

		TextView textView = new TextView(this);
		textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		textView.setGravity(Gravity.CENTER);
		textView.setText("内容");
		mMenu.initDropDownMenu(Arrays.asList(headers),popViews,textView);
	}

	private void initView() {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()){
			case 0:
				mAllAdapter.setSelectItem(position);
				mMenu.setTabText(position,position == 0 ? headers[0] : all[position]);
				mMenu.closeMenu();
				break;
			case 1:
				mSellAdapter.setSelectItem(position);
				mMenu.setTabText(position,position == 0 ? headers[1] : sell[position]);
				mMenu.closeMenu();
				break;
			case 2:
				mDistanceAdapter.setSelectItem(position);
				mMenu.setTabText(position,position == 0 ? headers[2] : distance[position]);
				mMenu.closeMenu();
				break;
			case 3:
				mMenu.closeMenu();
				break;
		}
	}
}
