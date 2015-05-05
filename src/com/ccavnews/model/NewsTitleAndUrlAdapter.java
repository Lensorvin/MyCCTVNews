package com.ccavnews.model;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xql.newsccav.R;

public class NewsTitleAndUrlAdapter extends BaseAdapter {
	private List<Map<String, Object>> listData;
	private LayoutInflater layoutInflater;
	private Context myContext;

	public NewsTitleAndUrlAdapter(Context aContext,
			List<Map<String, Object>> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(aContext);
		myContext = aContext;
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_title_layout,
					null);
			holder = new ViewHolder();
			holder.titleView = (TextView) convertView.findViewById(R.id.title);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.play_icon);
			holder.urlView = (TextView) convertView
					.findViewById(R.id.content_url);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.urlView.setText(listData.get(position).get("contentUrl")
				.toString());
		holder.titleView
				.setText(listData.get(position).get("title").toString());
		holder.imageView.setImageResource(R.drawable.news_cut);
		return convertView;
	}

	static class ViewHolder {
		TextView titleView;
		ImageView imageView;
		TextView urlView;
	}
}
