package com.yulinoo.plat.life.net.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;

import com.google.gson.Gson;
import com.yulinoo.plat.life.net.callback.ICallBack;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.utils.FieldValueUtil;


public class RequestService {
	private static final int CONN_TIMEOUT=5000;
	private static final int READ_TIMEOUT=10000;
	private static final String POST="POST";
	public <T>  void  request(final RequestBean<T> requestBean,final ICallBack callBack,final Context context){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String   URL           = requestBean.getURL();
				Object   requestBody   = requestBean.getRequestBody();
				HTTP_METHOD   method        = requestBean.getHttpMethod();
				HTTP_DATA_FORMAT   format        = requestBean.getRequsetFormat();
				Class<T> responseClass = requestBean.getResponseBody();
				request(requestBody, responseClass, URL, method, format,callBack,context);
			}
		}).start();
	}

	@SuppressWarnings("unchecked")
	private void request(Object requestBody, @SuppressWarnings("rawtypes") Class responseClazz,String URL,HTTP_METHOD method,HTTP_DATA_FORMAT format,ICallBack callBack,Context context){
		Map<String, Object> params=new HashMap<String, Object>();
		if (requestBody != null) {
			String[] fileds = FieldValueUtil.getFiledName(requestBody);
			for (String filed : fileds) {
				Object tmp=FieldValueUtil.getFieldValueByName(filed, requestBody);
				if(tmp!=null)
				{
					params.put(filed, tmp);
				}
			}
		}
		
		StringBuffer sBuffer = map2StringBuffer(params);
		final byte[] buffer = sBuffer.toString().getBytes(); 
		try {			
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//addHttpURLConnection(conn);
			conn.setConnectTimeout(CONN_TIMEOUT);//设置连接超时
			conn.setReadTimeout(READ_TIMEOUT);//设置读取超时
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod(POST);
			conn.setRequestProperty("Content-Length", buffer.length + "");  
			conn.setRequestProperty("Accept", "text/html, */*; q=0.01");  
			conn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");  
			//conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");  

			OutputStream out = conn.getOutputStream();  
			out.write(buffer);//参数
			out.close();
			//addHttpURLConnection(null);
			if (conn.getResponseCode() != 200) 
			{
				callBack.onError("请求网络出错:" + conn.getResponseCode());
				//throw new Exception("返回错误："+conn.getResponseCode());
			}else
			{
				InputStream is = conn.getInputStream();//得到网络返回的输入流
				String result = readData(is, "utf-8");
				//if(!URL.contains("messagebox/getMessagebox.do"))
				{
					System.out.println("--URL--"+URL+"?"+sBuffer+"----responseContent-------"+result);
				}
				if (responseClazz.toString().equals(String.class.toString())) {
					if(callBack != null) {
						callBack.onSuccess(result);
					}
				} else {
					Gson gson = new Gson();
					try {
						Object object = gson.fromJson(result, responseClazz);
						if(callBack != null) {
							callBack.onSuccess(object);
						}
					} catch (Exception e) {
						//e.printStackTrace();
						callBack.onError("请求网络出错:" + e.getMessage());
					}
				}
				conn.disconnect();
			}
		}catch (Exception e) {
			if(e instanceof UnknownHostException)
			{
				callBack.onError("请求网络出错:网络连接没打开");
			}else if(e instanceof SocketTimeoutException){
				callBack.onError("网络连接超时,请稍候再试");
			}else
			{
				callBack.onError("请求网络出错:" + e.getMessage());
			}
			
			e.printStackTrace();
		}







		//  		try {
		//  			HttpClient httpClient = new DefaultHttpClient();	
		//  			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5 * 1000);
		//			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20 * 10000);
		//			HttpRequestBase httpRequestBase = null;
		//          	
		//			Protocal protocal = null;
		//			if(method == HTTP_METHOD.GET) {
		//				protocal   = new GetProtocal();
		//			} else if(method == HTTP_METHOD.POST) {
		//				protocal   = new PostProtocal();
		//			} else if(method == HTTP_METHOD.PUT) {
		//				protocal   = new PutProtocal();
		//			} else if(method == HTTP_METHOD.DELETE) {
		//				protocal   = new DeleteProtocal();
		//			}
		//			httpRequestBase = protocal.buildData(requestBody, format, URL, httpClient);
		//			HttpResponse httpResponse = httpClient.execute(httpRequestBase);
		//			int res = httpResponse.getStatusLine().getStatusCode();
		//			if (res == 200) {
		//				String responseContent =  EntityUtils.toString(httpResponse.getEntity());
		//				if(!URL.contains("messagebox/getMessagebox.do"))
		//	  			{
		//					System.out.println("--URL--"+URL+"?"+((PostProtocal)protocal).getParam()+"----responseContent-------"+responseContent);
		//	  			}
		//				if (responseClazz.toString().equals(String.class.toString())) {
		//					if(callBack != null) {
		//						callBack.onSuccess(responseContent);
		//					}
		//				} else {
		//					Gson gson = new Gson();
		//					try {
		//						Object object = gson.fromJson(responseContent, responseClazz);
		//						if(callBack != null) {
		//							callBack.onSuccess(object);
		//						}
		//					} catch (Exception e) {
		//						e.printStackTrace();
		//						callBack.onError("请求网络出错:" + e.getMessage());
		//					}
		//				}
		//			} else {
		//				callBack.onError("请求网络出错:" + res);
		//			}
		//		} catch (Exception e) {
		//			callBack.onError(e.getMessage());
		//		}
		//      }

	}

	//第一个参数为输入流,第二个参数为字符集编码
	public static String readData(InputStream inSream, String charsetName) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while( (len = inSream.read(buffer)) != -1 ){
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inSream.close();
		return new String(data, charsetName);
	}
	
	public static StringBuffer map2StringBuffer(Map<String, Object> params) {  
		StringBuffer buf = new StringBuffer("");  
		if (params != null && params.size() > 0) {
			Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();  
			while (it.hasNext()) {  
				Map.Entry<String, Object> entry = it.next();  
				buf.append(entry.getKey()).append("=").append(entry.getValue()).toString();  
				buf.append("&");  
			}
		}
		if (buf.length() > 1)  
			buf.deleteCharAt(buf.length() - 1);  
		return buf;  
	} 
}
