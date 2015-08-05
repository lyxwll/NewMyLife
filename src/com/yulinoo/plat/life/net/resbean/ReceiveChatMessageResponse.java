package com.yulinoo.plat.life.net.resbean;

import java.util.List;

import com.yulinoo.plat.life.bean.ChatMessage;

public class ReceiveChatMessageResponse extends NormalResponse{
	private List<ChatMessage> list;

	public List<ChatMessage> getList() {
		return list;
	}

	public void setList(List<ChatMessage> list) {
		this.list = list;
	}
}
