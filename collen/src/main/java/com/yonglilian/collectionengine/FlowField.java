package com.yonglilian.collectionengine;

/**
 * <p>
 * Title:自定义表单引擎－－流程可操作字段类
 * </p>
 * <p>
 * Description: 与流程引擎协同工作时，流程动作中可操作的字段属性类
 * </p>
 * <p>
 * Copyright: Copyright
 * </p>
 * 
 * @author NFZR
 */
public class FlowField {
	private String mName;
	private boolean mIsShow = true;
	private boolean mIsWrite = true;
	private boolean mIsNull = true;
	private boolean mIsForce = false;
	private String mDefaultValue;

	public FlowField() {
	}

	public FlowField(String name) {
		this.mName = name;
	}

	public void setName(String name) {
		if (name.indexOf(".") > -1) {
			name = name.substring(name.indexOf(".") + 1);
		}
		this.mName = name;
	}

	public String getName() {
		return this.mName;
	}

	public void setIsShow(String isshow) {
		if (isshow != null && isshow.equals("0")) {
			this.mIsShow = false;
		}
	}

	public boolean isShow() {
		return this.mIsShow;
	}

	public void setIsWrite(String iswrite) {
		if (iswrite != null && iswrite.equals("0")) {
			this.mIsWrite = false;
		}
	}

	public boolean isWrite() {
		return this.mIsWrite;
	}

	public void setIsNull(String isnull) {
		if (isnull != null && isnull.equals("1")) {
			this.mIsNull = false;
		}
	}

	public boolean isNull() {
		return this.mIsNull;
	}

	public void setIsForce(String isforce) {
		if (isforce != null && isforce.equals("1")) {
			this.mIsForce = true;
		}
	}

	public boolean isForce() {
		return this.mIsForce;
	}

	public void setDefault(String defaultvalue) {
		this.mDefaultValue = defaultvalue;
	}

	public String getDefault() {
		return this.mDefaultValue;
	}
}
