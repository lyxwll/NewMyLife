package com.yulinoo.plat.life.net.resbean;

import com.yulinoo.plat.life.bean.MessageBox;

public class MessageBoxResponse extends NormalResponse{
	private MessageBox messageBox;

	public MessageBox getMessageBox() {
		return messageBox;
	}

	public void setMessageBox(MessageBox messageBox) {
		this.messageBox = messageBox;
	}

	
}
