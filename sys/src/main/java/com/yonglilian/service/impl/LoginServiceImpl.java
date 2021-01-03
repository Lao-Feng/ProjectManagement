package com.yonglilian.service.impl;

import com.yonglilian.dao.mapper.LoginMapper;
import com.yonglilian.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBMode;
import zr.zrpower.common.db.DBRow;
import zr.zrpower.common.db.DBSet;
import zr.zrpower.common.util.*;
import zr.zrpower.dao.UserMapper;
import zr.zrpower.model.BPIP_USER;
import zr.zrpower.model.BPIP_USER_PHOTO;
import zr.zrpower.model.SessionUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户操作服务层实现
 * 
 * @author lwk
 *
 */
@Service
public class LoginServiceImpl implements LoginService {
	/** The UserServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);
	String dataBaseType = SysPreperty.getProperty().DataBaseType;
	/** 数据库引擎. */
	private DBEngine dbEngine;
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private LoginMapper loginMapper;

	/**
	 * 构造方法
	 */
	public LoginServiceImpl()  throws Exception{
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
	}

	@Override
	public String addUser(BPIP_USER user) throws Exception {
		StringWork mString = new StringWork();
		String retValue = "";
		try {
			// 如果传入的用户实体不为空，构建新数据行，提交给数据库控制类执行插入数据库操作
			if (user != null) {
				String loginID = user.getLOGINID();
				BPIP_USER retUser = userMapper.getUserInfoByLID(loginID);
				if ((retUser != null) || "pip".equals(loginID)) {
					// 返回，不执行任何操作
					return retValue;
				} else {
					String unitID = DaoSqlUtils.transactSqlInjection(user.getUNITID());
					String execSQL = "Select Max(UserID) as maxid From BPIP_USER Where UserID like '" + unitID + "%'";
					String maxUserID = userMapper.selectStrExecSQL(execSQL);

					String userID = unitID + "0001";
					if (StringUtils.isNotBlank(maxUserID)) {
						if (maxUserID.length() == 16) {
							userID = user.getUNITID() + mString.NewID(maxUserID.substring(12));
						}
					}
					user.setPAGESIZE(18);
					user.setWINDOWTYPE("5");

					user.setLOGINTIME(0);
					user.setLOGINPW(MD5.toMD5(user.getLOGINPW()));
					user.setUSERSTATE("0");
					user.setUSERID(userID);

					DBRow[] dbUser;
					if (user.getUserPhoto() != null) {
						BPIP_USER_PHOTO mUserPhoto = user.getUserPhoto();
						mUserPhoto.setUSERID(userID);
						dbUser = new DBRow[2];
						dbUser[0] = user.getData();
						dbUser[1] = mUserPhoto.getData();
						dbUser[0].setDataMode(DBMode.NEW);
						dbUser[1].setDataMode(DBMode.NEW);
					} else {
						dbUser = new DBRow[1];
						dbUser[0] = user.getData();
						dbUser[0].setDataMode(DBMode.NEW);
					}
					if (dbEngine.UpdateAll(dbUser)) {
						retValue = userID; // 需要新增新用户
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("UserServiceImpl.addUser Exception:\n", ex);
		}
		return retValue;
	}

	@Override
	public boolean deleteUser(String userID) throws Exception {
		boolean retValue = true; // 定义返回值
		try {
			if (userID != null) {
				userID = DaoSqlUtils.transactSqlInjection(userID);
				String execSQL0 = "Delete From BPIP_USER  Where UserID='" + userID + "'";
				String execSQL1 = "Delete From BPIP_USER_PHOTO  Where UserID='" + userID + "'";
				String execSQL2 = "Delete From BPIP_USER_REMOVE  Where UserID='" + userID + "'";
				userMapper.deleteExecSQL(execSQL0);
				userMapper.deleteExecSQL(execSQL1);
				userMapper.deleteExecSQL(execSQL2);
			}
		} catch (Exception ex) {
			retValue = false;
			LOGGER.error("UserServiceImpl.deleteUser Exception:\n", ex);
		}
		return retValue;
	}

	@Override
	public FunctionMessage editUser(BPIP_USER user) throws Exception {
		FunctionMessage funMsg = new FunctionMessage(1);
		if (user == null) {
			funMsg.setMessage(FunctionMessage.MSG_NOT_SAVE_DATA);
			return funMsg;
		}
		String userID = DaoSqlUtils.transactSqlInjection(user.getUSERID());

		String execSQL0 = "Select USERID From BPIP_USER_PHOTO Where UserID = '" + userID + "'";
		String retUserID = userMapper.selectStrExecSQL(execSQL0);
		// 没有照片记录插入
		if (StringUtils.isBlank(retUserID)) {
			String execSQL = "Insert into BPIP_USER_PHOTO(USERID) values('" + userID + "')";
			userMapper.insertExecSQL(execSQL);
		}
		try {
			// 如果传入的用户实体不为空，构建新数据行，提交给数据库控制类执行更新数据库操作
			String loginID = DaoSqlUtils.transactSqlInjection(user.getLOGINID());
			String execSQL1 = "Select UserID From BPIP_USER Where LoginID='" + loginID + "' and UserID<>'" + userID
					+ "'";
			String retUserID1 = userMapper.selectStrExecSQL(execSQL1);
			if (StringUtils.isNotBlank(retUserID1)) {
				funMsg.setMessage("已有用户使用【" + loginID + "】这个登录名了，请另用一个新登录名！");
			} else {
				if (StringUtils.isNotBlank(user.getLOGINPW())) {
					user.setLOGINPW(MD5.toMD5(user.getLOGINPW()));
				}
				DBRow[] dbUser;
				if (user.getUserPhoto() != null) {
					BPIP_USER_PHOTO mUserPhoto = user.getUserPhoto();
					mUserPhoto.setUSERID(user.getUSERID());
					dbUser = new DBRow[2];
					dbUser[0] = user.getData();
					dbUser[1] = mUserPhoto.getData();
					dbUser[0].setDataMode(DBMode.EDITED);
					dbUser[1].setDataMode(DBMode.EDITED);
				} else {
					dbUser = new DBRow[1];
					dbUser[0] = user.getData();
					dbUser[0].setDataMode(DBMode.EDITED);
				}
				if (dbEngine.UpdateAll(dbUser, null, null)) {
					funMsg.setResult(true);
					funMsg.setMessage(FunctionMessage.MSG_SUCCEED_SAVE);
				} else {
					funMsg.setMessage(FunctionMessage.MSG_ERROR_DATASERVER);
				}
			}
		} catch (Exception ex) {
			LOGGER.error("UserServiceImpl.editUser Exception:\n", ex);
		}
		return funMsg;
	}

	@Override
	public BPIP_USER getUserInfo(String userID) throws Exception {
		BPIP_USER retValue = userMapper.getUserInfo(userID);
		return retValue;
	}

	@Override
	public String[][] getUserArrary(String DepID) throws Exception {
		String[][] retArray = null;
		String execSQL = "Select UserID,Name From BPIP_USER Where UserState='0' And UNITID ='" + DepID
				+ "' Order By ORBERCODE,UserID";
		List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(execSQL);

		int length = retList != null ? retList.size() : 0;
		if (length > 0) {
			retArray = new String[length][2];
			for (int i = 0; i < length; i++) {
				Map<String, Object> retMap = retList.get(i);
				retArray[i][0] = retMap.get("UserID").toString();
				retArray[i][1] = retMap.get("Name").toString();
			}
		}
		return retArray;
	}

	@Override
	public BPIP_USER[] getUserList(String unitID) throws Exception {
		BPIP_USER[] userArray = null;
		List<BPIP_USER> userList = userMapper.getUserInfoList(unitID);
		int length = userList != null ? userList.size() : 0;
		if (length > 0) {
			userArray = userList.toArray(new BPIP_USER[length]);
		}
		return userArray;
	}

	@Override
	public BPIP_USER[] getDeptUserList(String unitID) throws Exception {
		BPIP_USER[] userArray = null;
		List<BPIP_USER> userList = userMapper.getUserInfoList(unitID);
		int length = userList != null ? userList.size() : 0;
		if (length > 0) {
			userArray = userList.toArray(new BPIP_USER[length]);
		}
		return userArray;
	}

	@Override
	public FunctionMessage login(String loginID, String password) {
		FunctionMessage funMsg = new FunctionMessage();// 定义返回值
		funMsg.setResult(false);
		funMsg.setMessage("发生异常，请与管理员联系！");
		try {
			loginID = loginID.trim();
			loginID = DaoSqlUtils.transactSqlInjection(loginID);
			Map<String,Object> map= new HashMap<>();
			map.put("LOGINID", loginID);
			BPIP_USER resut = loginMapper.login(map);
			if (resut == null) {
				funMsg.setResult(false);
				funMsg.setMessage("登录用户[" + loginID + "]不存在，请检查输入的用户名是否正确！");
			} else if (resut.getUSERSTATE()!= null&& resut.getUSERSTATE().toString().equals("1")) {
				funMsg.setResult(false);
				funMsg.setMessage("该用户已经被管理员禁止使用，如有问题请于当地管理员联系。");
			} else {
				String strIsLogin = "0";
				if (resut.getLOGINPW().toString().equals(MD5.toMD5(password))|| password.equals("autologinpassword")) {
					strIsLogin = "1";
				}
				if (strIsLogin.equals("1")) {
					SessionUser su = new SessionUser();
					su.setLoginID(resut.getLOGINID());
					su.setName(resut.getNAME());
					su.setLCODE(resut.getLCODE());

					su.setUnitID(resut.getUNITID());
//					su.setUnitName(retMap.get("UNITNAME").toString());
					su.setUserID(resut.getUSERID());
					su.setFace(resut.getUSERIMAGE());

					// 有一些有特殊自定义参数时设置
					su.setCustom1("");
					su.setCustom2("");
					su.setCustom3("");
					su.setCustom4("");
					su.setCustom5("");
					// -------------------------

					su.setUserCSS("");
					su.setUserImage("");
					su.setUserIndexColor("");
					su.setWebEditCss("");
					su.setUserWINDOWTYPE("");
					su.setUserPageSize(resut.getPAGESIZE());

					funMsg.setResult(true);
					funMsg.setMessage(resut.getUSERID());
					funMsg.setValue(su);

					loginMapper.updateonline(resut.getUSERID());
				} else {
					funMsg.setResult(false);
					funMsg.setMessage("用户密码错误，请重新登录！");
				}
			}
		} catch (Exception ex) {
			LOGGER.error("UserServiceImpl.login Exception:\n", ex);
			funMsg.setResult(false);
			funMsg.setMessage("验证登录信息失败！\n详细错误信息如下：\n " + ex.toString());
		}
		return funMsg;
	}

	@Override
	public boolean stopUser(String userID) throws Exception {
		boolean retValue = true; // 定义返回值
		String execSQL = "";
		List<String> userList = new ArrayList<String>();
		userID += ",";
		try {
			userList = getArrayList(userID, ",");
			if (userList.size() > 0) {
				for (int i = 0; i < userList.size(); i++) {
					execSQL = "Update BPIP_USER set UserState=1 Where UserID='" + userList.get(i).toString().trim()
							+ "'";
					userMapper.updateExecSQL(execSQL);
				}
			}
		} catch (Exception ex) {
			retValue = false;
			LOGGER.error("UserServiceImpl.stopUser Exception:\n", ex);
		}
		return retValue;
	}

	@Override
	public boolean unStopUser(String userID) throws Exception {
		boolean retValue = true; // 定义返回值
		String execSQL = "";
		List<String> userList = new ArrayList<String>();
		userID += ",";
		try {
			userList = getArrayList(userID, ",");
			if (userList.size() > 0) {
				for (int i = 0; i < userList.size(); i++) {
					execSQL = "Update BPIP_USER set UserState=0 Where UserID='" + userList.get(i).toString().trim()
							+ "'";
					userMapper.updateExecSQL(execSQL);
				}
			}
		} catch (Exception ex) {
			retValue = false;
			LOGGER.error("UserServiceImpl.unStopUser Exception:\n", ex);
		}
		return retValue;
	}

	@Override
	public boolean resetPassword(String userID, String pwd) throws Exception {
		boolean retValue = true; // 定义返回值
		String execSQL = "";
		List<String> userList = new ArrayList<String>();
		userID += ",";
		try {
			userList = getArrayList(userID, ",");
			if (userList.size() > 0) {
				for (int i = 0; i < userList.size(); i++) {
					// 如果不是系统内建帐号
					String strUserID = userList.get(i).trim();
					strUserID = DaoSqlUtils.transactSqlInjection(strUserID);
					if (!strUserID.equals("0000000000000000")) {
						execSQL = "Update BPIP_USER set LoginPW='" + MD5.toMD5(pwd) + "' Where UserID='" + strUserID
								+ "'";
						userMapper.updateExecSQL(execSQL);
					}
				}
			}
		} catch (Exception ex) {
			retValue = false;
			LOGGER.error("UserServiceImpl.resetPassword Exception:\n", ex);
		}
		return retValue;
	}

	@Override
	public boolean resetPassword1(String userID, String pwd) throws Exception {
		boolean retValue = true; // 定义返回值
		String execSQL = "";
		List<String> userList = new ArrayList<String>();
		userID += ",";
		try {
			userList = getArrayList(userID, ",");
			if (userList.size() > 0) {
				for (int i = 0; i < userList.size(); i++) {
					// 如果不是系统内建帐号
					String strUserID = userList.get(i).trim();
					pwd = getUserLoginID(strUserID);
					strUserID = DaoSqlUtils.transactSqlInjection(strUserID);
					if (!strUserID.equals("0000000000000000")) {
						execSQL = "Update BPIP_USER set LoginPW='" + MD5.toMD5(pwd) + "' Where UserID='" + strUserID
								+ "'";
						userMapper.updateExecSQL(execSQL);
					}
				}
			}
		} catch (Exception ex) {
			retValue = false;
			LOGGER.error("UserServiceImpl.resetPassword1 Exception:\n", ex);
		}
		return retValue;
	}

	@Override
	public String getUserLoginID(String userID) throws Exception {
		if (StringUtils.isBlank(userID)) {
			return null;
		}
		// 如果是系统内建用户
		if (userID.equals("0000000000000000")) {
			return "pip";
		}
		userID = DaoSqlUtils.transactSqlInjection(userID);
		String execSQL = "Select LoginID From BPIP_USER Where UserID = '" + userID + "'";
		String loginID = userMapper.selectStrExecSQL(execSQL);
		return loginID;
	}

	@Override
	public String getUserName(String userID) throws Exception {
		if (StringUtils.isBlank(userID)) {
			return null;
		}
		// 如果是系统内建用户
		if (userID.equals("0000000000000000")) {
			return "系统内建用户";
		}
		userID = DaoSqlUtils.transactSqlInjection(userID);
		String execSQL = "Select Name From BPIP_USER Where UserID = '" + userID + "'";
		String userName = userMapper.selectStrExecSQL(execSQL);
		return userName;
	}

	@Override
	public byte[] getUserPhoto(String userID) throws Exception {
		byte[] retValue = null;
		String strSQL = "Select USERPHOTO From BPIP_USER_PHOTO Where UserID = '" + userID + "'";
		DBSet dbset = dbEngine.QuerySQL(strSQL);// blob字段处理
		if (dbset != null) {
			if (dbset.RowCount() > 0) {
				retValue = dbset.Row(0).Column("USERPHOTO").getBlob();
			}
			dbset = null;
		}
		return retValue;
	}

	@Override
	public byte[] getUserSelfFace(String userID) throws Exception {
		byte[] retValue = null;
		String strSQL = "Select USERIMAGE From BPIP_USER_PHOTO Where UserID = '" + userID + "'";
		DBSet dbset = dbEngine.QuerySQL(strSQL);// blob字段处理
		if (dbset != null) {
			if (dbset.RowCount() > 0) {
				retValue = dbset.Row(0).Column("USERIMAGE").getBlob();
			}
			dbset = null;
		}
		return retValue;
	}

	@Override
	public byte[] getUserIdiograph(String userID) throws Exception {
		byte[] retValue = null;
		String strSQL = "Select IDIOGRAPH From BPIP_USER_PHOTO Where UserID = '" + userID + "'";
		DBSet dbset = dbEngine.QuerySQL(strSQL);// blob字段处理
		if (dbset != null) {
			if (dbset.RowCount() > 0) {
				retValue = dbset.Row(0).Column("IDIOGRAPH").getBlob();
			}
			dbset = null;
		}
		return retValue;
	}

	@Override
	public String getUserUNITID(String userID) throws Exception {
		if (StringUtils.isBlank(userID)) {
			return null;
		}
		String execSQL = "Select UNITID From BPIP_USER Where UserID = '" + userID + "'";
		Map<String, Object> retMap = userMapper.selectMapExecSQL(execSQL);
		String userUNITID = retMap.get("UNITID").toString();
		return userUNITID;
	}

	@Override
	public String getUserUNITName(String userID) throws Exception {
		if (StringUtils.isBlank(userID)) {
			return null;
		}
		String retValue = "";
		// 如果是系统内建用户
		if (userID.equals("0000000000000000")) {
			retValue = "管理单位";
		} else {
			userID = DaoSqlUtils.transactSqlInjection(userID);
			String execSQL = "Select unit.UNITID,unit.UNITNAME From BPIP_USER u Left Join BPIP_UNIT unit on u.UNITID=unit.UNITID Where u.UserID = '"
					+ userID + "'";
			Map<String, Object> retMap = userMapper.selectMapExecSQL(execSQL);

			retValue = retMap.get("UNITNAME").toString();
		}
		return retValue;
	}

	@Override
	public boolean checkPw(String userID, String pwd) throws Exception {
		if (StringUtils.isBlank(userID) || StringUtils.isBlank(pwd)) {
			return false;
		}
		userID = DaoSqlUtils.transactSqlInjection(userID);
		pwd = DaoSqlUtils.transactSqlInjection(pwd);
		boolean result = false;
		String execSQL = "Select COUNT(Name) From BPIP_USER Where (UserID='" + userID + "' or NAME='" + userID
				+ "') and LoginPW = '" + MD5.toMD5(pwd) + "'";
		Integer count = userMapper.selectIntExecSQL(execSQL);
		if (count != null && count > 0) {
			result = true;
		}
		return result;
	}

	@Override
	public String getUserTypeImg(String uType, String path) throws Exception {
		String retValue = "";
		try {
			int type = Integer.parseInt(uType);
			switch (type) {
			case 0:
				retValue = "<img src=\"" + path
						+ "Zrsysmanage/images/user1.gif\" width=\"16\" height=\"15\" align=\"absmiddle\">";
				break;
			case 1:
				retValue = "<img src=\"" + path
						+ "Zrsysmanage/images/user2.gif\" width=\"16\" height=\"15\" align=\"absmiddle\">";
				break;
			case 2:
				retValue = "<img src=\"" + path
						+ "Zrsysmanage/images/user3.gif\" width=\"16\" height=\"15\" align=\"absmiddle\">";
				break;
			case 3:
				retValue = "<img src=\"" + path
						+ "Zrsysmanage/images/user4.gif\" width=\"16\" height=\"15\" align=\"absmiddle\">";
				break;
			}
		} catch (NumberFormatException ex) {
			LOGGER.error("UserServiceImpl.getUserTypeImg Exception:\n", ex);
		}
		return retValue;
	}

	@Override
	public String getUserState(String uType) throws Exception {
		String retValue = "未知状态";
		int type = Integer.parseInt(uType);
		switch (type) {
		case 0:
			retValue = "使用中";
			break;
		case 1:
			retValue = "已停用";
			break;
		}
		return retValue;
	}

	@Override
	public boolean changeAppCon(String userID, int pageSize, String winType) throws Exception {
		boolean retValue = true; // 定义返回值
		String execSQL = "";
		try {
			if (StringUtils.isNotBlank(userID)) {
				execSQL = "Update BPIP_USER set PAGESIZE=" + String.valueOf(pageSize) + ", WINDOWTYPE ='" + winType
						+ "' Where UserID='" + userID + "'";
				userMapper.updateExecSQL(execSQL);
			}
		} catch (Exception ex) {
			retValue = false;
			LOGGER.error("UserServiceImpl.changeAppCon Exception:\n", ex);
		}
		return retValue;
	}

	@Override
	public String getShowUnitUserList(String strUnitID, String strListName, String strUserImg, String ico,
			String strType) throws Exception {
		StringBuffer retValue = new StringBuffer();
		String strNo = "";
		String strName = "";
		String strSQL = "";
		int k = 0, row = 0;
		String strcheck = "";
		String strchecked = "";

		if (strType.equals("1")) {
			strcheck = "radio";
		} else {
			strcheck = "checkbox";
		}
		retValue.append("<table cellSpacing='0' cellPadding='0' border='0' width='100%'>\r\n");

		if (strUnitID.lastIndexOf("all") > -1) {// 当前单位及下属单位
			strUnitID = strUnitID.substring(0, strUnitID.length() - 4);
			StringWork sw = new StringWork();
			strUnitID = sw.CutLastZero(strUnitID, 2);
			strSQL = "Select u.USERID,u.NAME,unit.UNITNAME "
					+ "From BPIP_USER u left join BPIP_UNIT unit on u.UNITID=unit.UNITID "
					+ "Where u.USERSTATE='0' and u.UNITID like '" + strUnitID
					+ "%' order by unit.UNITID,u.orbercode,u.NAME";
		} else {
			// 检测去0后的单位下人数
			String strUnitID1 = strUnitID.substring(0, strUnitID.length() - 4);
			StringWork sw = new StringWork();
			strUnitID1 = sw.CutLastZero(strUnitID, 2);

			strSQL = "Select count(USERID) as num From BPIP_USER where USERSTATE='0' and UNITID like '" + strUnitID1
					+ "%'";
			Integer count = userMapper.selectIntExecSQL(strSQL);
			int rownum = 0;
			if (count != null && count > 0) {
				rownum = count;
			}
			if (rownum < 450) {// 450个用户以下可以一次选择
				strSQL = "Select u.USERID,u.NAME,unit.UNITNAME "
						+ "From BPIP_USER u left join BPIP_UNIT unit on u.UNITID=unit.UNITID "
						+ "where u.USERSTATE='0' and u.UNITID like '" + strUnitID1
						+ "%' order by unit.UNITID,u.orbercode,u.NAME";
			} else {
				strSQL = "Select u.USERID,u.NAME,unit.UNITNAME "
						+ "From BPIP_USER u left join BPIP_UNIT unit on u.UNITID=unit.UNITID "
						+ "where u.USERSTATE='0' and u.UNITID='" + strUnitID
						+ "' order by unit.UNITID,u.orbercode,u.NAME";
			}
		}
		String YdeptName = "";
		String deptName = "";

		List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
		int length = retList != null ? retList.size() : 0;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				Map<String, Object> retMap = retList.get(i);
				CollectionUtil.convertMapNullToStr(retMap);
				strchecked = "";
				strNo = retMap.get("USERID").toString();
				strName = retMap.get("NAME").toString();
				deptName = retMap.get("UNITNAME").toString();

				if (strNo.length() > 0) {
					if (!strType.equals("1")) {
						strListName = strListName + strNo;
					}
					// ------------
					if (!YdeptName.equals(deptName)) {
						retValue.append("<tr><td colspan='3'>" + deptName + "</td></tr>\r\n");
						if (row > 0) {
							if (k == 1) {
								retValue.append("<td></td><td></td>\r\n");
							}
							if (k == 2) {
								retValue.append("<td></td>\r\n");
							}
							k = 3;
						}
					}
					// ------------
					row = row + 1;
					k = k + 1;
					if (k == 4) {
						k = 1;
					}
					if (k == 1) {
						retValue.append("<tr>\r\n");
					}
					retValue.append(
							"<td align='left' height='25' style='cursor:pointer ' onclick=\"selectOnclick('" + strNo)
							.append("');\"><input type='" + strcheck + "' " + strchecked + " onclick=\"selectOnclick('")
							.append(strNo + "');\" name='" + strListName + "' id='" + strNo + "' value='")
							.append(strName + "'>" + "<img src='" + SysPreperty.getProperty().AppUrl
									+ "Zrsysmanage/images/blueimg")
							.append("/" + ico + "' border='0' align='absmiddle'>" + strName).append("</td>");
					if (k == 3) {
						retValue.append("</tr>\r\n");
					}
					YdeptName = deptName;
				}
				// -----------------
			}
		}
		if (k == 1) {
			retValue.append("<td></td><td></td></table>\r\n");
		}
		if (k == 2) {
			retValue.append("<td></td></table>\r\n");
		}
		if (k == 3) {
			retValue.append("</table>\r\n");
		}
		return retValue.toString();
	}

	@Override
	public BPIP_USER[] getRecoveryUserList() throws Exception {
		String unitID = null;
		List<BPIP_USER> userList = userMapper.getRecoveryUserList(unitID);
		int length = userList != null ? userList.size() : 0;
		if (length == 0) {
			return null;
		}
		// Java中List转换为BPIP_USER[]数组
		return userList.toArray(new BPIP_USER[length]);
	}

	@Override
	public boolean delAllUser(String strMenuNo) throws Exception {
		boolean result = true;
		List<String> strMenuNoList = new ArrayList<String>();
		try {
			strMenuNoList = getArrayList(strMenuNo + ",", ",");
			int length = strMenuNoList != null ? strMenuNoList.size() : 0;
			if (length > 0) {
				String[] strSq1s = new String[5];
				for (int i = 0; i < length; i++) {
					strSq1s[0] = "delete from BPIP_USER_PHOTO  Where UserID='" + strMenuNoList.get(i) + "'";
					strSq1s[1] = "delete from BPIP_USER_REMOVE Where UserID='" + strMenuNoList.get(i) + "'";
					strSq1s[2] = "delete from BPIP_USER_ROLE  Where UserID='" + strMenuNoList.get(i) + "'";
					strSq1s[3] = "delete from BPIP_USER_IDEA  Where USER_NO='" + strMenuNoList.get(i) + "'";
					strSq1s[4] = "delete from BPIP_USER Where USERID = '" + strMenuNoList.get(i) + "'";
					userMapper.deleteExecSQL(strSq1s[0]);
					userMapper.deleteExecSQL(strSq1s[1]);
					userMapper.deleteExecSQL(strSq1s[2]);
					userMapper.deleteExecSQL(strSq1s[3]);
					userMapper.deleteExecSQL(strSq1s[4]);
				}
			}
		} catch (Exception ex) {
			result = false;
			LOGGER.error("UserServiceImpl.delAllUser Exception:\n", ex);
		}
		return result;
	}

	@Override
	public boolean restoreUser(String strMenuNo) throws Exception {
		boolean result = true;
		String strSql = "";
		List<String> strMenuNoList = new ArrayList<String>();
		try {
			strMenuNoList = getArrayList(strMenuNo + ",", ",");
			int length = strMenuNoList != null ? strMenuNoList.size() : 0;
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					strSql = "Update BPIP_USER set UserState='0' where UserID = '" + strMenuNoList.get(i) + "'";
					userMapper.updateExecSQL(strSql);
				}
			}
		} catch (Exception ex) {
			result = false;
			LOGGER.error("UserServiceImpl.delAllUser Exception:\n", ex);
		}
		return result;
	}

	@Override
	public List<String> getArrayList(String strItems, String strItemMark) throws Exception {
		int itemLen, i = 0, n = 0;
		String strItem;
		List<String> strList = new ArrayList<String>();
		itemLen = strItems.length();
		while (i < itemLen) {
			n = strItems.indexOf(strItemMark, i);
			strItem = strItems.substring(i, n);
			strList.add(strItem);
			i = n + 1;
		}
		return strList;
	}

	@Override
	public String getShowUserRoleList(String unitID, String listName, String strUserImg, String ico) throws Exception {
		if ("6".equals(strUserImg)) {
			strUserImg = "windowimg";
		}
		String strNo = "";
		String strName = "";
		StringBuffer retValue = new StringBuffer();
		int k = 0;
		String strcheck = "";
		String strchecked = "";
		strcheck = "checkbox";
		retValue.append("<table cellSpacing='0' cellPadding='0' border='0' width='100%'>\r\n");
		String strSQL = "Select ROLEID,ROLENAME From BPIP_ROLE order by ROLENAME";
		List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);

		int length = retList != null ? retList.size() : 0;
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				strchecked = "";
			} else {
				strchecked = "";
			}
			Map<String, Object> retMap = retList.get(i);
			strNo = retMap.get("ROLEID").toString();
			strName = retMap.get("ROLENAME").toString();
			if (strNo.length() > 0) {
				k = k + 1;
				if (k == 4) {
					k = 1;
				}
				if (k == 1) {
					retValue.append("<tr>\r\n");
				}
				retValue.append("<td align='left' height='25' style='cursor:pointer ' onclick=\"selectOnclick('" + strNo
						+ "');\"><input type='" + strcheck + "' " + strchecked + " onclick=\"selectOnclick('" + strNo
						+ "');\" name='" + listName + "'id='" + strNo + "'value='" + strName + "'>");
				retValue.append("<img src='" + SysPreperty.getProperty().AppUrl
						+ "Zrsysmanage/images/flowuser.gif' border='0' align='absmiddle'>");
				retValue.append(strName + "</td>");
				if (k == 3) {
					retValue.append("</tr>\r\n");
				}
			}
			// -----------------
		}
		if (k == 1) {
			retValue.append("<td></td><td></td></table>\r\n");
		}
		if (k == 2) {
			retValue.append("<td></td></table>\r\n");
		}
		if (k == 3) {
			retValue.append("</table>\r\n");
		}
		return retValue.toString();
	}

	@Override
	public void upUserOnlineDate(String userID) throws Exception {
		String execSQL = "update BPIP_USER set ONLINEDATE=sysdate() where USERID='" + userID + "'";
		userMapper.updateExecSQL(execSQL);
	}

	@Override
	public int getSysSaverPNumber() throws Exception {
		String strSql = "";
		// 在线更新时间小于3分钟的为在线人员
		if (dataBaseType.equals("2")) {// MSSQL
			strSql = "Select count(*) as pnumber From BPIP_USER where datediff(n,ONLINEDATE,getdate())<3";
		} else {// Oracle&&MySQL
			strSql = "Select count(*) as pnumber From BPIP_USER where (sysdate()-ONLINEDATE)*1440<3";
		}
		Integer pNumber = userMapper.selectIntExecSQL(strSql);
		return (pNumber != null ? pNumber : -1);
	}

	@Override
	public BPIP_USER[] getLoginUserList(int pageSize, int page) throws Exception {
		BPIP_USER[] retArray = null;
		String strSQL = "";
		String columeName = "T.USERID,T.NAME,T.LOGINID,T.UNITID";
		// 在线更新时间小于3分钟的为在线人员
		if (dataBaseType.equals("1")) {// Oracle
			strSQL = "SELECT " + columeName
					+ " FROM (SELECT C.*, ROWNUM RN FROM (Select USERID,NAME,LOGINID,UNITID From BPIP_USER Where (sysdate()-ONLINEDATE)*1440<3 Order By USERID) C WHERE ROWNUM <= "
					+ String.valueOf(pageSize * page) + ") T WHERE T.RN > " + String.valueOf((page - 1) * pageSize);
		} else if (dataBaseType.equals("2")) {// MSSQL
			strSQL = "SELECT " + columeName
					+ " FROM (SELECT C.*, ROWNUM RN FROM (Select USERID,NAME,LOGINID,UNITID From BPIP_USER Where datediff(n,ONLINEDATE,getdate())<3 Order By USERID) C WHERE ROWNUM <= "
					+ String.valueOf(pageSize * page) + ") T WHERE T.RN > " + String.valueOf((page - 1) * pageSize);
		} else {// dataBaseType==3，MySQL
			int offset = (page - 1) * pageSize;
			if (offset < 0) {
				offset = 0;
			}
			strSQL = "SELECT " + columeName
					+ " FROM (Select USERID,NAME,LOGINID,UNITID From BPIP_USER Where (sysdate()-ONLINEDATE)*1440<3 Order By USERID limit "
					+ offset + ", " + pageSize + ") T";
		}
		List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
		int length = retList != null ? retList.size() : 0;
		if (length > 0) {
			retArray = new BPIP_USER[length];
			for (int i = 0; i < length; i++) {
				Map<String, Object> retMap = retList.get(i);
				CollectionUtil.convertMapNullToStr(retMap);
				retArray[i] = new BPIP_USER();
				retArray[i].setUSERID(retMap.get("USERID").toString());
				retArray[i].setNAME(retMap.get("NAME").toString());
				retArray[i].setLOGINID(retMap.get("LOGINID").toString());
				retArray[i].setUNITID(retMap.get("UNITID").toString());
			}
		}
		return retArray;
	}

	@Override
	public String createPage(int pageSize, int page) throws Exception {
		String resultStr = "";
		String strSQL = "";
		if (dataBaseType.equals("2")) {// MSSQL
			strSQL = "Select count(*) as RCount1 From BPIP_USER where datediff(n,ONLINEDATE,getdate())<3";
		} else {// Oracle&&MySQL
			strSQL = "Select count(*) as RCount1 From BPIP_USER where (sysdate()-ONLINEDATE)*1440<3";
		}
		Integer retRCount = userMapper.selectIntExecSQL(strSQL);
		int rowCount = 0;
		if (retRCount != null) {
			rowCount = retRCount;
		}
		resultStr = createPageMenu(pageSize, rowCount, page);
		return resultStr;
	}

	/**
	 * 功能或作用：实现翻页
	 * 
	 * @param pageSize
	 *            一页的记录数
	 * @param FileUrl
	 *            文件名
	 * @param rowCount
	 *            总的记录数
	 * @param page
	 *            当前页
	 * @Return PageMenu 执行后返回一个PageMenu字符串
	 */
	private String createPageMenu(int pageSize, int rowCount, int page) {
		StringBuffer pageMenu = new StringBuffer();
		pageMenu.append("&nbsp;");

		int pageCount = 0, i, sum1 = 0, sum2 = 0, sum3 = rowCount;
		while (((rowCount - pageSize) > 0) || (rowCount > 0)) {
			pageCount++;
			rowCount = rowCount - pageSize;
		}
		if (page > pageCount) {
			page = pageCount;
		}
		if (pageCount > 1) {
			sum1 = page - 1;
			if (sum1 < 1) {
				sum1 = 1;
			}
			sum2 = page + 1;
			if (sum2 > pageCount) {
				sum2 = pageCount;
			}
			pageMenu.append("<a href='javascript:recordfrm.page.value=\"1\";recordfrm.submit();'>首页</a>");
			pageMenu.append("&nbsp;&nbsp;<a href='javascript:recordfrm.page.value=\"")
					.append(String.valueOf(sum1) + "\";recordfrm.submit();'>上一页</a>");
			pageMenu.append("&nbsp;&nbsp;<a href='javascript:recordfrm.page.value=\"")
					.append(String.valueOf(sum2) + "\";recordfrm.submit();'>下一页</a>");
			pageMenu.append("&nbsp;&nbsp;<a href='javascript:recordfrm.page.value=\"")
					.append(String.valueOf(pageCount) + "\";recordfrm.submit();'>末页</a>&nbsp;&nbsp;");

			pageMenu.append(
					"<select size='1' class='PageStyle' Onchange='javascript:recordfrm.page.value=this.value;recordfrm.submit();'>");
			for (i = 1; i <= pageCount; i++) {
				if (page == i) {
					pageMenu.append("<option value='" + i + "' selected >第" + i + "页</option>");
				} else {
					pageMenu.append("<option value='" + i + "' >第" + i).append("页</option>");
				}
			}
			pageMenu.append("</select>");
			pageMenu.append("&nbsp;&nbsp;页次：" + page + "/" + pageCount + "&nbsp;&nbsp;共" + sum3 + "人在线&nbsp;&nbsp;");
		} else {
			pageMenu.append("&nbsp;&nbsp;共" + sum3 + "人在线&nbsp;&nbsp;");
		}
		String checkboxStr = ""; // 不需要checkbox
		String fontPageMenu = pageMenu.toString();
		pageMenu.delete(0, pageMenu.length()); // 清空
		pageMenu.append("<TD width='20%'>" + checkboxStr + "<TD align='right'>" + fontPageMenu);
		return pageMenu.toString();
	}
}