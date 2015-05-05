package com.ccavnews.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author Vincent
 * @version build 2015年4月28日 下午10:37:35 类说明
 */
public class MsgReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 拿到进度，更新UI

		Toast.makeText(context, "今天的新闻已更新完毕，快来看哦！", Toast.LENGTH_SHORT).show();
	}

}
