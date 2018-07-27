package com.example.lrd.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lrd.R;

import java.util.List;

/**
 * Created By LRD
 * on 2018/7/24  notes：自定义下拉菜单
 */
public class DropDownMenu extends LinearLayout {

	//顶部菜单布局
	private LinearLayout mTabMenuView;
	//底部容器 包含内容区域 遮罩区域 菜单弹出区域
	private FrameLayout mContainerView;
	//内容区域
	private View mContentView;
	//遮罩区域
	private View mMaskView;
	//菜单弹出区域
	private FrameLayout mPopUpView;

	//分割线颜色
	private int mDividerColor = 0xffcccccc;
	//文本选择颜色
	private int mTextSelectColor = 0xff890c85;
	//文本未选择颜色
	private int mTextUnSelectColor = 0xff111111;
	//遮罩层颜色
	private int mMaskColor = 0x88888888;
	//菜单背景颜色
	private int mMenuBackgroundColor = 0xffffffff;
	//水平分割线颜色
	private int mUnderLineColor = 0xffcccccc;
	//字体大小
	private int mTextSize = 14;
	//tab选中图标
	private int mMenuSelectImage;
	//tab未选中图标
	private int mMenuUnSelectImage;
	//当前选择的菜单
	private int currentPosition = -1;

	public DropDownMenu(Context context) {
		super(context,null);
	}

	public DropDownMenu(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs,0);
	}

	public DropDownMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		setOrientation(VERTICAL);

		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
		mDividerColor = array.getColor(R.styleable.DropDownMenu_DividerColor,mDividerColor);
		mTextSelectColor = array.getColor(R.styleable.DropDownMenu_SelectTextColor,mTextSelectColor);
		mTextUnSelectColor = array.getColor(R.styleable.DropDownMenu_UnSelectTextColor,mTextUnSelectColor);
		mMaskColor = array.getColor(R.styleable.DropDownMenu_MaskColor,mMaskColor);
		mMenuBackgroundColor = array.getColor(R.styleable.DropDownMenu_MenuBackgroundColor,mMenuBackgroundColor);
		mUnderLineColor = array.getColor(R.styleable.DropDownMenu_UnderlineColor,mUnderLineColor);
		mTextSize = array.getDimensionPixelSize(R.styleable.DropDownMenu_TextSize,mTextSize);
		mMenuSelectImage = array.getResourceId(R.styleable.DropDownMenu_SelectImage,mMenuSelectImage);
		mMenuUnSelectImage = array.getResourceId(R.styleable.DropDownMenu_UnSelectImage,mMenuUnSelectImage);
		array.recycle();

		initView(context);
	}

	//初始化控件
	private void initView(Context context) {
		//tab栏
		mTabMenuView = new LinearLayout(context);
		mTabMenuView.setBackgroundColor(Color.WHITE);
		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mTabMenuView.setOrientation(HORIZONTAL);
		mTabMenuView.setLayoutParams(layoutParams);
		addView(mTabMenuView,0);

		//下划线
		View view = new View(context);
		view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp2px(1.0f)));
		view.setBackgroundColor(mUnderLineColor);
		addView(view,1);

		//初始化
		mContainerView = new FrameLayout(context);
		mContainerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		addView(mContainerView,2);
	}

	/**
	 * 初始化显示内容
	 */
	public void initDropDownMenu(List<String> tabTexts,List<View> popViews,View content){
		this.mContentView = content;
		if (tabTexts.size() != popViews.size()){
			throw new IllegalArgumentException("菜单和菜单数量必须相等");
		}
		for (int i = 0; i < tabTexts.size(); i++) {
			addTab(tabTexts,i);
		}
		mContainerView.addView(mContentView,0);

		//遮罩层
		mMaskView = new View(getContext());
		mMaskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		mMaskView.setBackgroundColor(mMaskColor);
		mMaskView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closeMenu();
			}
		});
		mMaskView.setVisibility(GONE);
		mContainerView.addView(mMaskView,1);

		//弹出框
		mPopUpView = new FrameLayout(getContext());
		mPopUpView.setVisibility(GONE);
		for (int i = 0; i < popViews.size(); i++) {
			popViews.get(i).setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT));
			mPopUpView.addView(popViews.get(i),i);
		}
		mContainerView.addView(mPopUpView,2);
	}

	//设置文字
	private void addTab(List<String> tabTexts,  int index) {
		TextView mTabText = new TextView(getContext());
		mTabText.setSingleLine();
		mTabText.setEllipsize(TextUtils.TruncateAt.END);
		mTabText.setGravity(Gravity.CENTER);
		mTabText.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
		mTabText.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
		mTabText.setTextColor(mTextUnSelectColor);
		mTabText.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(mMenuUnSelectImage,null),null);
		mTabText.setText(tabTexts.get(index));
		mTabText.setPadding(dp2px(5),dp2px(12),dp2px(5),dp2px(12));
		mTabText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchMenu(v);
			}
		});
		mTabMenuView.addView(mTabText);

		//添加分割线
		if (index <tabTexts.size() -1 ){
			View view = new View(getContext());
			view.setLayoutParams(new LayoutParams(dp2px(0.5f), ViewGroup.LayoutParams.MATCH_PARENT));
			view.setBackgroundColor(mDividerColor);
			mTabMenuView.addView(view);
		}
	}

	/**
	 * 切换按钮
	 */
	private void switchMenu(View view) {
		for (int i = 0; i < mTabMenuView.getChildCount(); i=i+2) {
			if (view == mTabMenuView.getChildAt(i)){
				if (currentPosition == i){//关闭菜单
					closeMenu();
				}else {//弹出菜单
					if (currentPosition == -1){//初始，第一次打开
						mPopUpView.setVisibility(VISIBLE);
						mPopUpView.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dd_menu_in));
						mMaskView.setVisibility(VISIBLE);
						mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.mask_menu_in));
						mPopUpView.getChildAt(i/2).setVisibility(VISIBLE);
					}else {
						mPopUpView.getChildAt(i/2).setVisibility(VISIBLE);
					}

					currentPosition = i;
					((TextView)mTabMenuView.getChildAt(i)).setTextColor(mTextSelectColor);
					((TextView)mTabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(mMenuSelectImage),null);
				}
			}else {
				((TextView)mTabMenuView.getChildAt(i)).setTextColor(mTextUnSelectColor);
				((TextView)mTabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(mMenuUnSelectImage),null);
				mPopUpView.getChildAt(i/2).setVisibility(GONE);
			}
		}
	}

	//关闭菜单
	public void closeMenu() {
		if (currentPosition != -1){
			((TextView)mTabMenuView.getChildAt(currentPosition)).setTextColor(mTextUnSelectColor);
			((TextView)mTabMenuView.getChildAt(currentPosition)).setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(mMenuUnSelectImage),null);
			mPopUpView.setVisibility(GONE);
			mPopUpView.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dd_menu_out));
			mMaskView.setVisibility(GONE);
			mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.mask_menu_out));
			currentPosition = -1;
		}
	}

	/**
	 * 改变tab文字
	 */
	public void setTabText(int position,String text) {
		if (currentPosition != -1) {
			((TextView) mTabMenuView.getChildAt(currentPosition)).setText(text);
		}
	}

	private int dp2px(float v) {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,v,dm);
	}
}
