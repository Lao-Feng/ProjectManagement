package zr.zrpower.common.util;

public class SysConfig {
	/**
	 * SysConfig构造方法
	 */
	public SysConfig() {
		this.IsConvert = false;
		this.IsDebug = false;
		this.LogFilePath = "/usr/ZRpower/log";
		this.UploadFilePath = "/usr/ZRpower/UploadFile";
		this.UploadImagePath = "/usr/ZRpower/UploadImage";
		this.IcoPath = "/usr/ZRpower/UploadIco";

		this.MainDataSource = "dbcp";//"jdbc/bpip";数据源类型
		this.ShowTempletPath = "/usr/ZRpower/templet/show/";
		this.PrintTempletPath = "/usr/ZRpower/templet/print/";
		this.AppUrl = "/static/";

		this.SystemName = "";
		this.EnglishName = "";
		this.ShortName = "";
		this.UserUnit = "";
		this.ServerName = "";
		this.Versoin = "";

		this.Custom1 = "";
		this.Custom2 = "";
		this.Custom3 = "";
		this.Custom4 = "";
		this.Custom5 = "";
		this.datalinknum = "0";
	}

	/**
	 * SysConfig构造方法
	 * @param DataBaseType
	 */
	public SysConfig(String DataBaseType) {
		this.DataBaseType = DataBaseType;
	}

	public boolean IsConvert;
	public boolean IsDebug;
	public String LogFilePath;
	public String UploadFilePath;
	public String UploadImagePath;
	public String IcoPath;

	public String MainDataSource;
	public String ShowTempletPath;
	public String PrintTempletPath;

	public String AppUrl;
	public String SystemName;
	public String EnglishName;
	public String ShortName;
	public String UserUnit;
	public String Versoin;
	public String ServerName;
	public String FAQUnit;
	public String FAQUnitLink;
	public String WebType;

	/** 数据库类型，1：Oracle，2：MSSQL，3：MySQL. */
	public String DataBaseType;
	public String WebPath;
	public String Custom1;
	public String Custom2;
	public String Custom3;
	public String Custom4;
	public String Custom5;
	public String datalinknum;

}