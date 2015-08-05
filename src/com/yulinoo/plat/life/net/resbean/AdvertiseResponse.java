package com.yulinoo.plat.life.net.resbean;

import com.yulinoo.plat.life.bean.Advertise;

public class AdvertiseResponse extends NormalResponse{
	private Advertise merchantAdvert;

	public Advertise getMerchantAdvert() {
		return merchantAdvert;
	}

	public void setMerchantAdvert(Advertise merchantAdvert) {
		this.merchantAdvert = merchantAdvert;
	}

	
}
