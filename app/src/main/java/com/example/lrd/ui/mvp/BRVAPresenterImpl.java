package com.example.lrd.ui.mvp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.lrd.views.refresh_view.PullToRefreshView;

/**
 * Created By LRD
 * on 2018/8/1  notes：
 */
public class BRVAPresenterImpl implements BRVAPresenter{

	private  Context context;
	private  BRVAView mainView;
	private BRVAHModelImpl brvaModel;

	public BRVAPresenterImpl(Context context, BRVAView mainView){
		this.context = context;
		this.mainView = mainView;
		this.brvaModel = new BRVAHModelImpl();
	}

	@Override
	public void validate(PullToRefreshView refreshView, RecyclerView recyclerView) {
		brvaModel.init(context,refreshView,recyclerView);
	}

	@Override
	public void setAutoPlay() {
		brvaModel.setBannerItem();
	}
}
