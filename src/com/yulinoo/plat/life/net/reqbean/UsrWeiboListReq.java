package com.yulinoo.plat.life.net.reqbean;

//用户微博列表请求，一般用于论坛中个人发的帖子的请求
public class UsrWeiboListReq {
	private Long alongCategorySid;
	private Long alongForumSid;
	private Long productSid;
	private Long alongAreaSid;
	private Long merchantSid;
	private Integer pageNo;
	private Long accSid;//某个人发的所有帖子
	private String mevalue;
	public Long getAlongCategorySid() {
		return alongCategorySid;
	}
	public void setAlongCategorySid(Long alongCategorySid) {
		this.alongCategorySid = alongCategorySid;
	}
	public Long getAlongForumSid() {
		return alongForumSid;
	}
	public void setAlongForumSid(Long alongForumSid) {
		this.alongForumSid = alongForumSid;
	}
	public Long getProductSid() {
		return productSid;
	}
	public void setProductSid(Long productSid) {
		this.productSid = productSid;
	}
	public Long getAlongAreaSid() {
		return alongAreaSid;
	}
	public void setAlongAreaSid(Long alongAreaSid) {
		this.alongAreaSid = alongAreaSid;
	}
	public Long getMerchantSid() {
		return merchantSid;
	}
	public void setMerchantSid(Long merchantSid) {
		this.merchantSid = merchantSid;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Long getAccSid() {
		return accSid;
	}
	public void setAccSid(Long accSid) {
		this.accSid = accSid;
	}
	public String getMevalue() {
		return mevalue;
	}
	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}
	

}
