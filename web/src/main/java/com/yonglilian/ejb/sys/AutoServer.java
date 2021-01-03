package com.yonglilian.ejb.sys;

import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.Log;
import zr.zrpower.common.util.SysPreperty;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>
 * Title:系统自动执行服务
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c)
 * </p>
 * <p>
 * Company:
 * </p>
 */
public class AutoServer {
	static private Log log; // 日志
	DBEngine dbengine; // 数据库引擎
	static private int clients = 0;

	Timer SmsListener; // 队列侦听器
	Timer SmsListener1; // 队列侦听器
	boolean isRun = false; // 侦听是否运行
	int startNum = 0;

	public AutoServer() {
		dbengine = new DBEngine(
				SysPreperty.getProperty().MainDataSource,
				SysPreperty.getProperty().IsConvert
		);
		dbengine.initialize();

		if (clients < 1) {
			log = new Log();
			log.SetLogForClass("AutoServerBean");
			log.SetLogFile("AutoServerBean.log");
		}
		clients++;
	}

	public boolean StartScan() {
		if (!isRun) {
			// 间隔扫描的秒数
			int intnum = 3600;// 1个小时扫描一次

			SmsListener = new Timer();
			SmsListener.schedule(new ScanTask(), 0, intnum * 1000);

			isRun = true;
			log.WriteLog("AutoServer is Start Runing ....");

		}
		return isRun;
	}

	// 扫描任务
	class ScanTask extends TimerTask {
		public void run() {
			log.WriteLog("扫描任务开始！");

			// 更新业务系统的特殊情况
			updateSystemData();
		}
	}

	// 更新业务系统的一些特殊情况
	private void updateSystemData() {
		
	}
}