package com.yonglilian.service.impl;

import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.UserMapper;
import com.yonglilian.model.BPIP_OPERATE_TABLE;
import com.yonglilian.service.OperateTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 表结构维护管理服务层
 * @author lwk
 *
 */
@Service
public class OperateTableServiceImpl implements OperateTableService {
	/** The OperateTableServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(OperateTableServiceImpl.class);
	/**
	 * 数据库类型，1：Oracle，2：MSsql，3：MySQL
	 */
	static String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;

	@Override
	public boolean addField(BPIP_OPERATE_TABLE field) throws Exception {
	    boolean result = false;
	    String endStr = "";
	    String strBuf = "";
	    if (DataBaseType.equals("2")) {//MSSQL数据库
	       //处理MSSQL字段的类型，Oracle的VARCHAR2变成VARCHAR
	       if (field.FieldType.equals("VARCHAR2")) {
	          field.FieldType = "VARCHAR";
	       }
	       if (field.FieldType.equals("NUMBER")) {
	          field.FieldType = "NUMERIC";
	       }
	       if (field.FieldType.equals("BLOB")) {
	          field.FieldType = "IMAGE";
	       }
	       if (field.FieldType.equals("CLOB")) {
	          field.FieldType = "TEXT";
	       }
	       if (field.FieldType.equals("DATE")) {
	          field.FieldType = "DATETIME";
	       }
	    }
	    else if (DataBaseType.equals("3")) {//MySQL数据库
	       //处理MSSQL字段的类型，oracle的VARCHAR2变成VARCHAR
	       if (field.FieldType.equals("VARCHAR2")) {
	          field.FieldType = "VARCHAR";
	       }
	       if (field.FieldType.equals("NUMBER")) {
	          field.FieldType = "NUMERIC";
	       }
	       if (field.FieldType.equals("BLOB")) {
	          field.FieldType = "LONGBLOB";
	       }
	       if (field.FieldType.equals("CLOB")) {
	          field.FieldType = "LONGTEXT";
	       }
	       if (field.FieldType.equals("DATE")) {
	          field.FieldType = "DATETIME";
	       }
	    }
	    if (IsExistTable(field.TableName)) {
	        strBuf = "ALTER TABLE "+field.TableName+" ADD "+ field.FieldName;
	    }
	    else { //不存在表
	        strBuf ="CREATE TABLE "+field.TableName+" ( "+ field.FieldName;
	        endStr =" ) ";
	    }
	    strBuf = strBuf + "  " + field.FieldType + "  ";

	    if (field.Length > 0 && field.Precision > 0) {
	      strBuf = strBuf + "("+String.valueOf(field.Length)+","+String.valueOf(field.Precision)+")";
	    }
	    else
	    if (field.Length > 0) {
	      strBuf = strBuf + "("+String.valueOf(field.Length)+")";
	    }

	    if (field.IsNull) {
	      strBuf = strBuf + " NULL";
	    } else {
	      strBuf = strBuf + " NOT NULL";
	    }
	    if (field.IsPrimaryKey) {
	      strBuf = strBuf + " PRIMARY KEY";
	    }

	    if (field.DefaultValue.length() > 0) {
	      strBuf = strBuf + " DEFAULT "+field.DefaultValue;
	    }

	    strBuf = strBuf + endStr;
	    try {
	    	userMapper.insertExecSQL(strBuf);
	    	result = true;
	    } catch (Exception e) {
	      result = false;
	      LOGGER.error("addField出现异常", e);
	    }
	    return result;
	}

