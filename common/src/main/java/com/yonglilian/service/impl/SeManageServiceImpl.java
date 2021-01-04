package com.yonglilian.service.impl;

import com.yonglilian.common.util.StringWork;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.UserMapper;
import com.yonglilian.service.SeManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 系统管理服务层实现
 * @author lwk
 *
 */
@Service
public class SeManageServiceImpl implements SeManageService {
	private Random random = new Random();
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;

	@Override
	public String getSysFileNameList(String strFLOWID, String strFORMID) throws Exception {
	    String strResult = "";
	    String strSql = "";
	    String strALLPath = "";

	    strSql = "SELECT ID,FILENAME,FILEPATH,YNAME from BPIP_ATT "
	    	   + "WHERE FLOWID = '" + strFLOWID + "' and FORMID = '" + strFORMID + "'";
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        int intList = 0;
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          intList++;
	          strALLPath = retmap.get("FILEPATH").toString()+"/"+ retmap.get("FILENAME").toString();

	          strResult = strResult+intList + "、<a href='/serverfile?type=6&file=" 
	        		    + strALLPath +"&name="+retmap.get("YNAME").toString()+"' target=_blank title='下载'>" + retmap.get("YNAME").toString();
	          strResult = strResult+ "</a><br>";
	        }
	    }
	    else {
	       strResult = strResult+ "<font class='AlertSign'>〖无附件〗</font>";
	    }
	    return strResult;
	}

	/**
	 * 功能：新建文件夹
	 * @param strPath 文件夹的路径
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean createFileFoler(String strPath) throws Exception {
		boolean exists = (new File(strPath)).exists();
	    if (exists) {
	      return false;
	    } else {
	      try {
	        File f = new File(strPath);
	        f.mkdir();
	        return true;
	      } catch (SecurityException e) {
	        e.printStackTrace();
	      }
	    }
	    return false;
	}

	@Override
	public boolean sysAttFileAdd(String strFLOWID, String strFORMID, String strAID, String strFileName,
			String strUserID, String strUserFileID, String strpTitle) throws Exception {
		
	    String strALLPath= SysPreperty.getProperty().UploadFilePath + "/AttManage/"
	    				 + strUserID + "/" + strUserFileID + "/" + strFileName;

	    File objFile = new File(strALLPath.toString());
	    long filesize = objFile.length();
	    filesize = filesize / 1024;
	    String strID = "";
	    String strNum ="";
	    try {
	       strNum = myGetRandom(4);
	    } catch (Exception ex) {
	       strNum = "0";
	    }
	    strID = "A"+GlobalID()+strNum;

	    if (strID.length()>18) {
	       strID = strID.substring(0,18);
	    }
	    String strSql = "INSERT INTO BPIP_ATT(ID,FLOWID,FORMID,AID,FILEID,FILENAME,FILESIZE,FILEPATH,FILEUSERNO,ENTERDATE,YNAME)"
	    			  +" VALUES('" + strID + "','" + strFLOWID + "','" + strFORMID + "','" 
	    			  + strAID + "','" + strUserFileID + "','" + strFileName + "','" 
	    			  + filesize + "','" + strUserID + "/" + strUserFileID + "','" + strUserID 
	    			  + "',sysdate(),'"+strpTitle+"')";
	    userMapper.insertExecSQL(strSql);
	    
	    return true;
	}

	private String myGetRandom(int i) {
        if (i == 0) {
        	return "";
        }
        String revalue = "";
        for (int k = 0; k < i; k++) {
        	revalue = revalue + random.nextInt(9);
        }
        return revalue;
	}

	private String GlobalID(){
       String strResult = "";
       java.util.Date dt = new java.util.Date();
       long lg = dt.getTime();
       Long ld = new Long(lg);
       strResult = ld.toString();
       return strResult;
	}

	@Override
	public String showSysAttFile(String strFLOWID, String strFORMID, String strUserID) throws Exception {
	     String strID = "", strNAME = "";
	     String reValue= "<el-select v-model='attlist' style='width:322px' >\r\n";
	     String strSql= "";
	     if (strUserID.length() > 0) {
	       strSql = "select ID,YNAME from BPIP_ATT where FLOWID='" + strFLOWID 
	    		   + "' and FORMID='" + strFORMID + "' and FILEUSERNO='" + strUserID + "'";
	     } else {
	       strSql = "select ID,YNAME from BPIP_ATT where FLOWID='" + strFLOWID 
	    		  + "' and FORMID='" + strFORMID + "'";
	     }
	     List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
		 int length = retlist != null ? retlist.size() : 0;
	     if (length > 0) {
	         for (int i = 0; i < length; i++) {
	           Map<String, Object> retmap = retlist.get(i);
	           strID = retmap.get("ID").toString();
	           strNAME = retmap.get("YNAME").toString();
	           reValue = reValue + "<el-option value='" + strID + "' label='"+strNAME+"'></el-option>\r\n";
	         }
	     }
	     reValue = reValue + "</el-select>\r\n";
	     return reValue;
	}

	@Override
	public boolean sysAttFileDel(String strFileID) throws Exception {
	    String strALLPath1= "";
	    String strALLPath = "";
	    strALLPath1 = SysPreperty.getProperty().UploadFilePath+"/AttManage";
	    
	    String strSql = "select FILEPATH,FILENAME from BPIP_ATT where ID='" + strFileID + "'";
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(strSql);
	    if (retmap != null && retmap.size() > 0) {
	        strALLPath = strALLPath1 + "/" + retmap.get("FILEPATH").toString() + "/" + retmap.get("FILENAME").toString();
	    }
	    //删除服务器上的文件
	    File file = new File(strALLPath.toString());
	    file.delete();
	    //删除数据库里的记录
	    strSql = "delete From BPIP_ATT where ID='" + strFileID + "'";
	    userMapper.deleteExecSQL(strSql);
	    
	    return true;
	}

	@Override
	public String sysAttFileAllPath(String strFileID) throws Exception {
	    String strALLPath= "";
	    String strALLPath1="";
	    strALLPath1 = SysPreperty.getProperty().UploadFilePath+"/AttManage";

	    String strSql = "select FILEPATH,FILENAME from BPIP_ATT where ID='" + strFileID + "'";
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(strSql);
	    if (retmap != null && retmap.size() > 0) {
	    	strALLPath = strALLPath1+ "/" + retmap.get("FILEPATH").toString() + "/" + retmap.get("FILENAME").toString();
	    }
	    return strALLPath;
	}

	@Override
	public String getSysAttFileName(String strFileID) throws Exception {
	    String strName = "";
	    String strSql = "select FILENAME from BPIP_ATT where ID='" + strFileID + "'";
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(strSql);
	    if (retmap != null && retmap.size() > 0) {
	    	strName = retmap.get("FILENAME").toString();
	    }
	    return strName;
	}

	/**
	 * 功能或作用：格式化日期时间
	 * @param DateValue 输入日期或时间
	 * @param DateType 格式化 EEEE是星期, yyyy是年, MM是月, dd是日, HH是小时, mm是分钟,  ss是秒
	 * @return 输出字符串
	 */
	public String formatDate(String DateValue, String DateType) {
	    String Result;
	    SimpleDateFormat formatter = new SimpleDateFormat(DateType);
	    try {
	      Date mDateTime = formatter.parse(DateValue);
	      Result = formatter.format(mDateTime);
	    } catch (Exception ex) {
	      Result = ex.getMessage();
	    }
	    if (Result.equalsIgnoreCase("1900-01-01")) {
	      Result = "";
	    }
	    return Result;  
	}

	@Override
	public String getAllTrueNameLst(String strType, String strUnitID) throws Exception {
	    String strTrueName = "";
	    int intID = 0;
	    int intCount = 0;
	    String strResult = "";
	    String strSql = "";
	    String mSql = "";
	    StringWork mString = new StringWork();
	    String strTmpUnitID = mString.CutLastZero(strUnitID, 2);
	    if (strType.equals("1")) {
	      strSql = "SELECT UNITID,UNITNAME FROM BPIP_UNIT where (UNITID ='" +
	          strUnitID + "' or UNITID LIKE '%" + strTmpUnitID +
	          "%') and UNITID in (Select UNITID from BPIP_USER) order by UNITID";
	    } else {
	      strSql = "SELECT UNITID,UNITNAME FROM BPIP_UNIT where UNITID in (Select UNITID from BPIP_USER) order by UNITID ";
	    }
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
		int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          strResult = strResult + "<tr height='23'><td>&nbsp;" +
	        		  retmap.get("UNITNAME").toString() + "</td></tr>";
	          mSql = "Select LOGINID,USERID,NAME,USERSTATE from BPIP_USER where USERSTATE='0'  and UNITID='" +
	        		  retmap.get("UNITID").toString() + "' order by ORBERCODE,USERID";
	          
	          List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(mSql);
	  		  int length1 = retlist1 != null ? retlist1.size() : 0;
	          if (length1 > 0) {
	              intCount = 0;
	              for (int j = 0; j < length1; j++) {
	            	Map<String, Object> retmap1 = retlist.get(i);
	                intID++;
	                if (intCount == 0) {
	                  strTrueName = retmap1.get("NAME").toString();
	                  strResult = strResult + "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox' name='record" +
	                      intID + "' value='" +retmap1.get("USERID").toString() + "," +
	                      strTrueName + "'>" +
	                      strTrueName;
	                }
	                else {
	                  strTrueName = retmap1.get("NAME").toString();
	                  strResult = strResult +"&nbsp;&nbsp;<input type='checkbox' name='record" +
	                      intID + "' value='" + retmap1.get("USERID").toString() + "," +
	                      strTrueName + "'>" + strTrueName;
	                }
	                intCount++;
	                if (intCount == 6) {
	                  intCount = 0;
	                  strResult = strResult +"</td></tr>";
	                }
	              }
	              if (intCount < 6) {
	                strResult = strResult +"</td></tr>";
	              }
	          }
	        }
	    }
	    return strResult;
	}

	@Override
	public String getUserNameLst(String strUnitID, String strImage, String strIco) throws Exception {

	    String strTrueName = "";
	    int k = 0;
	    String strchecked = "0";
	    String  strResult= "";
	    String  mSql= "";
	    String strSql = "SELECT UNITID,UNITNAME FROM BPIP_UNIT where (UNITID ='" +
	        strUnitID + "') and UNITID in (Select UNITID from BPIP_USER) order by UNITID";
	    
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
		int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          strResult =  strResult + "<tr height='23'><td colspan='3' align='left'>" +
	        		  retmap.get("UNITNAME").toString() + "</td></tr>";
	          mSql = "Select LOGINID,USERID,NAME,USERSTATE from BPIP_USER where USERSTATE='0'  and UNITID='" +
	        		  retmap.get("UNITID").toString() + "' order by ORBERCODE,USERID";
	          
	          List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(mSql);
	  		  int length1 = retlist1 != null ? retlist1.size() : 0;
	          if (length1 > 0) {
	              for (int j = 0; j < length1; j++) {
	            	Map<String, Object> retmap1 = retlist.get(j);
	                if (j == -1) {
	                  strchecked = "checked";
	                }
	                else {
	                  strchecked = "";
	                }
	                k = k + 1;
	                if (k == 4) {
	                  k = 1;
	                }
	                if (k == 1) {
	                  strResult =  strResult + "<tr>\r\n";
	                }
	                strTrueName = retmap1.get("NAME").toString();
	                strResult =  strResult + "<td align='left' height='25' style='cursor:pointer ' onclick=\"javascript:document.getElementById('"+retmap1.get("USERID").toString()+"').checked='checked';\"><input type='radio' name='record" +
	                    strchecked + "' value='" +
	                    retmap1.get("USERID").toString() + "' id='" +
	                    retmap1.get("USERID").toString() + "'>" +
	                    "<img src='" +
	                    SysPreperty.getProperty().AppUrl + "Zrsysmanage/images/blueimg" +
	                    "/" +
	                    strIco + "' border='0' align='absmiddle'>" + strTrueName +
	                    "</td>";

	                if (k == 3) {
	                   strResult =  strResult + "</tr>\r\n";
	                }
	              }
	              if (k == 1) {
	                strResult =  strResult + "<td></td><td></td></tr>\r\n";
	              }
	              if (k == 2) {
	                strResult =  strResult + "<td></td></tr>\r\n";
	              }
	            }
	        }
	    }
	    return strResult;
	}

	/**
	 * 功能或作用：取出最大的记录流水号
	 * @param TableName 数据库表名
	 * @param FieldName 数据库字段名称
	 * @param FieldLen 数据库字段长度
	 * @Return MaxNo 执行后返回一个MaxNo字符串
	 */
	@SuppressWarnings("unused")
	private String getMaxFieldNo(String TableName, String FieldName, int FieldLen) {
	    String MaxNo = "";
	    int LenMaxNo = 0;
	    String strSQL = "SELECT MAX(" + FieldName + ") AS MaxNo FROM " + TableName;
	    try {
	      String retMaxNo = userMapper.selectStrExecSQL(strSQL);
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
	    }
	    catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	    return MaxNo;
	}
 
	/**
	 * 功能或作用：获取当前服务器日期（字符型）
	 * @return 当前日期（字符型）
	 */
	public String getDateAsStr() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	    String mDateTime = formatter.format(cal.getTime());
	    return (mDateTime);
	}

	@Override
	public int getAttNum(String strFLOWID, String strFORMID) throws Exception {
	    int retValue = 0;
	    String strsql = "Select count(ID) as NUM From BPIP_ATT where FLOWID ='"+strFLOWID+"' and FORMID='"+strFORMID+"'";
	    Integer retNum = userMapper.selectIntExecSQL(strsql);
	    if (retNum != null && retNum > 0) {
	    	retValue = retNum;
	    }
	    return retValue;
	}

	/**
	 * 获取整个目下的文件的大小
	 * @param f File
	 * @return long
	 */
	public long getFileSize (String file) {
	       File objFile = new File(file.toString());
	       long filesize = objFile.length();
	       filesize = filesize / 1024;
	       return filesize;
	}

}