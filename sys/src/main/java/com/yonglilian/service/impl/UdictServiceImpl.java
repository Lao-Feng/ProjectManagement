package com.yonglilian.service.impl;

import com.yonglilian.common.util.FunctionMessage;
import com.yonglilian.common.util.StringUtils;
import com.yonglilian.dao.UserMapper;
import com.yonglilian.model.CODE_YWXT;
import com.yonglilian.service.UdictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBRow;

import java.util.List;
import java.util.Map;

/**
 * 字典管理服务层实现
 * @author lwk
 *
 */
@Service
public class UdictServiceImpl implements UdictService {
	/** The UdictServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(UdictServiceImpl.class);
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;

	@Override
	public CODE_YWXT[] getCodeList(String strWhere) throws Exception {
	    CODE_YWXT bgs[] = null;
	    String blank = "";
	    String strTABLENAME = null;
	    String strSql = "Select TABLENAME from BPIP_TABLE where TABLEID = '"+strWhere+"'";
	    
	    Map<String, Object> retMap = userMapper.selectMapExecSQL(strSql);
	    if (retMap != null && !retMap.isEmpty()) {
	        strTABLENAME = retMap.get("TABLENAME").toString();
	     }
	     strSql = "Select CODE,NAME,SPELL,ISSHOW From "+strTABLENAME+" order by CODE";
	     List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSql);
	     int length = retList != null ? retList.size() : 0;
	     if (length > 0) {
	         bgs = new CODE_YWXT[length];
	         for (int i = 0; i < length; i++) {
	        	 Map<String, Object> map = retList.get(i);
	        	 bgs[i] = new CODE_YWXT();
	        	 bgs[i].setCODE(map.get("CODE") != null ? map.get("CODE").toString() : blank);
	        	 bgs[i].setNAME(map.get("NAME") != null ? map.get("NAME").toString() : blank);
	        	 bgs[i].setSPELL(map.get("SPELL") != null ? map.get("SPELL").toString() : blank);
	        	 bgs[i].setISSHOW(map.get("ISSHOW") != null ? map.get("ISSHOW").toString() : blank);
	         }
	     }
	     return bgs;
	}

	@Override
	public CODE_YWXT getDcitId(String strCode, String strTableID) throws Exception {
		CODE_YWXT bp = null;
	    String strTABLENAME = null;
	    String strSql = "", blank = "";
	    try {
	        strSql = "Select TABLENAME From BPIP_TABLE where TABLEID = '"+strTableID+"'";
	        String retTabName = userMapper.selectStrExecSQL(strSql);
	        if (StringUtils.isNotBlank(retTabName)) {
	            strTABLENAME = retTabName;
	            strSql = "Select CODE,NAME,SPELL,ISSHOW from "+strTABLENAME+" where CODE = '"+strCode+"'";
	            Map<String, Object> retMap = userMapper.selectMapExecSQL(strSql);
	            if (retMap != null && retMap.size() > 0) {
	            	bp = new CODE_YWXT();
	                bp.setCODE(retMap.get("CODE") != null ? retMap.get("CODE").toString() : blank);
	                bp.setNAME(retMap.get("NAME") != null ? retMap.get("NAME").toString() : blank);
	                bp.setSPELL(retMap.get("SPELL") != null ? retMap.get("SPELL").toString() : blank);
	                bp.setISSHOW(retMap.get("ISSHOW") != null ? retMap.get("ISSHOW").toString() : blank);
	            }
	         }
	     } catch (Exception e) {
	    	 LOGGER.error("UdictServiceImpl.getDcitId Exception:\n", e);
	     }
	     return bp;
	}

	@Override
	public FunctionMessage addDC(CODE_YWXT DC, String strTableID) throws Exception {
	    FunctionMessage funMsg = new FunctionMessage(1);
		String strTABLENAME = null;
        String strSql = "";
        String strCODE = null;
        String strNAME = null;
        String strSPELL = null;
        String strISSHOW = null;
	    try {
	         DBRow dbrow = DC.getData();// 数据表实体映射包
	         strCODE = dbrow.Column("CODE").getString();
	         strNAME = dbrow.Column("NAME").getString();
	         strSPELL = dbrow.Column("SPELL").getString();
	         strISSHOW = dbrow.Column("ISSHOW").getString();
	         
	         strSql = "Select TABLENAME from BPIP_TABLE where TABLEID = '"+strTableID+"'";
	         Map<String, Object> retMap = userMapper.selectMapExecSQL(strSql);
	         if (retMap != null) {
	             strTABLENAME = retMap.get("TABLENAME").toString();
	         }
	         strSql = "Insert into "+strTABLENAME+"(CODE,NAME,SPELL,ISSHOW) values ('"+strCODE+"','"+strNAME+"','"+strSPELL+"','"+strISSHOW+"')";
	         userMapper.insertExecSQL(strSql);
	         
	         funMsg.setMessage("录入成功");
	         funMsg.setResult(true);
	    } catch(Exception e) {
	    	LOGGER.error("UdictServiceImpl.addDC Exception:\n", e);
	    	funMsg.setResult(false);
	    	funMsg.setMessage("调用方法AddDC出现异常"+e.toString());
	    }
	    return funMsg;
	}

	@Override
	public FunctionMessage editDC(CODE_YWXT DC, String strTableID, String id) throws Exception {  
		FunctionMessage funMsg = new FunctionMessage(1);
        String strSql = "";
        String strCODE = null;
        String strNAME = null;
        String strSPELL = null;
        String strTABLENAME = null;
        String strISSHOW = null;
        try {
        	DBRow dbrow = DC.getData();
	        strCODE = dbrow.Column("CODE").getString();
	        strNAME = dbrow.Column("NAME").getString();
	        strSPELL = dbrow.Column("SPELL").getString();
	        strISSHOW = dbrow.Column("ISSHOW").getString();
	        
	        strSql = "Select TABLENAME from BPIP_TABLE where TABLEID = '"+strTableID+"'";
	        Map<String, Object> retMap = userMapper.selectMapExecSQL(strSql);
	        if (retMap != null) {
	            strTABLENAME = retMap.get("TABLENAME").toString();
	        }
	        strSql = "UPDATE "+strTABLENAME+" SET CODE ='"+strCODE+"',NAME='"+strNAME+"',SPELL='"+strSPELL+"',ISSHOW='"+strISSHOW+"' where CODE ='"+id+"'";
	        userMapper.updateExecSQL(strSql);
	        
	        funMsg.setMessage("修改成功");
	        funMsg.setResult(true);
        } catch(Exception e) {
	    	LOGGER.error("UdictServiceImpl.editDC Exception:\n", e);
        	funMsg.setResult(false);
        	funMsg.setMessage("调用方法EditDC出现异常"+e.toString());
        }
		return funMsg;
	}

	@Override
	public FunctionMessage deleteDC(String ID, String strTableID) throws Exception {
		FunctionMessage fm = new FunctionMessage(1);
        String strSql = "";
        String strTABLENAME = null;
        try {
        	strSql = "Select TABLENAME from BPIP_TABLE where TABLEID = '"+strTableID+"'";
        	strTABLENAME = userMapper.selectStrExecSQL(strSql);
        	
        	strSql = "Delete From "+strTABLENAME+" where CODE='"+ID+"'";
        	userMapper.deleteExecSQL(strSql);
        	
        	fm.setMessage("删除成功");
            fm.setResult(true);
        } catch(Exception e) {
	    	LOGGER.error("UdictServiceImpl.deleteDC Exception:\n", e);
        	fm.setResult(false);
        	fm.setMessage("调用方法DeleteDC出现异常"+e.toString());
        }
        return fm;
	}

	@Override
	public CODE_YWXT[] getCodeList1(String strWhere) throws Exception {
	     CODE_YWXT bgs[] = null;
//	     String strTABLENAME = null; 项目没有引用
//	     String strSql= strWhere;
//	     
//	     dbSet dbset = dbengine.QuerySQL(strSql);
//	     if (dbset != null) {
//	         strTABLENAME = dbset.Row(0).Column("TABLENAME").getString();
//	         dbset = null;
//	     }
//	     strSql = "Select * from "+strTABLENAME;
//	     dbSet dbset1 = dbengine.QuerySQL(strSql);
//	     
//	     if (dbset1 != null) {
//	         bgs = new CODE_YWXT[dbset1.RowCount()];
//	         for (int i = 0; i < dbset1.RowCount(); i++) {
//	        	 bgs[i] = new CODE_YWXT();
//	        	 bgs[i].fullData(dbset1.Row(i));
//	         }
//	         dbset1 = null;
//	     }
	     return bgs;
	}

	@Override
	public String showDictSelect(String dictTable, String strCode) throws Exception {
		return showDictSelect(getDictList(dictTable), strCode);
	}

	@Override
	public String[][] getDictList(String tableName) throws Exception {
		String[][] retValue = null;
		String strSQL = "Select Code,Name From "+tableName+" Order by Code";
		List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
		int length = retList != null ? retList.size() : 0;
		if (length > 0) {
			retValue = new String[length][2];
			for (int i = 0; i < length; i++) {
				retValue[i][0] = retList.get(i).get("Code").toString();
				retValue[i][1] = retList.get(i).get("Name").toString();
			}
		}
		return retValue;
	}

	@Override
	public String showDictSelect(String[][] dictList, String strCode) throws Exception {
		StringBuffer retValue = new StringBuffer("");
		retValue.append("<option></option>\n");
	    if (dictList != null && dictList.length > 0) {
        	for(int i=0;i<dictList.length;i++){
        		if(strCode!=null){
        			if (strCode.equals(dictList[i][0])) {
        				retValue.append("<option value=\""+dictList[i][0]+
                                    	"\" selected>"+
                                    	dictList[i][1]+
                                      "</option>\n");
        			} else {
        				retValue.append("<option value=\""+dictList[i][0]+"\">"+
                			  			dictList[i][1]+
                                      "</option>\n");
        			}
        		} else {
        			retValue.append("<option value=\""+dictList[i][0]+"\">"+
            			  			dictList[i][1]+
                                  "</option>\n");
        		}
        	}
	    }
	    return retValue.toString();
	}

	@Override
	public String getDictName(String tableName, String dictCode) throws Exception {
        String strSQL = "Select Name From "+tableName+" Where Code='"+dictCode+"'";
        String retName = userMapper.selectStrExecSQL(strSQL);
        
        return retName;
	}
}