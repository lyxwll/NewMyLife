package com.yulinoo.plat.life.net.protocal;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.yulinoo.plat.life.utils.FieldValueUtil;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;

public class PutProtocal implements Protocal {

	@Override
	public HttpRequestBase buildData(Object reqObject, HTTP_DATA_FORMAT format, String URL, HttpClient httpClient) {
		HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = new HttpPut(URL);
		try {
			HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
			if (reqObject != null) {
				if (format == HTTP_DATA_FORMAT.JSON) {
					httpEntityEnclosingRequestBase.setEntity(new StringEntity(new Gson().toJson(reqObject)));
				} else {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					String[] fileds = FieldValueUtil.getFiledName(reqObject);
					for (String filed : fileds) {
						params.add(new BasicNameValuePair(filed, FieldValueUtil.getFieldValueByName(filed, reqObject) + ""));
					}
					httpEntityEnclosingRequestBase.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				}
			}
		} catch (Exception e) {
		}
		return httpEntityEnclosingRequestBase;
	}

}
