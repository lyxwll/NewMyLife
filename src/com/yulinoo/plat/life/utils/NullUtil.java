package com.yulinoo.plat.life.utils;

import java.util.Collection;

/**
 * 对象为空的判断
 * 
 * @author wenping.rong
 * 
 */
public class NullUtil {

	/**
	 * 判断是否为NULL
	 * 
	 * @param obj
	 * @return
	 */
	public static final boolean isNull(Object obj) {
		if (null == obj) {
			return true;
		} else {
			return false;
		}
	}
	public static final boolean isStrNull(String obj) {
		if (null == obj) {
			return true;
		} else {
			if(obj.length()==0)
			{
				return true;
			}
			return false;
		}
	}
	public static final boolean isStrNotNull(String obj) {
		if(obj!=null&&obj.trim().length()>0)
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为Not NULL
	 * 
	 * @param obj
	 * @return
	 */
	public static final boolean isNotNull(Object obj) {
		if (null != obj) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean isNotEmpty(String str) {
		return isNotNull(str) && str.trim().length() > 0;
	}

	/**
	 * 判断数组是否为空
	 * 
	 * @param array
	 * @return
	 */
	public static final boolean isNotEmpty(Object[] array) {
		return isNotNull(array) && array.length > 0;
	}

	/**
	 * 判断集合是否为空
	 * 
	 * @param collection
	 * @return
	 */
	public static final boolean isNotEmpty(Collection<?> collection) {
		return isNotNull(collection) && collection.size() > 0;
	}

	public static final boolean isEmpty(String str) {
		return !isNotEmpty(str);
	}

	public static final boolean isEmpty(Object[] array) {
		return !isNotEmpty(array);
	}

	public static final boolean isEmpty(Collection<?> collection) {
		return !isNotEmpty(collection);
	}
}
