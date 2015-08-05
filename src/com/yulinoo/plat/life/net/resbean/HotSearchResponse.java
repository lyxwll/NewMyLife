package com.yulinoo.plat.life.net.resbean;

import java.util.List;

import com.yulinoo.plat.life.bean.HotSearch;


public class HotSearchResponse extends NormalResponse{
	private List<HotSearch> list;
	
	public List<HotSearch> getList() {
		return list;
	}

	public void setList(List<HotSearch> list) {
		this.list = list;
	}
	
}
