package zr.zrpower.common.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//dbcp数据源时引入
//druid数据源时引入
//c3p0数据源时引入

/**
 * 系统配置文件管理类
 * @author lwk
 *
 */
public class SysPreperty {
	/** The SysPreperty Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(SysPreperty.class);
	private static SysConfig sysConfig = null;

	public SysPreperty() {
	}

	public static SysConfig getProperty() {
		return sysConfig;
	}

	private static String doFixString(String str) {
		try {
			String convert = new String(str.getBytes("ISO8859-1"), "utf-8");
			return convert;
		} catch (Exception ex) {
			System.err.println("SysPerperty->Exception Information：" + ex.toString());
			return str;
		}
	}

	static {// 静态代码块，先于构造方法执行
		InputStream inputStream = null;
		String tmpDataBaseType = "";
		sysConfig = new SysConfig();
		try {
			// 加载项目配置文件zrpower.properties
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("zrpower.properties");
			
			Properties prop = new Properties();
			prop.load(inputStream);
			sysConfig.LogFilePath = prop.getProperty("LogFilePath");
			sysConfig.UploadFilePath = prop.getProperty("UploadFilePath");
			sysConfig.UploadImagePath = prop.getProperty("UploadImagePath");
			sysConfig.IcoPath = prop.getProperty("IcoPath");
			sysConfig.MainDataSource = prop.getProperty("MainDataSource");
			sysConfig.ShowTempletPath = prop.getProperty("ShowTempletPath");
			sysConfig.PrintTempletPath = prop.getProperty("PrintTempletPath");
			sysConfig.AppUrl = prop.getProperty("AppUrl");
			String sIsConvert = prop.getProperty("IsConvert");
			String sIsDebug = prop.getProperty("IsDebug");
			sysConfig.SystemName = prop.getProperty("SystemName");
			sysConfig.EnglishName = prop.getProperty("EnglishName");
			sysConfig.ShortName = prop.getProperty("ShortName");
			sysConfig.UserUnit = prop.getProperty("UserUnit");
			sysConfig.Versoin = prop.getProperty("Versoin");

			sysConfig.ServerName = prop.getProperty("ServerName");

			sysConfig.FAQUnit = prop.getProperty("FAQUnit");
			sysConfig.FAQUnitLink = prop.getProperty("FAQUnitLink");
			sysConfig.WebType = prop.getProperty("WebType");
			sysConfig.DataBaseType = prop.getProperty("DataBaseType");

			tmpDataBaseType = sysConfig.DataBaseType;
			// 有设置指定最大连接数的情况(格式:1,50；表示第一种数据库连接方式，最大连接数设置成50)
			if (tmpDataBaseType.indexOf(",") > -1) {
				sysConfig.datalinknum = tmpDataBaseType.split(",")[1];
				sysConfig.DataBaseType = tmpDataBaseType.split(",")[0];
			}
			sysConfig.WebPath = prop.getProperty("WebPath");
			sysConfig.Custom1 = prop.getProperty("Custom1");
			sysConfig.Custom2 = prop.getProperty("Custom2");
			sysConfig.Custom3 = prop.getProperty("Custom3");
			sysConfig.Custom4 = prop.getProperty("Custom4");
			sysConfig.Custom5 = prop.getProperty("Custom5");

			if (sIsDebug == null || sIsDebug.trim() == "" || sIsDebug.toLowerCase().equals("false")) {
				sysConfig.IsDebug = false;
			} else {
				sysConfig.IsDebug = true;
			}
			if (sIsConvert == null || sIsConvert.trim() == "" || sIsConvert.toLowerCase().equals("false")) {
				sysConfig.IsConvert = false;
			} else {
				sysConfig.IsConvert = true;
			}
			prop = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		boolean isFix = true;
		if (isFix) {
			sysConfig.LogFilePath = doFixString(sysConfig.LogFilePath);
			sysConfig.UploadFilePath = doFixString(sysConfig.UploadFilePath);
			sysConfig.UploadImagePath = doFixString(sysConfig.UploadImagePath);
			sysConfig.IcoPath = doFixString(sysConfig.IcoPath);
			sysConfig.MainDataSource = doFixString(sysConfig.MainDataSource);
			sysConfig.ShowTempletPath = doFixString(sysConfig.ShowTempletPath);
			sysConfig.PrintTempletPath = doFixString(sysConfig.PrintTempletPath);
			sysConfig.AppUrl = doFixString(sysConfig.AppUrl);
			sysConfig.SystemName = doFixString(sysConfig.SystemName);
			sysConfig.EnglishName = doFixString(sysConfig.EnglishName);
			sysConfig.ShortName = doFixString(sysConfig.ShortName);
			sysConfig.UserUnit = doFixString(sysConfig.UserUnit);
			sysConfig.Versoin = doFixString(sysConfig.Versoin);

			sysConfig.ServerName = doFixString(sysConfig.ServerName);
			sysConfig.FAQUnit = doFixString(sysConfig.FAQUnit);
			sysConfig.FAQUnitLink = doFixString(sysConfig.FAQUnitLink);
			sysConfig.WebType = doFixString(sysConfig.WebType);
			sysConfig.DataBaseType = doFixString(tmpDataBaseType);

			String tmptype = sysConfig.DataBaseType;
			// 有设置指定最大连接数的情况(格式:1,50；表示第一种数据库连接方式，最大连接数设置成50)
			if (tmptype.indexOf(",") > -1) {
				sysConfig.datalinknum = tmptype.split(",")[1];
				sysConfig.DataBaseType = tmptype.split(",")[0];
			}
			sysConfig.WebPath = doFixString(sysConfig.WebPath);
			sysConfig.Custom1 = doFixString(sysConfig.Custom1);
			sysConfig.Custom2 = doFixString(sysConfig.Custom2);
			sysConfig.Custom3 = doFixString(sysConfig.Custom3);
			sysConfig.Custom4 = doFixString(sysConfig.Custom4);
			sysConfig.Custom5 = doFixString(sysConfig.Custom5);
		}
	}

	public static DataSource getDataSource(String dsName) {
		DataSource dataSource = null;
		System.out.println("数据库连接池名称：" + dsName);
		if (dsName.equals("dbcp")) {// dbcp数据库连接池
			InputStream inputStream = null;
			try {
				// 加载数据库配置文件dbcp.properties
				inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("dbcp.properties");
				
				Properties prop = new Properties();
				prop.load(inputStream);
				
				dataSource = BasicDataSourceFactory.createDataSource(prop);
			} catch (Exception e) {
				LOGGER.error("初始实例化dbcp数据源出现错误:\\n:\n", e);
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return dataSource;
		} else if (dsName.indexOf("c3p0") != -1) {// c3p0数据库连接池
			String[] datalist = dsName.split(",");
			try {
				dataSource = new ComboPooledDataSource(datalist[1]);
			} catch (Exception e) {
				LOGGER.error("初始实例化c3p0数据源出现错误:\n", e);
			}
			return dataSource;
		} else if (dsName.equals("druid")) {// druid数据库连接池
			InputStream inputStream = null;
			try{
				// 加载数据库配置文件dbcp.properties
				inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("dbcp.properties");
				
				Properties prop = new Properties();
				prop.load(inputStream);
				
				dataSource = DruidDataSourceFactory.createDataSource(prop);
			} catch (Exception e) {
				LOGGER.error("初始实例化druid数据源出现错误:\n", e);
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return dataSource;
		} else {// 其它数据源数据库连接池
			// 服务器中的主数据源JNDI-------------
			Context context = null;
			try {
				context = new InitialContext();
				dataSource = (DataSource) context.lookup(dsName);
			} catch (NamingException ex) {
				ex.printStackTrace();
				return null;
			}
			return dataSource;
		}
	}
}