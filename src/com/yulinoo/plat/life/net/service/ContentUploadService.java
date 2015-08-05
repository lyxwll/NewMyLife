package com.yulinoo.plat.life.net.service;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.yulinoo.plat.life.net.callback.IContentReportCallback;
import com.yulinoo.plat.life.net.service.CustomMultipartEntity.ProgressListener;

public class ContentUploadService {
	private IContentReportCallback contentReportCallback;
	private long totalSize;

	public ContentUploadService(IContentReportCallback contentReportCallback) {
		this.contentReportCallback = contentReportCallback;
	}

	public void upload(final String filePath, final String url) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpClient httpClient = new DefaultHttpClient();
				try {
					HttpPost httpPost = new HttpPost(url);
					CustomMultipartEntity multipartContent = new CustomMultipartEntity(new ProgressListener() {
						@Override
						public void transferred(long num) {
							int progress = (int) (((float) num / (float) totalSize) * 100);
							if (progress >= 100)
								progress = 99;
							contentReportCallback.onProgress(progress);
						}
					});

					if (filePath != null) {
						multipartContent.addPart("file", new FileBody(new File(filePath)));
						
					}

					totalSize = multipartContent.getContentLength();
				
					httpPost.setEntity(multipartContent);

					HttpResponse httpResponse = httpClient.execute(httpPost);
					int res = httpResponse.getStatusLine().getStatusCode();
					if (res == 200) {
						String responseContent = EntityUtils.toString(httpResponse.getEntity());
						System.out.println(responseContent);
						contentReportCallback.onSuccess(new JSONObject(responseContent).getString("path"));
					} else {
						contentReportCallback.onError("网络出错,错误代码:" + res);
					}
				} catch (Exception e) {
					contentReportCallback.onError("程序异常:" + e.getMessage());
				}finally{
					httpClient.getConnectionManager().shutdown();
				}

			}
		}).start();

	}

}
