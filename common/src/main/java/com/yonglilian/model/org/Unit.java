package com.yonglilian.model.org;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Unit implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String Code; // 单位代码
	public String Name; // 单位名称
	public List<Object> Child;  // 直属下级部门

	public Unit() {
		Child = new ArrayList<Object>();
	}
}
