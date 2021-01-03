package com.yonglilian.service.impl;

import com.yonglilian.service.FlowAutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBSet;
import zr.zrpower.common.util.SysPreperty;

import java.util.*;


/**
 * 流程引擎自动执行服务---用于处理超时提示及自动活动处理等
 * @author lwk
 *
 */
@Service
public class FlowAutoServiceImpl implements FlowAutoService {
	/** The FlowAutoServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowAutoServiceImpl.class);
	private DBEngine dbEngine; // 数据库引擎
	static String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型

	private static int clients = 0;
	private Timer timerListener; // 队列侦听器
	private boolean isRun = false; // 侦听是否运行
	private int intadd = 0; // 本次增加短信的记录数

	public FlowAutoServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
		}
		clients++;
	}

	@Override
	public boolean startScan() {
		if (!isRun) {
			// 间隔扫描的秒数
			int intnum = 300;// 5分钟扫描一次
			// int intnum = 10;//10秒扫描一次
			timerListener = new Timer();
			timerListener.schedule(new ScanTask(), 0, intnum * 1000);
			isRun = true;
			LOGGER.info("FlowAutoServer is Start Runing ....");
		}
		return isRun;
	}

	// 扫描任务
	class ScanTask extends TimerTask {
		public void run() {
			LOGGER.info("扫描任务开始！");
			// 流程超时提示处理
			flowAbnormity();
			// 处理流程步骤改变后出问题的流程
			updateFlowData();
		}
	}

	// 处理流程步骤改变后出问题的流程
	private void updateFlowData() {
		// 删除已经没有流程的流转记录
		String strSQL = "";
		strSQL = "delete from flow_runtime_process where not exists ("
				+ "select identification from flow_config_process "
				+ "where identification=flow_runtime_process.flowid)";
		dbEngine.ExecuteSQL(strSQL);
		// 通过名称能够更新的先更新
		String str2 = "";
		String str4 = "";
		String str5 = "";
		String strTmp = "";
		String isEnd = "";
		strSQL = "select a.ID,a.FLOWID,a.NAME,a.CURRACTIVITY,c.FID from flow_runtime_process a left join FLOW_CONFIG_PROCESS  b  on a.FLOWID=b.identification left join flow_config_activity c on a.curractivity = c.id  where c.id IS NULL";
		DBSet dbset = dbEngine.QuerySQL(strSQL);
		DBSet dbset1 = null;
		if (dbset != null && dbset.RowCount() > 0) {
			for (int i = 0; i < dbset.RowCount(); i++) {
				str2 = dbset.Row(i).Column("NAME").getString();
				str4 = dbset.Row(i).Column("ID").getString();
				str5 = dbset.Row(i).Column("FID").getString();
				strTmp = "";
				try {
					strTmp = str2.split("→")[1];
				} catch (Exception ex) {
				}
				// 名称包含打印或者找不到的设置成结束状态
				isEnd = "0";
				if (str2.indexOf("打印") != -1) {
					isEnd = "1";
				}
				strSQL = "select ID from FLOW_CONFIG_ACTIVITY where NAME = '" + strTmp + "'";
				dbset1 = dbEngine.QuerySQL(strSQL);
				if (dbset1 != null && dbset1.RowCount() > 0) {
					strTmp = dbset1.Row(0).Column("ID").getString();
				} else {
					isEnd = "1";
				}
				if (isEnd.equals("1")) {
					// 查找本流程结束活动的编号
					strSQL = "select ID from FLOW_CONFIG_ACTIVITY where FID='" + str5 + "' and NAME = '结束'";
					dbset1 = dbEngine.QuerySQL(strSQL);
					strTmp = "";
					if (dbset1 != null && dbset1.RowCount() > 0) {
						strTmp = dbset1.Row(0).Column("ID").getString();
					}
					// 更新流程为结束
					if (strTmp.length() > 0) {
						strSQL = "update flow_runtime_process set CURRACTIVITY='" + strTmp + "',STATE='4' where ID='" + str4 + "'";
						dbEngine.ExecuteSQL(strSQL);
					}
				} else {
					// 更新流程
					if (strTmp.length() > 0) {
						strSQL = "update flow_runtime_process set CURRACTIVITY='" + strTmp + "' where ID='" + str4 + "'";
						dbEngine.ExecuteSQL(strSQL);
					}
				}
			}
		}
	}

	/**
	 * 功能：处理流程超时提示
	 */
	private void flowAbnormity() {
		try {
			String strACCEPTPSN = ""; // 当前接收人列表
			String strISABNORMITY = ""; // 是否已经异常处理
			String ABNORMITYID = "";// 超时处理类型
			String strNAME = "";// 流程名称
			String strMessage = "";// 提示内容
			int longDAY = 0; // 持续时间(小时数)
			int longFREQUENCY = 0; // 执行频率(小时数)
			String strExecute_No = "";
			int todayNum = 0; // 接收日期与现在的相差小时数
			int todayNum1 = 0; // 异常处理日期与现在的相差小时数
			int maxrow = 0;
			Calendar cal = Calendar.getInstance();
			Date cdate = cal.getTime();// 当前时间
			Vector<Object> mVec = new Vector<Object>(); // 存放所有SQL语句
			StringBuffer addBuf = new StringBuffer();

			// 只提示流程创建日期在两个月内的流程
			String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
			if (DataBaseType.equals("2")) {// mssql
				addBuf.append("select b.ID,b.CURRACTIVITY,b.ACCEPTPSN,b.ACCEPTDATE,b.ABNORMITYDATE,b.ISABNORMITY,b.NAME,a.ABNORMITYID,a.DAY,a.FREQUENCY "
						+ "from FLOW_CONFIG_TIME a left join FLOW_RUNTIME_PROCESS b on a.FID=b.CURRACTIVITY "
						+ "where b.STATE<>'2' and b.STATE<>'3' and b.STATE<>'4' and datediff(d,b.CREATEDATE,getdate())<=2 order by b.ISABNORMITY,b.CREATEDATE");
			} else {
				addBuf.append("select b.ID,b.CURRACTIVITY,b.ACCEPTPSN,b.ACCEPTDATE,b.ABNORMITYDATE,b.ISABNORMITY,b.NAME,a.ABNORMITYID,a.DAY,a.FREQUENCY "
						+ "from FLOW_CONFIG_TIME a left join FLOW_RUNTIME_PROCESS b on a.FID=b.CURRACTIVITY "
						+ "where b.STATE<>'2' and b.STATE<>'3' and b.STATE<>'4' and (sysdate-b.CREATEDATE)*1440<86400 order by b.ISABNORMITY,b.CREATEDATE");
			}
			DBSet dbset1 = dbEngine.QuerySQL(addBuf.toString());
			if (dbset1 != null && dbset1.RowCount() > 0) {
				if (dbset1.RowCount() > 80) {// 一次只处理80条记录
					maxrow = 80;
				} else {
					maxrow = dbset1.RowCount();
				}
				intadd = 0;// 本次增加短信的记录数为0
				String strMaxYID = getMaxFieldNo("BPIP_HANDSET", "ID", 20);// 待增加的最大短信ID
				for (int i = 0; i < maxrow; i++) {
					strExecute_No = dbset1.Row(i).Column("ID").getString(); // 得到流程流转的ID
					strACCEPTPSN = dbset1.Row(i).Column("ACCEPTPSN").getString(); // 得到当前接收人列表
					todayNum = getHours(dbset1.Row(i).Column("ACCEPTDATE").getDate(), cdate);// 得到接收日期与今天的相差小时数
					todayNum1 = getHours(dbset1.Row(i).Column("ABNORMITYDATE").getDate(), cdate);// 得到异常处理与今天的相差小时数
					strISABNORMITY = dbset1.Row(i).Column("ISABNORMITY").getString(); // 得到是否已经异常处理
					ABNORMITYID = dbset1.Row(i).Column("ABNORMITYID").getString(); // 得到超时处理类型
					strNAME = dbset1.Row(i).Column("NAME").getString(); // 得到流程名称
					longDAY = dbset1.Row(i).Column("DAY").getInteger();
					longFREQUENCY = dbset1.Row(i).Column("FREQUENCY").getInteger();

					addBuf.delete(0, addBuf.length());// 清空
					addBuf.append("提示:您收到的[" + strNAME + "]已经超过" + String.valueOf(longDAY)).append("小时未处理,请及时处理。");
					strMessage = addBuf.toString();
					LOGGER.info("相差小时数:", String.valueOf(todayNum));

					if ((todayNum > longDAY && longDAY != 0 && strISABNORMITY.equals("0"))
							|| (todayNum > longDAY && longDAY != 0 && strISABNORMITY.equals("1")
									&& todayNum1 > longFREQUENCY && longFREQUENCY != 0)) {// 达到处理的条件
						if (ABNORMITYID.equals("01")) {// 消息提示
							// 增加插入消息提示的sql
							mVec = getSendFlowMessage(strACCEPTPSN, strMessage, mVec);
						}
						else if (ABNORMITYID.equals("02")) {// 短信提示
							// ----------
							mVec = getSendHandSetMessage(strACCEPTPSN, strMessage, strMaxYID, mVec);
						} else {
						}
						// 更新流程的异常处理状态
						addBuf.delete(0, addBuf.length());// 清空
						addBuf.append("update FLOW_RUNTIME_PROCESS set ISABNORMITY='1',ABNORMITYDATE=sysdate where ID='")
							  .append(strExecute_No + "'");
						mVec.add(addBuf.toString());
					}
				}
				// 将所有SQL装入数组
				if (mVec.size() > 100) {
					String[] sqls = new String[100];
					String[] sqls1 = new String[mVec.size() % 100];
					int k = 0;
					int x = mVec.size();
					for (int j = 0; j < mVec.size(); j++) {
						if (x >= 100) {
							sqls[k] = (String) mVec.get(j);
						} else {
							sqls1[k] = (String) mVec.get(j);
						}
						k++;
						if (k == 100 || mVec.size() == (j + 1)) {
							// 执行相关sql
							if (x >= 100) {
								dbEngine.ExecuteSQLs(sqls);
							} else {
								dbEngine.ExecuteSQLs(sqls1);
							}
							k = 0;
							x = mVec.size() - j - 1;
						}
					}
				} else {
					String[] sqls = new String[mVec.size()];
					for (int j = 0; j < mVec.size(); j++) {
						sqls[j] = (String) mVec.get(j);
					}
					// 执行相关sql
					dbEngine.ExecuteSQLs(sqls);
				}
				dbset1 = null;// 赋空值
			}
		} catch (Exception ex) {
			LOGGER.info("流程引擎---FlowAbnormity函数出错：" + ex.toString());
			System.out.print("流程引擎---FlowAbnormity函数出错：" + ex.toString());
		}
	}

