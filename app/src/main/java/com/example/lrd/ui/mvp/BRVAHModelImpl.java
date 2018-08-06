package com.example.lrd.ui.mvp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.lrd.R;
import com.example.lrd.adapter.BAdapter;
import com.example.lrd.adapter.ViewPagerAdapter;
import com.example.lrd.bean.BannerBean;
import com.example.lrd.bean.Bbean;
import com.example.lrd.call.MessageEvent;
import com.example.lrd.call.RequestBeanCallback;
import com.example.lrd.http.Url;
import com.example.lrd.ui.WebViewActivity;
import com.example.lrd.utils.DeviceUtils;
import com.example.lrd.utils.HttpManager;
import com.example.lrd.utils.ToastUtil;
import com.example.lrd.views.ViewPagerScroller;
import com.example.lrd.views.refresh_view.PullToRefreshView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * Created By LRD
 * on 2018/8/1  notes：
 */
public class BRVAHModelImpl implements BRVAModel{
	private List<Bbean.DataBean.DatasBean> mData = new ArrayList<>();
	private List<BannerBean.DataBean> mDataBanner = new ArrayList<>();
	private Context mContext;
	private PullToRefreshView mRefresh;
	private RecyclerView mRecyclerView;
	private BAdapter mBAdapter;
	private ViewPagerAdapter mVAdapter;
	private LinearLayout mBox;//指示器容器
	private ViewPager mViewPager;
	private int currentIndex = 1;

	public void init(Context context, PullToRefreshView refreshView, RecyclerView recyclerView) {
		this.mContext = context;
		this.mRefresh = refreshView;
		this.mRecyclerView = recyclerView;

		initView();
		getBannerData();
		getListData();
		initListener();
	}

	private void initView() {
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
		mRecyclerView.setLayoutManager(layoutManager);
		mBAdapter = new BAdapter(R.layout.item_brvah_layout,mData);
		mBAdapter.openLoadAnimation();//默认为渐显效果
		mRecyclerView.setAdapter(mBAdapter);

		mBAdapter.addHeaderView(getHeaderView());
	}

	//添加头部
	private View getHeaderView() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.banner_layout, null);
		mViewPager = view.findViewById(R.id.viewPager_banner);
		mBox = view.findViewById(R.id.banner_box);
		ImageView point = view.findViewById(R.id.banner_point);
		setIndicator(point);

		//让翻页动画减速
		ViewPagerScroller pagerScroller = new ViewPagerScroller(mContext);
		pagerScroller.setScrollDuration(1000);
		pagerScroller.initViewPagerScroll(mViewPager);

		mVAdapter = new ViewPagerAdapter(mContext,mDataBanner);
		mViewPager.setAdapter(mVAdapter);

		//设置banner定时轮播
		mViewPager.setCurrentItem(mViewPager.getAdapter().getCount() / 2);
		mViewPager.postDelayed(()-> {
			EventBus.getDefault().post(new MessageEvent("Banner"));
		},4000);
		return view;
	}

	//轮播图数据
	private void getBannerData() {
		HttpManager.getInstance().requestPost(Url.EXTERNAL_BANNER_URL, BannerBean.class, null, new RequestBeanCallback<BannerBean>() {
			@Override
			public void onSuccess(BannerBean bean) {
				mVAdapter.setData(bean.getData());
				mDataBanner = bean.getData();
				setBoxData();
			}

			@Override
			public void onError(String error) {
				ToastUtil.getInstance(mContext).showToast("获取轮播图数据错误"+error);
			}
		});
	}
	//列表数据
	private void getListData() {
		HttpManager.getInstance().requestPost(Url.EXTERNAL_URL+currentIndex+"/json", Bbean.class, null, new RequestBeanCallback<Bbean>() {
			@Override
			public void onSuccess(Bbean bean) {
				List<Bbean.DataBean.DatasBean> datas = bean.getData().getDatas();
				if (datas != null && datas.size() > 0){
					if (currentIndex == 1){
						mBAdapter.setNewData(datas);
					}else {
						mBAdapter.addData(datas);
						mBAdapter.loadMoreComplete();
					}
					currentIndex = currentIndex + 1;
				}
			}

			@Override
			public void onError(String error) {
				ToastUtil.getInstance(mContext).showToast("获取数据错误"+error);
			}
		});
	}

	//监听
	private void initListener() {
		//下拉刷新
		mRefresh.setOnRefreshListener(()-> {
			mRefresh.postDelayed(() -> {
				currentIndex = 1;
				getBannerData();
				getListData();
				mRefresh.setRefreshing(false);
			},2000);
		});

		//上拉加载
		mBAdapter.setOnLoadMoreListener(this::getListData,mRecyclerView);
		//默认第一次加载会调用  加载更多，此方法控制第一次不进入加载回调
		mBAdapter.disableLoadMoreIfNotFullPage();

        //item点击事件
		mBAdapter.setOnItemClickListener((BaseQuickAdapter adapter, View view, int position)-> {
			List<Bbean.DataBean.DatasBean> data = adapter.getData();
			Intent intent = new Intent();
			intent.setClass(mContext, WebViewActivity.class);
			intent.putExtra("URL",data.get(position).getLink());
			mContext.startActivity(intent);
		});
	}

	//设置指示器
	private void setIndicator(ImageView point) {
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				position = position % mDataBanner.size();
				int redPointX = position * mContext.getResources().getDimensionPixelOffset(R.dimen.home_header_point);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) point.getLayoutParams();
				params.leftMargin = redPointX;
				point.setLayoutParams(params);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	//设置指示器容器数据
	private void setBoxData() {
		mBox.removeAllViews();
		for (int i = 0; i < mDataBanner.size(); i++) {
			ImageView wPoint = new ImageView(mContext);
			wPoint.setBackgroundResource(R.drawable.point_transparent_black);
			int mL = DeviceUtils.dip2px(mContext, 5);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mL,mL);
			if (i != 0){
				params.leftMargin = mL;
			}
			wPoint.setLayoutParams(params);
			mBox.addView(wPoint);
		}
	}


	//设置无限轮播
	public void setBannerItem() {
		int currentItem = mViewPager.getCurrentItem();
		mViewPager.setCurrentItem(++currentItem);
		mViewPager.postDelayed(()-> {
			EventBus.getDefault().post(new MessageEvent("Banner"));
		},4000);
	}
}
