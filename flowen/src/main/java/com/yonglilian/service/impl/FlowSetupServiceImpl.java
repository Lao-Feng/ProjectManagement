package com.yonglilian.service.impl;

import com.yonglilian.common.util.FunctionMessage;
import com.yonglilian.common.util.ReflectionUtil;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.UserMapper;
import com.yonglilian.dao.mapper.FlowDAOMapper;
import com.yonglilian.flowengine.mode.base.Button;
import com.yonglilian.flowengine.mode.base.Package;
import com.yonglilian.flowengine.mode.base.Variable;
import com.yonglilian.service.FlowSetupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBSet;

import java.util.List;
import java.util.Map;

/**
 * 流程引擎-流程基本属性管理服务层实现
 * @author lwk
 *
 */
@Service
public class FlowSetupServiceImpl implements FlowSetupService {
	/** The FlowSetupServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowSetupServiceImpl.class);
	FlowControlServiceImpl flowControlService = new FlowControlServiceImpl();
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
	/** 流程引擎相关接口的DAO层. */
	@Autowired
	private FlowDAOMapper flowDAOMapper;
	/** 数据库引擎. */
	private DBEngine dbEngine;
	static private int clients = 0;

	/**
	 * 构造方法
	 */
	public FlowSetupServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
		}
		clients = 1;
	}

	@Override
	public FunctionMessage addFlowPk(Package FlowPk, String UNITID) throws Exception {
		FunctionMessage funMsg = new FunctionMessage(1);
		try {
			String strMaxNo = getMaxFieldNo("FLOW_CONFIG_PACKAGE", "ID", 3);
			FlowPk.setID(strMaxNo);// 设置ID
			String strTmp = UNITID;
			if (strTmp.length() > 6) {
				strTmp = strTmp.substring(0, 6);
			}
			StringBuffer addBuf = new StringBuffer();
			addBuf.append("FP" + strTmp + strMaxNo);
			FlowPk.setIDENTIFICATION(addBuf.toString());// 设置标识
			if (dbEngine.ExecuteInsert(FlowPk.getData())) {
				funMsg.setMessage("流程包录入成功");
				funMsg.setResult(true);
			}
			FlowPk = null;
		} catch (Exception ex) {
			funMsg.setResult(false);
			funMsg.setMessage("调用方法addFlowPk出现异常" + ex.toString());
			LOGGER.error("调用方法addFlowPk出现异常", ex);
		}
		return funMsg;
	}

	@Override
	public FunctionMessage editFlowPk(Package FlowPk) throws Exception {
		FunctionMessage funMsg = new FunctionMessage(1);
		try {
			boolean isOk = dbEngine.ExecuteEdit(FlowPk.getData(), "ID=" + FlowPk.getID());
			if (isOk) {
				funMsg.setResult(true);
				funMsg.setMessage("流程包修改成功");
			} else {
				funMsg.setResult(false);
				funMsg.setMessage("流程包【" + FlowPk.getID() + "】不存在");
			}
			FlowPk = null;
		} catch (Exception ex) {
			funMsg.setResult(false);
			funMsg.setMessage("调用方法editFlowPk出现异常" + ex.toString());
			LOGGER.error("调用方法addFlowPk出现异常", ex);
		}
		return funMsg;
	}

	@Override
	public boolean deleteFlowPk(String strID) throws Exception {
		FunctionMessage funMsg = new FunctionMessage(1);
		// 检查要删除的流程包是否有下级流程
		StringBuffer addBuf = new StringBuffer();
		addBuf.append("Select count(ID) as MenuCount From FLOW_CONFIG_PROCESS "
					+ "Where FLOWPACKAGE in(select ID FROM FLOW_CONFIG_PACKAGE where ID='"+strID+"')");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			int menuCount = Integer.parseInt(retlist.get(0).get("MenuCount").toString());
			if (menuCount > 0) {
				funMsg.setMessage("该流程包与系统其它模块有关联，不能删除！");
				return false;
			}
		}
		addBuf.delete(0, addBuf.length());// 清空
		addBuf.append("Delete From FLOW_CONFIG_PACKAGE Where ID='" + strID + "'");
		Integer retInt = userMapper.deleteExecSQL(addBuf.toString());
		if (retInt != null && retInt > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Package[] getFlowPackageList(String strwhere) throws Exception {
		Package bgs[] = null;
		try {
			String strSql = strwhere;
			DBSet mdbset = dbEngine.QuerySQL(strSql);
			bgs = new Package[mdbset.RowCount()];
			if (mdbset.RowCount() == 0) {
				return null;
			} else {
				for (int i = 0; i < mdbset.RowCount(); i++) {
					bgs[i] = new Package();
					bgs[i].fullData(mdbset.Row(i));
				}
				mdbset = null;// 赋空值
			}
		} catch (Exception ex) {
			bgs = null;
			LOGGER.error("调用方法getFlowPackageList出现异常", ex);
		}
		return bgs;
	}

	@Override
	public Package[] getFlowPackageList1(String strFID) throws Exception {
		Package bgs[] = null;
		StringBuffer addBuf = new StringBuffer();
		try {
			if (strFID.length() > 0) {
				addBuf.append("Select * from FLOW_CONFIG_PACKAGE where FID='" + strFID + "' order by ID");
			} else {
				addBuf.append("Select * from FLOW_CONFIG_PACKAGE where FID='000' order by ID");
			}
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				return null;
			} else {// length > 0
				bgs = new Package[length];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					bgs[i] = (Package) ReflectionUtil.convertMapToBean(retmap, Package.class);
				}
			}
		} catch (Exception ex) {
			bgs = null;
			LOGGER.error("调用方法getFlowPackageList1出现异常", ex);
		}
		return bgs;
	}

	@Override
	public Package[] getFlowPackageList2(String strFID) throws Exception {
		Package bgs[] = null;
		StringBuffer addBuf = new StringBuffer();
		try {
			if (strFID.length() > 0) {
				addBuf.append("Select a.ID,a.IDENTIFICATION,a.NAME,a.STATUS,a.CREATEDATE,b.NAME AS CREATEPSN "
						+ "from FLOW_CONFIG_PACKAGE a Left Join BPIP_USER b on a.CREATEPSN=b.USERID "
						+ "where a.FID='" + strFID + "'  order by a.ID");
			} else {
				addBuf.append("Select a.ID,a.IDENTIFICATION,a.NAME,a.STATUS,a.CREATEDATE,b.NAME AS CREATEPSN "
						+ "from FLOW_CONFIG_PACKAGE a Left Join BPIP_USER b on a.CREATEPSN=b.USERID "
						+ "where a.FID='000'  order by a.ID");
			}
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				return null;
			} else {// length > 0
				bgs = new Package[length];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					bgs[i] = (Package) ReflectionUtil.convertMapToBean(retmap, Package.class);
				}
			}
		} catch (Exception ex) {
			bgs = null;
			LOGGER.error("调用方法getFlowPackageList2出现异常", ex);
		}
		return bgs;
	}

	@Override
	public Package getPackageID(String id) throws Exception {
		Package bp = null;
		try {
			StringBuffer addBuf = new StringBuffer();
			addBuf.append("Select * From FLOW_CONFIG_PACKAGE Where ID='" + id.trim() + "'");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
			if (retlist != null && retlist.size() > 0) {
				Map<String, Object> retmap = retlist.get(0);
				bp = (Package) ReflectionUtil.convertMapToBean(retmap, Package.class);
			}
		} catch (Exception ex) {
			bp = null;
			LOGGER.error("调用方法getPackageID出现异常", ex);
		}
		return bp;
	}

	@Override
	public String[][] getPackageList(String Table, String strID) throws Exception {
		String[][] retValue = null;
		StringBuffer addBuf = new StringBuffer();
		addBuf.append("Select ID,NAME From " + Table + " where STATUS = '1' and FLOWPACKAGE in "
				   + "(select FLOWPACKAGE from FLOW_CONFIG_PROCESS where ID = '"+strID+"') Order by ID");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			retValue = new String[length][2];
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmp = retlist.get(i);
				retValue[i][0] = retmp.get("ID").toString();
				retValue[i][1] = retmp.get("NAME").toString();
			}
		}
		return retValue;
	}

	@Override
	public String showPackageSelect1(String strID) throws Exception {
		return showPackageSelect1(getPackageList1(), strID);
	}

	@Override
	public String showPackageSelect(String Table, String strID) throws Exception {
		return showPackageSelect(getPackageList(Table, strID), strID);
	}

	@Override
	public String showPackageSelect1(String[][] Processlist, String strID) throws Exception {
		StringBuffer addBuf = new StringBuffer();
		if (Processlist != null) {
			if (Processlist.length > 0) {
				for (int i = 0; i < Processlist.length; i++) {
					if (strID != null) {
						if (strID.equals(Processlist[i][0])) {
							addBuf.append("<option value=\"" + Processlist[i][0] + "\" selected>")
								  .append(Processlist[i][1] + "</option>\n");
						} else {
							addBuf.append("<option value=\"" + Processlist[i][0] + "\">" + Processlist[i][1])
								  .append("</option>\n");
						}
					} else {
						addBuf.append("<option value=\"" + Processlist[i][0] + "\">" + Processlist[i][1])
							  .append("</option>\n");
					}
				}
			}
		}
		return addBuf.toString();
	}

	@Override
	public String showPackageSelect(String[][] Packagelist, String strID) throws Exception {
		StringBuffer addBuf = new StringBuffer();
		if (Packagelist != null) {
			if (Packagelist.length > 0) {
				for (int i = 0; i < Packagelist.length; i++) {
					if (strID != null) {
						if (strID.equals(Packagelist[i][0])) {
							addBuf.append("<option value=\"" + Packagelist[i][0] + "\" selected>")
								  .append(Packagelist[i][1] + "</option>\n");
						} else {
							addBuf.append("<option value=\"" + Packagelist[i][0] + "\">")
								  .append(Packagelist[i][1] + "</option>\n");
						}
					} else {
						addBuf.append("<option value=\"" + Packagelist[i][0] + "\">" + Packagelist[i][1])
							  .append("</option>\n");
					}
				}
			}
		}
		return addBuf.toString();
	}

	@Override
	public FunctionMessage addButton(Button Bt) throws Exception {
		StringBuffer addBuf = new StringBuffer();
		FunctionMessage funMsg = new FunctionMessage(1);
		try {
			addBuf.append("Select ID From FLOW_BASE_BUTTON Where BNAME='" + Bt.getNAME() + "'");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
			if (retlist != null && retlist.size() > 0) {
				funMsg.setResult(false);
				funMsg.setMessage("流程按钮【" + Bt.getID() + "】已经存在");
			} else {// retlist == null
				String strMaxNo = getMaxFieldNo("FLOW_BASE_BUTTON", "ID", 8);
				Bt.setID(strMaxNo);// 设置ID
				boolean isOK = dbEngine.ExecuteInsert(Bt.getData());
				if (isOK) {
					funMsg.setMessage("流程按钮录入成功");
					funMsg.setResult(true);
				}
			}
		} catch (Exception ex) {
			LOGGER.error("调用方法addButton出现异常", ex);
			funMsg.setResult(false);
			funMsg.setMessage("调用方法addButton出现异常" + ex.toString());
			return funMsg;
		}
		flowControlService.initHashtable();
		return funMsg;
	}

	@Override
	public FunctionMessage editButton(Button Bt) throws Exception {
		FunctionMessage funMsg = new FunctionMessage(1);
		try {
		    boolean isOk = dbEngine.ExecuteEdit(Bt.getData(), "ID=" + Bt.getID());
			if (isOk) {// 流程按钮修改成功
				funMsg.setResult(true);
				funMsg.setMessage("流程按钮修改成功");
			} else {// 流程按钮修改失败
				funMsg.setResult(false);
				funMsg.setMessage("流程按钮【" + Bt.getID() + "】不存在");
			}
		} catch (Exception ex) {
			LOGGER.error("调用方法editButton出现异常", ex);
			funMsg.setResult(false);
			funMsg.setMessage("调用方法editButton出现异常" + ex.toString());
			return funMsg;
		}
		flowControlService.initHashtable();
		return funMsg;
	}

	@Override
	public FunctionMessage deleteButton(String ID) throws Exception {
		FunctionMessage funMsg = new FunctionMessage(1);
		try {
			Integer retInt = flowDAOMapper.deleteButton(ID);
			if (retInt != null && retInt > 0) {
				funMsg.setResult(true);
				funMsg.setMessage("流程按钮【" + ID + "】已经删除");
			} else {
				funMsg.setMessage("流程按钮删除不成功");
				funMsg.setResult(false);
			}
		} catch (Exception ex) {
			LOGGER.error("调用方法deleteButton出现异常", ex);
			funMsg.setResult(false);
			funMsg.setMessage("调用方法deleteButton出现异常" + ex.toString());
			return funMsg;
		}
		flowControlService.initHashtable();
		return funMsg;
	}

	@Override
	public Button[] getButtonList() throws Exception {
		Button bgs[] = null;
		StringBuffer addBuf = new StringBuffer();
		try {
			addBuf.append("Select * from FLOW_BASE_BUTTON order by CODE,ID");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				return null;
			} else {// length > 0
				bgs = new Button[length];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					bgs[i] = (Button) ReflectionUtil.convertMapToBean(retmap, Button.class);
				}
			}
		} catch (Exception ex) {
			LOGGER.error("调用方法getButtonList出现异常", ex);
			return null;
		}
		return bgs;
	}

	@Override
	public Button getButtonID(String id) throws Exception {
		Button bp = null;
		StringBuffer addBuf = new StringBuffer();
		try {
			addBuf.append("Select * From FLOW_BASE_BUTTON Where ID='" + id.trim() + "'");
			Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
			if (retmap == null || retmap.size() < 1) {
				return null;
			} else {// retmap != null
				bp = (Button) ReflectionUtil.convertMapToBean(retmap, Button.class);
			}
		} catch (Exception ex) {
			LOGGER.error("调用方法getButtonID出现异常", ex);
			return null;
		}
		return bp;
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
	public String[][] getDocnameList(String Table) throws Exception {
		String[][] resultValue = null;
		StringBuffer addBuf = new StringBuffer();
		addBuf.append("Select ID,DOCNAME From " + Table + " Order by DOCNAME");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			resultValue = new String[length][2];
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				resultValue[i][0] = retmap.get("ID").toString();
				resultValue[i][1] = retmap.get("DOCNAME").toString();
			}
		}
		return resultValue;
	}

	@Override
	public String showDocnameSelect(String Table, String strID) throws Exception {
		return showDocnameSelect(getDocnameList(Table), strID);
	}

	@Override
	public String showDocnameSelect(String[][] DocnameList, String strID) throws Exception {
		StringBuffer addBuf = new StringBuffer();
		if (DocnameList != null) {
			if (DocnameList.length > 0) {
				for (int i = 0; i < DocnameList.length; i++) {
					if (strID != null) {
						if (strID.equals(DocnameList[i][0])) {
							addBuf.append("<option value=\"" + DocnameList[i][0] + "\" selected>")
								  .append(DocnameList[i][1] + "</option>\n");
						} else {
							addBuf.append("<option value=\"" + DocnameList[i][0] + "\">")
								  .append(DocnameList[i][1] + "</option>\n");
						}
					} else {
						addBuf.append("<option value=\"" + DocnameList[i][0] + "\">" + DocnameList[i][1])
							  .append("</option>\n");
					}
				}
			}
		}
		return addBuf.toString();
	}

	@Override
	public String IsSubPackage(String strID) throws Exception {
		String retValue = "0";
		int num1 = 0;
		StringBuffer addBuf = new StringBuffer();
		addBuf.append("Select count(ID) as Cnum From FLOW_CONFIG_PACKAGE where FID='" + strID + "'");
		Integer retCnum = userMapper.selectIntExecSQL(addBuf.toString());
		if (retCnum != null) {
			num1 = retCnum;
		}
		if (num1 > 0) {
			retValue = "1";
		} else {
			retValue = "0";
		}
		return retValue;
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
		StringBuffer addBuf = new StringBuffer();
		addBuf.append("SELECT MAX(" + FieldName + ") AS MaxNo FROM " + TableName);
		try {
			String retMaxNo = userMapper.selectStrExecSQL(addBuf.toString());
			if (retMaxNo == null) {
				retMaxNo = "";
			}
			MaxNo = retMaxNo;// 最大编号
			if (MaxNo.length() > 0) {
				MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
				LenMaxNo = MaxNo.length();
				addBuf.delete(0, addBuf.length());// 清空
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

	/**
	 * 功能：得到本单位的特殊流程组列表
	 * @param Table 表名
	 * @param UnitID 单位ID
	 * @return String[][] 返回列表
	 */
	private String[][] getPackageList1() {
		String[][] resultValue = null;
		
		resultValue = new String[14][2];
		resultValue[0][0] = "1";
		resultValue[0][1] = "本人";
		
		resultValue[1][0] = "2";
		resultValue[1][1] = "本部门的人";
		
		resultValue[2][0] = "3";
		resultValue[2][1] = "上一处理人";
		
		resultValue[3][0] = "4";
		resultValue[3][1] = "本流程的创建人";
		
		resultValue[4][0] = "5";
		resultValue[4][1] = "某一活动的处理人";
		
		resultValue[5][0] = "6";
		resultValue[5][1] = "其它(流程组)";
		
		resultValue[6][0] = "7";
		resultValue[6][1] = "其它(本部门流程组)";
		
		resultValue[7][0] = "E";
		resultValue[7][1] = "自定义选择部门";
		
		resultValue[8][0] = "F";
		resultValue[8][1] = "自定义选择人员";
		
		resultValue[9][0] = "H";
		resultValue[9][1] = "本部门及上级部门流程组";
		
		resultValue[10][0] = "I";
		resultValue[10][1] = "本部门及下级部门流程组";
		
		resultValue[11][0] = "X";
		resultValue[11][1] = "本部门及同级部门流程组";
		
		resultValue[12][0] = "M";
		resultValue[12][1] = "本部门-同级部门-上级部门流程组";
		
		resultValue[13][0] = "N";
		resultValue[13][1] = "本部门-上级同级部门流程组";
		
		return resultValue;
	}
}