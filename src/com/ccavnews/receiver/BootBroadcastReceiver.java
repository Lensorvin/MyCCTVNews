package com.ccavnews.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ccavnews.service.DailyRefreshService;

/**
 * @author Vincent
 * @version build 2015年4月28日 下午4:35:15 类说明
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
	// 重写onReceive方法
	@Override
	public void onReceive(Context context, Intent intent) {
		// 后边的XXX.class就是要启动的服务
		Intent service = new Intent(context, DailyRefreshService.class);
		context.startService(service);
		Log.v("TAG", "开机自动服务自动启动.....");
		// 启动应用，参数为需要自动启动的应用的包名
	}
}
