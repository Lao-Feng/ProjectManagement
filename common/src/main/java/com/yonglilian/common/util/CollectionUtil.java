package com.yonglilian.common.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 集合工具处理类
 * @author lwk
 *
 */
public class CollectionUtil {
	private static final String BLANK = "";

	/**
	 * 遍历Map集合，并处理Map集合中元素的null值
	 * @param paraMap
	 */
	public static void convertMapNullToStr(Map<String, Object> paraMap) {
		// 遍历Map集合中的元素
		Iterator<Entry<String, Object>> iter = paraMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value == null) {
				paraMap.put(key, BLANK);
			}
		}
	}
}