package com.yulinoo.plat.life.net.reqbean;

public class ForumMerchantReq {
	
	private Long alongForumSid;
	private Long categorySid;
	private Integer pageNo;
	private Integer queryType;//查询类型
	private Long areaInfoSid;//所在小区
	public Long getAlongForumSid() {
		return alongForumSid;
	}
	public void setAlongForumSid(Long alongForumSid) {
		this.alongForumSid = alongForumSid;
	}
	public Long getCategorySid() {
		return categorySid;
	}
	public void setCategorySid(Long categorySid) {
		this.categorySid = categorySid;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getQueryType() {
		return queryType;
	}
	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}
	public Long getAreaInfoSid() {
		return areaInfoSid;
	}
	public void setAreaInfoSid(Long areaInfoSid) {
		this.areaInfoSid = areaInfoSid;
	}


}
