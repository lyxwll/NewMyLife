package com.yulinoo.plat.life.utils;

import java.util.ArrayList;

import com.yulinoo.plat.life.bean.NewsClassify;

public class Constants {
	public static final int INDEX=1;
	public static final int CONCERN=2;
	public static final int NEIGHBOUR=3;
	public static final int NEIGHBOURTALK=4;
	public static final int ABOUTME=5;
	public static ArrayList<NewsClassify> getData() {
		ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
		NewsClassify classify = new NewsClassify();
		classify.setId(INDEX);
		classify.setTitle("首页");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(CONCERN);
		classify.setTitle("关注");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(NEIGHBOUR);
		classify.setTitle("近邻");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(NEIGHBOURTALK);
		classify.setTitle("邻语");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(ABOUTME);
		classify.setTitle("我的");
		newsClassify.add(classify);
		return newsClassify;
	}
}
