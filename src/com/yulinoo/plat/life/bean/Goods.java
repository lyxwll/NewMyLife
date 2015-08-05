package com.yulinoo.plat.life.bean;

import java.io.Serializable;

//商品信息
public class Goods extends BaseBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private long sid;//商品本身的主键
	private long createDate;//发布时间
	private String goodsDesc;//商品内容，即微博内容
	//private List<GoodsPic>	photoArray;//商品图片
	private long accSid;//发贴者的账号ID
	private int okNumber;//点赞次数
	private int commentNumber;//评论次数
	private int viewNumber;//浏览次数
	
	private String accName;//发表者的名字即昵称
	private String headPicture;//发表者的头像
	private int sex;//发帖者的性别
	
	private long alongForumSid;//所属栏目主键
	private String forumName;//栏目名称
	
	private long alongAreaSid;//所在小区ID
	private String areaName;//所在小区
	
	private long alongMerchantSid;//所属商家
	
	private String goodsPhotoArray;//产品的图片列表，以逗号分隔开
	
	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
	}
	public String getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public int getOkNumber() {
		return okNumber;
	}
	public void setOkNumber(int okNumber) {
		this.okNumber = okNumber;
	}
	public int getCommentNumber() {
		return commentNumber;
	}
	public void setCommentNumber(int commentNumber) {
		this.commentNumber = commentNumber;
	}
//	public List<GoodsPic> getPhotoArray() {
//		return photoArray;
//	}
//	public void setPhotoArray(List<GoodsPic> photoArray) {
//		this.photoArray = photoArray;
//	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public long getAlongForumSid() {
		return alongForumSid;
	}
	public void setAlongForumSid(long alongForumSid) {
		this.alongForumSid = alongForumSid;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public long getAlongAreaSid() {
		return alongAreaSid;
	}
	public void setAlongAreaSid(long alongAreaSid) {
		this.alongAreaSid = alongAreaSid;
	}
	
	public String getHeadPicture() {
		return headPicture;
	}
	public void setHeadPicture(String headPicture) {
		this.headPicture = headPicture;
	}
	public long getAlongMerchantSid() {
		return alongMerchantSid;
	}
	public void setAlongMerchantSid(long alongMerchantSid) {
		this.alongMerchantSid = alongMerchantSid;
	}
	public String getForumName() {
		return forumName;
	}
	public void setForumName(String forumName) {
		this.forumName = forumName;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public long getAccSid() {
		return accSid;
	}
	public void setAccSid(long accSid) {
		this.accSid = accSid;
	}
	public int getViewNumber() {
		return viewNumber;
	}
	public void setViewNumber(int viewNumber) {
		this.viewNumber = viewNumber;
	}
	public String getGoodsPhotoArray() {
		return goodsPhotoArray;
	}
	public void setGoodsPhotoArray(String goodsPhotoArray) {
		this.goodsPhotoArray = goodsPhotoArray;
	}
	
}
