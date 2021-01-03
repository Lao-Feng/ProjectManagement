package zr.zrpower.common.task;

import java.io.Serializable;
import java.util.Date;

public class TaskParameter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mName;
	private Object mValue;

	public TaskParameter(String name, Object value) {
		mName = name;
		mValue = value;
	}

	public String getName() {
		return mName;
	}

	public String getString() {
		if (mValue == null) {
			return null;
		}
		return (String) mValue;
	}

	public int getInteger() {
		if (mValue == null) {
			return 0;
		}
		return Integer.parseInt(mValue.toString());
	}

	public float getFloat() {
		if (mValue == null) {
			return 0.0F;
		}
		return Float.parseFloat(mValue.toString());
	}

	public Date getDate() {
		if (mValue == null) {
			return null;
		}
		return (Date) mValue;
	}

	public Object getValue() {
		return mValue;
	}
}