	/**
	 * 功能：增加发送提示消息的SQL
	 * @param strUserNos 待处理人编号字符串
	 * @param strMessage 提示的消息内容
	 * @return sql
	 */
	private Vector<Object> getSendFlowMessage(String strUserNos, String strMessage, Vector<Object> mVec) {
		String tmpUserID = ""; // 临时用户编号
		List<Object> UserList = new ArrayList<Object>();
		StringBuffer addBuf = new StringBuffer();
		UserList = getArrayList(strUserNos, ",");
		for (int i = 0; i < UserList.size(); i++) {
			tmpUserID = UserList.get(i).toString();
			addBuf.delete(0, addBuf.length());// 清空
			// 插入消息
			addBuf.append("Insert into BPIP_MSGCONTENT(CUSERNO,CONTENT,SENDDATE,ISCK) values ('")
				  .append(tmpUserID + "','" + strMessage + "',sysdate,'0')");
			mVec.add(addBuf.toString());
		}
		return mVec;
	}

	/**
	 * 功能：增加发送短信提示的SQL
	 * @param strUserNos 待处理人编号字符串
	 * @param strMessage 提示的内容
	 * @return sql
	 */
	private Vector<Object> getSendHandSetMessage(String strUserNos, String strMessage, String strMaxYID, Vector<Object> mVec) {
		List<Object> UserList = new ArrayList<Object>();
		String strMaxID = "";
		String strMobiles = "";
		String strMobile = "";
		String strUserNo = "";

		StringBuffer addBuf = new StringBuffer();
		UserList = getArrayList(strUserNos, ",");
		// -------------插入数据---------------
		UserList = getArrayList(strUserNos, ",");
		for (int i = 0; i < UserList.size(); i++) {
			strUserNo = UserList.get(i).toString();
			// 得到提醒人员的手机号
			strMobiles = GetUserMobile(strUserNo);
			if (strMobiles.length() > 0) {
				List<Object> arrList1 = new ArrayList<Object>();
				arrList1 = getArrayList(strMobiles, ",");
				for (int j = 0; j < arrList1.size(); j++) {
					strMobile = arrList1.get(j).toString();
					// 超过70个字的短信分两条发送
					if (strMessage.length() > 70) {
						strMaxID = getMaxFieldNoAdd(strMaxYID, 20, intadd);
						addBuf.delete(0, addBuf.length());// 清空
						addBuf.append("Insert into BPIP_HANDSET(ID,USERNO,MOBILE,CONTENT,SENDDATE,FINISHDATE,TYPEBOX,SENDTYPE,MONTH,WEEK,DAY,HOUR,MINUTE,ISSEND,TTABLEID,SUSERNO) values ('"
										+ strMaxID + "','0000000000000000','" + strMobile + "','" + strMessage.substring(0, 70) 
										+ "',sysdate,sysdate,5,0,0,0,0,0,0,'0','','" + strUserNo + "')");
						mVec.add(addBuf.toString());
						intadd++;
						strMaxID = getMaxFieldNoAdd(strMaxYID, 20, intadd);
						addBuf.delete(0, addBuf.length());// 清空
						addBuf.append("Insert into BPIP_HANDSET(ID,USERNO,MOBILE,CONTENT,SENDDATE,FINISHDATE,TYPEBOX,SENDTYPE,MONTH,WEEK,DAY,HOUR,MINUTE,ISSEND,TTABLEID,SUSERNO) values ('"
										+ strMaxID + "','0000000000000000','" + strMobile + "','" + strMessage.substring(71) 
										+ "',sysdate,sysdate,5,0,0,0,0,0,0,'0','','" + strUserNo + "')");
						mVec.add(addBuf.toString());
						intadd++;
					} else {
						strMaxID = getMaxFieldNoAdd(strMaxYID, 20, intadd);
						addBuf.delete(0, addBuf.length());// 清空
						addBuf.append("Insert into BPIP_HANDSET(ID,USERNO,MOBILE,CONTENT,SENDDATE,FINISHDATE,TYPEBOX,SENDTYPE,MONTH,WEEK,DAY,HOUR,MINUTE,ISSEND,TTABLEID,SUSERNO) values ('"
										+ strMaxID + "','0000000000000000','" + strMobile + "','" + strMessage
										+ "',sysdate,sysdate,5,0,0,0,0,0,0,'0','','" + strUserNo + "')");
						mVec.add(addBuf.toString());
						intadd++;
					}
				}
			}
		}
		// ----------------------------------------
		return mVec;
	}

