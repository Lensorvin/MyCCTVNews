package com.ccavnews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.ccavnews.model.NewsUtils;
import com.xql.newsccav.R;

public class ContentActivity extends Activity {
	private TextView title;
	private TextView concent;
	private String newsContent;
	private String url;
	private String newsTitle;
	Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent intent = getIntent();
		newsContent = intent.getStringExtra("content");
		url = intent.getStringExtra("contentUrl");
		newsTitle = intent.getStringExtra("newsTitle");
		handler = getHandler();
		ThreadStart();

	}

	private Handler getHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what < 0) {
					Toast.makeText(ContentActivity.this, "客官莫急，菜马上来",
							Toast.LENGTH_SHORT).show();
				} else {
					initListview();
				}
			}
		};
	}

	private void initListview() {
		setContentView(R.layout.news_content);
		title = (TextView) findViewById(R.id.title2);
		concent = (TextView) findViewById(R.id.content2);
		// concent.setMovementMethod(ScrollingMovementMethod.getInstance());
		title.setText(newsTitle);
		concent.setText(newsContent);
	}

	private void ThreadStart() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				try {
					NewsUtils un = new NewsUtils();
					// newsList = un.getNewsItemFromURL(newsUrl);
					if ("".equals(newsContent) || null == newsContent) {
						Log.i("ContentActivity thread ", "url =" + url);
						newsContent = un.getNewsFromURL(url);
					} else {

					}
					// Log.i("getNewsTitleFromURL ", "" + newsTitleList.size());
					msg.what = newsContent.length();
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
}
