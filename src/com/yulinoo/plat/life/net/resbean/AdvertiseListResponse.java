package com.yulinoo.plat.life.net.resbean;

import java.util.List;

import com.yulinoo.plat.life.bean.Advertise;

public class AdvertiseListResponse extends NormalResponse{

	private List<Advertise>  list;

	public List<Advertise> getList() {
		return list;
	}

	public void setList(List<Advertise> list) {
		this.list = list;
	}
}
