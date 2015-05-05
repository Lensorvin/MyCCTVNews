package com.ccavnews.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccavnews.model.Configeration;
import com.ccavnews.model.FileUtils;
import com.ccavnews.model.NewsItem;
import com.ccavnews.model.NewsItemAdapter;
import com.ccavnews.model.NewsTitleAndUrlAdapter;
import com.ccavnews.model.NewsUtils;
import com.ccavnews.receiver.MsgReceiver;
import com.ccavnews.service.DailyRefreshService;
import com.xql.newsccav.R;

public class TitleActivity extends Activity implements OnClickListener {
	Handler handler;
	private static ArrayList<NewsItem> newsList = null;
	private static ArrayList<String> newsTitleList = null;
	private static List<Map<String, Object>> newsTitleUrlList = null;
	private EditText dateSelect;
	private TextView appName;
	private Button subButton;
	private DatePickerDialog datePickerDialog;
	private SimpleDateFormat dateFormatter;
	// YYYY-MM-DD
	private String todayString = null;
	// YYYYMMDD
	private String dateString = null;
	private String newsUrl = null;
	private ProgressDialog circleProgress;
	private MsgReceiver msgReceiver;
	private Intent reflashIntent;
	private ListView lv;
	private BaseAdapter ba;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.title_main);

		// 进度条
		circleProgress = new ProgressDialog(TitleActivity.this);
		circleProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		circleProgress.setMessage("内容正在拼命加载，马上回来!");
		// 注册广播接收器
		msgReceiver = new MsgReceiver();
		// 初始化日期组件
		initDateModule();

		// 启动后台定时刷新服务
		Intent reflashIntent = new Intent(TitleActivity.this,
				DailyRefreshService.class);
		reflashIntent.putExtra("newsUrl", newsUrl);
		reflashIntent.putExtra("dateString", dateString);
		startService(reflashIntent);

		handler = getHandler();

		// 优先读取文件，如果存在则直接加载内容，否则再通过url获取
		GetFromFileThreadStart();

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	public void initDateModule() {
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date d = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
		todayString = sp.format(d);
		setUrlByDate(todayString);
		appName = (TextView) findViewById(R.id.app_nametext);
		appName.setText(R.string.app_name);
		dateSelect = (EditText) findViewById(R.id.date_select);
		dateSelect.setInputType(InputType.TYPE_NULL);
		dateSelect.setText(todayString);
		setDateTimeField();

		subButton = (Button) findViewById(R.id.subscribe_btn);
		subButton.setOnClickListener(this);
	}

	private void setDateTimeField() {
		dateSelect.setOnClickListener(this);

		Calendar newCalendar = Calendar.getInstance();
		datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				dateSelect.setText(dateFormatter.format(newDate.getTime()));
				// 根据日期选择器更新跳转URL
				setUrlByDate(dateSelect.getText().toString());

				circleProgress.show();

				GetFromFileThreadStart();
			}

		}, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
				newCalendar.get(Calendar.DAY_OF_MONTH));

	}

	/**
	 * @param dstr
	 *            根据日期String拼装URL
	 */
	public void setUrlByDate(String dstr) {
		String[] tempString = dstr.split("-");
		dateString = tempString[0] + tempString[1] + tempString[2];
		newsUrl = Configeration.CCTVnewsURLPref + dateString
				+ Configeration.CCTVnewsURLSurf;
		// newsUrl = Configeration.CCTVnewsURLPref + 20150414
		// + Configeration.CCTVnewsURLSurf;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private Handler getHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				circleProgress.cancel();
				// URL获取新闻失败
				if (msg.what < 0) {
					Toast.makeText(TitleActivity.this, "客官莫急，人家还没更新呢，看前几天的嘛",
							Toast.LENGTH_SHORT).show();
				}
				// 文件读取失败，再通过URL获取新闻
				else if (msg.what == 0) {
					GetFromUrlThreadStart();
				}
				// 文件读取成功，直接进行初始化
				else if (msg.what == 1) {
					initListViewByFile();
				}
				// 过URL获取新闻成功，进行初始化
				else if (msg.what > 1) {
					initListview();
				}
			}
		};
	}

	// 通过URL获取新闻列表方式，初始化ListView
	private void initListview() {
		ListView itemList = (ListView) findViewById(R.id.news_list);
		itemList.setAdapter(new NewsTitleAndUrlAdapter(this, newsTitleUrlList));
		itemList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				String contentUrl = (String) newsTitleUrlList.get(position)
						.get("contentUrl");
				String newsTitle = (String) newsTitleUrlList.get(position).get(
						"title");
				Intent intent = new Intent(TitleActivity.this,
						ContentActivity.class);
				intent.putExtra("contentUrl", contentUrl);
				intent.putExtra("newsTitle", newsTitle);
				startActivity(intent);
			}
		});
	}

	// 通过读取文件获取新闻列表方式，初始化ListView
	private void initListViewByFile() {
		ListView itemList = (ListView) findViewById(R.id.news_list);
		itemList.setAdapter(new NewsItemAdapter(this, newsList));
		itemList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				String contentUrl = (String) newsTitleUrlList.get(position)
						.get("contentUrl");
				String content = (String) newsTitleUrlList.get(position).get(
						"content");
				String newsTitle = (String) newsTitleUrlList.get(position).get(
						"title");
				Intent intent = new Intent(TitleActivity.this,
						ContentActivity.class);
				intent.putExtra("contentUrl", contentUrl);
				intent.putExtra("content", content);
				intent.putExtra("newsTitle", newsTitle);
				startActivity(intent);
			}
		});
	}

	private void GetFromUrlThreadStart() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				try {
					NewsUtils un = new NewsUtils();
					// newsList = un.getNewsItemFromURL(newsUrl);
					newsTitleUrlList = un.getNewsTitleAndUrlFromURL(newsUrl);
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
				}
				msg.what = newsTitleUrlList.size();
				handler.sendMessage(msg);
			}
		}).start();
	}

	private void GetFromFileThreadStart() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				try {
					// newsList = un.getNewsItemFromURL(newsUrl);
					FileUtils fu = new FileUtils();
					if (fu.fileIsExists(dateString)) {
						newsList = fu.readFile(dateString);
						if (null != newsList && "".equals(newsList)) {
							msg.what = 1;
						} else {
							msg.what = 0;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = 0;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == subButton) {

		} else if (v == dateSelect) {
			datePickerDialog.show();
		}

	}

	@Override
	protected void onDestroy() {
		// 注销广播
		// unregisterReceiver(msgReceiver);
		super.onDestroy();
	}
}
