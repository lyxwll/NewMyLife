package com.yulinoo.plat.life.bean;

import java.io.Serializable;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "ForumInfo")
public class ForumInfo extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "sid")
	private Long sid;
	@Column(name = "forumName")
	private String forumName;
	@Column(name = "alongCategorySid")
	private long alongCategorySid;
	@Column(name = "forumType")
	private int forumType;
	@Column(name = "productSid")
	private long productSid;//如果当前栏目为产品，那么此属性将会有值
	@Column(name = "commentCando")
	private Integer commentCando;//产品是否可以允许被评论
	@Column(name = "commentCanopen")
	private Integer commentCanopen;//产品评论是否可以公开
	@Column(name = "permissionPersional")
	private Integer permissionPersional;//是否允许个人发布
	@Column(name = "photoSrc")
	private String photoSrc;//栏目图片
	@Column(name = "accLevel")
	private int accLevel;//允许的账号级别
	@Column(name = "citySid")
	private Long citySid;//所属城市
	@Column(name = "updatedAt")
	private Long updatedAt;//更新时间
	@Column(name = "status")
	private int status;//状态，0表示逻辑删除，1表示有效
	
	
	public static final int FORUM_FORUM=0;
	public static final int FORUM_PRODUCT=1;
	
	private ProductInfo product;

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}


	public String getForumName() {
		return forumName;
	}

	public void setForumName(String forumName) {
		this.forumName = forumName;
	}

	public long getAlongCategorySid() {
		return alongCategorySid;
	}

	public void setAlongCategorySid(long alongCategorySid) {
		this.alongCategorySid = alongCategorySid;
	}

	public int getForumType() {
		return forumType;
	}

	public void setForumType(int forumType) {
		this.forumType = forumType;
	}

	public ProductInfo getProduct() {
		return product;
	}

	public void setProduct(ProductInfo product) {
		this.product = product;
	}

	public long getProductSid() {
		return productSid;
	}

	public void setProductSid(long productSid) {
		this.productSid = productSid;
	}

	public String getPhotoSrc() {
		return photoSrc;
	}

	public void setPhotoSrc(String photoSrc) {
		this.photoSrc = photoSrc;
	}

	public long getAccLevel() {
		return accLevel;
	}

	public void setAccLevel(int accLevel) {
		this.accLevel = accLevel;
	}

	public Long getCitySid() {
		return citySid;
	}

	public void setCitySid(Long citySid) {
		this.citySid = citySid;
	}

	

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getCommentCando() {
		return commentCando;
	}

	public void setCommentCando(Integer commentCando) {
		this.commentCando = commentCando;
	}

	public Integer getCommentCanopen() {
		return commentCanopen;
	}

	public void setCommentCanopen(Integer commentCanopen) {
		this.commentCanopen = commentCanopen;
	}

	public Integer getPermissionPersional() {
		return permissionPersional;
	}

	public void setPermissionPersional(Integer permissionPersional) {
		this.permissionPersional = permissionPersional;
	}
}