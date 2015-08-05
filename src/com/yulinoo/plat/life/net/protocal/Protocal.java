package com.yulinoo.plat.life.net.protocal;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;

public interface Protocal {
    public HttpRequestBase buildData(Object reqObject,HTTP_DATA_FORMAT format,String URL,HttpClient httpClient);
}
