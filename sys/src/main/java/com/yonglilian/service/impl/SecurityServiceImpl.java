package com.yonglilian.service.impl;

import com.yonglilian.common.util.CollectionUtil;
import com.yonglilian.common.util.ReflectionUtil;
import com.yonglilian.common.util.StringUtils;
import com.yonglilian.dao.UserMapper;
import com.yonglilian.dao.mapper.SecurityMapper;
import com.yonglilian.model.BPIP_MENU;
import com.yonglilian.model.BPIP_ROLE;
import com.yonglilian.model.BPIP_USER;
import com.yonglilian.model.BPIP_USER_ROLE;
import com.yonglilian.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统安全管理服务层实现
 * @author lwk
 *
 */
@Service
public class SecurityServiceImpl implements SecurityService {
	/** The SecurityServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityServiceImpl.class);
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
	/** 系统管理数据层. */
	@Autowired
	private SecurityMapper securityMapper;

	@Override
	public BPIP_MENU[] getMenuList(String strWhere) throws Exception {
		BPIP_MENU[] retValue = null;
		String strSQL = "Select MENUNO,MENUNAME,ISPOWER,URL,ISOPEN,FLAG,MNCODE From BPIP_MENU " + strWhere;
		List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
		int length = retList != null ? retList.size() : 0;
		if (length > 0) {
			retValue = new BPIP_MENU[length];
			for (int i = 0; i < length; i++) {
				Map<String, Object> retMap = retList.get(i);
	        	CollectionUtil.convertMapNullToStr(retMap);
				retValue[i] = new BPIP_MENU();
				retValue[i].setMENUNO(retMap.get("MENUNO").toString());
				retValue[i].setMENUNAME(retMap.get("MENUNAME").toString());
				retValue[i].setISPOWER(retMap.get("ISPOWER").toString());
				retValue[i].setURL(retMap.get("URL").toString());
				retValue[i].setISOPEN(retMap.get("ISOPEN").toString());
				retValue[i].setFLAG(retMap.get("FLAG").toString());
				retValue[i].setMNCODE(retMap.get("MNCODE").toString());
			}
		}
		return retValue;
	}

	@Override
	public BPIP_MENU[] getMenuList1(String strPARENTNO) throws Exception {
		BPIP_MENU[] retValue = null;
		String strSQL = "";
		strSQL = "Select MENUNO,MENUNAME,ISPOWER,URL,ISOPEN,FLAG,MNCODE From BPIP_MENU where MENUNO like '"
				+ strPARENTNO + "___' order by MNCODE,MENUNO";
		List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
		int length = retList != null ? retList.size() : 0;
		if (length > 0) {
			retValue = new BPIP_MENU[length];
			for (int i = 0; i < length; i++) {
				Map<String, Object> retMap = retList.get(i);
	        	CollectionUtil.convertMapNullToStr(retMap);
				retValue[i] = new BPIP_MENU();
				retValue[i].setMENUNO(retMap.get("MENUNO").toString());
				retValue[i].setMENUNAME(retMap.get("MENUNAME").toString());
				retValue[i].setISPOWER(retMap.get("ISPOWER").toString());
				retValue[i].setURL(retMap.get("URL").toString());
				retValue[i].setISOPEN(retMap.get("ISOPEN").toString());
				retValue[i].setFLAG(retMap.get("FLAG").toString());
				retValue[i].setMNCODE(retMap.get("MNCODE").toString());
			}
		}
		return retValue;
	}

	@Override
	public BPIP_MENU[] getMenuAllList() throws Exception {
		BPIP_MENU[] retValue = null;
		String strSQL = "";
		strSQL = "Select MENUNO,MENUNAME,ISPOWER,URL,ISOPEN,FLAG,MNCODE From BPIP_MENU Where ISPOWER = '1' order by MNCODE,MENUNO ";
		List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
		int length = retList != null ? retList.size() : 0;
		if (length > 0) {
			retValue = new BPIP_MENU[length];
			for (int i = 0; i < length; i++) {
				Map<String, Object> retMap = retList.get(i);
	        	CollectionUtil.convertMapNullToStr(retMap);
				retValue[i] = new BPIP_MENU();
				retValue[i].setMENUNO(retMap.get("MENUNO").toString());
				retValue[i].setMENUNAME(retMap.get("MENUNAME").toString());
				retValue[i].setISPOWER(retMap.get("ISPOWER").toString());
				retValue[i].setURL(retMap.get("URL").toString());
				retValue[i].setISOPEN(retMap.get("ISOPEN").toString());
				retValue[i].setFLAG(retMap.get("FLAG").toString());
				retValue[i].setMNCODE(retMap.get("MNCODE").toString());
			}
		}
		return retValue;
	}

