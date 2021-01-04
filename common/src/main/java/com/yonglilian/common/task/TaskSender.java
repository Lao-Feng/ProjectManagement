package com.yonglilian.common.task;

import com.yonglilian.common.util.FunctionMessage;

public class TaskSender {
	protected String cfJNDI;
	protected String qJNDI;

	public TaskSender() {
	}

	public void setConnFactory(String JNDI) {
		this.cfJNDI = JNDI;
	}

	public void setQueue(String JNDI) {
		this.qJNDI = JNDI;
	}

	public FunctionMessage Send(Task task) {
		FunctionMessage fm = new FunctionMessage(false, "未知名异常");
		try {
			fm.setResult(true);
			fm.setMessage("任务发送成功！");
		} catch (Exception ex) {
			fm.setMessage("详细错误信息：\n    " + ex.toString());
		}
		return fm;
	}
}