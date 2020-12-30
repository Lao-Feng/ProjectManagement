package zr.zrpower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.collectionengine.COLL_DOC_PRINT;
import zr.zrpower.collectionengine.CollectionInfo;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBRow;
import zr.zrpower.common.db.DBServer;
import zr.zrpower.common.db.DBSet;
import zr.zrpower.common.util.*;
import zr.zrpower.dao.UserMapper;
import zr.zrpower.model.COLL_CONFIG_OPERATE_FIELD;
import zr.zrpower.service.ConfigService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义表单引擎配置管理服务层实现
 * @author lwk
 * 
 */
@Service
public class ConfigServiceImpl implements ConfigService {
	/** The ConfigServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceImpl.class);
	static private int clients = 0;
	/**
	 * 数据库类型，1：Oracle，2：MSSQL，3：MySQL.
	 */
	String DataBaseType = SysPreperty.getProperty().DataBaseType;
	private DBEngine dbEngine;
	/** 自定义表单引擎的核心服务. */
	private CollectionServiceImpl collectService;
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;

	/**
	 * 构造函数
	 */
	public ConfigServiceImpl() {
      dbEngine = new DBEngine(
    		  SysPreperty.getProperty().MainDataSource, 
    		  SysPreperty.getProperty().IsConvert);
      dbEngine.initialize();
      collectService = new CollectionServiceImpl();
      if (clients<1) {
    	  //code
      }
      clients=1;
    }

	@Override
	public CollectionInfo[] allListConfig(String condition) throws Exception {
	    CollectionInfo bgs[] = null;
	    StringBuffer strSql = new StringBuffer();
	     try {
	      if (condition.trim().length() == 0) {
	        strSql.append("Select * From COLL_DOC_CONFIG order by ID");
	      } else {
	        strSql.append("Select * From COLL_DOC_CONFIG where ID='"+condition.trim()+"' order by ID");
	      }
	      List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
	      int length = retlist != null ? retlist.size() : 0;
	      if (length == 0) {
	    	  return null;
	      }
	      bgs = new CollectionInfo[length];
	      for (int i = 0; i < length; i++) {
	    	  Map<String, Object> retmap = retlist.get(i);
	    	  bgs[i] = (CollectionInfo) ReflectionUtil.convertMapToBean(retmap, CollectionInfo.class);
	      }
	      retlist = null;
	    } catch (Exception e) {
	      LOGGER.error("allListConfig出现异常(" + e.toString() + ")！");
	      return null;
	    }
	    return bgs;
	}

	@Override
	public CollectionInfo[] getListConfig() throws Exception {
	    CollectionInfo bgs[] = null;
	    try {
	       String strSql = "Select B.ID, B.DOCNAME, B.TEMPLAET, B.DOCTYPE, C.CHINESENAME "
	       		  		 + "From COLL_DOC_CONFIG B Left Join BPIP_TABLE C on B.MAINTABLE=C.TABLEID "
	       		  		 + "order by B.ID, B.DOCNAME";
	       List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
	       int length = retlist != null ? retlist.size() : 0;
	       if (length == 0) {
	           return null;
	       }
	       bgs = new CollectionInfo[length];
	       for (int i = 0; i < length; i++) {
	    	   Map<String, Object> retmap = retlist.get(i);
	    	   CollectionUtil.convertMapNullToStr(retmap);
	           retmap.put("MAINTABLE", retmap.get("CHINESENAME").toString());
	           
	           bgs[i] = (CollectionInfo) ReflectionUtil.convertMapToBean(retmap, CollectionInfo.class);
	       }
	       retlist = null;
	    } catch (Exception ex) {
	      LOGGER.error("getListConfig出现异常", ex);
	      return null;
	    }
	    return bgs;
	}

	@Override
	public CollectionInfo getInfoID(String id) throws Exception {
       CollectionInfo bp = null;
       StringBuffer strSql = new StringBuffer();
       try {
         strSql.append("Select * From COLL_DOC_CONFIG Where ID='"+id.trim()+"'");
         Map<String, Object> retmap = userMapper.selectMapExecSQL(strSql.toString());
         if (retmap == null || retmap.size() < 1) {
           return null;
         } else {// retmap != null
           bp = (CollectionInfo) ReflectionUtil.convertMapToBean(retmap, CollectionInfo.class);
           retmap = null;
         }
       } catch (Exception e) {
    	   e.printStackTrace();
    	   return null;
       }
       return bp;
	}

	@Override
	public FunctionMessage addConfig(CollectionInfo bg) throws Exception {
      DBServer dbServer = new DBServer();
      FunctionMessage funMsg = dbServer.addConfig(bg);
      return funMsg;
	}

