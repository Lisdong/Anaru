package com.example.lrd.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.lrd.R;
import com.example.lrd.bean.Bbean;

import java.util.List;

/**
 * Created By LRD
 * on 2018/8/1  notes：
 */
public class BAdapter extends BaseQuickAdapter<Bbean.DataBean.DatasBean, BaseViewHolder> {

	public BAdapter(int layoutResId, @Nullable List<Bbean.DataBean.DatasBean> data) {
		super(layoutResId, data);
	}

	@Override
	protected void convert(BaseViewHolder helper, Bbean.DataBean.DatasBean item) {
		helper.setText(R.id.item_brvah_tv,item.getAuthor());
	}
}
