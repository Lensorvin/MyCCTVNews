package com.ccavnews.service;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import com.ccavnews.model.FileUtils;
import com.ccavnews.model.MyApplication;
import com.ccavnews.model.NewsItem;
import com.ccavnews.model.NewsUtils;
import com.ccavnews.model.SharePreferenceUtils;

public class LoadNewsService extends IntentService {

	public LoadNewsService() {
		super("LoadNewsService");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		String url = intent.getStringExtra("contentUrl");
		String date = intent.getStringExtra("date");
		NewsUtils nu = new NewsUtils();
		ArrayList<NewsItem> newsList = new ArrayList<NewsItem>();
		FileUtils fu = new FileUtils();
		try {
			newsList = fu.readFile(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果读取为空，则重新下载
		if (newsList == null || "".equals(newsList)) {
			newsList = nu.buildNewsItemsList(url);
			if (newsList != null && !"".equals(newsList)) {
				try {
					fu.writeFile(newsList, date);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		SharePreferenceUtils spu = new SharePreferenceUtils();
		spu.saveNewsList(MyApplication.getAppContext(), newsList);
	}

	private boolean checkDate(int duration) {
		return false;
	}
}
