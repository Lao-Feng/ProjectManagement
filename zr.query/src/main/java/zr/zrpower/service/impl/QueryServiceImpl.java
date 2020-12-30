package zr.zrpower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.anqueryflow.html.CreateControl;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBRow;
import zr.zrpower.common.db.DBSet;
import zr.zrpower.common.db.DBType;
import zr.zrpower.common.util.ReflectionUtil;
import zr.zrpower.common.util.StringWork;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.UserMapper;
import zr.zrpower.model.SessionUser;
import zr.zrpower.queryengine.ItemField;
import zr.zrpower.queryengine.ItemList;
import zr.zrpower.queryengine.Request;
import zr.zrpower.queryengine.mode.*;
import zr.zrpower.service.QueryService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 查询控制管理EJB实现
 * @author lwk
 *
 */
@Service
public class QueryServiceImpl implements QueryService {
	/** The QueryServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryServiceImpl.class);
	private DBEngine dbEngine;
	private Timer timerListener; // 队列侦听器
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	static Map<String, Object> CodeHashtable = new HashMap<String, Object>();
	static Map<String, Object> ColListHashtable = new HashMap<String, Object>();
	static Map<String, Object> ColQListHashtable = new HashMap<String, Object>();
	static Map<String, Object> ADDHashtable1 = new HashMap<String, Object>();
	static Map<String, Object> ADDHashtable2 = new HashMap<String, Object>();
	static Map<String, Object> ADDHashtable3 = new HashMap<String, Object>();
	static Map<String, Object> ADDHashtable4 = new HashMap<String, Object>();
	static Map<String, Object> ADDHashtable5 = new HashMap<String, Object>();
	static Map<String, Object> ADDHashtable6 = new HashMap<String, Object>();
	static Map<String, Object> ADDHashtable7 = new HashMap<String, Object>();
	static Map<String, Object> ADDHashtable8 = new HashMap<String, Object>();
	static Map<String, Object> ADDHashtable9 = new HashMap<String, Object>();

	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;

	/**
	 * 构造方法
	 */
	public QueryServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
	          int intnum = 3600;// 60分钟扫描一次
	          timerListener = new Timer();
	          timerListener.schedule(new ScanTask(), 0, intnum * 1000);
		}
		clients = 1;
    }

	@Override
	public String[] getQueryTHTitle(String ID) throws Exception {
		ItemList ResultColList = null;
		ResultColList = (ItemList) ColListHashtable.get(ID);
		if (ResultColList == null) {
			LOGGER.info("初始化ResultColList----：");
			ResultColList = getResultFieldList(ID);
			if (ResultColList != null) {
				ColListHashtable.put(ID, ResultColList);
			}
		}
		String result[] = new String[ResultColList.getItemCount()];
		try {
			for (int i = 0; i < result.length; i++) {
				result[i] = ResultColList.getItemField(i).getChineseName();
			}
		} catch (Exception ex) {
			LOGGER.error("出现异常", ex.getMessage());
		}
		return result;
	}

	@Override
	public String getQueryTitle(String ID) throws Exception {
		String strResult = "";
		QUERY_CONFIG_TABLE entityObj = new QUERY_CONFIG_TABLE();
		entityObj = getConfigTable(ID);
		strResult = entityObj.getNAME();
		
		return strResult;
	}

	@Override
	public String createQueryItem(String ID, String QITEMTYPE, SessionUser User, Request req) throws Exception {
		String rvalue = "";
		if (QITEMTYPE == null) {
			QITEMTYPE = "";
		}
		if (QITEMTYPE.length() == 0) {
			QITEMTYPE = "1";
		}
		rvalue = createQueryItem_zdy(ID, User, req, 1);
		
		return rvalue;
	}

	@Override
	public int getQueryItemRowNum(String ID, String QITEMTYPE) throws Exception {
		int ItemRowNum = 0;
		String sqlSql = "";
		if (DataBaseType.equals("1")) {// oracle数据库
			sqlSql = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.*,c.ID as sortID from bpip_table a, bpip_field b,QUERY_CONFIG_QUERYFIELD c "
					+ "where b.tableid=a.tableid and a.tablename||'.'||b.fieldname=c.field and c.fid ='" + ID + "' order by SORTID";
		}
		else if (DataBaseType.equals("2")) {// mssql数据库
			sqlSql = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.*,c.ID as sortID from bpip_table a, bpip_field b,QUERY_CONFIG_QUERYFIELD c "
					+ "where b.tableid=a.tableid and a.tablename+'.'+b.fieldname=c.field and c.fid ='" + ID + "' order by SORTID";
		}
		else if (DataBaseType.equals("3")) {// mysql数据库
			sqlSql = "select a.TABLENAME,b.*,c.ID from bpip_table a, bpip_field b,QUERY_CONFIG_QUERYFIELD c "
					+ "where b.tableid=a.tableid and concat(a.tablename,'.',b.fieldname)=c.field and c.fid ='" + ID + "' order by c.ID";
		} else {
		}
		int i = 0;
		int k = 0;
		int k1 = 0;
		ItemList itemList = new ItemList();
		itemList = (ItemList) ADDHashtable1.get(ID);
		if (itemList == null) {
			DBRow[] dbMain = this.getTableProperty(sqlSql);
			itemList = new ItemList();
			if (dbMain != null) {
				for (i = 0; i < dbMain.length; i++) {
					if (DataBaseType.equals("3")) {// mysql数据库
						dbMain[i].addColumn("TABLECNNAME", "");
						dbMain[i].addColumn("sortID", "");
						dbMain[i].Column("TABLECNNAME").setValue(dbMain[i].Column("CHINESENAME").getString());
						dbMain[i].Column("sortID").setValue(dbMain[i].Column("ID").getString());
					}
					itemList.fullFromDbRow(dbMain[i]);
				}
			}
			if (itemList != null) {
				ADDHashtable1.put(ID, itemList);
			}
		}
		try {
			if (itemList != null) {
				ItemField itemField;
				i = 0;
				int dbTypeValue;
				while (i < itemList.getItemCount()) {
					ItemRowNum++;
					itemField = itemList.getItemField(i);
					dbTypeValue = itemField.getType().getValue();
					if (dbTypeValue == DBType.DATE.getValue() || dbTypeValue == DBType.DATETIME.getValue()) {
						i++;
						k = k + 1;
					} else {
						i++;
						k1 = k1 + 1;
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("发生异常", ex.toString());
		}
		if (QITEMTYPE == null) {
			QITEMTYPE = "";
		}
		if (QITEMTYPE.length() == 0) {
			QITEMTYPE = "1";
		}
		if (QITEMTYPE.equals("1")) {
			ItemRowNum = k1 / 2;
			if (k1 > 0 && k1 % 2 == 1) {
				ItemRowNum++;
			}
			ItemRowNum = ItemRowNum + k;
		}
		if (QITEMTYPE.equals("2")) {
			ItemRowNum = k1 + k * 2;
			ItemRowNum = ItemRowNum / 3;
			if (ItemRowNum > 0 && (ItemRowNum % 3 == 1 || ItemRowNum % 3 == 2)) {
				ItemRowNum++;
			}
		}
		return ItemRowNum;
	}

	@Override
	public String getQueryResultTable(String ID, String QTABLETYPE, String queryType, SessionUser User, Request Rq,
			String P1, String P2, String P3, String P4, String P5, String ifFirstData, int CurrentPage, String swidth,
			String sheight) throws Exception {
		String revalue = "";
		if (QTABLETYPE == null) {
			QTABLETYPE = "";
		}
		if (QTABLETYPE.length() == 0) {
			QTABLETYPE = "1";
		}
		revalue = getQueryResultTable_PHONE(ID, User, Rq, P1, P2, P3, P4, P5, ifFirstData, CurrentPage);
		
		return revalue;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getQueryResultPrintTable(String strID, SessionUser User, String P1, 
			String P2, String P3, String P4, String P5) throws Exception {
		LOGGER.info("getQueryResultPrintTable开始调用...");
		long startTime = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer("");
		StringBuffer sbResult = new StringBuffer("");
		ItemList ResultColList = null;
		ResultColList = getResultFieldList(strID);
		String QueryItemContionExpression = getQueryWhere(User.getUserID());
		
		String sReturn[] = getSortStr(strID);
		String SortStr = sReturn[0];
		String ResultFieldStr = sReturn[1];
		String ResultFieldStrHid = sReturn[2];
		if (ResultFieldStrHid.trim().length() > 0) {
			ResultFieldStr = ResultFieldStr + "," + ResultFieldStrHid;
		}
		sb.append(" SELECT  " + ResultFieldStr);
		sb.append(" FROM " + getFromTableStr(strID));
		sb.append(" WHERE " + getConnectionStr(strID));
		sb.append(" And (" + getInitCondition(strID, User, P1, P2, P3, P4, P5));
		sb.append(") And " + QueryItemContionExpression);
		sb.append(" " + SortStr);

		sbResult.append("<TABLE Id=\"PrintTable\" class=\"ReportTable\" width=100%>");
		if (ResultColList != null && ResultColList.getItemCount() > 0) {
			sbResult.append("<TR><TD class=\"k1\"  style=\"border:none;\" align=\"center\" colspan="
							+ String.valueOf(ResultColList.getItemCount())
							+ ">" + getQueryTitle(strID) + "</TD>\r\n</TR>\r\n");
			sbResult.append("<TR>");
			for (int i = 0; i < ResultColList.getItemCount(); i++) {
				sbResult.append("<TD align=center >");
				sbResult.append(ResultColList.getItemField(i).getChineseName());
				sbResult.append("</TD>\r\n");
			}
			sbResult.append("</TR>\r\n");
		}
		try {
			DBSet mdbset = dbEngine.QuerySQL(sb.toString());
			if (mdbset != null) {
				// --------------初始化has表---------
				Hashtable[] NameHashtable = initCodeHasTable(mdbset, ResultColList);
				// -------------------------------------
				for (int i = 0; i < mdbset.RowCount(); i++) {
					sbResult.append("<TR>");
					if (ResultColList != null && ResultColList.getItemCount() > 0) {
						for (int j = 0; j < ResultColList.getItemCount(); j++) {
							sbResult.append("<TD >");
							sbResult.append(getValueByColumn(mdbset.Row(i), 
									ResultColList.getItemField(j), NameHashtable));
							sbResult.append("</TD>\r\n");
						}
					}
					sbResult.append("</TR>\r\n");
				}
			}
			mdbset = null;
		} catch (Exception e) {
			LOGGER.error("getQueryResultPrintTable出现异常！" + e.getMessage());
		}
		sbResult.append("</TABLE>");

		long endTime = System.currentTimeMillis();
		LOGGER.info("getQueryResultPrintTable", "执行完成，耗时：" + (endTime - startTime) + " ms.");
		return sbResult.toString();
	}

	@Override
	public String getQueryResultTableBySql(String sqlStr, int currentPage, int pageSize, String[][] columnsName,
			String chkcolumnName, String chkType, boolean eDblClick) throws Exception {
		return getQueryResultTableBySql(sqlStr, currentPage, pageSize, columnsName, chkcolumnName, chkType, eDblClick, true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getQueryResultTableBySql(String sqlStr, int currentPage, int pageSize, String[][] columnsName,
			String chkcolumnName, String chkType, boolean eDblClick, boolean ifFirtSnCol) throws Exception {
		LOGGER.info("getQueryResultTableBySql开始调用...");
		long startTime = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		StringBuffer sbResult = new StringBuffer();
		StringBuffer sbdivTiSkin = new StringBuffer();
		ItemField itemField;

		String QALIGN = "0";
		String tmpValue = "";
		int rowNum = 0;
		sb.append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM (");
		sb.append(sqlStr);
		sb.append(" ) A WHERE ROWNUM <= " + String.valueOf(currentPage * pageSize) 
				 + ") WHERE RN > " + String.valueOf((currentPage - 1) * pageSize));
		DBSet ds = dbEngine.QuerySQL(sb.toString());
		LOGGER.info("sqlStr:" + sb.toString());
		try {
			ItemList ColList = new ItemList();
			for (int i = 0; i < columnsName.length; i++) {
				// log.WriteLog("column" + String.valueOf(i) + ":" + columnsName[i][0]);
				try {
					itemField = getItemFieldByTableField(columnsName[i][0].toUpperCase());
					itemField.setColWidth(columnsName[i][1]);
					ColList.addItem(itemField);
				} catch (Exception ex) {
					LOGGER.error("addItem出现异常:" + ex.getMessage() + ";字段名:" + columnsName[i][0]);
					continue;
				}
			}
			sbResult.append("<div id=\"grid\" style=\"overflow:hidden;width:100%; height:100%;\" class=\"divAllcss\">");
			sbResult.append("<div style=\"overflow:hidden;width:100%;height:23px;position:absolute;z-index:1\">");
			sbResult.append("<div style=\"overflow:hidden;width:100%;height:23px;position:absolute;z-index:1\" class=\"divToucss\">");
			sbResult.append("<table border=0 width=\"100%\" Id=\"tableTou\" cellspacing=0 cellpadding=0>");
			sbResult.append("<TR>");
			if (ifFirtSnCol) {
				sbResult.append("<td nowrap width=\"50px\">序号</td>\r\n");
			}
			if (ColList != null && ColList.getItemCount() > 0) {
				LOGGER.info("queryResultColList != null");
				if (chkType.equals("1") || chkType.equals("2")) {
					sbResult.append("<TD nowrap width=40 ><span cansort=\"false\">选择</TD>\r\n");
					sbdivTiSkin.append("<TD nowrap width=40></TD>\r\n");
				}
				for (int i = 0; i < ColList.getItemCount(); i++) {
					sbResult.append("<TD  width=\""
									+ ColList.getItemField(i).getColWidth()
									+ "px\" nowrap fieldName=\""
									+ ColList.getItemField(i).getName()
									+ "\"><span unselectable=on sortName=\"User.column"
									+ String.valueOf(i + 1)
									+ "\" sortType=\"ASC\" cansort=\"false\">"
									+ ColList.getItemField(i).getChineseName()
									+ "<label disabled style=\"display:none\">6</label></span></td>\r\n");
					sbdivTiSkin.append("<td nowrap width=\""
							+ ColList.getItemField(i).getColWidth()
							+ "px\"></td>\r\n");
				}
			}
			sbResult.append("<td style=\"display:blank\"><span></span></td>\r\n");
			sbResult.append("</TR></Table></div>");
			if (ifFirtSnCol) {
				// --------------------
				sbResult.append("<div style=\"width:53px;position:absolute;top:0;left:0;z-index:2;\" class=\"divToucss\">");
				sbResult.append("<table width=\"100%\" border=0 cellspacing=0 cellpadding=0><tr><td nowrap width=\"50px\"><span></span></td>\r\n</tr></table>\r\n");
				sbResult.append("</div>");
				// --------------------------------------------------
			}
			if (!chkType.equals("0")) {
				sbResult.append("<div style=\"width:40px;position:absolute;top:0;left:0;z-index:2;\" class=\"divToucss\">");
				sbResult.append("<table width=\"100%\" border=0 cellspacing=0 cellpadding=0><tr><td nowrap width=\"40px\"><span>选择</span></td>\r\n</tr></table>\r\n");
				sbResult.append("</div>");
			}
			if (ifFirtSnCol == false && chkType.equals("0")) {
				sbResult.append("<div style=\"width:1px;position:absolute;top:0;left:0;z-index:2;\" class=\"divToucss\">");
				sbResult.append("<table width=\"100%\" border=0 cellspacing=0 cellpadding=0><tr><td nowrap width=\"1px\"><span></span></td>\r\n</tr></table>\r\n");
				sbResult.append("</div>");
			}
			sbResult.append("</div>");
			sbResult.append("<div style=\"overflow:auto;width:100%; height:100%;position:absolute;\" class=\"divTiSkin\">");
			sbResult.append("<table border=0 width=\"100%\" cellspacing=0 cellpadding=0 Id=\"tableTi\">");
			sbResult.append("<tr style=\"display:block\">");
			if (ifFirtSnCol) {
				sbResult.append("<td nowrap  width=\"50px\"></td>\r\n");
			}
			sbResult.append(sbdivTiSkin);
			sbResult.append("<td style=\"display:blank\"><span></span></td>\r\n");
			sbResult.append("</tr>");
			
			if (ds != null && ds.RowCount() > 0) {
				// --------------初始化has表---------
				Hashtable[] NameHashtable = initCodeHasTable(ds, ColList);
				// -------------------------------------
				LOGGER.info("ds != null");
				/*
				 * try { tableName = ds.getTableName().toUpperCase();
				 * log.WriteLog("tableName1="+tableName);
				 *
				 * }catch(Exception ex) {
				 *
				 * try{
				 *
				 * tableName = ds.Row(0).getTableName();
				 * log.WriteLog("tableName2="+tableName); }catch(Exception ex1)
				 * { log.WriteLog("出现异常："+ex1.getMessage()); } }
				 */
				for (int i = 0; i < ds.RowCount(); i++) {
					rowNum++;
					if (eDblClick) {
						sbResult.append("<TR  onclick=\"ExeOnclick()\"  ondblclick=\"ExeOndblclick()\"");
					} else {
						sbResult.append("<TR ");
					}
					if ((i + 1) % 2 == 1) {
						sbResult.append(" class=\"oddtrcss\" ");
					}
					sbResult.append(">");
					if (ifFirtSnCol) {
						sbResult.append("<td nowrap  width=\"50px\"><span></span></td>\r\n");
					}
					// ---------------选择列--------------------------------------
					if (!chkType.equals("0")) {
						try {
							tmpValue = ds.Row(i).Column(chkcolumnName).getString();
							if (tmpValue == null) {
								tmpValue = "";
							}
						} catch (Exception ex) {
							LOGGER.error("取选择列的值时出异常，" + ex.getMessage());
						}
						if (chkType.equals("1")) { // 单选
							sbResult.append("<TD nowrap width=40 align=center><input type=radio name=chkRecord  value=\""
											+ tmpValue + "\" ></TD>\r\n");
						} else if (chkType.equals("2")) { // 多选
							sbResult.append("<TD nowrap width=40 align=center><input type=checkbox name=chkRecord  value=\""
											+ tmpValue + "\"></TD>\r\n");
						}
					}
					// ----------------------------------------------------------
					if (ColList != null && ColList.getItemCount() > 0) {
						for (int j = 0; j < ColList.getItemCount(); j++) {
							QALIGN = ColList.getItemField(j).getQALIGN();
							if (QALIGN == null) {
								QALIGN = "0";
							}
							if (QALIGN.equals("0")) {
								QALIGN = "left";
							}
							if (QALIGN.equals("1")) {
								QALIGN = "center";
							}
							if (QALIGN.equals("2")) {
								QALIGN = "right";
							}
							tmpValue = getValueByColumn(ds.Row(i), ColList.getItemField(j), NameHashtable);
							sbResult.append("<td nowrap style=\"text-align:"
									+ QALIGN
									+ "\" width=\""
									+ ColList.getItemField(j).getColWidth()
									+ "px\" fieldValue=\""
									+ getValueByColumnHid(ds.Row(i), ColList.getItemField(j))
									+ "\"><span title=\"" + tmpValue + "\">"
									+ tmpValue + "</span></td>\r\n");
						}
					}
					sbResult.append("<td style=\"display:blank\"><span></span></td>\r\n</tr>\r\n");
				}
			}
			sbResult.append("</table>");
			sbResult.append("<div style=\"overflow:hidden;width:100%;height:20px;\">\r\n");
			sbResult.append("</div></div>");
			sbResult.append("<div style=\"overflow:hidden;width:100%;height:21px;position:absolute;\">\r\n");
			sbResult.append("<div style=\"overflow:hidden;width:100%;height:21px;position:absolute;\" class=\"divSumcss\">\r\n");
			sbResult.append("<table border=0 width=\"100%\" cellspacing=0 cellpadding=0><tr>\r\n");
			sbResult.append("<td nowrap width=\"50px\"><span unselectable=on cansort=\"true\">合计</span></td>\r\n");
			sbResult.append("</tr></table>");
			sbResult.append("</div>");
			sbResult.append("<div style=\"width:53px;position:absolute;top:0;left:0;z-index:2;height:21px;\" class=\"divSumcss\">\r\n");
			sbResult.append("<table width=\"100%\" border=0  cellspacing=0  cellpadding=0>\r\n");
			sbResult.append("<tr><td nowrap width=\"50px\"><span>合计</span></td>\r\n</tr>\r\n");
			sbResult.append("</table>");
			sbResult.append("</div>");
			sbResult.append("</div>");
			
			if (ifFirtSnCol) {
				sbResult.append("<div style=\"width:53px;position:absolute;height:278px;overflow:hidden\" class=\"divLeftCss\">\r\n");
				sbResult.append("<table border=0 width=\"100%\" cellspacing=0 cellpadding=0 style=margin-top:19px;>\r\n");
				for (int i = 1; i <= rowNum; i++) {
					sbResult.append("<tr><td nowrap width=\"50px\"><span unselectable=on cansort=\"true\">\r\n"
									+ String.valueOf(i) + "</span></td></tr>\r\n");
				}
				sbResult.append("</table></div>");
			}
			sbResult.append("</div>\r\n");
			sbResult.append("<script language=\"javascript\">\r\n");
			sbResult.append("var grid = new GRID();\r\n");
			sbResult.append("grid.setSubmit(false);\r\n");
			sbResult.append("function showGrid()\r\n");
			sbResult.append("{");
			sbResult.append("  this.init(\"grid\");\r\n");
			sbResult.append("}\r\n");
			sbResult.append(" grid.show = showGrid;\r\n");
			sbResult.append(" </script>\r\n");
		} catch (Exception ex) {
			LOGGER.error("出现异常ABC：" + ex.getMessage());
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("getQueryResultTableBySql执行完成，耗时：" + (endTime - startTime) + " ms.");
		ds = null;
		return sbResult.toString();
	}

	@Override
	public String createPageNavigateBySql(String sqlStr, int currentPage, int pageSize) throws Exception {
		LOGGER.info("createPageNavigateBySql开始调用...");
		long startTime = System.currentTimeMillis();
		long endTime;
		String resultStr = "";
		int intRowCount;
		// sqlStr格式为=SELECT count(*) as RowCount Where ...
		DBSet ds = dbEngine.QuerySQL(sqlStr);
		if (ds == null) {
			intRowCount = 0;
		} else {
			intRowCount = ds.Row(0).Column("RCount").getInteger();
		}
		resultStr = createPageMenu(pageSize, intRowCount, currentPage);
		endTime = System.currentTimeMillis();
		LOGGER.info("createPageNavigateBySql执行完成，耗时：" + (endTime - startTime) + " ms.");
		ds = null;
		return resultStr;
	}

	@Override
	public String[] createPageNavigate(String ID, SessionUser User, String P1, String P2, String P3, String P4,
			String P5, int currentPage) throws Exception {
		LOGGER.info("createPageNavigate开始调用...");
		int page_num=User.getUserPageSize();
		if(page_num>15){
			page_num=15;
		}
		long startTime = System.currentTimeMillis();
		long startTime1;
		long endTime;
		String result[] = new String[2];
		String resultStr = "";
		int intRowCount;
		StringBuffer sbSql = new StringBuffer("");
		
		String MainTablePrimaryStr = getMainTablePrimaryStr(ID);
		String GetResultFieldStrHid = getResultFieldStrHid(ID);
		String GetResultFieldStr = getResultFieldStr(ID);
		if (GetResultFieldStr.indexOf(MainTablePrimaryStr) == -1
				&& GetResultFieldStrHid.indexOf(MainTablePrimaryStr) == -1) {
			if (GetResultFieldStr.trim().length() > 0) {
				GetResultFieldStr = MainTablePrimaryStr + "," + GetResultFieldStr;
			} else {
				GetResultFieldStr = MainTablePrimaryStr;
			}
		}
		String GetFromTableStr = getFromTableStr(ID);
		String GetConnectionStr = getConnectionStr(ID);
		String GetInitCondition = getInitCondition(ID, User, P1, P2, P3, P4, P5);
		String QueryItemContionExpression = getQueryWhere(User.getUserID());
		if (DataBaseType.equals("2") || DataBaseType.equals("3")) {// mssql和mysql数据库
			sbSql.append("SELECT Distinct " + GetResultFieldStr);
		} else {
			sbSql.append(" SELECT  count(*) as RCount FROM ");
			sbSql.append(" ( SELECT Distinct " + GetResultFieldStr);
		}
		if (GetResultFieldStrHid.trim().length() > 0) {
			sbSql.append("," + GetResultFieldStrHid);
		}
		sbSql.append(" FROM " + GetFromTableStr);
		sbSql.append(" WHERE " + GetConnectionStr);
		sbSql.append(" And " + GetInitCondition);
		if (DataBaseType.equals("2") || DataBaseType.equals("3"))// mssql和mysql数据库
		{
			sbSql.append(" And " + QueryItemContionExpression);
		} else {
			sbSql.append(" And " + QueryItemContionExpression + " ) ");
		}
		startTime1 = System.currentTimeMillis();
		DBSet ds = dbEngine.QuerySQL(sbSql.toString());
		endTime = System.currentTimeMillis();
		LOGGER.info("执行数据查询SQL：" + (endTime - startTime1) + " ms.");
		if (ds == null) {
			intRowCount = 0;
		} else {
			if (DataBaseType.equals("2") || DataBaseType.equals("3")) {// mssql和 mysql数据库
				intRowCount = ds.RowCount();
			} else {
				intRowCount = ds.Row(0).Column("RCount").getInteger();
			}
		}
		startTime1 = System.currentTimeMillis();
		resultStr = createPageMenu(page_num, intRowCount,currentPage);
		endTime = System.currentTimeMillis();
		
		endTime = System.currentTimeMillis();
		LOGGER.info("createPageNavigate执行完成，耗时：" + (endTime - startTime) + " ms.");
		ds = null;
		result[0] = resultStr;
		result[1] = String.valueOf(intRowCount);
		
		return result;
	}

	@Override
	public String createButtonNavigate(String ID, String QTABLETYPE, 
			SessionUser User, String BUTTONIDS) throws Exception {
		StringBuffer sb = new StringBuffer("");
		QUERY_CONFIG_BUTTON entityList[] = null;
		String strTS = "";
		String strROLES = "";
		String BUTTONIDS1 = "";
		if (QTABLETYPE == null) {
			QTABLETYPE = "";
		}
		if (QTABLETYPE.length() == 0) {
			QTABLETYPE = "1";
		}
		try {
			String sqlStr = "select ROLEID from BPIP_USER_ROLE where USERID='"+User.getUserID()+"'";
			List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(sqlStr);
			int length1 = retlist1 != null ? retlist1.size() : 0;
			if (length1 > 0) {
				for (int i = 0; i < length1; i++) {
					Map<String, Object> retmap1 = retlist1.get(i);
					if (i == 0) {
						strROLES = retmap1.get("ROLEID").toString();
					} else {
						strROLES = strROLES + "," + retmap1.get("ROLEID").toString();
					}
				}
			}
			//String isC = null;
			if (BUTTONIDS.length() > 2) {
				BUTTONIDS1 = "'" + BUTTONIDS.replaceAll(",", "','") + "'";
				sqlStr = "Select a.* from QUERY_CONFIG_BUTTON a, QUERY_CONFIG_BRELATION b Where a.ID=b.BID and b.FID='"
						+ ID + "' and a.ID in (" + BUTTONIDS1 + ") order by b.ID";
			} else {
				sqlStr = "Select a.* from QUERY_CONFIG_BUTTON a, QUERY_CONFIG_BRELATION b Where a.ID=b.BID and b.FID='"
						+ ID + "' order by b.ID";
			}
			List<Map<String, Object>> retlist2 = userMapper.selectListMapExecSQL(sqlStr);
			int length2 = retlist2 != null ? retlist2.size() : 0;
			if (length2 == 0) {
				entityList = null;
			} else {// length2 > 0
				entityList = new QUERY_CONFIG_BUTTON[length2];
				for (int i = 0; i < length2; i++) {
					Map<String, Object> retmap2 = retlist2.get(i);
					entityList[i] = (QUERY_CONFIG_BUTTON) ReflectionUtil.convertMapToBean(retmap2, QUERY_CONFIG_BUTTON.class);
				}
			}
		} catch (Exception e) {
			entityList = null;
		}
		if (entityList != null) {
			for (int i = 0; i < entityList.length; i++) {
				strTS = entityList[i].getROLEIDS();
				if (strTS == null) {
					strTS = "";
				}
				String rss[] = strROLES.split(",");
				String isrss = "0";
				for (int yy = 0; yy < rss.length; yy++) {
					if (strTS.indexOf(rss[yy]) > -1) {
						isrss = "1";
						break;
					}
				}
				if ((strTS.length() > 0 && isrss.equals("1")) || strTS.length() == 0) {
					String buttName = entityList[i].getRUNNAME();
					if (buttName.trim().length() >= 4) {
						buttName = buttName.trim().substring(0, 4);
					}
					if (QTABLETYPE.equals("1")) {
						// "javascript:parseButtonJs(\""+createButtonScript(entityList[i], ID)+"\")"
						sb.append("<li class=\"am-modal-actions-header\"><a onclick=javascript:parseButtonJs(\""
										+ createButtonScript(entityList[i], ID)
										+ "\") class=\"am-btn am-btn-primary am-radius\">"
										+ buttName + "</a></li>\r\n");
					} else {
						sb.append("<li class=\"am-modal-actions-header\"><a onclick=javascript:parseButtonJs_value(\""
										+ createButtonScript(entityList[i], ID)
										+ "\") class=\"am-btn am-btn-primary am-radius\">"
										+ buttName + "</a></li>\r\n");
					}
				}
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String[][] getExcelData(String ID, SessionUser User, String P1, String P2, String P3, 
			String P4, String P5, String strPage, String crow) throws Exception {
		ItemList ResultColList = null;
		ResultColList = getResultFieldList(ID);
		String QueryItemContionExpression = getQueryWhere(User.getUserID());
		String[][] result = null;
		String tmpStr;
		String sb = "";
		int getrow = 0;
		int cpage = 0;
		int sumrow = 0;
		int erow = 0;
		try {
			cpage = Integer.parseInt(strPage);
			sumrow = Integer.parseInt(crow);
		} catch (NumberFormatException ex1) {
			cpage = 0;
			sumrow = 0;
		}
		String sReturn[] = getSortStr(ID);
		String SortStr = sReturn[0];
		String ResultFieldStr = sReturn[1];
		String ResultFieldStrHid = sReturn[2];
		if (ResultFieldStrHid.trim().length() > 0) {
			ResultFieldStr = ResultFieldStr + "," + ResultFieldStrHid;
		}
		String FromTableStr = getFromTableStr(ID);
		String ConnectionStr = getConnectionStr(ID);
		String InitCondition = getInitCondition(ID, User, P1, P2, P3, P4, P5);
		if (crow.length() > 0) {
			try {
				getrow = Integer.parseInt(crow);
			} catch (NumberFormatException ex) {
				getrow = 0;
			}
		} else {
			getrow = 0;
		}
		// 分页---
		if (getrow > 5000) {
			if (DataBaseType.equals("1")) {// oracle数据库
				sb = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (";
			}
			sb = sb + " SELECT distinct " + ResultFieldStr + " FROM "
					+ FromTableStr + " WHERE " + ConnectionStr + " And "
					+ InitCondition + " And " + QueryItemContionExpression
					+ " " + SortStr;
			if (cpage * 5000 > sumrow) {
				erow = sumrow;
			} else {
				erow = cpage * 5000;
			}
			if (DataBaseType.equals("1") || DataBaseType.equals("2")) {// oracle和mssql数据库
				sb = sb + ") A WHERE ROWNUM <= " + String.valueOf(erow)
						+ ") WHERE RN > " + String.valueOf((cpage - 1) * 5000);
			}
			else if (DataBaseType.equals("3")) {// MySQL数据库
				int offset = (cpage - 1) * 5000;
				if (offset < 0) { offset = 0; }
				sb = sb + " LIMIT " + String.valueOf(offset) + "," + String.valueOf(erow);
			} else {
			}
		} else {
			// 不分页----
			sb = " SELECT distinct " + ResultFieldStr + " FROM " + FromTableStr
					+ " WHERE " + ConnectionStr + " And " + InitCondition
					+ " And " + QueryItemContionExpression + " " + SortStr;
		}
		DBSet mdbset = dbEngine.QuerySQL(sb);
		if (mdbset != null) {
			// --------------初始化has表---------
			Hashtable NameHashtable[] = initCodeHasTable(mdbset, ResultColList);
			// -------------------------------------
			result = new String[mdbset.RowCount()][ResultColList.getItemCount()];
			for (int i = 0; i < mdbset.RowCount(); i++) {
				for (int j = 0; j < ResultColList.getItemCount(); j++) {
					tmpStr = getValueByColumn(mdbset.Row(i), ResultColList.getItemField(j), NameHashtable);
					if (tmpStr.equals("&nbsp")) {
						tmpStr = "";
					}
					result[i][j] = tmpStr;
				}
			}
		}
		mdbset = null;
		return result;
	}

	@Override
	public String createButtonScript(QUERY_CONFIG_BUTTON buttonObj, String ID) throws Exception {
		LOGGER.info("CreateButtonScript开始调用...");
		long startTime = System.currentTimeMillis();
		QUERY_CONFIG_PARAMETER parameterList[] = null;
		String resultStr = "";
		StringBuffer sb = new StringBuffer();
		if (buttonObj != null) {
			try {
				resultStr = buttonObj.getPATHORJS();
				parameterList = getParameterList(" FID='"+ID+"' And BID='"+buttonObj.getID()+"'");
				if (parameterList != null && parameterList.length > 0) {
					if (buttonObj.getPATHORJS().indexOf("(") == -1) {
						for (int i = 0; i < parameterList.length; i++) {
							if (sb.toString().trim().length() > 0) {
								sb.append("&");
							}
							sb.append(parameterList[i].getNAME()+"=["+parameterList[i].getFIELD()+"]");
						}
						if (buttonObj.getPATHORJS().indexOf("?") > -1) {
							resultStr += "&" + sb.toString();
						} else {
							resultStr += "?" + sb.toString();
						}
					} else {
						for (int i = 0; i < parameterList.length; i++) {
							resultStr = resultStr.replaceAll("\\{"
									+ parameterList[i].getNAME() + "\\}", "["
									+ parameterList[i].getFIELD() + "]");
						}
					}
				}
			} catch (Exception ex) {
				LOGGER.error("出现异常", ex.getMessage());
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("createButtonScript执行完成，耗时：" + (endTime - startTime) + " ms.");
		return resultStr;
	}

	@Override
	public String[][] getDocNameArray() throws Exception {
		LOGGER.info("getDocNameArray开始调用...");
		long startTime = System.currentTimeMillis();
		String[][] result = null;
		String strSql = "Select * from BPIP_TABLE Where TABLETYPE = '3' Order By CHINESENAME";
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			result = new String[length][2];
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				result[i][0] = retmap.get("TABLENAME").toString();
				result[i][1] = retmap.get("CHINESENAME").toString();
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("getDocNameArray执行完成，耗时：" + (endTime - startTime) + " ms.");
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getIsmustJs(String ID, String ISNULL) throws Exception {
		String sqlSql = "";
		String ISMUST = "0";
		String ISDAY = "0";
		Hashtable Hashtable1 = new Hashtable();// 是否必填对照表
		Hashtable Hashtable2 = new Hashtable();// 日期范围对照表
		String sb = "function submitquery(){\r\n";
		sb = sb + "var isnull='0';\r\n";
		
		if (DataBaseType.equals("1")) {// oracle数据库
			sqlSql = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.*,c.ID as sortID,c.ISMUST,c.ISDAY,c.FIELD from bpip_table a, bpip_field b,QUERY_CONFIG_QUERYFIELD c "
					+ "where b.tableid=a.tableid and a.tablename||'.'||b.fieldname=c.field and c.fid ='" + ID + "' order by SORTID";
		}
		else if (DataBaseType.equals("2")) {// mssql数据库
			sqlSql = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.*,c.ID as sortID,c.ISMUST,c.ISDAY,c.FIELD from bpip_table a, bpip_field b,QUERY_CONFIG_QUERYFIELD c "
					+ "where b.tableid=a.tableid and a.tablename+'.'+b.fieldname=c.field and c.fid ='" + ID + "' order by SORTID";
		}
		else if (DataBaseType.equals("3")) {// mysql数据库
			sqlSql = "select a.TABLENAME,b.*,c.ID,c.ISMUST,c.ISDAY,c.FIELD from bpip_table a, bpip_field b,QUERY_CONFIG_QUERYFIELD c   where b.tableid=a.tableid and concat(a.tablename,'.',b.fieldname)=c.field and c.fid ='"
					+ ID + "' order by c.ID";
		} else {
		}
		int i = 0;
		ItemList itemList = new ItemList();
		itemList = (ItemList) ADDHashtable6.get(ID);
		if (itemList == null) {
			itemList = new ItemList();
			DBRow[] dbMain = this.getTableProperty(sqlSql);
			if (dbMain != null) {
				for (i = 0; i < dbMain.length; i++) {
					if (DataBaseType.equals("3")) {// mysql数据库
						dbMain[i].addColumn("TABLECNNAME", "");
						dbMain[i].addColumn("sortID", "");
						dbMain[i].Column("TABLECNNAME").setValue(dbMain[i].Column("CHINESENAME").getString());
						dbMain[i].Column("sortID").setValue(dbMain[i].Column("ID").getString());
					}
					Hashtable1.put(ID + "." + dbMain[i].Column("FIELD").getString(), dbMain[i].Column("ISMUST").getString());
					Hashtable2.put(ID + "." + dbMain[i].Column("FIELD").getString(), dbMain[i].Column("ISDAY").getString());
					itemList.fullFromDbRow(dbMain[i]);
				}
			}
			if (itemList != null) {
				ADDHashtable6.put(ID, itemList);
			}
		}
		try {
			if (itemList != null) {
				ItemField itemField;
				i = 0;
				int dbTypeValue;
				String controlName = "";
				while (i < itemList.getItemCount()) {
					itemField = itemList.getItemField(i);
					dbTypeValue = itemField.getType().getValue();
					// 得到是否必填
					ISMUST = (String) Hashtable1.get(ID + "." + itemField.getName());
					if (ISMUST == null) {
						ISMUST = "0";
					}
					if (dbTypeValue == DBType.DATE.getValue() || dbTypeValue == DBType.DATETIME.getValue()
							|| dbTypeValue == DBType.LONG.getValue() || dbTypeValue == DBType.FLOAT.getValue()) {
						controlName = itemField.getName();
						if (ISNULL.equals("0")) {
							sb = sb + "if(document.getElementById('" + controlName + "_Begin')"
									+ ".value.length>0 && document.getElementById('"
									+ controlName + "_End')"
									+ ".value.length>0){\r\n";
							sb = sb + "isnull='1';\r\n";
							sb = sb + "}\r\n";
						}
						if (ISMUST.equals("1")) {
							sb = sb + "if(document.getElementById('" + controlName + "_Begin')"
									+ ".value.length<1){\r\n";
							sb = sb + "alert('[" + itemField.getChineseName()
									+ "]不能为空，请填写！');\r\n";
							sb = sb + "document.getElementById('" + controlName
									+ "_Begin')" + ".focus();\r\n";
							sb = sb + "return;}\r\n";
							sb = sb + "if(document.getElementById('"
									+ controlName + "_End')"
									+ ".value.length<1){\r\n";
							sb = sb + "alert('[" + itemField.getChineseName()
									+ "]不能为空，请填写！');\r\n";
							sb = sb + "document.getElementById('" + controlName
									+ "_End')" + ".focus();\r\n";
							sb = sb + "return;}\r\n";
						}
						if (dbTypeValue == DBType.DATE.getValue() || dbTypeValue == DBType.DATETIME.getValue()) {
							ISDAY = (String) Hashtable2.get(ID + "." + itemField.getName());
							if (ISDAY == null) {
								ISDAY = "0";
							}
							if (ISDAY.length() == 0) {
								ISDAY = "0";
							}
							if (!ISDAY.equals("0") && ISDAY.length() > 0) {
								sb = sb + "if(document.getElementById('" + controlName + "_Begin')"
										+ ".value.length>0 && document.getElementById('"
										+ controlName + "_End')"
										+ ".value.length>0){\r\n";
								sb = sb + "var sday = " + ISDAY + ";\r\n";
								sb = sb + "var sDate1 = document.getElementById('"
										+ controlName + "_Begin')"
										+ ".value;\r\n";
								sb = sb + "var sDate2 = document.getElementById('"
										+ controlName + "_End')"
										+ ".value;\r\n";
								sb = sb + "var aDate,oDate1,oDate2,iDays;\r\n";
								sb = sb + "aDate = sDate1.split('-');\r\n";
								sb = sb + "oDate1 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);\r\n";
								sb = sb + "aDate = sDate2.split('-');\r\n";
								sb = sb + "oDate2 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);\r\n";
								sb = sb + "iDays = parseInt(Math.abs(oDate1 - oDate2)/1000/60/60/24);\r\n";
								sb = sb + "if (iDays>sday){\r\n";
								sb = sb + "alert('["
										+ itemField.getChineseName()
										+ "]超出规定的日期范围[" + ISDAY + "]天！');\r\n";
								sb = sb + "document.getElementById('"
										+ controlName + "_End')"
										+ ".focus();\r\n";
								sb = sb + "return;}\r\n";
								sb = sb + "}\r\n";
							}
						}
						i++;
					} else {
						if (ISNULL.equals("0")) {
							sb = sb + "if(document.getElementById('" + itemField.getName()
									+ "').value.length>0){\r\n";
							sb = sb + "isnull='1';\r\n";
							sb = sb + "}\r\n";
						}
						if (ISMUST.equals("1")) {
							sb = sb + "if(document.getElementById('" + itemField.getName()
									+ "').value.length<1){\r\n";
							sb = sb + "alert('[" + itemField.getChineseName()
									+ "]不能为空，请填写！');\r\n";
							sb = sb + "document.getElementById('"
									+ itemField.getName() + "').focus();\r\n";
							sb = sb + "return;}\r\n";
						}
						i++;
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("发生异常", ex.toString());
		}
		if (ISNULL.equals("0")) {
			sb = sb + "if(isnull=='0'){\r\n";
			sb = sb + "alert('查询条件不能为空！');\r\n";
			sb = sb + "return;\r\n";
			sb = sb + "}\r\n";
		}
		sb = sb + "document.recordfrm.submit();}\r\n";
		
		return sb;
	}

	@Override
	public String[] getIsNumber(String ID) throws Exception {
		String result[] = null;
		String ISNUMBER = "";
		String sqlSql = "select ISNUMBER from QUERY_CONFIG_SHOWFIELD where FID='"+ID+"' order by ID";
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sqlSql);
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			result = new String[length];
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				ISNUMBER = retmap.get("ISNUMBER").toString();
				if (ISNUMBER == null) {
					ISNUMBER = "0";
				}
				if (ISNUMBER.length() == 0) {
					ISNUMBER = "0";
				}
				result[i] = ISNUMBER;
			}
		}
		return result;
	}

	/**
	 * 得到个人查询条件
	 * @param userID
	 * @return
	 * @throws Exception 
	 */
	private String getQueryWhere(String userID) throws Exception {
		String strReturn = "";
		String strSql = "select TMP from QUERY_TMP where USERID='" + userID + "'";
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
		if (retlist != null && retlist.size() > 0) {
			if (retlist.get(0) != null && retlist.get(0).get("TMP") != null) {
				strReturn = retlist.get(0).get("TMP").toString();
			}
		}
		return strReturn;
	}

	@Override
	public String[][] getCodeTable(String NAME) throws Exception {
		String[][] result = null;
		result = (String[][]) CodeHashtable.get(NAME);
		String sqlValue = "";
		int getnum = 0;
		if (result == null) {
			// 用户表
			if (NAME.equals("BPIP_USER")) {
				sqlValue = "Select count(USERID) as NUM From BPIP_USER Where USERSTATE='0'";
				Integer retNUM = userMapper.selectIntExecSQL(sqlValue);
				getnum = retNUM != null ? retNUM : 0;
				if (getnum > 2000) {
					result = new String[1][2];
					result[0][0] = "000000000000";
					result[0][1] = "无";
					CodeHashtable.put(NAME, result);
				} else {
					sqlValue = "Select USERID,NAME From BPIP_USER Where USERSTATE='0' order by USERID";
					List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(sqlValue);
					int length1 = retlist1 != null ? retlist1.size() : 0;
					if (length1 > 0) {
						result = new String[length1][2];
						for (int i = 0; i < length1; i++) {
							Map<String, Object> retmap1 = retlist1.get(i);
							result[i][0] = retmap1.get("USERID").toString();
							result[i][1] = retmap1.get("NAME").toString();
						}
						if (result != null) {
							CodeHashtable.put(NAME, result);
						}
					}
				}
			}
			// 单位表
			if (NAME.equals("BPIP_UNIT")) {
				sqlValue = "Select count(UNITID) as NUM From BPIP_UNIT Where STATE!='1'";
				Integer retNUM = userMapper.selectIntExecSQL(sqlValue);
				if (retNUM != null) {
					getnum = retNUM;
				}
				if (getnum > 2000) {
					result = new String[1][2];
					result[0][0] = "000000000000";
					result[0][1] = "无";
					CodeHashtable.put(NAME, result);
				} else {
					sqlValue = "Select UNITID,UNITNAME From BPIP_UNIT Where STATE!='1' ORDER BY UNITID";
					List<Map<String, Object>> retlist2 = userMapper.selectListMapExecSQL(sqlValue);
					int length2 = retlist2 != null ? retlist2.size() : 0;
					if (length2 > 0) {
						result = new String[length2][2];
						for (int i = 0; i < length2; i++) {
							Map<String, Object> retmap2 = retlist2.get(i);
							result[i][0] = retmap2.get("UNITID").toString();
							result[i][1] = retmap2.get("UNITNAME").toString();
						}
						if (result != null) {
							CodeHashtable.put(NAME, result);
						}
					}
				}
			}
			// 单位表
			if (!NAME.equals("BPIP_USER") && !NAME.equals("BPIP_UNIT")) {
				sqlValue = "Select CODE,NAME From " + NAME + " where ISSHOW!='1' or ISSHOW is null order by CODE";
				List<Map<String, Object>> retlist3 = userMapper.selectListMapExecSQL(sqlValue);
				int length3 = retlist3 != null ? retlist3.size() : 0;
				if (length3 == 0) {
					sqlValue = "Select CODE,NAME From " + NAME + " order by CODE";
					retlist3 = userMapper.selectListMapExecSQL(sqlValue);
					length3 = retlist3 != null ? retlist3.size() : 0;
				}
				if (length3 > 0) {
					result = new String[length3][2];
					for (int i = 0; i < length3; i++) {
						Map<String, Object> retmap3 = retlist3.get(i);
						result[i][0] = retmap3.get("CODE").toString();
						result[i][1] = retmap3.get("NAME").toString();
					}
					if (result != null) {
						CodeHashtable.put(NAME, result);
					}
				}
			}
		}
		return result;
	}

	@Override
	public QUERY_CONFIG_TABLE getConfigTable(String ID) throws Exception {
		QUERY_CONFIG_TABLE entityObj = null;
		StringBuffer strSql = new StringBuffer();
		try {
			strSql.append("Select * From QUERY_CONFIG_TABLE Where ID='"+ID.trim()+"'");
			Map<String, Object> retmap = userMapper.selectMapExecSQL(strSql.toString());
			if (retmap != null && retmap.size() > 0) {
				entityObj = (QUERY_CONFIG_TABLE) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_TABLE.class);
			}
		} catch (Exception e) {
			LOGGER.error("调用方法GetConfigTable出现异常" + e.toString());
		}
		return entityObj;
	}

	@Override
	public QUERY_CONFIG_BUTTON getButtonByID(String id) throws Exception {
		QUERY_CONFIG_BUTTON bp = new QUERY_CONFIG_BUTTON();
		StringBuffer strSql = new StringBuffer();
		try {
			strSql.append("Select * From QUERY_CONFIG_BUTTON Where ID='" + id.trim() + "'");
			Map<String, Object> retmap = userMapper.selectMapExecSQL(strSql.toString());
			if (retmap == null || retmap.size() <= 0) {
				return null;
			} else {// retmap != null
				bp = (QUERY_CONFIG_BUTTON) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_BUTTON.class);
			}
		} catch (Exception e) {
			LOGGER.error("getButtonByID方法出现异常：" + e.toString());
		}
		return bp;
	}

	/**
	 * 根据配置ID等到结果列表
	 * @param FID 查询配置ID
	 * @return String
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ItemList getResultFieldList(String ID) {
		StringBuffer sb = new StringBuffer();
		ItemList ResultList = null;
		QUERY_CONFIG_SHOWFIELD entityList[] = null;
		String strSql = null;
		try {
			if (ID.trim().length() > 0) {
				strSql = "Select * from QUERY_CONFIG_SHOWFIELD WHERE FID='"+ID+"' AND ISSHOW='1' ORDER BY ID";
				List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
				int length = retlist != null ? retlist.size() : 0;
				if (length > 0) {
					entityList = new QUERY_CONFIG_SHOWFIELD[length];
					for (int i = 0; i < length; i++) {
						Map<String, Object> retmap = retlist.get(i);
						entityList[i] = (QUERY_CONFIG_SHOWFIELD) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_SHOWFIELD.class);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("getResultFieldStr出现异常！" + e.getMessage());
			entityList = null;
		}
		if (entityList != null) {
			ResultList = new ItemList();
			String[] tableFieldNames = new String[entityList.length];
			Hashtable tmpHashTable = new Hashtable();
			Hashtable tmpHashTable1 = new Hashtable();
			for (int i = 0; i < entityList.length; i++) {
				tableFieldNames[i] = entityList[i].getFIELD();
				if (tmpHashTable.get(entityList[i].getFIELD()) == null) {
					tmpHashTable.put(entityList[i].getFIELD(), entityList[i].getCOLWIDTH());
					tmpHashTable1.put(entityList[i].getFIELD(), entityList[i].getQALIGN());
				}
				if (sb.toString().trim().length() == 0) {
					sb.append(entityList[i].getFIELD());
				} else {
					sb.append("," + entityList[i].getFIELD());
				}
			}
			try {
				ResultList = getItemResultTableFieldShow(ID);
				for (int i = 0; i < ResultList.getItemCount(); i++) {
					if (tmpHashTable.get(ResultList.getItemField(i).getName()) != null) {
						ResultList.getItemField(i).setColWidth(
								String.valueOf(tmpHashTable.get(ResultList.getItemField(i).getName())));
					}
					if (tmpHashTable1.get(ResultList.getItemField(i).getName()) != null) {
						ResultList.getItemField(i).setQALIGN(
								String.valueOf(tmpHashTable1.get(ResultList.getItemField(i).getName())));
					}
				}
			} catch (Exception ex) {
				LOGGER.error("GetResultFieldStr：" + ex.getMessage());
			}
		}
		return ResultList;
	}

	/**
	 * 根据表名.字段名获取ItemField对象;
	 * @param tableField 表名.字段名的格式
	 * @param Fid 查询配置ID
	 * @return ItemField
	 */
	private ItemList getItemResultTableFieldShow(String Fid) {
		String sqlStr = "";
		if (DataBaseType.equals("1")) {// oracle数据库
			sqlStr = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.*,C.ID As CID from bpip_table a, bpip_field b,QUERY_CONFIG_SHOWFIELD c "
					+ "where b.tableid=a.tableid and a.tablename||'.'||b.fieldname=c.Field And  c.ISSHOW='1' And c.FID='" + Fid + "' order By CID";
		}
		else if (DataBaseType.equals("2")) {// mssql数据库
			sqlStr = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.*,C.ID As CID from bpip_table a, bpip_field b,QUERY_CONFIG_SHOWFIELD c "
					+ "where b.tableid=a.tableid and a.tablename+'.'+b.fieldname=c.Field And  c.ISSHOW='1' And c.FID='" + Fid + "' order By CID";
		}
		else if (DataBaseType.equals("3")) {// mysql数据库
			sqlStr = "select a.TABLENAME,b.*,C.ID from bpip_table a, bpip_field b,QUERY_CONFIG_SHOWFIELD c "
					+ "where b.tableid=a.tableid and concat(a.tablename,'.',b.fieldname)=c.Field And c.ISSHOW='1' And c.FID='" + Fid + "' order By C.ID";
		} else {
		}
		ItemList itemList = new ItemList();
		itemList = (ItemList) ADDHashtable2.get(Fid);
		if (itemList == null) {
			DBRow[] dbMain = this.getTableProperty(sqlStr);
			itemList = new ItemList();
			if (dbMain != null) {
				for (int i = 0; i < dbMain.length; i++) {
					if (DataBaseType.equals("3")) {// mysql数据库
						dbMain[i].addColumn("TABLECNNAME", "");
						dbMain[i].addColumn("CID", "");
						dbMain[i].Column("TABLECNNAME").setValue(
								dbMain[i].Column("CHINESENAME").getString());
						dbMain[i].Column("CID").setValue(
								dbMain[i].Column("ID").getString());
					}
					itemList.fullFromDbRow(dbMain[i]);
				}
			}
			if (itemList != null) {
				ADDHashtable2.put(Fid, itemList);
			}
		}
		return itemList;
	}

	/**
	 * 获取表字段属性
	 * @param TableName
	 * @return dbRow
	 */
	private DBRow[] getTableProperty(String sqlSql) {
		DBRow[] dbProperty = null;
		DBSet dsTable = dbEngine.QuerySQL(sqlSql);
		if (dsTable != null) {
			dbProperty = new DBRow[dsTable.RowCount()];
			for (int i = 0; i < dsTable.RowCount(); i++) {
				dbProperty[i] = new DBRow();
				dbProperty[i] = dsTable.Row(i);
				dbProperty[i].setTableName(dsTable.Row(i).Column("TABLENAME").getString());
			}
		}
		dsTable = null;
		return dbProperty;
	}

	/***
	 * 生成手机端表格
	 * @param ID
	 * @param User
	 * @param Rq
	 * @param P1
	 * @param P2
	 * @param P3
	 * @param P4
	 * @param P5
	 * @param ifFirstData
	 * @param CurrentPage
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	private String getQueryResultTable_PHONE(String ID, SessionUser User, Request Rq, String P1, String P2, 
			String P3, String P4, String P5, String ifFirstData, int CurrentPage) throws Exception {
		LOGGER.info("GetQueryResultTable_PHONE开始调用...");
		//手机端每页最多15条记录
		int page_num=User.getUserPageSize();
		if(page_num>15){
			page_num=15;
		}
		ItemList ResultColList = null;
		ItemList ResultColListHid = null;
		ResultColList = getResultFieldList(ID);
		ResultColListHid = getResultFieldListHid(ID);
		
		long startTime = System.currentTimeMillis();
		long startTime1 = System.currentTimeMillis();
		long endTime1 = System.currentTimeMillis();
//		int rowNum = 0;
		DBSet mdbset;
		String tmpValue = "";
		String QALIGN = "0";
		
		StringBuffer sb = new StringBuffer("");
		StringBuffer sbResult = new StringBuffer("");
		
		String FromTableStr = getFromTableStr(ID);
		String ConnectionStr = getConnectionStr(ID);
		String strPID = getTablePID(ID);// 得到主表的主键
		String InitCondition = getInitCondition(ID, User, P1, P2, P3, P4, P5);
		String QueryItemContionExpression = getQueryItemContionExpression(ID,User, Rq, P1, P2, P3, P4, P5);
		
		setQueryWhere(User.getUserID(), QueryItemContionExpression);
		
		String sReturn[] = getSortStr(ID);
		String SortStr = sReturn[0];
		String ResultFieldStr = sReturn[1];
		String ResultFieldStrHid = sReturn[2];
		if (DataBaseType.equals("2") || DataBaseType.equals("1")) {// oracle数据库
			sb.append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM (");
			sb.append(" SELECT Distinct " + ResultFieldStr);
		}
		else if (DataBaseType.equals("3")) {// MySQL数据库
			sb.append(" SELECT Distinct " + ResultFieldStr);
		} else {
		}
		if (ResultFieldStrHid.trim().length() > 0) {
			sb.append("," + ResultFieldStrHid);
		}
		sb.append(" FROM " + FromTableStr);
		sb.append(" WHERE " + ConnectionStr);
		sb.append(" And (" + InitCondition);
		sb.append(") And " + QueryItemContionExpression);
		sb.append(" " + SortStr);

		if (DataBaseType.equals("2") || DataBaseType.equals("1")) {// oracle数据库
			sb.append(") A WHERE ROWNUM <= " + String.valueOf(CurrentPage* page_num)
					+ ") WHERE RN > " + String.valueOf((CurrentPage - 1)* page_num));
		}
		else if (DataBaseType.equals("3")) {// MySQL数据库
			int offset = (CurrentPage - 1) * page_num;
			if (offset < 0) { offset = 0; }
			sb.append(" LIMIT " + String.valueOf(offset) + "," + String.valueOf(page_num));
		}
		LOGGER.info("strSqleee", sb.toString());
		//定义数组
		StringBuffer vale_list = new StringBuffer();
		//定义js方法
		sbResult.append("<script type=\"text/javascript\">\r\n");
		sbResult.append("var title_zmc_w = new Array();//标题名称_cn\r\n");
		sbResult.append("var title_ymc_w = new Array();//标题名称_en\r\n");
		sbResult.append("var title_with = new Array();//标题宽度\r\n");
		sbResult.append("var body_tr_key = new Array();//数据主键\r\n");
		sbResult.append("var title_alige = new Array();//列表位置\r\n");
		sbResult.append("var value_array = new Array();  //二维数组值\r\n");
		int resultItemCount = ResultColList != null ? ResultColList.getItemCount() : 0;
		if (resultItemCount > 0) {
//			int cuntNum = ResultColList.getItemCount();
			for (int i = 0; i < ResultColList.getItemCount(); i++) {
				sbResult.append("title_zmc_w["+i+"]=\""+ResultColList.getItemField(i).getChineseName()+"\";\r\n");
				sbResult.append("title_ymc_w["+i+"]=\""+ResultColList.getItemField(i).getName()+"\";\r\n");
				sbResult.append("title_with["+i+"]="+ResultColList.getItemField(i).getColWidth()+";\r\n");
			}
		}
		//定义显示的长度有多少列
		int w_num = resultItemCount;
		// 加入隐藏列
		if (ResultColListHid != null && ResultColListHid.getItemCount() > 0) {
			for (int j = 0; j < ResultColListHid.getItemCount(); j++) {
				sbResult.append("title_zmc_w["+(j+w_num)+"]=\""+ResultColListHid.getItemField(j).getChineseName()+"\";\r\n");
				sbResult.append("title_ymc_w["+(j+w_num)+"]=\""+ResultColListHid.getItemField(j).getName()+"\";\r\n");
				sbResult.append("title_with["+(j+w_num)+"]=0;\r\n");
			}
		}
//		if (ifFirstData.equals("1")) { // 是否查询显示数据
			try {
				startTime1 = System.currentTimeMillis();
				mdbset = dbEngine.QuerySQL(sb.toString());
				endTime1 = System.currentTimeMillis();
				LOGGER.info("执行数据查询SQL：" + (endTime1 - startTime1) + " ms.");
				vale_list.append("[");
				if (mdbset != null&&mdbset.RowCount()>0) {
					// --------------初始化has表---------
					Hashtable[] NameHashtable = initCodeHasTable(mdbset,ResultColList);
					// -------------------------------------
//					int cuntNum = ResultColList.getItemCount();
					for (int i = 0; i < mdbset.RowCount(); i++) {
						sbResult.append("body_tr_key["+i+"]=\""+mdbset.Row(i).Column(strPID).getString()+"\";\r\n");
						vale_list.append("[");
						String velue="";
//						rowNum++;
						// ------------------获取结果----------------------------------------
						if (ResultColList != null&& ResultColList.getItemCount() > 0) {
							for (int j = 0; j < ResultColList.getItemCount(); j++) {
								QALIGN = ResultColList.getItemField(j).getQALIGN();
								if (QALIGN == null) {
									QALIGN = "0";
								}
								if (QALIGN.equals("0")) {
									QALIGN = "left";
								}
								if (QALIGN.equals("1")) {
									QALIGN = "center";
								}
								if (QALIGN.equals("2")) {
									QALIGN = "right";
								}
								tmpValue = getValueByColumn(mdbset.Row(i),ResultColList.getItemField(j),NameHashtable);
								// 去掉回车和将单双引号变成全角----
								tmpValue = tmpValue.replaceAll("\'", "’");
								tmpValue = tmpValue.replaceAll("\"", "”");
								tmpValue = tmpValue.replaceAll("\r\n", "");
								tmpValue = tmpValue.replaceAll("\r", "");
								tmpValue = tmpValue.replaceAll("\n", "");
								// ----------------------------
								String str = tmpValue.replaceAll("&nbsp", "");
								velue+="\""+str+"\",";
							}
						}
						// 隐藏例的数据
						if (ResultColListHid != null&& ResultColListHid.getItemCount() > 0) {
							for (int j = 0; j < ResultColListHid.getItemCount(); j++) {
								tmpValue = getValueByColumnHid(mdbset.Row(i),ResultColListHid.getItemField(j));
								// 去掉回车和将单双引号变成全角----
								tmpValue = tmpValue.replaceAll("\'", "’");
								tmpValue = tmpValue.replaceAll("\"", "”");
								tmpValue = tmpValue.replaceAll("\r\n", "");
								tmpValue = tmpValue.replaceAll("\r", "");
								tmpValue = tmpValue.replaceAll("\n", "");
								// ----------------------------
								String str = tmpValue.replaceAll("&nbsp", "");
								velue+="\""+str+"\",";
							}
						}
						if(velue.length()>1){
							velue=velue.substring(0, velue.length()-1);
						}
						if(i == mdbset.RowCount()-1){
							vale_list.append(velue+"]");
						}else{
							vale_list.append(velue+"],");
						}
					}
				}
				vale_list.append("]");
			} catch (Exception e) {
				vale_list.append("[]");
				LOGGER.error("getQueryResultTable_PHONE出现异常！"+ e.getMessage());
			}
//		}
		sbResult.append("value_array="+vale_list+";\r\n");
		sbResult.append("var winHeight = window.innerHeight;\r\n");
		sbResult.append("var winWidth = window.innerWidth;\r\n");
		sbResult.append("window.addEventListener('resize', function(event){\r\n");
		sbResult.append("    winWidth = window.innerWidth;\r\n");
		sbResult.append("    winHeight = window.innerHeight;\r\n");
		sbResult.append("    //_$set_htmlTab();//填充数据\r\n");
		sbResult.append("});\r\n");
//		sbResult.append("setTimeout(function(){_$set_htmlTab();},0);\r\n");
		sbResult.append("</script>\r\n");

		long endTime = System.currentTimeMillis();
		LOGGER.info("GetQueryResultTable_PHONE执行完成，耗时："+ (endTime - startTime) + " ms.");
		mdbset = null;
		
		return sbResult.toString();
	}

	/**
	 * 根据配置ID等到结果列字串
	 * @param FID 查询配置ID
	 * @return String
	 */
	private String getResultFieldStr(String ID) {
		StringBuffer sb = new StringBuffer();
		QUERY_CONFIG_SHOWFIELD entityList[] = null;
		String strSql = null;
		LOGGER.info("getResultFieldStr开始调用...");
		long startTime = System.currentTimeMillis();
		try {
			if (ID.trim().length() > 0) {
				strSql = "Select * from QUERY_CONFIG_SHOWFIELD WHERE FID='" + ID + "' AND ISSHOW='1' ORDER BY ID";
				List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
				int length = retlist != null ? retlist.size() : 0;
				if (length > 0) {
					entityList = new QUERY_CONFIG_SHOWFIELD[length];
					for (int i = 0; i < length; i++) {
						Map<String, Object> retmap = retlist.get(i);
						entityList[i] = (QUERY_CONFIG_SHOWFIELD) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_SHOWFIELD.class);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("getResultFieldStr出现异常！" + e.getMessage());
			entityList = null;
		}
		if (entityList != null) {
			String[] tableFieldNames = new String[entityList.length];
			Hashtable<String, Object> tmpHashTable = new Hashtable<String, Object>();
			for (int i = 0; i < entityList.length; i++) {
				tableFieldNames[i] = entityList[i].getFIELD();
				if (tmpHashTable.get(entityList[i].getFIELD()) == null) {
					tmpHashTable.put(entityList[i].getFIELD(), entityList[i].getCOLWIDTH());
				}
				if (sb.toString().trim().length() == 0) {
					sb.append(entityList[i].getFIELD());
				} else {
					sb.append("," + entityList[i].getFIELD());
				}
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("getResultFieldStr执行完成，耗时：" + (endTime - startTime) + " ms.");
		return sb.toString();
	}

	/**
	 * 根据配置ID等到隐藏列字串
	 * @return String
	 */
	private String getResultFieldStrHid(String ID) {
		StringBuffer sb = new StringBuffer();
		QUERY_CONFIG_SHOWFIELD entityList[] = null;
		String strSql = null;
		LOGGER.info("GetResultFieldStrHid开始调用...");
		long startTime = System.currentTimeMillis();
		try {
			if (ID.trim().length() > 0) {
				strSql = "Select * from QUERY_CONFIG_SHOWFIELD WHERE FID='" + ID + "' AND ISSHOW='0' ORDER BY ID";
				List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
				int length = retlist != null ? retlist.size() : 0;
				if (length > 0) {
					entityList = new QUERY_CONFIG_SHOWFIELD[length];
					for (int i = 0; i < length; i++) {
						Map<String, Object> retmap = retlist.get(i);
						entityList[i] = (QUERY_CONFIG_SHOWFIELD) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_SHOWFIELD.class);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("getResultFieldStrHid出现异常！" + e.getMessage());
			entityList = null;
		}
		if (entityList != null) {
			String[] tableFieldNames = new String[entityList.length];
			for (int i = 0; i < entityList.length; i++) {
				tableFieldNames[i] = entityList[i].getFIELD();
				if (sb.toString().trim().length() == 0) {
					sb.append(entityList[i].getFIELD());
				} else {
					sb.append("," + entityList[i].getFIELD());
				}
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("getResultFieldStr执行完成，耗时：" + (endTime - startTime) + " ms.");
		
		return sb.toString();
	}

	/**
	 * 得到排序字串
	 * @return String
	 * @throws Exception 
	 */
	private String[] getSortStr(String ID) throws Exception {
		StringBuffer sb = new StringBuffer();
		String[] strReturn = new String[3];
		QUERY_CONFIG_ORDER entityList[] = null;
		String mainTablePrimaryStr = getMainTablePrimaryStr(ID);
		String getResultFieldStrHid = getResultFieldStrHid(ID);
		String getResultFieldStr = getResultFieldStr(ID);
		if (getResultFieldStr.indexOf(mainTablePrimaryStr) == -1 && getResultFieldStrHid.indexOf(mainTablePrimaryStr) == -1) {
			if (getResultFieldStr.trim().length() > 0) {
				getResultFieldStr = mainTablePrimaryStr + "," + getResultFieldStr;
			} else {
				getResultFieldStr = mainTablePrimaryStr;
			}
		}
		String strSql = null;
		LOGGER.info("getSortStr开始调用...");
		long startTime = System.currentTimeMillis();
		try {
			if (ID.trim().length() > 0) {
				strSql = "Select * from QUERY_CONFIG_ORDER WHERE FID='" + ID + "' ORDER BY ID";
				List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
				int length = retlist != null ? retlist.size() : 0;
				if (length > 0) {
					entityList = new QUERY_CONFIG_ORDER[length];
					for (int i = 0; i < length; i++) {
						Map<String, Object> retmap = retlist.get(i);
						entityList[i] = (QUERY_CONFIG_ORDER) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_ORDER.class);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("getSortStr出现异常！" + e.getMessage());
			entityList = null;
		}
		if (entityList != null) {
			LOGGER.info("2");
			sb.append(" Order by ");
			for (int i = 0; i < entityList.length; i++) {
				sb.append(entityList[i].getFIELD() + "  ");
				if (entityList[i].getTYPE().equals("1")) {
					sb.append(" ASC ");
				} else {
					sb.append(" DESC ");
				}
				if (i < entityList.length - 1) {
					sb.append(",");
				}
				// 保证排序字段在存在于查询列中
				if (getResultFieldStr.indexOf(entityList[i].getFIELD()) == -1
						&& getResultFieldStrHid.indexOf(entityList[i].getFIELD()) == -1) {
					if (getResultFieldStr.trim().length() > 0) {
						getResultFieldStr = getResultFieldStr + "," + entityList[i].getFIELD();
					} else if (getResultFieldStrHid.trim().length() > 0) {
						getResultFieldStrHid = getResultFieldStrHid + "," + entityList[i].getFIELD();
					} else {
						getResultFieldStr = entityList[i].getFIELD();
					}
				}
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("getSortStr执行完成，耗时：" + (endTime - startTime) + " ms.");
		strReturn[0] = sb.toString();
		strReturn[1] = getResultFieldStr;
		strReturn[2] = getResultFieldStrHid;
		return strReturn;
	}

	private String getValueByColumnHid(DBRow row, ItemField itemField) {
		int dbValueType;
		String tmpValue;
		String columnName;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dbValueType = itemField.getType().getValue();
		columnName = itemField.getName().split("\\.")[1];
		if (dbValueType == DBType.DATE.getValue() || dbValueType == DBType.DATETIME.getValue()) {
			try {
				// tmpValue = row.Column(columnName).getDate().toString();
				tmpValue = fmt.format(row.Column(columnName).getDate());
			} catch (Exception ex) {
				tmpValue = "";
			}
		}
		else if (dbValueType == DBType.FLOAT.getValue()) {
			try {
				tmpValue = String.valueOf(row.Column(columnName).getFloat());
			} catch (Exception ex) {
				tmpValue = "";
			}
			if (tmpValue.equals("0")) {
				tmpValue = "";
			}
		}
		else if (dbValueType == DBType.LONG.getValue()) {
			try {
				tmpValue = String.valueOf(row.Column(columnName).getInteger());
			} catch (Exception ex) {
				tmpValue = "";
			}
			if (tmpValue.equals("0")) {
				tmpValue = "";
			}
		}
		else {
			try {
				tmpValue = row.Column(columnName).getString();
			} catch (Exception ex) {
				tmpValue = "";
			}
		}
		if (tmpValue != null && tmpValue.trim().length() > 0) {
			return tmpValue;
		} else {
			tmpValue = "";
		}
		return tmpValue;
	}

	/**
	 * 将页面输入的项生成条件表达式
	 * @param ID 查询配置ID
	 * @param request
	 * @return String
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String getQueryItemContionExpression(String ID, SessionUser User,
			Request Rq, String P1, String P2, String P3, String P4, String P5) throws Exception {
		StringWork sw = new StringWork();
		StringBuffer sb = new StringBuffer(" 1=1 ");
		String conditionValue;
		ItemList itemList;
		ItemField itemField;
		int dbTypeValue;
		String sqlSql = "";
		String ISPRECISION = "";
		Hashtable Hashtable1 = new Hashtable();// 是否精确查询对照表
		if (DataBaseType.equals("1")) {// oracle数据库
			sqlSql = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.*,c.ID as sortID,c.ISPRECISION,c.FIELD from bpip_table a, bpip_field b,QUERY_CONFIG_QUERYFIELD c "
					+ "where b.tableid=a.tableid and a.tablename||'.'||b.fieldname=c.field and c.fid ='" + ID + "' order by SORTID";
		}
		else if (DataBaseType.equals("2")) {// mssql数据库
			sqlSql = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.*,c.ID as sortID,c.ISPRECISION,c.FIELD from bpip_table a, bpip_field b,QUERY_CONFIG_QUERYFIELD c "
					+ "where b.tableid=a.tableid and a.tablename+'.'+b.fieldname=c.field and c.fid ='" + ID + "' order by SORTID";
		}
		else if (DataBaseType.equals("3")) {// mysql数据库
			sqlSql = "select a.TABLENAME,b.*,c.ID,c.ISPRECISION,c.FIELD from bpip_table a, bpip_field b,QUERY_CONFIG_QUERYFIELD c "
					+ "where b.tableid=a.tableid and concat(a.tablename,'.',b.fieldname)=c.field and c.fid ='" + ID + "' order by c.ID";
		} else {
		}
		int i = 0;
		try {
			itemList = (ItemList) ADDHashtable3.get(ID);
			if (itemList == null) {
				DBRow[] dbMain = this.getTableProperty(sqlSql);
				itemList = new ItemList();
				if (dbMain != null) {
					for (i = 0; i < dbMain.length; i++) {
						if (DataBaseType.equals("3")) {// mysql数据库
							dbMain[i].addColumn("TABLECNNAME", "");
							dbMain[i].addColumn("sortID", "");
							dbMain[i].Column("TABLECNNAME").setValue(dbMain[i].Column("CHINESENAME").getString());
							dbMain[i].Column("sortID").setValue(dbMain[i].Column("ID").getString());
						}
						Hashtable1.put(ID + "." + dbMain[i].Column("FIELD").getString(), dbMain[i].Column("ISPRECISION").getString());
						itemList.fullFromDbRow(dbMain[i]);
					}
				}
				if (itemList != null) {
					ADDHashtable3.put(ID, itemList);
				}
			}
		} catch (Exception ex) {
			itemList = null;
		}
		if (itemList != null) {
			for (i = 0; i < itemList.getItemCount(); i++) {
				itemField = itemList.getItemField(i);
				dbTypeValue = itemField.getType().getValue();
				if (dbTypeValue == DBType.LONG.getValue() || dbTypeValue == DBType.FLOAT.getValue()
						|| dbTypeValue == DBType.DATE.getValue() || dbTypeValue == DBType.DATETIME.getValue()) {
					conditionValue = Rq.getItem((itemField.getName() + "_Begin").toUpperCase());
					if (conditionValue == null) {
						conditionValue = "";
					}
					if (conditionValue.trim().length() > 0) {
						sb.append(" And " + getConditionExpression(itemField.getName(), ">=", 
								conditionValue, dbTypeValue, User, P1, P2, P3, P4, P5));
					}
					conditionValue = Rq.getItem((itemField.getName() + "_End").toUpperCase());
					if (conditionValue == null) {
						conditionValue = "";
					}
					if (conditionValue.trim().length() > 0) {
						sb.append(" And " + getConditionExpression(itemField.getName(), "<=", 
								conditionValue, dbTypeValue, User, P1, P2, P3, P4, P5));
					}
				} else {
					conditionValue = Rq.getItem((itemField.getName()).toUpperCase());
					// -----多选查询的处理-----------------
					if (itemField.getCodeInput() == 4) {
						if (conditionValue != null && conditionValue.indexOf("ALL") == 0) {
							conditionValue = "";
						}
					}
					// ----------------------------------
					if (conditionValue == null) {
						conditionValue = "";
					}
					if (conditionValue.trim().length() > 0) {
						if (itemField.isUnit()) {
							conditionValue = sw.CutLastZero(conditionValue, 2);
							conditionValue = "'" + conditionValue + "%'";
						}
						// 得到是否精确查询
						ISPRECISION = (String) Hashtable1.get(ID + "." + itemField.getName());
						if (ISPRECISION == null) {
							ISPRECISION = "0";
						}
						if (ISPRECISION.equals("1")) {// 精确查询
							conditionValue = conditionValue.replaceAll("%", "");
							sb.append(" And " + getConditionExpression(itemField.getName(), "=", 
									conditionValue, dbTypeValue, User, P1, P2, P3, P4, P5));
						} else {
							sb.append(" And " + getConditionExpression(itemField.getName(), "Like", 
									conditionValue, dbTypeValue, User, P1, P2, P3, P4, P5));
						}
					}
				}
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	private String getValueByColumn(DBRow row, ItemField itemField, Hashtable NameHashtable[]) throws Exception {
		int dbValueType;
		String tmpValue;
		String columnName;
		Hashtable userList;
		Hashtable unitList;
		Hashtable codeList;
		
		userList = NameHashtable[0];
		unitList = NameHashtable[1];
		codeList = NameHashtable[2];
		dbValueType = itemField.getType().getValue();
		columnName = itemField.getName().split("\\.")[1];
		if (dbValueType == DBType.DATE.getValue()) {
			try {
				tmpValue = row.Column(columnName).getDate().toString();
				if (tmpValue.length() > 10) {
					tmpValue = tmpValue.substring(0, 10);
				}
			} catch (Exception ex) {
				tmpValue = "";
			}
		}
		else if (dbValueType == DBType.DATETIME.getValue()) {
			try {
				tmpValue = row.Column(columnName).getDate().toString();
				if (tmpValue.length() > 19) {
					tmpValue = tmpValue.substring(0, 19);
				}
			} catch (Exception ex) {
				tmpValue = "";
			}
		}
		else if (dbValueType == DBType.FLOAT.getValue()) {
			try {
				tmpValue = String.valueOf(row.Column(columnName).getDouble());
				if (tmpValue.lastIndexOf("E") > -1)// 将有科学计数的数字转化成传统方式
				{
					int flen = 0;
					flen = tmpValue.lastIndexOf("E");
					int flen1 = Integer.parseInt(tmpValue.substring(flen + 1,
							tmpValue.length()));
					tmpValue = tmpValue.substring(0, flen);
					tmpValue = tmpValue.replaceAll("\\.", "");
					if (tmpValue.length() > flen1) {
						tmpValue = tmpValue.substring(0, flen1 + 1) + "." 
								 + tmpValue.substring(flen1 + 1, tmpValue.length());
					}
					if (tmpValue.length() <= flen1) {
						int alen = flen1 - tmpValue.length() + 1;
						for (int y = 0; y < alen; y++) {
							tmpValue = tmpValue + "0";
						}
					}
				}
				if (!tmpValue.equals("0") && tmpValue.substring(tmpValue.length() - 1, 
						tmpValue.length()).equals(".")) {
					tmpValue = tmpValue.substring(0, tmpValue.length() - 1);
				}
				if (!tmpValue.equals(".0") && tmpValue.substring(tmpValue.length() - 2,
								tmpValue.length()).equals(".0")) {
					tmpValue = tmpValue.substring(0, tmpValue.length() - 2);
				}
				if (tmpValue.equals(".0")) {
					tmpValue = "0";
				}
				tmpValue = tmpValue.trim();
				if (tmpValue.length() == 0) {
					tmpValue = "0";
				}
			} catch (Exception ex) {
				tmpValue = "0";
			}
		}
		else if (dbValueType == DBType.LONG.getValue()) {
			try {
				tmpValue = String.valueOf(row.Column(columnName).getInteger());
			} catch (Exception ex) {
				tmpValue = "";
			}
			if (tmpValue.equals("0")) {
				tmpValue = "";
			}
		}
		else {
			try {
				tmpValue = row.Column(columnName).getString();
			} catch (Exception ex) {
				tmpValue = "";
			}
			if (itemField.isCode()) {
				if (itemField.getCodeInput() != 4) {
					try {
						// 测试--
						// log.WriteLog("得到hs代码表:"+itemField.getCodeTable() +
						// "^" + tmpValue);
						// log.WriteLog("得到hs代码表值:"+codeList.get(itemField.getCodeTable()
						// + "^" + tmpValue).toString());
						// 测试--
						if (codeList.get(itemField.getCodeTable() + "^" + tmpValue) != null) {
							tmpValue = codeList.get(itemField.getCodeTable() + "^" + tmpValue).toString();
						} else {
							if (tmpValue.length() > 0) {
								// tmpValue =
								// getDictName(tmpValue,itemField.getCodeTable());
							}
						}
					} catch (Exception ex) {
						LOGGER.error("获取代码值", ex.getMessage());
						if (tmpValue.length() > 0) {
							// tmpValue = getDictName(tmpValue,
							// itemField.getCodeTable());
						}
					}
				} else {
					tmpValue = getMulSelDictName(tmpValue, itemField.getCodeTable());
				}
			}
			if (itemField.isUser()) {
				if (itemField.getName().equals("FLOW_RUNTIME_PROCESS.ACCEPTPSN")
						|| itemField.getName().equals("FLOW_RUNTIME_PROCESS.FACCEPTPSN")) {
					tmpValue = getMulSelUserName(tmpValue);
				} else {
					if (userList != null && userList.get(tmpValue) != null) {
						tmpValue = userList.get(tmpValue).toString();
					} else {
						tmpValue = getUserName(tmpValue);
					}
				}
			}
			if (itemField.isUnit()) {
				if (unitList != null && unitList.get(tmpValue) != null) {
					tmpValue = unitList.get(tmpValue).toString();
				} else {
					if (tmpValue.length() > 0) {
						tmpValue = getUnitName(tmpValue);
					}
				}
			}
			/*
			 * if (itemField.isComm()) { //tmpValue = getCommName(tmpValue); if
			 * (commList != null && commList.get(tmpValue) != null) { tmpValue =
			 * commList.get(tmpValue).toString(); } else { tmpValue =
			 * getCommName(tmpValue); } }
			 */
		}
		if (tmpValue != null && tmpValue.trim().length() > 0) {
			return tmpValue;
		} else {
			tmpValue = "&nbsp";
		}
		return tmpValue;
	}

	private String createQueryItem_zdy(String ID, SessionUser User, Request Rq, int gcow) {
		LOGGER.info("CreateQueryItem_zdy开始调用...");
		long startTime = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		String sqlSql = "";
		String ISMUSTvalue = "";
		
		String DVALUE = "";
		String YVALUE = "";
		String ISDAY = "";
		String Endvalue = "";
		int zcnum = 0;
		
		String queryFlag = Rq.getItem("queryFlag");
		if (queryFlag == null) {
			queryFlag = "0";
		}
		// -----设置时间默认值时使用----
		String STvalue = "";// 开始时间
		String ETvalue = "";// 结束时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date datetest1 = new Date();
		Date datetest2 = new Date();
		Date datec = new Date();
		SimpleDateFormat formatc = new SimpleDateFormat("yyyy-MM-dd");
		long timec = 0;
		String qdatec = "";
		
		String datetest = "";
		long Time1 = 0;
		String fz = "";
		int intfz = 0;
		String isDateTime = "0";
		if (DataBaseType.equals("1")) {// oracle数据库
			sqlSql = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.*,c.ID as sortID,c.ISMUST,c.DVALUE,c.ISDAY,c.FIELD from bpip_table a, bpip_field b,QUERY_CONFIG_QUERYFIELD c "
					+ "where b.tableid=a.tableid and a.tablename||'.'||b.fieldname=c.field and c.fid ='" + ID + "' order by SORTID";
		}
		else if (DataBaseType.equals("2")) {// mssql数据库
			sqlSql = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.*,c.ID as sortID,c.ISMUST,c.DVALUE,c.ISDAY,c.FIELD from bpip_table a, bpip_field b,QUERY_CONFIG_QUERYFIELD c "
					+ "where b.tableid=a.tableid and a.tablename+'.'+b.fieldname=c.field and c.fid ='" + ID + "' order by SORTID";
		}
		else if (DataBaseType.equals("3")) {// mysql数据库
			sqlSql = "select a.TABLENAME,b.*,c.ID,c.ISMUST,c.DVALUE,c.ISDAY,c.FIELD from bpip_table a, bpip_field b,QUERY_CONFIG_QUERYFIELD c "
					+ "where b.tableid=a.tableid and concat(a.tablename,'.',b.fieldname)=c.field and c.fid ='" + ID + "' order by c.ID";
		} else {
		}
		int i = 0;
		ItemList itemList = new ItemList();
		itemList = (ItemList) ColQListHashtable.get(ID);
		if (itemList == null) {
			try {
				DBRow[] dbMain = this.getTableProperty(sqlSql);
				itemList = new ItemList();
				if (dbMain != null) {
					for (i = 0; i < dbMain.length; i++) {
						if (DataBaseType.equals("3")) {// mysql数据库
							dbMain[i].addColumn("TABLECNNAME", "");
							dbMain[i].addColumn("sortID", "");
							dbMain[i].Column("TABLECNNAME")
									.setValue(dbMain[i].Column("CHINESENAME").getString());
							dbMain[i].Column("sortID").setValue(
									dbMain[i].Column("ID").getString());
						}
						ADDHashtable7.put(ID + "." + dbMain[i].Column("FIELD").getString(),
								dbMain[i].Column("ISMUST").getString());
						ADDHashtable8.put(ID + "." + dbMain[i].Column("FIELD").getString(),
								dbMain[i].Column("DVALUE").getString());
						ADDHashtable9.put(ID + "." + dbMain[i].Column("FIELD").getString(),
								dbMain[i].Column("ISDAY").getString());
						itemList.fullFromDbRow(dbMain[i]);
					}
				}
			} catch (Exception ex) {
				itemList = null;
			}
			if (itemList != null) {
				ColQListHashtable.put(ID, itemList);
			}
		}
		try {
			if (itemList != null) {
				CreateControl createControl;
				ItemField itemField;
				i = 0;
				int dbTypeValue;
				String controlName = "";
				String valueStr = "";
				while (i < itemList.getItemCount()) {
					if (zcnum % gcow == 0) {
						sb.append("<li class=\"am-parent\">");
					}
					itemField = itemList.getItemField(i);
					dbTypeValue = itemField.getType().getValue();
					// 得到是否必填
					DVALUE = (String) ADDHashtable8.get(ID + "." + itemField.getName());
					if (DVALUE == null) {
						DVALUE = "";
					}
					if (DVALUE.equals("0")) {
						DVALUE = "";
					}
					YVALUE = "";
					isDateTime = "0";
					if (DVALUE.length() > 0) {
						if (DVALUE.lastIndexOf("{YYYY-MM-DD HH:MM:SS}") > -1) {
							isDateTime = "1";
							fz = DVALUE.replaceAll("\\{YYYY-MM-DD HH:MM:SS\\}", "");
							fz = fz.replaceAll("-", "");
							fz = fz.trim();
							try {
								intfz = Integer.parseInt(fz);
							} catch (NumberFormatException ex1) {
								intfz = 0;
							}
							datetest = format.format(datetest1);
							ETvalue = datetest + ":00";
							datetest2 = format.parse(datetest);
							Time1 = (datetest2.getTime() / 1000) - 60 * intfz;// 减去XX分钟
							datetest2.setTime(Time1 * 1000);
							datetest = format.format(datetest2);
							STvalue = datetest + ":00";
						} else {
							if (DVALUE.lastIndexOf("{YYYY-MM-DD}") > -1) {
								if (DataBaseType.equals("2")) {// mssql数据库
									YVALUE = DVALUE;
									fz = DVALUE.replaceAll("\\{YYYY-MM-DD\\}", "");
									fz = fz.replaceAll("-", "");
									fz = fz.trim();
									try {
										intfz = Integer.parseInt(fz);
									} catch (NumberFormatException ex1) {
										intfz = 0;
									}
									timec = datec.getTime() - intfz * 24 * 3600 * 1000;
									qdatec = formatc.format(timec);
									DVALUE = qdatec;
								} else {
									DVALUE = DVALUE.replaceAll("\\{YYYY-MM-DD\\}", "SYSDATE");
									YVALUE = DVALUE;
									DVALUE = "to_char(" + DVALUE + ",'yyyy-mm-dd')";
									DVALUE = prepareExpression(DVALUE, User, "", "", "", "", "");
								}
							}
						}
					}
					ISDAY = (String) ADDHashtable9.get(ID + "." + itemField.getName());
					if (ISDAY == null) {
						ISDAY = "";
					}
					Endvalue = "";
					if (DVALUE.length() > 0 && ISDAY.length() > 0 && !ISDAY.equals("0")) {
						if (DataBaseType.equals("2")) {// mssql数据库
							// Endvalue = YVALUE+"+"+ISDAY;
							try {
								intfz = Integer.parseInt(ISDAY);
							} catch (NumberFormatException ex1) {
								intfz = 0;
							}
							if (ISDAY == null || "".equals(ISDAY)) {
								ISDAY = "0";
							}
							if (ISDAY.equals("0")) {
								Endvalue = formatc.format(datec.getTime());
							} else {
								Endvalue = formatc.format(timec + intfz * 24 * 3600 * 1000);
							}
						} else {
							Endvalue = "to_char(" + YVALUE + "+" + ISDAY + ",'yyyy-mm-dd')";
						}
						Endvalue = prepareExpression(Endvalue, User, "", "", "", "", "");
					}
					if (dbTypeValue == DBType.DATE.getValue() || dbTypeValue == DBType.DATETIME.getValue()) {
						controlName = itemField.getName();
						// itemField.setName(controlName + "_Begin"); //构造起始值
						valueStr = Rq.getItem(controlName + "_Begin");
						if (dbTypeValue == DBType.DATETIME.getValue() && DVALUE.length() > 0)// 日期时间类型
						{
							Endvalue = DVALUE + " 23:59:59";
							DVALUE = DVALUE + " 00:00:00";
							if (isDateTime.equals("1")) {
								DVALUE = STvalue;
								Endvalue = ETvalue;
							}
						}
						if (valueStr.length() == 0) {
							if (!queryFlag.equals("1")) {
								valueStr = DVALUE;
							}
						}
						createControl = new CreateControl(itemField, User, valueStr);
						sb.append("<a href=\"##\" class=\"\" >" + itemField.getChineseName() + "</a>\r\n");
						sb.append("<ul class=\"am-menu-sub am-collapse  am-avg-sm-1 \">\r\n");
						sb.append("<li class=\"\">\r\n");
						sb.append(createControl.GetOutPutHTML().replaceAll(
								controlName, controlName + "_Begin") + ISMUSTvalue + "\r\n");
						sb.append("</li>\r\n");
						
						// itemField.setName(controlName + "_End"); //构造结束值
						valueStr = Rq.getItem(controlName + "_End");
						if (valueStr.length() == 0) {
							if (!queryFlag.equals("1")) {
								valueStr = Endvalue;
							}
						}
						createControl = new CreateControl(itemField, User, valueStr);
						// itemField.setName(controlName + "_End"); //构造结束值
						valueStr = Rq.getItem(controlName + "_End");
						createControl = new CreateControl(itemField, User, valueStr);
						sb.append("<li class=\"\">\r\n");
						sb.append(createControl.GetOutPutHTML().replaceAll(
								controlName, controlName + "_End") + ISMUSTvalue + "\r\n");
						sb.append("</li>\r\n");
						sb.append("</ul>\r\n");
						i++;
						zcnum = zcnum + 2;
					} else {
						// number类型
						if (dbTypeValue == DBType.LONG.getValue() || dbTypeValue == DBType.FLOAT.getValue()) {
							controlName = itemField.getName();
							// if(controlName.indexOf("_Begin")<0){
							// itemField.setName(controlName + "_Begin");
							// //构造起始值
							// }
							valueStr = Rq.getItem(controlName + "_Begin");
							createControl = new CreateControl(itemField, User, valueStr);
							sb.append("<a href=\"##\" class=\"\" >" + itemField.getChineseName() + "</a>\r\n");
							sb.append("<ul class=\"am-menu-sub am-collapse  am-avg-sm-1 \">\r\n");
							sb.append("<li class=\"\">\r\n");
							sb.append(createControl.GetOutPutHTML().replaceAll(
									controlName, controlName + "_Begin") + ISMUSTvalue + "\r\n");
							sb.append("</li>\r\n");
							
							// itemField.setName(controlName + "_End"); //构造结束值
							valueStr = Rq.getItem(controlName + "_End");
							createControl = new CreateControl(itemField, User, valueStr);
							sb.append("<li class=\"\">\r\n");
							sb.append(createControl.GetOutPutHTML().replaceAll(
									controlName, controlName + "_End") + ISMUSTvalue + "\r\n");
							sb.append("</li>\r\n");
							sb.append("</ul>\r\n");
							i++;
							zcnum = zcnum + 1;
						}
						else {
							valueStr = Rq.getItem(itemField.getName());
							if (valueStr.length() == 0) {
								if (!queryFlag.equals("1")) {
									valueStr = DVALUE;
								}
							}
							createControl = new CreateControl(itemField, User, valueStr);
							sb.append("<a href=\"##\" class=\"\" >" + itemField.getChineseName() + "</a>\r\n");
							sb.append("<ul class=\"am-menu-sub am-collapse  am-avg-sm-1 \">\r\n");
							sb.append("<li class=\"\">\r\n");
							sb.append(createControl.GetOutPutHTML() + ISMUSTvalue + "\r\n");
							sb.append("</li>\r\n");
							sb.append("</ul>\r\n");
							i++;
							zcnum = zcnum + 1;
						}
					}
					if (zcnum % gcow == 0) {
						sb.append("</li>\r\n");
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("发生异常", ex.toString());
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("CreateQueryItem_zdy执行完成，耗时：" + (endTime - startTime) + " ms.");
		
		return sb.toString();
	}

	/**
	 * 根据单位ID获取单位名称
	 * @param UnitID
	 * @return String
	 * @throws Exception 
	 */
	private String getUnitName(String UnitID) throws Exception {
		String result = null;
		String strSQL = "Select UnitID,UnitName,UNITNAME From BPIP_UNIT Where UnitID='"+UnitID+"'";
		Map<String, Object> retmap = userMapper.selectMapExecSQL(strSQL);
		if (retmap != null && retmap.size() > 0) {
			result = retmap.get("UNITNAME").toString();
		}
		return result;
	}

	/**
	 * 根据字典表、代码获取名称
	 * @param code 代码
	 * @param codeTable 代码表
	 * @return String
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	private String getDictName(String code, String codeTable) throws Exception {
		String result = "";
		String strSQL = "Select Code,Name From " + codeTable + " Where Code='" + code + "'";
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
		if (retlist != null && retlist.size() > 0) {
			if (retlist.get(0) != null && retlist.get(0).get("Name") != null) {
				result = retlist.get(0).get("Name").toString();
			}
		}
		return result;
	}

	/**
	 * 根据字典表、代码获取多项选择名称
	 * @param code 代码 如01,02
	 * @param codeTable 代码表
	 * @return String 中文 如Name1,Name2
	 * @throws Exception 
	 */
	private String getMulSelDictName(String code, String codeTable) throws Exception {
		String result = "";
		String strSQL = "Select Code,Name From " + codeTable + " Where Code In('" 
					   + code.replaceAll(",", "','") + "') order by CODE";
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			result = code;
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				result = result.replaceAll(retmap.get("Code").toString(), retmap.get("Name").toString());
			}
		}
		return result;
	}

	/**
	 * 根据用户表多项选择名称
	 * @param UserID 代码 如Id1,Id2
	 * @return String 中文 如Name1,Name2
	 * @throws Exception 
	 */
	private String getMulSelUserName(String code) throws Exception {
		String result = "";
		if (code.length() > 0) {
			String strSQL = "Select UserId,Name from Bpip_User Where UserId In('" + code.replaceAll(",", "','") + "')";
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
			int length = retlist != null ? retlist.size() : 0;
			if (length > 0) {
				result = code;
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					result = result.replaceAll(retmap.get("UserId").toString(), retmap.get("Name").toString());
				}
			}
		}
		return result;
	}

	private String getUserName(String UserID) throws Exception {
		String result = null;
		String strSQL = "Select UserID,Name From BPIP_USER Where UserID='" + UserID + "'";
		result = UserID;
		Map<String, Object> retmap = userMapper.selectMapExecSQL(strSQL);
		if (retmap != null && retmap.size() > 0) {
			result = retmap.get("Name").toString();
		}
		return result;
	}

	/**
	 * 功能或作用：实现翻页
	 * @param PageSize 一页的记录数
	 * @param FileUrl 文件名
	 * @param intRowCount 总的记录数
	 * @param intPage 当前页
	 * @Return PageMenu 执行后返回一个PageMenu字符串
	 */
	private String createPageMenu(int intPageSize, int intRowCount, int intPage) {
		LOGGER.info("createPageMenu开始调用...");
		long startTime = System.currentTimeMillis();
		long endTime;
		StringBuffer sbPageMenu = new StringBuffer();
		int intPageCount = 0, i, sum1 = 0, sum2 = 0, sum3 = intRowCount;
		if (intPageSize > 0) {
			while (((intRowCount - intPageSize) > 0) || (intRowCount > 0)) {
				intPageCount++;
				intRowCount = intRowCount - intPageSize;
			}
		}
		if (intPage > intPageCount) {
			intPage = intPageCount;
		}
		if (intPageCount > 1) {
			sum1 = intPage - 1;
			if (sum1 < 1) {
				sum1 = 1;
			}
			sum2 = intPage + 1;
			if (sum2 > intPageCount) {
				sum2 = intPageCount;
			}
			sbPageMenu.append("<li class=\"am-parent\">");
			sbPageMenu.append("<ul class=\"am-pagination\">");
			// class=\"am-disabled\" li 不让点击
			sbPageMenu.append("  <li><a href=\"javascript:recordfrm.page.value='1';recordfrm.submit();\">&laquo;</a></li>");// 首页
			// 总共intPageCount 第intPage
			String class_cssString = "";
			if (intPageCount <= 3) {// 小于三
				for (i = 1; i <= intPageCount; i++) {
					if (i == intPage) {
						class_cssString = "class=\"am-active\"";
					} else {
						class_cssString = "";
					}
					sbPageMenu.append("  <li " + class_cssString
							+ "><a href=\"javascript:recordfrm.page.value='"
							+ String.valueOf(i) + "';recordfrm.submit();\">"
							+ i + "</a></li>");
				}
			}
			if (intPageCount > 3) {// 大于三
				if (intPage < 3) {
					for (i = 1; i <= 3; i++) {
						if (i == intPage) {
							class_cssString = "class=\"am-active\"";
						} else {
							class_cssString = "";
						}
						sbPageMenu.append("  <li "
										+ class_cssString
										+ "><a href=\"javascript:recordfrm.page.value='"
										+ String.valueOf(i)
										+ "';recordfrm.submit();\">" + i
										+ "</a></li>");
					}
				} else if (intPage >= 3 && intPage < intPageCount) {
					sbPageMenu.append("  <li><a href=\"javascript:recordfrm.page.value='"
									+ String.valueOf(intPage - 1)
									+ "';recordfrm.submit();\">"
									+ (intPage - 1) + "</a></li>");
					sbPageMenu.append("  <li class=\"am-active\"><a href=\"javascript:recordfrm.page.value='"
									+ String.valueOf(intPage)
									+ "';recordfrm.submit();\">"
									+ (intPage)
									+ "</a></li>");
					sbPageMenu.append("  <li><a href=\"javascript:recordfrm.page.value='"
									+ String.valueOf(intPage + 1)
									+ "';recordfrm.submit();\">"
									+ (intPage + 1) + "</a></li>");
				} else if (intPage >= 3 && intPage == intPageCount) {
					sbPageMenu.append("  <li><a href=\"javascript:recordfrm.page.value='"
									+ String.valueOf(intPage - 2)
									+ "';recordfrm.submit();\">"
									+ (intPage - 2) + "</a></li>");
					sbPageMenu.append("  <li><a href=\"javascript:recordfrm.page.value='"
									+ String.valueOf(intPage - 1)
									+ "';recordfrm.submit();\">"
									+ (intPage - 1) + "</a></li>");
					sbPageMenu.append("  <li class=\"am-active\"><a href=\"javascript:recordfrm.page.value='"
									+ String.valueOf(intPage)
									+ "';recordfrm.submit();\">"
									+ (intPage)
									+ "</a></li>");
				} else {
					sbPageMenu.append("  <li class=\"am-active\"><a href=\"javascript:recordfrm.page.value='"
									+ String.valueOf(intPage)
									+ "';recordfrm.submit();\">"
									+ (intPage)
									+ "</a></li>");
				}
			}
			sbPageMenu.append("  <li><a href=\"javascript:recordfrm.page.value='"+intPageCount+"';recordfrm.submit();\">&raquo;</a></li>");// 尾页
			sbPageMenu.append("</ul>");
			sbPageMenu.append("</li>");
		} else {
			// sbPageMenu.append("&nbsp;&nbsp;共" + sum3 + "条");
			sbPageMenu.append("<li class=\"am-parent\">");
			sbPageMenu.append("<a href=\"#\">共" + sum3 + "条</a></li>");
			sbPageMenu.append("</li>");
		}
		// String checkboxStr;
		// /*
		// if (_queryType.equals("2")) {
		// checkboxStr =
		// "<input type='checkbox' name='selectall' onclick=SelectAll();>全选";
		// } else {
		// checkboxStr = "";
		// }*/
		// checkboxStr = ""; //不需要checkbox
		//
		// PageMenu = "<TD >" + checkboxStr + "<TD align='right'>" +
		// sbPageMenu.toString();
		endTime = System.currentTimeMillis();
		LOGGER.info("CreatePageMenu执行完成，耗时：" + (endTime - startTime) + " ms.");
		return sbPageMenu.toString();
	}

	/**
	 * 得到主表名.主键名字串
	 * @throws Exception 
	 */
	private String getMainTablePrimaryStr(String ID) throws Exception {
		String strReturn = "";
		String strSql = "Select * from query_config_table where ID='" + ID + "'";
		Map<String, Object> retmap = userMapper.selectMapExecSQL(strSql);
		String mainTable = "";
		String primaryKey = "";
		if (retmap != null && retmap.size() > 0) {
			mainTable = retmap.get("MAINTABLE").toString();
		}
		strSql = "select * from bpip_table where TableName='" + mainTable + "'";
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			if (retlist.get(0) != null && retlist.get(0).get("PRIMARYKEY") != null) {
				primaryKey = retlist.get(0).get("PRIMARYKEY").toString();
			}
		}
		strReturn = mainTable + "." + primaryKey;
		
		return strReturn;
	}

	/**
	 * 根据条件获取明细参数列属性信息列表
	 * @param strWhere 条件字符串
	 * @return QUERY_CONFIG_PARAMETER []
	 */
	private QUERY_CONFIG_PARAMETER[] getParameterList(String strWhere) {
		QUERY_CONFIG_PARAMETER entityList[] = null;
		String strSql = null;
		LOGGER.info("GetResultFieldByWhere开始调用...");
		long startTime = System.currentTimeMillis();
		try {
			if (strWhere.trim().length() == 0) {
				strSql = "Select * From QUERY_CONFIG_PARAMETER ";
			} else {
				strSql = "Select * From QUERY_CONFIG_PARAMETER where " + strWhere;
			}
			strSql += " Order by ID";
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
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
			LOGGER.error("getParameterList出现异常！" + e.getMessage());
			entityList = null;
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("getParameterList执行完成，耗时：" + (endTime - startTime) + " ms.");
		return entityList;
	}

	/**
	 * 根据条件获取明细参数列属性信息列表
	 * @param strWhere 条件字符串
	 * @return QUERY_CONFIG_PARAMETER []
	 */
	@SuppressWarnings("unused")
	private QUERY_CONFIG_TABLE getQueryObj(String ID) {
		QUERY_CONFIG_TABLE entityObj = null;
		QUERY_CONFIG_TABLE entityObj1 = null;
		StringBuffer strSql = new StringBuffer();
		try {
			strSql.append("Select * From QUERY_CONFIG_TABLE Where ID='" + ID.trim() + "'");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				entityObj1 = null;
			} else {// length > 0
				entityObj1 = (QUERY_CONFIG_TABLE) ReflectionUtil.convertMapToBean(retlist.get(0), QUERY_CONFIG_TABLE.class);
			}
			if (entityObj1 != null) {
				entityObj = entityObj1;
			}
		} catch (Exception e) {
			LOGGER.error("调用方法GetQueryObj出现异常" + e.toString());
		}
		return entityObj;
	}

	/**
	 * 处理表达式，使其能计算出来
	 * @param expression 待计算的函数表达式
	 * @return String 处理完后的表达式
	 */
	private String prepareExpression(String expression, SessionUser User,
			String P1, String P2, String P3, String P4, String P5) {
		LOGGER.info("prepareExpression开始调用...");
		long startTime = System.currentTimeMillis();
		String resultStr;
		SimpleDateFormat fmt;
		Date date = new Date();
		// 处理当前日期
		if (DataBaseType.equals("1")) {// Oracle数据库
			expression = expression.replaceAll("sysdate", "to_char(sysdate,'yyyy-mm-dd')");
		}
		else if (DataBaseType.equals("2")) {// MSSQL数据库
			expression = expression.replaceAll("sysdate", "getdate()");
		}
		else if (DataBaseType.equals("3")){// MySQL数据库
			expression = expression.replaceAll("sysdate", "str_to_date(sysdate,'%Y-%m-%d')");
		}
		// 处理自定义的变量
		if (expression.lastIndexOf("{LoginID}") > -1) { // 登录名
			expression = expression.replaceAll("\\{LoginID\\}", User.getLoginID());
		}
		if (expression.lastIndexOf("{Name}") > -1) { // 姓名
			expression = expression.replaceAll("\\{Name\\}", User.getName());
		}
		if (expression.lastIndexOf("{UserID}") > -1) { // 用户编号
			expression = expression.replaceAll("\\{UserID\\}", User.getUserID());
		}
		if (expression.lastIndexOf("{LCODE}") > -1) { // 用户内部编号
			expression = expression.replaceAll("\\{LCODE\\}", User.getLCODE());
		}
		if (expression.lastIndexOf("{UnitName}") > -1) { // 所在单位名称
			expression = expression.replaceAll("\\{UnitName\\}", User.getUnitName());
		}
		if (expression.lastIndexOf("CutLastZero({UnitID})") > -1) {// 所在单位编号，解决上级单位查看下级单位的问题
			zr.zrpower.common.util.StringWork strWork = new StringWork();
			expression = expression.replaceAll("CutLastZero\\(\\{UnitID\\}\\)",
					strWork.CutLastZero(User.getUnitID(), 2));
		}
		if (expression.lastIndexOf("{UnitID}") > -1) { // 所在单位编号
			expression = expression.replaceAll("\\{UnitID\\}", User.getUnitID());
		}
		if (expression.lastIndexOf("{Custom1}") > -1) { // 自定义参数1
			expression = expression.replaceAll("\\{Custom1\\}", User.getCustom1());
		}
		if (expression.lastIndexOf("{Custom2}") > -1) { // 自定义参数2
			expression = expression.replaceAll("\\{Custom2\\}", User.getCustom2());
		}
		if (expression.lastIndexOf("{Custom3}") > -1) { // 自定义参数3
			expression = expression.replaceAll("\\{Custom3\\}", User.getCustom3());
		}
		if (expression.lastIndexOf("{Custom4}") > -1) { // 自定义参数4
			expression = expression.replaceAll("\\{Custom4\\}", User.getCustom4());
		}
		if (expression.lastIndexOf("{Custom5}") > -1) { // 自定义参数5
			expression = expression.replaceAll("\\{Custom5\\}", User.getCustom5());
		}
		// -------------------------
		
		/******************************* 时间的处理 ************************************/
		if (expression.lastIndexOf("{YYYY年}") > -1) { // 当前年(中文式)
			fmt = new SimpleDateFormat("yyyy年");
			expression = expression.replaceAll("\\{YYYY年\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY年MM月}") > -1) { // 当前年月(中文式)
			fmt = new SimpleDateFormat("yyyy年MM月");
			expression = expression.replaceAll("\\{YYYY年\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY年MM月DD日}") > -1) { // 当前日期(中文式)
			fmt = new SimpleDateFormat("yyyy年MM月dd日");
			expression = expression.replaceAll("\\{YYYY年MM月DD日\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY年MM月DD日 HH:MM:SS}") > -1) { // 当前时间(中文式)
			fmt = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
			expression = expression.replaceAll("\\{YYYY年MM月DD日 HH:MM:SS\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY}") > -1) { // 当前年
			fmt = new SimpleDateFormat("yyyy");
			expression = expression.replaceAll("\\{YYYY\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY-MM}") > -1) { // 当前年月
			fmt = new SimpleDateFormat("yyyy-MM");
			expression = expression.replaceAll("\\{YYYY-MM\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY-MM-DD}") > -1) { // 当前日期
			fmt = new SimpleDateFormat("yyyy-MM-dd");
			expression = expression.replaceAll("\\{YYYY-MM-DD\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY-MM-DD HH:MM:SS}") > -1) { // 当前时间
			fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			expression = expression.replaceAll("\\{YYYY-MM-DD HH:MM:SS\\}", fmt.format(date));
		}
		/******************************* 自定义参数的处理 ************************************/
		if (expression.indexOf("P1") > -1 && P1 != null) {
			expression = expression.replaceAll("P1", P1.replaceAll(",", "','"));
		}
		if (expression.indexOf("P2") > -1 && P2 != null) {
			expression = expression.replaceAll("P2", P2.replaceAll(",", "','"));
		}
		if (expression.indexOf("P3") > -1 && P3 != null) {
			expression = expression.replaceAll("P3", P3.replaceAll(",", "','"));
		}
		if (expression.indexOf("P4") > -1 && P4 != null) {
			expression = expression.replaceAll("P4", P4.replaceAll(",", "','"));
		}
		if (expression.indexOf("P5") > -1 && P5 != null) {
			expression = expression.replaceAll("P5", P5.replaceAll(",", "','"));
		}
		String sqlStr;
		LOGGER.info("expression1=" + expression);
		resultStr = expression;
		if (!(expression.indexOf("+") == -1 && expression.indexOf("*") == -1
				&& expression.indexOf("||") == -1 && expression.indexOf("/") == -1
				&& expression.indexOf("(") == -1 && expression.indexOf(")") == -1)) {// 有运算符，需要将其交与数据库进行运算
			if (expression.toLowerCase().indexOf("select") > -1) {
				// 表达式中有select语句，表达式不能参与数据库运算，故将表达式直接赋予结果串
				resultStr = expression;
			} else {
				if (DataBaseType.equals("1")) {// Oracle数据库
					sqlStr = "Select " + expression + " as resultStr from dual";
				} else {// MSSQL or MySQL
					sqlStr = "Select " + expression + " as resultStr";
				}
				try {
					List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sqlStr);
					int length = retlist != null ? retlist.size() : 0;
					LOGGER.info(sqlStr);
					if (length > 0) {
						if (retlist.get(0) != null && retlist.get(0).get("resultStr") != null) {
							resultStr = retlist.get(0).get("resultStr").toString();
						}
					} else {// length == 0
						resultStr = expression; // 当retlist为null时，则表达式有误
					}
				} catch (Exception ex) {
					LOGGER.error("出现异常", ex.getMessage());
				}
				if (resultStr.equals("???")) {
					resultStr = expression;
				}
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("prepareExpression", "执行完成，耗时：" + (endTime - startTime) + " ms.");
		
		return resultStr;
	}

	/**
	 * 根据配置ID等到隐藏列表
	 * @return String
	 */
	private ItemList getResultFieldListHid(String ID) {
		StringBuffer sb = new StringBuffer();
		ItemList ResultList = null;
		QUERY_CONFIG_SHOWFIELD entityList[] = null;
		String strSql = null;
		LOGGER.info("GetResultFieldStrHid开始调用...");
		try {
			if (ID.trim().length() > 0) {
				strSql = "Select * from QUERY_CONFIG_SHOWFIELD WHERE FID='" + ID + "' AND ISSHOW='0' ORDER BY ID";
				List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
				int length = retlist != null ? retlist.size() : 0;
				if (length > 0) {
					entityList = new QUERY_CONFIG_SHOWFIELD[length];
					for (int i = 0; i < length; i++) {
						Map<String, Object> retmap = retlist.get(i);
						entityList[i] = (QUERY_CONFIG_SHOWFIELD) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_SHOWFIELD.class);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("GetResultFieldStrHid出现异常！" + e.getMessage());
			entityList = null;
		}
		if (entityList != null) {
			ResultList = new ItemList();
			String[] tableFieldNames = new String[entityList.length];
			for (int i = 0; i < entityList.length; i++) {
				tableFieldNames[i] = entityList[i].getFIELD();
				if (sb.toString().trim().length() == 0) {
					sb.append(entityList[i].getFIELD());
				} else {
					sb.append("," + entityList[i].getFIELD());
				}
			}
			// ----------------------------------------------------//
			ResultList = getItemFieldByTableField(tableFieldNames);
			// -------------------------------------------------------------//
		}
		return ResultList;
	}

	/**
	 * 根据表名.字段名获取ItemField对象;
	 *
	 * @param tableField
	 *            String 表名.字段名的格式
	 * @param Fid
	 *            查询配置ID
	 * @return ItemField
	 */
	private ItemList getItemFieldByTableField(String[] tableFields) {
		StringBuffer strSb = new StringBuffer("(' '");
		int i;
		for (i = 0; i < tableFields.length; i++) {
			strSb.append(",'" + tableFields[i].toUpperCase() + "'");
		}
		strSb.append(")");
		String sqlStr = "";
		if (DataBaseType.equals("1")) {// oracle数据库
			sqlStr = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.* from bpip_table a, bpip_field b "
					+ "where b.tableid=a.tableid and a.tablename||'.'||b.fieldname in " + strSb.toString();
		}
		else if (DataBaseType.equals("2")) {// mssql数据库
			sqlStr = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.* from bpip_table a, bpip_field b "
					+ "where b.tableid=a.tableid and a.tablename+'.'+b.fieldname in " + strSb.toString();
		}
		else if (DataBaseType.equals("3")) {// mysql数据库
			sqlStr = "select a.TABLENAME,b.* from bpip_table a, bpip_field b "
					+ "where b.tableid=a.tableid and concat(a.tablename,'.',b.fieldname) in " + strSb.toString();
		} else {
		}
		String strID = strSb.toString();
		ItemList itemList = new ItemList();
		itemList = (ItemList) ADDHashtable5.get(strID);
		if (itemList == null) {
			DBRow[] dbMain = this.getTableProperty(sqlStr);
			itemList = new ItemList();
			if (dbMain != null) {
				for (i = 0; i < dbMain.length; i++) {
					if (DataBaseType.equals("3")) {// mysql数据库
						dbMain[i].addColumn("TABLECNNAME", "");
						dbMain[i].Column("TABLECNNAME").setValue(dbMain[i].Column("CHINESENAME").getString());
					}
					itemList.fullFromDbRow(dbMain[i]);
				}
			}
			if (itemList != null) {
				ADDHashtable5.put(strID, itemList);
			}
		}
		return itemList;
	}

	@SuppressWarnings("rawtypes")
	private Hashtable[] initCodeHasTable(DBSet ds, ItemList ResultColList) throws Exception {
		LOGGER.info("initCodeHasTable开始调用...");
		if (ds == null) {
			return null;
		}
		Hashtable NameHashtable[] = null;
		NameHashtable = new Hashtable[3];
		Hashtable<String, Object> userList = new Hashtable<String, Object>();
		Hashtable<String, Object> unitList = new Hashtable<String, Object>();
		Hashtable<String, Object> codeList = new Hashtable<String, Object>();
		StringBuffer sbUser = new StringBuffer();
		sbUser.append(" USERID='' ");
		StringBuffer sbUnit = new StringBuffer();
		sbUnit.append(" UNITID='' ");
		ItemField itemField;
		String columnName;
		String strTmp = null, strTmp1 = null, strTmp2 = null;
		String strCodeTables = "";
		for (int y = 0; y < ResultColList.getItemCount(); y++) {
			itemField = ResultColList.getItemField(y);
			if (itemField.isCode()) {
				if (strCodeTables.length() == 0) {
					strCodeTables = itemField.getCodeTable();
				} else {
					strCodeTables = strCodeTables + "," + itemField.getCodeTable();
				}
			}
		}
		for (int i = 0; i < ds.RowCount(); i++) {
			for (int j = 0; j < ResultColList.getItemCount(); j++) {
				try {
					itemField = ResultColList.getItemField(j);
					columnName = itemField.getName().split("\\.")[1];
					if (itemField.isUser()) {
						strTmp = ds.Row(i).Column(columnName).getString();
						if (strTmp != null) {
							if (sbUser.toString().indexOf(strTmp) != -1) {
								// 已经包含，不增加条件
							} else {
								sbUser.append(" or USERID='" + strTmp + "'");
							}
						}
					}
					if (itemField.isUnit()) {
						strTmp = ds.Row(i).Column(columnName).getString();
						if (strTmp != null) {
							if (sbUnit.toString().indexOf(strTmp) != -1) {
								// 已经包含，不增加条件
							} else {
								sbUnit.append(" or UNITID='" + strTmp + "'");
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		// sbComm.append(")");
		if (sbUser.toString().length() > 10) {
			String[][] cList = getCodeTable("BPIP_USER");
			for (int i = 0; i < cList.length; i++) {
				userList.put(cList[i][0], cList[i][1]);
			}
		}
		if (sbUnit.toString().length() > 10) {
			String[][] cList = getCodeTable("BPIP_UNIT");
			for (int i = 0; i < cList.length; i++) {
				unitList.put(cList[i][0], cList[i][1]);
			}
		}
		try {
			String scodetable[] = strCodeTables.split(",");
			if (scodetable != null && scodetable.length > 0) {
				for (int x = 0; x < scodetable.length; x++) {
					if (scodetable[x].length() > 0) {
						String[][] cList = getCodeTable(scodetable[x]);
						for (int i = 0; i < cList.length; i++) {
							strTmp1 = scodetable[x] + "^" + cList[i][0];
							strTmp2 = cList[i][1];
							codeList.put(strTmp1, strTmp2);
						}
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("获取代码值initCodeHasTable", ex.getMessage());
		}
		NameHashtable[0] = userList;
		NameHashtable[1] = unitList;
		NameHashtable[2] = codeList;

		return NameHashtable;
	}

	/**
	 * 查询配置初始条件
	 * @param FID 配置ID
	 * @return String 查询初始条件串
	 * @throws Exception 
	 */
	private String getInitCondition(String ID, SessionUser User, String P1, 
			String P2, String P3, String P4, String P5) throws Exception {
		LOGGER.info("getInitCondition开始调用...");
		long startTime = System.currentTimeMillis();
		long endTime;
		StringBuffer sb = new StringBuffer("");
		QUERY_CONFIG_INIT entityList[] = null;
		String strSql = "";
		int i;
		ItemField itemField;
		int dbTypeValue;
		String strrvalue = "";
		try {
			if (ID.trim().length() > 0) {
				strSql = "Select * From QUERY_CONFIG_INIT where FID='" + ID + "' ORDER BY ID";
				List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
				int length = retlist != null ? retlist.size() : 0;
				if (length == 0) {
					entityList = null;
				} else {// length > 0
					entityList = new QUERY_CONFIG_INIT[length];
					for (i = 0; i < length; i++) {
						Map<String, Object> retmap = retlist.get(i);
						entityList[i] = (QUERY_CONFIG_INIT) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_INIT.class);
					}
				}
			} else {
				entityList = null;
			}
		} catch (Exception e) {
			LOGGER.error("getInitCondition出现异常！" + e.getMessage());
			entityList = null;
		}
		if (entityList != null) {
			ItemList tmpList = new ItemList();
			String[] tableFieldNames = new String[entityList.length];
			for (i = 0; i < entityList.length; i++) {
				tableFieldNames[i] = entityList[i].getFIELD();
			}
			tmpList = getItemFieldByTableField(tableFieldNames);
			Hashtable<String, Object> tmpHashtable = new Hashtable<String, Object>();
			for (i = 0; i < tmpList.getItemCount(); i++) {
				itemField = tmpList.getItemField(i);
				if (tmpHashtable.get(itemField.getName()) == null) {
					tmpHashtable.put(itemField.getName(), itemField.getType());
				}
			}
			for (i = 0; i < entityList.length; i++) {
				// 在条件表达式中只有存在Pn参数且其相应的值为空，则该条件表达式无效
				// itemField = getItemFieldByTableField(entityList[i].getFIELD());
				if (!entityList[i].getQLEFT().equals("0")) {// 左括号
					sb.append(entityList[i].getQLEFT());
				}
				// dbTypeValue = itemField.getType().getValue();
				dbTypeValue = ((DBType) tmpHashtable.get(entityList[i].getFIELD())).getValue();
				if (entityList[i].getWHEREVALUE().indexOf("P1") > -1 && P1 == null
						|| entityList[i].getWHEREVALUE().indexOf("P2") > -1 && P2 == null
						|| entityList[i].getWHEREVALUE().indexOf("P3") > -1 && P3 == null
						|| entityList[i].getWHEREVALUE().indexOf("P4") > -1 && P4 == null
						|| entityList[i].getWHEREVALUE().indexOf("P5") > -1 && P5 == null) {
					sb.append(" 1=1 ");
				} else {
					LOGGER.info("初始条件计算字段：" + entityList[i].getFIELD());
					LOGGER.info("初始条件计算前：" + entityList[i].getWHEREVALUE());
					strrvalue = getConditionExpression(entityList[i].getFIELD(), entityList[i].getSYMBOL(), 
							entityList[i].getWHEREVALUE(), dbTypeValue, User, P1, P2, P3, P4, P5);
					
					sb.append(strrvalue);
				}
				if (!entityList[i].getQRIGHT().equals("0")) { // 右括号
					sb.append(entityList[i].getQRIGHT());
				}
				if (i + 1 < entityList.length) { // 最后一个条件行无论何时不能加入逻辑符,以统一处理
					if (entityList[i].getLOGIC().equals("1")) {
						sb.append(" AND ");
					}
					if (entityList[i].getLOGIC().equals("2")) {
						sb.append(" OR ");
					}
				}
			}
		}
		if (sb.toString().trim().length() == 0) {
			sb.append(" 1=1 ");
		}
		LOGGER.info("初始条件：" + sb.toString());
		endTime = System.currentTimeMillis();
		LOGGER.info("getInitCondition", "执行完成，耗时：" + (endTime - startTime) + " ms.");
		return sb.toString();
	}

	/**
	 * 建立条件表达式
	 * @param tableField 表名.字段名
	 * @param symbol 表达式符号(>,=,<,Like等)
	 * @param conditionValue 表达式的值
	 * @param dbType 数据类型的值
	 * @return String
	 * @throws Exception 
	 */
	private String getConditionExpression(String tableField, String symbol, String conditionValue, 
			int dbType, SessionUser User, String P1, String P2, String P3, String P4, String P5) throws Exception {
		StringBuffer sb = new StringBuffer();
		String conditionValueTmp;
		// sb.append(" " + tableField + " "); //表名.字段名;
		// sb.append(" " + symbol + " "); //条件符
		conditionValue = prepareExpression(conditionValue, User, P1, P2, P3, P4, P5);
		conditionValueTmp = conditionValue;
		if (dbType == DBType.DATE.getValue() || dbType == DBType.DATETIME.getValue()) { // 日期型
			try {
				Date date = new Date(); // 测试若为日期型时，是否为有效的日期字串值
				DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					date = fmt.parse(conditionValue);
				} catch (Exception ex) {
					fmt = new SimpleDateFormat("yyyy-MM-dd");
					date = fmt.parse(conditionValue);
				}
				fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if (DataBaseType.equals("2")) {// mssql数据库
					conditionValueTmp = " '" + fmt.format(date) + "'";
				} else {
					conditionValueTmp = " to_date('" + fmt.format(date) + "','yyyy-mm-dd  hh24:mi:ss')";
				}

			} catch (Exception ex) {
				if (symbol.equals(">") || symbol.equals(">=")) {
					if (DataBaseType.equals("1")) {//Oracle数据库
						conditionValueTmp = " to_date('1000-01-01','yyyy-mm-dd hh24:mi:ss')";
					} else {
						conditionValueTmp = " '1000-01-01'";
					}
				} else if (symbol.equals("<") || symbol.equals("<=")) {
					if (DataBaseType.equals("1")) {//Oracle数据库
						conditionValueTmp = " to_date('3000-01-01','yyyy-mm-dd hh24:mi:ss')";
					} else {
						conditionValueTmp = " '3000-01-01'";
					}

				} else if (symbol.equals("!=")) {
					if (DataBaseType.equals("1")) {//Oracle数据库
						conditionValueTmp = " to_date('1000-01-01','yyyy-mm-dd  hh24:mi:ss')";
					} else {
						conditionValueTmp = " '1000-01-01'";
					}
				} else {
					conditionValueTmp = "sysdate";
				}
			}
		} else if (symbol.equalsIgnoreCase("Like") || symbol.equalsIgnoreCase("Not Like")) {
			if (conditionValue.indexOf("%") > -1) {
				conditionValueTmp = conditionValue;
				// sb.append(conditionValue);
			} else {
				// 全包含的情况，若开始或结束的字符为单引号"'"，则需将其去掉
				if (conditionValue.startsWith("'")) {
					conditionValue = conditionValue.substring(1);
				}
				if (conditionValue.endsWith("'")) {
					conditionValue = conditionValue.substring(0, conditionValue.length() - 1);
				}
				conditionValueTmp = "'%" + conditionValue + "%'";
				// sb.append("'%" + conditionValue + "%'");
			}
		} else if (dbType == DBType.STRING.getValue() && !conditionValue.equalsIgnoreCase("null")) {
			if (!conditionValue.startsWith("'") 
					&& conditionValue.toLowerCase().indexOf("select") == -1
					&& !conditionValue.startsWith("(")) {
				conditionValue = "'" + conditionValue;
			}
			if (!conditionValue.endsWith("'")
					&& conditionValue.toLowerCase().indexOf("select") == -1
					&& !conditionValue.endsWith(")")) {
				conditionValue = conditionValue + "'";
			}
			conditionValueTmp = conditionValue;
			// sb.append(conditionValue);
		} else {
			conditionValueTmp = conditionValue;
			// sb.append(conditionValue);
		}
		String[] QfieldAry = QRelationField(tableField);
		if (QfieldAry != null) {
			sb.append(" ( " + tableField + " "); // 表名.字段名;
			sb.append(" " + symbol + " "); // 条件符
			sb.append(conditionValueTmp);
			for (int i = 0; i < QfieldAry.length; i++) {
				if (!QfieldAry[i].equals("0") && !QfieldAry[i].equals("100")) {
					sb.append("  Or " + QfieldAry[i] + " " + symbol + " " + conditionValueTmp);
				}
			}
			sb.append(" )");
		} else {
			sb.append(" " + tableField + " "); // 表名.字段名;
			sb.append(" " + symbol + " "); // 条件符
			sb.append(conditionValueTmp);
		}
		return sb.toString();
	}

	private String[] QRelationField(String tableField) throws Exception {
		String[] strAry = null;
		String strTmp = "";
		String strSql = "";
		if (DataBaseType.equals("1")) {// oracle数据库
			strSql = "Select QFIELD from BPIP_FIELD A Inner Join BPIP_TABLE B On A.Tableid = B.Tableid "
					+ "Where B.Tablename||'.'||A.Fieldname='" + tableField + "'";
		}
		if (DataBaseType.equals("2")) {// mssql数据库
			strSql = "Select QFIELD from BPIP_FIELD A Inner Join BPIP_TABLE B On A.Tableid = B.Tableid "
					+ "Where B.Tablename+'.'+A.Fieldname='" + tableField + "'";
		}
		if (DataBaseType.equals("3")) {// mysql数据库
			strSql = "Select QFIELD from BPIP_FIELD A Inner Join BPIP_TABLE B On A.Tableid = B.Tableid "
					+ "Where concat(B.Tablename,'.',A.Fieldname)='" + tableField + "'";
		}
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
		if (retlist != null && retlist.size() > 0) {
			Map<String, Object> retmap = retlist.get(0);
			if (retmap != null && retmap.get("QFIELD") != null) {
				strTmp = retmap.get("QFIELD").toString();
			}
		}
		if (strTmp != null && strTmp.trim().length() > 0) {
			strAry = strTmp.split(",");
		}
		return strAry;
	}

	/**
	 * 设置个人查询条件
	 * @param userID
	 * @param strWhere
	 * @throws Exception 
	 */
	private void setQueryWhere(String userID, String strWhere) throws Exception {
		strWhere = strWhere.replaceAll("'", "''");
		if (strWhere.trim().length() == 0) {
			strWhere = " 1=1 ";
		}
		String strSql = "update QUERY_TMP set TMP='" + strWhere + "' where USERID='" + userID + "'";
		userMapper.updateExecSQL(strSql);
	}

	/**
	 * 获取配置关系表字串，例如 主表,主表1，主表2....
	 * @param ID 配置ID
	 * @return String
	 * @throws Exception 
	 */
	private String getFromTableStr(String ID) throws Exception {
		StringBuffer sb = new StringBuffer();
		String strSql = "";
		String tableName;
		QUERY_CONFIG_TABLE entityObj = null;
		entityObj = getConfigTable(ID);
		sb.append(entityObj.getMAINTABLE() + " ");
        strSql = "select * from QUERY_CONFIG_CONNECTION Where FID='" + ID + "' order by ID";
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
        int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				tableName = retmap.get("CFIELD").toString().split("\\.")[0];
				if (sb.toString().indexOf(tableName + " ") == -1) {
					sb.append("," + tableName + " ");
				}
				tableName = retmap.get("MFIELD").toString().split("\\.")[0];
				if (sb.toString().indexOf(tableName + " ") == -1) {
					sb.append("," + tableName + " ");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 根据配置ID等到关联表关系字串
	 * @param FID 查询配置ID
	 * @return String
	 */
	private String getConnectionStr(String ID) {
		StringBuffer sb = new StringBuffer(" 1=1 ");
		QUERY_CONFIG_CONNECTION entityList[] = null;
		String strSql = null;
		LOGGER.info("getConnectionStr开始调用...");
		long startTime = System.currentTimeMillis();
		try {
			if (ID.trim().length() > 0) {
				strSql = "select * from QUERY_CONFIG_CONNECTION Where FID='"+ID+"' order by ID";
				List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
				int length = retlist != null ? retlist.size() : 0;
				if (length > 0) {
					entityList = new QUERY_CONFIG_CONNECTION[length];
					for (int i = 0; i < length; i++) {
						Map<String, Object> retmap = retlist.get(i);
						entityList[i] = (QUERY_CONFIG_CONNECTION) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_CONNECTION.class);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("getConnectionStr出现异常！" + e.getMessage());
			entityList = null;
		}
		if (entityList != null) {
			for (int i = 0; i < entityList.length; i++) {
				if (entityList[i].getJOINTYPE().equals("1")) {// 一般相等联接
					sb.append(" And " + entityList[i].getCFIELD() + "=" + entityList[i].getMFIELD() + " ");
				} else if (entityList[i].getJOINTYPE().equals("2")) { // 左外关联
					sb.append(" And " + entityList[i].getCFIELD() + "=" + entityList[i].getMFIELD() + "(+) ");
				} else if (entityList[i].getJOINTYPE().equals("3")) { // 右外关联
					sb.append(" And (+)" + entityList[i].getCFIELD() + "=" + entityList[i].getMFIELD() + " ");
				} else {
					sb.append(" And " + entityList[i].getCFIELD() + "=" + entityList[i].getMFIELD() + " ");
				}
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("getConnectionStr执行完成，耗时：" + (endTime - startTime) + " ms.");
		
		return sb.toString();
	}

	/**
	 * 根据配置ID得到主表的主键
	 * @param ID
	 * @return
	 * @throws Exception 
	 */
	private String getTablePID(String ID) throws Exception {
		String strSql = "";
		String tableName = "";
		String PID = "";
		
		QUERY_CONFIG_TABLE entityObj = getConfigTable(ID);
		tableName = entityObj.getMAINTABLE();
		
		strSql = "Select PRIMARYKEY from BPIP_TABLE Where TABLENAME='" + tableName + "'";
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
		if (retlist != null && retlist.size() > 0) {
			if (retlist.get(0) != null && retlist.get(0).get("PRIMARYKEY") != null) {
				PID = retlist.get(0).get("PRIMARYKEY").toString();
			}
		}
		return PID;
	}

	/**
	 * 根据表名.字段名获取ItemField对象;
	 * @param tableField 表名.字段名的格式
	 * @return ItemField
	 */
	private ItemField getItemFieldByTableField(String tableField) {
		ItemField itemField = null;
		int i;
		String sqlStr = "";
		if (DataBaseType.equals("1")) {// oracle数据库
			sqlStr = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.* from bpip_table a, bpip_field b "
					+ "where b.tableid=a.tableid and a.tablename||'.'||b.fieldname='" + tableField + "' ";
		}
		else if (DataBaseType.equals("2")) {// mssql数据库
			sqlStr = "select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.* from bpip_table a, bpip_field b "
					+ "where b.tableid=a.tableid and a.tablename+'.'+b.fieldname='" + tableField + "' ";
		}
		else if (DataBaseType.equals("3")) {// mysql数据库
			sqlStr = "select a.TABLENAME,b.* from bpip_table a, bpip_field b "
					+ "where b.tableid=a.tableid and concat(a.tablename,'.',b.fieldname)='" + tableField + "' ";
		} else {
		}
		ItemList itemList = new ItemList();
		itemList = (ItemList) ADDHashtable4.get(tableField);
		if (itemList == null) {
			DBRow[] dbMain = this.getTableProperty(sqlStr);
			itemList = new ItemList();
			if (dbMain != null) {
				for (i = 0; i < dbMain.length; i++) {
					if (DataBaseType.equals("3")) {// mysql数据库
						dbMain[i].addColumn("TABLECNNAME", "");
						dbMain[i].Column("TABLECNNAME").setValue(dbMain[i].Column("CHINESENAME").getString());
					}
					itemList.fullFromDbRow(dbMain[i]);
				}
			}
			if (itemList != null) {
				ADDHashtable4.put(tableField, itemList);
			}
		}
		if (itemList.getItemCount() > 0) {
			itemField = itemList.getItemField(0);
		}
		return itemField;
	}

	public void inithashtable() {
		CodeHashtable.clear();
		ColListHashtable.clear();
		ColQListHashtable.clear();
		ADDHashtable1.clear();
		ADDHashtable2.clear();
		ADDHashtable3.clear();
		ADDHashtable4.clear();
		ADDHashtable5.clear();
		ADDHashtable6.clear();
		ADDHashtable7.clear();
		ADDHashtable8.clear();
		ADDHashtable9.clear();
		dbEngine.inithashtable();
	}

	private void doMemory_Manage() {
		LOGGER.info("内存清理开始。");
		inithashtable();
		LOGGER.info("内存清理清结束。");
	}

	class ScanTask extends TimerTask {
		public void run() {
			LOGGER.info("查询引擎扫描任务开始！");
			doMemory_Manage();
		}
	}

}