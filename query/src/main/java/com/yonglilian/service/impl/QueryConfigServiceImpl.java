package com.yonglilian.service.impl;


import com.yonglilian.common.util.ReflectionUtil;
import com.yonglilian.common.util.StringUtils;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.UserMapper;
import com.yonglilian.dao.mapper.QueryConfigMapper;
import com.yonglilian.queryengine.mode.*;
import com.yonglilian.service.QueryConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBServer;
import zr.zrpower.common.db.DBSet;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 查询配置管理EJB服务层
 * @author lwk
 *
 */
@Service
public class QueryConfigServiceImpl implements QueryConfigService {
	/** The QueryConfigServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryConfigServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
	/** 查询配置的DAO层. */
	@Autowired
	private QueryConfigMapper queryConfigMapper;

	/**
	 * 构造方法
	 */
	public QueryConfigServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource,
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}

	@Override
	public QUERY_CONFIG_CONNECTION[] getConfigConnectionByWhere(String strWhere) throws Exception {
		QUERY_CONFIG_CONNECTION entityList[] = null;
		StringBuffer strSql = new StringBuffer();
		try {
			if (strWhere.trim().length() == 0) {
				strSql.append("Select * From QUERY_CONFIG_CONNECTION ");
			} else {
				strSql.append("Select * From QUERY_CONFIG_CONNECTION  where ").append(strWhere);
			}
			strSql.append(" Order by ID");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				return null;
			} else {// length > 0
				entityList = new QUERY_CONFIG_CONNECTION[length];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					entityList[i] = (QUERY_CONFIG_CONNECTION) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_CONNECTION.class);
				}
			}
		} catch (Exception e) {
			LOGGER.error("getConfigConnectionByWhere出现异常！" + e.getMessage());
			return null;
		}
		return entityList;
	}

	@Override
	public QUERY_CONFIG_TABLE getConfigByID(String ID) throws Exception {
		QUERY_CONFIG_TABLE entityObj = null;
		StringBuffer strSql = new StringBuffer();
		try {
			strSql.append("Select * From QUERY_CONFIG_TABLE Where ID='"+ID.trim()+"'");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
			if (retlist == null || retlist.size() < 1) {
				return null;
			} else {// retlist != null
				entityObj = (QUERY_CONFIG_TABLE) ReflectionUtil.convertMapToBean(retlist.get(0), QUERY_CONFIG_TABLE.class);
			}
		} catch (Exception e) {
			LOGGER.error("调用方法GetConfigByID出现异常" + e.toString());
			return null;
		}
		return entityObj;
	}

	@Override
	public boolean saveBaseProperty(QUERY_CONFIG_TABLE entityObj) throws Exception {
		boolean result = false;
		DBServer dserver = new DBServer();
		dserver.saveBaseProperty(entityObj);
		return result;
	}

	@Override
	public boolean saveRelationProperty(String FID, String FieldInfoStr) throws Exception {
		// 左关联表名#左关联字段#右关联表名#右关联字段#关联类型&
		boolean result = false;
		int i;
		String tempArray1[] = FieldInfoStr.split("&");
		String tempArray2[];
		StringBuffer strSql = new StringBuffer();
		try {
			strSql.append("Delete from QUERY_CONFIG_CONNECTION Where FID='" + FID + "'");
			userMapper.deleteExecSQL(strSql.toString());
			String MaxFieldNo = getMaxFieldNo("QUERY_CONFIG_CONNECTION", "ID", 8);
			for (i = 0; i < tempArray1.length; i++) {
				tempArray2 = tempArray1[i].split("#");
				if (tempArray2[0].length() == 0) {
					continue;
				}
				strSql.delete(0, strSql.length()); // 清空
				strSql.append("Insert Into QUERY_CONFIG_CONNECTION(ID,FID,CFIELD,MFIELD,JOINTYPE) Values(" + "'"
						+ getMaxFieldNoAdd(MaxFieldNo, 8, i) + "'," + "'" + FID + "'," + "'" + tempArray2[0] + "."
						+ tempArray2[1] + "'," + "'" + tempArray2[2] + "." + tempArray2[3] + "'," + "'" + tempArray2[4]
						+ "'" + ")");
				userMapper.insertExecSQL(strSql.toString());
			}
			result = true;
		} catch (Exception ex) {
			result = false;
		}
		return result;
	}

	@Override
	public boolean saveInitWhereProperty(String FID, String FieldInfoStr) throws Exception {
		// 左括号#条件列#条件符#条件值#右括号#逻辑符&
		boolean result = false;
		int i;
		StringBuffer strSql = new StringBuffer();
		String tempArray1[] = FieldInfoStr.split("&");
		String tempArray2[];
		try {
			strSql.append("Delete from QUERY_CONFIG_INIT Where FID='" + FID).append("'");
			userMapper.deleteExecSQL(strSql.toString());
			String MaxFieldNo = getMaxFieldNo("QUERY_CONFIG_INIT", "ID", 8);
			for (i = 0; i < tempArray1.length; i++) {
				tempArray2 = tempArray1[i].split("#");
				if (tempArray2[0].length() == 0) {
					continue;
				}
				strSql.delete(0, strSql.length()); // 清空
				strSql.append("Insert Into QUERY_CONFIG_INIT(ID,FID,QLEFT,FIELD,SYMBOL,WHEREVALUE,QRIGHT,LOGIC) Values("
						+ "'" + getMaxFieldNoAdd(MaxFieldNo, 8, i) + "'," + "'" + FID + "'," + "'" + tempArray2[0]
						+ "'," + "'" + tempArray2[1] + "'," + "'" + tempArray2[2] + "'," + "'"
						+ convertQuotationSingleToDouble(tempArray2[3]) + "'," + "'" + tempArray2[4] + "'," + "'"
						+ tempArray2[5] + "'" + ")");
				userMapper.insertExecSQL(strSql.toString());
			}
			result = true;
		} catch (Exception ex) {
			result = false;
		}
		return result;
	}

	@Override
	public QUERY_CONFIG_INIT[] getConfigInitByWhere(String strWhere) throws Exception {
		QUERY_CONFIG_INIT entityList[] = null;
		StringBuffer strSql = new StringBuffer();
		try {
			if (strWhere.trim().length() == 0) {
				strSql.append("Select * From QUERY_CONFIG_INIT ");
			} else {
				strSql.append("Select * From QUERY_CONFIG_INIT where " + strWhere);
			}
			strSql.append(" Order by ID");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				return null;
			} else {// length > 0
				entityList = new QUERY_CONFIG_INIT[length];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					entityList[i] = (QUERY_CONFIG_INIT) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_INIT.class);
				}
			}
		} catch (Exception e) {
			LOGGER.info("getConfigInitByWhere出现异常！" + e.getMessage());
			return null;
		}
		return entityList;
	}

	@Override
	public boolean saveQueryFieldProperty(String FID, String FieldInfoStr) throws Exception {
		// 表名.字段名,表名.字段名,
		boolean result = false;
		int i;
		String strSql = "";
		String tempArray[] = FieldInfoStr.split(",");

		String FIELD = "";
		String ISPRECISION = "";
		String ISMUST = "";
		String ISDAY = "";
		String DVALUE = "";
		Hashtable<String, Object> Hashtable1 = new Hashtable<String, Object>();// 是否精确查询对照表
		Hashtable<String, Object> Hashtable2 = new Hashtable<String, Object>();// 是否必填对照表
		Hashtable<String, Object> Hashtable3 = new Hashtable<String, Object>();// 日期范围对照表
		Hashtable<String, Object> Hashtable4 = new Hashtable<String, Object>();// 默认值对照表
		try {
			strSql = "select * from QUERY_CONFIG_QUERYFIELD Where FID='" + FID + "'";
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
			int length = retlist != null ? retlist.size() : 0;
			if (length > 0) {
				for (int k = 0; k < length; k++) {
					Map<String, Object> retmap = retlist.get(k);
					FIELD = retmap.get("FIELD").toString();
					ISPRECISION = retmap.get("ISPRECISION").toString();
					ISMUST = retmap.get("ISMUST").toString();
					ISDAY = retmap.get("ISDAY").toString();
					DVALUE = retmap.get("DVALUE").toString();
					Hashtable1.put(FID + "." + FIELD, ISPRECISION);
					Hashtable2.put(FID + "." + FIELD, ISMUST);
					Hashtable3.put(FID + "." + FIELD, ISDAY);
					Hashtable4.put(FID + "." + FIELD, DVALUE);
				}
			}
			strSql = "Delete from QUERY_CONFIG_QUERYFIELD Where FID='" + FID + "'";
			userMapper.deleteExecSQL(strSql);
			String MaxFieldNo = getMaxFieldNo("QUERY_CONFIG_QUERYFIELD", "ID", 8);
			for (i = 0; i < tempArray.length; i++) {
				if (tempArray[i].trim().equals("")) {
					continue;
				}
				ISPRECISION = (String) Hashtable1.get(FID + "." + tempArray[i]);
				if (ISPRECISION == null) {
					ISPRECISION = "0";
				}
				ISMUST = (String) Hashtable2.get(FID + "." + tempArray[i]);
				if (ISMUST == null) {
					ISMUST = "0";
				}
				ISDAY = (String) Hashtable3.get(FID + "." + tempArray[i]);
				if (ISDAY == null) {
					ISDAY = "0";
				}
				DVALUE = (String) Hashtable4.get(FID + "." + tempArray[i]);
				if (DVALUE == null) {
					DVALUE = "";
				}
				strSql = "Insert Into QUERY_CONFIG_QUERYFIELD(ID,FID,FIELD,ISPRECISION,ISMUST,ISDAY,DVALUE) Values('"
						+ getMaxFieldNoAdd(MaxFieldNo, 8, i) + "','" + FID + "','" + tempArray[i] + "','" + ISPRECISION
						+ "','" + ISMUST + "','" + ISDAY + "','" + DVALUE + "')";

				userMapper.insertExecSQL(strSql);
			}
			result = true;
		} catch (Exception ex) {
			LOGGER.error("出现异常", ex.getMessage());
			result = false;
		}
		return result;
	}

	@Override
	public QUERY_CONFIG_QUERYFIELD[] getQueryFieldByWhere(String strWhere) throws Exception {
		QUERY_CONFIG_QUERYFIELD entityList[] = null;
		StringBuffer strSql = new StringBuffer();
		try {
			if (strWhere.trim().length() == 0) {
				strSql.append("Select * From QUERY_CONFIG_QUERYFIELD ");
			} else {
				strSql.append("Select * From QUERY_CONFIG_QUERYFIELD where ").append(strWhere);
			}
			strSql.append(" Order by ID");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				entityList = null;
			} else {// length > 0
				entityList = new QUERY_CONFIG_QUERYFIELD[length];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					entityList[i] = (QUERY_CONFIG_QUERYFIELD) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_QUERYFIELD.class);
				}
			}
		} catch (Exception e) {
			LOGGER.error("getQueryFieldByWhere出现异常！" + e.getMessage());
			entityList = null;
		}
		return entityList;
	}

	@Override
	public boolean saveResultFieldProperty(String FID, String disFieldInfoStr, String hidFieldInfoStr) throws Exception {
		// 表名.字段名,表名.字段名,
		boolean result = false;
		int i;
		String tempArray[], tempArray1[];
		StringBuffer strSql = new StringBuffer();
		String ISNUMBER = "0";
		String QALIGN = "0";
		try {
			strSql.append("Delete from QUERY_CONFIG_SHOWFIELD Where FID='" + FID + "'");
			userMapper.deleteExecSQL(strSql.toString());
			tempArray = disFieldInfoStr.split(",");
			String MaxFieldNo = getMaxFieldNo("QUERY_CONFIG_SHOWFIELD", "ID", 8);
			for (i = 0; i < tempArray.length; i++) {
				if (tempArray[i].trim().equals("")) {
					continue;
				}
				tempArray1 = tempArray[i].split("#");
				if (tempArray1[2] == null) {
					ISNUMBER = "0";
				} else {
					if (tempArray1[2].equals("是")) {
						ISNUMBER = "1";
					} else {
						ISNUMBER = "0";
					}
				}
				if (tempArray1[3] == null) {
					QALIGN = "0";
				}
				if (tempArray1[3].equals("居左")) {
					QALIGN = "0";
				}
				if (tempArray1[3].equals("居中")) {
					QALIGN = "1";
				}
				if (tempArray1[3].equals("居右")) {
					QALIGN = "2";
				}
				strSql.delete(0, strSql.length()); // 清空
				strSql.append("Insert Into QUERY_CONFIG_SHOWFIELD(ID,FID,FIELD,ISSHOW,COLWIDTH,ISNUMBER,QALIGN) Values("
						+ "'" + getMaxFieldNoAdd(MaxFieldNo, 8, i) + "'," + "'" + FID + "'," + "'" + tempArray1[0]
						+ "','1','").append(tempArray1[1] + "','" + ISNUMBER + "'" + ",'" + QALIGN + "')");

				userMapper.insertExecSQL(strSql.toString());
			}
			tempArray = hidFieldInfoStr.split(",");
			String MaxFieldNo1 = getMaxFieldNo("QUERY_CONFIG_SHOWFIELD", "ID", 8);
			for (i = 0; i < tempArray.length; i++) {
				if (tempArray[i].trim().equals("")) {
					continue;
				}
				strSql.delete(0, strSql.length()); // 清空
				strSql.append("Insert Into QUERY_CONFIG_SHOWFIELD(ID,FID,FIELD,ISSHOW,ISNUMBER,QALIGN) Values(" + "'"
						+ getMaxFieldNoAdd(MaxFieldNo1, 8, i) + "'," + "'" + FID + "'," + "'" + tempArray[i]
						+ "','0','0','0'" + ")");

				userMapper.insertExecSQL(strSql.toString());
			}
			result = true;
		} catch (Exception ex) {
			LOGGER.error("出现异常", ex.getMessage());
			result = false;
		}
		return result;
	}

	@Override
	public QUERY_CONFIG_SHOWFIELD[] getResultFieldByWhere(String strWhere) throws Exception {
		QUERY_CONFIG_SHOWFIELD entityList[] = null;
		StringBuffer strSql = new StringBuffer();
		try {
			if (strWhere.trim().length() == 0) {
				strSql.append("Select * From QUERY_CONFIG_SHOWFIELD ");
			} else {
				strSql.append("Select * From QUERY_CONFIG_SHOWFIELD where ").append(strWhere);
			}
			strSql.append(" Order by ID");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				entityList = null;
			} else {// length > 0
				entityList = new QUERY_CONFIG_SHOWFIELD[length];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					entityList[i] = (QUERY_CONFIG_SHOWFIELD) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_SHOWFIELD.class);
				}
			}
		} catch (Exception e) {
			LOGGER.error("getResultFieldByWhere出现异常！" + e.getMessage());
			entityList = null;
		}
		return entityList;
	}

	@Override
	public boolean saveDetailParameterProperty(String FID, String FieldInfoStr, String BRelationInfoStr) throws Exception {
		// 参数名#参数字段名#按钮ID&
		// 按钮ID,按钮ID
		boolean result = false;
		int i;
		String tempArray1[] = FieldInfoStr.split("&");
		String tempArray2[];
		StringBuffer strSql = new StringBuffer();
		try {
			strSql.append("Delete from QUERY_CONFIG_PARAMETER Where FID='" + FID + "'");
			userMapper.deleteExecSQL(strSql.toString());
			String MaxFieldNo = getMaxFieldNo("QUERY_CONFIG_PARAMETER", "ID", 8);
			for (i = 0; i < tempArray1.length; i++) {
				tempArray2 = tempArray1[i].split("#");
				if (tempArray2[0].length() == 0) {
					continue;
				}
				strSql.delete(0, strSql.length()); // 清空
				strSql.append("Insert Into QUERY_CONFIG_PARAMETER(ID,FID,NAME,FIELD,BID) Values(" + "'"
						+ getMaxFieldNoAdd(MaxFieldNo, 8, i) + "'," + "'" + FID + "'," + "'" + tempArray2[0]
						+ "'," + "'" + tempArray2[1] + "'," + "'" + tempArray2[2] + "'" + ")");

				userMapper.insertExecSQL(strSql.toString());
			}
			/***************** 按钮配置关系 *****************/
			tempArray1 = BRelationInfoStr.split(",");
			strSql.delete(0, strSql.length()); // 清空
			strSql.append("Delete from QUERY_CONFIG_BRELATION Where FID='" + FID + "'");
			userMapper.deleteExecSQL(strSql.toString());
			String MaxFieldNo1 = getMaxFieldNo("QUERY_CONFIG_BRELATION", "ID", 8);
			for (i = 0; i < tempArray1.length; i++) {
				if (tempArray1[i].trim().equals("")) {
					continue;
				}
				strSql.delete(0, strSql.length()); // 清空
				strSql.append("Insert Into QUERY_CONFIG_BRELATION(ID,FID,BID) Values(" + "'"
						+ getMaxFieldNoAdd(MaxFieldNo1, 8, i) + "'," + "'" + FID + "'," + "'"
						+ tempArray1[i] + "'" + ")");

				userMapper.insertExecSQL(strSql.toString());
			}
			/***************** 按钮配置关系 *****************/
			result = true;
		} catch (Exception ex) {
			LOGGER.error("出现异常", ex.getMessage());
			result = false;
		}
		return result;
	}

	@Override
	public QUERY_CONFIG_PARAMETER[] getDetailParameterFieldByWhere(String strWhere) throws Exception {
		QUERY_CONFIG_PARAMETER entityList[] = null;
		StringBuffer strSql = new StringBuffer();
		try {
			if (strWhere.trim().length() == 0) {
				strSql.append("Select * From QUERY_CONFIG_PARAMETER ");
			} else {
				strSql.append("Select * From QUERY_CONFIG_PARAMETER where ").append(strWhere);
			}
			strSql.append(" Order by ID");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				return null;
			} else {// length > 0
				entityList = new QUERY_CONFIG_PARAMETER[length];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					entityList[i] = (QUERY_CONFIG_PARAMETER) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_PARAMETER.class);
				}
			}
		} catch (Exception e) {
			LOGGER.error("getDetailParameterFieldByWhere出现异常！" + e.getMessage());
			entityList = null;
		}
		return entityList;
	}

	@Override
	public boolean saveSortProperty(String FID, String FieldInfoStr) throws Exception {
		// 排序字段#类型&
		boolean result = false;
		int i;
		StringBuffer strSql = new StringBuffer();
		String tempArray1[] = FieldInfoStr.split("&");
		String tempArray2[];
		try {
			strSql.append("Delete from QUERY_CONFIG_ORDER Where FID='" + FID).append("'");
			userMapper.deleteExecSQL(strSql.toString());
			String MaxFieldNo = getMaxFieldNo("QUERY_CONFIG_ORDER", "ID", 8);
			for (i = 0; i < tempArray1.length; i++) {
				tempArray2 = tempArray1[i].split("#");
				if (tempArray2[0].length() == 0) {
					continue;
				}
				strSql.delete(0, strSql.length()); // 清空
				strSql.append("Insert Into QUERY_CONFIG_ORDER(ID,FID,FIELD,TYPE) Values(" + "'"
						+ getMaxFieldNoAdd(MaxFieldNo, 8, i) + "'," + "'" + FID + "'," + "'"
						+ tempArray2[0] + "'," + "'" + tempArray2[1] + "'" + ")");

				userMapper.deleteExecSQL(strSql.toString());
			}
			result = true;
		} catch (Exception ex) {
			result = false;
		}
		// 始初化
		try {
			QueryControlImpl queryControl = new QueryControlImpl();
			queryControl.inithashtable();
			QueryServiceImpl queryService = new QueryServiceImpl();
			queryService.inithashtable();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public QUERY_CONFIG_ORDER[] getSortByWhere(String strWhere) throws Exception {
		QUERY_CONFIG_ORDER entityList[] = null;
		StringBuffer strSql = new StringBuffer();
		try {
			if (strWhere.trim().length() == 0) {
				strSql.append("Select * From QUERY_CONFIG_ORDER ");
			} else {
				strSql.append("Select * From QUERY_CONFIG_ORDER where ").append(strWhere);
			}
			strSql.append(" Order by ID");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				return null;
			} else {// length > 0
				entityList = new QUERY_CONFIG_ORDER[length];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					entityList[i] = (QUERY_CONFIG_ORDER) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_ORDER.class);
				}
			}
		} catch (Exception e) {
			LOGGER.error("getSortByWhere出现异常！" + e.getMessage());
			entityList = null;
		}
		return entityList;
	}

	@Override
	public boolean deleteConfigByID(String FID) throws Exception {
		boolean result = false;
		StringBuffer strSql = new StringBuffer();
		try {
			strSql.append("Delete from QUERY_CONFIG_PARAMETER Where FID='" + FID + "';"
					+ "Delete from QUERY_CONFIG_CONNECTION Where FID='")
					.append(FID + "';" + "Delete from QUERY_CONFIG_INIT Where FID='" + FID)
					.append("';" + "Delete QUERY_CONFIG_QUERYFIELD Where FID='")
					.append(FID + "';" + "Delete from QUERY_CONFIG_SHOWFIELD Where FID='")
					.append(FID + "';" + "Delete from QUERY_CONFIG_BRELATION Where FID='")
					.append(FID + "';" + "Delete from QUERY_CONFIG_ORDER Where FID='" + FID + "';"
							+ "Delete from QUERY_CONFIG_BUTTON Where FID='" + FID + "';"
							+ "Delete from QUERY_CONFIG_TABLE Where ID='" + FID)
					.append("';");

			for (String execSQL : strSql.toString().split(";")) {
				userMapper.deleteExecSQL(execSQL);
			}
			result = true;
		} catch (Exception ex) {
			LOGGER.error("出现异常", ex.getMessage());
			result = false;
		}
		return result;
	}

	@Override
	public String getMaxFieldNo(String TableName, String FieldName, int FieldLen) throws Exception {
		String MaxNo = "";
		int LenMaxNo = 0;
		StringBuffer strSql = new StringBuffer();
		strSql.append("SELECT MAX(" + FieldName + ") AS MaxNo FROM " + TableName);
		try {
			String retMaxNo = userMapper.selectStrExecSQL(strSql.toString());
			if (retMaxNo == null) {
				retMaxNo = "";
			}
			MaxNo = retMaxNo;// 获取最大编号
			if (MaxNo.length() > 0) {
				MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
				LenMaxNo = MaxNo.length();
				MaxNo = "0000000000000000000000000" + MaxNo;
			} else {
				MaxNo = "00000000000000000000000001";
				LenMaxNo = 1;
			}
			MaxNo = MaxNo.substring(25 - FieldLen + LenMaxNo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return MaxNo;
	}

	@Override
	public QUERY_CONFIG_TABLE[] all_Query_List(String where) throws Exception {
		QUERY_CONFIG_TABLE bgs[] = null;
		StringBuffer strSql = new StringBuffer();
		try {
			if (where.trim().length() == 0) {
				strSql.append("Select * From QUERY_CONFIG_TABLE ");
			} else {
				strSql.append("Select * From QUERY_CONFIG_TABLE where " + where);
			}
			strSql.append(" Order by ID");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				return null;
			} else {// length > 0
				bgs = new QUERY_CONFIG_TABLE[length];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					bgs[i] = (QUERY_CONFIG_TABLE) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_TABLE.class);
				}
			}
		} catch (Exception e) {
			LOGGER.error("all_Query_List出现异常！" + e.getMessage());
			return null;
		}
		return bgs;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getTableList(String tableName) throws Exception {
		List result = new ArrayList();
		StringBuffer strSql = new StringBuffer();
		if (tableName.trim().length() > 0) {
			strSql.append("Select TableName,ChineseName from BPIP_TABLE where TableName !='")
				  .append(tableName + "' order by ChineseName");
		} else {
			strSql.append("Select TableName,ChineseName from BPIP_TABLE order by ChineseName");
		}
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				String[] saTmp = new String[2];
				saTmp[0] = retmap.get("TableName").toString();
				saTmp[1] = retmap.get("CHINESENAME").toString();
				result.add(saTmp);
			}
		}
		return result;
	}

	@Override
	public List<Object> getFieldList(String TableName) throws Exception {
		List<Object> result = new ArrayList<Object>();
		StringBuffer strSql = new StringBuffer();
		strSql.append("select FieldID,FieldName,CHINESENAME from bpip_field "
					+ "Where TABLEID=(Select TableID From Bpip_Table Where TableName='")
			  .append(TableName.toUpperCase() + "') And FIELDTYPE!='3' Order by FieldID");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				String[] saTmp = new String[2];
				saTmp[0] = retmap.get("FieldName").toString();
				saTmp[1] = retmap.get("CHINESENAME").toString();
				result.add(saTmp);
			}
		}
		return result;
	}

	@Override
	public List<Object> getTableFieldList(String TableName, boolean flag) throws Exception {
		List<Object> result = new ArrayList<Object>();
		String strSql = "";
		String strSqlA = "";
		String strTMP = "";
		String tmpTableName;
		DBSet ds = null;
		DBSet ds1 = null;
		String[] tableArray = TableName.split("\\.");
		if (tableArray.length > 0) {
			// 先处理主表
			tmpTableName = tableArray[0];
			if (DataBaseType.equals("3")) {// MySQL数据库
				strSql = "Select b.Tablename,a.FieldName,a.fieldtype, b.CHINESENAME FROM bpip_field a,bpip_table b where a.Tableid=b.tableid And b.TableName='"
						+ tmpTableName + "' And a.FIELDTYPE!='3' Order By b.TableId,a.FieldId";
				strSqlA = "Select b.Tablename,a.FieldName,a.fieldtype, a.CHINESENAME  FROM bpip_field a,bpip_table b where a.Tableid=b.tableid And b.TableName='"
						+ tmpTableName + "' And a.FIELDTYPE!='3' Order By b.TableId,a.FieldId";
				ds1 = dbEngine.QuerySQL(strSqlA);
			} else {
				strSql = "Select b.Tablename,a.FieldName,a.fieldtype, b.CHINESENAME as tableCHINESENAME,a.CHINESENAME as fieldCHINESENAME FROM bpip_field a,bpip_table b "
						+ "where a.Tableid=b.tableid And b.TableName='" + tmpTableName + "' And a.FIELDTYPE!='3' Order By b.TableId,a.FieldId";
			}
			ds = dbEngine.QuerySQL(strSql);
			if (ds != null && ds.RowCount() > 0) {
				for (int i = 0; i < ds.RowCount(); i++) {
					String[] saTmp = new String[2];
					strTMP = ds.Row(i).Column("Tablename").getString() + "." + ds.Row(i).Column("FieldName").getString();
					saTmp[0] = strTMP;
					if (flag) {
						strTMP = saTmp[0] + "." + ds.Row(i).Column("fieldtype").getString();
						saTmp[0] = strTMP;
					}
					if (DataBaseType.equals("3")) {// mysql数据库
						strTMP = ds.Row(i).Column("CHINESENAME").getString() + "." + ds1.Row(i).Column("CHINESENAME").getString();
					} else {
						strTMP = ds.Row(i).Column("tableCHINESENAME").getString() + "." + ds.Row(i).Column("fieldCHINESENAME").getString();
					}
					saTmp[1] = strTMP;
					result.add(saTmp);
				}
			}
			// 再处理关联表
			if (tableArray.length > 1) {
				tmpTableName = "";
				for (int i = 1; i < tableArray.length; i++) {
					strTMP = tmpTableName + "'" + tableArray[i] + "',";
					tmpTableName = strTMP;
				}
				strTMP = tmpTableName + "''";
				tmpTableName = strTMP;

				if (DataBaseType.equals("3")) {// mysql数据库
					strSql = "Select b.Tablename,a.FieldName,a.fieldtype, b.CHINESENAME  FROM bpip_field a,bpip_table b where a.Tableid=b.tableid And b.TableName In("
							+ tmpTableName + ") And a.FIELDTYPE!='3' Order By b.TableId,a.FieldId";
					strSqlA = "Select b.Tablename,a.FieldName,a.fieldtype, a.CHINESENAME  FROM bpip_field a,bpip_table b where a.Tableid=b.tableid And b.TableName In("
							+ tmpTableName + ") And a.FIELDTYPE!='3' Order By b.TableId,a.FieldId";
					ds1 = dbEngine.QuerySQL(strSqlA);
				} else {
					strSql = "Select b.Tablename,a.FieldName,a.fieldtype, b.CHINESENAME as tableCHINESENAME,a.CHINESENAME as fieldCHINESENAME   FROM bpip_field a,bpip_table b where a.Tableid=b.tableid And b.TableName In("
							+ tmpTableName + ") And a.FIELDTYPE!='3' Order By b.TableId,a.FieldId";
				}
				ds = dbEngine.QuerySQL(strSql);
				if (ds != null && ds.RowCount() > 0) {
					for (int i = 0; i < ds.RowCount(); i++) {
						String[] saTmp = new String[2];
						strTMP = ds.Row(i).Column("Tablename").getString() + "."
								+ ds.Row(i).Column("FieldName").getString();
						saTmp[0] = strTMP;
						if (flag) {
							strTMP = saTmp[0] + "." + ds.Row(i).Column("fieldtype").getString();
							saTmp[0] = strTMP;
						}
						if (DataBaseType.equals("3")) {// mysql数据库
							strTMP = ds.Row(i).Column("CHINESENAME").getString() + "." + ds1.Row(i).Column("CHINESENAME").getString();
						} else {
							strTMP = ds.Row(i).Column("tableCHINESENAME").getString() + "." + ds.Row(i).Column("fieldCHINESENAME").getString();
						}
						saTmp[1] = strTMP;
						result.add(saTmp);
					}
				}
			}
		}
		ds = null;
		return result;
	}

	@Override
	public String getTableFieldCnNameByEn(String tableFieldName) throws Exception {
		String strSql = "";
		String strSqlA = "";
		String strTMP = "";
		String resultStr = "";
		DBSet ds1 = null;
		if (DataBaseType.equals("1")) {// oracle数据库
			strSql = "select b.CHINESENAME as tableCHINESENAME,a.CHINESENAME as fieldCHINESENAME FROM bpip_field a,bpip_table b "
					+ "where a.Tableid=b.tableid and b.tablename||'.'||a.fieldname='" + tableFieldName + "'";
		}
		else if (DataBaseType.equals("2")) {// mssql数据库
			strSql = "select b.CHINESENAME as tableCHINESENAME,a.CHINESENAME as fieldCHINESENAME FROM bpip_field a,bpip_table b where a.Tableid=b.tableid and b.tablename+'.'+a.fieldname='"
					+ tableFieldName + "'";
		}
		else if (DataBaseType.equals("3")) {// mysql数据库
			strSql = "select b.CHINESENAME FROM bpip_field a,bpip_table b where a.Tableid=b.tableid and concat(b.tablename,'.',a.fieldname)='"
					+ tableFieldName + "'";
			strSqlA = "select a.CHINESENAME FROM bpip_field a,bpip_table b where a.Tableid=b.tableid and concat(b.tablename,'.',a.fieldname)='"
					+ tableFieldName + "'";
			ds1 = dbEngine.QuerySQL(strSqlA);
		} else {
		}
		DBSet ds = dbEngine.QuerySQL(strSql);
		if (ds != null && ds.RowCount() > 0) {
			if (DataBaseType.equals("3")) {// mysql数据库
				strTMP = ds.Row(0).Column("CHINESENAME").getString() + "." + ds1.Row(0).Column("CHINESENAME").getString();
			} else {
				strTMP = ds.Row(0).Column("tableCHINESENAME").getString() + "." + ds.Row(0).Column("fieldCHINESENAME").getString();
			}
			resultStr = strTMP;
		}
		ds = null;
		return resultStr;
	}

	@Override
	public String max_OrderNo(String prentNo) throws Exception {
		int intResult = 0;
		String strMenu_No = "";
		String strMaxNO = "0";
		StringBuffer strSql = new StringBuffer();
		strSql.append("SELECT MAX(ORDERCODE) AS MAX_NO FROM BPIP_MENU WHERE MENUNO like '" + prentNo + "___'");
		try {
			String retMAX_NO = userMapper.selectStrExecSQL(strSql.toString());
			if (StringUtils.isNotBlank(retMAX_NO)) {
				strMaxNO = retMAX_NO;
				if (strMaxNO.length() == 0) {
					strMaxNO = "0";
				}
			}
			intResult = Integer.parseInt(strMaxNO);
			if (intResult == 0) {
				strMenu_No = "0";
			} else {
				strMenu_No = String.valueOf(intResult + 1001).substring(2, 4);
			}
		} catch (Exception ex) {
			LOGGER.error("Max_OrderNo", "Max_OrderNo出现异常！" + ex.getMessage());
		}
		return strMenu_No;
	}

	@Override
	public boolean querycopy(String strID) throws Exception {
		String strTmp = "";// 临时编号
		String strMaxNo = "";// 最大流水号
		String strFID = "";// 父编号
		String strSql = "";// strSQL

		// 复制主配置表-------------------------
		strSql = "Select * From QUERY_CONFIG_TABLE where ID ='" + strID + "'";
		Map<String, Object> retmap = userMapper.selectMapExecSQL(strSql);
		if (retmap != null && retmap.size() > 0) {
			strMaxNo = getMaxFieldNo("QUERY_CONFIG_TABLE", "ID", 8);
			strFID = strMaxNo;
			retmap.put("ID", strMaxNo);
			strTmp = retmap.get("NAME").toString();
			retmap.put("NAME", "复件_" + strTmp);// 设置名称

			queryConfigMapper.insertQUERY_CONFIG_TABLE(retmap);
		}
		// --------------------------------------------
		if (strFID.length() > 0) {
			// 复制显示结果配置表-------------------------
			strSql = "Select * From QUERY_CONFIG_SHOWFIELD where FID ='" + strID + "' order by ID";
			List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(strSql);
			int length1 = retlist1 != null ? retlist1.size() : 0;
			if (length1 > 0) {
				for (int i = 0; i < length1; i++) {
					Map<String, Object> retmap1 = retlist1.get(i);
					strMaxNo = getMaxFieldNo("QUERY_CONFIG_SHOWFIELD", "ID", 8);
					retmap1.put("ID", strMaxNo);// 设置ID
					retmap1.put("FID", strFID);// 设置FID

					queryConfigMapper.insertQUERY_CONFIG_TABLE(retmap1);
				}
				retlist1 = null;
			}
			// 复制查询字段配置表-------------------------
			strSql = "Select * From QUERY_CONFIG_QUERYFIELD where FID ='" + strID + "' order by ID";
			List<Map<String, Object>> retlist2 = userMapper.selectListMapExecSQL(strSql);
			int length2 = retlist2 != null ? retlist2.size() : 0;
			if (length2 > 0) {
				for (int i = 0; i < length2; i++) {
					Map<String, Object> retmap2 = retlist2.get(i);
					strMaxNo = getMaxFieldNo("QUERY_CONFIG_QUERYFIELD", "ID", 8);
					retmap2.put("ID", strMaxNo);// 设置ID
					retmap2.put("FID", strFID);// 设置FID

					queryConfigMapper.insertQUERY_CONFIG_QUERYFIELD(retmap2);
				}
				retlist2 = null;
			}
			// --------------------------------

			// 复制初始条件配置表-------------------------
			strSql = "Select * From QUERY_CONFIG_INIT where FID ='" + strID + "' order by ID";
			List<Map<String, Object>> retlist3 = userMapper.selectListMapExecSQL(strSql);
			int length3 = retlist3 != null ? retlist3.size() : 0;
			if (length3 > 0) {
				for (int i = 0; i < length3; i++) {
					Map<String, Object> retmap3 = retlist3.get(i);
					strMaxNo = getMaxFieldNo("QUERY_CONFIG_INIT", "ID", 8);

					retmap3.put("ID", strMaxNo);// 设置ID
					retmap3.put("FID", strFID);// 设置FID

					queryConfigMapper.insertQUERY_CONFIG_INIT(retmap3);
				}
				retlist3 = null;
			}
			// 复制查询关系配置表-------------------------
			strSql = "Select * From QUERY_CONFIG_CONNECTION where FID ='" + strID + "' order by ID";
			List<Map<String, Object>> retlist4 = userMapper.selectListMapExecSQL(strSql);
			int length4 = retlist4 != null ? retlist4.size() : 0;
			if (length4 > 0) {
				for (int i = 0; i < length4; i++) {
					Map<String, Object> retmap4 = retlist4.get(i);
					strMaxNo = getMaxFieldNo("QUERY_CONFIG_CONNECTION", "ID", 8);
					retmap4.put("ID", strMaxNo);// 设置ID
					retmap4.put("FID", strFID);// 设置FID

					queryConfigMapper.insertQUERY_CONFIG_CONNECTION(retmap4);
				}
				retlist4 = null;
			}
			// 复制按钮配置表-------------------------
			strSql = "Select * From QUERY_CONFIG_BRELATION where FID ='" + strID + "' order by ID";
			List<Map<String, Object>> retlist5 = userMapper.selectListMapExecSQL(strSql);
			int length5 = retlist5 != null ? retlist5.size() : 0;
			if (length5 > 0) {
				for (int i = 0; i < length5; i++) {
					Map<String, Object> retmap5 = retlist5.get(i);
					strMaxNo = getMaxFieldNo("QUERY_CONFIG_BRELATION", "ID", 8);
					retmap5.put("ID", strMaxNo);// 设置ID
					retmap5.put("FID", strFID);// 设置FID

					queryConfigMapper.insertQUERY_CONFIG_BRELATION(retmap5);
				}
				retlist5 = null;
			}
			// 复制按钮参数配置表-------------------------
			strSql = "Select * From QUERY_CONFIG_PARAMETER where FID ='" + strID + "' order by ID";
			List<Map<String, Object>> retlist6 = userMapper.selectListMapExecSQL(strSql);
			int length6 = retlist6 != null ? retlist6.size() : 0;
			if (length6 > 0) {
				for (int i = 0; i < length6; i++) {
					Map<String, Object> retmap6 = retlist6.get(i);
					strMaxNo = getMaxFieldNo("QUERY_CONFIG_PARAMETER", "ID", 8);

					retmap6.put("ID", strMaxNo);// 设置ID
					retmap6.put("FID", strFID);// 设置FID

					queryConfigMapper.insertQUERY_CONFIG_PARAMETER(retmap6);
				}
				retlist6 = null;
			}
			// 复制排序配置表-------------------------
			strSql = "Select * From QUERY_CONFIG_ORDER where FID ='" + strID + "' order by ID";
			List<Map<String, Object>> retlist7 = userMapper.selectListMapExecSQL(strSql);
			int length7 = retlist7 != null ? retlist7.size() : 0;
			if (length7 > 0) {
				for (int i = 0; i < length7; i++) {
					Map<String, Object> retmap7 = retlist7.get(i);
					strMaxNo = getMaxFieldNo("QUERY_CONFIG_ORDER", "ID", 8);

					retmap7.put("ID", strMaxNo);// 设置ID
					retmap7.put("FID", strFID);// 设置FID

					queryConfigMapper.insertQUERY_CONFIG_ORDER(retmap7);
				}
				retlist7 = null;
			}
			// 复制私有按钮数据-------------------------
			strSql = "Select * From QUERY_CONFIG_BUTTON where FID ='" + strID + "' order by ID";
			List<Map<String, Object>> retlist8 = userMapper.selectListMapExecSQL(strSql);
			int length8 = retlist8 != null ? retlist8.size() : 0;
			if (length8 > 0) {
				for (int i = 0; i < length8; i++) {
					Map<String, Object> retmap8 = retlist8.get(i);
					strMaxNo = getMaxFieldNo("QUERY_CONFIG_BUTTON", "ID", 8);

					retmap8.put("ID", strMaxNo);// 设置ID
					retmap8.put("FID", strFID);// 设置FID

					queryConfigMapper.insertQUERY_CONFIG_BUTTON(retmap8);
				}
				retlist8 = null;
			}
		}
		return true;
	}

	@Override
	public String getPrecision(String strID, String strFiled) throws Exception {
		String sreturn = "0,0,0,0";
		String T1 = "0";
		String T2 = "0";
		String T3 = "0";
		String T4 = "0";
		String strSql = "Select ISPRECISION,ISMUST,ISDAY,DVALUE From QUERY_CONFIG_QUERYFIELD "
				+ "where FID ='" + strID + "' and FIELD='" + strFiled + "'";
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			Map<String, Object> retmap = retlist.get(0);
			T1 = retmap.get("ISPRECISION").toString();
			T2 = retmap.get("ISMUST").toString();
			T3 = retmap.get("ISDAY").toString();
			T4 = retmap.get("DVALUE").toString();

			if (T1 == null) {
				T1 = "0";
			}
			if (T2 == null) {
				T2 = "0";
			}
			if (T3 == null) {
				T3 = "0";
			}
			if (T4 == null) {
				T4 = "0";
			}
			if (T1.length() == 0) {
				T1 = "0";
			}
			if (T2.length() == 0) {
				T2 = "0";
			}
			if (T3.length() == 0) {
				T3 = "0";
			}
			if (T4.length() == 0) {
				T4 = "0";
			}
		}
		sreturn = T1 + "," + T2 + "," + T3 + "," + T4;
		return sreturn;
	}

	@Override
	public boolean setPrecision(String strID, String strFiled, String svalue) throws Exception {
		String T1 = svalue.split(",")[0];
		String T2 = svalue.split(",")[1];
		String T3 = svalue.split(",")[2];
		String T4 = svalue.split(",")[3];
		// 始初化
		try {
			QueryControlImpl queryControl = new QueryControlImpl();
			queryControl.inithashtable();
			QueryServiceImpl queryService = new QueryServiceImpl();
			queryService.inithashtable();
		} catch (Exception ex) {
		}
		String strSql = "Update QUERY_CONFIG_QUERYFIELD set ISPRECISION='" + T1 + "',ISMUST='" + T2 + "',ISDAY='" + T3
				+ "',DVALUE='" + T4 + "'  where FID ='" + strID + "' and FIELD='" + strFiled + "'";
		Integer retInt = userMapper.updateExecSQL(strSql);
		if (retInt != null && retInt > 0) {
			return true;
		}
		return false;
	}

	@Override
	public String newQuery() throws Exception {
		String ID = getMaxFieldNo("QUERY_CONFIG_TABLE", "ID", 8);
		String strSql = "Insert Into QUERY_CONFIG_TABLE(ID,IFFIRSTDATA,IFMULSEL,IFCOMBTN) "
					  + "Values('" + ID + "','1','0','0')";
		userMapper.insertExecSQL(strSql);

		return ID;
	}

	@Override
	public Variable[] getVariableList() throws Exception {
		Variable bgs[] = null;
		bgs = new Variable[19];

		bgs[0] = new Variable();
		bgs[0].setCODE("{LoginID}");
		bgs[0].setNAME("登录名");

		bgs[1] = new Variable();
		bgs[1].setCODE("{Name}");
		bgs[1].setNAME("用户名");

		bgs[2] = new Variable();
		bgs[2].setCODE("{UserID}");
		bgs[2].setNAME("用户ID");

		bgs[3] = new Variable();
		bgs[3].setCODE("{LCODE}");
		bgs[3].setNAME("用户内部编号");

		bgs[4] = new Variable();
		bgs[4].setCODE("{UnitID}");
		bgs[4].setNAME("所在单位编号");

		bgs[5] = new Variable();
		bgs[5].setCODE("{UnitName}");
		bgs[5].setNAME("所在单位名称");

		bgs[6] = new Variable();
		bgs[6].setCODE("{YYYY年}");
		bgs[6].setNAME("当前年(中文式)");

		bgs[7] = new Variable();
		bgs[7].setCODE("{YYYY年MM月}");
		bgs[7].setNAME("当前年月(中文式)");

		bgs[8] = new Variable();
		bgs[8].setCODE("{YYYY年MM月DD日}");
		bgs[8].setNAME("当前日期(中文式)");

		bgs[9] = new Variable();
		bgs[9].setCODE("{YYYY年MM月DD日 HH:MM:SS}");
		bgs[9].setNAME("当前时间(中文式)");

		bgs[10] = new Variable();
		bgs[10].setCODE("{YYYY}");
		bgs[10].setNAME("当前年");

		bgs[11] = new Variable();
		bgs[11].setCODE("{YYYY-MM}");
		bgs[11].setNAME("当前年月");

		bgs[12] = new Variable();
		bgs[12].setCODE("{YYYY-MM-DD}");
		bgs[12].setNAME("当前日期");

		bgs[13] = new Variable();
		bgs[13].setCODE("{YYYY-MM-DD HH:MM:SS}");
		bgs[13].setNAME("当前时间");

		bgs[14] = new Variable();
		bgs[14].setCODE("{Custom1}");
		bgs[14].setNAME("自定义session1");

		bgs[15] = new Variable();
		bgs[15].setCODE("{Custom2}");
		bgs[15].setNAME("自定义session2");

		bgs[16] = new Variable();
		bgs[16].setCODE("{Custom3}");
		bgs[16].setNAME("自定义session3");

		bgs[17] = new Variable();
		bgs[17].setCODE("{Custom4}");
		bgs[17].setNAME("自定义session4");

		bgs[18] = new Variable();
		bgs[18].setCODE("{Custom5}");
		bgs[18].setNAME("自定义session5");

		return bgs;
	}

	@Override
	public String getDictList(String tableName) throws Exception {
		String strCODE = "";
		String strNAME = "";
		String strSPELL = "";
		StringBuffer resultStr = new StringBuffer();
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select * from " + tableName + " order by CODE");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sqlStr.toString());
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				strCODE = "";
				strNAME = "";
				strSPELL = "";
				strCODE = retmap.get("CODE").toString();
				strNAME = retmap.get("NAME").toString();
				strSPELL = retmap.get("SPELL").toString();

				resultStr.append("<option value='" + strCODE + "/" + strSPELL + "#" + strCODE + "'>"
						+ strCODE + " " + strNAME + "</option>");
			}
		}
		return resultStr.toString();
	}

	/**
	 * 将字符串中的单引号转换为双引号，若字符串中有单引号在保存时才不会出错
	 * @param expression 待转换的字符串
	 * @return String 已经转换好的字符串
	 */
	private String convertQuotationSingleToDouble(String expression) {
		return expression.replaceAll("\'", "''");
	}

	/**
	 * 功能或作用：根据数据库中的最大值与累加数据计算出最大的记录流水号
	 * @param strMaxNO 原最大编号
	 * @param FieldLen 数据库字段长度
	 * @param intadd 累加数
	 * @Return MaxNo 执行后返回一个MaxNo字符串
	 */
	private String getMaxFieldNoAdd(String strMaxNO, int FieldLen, int intadd) {
		String MaxNo = "";
		int LenMaxNo = 0;
		try {
			MaxNo = strMaxNO;
			if (MaxNo.length() > 0) {
				MaxNo = String.valueOf(Integer.parseInt(MaxNo) + intadd);
				LenMaxNo = MaxNo.length();
				StringBuffer addBuf = new StringBuffer();
				addBuf.append("0000000000000000000000000" + MaxNo);
				MaxNo = addBuf.toString();
			} else {
				MaxNo = "00000000000000000000000001";
				LenMaxNo = 1;
			}
			MaxNo = MaxNo.substring(25 - FieldLen + LenMaxNo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return MaxNo;
	}

}