	@Override
	public boolean addMenu(BPIP_MENU menu, String strPARENTNO) throws Exception {
		if (menu != null) {
			String strMenuNo = "001";// 得到最大编号
			if (menu != null && strPARENTNO != "") {
				int intMoxno = getMaxNo(strPARENTNO, 1);
				if (intMoxno == 0) {
					strMenuNo = strPARENTNO + "001";
				} else {
					strMenuNo = strPARENTNO + String.valueOf(intMoxno + 1001).substring(1, 4);
				}
			} else {
				int intMoxno = getMaxNo("", 0);
				strMenuNo = String.valueOf(intMoxno + 1001).substring(1, 4);
			}
			menu.setMENUNO(strMenuNo);// 得到菜单编号
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("MENUNO", menu.getMENUNO());
			paraMap.put("MENUNAME", menu.getMENUNAME());
			paraMap.put("ISPOWER", menu.getISPOWER());
			paraMap.put("URL", menu.getURL());
			paraMap.put("ISOPEN", menu.getISOPEN());
			paraMap.put("FLAG", menu.getFLAG());
			paraMap.put("MNCODE", menu.getMNCODE());
			
			Integer result = securityMapper.insertMenu(paraMap);
			if (result != null && result > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean deleteMenu(String menuID) throws Exception {
	    //检查要删除的菜单是否有下级菜单
	    String strSQL = "Select count(*) as MenuCount From BPIP_MENU Where MENUNO like '"+menuID+"___'";
	    Integer retInt = userMapper.selectIntExecSQL(strSQL);
	    if (retInt != null && retInt > 0) {
	    	return false;
	    }
	    //将父节点的子菜单数减
	    String[] strSQLs = new String[2];
	    strSQL = "Delete From BPIP_ROLE_RERMISSISSON Where MENUNO='"+menuID+"'";
	    strSQLs[0] =strSQL;
	    userMapper.deleteExecSQL(strSQLs[0]);
	    strSQL = "Delete From BPIP_MENU Where MENUNO='"+menuID+"'";
	    strSQLs[1] =strSQL;
	    userMapper.deleteExecSQL(strSQLs[1]);
	    
	    return true;
	}

	@Override
	public boolean updateMenu(BPIP_MENU menu) throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("MENUNO", menu.getMENUNO());
		paraMap.put("MENUNAME", menu.getMENUNAME());
		paraMap.put("ISPOWER", menu.getISPOWER());
		paraMap.put("URL", menu.getURL());
		paraMap.put("ISOPEN", menu.getISOPEN());
		paraMap.put("FLAG", menu.getFLAG());
		paraMap.put("MNCODE", menu.getMNCODE());
		
		Integer result = securityMapper.updateMenu(paraMap);
		if (result != null && result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public BPIP_ROLE[] getRoleList(String strWhere) throws Exception {
	    BPIP_ROLE[] retValue = null;
	    String execSQL = "Select ROLEID,ROLENAME,DESCRIPTION From BPIP_ROLE "+strWhere;
	    
	    List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(execSQL);
	    int length = retList != null ? retList.size() : 0;
	    if (length > 0) {
	    	retValue = new BPIP_ROLE[length];
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retMap = retList.get(i);
	        	CollectionUtil.convertMapNullToStr(retMap);
	        	retValue[i] = new BPIP_ROLE();
	        	retValue[i].setROLEID(Integer.parseInt(retMap.get("ROLEID").toString()));
	        	retValue[i].setROLENAME(retMap.get("ROLENAME").toString());
	        	retValue[i].setDESCRIPTION(retMap.get("DESCRIPTION").toString());
	        }
	    }
	    return retValue;
	}

	@Override
	public BPIP_ROLE[] getRoleList2(String roleID) throws Exception {
	   BPIP_ROLE[] retValue = null;
	   String strSQL = "Select ROLEID,ROLENAME,DESCRIPTION From BPIP_ROLE where ROLEID="+roleID;
	   
	   List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
	    int length = retList != null ? retList.size() : 0;
	    if (length > 0) {
	    	retValue = new BPIP_ROLE[length];
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retMap = retList.get(i);
	        	CollectionUtil.convertMapNullToStr(retMap);
	        	retValue[i] = new BPIP_ROLE();
	        	retValue[i].setROLEID(Integer.parseInt(retMap.get("ROLEID").toString()));
	        	retValue[i].setROLENAME(retMap.get("ROLENAME").toString());
	        	retValue[i].setDESCRIPTION(retMap.get("DESCRIPTION").toString());
	        }
	    }
	    return retValue;
	}

	@Override
	public BPIP_ROLE[] getRoleList1() throws Exception {
		BPIP_ROLE[] retValue = null;
	    String execSQL = "Select ROLEID,ROLENAME,DESCRIPTION From BPIP_ROLE order by ROLEID";
	    
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(execSQL);
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	    	retValue = new BPIP_ROLE[length];
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retMap = retlist.get(i);
	        	CollectionUtil.convertMapNullToStr(retMap);
	        	retValue[i] = new BPIP_ROLE();
	        	retValue[i].setROLEID(Integer.parseInt(retMap.get("ROLEID").toString()));
	        	retValue[i].setROLENAME(retMap.get("ROLENAME").toString());
	        	retValue[i].setDESCRIPTION(retMap.get("DESCRIPTION").toString());
	        }
	    }
	    return retValue;
	}

