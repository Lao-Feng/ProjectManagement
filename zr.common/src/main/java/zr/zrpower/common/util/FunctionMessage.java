package zr.zrpower.common.util;

import java.io.Serializable;

public class FunctionMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public FunctionMessage() {
		mResult = false;
		mMessage = "";
		mVec = null;
	}

	public FunctionMessage(boolean result, String message) {
		mResult = false;
		mMessage = "";
		mVec = null;
		mResult = result;
		mMessage = message;
	}

	public FunctionMessage(int iDefaultMessage) {
		mResult = false;
		mMessage = "";
		mVec = null;
		switch (iDefaultMessage) {
		case 1: // '\001'
			mMessage = MSG_ERROR_APPSERVER;
			break;

		case 2: // '\002'
			mMessage = MSG_ERROR_DATASERVER;
			break;

		case 3: // '\003'
			mMessage = MSG_NOT_SAVE_DATA;
			break;

		case 4: // '\004'
			mMessage = MSG_SUCCEED_SAVE;
			mResult = true;
			break;
		}
	}

	public void setResult(boolean result) {
		mResult = result;
	}

	public void setMessage(String message) {
		mMessage = message;
	}

	public boolean getResult() {
		return mResult;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setValue(Object mValue) {
		mVec = mValue;
	}

	public Object getValue() {
		return mVec;
	}

	private boolean mResult;
	private String mMessage;
	private Object mVec;
	public static String MSG_ERROR_DATASERVER = "数据库发生异常，请稍后再试！\n如果问题依然存在请与系统管理员联系。";
	public static String MSG_ERROR_APPSERVER = "中间件服务器发生异常，请稍后再试！\n如果问题依然存在请与系统管理员联系。";
	public static String MSG_NOT_SAVE_DATA = "没有要保存的数据。";
	public static String MSG_SUCCEED_SAVE = "保存成功！";

}
