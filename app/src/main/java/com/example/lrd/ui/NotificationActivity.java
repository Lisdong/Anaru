package com.example.lrd.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.support.v4.app.NotificationCompat;

import com.example.lrd.R;
import com.example.lrd.model.ToolbarHelper;
import com.example.lrd.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Notification.VISIBILITY_SECRET;

/**
 * Created By LRD
 * on 2018/7/23
 */
public class NotificationActivity extends BaseActivity {
	@BindView(R.id.nf_btn)
	Button nfBtn;

	@Override
	protected int getContentView() {
		return R.layout.notification_layout;
	}

	@Override
	protected void initToolbar(ToolbarHelper toolbar) {
		super.initToolbar(toolbar);
		toolbar.setTitle("notification");
	}

	@Override
	public void init() {

	}

	//单文本
	private void sendNotification(){
		final NotificationCompat.Builder builder = getNotification("文本","这是单文本notification");
		getManager().notify(1, builder.build());
	}

	//进度条
	private void sendProgressNotification(){
		final NotificationCompat.Builder builder = getNotification("进度条","这是含有进度条的notification");
		getManager().notify(2, builder.build());
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i <= 15; i++) {
					try {
						Thread.sleep(2000);
						builder.setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);
						builder.setProgress(15,i,false);
						getManager().notify(2,builder.build());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	//自定义notification
	private void sendCustomNotification(){
		NotificationCompat.Builder notification = getNotification(null, null);
		RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.notification_my_layout);
		PendingIntent pendingIntent = PendingIntent.getActivity(this,100,new Intent(this,NotificationActivity.class),PendingIntent.FLAG_CANCEL_CURRENT);
		contentView.setOnClickPendingIntent(R.id.btn_go,pendingIntent);
		notification.setCustomContentView(contentView);
		getManager().notify(3,notification.build());
	}

	@OnClick({R.id.nf_btn,R.id.nf_btn2,R.id.nf_btn3})
	public void onViewClicked(View view) {
		int id = view.getId();
		switch (id){
			case R.id.nf_btn:
				sendNotification();
				break;
			case R.id.nf_btn2:
				sendProgressNotification();
				break;
			case R.id.nf_btn3:
				sendCustomNotification();
				break;
		}
	}

	private NotificationManager getManager(){
		return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	private NotificationCompat.Builder getNotification(String title,String text){
		NotificationCompat.Builder builder;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			NotificationChannel channel = new NotificationChannel("channel_这是id", "这是name", NotificationManager.IMPORTANCE_DEFAULT);
			channel.canBypassDnd();//可否绕过请勿打扰模式
			channel.enableLights(true);//手机闪光
			channel.setLockscreenVisibility(VISIBILITY_SECRET);//锁屏显示通知
			channel.setLightColor(Color.RED);//灯的颜色
			channel.canShowBadge();//桌面是否可以显示角标
			channel.enableVibration(true);//是否可以震动
			channel.getAudioAttributes();//获取系统通知响铃
			channel.getGroup();//获取通知渠道组
			channel.setBypassDnd(true);//设置可以绕过
			channel.setVibrationPattern(new long[]{100,100,100});
			channel.shouldShowLights();//是否会闪光

			getManager().createNotificationChannel(channel);

			builder = new NotificationCompat.Builder(this, "channel_这是id");
			if (!TextUtils.isEmpty(text))builder.setContentText(text);
		}else {
			builder = new NotificationCompat.Builder(this,null);
			//魅族5.1 text内不能含有 ！
			if (!TextUtils.isEmpty(text))builder.setContentText(text);
		}
		builder.setAutoCancel(true);
		if (!TextUtils.isEmpty(title))builder.setContentTitle(title);
		builder.setSmallIcon(R.mipmap.ic_launcher);
		return builder;
	}
}
