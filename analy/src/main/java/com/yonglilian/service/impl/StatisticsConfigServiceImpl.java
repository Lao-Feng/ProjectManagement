package com.yonglilian.service.impl;

import com.yonglilian.analyseengine.mode.*;
import com.yonglilian.service.StatisticsConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBServer;
import zr.zrpower.common.util.FunctionMessage;
import zr.zrpower.common.util.ReflectionUtil;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.UserMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: 数据统计EJB服务
 * </p>Description:数据统计函数库</p>
 * @author lwk
 * 
 */
@Service
public class StatisticsConfigServiceImpl implements StatisticsConfigService {
	/** The StatisticsConfigServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsConfigServiceImpl.class);
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
	/** 数据库引擎. */
	private DBEngine dbEngine;
	static private int clients = 0;

	/**
	 * 构造方法
	 */
	public StatisticsConfigServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
		}
		clients = 1;
	}

	@Override
	public ANALYSE_STATISTICS_MAIN[] getStatisticsMainList() throws Exception {
        ANALYSE_STATISTICS_MAIN[] retValue = null;
        String execSQL = "Select * From ANALYSE_STATISTICS_MAIN Order by ID";
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(execSQL);
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
        	retValue = new ANALYSE_STATISTICS_MAIN[length];
            for (int i = 0; i < length; i++) {
            	Map<String, Object> retMap = retlist.get(i);
            	retValue[i] = (ANALYSE_STATISTICS_MAIN) ReflectionUtil.convertMapToBean(retMap, ANALYSE_STATISTICS_MAIN.class);
            }
        }
        return retValue;
	}

	@Override
	public ANALYSE_STATISTICS_MAIN[] getANALYSE_STATISTICS_MAIN(String ID) throws Exception {
        ANALYSE_STATISTICS_MAIN[] retValue = null;
        String execSQL = "Select * From ANALYSE_STATISTICS_MAIN where ID='"+ID+"'";
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(execSQL);
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
        	retValue = new ANALYSE_STATISTICS_MAIN[length];
            for (int i = 0; i < length; i++) {
            	Map<String, Object> retMap = retlist.get(i);
            	retValue[i] = (ANALYSE_STATISTICS_MAIN) ReflectionUtil.convertMapToBean(retMap, ANALYSE_STATISTICS_MAIN.class);
            }
        }
        return retValue;
	}

	@Override
	public Map<String, Object> getTableEnumList() throws Exception {
		Map<String, Object> retValue = new HashMap<String, Object>();
        String execSQL = "Select TABLEID,CHINESENAME From BPIP_TABLE";
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(execSQL);
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
            	Map<String, Object> retMap = retlist.get(i);
            	retValue.put(retMap.get("TABLEID").toString(), retMap.get("CHINESENAME").toString());
            }
        }
        return retValue;
	}

	@Override
	public String[][] GetTableList() throws Exception {
        String[][] result = null;
        String strSQL = "Select TableNAME,CHINESENAME From BPIP_Table where TABLETYPE='2' or TABLETYPE='3' order by CHINESENAME";
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
            result = new String[length][2];
            for (int i = 0; i < length; i++) {
            	Map<String, Object> retMap = retlist.get(i);
                result[i][0] = retMap.get("TableNAME").toString();
                result[i][1] = retMap.get("CHINESENAME").toString();
            }
        }
        return result;
	}

	@Override
	public List<Map<String, Object>> getTableList1(String StrWhere) throws Exception {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String execSQL = "Select TABLENAME,CHINESENAME from BPIP_TABLE where "+StrWhere+" order by TABLEID";
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(execSQL);
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
        	for (int i = 0; i < length; i++) {
        		Map<String, Object> retMap = retlist.get(i);
	            String[] saTmp = new String[2];
	            saTmp[0] = retMap.get("TABLENAME").toString();
	            saTmp[1] = retMap.get("CHINESENAME").toString();
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
	public String[][] getTableFieldList(String strID) throws Exception {
        String[][] result = null;
        String execSQL = "Select FIELDNAME,CHINESENAME From BPIP_FIELD where FIELDTYPE<>'3' and FIELDTYPE<>'7'"
        			   + " and TABLEID in (select TABLEID from ANALYSE_MAIN where ID='" + strID + "')";
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(execSQL);
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
            result = new String[length][2];
            for (int i = 0; i < length; i++) {
            	Map<String, Object> retMap = retlist.get(i);
                result[i][0] = retMap.get("FIELDNAME").toString();
                result[i][1] = retMap.get("CHINESENAME").toString();
            }
        }
        return result;
	}

	@Override
	public String[][] getTableFieldList1(String strID) throws Exception {
        String[][] result = null;
        String execSQL = "Select FIELDTYPE,FIELDID,CHINESENAME From BPIP_FIELD where FIELDTYPE<>'3' and "
        			   + "FIELDTYPE<>'7' and TABLEID in (select TABLEID from ANALYSE_MAIN where ID='"+strID+"')";
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(execSQL);
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
            result = new String[length][2];
            for (int i = 0; i < length; i++) {
            	Map<String, Object> retMap = retlist.get(i);
                result[i][0] = retMap.get("FIELDTYPE").toString().trim()+","+retMap.get("FIELDID").toString();
                result[i][1] = retMap.get("CHINESENAME").toString();
            }
        }
        return result;
	}

	@Override
	public String[][] getTableFieldList2(String strID) throws Exception {
        String[][] result = null;
        String execSQL = "Select FIELDID,CHINESENAME From BPIP_FIELD where FIELDTYPE='5' and "
        			   + "TABLEID in (select TABLEID from ANALYSE_MAIN where ID='" + strID+"')";
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(execSQL);
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
            result = new String[length][2];
            for (int i = 0; i < length; i++) {
            	Map<String, Object> retMap = retlist.get(i);
                result[i][0] = retMap.get("FIELDID").toString();
                result[i][1] = retMap.get("CHINESENAME").toString();
            }
        }
        return result;
	}

	@Override
	public String[][] getTableFieldList3(String tableName) throws Exception {
        String[][] result = null;
        String execSQL = "Select FIELDNAME,CHINESENAME From BPIP_FIELD where TABLEID in ("
        			   + "Select TABLEID From BPIP_TABLE WHERE TABLENAME = '"+tableName+"')";
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(execSQL);
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
            result = new String[length][2];
            for (int i = 0; i < length; i++) {
            	Map<String, Object> retMap = retlist.get(i);
                result[i][0] = retMap.get("FIELDNAME").toString();
                result[i][1] = retMap.get("CHINESENAME").toString();
            }
        }
        return result;
	}

	@Override
	public FunctionMessage saveAnalyseMain(ANALYSE_STATISTICS_MAIN entityObj) throws Exception {
        FunctionMessage funMsg = new FunctionMessage(1);
        DBServer dserver = new DBServer();
        // dserver.saveAnalyseMain(entityObj);
        // TODO: 2021/1/3 临时注释

        return funMsg;
	}

	@Override
	public boolean statisticsDelete(String strID) throws Exception {
		boolean result = false;
        String execSQL = ""; // 执行的SQL语句
        try {
            execSQL = "delete from ANALYSE_STATISTICS_CCONNECTION where FID in (select ID from ANALYSE_STATISTICS_CFIELD where FID='"+strID+"')";
            userMapper.deleteExecSQL(execSQL);
            
            execSQL = "delete from ANALYSE_STATISTICS_CWHERE where FID in (select ID from ANALYSE_STATISTICS_CFIELD where FID='"+strID+"')";
            userMapper.deleteExecSQL(execSQL);
            
            //删除统计计算字段配置表
            execSQL = "delete from ANALYSE_STATISTICS_CFIELD where FID='"+strID+"'";
            userMapper.deleteExecSQL(execSQL);
            
            //删除统计条件表
            execSQL = "delete from ANALYSE_STATISTICS_WHERE where FID='"+strID+"'";
            userMapper.deleteExecSQL(execSQL);
            
            //删除自定义统计字段配置
            execSQL = "delete from ANALYSE_STATISTICS_CUSTOM where FID='"+strID+"'";
            userMapper.deleteExecSQL(execSQL);
            
            //删除配置
            execSQL = "delete from ANALYSE_STATISTICS_MAIN where ID='"+strID+"'";
            userMapper.deleteExecSQL(execSQL);
            
            result = true;
        } catch (Exception ex) {
        	result = false;
        	LOGGER.error("提示：调用statisticsDelete出现异常:\n", ex);
        }
        return result;
	}

	@Override
	public ANALYSE_STATISTICS_CFIELD[] getAnalyseCFieldList(String strFID) throws Exception {
        ANALYSE_STATISTICS_CFIELD entitys[] = null;
        StringBuffer  addBuf = new   StringBuffer();
        addBuf.append("select * from ANALYSE_STATISTICS_CFIELD ");
        LOGGER.info("getAnalyseCFieldList开始调用...");
        long startTime = System.currentTimeMillis();
        if (!strFID.equals("")) {
            addBuf.append(" Where FID ='"+strFID+"'");
        }
        addBuf.append(" Order By SHOWCODE");
        try {
        	List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
            int length = retlist != null ? retlist.size() : 0;
            if (length == 0) {
                entitys = null;
            } else {// length > 0
                entitys = new ANALYSE_STATISTICS_CFIELD[length];
                for (int i = 0; i < length; i++) {
                	Map<String, Object> retMap = retlist.get(i);
                    entitys[i] = (ANALYSE_STATISTICS_CFIELD) ReflectionUtil.convertMapToBean(retMap, ANALYSE_STATISTICS_CFIELD.class);
                }
            }
        } catch (Exception ex) {
        	LOGGER.error("getAnalyseCFieldList出现异常", ex);
            entitys = null;
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("getAnalyseCFieldList执行完成，耗时：" + (endTime - startTime) + " ms.");
        return entitys;
	}

	@Override
	public FunctionMessage saveAnalyseCFieldInfo(String strFID, String analyseCField) throws Exception {
        FunctionMessage funMsg = new FunctionMessage(1);
        ANALYSE_STATISTICS_CFIELD entitys = new ANALYSE_STATISTICS_CFIELD();
        LOGGER.info("saveAnalyseCFieldInfo开始调用...");
        long startTime = System.currentTimeMillis();
        String SFIELDNAME = "", SFIELDSHOWNAME = "", EXPRESSIONS = "",
                DISTINCTION = "", ISSHOW = "", EXPRESSIONSWHERE = "", 
                ADDSIGN = "", RADIXPOINT = "", SHOWCODE = "", ID = "";
        String TABLEID = "", CPLANARFIELD = "", CJOIN = "",CPLANARESPECIAL="";
        //当前表复用字段#复用表复用字段&
        LOGGER.info("统计计算字段配置信息字符串" + analyseCField);
        int i = 0, js = 0;
        String tempArray1[] = analyseCField.split("&");
        String tempArray2[];
        String StrId = "";
        String strtvalue ="";
        for (i = 0; i < tempArray1.length; i++) {
            tempArray2 = tempArray1[i].split("#");
            if (tempArray2[13].length() > 0) {
                StrId = StrId + "'" + tempArray2[13] + "',";
            }
        }
        if (StrId.length() > 0) {
            StrId = StrId.substring(0, StrId.length() - 1);
            String[] strSQls =  new String[3];
            strSQls[0] = "Delete from ANALYSE_STATISTICS_CFIELD Where FID='"+strFID+"' and ID not in (" + StrId + ")";
            strSQls[1] = "delete from ANALYSE_STATISTICS_CCONNECTION where not exists (select ID from ANALYSE_STATISTICS_CFIELD where ID = ANALYSE_STATISTICS_CCONNECTION.FID)";
            strSQls[2] = "delete from ANALYSE_STATISTICS_CWHERE where not exists (select ID from ANALYSE_STATISTICS_CFIELD where ID = ANALYSE_STATISTICS_CWHERE.FID)";
            
            userMapper.deleteExecSQL(strSQls[0]);
            userMapper.deleteExecSQL(strSQls[1]);
            userMapper.deleteExecSQL(strSQls[2]);
        }

        for (i = 0; i < tempArray1.length; i++) {
            tempArray2 = tempArray1[i].split("#");
            if (tempArray2[0].length() > 0) {
                SFIELDNAME = tempArray2[0];
            } else {
                SFIELDNAME = "";
            }
            entitys.setSFIELDNAME(SFIELDNAME);
            if (tempArray2[1].length() > 0) {
                SFIELDSHOWNAME = tempArray2[1];
            } else {
                SFIELDSHOWNAME = "";
            }
            entitys.setSFIELDSHOWNAME(SFIELDSHOWNAME);

            if (tempArray2[2].length() > 0) {
                EXPRESSIONS = tempArray2[2];
            } else {
                EXPRESSIONS = "";
            }
            entitys.setEXPRESSIONS(EXPRESSIONS);
            if (tempArray2[3].length() > 0) {
                DISTINCTION = tempArray2[3];
                try {
                  entitys.setDISTINCTION(Integer.parseInt(DISTINCTION));
                }
                catch (NumberFormatException ex) {
                  DISTINCTION = "";
                }
            } else {
                DISTINCTION = "";
            }
            if (tempArray2[4].length() > 0) {
                ISSHOW = tempArray2[4];
            } else {
                ISSHOW = "";
            }
            entitys.setISSHOW(ISSHOW);

            if (tempArray2[5].length() > 0) {
                EXPRESSIONSWHERE = tempArray2[5];
            } else {
                EXPRESSIONSWHERE = "";
            }
            entitys.setEXPRESSIONSWHERE(EXPRESSIONSWHERE);
            if (tempArray2[6].length() > 0) {
                ADDSIGN = tempArray2[6];
            } else {
                ADDSIGN = "";
            }
            entitys.setADDSIGN(ADDSIGN);
            if (tempArray2[7].length() > 0) {
                RADIXPOINT = tempArray2[7];
                try {
                  entitys.setRADIXPOINT(Integer.parseInt(RADIXPOINT));
                }
                catch (NumberFormatException ex1) {
                  RADIXPOINT = "";
                }
            } else {
                RADIXPOINT = "";
            }
            if (tempArray2[8].length() > 0) {
                SHOWCODE = tempArray2[8];
            } else {
                SHOWCODE = "";
            }
            entitys.setSHOWCODE(SHOWCODE);
            if (tempArray2[9].length() > 0) {
                TABLEID = tempArray2[9];
            } else {
                TABLEID = "";
            }
            entitys.setTABLEID(TABLEID);
            if (tempArray2[10].length() > 0) {
                CPLANARFIELD = tempArray2[10];
            } else {
                CPLANARFIELD = "";
            }
            entitys.setCPLANARFIELD(CPLANARFIELD);
            if (tempArray2[11].length() > 0) {
                CJOIN = tempArray2[11];
            } else {
                CJOIN = "";
            }
            entitys.setCJOIN(CJOIN);

            if (tempArray2[12].length() > 0) {
                CPLANARESPECIAL = tempArray2[12];
            } else {
                CPLANARESPECIAL = "";
            }
            entitys.setCPLANARESPECIAL(CPLANARESPECIAL);

            if (tempArray2[13].length() > 0) {
                ID = tempArray2[13];
            } else {
                ID = "";
            }
            entitys.setID(ID);
            entitys.setFID(strFID);
            if (entitys.getID().length() > 2) {
                js = js +1;
                entitys.setSAVEFIELD("FIELD"+String.valueOf(js+1));
                boolean isOk = dbEngine.ExecuteEdit(entitys.getData(), "ID='"+entitys.getID()+"'");
                if (isOk) {
                    funMsg.setResult(true);
                } else {
                	funMsg.setResult(false);
                }
            } else {
                if (entitys.getID().equals("1")) {
                    entitys.setID(getMaxFieldNo("ANALYSE_STATISTICS_CFIELD", "ID", 12));
                    js = js +1;
                    entitys.setSAVEFIELD("FIELD"+String.valueOf(js+1));
                    //处理包含[]格式的字段
                    strtvalue = entitys.getEXPRESSIONS();
                    strtvalue = strtvalue.replaceAll("\\[","");
                    strtvalue = strtvalue.replaceAll("\\]","");
                    entitys.setEXPRESSIONS(strtvalue);
                    
                    boolean isOk = dbEngine.ExecuteInsert(entitys.getData());
                    if (isOk) {
                    	funMsg.setResult(true);
                    } else {
                    	funMsg.setResult(false);
                    }
                } else {
                	funMsg.setResult(true);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("saveAnalyseCFieldInfo执行完成，耗时：" + (endTime - startTime) + " ms.");
        return funMsg;
	}

	@Override
	public ANALYSE_STATISTICS_WHERE[] getAnalyseWhereList(String strFID) throws Exception {
        ANALYSE_STATISTICS_WHERE entitys[] = null;
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("select * from ANALYSE_STATISTICS_WHERE ");
        LOGGER.info("getAnalyseWhereList开始调用...");
        long startTime = System.currentTimeMillis();
        if (!strFID.equals("")) {
            addBuf.append(" Where FID ='"+strFID+"'");
        }
        addBuf.append(" Order By ID");
        try {
        	List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
            int length = retlist != null ? retlist.size() : 0;
            if (length == 0) {
                entitys = null;
            } else {// length > 0
                entitys = new ANALYSE_STATISTICS_WHERE[length];
                for (int i = 0; i < length; i++) {
                	Map<String, Object> retMap = retlist.get(i);
                    entitys[i] = (ANALYSE_STATISTICS_WHERE) ReflectionUtil.convertMapToBean(retMap, ANALYSE_STATISTICS_WHERE.class);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("getAnalyseWhereList出现异常:\n", ex);
            entitys = null;
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("getAnalyseWhereList执行完成，耗时：" + (endTime - startTime) + " ms.");
        return entitys;
	}

	@Override
	public FunctionMessage saveAnalyseWhereInfo(String strFID, String analyseWhere) throws Exception {
        FunctionMessage funMsg = new FunctionMessage(1);
        ANALYSE_STATISTICS_WHERE entitys = new ANALYSE_STATISTICS_WHERE();
        StringBuffer addBuf = new StringBuffer();
        LOGGER.info("saveAnalyseWhereInfo开始调用...");
        long startTime = System.currentTimeMillis();
        String LEFT = "", FIELD = "", SYMBOL = "", WHEREVALUE = "", RIGHT = "", LOGIC = "", ID = "";
        //当前表复用字段#复用表复用字段&
        int i = 0;
        String tempArray1[] = analyseWhere.split("&");
        String tempArray2[];
        String StrId = "";
        for (i = 0; i < tempArray1.length; i++) {
            tempArray2 = tempArray1[i].split("#");
            if (tempArray2[6].length() > 0) {
                StrId = StrId + "'" + tempArray2[6] + "',";
            }
        }
        if (StrId.length() > 0) {
            StrId = StrId.substring(0, StrId.length() - 1);
            addBuf.append("Delete from ANALYSE_STATISTICS_WHERE Where FID='"+strFID).append("' and ID not in ("+StrId+")");
        } else {
            addBuf.append("Delete from ANALYSE_STATISTICS_WHERE Where FID='"+strFID+"'");
        }
        userMapper.deleteExecSQL(addBuf.toString());
        for (i = 0; i < tempArray1.length; i++) {
            tempArray2 = tempArray1[i].split("#");
            if (tempArray2[0].length() == 0) {
                continue;
            }
            LEFT = tempArray2[0];
            entitys.setSLEFT(LEFT);
            if (tempArray2[1].length() > 0) {
                FIELD = tempArray2[1];
            } else {
                FIELD = "";
            }
            entitys.setFIELD(FIELD);
            if (tempArray2[2].length() > 0) {
                SYMBOL = tempArray2[2];
            } else {
                SYMBOL = "";
            }
            entitys.setSYMBOL(SYMBOL);
            if (tempArray2[3].length() > 0) {
                WHEREVALUE = tempArray2[3];
            } else {
                WHEREVALUE = "";
            }
            entitys.setWHEREVALUE(WHEREVALUE);
            if (tempArray2[4].length() > 0) {
                RIGHT = tempArray2[4];
            } else {
                RIGHT = "";
            }
            entitys.setSRIGHT(RIGHT);
            if (tempArray2[5].length() > 0) {
                LOGIC = tempArray2[5];
            } else {
                LOGIC = "";
            }
            entitys.setLOGIC(LOGIC);
            if (tempArray2[6].length() > 0) {
                ID = tempArray2[6];
            } else {
                ID = "";
            }
            entitys.setID(ID);
            entitys.setFID(strFID);
            if (entitys.getID().length() > 2) {
                boolean isOk = dbEngine.ExecuteEdit(entitys.getData(), "ID='"+entitys.getID()+"'");
                if (isOk) {
                    funMsg.setResult(true);
                } else {
                	funMsg.setResult(false);
                }
            } else {
                if (entitys.getID().equals("1")) {
                    entitys.setID(getMaxFieldNo("ANALYSE_STATISTICS_WHERE", "ID", 12));
                    boolean isOk = dbEngine.ExecuteInsert(entitys.getData());
                    if (isOk) {
                    	funMsg.setResult(true);
                    } else {
                    	funMsg.setResult(false);
                    }
                } else {
                	funMsg.setResult(true);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("saveAnalyseWhereInfo执行完成，耗时：" + (endTime - startTime) + " ms.");
        return funMsg;
	}

	@Override
	public ANALYSE_STATISTICS_CCONNECTION[] getAnalyseCConnectionList(String strFID) throws Exception {
        ANALYSE_STATISTICS_CCONNECTION entitys[] = null;
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("select * from ANALYSE_STATISTICS_CCONNECTION ");
        LOGGER.info("getAnalyseCConnectionList开始调用...");
        long startTime = System.currentTimeMillis();
        if (!strFID.equals("")) {
            addBuf.append(" Where FID ='"+strFID+"'");
        }
        addBuf.append(" Order By ID");
        try {
        	List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
            int length = retlist != null ? retlist.size() : 0;
            if (length == 0) {
                entitys = null;
            } else {// length > 0
                entitys = new ANALYSE_STATISTICS_CCONNECTION[length];
                for (int i = 0; i < length; i++) {
                	Map<String, Object> retMap = retlist.get(i);
                    entitys[i] = (ANALYSE_STATISTICS_CCONNECTION) ReflectionUtil.convertMapToBean(retMap, ANALYSE_STATISTICS_CCONNECTION.class);
                }
            }
        } catch (Exception ex) {
        	LOGGER.error("getAnalyseCConnectionList出现异常:\n", ex);
            entitys = null;
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("getAnalyseCConnectionList执行完成，耗时：" + (endTime - startTime) + " ms.");
        return entitys;
	}

	@Override
	public FunctionMessage saveAnalyseCConnectionInfo(String FID, String analyseConnection) throws Exception {
        FunctionMessage funMsg = new FunctionMessage(1);
        StringBuffer addBuf = new StringBuffer();
        ANALYSE_STATISTICS_CCONNECTION entitys = new ANALYSE_STATISTICS_CCONNECTION();
        LOGGER.info("saveAnalyseCConnectionInfo开始调用...");
        long startTime = System.currentTimeMillis();
        String MFIELD = "";
        String CFIELD = "";
        String JOINTYPE = "";
        String ID = "";
        //当前表复用字段#复用表复用字段&
        int i = 0;
        String tempArray1[] = analyseConnection.split("&");
        String tempArray2[];
        String StrId = "";
        for (i = 0; i < tempArray1.length; i++) {
            tempArray2 = tempArray1[i].split("#");
            if (tempArray2[3].length() > 0) {
                StrId = StrId + "'" + tempArray2[3] + "',";
            }
        }
        if (StrId.length() > 0) {
            StrId = StrId.substring(0, StrId.length() - 1);
            addBuf.append("Delete from ANALYSE_STATISTICS_CCONNECTION Where FID='"+FID+"' and ID not in ("+StrId+")");
        } else {
            addBuf.append("Delete from ANALYSE_STATISTICS_CCONNECTION Where FID='"+FID+"'");
        }
        userMapper.deleteExecSQL(addBuf.toString());
        for (i = 0; i < tempArray1.length; i++) {
            tempArray2 = tempArray1[i].split("#");
            if (tempArray2[0].length() == 0) {
                continue;
            }
            CFIELD = tempArray2[0];
            entitys.setCFIELD(CFIELD);
            if (tempArray2[1].length() > 0) {
                MFIELD = tempArray2[1];
            } else {
                MFIELD = "";
            }
            entitys.setMFIELD(MFIELD);
            if (tempArray2[2].length() > 0) {
                JOINTYPE = tempArray2[2];
            } else {
                JOINTYPE = "";
            }
            entitys.setJOINTYPE(JOINTYPE);
            if (tempArray2[3].length() > 0) {
                ID = tempArray2[3];
            } else {
                ID = "";
            }
            entitys.setID(ID);
            entitys.setFID(FID);
            if (entitys.getID().length() > 2) {
            	boolean isOk = dbEngine.ExecuteEdit(entitys.getData(), "ID='" + entitys.getID() + "'");
                if (isOk) {
                    funMsg.setResult(true);
                } else {
                    funMsg.setResult(false);
                }
            } else {
                if (entitys.getID().equals("1")) {
                    entitys.setID(getMaxFieldNo("ANALYSE_STATISTICS_CCONNECTION", "ID", 14));
                    boolean isOk = dbEngine.ExecuteInsert(entitys.getData());
                    if (isOk) {
                        funMsg.setResult(true);
                    } else {
                        funMsg.setResult(false);
                    }
                } else {
                	funMsg.setResult(true);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("saveAnalyseCConnectionInfo执行完成，耗时：" + (endTime - startTime) + " ms.");
        return funMsg;
	}

	@Override
	public ANALYSE_STATISTICS_CUSTOM[] getAnalyseCustomList(String strFID) throws Exception {
        ANALYSE_STATISTICS_CUSTOM entitys[] = null;
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("select * from ANALYSE_STATISTICS_CUSTOM ");
        LOGGER.info("getAnalyseCustomList开始调用...");
        long startTime = System.currentTimeMillis();
        if (!strFID.equals("")) {
            addBuf.append(" Where FID ='"+strFID+"'");
        }
        addBuf.append(" Order By ID");
        try {
        	List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
            int length = retlist != null ? retlist.size() : 0;
            if (length == 0) {
                entitys = null;
            } else {// length > 0
                entitys = new ANALYSE_STATISTICS_CUSTOM[length];
                for (int i = 0; i < length; i++) {
                	Map<String, Object> retMap = retlist.get(i);
                    entitys[i] = (ANALYSE_STATISTICS_CUSTOM) ReflectionUtil.convertMapToBean(retMap, ANALYSE_STATISTICS_CUSTOM.class);
                }
            }
        } catch (Exception ex) {
        	LOGGER.error("getAnalyseCustomList出现异常:\n", ex);
            entitys = null;
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("getAnalyseCustomList执行完成，耗时：" + (endTime - startTime) + " ms.");
        return entitys;
	}

	@Override
	public ANALYSE_STATISTICS_CWHERE[] getAnalyseCWhereList(String strFID) throws Exception {
        ANALYSE_STATISTICS_CWHERE entitys[] = null;
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("select * from ANALYSE_STATISTICS_CWHERE ");
        LOGGER.info("getAnalyseCWhereList开始调用...");
        long startTime = System.currentTimeMillis();
        if (!strFID.equals("")) {
            addBuf.append(" Where FID ='"+strFID+"'");
        }
        addBuf.append(" Order By ID");
        try {
        	List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
            int length = retlist != null ? retlist.size() : 0;
            if (length == 0) {
                entitys = null;
            } else {// length > 0
                entitys = new ANALYSE_STATISTICS_CWHERE[length];
                for (int i = 0; i < length; i++) {
                	Map<String, Object> retMap = retlist.get(i);
                    entitys[i] = (ANALYSE_STATISTICS_CWHERE) ReflectionUtil.convertMapToBean(retMap, ANALYSE_STATISTICS_CWHERE.class);
                }
            }
        } catch (Exception ex) {
        	LOGGER.error("getAnalyseCWhereList出现异常:\n", ex);
            entitys = null;
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("getAnalyseCWhereList执行完成，耗时：" + (endTime - startTime) + " ms.");
        return entitys;
	}

	@Override
	public FunctionMessage saveAnalyseCWhereInfo(String FID, String analyseCWhere) throws Exception {
        FunctionMessage funMsg = new FunctionMessage(1);
        StringBuffer addBuf = new StringBuffer();
        ANALYSE_STATISTICS_CWHERE entitys = new ANALYSE_STATISTICS_CWHERE();
        LOGGER.info("saveAnalyseWhereInfo开始调用...");
        long startTime = System.currentTimeMillis();
        String LEFT = "", FIELD = "", SYMBOL = "", WHEREVALUE = "", RIGHT = "", LOGIC = "", ID = "";
        //当前表复用字段#复用表复用字段&
        int i = 0;
        String tempArray1[] = analyseCWhere.split("&");
        String tempArray2[];
        String StrId = "";
        for (i = 0; i < tempArray1.length; i++) {
            tempArray2 = tempArray1[i].split("#");
            if (tempArray2[6].length() > 0) {
                StrId = StrId + "'" + tempArray2[6] + "',";
            }
        }
        if (StrId.length() > 0) {
            StrId = StrId.substring(0, StrId.length() - 1);
            addBuf.append("Delete from ANALYSE_STATISTICS_CWHERE Where FID='"+FID+"' and ID not in ("+StrId+")");
        } else {
            addBuf.append("Delete from ANALYSE_STATISTICS_CWHERE Where FID='"+FID+"'");
        }
        userMapper.deleteExecSQL(addBuf.toString());
        for (i = 0; i < tempArray1.length; i++) {
            tempArray2 = tempArray1[i].split("#");
            if (tempArray2[0].length() == 0) {
                continue;
            }
            LEFT = tempArray2[0];
            entitys.setSLEFT(LEFT);
            if (tempArray2[1].length() > 0) {
                FIELD = tempArray2[1];
            } else {
                FIELD = "";
            }
            entitys.setFIELD(FIELD);
            if (tempArray2[2].length() > 0) {
                SYMBOL = tempArray2[2];
            } else {
                SYMBOL = "";
            }
            entitys.setSYMBOL(SYMBOL);
            if (tempArray2[3].length() > 0) {
                WHEREVALUE = tempArray2[3];
            } else {
                WHEREVALUE = "";
            }
            entitys.setWHEREVALUE(WHEREVALUE);
            if (tempArray2[4].length() > 0) {
                RIGHT = tempArray2[4];
            } else {
                RIGHT = "";
            }
            entitys.setSRIGHT(RIGHT);

            if (tempArray2[5].length() > 0) {
                LOGIC = tempArray2[5];
            } else {
                LOGIC = "";
            }
            entitys.setLOGIC(LOGIC);
            if (tempArray2[6].length() > 0) {
                ID = tempArray2[6];
            } else {
                ID = "";
            }
            entitys.setID(ID);
            entitys.setFID(FID);
            if (entitys.getID().length() > 2) {
            	boolean isOk = dbEngine.ExecuteEdit(entitys.getData(), "ID='"+entitys.getID()+"'");
            	if (isOk) {
                    funMsg.setResult(true);
                } else {
                    funMsg.setResult(false);
                }
            } else {
                if (entitys.getID().equals("1")) {
                    entitys.setID(getMaxFieldNo("ANALYSE_STATISTICS_CWHERE", "ID", 14));
                	boolean isOk = dbEngine.ExecuteInsert(entitys.getData());
                    if (isOk) {
                        funMsg.setResult(true);
                    } else {
                        funMsg.setResult(false);
                    }
                } else {
                	funMsg.setResult(true);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("saveAnalyseCWhereInfo执行完成，耗时：" + (endTime - startTime) + " ms.");
        return funMsg;
	}

	@Override
	public String getAllTableFieldName(String tableOrFieldchinesename, String tableOrFieldTablename,
			String tableOrFieldId, String tableOrFieldIdCondition) throws Exception {
        String retValue = "";
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select "+tableOrFieldchinesename+" From "+tableOrFieldTablename)
        	  .append(" Where "+tableOrFieldId+" = '"+tableOrFieldIdCondition+"'");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0 && retlist.get(0) != null) {
        	if (retlist.get(0).get(tableOrFieldchinesename) != null) {
        		retValue = retlist.get(0).get(tableOrFieldchinesename).toString();
        	}
        }
        return retValue;
	}

	@Override
	public String getAllFieldName(String tableId, String fieldName) throws Exception {
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select CHINESENAME From BPIP_FIELD Where TABLEID = '")
              .append(tableId+"' AND FIELDNAME ='"+fieldName).append("'");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        int length = retlist != null ? retlist.size() : 0;
        addBuf.delete(0, addBuf.length());//清空
        if (length > 0 && retlist.get(0) != null) {
        	if (retlist.get(0).get("CHINESENAME") != null) {
        		addBuf.append(retlist.get(0).get("CHINESENAME").toString());
        	}
        }
        return addBuf.toString();
	}

	@Override
	public String[][] getTableFieldListList(String tableName) throws Exception {
        String[][] retValue = null;
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.FIELDNAME,b.CHINESENAME as FIELDCNNAME ")
              .append("From BPIP_TABLE a,BPIP_FIELD b Where b.TABLEID = a.tableid and  "+"(")
              .append(tableName+") order by a.tableid,b.fieldid");
        LOGGER.info("sql=" + addBuf.toString());
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
        	retValue = new String[length][2];
            for (int i = 0; i < length; i++) {
            	Map<String, Object> retMap = retlist.get(i);
            	retValue[i][0] = retMap.get("TABLENAME").toString() + "." +
            					 retMap.get("FIELDNAME").toString();
            	retValue[i][1] = retMap.get("TABLECNNAME").toString() + "." +
            					 retMap.get("FIELDCNNAME").toString();
            }
        }
        return retValue;
	}

	@Override
	public String showTableFieldSelect(String tableID) throws Exception {
		return showTableFieldSelect(getTableFieldListList(tableID));
	}

	@Override
	public String showTableFieldSelect(String[][] tableFieldList) throws Exception {
        StringBuffer addBuf = new StringBuffer();
        if (tableFieldList != null && tableFieldList.length > 0) {
            for (int i = 0; i < tableFieldList.length; i++) {
                addBuf.append("<option value=\""+tableFieldList[i][0])
                      .append("\">"+tableFieldList[i][1]+","+tableFieldList[i][0]+"</option>\n");
            }
        }
        return addBuf.toString();
	}

	@Override
	public String[][] getFieldListList(String tableName) throws Exception {
        String[][] retValue = null;
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select a.FIELDNAME,a.CHINESENAME From BPIP_FIELD a left join BPIP_TABLE b on a.TABLEID=b.TABLEID WHERE b.TABLENAME = '")
              .append(tableName+"'");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
        	retValue = new String[length][2];
            for (int i = 0; i < length; i++) {
            	Map<String, Object> retMap = retlist.get(i);
            	retValue[i][0] = retMap.get("FIELDNAME").toString();
            	retValue[i][1] = retMap.get("CHINESENAME").toString();
            }
        }
        return retValue;
	}

	@Override
	public String showFieldSelect(String tableName) throws Exception {
		return showFieldSelect(getFieldListList(tableName));
	}

	@Override
	public String showFieldSelect(String[][] fieldList) throws Exception {
        StringBuffer addBuf = new StringBuffer();
        if (fieldList != null && fieldList.length > 0) {
            for (int i = 0; i < fieldList.length; i++) {
                addBuf.append("<option value=\""+fieldList[i][0])
                      .append("\">"+fieldList[i][1]+"</option>\n");
            }
        }
        return addBuf.toString();
	}

	@Override
	public String[][] getTableList() throws Exception {
        String[][] result = null;
        String strSQL = "Select TableNAME,CHINESENAME From BPIP_Table where TABLETYPE='2' or TABLETYPE='3' order by CHINESENAME";
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
            result = new String[length][2];
            for (int i = 0; i < length; i++) {
            	Map<String, Object> retMap = retlist.get(i);
                result[i][0] = retMap.get("TableNAME").toString();
                result[i][1] = retMap.get("CHINESENAME").toString();
            }
        }
        return result;
	}

	@Override
	public List<Map<String, Object>> getFieldList(String tableName) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select a.FIELDNAME,a.CHINESENAME From BPIP_FIELD a left join BPIP_TABLE b on a.TABLEID=b.TABLEID Where b.TABLENAME='")
              .append(tableName+"'");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                String[] saTmp = new String[2];
                Map<String, Object> retMap = retlist.get(i);
                saTmp[0] = retMap.get("FIELDNAME").toString();
                saTmp[1] = retMap.get("CHINESENAME").toString();
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
	public String[][] getFieldList1(String tableName) throws Exception {
        String[][] result = null;
        StringBuffer  addBuf = new   StringBuffer();
        addBuf.append("Select a.FIELDNAME,a.CHINESENAME From BPIP_FIELD a left join BPIP_TABLE b on a.TABLEID=b.TABLEID Where b.TABLENAME='")
              .append(tableName+"'");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
            result = new String[length][2];
            for (int i = 0; i < length; i++) {
            	Map<String, Object> retMap = retlist.get(i);
                result[i][0] = retMap.get("FIELDNAME").toString();
                result[i][1] = retMap.get("CHINESENAME").toString();
            }
        }
        return result;
	}

	@Override
	public void setReTable(String ID, String FILENAME) throws Exception {
		String strSql = "Update ANALYSE_STATISTICS_MAIN Set TIMETEMPLATE = '"+FILENAME+"' Where ID='"+ID+"'";
        userMapper.updateExecSQL(strSql);
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

    /**
     * 功能：取出最大的记录流水号
     * @param tableName 数据库表名
     * @param fieldName 数据库字段名称
     * @param fieldLen 数据库字段长度
     * @Return MaxNo 执行后返回一个MaxNo字符串
     */
    private String getMaxFieldNo(String tableName, String fieldName, int fieldLen) {
        String MaxNo = "";
        int LenMaxNo = 0;
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("SELECT MAX("+fieldName+") AS MaxNo FROM "+tableName);
        try {
        	String retMaxNo = userMapper.selectStrExecSQL(addBuf.toString());
        	if (retMaxNo == null) {
        		retMaxNo = "";
        	}
            MaxNo = retMaxNo;// 最大编号
            if (MaxNo.length() > 0) {
                MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
                LenMaxNo = MaxNo.length();
                addBuf.delete(0,addBuf.length());//清空
                addBuf.append("0000000000000000000000000"+MaxNo);
                MaxNo = addBuf.toString();
            } else {
                MaxNo = "00000000000000000000000001";
                LenMaxNo = 1;
            }
            MaxNo = MaxNo.substring(25 - fieldLen + LenMaxNo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MaxNo;
    }
}