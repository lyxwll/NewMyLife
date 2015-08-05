package com.yulinoo.plat.life.net.callback;

/**
 * Form表单上传callback
 * @author jefry
 *
 */
public interface UIUploadCallback  {
	
	public void onError(String message);

	public void onSuccess(String urlPath);

	public void onProgress(String progress);
	
	public void onOffLine();
}
