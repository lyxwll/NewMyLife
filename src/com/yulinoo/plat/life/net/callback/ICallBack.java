package com.yulinoo.plat.life.net.callback;



/**
 * @author jefry
 *
 */
public interface ICallBack {
  
	/**
	 * 
	 * @param jsonBody
	 */
	public void onSuccess(Object respose);
   
    public void onError(String message);
    
    
    


}
