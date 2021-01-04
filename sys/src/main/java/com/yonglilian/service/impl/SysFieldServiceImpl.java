package com.yonglilian.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yonglilian.common.util.FunctionMessage;
import com.yonglilian.common.util.StringUtils;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.UserMapper;
import com.yonglilian.dao.mapper.FieldMapper;
import com.yonglilian.dao.mapper.TableMapper;
import com.yonglilian.model.dbmanage.BPIP_FIELD;
import com.yonglilian.service.FieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBRow;
import zr.zrpower.common.db.DBSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字段名表管理服务层实现
 * @author lwk
 *
 */
@Service
public class SysFieldServiceImpl implements FieldService {
	/** The FieldServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FieldServiceImpl.class);
	private DBEngine dbEngine; // 数据库引擎
	private static int clients = 0;
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
	/** 表名表管理数据层. */
	@Autowired
	private TableMapper tableMapper;
	/** 字段名表管理数据层. */
	@Autowired
	private FieldMapper fieldMapper;

	/**
	 * 构造方法
	 */
	public SysFieldServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients<1) {
		}
		clients++; 
	}

	@Override
	public FunctionMessage addTC(BPIP_FIELD TC) throws Exception {
		String strSql = "";
		FunctionMessage funMsg = new FunctionMessage(1);
		try {
			strSql = "Select FIELDID,TABLEID,FIELDNAME,FIELDTAG,CHINESENAME,FIELDTYPE,FIELDLENGTH,ISNULL," 
						   +"DICTTABLE,DESCRIPTION,TAGEXT,AUTO1,AUTO2,AUTO3,BLOBSIZE,QFIELD,ISKEY " 
					+"From BPIP_FIELD Where FIELDID='"+TC.getFIELDID()+"'";
			
		    Map<String, Object> retMap = userMapper.selectMapExecSQL(strSql);
			if (retMap != null && retMap.size() > 0) {
				funMsg.setResult(false);
				funMsg.setMessage("字段名【"+TC.getFIELDID()+"】已经存在");
			} else {
				DBRow dbrow = TC.getData();// 数据表实体映射包
				String strMaxNo = getMaxFIELDID();//设置FIELDID
				
		        Map<String, Object> paraMap = new HashMap<String, Object>();
		        paraMap.put("FIELDID", strMaxNo);//设置FIELDID
		        paraMap.put("TABLEID", dbrow.Column("TABLEID").getString());
		        paraMap.put("FIELDNAME", dbrow.Column("FIELDNAME").getString());
		        paraMap.put("FIELDTAG", dbrow.Column("FIELDTAG").getString());
		        paraMap.put("CHINESENAME", dbrow.Column("CHINESENAME").getString());
		        paraMap.put("FIELDTYPE", dbrow.Column("FIELDTYPE").getString());
		        Integer FIELDLENGTH = null;
		        if (StringUtils.isNotBlank(dbrow.Column("FIELDLENGTH").getString())) {
		        	FIELDLENGTH = Integer.parseInt(dbrow.Column("FIELDLENGTH").getString());
		        }
		        paraMap.put("FIELDLENGTH", FIELDLENGTH);
		        paraMap.put("ISNULL", dbrow.Column("ISNULL").getString());
		        paraMap.put("DICTTABLE", dbrow.Column("DICTTABLE").getString());
		        paraMap.put("DESCRIPTION", dbrow.Column("DESCRIPTION").getString());
		        Integer TAGEXT = null;
		        if (StringUtils.isNotBlank(dbrow.Column("TAGEXT").getString())) {
		        	TAGEXT = Integer.parseInt(dbrow.Column("TAGEXT").getString());
		        }
		        paraMap.put("TAGEXT", TAGEXT);
		        paraMap.put("AUTO1", dbrow.Column("AUTO1").getString());
		        paraMap.put("AUTO2", dbrow.Column("AUTO2").getString());
		        paraMap.put("AUTO3", dbrow.Column("AUTO3").getString());
		        Integer BLOBSIZE = null;
		        if (StringUtils.isNotBlank(dbrow.Column("BLOBSIZE").getString())) {
		        	BLOBSIZE = Integer.parseInt(dbrow.Column("BLOBSIZE").getString());
		        }
		        paraMap.put("BLOBSIZE", BLOBSIZE);
		        paraMap.put("QFIELD", dbrow.Column("QFIELD").getString());
		        paraMap.put("ISKEY", dbrow.Column("ISKEY").getString());
		        
		        Integer result = tableMapper.insertField(paraMap);
				if (result != null && result > 0) {
					funMsg.setMessage("录入成功");
					funMsg.setResult(true);
				}
			}
		} catch(Exception e) {
			LOGGER.error("FieldServiceImpl.addTC Exception:\n", e);
			funMsg.setResult(false);
			funMsg.setMessage("调用方法AddTC出现异常"+e.toString());
		    return funMsg;
		  }
		  return funMsg;
	}

	@Override
	public FunctionMessage editTC(BPIP_FIELD TC) throws Exception {
	    FunctionMessage funMsg = new FunctionMessage(1);
	    try {
	    	DBRow dbrow = TC.getData();// 数据表实体映射包
	    	
	    	Map<String, Object> paraMap = new HashMap<String, Object>();
	    	paraMap.put("FIELDID", dbrow.Column("FIELDID").getString());//设置FIELDID
	        paraMap.put("TABLEID", dbrow.Column("TABLEID").getString());
	        paraMap.put("FIELDNAME", dbrow.Column("FIELDNAME").getString());
	        paraMap.put("FIELDTAG", dbrow.Column("FIELDTAG").getString());
	        paraMap.put("CHINESENAME", dbrow.Column("CHINESENAME").getString());
	        paraMap.put("FIELDTYPE", dbrow.Column("FIELDTYPE").getString());
	        Integer FIELDLENGTH = null;
	        if (StringUtils.isNotBlank(dbrow.Column("FIELDLENGTH").getString())) {
	        	FIELDLENGTH = Integer.parseInt(dbrow.Column("FIELDLENGTH").getString());
	        }
	        paraMap.put("FIELDLENGTH", FIELDLENGTH);
	        paraMap.put("ISNULL", dbrow.Column("ISNULL").getString());
	        paraMap.put("DICTTABLE", dbrow.Column("DICTTABLE").getString());
	        paraMap.put("DESCRIPTION", dbrow.Column("DESCRIPTION").getString());
	        Integer TAGEXT = null;
	        if (StringUtils.isNotBlank(dbrow.Column("TAGEXT").getString())) {
	        	TAGEXT = Integer.parseInt(dbrow.Column("TAGEXT").getString());
	        }
	        paraMap.put("TAGEXT", TAGEXT);
	        paraMap.put("AUTO1", dbrow.Column("AUTO1").getString());
	        paraMap.put("AUTO2", dbrow.Column("AUTO2").getString());
	        paraMap.put("AUTO3", dbrow.Column("AUTO3").getString());
	        Integer BLOBSIZE = null;
	        if (StringUtils.isNotBlank(dbrow.Column("BLOBSIZE").getString())) {
	        	BLOBSIZE = Integer.parseInt(dbrow.Column("BLOBSIZE").getString());
	        }
	        paraMap.put("BLOBSIZE", BLOBSIZE);
	        paraMap.put("QFIELD", dbrow.Column("QFIELD").getString());
	        paraMap.put("ISKEY", dbrow.Column("ISKEY").getString());
		     
		     Integer result = tableMapper.editField(paraMap);
		     if (result != null & result > 0) {
		    	 funMsg.setResult(true);
		    	 funMsg.setMessage("字段名修改成功");
		     } else {
		    	 funMsg.setResult(false);
		    	 funMsg.setMessage("字段名【"+TC.getTABLEID()+"】不存在");
		     }
	    } catch(Exception e) {
	    	LOGGER.error("FieldServiceImpl.editTC Exception:\n", e);
	    	funMsg.setResult(false);
	    	funMsg.setMessage("调用方法EditTC出现异常"+e.toString());
	    	return funMsg;
	    }
	    return funMsg;
	}

	@Override
	public FunctionMessage deleteTC(String ID) throws Exception {
		FunctionMessage funMsg = new FunctionMessage(1);
	    try {
	    	String strSQL = "Delete From BPIP_FIELD Where FIELDID='"+ID+"'";
	        Integer result = userMapper.deleteExecSQL(strSQL);
	        if (result != null && result > 0) {
	        	funMsg.setResult(true);
	        	funMsg.setMessage("字段名【"+ID+"】已经删除");
	        } else {
	        	funMsg.setMessage("删除不成功");
	        	funMsg.setResult(false);
	        }
	    } catch(Exception e) {
	    	LOGGER.error("FieldServiceImpl.deleteTC Exception:\n", e);
	    	funMsg.setResult(false);
	    	funMsg.setMessage("调用方法DeleteTC出现异常"+e.toString());
	        return funMsg;
	    }
	    return funMsg;
	}

	@Override
	public BPIP_FIELD[] getBpipFieldList(String condition) throws Exception {
		BPIP_FIELD bgs[] = null;
//		String strSQL = "";
		try {
//			if (condition == null || condition.trim().length() == 0) {
//		        strSQL = "Select FIELDID,TABLEID,FIELDNAME,FIELDTAG,CHINESENAME,FIELDTYPE,FIELDLENGTH,ISNULL," 
//						   	   +"DICTTABLE,DESCRIPTION,TAGEXT,AUTO1,AUTO2,AUTO3,BLOBSIZE,QFIELD,ISKEY "
//						+"From BPIP_FIELD Order By FIELDID";
//			} else {
//				condition = condition.trim();
//				strSQL = "Select FIELDID,TABLEID,FIELDNAME,FIELDTAG,CHINESENAME,FIELDTYPE,FIELDLENGTH,ISNULL," 
//					   	   	   +"DICTTABLE,DESCRIPTION,TAGEXT,AUTO1,AUTO2,AUTO3,BLOBSIZE,QFIELD,ISKEY "
//					   	+"From BPIP_FIELD Where TABLEID ='"+condition+"' Order By FIELDID";
//			}
			if (StringUtils.isBlank(condition)) {
				condition = null;
			}
			List<BPIP_FIELD> fieldList = fieldMapper.getBpipFieldList(condition);
			int length = fieldList != null ? fieldList.size() : 0;
			if (length > 0) {
				bgs = fieldList.toArray(new BPIP_FIELD[length]);
			}
		} catch (Exception e) {
	    	LOGGER.error("FieldServiceImpl.getBpipFieldList Exception:\n", e);
		}
		return bgs;
	}

	@Override
	public BPIP_FIELD getFieldId(String fieldID) throws Exception {
		BPIP_FIELD bp = fieldMapper.getFieldId(fieldID);
	    return bp;
	}

	@Override
	public String getMinTABLEID(String strok) throws Exception {
		String result = "";
		String strSql = strok;
		try {
			Map<String, Object> retMap = userMapper.selectMapExecSQL(strSql);
			if (retMap != null && retMap.size() > 0) {
				result = retMap.get("TABLEID1").toString();
			}
		} catch (Exception e) {
	    	LOGGER.error("FieldServiceImpl.getMinTABLEID Exception:\n", e);
		}
		return result;
	}

	@Override
	public BPIP_FIELD[] getCHINESENAME(String where) throws Exception {
		BPIP_FIELD[] retValue = null;
		String strSQL = where;//项目中没有引用
		DBSet set = dbEngine.QuerySQL(strSQL, "BPIP_FIELD");
		if (set != null) {
			if (set.RowCount() > 0) {
				retValue = new BPIP_FIELD[set.RowCount()];
				for (int i = 0; i < set.RowCount(); i++) {
					retValue[i] = new BPIP_FIELD();
					retValue[i].fullData(set.Row(i));
				}
			}
			set = null;
		}
		return retValue;
	}

	@Override
	public BPIP_FIELD[] getCHINESENAME1(String tableID) throws Exception {
		BPIP_FIELD[] retValue = null;
		String strSQL = "";
		if (tableID == null || tableID.trim().length() == 0) {
			strSQL = "Select CHINESENAME From BPIP_FIELD Where TABLEID in (Select min(TABLEID) as TABLEID1 From BPIP_TABLE Where TABLETYPE = '1') Order by FIELDID";
		} else{
			strSQL = "Select CHINESENAME From BPIP_FIELD Where TABLEID ='"+tableID+"' Order by FIELDID";
		}
		List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
		int length = retList != null ? retList.size() : 0;
		if (length > 0) {
			retValue = new BPIP_FIELD[length];
			for (int i = 0; i < length; i++) {
				Map<String, Object> tmpMap = retList.get(i);
				Object chineseName = tmpMap.get("CHINESENAME");
				retValue[i] = new BPIP_FIELD();
				retValue[i].setCHINESENAME(chineseName != null ? chineseName.toString() : null);
			}
		}
		return retValue;
	}

	@Override
	public BPIP_FIELD[] getFIELDNAME(String where) throws Exception {
		BPIP_FIELD[] retValue = null;
		String strSQL = where;//项目中没有引用
		DBSet set = dbEngine.QuerySQL(strSQL, "BPIP_FIELD");
		if (set != null) {
			if (set.RowCount() > 0) {
				retValue = new BPIP_FIELD[set.RowCount()];
				for (int i = 0; i < set.RowCount(); i++) {
					retValue[i] = new BPIP_FIELD();
					retValue[i].fullData(set.Row(i));
				}
			}
			set = null;
		}
		return retValue;
	}

	@Override
	public BPIP_FIELD[] getFIELDNAME1(String tableID) throws Exception {
		BPIP_FIELD[] retValue = null;
		String strSQL = "";
		if (tableID.trim().length() == 0) {
		    strSQL = "Select FIELDNAME from BPIP_FIELD where TABLEID in (Select min(TABLEID) as TABLEID1 from BPIP_TABLE where TABLETYPE = '1') order by FIELDID";
		} else{
		    strSQL = "Select FIELDNAME from BPIP_FIELD where TABLEID ='"+tableID+"' order by FIELDID";
		}
		List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
		int length = retList != null ? retList.size() : 0;
		if (length > 0) {
			retValue = new BPIP_FIELD[length];
			for (int i = 0; i < length; i++) {
				Map<String, Object> tmpMap = retList.get(i);
				Object fieldName = tmpMap.get("FIELDNAME");
				retValue[i] = new BPIP_FIELD();
				retValue[i].setFIELDNAME(fieldName != null ? fieldName.toString() : null);
			}
		}
		return retValue;
	}

	@Override
	public String getTableName(String tableID) throws Exception {
		String retValue = "";
		String strSQL = "Select TABLENAME From BPIP_TABLE Where TABLEID = '"+tableID+"'";
		Map<String, Object> retMap = userMapper.selectMapExecSQL(strSQL);
		if (retMap != null && retMap.size() > 0) {
			retValue = retMap.get("TABLENAME").toString();
		}
		return retValue;
	}

	@Override
	public String getTableCHINESENAME(String tableID) throws Exception {
	   String retValue = "";
	   String strSQL = "Select CHINESENAME From BPIP_TABLE Where TABLEID = '"+tableID+"'";
	   Map<String, Object> retMap = userMapper.selectMapExecSQL(strSQL);
	   if (retMap != null && retMap.size() > 0) {
		   retValue = retMap.get("CHINESENAME").toString();
	   }
	   return retValue;
	}

	@Override
	public String getFieldCHINESENAME(String fieldId) throws Exception {
		if (StringUtils.isBlank(fieldId) || fieldId.length()==0) {
			return StringUtils._BLANK;
		}
	    String retValue = "";
	    fieldId = fieldId.trim();
	    String strSQL = "Select CHINESENAME From BPIP_FIELD Where FIELDID = '"+fieldId+"'";
	    Map<String, Object> retMap = userMapper.selectMapExecSQL(strSQL);
	    if (retMap != null && retMap.size() > 0) {
	    	retValue = retMap.get("CHINESENAME").toString();
	    }
		return retValue;
	}

	@Override
	public String sumTable(String ID) throws Exception {
       String sum = "0";
       String strSQL = "select count(*) as count from BPIP_FIELD where TABLEID='"+ID+"'";
       Integer count = userMapper.selectIntExecSQL(strSQL);
       if (count != null) {
           sum = String.valueOf(count);
       }
       return sum;
	}

	@Override
	public String getTaNAME(String tableID) throws Exception {
	    String retValue = "";
	    Map<String, Object> map0, map1, map2;
	    String strSQL0, strSQL1, strSQL2, tableIdStr, fieldIdStr, fieldIdStr1;
	    int i = 0, j = 4, k = 12;
	    while(k<=tableID.length()){
	        tableIdStr = tableID.substring(i, j);
	        fieldIdStr = tableID.substring(i+4, j+8);
	        fieldIdStr1 = tableID.substring(k, k+8);
	        i = j+16;
	        j += 20;
	        k += 20;
	        
	        fieldIdStr = fieldIdStr.trim();
	        fieldIdStr1 = fieldIdStr1.trim();
	        if (fieldIdStr.length()>0 && fieldIdStr1.length()>0) {
	           strSQL0 = "Select CHINESENAME From BPIP_TABLE Where TABLEID = '"+tableIdStr+"'";
	           strSQL1 = "Select CHINESENAME From BPIP_FIELD Where FIELDID = '"+fieldIdStr+"'";
	           strSQL2 = "Select CHINESENAME From BPIP_FIELD Where FIELDID = '"+fieldIdStr1+"'";
	           
	           map0 = userMapper.selectMapExecSQL(strSQL0);
	           map1 = userMapper.selectMapExecSQL(strSQL1);
	           if (fieldIdStr.equals(fieldIdStr1)) {
	        	   map2 = map1;
	           } else {
	        	   map2 = userMapper.selectMapExecSQL(strSQL2);
	           }
	           if (map0 != null && !map0.isEmpty() || map1 != null && !map1.isEmpty() || map2 != null && !map2.isEmpty()) {
	        	   retValue = map0.get("CHINESENAME").toString()+"."+
	        			   	  map1.get("CHINESENAME").toString()+"&"+
	        			   	  map2.get("CHINESENAME").toString()+"&nbsp;&nbsp;";
	           }
	        }
	    }
	    return retValue;
	}

	@Override
	public String[][] getFieldList(String tableID) throws Exception {
		String[][] retValue = null;
	    String strSQL = "";
	    strSQL = "Select FIELDID,CHINESENAME From BPIP_FIELD where TABLEID = '"+tableID+"' Order by FIELDID";
	    List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
	    int length = retList != null ? retList.size() : 0;
	    if (length > 0) {
	    	retValue = new String[length][2];
	    	for (int i = 0; i < length; i++) {
	    		Map<String, Object> retMap = retList.get(i);
	    		retValue[i][0] = retMap.get("FIELDID").toString();
	    		retValue[i][1] = retMap.get("CHINESENAME").toString();
	    	}
	    }
	    return retValue;
	}

	@Override
	public String showFieldSelect(String tableID, String strID) throws Exception {
		return showFieldSelect(getFieldList(tableID), strID);
	}

	@Override
	public String showFieldSelect(String[][] tableList, String strID) throws Exception {
        StringBuffer retValue = new StringBuffer("");
        retValue.append("<option></option>\n");
        int length = tableList != null ? tableList.length : 0;
        if (length > 0) {
        	for (int i = 0; i < length; i++) {
        		if (strID != null) {
        			if (strID.equals(tableList[i][0])) {
        				retValue.append("<option value=\""+tableList[i][0]+
        								"\" selected>"+
        								tableList[i][1]+
        								"</option>\n");
        			} else {
        				retValue.append("<option value=\""+tableList[i][0]+"\">"+
            	   						tableList[i][1]+
            	   						"</option>\n");
        			}
        		} else {
        				retValue.append("<option value=\""+tableList[i][0]+"\">"+
        								tableList[i][1]+
        								"</option>\n");
        		}
        	}
        }
        return retValue.toString();
	}

	@Override
	public String getAllTableFieldName(String tableOrFieldChinesename, String tableOrFieldTablename,
			String tableOrFieldId, String tableOrFieldIdCondition) throws Exception {
	    String retValue = "";
	    if (tableOrFieldChinesename == null) {
	    	tableOrFieldChinesename = "";
	    }
	    tableOrFieldChinesename = tableOrFieldChinesename.trim();
	    if (tableOrFieldChinesename.length()>0) {
	    	String strSQL = "Select "+tableOrFieldChinesename+" From "+tableOrFieldTablename
	    				  +" Where "+tableOrFieldId+" = '"+tableOrFieldIdCondition+"'";
	    	List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
	    	if (retList != null && retList.size()>0) {
	    		if (retList.get(0) != null && retList.get(0).get(""+tableOrFieldChinesename+"") != null) {
	    			retValue = retList.get(0).get(""+tableOrFieldChinesename+"").toString();
	    		}
	    	}
	    }
	    return retValue;
	}

	@Override
	public JSONArray dtselect(String tableName, String fieldName, String sID) throws Exception {
        JSONArray jsonArray = new JSONArray();
        String FIELDTAG = "";
        String DICTTABLE = "";
        String strSQL = "SELECT b.FIELDTAG,b.DICTTABLE FROM BPIP_TABLE AS a LEFT JOIN BPIP_FIELD b ON a.TABLEID = b.TABLEID "
        		       +"WHERE a.TABLENAME = '"+tableName+"' AND b.FIELDNAME = '"+fieldName+"'";
        
        List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
        if (retList != null && retList.size()>0) {
        	Map<String, Object> retmap = retList.get(0);
        	if (retmap != null) {
        		FIELDTAG = retmap.get("FIELDTAG") != null ? retmap.get("FIELDTAG").toString() : "";
        		DICTTABLE = retmap.get("DICTTABLE") != null ? retmap.get("DICTTABLE").toString() : "";
        	}
        }
        //代码表标记
        if (FIELDTAG.equals("2")) {
            strSQL = "Select CODE,NAME From "+DICTTABLE+" where (ISSHOW!='1' or ISSHOW is null) and CODE like '%"+sID+"' Order by CODE";
            List<Map<String, Object>> retList1 = userMapper.selectListMapExecSQL(strSQL);
            int length1 = retList1 != null ? retList1.size() : 0;
            if (length1 > 0) {
            	for (int i = 0; i < length1; i++) {
            		JSONObject jsonObject = new JSONObject();
            		jsonObject.put("CODE", retList1.get(i).get("CODE").toString());
            		jsonObject.put("NAME", retList1.get(i).get("NAME").toString());
            		jsonArray.add(jsonObject);
            	}
            }
        }
        //用户表标记
        if (FIELDTAG.equals("3")) {
        	strSQL = "Select USERID,NAME From BPIP_USER  where USERSTATE='0' and USERID like '%"+sID+"' order by USERID";
        	List<Map<String, Object>> retList2 = userMapper.selectListMapExecSQL(strSQL);
            int length2 = retList2 != null ? retList2.size() : 0;
        	if (length2 > 0) {
        		for(int i = 0; i < length2; i++) {
        			JSONObject jsonObject = new JSONObject();
        			jsonObject.put("CODE", retList2.get(i).get("USERID").toString());
        			jsonObject.put("NAME", retList2.get(i).get("NAME").toString());
        			jsonArray.add(jsonObject);
        		}
        	}
        }
        return jsonArray;
	}

	/**
     * 功能或作用：取出最大FIELDID
     * @Return String 执行后返回一个最大FIELDID
     */
    private String getMaxFIELDID() {
    	String strSql = "", maxNo = "";
    	int lenMaxNo = 0;
    	strSql = "SELECT MAX(FIELDID) AS MaxNo FROM BPIP_FIELD";
    	try {
    		String retMaxNo = userMapper.selectStrExecSQL(strSql);
    		if (retMaxNo != null) {
    			if (retMaxNo.trim().length()==0) {
    				maxNo = "0000000000000000000000001";
    			} else {
    				maxNo = retMaxNo.trim();
    				maxNo = String.valueOf(Integer.parseInt(maxNo) + 1);
    				lenMaxNo = maxNo.length();
    				maxNo = "0000000000000000000000000" + maxNo;
    			}
    		}
    		maxNo = maxNo.substring(17 + lenMaxNo);
    	} catch (Exception ex) {
    		LOGGER.error("FieldServiceImpl.getMaxFIELDID Exception:\n", ex);
    	}
    	return maxNo;
    }
}