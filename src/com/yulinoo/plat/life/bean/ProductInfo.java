package com.yulinoo.plat.life.bean;

import java.io.Serializable;

public class ProductInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int PERSONAL_CAN_PUBLISH = 1;// 个人允许在该产品下进行发布
	public static final int PERSONAL_CAN_NOT_PUBLISH = 2;// 个人不允许在该产品下进行发布

	private Integer commentCanopen;
	private Integer merchantCanChoice;
	private Integer commentCando;
	private Integer releaseDirection;
	private Integer showType;
	private Integer needSenderPhoneVerify;
	private Integer permissionPersional;
	private String productName;
	private Integer productType;
	private Long sid;
	private Integer isAttachment;

	public Integer getCommentCanopen() {
		return commentCanopen;
	}

	public void setCommentCanopen(Integer commentCanopen) {
		this.commentCanopen = commentCanopen;
	}

	public Integer getMerchantCanChoice() {
		return merchantCanChoice;
	}

	public void setMerchantCanChoice(Integer merchantCanChoice) {
		this.merchantCanChoice = merchantCanChoice;
	}

	public Integer getCommentCando() {
		return commentCando;
	}

	public void setCommentCando(Integer commentCando) {
		this.commentCando = commentCando;
	}

	public Integer getReleaseDirection() {
		return releaseDirection;
	}

	public void setReleaseDirection(Integer releaseDirection) {
		this.releaseDirection = releaseDirection;
	}

	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	public Integer getNeedSenderPhoneVerify() {
		return needSenderPhoneVerify;
	}

	public void setNeedSenderPhoneVerify(Integer needSenderPhoneVerify) {
		this.needSenderPhoneVerify = needSenderPhoneVerify;
	}

	public Integer getPermissionPersional() {
		return permissionPersional;
	}

	public void setPermissionPersional(Integer permissionPersional) {
		this.permissionPersional = permissionPersional;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public Integer getIsAttachment() {
		return isAttachment;
	}

	public void setIsAttachment(Integer isAttachment) {
		this.isAttachment = isAttachment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	//
	// private Long sid;
	// private String productName;
	// private long alongCategorySid;
	// private long alongMerchantSid;
	// private String prductCategory;
	// private int viewNumber;
	// private int praiseNumber;
	// private int okNumber;
	// private int relpyNumber;
	// private int picNumber;
	// private int commentNumber;
	// private int permissionPersional;//是否允许个人发布
	//
	//
	// public boolean checked = false;
	//
	//
	//
	//
	//
	//
	// public int getPermissionPersional() {
	// return permissionPersional;
	// }
	//
	// public void setPermissionPersional(int permissionPersional) {
	// this.permissionPersional = permissionPersional;
	// }
	//
	// public int getViewNumber() {
	// return viewNumber;
	// }
	//
	// public void setViewNumber(int viewNumber) {
	// this.viewNumber = viewNumber;
	// }
	//
	// public int getPraiseNumber() {
	// return praiseNumber;
	// }
	//
	// public void setPraiseNumber(int praiseNumber) {
	// this.praiseNumber = praiseNumber;
	// }
	//
	// public int getOkNumber() {
	// return okNumber;
	// }
	//
	// public void setOkNumber(int okNumber) {
	// this.okNumber = okNumber;
	// }
	//
	// public int getRelpyNumber() {
	// return relpyNumber;
	// }
	//
	// public void setRelpyNumber(int relpyNumber) {
	// this.relpyNumber = relpyNumber;
	// }
	//
	// public int getPicNumber() {
	// return picNumber;
	// }
	//
	// public void setPicNumber(int picNumber) {
	// this.picNumber = picNumber;
	// }
	//
	// public int getCommentNumber() {
	// return commentNumber;
	// }
	//
	// public void setCommentNumber(int commentNumber) {
	// this.commentNumber = commentNumber;
	// }
	//
	// public long getAlongCategorySid() {
	// return alongCategorySid;
	// }
	//
	// public void setAlongCategorySid(long alongCategorySid) {
	// this.alongCategorySid = alongCategorySid;
	// }
	//
	// public long getAlongMerchantSid() {
	// return alongMerchantSid;
	// }
	//
	// public void setAlongMerchantSid(long alongMerchantSid) {
	// this.alongMerchantSid = alongMerchantSid;
	// }
	//
	// public String getPrductCategory() {
	// return prductCategory;
	// }
	//
	// public void setPrductCategory(String prductCategory) {
	// this.prductCategory = prductCategory;
	// }
	//
	// public Long getSid() {
	// return sid;
	// }
	//
	// public void setSid(Long sid) {
	// this.sid = sid;
	// }
	//
	// public String getProductName() {
	// return productName;
	// }
	//
	// public void setProductName(String productName) {
	// this.productName = productName;
	// }

}