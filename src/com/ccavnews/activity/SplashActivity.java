package com.ccavnews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.xql.newsccav.R;

/**
 * @author Vincent 启动页面
 */
public class SplashActivity extends Activity {
	private final int SPLASH_DISPLAY_LENGTH = 1500;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.splash_screen_layout);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				// 跳转主页面
				Intent mainIntent = new Intent(SplashActivity.this,
						TitleActivity.class);
				startActivity(mainIntent);
				finish();
			}
		}, SPLASH_DISPLAY_LENGTH);
	}

}
