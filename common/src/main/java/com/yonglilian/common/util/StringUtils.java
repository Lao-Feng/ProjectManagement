package com.yonglilian.common.util;

/**
 * 字符串工具类
 * @author lwk
 *
 */
public class StringUtils {
	public static final String _BLANK = "";

	/**
	 * 判断字符串str是否为空
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (str == null || _BLANK.equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串str是否不为空
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		if (str != null && str.length() > 0) {
			return true;
		}
		return false;
	}

}