	@Override
	public String getRoleUserList(String roleNo, String listName, String Is_Enabled) throws Exception {
	    StringBuffer strResult = new StringBuffer("");
	    strResult.append("<select id='"+listName+"' name='"+listName+
		        "' style='width:350px;height:340px;' multiple "+Is_Enabled+">\r\n");
	    
	    String strSQL = "Select a.ROLEID,a.USERID,a.UNITID,b.USERID,b.NAME,c.UNITID,c.UNITNAME "
	    			  + "from BPIP_USER_ROLE a Left Join BPIP_USER b on a.USERID=b.USERID Left Join BPIP_UNIT c On a.UNITID=c.UNITID "
	    			  + "where a.ROLEID = '"+roleNo+"' order by a.ROLEID,a.USERID";
	    
	    List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
	    int length = retList != null ? retList.size() : 0;
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retMap = retList.get(i);
	        	CollectionUtil.convertMapNullToStr(retMap);
	        	strResult.append("<option value='"+
	        			retMap.get("USERID").toString()+"'>"+
	        			retMap.get("UNITNAME").toString()+"|"+
	        			retMap.get("NAME").toString()+
	              "</option>\r\n");
	        }
	    }
	    strResult.append("</select>\r\n");
	    return strResult.toString();
	}

	@Override
	public String getUserUnitName(String userNo) throws Exception {
	    String strUNITID = "";
	    String strResult = "";
	    String strSQL = "Select UNITID from BPIP_USER where USERID = '"+userNo+"'";
	    
	    String retUNITID = userMapper.selectStrExecSQL(strSQL);
	    if (StringUtils.isNotBlank(retUNITID)) {
	    	strUNITID = retUNITID;
	    	strSQL = "Select UNITNAME from BPIP_UNIT where UNITID = '"+strUNITID+ "'";
	    	String retUNITNAME = userMapper.selectStrExecSQL(strSQL);
	    	strResult = retUNITNAME;
	    }
	    return strResult;
	}

	@Override
	public String getUserTrueName(String userNo) throws Exception {
	    String strSQL = "Select NAME from BPIP_USER where USERID = '"+userNo+"'";
	    String strResult = userMapper.selectStrExecSQL(strSQL);
	    
	    return strResult;
	}

	@Override
	public String getUserAllList(String listName, String Is_Enabled) throws Exception {
	    StringBuffer strResult = new StringBuffer("");
	    strResult.append("<select name='"+listName+
	        "'  style='width:350px;height:340px;' multiple "+Is_Enabled+">\r\n");
	    
	    String strSQL = "Select a.USERID,a.UNITID,a.NAME,b.UNITID,b.UNITNAME "
		    	      + "From BPIP_USER a Left Join BPIP_UNIT b on a.UNITID=b.UNITID "
		    	      + "where a.USERSTATE='0' order by a.UNITID,a.USERID";
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	    	for (int i = 0; i < length; i++) {
	    		Map<String, Object> retMap = retlist.get(i);
	    		CollectionUtil.convertMapNullToStr(retMap);
		        strResult.append("<option value='"+
		        		retMap.get("USERID").toString()+"'>"+
		        		retMap.get("UNITNAME").toString()+"|"+
		        		retMap.get("NAME").toString()+
		              "</option>\r\n");
		        }
	    }
	    strResult.append("</select>\r\n");
	    
	    return strResult.toString();
	}

	@Override
	public boolean addRole(BPIP_ROLE role, String menuNo) throws Exception {
	    boolean strResult = false;
	    List<String> menuNoList = null;
	    String strRoleName = role.getROLENAME();
	    String strRoleID = String.valueOf(role.getROLEID());
	    if (isExitRoleName(strRoleID, strRoleName)) {
	    	return false;
	    }
	    String strSQL = "Select ROLEID,ROLENAME,DESCRIPTION From BPIP_ROLE Where ROLEID="+role.getROLEID();
	    Map<String, Object> retMap = userMapper.selectMapExecSQL(strSQL);
	    if (retMap != null && retMap.size() > 0) {
	        return false;
	    } else {
	    	retMap = new HashMap<String, Object>();
	    	retMap.put("ROLEID", role.getROLEID());
	    	retMap.put("ROLENAME", role.getROLENAME());
	    	retMap.put("DESCRIPTION", role.getDESCRIPTION());
	    	Integer retInt = securityMapper.insertRole(retMap);
	    	if (retInt != null && retInt > 0) {
	    		// 得到刚插入的角色ID
	    		strRoleID = String.valueOf(getRoleID(strRoleName));
	    		menuNo += ",";
	    		try {
	    			menuNoList = getArrayList(menuNo, ",");
	    			// 保存角色对应菜单
	    			if (menuNoList.size() > 0) {
	    				for (int i = 0; i < menuNoList.size(); i++) {
	    					strSQL = "Insert into BPIP_ROLE_RERMISSISSON(ROLEID,MENUNO) values ("+
	    							strRoleID+",'"+menuNoList.get(i).trim()+"')";
	    					userMapper.insertExecSQL(strSQL);
	    				}
	    				strResult = true;
	    			}
	    		} catch(Exception ex) {
	    			LOGGER.error("SecurityServiceImpl.addRole Exception:\n", ex);
	    		}
	    	}
	    }
	    return strResult;
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
	public Integer getRoleID(String roleName) throws Exception {
	    String strSQL = "Select ROLEID from BPIP_ROLE where ROLENAME = '"+roleName+ "'";
	    Integer retRoleID = userMapper.selectIntExecSQL(strSQL);
	    
	    return retRoleID;
	}

	@Override
	public BPIP_USER[] getRoleUserList(String roleID) throws Exception {
	    BPIP_USER[] retValue = null;
	    String strSQL = "select USERID,LOGINID,USERSTATE,NAME,LCODE,UNITID,SEX,BIRTHDAY,PHONE,MOBILE,"
	    					 + "EMAIL,USERIMAGE,LOGINTIME,PAGESIZE,WINDOWTYPE,DEPTINSIDE,ORBERCODE,ONLINEDATE "
	    					 + "from BPIP_USER where USERID in (select USERID from BPIP_USER_ROLE where ROLEID="+roleID+") order by UNITID";
	    
	    List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
	    int length = retList != null ? retList.size() : 0;
	    if (length > 0) {
	        retValue = new BPIP_USER[length];
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retMap = retList.get(i);
	        	retValue[i] = (BPIP_USER) ReflectionUtil.convertMapToBean(retMap, BPIP_USER.class);
	        }
	    }
	    return retValue;
	}

	@Override
	public BPIP_MENU[] getRoleMenuList(String roleID) throws Exception {
	    BPIP_MENU[] retValue = null;
	    String  strSQL = "select MENUNO,MENUNAME,ISPOWER,URL,ISOPEN,FLAG,MNCODE "
	    			   + "From BPIP_MENU where MENUNO in (select MENUNO from BPIP_ROLE_RERMISSISSON "
	    			   + "where ROLEID=" + roleID + ") order by MNCODE,MENUNO";
	    
	    List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
	    int length = retList != null ? retList.size() : 0;
	    if (length > 0) {
	    	retValue = new BPIP_MENU[length];
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retMap = retList.get(i);
	        	retValue[i] = (BPIP_MENU) ReflectionUtil.convertMapToBean(retMap, BPIP_MENU.class);
	        }
	    }
	    return retValue;
	}

	@Override
	public String getMenuParentName(String menuNo, int intFlag) throws Exception {
	    String strResult = "";
	    String strSQL = "";
	    switch (intFlag) {
	      case 1: // '\000'
	        strSQL = "SELECT MENUNAME FROM BPIP_MENU WHERE FLAG='1' AND MENUNO = '"+
	        		menuNo.substring(0, 3)+"'";
	        break;
	      case 2: // '\000000'
	        strSQL = "SELECT MENUNAME FROM BPIP_MENU WHERE FLAG='1' AND MENUNO = '"+
	        		menuNo.substring(0, menuNo.length() - 3)+"'";
	        break;
	    }
	    String retMENUNAME = userMapper.selectStrExecSQL(strSQL);
	    strResult = retMENUNAME;
	    
	    return strResult;
	}

	@Override
	public boolean doRoleMenuDel(String roleNo, String menuNo) throws Exception {
		menuNo = menuNo.replaceAll("\\,","','");
        String strSQL = "DELETE FROM BPIP_ROLE_RERMISSISSON WHERE ROLEID = "+
        				 roleNo+" and MENUNO in ('"+menuNo+"')";
        Integer retInt = userMapper.deleteExecSQL(strSQL);
        if (retInt != null && retInt > 0) {
        	return true;
        }
        return false;
	}

	@Override
	public boolean doRoleUserDel(String roleNo, String userNo) throws Exception {
		userNo = userNo.replaceAll("\\,","','");
        String strSQL = "DELETE FROM BPIP_USER_ROLE WHERE ROLEID="+roleNo+" and USERID in ('"+userNo+"')";
        
        Integer retInt = userMapper.deleteExecSQL(strSQL);
        if (retInt != null && retInt > 0) {
        	return true;
        }
        return false;
	}

	@Override
	public boolean editRole(BPIP_ROLE role, String menuNo) throws Exception {
	    boolean strResult = false;
	    List<String> menuNoList = null;
	    String strSQL = "";
	    String strRoleName = role.getROLENAME();
	    String strRoleID = String.valueOf(role.getROLEID());
	    if (isExitRoleName(strRoleID, strRoleName) && isExitRoleID(strRoleID)) {
	    	return false;
	    }
	    strSQL = "DELETE FROM BPIP_ROLE_RERMISSISSON WHERE ROLEID = "+strRoleID+" ";
	    userMapper.deleteExecSQL(strSQL);
	    menuNo += ",";
	    Map<String, Object> tmpMap = new HashMap<String, Object>();
	    tmpMap.put("ROLEID", role.getROLEID());
	    tmpMap.put("ROLENAME", role.getROLENAME());
	    tmpMap.put("DESCRIPTION", role.getDESCRIPTION());
	    
	    Integer retInt = securityMapper.updateRole(tmpMap);
	    if (retInt != null && retInt > 0) {
	        try {
	        	menuNoList = getArrayList(menuNo, ",");
	        	// 保存角色对应菜单
		        if (menuNoList.size() > 0) {
		        	for (int i = 0; i < menuNoList.size(); i++) {
		        		strSQL = "Insert into BPIP_ROLE_RERMISSISSON(ROLEID,MENUNO) values ("+
		        				strRoleID+",'"+menuNoList.get(i).toString().trim()+ "')";
		        		
		        		Integer retVal = userMapper.insertExecSQL(strSQL);
		        		if (retVal != null && retVal > 0) {
		        			strResult = true;
		        		}
		        	}
		       }
	        } catch(Exception ex) {
    			LOGGER.error("SecurityServiceImpl.editRole Exception:\n", ex);
	        }
	    }
	    return strResult;
	}

	@Override
	public boolean editRoleUser(String strRole, String unitId, String formFields) throws Exception {
		boolean strResult = true;
		try {
			String strSQL = "";
		    String strRoleID = strRole;
		    // 保存角色对应用户
		    List<String> userNoList = getArrayList(formFields, ",");
		    if (formFields == null || formFields.trim().length() == 0) {
		        strSQL = "DELETE FROM BPIP_USER_ROLE WHERE ROLEID = "
		        		+ strRoleID+" and UNITID Like '"+unitId+"%' ";
		        Integer retInt = userMapper.deleteExecSQL(strSQL);
		        if (retInt != null && retInt > 0) {
		          strResult = true;
		        } else {
		          strResult = false;
		        }
		        return strResult;
		    } else {
		    	String strl = formFields.substring(0, formFields.length() - 1);
		        doRole_User_Del1(strRoleID, strl, unitId);
		    }
		    for (int i = 0; i < userNoList.size(); i++) {
		        // 检查待插入的记录是否存在
		        strSQL = "Select count(*) as RCount From BPIP_USER_ROLE Where ROLEID="
		        		+ strRoleID+" and USERID='"+ userNoList.get(i).toString().trim()+"'";
		        
		        Integer retCount = userMapper.selectIntExecSQL(strSQL);
		        if (retCount == null || retCount == 0) {
		            strSQL = "Insert into BPIP_USER_ROLE(USERID,ROLEID,UNITID) values ('"
		            		+ userNoList.get(i).toString().trim()+"',"+strRoleID
		            		+ ",'"+getUserUnitId(userNoList.get(i).toString().trim())+"')";
		            
		            userMapper.insertExecSQL(strSQL);
		        }
		    }
		} catch (Exception ex) {
			strResult = false;
			LOGGER.error("SecurityServiceImpl.editRoleUser Exception:\n", ex);
		}
		return strResult;
	}

	@Override
	public boolean doRoleDel(String roleID) throws Exception {
		boolean result = false;
		try {
			String[] strSQLs = new String[3];
			strSQLs[0]="DELETE FROM BPIP_USER_ROLE WHERE ROLEID = "+roleID;
		    strSQLs[1]="DELETE FROM BPIP_ROLE_RERMISSISSON WHERE ROLEID = "+roleID;
		    strSQLs[2]="DELETE FROM BPIP_ROLE WHERE ROLEID = "+roleID;
		    
		    userMapper.deleteExecSQL(strSQLs[0]);
		    userMapper.deleteExecSQL(strSQLs[1]);
		    userMapper.deleteExecSQL(strSQLs[2]);
		} catch (Exception ex) {
			result = false;
			LOGGER.error("SecurityServiceImpl.editRoleUser Exception:\n", ex);
		}
		return result;
	}

	@Override
	public boolean delTableData(String strTable, String strID, String strType) throws Exception {
		String strSQL = "";
	    String strPRIMARYKEY = "ID";
	    boolean result = false;
	    try {
	    	// 根据表名得到表的关键字段的名称
		    strSQL = "Select PRIMARYKEY from BPIP_TABLE where TABLENAME = '"+strTable+"'";
		    strPRIMARYKEY = userMapper.selectStrExecSQL(strSQL);
		    if (strType.equals("1")) {
		    	strSQL = "DELETE FROM "+strTable+" WHERE "+strPRIMARYKEY+" = '"+strID+"'";
		    } else {
		    	strSQL = "DELETE FROM "+strTable+" WHERE "+strPRIMARYKEY+" = "+strID;
		    }
		    userMapper.deleteExecSQL(strSQL);
		    result = true;
	    } catch (Exception ex) {
			result = false;
			LOGGER.error("SecurityServiceImpl.delTableData Exception:\n", ex);
	    }
	    return result;
	}

	@Override
	public boolean doUserRoleManage(String roelListStr, String userListStr) throws Exception {
		boolean result = false;
	    String strSQL = "";
	    List<String> arrList_RoelList = null;
	    List<String> arrList_UserList = null;
	    roelListStr += ",";
	    userListStr += ",";
	    try {
	        arrList_RoelList = getArrayList(roelListStr, ",");
	        arrList_UserList = getArrayList(userListStr, ",");
	        if (arrList_UserList.size() > 0) {
	        	for (int i = 0; i < arrList_UserList.size(); i++) {
	        		//首先删除该用户已经分配的所有角色
	        		strSQL = "Delete from BPIP_USER_ROLE Where USERID = '"+arrList_UserList.get(i).toString().trim()+"'";
	        		userMapper.deleteExecSQL(strSQL);
	        		
	        		if (!roelListStr.equals("0")) {
	        			if (arrList_RoelList.size() > 0) {
	        				for (int k = 0; k < arrList_RoelList.size(); k++) {
	        					//保存用户对应的角色
	        					strSQL = "Insert into BPIP_USER_ROLE(USERID,ROLEID,UNITID) values ('"+
					                      arrList_UserList.get(i).toString().trim()+"',"+
					                      arrList_RoelList.get(k).toString().trim()+
					                      ",'"+getUserUnitId(arrList_UserList.get(i).toString().trim())+"')";
	        					
	        					userMapper.insertExecSQL(strSQL);
	        				}
	        			}
	        		}
	        		result = true;
	        	}
	        }
	    } catch(Exception ex) {
			result = false;
			LOGGER.error("SecurityServiceImpl.delTableData Exception:\n", ex);
	    }
	    return result;
	}

	@Override
	public BPIP_USER_ROLE[] getUserRoleList(String userID) throws Exception {
       BPIP_USER_ROLE[] retValue = null;
       String strSQL = "select DISTINCT USERID,ROLEID,UNITID from BPIP_USER_ROLE "
       				 + "where USERID = '"+userID+"' order by ROLEID";
       
       List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
       int length = retList != null ? retList.size() : 0;
       if (length > 0) {
    	 retValue = new BPIP_USER_ROLE[length];
    	 for (int i = 0; i < length; i++) {
    		 Map<String, Object> retMap = retList.get(i);
    		 retValue[i] = (BPIP_USER_ROLE) ReflectionUtil.convertMapToBean(retMap, BPIP_USER_ROLE.class);
    	 }
       }
       return retValue;
	}

	@Override
	public String getRoleTrueName(String roleNo) throws Exception {
	     String strSQL = "Select ROLENAME from BPIP_ROLE where ROLEID = '"+roleNo+"'";
	     String strResult = userMapper.selectStrExecSQL(strSQL);
	     
	     return strResult;
	}

	@Override
	public Integer getRoleId() throws Exception {
	     int returnValue = 0;
	     String strSQL = "Select MAX(ROLEID) AS MaxNo from BPIP_ROLE";
	     Integer retVal = userMapper.selectIntExecSQL(strSQL);
	     if (retVal != null && retVal > 0) {
	       returnValue = retVal+1;
	     }
	     return returnValue;
	}

	@Override
	public BPIP_ROLE[] getXzRoleList(String unitId) throws Exception {
	     BPIP_ROLE[] retValue = null;
	     String querySQL = "Select ROLEID,ROLENAME,DESCRIPTION From BPIP_ROLE order by ROLEID";
	     
	     List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(querySQL);
	     int length = retList != null ? retList.size() : 0;
	     if (length > 0) {
	    	 retValue = new BPIP_ROLE[length];
	         for (int i = 0; i < length; i++) {
	        	 Map<String, Object> retMap = retList.get(i);
	        	 retValue[i] = (BPIP_ROLE) ReflectionUtil.convertMapToBean(retMap, BPIP_ROLE.class);
	         }
	     }
	     return retValue;
	}

	@Override
	public BPIP_MENU[] getMenuAllListRole(String roleId) throws Exception {
	    BPIP_MENU[] retValue = null;
	    String strSQL = "select MENUNO,MENUNAME,ISPOWER,URL,ISOPEN,FLAG,MNCODE From BPIP_MENU "
	    		+ "where FLAG='1' AND MENUNO in (select MENUNO from bpip_role_rermississon where ROLEID='"+roleId+"') order by MNCODE,MENUNO";
	    
	    List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
	    int length = retList != null ? retList.size() : 0;
	    if (length > 0) {
	    	retValue = new BPIP_MENU[length];
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retMap = retList.get(i);
	        	retValue[i] = (BPIP_MENU) ReflectionUtil.convertMapToBean(retMap, BPIP_MENU.class);
	        }
	    }
	    return retValue;
	}

	/**
	 * 功能或作用：根据用户编号和角色编号删除用户角色表的记录
	 * @param roleNo 角色ID
	 * @param user_No 用户编号
	 * @param unitId
	 * @return
	 * @throws Exception 
	 */
    private boolean doRole_User_Del1(String roleNo, String userNo, String unitId) throws Exception {
        userNo = userNo.trim().replaceAll("\\,","','");
        String strSQL = "DELETE FROM BPIP_USER_ROLE WHERE ROLEID="+roleNo
        			  + " and UNITID like '"+unitId+"%' and USERID not in ('"+userNo.trim()+"')";
        
        Integer retInt = userMapper.deleteExecSQL(strSQL);
        if (retInt != null && retInt > 0) {
        	return true;
        }
        return false;
    }

	/**
	 * 得到最大的菜单编号
	 * @param strMenuNo
	 * @param intFlag
	 * @return
	 */
	private int getMaxNo(String strMenuNo, int intFlag) {
		int intResult = 0;
		StringBuffer strSQL = new StringBuffer();
		String strMaxNO = "0";
		switch (intFlag) {
		case 0: // '\0'
			strSQL.append("SELECT MAX(MENUNO) AS MAX_NO FROM BPIP_MENU WHERE MENUNO like '___'");
			break;
		case 1: // '\001'
			strSQL.append("SELECT MAX(MENUNO) AS MAX_NO FROM BPIP_MENU WHERE MENUNO like '" + strMenuNo + "___'");
			break;
		}
		try {
			strMaxNO = userMapper.selectStrExecSQL(strSQL.toString());
			if (StringUtils.isBlank(strMaxNO)) {
				strMaxNO = "0";
			} else {
				strMaxNO = strMaxNO.substring(strMaxNO.length() - 3, strMaxNO.length());
			}
		} catch (Exception ex) {
			LOGGER.error("SecurityServiceImpl.getMaxNo Exception:\n", ex);
		}
		intResult = Integer.parseInt(strMaxNO);
		return intResult;
	}

	/**
	 * 功能或作用：判断是否已经存在角色
	 * @param roleID 角色ID
	 * @param roleName 角色名称
	 * @return returnValue 返回是否
	 */
	private boolean isExitRoleName(String roleID, String roleName) {
		String strSQL = "";
		if (roleID != "") {
			strSQL = "Select count(*) as RoleCount From BPIP_ROLE Where ROLENAME='" + roleName + "' and ROLEID<>" + roleID;
		} else {
			strSQL = "Select count(*) as RoleCount From BPIP_ROLE Where ROLENAME='" + roleName + "'";
		}
		Integer retInt = 0;
		try {
			retInt = userMapper.selectIntExecSQL(strSQL);
		} catch (Exception ex) {
			LOGGER.error("SecurityServiceImpl.isExitRoleName Exception:\n", ex);
		}
		if (retInt != null && retInt > 0) {
			return true;
		}
		return false;
	}
 
	/**
	 * 功能或作用：判断是否已经存在角色
	 * @param roleID 角色ID
	 * @return
	 * @throws Exception 
	 */
	private boolean isExitRoleID(String roleID) throws Exception {
		boolean blResult = false;
	    String strSQL = "";
	    if (roleID != "") {
	        strSQL = "Select count(*) as RoleCount From BPIP_USER_ROLE Where ROLEID ="+roleID;
	        Integer retInt = userMapper.selectIntExecSQL(strSQL);
	 	    if (retInt != null && retInt > 0) {
	 	    	blResult = true;
	 	    }
	    }
	    return blResult;
	}

	/**
	 * 功能或作用：根据用户编号得到用户的单位编号
	 * @param userNo 用户编号
	 * @return returnValue 返回单位编号
	 * @throws Exception 
	 */
	private String getUserUnitId(String userNo) throws Exception {
	   String strSQL = "Select UNITID from BPIP_USER where USERID = '"+userNo+"'";
	   String retUnitID = userMapper.selectStrExecSQL(strSQL);
	   
	   return retUnitID;
	}

}