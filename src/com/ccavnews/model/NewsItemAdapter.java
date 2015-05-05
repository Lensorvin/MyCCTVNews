package com.ccavnews.model;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xql.newsccav.R;

public class NewsItemAdapter extends BaseAdapter {
	private ArrayList<NewsItem> listData;
	private LayoutInflater layoutInflater;

	public NewsItemAdapter(Context aContext, ArrayList<NewsItem> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(aContext);
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
			convertView = layoutInflater
					.inflate(R.layout.list_row_layout, null);
			holder = new ViewHolder();
			holder.titleView = (TextView) convertView.findViewById(R.id.title);
			holder.contentView = (TextView) convertView
					.findViewById(R.id.content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.titleView.setText(listData.get(position).getTitle());
		holder.contentView.setText(listData.get(position).getContent());
		return convertView;
	}

	static class ViewHolder {
		TextView titleView;
		TextView contentView;
	}
}
