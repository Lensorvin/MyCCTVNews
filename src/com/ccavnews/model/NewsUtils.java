package com.ccavnews.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class NewsUtils {
	final String itemIndex = Configeration.itemIndex;
	final String contentIndex = Configeration.contentIndex;

	public ArrayList<NewsItem> getNewsItemFromURL(String url) {
		Document doc = null;
		ArrayList<NewsItem> itemList = new ArrayList<NewsItem>();
		try {
			doc = Jsoup.connect(url).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 根据页面元素ID获取新闻列表体
		Element content = doc.getElementById(itemIndex);
		Elements pieces = content.getElementsByAttributeValue("target",
				"_blank");
		for (int i = 1; i <= pieces.size() - 1; i++) {
			// 标题去前缀
			NewsItem item = new NewsItem(pieces.get(i).select("a").text()
					.substring(Configeration.titlePref.length()), null, pieces
					.get(i).attr("href"));
			// getNewsFromURL(item);
			itemList.add(item);
		}
		return itemList;
	}

	public ArrayList<String> getNewsTitleFromURL(String url) throws Exception {
		Document doc = null;
		ArrayList<String> itemList = new ArrayList<String>();
		try {
			doc = Jsoup.connect(url).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 根据页面元素ID获取新闻列表体
		Element content = doc.getElementById(itemIndex);
		Elements pieces = content.getElementsByAttributeValue("target",
				"_blank");
		for (int i = 1; i <= pieces.size() - 1; i++) {
			// 标题去前缀
			String title = pieces.get(i).select("a").text()
					.substring(Configeration.titlePref.length());
			itemList.add(title);
		}
		return itemList;
	}

	public List<Map<String, Object>> getNewsTitleAndUrlFromURL(String url)
			throws Exception {
		Document doc = null;
		Map<String, Object> mMap = null;
		List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
		try {
			doc = Jsoup.connect(url).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 根据页面元素ID获取新闻列表体
		Element content = doc.getElementById(itemIndex);
		Elements pieces = content.getElementsByAttributeValue("target",
				"_blank");
		for (int i = 1; i <= pieces.size() - 1; i++) {
			// 标题去前缀
			String title = pieces.get(i).select("a").text()
					.substring(Configeration.titlePref.length());
			String contentUrl = pieces.get(i).attr("href");
			mMap = new HashMap<String, Object>();
			mMap.put("contentUrl", contentUrl);
			mMap.put("title", title);
			itemList.add(mMap);
		}
		return itemList;
	}

	public String getNewsFromURL(String urlstr) {
		Document doc = null;
		String contentTemp = null;
		if ("".equals(urlstr) || urlstr == null) {
			return null;
		}
		Log.i("getNewsFromURL  urlstr = ", "urlstr =" + urlstr);
		try {
			doc = Jsoup.connect(urlstr).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (doc == null || "".equals(doc)) {
			Log.i("buildNewsItemsList  getNewsFromURL", "doc is null!!!!!!!!!!");
		}
		// 根据页面元素ID获取新闻正文体
		Element content = doc.getElementById(Configeration.contentIndex);
		Elements sections = content.select("p");
		StringBuffer buffer = new StringBuffer();
		for (Element section : sections) {
			if (section.select("strong").size() != 0
					&& !section.select("strong").isEmpty()) {
				// 国内快讯小标题处理
				buffer.append("\n");
				buffer.append("        ");
				buffer.append(section.select("strong").text().toString());
				buffer.append("\n");
				// contentTemp += section.select("strong").text().toString()
				// + "\n";
			} else {
				buffer.append("\n");
				buffer.append(section.text().toString());
				buffer.append("\n");
				// contentTemp += section.text().toString() + "\n";
			}
		}
		// 新闻去前缀
		// String news =
		// contentTemp.substring(Configeration.contentPref.length());
		String news = buffer.toString();
		// newsItem.setContent(contentTemp.substring(Configeration.contentPref
		// .length()));
		return news;
	}

	public ArrayList<NewsItem> buildNewsItemsList(String urlstr) {
		Document doc = null;
		ArrayList<NewsItem> itemList = new ArrayList<NewsItem>();
		Log.i("buildNewsItemsList  urlstr = ", "urlstr =" + urlstr);
		try {
			doc = Jsoup.connect(urlstr).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 根据页面元素ID获取新闻列表体
		if (doc == null || "".equals(doc)) {
			Log.i("buildNewsItemsList", "doc is null!!!!!!!!!!");
		}
		Element content = doc.getElementById(itemIndex);
		Elements pieces = content.getElementsByAttributeValue("target",
				"_blank");
		for (int i = 1; i <= pieces.size() - 1; i++) {
			String news = getNewsFromURL(pieces.get(i).attr("href"));
			NewsItem item = new NewsItem(pieces.get(i).select("a").text()
					.substring(Configeration.titlePref.length()), news, null);

			itemList.add(item);
		}
		return itemList;
	}
}
