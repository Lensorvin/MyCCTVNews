package com.ccavnews.model;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.ccavnews.activity.SplashActivity;
import com.xql.newsccav.R;

/**
 * @author Vincent
 * @version build 2015年4月28日 下午5:42:32 新闻更新通知
 */
public class ReflashNotification {
	public void createNotification(Context context) {
		Intent intent = new Intent(context, SplashActivity.class);
		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent, 0);

		Builder noti = new Notification.Builder(context)
				.setContentTitle("温馨贴士").setContentText("今天的新闻已经更新完毕")
				.setSmallIcon(R.drawable.icon).setContentIntent(pIntent);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, noti.build());

	}
}
