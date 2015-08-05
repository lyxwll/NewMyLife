package com.yulinoo.plat.life.net.service;

import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;


public class RequestBean<T> {
    private String URL;                                                
    private Object requestBody;                                           
    private HTTP_DATA_FORMAT requsetFormat = HTTP_DATA_FORMAT.FORM;     
    private HTTP_METHOD httpMethod    = HTTP_METHOD.POST;                
    private Class<T>  responseBody;                                    
    
    	
	public HTTP_DATA_FORMAT getRequsetFormat() {
		return requsetFormat;
	}
	public void setRequsetFormat(HTTP_DATA_FORMAT requsetFormat) {
		this.requsetFormat = requsetFormat;
	}
	public HTTP_METHOD getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(HTTP_METHOD httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public Object getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(Object requestBody) {
		this.requestBody = requestBody;
	}
	public Class<T> getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(Class<T> responseBody) {
		this.responseBody = responseBody;
	}
    
    
}