	@Override
	public FunctionMessage editConfig(CollectionInfo bg) throws Exception {
	    boolean isOk = false;
	    FunctionMessage fm = new FunctionMessage(1);
	    StringBuffer addBuf = new StringBuffer();
	     try {
	      DBRow dbrow = bg.getData();
	      addBuf.append("ID='"+bg.getID().trim()+"'");
	      isOk = dbEngine.ExecuteEdit(dbrow, addBuf.toString());
	      if (isOk) {
	        fm.setResult(true);
	        fm.setMessage("配置文件修改成功");
	      } else {
	        fm.setResult(false);
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append("配置文件【"+bg.getID().trim()+"】不存在");
	        fm.setMessage(addBuf.toString());
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	      fm.setResult(false);
	      fm.setMessage("调用方法editConfig出现异常" + e.toString());
	      return fm;
	    }
	    collectService.inithashtable();
	    return fm;
	}

	@Override
	public boolean delConfig(String ID) throws Exception {
	   boolean retval = false;
	   StringBuffer strSQL = new StringBuffer();
	   strSQL.append("Select TEMPLAET From COLL_DOC_CONFIG Where ID='"+ID+"'");
	   Map<String, Object> retmap = userMapper.selectMapExecSQL(strSQL.toString());
	   if(retmap != null && retmap.size() > 0) {
	         String strFileName = retmap.get("TEMPLAET").toString();
	         if(strFileName!=null){
	            if(!strFileName.equals("")){
	              File file = new File(strFileName);
	              if(file.exists()){
	                 try {
	                   file.delete();
	                 } catch (Exception ex) {
	                	 ex.printStackTrace();
	                 }
	              }
	            }
	         }
	         retmap = null;
	   }
	   String[] strSQLs = new String[2];
	   strSQL.delete(0,strSQL.length());//清空
	   strSQL.append("Delete From COLL_DOC_PRINT Where DOCID='"+ID+"'");
	   strSQLs[0] = strSQL.toString();
	   strSQL.delete(0,strSQL.length());//清空
	   strSQL.append("Delete From COLL_DOC_CONFIG Where ID='"+ID+"'");
	   strSQLs[1] = strSQL.toString();
	   for (String execSQL : strSQLs) {
		   userMapper.deleteExecSQL(execSQL);
	   }
	   retval = true;
	   return retval;
	}

	@Override
	public Map<String, String> doGetTableList() throws Exception {
	    Map<String, String> resultValue = new HashMap<String, String>();
	    String strSQL = "Select TABLEID,CHINESENAME From BPIP_TABLE";
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = retlist.get(i);
		    	CollectionUtil.convertMapNullToStr(retmap);
	        	resultValue.put(retmap.get("TABLEID").toString(), 
	        					retmap.get("CHINESENAME").toString());
	        }
	        retlist = null;
	    }
	    return resultValue;
	}

	@Override
	public Map<String, String> GetFieldList() throws Exception {
	    Map<String, String> resultValue = new HashMap<String, String>();
	    String strSQL = "Select FIELDID,CHINESENAME From BPIP_FIELD";
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = retlist.get(i);
	        	CollectionUtil.convertMapNullToStr(retmap);
	        	resultValue.put(retmap.get("FIELDID").toString(), 
	        					retmap.get("CHINESENAME").toString());
	        }
	        retlist = null;
	    }
	    return resultValue;
	}

	@Override
	public String getMaxFieldNo(String table) throws Exception {
	    String MaxNo = "";
	    int LenMaxNo = 0;
	    StringBuffer strSQL = new StringBuffer();
	    strSQL.append("SELECT MAX(ID) AS MaxNo FROM "+table.trim());
	    try {
	      DBSet dbset = dbEngine.QuerySQL(strSQL.toString());
	      if (dbset != null) {
	        if (dbset.Row(0).Column("MaxNo").getString().length() == 0) {
	          MaxNo = "00000001";
	        } else {
	          MaxNo = dbset.Row(0).Column("MaxNo").getString();
	          MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
	          LenMaxNo = MaxNo.length();
	          MaxNo = "0000000000000000000000000" + MaxNo;
	          MaxNo = MaxNo.substring(17 + LenMaxNo);
	        }
	        dbset = null;
	      }
	    } catch (Exception ex) {
	    	LOGGER.error("getMaxFieldNo出现异常(" + ex.toString() + ")！");
	    }
	    return MaxNo;
	}

	@Override
	public String showList(String table, String where, String value, 
			String name, String listName, String def) throws Exception {
	    StringBuffer strSQL = new StringBuffer();
	    StringBuffer Result = new StringBuffer();
	    int count = 0;
	    boolean result = false;
	    if (def.trim().length() != 0) {
	    	result = true;
	    }
	    if (where.trim().length() != 0) {
	    	strSQL.append("SELECT * FROM "+table.trim()+" " + where.trim());
	    } else {
	    	strSQL.append("SELECT * FROM "+table.trim());
	    }
	    try {
	      DBSet dbset = dbEngine.QuerySQL(strSQL.toString());
	      if (dbset != null) {
	        if (table.trim().equals("BPIP_TABLE")) {
	          Result.append("<select name='"+listName+
	              "' size='1' class='SelectStyle' onchange='sele()'>"+"\r\n");
	        } else {
	          Result.append("<select name='"+listName+
	              "' size='1' class='SelectStyle'>"+"\r\n");
	        }
	        Result.append("<option value=''> </option>"+"\r\n");
	        while (dbset.RowCount() > count) {
	          if (!result) {
	            if (count == 0) {
	              Result.append("<option value='"+
	            		  dbset.Row(count).Column(value.trim()).getString()+
	                  "' selected>"+dbset.Row(count).Column(name.trim()).getString()+
	                  "</option>"+"\r\n");
	            } else {
	              Result.append("<option value='"+
	            		  dbset.Row(count).Column(value.trim()).getString()+"'>"+
	            		  dbset.Row(count).Column(name.trim()).getString()+"</option>"+
	                  "\r\n");
	            }
	          } else {
	            if (dbset.Row(count).Column(value.trim()).getString().trim().equals(
	                def.trim())) {
	              Result.append("<option value='"+
	            		  dbset.Row(count).Column(value.trim()).getString()+
	                  "' selected>"+dbset.Row(count).Column(name.trim()).getString()+
	                  "</option>"+"\r\n");
	            } else {
	              Result.append("<option value='"+
	            		  dbset.Row(count).Column(value.trim()).getString()+"'>"+
	            		  dbset.Row(count).Column(name.trim()).getString()+"</option>"+
	                  "\r\n");
	            }
	          }
	          count++;
	        }
	        Result.append("</select>"+"\r\n");
	        dbset = null;
	      } else {
	        Result.append("<select name='"+listName+
	            "' size='1' class='SelectStyle'>"+"\r\n");
	        Result.append("<option value=''> </option>"+"\r\n");
	        Result.append("</select>"+"\r\n");
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	    return Result.toString();
	}

	@Override
	public String[][] getTableList() throws Exception {
	     String[][] result = null;
	     String strSQL = "Select TableID, CHINESENAME From BPIP_Table "
	     			   + "where TABLETYPE='2' or TABLETYPE='3' order by CHINESENAME";
	     List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
	     int length = retlist != null ? retlist.size() : 0;
	     if (length > 0) {
	       result = new String[length][2];
	       for (int i=0; i<length; i++) {
	    	   Map<String, Object> retmap = retlist.get(i);
	    	   CollectionUtil.convertMapNullToStr(retmap);
	    	   result[i][0] = retmap.get("TableID").toString();
	    	   result[i][1] = retmap.get("CHINESENAME").toString();
	       }
	       retlist = null;
	     }
	     return result;
	}

	@Override
	public List<Map<String, Object>> getFieldList(String tableID) throws Exception {
	     List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	     StringBuffer strSQL = new StringBuffer();
	     strSQL.append("Select FieldID,CHINESENAME From BPIP_Field Where TableID='"+tableID+"'");
	     List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
	     int length = retlist != null ? retlist.size() : 0;
	     if (length > 0) {
	       for (int i=0; i<length; i++) {
	    	   Map<String, Object> retmap = retlist.get(i);
	    	   String[] saTmp = new String[2];
	    	   saTmp[0] = retmap.get("FieldID").toString();
	    	   saTmp[1] = retmap.get("CHINESENAME").toString();
	    	   result.add(new HashMap<String, Object>(){
	    		   /**
					* 
					*/
				   private static final long serialVersionUID = 1L;
				   {
					   put("id", saTmp[0]);
					   put("text", saTmp[1]);
	    		   }
	    	   });
	       }
	       retlist = null;
	     }
	     return result;
	}

	@Override
	public String[][] getFieldList1(String tableID) throws Exception {
        String[][] result = null;
        StringBuffer strSQL = new StringBuffer();
        strSQL.append("Select FieldID,CHINESENAME From BPIP_Field Where TableID='"+tableID+"'");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
	     int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
          result = new String[length][2];
          for (int i=0; i<length; i++) {
        	  Map<String, Object> retmap = retlist.get(i);
        	  CollectionUtil.convertMapNullToStr(retmap);
        	  result[i][0] = retmap.get("FieldID").toString();
        	  result[i][1] = retmap.get("CHINESENAME").toString();
          }
          retlist = null;
        }
        return result;
	}

	@Override
	public FunctionMessage addPrint(COLL_DOC_PRINT print) throws Exception {
		StringBuffer strSql = new StringBuffer();
	    FunctionMessage fm = new FunctionMessage(1);
	    try {
	        strSql.append("Select * From COLL_DOC_PRINT Where ID='"+print.getID()+"'");
	        DBSet dbset = dbEngine.QuerySQL(strSql.toString());
	        if (dbset != null) {
	          fm.setResult(false);
	          strSql.delete(0,strSql.length());//清空
	          strSql.append("打印模版【"+print.getID()+"】已经存在");
	          fm.setMessage(strSql.toString());
	        } else {
	          String strMaxNo = getMaxFieldNo("COLL_DOC_PRINT");
	          print.setID(strMaxNo);//设置ID
	          if (dbEngine.ExecuteInsert(print.getData())) {
	            fm.setMessage("打印模版录入成功");
	            fm.setResult(true);
	          }
	        }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	        fm.setResult(false);
	        fm.setMessage("调用方法addPrint出现异常" + ex.toString());
	    }
	    return fm;
	}

	@Override
	public FunctionMessage editPrint(COLL_DOC_PRINT print) throws Exception {
	      boolean isOk = false;
	      FunctionMessage fm = new FunctionMessage(1);
	      StringBuffer addBuf = new StringBuffer();
	      try {
	        DBRow dbrow = print.getData();
	        addBuf.append("ID='"+print.getID()+"'");
	        isOk = dbEngine.ExecuteEdit(dbrow, addBuf.toString());
	        if (isOk) {
	          fm.setResult(true);
	          fm.setMessage("打印模版修改成功");
	        } else {
	          fm.setResult(false);
	          addBuf.delete(0,addBuf.length());//清空
	          addBuf.append("打印模版【"+print.getID()+"】不存在");
	          fm.setMessage(addBuf.toString());
	        }
	      } catch (Exception e) {
	    	e.printStackTrace();
	        fm.setResult(false);
	        fm.setMessage("调用方法EditRel出现异常" + e.toString());
	      }
	      return fm;
	}

	@Override
	public FunctionMessage deletePrint(String ID) throws Exception {
	    boolean isOk = false;
	    FunctionMessage fm = new FunctionMessage(1);
	    StringBuffer addBuf = new StringBuffer();
	    try {
	      addBuf.append("ID='"+ID+"'");
	      isOk = dbEngine.ExecuteDelete("COLL_DOC_PRINT", addBuf.toString());
	      if (isOk) {
	        fm.setResult(true);
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append("打印模版【"+ID+"】已经删除");
	        fm.setMessage(addBuf.toString());
	      } else {
	        fm.setMessage("打印模版删除不成功");
	        fm.setResult(false);
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	      fm.setResult(false);
	      fm.setMessage("调用方法DeletePrint出现异常" + e.toString());
	    }
	    return fm;
	}

	@Override
	public COLL_DOC_PRINT[] getPrintList(String type) throws Exception {
	      COLL_DOC_PRINT bgs[] = null;
	      StringBuffer strSql = new StringBuffer();
	      try {
	        strSql.append("Select * from COLL_DOC_PRINT where DOCID = '"+type+"' order by ID");
	        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
	        int length = retlist != null ? retlist.size() : 0;
	        if (length == 0) {
	        	return null;
	        } else {// length > 0
	        	bgs = new COLL_DOC_PRINT[length];
	        	for (int i = 0; i < length; i++) {
	        		Map<String, Object> retmap = retlist.get(i);
	        		bgs[i] = (COLL_DOC_PRINT) ReflectionUtil.convertMapToBean(retmap, COLL_DOC_PRINT.class);
	        	}
	        	retlist = null;
	        }
	      } catch (Exception e) {
	    	  e.printStackTrace();
	      }
	      return bgs;
	}

	@Override
	public COLL_DOC_PRINT getPrintID(String id) throws Exception {
	      COLL_DOC_PRINT bp = null;
	      StringBuffer strSql = new StringBuffer();
	      try {
	          strSql.append("Select * From COLL_DOC_PRINT Where ID='"+id.trim()+"'");
	          Map<String, Object> retmap = userMapper.selectMapExecSQL(strSql.toString());
	          if (retmap != null && retmap.size() > 0) {
	            	bp = (COLL_DOC_PRINT) ReflectionUtil.convertMapToBean(retmap, COLL_DOC_PRINT.class);
	            	retmap = null;
	          }
	      } catch (Exception e) {
	    	  e.printStackTrace();
	      }
	      return bp;
	}

	@Override
	public String getMinID(String tableName, String sID, String ID) throws Exception {
      String result = "";
      StringBuffer strSql = new StringBuffer();
      try {
    	  strSql.append("Select min(ID) as ID1 from "+tableName+" where "+sID+"='" +ID+ "'");
    	  DBSet mdbset = dbEngine.QuerySQL(strSql.toString());
    	  if (mdbset == null) {
    		  return result;
    	  } else {
    		  result = mdbset.Row(0).Column("ID1").getString();
    		  mdbset = null;
    	  }
      } catch (Exception e) {
    	  e.printStackTrace();
      }
      return result;
	}

	@Override
	public boolean updateTempaet(String tempaetID, CollectionInfo tempaet) throws Exception {
	    collectService.inithashtable();
	    DBRow dbrow = tempaet.getData();
	    String name = dbrow.Column("TEMPLAET").getString();
	    StringBuffer strSQL = new StringBuffer();
	    strSQL.append("Update COLL_DOC_CONFIG Set TEMPLAET = '"+name+"' Where ID='"+tempaetID+"'");
	    
	    Integer updateVal = userMapper.updateExecSQL(strSQL.toString());
	    if (updateVal != null && updateVal > 0) {
	    	return true;
	    }
	    return false;
	}

	@Override
	public boolean updatePrintTempaet(String printTempaetID, COLL_DOC_PRINT printTempaet) throws Exception {
		collectService.inithashtable();
	    DBRow dbrow = printTempaet.getData();
	    String name = dbrow.Column("TEMPLAET").getString();
	    StringBuffer strSQL = new StringBuffer();
	    strSQL.append("Update COLL_DOC_PRINT Set TEMPLAET = '"+name+"' Where ID='"+printTempaetID+"'");
	    
	    Integer updateVal = userMapper.updateExecSQL(strSQL.toString());
	    if (updateVal != null && updateVal > 0) {
	    	return true;
	    }
	    return false;
	}

	@Override
	public String getCtrlAbleFieldByFID(String ID) throws Exception {
	      LOGGER.info("getCtrlAbleFieldByFID开始调用...");
	      long startTime = System.currentTimeMillis();
	      StringBuffer resultStr = new StringBuffer("");
	      String ISDISPLAY = "", ISEDIT = "", ISMUSTFILL = "", ISFORCE="", DEFAULT1 = "";
	      StringBuffer addBuf = new StringBuffer();
	      StringBuffer addBuf1 = new StringBuffer();
	      if (DataBaseType.equals("3")) {//mysql数据库
	          addBuf.append("Select A.*, IFNULL(B.ISDISPLAY,'1') ISDISPLAY,"+" IFNULL(B.ISEDIT,'0') ISEDIT,")
	           		.append("IFNULL(B.ISMUSTFILL,'0') ISMUSTFILL,"+"IFNULL(B.ISFORCE,'0') ISFORCE,")
	           		.append("B.DEFAULT1 from "+" (Select a.TABLEID, a.TABLENAME,a.CHINESENAME,b.FIELDNAME,")
	           		.append("b.FIELDID from BPIP_TABLE a, BPIP_FIELD b where b.TABLEID=a.TABLEID ")
	           		.append(" and a.TABLEID in (")
	           		.append(" Select MAINTABLE from COLL_DOC_CONFIG  where ID='"+ID+"') order by TABLEID,FIELDID) A Left outer join ")
	           		.append(" (SELECT * FROM  COLL_CONFIG_OPERATE_FIELD Where FID='"+ID)
	           		.append("') B "+" on concat(A.TABLENAME,'.',A.FIELDNAME)=B.FIELD Order by TABLEID,FIELDID");
	          
	          addBuf1.append("Select A.*, IFNULL(B.ISDISPLAY,'1')  ISDISPLAY,"+" IFNULL(B.ISEDIT,'0') ISEDIT,")
	          		 .append("IFNULL(B.ISMUSTFILL,'0') ISMUSTFILL,"+"IFNULL(B.ISFORCE,'0') ISFORCE,")
	          		 .append("B.DEFAULT1 from  "+" (Select  a.TABLEID,a.TABLENAME,b.FIELDNAME,")
	          		 .append(" b.CHINESENAME,b.FIELDID from BPIP_TABLE a, BPIP_FIELD b where b.TABLEID=a.TABLEID ")
	          		 .append(" and a.TABLEID in (")
	          		 .append(" Select MAINTABLE from COLL_DOC_CONFIG where ID='"+ID+"') order by TABLEID,FIELDID) A Left outer join ")
	          		 .append(" (SELECT * FROM  COLL_CONFIG_OPERATE_FIELD Where FID='"+ID)
	          		 .append("') B "+" on concat(A.TABLENAME,'.',A.FIELDNAME)=B.FIELD Order by TABLEID,FIELDID");
	      } else {
	    	  addBuf.append("Select A.*, nvl(B.ISDISPLAY,'1')  ISDISPLAY,"+" nvl(B.ISEDIT,'0') ISEDIT,")
	           		.append("nvl(B.ISMUSTFILL,'0') ISMUSTFILL,"+"nvl(B.ISFORCE,'0') ISFORCE,")
	           		.append("B.DEFAULT1 from  "+" (Select a.TABLEID, a.TABLENAME,a.CHINESENAME as TABLECHNAME,b.FIELDNAME,")
	           		.append(" b.CHINESENAME as FIELDCHNAME,b.FIELDID from BPIP_TABLE a, BPIP_FIELD b where b.TABLEID=a.TABLEID ")
	           		.append(" and a.TABLEID in (")
	           		.append(" Select MAINTABLE from COLL_DOC_CONFIG  where ID='"+ID+"')) A Left outer join ")
	           		.append(" (SELECT * FROM  COLL_CONFIG_OPERATE_FIELD Where FID='"+ID)
	           		.append("') B "+" on A.TABLENAME||'.'||A.FIELDNAME=B.FIELD Order by TABLEID,FIELDID");
	      }
	      try {
	        List<Map<String, Object>> mdbset = userMapper.selectListMapExecSQL(addBuf.toString()); 
	        List<Map<String, Object>> mdbset1 = null;
	        if (DataBaseType.equals("3")) {//mysql数据库
	        	mdbset1 = userMapper.selectListMapExecSQL(addBuf1.toString());
	        }
	        if (mdbset != null) {
	          for (int i = 0; i < mdbset.size(); i++) {
	        	Map<String, Object> retmap = mdbset.get(i);
	        	CollectionUtil.convertMapNullToStr(retmap);
	            if (DataBaseType.equals("3")) {//mysql数据库
	                resultStr.append(retmap.get("TABLENAME").toString() + "#" 
	                			   + retmap.get("CHINESENAME").toString() + "#" 
	                			   + retmap.get("FIELDNAME").toString() + "#" 
	                			   + mdbset1.get(i).get("CHINESENAME").toString() + "#");
	            } else {
	                resultStr.append(retmap.get("TABLENAME").toString() + "#" 
	                			   + retmap.get("TABLECHNAME").toString() + "#" 
	                			   + retmap.get("FIELDNAME").toString() + "#" 
	                			   + retmap.get("FIELDCHNAME").toString() + "#");
	            }
	            ISDISPLAY = retmap.get("ISDISPLAY").toString();
	            if (ISDISPLAY.equals("1")) {
	              resultStr.append("是#");
	            } else {
	              resultStr.append("否#");
	            }
	            ISEDIT = retmap.get("ISEDIT").toString();
	            if (ISEDIT.equals("1")) {
	              resultStr.append("是#");
	            } else {
	              resultStr.append("否#");
	            }
	            ISMUSTFILL = retmap.get("ISMUSTFILL").toString();
	            if (ISMUSTFILL.equals("1")) {
	              resultStr.append("是#");
	            } else {
	              resultStr.append("否#");
	            }
	            ISFORCE = retmap.get("ISFORCE").toString();
	            if (ISFORCE.equals("1")) {
	              resultStr.append("是#");
	            } else {
	              resultStr.append("否#");
	            }
	            DEFAULT1 = retmap.get("DEFAULT1").toString();
	            if (DEFAULT1 != null) {
	              resultStr.append(DEFAULT1 + "&");
	            } else {
	              resultStr.append("&");
	            }
	          }
	          mdbset = null;//赋空值
	        }
	      } catch (Exception ex) {
	    	  LOGGER.error("getCtrlAbleFieldByFID出现异常", ex);
	      }
	      long endTime = System.currentTimeMillis();
	      LOGGER.info("getCtrlAbleFieldByFID执行完成，耗时：" + (endTime - startTime) + " ms.");
	      return resultStr.toString();
	}

	@Override
	public FunctionMessage saveCtlFieldInfo(String FID, String fieldInfoStr) throws Exception {
        FunctionMessage fm = new FunctionMessage(1);
        StringBuffer addBuf = new StringBuffer();
        LOGGER.info("saveCtlFieldInfo开始调用...");
        COLL_CONFIG_OPERATE_FIELD ctlFieldObj;
        long startTime = System.currentTimeMillis();
        String strIsDisplay, strIsEdit, strIsMustFill, strDEFAULT,strIsForce;
        int i = 0;
        String tempArray1[] = fieldInfoStr.split("&");
        String tempArray2[];
        //删除可能与当前活动有关但已过失无用的记录
        StringBuffer sbTable=new StringBuffer("('TESTTABLEAAA'");
        try {
          for (i = 0; i < tempArray1.length; i++) {
            tempArray2 = tempArray1[i].split("#");
            if (tempArray2[0].trim().length() > 0) {
              sbTable.append(",'" + tempArray2[0].trim() + "'");
            }
          }
          sbTable.append(")");
          addBuf.delete(0,addBuf.length());//清空
          addBuf.append("DELETE FROM COLL_CONFIG_OPERATE_FIELD WHERE FID='"+FID)
              	.append("' AND FIELD NOT IN "+sbTable.toString());
          userMapper.deleteExecSQL(addBuf.toString());
        } catch(Exception ex) {
             LOGGER.info("saveCtlFieldInfo出现异常", ex);
        }
        for (i = 0; i < tempArray1.length; i++) {
          tempArray2 = tempArray1[i].split("#");
          if (tempArray2[0].length() == 0) {
            continue;
          }
          if (tempArray2[1].equals("是")) { //是否显示
            strIsDisplay = "1";
          } else {
            strIsDisplay = "0";
          }
          if (tempArray2[2].equals("是")) { //是否编辑
            strIsEdit = "1";
          } else {
            strIsEdit = "0";
          }
          if (tempArray2[3].equals("是")) { //是否必填
            strIsMustFill = "1";
          } else {
            strIsMustFill = "0";
          }
          if(tempArray2[4].equals("是")){ //是否强制设置默认值
            strIsForce = "1";
          } else {
            strIsForce = "0";
          }
          //若tempArray2.length<=4，则表示缺省值为空,则取tempArray2[4]的值时会出错
          if (tempArray2.length > 5) {
            strDEFAULT = convertQuotationSingleToDouble(tempArray2[5]);
          } else {
            strDEFAULT = " ";
          }
          try {
            addBuf.delete(0, addBuf.length());//清空
            addBuf.append("Select ID,FID,FIELD,ISDISPLAY,ISEDIT,ISMUSTFILL,DEFAULT1,ISFORCE From COLL_CONFIG_OPERATE_FIELD Where FID='"+FID)
                  .append("' And FIELD='"+tempArray2[0]+"'");
            List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
            if (retlist == null || retlist.size() < 1) {
              addBuf.delete(0, addBuf.length());//清空
              addBuf.append("Insert Into COLL_CONFIG_OPERATE_FIELD(ID,FID,FIELD,ISDISPLAY,ISEDIT,ISMUSTFILL,ISFORCE,DEFAULT1) Values('")
                    .append(getMaxID("COLL_CONFIG_OPERATE_FIELD", 8)+"','"+FID+"','")
                    .append(tempArray2[0]+"','"+strIsDisplay+"','"+strIsEdit)
                   	.append("','"+strIsMustFill+"','"+strIsForce+"','")
                 	.append(strDEFAULT+"')");
              userMapper.insertExecSQL(addBuf.toString());
            } else {
              ctlFieldObj = (COLL_CONFIG_OPERATE_FIELD) ReflectionUtil.
            		  convertMapToBean(retlist.get(0), COLL_CONFIG_OPERATE_FIELD.class);
              if (!(ctlFieldObj.getISDISPLAY().equals(strIsDisplay) &&
                     ctlFieldObj.getISEDIT().equals(strIsEdit) &&
                     ctlFieldObj.getISMUSTFILL().equals(strIsMustFill) &&
                     ctlFieldObj.getISFORCE().equals(strIsForce)&&
                     ctlFieldObj.getDEFAULT1().trim().equals(strDEFAULT.trim()))) {
                addBuf.delete(0, addBuf.length());//清空
                addBuf.append("Update COLL_CONFIG_OPERATE_FIELD Set ISDISPLAY='"+strIsDisplay)
                     .append("',ISEDIT='"+strIsEdit+"',ISMUSTFILL='"+strIsMustFill)
                     .append("',ISFORCE='"+strIsForce+"',DEFAULT1='"+strDEFAULT)
                     .append("' Where  FID='"+FID+"' And FIELD='"+tempArray2[0])
                     .append("' AND ID='"+ctlFieldObj.getID()+"'");
                
                userMapper.updateExecSQL(addBuf.toString());
              }
            }
          } catch (Exception ex) {
            LOGGER.info("ConfigServiceImpl.saveCtlFieldInfo出现异常", ex);
            fm.setResult(false);
            fm.setMessage("调用方法saveCtlFieldInfo出现异常" + ex.toString());
          }
        }
        collectService.inithashtable();

        long endTime = System.currentTimeMillis();
        LOGGER.info("saveCtlFieldInfo", "执行完成，耗时：" + (endTime - startTime) + " ms.");
        return fm;
	}

    /**
    * 将字符串中的单引号转换为双引号，若字符串中有单引号在保存时才不会出错
    * @param expression String    待转换的字符串
    * @return String              已经转换好的字符串
    */
    private String convertQuotationSingleToDouble(String expression) {
        return expression.replaceAll("\'", "''");
    }

	@Override
	public String getMaxID(String tableName, int idLength) throws Exception {
	     String maxNo = "";
	     int lenMaxNo = 0;
	     StringBuffer addBuf = new StringBuffer();
	     LOGGER.info("GetMaxID", "开始调用");
	     long startTime = System.currentTimeMillis();
	     addBuf.append("SELECT MAX(ID) AS MaxNo FROM "+tableName);
	     try {
	    	 String retMaxNo = userMapper.selectStrExecSQL(addBuf.toString());
	    	 if (retMaxNo != null) {
	    		 if (retMaxNo.trim().length() == 0) {
	    			 maxNo = "0000000000000000000000001";
	    		 } else {
	    			 maxNo = retMaxNo.trim();
	    			 maxNo = String.valueOf(Integer.parseInt(maxNo) + 1);
	    			 lenMaxNo = maxNo.length();
		             addBuf.delete(0,addBuf.length());//清空
		             addBuf.append("0000000000000000000000000"+maxNo);
		             maxNo = addBuf.toString();
	    		 }
	       }
	       maxNo = maxNo.substring(25 - idLength + lenMaxNo);
	     } catch (Exception ex) {
	    	 LOGGER.error("ConfigServiceImpl.getMaxID Exception:\n", ex);
	     }
	     long endTime = System.currentTimeMillis();
	     LOGGER.info("getMaxID", "执行完成，耗时：" + (endTime - startTime) + " ms.");
	     LOGGER.info("getMaxID", "结束调用");
	     
	     return maxNo;
	}

	@Override
	public String getSFieldList(String listName, String ID) throws Exception {
      StringBuffer strResult = new StringBuffer("");
      String strMAINTABLE = "";
      String strSQL = "Select MAINTABLE from COLL_DOC_CONFIG  where ID='"+ID+"'";
      String retMainTab = userMapper.selectStrExecSQL(strSQL);
      if (StringUtils.isNotBlank(retMainTab)){
          strMAINTABLE = retMainTab;
      }
      if (strMAINTABLE.length()>0) {
          strSQL = "select FIELDNAME,CHINESENAME from BPIP_FIELD where TABLEID='"+strMAINTABLE+"' order by FIELDID";
          List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
          int length = retlist != null ? retlist.size() : 0;
          if (length > 0){
        	  strResult.append("<select name='"+listName
        			  +"' class='SelectStyle2' size=20 multiple  style='width:190px;height:300px;' >\r\n");
              for (int i = 0; i < length; i++) {
            	  Map<String, Object> retmap = retlist.get(i);
            	  strResult.append("<option value='"+
                    		retmap.get("FIELDNAME").toString()+"'>"+
                    		retmap.get("CHINESENAME").toString()+
                    		"</option>\r\n");
              }
          }
      }
      strResult.append("</select>\r\n");
      
      return strResult.toString();
	}

	@Override
	public boolean saveTemplaet(String ID, String type, String fields) throws Exception {
      StringBuffer strRes = new StringBuffer();//自定义表单
      StringBuffer strPRes = new StringBuffer();//打印表单
      // 显示的字段中是否有blob类型的字段。
      boolean isblob = false;
      boolean isnotkey = true;
      String strBlobName = "";
      String strkey = "";
      String strSQL = "select DOCNAME,MAINTABLE from COLL_DOC_CONFIG where ID='"+ID+"'";
      String strDOCNAME = "", strMAINTABLE = "", strfields = "";
      Map<String, Object> retMap = userMapper.selectMapExecSQL(strSQL);
      CollectionUtil.convertMapNullToStr(retMap);
      if (retMap != null && retMap.size() > 0){
    	  strDOCNAME = retMap.get("DOCNAME").toString();
          strMAINTABLE = retMap.get("MAINTABLE").toString();
      }
      strfields = fields;
      if (strfields.length()>0) {
         strfields = strfields.replaceAll(",","','");
         strfields = "'"+strfields+"'";
      }    
      strSQL ="select FIELDNAME from BPIP_FIELD where TABLEID='"+strMAINTABLE+"' and FIELDNAME IN ("+strfields+") and FIELDTYPE='3'";
      List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
      if (retList != null && retList.size() > 0){
          isblob = true;
          if (retList.get(0) != null && retList.get(0).get("FIELDNAME") != null) {
        	  strBlobName = retList.get(0).get("FIELDNAME").toString();
          }
      }
      //得到主键
      strSQL ="select FIELDNAME from BPIP_FIELD where TABLEID='"+strMAINTABLE+"' and ISKEY='1'";
      retList = userMapper.selectListMapExecSQL(strSQL);
      if (retList != null && retList.size() > 0){
    	  if (retList.get(0) != null && retList.get(0).get("FIELDNAME") != null) {
    		  strkey = retList.get(0).get("FIELDNAME").toString();
    	  }
      }
      //主键是否包含在选择的字段中
      if (strfields.indexOf("'"+strkey+"'")!= -1) {
         isnotkey = false;
      }
      Map<String, Object> dHtable = new HashMap<String, Object>();
      strSQL ="select FIELDNAME,CHINESENAME from BPIP_FIELD where TABLEID='"+strMAINTABLE+"' and FIELDNAME IN ("+strfields+")";
      retList = userMapper.selectListMapExecSQL(strSQL);
      if (retList != null && retList.size() > 0){
         for (int i=0; i<retList.size(); i++) {
             dHtable.put(retList.get(i).get("FIELDNAME").toString(), retList.get(i).get("CHINESENAME").toString());
         }
      }
      //---------------------------------------
      if (type.equals("1")) {//第一种模板
          strRes.append("<html>\r\n");
          strRes.append("<head>\r\n");
          strRes.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n");
          strRes.append("<title>"+strDOCNAME+"</title>\r\n");
          strRes.append("<link href=\"/static/ZrCollEngine/Res/doc.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
          strRes.append("</head>\r\n");
          strRes.append("<body>\r\n");
          if (isblob) {
            strRes.append("<form name=\"form1\" method=\"post\" enctype=\"multipart/form-data\" action=\"\">\r\n");
          } else {
            strRes.append("<form name=\"form1\" method=\"post\" action=\"\">\r\n");
          }
          if (isnotkey) {
             strRes.append("<input type=\"hidden\" id=\""+strkey+"\" name=\""+strkey+"\" size=\"50\">\r\n");
          }
          strRes.append("<div id=\"cc\" class=\"easyui-layout\" style=\"width:100%;height:96%; top:5px;\">\r\n");
          strRes.append("<div data-options=\"region:'center',title:'"+strDOCNAME+"',iconCls:'icon-ok'\">\r\n");

          strRes.append("<table border=\"0\" cellpadding=\"3\" width=\"96%\">\r\n");
          strRes.append("<tr><td valign=\"top\" align=\"center\">\r\n");
          strRes.append("<table width=\"900\" border=\"0\"  bgcolor=\"#DFDCD7\" cellspacing=\"1\" cellpadding=\"5\"  id=\"table1\">\r\n");

          String strFields[] = fields.split(",");
          String strCFieldName = "";
          int jnum = 0;
          for (int i=0; i<strFields.length; i++) {
              strCFieldName = (String)dHtable.get(strFields[i]);
              if (i%2==0 && i==0) {
                  strRes.append("<TR>\r\n");
              }
              if (i%2==0 && i!=0) {
                  strRes.append("</TR><TR>\r\n");
              }
              if (strBlobName.equals(strFields[i])){
                 strRes.append("<TD  bgcolor=\"#F2F2F2\" width=\"220\">"+strCFieldName+"：</TD>\r\n");
                 strRes.append("<TD  bgcolor=\"#FFFFFF\" width=\"430\"><img name=\""+strFields[i]+"\" id=\""+strFields[i]+"\" title=\"照片显示\" width=\"94\" height=\"120\"><br><div id='show2'><input name=\"PHOTO\" type=\"file\" size=\"35\"></div></TD>\r\n");
              } else {
                 strRes.append("<TD  bgcolor=\"#F2F2F2\" width=\"220\">"+strCFieldName+"：</TD>\r\n");
                 strRes.append("<TD  bgcolor=\"#FFFFFF\" width=\"430\"><input type=\"text\" name=\""+strFields[i]+"\" size=\"25\"></TD>\r\n");
              }
              jnum = i;
          }
          if (jnum % 2==0) {
              strRes.append("<TD  bgcolor=\"#F2F2F2\" width=\"220\"></TD>\r\n");
              strRes.append("<TD  bgcolor=\"#FFFFFF\" width=\"430\"></TD>\r\n");
              strRes.append("</TR>\r\n");
          }
          strRes.append("</table>\r\n");
          strRes.append("<p></p></td></tr>\r\n");
          strRes.append("</table>\r\n");
          strRes.append("</div>\r\n");
          strRes.append("</div>\r\n");
          strRes.append("</form>\r\n");
          strRes.append("</body>\r\n");
          strRes.append("</html>\r\n");
      }

      //---------------以前的手机模板，保留2018-01-14---------------//
//	  if (type.equals("2")) {//手机模板
//	          strRes.append("<html>\r\n");
//	          strRes.append("<head>\r\n");
//	          strRes.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n");
//	          strRes.append("<title>"+strDOCNAME+"</title>\r\n");
//	          strRes.append("<link href=\"/static/ZrCollEngine/Res/doc.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
//	          strRes.append("</head>\r\n");
//	          strRes.append("<body>\r\n");
//	          if (isblob) {
//	            strRes.append("<form name=\"form1\" method=\"post\" enctype=\"multipart/form-data\" action=\"\">\r\n");
//	          } else {
//	            strRes.append("<form name=\"form1\" method=\"post\" action=\"\">\r\n");
//	          }
//	          if (isnotkey) {
//	             strRes.append("<input type=\"hidden\" name=\""+strkey+"\" size=\"50\">\r\n");
//	          }
//	          strRes.append("<div id=\"cc\" class=\"easyui-layout\" style=\"width:100%;height:96%; top:5px;\">\r\n");
//	          strRes.append("<div data-options=\"region:'center',title:'"+strDOCNAME+"',iconCls:'icon-ok'\">\r\n");
//
//	          strRes.append("<table border=\"0\" cellpadding=\"3\" width=\"96%\">\r\n");
//	          strRes.append("<tr><td valign=\"top\" align=\"center\">\r\n");
//
//	          strRes.append("<table width=\"100%\" border=\"0\"  bgcolor=\"#DFDCD7\" cellspacing=\"1\"  cellpadding=\"5\"  id=\"table1\">\r\n");
//
//	          String strFields[] = fields.split(",");
//	          String strCFieldName = "";
//	          for (int i=0;i<strFields.length;i++) {
//	              strCFieldName = (String)dHtable.get(strFields[i]);
//	              if (i==0) {
//	                  strRes.append("<TR>\r\n");
//	              }
//	              if (i!=0) {
//	                  strRes.append("</TR><TR>\r\n");
//	              }
//	              if (strBlobName.equals(strFields[i])) {
//	                 strRes.append("<TD  bgcolor=\"#F2F2F2\" width=\"40%\">"+strCFieldName+"：</TD>\r\n");
//	                 strRes.append("<TD  bgcolor=\"#FFFFFF\" width=\"60%\"><img name=\""+strFields[i]+"\" id=\""+strFields[i]+"\" title=\"照片显示\" width=\"94\" height=\"120\"><br><div id='show2'><input name=\"PHOTO\" type=\"file\" size=\"20\"></div></TD>\r\n");
//	              } else {
//	                 strRes.append("<TD  bgcolor=\"#F2F2F2\" width=\"40%\">"+strCFieldName+"：</TD>\r\n");
//	                 strRes.append("<TD  bgcolor=\"#FFFFFF\" width=\"60%\"><input type=\"text\" name=\""+strFields[i]+"\" size=\"25\"></TD>\r\n");
//	              }
//	          }
//	          strRes.append("</TR>\r\n");
//	          strRes.append("</table>\r\n");
//	          strRes.append("<p><p/></td></tr>\r\n");
//	          strRes.append("</table>\r\n");
//	          strRes.append("</div>\r\n");
//	          strRes.append("</div>\r\n");
//	          strRes.append("</form>\r\n");
//	          strRes.append("</body>\r\n");
//	          strRes.append("</html>\r\n");
//	      }

	  //最新手机模板 2018-01-14 ftl
	  if (type.equals("2")) {//手机模板
	          strRes.append("<html class=\"no-js\">\r\n");
	          strRes.append("<head>\r\n");
	          strRes.append("<meta charset=\"utf-8\">\r\n");
	          strRes.append("<title>"+strDOCNAME+"</title>\r\n");
	          strRes.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n");
	          strRes.append("<meta name=\"description\" content=\"\">\r\n");
	          strRes.append("<meta name=\"keywords\" content=\"\">\r\n");
	          strRes.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\">\r\n");
	          strRes.append("<meta name=\"apple-touch-fullscreen\" content=\"yes\">\r\n");
	          strRes.append("<meta name=\"x5-fullscreen\" content=\"true\">\r\n");
	          strRes.append("<!-- Set render engine for 360 browser -->\r\n");
	          strRes.append("<meta name=\"renderer\" content=\"webkit\">\r\n");
	          strRes.append("<!-- No Baidu Siteapp-->\r\n");
	          strRes.append("<meta http-equiv=\"Cache-Control\" content=\"no-siteapp\"/>\r\n");
	          strRes.append("<!-- Add to homescreen for Chrome on Android -->\r\n");
	          strRes.append("<meta name=\"mobile-web-app-capable\" content=\"yes\">\r\n");
	          strRes.append("<!-- Add to homescreen for Safari on iOS -->\r\n");
	          strRes.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\r\n");
	          strRes.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\r\n");
	          strRes.append("<meta name=\"apple-mobile-web-app-title\" content=\"Amaze UI\"/>\r\n");
	          strRes.append("<!-- Tile icon for Win8 (144x144 + tile color) -->\r\n");
	          strRes.append("<meta name=\"msapplication-TileImage\" content=\"assets/i/app-icon72x72@2x.png\">\r\n");
	          strRes.append("<meta name=\"msapplication-TileColor\" content=\"#0e90d2\">\r\n");
	          strRes.append("<link rel=\"icon\" type=\"image/png\" href=\"/static/assets/i/favicon.png\">\r\n");
	          strRes.append("<link rel=\"icon\" sizes=\"192x192\" href=\"/static/assets/i/app-icon72x72@2x.png\">\r\n");
	          strRes.append("<link rel=\"apple-touch-icon-precomposed\" href=\"/static/assets/i/app-icon72x72@2x.png\">\r\n");
	          strRes.append("<link rel=\"stylesheet\" href=\"/static/assets/font_icon/css/font-awesome.css\">\r\n");
	          strRes.append("<link rel=\"stylesheet\" href=\"/static/assets/css/amazeui.min.css\">\r\n");
	          strRes.append("<link rel=\"stylesheet\" href=\"/static/assets/css/app.css\">\r\n");
	          strRes.append("<link rel=\"stylesheet\" href=\"/static/assets/css/amazeui.datetimepicker.css\"/>\r\n");
	          strRes.append("<link rel=\"stylesheet\" href=\"/static/assets/css/from.css\">\r\n");
	          strRes.append("<link rel=\"stylesheet\" href=\"/static/assets/css/amazeui.datetimepicker.css\"/>\r\n");

	          strRes.append("<script type=\"text/javascript\" src=\"/static/assets/js/jquery.min.js\"></script>\r\n");
	          strRes.append("<script type=\"text/javascript\" src=\"/static/assets/js/amazeui.min_coll.js\"></script>\r\n");
	          strRes.append("<script type=\"text/javascript\" src=\"/static/assets/js/amazeui.datetimepicker.min.js\"></script>\r\n");
	          strRes.append("<script type=\"text/javascript\" src=\"/static/assets/js/locales/amazeui.datetimepicker.zh-CN.js\"></script>\r\n");
	          strRes.append("<script type=\"text/javascript\" src=\"/static/ZrPhoneEngine/js/QueryEngine.js\"></script>\r\n");
	          strRes.append("<script type=\"text/javascript\" src=\"/static/Zrsysmanage/script/Public.js\"></script>\r\n");
	          strRes.append("<style type='text/css'>\r\n");
	          strRes.append(".NoNewline\r\n");
	          strRes.append(" {\r\n");
	          strRes.append(" word-break: keep-all;/*必须*/\r\n");
	          strRes.append(" white-space: nowrap; //不换行  \r\n");
	          strRes.append(" text-overflow: ellipsis; //超出部分用....代替  \r\n");
	          strRes.append(" overflow: hidden; width:80px;//超出隐藏  \r\n");
	          strRes.append(" }\r\n");
	          strRes.append("label.am-checkbox, label.am-radio {\r\n");
	          strRes.append("    font-weight: 400;\r\n");
	          strRes.append("    color: rgb(66, 62, 62);\r\n");
	          strRes.append(" }\r\n");
	          strRes.append(".am-ucheck-icons {\r\n");
	          strRes.append("   color: rgb(12, 32, 204);\r\n");
	          strRes.append("}\r\n");
	          strRes.append("</style> \r\n");
	          strRes.append("</head>\r\n");
	          strRes.append("<body>\r\n");
	          strRes.append("<!-- main  start -->\r\n");
	          strRes.append("<div class=\"main-contor\">\r\n");
	          if (isblob) {
	            strRes.append("<form name=\"form1\" method=\"post\" enctype=\"multipart/form-data\" action=\"\" class=\"am-form\" data-am-validator>\r\n");
	          } else {
	            strRes.append("<form name=\"form1\" method=\"post\" action=\"\" class=\"am-form\" data-am-validator>\r\n");
	          }
	          if (isnotkey) {
	             strRes.append("<input type=\"hidden\" name=\""+strkey+"\" size=\"50\">\r\n");
	          }
	          strRes.append("<ul data-am-widget=\"gallery\" class=\"am-gallery am-gallery-default am-no-layout\" data-am-gallery=\"{ pureview: true }\" >\r\n");
	          strRes.append("<li>\r\n");
	          String strFields[] = fields.split(",");
	          String strCFieldName = "";
	          for (int i=0;i<strFields.length;i++){
	              strCFieldName = (String)dHtable.get(strFields[i]);
	              if (strBlobName.equals(strFields[i])){
	                 strRes.append("<div class=\"am-form-group am-form-file\" id='PHOTO_h'>\r\n");
	                 strRes.append("<label for=\"doc-vld-input1\">"+strCFieldName+"</label>\r\n");
	                 strRes.append("<img name=\""+strFields[i]+"\" id=\""+strFields[i]+"\" title=\"照片显示\" width=\"94\" height=\"120\">\r\n");
	                 strRes.append("</div>\r\n");
	                 strRes.append("<div class=\"am-form-group am-form-file\" id='PHOTO_f'>\r\n");
	                 strRes.append("<label for=\"doc-vld-input1\">操作</label>\r\n");
	                 strRes.append("<button type=\"button\" class=\"am-btn am-btn-danger am-btn-sm\">\r\n");
	                 strRes.append("<i class=\"am-icon-cloud-upload\"></i> 选择图片</button>\r\n");
	                 strRes.append("<input name=\"PHOTO\" type=\"file\" multiple placeholder=\""+strCFieldName+"\">\r\n");
	                 strRes.append("</div>\r\n");
	              } else {
	            	  strRes.append("<div class=\"am-form-group\" id=\""+strFields[i]+"_h\">\r\n");
	            	  strRes.append("<label for=\"doc-vld-input1\">"+strCFieldName+"</label>\r\n");
	                  strRes.append("<input type=\"text\" name=\""+strFields[i]+"\" id=\""+strFields[i]+"\" placeholder=\""+strCFieldName+"\">\r\n");
	                  strRes.append("</div>\r\n");
	              }
	              //strRes.append("<hr data-am-widget=\"divider\" class=\"am-divider am-divider-default\" />\r\n");
	          }
	          strRes.append("</li>\r\n");
	          strRes.append("</ul>\r\n");
	          strRes.append("</form>\r\n");
	          strRes.append("</div>\r\n");
	          strRes.append("<!-- main  end -->\r\n");
	          strRes.append("</body>\r\n");
	          strRes.append("</html>\r\n");
	      }
	      //--------------------打印表单--------------------//
	      strPRes.append("<html>\r\n");
	      strPRes.append("<head>\r\n");
	      strPRes.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n");
	      strPRes.append("<title>"+strDOCNAME+"</title>\r\n");
	      strPRes.append("</head>\r\n");
	      strPRes.append("<body topmargin=\"2\" leftmargin=\"12\" rightmargin=\"2\" bottommargin=\"2\">\r\n");
	      strPRes.append("<form name=\"form1\" method=\"post\" action=\"\">\r\n");
	      strPRes.append("<br><br>\r\n");
	      strPRes.append("<p align=\"center\"><b><font size=\"5\">"+strDOCNAME+"</font></b> </p>\r\n");
	      strPRes.append("<table width=\"760\" border=\"1\"  bgcolor=\"#C0C0C0\" cellspacing=\"0\" id=\"table1\" bordercolorlight=\"#000000\" cellpadding=\"0\">\r\n");
	      //----------------------------------------------//
	      String strFields[] = fields.split(",");
	      String strCFieldName = "";
	      int jnum = 0;
	      for (int i=0;i<strFields.length;i++) {
              strCFieldName = (String)dHtable.get(strFields[i]);
              if (i%2==0 && i==0) {
                  strPRes.append("<TR>\r\n");
              }
              if (i%2==0 && i!=0) {
                  strPRes.append("</TR><TR>\r\n");
              }
              if (strBlobName.equals(strFields[i])) {
                 strPRes.append("<TD bgcolor=\"#FFFFFF\" width=\"110\">"+strCFieldName+"：</TD>\r\n");
                 strPRes.append("<TD bgcolor=\"#FFFFFF\" width=\"270\"><img name=\""+strFields[i]+"\" id=\""+strFields[i]+"\" title=\"照片显示\" width=\"94\" height=\"120\"></TD>\r\n");
              } else {
                 strPRes.append("<TD bgcolor=\"#FFFFFF\" width=\"110\">"+strCFieldName+"：</TD>\r\n");
                 strPRes.append("<TD bgcolor=\"#FFFFFF\" width=\"270\"><input type=\"text\" name=\""+strFields[i]+"\" size=\"16\"></TD>\r\n");
              }
              jnum = i;
	      }
	      if (jnum % 2==0) {
              strPRes.append("<TD bgcolor=\"#FFFFFF\" width=\"110\"></TD>\r\n");
              strPRes.append("<TD bgcolor=\"#FFFFFF\" width=\"270\"></TD>\r\n");
              strPRes.append("</TR>\r\n");
	      }
	      strPRes.append("</table>\r\n");
	      strPRes.append("</body>\r\n");
	      strPRes.append("</html>\r\n");
	      //----------------------------打印表单----------------------------//

	      //printTempletPath
	      //--------------生成模板文件-------------//
	      boolean bFile1;
	      File tempFile1 = new File(SysPreperty.getProperty().ShowTempletPath+"/"+ID+".htm");
	      tempFile1.delete();
          try {
            bFile1 = tempFile1.createNewFile();
            if (bFile1) {
                FileWriter fw1 = new FileWriter(SysPreperty.getProperty().ShowTempletPath+"/"+ID+".htm");
                fw1.write(strRes.toString(), 0, strRes.toString().length());
                fw1.flush();
                fw1.close();
            }
          } catch (IOException ex) {
        	  ex.printStackTrace();
          }
	      //--------------生成打印模板文件-------------//
	      boolean bFile2;
	      File tempFile2 = new File(SysPreperty.getProperty().PrintTempletPath+"/"+ID+"_01.htm");
	      tempFile2.delete();
          try {
            bFile2 = tempFile2.createNewFile();
            if (bFile2) {
                FileWriter fw2 = new FileWriter(SysPreperty.getProperty().PrintTempletPath+"/"+ID+"_01.htm");
                fw2.write(strPRes.toString(), 0, strPRes.toString().length());
                fw2.flush();
                fw2.close();
            }
          } catch (IOException ex) {
        	  ex.printStackTrace();
          }
	      //----------------------------------
	      strSQL ="update COLL_DOC_CONFIG set DOCTYPE='0',TEMPLAET='"+ID+".htm' where ID='"+ID+"'";
	      userMapper.updateExecSQL(strSQL);
	      String strMid = getMaxID("COLL_DOC_PRINT",8);
	      strSQL ="Insert Into COLL_DOC_PRINT (ID,DOCID,TEMPLAET,PAGE) Values('"+strMid+"','"+ID+"','"+ID+"_01.htm',1)";
	      userMapper.insertExecSQL(strSQL);

	      return true;	  
	}

	@Override
	public List<Map<String, Object>> getTableList1() throws Exception {
	  List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	  String strSQL = "Select TABLENAME,CHINESENAME from BPIP_TABLE where TABLETYPE ='2' or TABLETYPE ='3' order by TABLEID";
	  
	  List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(strSQL);
	  int length = retList != null ? retList.size() : 0;
	  if (length > 0) {
	    for (int i = 0; i < length; i++) {
	    	Map<String, Object> retmap = retList.get(i);
	    	String[] saTmp = new String[2];
	    	saTmp[0] = retmap.get("TABLENAME").toString();
	    	saTmp[1] = retmap.get("CHINESENAME").toString();
	    	result.add(new HashMap<String, Object>(){
    		   /**
				* 
				*/
			   private static final long serialVersionUID = 1L;
			   {
				   put("id", saTmp[0]);
				   put("text", saTmp[1]);
    		   }
    	   });
	    }
	  }
	  return result;
	}

	@Override
	public List<Object> doGetFieldList(String tableName) throws Exception {
		List<Object> result = new ArrayList<Object>();
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("select FieldID,FieldName,CHINESENAME from bpip_field Where TABLEID=(Select TableID From Bpip_Table Where TableName='")
	       	  .append(tableName.toUpperCase()+"') Order by FieldID");
	    
	    List<Map<String, Object>> retList = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retList != null ? retList.size() : 0;
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = retList.get(i);
	        	String[] saTmp = new String[2];
	        	saTmp[0] = retmap.get("FieldName").toString();
	        	saTmp[1] = retmap.get("CHINESENAME").toString();
	        	result.add(saTmp);
	        }
	    }
	    return result;
	}

	@Override
	public String getTableIDByTableName(String tableName) throws Exception {
		String execSQL = "select TABLEID from bpip_table where TABLENAME = '"+tableName+"'";
		String tableID = userMapper.selectStrExecSQL(execSQL);
		return tableID;
	}
}