package com.yulinoo.plat.life.net.resbean;

import java.util.List;

import com.yulinoo.plat.life.bean.UserGroup;

public class UsrGroupResponse extends NormalResponse{
	private List<UserGroup> list;

	public List<UserGroup> getList() {
		return list;
	}

	public void setList(List<UserGroup> list) {
		this.list = list;
	}

}
