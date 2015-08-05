package com.yulinoo.plat.life.bean;

import java.io.Serializable;
import java.util.List;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "ConcernNumber")
public class ConcernNumber extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "alongSid")
	private Long alongSid;
	@Column(name = "concernAccSid")
	private Long concernAccSid;
	@Column(name = "number")
	private Integer number;
	
	public static List<ConcernNumber> getUsrConcernNumber(Long accSid)
	{
		return new Select().from(ConcernNumber.class).where("alongSid=?",accSid).execute();
	}
	public static void setConcernNumberReaded(Long accSid,Long concernAccSid)
	{
		new Delete().from(ConcernNumber.class).where("alongSid=? and concernAccSid=?",accSid,concernAccSid).execute();
	}
	
	public Long getAlongSid() {
		return alongSid;
	}
	public void setAlongSid(Long alongSid) {
		this.alongSid = alongSid;
	}
	public Long getConcernAccSid() {
		return concernAccSid;
	}
	public void setConcernAccSid(Long concernAccSid) {
		this.concernAccSid = concernAccSid;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	
	
}