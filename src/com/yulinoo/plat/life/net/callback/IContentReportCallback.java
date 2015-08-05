package com.yulinoo.plat.life.net.callback;


public interface IContentReportCallback {
   public void onError(String message);
   public void onSuccess(String urlPath);
   public void onProgress(int progress);
}