	@Override
	public boolean deleteField(BPIP_OPERATE_TABLE field) throws Exception {
	    String strSql = "";
	    try {
	    	if (getColumnCount(field.TableName) == 0) {//该表无字段
	  	      //表中只一个字段，删除此字段后表即不存在，所以要执行删除表的操作
	  	      if (DataBaseType.equals("2") || DataBaseType.equals("3")) {//MSSQL或MySQL
	  	        strSql = "DROP TABLE "+field.TableName;
	  	      } else {// DataBaseType=1，Oracle数据库
	  	        strSql = "DROP TABLE "+field.TableName+" CASCADE CONSTRAINT";
	  	      }
	  	      userMapper.deleteExecSQL(strSql);
	  	      //删除表名表
	  	      String delSql = "DELETE FROM BPIP_TABLE WHERE TABLENAME='"+field.TableName+"'";
	  	      userMapper.deleteExecSQL(delSql);
	  	    } else {
	  	      strSql = "ALTER TABLE "+field.TableName+" DROP COLUMN "+field.FieldName;
	  	      userMapper.updateExecSQL(strSql);
	  	    }
	    	return true;
	    } catch (Exception ex) {
	      LOGGER.error("DeleteField-出现异常", ex);
	    }
	    return false;
	}

	@Override
	public boolean deleteTable(String tableName) throws Exception {
	    String strSql = "";
	    boolean result = false;
	    if (DataBaseType.equals("2") || DataBaseType.equals("3")) {//MSSQL或MySQL
	      strSql = "DROP TABLE "+tableName;
	    } else {
	      strSql = "DROP TABLE "+tableName+" CASCADE CONSTRAINT";
	    }
	    try {
	    	userMapper.deleteExecSQL(strSql);
	        result = true;
	    } catch (Exception ex) {
	        LOGGER.error("DeleteTable-出现异常", ex);
	    }
	    return result;
	}

	@Override
	public boolean modifyField(BPIP_OPERATE_TABLE field) throws Exception {
	    LOGGER.info("modifyField开始调用...");
	    boolean result = false;
	    StringBuffer strBuf = new StringBuffer();
	    
	    if (DataBaseType.equals("2")) {//MSSQL数据库
	       //处理MSSQL字段的类型，Oracle的VARCHAR2变成VARCHAR
	       if (field.FieldType.equals("VARCHAR2")) {
	          field.FieldType = "VARCHAR";
	       }
	       if (field.FieldType.equals("NUMBER")) {
	          field.FieldType = "NUMERIC";
	       }
	       if (field.FieldType.equals("BLOB")) {
	          field.FieldType = "IMAGE";
	       }
	       if (field.FieldType.equals("CLOB")) {
	          field.FieldType = "TEXT";
	       }
	       if (field.FieldType.equals("DATE")) {
	          field.FieldType = "DATETIME";
	       }
	    }
	    else if (DataBaseType.equals("3")) {//MySQL数据库
	       //处理MSSQL字段的类型，Oracle的VARCHAR2变成VARCHAR
	       if (field.FieldType.equals("VARCHAR2")) {
	          field.FieldType = "VARCHAR";
	       }
	       if (field.FieldType.equals("NUMBER")) {
	          field.FieldType = "NUMERIC";
	       }
	       if (field.FieldType.equals("BLOB")) {
	          field.FieldType = "LONGBLOB";
	       }
	       if (field.FieldType.equals("CLOB")) {
	          field.FieldType = "LONGTEXT";
	       }
	       if (field.FieldType.equals("DATE")) {
	          field.FieldType = "DATETIME";
	       }
	    } else {
	    }
	    if (!field.FieldName.equals(field.OldFieldName)) {
	        if (DataBaseType.equals("1")) {//oracle数据库
	          strBuf.append("ALTER TABLE "+field.TableName+"  RENAME COLUMN "+
	                      field.OldFieldName+" TO "+field.FieldName+";");
	        }
	        else if (DataBaseType.equals("2")) {//mssql数据库
	          strBuf.append("exec sp_rename '"+field.TableName+"."+
	                      field.OldFieldName+"' , '"+field.FieldName+"';");
	        }
	        else if (DataBaseType.equals("3")) {//mysql数据库
	          strBuf.append("ALTER TABLE "+field.TableName+"  change "+
	                      field.OldFieldName+" "+field.FieldName+";");
	        } else {
	        }
	    }
        if (DataBaseType.equals("1")) {//oracle数据库
        	strBuf.append("ALTER TABLE "+field.TableName+" MODIFY "+
                      field.FieldName);
        }
        else if (DataBaseType.equals("2")) {//mssql数据库
        	strBuf.append("ALTER TABLE "+field.TableName+" ALTER COLUMN "+
                      field.FieldName);
        }
        else if (DataBaseType.equals("3")) {//mysql数据库
	        strBuf.append("ALTER TABLE "+field.TableName+"  modify column  "+
	                      field.FieldName);
	    } else {
	    }
	    strBuf.append(" "+field.FieldType+"");

	    if (field.Length > 0 && field.Precision > 0) {
	        strBuf.append("("+String.valueOf(field.Length)+","+
	                      String.valueOf(field.Precision)+")");
	    }
	    else if (field.Length > 0) {
	        strBuf.append("("+String.valueOf(field.Length)+")");
	    }

	    if (!field.IsNull) {
	    	strBuf.append(" NOT NULL");
	    }
	    if (field.IsNull) {
	    	strBuf.append(" NULL");
	    }

	    if (field.IsPrimaryKey) {
	        strBuf.append(" PRIMARY KEY");
	    }
	    if (field.DefaultValue.length() > 0) {
	        strBuf.append(" DEFAULT "+field.DefaultValue);
	    }
	    strBuf.append(";");
	    try {
	        LOGGER.info("执行的SQL语句", strBuf.toString());
	        String[] sqls = strBuf.toString().split(";");
	        for (String sql : sqls) {
	        	userMapper.updateExecSQL(sql);
	        }
	    }
	    catch (Exception e) {
	        LOGGER.error("出现异常", e.getMessage());
	    }
	    return result;
	}

