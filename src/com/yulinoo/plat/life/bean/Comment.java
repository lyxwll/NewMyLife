package com.yulinoo.plat.life.bean;

//评论内容
public class Comment extends BaseBean{
	
	private long sid;
	private int commentType;
	private long createdDate;
	private String commentCont;
	
	private long alongGoodsSid;
	private long alongMerchantSid;
	
	private long alongAccSid;
	private String accName;
	private String headPicture;
	
	private long alongAreaSid;
	private String areaName;
	private int sex;
	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
	}
	public int getCommentType() {
		return commentType;
	}
	public void setCommentType(int commentType) {
		this.commentType = commentType;
	}
	public long getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}
	public String getCommentCont() {
		return commentCont;
	}
	public void setCommentCont(String commentCont) {
		this.commentCont = commentCont;
	}
	public long getAlongGoodsSid() {
		return alongGoodsSid;
	}
	public void setAlongGoodsSid(long alongGoodsSid) {
		this.alongGoodsSid = alongGoodsSid;
	}
	public long getAlongMerchantSid() {
		return alongMerchantSid;
	}
	public void setAlongMerchantSid(long alongMerchantSid) {
		this.alongMerchantSid = alongMerchantSid;
	}
	public long getAlongAccSid() {
		return alongAccSid;
	}
	public void setAlongAccSid(long alongAccSid) {
		this.alongAccSid = alongAccSid;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getHeadPicture() {
		return headPicture;
	}
	public void setHeadPicture(String headPicture) {
		this.headPicture = headPicture;
	}
	public long getAlongAreaSid() {
		return alongAreaSid;
	}
	public void setAlongAreaSid(long alongAreaSid) {
		this.alongAreaSid = alongAreaSid;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	
	
}
