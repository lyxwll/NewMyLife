package com.yulinoo.plat.life.net.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.yulinoo.plat.life.net.callback.IPictureDownloadCallback;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MD5Util;

import config.ConfigCache;

public class DownloadPictureService {

	private IPictureDownloadCallback pictureDownloadCallback;
	

	public DownloadPictureService(
			IPictureDownloadCallback pictureDownloadCallback) {
		this.pictureDownloadCallback = pictureDownloadCallback;
	}

	public void dowload(final String fileUrl) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				downloadPic(fileUrl,0);
			}
		}).start();
	}
	
	/**
	 * 
	 * @param fileUrl
	 * @param id 可以不填
	 */
	public void dowload(final String fileUrl,final Integer id) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				downloadPic(fileUrl,id);
			}
		}).start();
	}

	private void downloadPic(String picUrl,Integer id) {
		File tmpFile = null;
		try {
			String urlStr = Constant.URL.ADMIN_SERVER_DOMAIN + picUrl;
			String fileName = MD5Util.getMD5Str(urlStr);
			File file = new File(ConfigCache.getCachePath());
			if (!file.exists()) {
				file.mkdirs();
			}
			String filePath = ConfigCache.getCachePath() + fileName;
			tmpFile = new File(filePath);
			if (tmpFile.exists() && tmpFile.length() > 5) {
				pictureDownloadCallback.onSuccess(filePath,id);
				return;
			}
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();

			FileOutputStream fos = new FileOutputStream(filePath);
			byte buf[] = new byte[1024];
			do {
				int numread = is.read(buf);
				if (numread < 0) {
					break;
				}
				fos.write(buf, 0, numread);
			} while (true);// 点击取消就停止下载.

			fos.close();
			is.close();
			pictureDownloadCallback.onSuccess(filePath,id);
		} catch (Exception e) {
			try {
				tmpFile.delete();
			} catch (Exception e1) {
			}
			pictureDownloadCallback.onError(id);
			//pictureDownloadCallback.onSuccess(null);
		}

	}
	
	public void downloadPicByDetailPage(final String picUrl) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				File tmpFile = null;
				try {
					String urlStr =Constant.URL.ADMIN_SERVER_DOMAIN + picUrl;
					String fileName = MD5Util.getMD5Str(urlStr);
					File file = new File(ConfigCache.getCachePath());
					if (!file.exists()) {
						file.mkdirs();
					}
					String filePath = ConfigCache.getCachePath() + fileName;
					tmpFile = new File(filePath);
					if (tmpFile.exists() && tmpFile.length() > 0) {
						pictureDownloadCallback.onSuccess(filePath,0);
						return;
					}
					URL url = new URL(urlStr);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					InputStream is = conn.getInputStream();

					FileOutputStream fos = new FileOutputStream(filePath);
					byte buf[] = new byte[1024];
					do {
						int numread = is.read(buf);
						if (numread <= 0) {
							break;
						}
						fos.write(buf, 0, numread);
					} while (true);

					fos.close();
					is.close();
					pictureDownloadCallback.onSuccess(filePath,0);
				} catch (Exception e) {
					try {
						e.printStackTrace();
						tmpFile.delete();
					} catch (Exception e1) {
					}
					pictureDownloadCallback.onError(0);
					//pictureDownloadCallback.onSuccess(null);
				}
				
			}
		}).start();
		

	}


}
