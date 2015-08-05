package com.yulinoo.plat.life.net.callback;


/**
 * 
 * @author jefry
 *
 */
public interface UICallback<T> {
	
	public void onSuccess(T respose);
   
    public void onError(String message);
    
    public void onOffline(String message);
}
