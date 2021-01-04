package com.yonglilian.common.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * Java反射工具类
 * @author lwk
 *
 */
public class ReflectionUtil {
	private static final String BLANK = "";
	/**
	 * 对象属性的setter方法前缀prefix
	 */
	private static String set = "set";

	/**
	 * 通过反射技术将Map集合转化为相应的JavaBean对象
	 * @param map
	 * @param clazz
	 * @throws Exception
	 */
	public static Object convertMapToBean(final Map<String, Object> map, Class<?> clazz) {
		Object object = null;
		try {
			if (map != null && map.size() > 0) {
				object = clazz.newInstance();// 实例化对象
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					String propertyName = entry.getKey(); // 属性名
					Object value = entry.getValue();
					String setMethodName = set + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
					Field field = getClassField(clazz, propertyName);
					if (field == null) {
						continue;
					}
					Class<?> fieldTypeClass = field.getType();
					value = convertValType(value, fieldTypeClass);
					try {
						clazz.getMethod(setMethodName, field.getType()).invoke(object, value);// 调用对象的set方法设置属性值
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return object;
	}
 
	/**
	 * 通过反射技术将object对象转化为相应的Map集合-调用
	 * @param object
	 * @return
	 */
	public static Map<String, Object> convertObjectToMap(final Object object) {
		boolean keepNullVal = true;// 是否保留Null值的属性字段
        Map<String, Object> retMap = objectToMap(object, keepNullVal);
        return retMap;
    }

	/**
	 * 根据clazz类的属性处理Map集合
	 * @param paraMap
	 * @param clazz
	 * @throws Exception
	 */
	public static void handleParaMap(Map<String, Object> paraMap, Class<?> clazz) throws Exception {
		if (paraMap == null || paraMap.size() < 1) {
			return;
		}
		List<String> clazzList = new ArrayList<String>();// 类所有属性
		final Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            clazzList.add(field.getName());
        }
        List<String> mapList = new ArrayList<String>();// Map所有属性
		Set<String> keySet = paraMap.keySet();
		for (String keyName : keySet) {
			mapList.add(keyName);
		}
		mapList.add("serialVersionUID");// 类序列号属性
		//求出fieldList和mapList的差集
		clazzList.removeAll(mapList);
		for (String fieldName : clazzList) {
			paraMap.put(fieldName, null);
		}
	}

	/**
	 * 通过反射技术将object对象转化为相应的Map集合-实现
	 * @param object
	 * @param keepNullVal
	 * @return
	 */
	private static Map<String, Object> objectToMap(final Object object, boolean keepNullVal) {
        if (object == null) {
            return null;
        }
        final Map<String, Object> retMap = new HashMap<String, Object>();
        try {
        	final Class<?> clazz = object.getClass();
        	final Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if (keepNullVal) {// keepNullVal == true
                	retMap.put(field.getName(), field.get(object));
                } else {// keepNullVal == false
                	Object fieldVal = field.get(object);// 对象属性的值
                    if (fieldVal != null && !BLANK.equals(fieldVal.toString())) {
                    	retMap.put(field.getName(), field.get(object));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retMap;
    }

	/**
	 * 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)
	 * @param clazz 指定的class
	 * @param fieldName 字段名称
	 * @return Field对象
	 */
	private static Field getClassField(Class<?> clazz, String fieldName) {
		if (Object.class.getName().equals(clazz.getName())) {
			return null;
		}
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {// 递归查找在class中的对应的Field对象(包括查找父类)
			return getClassField(superClass, fieldName);
		}
		return null;
	}

	/**
	 * 将Object类型的值，转换成bean对象属性里对应的类型值
	 * @param value Object对象值
	 * @param fieldTypeClass 属性的类型
	 * @return 转换后的值
	 */
	private static Object convertValType(Object value, Class<?> fieldTypeClass) {
		Object retVal = null;
		if (value == null) {
			return retVal;
		}
		// BigDecimal类型
		if (BigDecimal.class.getName().equals(fieldTypeClass.getName())) {
			retVal = new BigDecimal(value.toString());
		}
		// String类型
		else if (String.class.getName().equals(fieldTypeClass.getName())) {
			retVal = value.toString();
		}
		// Long类型long
		else if (Long.class.getName().equals(fieldTypeClass.getName()) 
				|| long.class.getName().equals(fieldTypeClass.getName())) {
			
			retVal = Long.parseLong(value.toString());
		}
		// Integer类型int
		else if (Integer.class.getName().equals(fieldTypeClass.getName()) 
				|| int.class.getName().equals(fieldTypeClass.getName())) {
			
			retVal = (int) Float.parseFloat(value.toString());
		}
		// Float类型float
		else if (Float.class.getName().equals(fieldTypeClass.getName()) 
				|| float.class.getName().equals(fieldTypeClass.getName())) {
			
			retVal = Float.parseFloat(value.toString());
		}
		// Double类型double
		else if (Double.class.getName().equals(fieldTypeClass.getName()) 
				|| double.class.getName().equals(fieldTypeClass.getName())) {
			
			retVal = Double.parseDouble(value.toString());
		}
		// Byte类型byte
		else if (Byte.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Byte.parseByte(value.toString());
		}
		// 其他类型
		else {
			retVal = value;// Object类型
		}
		return retVal;
	}
}