package com.ccavnews.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;

import com.xql.newsccav.R;

/**
 * @author Vincent
 * @version build 2015年4月28日 下午5:34:06 类说明
 */
public class NotificationActivity extends Activity {
	public void createNotification(View view) {
		Intent intent = new Intent(this, SplashActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		Builder noti = new Notification.Builder(this).setContentTitle("温馨贴士")
				.setContentText("今天的新闻已经更新完毕").setSmallIcon(R.drawable.icon)
				.setContentIntent(pIntent);
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		notificationManager.notify(0, noti.build());

	}
}
