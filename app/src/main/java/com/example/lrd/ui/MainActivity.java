package com.example.lrd.ui;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.lrd.R;
import com.example.lrd.bean.MainBean;
import com.example.lrd.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.recyclerView_main)
    RecyclerView mRecyclerView;
    private MAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        initView();
        initListener();
    }

    private void initView() {
        ArrayList<MainBean> mStr = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            MainBean mainBean = new MainBean();
            if (i == 0)mainBean.setName("指纹解锁");
            else if (i == 1)mainBean.setName("图片选择");
            else if (i == 2)mainBean.setName("8.0通知栏");
            else if (i == 3)mainBean.setName("下拉菜单");
            else if (i == 4)mainBean.setName("BRVAH");
            else if (i == 5)mainBean.setName("WebActivity");
            else mainBean.setName("无");
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
            if (position == 0)startActivity(FingerPrintActivity.class);
            if (position == 1)startActivity(PhotoPickActivity.class);
            if (position == 2)startActivity(NotificationActivity.class);
            if (position == 3)startActivity(DropDownMenuActivity.class);
            if (position == 4)startActivity(BRVAHActivity.class);
            if (position == 5)startActivity(MyProjectActivity.class);
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
