package com.yonglilian.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataTool {
	private static final ObjectMapper ObjMapper = new ObjectMapper();
	
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}

	public static String constructResponse(int code, String msg, Object data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("msg", msg);
		map.put("data", data);
		
		String mapJakcson = "{\"code\":\"-101\",\"msg\":\"\":\"data\":null}";
		try {
			mapJakcson = ObjMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return mapJakcson;
	}

	public static String trimStr(String str) {
		if (str == null) {
			return StringUtils._BLANK;
		}
		return str.trim();
	}

}
