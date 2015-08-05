package com.yulinoo.plat.life.net.protocal;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;

import com.yulinoo.plat.life.utils.FieldValueUtil;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;

public class GetProtocal implements Protocal {

	@Override
	public HttpRequestBase buildData(Object reqObject, HTTP_DATA_FORMAT format, String URL, HttpClient httpClient) {

		try {
			HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
			StringBuffer queryString = new StringBuffer();
			if (reqObject != null) {
				String[] fileds = FieldValueUtil.getFiledName(reqObject);

				for (String filed : fileds) {
					if (FieldValueUtil.getFieldValueByName(filed, reqObject) != null) {
						if (queryString.length() == 0) {
							queryString.append("?");
						} else {
							queryString.append("&");
						}
						queryString.append(filed).append("=").append(FieldValueUtil.getFieldValueByName(filed, reqObject) + "");
					}
				}

			}
			URL = URL + queryString.toString();

		} catch (Exception e) {
		}

		HttpGet httpGet = new HttpGet(URL);

		return httpGet;
	}

}
