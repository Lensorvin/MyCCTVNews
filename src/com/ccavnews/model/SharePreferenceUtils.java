package com.ccavnews.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

public class SharePreferenceUtils {

	public static final String PREFS_NAME = "NEWS_LIST";
	public static final String FAVORITES = "NewsList_Favorite";

	public SharePreferenceUtils() {
		super();
	}

	// This four methods are used for maintaining favorites.
	public void saveNewsList(Context context, ArrayList<NewsItem> newsList) {
		SharedPreferences settings;
		Editor editor;

		settings = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		editor = settings.edit();

		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(newsList);

		editor.putString(FAVORITES, jsonFavorites);

		editor.commit();
	}

	public void addNewsList(Context context, NewsItem newsItem) {
		ArrayList<NewsItem> favorites = getNewsList(context);
		if (favorites == null)
			favorites = new ArrayList<NewsItem>();
		favorites.add(newsItem);
		saveNewsList(context, favorites);
	}

	public void removeNewsList(Context context, NewsItem product) {
		ArrayList<NewsItem> favorites = getNewsList(context);
		if (favorites != null) {
			favorites.remove(product);
			saveNewsList(context, favorites);
		}
	}

	public ArrayList<NewsItem> getNewsList(Context context) {
		SharedPreferences settings;
		List<NewsItem> favorites;

		settings = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);

		if (settings.contains(FAVORITES)) {
			String jsonFavorites = settings.getString(FAVORITES, null);
			Gson gson = new Gson();
			NewsItem[] favoriteItems = gson.fromJson(jsonFavorites,
					NewsItem[].class);

			favorites = Arrays.asList(favoriteItems);
			favorites = new ArrayList<NewsItem>(favorites);
		} else
			return null;

		return (ArrayList<NewsItem>) favorites;
	}
}
