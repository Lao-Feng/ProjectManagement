package zr.zrpower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBSet;
import zr.zrpower.common.util.FunctionMessage;
import zr.zrpower.common.util.ReflectionUtil;
import zr.zrpower.common.util.StringUtils;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.UserMapper;
import zr.zrpower.flowengine.mode.base.Button;
import zr.zrpower.flowengine.mode.base.Group;
import zr.zrpower.flowengine.mode.config.COLL_CONFIG_OPERATE_FIELD;
import zr.zrpower.flowengine.mode.config.FLOW_CONFIG_ACTIVITY;
import zr.zrpower.flowengine.mode.config.FLOW_CONFIG_TIME;
import zr.zrpower.service.FlowManageActService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import zr.zrpower.common.db.DBServer;

/**
 * 管理活动的各种信息，包括以下各种活动属性: 
 * 基本属性
 * 策略属性
 * 超时属性
 * 操作属性
 * 可控字段属性
 * 签章属性
 * 参与者属性
 * 特殊属性
 * @author lwk
 *
 */
@Service
public class FlowManageActServiceImpl implements FlowManageActService {
	/** The FlowManageActServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowManageActServiceImpl.class);
	private DBEngine dbEngine;
	static private int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型

	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;

	public FlowManageActServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
		}
		clients = 1;
	}

	@Override
	public FunctionMessage saveBaseProperty(FLOW_CONFIG_ACTIVITY entityObj) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    FunctionMessage funMsg = new FunctionMessage(1);
	    LOGGER.info("saveBaseProperty开始调用...");
	    long startTime = System.currentTimeMillis();
	    
	    addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY Where ID='"+entityObj.getID()+"'");
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    addBuf.delete(0, addBuf.length());//清空
	    int optflag = 0;// 数据库操作标志位
	    if (retmap != null && retmap.size() > 0) {
	    	optflag = 0;// 修改
	    	addBuf.append("Update FLOW_CONFIG_ACTIVITY Set IDENTIFICATION='")
		          .append(entityObj.getIDENTIFICATION()+"'")
		          .append(",NAME='"+entityObj.getNAME()+"'")
		          .append(",FID='"+entityObj.getFID()+"'")
		          .append(",DESC1='"+entityObj.getDESC1()+"'")
		          .append(",TYPE='"+entityObj.getTYPE()+"'")
		          .append(",ORDER1="+entityObj.getORDER1()+",ISSIGN='")
		          .append(entityObj.getISSIGN()+"'")
		          .append(",OPENHELP='"+entityObj.getOPENHELP()+"'")
		          .append(",ISNOTE='"+entityObj.getISNOTE()+"'")
		          .append(",FORMPATH='"+entityObj.getFORMPATH()+"'")
		          .append(",ISMESSAGE='"+entityObj.getISMESSAGE()+"'")
		          .append(",MESSAGE='"+entityObj.getMESSAGE()+"'")
		          .append(",ADDFORMPATH='"+entityObj.getADDFORMPATH()+"'")
		          .append(",ADDFORMWIDTH='"+entityObj.getADDFORMWIDTH()+"'")
		          .append(",ADDFORMHEIGHT='"+entityObj.getADDFORMHEIGHT()+"'")
		          .append(",ADDFORMMESSAGE='"+entityObj.getADDFORMMESSAGE()+"'")
		          .append(",ISBRANCH='"+entityObj.getISBRANCH()+"'")
		          .append("  Where ID='"+entityObj.getID()+"'");
	    } else {
	    	optflag = 1;// 新增
	    	addBuf.append("Insert Into FLOW_CONFIG_ACTIVITY(ID,FID,IDENTIFICATION,NAME,DESC1,TYPE,"
	    			+ "ORDER1,ISSIGN,OPENHELP,ISNOTE,FORMPATH,ISMESSAGE,MESSAGE,ADDFORMPATH,"
	    			+ "ADDFORMWIDTH,ADDFORMHEIGHT,ADDFORMMESSAGE,ISBRANCH) values('")
	              .append(entityObj.getID()+"','"+entityObj.getFID()+"','"+entityObj.getIDENTIFICATION())
	              .append("','"+entityObj.getNAME()+"','"+entityObj.getDESC1()+"','")
	              .append(entityObj.getTYPE()+"',"+entityObj.getORDER1()+",'"+entityObj.getISSIGN())
	              .append("','"+entityObj.getOPENHELP()+"','"+entityObj.getISNOTE()+"','")
	              .append(entityObj.getFORMPATH()+"','"+entityObj.getISMESSAGE()+"','")
	              .append(entityObj.getMESSAGE()+"','"+entityObj.getADDFORMPATH()+"','")
	              .append(entityObj.getADDFORMWIDTH()+"','"+entityObj.getADDFORMHEIGHT())
	              .append("','"+entityObj.getADDFORMMESSAGE()+"','"+entityObj.getISBRANCH()+"')");
	    }
	    try {
	    	Integer retInt = 0;
	    	if (optflag == 1) {// 新增
	    		retInt = userMapper.insertExecSQL(addBuf.toString());
	    	} else {// optflag == 0，修改
	    		retInt = userMapper.updateExecSQL(addBuf.toString());
	    	}
	    	if (retInt != null && retInt > 0) {
	    		funMsg.setMessage("保存成功");
	    		funMsg.setResult(true);
	    	}
	    }
	    catch (Exception e) {
	    	LOGGER.error("FlowManageActServiceImpl.saveBaseProperty Exception:\n", e);
	    	funMsg.setResult(false);
	    	funMsg.setMessage("调用方法saveBaseProperty出现异常" + e.toString());
	    	return funMsg;
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("saveBaseProperty执行完成，耗时：" + (endTime - startTime) + " ms.");
	    return funMsg;
	}

	@Override
	public FunctionMessage saveStrategyProperty(FLOW_CONFIG_ACTIVITY entityObj) throws Exception {
	    //DBServer dbServer = new DBServer();
	    //funMsg = dbServer.saveStrategyProperty(entityObj);
	    FunctionMessage funMsg = saveStrategyProperty1(entityObj);
	    return funMsg;  
	}

	@Override
	public FunctionMessage saveAttProperty(FLOW_CONFIG_ACTIVITY entityObj) throws Exception {
	    FunctionMessage fm = new FunctionMessage(1);
	    LOGGER.info("saveAttProperty开始调用...");
	    long startTime = System.currentTimeMillis();
	    StringBuffer  addBuf = new   StringBuffer();
	    addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY Where ID='"+entityObj.getID()+"'");
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    addBuf.delete(0, addBuf.length());//清空
	    int optflag = 0;// 数据库操作标志位
	    if (retmap != null && retmap.size() > 0) {
	    	optflag = 0;// 修改
	        addBuf.append("Update FLOW_CONFIG_ACTIVITY Set ATTTYPE='"+entityObj.getATTTYPE())
	              .append("',ATTNUM="+entityObj.getATTNUM()+" Where ID='")
	              .append(entityObj.getID()+"'");
	    } else {// retmap == null
	    	optflag = 1;// 新增
	        addBuf.append("Insert Into FLOW_CONFIG_ACTIVITY(ID,ATTTYPE,ATTNUM) values('")
	              .append(entityObj.getID()+"','"+entityObj.getATTTYPE()+"',")
	              .append(entityObj.getATTNUM());
	    }
	    try {
	    	Integer retInt = 0;
	    	if (optflag == 1) {// 新增
	    		retInt = userMapper.insertExecSQL(addBuf.toString());
	    	} else {// optflag == 0，修改
	    		retInt = userMapper.updateExecSQL(addBuf.toString());
	    	}
	    	if (retInt != null && retInt > 0) {
	    		fm.setMessage("保存成功");
	    		fm.setResult(true);
	    	}
	    } catch (Exception e) {
	    	LOGGER.error("FlowManageActServiceImpl.saveAttProperty Exception:\n", e);
	    	fm.setResult(false);
	    	fm.setMessage("调用方法SaveAttProperty出现异常" + e.toString());
	    	return fm;
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("saveAttProperty执行完成，耗时：" + (endTime - startTime) + " ms.");
	    return fm;  
	}

	@Override
	public FLOW_CONFIG_ACTIVITY getActivityById(String ID) throws Exception {
	    FLOW_CONFIG_ACTIVITY entityObj = null;
	    LOGGER.info("getActivityById开始调用...");
	    long startTime = System.currentTimeMillis();
	    StringBuffer  addBuf = new   StringBuffer();
	    try {
	    	addBuf.append("Select * From FLOW_CONFIG_ACTIVITY Where ID='"+ID.trim()+"'");
	    	Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    	if (retmap != null && retmap.size() > 0) {
	    		entityObj = (FLOW_CONFIG_ACTIVITY) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_ACTIVITY.class);
	    	}
	    } catch (Exception e) {
	    	LOGGER.error("调用方法getActivityById出现异常" + e.toString());
	    	return null;
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("getActivityById执行完成，耗时：" + (endTime - startTime) + " ms.");
	    return entityObj;  
	}

	@Override
	public String getActivityMaxID() throws Exception {
	    DBSet dbset = null;
	    String MaxNo = "";
	    int LenMaxNo = 0;
	    StringBuffer  addBuf = new   StringBuffer();
	    LOGGER.info("getActivityMaxID开始调用...");
	    long startTime = System.currentTimeMillis();
	    addBuf.append("SELECT MAX(ID) AS MaxNo FROM FLOW_CONFIG_ACTIVITY");
	    try {
	    	dbset = dbEngine.QuerySQL(addBuf.toString());
	      if (dbset != null) {
	        if (dbset.Row(0).Column("MaxNo").getString().trim().length() == 0) {
	          MaxNo = "0000000000000000000000001";
	        }
	        else {
	          MaxNo = dbset.Row(0).Column("MaxNo").getString().trim();
	          MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
	          LenMaxNo = MaxNo.length();
	          addBuf.delete(0,addBuf.length());//清空
	          addBuf.append("0000000000000000000000000"+MaxNo);
	          MaxNo = addBuf.toString();
	        }
	        dbset=null;//赋空值
	      }
	      MaxNo = MaxNo.substring(15 + LenMaxNo);
	    } catch (Exception ex) {
	    	LOGGER.error("FlowManageActServiceImpl.getActivityMaxID Exception:\n", ex);
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("getActivityMaxID执行完成，耗时：" + (endTime - startTime) + " ms.");
	    LOGGER.info("getActivityMaxID结束调用.");
	    return MaxNo;
	}

	@Override
	public FLOW_CONFIG_TIME[] getOverTimeList(String strWhere) throws Exception {
	    FLOW_CONFIG_TIME entitys[] = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("select * from FLOW_CONFIG_TIME ");
	    LOGGER.info("getOverTimeList开始调用...");
	    long startTime = System.currentTimeMillis();
	    if (!strWhere.equals("")) {
	      addBuf.append(" Where "+strWhere);
	    }
	    addBuf.append(" Order By ID");
	    try {
	      List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	      int length = retlist != null ? retlist.size() : 0;
	      if (length == 0) {
	        entitys = null;
	      } else {// length > 0
	        entitys = new FLOW_CONFIG_TIME[length];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          entitys[i] = (FLOW_CONFIG_TIME) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_TIME.class);
	        }
	      }
	    } catch (Exception e) {
	      LOGGER.error("FlowManageActServiceImpl.getOverTimeList Exception:\n", e);
	      entitys = null;
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("getOverTimeList执行完成，耗时：" + (endTime - startTime) + " ms.");
	    return entitys;  
	}

	@Override
	public FunctionMessage saveOverTimeFieldInfo(String FID, String FieldInfoStr) throws Exception {
	    FunctionMessage fm = new FunctionMessage(1);
	    StringBuffer addBuf = new StringBuffer();
	    LOGGER.info("saveOverTimeFieldInfo开始调用...");
	    long startTime = System.currentTimeMillis();
	    String ABNORMITYID = "", NAME = "";
	    String DAY = "0", FREQUENCY = "0";
	    //异常处理ID#持续时间(天)#限制名称#执行频率(天)&
	    int i = 0;
	    String tempArray1[] = FieldInfoStr.split("@");
	    String tempArray2[];
	    addBuf.append("Delete from FLOW_CONFIG_TIME Where FID='"+FID+"'");
	    userMapper.deleteExecSQL(addBuf.toString());
	    for (i = 0; i < tempArray1.length; i++) {
	      ABNORMITYID = "";
	      NAME = "";
	      DAY = "0";
	      FREQUENCY = "0";
	      tempArray2 = tempArray1[i].split("#");
	      if (tempArray2[0].length() == 0) {
	        continue;
	      }
	      ABNORMITYID = tempArray2[0];
	      if (tempArray2[1].length() > 0) {
	        DAY = tempArray2[1];
	        
		    NAME = tempArray2[2];
		    ABNORMITYID = tempArray2[3];
		    if (ABNORMITYID.equals("消息提示")){ABNORMITYID="01";}
		    if (ABNORMITYID.equals("短信提示")){ABNORMITYID="02";}
		    
		    if (!tempArray2[4].equals("NULL")) {
		        FREQUENCY = tempArray2[4];
		    }
		    addBuf.delete(0,addBuf.length());//清空
		    addBuf.append("Insert Into FLOW_CONFIG_TIME(ID,FID,DAY,NAME,ABNORMITYID,FREQUENCY) Values('")
		          .append(getMaxID("FLOW_CONFIG_TIME", 8)+"','"+FID+"',"+DAY)
		          .append(",'"+NAME+"','"+ABNORMITYID+"',"+FREQUENCY+")");
		    try {
		    	  userMapper.insertExecSQL(addBuf.toString());
		    } catch (Exception ex) {
		    	  LOGGER.error("saveOverTimeFieldInfo出现异常", ex);
		    	  fm.setResult(false);
		    	  fm.setMessage("调用方法SaveOverTimeFieldInfo出现异常" + ex.toString());
		    }
	      }
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("saveOverTimeFieldInfo执行完成，耗时：" + (endTime - startTime) + " ms.");
	    return fm;
	}

	@Override
	public Button[] getButtonList() throws Exception {
	    Button entitys[] = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("select * from FLOW_BASE_BUTTON WHERE TYPE<>'4' order by CODE,ID");
	    LOGGER.info("getButtonList开始调用...");
	    long startTime = System.currentTimeMillis();
	    try {
	      List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	      int length = retlist != null ? retlist.size() : 0;
	      if (length == 0) {
	        entitys = null;
	      } else {// length > 0
	        entitys = new Button[length];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          entitys[i] = (Button) ReflectionUtil.convertMapToBean(retmap, Button.class);
	        }
	      }
	    } catch (Exception e) {
	    	LOGGER.error("FlowManageActServiceImpl.getButtonList Exception:\n" ,e);
	    	entitys = null;
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("getButtonList执行完成，耗时：" + (endTime - startTime) + " ms.");
	    return entitys;  
	}

	@Override
	public Button[] getButtonListByFid(String FID) throws Exception {
	    Button entitys[] = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("select b.ID,b.BNAME,b.type,b.property,b.ico,a.id as AID from FLOW_CONFIG_ACTIVITY_BUTTON a,FLOW_BASE_BUTTON b "
	    			+ "where a.buttonid=b.id and a.fid='" + FID + "' order by AID ");
	    LOGGER.info("getButtonListByFid开始调用...");
	    long startTime = System.currentTimeMillis();
	    try {
	      List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	      int length = retlist != null ? retlist.size() : 0;
	      if (length == 0) {
	        entitys = null;
	      } else {// length > 0
	        entitys = new Button[length];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          entitys[i] = (Button) ReflectionUtil.convertMapToBean(retmap, Button.class);
	        }
	      }
	    }
	    catch (Exception e) {
	    	LOGGER.error("FlowManageActServiceImpl.getButtonListByFid Exception:\n" ,e);
	    	entitys = null;
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("getButtonListByFid执行完成，耗时：" + (endTime - startTime) + " ms.");
	    return entitys;  
	}

	@Override
	public FunctionMessage addButtonToActivity(String FID, String BUTTONID) throws Exception {
	    FunctionMessage fm = new FunctionMessage(1);
	    LOGGER.info("addButtonToActivity开始调用...");
	    long startTime = System.currentTimeMillis();
	    StringBuffer  addBuf = new   StringBuffer();
	    addBuf.append("Insert Into FLOW_CONFIG_ACTIVITY_BUTTON(ID,FID,BUTTONID) values('")
	          .append(getMaxID("FLOW_CONFIG_ACTIVITY_BUTTON", 12)+"','"+FID+"','"+BUTTONID+"')");
	    try {
	      Integer retInt = userMapper.insertExecSQL(addBuf.toString());
	      if (retInt != null && retInt > 0) {
	        fm.setMessage("保存成功");
	        fm.setResult(true);
	      }
	    } catch (Exception e) {
	      LOGGER.error("FlowManageActServiceImpl.addButtonToActivity Exception:\n" ,e);
	      fm.setResult(false);
	      fm.setMessage("调用方法AddButtonToActivity出现异常" + e.toString());
	      return fm;
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("addButtonToActivity执行完成，耗时：" + (endTime - startTime) + " ms.");
	    return fm;
	}

	@Override
	public FunctionMessage deleteButtonFromActivity(String FID) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    FunctionMessage fm = new FunctionMessage(1);
	    LOGGER.info("deleteButtonFromActivity开始调用...");
	    long startTime = System.currentTimeMillis();
	    addBuf.append("Delete FLOW_CONFIG_ACTIVITY_BUTTON Where FID='" + FID + "'");
	    try {
	      boolean isOK = dbEngine.ExecuteSQL(addBuf.toString());
	      if (isOK) {
	        fm.setMessage("删除成功");
	        fm.setResult(true);
	      }
	    } catch (Exception e) {
		  LOGGER.error("FlowManageActServiceImpl.deleteButtonFromActivity Exception:\n" ,e);
	      fm.setResult(false);
	      fm.setMessage("调用方法deleteButtonFromActivity出现异常" + e.toString());
	      return fm;
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("deleteButtonFromActivity执行完成，耗时：" + (endTime - startTime) + " ms.");
	    return fm;
	}

	@Override
	public String getCtrlAbleFieldByFID(String FID, String AID) throws Exception {
		LOGGER.info("getCtrlAbleFieldByFID开始调用...");
	    long startTime = System.currentTimeMillis();
	    StringBuffer resultStr = new StringBuffer("");
	    String ISDISPLAY = "", ISEDIT = "", ISMUSTFILL = "", ISFORCE="", DEFAULT1 = "";
	    StringBuffer addBuf = new StringBuffer();
	    StringBuffer addBuf1 = new StringBuffer();
	    if (DataBaseType.equals("3")) {//mysql数据库
	        addBuf.append("Select A.*, IFNULL(B.ISDISPLAY,'1')  ISDISPLAY,"+" IFNULL(B.ISEDIT,'0') ISEDIT,")
		          .append("IFNULL(B.ISMUSTFILL,'0') ISMUSTFILL,"+"IFNULL(B.ISFORCE,'0') ISFORCE,")
		          .append("B.DEFAULT1 from  "+" (Select a.TABLEID, a.TABLENAME,a.CHINESENAME,b.FIELDNAME,")
		          .append("b.FIELDID from BPIP_TABLE a, BPIP_FIELD b  where b.TABLEID=a.TABLEID ")
		          .append(" and a.TABLEID in (")
		          .append(" Select MAINTABLE from COLL_DOC_CONFIG where ID=(Select DOCID From FLOW_CONFIG_PROCESS ")
		          .append(" Where ID='"+FID+"')) order by TABLEID,FIELDID) A   Left outer join ")
		          .append(" (SELECT * FROM  COLL_CONFIG_OPERATE_FIELD Where FID='"+AID)
		          .append("')   B "+" on concat(A.TABLENAME,'.',A.FIELDNAME)=B.FIELD Order by TABLEID,FIELDID");
	        
	        addBuf1.append("Select A.*, IFNULL(B.ISDISPLAY,'1')  ISDISPLAY,"+" IFNULL(B.ISEDIT,'0') ISEDIT,")
		           .append("IFNULL(B.ISMUSTFILL,'0') ISMUSTFILL,"+"IFNULL(B.ISFORCE,'0') ISFORCE,")
		           .append("B.DEFAULT1 from  "+" (Select  a.TABLEID,a.TABLENAME,b.FIELDNAME,")
		           .append(" b.CHINESENAME,b.FIELDID from BPIP_TABLE a, BPIP_FIELD b  where b.TABLEID=a.TABLEID ")
		           .append(" and a.TABLEID in (")
		           .append(" Select MAINTABLE from COLL_DOC_CONFIG where ID=(Select DOCID From FLOW_CONFIG_PROCESS ")
		           .append(" Where ID='"+FID+"')) order by TABLEID,FIELDID) A   Left outer join ")
		           .append(" (SELECT * FROM  COLL_CONFIG_OPERATE_FIELD Where FID='"+AID)
		           .append("')   B "+" on concat(A.TABLENAME,'.',A.FIELDNAME)=B.FIELD Order by TABLEID,FIELDID");
	    } else {
	    	addBuf.append("Select A.*, nvl(B.ISDISPLAY,'1')  ISDISPLAY,"+" nvl(B.ISEDIT,'0') ISEDIT,")
		          .append("nvl(B.ISMUSTFILL,'0') ISMUSTFILL,"+"nvl(B.ISFORCE,'0') ISFORCE,")
		          .append("B.DEFAULT1 from  "+" (Select a.TABLEID, a.TABLENAME,a.CHINESENAME as TABLECHNAME,b.FIELDNAME,")
		          .append(" b.CHINESENAME as FIELDCHNAME,b.FIELDID from BPIP_TABLE a, BPIP_FIELD b  where b.TABLEID=a.TABLEID ")
		          .append(" and a.TABLEID in (")
		          .append(" Select MAINTABLE from COLL_DOC_CONFIG where ID=(Select DOCID From FLOW_CONFIG_PROCESS ")
		          .append(" Where ID='"+FID+"'))) A   Left outer join ")
		          .append(" (SELECT * FROM  COLL_CONFIG_OPERATE_FIELD Where FID='"+AID)
		          .append("')   B "+" on A.TABLENAME||'.'||A.FIELDNAME=B.FIELD Order by TABLEID,FIELDID");
	    }
	    try {
	      DBSet mdbset = dbEngine.QuerySQL(addBuf.toString());
	      DBSet mdbset1 = null;
	      if (DataBaseType.equals("3")) {//mysql数据库
	          mdbset1 = dbEngine.QuerySQL(addBuf1.toString());
	      }
	      if (mdbset != null) {
	        for (int i = 0; i < mdbset.RowCount(); i++) {
	          if (DataBaseType.equals("3")) {//mysql数据库
	              resultStr.append(mdbset.Row(i).Column("TABLENAME").getString() + "#" +
                                   mdbset.Row(i).Column("CHINESENAME").getString() +
                                   "#" +
                                   mdbset.Row(i).Column("FIELDNAME").getString() + "#" +
                                   mdbset1.Row(i).Column("CHINESENAME").getString() +
	                               "#");
	          } else {
	              resultStr.append(mdbset.Row(i).Column("TABLENAME").getString() + "#" +
	                               mdbset.Row(i).Column("TABLECHNAME").getString() +
	                               "#" +
	                               mdbset.Row(i).Column("FIELDNAME").getString() + "#" +
	                               mdbset.Row(i).Column("FIELDCHNAME").getString() +
	                           	   "#");
	          }
	          ISDISPLAY = mdbset.Row(i).Column("ISDISPLAY").getString();
	          if (ISDISPLAY.equals("1")) {
	            resultStr.append("是#");
	          } else {
	            resultStr.append("否#");
	          }
	          ISEDIT = mdbset.Row(i).Column("ISEDIT").getString();
	          if (ISEDIT.equals("1")) {
	            resultStr.append("是#");
	          } else {
	            resultStr.append("否#");
	          }
	          ISMUSTFILL = mdbset.Row(i).Column("ISMUSTFILL").getString();
	          if (ISMUSTFILL.equals("1")) {
	            resultStr.append("是#");
	          } else {
	            resultStr.append("否#");
	          }
	          ISFORCE = mdbset.Row(i).Column("ISFORCE").getString();
	          if (ISFORCE.equals("1")) {
	            resultStr.append("是#");
	          } else {
	            resultStr.append("否#");
	          }
	          DEFAULT1 = mdbset.Row(i).Column("DEFAULT1").getString();
	          if (DEFAULT1 != null) {
	            resultStr.append(DEFAULT1 + "&");
	          } else {
	            resultStr.append("&");
	          }
	        }
	        mdbset = null;//赋空值
	      }
	    } catch (Exception e) {
	      LOGGER.error("getCtrlAbleFieldByFID出现异常", e.getMessage());
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("getCtrlAbleFieldByFID执行完成，耗时：" + (endTime - startTime) + " ms.");
	    
	    return resultStr.toString();
	}

	@Override
	public FunctionMessage saveCtlFieldInfo(String FID, String FieldInfoStr) throws Exception {
	    FunctionMessage fm = new FunctionMessage(1);
	    StringBuffer addBuf = new StringBuffer();
	    LOGGER.info("saveCtlFieldInfo开始调用...");
	    COLL_CONFIG_OPERATE_FIELD ctlFieldObj;
	    long startTime = System.currentTimeMillis();
	    String strIsDisplay, strIsEdit, strIsMustFill, strDEFAULT,strIsForce;
	    int i = 0;
	    String tempArray1[] = FieldInfoStr.split("@");
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
	      addBuf.delete(0, addBuf.length());//清空
	      addBuf.append("DELETE FROM COLL_CONFIG_OPERATE_FIELD WHERE FID='"+FID)
	          	.append("' AND FIELD NOT IN "+sbTable.toString());
	      
	      userMapper.deleteExecSQL(addBuf.toString());
	    } catch(Exception ex) {
	    	LOGGER.error("saveCtlFieldInfo出现异常", ex.getMessage());
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
	        addBuf.append("Select * from COLL_CONFIG_OPERATE_FIELD Where FID='"+FID
	        			 +"' And FIELD='"+tempArray2[0]+"'");
	        List<Map<String, Object>> mdbset = userMapper.selectListMapExecSQL(addBuf.toString());
	        if (mdbset == null || mdbset.size() < 1) {
	          addBuf.delete(0, addBuf.length());//清空
	          addBuf.append("Insert Into COLL_CONFIG_OPERATE_FIELD(ID,FID,FIELD,ISDISPLAY,ISEDIT,ISMUSTFILL,ISFORCE,DEFAULT1) Values('")
	                .append(getMaxID("COLL_CONFIG_OPERATE_FIELD", 8)+"','"+FID+"','")
	                .append(tempArray2[0]+"','"+strIsDisplay+"','"+strIsEdit)
	                .append("','"+strIsMustFill+"','"+strIsForce+"','")
	                .append(strDEFAULT+"')");
	          
	          userMapper.insertExecSQL(addBuf.toString());
	        } else {// mdbset != null && mdbset.size() > 0
	          ctlFieldObj = (COLL_CONFIG_OPERATE_FIELD) ReflectionUtil.convertMapToBean(mdbset.get(0), COLL_CONFIG_OPERATE_FIELD.class);
	          //ctlFieldObj.fullData(mdbset.Row(0));
	          if (!(ctlFieldObj.getISDISPLAY().equals(strIsDisplay) &&
	                 ctlFieldObj.getISEDIT().equals(strIsEdit) &&
	                 ctlFieldObj.getISMUSTFILL().equals(strIsMustFill) &&
	                 ctlFieldObj.getISFORCE().equals(strIsForce) &&
	                 ctlFieldObj.getDEFAULT1().trim().equals(strDEFAULT.trim()))) {
	        	
	            addBuf.delete(0, addBuf.length());//清空
	            addBuf.append("Update COLL_CONFIG_OPERATE_FIELD Set ISDISPLAY='"+strIsDisplay)
	                .append("',ISEDIT='"+strIsEdit+"',ISMUSTFILL='"+strIsMustFill)
	                .append("',ISFORCE='"+strIsForce+"',DEFAULT1='"+strDEFAULT)
	                .append("' Where FID='"+FID+"' And FIELD='"+tempArray2[0])
	                .append("' AND ID='"+ctlFieldObj.getID()+"'");
	            
	            userMapper.updateExecSQL(addBuf.toString());
	          }
	        }
	        mdbset = null;//赋空值
	      } catch (Exception e) {
	        LOGGER.error("saveCtlFieldInfo出现异常", e.getMessage());
	        fm.setResult(false);
	        fm.setMessage("调用方法saveCtlFieldInfo出现异常" + e.toString());
	      }
	    }
	    try {
	    	FlowControlServiceImpl fc = new FlowControlServiceImpl();
		    fc.initHashtable();
		    
		    CollectionServiceImpl cs = new CollectionServiceImpl();
		    cs.inithashtable();
	    } catch (Exception ex1) {
	    	ex1.printStackTrace();
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("saveCtlFieldInfo执行完成，耗时：" + (endTime - startTime) + " ms.");
	    return fm;
	}

	@Override
	public Group[] getUserGroupList() throws Exception {
	    Group entitys[] = null;
	    StringBuffer addBuf = new StringBuffer();
	    String strID="";
	    String strNAME="";
	    addBuf.append("select * from BPIP_ROLE order by ROLEID");
	    try {
	      List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	      int length = retlist != null ? retlist.size() : 0;
	      if (length == 0) {
	        entitys = null;
	      } else {// length > 0
	        entitys = new Group[length];
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = retlist.get(i);
	            entitys[i] = new Group();
	            strID= retmap.get("ROLEID").toString();
	            strNAME= retmap.get("ROLENAME").toString();
	            entitys[i].setID(strID);
	            entitys[i].setNAME(strNAME);
	        }
	      }
	    } catch (Exception e) {
	      LOGGER.error("getUserGroupList出现异常", e.getMessage());
	      entitys = null;
	    }
	    return entitys;
	}

	@Override
	public Group[] getUserGroupListByFid(String FID) throws Exception {
	    Group entitys[] = null;
	    String strID = "";
	    String strNAME = "";
	    StringBuffer addBuf = new StringBuffer();
	    
	    addBuf.append("select * from BPIP_ROLE ");
	    if (!FID.trim().equals("")) {  
	    	addBuf.append("Where ROLEID in (Select GROUPID from FLOW_CONFIG_ACTIVITY_GROUP "
	    				+ "Where ACTIVITYID='" + FID + "') ");
	    }
	    addBuf.append(" order by ROLEID");
	    try {
	      List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	      int length = retlist != null ? retlist.size() : 0;
	      if (length == 0) {
	        entitys = null;
	      } else {// length > 0
	        entitys = new Group[length];
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = retlist.get(i);
	        	entitys[i] = new Group();
	        	strID= retmap.get("ROLEID").toString();
	        	strNAME= retmap.get("ROLENAME").toString();
	        	
	        	entitys[i].setID(strID);
	        	entitys[i].setNAME(strNAME);
	        }
	      }
	    } catch (Exception e) {
		    LOGGER.error("getUserGroupListByFid出现异常", e.getMessage());
	    	entitys = null;
	    }
	    return entitys;
	}

	@Override
	public FunctionMessage addUserGroupToActivity(String FID, String GROUPID) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    FunctionMessage fm = new FunctionMessage(1);
	    LOGGER.info("addUserGroupToActivity开始调用...");
	    long startTime = System.currentTimeMillis();
	    addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY_GROUP Where ACTIVITYID='"+FID+"' and GROUPID='"+GROUPID+"'");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    if (retlist != null && retlist.size() > 0) {
	      fm.setResult(false);
	      fm.setMessage("异常名称【指定的用户组已经存在活动中】已经存在");
	      return fm;
	    }
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("Insert Into FLOW_CONFIG_ACTIVITY_GROUP(ID,ACTIVITYID,GROUPID) values('")
	          .append(getMaxID("FLOW_CONFIG_ACTIVITY_GROUP", 10)+"','"+FID+"','"+GROUPID+"')");
	    try {
	      Integer retInt = userMapper.insertExecSQL(addBuf.toString());
	      if (retInt != null && retInt > 0) {
	        fm.setMessage("保存成功");
	        fm.setResult(true);
	      }
	    } catch (Exception e) {
		  LOGGER.error("addUserGroupToActivity出现异常", e.getMessage());
	      fm.setResult(false);
	      fm.setMessage("调用方法AddUserGroupToActivity出现异常" + e.toString());
	      return fm;
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("addUserGroupToActivity执行完成，耗时：" + (endTime - startTime) + " ms.");
	    return fm;
	}

	@Override
	public FunctionMessage deleteUserGroupFromActivity(String FID) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    FunctionMessage fm = new FunctionMessage(1);
	    LOGGER.info("deleteUserGroupFromActivity开始调用...");
	    long startTime = System.currentTimeMillis();
	    addBuf.append("Delete FLOW_CONFIG_ACTIVITY_GROUP Where ACTIVITYID='"+FID+"'");
	    try {
	      Integer retInt = userMapper.deleteExecSQL(addBuf.toString());
	      if (retInt != null && retInt > 0) {
	        fm.setMessage("删除成功");
	        fm.setResult(true);
	      }
	    } catch (Exception e) {
	      LOGGER.error("deleteUserGroupFromActivity出现异常", e.getMessage());
	      fm.setResult(false);
	      fm.setMessage("调用方法DeleteUserGroupFromActivity出现异常" + e.toString());
	      return fm;
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("deleteUserGroupFromActivity执行完成，耗时：" + (endTime - startTime) + " ms.");
	    return fm;  
	}

	@Override
	public List<Map<String, Object>> getTableList() throws Exception {
	    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	    String strSQL = "Select TABLENAME,CHINESENAME From BPIP_TABLE where TABLETYPE ='2' or TABLETYPE ='3' order by TABLEID";
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	      for (int i = 0; i < length; i++) {
	    	Map<String, Object> retmap = retlist.get(i);
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
	public List<Map<String, Object>> getTableList1(String StrWhere) throws Exception {
	     List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	     StringBuffer addBuf = new StringBuffer();
	     addBuf.append("Select TABLENAME,CHINESENAME from BPIP_TABLE where "+StrWhere+" order by TABLEID");
	     List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
		 int length = retlist != null ? retlist.size() : 0;
	     if (length > 0) {
	       for (int i = 0; i < length; i++) {
	    	 Map<String, Object> retmap = retlist.get(i);
	         String[] saTmp = new String[2];
	         saTmp[0] = retmap.get("TableName").toString();
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
	public List<Map<String, Object>> getFieldList(String TableName) throws Exception {
	    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("select FieldID,FieldName,CHINESENAME from bpip_field "
	    			+ "Where TABLEID=(Select TableID From Bpip_Table "
	    			+ "Where TableName='" + TableName.toUpperCase()+"') Order by FieldID");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
		int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	      for (int i = 0; i < length; i++) {
	    	Map<String, Object> retmap = retlist.get(i);
	        String[] saTmp = new String[2];
	        saTmp[0] = retmap.get("FieldName").toString();
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
	public String getMaxID(String tableName, int idLength) throws Exception {
	    String MaxNo = "";
	    int LenMaxNo = 0;
	    StringBuffer addBuf = new StringBuffer();
	    LOGGER.info("getMaxID开始调用...");
	    long startTime = System.currentTimeMillis();
	    addBuf.append("SELECT MAX(ID) AS MaxNo FROM " + tableName);
	    try {
	      String retMaxNo = userMapper.selectStrExecSQL(addBuf.toString());
	      if (StringUtils.isNotBlank(retMaxNo)) {
	    	  MaxNo = retMaxNo;
	          MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
	          LenMaxNo = MaxNo.length();
	          addBuf.delete(0,addBuf.length());//清空
	          addBuf.append("0000000000000000000000000"+MaxNo);
	          MaxNo = addBuf.toString();
	      } else {// retMaxNo == null || retMaxNo.length() == 0
	    	  MaxNo = "0000000000000000000000001";
	      }
	      MaxNo = MaxNo.substring(25 - idLength + LenMaxNo);
	    }
	    catch (Exception ex) {
	    	LOGGER.error("getMaxID Exception:\n", ex.getMessage());
	    }
	    long endTime = System.currentTimeMillis();
	    LOGGER.info("getMaxID执行完成，耗时：" + (endTime - startTime) + " ms.");
	    LOGGER.info("getMaxID结束调用");
	    return MaxNo;
	}

	@Override
	public String getDocID(String strID) throws Exception {
	      StringBuffer addBuf = new StringBuffer();
	      addBuf.append("Select DOCID From FLOW_CONFIG_PROCESS where ID='"+strID+"'");
	      Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	      addBuf.delete(0,addBuf.length());//清空
	      if (retmap != null && retmap.size() > 0) {
	          addBuf.append(retmap.get("DOCID").toString());
	      }
	      return addBuf.toString();  
	}

	/**
	 * 将字符串中的单引号转换为双引号，若字符串中有单引号在保存时才不会出错
	 * @param expression String    待转换的字符串
	 * @return String              已经转换好的字符串
	 */ 
	private String convertQuotationSingleToDouble(String expression) {
	    return expression.replaceAll("\'", "''");
	}

