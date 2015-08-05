package com.yulinoo.plat.life.net.resbean;

import java.util.List;

import com.yulinoo.plat.life.bean.AreaInfo;

public class AreaInfoResponse extends NormalResponse{
	private List<AreaInfo> list;

	public List<AreaInfo> getList() {
		return list;
	}

	public void setList(List<AreaInfo> list) {
		this.list = list;
	}

}
