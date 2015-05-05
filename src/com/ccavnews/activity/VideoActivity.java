package com.ccavnews.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.xql.newsccav.R;

public class VideoActivity extends Activity {
	VideoView videoView;
	MediaController mediaController;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_layout);

		videoView = (VideoView) findViewById(R.id.myvideoview);
		Intent intent = getIntent();
		String url = intent.getStringExtra("videoUrl");

		playvideo(url);

	}

	public void playvideo(String videopath) {
		Log.e("entered", "playvide");
		Log.e("path is", "" + videopath);
		try {
			progressDialog = ProgressDialog.show(VideoActivity.this, "",
					"Buffering video...", false);
			progressDialog.setCancelable(true);
			getWindow().setFormat(PixelFormat.TRANSLUCENT);

			mediaController = new MediaController(VideoActivity.this);

			Uri video = Uri.parse(videopath);
			videoView.setMediaController(mediaController);
			videoView.setVideoURI(video);

			videoView.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					progressDialog.dismiss();
					videoView.start();
				}
			});

		} catch (Exception e) {
			progressDialog.dismiss();
			System.out.println("Video Play Error :" + e.getMessage());
		}

	}
}