	/**
	 * 处理流程活动策略属性信息
	 * @param entityObj 活动信息的实体
	 * @return FunctionMessage
	 */
	private FunctionMessage saveStrategyProperty1(FLOW_CONFIG_ACTIVITY entityObj) {
		FunctionMessage fm = new FunctionMessage(1);
		LOGGER.info("SaveStrategyProperty开始调用...");
		long startTime = System.currentTimeMillis();
		StringBuffer addBuf = new StringBuffer();
		addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY Where ID='" + entityObj.getID() + "'");
		DBSet mdbset = dbEngine.QuerySQL(addBuf.toString());
		addBuf.delete(0, addBuf.length());// 清空
		if (mdbset != null) {
			addBuf.append(" Update FLOW_CONFIG_ACTIVITY Set ASTRATEGY='" + entityObj.getASTRATEGY())
					.append("'" + ",CSTRATEGY='" + entityObj.getCSTRATEGY() + "'")
					.append(",CNUM=" + entityObj.getCNUM() + ",ISSAVE1='" + entityObj.getISSAVE1())
					.append("',ISSAVE2='" + entityObj.getISSAVE2() + "'" + ",ISLEAVE1='")
					.append(entityObj.getISLEAVE1() + "',ISLEAVE2='" + entityObj.getISLEAVE2()).append("'"
							+ ",ISBRANCH='" + entityObj.getISBRANCH() + "'" + "  Where ID='" + entityObj.getID() + "'");
			mdbset = null;// 赋空值
		} else {
			addBuf.append(
					"Insert Into FLOW_CONFIG_ACTIVITY(ID,ASTRATEGY,CSTRATEGY,CNUM,ISSAVE1,ISSAVE2,ISLEAVE1,ISLEAVE2,ISBRANCH) values('")
					.append(entityObj.getID() + "','" + entityObj.getASTRATEGY() + "','" + entityObj.getCSTRATEGY())
					.append("'," + entityObj.getCNUM() + ",'" + entityObj.getISSAVE1() + "','")
					.append(entityObj.getISSAVE2() + "'" + entityObj.getISLEAVE1() + "','" + entityObj.getISLEAVE2())
					.append("','" + entityObj.getISBRANCH() + "')");
		}
		try {
			if (dbEngine.ExecuteSQL(addBuf.toString())) {
				fm.setMessage("保存成功");
				fm.setResult(true);
			}
		} catch (Exception e) {
			fm.setResult(false);
			fm.setMessage("调用方法SaveStrategyProperty出现异常" + e.toString());
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info("SaveStrategyProperty执行完成，耗时："+(endTime-startTime)+" ms.");
		return fm;
	}
}