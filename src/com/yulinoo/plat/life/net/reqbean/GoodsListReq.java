package com.yulinoo.plat.life.net.reqbean;

public class GoodsListReq {
	private Long merchantSid;
	private Long productSid;
	private String mevalue;
	private Integer pageNo;
	public Long getMerchantSid() {
		return merchantSid;
	}
	public void setMerchantSid(Long merchantSid) {
		this.merchantSid = merchantSid;
	}
	
	public String getMevalue() {
		return mevalue;
	}
	public void setMevalue(String mevalue) {
		this.mevalue = mevalue;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Long getProductSid() {
		return productSid;
	}
	public void setProductSid(Long productSid) {
		this.productSid = productSid;
	}
	

	
}
