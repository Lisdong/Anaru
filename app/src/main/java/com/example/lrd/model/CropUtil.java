package com.example.lrd.model;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.example.lrd.R;
import com.example.lrd.call.ICrop;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created By LRD
 * on 2018/7/25  notes：初始化裁剪工具
 */
public class CropUtil implements ICrop {
	private Activity context;

	public CropUtil(Activity context){
		this.context = context;
	}

	@Override
	public void initUCrop(Uri uri) {
			SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
			long time = System.currentTimeMillis();
			String imageName = timeFormatter.format(new Date(time));

			Uri destinationUri = Uri.fromFile(new File(context.getCacheDir(), imageName + ".jpeg"));

			UCrop.Options options = new UCrop.Options();
			//设置裁剪图片可操作的手势
			options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
			//设置隐藏底部容器，默认显示
			//options.setHideBottomControls(true);
			//设置toolbar颜色
			options.setToolbarColor(ActivityCompat.getColor(context, R.color.colorPrimary));
			//设置状态栏颜色
			options.setStatusBarColor(ActivityCompat.getColor(context, R.color.colorPrimaryDark));

			//开始设置
			//设置最大缩放比例
			options.setMaxScaleMultiplier(5);
			//设置图片在切换比例时的动画
			options.setImageToCropBoundsAnimDuration(666);
			//设置裁剪窗口是否为椭圆
			//options.setOvalDimmedLayer(true);
			//设置是否展示矩形裁剪框
			// options.setShowCropFrame(false);
			//设置裁剪框横竖线的宽度
			//options.setCropGridStrokeWidth(20);
			//设置裁剪框横竖线的颜色
			//options.setCropGridColor(Color.GREEN);
			//设置竖线的数量
			//options.setCropGridColumnCount(2);
			//设置横线的数量
			//options.setCropGridRowCount(1);

			UCrop.of(uri, destinationUri)
					.withAspectRatio(1, 1)
					.withMaxResultSize(1000, 1000)
					.withOptions(options)
					.start(context);
	}
}
