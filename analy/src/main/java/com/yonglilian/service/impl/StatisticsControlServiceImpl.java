package com.yonglilian.service.impl;

import com.alibaba.fastjson.JSON;
import com.yonglilian.analyseengine.ItemField;
import com.yonglilian.analyseengine.ItemList;
import com.yonglilian.analyseengine.mode.*;
import com.yonglilian.common.util.ReflectionUtil;
import com.yonglilian.common.util.StringUtils;
import com.yonglilian.common.util.StringWork;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.UserMapper;
import com.yonglilian.model.SessionUser;
import com.yonglilian.service.StatisticsControlService;
import org.htmlparser.Parser;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBRow;
import zr.zrpower.common.db.DBSet;
import zr.zrpower.common.db.DBType;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Title:数据统计引擎服务
 * @author lwk
 *
 */
@Service
public class StatisticsControlServiceImpl implements StatisticsControlService {
	/** The StatisticsConfigServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsControlServiceImpl.class);
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
	/** 数据库引擎. */
	private DBEngine dbEngine;
	private static int clients = 0;
	private Timer timerListener; //队列侦听器
	static String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	static Map<String, Object> SetHashtable1 = new HashMap<String, Object>();
    static Map<String, Object> SetHashtable2 = new HashMap<String, Object>();
    static Map<String, Object> SetHashtable3 = new HashMap<String, Object>();
    static Map<String, Object> SetHashtable31 = new HashMap<String, Object>();
    static Map<String, Object> SetHashtable4 = new HashMap<String, Object>();

