package com.ccavnews.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.ccavnews.model.AlarmReceiver;
import com.ccavnews.model.FileUtils;
import com.ccavnews.model.NewsItem;
import com.ccavnews.model.NewsUtils;
import com.ccavnews.model.ReflashNotification;

/**
 * @author Vincent
 * @version build 2015年4月27日 下午5:48:17 后台更新服务，每日定时启动
 */
public class DailyRefreshService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d("DailyRefreshService",
						"executed at " + new Date().toString());
				String newsUrl = intent.getStringExtra("newsUrl");
				String dateString = intent.getStringExtra("dateString");
				NewsUtils nu = new NewsUtils();
				ArrayList<NewsItem> newsList = new ArrayList<NewsItem>();
				FileUtils fu = new FileUtils();
				newsList = nu.buildNewsItemsList(newsUrl);
				if (newsList != null && !"".equals(newsList)) {
					try {
						fu.writeFile(newsList, dateString);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		// 启动定时器
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

		Intent i = new Intent(this, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, checkTime(), pi);
		// 更新完毕，发出消息栏通知
		ReflashNotification rf = new ReflashNotification();
		rf.createNotification(this);
		// intent.putExtra("progress", progress);
		// 启动广播通知主线程刷新
		Intent reflashIntent = new Intent("android.intent.action.REFLASH");
		sendBroadcast(reflashIntent);
		return super.onStartCommand(reflashIntent, flags, startId);
	}

	/**
	 * 
	 * @Title: checkTime
	 * 
	 * @Description: TODO
	 * 
	 * @return
	 * 
	 * @return: long
	 */
	private long checkTime() {
		long systemTime = System.currentTimeMillis();
		long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		// 这里时区需要设置一下，不然会有8个小时的时间差
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 20);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// 选择的定时时间
		long selectTime = calendar.getTimeInMillis();
		// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
		if (systemTime > selectTime) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			selectTime = calendar.getTimeInMillis();
		}
		// 计算现在时间到设定时间的时间差
		long time = selectTime - systemTime;
		firstTime += time;
		return firstTime;
	}
}
