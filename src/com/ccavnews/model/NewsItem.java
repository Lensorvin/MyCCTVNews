package com.ccavnews.model;

import java.io.Serializable;

public class NewsItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NewsItem(String title, String content, String url) {
		super();
		Title = title;
		Content = content;
		Url = url;
	}

	private String Title;
	private String Content;
	private String Url;

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

}
