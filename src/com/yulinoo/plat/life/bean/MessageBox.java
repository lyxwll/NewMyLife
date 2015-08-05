package com.yulinoo.plat.life.bean;

import java.util.List;
import java.util.Map;

public class MessageBox {
	private Integer commentNumber;
	private Integer praiseNumber;
	private Integer messageTotal;
	private Integer chatBoxNumber;
	private Integer letterNumber;
	private Map<Long, Integer> attUpdate;
	
	private List<MessageCenterInfo> contactList;
	public Integer getCommentNumber() {
		return commentNumber;
	}
	public void setCommentNumber(Integer commentNumber) {
		this.commentNumber = commentNumber;
	}
	public Integer getPraiseNumber() {
		return praiseNumber;
	}
	public void setPraiseNumber(Integer praiseNumber) {
		this.praiseNumber = praiseNumber;
	}
	public Integer getMessageTotal() {
		return messageTotal;
	}
	public void setMessageTotal(Integer messageTotal) {
		this.messageTotal = messageTotal;
	}
	public Integer getChatBoxNumber() {
		return chatBoxNumber;
	}
	public void setChatBoxNumber(Integer chatBoxNumber) {
		this.chatBoxNumber = chatBoxNumber;
	}
	public Integer getLetterNumber() {
		return letterNumber;
	}
	public void setLetterNumber(Integer letterNumber) {
		this.letterNumber = letterNumber;
	}
	public List<MessageCenterInfo> getContactList() {
		return contactList;
	}
	public void setContactList(List<MessageCenterInfo> contactList) {
		this.contactList = contactList;
	}
	public Map<Long, Integer> getAttUpdate() {
		return attUpdate;
	}
	public void setAttUpdate(Map<Long, Integer> attUpdate) {
		this.attUpdate = attUpdate;
	}
	
	
}
