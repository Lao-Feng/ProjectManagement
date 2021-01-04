package com.yonglilian.timertask;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 平台基础数据恢复定时任务
 */
@Component
public class AutoInitialDataTask {
	/**
	 * 系统自动执行服务
	 */
	private static final UpdateZrPowerData updateZrPowerData = new UpdateZrPowerData();

    @Scheduled(cron = "* * 0/1 * * ?")  //每隔30秒执行一次定时任务
    public void autoInitialData() {
    	System.out.println("开始恢复ZRPower系统基础数据...");
    	try {
    		updateZrPowerData.updateSystemData();
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
}