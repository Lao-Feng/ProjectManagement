package com.yonglilian.model;

import java.io.Serializable;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据实体包
 * </p>
 * <p>
 * Copyright: Copyright (c)
 * </p>
 * <p>
 * Company:
 * </p>
 */
public class BPIP_OPERATE_TABLE implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String TableName; // 表名
	public String FieldName; // 字段名
	public String OldFieldName;
	public String FieldType; // 字段类型
	public int Length; // 长度
	public int Precision; // 精度
	public boolean IsNull; // 可否为空
	public String DefaultValue; // 默认值
	public boolean IsPrimaryKey; // 是否为主键
	public String TableSpace;// 表分类

	public BPIP_OPERATE_TABLE() {
		FieldType = "VARCHAR2";
		Length = 10;
		Precision = 0;
		IsNull = true;
		DefaultValue = "";
		IsPrimaryKey = false;
		TableSpace = "";
	}
}
