
package com.yulinoo.plat.life.bean;

import java.io.Serializable;
import java.util.List;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "ChatMessage")
public class ChatMessage extends BaseBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@Column(name = "fromAreaSid")
    private long fromAreaSid;
	@Column(name = "fromAreaName")
    private String fromAreaName;
	@Column(name = "content")
    private String content;
	@Column(name = "accSid")
    private long accSid;
	@Column(name = "accName")
    private String accName;
	@Column(name = "userHead")
    private String userHead;
	@Column(name = "sendTime")
    private long sendTime;
	@Column(name = "isMyMessage")
    private boolean isMyMessage;//表示是不是自己的消息
	@Column(name = "sex")
    private int sex;
	
	public static List<ChatMessage> getChatMessage(Long endTime)
	{
		return new Select().from(ChatMessage.class).where("sendTime<?",endTime).orderBy("sendTime desc").limit(50).execute();
	}
	
	public long getFromAreaSid() {
		return fromAreaSid;
	}


	public void setFromAreaSid(long fromAreaSid) {
		this.fromAreaSid = fromAreaSid;
	}


	public String getFromAreaName() {
		return fromAreaName;
	}


	public void setFromAreaName(String fromAreaName) {
		this.fromAreaName = fromAreaName;
	}


	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public long getAccSid() {
		return accSid;
	}
	public void setAccSid(long accSid) {
		this.accSid = accSid;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getUserHead() {
		return userHead;
	}
	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}
	public long getSendTime() {
		return sendTime;
	}
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	public boolean isMyMessage() {
		return isMyMessage;
	}
	public void setMyMessage(boolean isMyMessage) {
		this.isMyMessage = isMyMessage;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
}
