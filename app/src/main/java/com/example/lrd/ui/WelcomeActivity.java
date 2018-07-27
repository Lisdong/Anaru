package com.example.lrd.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.lrd.R;
import com.example.lrd.ui.base.BaseActivity;
import com.example.lrd.utils.DeviceUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created By LRD
 * on 2018/7/2
 */
public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.wc_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.wc_box)
    LinearLayout mBox;
    @BindView(R.id.wc_point)
    ImageView mPoint;
    @BindView(R.id.wc_btn)
    Button mBtn;
    private ArrayList<ImageView> images;

    @Override
    protected int getContentView() {
        return R.layout.welcome_layout;
    }

    @Override
    public void init() {
        initData();
        initView();
        initListener();
    }

    private void initView() {
        mViewPager.setAdapter(new SPVPAdapter());
    }
    private void initData() {
        int[] imgList = new int[]{R.drawable.pictrue1,R.drawable.pictrue2,R.drawable.pictrue3,R.drawable.pictrue4};
        images = new ArrayList<>();
        for (int i = 0; i < imgList.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imgList[i]);
            images.add(imageView);

            ImageView wPoint = new ImageView(this);
            wPoint.setBackgroundResource(R.drawable.point_gray);
            int mL = DeviceUtils.dip2px(this, 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mL,mL);
            if (i != 0){
                params.leftMargin = mL;
            }
            wPoint.setLayoutParams(params);
            mBox.addView(wPoint);
        }
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int mL = DeviceUtils.dip2px(WelcomeActivity.this, 16);
                int mPointX = (int)((positionOffset + position) * mL);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPoint.getLayoutParams();
                params.leftMargin = mPointX;
                mPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == images.size()-1){
                    mBtn.setVisibility(View.VISIBLE);
                }else {
                    mBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class SPVPAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = images.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position,@NonNull Object object) {
            container.removeView((View) object);
        }
    }
    @OnClick(R.id.wc_btn)
    public void onViewClicked() {
        startActivity(MainActivity.class);
        finish();
    }
}
