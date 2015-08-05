
package com.yulinoo.plat.life.net.reqbean;
//发送聊天室消息请求
public class SendChatMessageReq {
    private Long fromAreaSid;
    private String fromAreaName;
    private Long toAreaSid;
    private String mevalue;
    private String content;
	public Long getFromAreaSid() {
		return fromAreaSid;
	}
	public void setFromAreaSid(Long fromAreaSid) {
		this.fromAreaSid = fromAreaSid;
	}
	public String getFromAreaName() {
		return fromAreaName;
	}
	public void setFromAreaName(String fromAreaName) {
		this.fromAreaName = fromAreaName;
	}
	public Long getToAreaSid() {
		return toAreaSid;
	}
	public void setToAreaSid(Long toAreaSid) {
		this.toAreaSid = toAreaSid;
	}
	public String getMevalue() {
		return mevalue;
	}
	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	
	
}
