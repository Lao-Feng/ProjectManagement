package com.yonglilian.common.util;



import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class Log implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Log() {
		bIsDebug = true;
		SaveToLogFile = false;
		logClass = "";
		LogFilePath = "";
		ReadProperty();
	}

	public Log(String ClassName) {
		bIsDebug = true;
		SaveToLogFile = false;
		logClass = "";
		LogFilePath = "";
		logClass = ClassName;
		ReadProperty();
	}

	public Log(String ClassName, String LogFileName) {
		bIsDebug = true;
		SaveToLogFile = false;
		logClass = "";
		LogFilePath = "";
		SetLogFile(LogFileName);
		logClass = ClassName;
		ReadProperty();
	}

	private void ReadProperty() {
		bIsDebug = SysPreperty.getProperty().IsDebug;
		LogFilePath = SysPreperty.getProperty().LogFilePath;
		File file = new File(LogFilePath);
		// 判断文件夹是否存在,如果不存在则指定文件夹
		if (!file.exists()) {
			LogFilePath = "C:/";
		}
	}

	public void SetLogFile(String LogFileName) {
		if (LogFileName != null && !LogFileName.equals("")) {
			LogFileName = LogFilePath + '/' + LogFileName;
			try {
				logFile = new PrintWriter(new FileWriter(LogFileName, true), true);
			} catch (IOException ex) {
			}
			SaveToLogFile = true;
		}
	}

	public void SetLogForClass(String ClassName) {
		logClass = ClassName;
	}

	public void WriteLog(String LogMsg) {
		String strMsg = "";
		if (bIsDebug) {
			strMsg = "【" + logClass + "】Class Info：" + LogMsg;
			Write(strMsg, false);
		}
	}

	public void WriteLog(String FunctionName, String LogMsg) {
		String strMsg = "";
		if (bIsDebug) {
			strMsg = "【" + logClass + "】Class---" + FunctionName + " Method：" + LogMsg;
			Write(strMsg, false);
		}
	}

	public void WriteErrorLog(String LogMsg) {
		Write("【" + logClass + "】Class Exception", true);
		Write("Exception Info：" + LogMsg, true);
	}

	public void WriteErrorLog(String FunctionName, String LogMsg) {
		Write("【" + logClass + "】Class Has Exception", true);
		Write("Exception At Method：" + FunctionName, true);
		Write("Exception Info：" + LogMsg, true);
	}

	private void Write(String strMsg, boolean IsError) {
		if (SaveToLogFile)
			SaveToFile(strMsg);
		else if (IsError)
			System.err.println(strMsg);
		else
			System.out.println(strMsg);
	}

	private void SaveToFile(String LogMsg) {
		Date date = new Date();
		String strDate = "";
		if (date != null) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			strDate = df.format(date);
		}
		LogMsg = strDate + " " + LogMsg;
		log.info(LogMsg);
		// logFile.println(LogMsg);
	}

	private boolean bIsDebug;
	private boolean SaveToLogFile;
	private PrintWriter logFile;
	private String logClass;
	public String LogFilePath;
}