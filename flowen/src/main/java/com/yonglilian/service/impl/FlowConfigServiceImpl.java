package com.yonglilian.service.impl;

import com.yonglilian.dao.mapper.FlowDAOMapper;
import com.yonglilian.flowengine.mode.base.Package;
import com.yonglilian.flowengine.mode.config.*;
import com.yonglilian.service.FlowConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBMode;
import zr.zrpower.common.db.DBRow;
import zr.zrpower.common.db.DBSet;
import zr.zrpower.common.util.FunctionMessage;
import zr.zrpower.common.util.ReflectionUtil;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * 流程引擎-流程配置管理
 * @author lwk
 *
 */
@Service
public class FlowConfigServiceImpl implements FlowConfigService {
	/** The FlowSetupServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowSetupServiceImpl.class);
	FlowControlServiceImpl flowControl = new FlowControlServiceImpl();
	/** 数据库引擎. */
	private DBEngine dbEngine;
	static private int clients = 0;
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
	/** 流程引擎相关接口的DAO层. */
	@Autowired
	private FlowDAOMapper flowDAOMapper;

	/**
	 * 构造方法
	 */
	public FlowConfigServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
	    if (clients<1) {
	      clients = 1;
	    }  
	}

	@Override
	public FunctionMessage addPr(FLOW_CONFIG_PROCESS Pr, String strFormfields, String strFormfields1) throws Exception {
	    FunctionMessage fm = new FunctionMessage(1);
	    StringBuffer addBuf = new StringBuffer();
	    try {
	        String strMaxNo = getMaxFieldNo("FLOW_CONFIG_PROCESS","ID",10);
	        Pr.setID(strMaxNo);//设置ID
	        addBuf.append("WF"+strMaxNo.substring(2,10));
	        Pr.setIDENTIFICATION(addBuf.toString());//设置标识
	        boolean isOK = dbEngine.ExecuteInsert(Pr.getData());
	        if (isOK) {
	          addProcessGroup(strFormfields,strFormfields1,strMaxNo);
	          //一般流程
	          if(Pr.getTYPE().equals("1")){
	        	  //自动插入活动表[FLOW_CONFIG_ACTIVITY]
		          //开始
		          String[] strSQLs = new String[2];
		          String strMaxNo1 = getMaxFieldNo("FLOW_CONFIG_ACTIVITY","ID",10);   //活动ID
		          addBuf.delete(0,addBuf.length());//清空
		          addBuf.append("FA"+strMaxNo1);
		          String strIdentification1 = addBuf.toString();                      //活动标识
		          addBuf.delete(0,addBuf.length());//清空
		          addBuf.append("Insert into FLOW_CONFIG_ACTIVITY(ID,FID,IDENTIFICATION,NAME,DESC1,TYPE,ORDER1,ISSIGN,ASTRATEGY,CSTRATEGY,CNUM,OPENHELP,XYCSS) values('")
		              	.append(strMaxNo1+"','"+strMaxNo+"','"+strIdentification1+"','开始','',1,0,'','','',null,'','1/0/开始活动//1/153px/707px')");
		          strSQLs[0] = addBuf.toString();
		          
		          //结束
		          String strMaxNo3 = getMoreMaxFieldNo(strMaxNo1,10,1);  //活动ID
		          
		          addBuf.delete(0,addBuf.length());//清空
		          addBuf.append("FA"+strMaxNo3);
		          String strIdentification3 = addBuf.toString();                       //活动标识
		          
		          addBuf.delete(0,addBuf.length());//清空
		          addBuf.append("Insert into FLOW_CONFIG_ACTIVITY(ID,FID,IDENTIFICATION,NAME,DESC1,TYPE,ORDER1,ISSIGN,ASTRATEGY,CSTRATEGY,CNUM,OPENHELP,XYCSS) values('")
		             	.append(strMaxNo3+"','"+strMaxNo+"','"+strIdentification3+"','结束','',2,10000,'','','',null,'','1/-1/结束活动//2/2144px/769px')");
		          strSQLs[1] = addBuf.toString();
		          
		          //dbEngine.ExecuteSQLs(strSQLs);
		          for (String strSQL : strSQLs) {
		        	  userMapper.insertExecSQL(strSQL);
		          }
	          }
	          //基础数据
	          if(Pr.getTYPE().equals("2")){
		          //自动插入活动表[FLOW_CONFIG_ACTIVITY]
		          //开始
		          String[] strSQLs = new String[5];
		          String strMaxNo1 = getMaxFieldNo("FLOW_CONFIG_ACTIVITY","ID",10);   //活动ID
		          addBuf.delete(0,addBuf.length());//清空
		          addBuf.append("FA"+strMaxNo1);
		          String strIdentification1 = addBuf.toString();                      //活动标识
		          addBuf.delete(0,addBuf.length());//清空
		          addBuf.append("Insert into FLOW_CONFIG_ACTIVITY(ID,FID,IDENTIFICATION,NAME,DESC1,TYPE,ORDER1,ISSIGN,ASTRATEGY,CSTRATEGY,CNUM,OPENHELP,XYCSS) values('")
		              .append(strMaxNo1+"','"+strMaxNo+"','"+strIdentification1+"','开始','',1,0,'','','','','','1/0/开始活动//1/153px/707px')");
		          strSQLs[0] = addBuf.toString();
		          
		          //数据录入
		          String strMaxNo2 = getMoreMaxFieldNo(strMaxNo1,10,1); //活动ID
		          addBuf.delete(0,addBuf.length());//清空
		          addBuf.append("FA"+strMaxNo2);
		          String strIdentification2 = addBuf.toString(); //活动标识
		          addBuf.delete(0,addBuf.length());//清空
		          addBuf.append("Insert into FLOW_CONFIG_ACTIVITY(ID,FID,IDENTIFICATION,NAME,DESC1,TYPE,ORDER1,ISSIGN,ASTRATEGY,CSTRATEGY,CNUM,OPENHELP,XYCSS) values('")
		                .append(strMaxNo2+"','"+strMaxNo+"','"+strIdentification2+"','数据录入','',3,0,1,1,1,0,1,'1/")
		                .append(strMaxNo2+"/数据录入/0/3/995px/389px')");
		          strSQLs[1] = addBuf.toString();
		          
		          //结束
		          String strMaxNo3 = getMoreMaxFieldNo(strMaxNo1,10,2);  //活动ID
		          addBuf.delete(0,addBuf.length());//清空
		          addBuf.append("FA"+strMaxNo3);
		          String strIdentification3 = addBuf.toString();                       //活动标识
		          addBuf.delete(0,addBuf.length());//清空
		          addBuf.append("Insert into FLOW_CONFIG_ACTIVITY(ID,FID,IDENTIFICATION,NAME,DESC1,TYPE,ORDER1,ISSIGN,ASTRATEGY,CSTRATEGY,CNUM,OPENHELP,XYCSS) values('")
		              	.append(strMaxNo3+"','"+strMaxNo+"','"+strIdentification3+"','结束','',2,10000,'','','','','','1/-1/结束活动/")
		              	.append(strMaxNo2+"/2/2144px/769px')");
		          strSQLs[2] = addBuf.toString();
		          
		          //自动插入活动依赖转发(关系)表[FLOW_CONFIG_ACTIVITY_CONNE]
		          //开始到数据录入依赖转发关系线
		         String strMaxNo4 = getMaxFieldNo("FLOW_CONFIG_ACTIVITY_CONNE","ID",10);
		         addBuf.delete(0,addBuf.length());//清空
		         addBuf.append("Insert into FLOW_CONFIG_ACTIVITY_CONNE(ID,FID,CID,SID) values('"+strMaxNo4)
		               .append("','"+strMaxNo+"','"+strMaxNo2+"','"+strMaxNo1+"')");
		         strSQLs[3] = addBuf.toString();
		         
		         //数据录入到结束依赖转发关系线
		         String strMaxNo5 = getMoreMaxFieldNo(strMaxNo4,10,1);
		         addBuf.delete(0,addBuf.length());//清空
		         addBuf.append("Insert into FLOW_CONFIG_ACTIVITY_CONNE(ID,FID,CID,SID) values('"+strMaxNo5)
		               .append("','"+strMaxNo+"','"+strMaxNo3+"','"+strMaxNo2+"')");
		         strSQLs[4] = addBuf.toString();
		         
		         //dbEngine.ExecuteSQLs(strSQLs);
		         for (String strSQL : strSQLs) {
		        	 userMapper.insertExecSQL(strSQL);
		         }
	         }
	         fm.setMessage("录入成功");
	         fm.setResult(true);
	        }
	    } catch (Exception e) {
	    	LOGGER.error("FlowConfigServiceImpl.addPr Exception:\n", e);
	    	fm.setResult(false);
	    	fm.setMessage("调用方法AddPr出现异常" + e.toString());
	    	return fm;
	    }
	    flowControl.initHashtable();
	    return fm;
	}

	@Override
	public FunctionMessage editPr(FLOW_CONFIG_PROCESS Pr, String strFormfields, 
			String strFormfields1) throws Exception {
	    FunctionMessage fm = new FunctionMessage(1);
	    try {
	    	boolean isOk = dbEngine.ExecuteEdit(Pr.getData(), "ID=" + Pr.getID());
	    	String strID = Pr.getID();
	    	if (isOk) {
		        deleteProcess(strID);
		        addProcessGroup(strFormfields,strFormfields1, strID);
		        fm.setResult(true);
		        fm.setMessage("修改成功");
	    	}  else {
		        fm.setResult(false);
		        fm.setMessage("流程基本属性【" + Pr.getID() + "】不存在");
	    	}
	    } catch (Exception e) {
	    	fm.setResult(false);
		    fm.setMessage("调用方法EditPr出现异常" + e.toString());
		    return fm;
	    }
	    flowControl.initHashtable();
	    
	    return fm;
	}

	@Override
	public FunctionMessage deletePr(String ID) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    FunctionMessage fm = new FunctionMessage(1);
	    //-------------先删除流程下的相关配置---------------//
        //删除当前流程的监控和任务重分配用户组
        String[] strSQLs = new String[7];
        addBuf.append("Delete from FLOW_CONFIG_PROSESS_GROUP where PROSESSID = '"+ID+"'");
        strSQLs[0] = addBuf.toString();
        
        //删除当前流程下的活动(流程步骤)间的关系
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Delete from FLOW_CONFIG_ACTIVITY_CONNE where FID = '"+ID+"'");
        strSQLs[1] = addBuf.toString();
        
        //删除当前流程下的活动的超时属性
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Delete from FLOW_CONFIG_TIME where FID in (select ID from FLOW_CONFIG_ACTIVITY where FID ='"+ID+"')");
        strSQLs[2] = addBuf.toString();
        
        //删除当前流程下的活动的按钮
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Delete from FLOW_CONFIG_ACTIVITY_BUTTON where FID in (select ID from FLOW_CONFIG_ACTIVITY where FID ='"+ID+"')");
        strSQLs[3] = addBuf.toString();
        
        //删除当前流程下的活动的可操作字段
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Delete from COLL_CONFIG_OPERATE_FIELD where FID in (select ID from FLOW_CONFIG_ACTIVITY where FID ='"+ID+"')");
        strSQLs[4] = addBuf.toString();
        
        //删除当前流程下的活动的流程组配置
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Delete from FLOW_CONFIG_ACTIVITY_GROUP where ACTIVITYID in (select ID from FLOW_CONFIG_ACTIVITY where FID ='"+ID+"')");
        strSQLs[5] = addBuf.toString();
        
        //删除当前流程下的流程活动(流程步骤)
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Delete from FLOW_CONFIG_ACTIVITY where FID = '"+ID+"'");
        strSQLs[6] = addBuf.toString();
	    
        //dbEngine.ExecuteSQLs(strSQLs);
        for (String strSQL : strSQLs) {
        	userMapper.deleteExecSQL(strSQL);
        }
	    //---------------先删除流程下的相关配置结束--------------//
        Integer retInt = flowDAOMapper.deleteFLOW_CONFIG_PROCESS(ID);
	    if (retInt != null && retInt > 0) {
           fm.setResult(true);
           fm.setMessage("流程【" + ID + "】已经删除!");
	    } else {
           fm.setMessage("流程删除失败!");
           fm.setResult(false);
	    }
	    flowControl.initHashtable();
	    
	    return fm;
	}

	@Override
	public FLOW_CONFIG_PROCESS[] getFlowPROCESSList(String strwhere) throws Exception {
	    FLOW_CONFIG_PROCESS bgs[] = null;
	    String strSql = null;
	    try {
	      strSql = strwhere;
	      DBSet mdbset = dbEngine.QuerySQL(strSql);
	      bgs = new FLOW_CONFIG_PROCESS[mdbset.RowCount()];
	      if (mdbset.RowCount() == 0) {
	    	  return null;
	      } else {
	        for (int i = 0; i < mdbset.RowCount(); i++) {
	          bgs[i] = new FLOW_CONFIG_PROCESS();
	          bgs[i].fullData(mdbset.Row(i));
	        }
	      }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return null;
	    }
	    return bgs;
	}

	@Override
	public FLOW_CONFIG_PROCESS getPROCESSID(String id) throws Exception {
	    FLOW_CONFIG_PROCESS bp = null;
	    StringBuffer addBuf = new StringBuffer();
	    try {
	      addBuf.append("Select * From FLOW_CONFIG_PROCESS Where ID='"+id.trim()+"'");
	      Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	      if (retmap == null || retmap.size() <= 0) {
	        return null;
	      } else {// retmap != null
	          bp = (FLOW_CONFIG_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_PROCESS.class);
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }
	    return bp;
	}

	@Override
	public String group_AllList(String Lst_Name) throws Exception {
	    String strID=""; String strNAME="";
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select ROLEID,ROLENAME from BPIP_ROLE order by ROLEID");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("<select name='"+Lst_Name+"' style='width:242px;height:150px' size=15 multiple >\r\n");
	    if (length > 0) {
	    	for (int i = 0; i < length; i++) {
	    		Map<String, Object> retmap = retlist.get(i);
	    		//查找当前用户是只能监控本部门的流程
	    		strID = retmap.get("ROLEID").toString();
	    		strNAME = retmap.get("ROLENAME").toString();
	    		
	    		addBuf.append("<option value='"+strID+"'>"+strNAME+"</option>\r\n");
	        }
	    }
	    addBuf.append("</select>\r\n");
	    return addBuf.toString();
	}

	@Override
	public String flow_Group_List(String strGroupId, String Lst_Name, String Is_Enabled) throws Exception {
	    String strName = "";
	    StringBuffer addBuf = new StringBuffer();
	    //查找当前用户是只能监控本部门的流程
	    addBuf.append("Select ISDEPT,GROUPID FROM FLOW_CONFIG_PROSESS_GROUP Where TYPE = '1' AND PROSESSID = '")
	          .append(strGroupId+"' order by ID");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("<select name='"+Lst_Name+"' style='width:242px;height:150px' size=15 multiple "+Is_Enabled)
	          .append(">\r\n");
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = retlist.get(i);
	        	strName = getFlowGroupTrueName(retmap.get("GROUPID").toString());
	        	if (retmap.get("ISDEPT").toString().equals("1")) {
	              addBuf.append("<option value='"+retmap.get("GROUPID").toString())
	                    .append(";1'>"+strName+"(本部门)</option>\r\n");
	        	} else {
	              addBuf.append("<option value='"+retmap.get("GROUPID").toString())
	                    .append(";0'>"+strName+"</option>\r\n");
	        	}
	        }
	    }
	    addBuf.append("</select>\r\n");
	    return addBuf.toString();
	}

	@Override
	public String flow_Group_List1(String strGroupId, String Lst_Name, String Is_Enabled) throws Exception {
	      String strName = "";
	      StringBuffer addBuf = new StringBuffer();
	      //查找当前用户是只能监控本部门的流程
	      addBuf.append("Select ISDEPT,GROUPID FROM FLOW_CONFIG_PROSESS_GROUP Where TYPE = '2' AND PROSESSID = '")
	            .append(strGroupId+"' order by ID");
	      List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	      int length = retlist != null ? retlist.size() : 0;
	      addBuf.delete(0,addBuf.length());//清空
	      addBuf.append("<select name='"+Lst_Name+"' style='width:242px;height:150px' size=15 multiple ")
	            .append(Is_Enabled+">\r\n");
	      if (length > 0) {
	          for (int i = 0; i < length; i++) {
	        	  Map<String, Object> retmap = retlist.get(i);
	        	  strName = getFlowGroupTrueName(retmap.get("GROUPID").toString());
	        	  if (retmap.get("ISDEPT").toString().equals("1")) {
	        		  addBuf.append("<option value='"+retmap.get("GROUPID").toString())
	                  		.append(";1'>"+strName+"(本部门)</option>\r\n");
	        	  } else {
	        		  addBuf.append("<option value='"+retmap.get("GROUPID").toString())
	        		  		.append(";0'>"+strName+"</option>\r\n");
	        	  }
	          }
	      }
	      addBuf.append("</select>\r\n");
	      return addBuf.toString();
	}

	@Override
	public List<Object> getArrayList(String strItems, String strItemMark) throws Exception {
	    int intItemLen, i = 0, n = 0;
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

	@Override
	public FLOW_CONFIG_PROCESS[] getFlowList() throws Exception {
	     FLOW_CONFIG_PROCESS[] retValue = null;
	     StringBuffer addBuf = new StringBuffer();
	     addBuf.append("Select * From FLOW_CONFIG_PROCESS order by ID");
	     List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	     int length = retlist != null ? retlist.size() : 0;
	     if (length > 0) {
	    	 retValue = new FLOW_CONFIG_PROCESS[length];
	         for (int i = 0; i < length; i++) {
	        	 Map<String, Object> retmap = retlist.get(i);
	        	 retValue[i] = (FLOW_CONFIG_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_PROCESS.class);
	         }
	     }
	     return retValue;
	 }

	@Override
	public String getFlowPackage(String ID) throws Exception {
	     StringBuffer addBuf = new StringBuffer();
	     addBuf.append("Select FLOWPACKAGE From FLOW_CONFIG_PROCESS WHERE ID='"+ID+"'");
	     Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	     addBuf.delete(0, addBuf.length()); //清空
	     if (retmap != null && retmap.size() > 0) {
	         addBuf.append(retmap.get("FLOWPACKAGE").toString());
	     }
	     return addBuf.toString();
	}

	@Override
	public FLOW_CONFIG_PROCESS[] getFlowList(String strPackageID) throws Exception {
	    FLOW_CONFIG_PROCESS[] returnValue = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select * From FLOW_CONFIG_PROCESS  where FLOWPACKAGE ='"+strPackageID)
	          .append("' order by CODE,ID");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        returnValue = new FLOW_CONFIG_PROCESS[length];
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = retlist.get(i);
	        	returnValue[i] = (FLOW_CONFIG_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_PROCESS.class);
	        }
	    }
	   return returnValue;
	}

	@Override
	public FLOW_CONFIG_PROCESS[] getFlowList1() throws Exception {
	    FLOW_CONFIG_PROCESS[] returnValue = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select * From FLOW_CONFIG_PROCESS where TYPE='1' and FLOWPACKAGE in "
	    			+ "(select ID From FLOW_CONFIG_PACKAGE) order by NAME,CODE,ID");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        returnValue = new FLOW_CONFIG_PROCESS[length];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          returnValue[i] = (FLOW_CONFIG_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_PROCESS.class);
	        }
	    }
	   return returnValue;
	}

	@Override
	public FLOW_CONFIG_PROCESS[] getFlowList2(String strPackageID) throws Exception {
	    FLOW_CONFIG_PROCESS[] returnValue = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select a.ID,a.NAME,a.CREATEDATE,a.STATUS,b.NAME AS CREATEPSN From FLOW_CONFIG_PROCESS a Left Join BPIP_USER b on a.CREATEPSN=b.USERID "
	    			+ "where a.FLOWPACKAGE ='"+strPackageID+"' order by a.CODE,a.ID");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        returnValue = new FLOW_CONFIG_PROCESS[length];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          returnValue[i] = (FLOW_CONFIG_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_PROCESS.class);
	        }
	    }
	   return returnValue;
	}

	@Override
	public boolean copyFlowPublic(Vector<Object> vec, int cnum1, int cnum2) throws Exception {
	       DBRow[] drs = new DBRow[cnum2];
	       int k =0;
	       for (int i = cnum1*100; i < cnum2+cnum1*100; i++){
	         drs[k] = (DBRow) vec.get(i);
	         k++;
	       }
	       dbEngine.UpdateAll(drs);
	       LOGGER.info("批量执行SQL：第"+String.valueOf(cnum1)+"次执行.");
	       return true;
	}

	@Override
	public Vector<Object> copyFlowPackage(String strID, String strUnitID) throws Exception {
        String strTmp = "";               //临时编号
        String strMaxNo = "";             //最大流水号
        String strTmpMaxNo = "";          //临时最大流水号
        String strPackageIDs = "";        //流程包
        String strProcesss = "";          //流程
        String strActivitys = "";         //活动
        //得到流程引擎独立运转的单位编号位数
        int len = 2;
        String strUnitID1 = strUnitID.substring(0,len);
        Vector<Object> vec = new Vector<Object>();
        StringBuffer addBuf = new StringBuffer();
        StringBuffer tmpBuf = new StringBuffer();
        //复制流程包-----------------------
        addBuf.append("Select * From FLOW_CONFIG_PACKAGE where ID = '"+strID+"' OR FID='"+strID+"' "
              		+ "or FID in(select ID from FLOW_CONFIG_PACKAGE where FID='"+strID+"')");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        int length = retlist != null ? retlist.size() : 0;
        addBuf.delete(0,addBuf.length());//清空
        if (length > 0) {
             strTmpMaxNo = getMaxFieldNo("FLOW_CONFIG_PACKAGE","ID",3);
             for (int i = 0; i < length; i++){
            	 Map<String, Object> retmap = retlist.get(i);
                 Package package1 = (Package) ReflectionUtil.convertMapToBean(retmap, Package.class);
                 strMaxNo = getMoreMaxFieldNo(strTmpMaxNo,3,i);
                 addBuf.append(retmap.get("ID").toString()+","+strMaxNo+";");
                 package1.setID(strMaxNo);    //设置流程包ID
                 
                 strTmp = retmap.get("FID").toString();
                 if (!strTmp.equals("000"))//非第一层的包
                 {
                   strTmp = getNewCode(strPackageIDs,strTmp);//得到新的FID
                   if (strTmp.length()==0) {
                     strTmp = retmap.get("FID").toString();
                   }
                   package1.setFID(strTmp); //设置FID
                 }
                 tmpBuf.delete(0,tmpBuf.length());//清空
                 tmpBuf.append("FP"+strUnitID1+strMaxNo);

                 package1.setIDENTIFICATION(tmpBuf.toString()); //设置流程包标识
                 tmpBuf.delete(0,tmpBuf.length());//清空
                 tmpBuf.append("复件"+retmap.get("NAME").toString());
                 package1.setNAME(tmpBuf.toString()); //设置流程包名称
                 
                 DBRow dr = package1.getData();
                 dr.setDataMode(DBMode.NEW);
                 vec.add(dr);
                 package1 = null;
             }
        }
        strPackageIDs = addBuf.toString();
        //--------------------------------
        //复制流程-------------------------
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Select * From FLOW_CONFIG_PROCESS where FLOWPACKAGE in (select ID from FLOW_CONFIG_PACKAGE "
        			+ "where ID='"+strID+"' OR FID='"+strID+"' or FID in(select ID from FLOW_CONFIG_PACKAGE where FID='"
            		+ strID+"')) order by ID");
        List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(addBuf.toString());
        int length1 = retlist1 != null ? retlist1.size() : 0;
        addBuf.delete(0, addBuf.length());//清空
        if (length1 > 0) {
             strTmpMaxNo = getMaxFieldNo("FLOW_CONFIG_PROCESS","ID",10);
             LOGGER.info("复制流程包：流程数："+String.valueOf(length1));
             for (int i = 0; i < length1; i++){
            	 Map<String, Object> retmap1 = retlist1.get(i);
                 FLOW_CONFIG_PROCESS process = (FLOW_CONFIG_PROCESS) ReflectionUtil.convertMapToBean(retmap1, FLOW_CONFIG_PROCESS.class);
                 strMaxNo = getMoreMaxFieldNo(strTmpMaxNo,10,i);
                 addBuf.append(retmap1.get("ID").toString()+","+strMaxNo+";");
                 
                 process.setID(strMaxNo);    //设置流程ID
                 if (strMaxNo.length()>=10){strMaxNo=strMaxNo.substring(2,10);}
                 tmpBuf.delete(0,tmpBuf.length());//清空
                 tmpBuf.append("WF"+strMaxNo);
                 process.setIDENTIFICATION(tmpBuf.toString());//设置流程标识
                 strTmp = retmap1.get("FLOWPACKAGE").toString();
                 strTmp = getNewCode(strPackageIDs,strTmp);
                 process.setFLOWPACKAGE(strTmp);//设置新的流程包
                 
                 DBRow dr = process.getData();
                 dr.setDataMode(DBMode.NEW);
                 vec.add(dr);
                 
                 process = null;
             }
        }
        strProcesss = addBuf.toString();
        //--------------------------------
        //复制流程的监控和任务重分配用户组-----
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Select * From FLOW_CONFIG_PROSESS_GROUP where PROSESSID in (select ID from FLOW_CONFIG_PROCESS "
        			+ "where FLOWPACKAGE in (select ID from FLOW_CONFIG_PACKAGE "
        			+ "where ID='"+strID+"' OR FID='"+strID+"' or FID in(select ID from FLOW_CONFIG_PACKAGE where FID='"+strID+"'))) order by ID");
        List<Map<String, Object>> retlist2 = userMapper.selectListMapExecSQL(addBuf.toString());
        int length2 = retlist2 != null ? retlist2.size() : 0;
        if (length2 > 0) {
             strTmpMaxNo = getMaxFieldNo("FLOW_CONFIG_PROSESS_GROUP","ID",10);
             LOGGER.info("复制流程包：流程的监控和任务重分配用户组数："+String.valueOf(length2));
             for (int i = 0; i < length2; i++){
            	 Map<String, Object> retmap2 = retlist2.get(i);
                 FLOW_CONFIG_PROSESS_GROUP prosess_Group = (FLOW_CONFIG_PROSESS_GROUP) ReflectionUtil.convertMapToBean(retmap2, FLOW_CONFIG_PROSESS_GROUP.class);
                 strMaxNo = getMoreMaxFieldNo(strTmpMaxNo,10,i);
                 
                 prosess_Group.setID(strMaxNo);    //设置ID
                 strTmp = retmap2.get("PROSESSID").toString();
                 strTmp = getNewCode(strProcesss,strTmp);
                 prosess_Group.setPROSESSID(strTmp);    //设置新的过程ID
                 
                 DBRow dr = prosess_Group.getData();
                 dr.setDataMode(DBMode.NEW);
                 vec.add(dr);
                 prosess_Group = null;
             }
        }
        //-------------------------------------------
        //复制活动(流程步骤)前编号对应关系----------------
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Select * From FLOW_CONFIG_ACTIVITY where FID in (select ID from FLOW_CONFIG_PROCESS "
        			+ "where FLOWPACKAGE in (select ID from FLOW_CONFIG_PACKAGE where ID='")
        	  .append(strID+"' OR FID='"+strID+"' or FID in(select ID from FLOW_CONFIG_PACKAGE where FID='")
        	  .append(strID+"'))) order by ID");
        List<Map<String, Object>> retlist31 = userMapper.selectListMapExecSQL(addBuf.toString());
        int length31 = retlist31 != null ? retlist31.size() : 0;
        addBuf.delete(0,addBuf.length());//清空
        if (length31 > 0) {
	        strTmpMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY","ID",10);
	        for (int i = 0; i < length31; i++){
	        	Map<String, Object> retmap31 = retlist31.get(i);
	        	strMaxNo = getMoreMaxFieldNo(strTmpMaxNo,10,i);
	        	addBuf.append(retmap31.get("ID").toString()+","+strMaxNo+";");
	        }
        }
        strActivitys = addBuf.toString();
        //--------------------------------
        //复制活动(流程步骤)----------------
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Select * From FLOW_CONFIG_ACTIVITY where FID in (select ID from FLOW_CONFIG_PROCESS "
        			+ "where FLOWPACKAGE in (select ID from FLOW_CONFIG_PACKAGE where ID='")
              .append(strID+"' OR FID='"+strID+"' or FID in(select ID from FLOW_CONFIG_PACKAGE where FID='")
              .append(strID+"'))) order by ID");
        List<Map<String, Object>> retlist3 = userMapper.selectListMapExecSQL(addBuf.toString());
        int length3 = retlist3 != null ? retlist3.size() : 0;
        addBuf.delete(0,addBuf.length());//清空
        if (length3 > 0) {
             strTmpMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY","ID",10);
             LOGGER.info("复制流程包：活动(流程步骤)数："+String.valueOf(length3));
             for (int i = 0; i < length3; i++) {
            	 Map<String, Object> retmap3 = retlist3.get(i);
                 FLOW_CONFIG_ACTIVITY activity = (FLOW_CONFIG_ACTIVITY) ReflectionUtil.convertMapToBean(retmap3, FLOW_CONFIG_ACTIVITY.class);
                 strMaxNo = getMoreMaxFieldNo(strTmpMaxNo,10,i);
                 
                 activity.setID(strMaxNo);    //设置活动ID
                 tmpBuf.delete(0,tmpBuf.length());//清空
                 tmpBuf.append("FA"+strMaxNo);
                 activity.setIDENTIFICATION(tmpBuf.toString());//设置活动标识
                 
                 strTmp = retmap3.get("FID").toString();
                 strTmp = getNewCode(strProcesss,strTmp);
                 activity.setFID(strTmp);    //设置新的过程ID
                 //重设置位置信息---
                 strTmp = getXYCSS(strActivitys,activity.getXYCSS());
                 activity.setXYCSS(strTmp);
                 
                 DBRow dr = activity.getData();
                 dr.setDataMode(DBMode.NEW);
                 vec.add(dr);
                 activity = null;
             }
        }
        //--------------------------------
        //复制活动(流程步骤)间的关系----------
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Select * From FLOW_CONFIG_ACTIVITY_CONNE where FID in (select ID from FLOW_CONFIG_PROCESS "
        			+ "where FLOWPACKAGE in (select ID from FLOW_CONFIG_PACKAGE where ID='")
              .append(strID+"' OR FID='"+strID+"' or FID in(select ID from FLOW_CONFIG_PACKAGE where FID='")
              .append(strID+"'))) order by ID");
        List<Map<String, Object>> retlist4 = userMapper.selectListMapExecSQL(addBuf.toString());
        int length4 = retlist4 != null ? retlist4.size() : 0;
        if (length4 > 0) {
             strTmpMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY_CONNE","ID",10);
             for (int i = 0; i < length4; i++){
            	 Map<String, Object> retmap4 = retlist4.get(i);
                 FLOW_CONFIG_ACTIVITY_CONNE activityConne = (FLOW_CONFIG_ACTIVITY_CONNE) ReflectionUtil.convertMapToBean(retmap4, FLOW_CONFIG_ACTIVITY_CONNE.class);
                 strMaxNo = getMoreMaxFieldNo(strTmpMaxNo,10,i);
                 
                 activityConne.setID(strMaxNo);    //设置ID
                 strTmp = retmap4.get("FID").toString();
                 strTmp = getNewCode(strProcesss,strTmp);
                 activityConne.setFID(strTmp);    //设置新的过程ID
                 
                 strTmp = retmap4.get("CID").toString();
                 strTmp = getNewCode(strActivitys,strTmp);
                 activityConne.setCID(strTmp);    //设置新的当前活动ID
                 
                 strTmp = retmap4.get("SID").toString();
                 strTmp = getNewCode(strActivitys,strTmp);
                 activityConne.setSID(strTmp);    //设置新的前趋活动ID
                 
                 strTmp = retmap4.get("EID").toString();
                 strTmp = getNewCode(strActivitys,strTmp);
                 activityConne.setEID(strTmp);    //设置新的后续活动ID
                 
                 DBRow dr = activityConne.getData();
                 dr.setDataMode(DBMode.NEW);
                 vec.add(dr);
                 activityConne = null;
             }
        }
        //--------------------------------
        //复制活动的超时属性----------------
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Select * From FLOW_CONFIG_TIME where FID in (select ID from FLOW_CONFIG_ACTIVITY where FID in (select ID from FLOW_CONFIG_PROCESS "
        			+ "where FLOWPACKAGE in (select ID from FLOW_CONFIG_PACKAGE where ID='")
              .append(strID+"' OR FID='"+strID+"' or FID in(select ID from FLOW_CONFIG_PACKAGE where FID='"+strID+"')))) order by ID");
        List<Map<String, Object>> retlist5 = userMapper.selectListMapExecSQL(addBuf.toString());
        int length5 = retlist5 != null ? retlist5.size() : 0;
        if (length5 > 0) {
             strTmpMaxNo = getMaxFieldNo("FLOW_CONFIG_TIME","ID",8);
             for (int i = 0; i < length5; i++){
            	 Map<String, Object> retmap5 = retlist5.get(i);
                 FLOW_CONFIG_TIME configTime = (FLOW_CONFIG_TIME) ReflectionUtil.convertMapToBean(retmap5, FLOW_CONFIG_TIME.class);
                 strMaxNo = getMoreMaxFieldNo(strTmpMaxNo,8,i);
                 
                 configTime.setID(strMaxNo);    //设置ID
                 strTmp = retmap5.get("FID").toString();
                 strTmp = getNewCode(strActivitys,strTmp);
                 configTime.setFID(strTmp);    //设置新的活动ID
                 
                 DBRow dr = configTime.getData();
                 dr.setDataMode(DBMode.NEW);
                 vec.add(dr);
                 configTime = null;
             }
        }
        //--------------------------------
        //复制活动的按钮--------------------
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Select * From FLOW_CONFIG_ACTIVITY_BUTTON where FID in (select ID from FLOW_CONFIG_ACTIVITY where FID in (select ID from FLOW_CONFIG_PROCESS "
        			+ "where FLOWPACKAGE in (select ID from FLOW_CONFIG_PACKAGE where ID='")
              .append(strID+"' OR FID='"+strID+"' or FID in(select ID from FLOW_CONFIG_PACKAGE where FID='")
              .append(strID+"')))) order by ID");
        List<Map<String, Object>> retlist6 = userMapper.selectListMapExecSQL(addBuf.toString());
        int length6 = retlist6 != null ? retlist6.size() : 0;
        if (length6 > 0) {
             strTmpMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY_BUTTON","ID",12);
             for (int i = 0; i < length6; i++) {
            	 Map<String, Object> retmap6 = retlist6.get(i);
                 FLOW_CONFIG_ACTIVITY_BUTTON activityButton = (FLOW_CONFIG_ACTIVITY_BUTTON) ReflectionUtil.convertMapToBean(retmap6, FLOW_CONFIG_ACTIVITY_BUTTON.class);
                 strMaxNo = getMoreMaxFieldNo(strTmpMaxNo,12,i);
                 activityButton.setID(strMaxNo);    //设置ID
                 
                 strTmp = retmap6.get("FID").toString();
                 strTmp = getNewCode(strActivitys,strTmp);
                 activityButton.setFID(strTmp);    //设置新的活动ID
                 
                 DBRow dr = activityButton.getData();
                 dr.setDataMode(DBMode.NEW);
                 vec.add(dr);
                 activityButton = null;
             }
        }
        //--------------------------------
        //复制活动的可操作字段---------------
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Select * From COLL_CONFIG_OPERATE_FIELD where FID in (select ID from FLOW_CONFIG_ACTIVITY "
        			+ "where FID in (select ID from FLOW_CONFIG_PROCESS where FLOWPACKAGE in (select ID from FLOW_CONFIG_PACKAGE "
        			+ "where ID='"+strID+"' OR FID='"+strID+"' or FID in(select ID from FLOW_CONFIG_PACKAGE where FID='")
              .append(strID+"')))) order by ID");
        List<Map<String, Object>> retlist7 = userMapper.selectListMapExecSQL(addBuf.toString());
        int length7 = retlist7 != null ? retlist7.size() : 0;
        if (length7 > 0) {
             strTmpMaxNo = getMaxFieldNo("COLL_CONFIG_OPERATE_FIELD","ID",8);
             for (int i = 0; i < length7; i++) {
            	 Map<String, Object> retmap7 = retlist7.get(i);
                 COLL_CONFIG_OPERATE_FIELD operateField = (COLL_CONFIG_OPERATE_FIELD) ReflectionUtil.convertMapToBean(retmap7, COLL_CONFIG_OPERATE_FIELD.class);
                 strMaxNo = getMoreMaxFieldNo(strTmpMaxNo,8,i);
                 
                 operateField.setID(strMaxNo);    //设置ID
                 strTmp = retmap7.get("FID").toString();
                 strTmp = getNewCode(strActivitys,strTmp);
                 operateField.setFID(strTmp);    //设置新的活动ID
                 
                 DBRow dr = operateField.getData();
                 dr.setDataMode(DBMode.NEW);
                 vec.add(dr);
                 operateField = null;
             }
        }
        //-------------------------------
        //复制活动的流程组配置---------------
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Select * From FLOW_CONFIG_ACTIVITY_GROUP where ACTIVITYID in (select ID from FLOW_CONFIG_ACTIVITY "
        			+ "where FID in (select ID from FLOW_CONFIG_PROCESS where  FLOWPACKAGE in (select ID from FLOW_CONFIG_PACKAGE "
        			+ "where ID='"+strID+"' OR FID='"+strID+"' or FID in(select ID from FLOW_CONFIG_PACKAGE where FID='")
              .append(strID+"')))) order by ID");
        List<Map<String, Object>> retlist10 = userMapper.selectListMapExecSQL(addBuf.toString());
        int length10 = retlist10 != null ? retlist10.size() : 0;
        if (length10 > 0) {
             strTmpMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY_GROUP","ID",10);
             for (int i = 0; i < length10; i++) {
            	 Map<String, Object> retmap10 = retlist10.get(i);
                 FLOW_CONFIG_ACTIVITY_GROUP activityGroup = (FLOW_CONFIG_ACTIVITY_GROUP) ReflectionUtil.convertMapToBean(retmap10, FLOW_CONFIG_ACTIVITY_GROUP.class);
                 strMaxNo = getMoreMaxFieldNo(strTmpMaxNo,10,i);
                 
                 activityGroup.setID(strMaxNo);    //设置ID
                 strTmp = retmap10.get("ACTIVITYID").toString();
                 strTmp = getNewCode(strActivitys,strTmp);
                 activityGroup.setACTIVITYID(strTmp);//设置新的活动ID
                 
                 DBRow dr = activityGroup.getData();
                 dr.setDataMode(DBMode.NEW);
                 vec.add(dr);
                 activityGroup =null;
             }
        }
        //--------------------------------
        LOGGER.info("复制流程包：SQL生成结束");
        LOGGER.info("复制流程包：SQL记录数："+String.valueOf(vec.size()));
        return vec;
	}

	@Override
	public boolean copyFlow(String strID) throws Exception {
		boolean retValue = true;
        FLOW_CONFIG_PROCESS[] Process = null;
        FLOW_CONFIG_ACTIVITY[] Activity = null;
        FLOW_CONFIG_PROSESS_GROUP[] Prosess_Group = null;
        FLOW_CONFIG_ACTIVITY_CONNE[] ActivityConne = null;
        FLOW_CONFIG_TIME[] ConfigTime = null;
        FLOW_CONFIG_ACTIVITY_BUTTON[] ActivityButton = null;
        COLL_CONFIG_OPERATE_FIELD[] OperateField = null;
        FLOW_CONFIG_ACTIVITY_GROUP[] ActivityGroup = null;
        
        String strTmp = "";               //临时编号
        String strMaxNo = "";             //最大流水号
        String strProcesss = "";          //流程
        String strActivitys = "";         //活动
        StringBuffer addBuf = new StringBuffer();
        StringBuffer tmpBuf = new StringBuffer();
        
        //--------------------复制流程--------------------//
        addBuf.append("Select * From FLOW_CONFIG_PROCESS where ID ='"+strID+"' order by ID");
        DBSet dbset1 = dbEngine.QuerySQL(addBuf.toString());
        addBuf.delete(0,addBuf.length());//清空
        if (dbset1 != null && dbset1.RowCount() > 0) {
        	Process = new FLOW_CONFIG_PROCESS[dbset1.RowCount()];
            for (int i = 0; i < dbset1.RowCount(); i++){
                Process[i] = new FLOW_CONFIG_PROCESS();
                strMaxNo = getMaxFieldNo("FLOW_CONFIG_PROCESS","ID",10);
                addBuf.append(dbset1.Row(i).Column("ID").getString()+","+strMaxNo+";");
                Process[i].fullData(dbset1.Row(i));
                Process[i].setID(strMaxNo);    //设置流程ID
                if (strMaxNo.length()>=10){strMaxNo=strMaxNo.substring(2,10);}
                tmpBuf.delete(0,tmpBuf.length());//清空
                tmpBuf.append("WF"+strMaxNo);
                Process[i].setIDENTIFICATION(tmpBuf.toString());//设置流程标识
                
                strTmp = dbset1.Row(i).Column("NAME").getString();
                tmpBuf.delete(0,tmpBuf.length());//清空
                tmpBuf.append("复件"+strTmp);
                Process[i].setNAME(tmpBuf.toString());   //设置流程名称
                
                retValue = dbEngine.ExecuteInsert(Process[i].getData());
                
                //删除相关子表的数据...
                dbEngine.ExecuteSQL("delete From FLOW_CONFIG_ACTIVITY where FID ='"+strMaxNo+"'");
                dbEngine.ExecuteSQL("delete From FLOW_CONFIG_PROSESS_GROUP where PROSESSID ='"+strMaxNo+"'");
                dbEngine.ExecuteSQL("delete From FLOW_CONFIG_ACTIVITY_CONNE where FID ='"+strMaxNo+"'");
            }
            Process = null;
            dbset1 = null;//赋空值
         }
         strProcesss = addBuf.toString();
         //--------------------------------
         //复制流程的监控和任务重分配用户组-----
         addBuf.delete(0, addBuf.length());//清空
         addBuf.append("Select * From FLOW_CONFIG_PROSESS_GROUP where PROSESSID ='"+strID)
               .append("' order by ID");
         DBSet dbset2 = dbEngine.QuerySQL(addBuf.toString());
         if (dbset2 != null && dbset2.RowCount() > 0) {
             Prosess_Group = new FLOW_CONFIG_PROSESS_GROUP[dbset2.RowCount()];
             for (int i = 0; i < dbset2.RowCount(); i++){
                 Prosess_Group[i] = new FLOW_CONFIG_PROSESS_GROUP();
                 strMaxNo = getMaxFieldNo("FLOW_CONFIG_PROSESS_GROUP","ID",10);
                 Prosess_Group[i].fullData(dbset2.Row(i));
                 Prosess_Group[i].setID(strMaxNo);    //设置ID

                 strTmp = dbset2.Row(i).Column("PROSESSID").getString();
                 strTmp = getNewCode(strProcesss,strTmp);
                 Prosess_Group[i].setPROSESSID(strTmp);    //设置新的过程ID
                 
                 dbEngine.ExecuteInsert(Prosess_Group[i].getData());
             }
             Prosess_Group = null;
             dbset2 = null;//赋空值
         }
         //--------------------------------
         //复制活动(流程步骤)前得到编号关系-----
         addBuf.delete(0,addBuf.length());//清空
         addBuf.append("Select * From FLOW_CONFIG_ACTIVITY where FID ='"+strID+"' order by ID");
         DBSet dbset31 = dbEngine.QuerySQL(addBuf.toString());
         addBuf.delete(0,addBuf.length());//清空
         if (dbset31 != null && dbset31.RowCount() > 0) {
             strTmp = getMaxFieldNo("FLOW_CONFIG_ACTIVITY","ID",10);
             strMaxNo =strTmp;
             for (int i = 0; i < dbset31.RowCount(); i++){
                   addBuf.append(dbset31.Row(i).Column("ID").getString()+","+strMaxNo).append(";");
                   strMaxNo = getMoreMaxFieldNo(strTmp,10,i);
             
             }
             dbset31=null;//赋空值
         }
         strActivitys = addBuf.toString();
         //--------------------------------
         //复制活动(流程步骤)----------------
         addBuf.delete(0,addBuf.length());//清空
         addBuf.append("Select * From FLOW_CONFIG_ACTIVITY where FID ='"+strID+"' order by ID");
         DBSet dbset3 = dbEngine.QuerySQL(addBuf.toString());
         addBuf.delete(0,addBuf.length());//清空
         if (dbset3 != null && dbset3.RowCount() > 0) {
              Activity = new FLOW_CONFIG_ACTIVITY[dbset3.RowCount()];
              for (int i = 0; i < dbset3.RowCount(); i++){
                   Activity[i] = new FLOW_CONFIG_ACTIVITY();
                   strMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY","ID",10);
                   
                   Activity[i].fullData(dbset3.Row(i));
                   Activity[i].setID(strMaxNo);    //设置活动ID
                   tmpBuf.delete(0,tmpBuf.length());//清空
                   tmpBuf.append("FA"+strMaxNo);
                   Activity[i].setIDENTIFICATION(tmpBuf.toString());//设置活动标识
                   
                   strTmp = dbset3.Row(i).Column("FID").getString();
                   strTmp = getNewCode(strProcesss,strTmp);
                   Activity[i].setFID(strTmp);    //设置新的过程ID
                   //重设置位置信息---
                   strTmp = getXYCSS(strActivitys,Activity[i].getXYCSS());
                   Activity[i].setXYCSS(strTmp);
                   
                   dbEngine.ExecuteInsert(Activity[i].getData());
              }
              Activity = null;
              dbset3 = null;//赋空值
         }
         //--------------------------------
         //复制活动(流程步骤)间的关系----------
         addBuf.delete(0,addBuf.length());//清空
         addBuf.append("Select * From FLOW_CONFIG_ACTIVITY_CONNE where FID ='"+strID)
               .append("' order by ID");
         DBSet dbset4 = dbEngine.QuerySQL(addBuf.toString());
         if (dbset4 != null && dbset4.RowCount() > 0) {
               ActivityConne = new FLOW_CONFIG_ACTIVITY_CONNE[dbset4.RowCount()];
               for (int i = 0; i < dbset4.RowCount(); i++){
                      ActivityConne[i] = new FLOW_CONFIG_ACTIVITY_CONNE();
                      strMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY_CONNE","ID",10);
                      
                      ActivityConne[i].fullData(dbset4.Row(i));
                      ActivityConne[i].setID(strMaxNo);    //设置ID
                      
                      strTmp = dbset4.Row(i).Column("FID").getString();
                      strTmp = getNewCode(strProcesss,strTmp);
                      ActivityConne[i].setFID(strTmp);    //设置新的过程ID
                      
                      strTmp = dbset4.Row(i).Column("CID").getString();
                      strTmp = getNewCode(strActivitys,strTmp);
                      ActivityConne[i].setCID(strTmp);    //设置新的当前活动ID
                      
                      strTmp = dbset4.Row(i).Column("SID").getString();
                      strTmp = getNewCode(strActivitys,strTmp);
                      ActivityConne[i].setSID(strTmp);    //设置新的前趋活动ID
                      
                      strTmp = dbset4.Row(i).Column("EID").getString();
                      strTmp = getNewCode(strActivitys,strTmp);
                      ActivityConne[i].setEID(strTmp);    //设置新的后续活动ID
                      
                      dbEngine.ExecuteInsert(ActivityConne[i].getData());
               }
               ActivityConne = null;
               dbset4 = null;//赋空值
         }
         //--------------------------------
         //复制活动的超时属性-----------------
         addBuf.delete(0,addBuf.length());//清空
         addBuf.append("Select * From FLOW_CONFIG_TIME where FID in (select ID from FLOW_CONFIG_ACTIVITY where FID ='")
               .append(strID+"') order by ID");
         DBSet dbset5 = dbEngine.QuerySQL(addBuf.toString());
         if (dbset5 != null && dbset5.RowCount() > 0) {
              ConfigTime = new FLOW_CONFIG_TIME[dbset5.RowCount()];
              for (int i = 0; i < dbset5.RowCount(); i++){
                  ConfigTime[i] = new FLOW_CONFIG_TIME();
                  strMaxNo = getMaxFieldNo("FLOW_CONFIG_TIME","ID",8);
                  
                  ConfigTime[i].fullData(dbset5.Row(i));
                  ConfigTime[i].setID(strMaxNo);    //设置ID
                  
                  strTmp = dbset5.Row(i).Column("FID").getString();
                  strTmp = getNewCode(strActivitys,strTmp);
                  ConfigTime[i].setFID(strTmp);    //设置新的活动ID
                  
                  dbEngine.ExecuteInsert(ConfigTime[i].getData());
              }
              ConfigTime = null;
              dbset5 = null;//赋空值
         }
         //--------------------------------
         //复制活动的按钮--------------------
         addBuf.delete(0,addBuf.length());//清空
         addBuf.append("Select * From FLOW_CONFIG_ACTIVITY_BUTTON where FID in (select ID "
         			 + "from FLOW_CONFIG_ACTIVITY where FID ='"+strID+"') order by ID");
         DBSet dbset6 = dbEngine.QuerySQL(addBuf.toString());
         if (dbset6 != null && dbset6.RowCount() > 0) {
              ActivityButton = new FLOW_CONFIG_ACTIVITY_BUTTON[dbset6.RowCount()];
              for (int i = 0; i < dbset6.RowCount(); i++){
                  ActivityButton[i] = new FLOW_CONFIG_ACTIVITY_BUTTON();
                  strMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY_BUTTON","ID",12);
                  
                  ActivityButton[i].fullData(dbset6.Row(i));
                  ActivityButton[i].setID(strMaxNo);    //设置ID
                  
                  strTmp = dbset6.Row(i).Column("FID").getString();
                  strTmp = getNewCode(strActivitys,strTmp);
                  ActivityButton[i].setFID(strTmp);    //设置新的活动ID
                  
                  dbEngine.ExecuteInsert(ActivityButton[i].getData());
              }
              ActivityButton = null;
              dbset6 = null;//赋空值
         }
         //--------------------------------
         //复制活动的可操作字段---------------
         addBuf.delete(0,addBuf.length());//清空
         addBuf.append("Select * From COLL_CONFIG_OPERATE_FIELD where FID in (select ID "
         			 + "from FLOW_CONFIG_ACTIVITY where FID ='"+strID+"') order by ID");
         DBSet dbset7 = dbEngine.QuerySQL(addBuf.toString());
         if (dbset7 != null && dbset7.RowCount() > 0) {
              OperateField = new COLL_CONFIG_OPERATE_FIELD[dbset7.RowCount()];
              for (int i = 0; i < dbset7.RowCount(); i++){
                  OperateField[i] = new COLL_CONFIG_OPERATE_FIELD();
                  strMaxNo = getMaxFieldNo("COLL_CONFIG_OPERATE_FIELD","ID",8);
                  
                  OperateField[i].fullData(dbset7.Row(i));
                  OperateField[i].setID(strMaxNo);    //设置ID
                  
                  strTmp = dbset7.Row(i).Column("FID").getString();
                  strTmp = getNewCode(strActivitys,strTmp);
                  OperateField[i].setFID(strTmp);    //设置新的活动ID
                  
                  dbEngine.ExecuteInsert(OperateField[i].getData());
              }
              OperateField = null;      
              dbset7 = null;//赋空值
         }
         //--------------------------------
         //复制活动的流程组配置---------------
         addBuf.delete(0,addBuf.length());//清空
         addBuf.append("Select * From FLOW_CONFIG_ACTIVITY_GROUP where ACTIVITYID in (select ID "
           			 + "from FLOW_CONFIG_ACTIVITY where FID ='"+strID+"') order by ID");
         DBSet dbset10 = dbEngine.QuerySQL(addBuf.toString());
         if (dbset10 != null && dbset10.RowCount() > 0){
              ActivityGroup = new FLOW_CONFIG_ACTIVITY_GROUP[dbset10.RowCount()];
              for (int i = 0; i < dbset10.RowCount(); i++){
                  ActivityGroup[i] = new FLOW_CONFIG_ACTIVITY_GROUP();
                  strMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY_GROUP","ID",10);
                  
                  ActivityGroup[i].fullData(dbset10.Row(i));
                  ActivityGroup[i].setID(strMaxNo);    //设置ID
                  
                  strTmp = dbset10.Row(i).Column("ACTIVITYID").getString();
                  strTmp = getNewCode(strActivitys,strTmp);
                  ActivityGroup[i].setACTIVITYID(strTmp);//设置新的活动ID
                  
                  dbEngine.ExecuteInsert(ActivityGroup[i].getData());
              }
              ActivityGroup = null;
              dbset10 = null;//赋空值
         }
         //--------------------------------
         return retValue;
	}

	@Override
	public boolean copyActivity(String strID) throws Exception {
	       boolean retValue = true;
	       FLOW_CONFIG_ACTIVITY[] Activity = null;
	       FLOW_CONFIG_TIME[] ConfigTime = null;
	       FLOW_CONFIG_ACTIVITY_BUTTON[] ActivityButton = null;
	       COLL_CONFIG_OPERATE_FIELD[] OperateField = null;
	       FLOW_CONFIG_ACTIVITY_GROUP[] ActivityGroup = null;
	       
	       StringBuffer addBuf = new StringBuffer();
	       StringBuffer tmpBuf = new StringBuffer();
	       
	       String strTmp = ""; //临时编号
	       String strMaxNo = ""; //最大流水号
	       String strActivitys = ""; //活动
	       
	       //复制活动(流程步骤)----------------
	       addBuf.append("Select * From FLOW_CONFIG_ACTIVITY where ID ='"+strID+"' order by ID");
	       DBSet dbset1 = dbEngine.QuerySQL(addBuf.toString());
	       addBuf.delete(0,addBuf.length());//清空
	       if (dbset1 != null && dbset1.RowCount() > 0) {
	           Activity = new FLOW_CONFIG_ACTIVITY[dbset1.RowCount()];
	           for (int i = 0; i < dbset1.RowCount(); i++) {
	             Activity[i] = new FLOW_CONFIG_ACTIVITY();
	             strMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY", "ID", 10);
	             addBuf.append(dbset1.Row(i).Column("ID").getString()+","+strMaxNo+";");
	             Activity[i].fullData(dbset1.Row(i));
	             Activity[i].setID(strMaxNo); //设置活动ID
	             
	             tmpBuf.delete(0,tmpBuf.length());//清空
	             tmpBuf.append("FA"+strMaxNo);
	             Activity[i].setIDENTIFICATION(tmpBuf.toString()); //设置活动标识
	             
	             strTmp = dbset1.Row(i).Column("NAME").getString();
	             tmpBuf.delete(0,tmpBuf.length());//清空
	             tmpBuf.append("复件"+strTmp);
	             Activity[i].setNAME(tmpBuf.toString()); //设置活动名称
	             Activity[i].setXYCSS("n1/"+strMaxNo+"/步骤//3/600px/310px");
	             
	             dbEngine.ExecuteInsert(Activity[i].getData());
	           }
	           Activity = null;
	           dbset1 = null;//赋空值
	       }
	       strActivitys = addBuf.toString();
	       //--------------------------------
	       //复制活动的超时属性----------------
	       addBuf.delete(0,addBuf.length());//清空
	       addBuf.append("Select * From FLOW_CONFIG_TIME where FID ='"+strID+"' order by ID");
	       DBSet dbset2 = dbEngine.QuerySQL(addBuf.toString());
	       if (dbset2 != null && dbset2.RowCount() > 0) {
	           ConfigTime = new FLOW_CONFIG_TIME[dbset2.RowCount()];
	           for (int i = 0; i < dbset2.RowCount(); i++) {
	             ConfigTime[i] = new FLOW_CONFIG_TIME();
	             strMaxNo = getMaxFieldNo("FLOW_CONFIG_TIME", "ID", 8);
	             
	             ConfigTime[i].fullData(dbset2.Row(i));
	             ConfigTime[i].setID(strMaxNo); //设置ID
	             
	             strTmp = dbset2.Row(i).Column("FID").getString();
	             strTmp = getNewCode(strActivitys, strTmp);
	             ConfigTime[i].setFID(strTmp); //设置新的活动ID
	             
	             dbEngine.ExecuteInsert(ConfigTime[i].getData());
	           }
	           ConfigTime = null;
	           dbset2 = null;//赋空值
	       }
	       //--------------------------------
	       //复制活动的按钮--------------------
	       addBuf.delete(0,addBuf.length());//清空
	       addBuf.append("Select * From FLOW_CONFIG_ACTIVITY_BUTTON where FID ='"+strID+"' order by ID");
	       DBSet dbset3 = dbEngine.QuerySQL(addBuf.toString());
	       if (dbset3 != null && dbset3.RowCount() > 0) {
	           ActivityButton = new FLOW_CONFIG_ACTIVITY_BUTTON[dbset3.RowCount()];
	           for (int i = 0; i < dbset3.RowCount(); i++) {
	        	   ActivityButton[i] = new FLOW_CONFIG_ACTIVITY_BUTTON();
	        	   strMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY_BUTTON", "ID", 12);
	        	   
	        	   ActivityButton[i].fullData(dbset3.Row(i));
	        	   ActivityButton[i].setID(strMaxNo); //设置ID
	        	   
	        	   strTmp = dbset3.Row(i).Column("FID").getString();
	        	   strTmp = getNewCode(strActivitys, strTmp);
	        	   ActivityButton[i].setFID(strTmp); //设置新的活动ID
	        	   
	        	   dbEngine.ExecuteInsert(ActivityButton[i].getData());
	           }
	           ActivityButton = null;
	           dbset3 = null;//赋空值
	       }
	       //--------------------------------
	       //复制活动的可操作字段---------------
	       addBuf.delete(0,addBuf.length());//清空
	       addBuf.append("Select * From COLL_CONFIG_OPERATE_FIELD where FID ='"+strID+"' order by ID");
	       DBSet dbset4 = dbEngine.QuerySQL(addBuf.toString());
	       if (dbset4 != null && dbset4.RowCount() > 0) {
	           OperateField = new COLL_CONFIG_OPERATE_FIELD[dbset4.RowCount()];
	           for (int i = 0; i < dbset4.RowCount(); i++) {
	             OperateField[i] = new COLL_CONFIG_OPERATE_FIELD();
	             strMaxNo = getMaxFieldNo("COLL_CONFIG_OPERATE_FIELD", "ID", 8);
	             
	             OperateField[i].fullData(dbset4.Row(i));
	             OperateField[i].setID(strMaxNo); //设置ID
	             
	             strTmp = dbset4.Row(i).Column("FID").getString();
	             strTmp = getNewCode(strActivitys, strTmp);
	             OperateField[i].setFID(strTmp); //设置新的活动ID
	             
	             dbEngine.ExecuteInsert(OperateField[i].getData());
	           }
	           OperateField = null;
	           dbset4 = null;//赋空值
	       }
	       //--------------------------------
	       //复制活动的流程组配置---------------
	       addBuf.delete(0,addBuf.length());//清空
	       addBuf.append("Select * From FLOW_CONFIG_ACTIVITY_GROUP where ACTIVITYID ='"+strID+"' order by ID");
	       DBSet dbset7 = dbEngine.QuerySQL(addBuf.toString());
	       if (dbset7 != null && dbset7.RowCount() > 0) {
	           ActivityGroup = new FLOW_CONFIG_ACTIVITY_GROUP[dbset7.RowCount()];
	           for (int i = 0; i < dbset7.RowCount(); i++) {
	             ActivityGroup[i] = new FLOW_CONFIG_ACTIVITY_GROUP();
	             strMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY_GROUP", "ID", 10);
	             
	             ActivityGroup[i].fullData(dbset7.Row(i));
	             ActivityGroup[i].setID(strMaxNo); //设置ID
	             
	             strTmp = dbset7.Row(i).Column("ACTIVITYID").getString();
	             strTmp = getNewCode(strActivitys, strTmp);
	             ActivityGroup[i].setACTIVITYID(strTmp); //设置新的活动ID
	             
	             dbEngine.ExecuteInsert(ActivityGroup[i].getData());
	           }
	           ActivityGroup = null;
	           dbset7 = null;//赋空值
	       }
	       //--------------------------------
	       return retValue;
	}

	@Override
	public boolean activitynew(String strID) throws Exception {
        String xycss ="";
        String strMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY", "ID", 10);
        xycss = "n1/"+strMaxNo+"/未设置活动//3/200px/200px";
        String strSql = "Insert into FLOW_CONFIG_ACTIVITY(ID,FID,IDENTIFICATION,NAME,DESC1,TYPE,ORDER1,XYCSS) values ('"
        				+strMaxNo+"','"+strID+"','FA"+strMaxNo+"','未设置活动','','3','0','"+xycss+"')";
        Integer retInt = userMapper.insertExecSQL(strSql);
        if (retInt != null && retInt > 0) {
        	return true;
        }
        return false;
	}

	@Override
	public boolean activitynew1(String strflowcno, String strflowsno) throws Exception {
        String xycss ="";
        String strMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY", "ID", 10);
        String strName = getProcessTrueName(strflowsno);
        strName = "子流程:"+strName;
        xycss = "n1/"+strMaxNo+"/"+strName+"//5/200px/300px";
        String strSql = "Insert into FLOW_CONFIG_ACTIVITY(ID,FID,IDENTIFICATION,NAME,DESC1,TYPE,ORDER1,XYCSS) values ('"
        				+strMaxNo+"','"+strflowcno+"','FA"+strMaxNo+"','"+strName+"','"+strflowsno+"','5','9999','"+xycss+"')";
        Integer retInt = userMapper.insertExecSQL(strSql);
        if (retInt != null && retInt > 0) {
        	return true;
        }
        return false;
	}

	@Override
	public Package[] getPackageCustomList(String strWhere) throws Exception {
	     Package[] returnValue = null;
	     //得到流程引擎独立运转的单位编号位数
	     StringBuffer addBuf = new StringBuffer();
	     if (strWhere.length()>0) {
	        addBuf.append("Select * From FLOW_CONFIG_PACKAGE where "+strWhere+" order by ID");
	     } else {
	        addBuf.append("Select * From FLOW_CONFIG_PACKAGE order by ID");
	     }
	     List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	     int length = retlist != null ? retlist.size() : 0;
	     if (length > 0) {
	         returnValue = new Package[length];
	         for (int i = 0; i < length; i++) {
	           Map<String, Object> retmap = retlist.get(i);
	           returnValue[i] = (Package) ReflectionUtil.convertMapToBean(retmap, Package.class);
	         }
	     }
	    return returnValue;
	}

	@Override
	public FLOW_CONFIG_ACTIVITY_CONNE getActivityID(String id) throws Exception {
		FLOW_CONFIG_ACTIVITY_CONNE bp = null;
		StringBuffer addBuf = new StringBuffer();
		try {
			addBuf.append("Select * From FLOW_CONFIG_ACTIVITY_CONNE Where ID='"+id.trim()+"'");
			Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
			if (retmap == null || retmap.size() < 1) {
				return null;
			} else {// retmap != null
				bp = (FLOW_CONFIG_ACTIVITY_CONNE) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_ACTIVITY_CONNE.class);
         	}
       	} catch (Exception e) {
       		e.printStackTrace();
       		return null;
       	}
		return bp;
	}

	@Override
	public String getActivityID(String cid, String eid) throws Exception {
		String strre = "";
        StringBuffer addBuf = new StringBuffer();
        cid=cid.replaceAll("Z","");
        eid=eid.replaceAll("Z","");
        try {
          addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY_CONNE "
          			  + "Where (SID='"+cid+"' and CID='"+eid+"') or (EID='"+cid+"' and CID='"+eid+"')");
          List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
          int length = retlist != null ? retlist.size() : 0;
          if (length == 0) {
        	  return "";
          } else {// length > 0
        	  if (retlist.get(0) != null && retlist.get(0).get("ID") != null) {
        		  strre = retlist.get(0).get("ID").toString();
        	  }
          }
        } catch (Exception e) {
        	e.printStackTrace();
        	return "";
        }
        return strre;
	}

	@Override
	public FunctionMessage updataActivity(String strID, String strName, String StrDesc1, String strwhere1, 
			String type, String ISNEED, String ISATT, String strFormfields) throws Exception {
	      StringBuffer addBuf = new StringBuffer();
	      FunctionMessage fm = new FunctionMessage(1);
	      try {
	           addBuf.append("UPDATE FLOW_CONFIG_ACTIVITY_CONNE SET NAME='"+strName+"',DESC1='")
	                 .append(StrDesc1+"',WHERE1='"+convertQuotationSingleToDouble(strwhere1))
	                 .append("',TYPE ='"+type+"',ISNEED='"+ISNEED+"',ISATT='")
	                 .append(ISATT+"' where ID ='"+strID+"'");
	           userMapper.updateExecSQL(addBuf.toString());
	           //删除接收类型为某一步的处理人时的步骤列表
	           deleteActivity(strID);
	           //增加接收类型为某一步的处理人时的步骤列表
	           addActivity(strFormfields, type, strID);

	          fm.setResult(true);
	          fm.setMessage("保存成功");
	       }
	      catch (Exception e) {
	        fm.setResult(false);
	        fm.setMessage("调用方法UpdataActivity出现异常" + e.toString());
	        return fm;
	      }
	      flowControl.initHashtable();
	      return fm;
	}

	@Override
	public String activity_AllList(String Lst_Name, String Is_Enabled, String strFlowID) throws Exception {
	     StringBuffer addBuf = new StringBuffer();
	     addBuf.append("Select ID,NAME,FID from FLOW_CONFIG_ACTIVITY where TYPE<>'1' and TYPE<>'2' "
	     			 + "and FID in (select ID from FLOW_CONFIG_PROCESS "
	     			 + "where FLOWPACKAGE in (select FLOWPACKAGE from FLOW_CONFIG_PROCESS where ID ='")
	           .append(strFlowID+"')) order by ORDER1");
	     List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	     int length = retlist != null ? retlist.size() : 0;
	     addBuf.delete(0,addBuf.length());//清空
	     addBuf.append("<select name='"+Lst_Name+"'  style='width:242px;height:150px'  size=15 multiple ")
	           .append(Is_Enabled+">\r\n");
	     if (length > 0) {
	         for (int i = 0; i < length; i++) {
	        	 Map<String, Object> retmap = retlist.get(i);
	             addBuf.append("<option value='"+retmap.get("ID").toString())
	                   .append("'>"+getProcessTrueName(retmap.get("FID").toString()))
	                   .append("---"+getGroupTrueName1(retmap.get("ID").toString()))
	                   .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
	                   .append(retmap.get("FID").toString()+"</option>\r\n");
	         }
	     }
	     addBuf.append("</select>\r\n");
	     return addBuf.toString();
	}

	@Override
	public String flow_Activity_List(String strId, String Lst_Name, String Is_Enabled) throws Exception {
	     StringBuffer addBuf = new StringBuffer();
	     addBuf.append("Select * from FLOW_CONFIG_ACTIVITY where ID in ("
	     			 + "Select AAID FROM FLOW_CONFIG_CONN_AUTHOR Where CONNID = '"+strId+"') order by ID");
	     List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	     int length = retlist != null ? retlist.size() : 0;
	     addBuf.delete(0, addBuf.length());//清空
	     addBuf.append("<select name='"+Lst_Name+"' style='width:242px;height:150px' size=15 multiple ")
	           .append(Is_Enabled+">\r\n");
	     if (length > 0) {
	         for (int i = 0; i < length; i++) {
	        	 Map<String, Object> retmap = retlist.get(i);
	             addBuf.append("<option value='"+retmap.get("ID").toString())
	                   .append("'>"+getProcessTrueName(retmap.get("FID").toString()))
	                   .append("---"+getGroupTrueName1(retmap.get("ID").toString()))
	                   .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
	                   .append(retmap.get("FID").toString()+"</option>\r\n");
	         }
	     }
	     addBuf.append("</select>\r\n");
	     return addBuf.toString();
	}

	@Override
	public String flow_Activity_List1(String strId, String Lst_Name, String Is_Enabled) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select * from FLOW_CONFIG_ACTIVITY where TYPE<>'1' and TYPE<>'2' and FID  ='")
	          .append(strId+"' order by ORDER1");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("<select name='"+Lst_Name+"'  style='width:242px;height:150px'  size=15 multiple ")
	          .append(Is_Enabled+">\r\n");
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = retlist.get(i);
	            addBuf.append("<option value='"+retmap.get("ID").toString())
	                  .append("'>"+getProcessTrueName(retmap.get("FID").toString()))
	                  .append("---"+getGroupTrueName1(retmap.get("ID").toString()))
	                  .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
	                  .append(retmap.get("FID").toString()+"</option>\r\n");
	        }
	    }
	    addBuf.append("</select>\r\n");
	    return addBuf.toString();
	}

	@Override
	public String getGroupTrueName1(String strID) throws Exception {
		  StringBuffer addBuf = new StringBuffer();
	      addBuf.append("Select NAME from FLOW_CONFIG_ACTIVITY where ID = '"+strID+"'");
	      Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	      addBuf.delete(0,addBuf.length());//清空
	      if (retmap != null && retmap.size() > 0) {
	    	  addBuf.append(retmap.get("NAME").toString());
	      }
	      return addBuf.toString();
	}

	@Override
	public String getProcessTrueName(String strGroupID) throws Exception {
		StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select NAME from FLOW_CONFIG_PROCESS where ID = '"+strGroupID+"'");
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    if (retmap != null && retmap.size() > 0) {
	       addBuf.append(retmap.get("NAME").toString());
	    }
	    return addBuf.toString();
	}

	@Override
	public String getPackageName(String strID) throws Exception {
		StringBuffer addBuf = new StringBuffer();
		String strResult = "";
		int i = 0;
		while(!strID.equals("000") && i<10) {
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append("Select FID,NAME from FLOW_CONFIG_PACKAGE where ID = '"+strID+"'");
	        Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	        if (retmap != null && retmap.size()>0) {
	        	addBuf.delete(0,addBuf.length());//清空
	        	addBuf.append(retmap.get("NAME").toString()+"→"+strResult);
	        	strResult = addBuf.toString();
	        	strID = retmap.get("FID").toString();
	        }
	        i++;
		}
		return strResult;
	}

	@Override
	public String getFPackageName(String strID) throws Exception {
	     StringBuffer addBuf = new StringBuffer();
	     String strResult = "";
	     int i = 0;
	     addBuf.append("Select FLOWPACKAGE from FLOW_CONFIG_PROCESS where ID = '"+strID+"'");
	     Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	     if (retmap != null && retmap.size() > 0) {
	    	 strID = retmap.get("FLOWPACKAGE").toString();
	     }
	     while(!strID.equals("000") && i<10) {
	          addBuf.delete(0,addBuf.length());//清空
	          addBuf.append("Select FID,NAME from FLOW_CONFIG_PACKAGE where ID = '"+strID+"'");
	          Map<String, Object> retmap1 = userMapper.selectMapExecSQL(addBuf.toString());
	          if (retmap1 != null && retmap1.size() > 0) {
	              addBuf.delete(0,addBuf.length());//清空
	              addBuf.append(retmap1.get("NAME").toString()+"→"+strResult);
	              strResult = addBuf.toString();
	              strID = retmap1.get("FID").toString();
	          }
	          i++;
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
	private String getMaxFieldNo(String TableName, String FieldName, int FieldLen) {	
        String MaxNo="";
        int LenMaxNo=0;
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("SELECT MAX("+FieldName+") AS MaxNo FROM "+TableName);
        try {
        	DBSet dbset = dbEngine.QuerySQL(addBuf.toString());
        	if (dbset != null && dbset.RowCount() > 0) {
    			MaxNo = dbset.Row(0).Column("MaxNo").getString();
    			if (MaxNo.length() > 0) {
    				MaxNo = String.valueOf(Integer.parseInt(MaxNo)+1);
    				LenMaxNo = MaxNo.length();
    				addBuf.delete(0, addBuf.length());// 清空
    				addBuf.append("0000000000000000000000000"+MaxNo);
    				MaxNo=addBuf.toString();
    			} else {
    				MaxNo="00000000000000000000000001";
    				LenMaxNo =1;
    			}
        		dbset = null;//赋空值
        	}
        	MaxNo=MaxNo.substring(25-FieldLen+LenMaxNo);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        return MaxNo;
	}

	/**
	 * 功能或作用：取出最大的记录流水号(多记录同时插入时使用)
	 * @param StartCode 初始编号
	 * @param FieldLen 数据库字段长度
	 * @param cnum 当前累加数
	 * @Return MaxNo 执行后返回一个MaxNo字符串
	 */
	private String getMoreMaxFieldNo(String StartCode, int FieldLen, int cnum) {
		String MaxNo="";
		int LenMaxNo = 0;
		try {
			if (StartCode.length() > 0) {
				MaxNo = String.valueOf(Integer.parseInt(StartCode) + 1 + cnum);
	            LenMaxNo = MaxNo.length();
	            StringBuffer  addBuf = new   StringBuffer();
	            addBuf.append("0000000000000000000000000"+MaxNo);
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
	 * 功能或作用：增加FLOW_CONFIG_PROCESS_GROUP活动流程组表
	 * @param formfields 选择的列表(流程用户组)
	 * @return returnValue 返回值
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean addProcessGroup(String formfields, String formfields1, String processid) throws Exception {
	      int i = 0;
	      int addNum = 0;
	      String strIsDept="0";
	      String strCode="";
	      List arrList_Process_Id = new ArrayList();
	      List arrList_Process_Id1 = new ArrayList();
	      String strYid = "";
	      String strid = "";
	      StringBuffer addBuf = new StringBuffer();
	      Vector mVec = new Vector();  //存放所有SQL语句
	      
	      arrList_Process_Id = getArrayList(formfields, "#");//","
	      arrList_Process_Id1 = getArrayList(formfields1, "#");//","
	      strYid = getMaxFieldNo("FLOW_CONFIG_PROSESS_GROUP","ID",10);
	      for (i = 0; i < arrList_Process_Id .size(); i++) {
	          List isdept = getArrayList(arrList_Process_Id.get(i).toString().trim()+";",";");
	          strCode = isdept.get(0).toString().trim();
	          strIsDept = isdept.get(1).toString().trim();
	          strid = getMoreMaxFieldNo(strYid,10,addNum);
	          
	          addBuf.delete(0,addBuf.length());//清空
	          addBuf.append("Insert into FLOW_CONFIG_PROSESS_GROUP(ID,PROSESSID,GROUPID,TYPE,ISDEPT) values ('")
	              	.append(strid+"','"+processid+"','"+strCode+"','1','")
	              	.append(strIsDept+"')");
	          mVec.add(addBuf.toString());
	          addNum ++;
	        }
	        for (i = 0; i < arrList_Process_Id1 .size(); i++) {
	            List isdept1 = getArrayList(arrList_Process_Id1.get(i).toString().trim()+";", ";");
	            strCode = isdept1.get(0).toString().trim();
	            strIsDept = isdept1.get(1).toString().trim();
	            
	            strid = getMoreMaxFieldNo(strYid,10,addNum);
	            addBuf.delete(0,addBuf.length());//清空
	            addBuf.append("Insert into FLOW_CONFIG_PROSESS_GROUP(ID,PROSESSID,GROUPID,TYPE,ISDEPT) values ('")
	            	  .append(strid+"','"+processid+"','"+strCode+"','2','")
	              .append(strIsDept+"')");
	          mVec.add(addBuf.toString());
	          addNum ++;
	        }
	        //将所有SQL装入数组
	        String[] sqls = new String[mVec.size()];
	        for(int j=0;j<mVec.size();j++){
	            sqls[j] = (String)mVec.get(j);
	        }
	        //执行相关sql
	        return dbEngine.ExecuteSQLs(sqls); 
	}

	/**
	* 功能:删除流程基本属性
	* @return FunctionMessage
	*/
	private FunctionMessage deleteProcess(String ID) {
	     StringBuffer addBuf = new StringBuffer();
	     FunctionMessage fm = new FunctionMessage(1);
	     try {
	    	 addBuf.append("Delete From FLOW_CONFIG_PROSESS_GROUP where PROSESSID='"+ID+"'");
	    	 userMapper.deleteExecSQL(addBuf.toString());
	         fm.setResult(true);
	         fm.setMessage("流程基本属性【" + ID + "】已经删除");
	     } catch (Exception e) {
	    	 e.printStackTrace();
	    	 fm.setResult(false);
	    	 fm.setMessage("调用方法DeleteProcess出现异常" + e.toString());
	     }
	     return fm;
	}

	/**
	* 功能：根据流程用户组编号得到流程用户组的名称(区分流程引擎流程组和系统管理角色)
	* @param strGroupID用户组编号
	* @return returnValue 返回流程用户组名称
	 * @throws Exception 
	*/
	private String getFlowGroupTrueName(String strGroupID) throws Exception {
        StringBuffer addBuf = new StringBuffer();
        //查找当前用户是只能监控本部门的流程
        addBuf.append("Select ROLENAME from BPIP_ROLE where ROLEID ="+strGroupID);
        Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
        addBuf.delete(0,addBuf.length());//清空
        if (retmap != null && retmap.size()>0) {
            addBuf.append(retmap.get("ROLENAME").toString());
        }
        return addBuf.toString();
	}

	/**
	 * 功能或作用：根据原编号得到新编号(复制流程时使用)
	 * @param strCodes 查找的字符串
	 * @param strCode 原来的编号
	 * @throws Exception 
	 * @Return returnCode 返回新编号
	 */
	@SuppressWarnings("rawtypes")
	private String getNewCode(String strCodes,String strCode) throws Exception {
        String returnCode = "";
        String strTmp = "";
        List arrList = null;
        List arrList1 = null;
        arrList = getArrayList(strCodes,";");
        for (int i=0; i<arrList.size();i++) {
        	strTmp = arrList.get(i).toString();
        	arrList1 = getArrayList(strTmp+",",",");
        	if (arrList1.get(0).toString().equals(strCode)) {
        		returnCode = arrList1.get(1).toString();
        		break;
        	}
        }
        return returnCode;
	}

	/**
	 * 功能或作用：根据原编号得到新编号关联信息(复制流程时使用)
	 * @param strCodes 查找的字符串
	 * @param strCode 原来的编号串
	 * @throws Exception  
	 * @Return returnCode 返回新编号串
	 */
	private String getXYCSS(String strCodes,String strCode) throws Exception {
          String returnCode = strCode;
          String strTmp = "";
          List<Object> arrList = getArrayList(strCodes,";");
          for (int i=0; i<arrList.size();i++) {
	          strTmp = arrList.get(i).toString();
	          List<Object> arrList1 = getArrayList(strTmp+",",",");
	          returnCode = returnCode.replaceAll(
	        		  arrList1.get(0).toString(), arrList1.get(1).toString());
          }
          return returnCode;
	}

	/**
	 * 功能或作用：增加FLOW_CONFIG_CONN_AUTHOR活动流程活动表
	 * @param formfields 选择的列表(流程活动)
	 * @return returnValue 返回值
	 * @throws Exception 
	 */
	private boolean addActivity(String formfields, String type,String ConnID) throws Exception {
	      int i = 0;
	      String strYid ="";
	      String strid ="";
	      StringBuffer addBuf = new StringBuffer();
	      strYid = getMaxFieldNo("FLOW_CONFIG_CONN_AUTHOR","ID",8);
	      Vector<Object> mVec = new Vector<Object>();  //存放所有SQL语句
	      List<Object> arrList_Process_Id = getArrayList(formfields,"#");
	      for (i = 0; i < arrList_Process_Id .size(); i++) {
	         strid = getMoreMaxFieldNo(strYid,8,i);
	         addBuf.delete(0,addBuf.length());//清空
	         addBuf.append("Insert into FLOW_CONFIG_CONN_AUTHOR(ID,CONNTYPE,CONNID,AAID) values ('")
	               .append(strid+"','"+type+"','"+ConnID+"','"+arrList_Process_Id.get(i).toString().trim())
	               .append("')");
	         mVec.add(addBuf.toString());
	      }
	      //将所有SQL装入数组
	      String[] sqls = new String[mVec.size()];
	      for (int j=0; j<mVec.size(); j++) {
	            sqls[j] = (String) mVec.get(j);
	            //执行相关sql
	            userMapper.insertExecSQL(sqls[j]);
	      }
	      return true;
	}

	/**
	* 删除流程活动
	* @return FunctionMessage
	*/
	private FunctionMessage deleteActivity(String ID) {
	    StringBuffer addBuf = new StringBuffer();
	    FunctionMessage fm = new FunctionMessage(1);
	    try {
	        addBuf.append("Delete From FLOW_CONFIG_CONN_AUTHOR where CONNID='"+ID+"'");
	        userMapper.deleteExecSQL(addBuf.toString());
	        fm.setResult(true);
	        fm.setMessage("删除成功");
	    } catch (Exception e) {
	      fm.setResult(false);
	      fm.setMessage("调用方法DeleteActivity出现异常" + e.toString());
	      return fm;
	    }
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

}