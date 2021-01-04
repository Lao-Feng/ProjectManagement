package com.yonglilian.common.task;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Task implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mName;
	private Map<String, Object> params;

	public Task(String name) {
		mName = null;
		mName = name;
		params = new HashMap<String, Object>();
	}

	public String getName() {
		return mName;
	}

	public void addParameter(String name, Object parm) {
		params.put(name.toUpperCase(), new TaskParameter(name, parm));
	}

	public TaskParameter getParameter(String parmname) {
		if (params.get(parmname.toUpperCase()) != null) {
			return (TaskParameter) params.get(parmname.toUpperCase());
		} else {
			return null;
		}
	}

	public int getParameterCount() {
		return params.size();
	}

	public String[] getParameterNameList() {
		int i = params.size();
		String[] result = new String[i];
		Iterator<Entry<String, Object>> names = params.entrySet().iterator();
		for (int j = 0; names.hasNext(); j++) {
			Entry<String, Object> entry = names.next();
			result[j] = entry.getKey();
		}
		return result;
	}

}
