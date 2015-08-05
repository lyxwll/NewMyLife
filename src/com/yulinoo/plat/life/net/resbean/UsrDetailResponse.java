package com.yulinoo.plat.life.net.resbean;

import com.yulinoo.plat.life.bean.Account;

public class UsrDetailResponse extends NormalResponse{
	private Account accout;

	public Account getAccout() {
		return accout;
	}

	public void setAccout(Account accout) {
		this.accout = accout;
	}
	
}
