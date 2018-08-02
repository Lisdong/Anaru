package com.example.lrd.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.lrd.bean.BannerBean;

import java.util.List;

/**
 * Created By LRD
 * on 2018/8/1  notes：
 */
public class ViewPagerAdapter extends PagerAdapter{
	private Context context;
	private List<BannerBean.DataBean> mDataBanner;

	public ViewPagerAdapter(Context context, List<BannerBean.DataBean> mDataBanner) {
		this.context = context;
		this.mDataBanner = mDataBanner;
	}

	@Override
	public int getCount() {
		return mDataBanner.size()*1000*10;
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == object;
	}

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		position = position % mDataBanner.size();
		String imagePath = mDataBanner.get(position).getImagePath();
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		Glide.with(context).load(imagePath).into(imageView);
		container.addView(imageView);
		return imageView;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position,@NonNull Object object) {
		container.removeView((View) object);
	}

	public void setData(List<BannerBean.DataBean> mdata) {
		mDataBanner = mdata;
		notifyDataSetChanged();
	}
}