	/**
	 * 功能：根据人员编号得到手机号。
	 * 
	 * @param strUserNo
	 *            人员编号
	 * @return returnValue 返回手机号
	 */
	private String GetUserMobile(String strUserNo) {
		StringBuffer strSql = new StringBuffer();
		String returnValue = ""; // 返回值
		strSql.append("Select MOBILE From BPIP_USER where USERID='" + strUserNo + "'");
		DBSet dbset0 = dbEngine.QuerySQL(strSql.toString());
		if (dbset0 != null && dbset0.RowCount() > 0) {
			returnValue = dbset0.Row(0).Column("MOBILE").getString();
			dbset0 = null;// 赋空值
		}
		return returnValue;
	}

	// 得到两个日期相差的小时数
	private int getHours(Date startday, Date endday) {
		// 确保startday在endday之前
		if (startday.after(endday)) {
			Date cal = startday;
			startday = endday;
			endday = cal;
		}
		// 分别得到两个时间的毫秒数
		long sl = startday.getTime();
		long el = endday.getTime();
		long ei = el - sl;
		// 根据毫秒数计算间隔小时数
		return (int) (ei / (1000 * 60 * 60));
	}

	/**
	 * 功能或作用：分析规则字符串，生成数组
	 * @param strItems 字符串
	 * @param strItemMark 标识符
	 * @return 返回数组
	 */
	private List<Object> getArrayList(String strItems, String strItemMark) {
		int intItemLen, i = 0, n = 0;
		strItems = strItems + strItemMark;
		String strItem;
		List<Object> strList = new ArrayList<Object>();
		intItemLen = strItems.length();
		while (i < intItemLen) {
			n = strItems.indexOf(strItemMark, i);
			strItem = strItems.substring(i, n);
			strList.add(strItem);
			i = n + 1;
		}
		return strList;
	}

	/**
	 * 功能或作用：取出最大的记录流水号
	 * @param TableName 数据库表名
	 * @param FieldName 数据库字段名称
	 * @param FieldLen 数据库字段长度
	 * @Return MaxNo 执行后返回一个MaxNo字符串
	 */
	private String getMaxFieldNo(String TableName, String FieldName, int FieldLen) {
		String MaxNo = "";
		int LenMaxNo = 0;
		StringBuffer strSql = new StringBuffer();
		strSql.append("SELECT MAX(" + FieldName + ") AS MaxNo FROM " + TableName);
		try {
			DBSet dbset = dbEngine.QuerySQL(strSql.toString());
			if (dbset != null && dbset.RowCount() > 0) {
				MaxNo = dbset.Row(0).Column("MaxNo").getString();
				if (MaxNo != null && MaxNo.length() > 0) {
					MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
					LenMaxNo = MaxNo.length();
					MaxNo = "0000000000000000000000000" + MaxNo;
				} else {
					MaxNo = "00000000000000000000000001";
					LenMaxNo = 1;
				}
				dbset = null;// 赋空值
			}
			MaxNo = MaxNo.substring(25 - FieldLen + LenMaxNo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return MaxNo;
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