package com.yulinoo.plat.life.net.resbean;

import java.util.List;

import com.yulinoo.plat.life.bean.Merchant;

public class MerchantResponse extends NormalResponse{
	private List<Merchant> list;

	public List<Merchant> getList() {
		return list;
	}

	public void setList(List<Merchant> list) {
		this.list = list;
	}

}
