package com.yulinoo.plat.life.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import config.AppContext;

//分类下的标签
@Table(name = "MessageCenterInfo")
public class MessageCenterInfo extends BaseBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@Column(name = "sid")
	private Long sid;//记录主键
	@Column(name = "responseUsrAreaName")
	private String responseUsrAreaName;//响应者小区名称
	@Column(name = "responseUsrAreaSid")
	private Long responseUsrAreaSid;//响应者小区ID
	@Column(name = "responseUsrSid")
	private Long responseUsrSid;//响应者ID
	@Column(name = "responseNickName")
	private String responseNickName;//响应者昵称
	@Column(name = "responseTime")
	private Long responseTime;//响应时间
	@Column(name = "responseContent")
	private String responseContent;//响应内容
	@Column(name = "responseHeaderPicture")
	private String responseHeaderPicture;//响应者头像
	@Column(name = "readStatus")
	private Integer readStatus;//阅读状态
	@Column(name = "messageType")
	private Integer messageType;//消息类型
	@Column(name = "alongGoodsSid")
	private Long alongGoodsSid;//所属商品主键
	@Column(name = "goodsPublishUsrHeaderPicture")
	private String goodsPublishUsrHeaderPicture;//所属用户头像
	@Column(name = "goodsPublishUsrName")
	private String goodsPublishUsrName;//所属用户昵称
	@Column(name = "goodsPublishUsrSid")
	private Long goodsPublishUsrSid;//所属用户主键
	@Column(name = "goodsPublishContent")
	private String goodsPublishContent;//商品(主题)内容
	@Column(name = "goodsPublishTime")
	private Long goodsPublishTime;//商品发表时间
	@Column(name = "goodsPublishAreaName")
	private String goodsPublishAreaName;//商品发表小区
	@Column(name = "goodsPublishAreaSid")
	private String goodsPublishAreaSid;//商品发表小区ID

	//获取我的最近联系人消息列表
	public static List<MessageCenterInfo> getMessageCenterInfo()
	{
		Account account=AppContext.currentAccount();
		if(account!=null)
		{
			return new Select().from(MessageCenterInfo.class).where("goodsPublishUsrSid=?",account.getSid())
					.orderBy("responseTime desc").execute();
		}else
		{
			return new ArrayList<MessageCenterInfo>();
		}
	}

	public static void updateMci(MessageCenterInfo mci)
	{
		Account account=AppContext.currentAccount();
		if(account!=null)
		{
			List<MessageCenterInfo> lst=new Select().from(MessageCenterInfo.class).where("goodsPublishUsrSid=? and responseUsrSid=?",account.getSid(),mci.getResponseUsrSid()).execute();
			if(lst!=null&&lst.size()==1)
			{
				MessageCenterInfo mc=lst.get(0);
				mc.setResponseContent(mci.getResponseContent());
				mc.setResponseTime(mci.getResponseTime());
				mc.save();
			}
		}
	}


	public String getResponseUsrAreaName() {
		return responseUsrAreaName;
	}
	public void setResponseUsrAreaName(String responseUsrAreaName) {
		this.responseUsrAreaName = responseUsrAreaName;
	}
	public Long getResponseUsrAreaSid() {
		return responseUsrAreaSid;
	}
	public void setResponseUsrAreaSid(Long responseUsrAreaSid) {
		this.responseUsrAreaSid = responseUsrAreaSid;
	}
	public Long getResponseUsrSid() {
		return responseUsrSid;
	}
	public void setResponseUsrSid(Long responseUsrSid) {
		this.responseUsrSid = responseUsrSid;
	}
	public String getResponseNickName() {
		return responseNickName;
	}
	public void setResponseNickName(String responseNickName) {
		this.responseNickName = responseNickName;
	}
	public Long getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Long responseTime) {
		this.responseTime = responseTime;
	}
	public String getResponseContent() {
		return responseContent;
	}
	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}
	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
	}
	public String getResponseHeaderPicture() {
		return responseHeaderPicture;
	}
	public void setResponseHeaderPicture(String responseHeaderPicture) {
		this.responseHeaderPicture = responseHeaderPicture;
	}
	public Integer getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(Integer readStatus) {
		this.readStatus = readStatus;
	}
	public Integer getMessageType() {
		return messageType;
	}
	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}
	public Long getAlongGoodsSid() {
		return alongGoodsSid;
	}
	public void setAlongGoodsSid(Long alongGoodsSid) {
		this.alongGoodsSid = alongGoodsSid;
	}
	public String getGoodsPublishUsrHeaderPicture() {
		return goodsPublishUsrHeaderPicture;
	}
	public void setGoodsPublishUsrHeaderPicture(String goodsPublishUsrHeaderPicture) {
		this.goodsPublishUsrHeaderPicture = goodsPublishUsrHeaderPicture;
	}
	public Long getGoodsPublishUsrSid() {
		return goodsPublishUsrSid;
	}
	public void setGoodsPublishUsrSid(Long goodsPublishUsrSid) {
		this.goodsPublishUsrSid = goodsPublishUsrSid;
	}
	public String getGoodsPublishContent() {
		return goodsPublishContent;
	}
	public void setGoodsPublishContent(String goodsPublishContent) {
		this.goodsPublishContent = goodsPublishContent;
	}
	public Long getGoodsPublishTime() {
		return goodsPublishTime;
	}
	public void setGoodsPublishTime(Long goodsPublishTime) {
		this.goodsPublishTime = goodsPublishTime;
	}
	public String getGoodsPublishAreaName() {
		return goodsPublishAreaName;
	}
	public void setGoodsPublishAreaName(String goodsPublishAreaName) {
		this.goodsPublishAreaName = goodsPublishAreaName;
	}
	public String getGoodsPublishUsrName() {
		return goodsPublishUsrName;
	}
	public void setGoodsPublishUsrName(String goodsPublishUsrName) {
		this.goodsPublishUsrName = goodsPublishUsrName;
	}
	public String getGoodsPublishAreaSid() {
		return goodsPublishAreaSid;
	}
	public void setGoodsPublishAreaSid(String goodsPublishAreaSid) {
		this.goodsPublishAreaSid = goodsPublishAreaSid;
	}
}