	@Override
	public boolean changeTableName(String oldName, String newName) throws Exception {
	    boolean result = false;
	    String strSql = "ALTER TABLE "+oldName+" rename to "+newName;
	    try {
	    	  userMapper.updateExecSQL(strSql);
	    	  result = true;
	    } catch (Exception e) {
	        result = false;
	        LOGGER.error("ChangeTableName-出现异常", e.getMessage());
	    }
	    return result;
	}

	/**
	 * 功能：判断数据库中指定表中的字段数量
	 * @param tableName 表名
	 * @return
	 */
	private int getColumnCount(String tableName) {
	    String strSql = "select FIELDID from BPIP_FIELD where TABLEID in (select TABLEID from BPIP_TABLE "
	    			  + "where TABLENAME=upper('" + tableName + "') or TABLENAME='" + tableName + "')";
	    int length = 0;
	    try {
	    	List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
	    	length = retlist != null ? retlist.size() : 0;
	    }
	    catch (Exception e) {
	    	LOGGER.error("OperateTableServiceImpl.getColumnCount出现异常", e);
	    }
	    return length;
	}

	/**
     * 功能：判断数据库中是否有指定的表存在
     * @param tableName 表名字串
     * @return 返回boolearn值
     **/
    private boolean IsExistTable(String tableName) {
	      LOGGER.info("IsExistTable开始调用...");
	      boolean result = false;
	      String strSql = "select count(TABLEID) as num from BPIP_FIELD "
	      				+ "where TABLEID in (select TABLEID from BPIP_TABLE "
	      				+ "where TABLENAME=upper('"+tableName+"') or TABLENAME='"+ tableName+"')";
	      
	      int rowCount  = 0;
	      try {
	    	  Integer retNum = userMapper.selectIntExecSQL(strSql);
	    	  if (retNum != null && retNum > 0) {
	    		  rowCount = retNum;
	    	  }
	      } catch (Exception e) {
	        rowCount = 0;
	        LOGGER.error("OperateTableServiceImpl.IsExistTable出现异常", e);
	      }
	      if (rowCount > 0) {
	        result = true;
	      } else {
	        result = false;
	      }
	      LOGGER.info("IsExistTable", "结束调用");
	      return result;
    }
}