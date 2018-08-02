package com.example.lrd.ui.mvp;

import android.support.v7.widget.RecyclerView;

import com.example.lrd.views.refresh_view.PullToRefreshView;

/**
 * Created By LRD
 * on 2018/8/1  notes：
 */
public interface BRVAPresenter {
	void validate(PullToRefreshView refreshView, RecyclerView recyclerView);
	void setAutoPlay();
}
