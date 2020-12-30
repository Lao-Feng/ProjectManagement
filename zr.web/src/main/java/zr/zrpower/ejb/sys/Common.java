package zr.zrpower.ejb.sys;

import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBSet;
import zr.zrpower.common.util.SysPreperty;

public class Common {
	public Common() {
	}

	/**
	 * 功能或作用：取出最大的记录流水号
	 * 
	 * @param TableName
	 *            数据库表名
	 * @param FieldName
	 *            数据库字段名称
	 * @param FieldLen
	 *            数据库字段长度
	 * @Return MaxNo 执行后返回一个MaxNo字符串
	 */
	public static String getMaxFieldNo(String TableName, String FieldName, int FieldLen) {
		String MaxNo = "";
		int LenMaxNo = 0;
		DBEngine dbengine;
		dbengine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbengine.initialize();
		String strSQL = "SELECT MAX(" + FieldName + ") AS MaxNo FROM " + TableName;
		try {
			DBSet dbset = dbengine.QuerySQL(strSQL.toString());
			if (dbset != null) {
				if (dbset.RowCount() > 0) {
					MaxNo = dbset.Row(0).Column("MaxNo").getString();
					if (MaxNo.length() > 0) {
						MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
						LenMaxNo = MaxNo.length();
						MaxNo = "0000000000000000000000000" + MaxNo;
					} else {
						MaxNo = "00000000000000000000000001";
						LenMaxNo = 1;
					}
				}
				dbset = null;
			}
			MaxNo = MaxNo.substring(25 - FieldLen + LenMaxNo);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		return MaxNo;
	}
}