	/**
	 * 构造方法
	 */
	public StatisticsControlServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
	          int intnum = 3600;// 60分钟扫描一次
	          timerListener = new Timer();
	          timerListener.schedule(new ScanTask(), 0, intnum * 1000);
		}
		clients = 1;
    }

	/**
	 * ftl  调整（生成）
	 */
	@Override
	public String getCompute(String strID, String strP1, String strP2, String strP3, String strP4, 
			String strP5, SessionUser user, String strConn, String UNITID) throws Exception {
        String retvalue = "1";
        try {
        	getCompute2(strID, strP1, strP2, strP3, strP4, strP5, user,strConn,UNITID);
        } catch (Exception ex) {
        	LOGGER.error("StatisticsControlServiceImpl统计计算出错:\n", ex);
            retvalue="0";
        }
        return retvalue;
	}

	@Override
	public String getLinkWhere(String strID, String UNITID, String TIER, String P1, String P2, 
			String P3, String P4, String P5, SessionUser user) throws Exception {

        String strReturnInitWhere="";
        String strReturnCWhere="";
        String strReturnUnitWhere="";
        String strInitWhere="";//统计的初始化条件
        String strInitCWhere="";//统计字段的初始化条件
        String strCFIELDWhere="";//计算字段的计算条件
        String strTmp="";
        StringBuffer  addBuf = new   StringBuffer();
        //根据统计配置ID和列号得到计算字段的ID
        String strComTierID = getComTierID(strID,TIER);
        //得到统计字段的初始条件
        strInitCWhere = getInitCCondition(strComTierID,P1,P2,P3,P4,P5,user);
        if (strInitCWhere.trim().length()==0)//计算字段没有初始条件时采用统计配置的初始条件
        {
            //得到计算字段的初始条件
            strInitWhere = getInitCondition(strID,P1,P2,P3,P4,P5,user);
            if (strInitWhere.trim().length()>0) {
                addBuf.append(" and ("+strInitWhere+")");
            }
        } else {
            if (strInitCWhere.trim().length()>0) {
                addBuf.append(" and ("+strInitCWhere+")");
            }
        }
        strReturnInitWhere = addBuf.toString();
        //得到选中列本身的计算条件
        strCFIELDWhere = getCFIELDWhere(strComTierID);
        if (strCFIELDWhere.trim().length()>0) {
            addBuf.delete(0,addBuf.length());//清空
            addBuf.append(" and ("+strCFIELDWhere+")");
            strReturnCWhere = addBuf.toString();
        }
        //得到组成的所属单位的查询条件
        strReturnUnitWhere = getUnitWhere(strID,strComTierID);
        if (strReturnUnitWhere.trim().length()>0) {
            addBuf.delete(0,addBuf.length());//清空
            addBuf.append(" and ("+strReturnUnitWhere+" like '"+UNITID+"%')");
            strReturnUnitWhere = addBuf.toString();
        }
        //组成完整和条件
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append(" 1=1 "+strReturnInitWhere+strReturnCWhere+strReturnUnitWhere);
        strTmp=addBuf.toString();
        strTmp = strTmp.replaceAll("\\[","");
        strTmp = strTmp.replaceAll("\\]","");
        return strTmp;
	}

	@Override
	public ANALYSE_STATISTICS_MAIN getSmain(String strID) throws Exception {
       ANALYSE_STATISTICS_MAIN smain = null;
       StringBuffer addBuf = new StringBuffer();
       try {
           if (strID.trim().length() > 0) {
               addBuf.append("Select * from ANALYSE_STATISTICS_MAIN WHERE ID ='").append(strID+"'");
               Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
               if (retmap != null && retmap.size() > 0) {
            	   smain = (ANALYSE_STATISTICS_MAIN) ReflectionUtil.convertMapToBean(retmap, ANALYSE_STATISTICS_MAIN.class);
               }
           }
       } catch (Exception e) {
           LOGGER.error("getSmain出现异常！" + e.getMessage());
       }
       return smain;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getEndTableList(String strID, String strUserNo) throws Exception {
        List result = new ArrayList();
        ANALYSE_STATISTICS_RESULT STATISTICS_RESULT = null;
        StringBuffer addBuf = new StringBuffer();
       //删除临时统计表中本用户统计的不相关的数据
       addBuf.append("delete from ANALYSE_STATISTICS_RESULT Where (SUSERNO='")
             .append(strUserNo+"' and FID<>'"+strID+"') or FID is null");
       userMapper.deleteExecSQL(addBuf.toString());
       addBuf.delete(0,addBuf.length());//清空
       addBuf.append("Select * From ANALYSE_STATISTICS_RESULT Where SUSERNO='"+strUserNo+"' order by ID");
       List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
       int length = retlist != null ? retlist.size() : 0;
       if (length > 0) {
           for (int i = 0; i < length; i++) {
        	   Map<String, Object> retmap = retlist.get(i);
               STATISTICS_RESULT = (ANALYSE_STATISTICS_RESULT) ReflectionUtil.convertMapToBean(retmap, ANALYSE_STATISTICS_RESULT.class);
               result.add(STATISTICS_RESULT);
           }
        }
        return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String[][] getTableShowList(String strID) throws Exception {
        String[][] result2 = null;
        result2 = (String[][]) SetHashtable2.get(strID);
        if (result2 == null) {
	        String result = "";
	        String[][] result1 = null;
	        String strSAVEFIELD = "";
	        String strFIELDNAME = "";
	        String strSHOWCODE = "";
	        String strWIDTH = "";
	        String tmpName = "";
	        ANALYSE_STATISTICS_MAIN Smain = null;
	        Smain = getSmain(strID);
	        StringBuffer addBuf = new StringBuffer();
	        StringBuffer reBuf = new StringBuffer();
	        try {
		        String strREPORTTYPE = "2";//二维表
		        if (strREPORTTYPE.equals("2")) {//是二维表时需要加维数据字段
		            //维数据保存对应字段
		            strSAVEFIELD = "FIELD1";
		            List arrList = getArrayList(strSAVEFIELD,".");
		            if (arrList.size()==2) {
		              strSAVEFIELD = arrList.get(1).toString();
		            }
		            reBuf.append(strSAVEFIELD);
		            //维数据字段显示名称
		            tmpName = getTABLEFIELDNAME(Smain.getTABLEID(), Smain.getCPLANARFIELD());
		            if (tmpName.equals("0")){tmpName="二维名称";}
		            strFIELDNAME = tmpName;
		            
		            reBuf.append("/"+strFIELDNAME);
		            //维数据显示宽度
		            strWIDTH = "120";
		            reBuf.append("/"+strWIDTH);
		            
		            //显示序号
		            reBuf.append("/00");
		        }
		        //得到显示字段-------------------
		        
		        //得到计算字段-------------------
		        addBuf.delete(0,addBuf.length());//清空
		        addBuf.append("Select SAVEFIELD,SFIELDSHOWNAME,SHOWCODE from ANALYSE_STATISTICS_CFIELD "
		        			+ "Where FID='"+strID+"' and ISSHOW<>'1' order by ID");
		        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
		        int length = retlist != null ? retlist.size() : 0;
		        if (length > 0){
		           for (int i = 0; i < length; i++) {
		        	  Map<String, Object> retmap = retlist.get(i);
		              strSAVEFIELD = retmap.get("SAVEFIELD").toString();
		              List arrList = getArrayList(strSAVEFIELD,".");
		              if (arrList.size()==2) {
		                 strSAVEFIELD = arrList.get(1).toString();
		              }
		              if (reBuf.toString().length()==0) {
		                  reBuf.append(strSAVEFIELD);
		              } else {
		                  reBuf.append("^"+strSAVEFIELD);
		              }
		              //字段显示名称
		              strFIELDNAME = retmap.get("SFIELDSHOWNAME").toString();
		              reBuf.append("/"+strFIELDNAME);
		              //显示宽度
		              strWIDTH = "120";
		              reBuf.append("/"+strWIDTH);
		              //显示序号
		              strSHOWCODE = retmap.get("SHOWCODE").toString();
		              reBuf.append("/"+strSHOWCODE);
		           }
		        }
		        result = reBuf.toString();
		        LOGGER.info("字段串："+result);
		
		        List arrList = new ArrayList();
		        List arrList1 = new ArrayList();
		        arrList = getArrayList(result,"^");
		        result1 = new String[arrList.size()][4];
		        //按序号后排序处理后返回
		        for (int i = 0; i < arrList.size(); i++) {
		            arrList1 = getArrayList(arrList.get(i).toString(),"/");
		            result1[i][0]=arrList1.get(0).toString();
		            result1[i][1]=arrList1.get(1).toString();
		            result1[i][2]=arrList1.get(2).toString();
		            result1[i][3]=arrList1.get(3).toString();
		        }
		        //处理排序(冒泡排序法)
		        String tmp[] = new String[4];
		        for (int i = 0; i < result1.length; i++) {
		             for (int j = 0; j < result1.length - i - 1; j++) {
		                 //对邻接的元素进行比较，如果后面的小，就交换
		                 if (result1[j][3].compareTo(result1[j + 1][3]) > 0) {
		                     tmp = result1[j];
		                     result1[j] = result1[j + 1];
		                     result1[j + 1] = tmp;
		                 }
		             }
		         }
		         //排序结束
		         result2 = new String[result1.length][3];
		         for (int i = 0; i < result1.length; i++) {
		             result2[i][0] = result1[i][0];
		             result2[i][1] = result1[i][1];
		             result2[i][2] = result1[i][2];
		         }
		     } catch (Exception ex) {
		    	 LOGGER.error("出错:"+ex.toString());
		     }
		     if (result2 != null) {
		        SetHashtable2.put(strID,result2);
		     }
	    }
        return result2;
	}

	@Override
	public String getTiers(String strID) throws Exception {
        String retValue ="";
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("select DISTINCTION from ANALYSE_STATISTICS_CFIELD "
        			+ "where FID='"+strID+"' and ISSHOW<>'1' order by SHOWCODE");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        int length = retlist != null ? retlist.size() : 0;
        addBuf.delete(0,addBuf.length());//清空
        if (length > 0){
            for (int i = 0; i < length; i++) {
        	    Map<String, Object> retmap = retlist.get(i);
                int intDISTINCTION = (int) Float.parseFloat(retmap.get("DISTINCTION").toString());
                if (intDISTINCTION == 1) {//计算级别为1的表示有可显示的详细列表
            	    addBuf.append(String.valueOf(i+1)+",");
                }
            }
        }
        retValue = addBuf.toString();
        if (retValue == null) {
        	retValue =" ";
        }
        return retValue;
	}

	/**
	 * ftl  调整（功能：得到年、季度、月、周、日、自定义日期格式HTML）
	 * @return retValue
	 */
	@Override
	public String getDateHtml() throws Exception {
        StringBuffer addBuf = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //得到当天属于本年的第几周
        int intweek=cal.get(Calendar.WEEK_OF_YEAR);
        if (intweek>1){intweek=intweek-1;}

        String mDate = formatter.format(cal.getTime());
        String strDate1 = mDate.substring(0,4)+"-"+mDate.substring(5,7)+"-01";
        String strDate2 = mDate.substring(0,4)+"-"+mDate.substring(5,7)+"-"+mDate.substring(8,10);

        mDate = mDate.substring(0,4);
        String sYear = "<select name='year1' id='year1' size='1' class='easyui-combobox' style='width:100px'>\r\n";
        for (int i=2000;i<=Integer.parseInt(mDate);i++) {
           if (i==Integer.parseInt(mDate)) {
                sYear = sYear +"<option value='"+String.valueOf(i)+"' selected>"+String.valueOf(i)+"</option>\r\n";
           } else {
                sYear = sYear +"<option value='"+String.valueOf(i)+"'>"+String.valueOf(i)+"</option>\r\n";
           }
        }
        sYear = sYear +"</select>\r\n";
        addBuf.append("<TABLE width=\"100%\" cellSpacing='0'>\r\n")
              .append("<TR><td width='420'>&nbsp;报表类别:\r\n")
              .append("<input type='radio' value='1' name= 'type' checked onclick=\"document.all('div1').style.display='';document.all('div2').style.display='none';document.all('div3').style.display='none';document.all('div4').style.display='none';document.all('div5').style.display='none';document.all('div6').style.display='none';\">年报\r\n")
              .append("<input type='radio' value='2' name= 'type' onclick=\"document.all('div1').style.display='';document.all('div2').style.display='';document.all('div3').style.display='none';document.all('div4').style.display='none';document.all('div5').style.display='none';document.all('div6').style.display='none';\">季度报\r\n")
              .append("<input type='radio' value='3' name= 'type' onclick=\"document.all('div1').style.display='';document.all('div2').style.display='none';document.all('div3').style.display='';document.all('div4').style.display='none';document.all('div5').style.display='none';document.all('div6').style.display='none';\">月报\r\n")
              .append("<input type='radio' value='4' name= 'type' onclick=\"document.all('div1').style.display='';document.all('div2').style.display='none';document.all('div3').style.display='none';document.all('div4').style.display='';document.all('div5').style.display='none';document.all('div6').style.display='none';\">周报\r\n")
              .append("<input type='radio' value='5' name= 'type' onclick=\"document.all('div1').style.display='none';document.all('div2').style.display='none';document.all('div3').style.display='none';document.all('div4').style.display='none';document.all('div5').style.display='';document.all('div6').style.display='none';\">日报\r\n")
              .append("<input type='radio' value='6' name= 'type' onclick=\"document.all('div1').style.display='none';document.all('div2').style.display='none';document.all('div3').style.display='none';document.all('div4').style.display='none';document.all('div5').style.display='none';document.all('div6').style.display='';\">自定义日期\r\n")
              .append("</td><td><TABLE cellSpacing='0'><tr><td><div id='div1' style=\"overflow:'hidden';display:''\">\r\n")
              .append("日期范围:"+sYear+"年\r\n"+"</div>\r\n")
              .append("</td><td><div id='div2' style=\"overflow:'hidden';display:none\">\r\n")
              .append("<select id='Quarter' name='Quarter' size='1' class='easyui-combobox' style='width:100px'>\r\n");

        
        for (int i=1; i<=52; i++) {
            if (i==intweek) {//本周默认为已选
                addBuf.append("<option value='"+String.valueOf(i)+"' selected>第")
                      .append(String.valueOf(i)+"周</option>\r\n");
            } else {
                addBuf.append("<option value='"+String.valueOf(i)+"'>第")
                      .append(String.valueOf(i)+"周</option>\r\n");
            }
        }
        addBuf.append("</select>\r\n"+"</div>\r\n")
	          .append("</td><td><div id='div5' style=\"overflow:'hidden';display:none\">\r\n")
	          .append("<input type='text' style='width:100px;' name='date1' id='date1' class='easyui-datebox' maxlength=10 value='")
	          .append(strDate1+"' onkeypress='return CheckTime();' onblur='ValidDate(this);' onkeyup='onlyDate();'>")
	          .append("</div>\r\n"+"</td>\r\n")
	          .append("</td><td><div id='div6' style=\"overflow:'hidden';display:none\">\r\n")
	          .append("从<input type='text'  style='width:100px;' name='date2' id='date2' class='easyui-datebox' maxlength=10 value='")
	          .append(strDate1+"' onkeypress='return CheckTime();' onblur='ValidDate(this);' onkeyup='onlyDate();'>")
	          .append("至<input type='text'  style='width:100px;'  name='date3' id='date3' class='easyui-datebox' maxlength=10 value='")
	          .append(strDate2+"' onkeypress='return CheckTime();' onblur='ValidDate(this);' onkeyup='onlyDate();'>")
	          .append("</div>\r\n"+"</td>\r\n"+"</tr></table>\r\n");
        return addBuf.toString();
	}

	@Override
	public String getMonthHtml() throws Exception {
	       StringBuffer addBuf = new StringBuffer();
	       Calendar cal = Calendar.getInstance();
	       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	       String mDate = formatter.format(cal.getTime());
	       String mMonth = mDate.substring(5,7);
	       mDate = mDate.substring(0,4);

	       String sYear = "<select id='year1' name='year1' size='1' class='easyui-combobox' style='width:100px'>\r\n";
	       for (int i=2000;i<=Integer.parseInt(mDate);i++) {
	          if (i==Integer.parseInt(mDate)) {
	             sYear = sYear +"<option value='"+String.valueOf(i)+"' selected>"+String.valueOf(i)+"</option>\r\n";
	          } else {
	             sYear = sYear +"<option value='"+String.valueOf(i)+"'>"+String.valueOf(i)+"</option>\r\n";
	          }
	       }
	       sYear = sYear +"</select>\r\n";
	       addBuf.append("<TABLE width=\"700\" cellSpacing='0'>\r\n")
	             .append("<TR><td>&nbsp;\r\n")
	             .append("日期:"+sYear+"年\r\n"+"\r\n")
	             .append("<select id='Month' name='Month'  class='easyui-combobox' style='width:100px'>\r\n");
	       if (mMonth.equals("01")) {
	           addBuf.append("<option value='01' selected>一月</option>\r\n");
	       } else {
	           addBuf.append("<option value='01'>一月</option>\r\n");
	       }
	       if (mMonth.equals("02")) {
	           addBuf.append("<option value='02' selected>二月</option>\r\n");
	       } else {
	           addBuf.append("<option value='02'>二月</option>\r\n");
	       }
	       if (mMonth.equals("03")) {
	           addBuf.append("<option value='03' selected>三月</option>\r\n");
	       } else {
	           addBuf.append("<option value='03'>三月</option>\r\n");
	       }
	       if (mMonth.equals("04")) {
	           addBuf.append("<option value='04' selected>四月</option>\r\n");
	       } else {
	           addBuf.append("<option value='04'>四月</option>\r\n");
	       }
	       if (mMonth.equals("05")) {
	           addBuf.append("<option value='05' selected>五月</option>\r\n");
	       } else {
	           addBuf.append("<option value='05'>五月</option>\r\n");
	       }
	       if (mMonth.equals("06")) {
	           addBuf.append("<option value='06' selected>六月</option>\r\n");
	       } else {
	           addBuf.append("<option value='06'>六月</option>\r\n");
	       }
	       if (mMonth.equals("07")) {
	           addBuf.append("<option value='07' selected>七月</option>\r\n");
	       } else {
	           addBuf.append("<option value='07'>七月</option>\r\n");
	       }
	       if (mMonth.equals("08")) {
	           addBuf.append("<option value='08' selected>八月</option>\r\n");
	       } else {
	           addBuf.append("<option value='08'>八月</option>\r\n");
	       }
	       if (mMonth.equals("09")) {
	           addBuf.append("<option value='09' selected>九月</option>\r\n");
	       } else {
	           addBuf.append("<option value='09'>九月</option>\r\n");
	       }
	       if (mMonth.equals("10")) {
	           addBuf.append("<option value='10' selected>十月</option>\r\n");
	       } else {
	           addBuf.append("<option value='10'>十月</option>\r\n");
	       }
	       if (mMonth.equals("11")) {
	           addBuf.append("<option value='11' selected>十一月</option>\r\n");
	       } else {
	           addBuf.append("<option value='11'>十一月</option>\r\n");
	       }
	       if (mMonth.equals("12")) {
	           addBuf.append("<option value='12' selected>十二月</option>\r\n");
	       } else {
	           addBuf.append("<option value='12'>十二月</option>\r\n");
	       }
	       addBuf.append("</select></td>\r\n");
	       return addBuf.toString();
	}

	@Override
	public String getYearHtml() throws Exception {
        StringBuffer addBuf = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String mDate = formatter.format(cal.getTime());
        mDate = mDate.substring(0,4);

        String sYear = "<select  id='year1' name='year1' size='1' class='easyui-combobox' style='width:100px'>\r\n";
        for (int i=2000;i<=Integer.parseInt(mDate);i++) {
           if (i==Integer.parseInt(mDate)) {
              sYear = sYear +"<option value='"+String.valueOf(i)+"' selected>"+String.valueOf(i)+"</option>\r\n";
           } else {
              sYear = sYear +"<option value='"+String.valueOf(i)+"'>"+String.valueOf(i)+"</option>\r\n";
           }
        }
        sYear = sYear +"</select>\r\n";
        addBuf.append("<TABLE width=\"700\" cellSpacing='0'>\r\n")
              .append("<TR><td>&nbsp;\r\n")
              .append("年份:"+sYear+"年\r\n"+"\r\n")
              .append("</td>\r\n");
        return addBuf.toString();
	}

	@Override
	public String getDateDayHtml() throws Exception {
        StringBuffer addBuf = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String mDate = formatter.format(cal.getTime());
        String strDate1 = mDate.substring(0,4)+"-"+mDate.substring(5,7)+"-"+mDate.substring(8,10);
        
        addBuf.append("<TABLE width=\"750\" cellSpacing='0'>\r\n")
              .append("<TR><td>&nbsp;日期:\r\n")
              .append("</td><td>\r\n"+"<input type='text' name='date1'  id='date1' class='easyui-datebox' maxlength=10 value='")
              .append(strDate1+"' onkeypress='return CheckTime();' onblur='ValidDate(this);' onkeyup='onlyDate();'>")
			  .append("</td>\r\n");
        return addBuf.toString();
	}

	@Override
	public String getCodeHtml(String tablename) throws Exception {
        String strValue = "";
        String strSQL = "";
        String code = "";
        String name = "";
        if (tablename.length() > 0) {
            strValue = "<select id='code' name='code' size='1' class='easyui-combobox' style='width:105px'>\r\n";
            strSQL = "select CODE,NAME from "+tablename+" order by CODE";
            List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
            int length = retlist != null ? retlist.size() : 0;
            if (length > 0) {
               for (int i = 0; i < length; i++) {
            	   Map<String, Object> retmap = retlist.get(i);
                   code = retmap.get("CODE").toString();
                   name = retmap.get("NAME").toString();
                   if (i == 0) {
                       strValue = strValue +"<option value='"+code+"/"+name+"' selected>"+name+"</option>\r\n";
                   } else {
                       strValue = strValue +"<option value='"+code+"/"+name+"'>"+name+"</option>\r\n";
                   }
               }
            }
            strValue = strValue +"</select>\r\n";
        } else {
            strValue = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n";
        }
        return strValue;
	}

	@Override
	public String getYearComHtml() throws Exception {
        StringBuffer addBuf = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String mDate = formatter.format(cal.getTime());
        String strDate1 = mDate.substring(0,4);
        String strDate2 = mDate.substring(0,4);
        strDate2 = String.valueOf(Integer.parseInt(strDate2)-1);

        String sYear1 = "<select name='date2' id='date2' size='1' class='easyui-combobox' style='width:100px'>\r\n";
        for (int i = 2000; i <= Integer.parseInt(strDate1); i++) {
           if (i == Integer.parseInt(strDate1)) {
                sYear1 = sYear1 +"<option value='"+String.valueOf(i)+"' selected>"+String.valueOf(i)+"</option>\r\n";
           } else {
                sYear1 = sYear1 +"<option value='"+String.valueOf(i)+"'>"+String.valueOf(i)+"</option>\r\n";
           }
        }
        sYear1 = sYear1 +"</select>\r\n";
        String sYear2 = "<select name='date1' id='date1' size='1' class='easyui-combobox' style='width:100px'>\r\n";
        for (int i = 2000; i <= Integer.parseInt(strDate2); i++) {
           if (i == Integer.parseInt(strDate2)) {
                sYear2 = sYear2 +"<option value='"+String.valueOf(i)+"' selected>"+String.valueOf(i)+"</option>\r\n";
           } else {
                sYear2 = sYear2 +"<option value='"+String.valueOf(i)+"'>"+String.valueOf(i)+"</option>\r\n";
           }
        }
        sYear2 = sYear2 +"</select>\r\n";
        addBuf.append("<TABLE width=\"100%\" cellSpacing='0'>\r\n")
              .append("<TR><td width='60'>&nbsp;统计日期:\r\n")
              .append("</td><td>\r\n")
              .append("&nbsp;&nbsp;比较年份1")
              .append(sYear2+"\r\n")
              .append("&nbsp;比较年份2")
              .append(sYear1+"\r\n")
              .append("</td>\r\n"+"</tr></table>\r\n");
        return addBuf.toString();
	}

	@Override
	public String getYearMonthComHtml() throws Exception {
        StringBuffer addBuf = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String mDate = formatter.format(cal.getTime());
        String strDate1 = mDate.substring(0,4);
        String strDate2 = mDate.substring(0,4);
        strDate2 = String.valueOf(Integer.parseInt(strDate2)-1);
        String mMonth = mDate.substring(5,7);

        String sYear1 = "<select name='date2' id='date2' size='1' class='easyui-combobox' style='width:100px'>\r\n";
        for (int i=2000;i<=Integer.parseInt(strDate1);i++) {
           if (i==Integer.parseInt(strDate1)) {
                sYear1 = sYear1 +"<option value='"+String.valueOf(i)+"' selected>"+String.valueOf(i)+"</option>\r\n";
           } else {
                sYear1 = sYear1 +"<option value='"+String.valueOf(i)+"'>"+String.valueOf(i)+"</option>\r\n";
           }
        }
        sYear1 = sYear1 +"</select>\r\n";

        String sYear2 = "<select name='date1' id='date1' size='1' class='easyui-combobox' style='width:100px'>\r\n";
        for (int i=2000;i<=Integer.parseInt(strDate2);i++) {
           if (i==Integer.parseInt(strDate2)) {
                sYear2 = sYear2 +"<option value='"+String.valueOf(i)+"' selected>"+String.valueOf(i)+"</option>\r\n";
           } else {
                sYear2 = sYear2 +"<option value='"+String.valueOf(i)+"'>"+String.valueOf(i)+"</option>\r\n";
           }
        }
        sYear2 = sYear2 +"</select>\r\n";
        addBuf.append("<TABLE width=\"100%\" cellSpacing='0'>\r\n")
              .append("<TR><td width='60'>&nbsp;统计日期:\r\n")
              .append("</td><td>\r\n")
              .append("&nbsp;&nbsp;比较年月1")
              .append(sYear2+"\r\n")
              .append("<select id='Month1' name='Month1' size='1' class='easyui-combobox' style='width:100px'>\r\n");
        if (mMonth.equals("01")) {
            addBuf.append("<option value='01' selected>一月</option>\r\n");
        } else {
            addBuf.append("<option value='01'>一月</option>\r\n");
        }
        if (mMonth.equals("02")) {
            addBuf.append("<option value='02' selected>二月</option>\r\n");
        } else {
            addBuf.append("<option value='02'>二月</option>\r\n");
        }
        if (mMonth.equals("03")) {
            addBuf.append("<option value='03' selected>三月</option>\r\n");
        } else {
            addBuf.append("<option value='03'>三月</option>\r\n");
        }
        if (mMonth.equals("04")) {
            addBuf.append("<option value='04' selected>四月</option>\r\n");
        } else {
            addBuf.append("<option value='04'>四月</option>\r\n");
        }
        if (mMonth.equals("05")) {
            addBuf.append("<option value='05' selected>五月</option>\r\n");
        } else {
            addBuf.append("<option value='05'>五月</option>\r\n");
        }
        if (mMonth.equals("06")) {
            addBuf.append("<option value='06' selected>六月</option>\r\n");
        } else {
            addBuf.append("<option value='06'>六月</option>\r\n");
        }
        if (mMonth.equals("07")) {
            addBuf.append("<option value='07' selected>七月</option>\r\n");
        } else {
            addBuf.append("<option value='07'>七月</option>\r\n");
        }
        if (mMonth.equals("08")) {
            addBuf.append("<option value='08' selected>八月</option>\r\n");
        } else {
            addBuf.append("<option value='08'>八月</option>\r\n");
        }
        if (mMonth.equals("09")) {
            addBuf.append("<option value='09' selected>九月</option>\r\n");
        } else {
            addBuf.append("<option value='09'>九月</option>\r\n");
        }
        if (mMonth.equals("10")) {
            addBuf.append("<option value='10' selected>十月</option>\r\n");
        } else {
            addBuf.append("<option value='10'>十月</option>\r\n");
        }
        if (mMonth.equals("11")) {
            addBuf.append("<option value='11' selected>十一月</option>\r\n");
        } else {
            addBuf.append("<option value='11'>十一月</option>\r\n");
        }
        if (mMonth.equals("12")) {
            addBuf.append("<option value='12' selected>十二月</option>\r\n");
        } else {
            addBuf.append("<option value='12'>十二月</option>\r\n");
        }
        addBuf.append("</select>\r\n")
              .append("&nbsp;比较年月2")
              .append(sYear1+"\r\n")
              .append("<select id='Month2' name='Month2' size='1' class='easyui-combobox' style='width:100px'>\r\n");

        if (mMonth.equals("01")) {
            addBuf.append("<option value='01' selected>一月</option>\r\n");
        } else {
            addBuf.append("<option value='01'>一月</option>\r\n");
        }
        if (mMonth.equals("02")) {
            addBuf.append("<option value='02' selected>二月</option>\r\n");
        } else {
            addBuf.append("<option value='02'>二月</option>\r\n");
        }
        if (mMonth.equals("03")) {
            addBuf.append("<option value='03' selected>三月</option>\r\n");
        } else {
            addBuf.append("<option value='03'>三月</option>\r\n");
        }
        if (mMonth.equals("04")) {
            addBuf.append("<option value='04' selected>四月</option>\r\n");
        } else {
            addBuf.append("<option value='04'>四月</option>\r\n");
        }
        if (mMonth.equals("05")) {
            addBuf.append("<option value='05' selected>五月</option>\r\n");
        } else {
            addBuf.append("<option value='05'>五月</option>\r\n");
        }
        if (mMonth.equals("06")) {
            addBuf.append("<option value='06' selected>六月</option>\r\n");
        } else {
            addBuf.append("<option value='06'>六月</option>\r\n");
        }
        if (mMonth.equals("07")) {
            addBuf.append("<option value='07' selected>七月</option>\r\n");
        } else {
            addBuf.append("<option value='07'>七月</option>\r\n");
        }
        if (mMonth.equals("08")) {
            addBuf.append("<option value='08' selected>八月</option>\r\n");
        } else {
            addBuf.append("<option value='08'>八月</option>\r\n");
        }
        if (mMonth.equals("09")) {
            addBuf.append("<option value='09' selected>九月</option>\r\n");
        } else {
            addBuf.append("<option value='09'>九月</option>\r\n");
        }
        if (mMonth.equals("10")) {
            addBuf.append("<option value='10' selected>十月</option>\r\n");
        } else {
            addBuf.append("<option value='10'>十月</option>\r\n");
        }
        if (mMonth.equals("11")) {
            addBuf.append("<option value='11' selected>十一月</option>\r\n");
        } else {
            addBuf.append("<option value='11'>十一月</option>\r\n");
        }
        if (mMonth.equals("12")) {
            addBuf.append("<option value='12' selected>十二月</option>\r\n");
        } else {
            addBuf.append("<option value='12'>十二月</option>\r\n");
        }
        addBuf.append("</select>\r\n"+"</td>\r\n"+"</tr></table>\r\n");
        return addBuf.toString();
	}

	@Override
	public String getYearQuarterComHtml() throws Exception {
        StringBuffer addBuf = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String mDate = formatter.format(cal.getTime());
        String strDate1 = mDate.substring(0,4);
        String strDate2 = mDate.substring(0,4);
        strDate2 = String.valueOf(Integer.parseInt(strDate2)-1);
        String mMonth = mDate.substring(5,7);

        String sYear1 = "<select name='date2' id='date2' size='1' class='easyui-combobox' style='width:65px'>\r\n";
	    for (int i=2000;i<=Integer.parseInt(strDate1);i++) {
	      if (i==Integer.parseInt(strDate1)) {
	           sYear1 = sYear1 +"<option value='"+String.valueOf(i)+"' selected>"+String.valueOf(i)+"</option>\r\n";
	      } else {
	           sYear1 = sYear1 +"<option value='"+String.valueOf(i)+"'>"+String.valueOf(i)+"</option>\r\n";
	      }
	    }
	    sYear1 = sYear1 +"</select>\r\n";
	    String sYear2 = "<select name='date1' id='date1' size='1' class='easyui-combobox' style='width:65px'>\r\n";
	    for (int i=2000;i<=Integer.parseInt(strDate2);i++) {
	      if (i==Integer.parseInt(strDate2)) {
	           sYear2 = sYear2 +"<option value='"+String.valueOf(i)+"' selected>"+String.valueOf(i)+"</option>\r\n";
	      } else {
	           sYear2 = sYear2 +"<option value='"+String.valueOf(i)+"'>"+String.valueOf(i)+"</option>\r\n";
	      }
	    }
	    sYear2 = sYear2 +"</select>\r\n";
        addBuf.append("<TABLE width=\"100%\" cellSpacing='0'>\r\n")
              .append("<TR><td width='60'>&nbsp;统计日期:\r\n")
              .append("</td><td>\r\n")
              .append("&nbsp;&nbsp;比较日期1")
              .append(sYear2+"\r\n")
              .append("<select id='Quarter1' name='Quarter1' size='1' class='easyui-combobox' style='width:100px'>\r\n");

        if (mMonth.equals("01") || mMonth.equals("02") || mMonth.equals("03")) {
            addBuf.append("<option value='1' selected>第一季度</option>\r\n");
        } else {
            addBuf.append("<option value='1'>第一季度</option>\r\n");
        }
        if (mMonth.equals("04") || mMonth.equals("05") || mMonth.equals("06")) {
            addBuf.append("<option value='2' selected>第二季度</option>\r\n");
        } else {
            addBuf.append("<option value='2'>第二季度</option>\r\n");
        }
        if (mMonth.equals("07") || mMonth.equals("08") || mMonth.equals("09")) {
            addBuf.append("<option value='3' selected>第三季度</option>\r\n");
        } else {
            addBuf.append("<option value='3'>第三季度</option>\r\n");
        }
        if (mMonth.equals("10") || mMonth.equals("11") || mMonth.equals("12")) {
            addBuf.append("<option value='4' selected>第四季度</option>\r\n");
        } else {
            addBuf.append("<option value='4'>第四季度</option>\r\n");
        }
        addBuf.append("</select>\r\n")
              .append("&nbsp;比较日期2")
              .append(sYear1+"\r\n")
              .append("<select id='Quarter2' name='Quarter2' size='1' class='easyui-combobox' style='width:100px'>\r\n");

        if (mMonth.equals("01") || mMonth.equals("02") || mMonth.equals("03")) {
            addBuf.append("<option value='1' selected>第一季度</option>\r\n");
        } else {
            addBuf.append("<option value='1'>第一季度</option>\r\n");
        }
        if (mMonth.equals("04") || mMonth.equals("05") || mMonth.equals("06")) {
            addBuf.append("<option value='2' selected>第二季度</option>\r\n");
        } else {
            addBuf.append("<option value='2'>第二季度</option>\r\n");
        }
        if (mMonth.equals("07") || mMonth.equals("08") || mMonth.equals("09")) {
            addBuf.append("<option value='3' selected>第三季度</option>\r\n");
        } else {
            addBuf.append("<option value='3'>第三季度</option>\r\n");
        }
        if (mMonth.equals("10") || mMonth.equals("11") || mMonth.equals("12")) {
            addBuf.append("<option value='4' selected>第四季度</option>\r\n");
        } else {
            addBuf.append("<option value='4'>第四季度</option>\r\n");
        }
        addBuf.append("</select>\r\n"+"</td>\r\n"+"</tr></table>\r\n");
        return addBuf.toString();
	}

	@Override
	public String getShowHtml(String strID, String userID) throws Exception {
       StringBuffer addBuf = new  StringBuffer();
       StringBuffer strReHtml = new StringBuffer();
       ANALYSE_STATISTICS_MAIN Smain = null;
       Smain = getSmain(strID);
       int addnum = 0;

       //由于网上销售版取消统计模块路径的设置,利用自定义表单模板路径来处理。
       String strTemplat = SysPreperty.getProperty().LogFilePath;
       strTemplat = strTemplat.substring(0,strTemplat.length()-3)+"StatExcel/"+Smain.getTIMETEMPLATE();

       Parser parser = new Parser();
       Html html = null;
       try {
          parser.setURL(strTemplat);
          parser.setEncoding("UTF-8");
          html = (Html) (parser.elements().nextNode());
          LOGGER.info("解析装载模版成功");
       }
       catch (ParserException ex) {
    	   LOGGER.error("分析模板:", ex.toString());
    	   return "报表引擎发生错误，读取模版失败，请先生成默认模板或上传模板！<br>详细错误信息：<br>" + ex.toString();
       }
       //--------------------------
       String strYunit="";
       String strName="";
       String strShowName="";
       String openclick="";
       TableRow tr = null;
       TableColumn td = null;
       NodeList col = null;
       NodeList nl = null;
       TableTag tt = null;
       int cnum=0;
       String ISNUMBER="";
       String ADDFIELD="";
       ISNUMBER=Smain.getISNUMBER();
       ADDFIELD=Smain.getADDFIELD();
       if (ISNUMBER==null){ISNUMBER="0";}
       if (ADDFIELD==null){ADDFIELD="";}
       String [] addlist = ADDFIELD.split(",");
       String addtabname = Smain.getPLANARTABLE();
       String addfield = Smain.getPLANARFIELD();

       //得到表格显示的相关属性
       String TableShowList[][]=getTableShowList(strID);
       NodeList nlTable = html.searchFor(new TableTag().getClass(), true);

       //=====ftl  2016-11-8===================
       String strshow_data="var show_data=";//数据JSON

       //单双击事件JS数组
       String onDbClikeArray="var arrayJs=[";
       //存放全部填充的序号数组，已经填充的为空，为填充的有值
       Hashtable<String, Object> clonum=new Hashtable<String, Object>();

       String dataJs="";//数据填充JS
       String rowspan="",oldStr="",newStr="",StrName="",colspan="";
       int colNum=0; int length = 0;
       for (int i = 0; i < nlTable.size(); i++) {
            tt = (TableTag) nlTable.elementAt(i);
            //strReHtml.append("<table border='1' align=\"left\" cellSpacing='0' width='"+tt.getAttribute("width")+"' cellPadding='0' bordercolor='#FFFFFF'>");
            strReHtml.append("<table id=\"data\" class=\"easyui-datagrid\" data-options=\"url:'',method:'get',border:false,fit:true,singleSelect:true\">\r\n");
            strReHtml.append("<thead>\r\n");
            nl = tt.searchFor(new TableRow().getClass(), true);
            //循环<tr>
            for (int j = 0; j < nl.size(); j++) {
            	if(j < nl.size()-1){
            		strReHtml.append("<tr>\r\n");
            	}
                tr = (TableRow) nl.elementAt(j);
                col = tr.searchFor(new TableColumn().getClass(), true);
                //遍历<td>组件
                for (int k = 0; k < col.size(); k++) {
                	//字段名称，
                	oldStr=col.elementAt(k).getText().replaceAll("\\\\n", "").toLowerCase();//转换小写
                	oldStr=oldStr.replaceAll("td", "");//替换td
                	//字段名称
                	StrName=col.elementAt(k).getChildren().toString();
                	StrName=StrName.substring(StrName.indexOf("]):")+3, StrName.length());//截取字符
                	StrName=StrName.replaceAll("\\\\n", "").trim();//去除换行符
                	//获取占用的行数
                	if (oldStr.indexOf("rowspan")>-1) {
                		newStr=oldStr.substring(oldStr.indexOf("rowspan"), oldStr.length());
                		newStr=newStr.substring(newStr.indexOf("\"")+1, newStr.length());
                		rowspan=newStr.substring(0, newStr.indexOf("\""));
                	} else {
                		rowspan="1";
                	}
                	//获取占用几列colspan
                	if (oldStr.indexOf("colspan")>-1) {
                		newStr=oldStr.substring(oldStr.indexOf("colspan"), oldStr.length());
                		newStr=newStr.substring(newStr.indexOf("\"")+1, newStr.length());
                		colspan=newStr.substring(0, newStr.indexOf("\""));
                		if(Integer.valueOf(rowspan)>=col.size()){
                			colspan="0";
                		} else {
                			for(int o=0;o<Integer.valueOf(colspan);o++){
                				//判断是否当前引用clonum
                        		clonum.put((colNum+o)+"", ""+(colNum+o));
                			}
                		}
                	} else {
                		colspan="0";
                	}
                	colNum=Integer.valueOf(colspan)+colNum;
                	//替换td==th
            		oldStr=col.elementAt(k).getText().replaceAll("\\\\n", "").toLowerCase();//转换小写
                	oldStr=oldStr.replaceAll("td", "<th");//替换td
                	//头部
                	if(j<nl.size()-1){
                    	//判断是否为最后一行
                    	if(j==nl.size()-2){
                    		//比较满意用到的字段
                    		boolean fa=false;
                    		for(int o=0;o<clonum.size();o++){
                    			if(clonum.get(""+o).toString().length()>0){
                    				oldStr+=" data-options=\"field:'COL"+clonum.get(""+o)+"',align:'center'\">"+StrName+"</th>";
                            		//判断是否当前引用clonum
                            		clonum.put(o+"", "");
                            		fa=true;
                            		break;
                    			}
                    		}
                    		if(!fa){
                    			oldStr+=" data-options=\"field:'COL"+colNum+"',align:'center'\">"+StrName+"</th>";
                        		//判断是否当前引用clonum
                        		clonum.put(colNum+"", "");
                        		colNum++;
                    		}
                    	}else if(((Integer.valueOf(rowspan)+j)==(nl.size()-1))&&j<nl.size()-2){
                    		oldStr+=" data-options=\"field:'COL"+colNum+"',align:'center'\">"+StrName+"</th>";
                    		//判断是否当前引用clonum
                    		clonum.put(colNum+"", "");
                    		colNum++;
                    	}else{
                    		oldStr+=">"+StrName+"</th>";
                    	}
                    	strReHtml.append(oldStr+"\r\n");
                	}
                }
                if(j < nl.size()-1){
                	strReHtml.append("</tr>\r\n");
            	}
                //生成数据
                if (j==nl.size()-1){
                  //-----------------------------------------
                  //删除临时统计表中本用户统计的不相关的数据
                  addBuf.delete(0,addBuf.length());//清空
                  addBuf.append("delete from ANALYSE_STATISTICS_RESULT Where (SUSERNO='"+userID+"' and FID<>'"+strID+"') or FID is null");
                  userMapper.deleteExecSQL(addBuf.toString());
                  addBuf.delete(0,addBuf.length());//清空
                  addBuf.append("Select * From ANALYSE_STATISTICS_RESULT Where SUSERNO='"+userID+"' order by ID");
                  List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
                  length = retlist != null ? retlist.size() : 0;
                  if (length > 0) {
                	  colNum=0;
                	  strshow_data += "{\"total\":" + length + ",\"rows\":[";
                      for (int a1 = 0; a1 < length; a1++) {
                    	  Map<String, Object> retmap = retlist.get(a1);
                    	  //=============ftl start======
                    	  //=============ftl end  ======
                          cnum = 0;
                          strYunit = retmap.get("FIELD50").toString();
                          if (strYunit==null){strYunit="";}
                          strshow_data = strshow_data + "{";
                          addnum = 0;
                          for (int a2=0;a2<TableShowList.length;a2++){
                              ///====================================
                              if (ISNUMBER.equals("1")&& a2==0){//增加序号列
                            	  strshow_data = strshow_data + "\"COL"+a2+"\":\"" +(a1+1)+ "\",";
                            	  addnum++;
                                  cnum=cnum+1;
                      		  }
                              if (ADDFIELD.length()>0 && a2==1){//有附加字段时=COL2开始
                                  for (int add=0;add<addlist.length;add++){
                                     strName= addlist[add];
                                     strShowName = getAddValue(addtabname,strName,addfield,strYunit);
                                     strshow_data = strshow_data + "\"COL"+(a2+add+1)+"\":\"" +strShowName+ "\",";
                                     addnum++;
                                     cnum=cnum+1;
                                  }
                              }
                              ///==================================================
                              strName=TableShowList[a2][0];//字段名称
                              strShowName= retmap.get(strName).toString();//对应的数据
                              
                              openclick=strShowName;
                              if (strShowName==null){
                                 strShowName = "&nbsp;";
                              }
                              if (strShowName.length()==0){
                                 strShowName = "&nbsp;";
                              }
                              //单元格事件                              
                              if (a2>0 && !strYunit.equals("0")){
                                strShowName = "<a style='color:#000000;cursor:pointer;text-decoration:none' href=javascript:opendetail('" +strYunit + "','" + openclick + "','"+String.valueOf(a2)+"');>"+strShowName+"</a>";
                              }
                              
              				  if (a2 < TableShowList.length - 1) {
              					  strshow_data = strshow_data + "\"COL"+(addnum+a2)+"\":\"" +strShowName+ "\",";
              				  } else {
              					  strshow_data = strshow_data + "\"COL"+(addnum+a2)+"\":\"" +strShowName+ "\"";
              				  }
                              try {
	                            try {
	                                td = (TableColumn) col.elementAt(a2);
	                                if (issub(strID,strYunit).equals("1") && !strYunit.equals("0")){//有子级时
	                                    td.setAttribute("ondblclick","opencompute('" +strYunit + "','" + openclick + "','"+String.valueOf(a2+cnum+1)+"','1');");
	                                  //================================================================================
	                                  //加载单双击事件数组
	                                    if (a2==0) {
	                                    	 onDbClikeArray+="\"opencompute('" +strYunit + "','" + openclick + "','"+String.valueOf(a2+cnum+1)+"','1');\"";
	                    				 }
	                                } else {
	                                    td.setAttribute("ondblclick","");
	                                }
	                            } catch (Exception ex2) {
	                                LOGGER.error("出错："+ex2.toString());
	                            }
                                //col.elementAt(cnum).getChildren().removeAll();
                                //col.elementAt(cnum).getChildren().add(tmp);
                              } catch (Exception ex1) {
                            	  LOGGER.error("读取计算结果出错："+ex1.toString());
                              }
                              cnum=cnum+1;
                          }
                          //封装<tr>
                          if (a1 < length - 1) {
        					  	strshow_data += "},\r\n";
        					  	onDbClikeArray+=",";
        				  } else {
        						strshow_data += "}";
        				  }
                       }
                       strshow_data = strshow_data + "]}";
                       onDbClikeArray+="];";
                  }else{
                	  strshow_data += "{\"total\":0,\"rows\":[]}";
                  }
                  //--------------------------------
                }else{
                    tr.setAttribute("class","Window_TbT");
                    //strReHtml.append(tr.toHtml());
                }
            }
        }
        //填充javascript，用于填充统计数据
        dataJs+="<script type=\"text/javascript\">\r\n";
        dataJs+=strshow_data+";\r\n";
        dataJs+="////将数据绑定到datagrid\r\n";
        dataJs+="function setTabeDate(){\r\n";
        dataJs+=" $('#data').datagrid('loadData', show_data);  \r\n";
        dataJs+="}\r\n";
        dataJs+="setTimeout(\"setTabeDate();\",500);\r\n";

        //增加javascript单双击事件
        dataJs+="\r\n\r\n\r\n";
        dataJs+="//增加js单双击事件\r\n";
        //赋值数组onDbClikeArray
        if(onDbClikeArray.length()<=13){
        	onDbClikeArray="var arrayJs=[];";
        }
        dataJs+=onDbClikeArray+"\r\n";
        dataJs+="$(function(){ \r\n";
        dataJs+="$('#data').datagrid({\r\n";
        dataJs+="//单击事件   \r\n";
        dataJs+="onClickRow:function(rowIndex,rowData){\r\n";
        dataJs+="//alert(rowIndex+\"===\"+arrayJs.length);\r\n";
        dataJs+="},\r\n";
        dataJs+="//双击事件\r\n";
        dataJs+="onDblClickRow:function(rowIndex,rowData){\r\n";//
        dataJs+="  //首先查看是否有双击事件\r\n";
        //dataJs+="  alert(rowIndex+\"--===--\"+rowData);\r\n";
        //dataJs+="  alert(arrayJs[rowIndex]);\r\n";
        dataJs+="  if(arrayJs.length>0){\r\n";
        dataJs+="     eval(arrayJs[rowIndex]);\r\n";
        dataJs+="  }\r\n";
        //dataJs+="  alert(rowIndex+\"--===--\"+arrayJs.length);\r\n";
        dataJs+="}\r\n";
        dataJs+="});\r\n";
        dataJs+="});\r\n";

        dataJs+="</script>\r\n";

        //System.out.println(strshow_data);
        //----------------------------
        strReHtml.append("</table>\r\n"+dataJs);
        String retHtml = strReHtml.toString();
        //System.out.println(retHtml);
        return retHtml;
	}

	/**
	 * ftl  调整（生成默认模板）
	 * @param ID
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean savetemplaet(String ID) throws Exception {
        StringBuffer strRet = new StringBuffer();
        String strName ="",add1="",add2="";
        ANALYSE_STATISTICS_MAIN Smain = getSmain(ID);
        
        String ISNUMBER="";
        String ADDFIELD="";
        String tmpName = "";
        ISNUMBER=Smain.getISNUMBER();
        ADDFIELD=Smain.getADDFIELD();
        if (ISNUMBER==null){ISNUMBER="0";}
        if (ADDFIELD==null){ADDFIELD="";}
        String [] addlist = ADDFIELD.split(",");
        
        strRet.append("<html>\r\n");
        strRet.append("<head>\r\n");
        strRet.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n");
        strRet.append("<title></title>\r\n");
        strRet.append("</head>\r\n");
        strRet.append("<body>\r\n");
        strRet.append("<table border=\"1\" id=\"table1\">\r\n");
        
        add1="<tr>\r\n";
        add2="<tr>\r\n";
//        if (ISNUMBER.equals("1")) {//增加序号列
//             add1=add1+"<td width=\"60\" height=\"30\">\r\n序号\r\n</td>\r\n";
//             add2=add2+"<td width=\"60\" height=\"28\">\r\n</td>\r\n";
//        }
        //得到表格显示的相关属性
        String TableShowList[][]=getTableShowList(ID);
        for (int a1=0;a1<TableShowList.length;a1++) {
             if (ADDFIELD.length()>0 && a1==1)//有附加字段时
             {
                 for (int add=0;add<addlist.length;add++) {
                        tmpName=getTABLEFIELDNAME(Smain.getPLANARTABLE(),addlist[add]);
                        if (tmpName.equals("0")){tmpName="附加字段["+addlist[add]+"]";}
                        strName= tmpName;
                        add1=add1+"<td width=\"120\" height=\"30\">\r\n"+strName+"\r\n</td>\r\n";
                        add2=add2+"<td width=\"120\" height=\"28\">\r\n</td>\r\n";
                 }
             }
             strName=TableShowList[a1][1];
             add1=add1+"<td width=\"120\" height=\"30\">\r\n"+strName+"\r\n</td>\r\n";
             add2=add2+"<td width=\"120\" height=\"28\">\r\n</td>\r\n";
        }
        add1=add1 + "</tr>\r\n";
        add2=add2 + "</tr>\r\n";
        
        strRet.append(add1);
        strRet.append(add2);
        strRet.append("</table>\r\n");
        strRet.append("</body>\r\n");
        strRet.append("</html>\r\n");
        
        //-----生成模板文件-------------
        File tempFile1;
        boolean bFile1;
        
       String strTemplat = SysPreperty.getProperty().LogFilePath;
       strTemplat = strTemplat.substring(0,strTemplat.length()-3)+"StatExcel/"+ID+".htm";
       tempFile1 = new File(strTemplat);
       tempFile1.delete();
       try {
          bFile1 = tempFile1.createNewFile();
          if (bFile1) {
//            FileWriter fw1 = new FileWriter(strTemplat);
            Writer fw1 = new OutputStreamWriter(new FileOutputStream(tempFile1), "UTF-8");
            fw1.write(strRet.toString(), 0, strRet.toString().length());
            fw1.flush();
            fw1.close();
          }
         } catch (IOException ex) {
        	 ex.printStackTrace();
         }
         String strSql = "Update ANALYSE_STATISTICS_MAIN Set TIMETEMPLATE = '"+ID+".htm' Where ID='"+ID+"'";
         userMapper.updateExecSQL(strSql);
         doMemory_Manage();// 内存清理
         return true;
	}

	@Override
	public String[] getTHTitle(String ID) throws Exception {
	     int anum=0,znum=0,xnum=0,cnum=0,ycnum=0;
	     String ADDFIELD ="",ISSHOW="",tmpName="",isnumber="";
	     String SFIELDSHOWNAME ="";
	     ANALYSE_STATISTICS_MAIN STATISTICS = getSmain(ID);
	     ADDFIELD = STATISTICS.getADDFIELD();
	     isnumber= STATISTICS.getISNUMBER();
	     if (isnumber==null){isnumber="0";}
	     
	     if (ADDFIELD==null){ADDFIELD="";}
	     if (ADDFIELD.length()==0){
	    	 znum=1; xnum=1;
	     } else {
	        if (ADDFIELD.indexOf(",")>-1) {
	          try {
	            znum = 1 + ADDFIELD.split(",").length;
	            xnum = 1 + ADDFIELD.split(",").length;
	          } catch (Exception ex) {
	            LOGGER.error("出错："+ex.toString());
	          }
	        } else {
	          znum = 2;
	          xnum = 2;
	        }
	     }
	     //统计计算字段列表
	     ANALYSE_STATISTICS_CFIELD CField[] = null;
	     CField = getResultCFieldStr(ID);
	     for (int i = 0; i < CField.length; i++) {
	         ISSHOW = CField[i].getISSHOW();
	         if (ISSHOW==null){ISSHOW="0";}
	         if (ISSHOW.equals("0")){anum=anum+1;}
	     }
	     znum =  znum + anum;
	     if (isnumber.equals("1")){
	       znum = znum + 1;
	       cnum = 1;
	     }
	     String result[] = new String[znum];
	     if (isnumber.equals("1")){
	       result[0] = "序号";
	     }
	     tmpName=getTABLEFIELDNAME(STATISTICS.getTABLEID(),STATISTICS.getCPLANARFIELD());
	     if (tmpName.equals("0")){tmpName="二维名称";}
	     result[cnum] = tmpName;
	     if (ADDFIELD.length()>0) {
	        String [] tepvalue = ADDFIELD.split(",");
	        for (int k = 1; k < xnum; k++) {
	          tmpName=getTABLEFIELDNAME(STATISTICS.getPLANARTABLE(),tepvalue[k-1]);
	          if (tmpName.equals("0")){tmpName=tepvalue[k-1];}
	          result[k+cnum] = tmpName;
	        }
	     }
	     ycnum=0;
	     for (int i = 0; i < CField.length; i++) {
	         ISSHOW = CField[i].getISSHOW();
	         if (ISSHOW==null){ISSHOW="0";}
	         if (ISSHOW.equals("0")){
	            SFIELDSHOWNAME = CField[i].getSFIELDSHOWNAME();
	            if (SFIELDSHOWNAME==null){SFIELDSHOWNAME="无标题";}
	            LOGGER.info("存储序号:"+String.valueOf((i-ycnum)+xnum+cnum));
	            LOGGER.info("存储值:"+SFIELDSHOWNAME);
	            try {
	              result[ (i - ycnum) + xnum + cnum] = SFIELDSHOWNAME;
	            } catch (Exception ex1) {
	            	LOGGER.info("存储出错:"+ex1.toString());
	            }
	         } else {
	           ycnum=ycnum+1;
	         }
	     }
	     return result;
	}

	@Override
	public String[] getIsNumber(String ID) throws Exception {
	   int anum=0,znum=0,xnum=0,cnum=0,ycnum=0;
	   String ADDFIELD ="",ISSHOW="",isnumber="";
	   ANALYSE_STATISTICS_MAIN STATISTICS = getSmain(ID);
	   ADDFIELD = STATISTICS.getADDFIELD();
	   isnumber= STATISTICS.getISNUMBER();
	   if (isnumber==null){isnumber="0";}
	      if (ADDFIELD==null){ADDFIELD="";}
	      if (ADDFIELD.length()==0){
	    	  znum=1;xnum=1;
	      } else {
	         if (ADDFIELD.indexOf(",")>-1) {
	          try {
	            znum = 1 + ADDFIELD.split(",").length;
	            xnum = 1 + ADDFIELD.split(",").length;
	          }
	          catch (Exception ex) {
	            LOGGER.error("出错："+ex.toString());
	          }
	         } else {
	           znum = 2;
	           xnum = 2;
	         }
	      }
	      //统计计算字段列表
	      ANALYSE_STATISTICS_CFIELD CField[] = null;
	      CField = getResultCFieldStr(ID);
	      for (int i = 0; i < CField.length; i++) {
	          ISSHOW = CField[i].getISSHOW();
	          if (ISSHOW==null){ISSHOW="0";}
	          if (ISSHOW.equals("0")){anum=anum+1;}
	      }
	      znum = znum + anum;
	      if (isnumber.equals("1")){
	       znum = znum + 1;
	       cnum = 1;
	     }
	      String result[] = new String[znum];
	      if (isnumber.equals("1")){
	       result[0] = "0";
	      }
	      result[cnum] = "0";
	      if (ADDFIELD.length()>0) {
	         for (int k = 1; k < xnum; k++) {
	            result[k+cnum] = "0";
	         }
	      }
	    ycnum=0;
	    for (int i = 0; i < CField.length; i++) {
          ISSHOW = CField[i].getISSHOW();
          if (ISSHOW==null){ISSHOW="0";}
          if (ISSHOW.equals("0")) {
             result[(i - ycnum)+xnum+cnum] = "1";
          } else {
             ycnum=ycnum+1;
          }
	    }
	    return result;
	}

	@Override
	public String[][] getExcelData(String strID, String userID) throws Exception {
	    ANALYSE_STATISTICS_MAIN Smain = getSmain(strID);
	    int addnum=0;
	    String strsql ="";
	    
	    //--------------------------
	    String strYunit="";
	    String strName="";
	    String strShowName="";
	    
	    String ISNUMBER="";
	    String ADDFIELD="";
	    ISNUMBER=Smain.getISNUMBER();
	    ADDFIELD=Smain.getADDFIELD();
	    if (ISNUMBER==null){ISNUMBER="0";}
	    if (ADDFIELD==null){ADDFIELD="";}
	    String [] addlist = ADDFIELD.split(",");
	    String addtabname = Smain.getPLANARTABLE();
	    String addfield = Smain.getPLANARFIELD();
	    String reList[][]=null;
	    
	    //得到表格显示的相关属性
	    String TableShowList[][]=getTableShowList(strID);
	    strsql ="Select * From ANALYSE_STATISTICS_RESULT Where SUSERNO='"+userID+"' order by ID";
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strsql);
	    int length = retlist != null ? retlist.size() : 0;
        if (length > 0){
           reList = new String[length][getIsNumber(strID).length];
           for (int a1 = 0; a1 < length; a1++) {
        	   	Map<String, Object> retmap = retlist.get(a1);
                strYunit = retmap.get("FIELD50").toString();
                if (strYunit==null){strYunit="";}
                 
                addnum = 0;
                if (ISNUMBER.equals("1")) {//增加序号列
                     reList[a1][addnum]= String.valueOf(a1+1);
                     addnum++;
                }
                if (TableShowList.length>0) {
                	strShowName = retmap.get("FIELD1").toString();
                	reList[a1][addnum] = strShowName;
                	addnum++;
                }
                if (ADDFIELD.length()>0) {//有附加字段时
                   for (int add=0;add<addlist.length;add++) {
                      strName= addlist[add];
                      strShowName = getAddValue(addtabname,strName,addfield,strYunit);
                      if (strYunit.equals("0")){strShowName="";}
                      
                      reList[a1][addnum]= strShowName;
                      addnum++;
                   }
                }
                for (int a2=1;a2<TableShowList.length;a2++) {
                   strName=TableShowList[a2][0];
                   strShowName= retmap.get(strName).toString();
                   if (strShowName==null) {
                      strShowName = "";
                   }
                   if (strShowName.length()==0) {
                      strShowName = "";
                   }
                   reList[a1][addnum] = strShowName;
                   addnum++;
               }
           }
        }
	    return reList;
	}

    /**
    * ftl  调整（功能:计算二维报表(二维关系为表时)）
    * @param strID 统计配置ID
    * @param strP1 参数1
    * @param strP2 参数2
    * @param strP3 参数3
    * @param strP4 参数4
    * @param strP5 参数5
    * @param UNITID 默认单位id
    * @param strConnValue 自定义条件
    * @param String UNITID 默认单位编号
    * @param String strAuto 1 自动运算报表 2实时运算报表
    * @return
     * @throws Exception 
    */
   @SuppressWarnings({ "unchecked", "rawtypes", "unused" })
   private void getCompute2(String strID, String P1, String P2, String P3, String P4, 
		   String P5, SessionUser user, String strConnValue, String UNITID) throws Exception {
	     
         String strSQL = "";
         List arrList1 = new ArrayList();
         List arrList2 = new ArrayList();
         StringBuffer addBuf = new StringBuffer();
         StringBuffer tmpBuf = new StringBuffer();
         StringWork sw = new StringWork();
         //得到统计配置表的相关属性
         ANALYSE_STATISTICS_MAIN Smain = null;
         Smain = getSmain(strID);
         //维数据对应的表
         String strPLANARTABLE=Smain.getPLANARTABLE();
         //维数据对应的关联字段
         String strPLANARFIELD=Smain.getPLANARFIELD();
         //当前表与维数据对应的关联字段
         String strCPLANARFIELD=Smain.getCPLANARFIELD();
         //维数据的条件
         String strWHEREVALUE=Smain.getWHEREVALUE();
         //维数据保存对应字段
         String strWSAVEFIELD="FIELD1";
         //维数据对应的关联中文字段
         String strPLANARFIELDNAME=Smain.getPLANARFIELDNAME();
         //统计的单位编码位数
         int strUNITLEN=2;
         String isunit = Smain.getISUNIT();
         if (isunit == null){isunit="0";}
         //得到是否显示空行
         String ISZERO = Smain.getISZERO();
         if (ISZERO == null){ISZERO="0";}
         String shownow ="0";
         //用用户的Custom5来记录行政区划
         String Custom5 = user.getCustom5();
         String rowsum = "0";
         if (Custom5==null){Custom5="";}
         strUNITLEN = Custom5.length()+2;
         LOGGER.info("getCompute2默认单位ID:"+UNITID);
         //有默认单位时---------------
         if (UNITID.length() > 0) {
            String tmpunit = UNITID;
            tmpunit = sw.CutLastZero(tmpunit, 2);//按2位去掉后面的0
            strUNITLEN = tmpunit.length()+2;
         }
         //有默认单位时----------------
         //配置关系表字符串
         String strTable = getFromTableStr(strID);
         LOGGER.info("getCompute2关联表字符串:"+strTable);
         //关联表关系字串
         String strConn = "";
         //统计计算字段列表
         ANALYSE_STATISTICS_CFIELD CField[] = null;
         CField = getResultCFieldStr(strID);
         //统计初始条件字符串
         String strInitCon = getInitCondition(strID,P1,P2,P3,P4,P5,user);
         LOGGER.info("初始条件:"+strInitCon);
         //计算维数据的条件表达式
         if (strWHEREVALUE.length()>0) {
        	 strWHEREVALUE = prepareExpression(strWHEREVALUE,P1,P2,P3,P4,P5,user);
         }
         LOGGER.info(strWHEREVALUE);

         String strPLANARVALUE="";//数据维的值
         String strPLANARFIELDNAMEVALUE="";//维数据对应的关联中文字段的值
         String strEXPRESSIONSWHERE="";//表达式的运算条件

         String strSENDTABLE = "0";//得到统计结果待保存的表
         String strTableName = "";//表名
         String strTableKey = "";//主键
         String strMaxNo = "";//最大记录号
         String strFIELDVALUE ="";//字段的值
         String strFIELDCODE ="";//字段的值code
         String expression ="";//待计算的表达式
         String expression1 ="";//待计算的表达式1
         String CFields ="";//计算字段名称及结果串
         String strTmp="";//临时变量
         String strIsSum="0";//是否有特殊的计算函数
         String strSumSql="";//特殊的计算函数sql
         int dbTypeValue=1;//字段的数据类型
         int intlen = 0;//主键的长度
         String ComFromTableStr ="";//计算字段关联表串
         String ComConnectionStr ="";//计算字段关联字符串
         String strInitCCon ="";//计算字段的初始条件
         ItemField itemField1=null;
         String comWHEREVALUE = "";
         String strCPLANARESPECIAL = "";

         //当前表与维数据表对应字段是否为表名.字段名的格式
         if (strCPLANARFIELD.lastIndexOf(".") > -1) {
        	 LOGGER.info("当前表与维数据表对应字段已经是表名.字段名的格式。");
         } else {
        	 strCPLANARFIELD = Smain.getTABLEID()+"."+strCPLANARFIELD;
         }
         addBuf.delete(0,addBuf.length());//清空
         addBuf.append("select * from "+strPLANARTABLE+" where 1=1");

         if (strPLANARTABLE.toLowerCase().indexOf("bpip_unit") == -1 || isunit.equals("0")) {//非单位表
        	 if (UNITID.length()>0) {
        		 if (strWHEREVALUE.length()>0) {
                      addBuf.append(" and ("+strWHEREVALUE+") and "+strPLANARFIELD+" like '"+UNITID+"%'");
                      tmpBuf.delete(0,tmpBuf.length());//清空
                      tmpBuf.append(" in (select "+strPLANARFIELD+" from ")
                          	.append(strPLANARTABLE+" where ("+strWHEREVALUE+") and "+strPLANARFIELD+" like '"+UNITID+"%')");
                      comWHEREVALUE = tmpBuf.toString();
        		 } else {
        			 addBuf.append(" and "+strPLANARFIELD+" like '"+UNITID+"%'");
                     tmpBuf.delete(0,tmpBuf.length());//清空
                     tmpBuf.append(" in (select "+strPLANARFIELD+" from ")
                           .append(strPLANARTABLE+" where "+strPLANARFIELD+" like '"+UNITID+"%')");
                     comWHEREVALUE = tmpBuf.toString();
        		 }
        	 } else {
        		 if (strWHEREVALUE.length()>0) {
                    addBuf.append(" and "+strWHEREVALUE);
                    tmpBuf.delete(0,tmpBuf.length());//清空
                    tmpBuf.append(" in (select "+strPLANARFIELD+" from ")
                          .append(strPLANARTABLE+" where "+strWHEREVALUE+")");
                    comWHEREVALUE = tmpBuf.toString();
        		 }                
        	 }
         }
         if (strPLANARTABLE.toLowerCase().indexOf("bpip_unit") > -1 || isunit.equals("1")) {//维数据对应的表为单位表
           if (strWHEREVALUE.length()>0) {
              addBuf.append(" and "+strWHEREVALUE);
              tmpBuf.delete(0,tmpBuf.length());//清空
              tmpBuf.append(" in (select "+strPLANARFIELD+" from ")
                    .append(strPLANARTABLE+" where "+strWHEREVALUE+")");
              comWHEREVALUE = tmpBuf.toString();
           }
           String strZreo="";
           String strZreo1="";
           //维数据项是否包含统计单位
           String strWISMATCH = Smain.getWISMATCH();
           for(int i=0; i<12-strUNITLEN; i++) {
        	   strZreo=strZreo+"0";
           }
           strZreo1=strZreo+"00";
           if (strWISMATCH.equals("1")) {
              addBuf.append(" and "+strPLANARFIELD+" like '%"+strZreo+"'");
              if (UNITID.length()>0) {
            	  addBuf.append(" and "+ strPLANARFIELD+" like '"+UNITID+"%'");
              }
              tmpBuf.delete(0,tmpBuf.length());//清空
              tmpBuf.append(" <>'000000000000' ");
              comWHEREVALUE = tmpBuf.toString();
         } else {
              addBuf.append(" and "+strPLANARFIELD+" like '%"+strZreo)
                    .append("' and "+strPLANARFIELD+" not like '%"+strZreo1+"'");
              if (UNITID.length()>0) {
                addBuf.append("  and "+ strPLANARFIELD+" like '"+UNITID+"%'");
              }
              tmpBuf.delete(0,tmpBuf.length());//清空
              tmpBuf.append(" in (select "+strPLANARFIELD+" from "+strPLANARTABLE)
                    .append(" where ")
                    .append(strPLANARFIELD+" not like '%"+strZreo1)
                    .append("')");
              comWHEREVALUE = tmpBuf.toString();
         }
         }
         addBuf.append(" order by "+strPLANARFIELD);
         strSQL =addBuf.toString();
         //替换字段中的[]
         strSQL=strSQL.replaceAll("\\[","");
         strSQL=strSQL.replaceAll("\\]","");
         //-------------------
         LOGGER.info("维数据的sql:"+strSQL);
         if (strSENDTABLE.equals("0")) {//存放在统计临时结果表中
             strTableName = "ANALYSE_STATISTICS_RESULT";
             strTableKey = "ID";
             intlen = 12;

             //删除临时结果表里的个人统计数据
             addBuf.delete(0,addBuf.length());//清空
             addBuf.append("delete from ANALYSE_STATISTICS_RESULT where SUSERNO='"+user.getUserID())
                     .append("' and FID='"+strID+"'");
             //dbEngine.ExecuteSQL(addBuf.toString());
             userMapper.deleteExecSQL(addBuf.toString());
         }    
         //存放各列的总数，用于计算平均值和总和
         int len=0;
         if (CField != null) {
            len=CField.length;
         }
         String SUMrow[]=new String[len+10];
         //赋初值为0
         for (int y=0;y<len+10;y++) {
            SUMrow[y]="0";
         }
         //---------------------------
         String strCPLANARFIELD1="";

         //-------------------------处理分组的情况代码----------------------------
         //定义一个二维数组来存放结果
         String[][] resultList = null;
         //定义编号对应行的对照表
         Hashtable<String, Object> CodeHashtable = new Hashtable<String, Object>();
         //列序号对应保存字段对照表
         Hashtable<String, Object> SaveHashtable = new Hashtable<String, Object>();

         int Arrange = 1;
         int CArrange = 1;
         //得到是否显示平均值统计行
         String strISAGV = Smain.getISAGV();
         //得到是否显示合计统计行
         String strISSUM = Smain.getISSUM();
         int arrrow = 0;
         int datarow = 0;
         String AGVSUMtype="1";
         int downRow=0;
         
         //mdbset0 = dbEngine.QuerySQL(strSQL);
         List<Map<String, Object>> mdbset0 = userMapper.selectListMapExecSQL(strSQL);
         if (mdbset0 != null && mdbset0.size()>0){
            //(列多加的10列防止计算列中间断号时数组溢出的问题)
            arrrow = mdbset0.size();
            datarow = arrrow;
            if (strISAGV.equals("1"))
            {arrrow = arrrow + 1;
            }
            if (strISSUM.equals("1"))
            {arrrow = arrrow + 1;
            }

            resultList = new String[arrrow][CField.length+10];
            for (int i=0;i<mdbset0.size();i++)
            {
                arrList1 = getArrayList(strPLANARFIELD,".");
                if (arrList1.size()==2)
                {strPLANARFIELD=arrList1.get(1).toString();}
                arrList1 = getArrayList(strPLANARFIELDNAME,".");
                if (arrList1.size()==2)
                {strPLANARFIELDNAME=arrList1.get(1).toString();}

                strPLANARVALUE= mdbset0.get(i).get(strPLANARFIELD).toString();
                strPLANARFIELDNAMEVALUE= mdbset0.get(i).get(strPLANARFIELDNAME).toString();
                //单位表时取前面的单位编号
                if (strPLANARTABLE.toLowerCase().indexOf("bpip_unit") > -1 || isunit.equals("1")) {//维数据对应的表为单位表
                    //编号对应行的对照表
                    strPLANARVALUE=strPLANARVALUE.substring(0,strUNITLEN);
                    CodeHashtable.put(strPLANARVALUE,String.valueOf(i));
                } else {
                    //编号对应行的对照表
                    CodeHashtable.put(strPLANARVALUE,String.valueOf(i));
                }
                //第一列存维数据表的对应字段名称
                resultList[i][0] = strPLANARFIELDNAMEVALUE;
                //将其它列初始化为0
                for (int j=1;j<CField.length+10;j++) {
                    resultList[i][j] = "0";
                }
                //&&&&&&&&&&&&&&&&&&&&&&&&&
                //if (strPLANARTABLE.toLowerCase().indexOf("bpip_unit") > -1)//维数据对应的表为单位表
                //{
                    resultList[i][CField.length+9] = strPLANARVALUE;
                //}
                //&&&&&&&&&&&&&&&&&&&&&&&&&
            }

            //平均值或总和行初始化为0-----------------
            if (strISAGV.equals("1") && strISSUM.equals("1")) {
                resultList[arrrow-2][0] = "平均值";
                resultList[arrrow-1][0] = "合  计";
                for (int j=1;j<CField.length+10;j++) {
                   resultList[arrrow-2][j] = "0";
                   resultList[arrrow-1][j] = "0";
                }
                AGVSUMtype="1";
                downRow=2;
            } else {
                if (strISAGV.equals("1")) {
                   resultList[arrrow-1][0] = "平均值";
                   AGVSUMtype="2";
                   downRow=1;
                }
                if (strISSUM.equals("1")) {
                   resultList[arrrow-1][0] = "合  计";
                   downRow=1;
                }
                for (int j=1;j<CField.length+10;j++) {
                   resultList[arrrow-1][j] = "0";
                   AGVSUMtype="3";
                }
            }
            //------------------------------------
            mdbset0=null;//赋空值
         } else {
            return;
         }
        //计算字段设置结果
        if (CField != null) {
           CFields ="";
           SaveHashtable.put("0", strWSAVEFIELD);
           for (int k = 0; k < CField.length;k++){
              expression = CField[k].getEXPRESSIONS();
              strFIELDVALUE="";
              try {
                     Arrange = Integer.parseInt(CField[k].getSHOWCODE());
              } catch (NumberFormatException ex4) {
                     Arrange =1;
                     LOGGER.info("计算字段的序号不能转换为数字:"+ex4.toString());
              }
              //列序号对应保存字段对照表
              SaveHashtable.put(String.valueOf(Arrange),CField[k].getSAVEFIELD());
              //计算字段名对应列对照表
              //CompterHashtable.put(,CField[k].getSAVEFIELD());

              //处理SUM、COUNT、AVG、MAX、MIN几个函数的情况
              strIsSum="0";
              strSumSql="";
              if (expression.toLowerCase().indexOf("sum(") > -1 || expression.toLowerCase().indexOf("count(") > -1 || expression.toLowerCase().indexOf("avg(") > -1 || expression.toLowerCase().indexOf("max(") > -1 || expression.toLowerCase().indexOf("min(") > -1){
                  strIsSum="1";
                  ComFromTableStr = getComFromTableStr(CField[k].getID());//计算字段关联表串
                  ComConnectionStr = getComConnectionStr(CField[k].getID());//计算字段关联字符串
                  //if (ComFromTableStr.length()<strTable.length() && ComFromTableStr.lastIndexOf(",")==-1)
                  if (ComFromTableStr.equals(strTable))
                  {
                     ComFromTableStr = strTable;
                     ComConnectionStr = strConn;
                  }

                  LOGGER.info("计算字段关联表串:"+ComFromTableStr);
                  LOGGER.info("计算字段关联字符串:"+ComConnectionStr);

                  //得到计算字段中与二维表的对应关系和条件（当没有设置时采用配置的属性）
                  strCPLANARFIELD1 = CField[k].getCPLANARFIELD();
                  LOGGER.info("二维对应字段："+strCPLANARFIELD1);
                  if (strCPLANARFIELD1.length()==0 || strCPLANARFIELD1==null || strCPLANARFIELD1.equals("0"))
                  {
                      strCPLANARFIELD1 = strCPLANARFIELD;
                  }
                  //当前表与维数据对应的特殊关联
                  strCPLANARESPECIAL = CField[k].getCPLANARESPECIAL();
                  addBuf.delete(0,addBuf.length());//清空
                  if (strCPLANARESPECIAL.trim().length()==0)
                  {
                      if (strCPLANARFIELD1.lastIndexOf(".")==-1)
                      {
                        strCPLANARFIELD1 = CField[k].getTABLEID()+"."+strCPLANARFIELD1;
                      }

                      if (strPLANARTABLE.toLowerCase().indexOf("bpip_unit") > -1 || isunit.equals("1"))//维数据对应的表为单位表
                      {
                          addBuf.append("select substr("+strCPLANARFIELD1)
                                .append(",1,"+strUNITLEN+") as code,")
                                .append(expression+" as cvalue");
                      } else {
                          addBuf.append("select "+strCPLANARFIELD1+" as code,")
                                .append(expression+" as cvalue");
                      }
                  } else {
                      if (strCPLANARESPECIAL.lastIndexOf(".")==-1) {
                        strCPLANARESPECIAL = CField[k].getTABLEID()+"."+strCPLANARESPECIAL;
                      }
                      if (strPLANARTABLE.toLowerCase().indexOf("bpip_unit") > -1 || isunit.equals("1"))//维数据对应的表为单位表
                      {
                          addBuf.append("select substr("+strCPLANARESPECIAL)
                                .append(",1,"+strUNITLEN+") as code,")
                                .append(expression+" as cvalue");
                      } else {
                          addBuf.append("select "+strCPLANARESPECIAL+" as code,")
                                .append(expression+" as cvalue");
                      }
                  }
                  //在条件中增加数据维的条件
                  if (strPLANARTABLE.toLowerCase().indexOf("bpip_unit") > -1 || isunit.equals("1"))//维数据对应的表为单位表
                  {
                       if ((CField[k].getTABLEID().length()>0 && !CField[k].getTABLEID().equals("0")) || ComFromTableStr.lastIndexOf(",")>-1)//计算字段设置有关联表
                       {
                           addBuf.append(" from "+ComFromTableStr+" where "+ComConnectionStr);
                       } else {
                           addBuf.append(" from "+strTable+" where "+strConn);
                       }
                  } else {
                       if ((CField[k].getTABLEID().length()>0 && !CField[k].getTABLEID().equals("0")) || ComFromTableStr.lastIndexOf(",")>-1)//计算字段设置有关联表
                       {
                           addBuf.append(" from "+ComFromTableStr+" where "+ComConnectionStr);
                       } else {
                           addBuf.append(" from "+strTable+" where "+strConn);
                       }
                  }
                  //统计初始条件
                  strInitCCon = getInitCCondition(CField[k].getID(),P1,P2,P3,P4,P5,user);
                  if (strInitCCon.trim().length()==0) {
                    if (strInitCon.length()>0) {
                        addBuf.append(" and "+strInitCon);
                    }
                  } else {
                      addBuf.append(" and "+strInitCCon);
                  }
                  //addBuf.append(" and "+strCPLANARFIELD + " is not null");

                  //自定义条件
                  if (strConnValue.length()>0) {
                      addBuf.append(" and ("+strConnValue+")");
                  }
                  //------------------处理当前表与维数据表的关系-------------------
                  if (strCPLANARESPECIAL.trim().length()==0) {
                      if (comWHEREVALUE.length()>0) {
                         addBuf.append(" and "+strCPLANARFIELD1+comWHEREVALUE);
                      }
                  }
                  LOGGER.info("关系:"+comWHEREVALUE);

                  strEXPRESSIONSWHERE = CField[k].getEXPRESSIONSWHERE();
                  if (strEXPRESSIONSWHERE!=null && strEXPRESSIONSWHERE.length()>0) {
                     strEXPRESSIONSWHERE = prepareExpression(strEXPRESSIONSWHERE,P1,P2,P3,P4,P5,user);//计算表达式
                     if (strEXPRESSIONSWHERE.toLowerCase().indexOf("or") > -1) {
                         strEXPRESSIONSWHERE = "("+strEXPRESSIONSWHERE+")";
                     }
                     addBuf.append(" and "+strEXPRESSIONSWHERE);
                  }
                  //加上分组统计条件
                  if (strCPLANARESPECIAL.trim().length()==0) {
                      if (strCPLANARFIELD1.lastIndexOf(".")==-1) {
                        strCPLANARFIELD1 = CField[k].getTABLEID()+"."+strCPLANARFIELD1;
                      }
                      if (strPLANARTABLE.toLowerCase().indexOf("bpip_unit") > -1 || isunit.equals("1"))//维数据对应的表为单位表
                      {
                          addBuf.append(" group by substr("+strCPLANARFIELD1+",1,")
                                .append(strUNITLEN+")");
                      } else {
                          addBuf.append(" group by "+strCPLANARFIELD1);
                      }
                  } else {
                      if (strCPLANARESPECIAL.lastIndexOf(".")==-1) {
                         strCPLANARESPECIAL = CField[k].getTABLEID()+"."+strCPLANARESPECIAL;
                      }
                      if (strPLANARTABLE.toLowerCase().indexOf("bpip_unit") > -1 || isunit.equals("1"))//维数据对应的表为单位表
                      {
                          addBuf.append(" group by substr("+strCPLANARESPECIAL+",1,")
                                .append(strUNITLEN+")");
                      } else {
                          addBuf.append(" group by "+strCPLANARESPECIAL);
                      }
                  }
                  strSumSql = addBuf.toString();
                  strTmp = expression.substring(expression.indexOf("(")+1,expression.indexOf(")"));

                  try {
                      itemField1 = getItemFieldByTableField(strTmp);//得到字段的相关属性
                  } catch (Exception ex) {
                      //log.WriteLog("得到字段的相关属性出错:"+ex.toString());
                      //log.WriteLog("字段:"+strTmp);
                  }
                  //为空或算记录数时按整数取
                  if (itemField1==null || expression.toLowerCase().indexOf("count(") > -1) {
                	  dbTypeValue = 6;
                  } else {
                    if (expression.toLowerCase().indexOf("sum(") > -1 || expression.toLowerCase().indexOf("avg(") > -1)
                    {
                       dbTypeValue=2;
                    } else {
                       dbTypeValue = itemField1.getType().getValue();
                    }
                  }
                  //替换字段中的[]
                  strSumSql = strSumSql.replaceAll("\\[","");
                  strSumSql = strSumSql.replaceAll("\\]","");
                  strSumSql = strSumSql.replaceAll("where  and"," where ");
                  //-------------------
                  LOGGER.info("计算sql:"+strSumSql);
                  int js=0;
                  try {
                  //DBSet dsSum = dbEngine.QuerySQL(strSumSql);
                  List<Map<String, Object>> dsSum = userMapper.selectListMapExecSQL(strSumSql);
                  if(dsSum != null && dsSum.size() > 0) {
                      for (int sum=0; sum<dsSum.size(); sum++) {
                         js=0;
                         strFIELDCODE=dsSum.get(sum).get("code").toString();
                         LOGGER.info("类型："+dbTypeValue);
                         switch (dbTypeValue){
                           case 1://字符型
                              strFIELDVALUE=dsSum.get(sum).get("cvalue").toString();
                              break;
                           case 2://浮点型
                              strFIELDVALUE=dsSum.get(sum).get("cvalue").toString();
                              if (strFIELDVALUE.lastIndexOf("E") > -1)//将有科学计数的数字转化成传统方式
                              {
                                  int flen = 0;
                                  flen = strFIELDVALUE.lastIndexOf("E");
                                  int flen1 =  Integer.parseInt(strFIELDVALUE.substring(flen+1,strFIELDVALUE.length()));
                                  strFIELDVALUE = strFIELDVALUE.substring(0,flen);
                                  strFIELDVALUE = strFIELDVALUE.replaceAll("\\.","");
                                  if (strFIELDVALUE.length()>flen1) {
                                     strFIELDVALUE = strFIELDVALUE.substring(0,flen1+1)+"."+strFIELDVALUE.substring(flen1+1,strFIELDVALUE.length());
                                  }
                                  if (strFIELDVALUE.length()<=flen1) {
                                     int alen = flen1-strFIELDVALUE.length()+1;
                                     for (int y=0;y<alen;y++) {
                                          strFIELDVALUE = strFIELDVALUE +"0";
                                     }
                                  }
                              }
                              if ((strFIELDVALUE.length()-strFIELDVALUE.indexOf(".")>2) && strFIELDVALUE.indexOf(".")>0)//小数点位数大于2时
                              {
		                        //double floatTax = Double.parseDouble(strFIELDVALUE);
		                        //BigDecimal floatValue = new BigDecimal(floatTax);
		                        //保留小数点RADIXPOINT位
		                        //double actualTax = floatValue.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		                        //strFIELDVALUE =  String.valueOf(actualTax);
		                        int wlen = strFIELDVALUE.length()-strFIELDVALUE.indexOf(".");
		                        strFIELDVALUE = strFIELDVALUE.substring(0,strFIELDVALUE.indexOf(".")+3);
		                        if (wlen>4)
		                        {
		                           if (strFIELDVALUE.substring(strFIELDVALUE.indexOf(".")+1,strFIELDVALUE.length()).equals("09"))
		                           {
		                              strFIELDVALUE = strFIELDVALUE.substring(0,strFIELDVALUE.indexOf(".")+1)+"1";
		                           }
		                        }
		                     } else {
		                         if (strFIELDVALUE.indexOf(".")>0 && strFIELDVALUE.length()>2)
		                         {
		                            if (strFIELDVALUE.substring(strFIELDVALUE.length()-2,strFIELDVALUE.length()).equals(".0"))
		                            {
		                               strFIELDVALUE=strFIELDVALUE.substring(0,strFIELDVALUE.length()-2);
		                            }
		                         }
		                     }
                              break;
                           case 4://日期型
                              strFIELDVALUE=dsSum.get(sum).get("cvalue").toString();
                              if ((strFIELDVALUE.equals("-") || strFIELDVALUE.equals("/")) && strFIELDVALUE.length()>10)
                              {
                                 strFIELDVALUE = strFIELDVALUE.substring(0,10);
                              }
                              break;
                          case 5://日期时间型
                              strFIELDVALUE=dsSum.get(sum).get("cvalue").toString();

                              break;
                          case 6://整型
                              strFIELDVALUE=dsSum.get(sum).get("cvalue").toString();
                              
                              if (Integer.parseInt(dsSum.get(sum).get("cvalue").toString())==0 && 
                            		  Double.parseDouble(dsSum.get(sum).get("cvalue").toString()) != 0)
                              {
                                  strFIELDVALUE=dsSum.get(sum).get("cvalue").toString();
                                  if (strFIELDVALUE.lastIndexOf("E") > -1)//将有科学计数的数字转化成传统方式
                                  {
                                     int flen = 0;
                                     flen = strFIELDVALUE.lastIndexOf("E");
                                     int flen1 =  Integer.parseInt(strFIELDVALUE.substring(flen+1,strFIELDVALUE.length()));
                                     
                                     strFIELDVALUE = strFIELDVALUE.substring(0,flen);
                                     strFIELDVALUE = strFIELDVALUE.replaceAll("\\.","");
                                     
                                     if (strFIELDVALUE.length()<=flen1) {
                                       int alen = flen1-strFIELDVALUE.length()+1;
                                       for (int y=0;y<alen;y++)
                                       {
                                        strFIELDVALUE = strFIELDVALUE +"0";
                                       }
                                      }
                                  }
                                  js=1;
                              }
                              break;
                         }
                         
                         //-------------------------------------
                         //------累加值----------
                       try {
                           //if (dbTypeValue==6 && js==0)
                           //{
                           //    SUMrow[Arrange] = String.valueOf(Integer.parseInt(SUMrow[Arrange]) + Integer.parseInt(strFIELDVALUE));
                           //}else
                           //{
                               LOGGER.info("将要加的值："+strFIELDVALUE);
                               SUMrow[Arrange] = String.valueOf(Double.parseDouble(SUMrow[Arrange]) + Double.parseDouble(strFIELDVALUE));
                               if (SUMrow[Arrange].lastIndexOf("E") > -1)//将有科学计数的数字转化成传统方式
                               {
                                  int flen = 0;
                                  flen = SUMrow[Arrange].lastIndexOf("E");
                                  int flen1 =  Integer.parseInt(SUMrow[Arrange].substring(flen+1,SUMrow[Arrange].length()));
                                  SUMrow[Arrange] = SUMrow[Arrange].substring(0,flen);
                                  SUMrow[Arrange] = SUMrow[Arrange].replaceAll("\\.","");
                                  if (SUMrow[Arrange].length()>flen1)
                                  {
                                     SUMrow[Arrange] = SUMrow[Arrange].substring(0,flen1+1)+"."+SUMrow[Arrange].substring(flen1+1,SUMrow[Arrange].length());
                                  }
                                  if (SUMrow[Arrange].length()<=flen1)
                                  {
                                    int alen = flen1-SUMrow[Arrange].length()+1;
                                    for (int y=0;y<alen;y++)
                                    {
                                       SUMrow[Arrange] = SUMrow[Arrange] +"0";
                                     }
                                  }
                              }
                           //}
                        } catch (NumberFormatException ex3) {
                            LOGGER.info("计算项累加出错："+ex3.toString());
                            SUMrow[Arrange] = "0";
                       }
                       //小数点位数处理
                       /*
                       if (strFIELDVALUE.indexOf(".")>-1)//结果包含小数点
                       {
                          int RADIXPOINT = CField[k].getRADIXPOINT();
                          double floatTax = Double.parseDouble(strFIELDVALUE);
                          BigDecimal floatValue = new BigDecimal(floatTax);
                          //保留小数点RADIXPOINT位
                          double actualTax = floatValue.setScale(RADIXPOINT,BigDecimal.ROUND_HALF_UP).doubleValue();
                          strFIELDVALUE =  String.valueOf(actualTax);
                          if (strFIELDVALUE.indexOf(".") != -1) {
                             int a1 = strFIELDVALUE.indexOf(".");
                             int a2 = strFIELDVALUE.length();
                             if ((a2-a1)>(RADIXPOINT+1)) {
                                strFIELDVALUE = strFIELDVALUE.substring(0,a1+RADIXPOINT+1);
                             }
                          }
                       }*/
                       //------------------
                       //附加符号处理
                       String strADDSIGN = CField[k].getADDSIGN();
                       if (strADDSIGN.length()>0 && !strADDSIGN.equals("null")) {
                           strFIELDVALUE = strFIELDVALUE+strADDSIGN;
                       }
                       //将结果保存到二维数组对应的项中-----------
                       try {
                          strTmp = (String) CodeHashtable.get(strFIELDCODE);
                          resultList[Integer.parseInt(strTmp)][Arrange] = strFIELDVALUE;
                          LOGGER.info("resultList["+strTmp+"]["+String.valueOf(Arrange)+"]结果:"+resultList[Integer.parseInt(strTmp)][Arrange]);

                       } catch (NumberFormatException ex6) {
                    	   LOGGER.info("结果存入数组出错："+ex6);
                       }
                     }
                     dsSum=null;//赋空值
                 }
                 }catch (Exception ex2) {
                	 LOGGER.error("计算结果出错："+ex2.toString());
                     //将结果设置成0
                     strFIELDVALUE = "0";
                 }
              }
              if (strIsSum.equals("0"))//非特殊函数时
              {
                //处理表达式中有计算列的情况
                if (CFields.length()>0)
                {
                	LOGGER.info("cfields:"+CFields);
                    int resultlen=0;
                    if (resultList.length>2) {
                    	resultlen=resultList.length-downRow;
                    } else {
                    	resultlen=resultList.length;
                    }
                    for (int csum=0;csum<resultlen;csum++) {
                       expression1 = expression;
                       arrList1 = getArrayList(CFields,",");
                       for (int x = 0; x < arrList1.size();x++) {
                           strTmp = arrList1.get(x).toString();
                           arrList2 = getArrayList(strTmp,"/");
                           CArrange = Integer.parseInt(arrList2.get(1).toString());
                           try {
                              expression1 = expression1.replaceAll(arrList2.get(0).toString(),resultList[csum][CArrange]);
                              //不区分大小写的替换-------------
                              strTmp = arrList2.get(0).toString().toLowerCase();
                              expression1 = expression1.replaceAll(strTmp,resultList[csum][CArrange]);
                              strTmp = arrList2.get(0).toString().toUpperCase();
                              expression1 = expression1.replaceAll(strTmp,resultList[csum][CArrange]);
                              //不区分大小写的替换-------------
                           } catch (Exception ex5){
                        	   LOGGER.error("替换表达式的值出错:"+ex5.toString());
                           }
                       }
                       //表达式计算
                       LOGGER.info("计算前表达式:"+expression1);
                       expression1 = prepareExpression(expression1,P1,P2,P3,P4,P5,user);
                       LOGGER.info("计算后表达式:"+expression1);
                       strFIELDVALUE=expression1;
                       if (strFIELDVALUE.indexOf("/0")>-1)//包含除数为0的情况
                       {
                            strFIELDVALUE="0";
                       }
                       //------累加值----------
                       try {
                    	   LOGGER.info("计算项累加11:"+strFIELDVALUE);
                           SUMrow[Arrange] = String.valueOf(Double.parseDouble(SUMrow[Arrange]) + Double.parseDouble(strFIELDVALUE));
                       } catch (NumberFormatException ex3) {
                    	   LOGGER.error("计算项累加出错："+ex3.toString());
                           SUMrow[Arrange] = "0";
                       }
                       //小数点位数处理
                       /*
                       if (strFIELDVALUE.indexOf(".")>-1)//结果包含小数点
                       {
                          int RADIXPOINT = CField[k].getRADIXPOINT();
                          double floatTax = Double.parseDouble(strFIELDVALUE);
                          BigDecimal floatValue = new BigDecimal(floatTax);
                          //保留小数点RADIXPOINT位
                          float actualTax = floatValue.setScale(RADIXPOINT,BigDecimal.ROUND_HALF_UP).floatValue();
                          strFIELDVALUE =  String.valueOf(actualTax);
                          if (strFIELDVALUE.indexOf(".") != -1) {
                             int a1 = strFIELDVALUE.indexOf(".");
                             int a2 = strFIELDVALUE.length();
                             if ((a2-a1)>(RADIXPOINT+1)) {
                                strFIELDVALUE = strFIELDVALUE.substring(0,a1+RADIXPOINT+1);
                             }
                          }
                       }*/
                       //------------------
                       //附加符号处理
                       String strADDSIGN = CField[k].getADDSIGN();
                       if (strADDSIGN.length()>0 && !strADDSIGN.equals("null")) {
                           strFIELDVALUE = strFIELDVALUE+strADDSIGN;
                       }
                       //将结果保存到二维数组对应的项中-----------
                       resultList[csum][Arrange] = strFIELDVALUE;
                       LOGGER.info("resultList["+String.valueOf(csum)+"]["+String.valueOf(Arrange)+"]结果:"+resultList[csum][Arrange]);
                    }
                }
              }
              addBuf.delete(0,addBuf.length());//清空
              if (CFields.length()==0) {
                  addBuf.append(CFields+CField[k].getSFIELDNAME()+"/"+String.valueOf(Arrange));
              } else {
                  addBuf.append(CFields+","+CField[k].getSFIELDNAME())
                        .append("/"+String.valueOf(Arrange));
              }
              CFields = addBuf.toString();
           }
        }
        //平均值及总和行的计算处理-------------------------
        for (int i=datarow;i<arrrow;i++) {
            if (CField != null){
               CFields ="";
               for (int k = 0; k < CField.length;k++) {
                   try {
                         Arrange = Integer.parseInt(CField[k].getSHOWCODE());
                  } catch (NumberFormatException ex4) {
                         Arrange =1;
                         LOGGER.error("计算字段的序号不能转换为数字:"+ex4.toString());
                  }
                   try {
                         if ((AGVSUMtype.equals("1") || AGVSUMtype.equals("2")) && i==datarow) {
                             strFIELDVALUE = String.valueOf(Double.parseDouble(SUMrow[Arrange])/datarow);
                         }
                         if ((AGVSUMtype.equals("1") && i==datarow+1) || AGVSUMtype.equals("3")) {
                             strFIELDVALUE = String.valueOf(Double.parseDouble(SUMrow[Arrange]));
                         }
                   } catch (NumberFormatException ex3) {
                	   LOGGER.error("计算值项出错："+ex3.toString());
                       strFIELDVALUE = "0";
                   }
                   if (CFields.length()==0) {
                	   CFields =CFields +CField[k].getSFIELDNAME()+"/"+strFIELDVALUE;
                   } else {
                	   CFields =CFields +","+CField[k].getSFIELDNAME()+"/"+strFIELDVALUE;
                   }
                     //直接计算列处理
                     expression = CField[k].getEXPRESSIONS();
                     if (expression.toLowerCase().indexOf("sum(") <= -1 && expression.toLowerCase().indexOf("count(") <= -1 && expression.toLowerCase().indexOf("avg(") <= -1 && expression.toLowerCase().indexOf("max(") <= -1 && expression.toLowerCase().indexOf("min(") <= -1)
	                 {
	                     //处理表达式中有计算列的情况
	                     if (CFields.length()>0)
	                     {
	                        arrList1 = getArrayList(CFields,",");
	                        for (int x = 0; x < arrList1.size();x++)
	                        {
	                           strTmp = arrList1.get(x).toString();
	                           arrList2 = getArrayList(strTmp,"/");
	                           expression = expression.replaceAll(arrList2.get(0).toString(),arrList2.get(1).toString());
	
	                            //不区分大小写的替换-------------
	                            strTmp = arrList2.get(0).toString().toLowerCase();
	                            expression = expression.replaceAll(strTmp,arrList2.get(1).toString());
	                            strTmp = arrList2.get(0).toString().toUpperCase();
	                            expression = expression.replaceAll(strTmp,arrList2.get(1).toString());
	                            //不区分大小写的替换-------------
	                        }
	                     }
	                     //表达式计算
	                     if (expression.indexOf("/0")>-1)//包含除数为0的情况
	                     {
	                         expression="0";
	                     }
	                     expression = prepareExpression(expression,P1,P2,P3,P4,P5,user);
	                     strFIELDVALUE=expression;
	                 }
                     //小数点位数处理
                     if (strFIELDVALUE.indexOf(".")>-1)//结果包含小数点
                     {
                         /*
                         int RADIXPOINT = 2;
                         if (CField[k].getRADIXPOINT()>0) {
                            RADIXPOINT = CField[k].getRADIXPOINT();
                         }
                         double floatTax = Double.parseDouble(strFIELDVALUE);
                          BigDecimal floatValue = new BigDecimal(floatTax);
                          //保留小数点RADIXPOINT位
                          float actualTax = floatValue.setScale(RADIXPOINT,BigDecimal.ROUND_HALF_UP).floatValue();
                          strFIELDVALUE =  String.valueOf(actualTax);
                          if (strFIELDVALUE.indexOf(".") != -1)
                          {
                             int a1 = strFIELDVALUE.indexOf(".");
                             int a2 = strFIELDVALUE.length();
                             if ((a2-a1)>(RADIXPOINT+1))
                             {
                                strFIELDVALUE = strFIELDVALUE.substring(0,a1+RADIXPOINT+1);
                             }
                          }*/
                         strFIELDVALUE=sw.CutLastZero(strFIELDVALUE,1);
                         if (strFIELDVALUE.length()>1) {
                           if (strFIELDVALUE.substring(strFIELDVALUE.length()-1).equals(".")) {
                              strFIELDVALUE = strFIELDVALUE.substring(0,strFIELDVALUE.length()-1);
                           }
                         }
                     }
                     //------------------
                     if (strFIELDVALUE.lastIndexOf("E") > -1)//将有科学计数的数字转化成传统方式
                     {
                          int flen = 0;
                          flen = strFIELDVALUE.lastIndexOf("E");
                          int flen1 =  Integer.parseInt(strFIELDVALUE.substring(flen+1,strFIELDVALUE.length()));
                          strFIELDVALUE = strFIELDVALUE.substring(0,flen);
                          strFIELDVALUE = strFIELDVALUE.replaceAll("\\.","");
                          if (strFIELDVALUE.length()>flen1) {
                             strFIELDVALUE = strFIELDVALUE.substring(0,flen1+1)+"."+strFIELDVALUE.substring(flen1+1,strFIELDVALUE.length());
                          }
                          if (strFIELDVALUE.length()<=flen1) {
                             int alen = flen1-strFIELDVALUE.length()+1;
                             for (int y=0;y<alen;y++) {
                                strFIELDVALUE = strFIELDVALUE +"0";
                             }
                          }
                     }
                     if ((strFIELDVALUE.length()-strFIELDVALUE.indexOf(".")>2) && strFIELDVALUE.indexOf(".")>0)//小数点位数大于2时
                     {
                        //double floatTax = Double.parseDouble(strFIELDVALUE);
                        //BigDecimal floatValue = new BigDecimal(floatTax);
                        //保留小数点RADIXPOINT位
                        //double actualTax = floatValue.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                        //strFIELDVALUE =  String.valueOf(actualTax);
                        int wlen = strFIELDVALUE.length()-strFIELDVALUE.indexOf(".");
                        strFIELDVALUE = strFIELDVALUE.substring(0,strFIELDVALUE.indexOf(".")+3);
                        if (wlen>4) {
                           if (strFIELDVALUE.substring(strFIELDVALUE.indexOf(".")+1,strFIELDVALUE.length()).equals("09"))
                           {
                              strFIELDVALUE = strFIELDVALUE.substring(0,strFIELDVALUE.indexOf(".")+1)+"1";
                           }
                        }
                      } else {
                          if (strFIELDVALUE.indexOf(".")>0 && strFIELDVALUE.length()>2) {
                             if (strFIELDVALUE.substring(strFIELDVALUE.length()-2,strFIELDVALUE.length()).equals(".0"))
                             {
                                strFIELDVALUE=strFIELDVALUE.substring(0,strFIELDVALUE.length()-2);
                             }
                          }
                      }
                     //附加符号处理
                     String strADDSIGN = CField[k].getADDSIGN();
                     if (strADDSIGN.length()>0 && !strADDSIGN.equals("null")) {
                         strFIELDVALUE = strFIELDVALUE+strADDSIGN;
                     }
                     //将结果保存到二维数组对应的项中-----------
                     resultList[i][Arrange] = strFIELDVALUE;
               }
            }
            //dbengine.ExecuteSQL(strSQL);
        }
      //将记录插入数据库中
      if (resultList!=null) {
        String sqlleft="";
        String sqlright="";
        Vector mVec = new Vector();  //存放所有SQL语句
        String strYMaxNo = getMaxFieldNo(strTableName,strTableKey,intlen);
        //判断只有一条记录时平均值和总和不需要
        if (strISAGV.equals("1") && strISSUM.equals("1")) {//有平均值和总和
            if (arrrow==3){arrrow=1;}
        } else {
            if ((strISAGV.equals("1")) || (strISSUM.equals("1")))//有平均值或总和
            {
               if (arrrow==2){arrrow=1;}
            }
        }
        for (int i=0;i<arrrow;i++) {
            rowsum = "0";
            addBuf.delete(0,addBuf.length());//清空
            tmpBuf.delete(0,tmpBuf.length());//清空
            strMaxNo = getMaxFieldNoAdd(strYMaxNo,intlen,i);
            
            addBuf.append(strTableKey+",FID,SUSERNO");
            tmpBuf.append("'"+strMaxNo+"','"+strID)
                  .append("','"+user.getUserID()+"'");
            for (int j=0;j<CField.length+1;j++) {
                strTmp= (String) SaveHashtable.get(String.valueOf(j));
                if (addBuf.toString().lastIndexOf(strTmp)<=-1) {
                   addBuf.append(","+strTmp);
                   tmpBuf.append(",'"+resultList[i][j]+"'");
                   try {
                       if (Integer.parseInt(resultList[i][j]) > 0) {
                          rowsum = "1";
                       }
                   } catch (NumberFormatException ex7) {
                      //System.out.println("出错："+ex7.toString());
                   }
                }
            }
            //&&&&&&&&&&&&&&&&处理生成报表后直接点击某单位时打开单位下的统计&&&&&&&&
            //if (strPLANARTABLE.toLowerCase().indexOf("bpip_unit") > -1)//维数据对应的表为单位表
            //{
                addBuf.append(",FIELD50");
                tmpBuf.append(",'"+resultList[i][CField.length+9]+"'");
            //}
            //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
            sqlleft = addBuf.toString();
            sqlright = tmpBuf.toString();

            shownow ="0";
            if (ISZERO.equals("0")) {
               if (rowsum.equals("1") || (rowsum.equals("0") && sqlright.indexOf("平均值") > -1)) {
                 shownow ="1";
               }
            } else {
               if (rowsum.equals("1") || (rowsum.equals("0") && sqlright.indexOf("平均值") > -1) || rowsum.equals("0") )
               {
                 shownow ="1";
               }
            }
            if (shownow.equals("1")) {
                addBuf.delete(0,addBuf.length());//清空
                addBuf.append("Insert into "+strTableName+"(")
                    .append(sqlleft+") values ("+sqlright)
                    .append(")");
                LOGGER.info("插入报表结果sql:"+addBuf.toString());
                mVec.add(addBuf.toString());
            }
        }
        //将所有SQL装入数组
        if (mVec.size()>100) {
          String[] sqls = new String[100];
          String[] sqls1 = new String[mVec.size()%100];
          int k=0;
          int x=mVec.size();

          for(int j=0;j<mVec.size();j++){
            if (x>=100) {
               sqls[k] = (String)mVec.get(j);
            } else {
               sqls1[k] = (String)mVec.get(j);
            }
            k++;
            if (k==100 || mVec.size()==(j+1)) {
                //执行相关sql
                if (x>=100) {
                	//dbEngine.ExecuteSQLs(sqls);
                	for (String sql : sqls) {
                		userMapper.insertExecSQL(sql);
                	}
                } else {
                	//dbEngine.ExecuteSQLs(sqls1);
                	for (String sql : sqls1) {
                		userMapper.insertExecSQL(sql);
                	}
                }
                k=0;
                x=mVec.size()-j-1;
            }
          }
        } else {
          String[] sqls = new String[mVec.size()];
          for(int j=0;j<mVec.size();j++){
            sqls[j] = (String)mVec.get(j);
          }
          //执行相关sql
          //dbEngine.ExecuteSQLs(sqls);
          for (String sql : sqls) {
        	  userMapper.insertExecSQL(sql);
          }
        }
      }
      //--------------------------------------------------------------------
    }

    /**
     * 功能:根据表名和字段名得到字段中文名
     * @param strName 表名
     * @param strFIELD 字段名
     * @return
     * @throws Exception 
     */
    private String getTABLEFIELDNAME(String strName, String strFIELD) throws Exception{
        StringBuffer addBuf = new StringBuffer();
        String retValue = "";
        addBuf.append("Select CHINESENAME from BPIP_FIELD where FIELDNAME='"+strFIELD)
              .append("' and TABLEID in (Select TABLEID From BPIP_TABLE where TABLENAME='")
              .append(strName+"')");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        if (retlist != null && retlist.size() > 0) {
        	if (retlist.get(0) != null && retlist.get(0).get("CHINESENAME") != null) {
        		retValue = retlist.get(0).get("CHINESENAME").toString();
        	}
        	if (retValue == null) {
        		retValue = "0";
        	}
        }
        return retValue;
    }

    /**
     * 获取配置关系表字符串，例如 主表,关联表1，关联表2....
     * @param ID String 配置ID
     * @return String
     * @throws Exception 
     */
    private String getFromTableStr(String strID) throws Exception {
       String strRvalue = "";
       StringBuffer buf = new StringBuffer();
       StringBuffer addBuf = new StringBuffer();
       addBuf.append("Select TABLEID From ANALYSE_STATISTICS_MAIN Where ID='"+strID+"'");
       Map<String, Object> retMap = userMapper.selectMapExecSQL(addBuf.toString());//首先得到主表
       if (retMap != null && retMap.size() > 0) {
    	   buf.append(retMap.get("TABLEID").toString());
       }
       strRvalue = buf.toString();
       return strRvalue;
    }

    /**
     * 根据配置ID得到统计计算字段实体
     * @param strID 配置ID
     * @return String
     */
    private ANALYSE_STATISTICS_CFIELD[] getResultCFieldStr(String strID) {
        ANALYSE_STATISTICS_CFIELD resultList[] = null;
        StringBuffer addBuf = new StringBuffer();
        try {
            if (strID.trim().length() > 0) {
                addBuf.append("Select * from ANALYSE_STATISTICS_CFIELD WHERE FID='")
                      .append(strID+"' ORDER BY DISTINCTION,ID");
                List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
                int length = retlist != null ? retlist.size() : 0;
                if (length > 0) {
                	resultList = new ANALYSE_STATISTICS_CFIELD[length];
                    for (int i = 0; i < length; i++) {
                    	Map<String, Object> retmap = retlist.get(i);
                    	resultList[i] = (ANALYSE_STATISTICS_CFIELD) ReflectionUtil.convertMapToBean(retmap, ANALYSE_STATISTICS_CFIELD.class);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("getResultCFieldStr出现异常:\n", ex);
        }
        return resultList; 
    }

    /**
     * 统计初始条件
     * @param strID  配置ID
     * @param P1 参数1
     * @param P2 参数2
     * @param P3 参数3
     * @param P4 参数4
     * @param P5 参数5
     * @param user 用户SESSION值
     * @return String   统计初始条件串
     */
    private String getInitCondition(String strID, String P1, String P2, 
    		String P3, String P4, String P5, SessionUser user) {
         ANALYSE_STATISTICS_WHERE entityList[] = null;
         StringBuffer sb = new StringBuffer();
         int i;
         ItemField itemField;
         int dbTypeValue;
         try {
             if (strID.trim().length() > 0) {
                 sb.append("Select * From ANALYSE_STATISTICS_WHERE where FID='"+strID+"' ORDER BY ID");
                 List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sb.toString());
                 int length = retlist != null ? retlist.size() : 0;
                 if (length == 0){
                     entityList = null;
                 } else {
                     entityList = new ANALYSE_STATISTICS_WHERE[length];
                     for (i = 0; i < length; i++) {
                    	 Map<String, Object> retmap = retlist.get(i);
                         entityList[i] = (ANALYSE_STATISTICS_WHERE) ReflectionUtil.convertMapToBean(retmap, ANALYSE_STATISTICS_WHERE.class);
                     }
                 }
             } else {
                 entityList = null;
             }
         } catch (Exception e) {
             LOGGER.error("getInitCondition出现异常:\n", e.getMessage());
             entityList = null;
         }
         sb.delete(0,sb.length());//清空
         if (entityList != null) {
             for (i = 0; i < entityList.length; i++) {
                 //在条件表达式中只有存在Pn参数且其相应的值为空，则该条件表达式无效
                 itemField = getItemFieldByTableField(entityList[i].getFIELD());
                 if (!entityList[i].getSLEFT().equals("0")){//左括号
                     sb.append(entityList[i].getSLEFT());
                 }
                 dbTypeValue = itemField.getType().getValue();
                 if (entityList[i].getWHEREVALUE().indexOf("P1") > -1 && P1 == null ||
                     entityList[i].getWHEREVALUE().indexOf("P2") > -1 && P2 == null ||
                     entityList[i].getWHEREVALUE().indexOf("P3") > -1 && P3 == null ||
                     entityList[i].getWHEREVALUE().indexOf("P4") > -1 && P4 == null ||
                     entityList[i].getWHEREVALUE().indexOf("P5") > -1 && P5 == null) {
                     sb.append(" 1=1 ");
                 } else {
                     sb.append(getConditionExpression(entityList[i].getFIELD(),
                             entityList[i].getSYMBOL(),
                             entityList[i].getWHEREVALUE(),
                             dbTypeValue,P1,P2,P3,P4,P5,user));
                 }
                 if (!entityList[i].getSRIGHT().equals("0")) { //右括号
                     sb.append(entityList[i].getSRIGHT());
                 }
                 if (i + 1 < entityList.length) { //最后一个条件行无论何时不能加入逻辑符,以统一处理
                     if (entityList[i].getLOGIC().equals("1")) {
                         sb.append(" AND ");                       }

                     if (entityList[i].getLOGIC().equals("2")) {
                         sb.append(" OR ");
                     }
                 }
             }
         }
             if (sb.toString().trim().length() == 0) {
                 sb.append(" 1=1 ");
             }

             return sb.toString();
         
    }

    /**
     * 根据表名.字段名获取ItemField对象
     * @param tableField String  表名.字段名的格式
     * @return ItemField
     */
    private ItemField getItemFieldByTableField(String tableField) {
        ItemField itemField = null;
        int i;
        StringBuffer addBuf = new StringBuffer();
        if (DataBaseType.equals("3")) {//mysql数据库
            addBuf.append("select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.* from bpip_table a, bpip_field b "
            		+ "where b.tableid=a.tableid and concat(a.tablename,'.',b.fieldname)='"+tableField+"' ");
        } else {
            addBuf.append("select a.TABLENAME,a.CHINESENAME as TABLECNNAME,b.* from bpip_table a, bpip_field b "
            		+ "where b.tableid=a.tableid and a.tablename||'.'||b.fieldname='"+tableField+"' ");
        }
        DBRow[] dbMain = null;
        String strIS ="";
        strIS = (String) SetHashtable31.get(tableField);
        if (strIS == null) {
            dbMain = this.getTableProperty(addBuf.toString());
            SetHashtable31.put(tableField,"1");
            if (dbMain != null) {
               SetHashtable3.put(tableField,dbMain);
            }
        } else {
           dbMain = (DBRow[]) SetHashtable3.get(tableField);
        }
        ItemList itemList = new ItemList();
        for (i = 0; i < dbMain.length; i++) {
            itemList.fullFromDbRow(dbMain[i]);
        }
        if (itemList.getItemCount() > 0) {
            itemField = itemList.getItemField(0);
        }
        dbMain = null;
        return itemField;
    }

    /**
     * 获取表字段属性
     * @param TableName String
     * @return dbRow
     */
     private DBRow[] getTableProperty(String sqlSql) {
        DBRow[] dbProperty = null;
        DBSet dsTable = dbEngine.QuerySQL(sqlSql);
        if (dsTable != null) {
            dbProperty = new DBRow[dsTable.RowCount()];
            for (int i = 0; i < dsTable.RowCount(); i++) {
                dbProperty[i] = new DBRow();
                dbProperty[i] = dsTable.Row(i);
                dbProperty[i].setTableName(dsTable.Row(i).Column("TABLENAME").getString());
            }
            dsTable=null;//赋空值
        }
        return dbProperty; 
     }

     /**
      * 建立条件表达式
      * @param tableField String                  表名.字段名
      * @param symbol String                      表达式符号(>,=,<,Like等)
      * @param conditionValue String              表达式的值
      * @param dbTypeValue int                    数据类型的值
      * @return String
      */
     private String getConditionExpression(String tableField, String symbol, String conditionValue, 
    		 int dbTypeValue, String P1, String P2, String P3, String P4, String P5, SessionUser user) {
         StringBuffer sb = new StringBuffer();
         if (dbTypeValue == DBType.DATE.getValue() || dbTypeValue == DBType.DATETIME.getValue()) {//日期型
        	 if (DataBaseType.equals("1")) {//Oracle数据库
        		 sb.append(" to_char(" + tableField +",'YYYY-MM-DD') "); //表名.字段名;
        	 } else {
        		 sb.append(tableField +" ");//表名.字段名;
        	 }
         } else {
             sb.append(" " + tableField + " ");//表名.字段名;
         }
         sb.append(" " + symbol + " ");
         conditionValue = prepareExpression(conditionValue,P1,P2,P3,P4,P5,user);
         if (dbTypeValue == DBType.DATE.getValue() ||
             dbTypeValue == DBType.DATETIME.getValue()) { //日期型
             try {
                 java.util.Date date = new java.util.Date(); //测试若为日期型时，是否为有效的日期字串值
                 java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd");
                 try {
                     date = fmt.parse(conditionValue);
                 } catch (Exception ex) {
                     fmt = new java.text.SimpleDateFormat("yyyy-MM-dd");
                     date = fmt.parse(conditionValue);
                 }
                 fmt = new java.text.SimpleDateFormat("yyyy-MM-dd");
                 sb.append(" '" + fmt.format(date) + "' ");
             } catch (Exception ex) {
                 if (symbol.equals(">") || symbol.equals(">=")) {
                     sb.append(" '1000-01-01' ");
                 } else if (symbol.equals("<") || symbol.equals("<=")) {
                     sb.append(" '3000-01-01' ");
                 } else if (symbol.equals("!=")) {
                     sb.append(" '1000-01-01' ");
                 } else {
                     sb.append("sysdate");
                 }
             }
         } else if (symbol.equalsIgnoreCase("Like") ||
                    symbol.equalsIgnoreCase("Not Like")) {
             if (conditionValue.indexOf("%") > -1) {
                 sb.append(conditionValue);
             } else {
                 //全包含的情况，若开始或结束的字符为单引号"'"，则需将其去掉
                 if (conditionValue.startsWith("'")) {
                     conditionValue = conditionValue.substring(1);
                 }

                 if (conditionValue.endsWith("'")) {
                     conditionValue = conditionValue.substring(0,
                             conditionValue.length() - 1);
                 }
                 sb.append("'%" + conditionValue + "%'");
             }
         } else if (dbTypeValue == DBType.STRING.getValue() &&
                    !conditionValue.equalsIgnoreCase("null")) {

             if (!conditionValue.startsWith("'") &&
                 conditionValue.toLowerCase().indexOf("select") == -1 &&
                 !conditionValue.startsWith("(")) {
                 conditionValue = "'" + conditionValue;
             }
             if (!conditionValue.endsWith("'") &&
                 conditionValue.toLowerCase().indexOf("select") == -1 &&
                 !conditionValue.endsWith(")")) {
                 conditionValue = conditionValue + "'";
             }
             sb.append(conditionValue);

         } else {
             sb.append(conditionValue);
         }
         return sb.toString();

     }

     /**
      * 处理表达式，使其能计算出来
      * @param expression	待计算的函数表达式
      * @return String		处理完后的表达式
      */
     private String prepareExpression(String expression, String P1, String P2, 
    		 String P3, String P4, String P5, SessionUser user) {
         LOGGER.info("prepareExpression开始调用...");
         long startTime = System.currentTimeMillis();
         String resultStr;
         SimpleDateFormat fmt;
         Date date = new Date();
         StringBuffer addBuf = new StringBuffer();
         //处理当前日期
         if (DataBaseType.equals("1")) {// Oracle数据库
        	 expression = expression.replaceAll("sysdate", "to_char(sysdate,'yyyy-mm-dd')");
 		 }
 		 else if (DataBaseType.equals("2")) {// MSSQL数据库
 			 expression = expression.replaceAll("sysdate", "getdate()");
 		 }
 		 else if (DataBaseType.equals("3")){// MySQL数据库
 			 expression = expression.replaceAll("sysdate", "str_to_date(sysdate,'%Y-%m-%d')");
 		 }
         //处理自定义的变量
         if (expression.lastIndexOf("{LoginID}") > -1) { //登录名
             expression = expression.replaceAll("\\{LoginID\\}", user.getLoginID());
         }
         if (expression.lastIndexOf("{Name}") > -1) { //姓名
             expression = expression.replaceAll("\\{Name\\}", user.getName());
         }
         if (expression.lastIndexOf("{UserID}") > -1) { //用户编号
             expression = expression.replaceAll("\\{UserID\\}", user.getUserID());
         }
         if (expression.lastIndexOf("{LCODE}") > -1) { //用户内部编号
             expression = expression.replaceAll("\\{LCODE\\}", user.getLCODE());
         }
         if (expression.lastIndexOf("{UnitName}") > -1) { //所在单位名称
             expression = expression.replaceAll("\\{UnitName\\}", user.getUnitName());
         }
         if (expression.lastIndexOf("{UnitID}") > -1) { //所在单位编号
             expression = expression.replaceAll("\\{UnitID\\}", user.getUnitID());
         }

         if (expression.lastIndexOf("{Custom1}") > -1) { //自定义参数1
             expression = expression.replaceAll("\\{Custom1\\}", user.getCustom1());
         }
         if (expression.lastIndexOf("{Custom2}") > -1) { //自定义参数2
            expression = expression.replaceAll("\\{Custom2\\}", user.getCustom2());
         }
         if (expression.lastIndexOf("{Custom3}") > -1) { //自定义参数3
            expression = expression.replaceAll("\\{Custom3\\}", user.getCustom3());
         }
         if (expression.lastIndexOf("{Custom4}") > -1) { //自定义参数4
            expression = expression.replaceAll("\\{Custom4\\}", user.getCustom4());
         }
         if (expression.lastIndexOf("{Custom5}") > -1) { //自定义参数5
            expression = expression.replaceAll("\\{Custom5\\}", user.getCustom5());
         }
         
         /*******************************时间的处理************************************/
         if (expression.lastIndexOf("{YYYY年}") > -1) { //当前年(中文式)
             fmt = new SimpleDateFormat("yyyy年");
             expression = expression.replaceAll("\\{YYYY年\\}", fmt.format(date));
         }
         if (expression.lastIndexOf("{YYYY年MM月}") > -1) { //当前年月(中文式)
             fmt = new SimpleDateFormat("yyyy年MM月");
             expression = expression.replaceAll("\\{YYYY年\\}", fmt.format(date));
         }
         if (expression.lastIndexOf("{YYYY年MM月DD日}") > -1) { //当前日期(中文式)
             fmt = new SimpleDateFormat("yyyy年MM月dd日");
             expression = expression.replaceAll("\\{YYYY年MM月DD日\\}", fmt.format(date));
         }
         if (expression.lastIndexOf("{YYYY年MM月DD日 HH:MM:SS}") > -1) { //当前时间(中文式)
             fmt = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
             expression = expression.replaceAll("\\{YYYY年MM月DD日 HH:MM:SS\\}", fmt.format(date));
         }
         if (expression.lastIndexOf("{YYYY}") > -1) { //当前年
             fmt = new SimpleDateFormat("yyyy");
             expression = expression.replaceAll("\\{YYYY\\}", fmt.format(date));
         }
         if (expression.lastIndexOf("{YYYY-MM}") > -1) { //当前年月
             fmt = new SimpleDateFormat("yyyy-MM");
             expression = expression.replaceAll("\\{YYYY-MM\\}", fmt.format(date));
         }
         if (expression.lastIndexOf("{YYYY-MM-DD}") > -1) { //当前日期
             fmt = new SimpleDateFormat("yyyy-MM-dd");
             expression = expression.replaceAll("\\{YYYY-MM-DD\\}", fmt.format(date));
         }
         if (expression.lastIndexOf("{YYYY-MM-DD HH:MM:SS}") > -1) { //当前时间
             fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             expression = expression.replaceAll("\\{YYYY-MM-DD HH:MM:SS\\}", fmt.format(date));
         }

         /*******************************自定义参数的处理************************************/
         if (expression.toLowerCase().indexOf("p1") > -1 && P1 != null) {
             expression = expression.replaceAll("P1", P1.replaceAll(",", "','"));
             expression = expression.replaceAll("p1", P1.replaceAll(",", "','"));
         }
         if (expression.toLowerCase().indexOf("p2") > -1 && P2 != null) {
             expression = expression.replaceAll("P2", P2.replaceAll(",", "','"));
             expression = expression.replaceAll("p2", P2.replaceAll(",", "','"));
         }
         if (expression.toLowerCase().indexOf("p3") > -1 && P3 != null) {
             expression = expression.replaceAll("P3", P3.replaceAll(",", "','"));
             expression = expression.replaceAll("p3", P3.replaceAll(",", "','"));
         }
         if (expression.toLowerCase().indexOf("p4") > -1 && P4 != null) {
             expression = expression.replaceAll("P4", P4.replaceAll(",", "','"));
             expression = expression.replaceAll("p4", P4.replaceAll(",", "','"));
         }
         if (expression.toLowerCase().indexOf("p5") > -1 && P5 != null) {
             expression = expression.replaceAll("P5", P5.replaceAll(",", "','"));
             expression = expression.replaceAll("p5", P5.replaceAll(",", "','"));
         }
         LOGGER.info("expression1="+expression);
         resultStr = expression;
         //有运算符，需要将其交与数据库进行运算
         if (!(expression.indexOf("+") == -1 && expression.indexOf("-") == -1 && expression.indexOf("*") == -1 &&
               expression.indexOf("||") == -1 && expression.indexOf("/") == -1 && 
               expression.indexOf("(") == -1 && expression.indexOf(")") == -1
             ) && ((expression.length()==10 && (!expression.substring(4,5).equals("-") 
            		 || !expression.substring(7,8).equals("-"))) || expression.length()!=10 
            		 || (expression.length()==10 && (expression.indexOf("+") > -1 
            				 || expression.indexOf("*") > -1 || expression.indexOf("/") > -1)))) {

             if (expression.toLowerCase().indexOf("select") > -1) {
                 //表达式中有select语句，表达式不能参与数据库运算，故将表达式直接赋予结果串
                 resultStr = expression;
             } else {
               if (DataBaseType.equals("1")) {//Oracle数据库
            	   addBuf.append("Select "+expression+" as resultStr from dual");
               } else {// MSSQL or MySQL
            	   addBuf.append("Select "+expression+" as resultStr");
               }
               try {
                     //mdbset = dbEngine.QuerySQL(addBuf.toString());
            	   	 List<Map<String, Object>> mdbset = userMapper.selectListMapExecSQL(addBuf.toString());
                     LOGGER.info(addBuf.toString());
                     if (mdbset != null && mdbset.size() > 0) {
                    	 if (mdbset.get(0) != null && mdbset.get(0).get("resultStr") != null) {
                    		 resultStr = mdbset.get(0).get("resultStr").toString();
                    	 }
                         mdbset = null;//赋空值
                     } else {
                         resultStr = expression; //当mdbset为null时，则表达式有误
                     }
                 } catch (Exception ex) {
                	 LOGGER.error("prepareExpression出现异常:\n", ex.getMessage());
                 }
                 if (resultStr.equals("???")) {
                     resultStr = expression;
                 }
             }
         }
         long endTime = System.currentTimeMillis();
         LOGGER.info("prepareExpression执行完成，耗时：" + (endTime - startTime) + " ms.");
         return resultStr;
     }

     /**
      * 功能：分析规则字符串，生成数组
      * @param strItems 字符串
      * @param strItemMark 标识符
      * @return 返回数组
      */
     @SuppressWarnings({ "rawtypes", "unchecked" })
	 private List getArrayList(String strItems, String strItemMark) {
         int intItemLen, i = 0, n = 0;
         strItems = strItems + strItemMark;
         String strItem;
         List strList = new ArrayList();
         intItemLen = strItems.length();
         while (i < intItemLen) {
           n = strItems.indexOf(strItemMark, i);
           strItem = strItems.substring(i, n);
           strList.add(strItem);
           i = n + 1;
         }
         return strList;
     }

     /**
      * 获取计算字段关系表字符串，例如 主表,关联表1，关联表2....
      * @param ID String 计算字段ID
      * @return String
     * @throws Exception 
      */
     private String getComFromTableStr(String strID) throws Exception {
         String returnValue = (String) SetHashtable1.get(strID);
         if (returnValue == null) {
         StringBuffer sb = new StringBuffer();
         String tableName="";
         String MaintableName="";
         StringBuffer addBuf = new StringBuffer();
         addBuf.append("select TABLEID from ANALYSE_STATISTICS_CFIELD Where ID='"+strID+"'");
         //mdbset = dbEngine.QuerySQL(addBuf.toString()); //得到计算字段中的主表
         List<Map<String, Object>> mdbset = userMapper.selectListMapExecSQL(addBuf.toString());
         if (mdbset != null && mdbset.size() > 0) {
        	 if (mdbset.get(0) != null && mdbset.get(0).get("TABLEID") != null) {
        		 MaintableName = mdbset.get(0).get("TABLEID").toString();
        	 }
             mdbset = null;//赋空值
         }
         if (MaintableName.length()>0) {
            sb.append(MaintableName);
         } else {
            addBuf.delete(0,addBuf.length());//清空
            addBuf.append("Select TABLEID from  ANALYSE_STATISTICS_MAIN  Where  ID  in (select FID from ANALYSE_STATISTICS_CFIELD Where ID='")
                  .append(strID+"')");
            //mdbset = dbEngine.QuerySQL(addBuf.toString()); //得到配置中的主表
            mdbset = userMapper.selectListMapExecSQL(addBuf.toString());
            if (mdbset != null && mdbset.size() > 0) {
            	if (mdbset.get(0) != null && mdbset.get(0).get("TABLEID") != null) {
            		MaintableName = mdbset.get(0).get("TABLEID").toString();
            	}
                mdbset = null;//赋空值
            }
            sb.append(MaintableName);
         }
         addBuf.delete(0,addBuf.length());//清空
         addBuf.append("select CFIELD,MFIELD from ANALYSE_STATISTICS_CCONNECTION Where FID='").append(strID+"'");
         //mdbset = dbEngine.QuerySQL(addBuf.toString());
         mdbset = userMapper.selectListMapExecSQL(addBuf.toString());
         if (mdbset != null && mdbset.size() > 0) {
             for (int i = 0; i < mdbset.size(); i++) {
                 tableName = mdbset.get(i).get("CFIELD").toString().split("\\.")[0];
                 if (sb.toString().indexOf(tableName) == -1){
                     sb.append("," + tableName);
                 }
                 tableName = mdbset.get(i).get("MFIELD").toString().split("\\.")[0];
                 if (sb.toString().indexOf(tableName) == -1) {
                     sb.append("," + tableName);
                 }
             }
             mdbset = null;//赋空值
         }
         returnValue = sb.toString();
         if (StringUtils.isBlank(returnValue)) {
           returnValue = " ";
         }
         SetHashtable1.put(strID, returnValue);
         }
         return returnValue;
     }

     /**
      * 根据计算字段ID得到关联表关系字串
      * @param strID 配置ID
      * @return String
      */
     private String getComConnectionStr(String strID) {
            String retValue = "";
            StringBuffer addBuf = new StringBuffer();
            try {
                if (strID.trim().length() > 0) {
                    addBuf.append("Select JOINTYPE,CFIELD,MFIELD From ANALYSE_STATISTICS_CCONNECTION "
                    		+ "Where FID='"+strID+"' Order By ID");
                    //DBSet mdbset = dbEngine.QuerySQL(addBuf.toString(), "st5", strID);
                    List<Map<String, Object>> mdbset = userMapper.selectListMapExecSQL(addBuf.toString());
                    addBuf.delete(0,addBuf.length());//清空
                    addBuf.append(" 1=1 ");
                    if (mdbset != null && mdbset.size() > 0) {
                        for (int i = 0; i < mdbset.size(); i++) {
                            if (mdbset.get(i).get("JOINTYPE").toString().equals("1")) { //一般相等联接
                               addBuf.append("  And " + mdbset.get(i).get("CFIELD").toString() + "=" + mdbset.get(i).get("MFIELD").toString() + "  ");
                            } else if (mdbset.get(i).get("JOINTYPE").toString().equals("2")) { //左外关联
                               addBuf.append("  And " + mdbset.get(i).get("CFIELD").toString() + "=" + mdbset.get(i).get("MFIELD").toString() + "(+)  ");
                            } else if (mdbset.get(i).get("JOINTYPE").toString().equals("3")) { //右外关联
                               addBuf.append("  And  (+)" + mdbset.get(i).get("CFIELD").toString() + "=" + mdbset.get(i).get("MFIELD").toString() + "  ");
                            } else {
                               addBuf.append("  And " + mdbset.get(i).get("CFIELD").toString() + "=" + mdbset.get(i).get("MFIELD").toString() + "  ");
                            }
                        }
                        mdbset = null;//赋空值
                    }
                }
            } catch (Exception e) {
                LOGGER.error("getComConnectionStr出现异常:\n", e.getMessage());
            }
            retValue = addBuf.toString();
            if (StringUtils.isBlank(retValue)) {
            	retValue = "";
            }
            return retValue; 
     }

     /**
      * 统计计算字段的初始条件
      * @param strID  计算字段ID
      * @param P1 参数1
      * @param P2 参数2
      * @param P3 参数3
      * @param P4 参数4
      * @param P5 参数5
      * @param user 用户SESSION值
      * @return String   统计计算字段初始条件串
      */
      private String getInitCCondition(String strID, String P1, String P2, String P3, String P4, String P5, SessionUser user) {
          LOGGER.info("getInitCCondition开始调用...");
          ANALYSE_STATISTICS_CWHERE entityList[] = null;
          StringBuffer sb = new StringBuffer();
          int i;
          ItemField itemField;
          int dbTypeValue;
          try {
              if (strID.trim().length() > 0) {
                  sb.append("Select * From ANALYSE_STATISTICS_CWHERE where FID='")
                    .append(strID+"' ORDER BY ID");
                  DBSet mdbset = dbEngine.QuerySQL(sb.toString(),"st6",strID);
                  if (mdbset == null){
                      entityList = null;
                  } else {
                      entityList = new ANALYSE_STATISTICS_CWHERE[mdbset.RowCount()];
                      for (i = 0; i < mdbset.RowCount(); i++){
                          entityList[i] = new ANALYSE_STATISTICS_CWHERE();
                          entityList[i].fullData(mdbset.Row(i));
                      }
                      mdbset=null;//赋空值
                  }
              } else {
                  entityList = null;
              }
          } catch (Exception e) {
              LOGGER.error("getInitCondition出现异常:\n" + e.getMessage());
              entityList = null;
          }
          sb.delete(0,sb.length());//清空
          if (entityList != null) {
              for (i = 0; i < entityList.length; i++) {
                  //在条件表达式中只有存在Pn参数且其相应的值为空，则该条件表达式无效
                  itemField = getItemFieldByTableField(entityList[i].getFIELD());
                  if (!entityList[i].getSLEFT().equals("0")){//左括号
                      sb.append(entityList[i].getSLEFT());
                  }
                  dbTypeValue = itemField.getType().getValue();
                  if (entityList[i].getWHEREVALUE().indexOf("P1") > -1 && P1 == null ||
                      entityList[i].getWHEREVALUE().indexOf("P2") > -1 && P2 == null ||
                      entityList[i].getWHEREVALUE().indexOf("P3") > -1 && P3 == null ||
                      entityList[i].getWHEREVALUE().indexOf("P4") > -1 && P4 == null ||
                      entityList[i].getWHEREVALUE().indexOf("P5") > -1 && P5 == null) {
                      sb.append(" 1=1 ");
                  } else {
                      sb.append(getConditionExpression(entityList[i].getFIELD(),
                              entityList[i].getSYMBOL(),
                              entityList[i].getWHEREVALUE(),
                              dbTypeValue,P1,P2,P3,P4,P5,user));
                  }
                  if (!entityList[i].getSRIGHT().equals("0")) { //右括号
                      sb.append(entityList[i].getSRIGHT());
                  }
                  if (i + 1 < entityList.length) { //最后一个条件行无论何时不能加入逻辑符,以统一处理
                      if (entityList[i].getLOGIC().equals("1")) {
                          sb.append(" AND ");                       
                      }
                      if (entityList[i].getLOGIC().equals("2")) {
                          sb.append(" OR ");
                      }
                  }
              }
          }
          LOGGER.info("计算字段中的初始条件:"+sb.toString());
          return sb.toString();
      }

    /**
     * 功能或作用：根据数据库中的最大值与累加数据计算出最大的记录流水号
     * @param strMaxNO 原最大编号
     * @param FieldLen 数据库字段长度
     * @param intadd 累加数
     * @Return MaxNo 执行后返回一个MaxNo字符串
     */
    private String getMaxFieldNoAdd(String strMaxNO, int FieldLen, int intadd){
        String MaxNo = "";
        int LenMaxNo = 0;
        try {
              MaxNo = strMaxNO;
              if (MaxNo.length() > 0){
                MaxNo = String.valueOf(Integer.parseInt(MaxNo) + intadd);
                LenMaxNo = MaxNo.length();
                StringBuffer  addBuf = new   StringBuffer();
                addBuf.append("0000000000000000000000000"+MaxNo);
                MaxNo = addBuf.toString();
              } else {
                MaxNo = "00000000000000000000000001";
                LenMaxNo = 1;
              }
              MaxNo = MaxNo.substring(25 - FieldLen + LenMaxNo);
        }
        catch (Exception ex) {
              System.err.println(ex.getMessage());
        }
        return MaxNo;
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
      addBuf.append("SELECT MAX("+FieldName+") AS MaxNo FROM "+TableName);
      try {
        DBSet dbset = dbEngine.QuerySQL(addBuf.toString());
        if (dbset != null && dbset.RowCount() > 0) {
            MaxNo = dbset.Row(0).Column("MaxNo").getString();
            if (MaxNo != null && MaxNo.length() > 0) {
              MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
              LenMaxNo = MaxNo.length();
              addBuf.delete(0,addBuf.length());//清空
              addBuf.append("0000000000000000000000000"+MaxNo);
              MaxNo = addBuf.toString();
            } else {
              MaxNo = "00000000000000000000000001";
              LenMaxNo = 1;
            }
            dbset = null;//赋空值
        }
        MaxNo = MaxNo.substring(25 - FieldLen + LenMaxNo);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      return MaxNo;
    }

    /**
     * 功能：根据统计配置ID和列号得到计算字段的ID
     * @param strID 配置ID
     * @param tier 列号
     * @return 计算字段的ID
     * @throws Exception 
     */
    private String getComTierID(String strID, String tier) throws Exception {
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("select ID from ANALYSE_STATISTICS_CFIELD where FID='")
              .append(strID+"' and ISSHOW<>'1' order by SHOWCODE");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        int length = retlist != null ? retlist.size() : 0;
        addBuf.delete(0,addBuf.length());//清空
        if (length > 0){
           int i= Integer.parseInt(tier);
           i = i-1;
           addBuf.append(retlist.get(i).get("ID").toString());
        }
        return addBuf.toString();
    }

    /**
     * 功能：得到显示详细信息时所属单位的字段
     * @param strID 配置ID
     * @param strComTierID 计算字段ID
     * @return 返回条件
     * @throws Exception 
     */
    private String getUnitWhere(String strID, String strComTierID) throws Exception{
         StringBuffer addBuf = new StringBuffer();
         addBuf.append("select CPLANARFIELD from ANALYSE_STATISTICS_CFIELD where ID='").append(strComTierID+"'");
         List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(addBuf.toString());
         int length1 = retlist1 != null ? retlist1.size() : 0;
         addBuf.delete(0,addBuf.length());//清空
         if (length1 > 0){
        	 if (retlist1.get(0) != null && retlist1.get(0).get("CPLANARFIELD") != null) {
        		 addBuf.append(retlist1.get(0).get("CPLANARFIELD").toString());
        	 }
         }
         if (addBuf.toString().length()==0 || addBuf.toString().equals("0")) {
             addBuf.delete(0,addBuf.length());//清空
             addBuf.append("select CPLANARFIELD from  ANALYSE_STATISTICS_MAIN  where  ID='").append(strID+"'");
             List<Map<String, Object>> retlist2 = userMapper.selectListMapExecSQL(addBuf.toString());
             int length2 = retlist2 != null ? retlist2.size() : 0;
             addBuf.delete(0,addBuf.length());//清空
             if (length2 > 0){
            	 if (retlist2.get(0) != null && retlist2.get(0).get("CPLANARFIELD") != null) {
            		 addBuf.append(retlist2.get(0).get("CPLANARFIELD").toString());
            	 }
             }
         }
         return addBuf.toString(); 
    }

    /**
     * 功能：根据计算字段ID得到计算字段的条件
     * @param strID 计算字段ID
     * @return 计算字段的条件
     * @throws Exception 
     */
    private String getCFIELDWhere(String strID) throws Exception {
         StringBuffer  addBuf = new   StringBuffer();
         addBuf.append("select EXPRESSIONSWHERE from ANALYSE_STATISTICS_CFIELD where ID='").append(strID+"'");
         Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
         addBuf.delete(0,addBuf.length());//清空
         if (retmap != null && retmap.size() > 0){
             addBuf.append(retmap.get("EXPRESSIONSWHERE").toString());
         }
         return addBuf.toString();
    }

    private String getAddValue(String tablename, String showfield, String idfield, String idvalue) {
        String revalue = "";
        String strSql = "";
        String strtype ="1";
        revalue = (String) SetHashtable4.get(tablename+showfield+idvalue);
        if (revalue==null) {
             strSql = "select "+showfield+" from "+tablename+" where "+idfield+" like '"+idvalue+"%' order by "+idfield;
             DBSet dsSum = dbEngine.QuerySQL(strSql);
             if(dsSum!=null&&dsSum.RowCount()>0){
                strSql = "SELECT b.FIELDTYPE FROM BPIP_TABLE a,BPIP_FIELD b "
                	   + "Where a.TABLEID=b.TABLEID and a.TABLENAME='"+tablename+"' and b.FIELDNAME='"+showfield+"'";
                DBSet dsSum1 = dbEngine.QuerySQL(strSql);
                strtype = dsSum1.Row(0).Column("FIELDTYPE").getString();
                if (strtype==null){strtype ="1";}
                if (strtype.equals("1")){
                   revalue = dsSum.Row(0).Column(showfield).getString();
                }
                if (strtype.equals("4")){
                   revalue = String.valueOf(dsSum.Row(0).Column(showfield).getDate());
                   revalue = revalue.substring(0,10);
                }
                if (strtype.equals("5")){
                   revalue = String.valueOf(dsSum.Row(0).Column(showfield).getDate());
                }
                if (strtype.equals("6")){
                   revalue = String.valueOf(dsSum.Row(0).Column(showfield).getInteger());
                }
             }
             if (revalue==null){revalue="&nbsp;";}
             SetHashtable4.put(tablename+showfield+idvalue,revalue);
        }
        return revalue;
    }

    /**
     * 得到是否有子报表
     * @param FID
     * @param ID
     * @return
     * @throws Exception
     */
    private String issub(String FID, String ID) throws Exception {
        String issub = "0";
        String tablename = "";
        String tablecode = "";
        String strSql = "";
        int getnum = 0;
        //得到统计配置表的相关属性
        ANALYSE_STATISTICS_MAIN Smain = getSmain(FID);
        tablename = Smain.getPLANARTABLE();
        tablecode = Smain.getPLANARFIELD();
        strSql = "select count("+tablecode+") as gnum from "+tablename+" where "+tablecode+" like '"+ID+"%'";
        //DBSet dsSum = dbEngine.QuerySQL(strSql,"st8",FID+ID);
        List<Map<String, Object>> dsSum = userMapper.selectListMapExecSQL(strSql);
        if(dsSum!=null&&dsSum.size()>0){
           getnum = Integer.parseInt(dsSum.get(0).get("gnum").toString());
        }
        if (getnum>1){issub = "1";}
        return issub;
    }

    private void doMemory_Manage() {
         LOGGER.info("内存清理开始。");
         initHashtable();
         LOGGER.info("内存清理清结束。");
    }

    public void initHashtable() {
         SetHashtable1.clear();
         SetHashtable2.clear();
         SetHashtable3.clear();
         SetHashtable31.clear();
         SetHashtable4.clear();
         dbEngine.inithashtable();
    }

    class ScanTask extends TimerTask {
        public void run() {
            LOGGER.info("数据分析引擎扫描任务开始！");
            doMemory_Manage();
        }
    }

    /**
     * ftl  调整（获取element ui 统计显示表头）
     */
	@Override
	public Map<String,Object> getShowPcHtml(String strID, String userID) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> returnMap = new HashMap<>();
		List<Object> headsRows = new ArrayList<>();//数据头部
		List<Map<String,Object>> headsSpans = new ArrayList<>();//表头合并参数
		ANALYSE_STATISTICS_MAIN Smain = null;
		Smain = getSmain(strID);
	    //由于网上销售版取消统计模块路径的设置,利用自定义表单模板路径来处理。
		String strTemplat = SysPreperty.getProperty().LogFilePath;
	       strTemplat = strTemplat.substring(0,strTemplat.length()-3)+"StatExcel/"+Smain.getTIMETEMPLATE();

	       Parser parser = new Parser();
	       Html html = null;
	       try {
	          parser.setURL(strTemplat);
	          parser.setEncoding("UTF-8");
	          html = (Html) (parser.elements().nextNode());
	          LOGGER.info("解析装载模版成功");
	       }
	       catch (ParserException ex) {
	    	   LOGGER.error("分析模板:", ex.toString());
	       }

	       TableRow tr = null;
	       NodeList col = null;
	       NodeList nl = null;
	       TableTag tt = null;
	       NodeList nlTable = html.searchFor(new TableTag().getClass(), true);
	       String oldStr="",newStr="",StrName="";
	       for (int i = 0; i < nlTable.size(); i++) {
	            tt = (TableTag) nlTable.elementAt(i);
	            nl = tt.searchFor(new TableRow().getClass(), true);
	            List<String> lastWidth = new ArrayList<>();//表头最后一行宽度
	            List<Map<Integer,Integer>> rows = new ArrayList<>();
	            Map<Integer,Integer> rowsStrat = new HashMap<>();
	            //循环<tr>
	            for (int j = 0; j < nl.size(); j++) {
	            	Map<String,String> colsName = new HashMap<>();//数据头部名称
	            	int fidNum=1;
	            	Map<Integer,Integer> cols = new HashMap<>();
	                tr = (TableRow) nl.elementAt(j);
	                col = tr.searchFor(new TableColumn().getClass(), true);
	                //遍历<td>组件
	                Map<Integer,Integer> upTdNum=new HashMap<>();
	                for (int k = 0; k < col.size(); k++) {
	         	        int rowspan=0,colspan=0;
	         	        cols.put(k, 0);
	                	Map<String,Object> tdRowColsSpan = new HashMap<>();//占用的行和列数
	                	//字段名称，
	                	oldStr=col.elementAt(k).getText().replaceAll("\\\\n", "").toLowerCase();//转换小写
	                	oldStr=oldStr.replaceAll("td", "");//替换td
	                	StrName=col.elementAt(k).getChildren().toHtml().replaceAll("\\\\n", "").trim();//去除换行符
	                	//获取占用的行数
	                	if (oldStr.indexOf("rowspan")>-1) {
	                		newStr=oldStr.substring(oldStr.indexOf("rowspan"), oldStr.length());
	                		newStr=newStr.substring(newStr.indexOf("\"")+1, newStr.length());
	                		rowspan=Integer.valueOf(newStr.substring(0, newStr.indexOf("\"")));
	                		rowspan = rowspan>1 ? rowspan-1 : 0;
	                	}
	                	//获取占用几列colspan
	                	if (oldStr.indexOf("colspan")>-1) {
	                		newStr=oldStr.substring(oldStr.indexOf("colspan"), oldStr.length());
	                		newStr=newStr.substring(newStr.indexOf("\"")+1, newStr.length());
	                		colspan=Integer.valueOf(newStr.substring(0, newStr.indexOf("\"")));
	                		colspan = colspan>1 ? colspan-1 : 0;
	                	}
	                	upTdNum.put(k, rowspan);
	                	if(k==0) {
	                		rowsStrat.put(j, rowspan);
	                	}else if(k>1&&upTdNum.get((k-1))>0) {
	                		rowsStrat.put(j, rowsStrat.get(j)+upTdNum.get((k-1)));
	                	}
	                	cols.put(k, rowspan);
	                	
	                	//加载起始,占用列数量
	                	if(k==0&&j>0&&rowsStrat.get((j-1))>0) {
	                		for(int kg=0;kg<rowsStrat.get((j-1));kg++) {
	                			colsName.put("FIELD"+fidNum,"");
	                			fidNum ++;
	                		}
	                	}
	                	int h = j>0&&rowsStrat.get((j-1)) >0 ? rowsStrat.get((j-1)) : 0;
	                	
	                	//加载头部前，先判断上一行是否占用本行
	                	int upRow = j>0 ? j-1 : 0;
	                	if(j>0&&rows.get(upRow)!=null&&rows.get(upRow).containsKey((k+h))&&rows.get(upRow).get((k+h))>0) {//判断上一行是否占用本行
	                		int len = rows.get(upRow).get((k+h));
	                		for(int kg=0;kg<len;kg++) {
	                			colsName.put("FIELD"+(fidNum),"");
	                			fidNum ++;
	                		}
	                		colsName.put("FIELD"+(fidNum),""+StrName+"");
                			fidNum ++;
	                		//将上一行值-1，加载到本行，便于下一行应用
	                		int nextRows = len>1 ? len-1 : 1;
	                		cols.put(k, nextRows);
	                	}else {
	                		colsName.put("FIELD"+(fidNum),""+StrName+"");
                			fidNum ++;
	                	}
	                	
	                	//最后一行，判断有宽度，加载宽度，
	                	if(j==nl.size()-1){
	                		if(oldStr.indexOf("width")>-1) {
	                			String width=oldStr.substring(oldStr.indexOf("width"), oldStr.length());
	                			width=width.substring(width.indexOf("\"")+1, width.length());
	                			width=width.substring(0, width.indexOf("\""));
	                			lastWidth.add(width);
	                		}else {
	                			lastWidth.add("100");
	                		}
	                	}
	                	
	                	if(rowspan>0||colspan>0) {
	                		//加载空格
	                		if(colspan>0) {
	                			for(int tag=0;tag<colspan;tag++) {
	                				colsName.put("FIELD"+(fidNum),"");
		                			fidNum ++;
	                			}
	                		}
	                		rowspan = rowspan>0 ? rowspan+1 : 1;
	                		colspan = colspan>0 ? colspan+1 : 1;
	                		//装载合并单元格
	                		tdRowColsSpan.put("row", j);
	                		tdRowColsSpan.put("col", k+h);
	                		tdRowColsSpan.put("rowspan", rowspan);
	                		tdRowColsSpan.put("colspan", colspan);
	                		headsSpans.add(tdRowColsSpan);
	                	}
	                }
	                rows.add(cols);
	                //装载表头[]
	                headsRows.add(colsName);
	            }
	            returnMap.put("headWidth", lastWidth);
	        }
	        String spanJson= JSON.toJSON(headsSpans).toString();
	        String headsJson= JSON.toJSON(headsRows).toString();
	        returnMap.put("headData", headsJson);
	        returnMap.put("headSpan", spanJson.replaceAll("\"", ""));
	        return returnMap;
	}

	
	
	/**
	 * ftl  调整（获取pc端报表生成的报表数据）
	 */
	@Override
	public String getShowPcDatas(String strID, String userID) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer addBuf = new StringBuffer();
        //删除临时统计表中本用户统计的不相关的数据
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("delete from ANALYSE_STATISTICS_RESULT Where (SUSERNO='"+userID+"' and FID<>'"+strID+"') or FID is null");
        userMapper.deleteExecSQL(addBuf.toString());
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("Select * From ANALYSE_STATISTICS_RESULT Where SUSERNO='"+userID+"' order by ID");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        int length = retlist != null ? retlist.size() : 0;
        String strYunit="";
        if (length > 0) {
            for (int a1 = 0; a1 < length; a1++) {
          	  Map<String, Object> retmap = retlist.get(a1);
                strYunit = retmap.get("FIELD50").toString();
                if (strYunit==null){strYunit="";}
                if (issub(strID,strYunit).equals("1") && !strYunit.equals("0")){//有子级时
                	retlist.get(a1).put("ZRDOWN", strYunit);
                }else {
                	retlist.get(a1).put("ZRDOWN", 0);
                }
             }
        }
        //--------------------------------
        String json= JSON.toJSON(retlist).toString();
		return json;
	}

	/**
	 * ftl  调整（生成下拉字段表条件）
	 */
	@Override
	public List<Map<String, Object>> getVueCodeHtml(String tablename) throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> retlist = null;
        if (tablename.length() > 0) {
            String strSQL = "select CODE,NAME from "+tablename+" order by CODE";
            retlist = userMapper.selectListMapExecSQL(strSQL);
        }
        return retlist;
	}
}