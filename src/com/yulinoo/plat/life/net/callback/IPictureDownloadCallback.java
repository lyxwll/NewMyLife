package com.yulinoo.plat.life.net.callback;

public interface IPictureDownloadCallback {
  public void onSuccess(String fileName,final Integer id);
  public void onError(Integer id);
}
