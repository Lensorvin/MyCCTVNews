package com.ccavnews.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ccavnews.service.DailyRefreshService;

/**
 * @author Vincent
 * @version build 2015年4月27日 下午5:49:34 类说明
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, DailyRefreshService.class);
		context.startService(i);
	}
}
