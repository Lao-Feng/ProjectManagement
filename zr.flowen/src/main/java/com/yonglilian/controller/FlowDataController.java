package com.yonglilian.controller;

import com.alibaba.fastjson.JSONObject;
import com.yonglilian.flowengine.mode.monitor.FLOW_RUNTIME_ENTRUSTLOG;
import com.yonglilian.flowengine.mode.monitor.FLOW_RUNTIME_PROCESS;
import com.yonglilian.model.BPIP_USER_IDEA;
import com.yonglilian.service.FlowMonitorService;
import com.yonglilian.service.UserIdeaService;
import com.yonglilian.service.impl.FlowMonitorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.SessionUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 字典管理控制器
 * @author lwk
 *
 */
@ZrSafety
@RestController
@RequestMapping("/flowdata")
public class FlowDataController {
	/** The DictDataController Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowDataController.class);
	/** 意见设置服务层 . */
	@Autowired
	private UserIdeaService userIdeaService;
	/** 流程监控函数库服务层. */
	@Autowired
	private FlowMonitorService flowMonitorService;

	/**
	 * 获取字典JSON数据
	 * @param method
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"/json"}, produces = {"application/json;charset=UTF-8"})
	public String actionJson(String method, HttpServletRequest request, HttpServletResponse response){
		String result = "";
		try {
			
			// 生成==流程个人处理意见
			if ("flowidea".equals(method)) {
				result = getFlowidea(request, response);
			}
			else if ("flowDbList".equals(method)) {
				result = flowDbList(request, response);
			}
			else if ("flowGetProList".equals(method)) {
				result = getProList(request, response);
			}
			else if ("flowGetProList2".equals(method)) {
				result = getProList2(request, response);
			}
			else if ("flowGetProFinshList".equals(method)) {
				result = getProFinshList(request, response);
			}
			else if ("flowDailyList".equals(method)) {
				result = dailyList(request, response);
			}
			else if ("flowGetGetProList1".equals(method)) {
				result = getGetProList1(request, response);
			}else {
			}
		} catch (Exception ex) {
			LOGGER.error("DictDataController.actionJson Exception:\n", ex);
		}
		return result;
	}

    

    /**
     * 流程引擎
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String getFlowidea(HttpServletRequest request,HttpServletResponse response) throws Exception{
         BPIP_USER_IDEA[] ideas = null;
         String struserid=request.getParameter("userid");
         ideas = userIdeaService.getIdeaList1(struserid);
         String strshow_data = "";
         
         if(ideas.length>0&&ideas!=null){
             strshow_data = "{\"total\":" + ideas.length + ",\"rows\":[";
             for(int i=0;i<ideas.length;i++){
                    strshow_data = strshow_data + "{";
                    strshow_data = strshow_data + "\"ID\":\"" + ideas[i].getID()+ "\",";
                    if (i < ideas.length - 1) {
                        strshow_data = strshow_data + "\"CONTENT\":\""+ ideas[i].getCONTENT()+ "\"},\r\n";
                    } else {
                        strshow_data = strshow_data + "\"CONTENT\":\""+ ideas[i].getCONTENT()+ "\"}";
                    }
              }
              strshow_data = strshow_data + "]}";
         } else {
        	 strshow_data = "{\"total\":0,\"rows\":[]}";
         }
         return strshow_data;
    }

	private String flowDbList(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception {
        response.setContentType("application/json");
        //request.setCharacterEncoding("utf-8");
        //response.setContentType("text/html; charset=utf-8");
        JSONObject json = new JSONObject();
        HttpSession session = request.getSession(true);
        SessionUser user = (SessionUser)session.getAttribute("userinfo");
        String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
        List<Object> newList = new ArrayList<Object>();
        
        FLOW_RUNTIME_PROCESS[] flow = null;
        
        String page=request.getParameter("page");
        String ID=request.getParameter("ID");
        String FDATE1=request.getParameter("FDATE1");
        String FDATE2=request.getParameter("FDATE2");
        String strw2 = " 1=1 ";

         if(request.getParameter("ID")!=null && !request.getParameter("ID").equals("")){
             strw2 = strw2+" and B.ID = '"+ID+"'";
         }
         if(request.getParameter("FDATE1")!=null && !request.getParameter("FDATE1").equals("")){
                if (request.getParameter("FDATE2")!=null && !request.getParameter("FDATE2").equals("")){
                     if (DataBaseType.equals("1")) {
                    	 strw2 = strw2 +" and B.CREATEDATE >= to_date('"+FDATE1+" 00:00:00','yyyy-mm-dd hh24:mi:ss') AND B.CREATEDATE <= to_date('"+FDATE2+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
                     }
                     else if (DataBaseType.equals("2")) {
                    	 strw2 = strw2 +" and B.CREATEDATE >= '"+FDATE1+" 00:00:00' AND B.CREATEDATE <= '"+FDATE2+" 23:59:59'";
                     }
                     else if (DataBaseType.equals("3")) {
                         strw2 = strw2 +" and B.CREATEDATE >= '"+FDATE1+" 00:00:00' AND B.CREATEDATE <= '"+FDATE2+" 23:59:59'";
                     }
                }
         }

        //得到查询的记录集
	    flow = flowMonitorService.getFlowTransactList2(user.getUserID(),Integer.parseInt(page),20,strw2);
	    String pageCount="0";
	    try {
                 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 if (flow!=null && flow.length>0){
                     pageCount = FlowMonitorServiceImpl.GetFlowTransactList2_pageCount;
                     for (int i=0; i<flow.length; i++) {
                    	 try {
                              Map<String, Object> map = new HashMap<String, Object>();
                              map.put("ID", flow[i].getID());
                              map.put("NAME", flow[i].getNAME());
                              map.put("CREATEDATE", formatter.format(flow[i].getCREATEDATE()));
                              
                              newList.add(map);
                    	 } catch (Exception e) {}
                     }
                }
        }catch (Exception ex2) {}

        json.put("rows", newList);
        json.put("total",String.valueOf(pageCount));
        newList=null;
        return json.toJSONString();
    }

	private String getProList(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception {
        response.setContentType("application/json");
        //request.setCharacterEncoding("utf-8");
        //response.setContentType("text/html; charset=utf-8");
        JSONObject json=new JSONObject();
        //HttpSession session=request.getSession(true);
	    //SessionUser user = (SessionUser)session.getAttribute("userinfo");
	    String DataBaseType = SysPreperty.getProperty().DataBaseType;//数据库类型
	    List<Object> newList = new ArrayList<Object> ();

        FLOW_RUNTIME_PROCESS[] flow = null;

        String page=request.getParameter("page");
        String ID=request.getParameter("ID");
        String FDATE1=request.getParameter("FDATE1");
        String FDATE2=request.getParameter("FDATE2");
        String strw2 = " 1=1 ";

         if(request.getParameter("ID")!=null && !request.getParameter("ID").equals("")){
             strw2 = strw2+" and B.ID LIKE '%"+ID+"%'";
         }
         if(request.getParameter("FDATE1")!=null && !request.getParameter("FDATE1").equals("")) {
                if (request.getParameter("FDATE2")!=null && !request.getParameter("FDATE2").equals("")) {
                     if (DataBaseType.equals("1")) {
                    	 strw2 = strw2 +" and B.CREATEDATE >= to_date('"+FDATE1+" 00:00:00','yyyy-mm-dd hh24:mi:ss') AND B.CREATEDATE <= to_date('"+FDATE2+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
                     }
                     else if (DataBaseType.equals("2")) {
                    	 strw2 = strw2 +" and B.CREATEDATE >= '"+FDATE1+" 00:00:00' AND B.CREATEDATE <= '"+FDATE2+" 23:59:59'";
                     }
                     else if (DataBaseType.equals("3")) {
                         strw2 = strw2 +" and B.CREATEDATE >= '"+FDATE1+" 00:00:00' AND B.CREATEDATE <= '"+FDATE2+" 23:59:59'";
                     }
                  }
        }
        //得到查询的记录集
	    flow = flowMonitorService.getProList(Integer.parseInt(page),20,strw2);
	    String pageCount="0";
        try {
                 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 if (flow!=null && flow.length>0){
                         pageCount=FlowMonitorServiceImpl.GetProList_pageCount;
                         for(int i=0;i<flow.length;i++){
                         try {
                                  Map<String, Object> map = new HashMap<String, Object>();
                                  map.put("ID", flow[i].getID());
                                  map.put("NAME", flow[i].getNAME());
                                  map.put("CREATEPSN", flow[i].getCREATEPSN());
                                  map.put("CREATEDATE", formatter.format(flow[i].getCREATEDATE()));
                                  
                                  newList.add(map);
                        } catch (Exception e) {}
                  }
                }
        }catch (Exception ex2) {}

        json.put("rows", newList);
        json.put("total",String.valueOf(pageCount));
        newList=null;
        return json.toJSONString();
    }

	private String getProList2(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception {
        response.setContentType("application/json");
        //request.setCharacterEncoding("utf-8");
        //response.setContentType("text/html; charset=utf-8");
        JSONObject json = new JSONObject();
        HttpSession session = request.getSession(true);
	    SessionUser user = (SessionUser)session.getAttribute("userinfo");
	    String DataBaseType = SysPreperty.getProperty().DataBaseType;//数据库类型
	    List<Object> newList = new ArrayList<Object> ();

        FLOW_RUNTIME_PROCESS[] flow = null;

        String page=request.getParameter("page");
        String ID=request.getParameter("ID");
        String FDATE1=request.getParameter("FDATE1");
        String FDATE2=request.getParameter("FDATE2");
        String strw2 = " 1=1 ";

         if(request.getParameter("ID")!=null && !request.getParameter("ID").equals("")){
             strw2 = strw2+" and B.ID LIKE '%"+ID+"%'";
         }
         if(request.getParameter("FDATE1")!=null && !request.getParameter("FDATE1").equals("")) {
                if (request.getParameter("FDATE2")!=null && !request.getParameter("FDATE2").equals("")) {
                     if (DataBaseType.equals("1")) {
                        strw2 = strw2 +" and B.CREATEDATE >= to_date('"+FDATE1+" 00:00:00','yyyy-mm-dd hh24:mi:ss') AND B.CREATEDATE <= to_date('"+FDATE2+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
                     }
                     else if (DataBaseType.equals("2")) {
                        strw2 = strw2 +" and B.CREATEDATE >= '"+FDATE1+" 00:00:00' AND B.CREATEDATE <= '"+FDATE2+" 23:59:59'";
                     }
                     else if (DataBaseType.equals("3")) {
                        strw2 = strw2 +" and B.CREATEDATE >= '"+FDATE1+" 00:00:00' AND B.CREATEDATE <= '"+FDATE2+" 23:59:59'";
                     }
                }
         }
        //得到查询的记录集
	    flow = flowMonitorService.getProList2(user.getUserID(),Integer.parseInt(page),20,strw2);
	    String pageCount="0";
        try {
                 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 if (flow!=null && flow.length>0){
                         pageCount=FlowMonitorServiceImpl.GetProList2_pageCount;
                         for(int i=0;i<flow.length;i++){
                          try {
                                  Map<String, Object> map = new HashMap<String, Object>();
                                  map.put("ID", flow[i].getID());
                                  map.put("NAME", flow[i].getNAME());
                                  map.put("CREATEPSN", flow[i].getCREATEPSN());
                                  map.put("CREATEDATE", formatter.format(flow[i].getCREATEDATE()));
                                  map.put("FACCEPTPSN", flow[i].getFACCEPTPSN());
                                  map.put("ACCEPTPSN", flow[i].getACCEPTPSN());

                                  newList.add(map);
                        } catch (Exception e) {}
                  }
                }
        }catch (Exception ex2) {}

        json.put("rows", newList);
        json.put("total",String.valueOf(pageCount));
        newList=null;
        return json.toJSONString();
    }

	private String getProFinshList(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception {
        response.setContentType("application/json");
        //request.setCharacterEncoding("utf-8");
        //response.setContentType("text/html; charset=utf-8");
        JSONObject json=new JSONObject();
        HttpSession session=request.getSession(true);
	    SessionUser user = (SessionUser)session.getAttribute("userinfo");
	    String DataBaseType = SysPreperty.getProperty().DataBaseType;//数据库类型
	    List<Object> newList = new ArrayList<Object>();

        FLOW_RUNTIME_PROCESS[] flow = null;

        String page=request.getParameter("page");
        String ID=request.getParameter("ID");
        String FDATE1=request.getParameter("FDATE1");
        String FDATE2=request.getParameter("FDATE2");
        String strw2 = " 1=1 ";

         if(request.getParameter("ID")!=null && !request.getParameter("ID").equals("")){
             strw2 = strw2+" and B.ID LIKE '%"+ID+"%'";
         }
         if(request.getParameter("FDATE1")!=null && !request.getParameter("FDATE1").equals("")) {
                if (request.getParameter("FDATE2")!=null && !request.getParameter("FDATE2").equals("")) {
                     if (DataBaseType.equals("1")) {
                    	 strw2 = strw2 +" and B.CREATEDATE >= to_date('"+FDATE1+" 00:00:00','yyyy-mm-dd hh24:mi:ss') AND B.CREATEDATE <= to_date('"+FDATE2+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
                     }
                     else if (DataBaseType.equals("2")) {
                    	 strw2 = strw2 +" and B.CREATEDATE >= '"+FDATE1+" 00:00:00' AND B.CREATEDATE <= '"+FDATE2+" 23:59:59'";
                     }
                     else if (DataBaseType.equals("3")) {
                         strw2 = strw2 +" and B.CREATEDATE >= '"+FDATE1+" 00:00:00' AND B.CREATEDATE <= '"+FDATE2+" 23:59:59'";
                     }
                }
         }
        //得到查询的记录集
	    flow = flowMonitorService.getProFinshList(user.getUserID(),Integer.parseInt(page),20,strw2);
	    String pageCount="0";
        try {
                 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 if (flow!=null && flow.length>0){
                         pageCount=FlowMonitorServiceImpl.GetProFinshList_pageCount;
                         for(int i=0;i<flow.length;i++){
                          try {
                                  Map<String, Object> map = new HashMap<String, Object>();
                                  map.put("ID", flow[i].getID());
                                  map.put("NAME", flow[i].getNAME());
                                  map.put("CREATEPSN", flow[i].getCREATEPSN());
                                  map.put("CREATEDATE", formatter.format(flow[i].getCREATEDATE()));

                                  newList.add(map);
                        } catch (Exception e) {}
                  }
                }
        }catch (Exception ex2) {}

        json.put("rows", newList);
        json.put("total",String.valueOf(pageCount));
        newList=null;
        return json.toJSONString();
    }

    private String dailyList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        //request.setCharacterEncoding("utf-8");
        //response.setContentType("text/html; charset=utf-8");
        JSONObject json = new JSONObject();
        //HttpSession session=request.getSession(true);
	    //SessionUser user = (SessionUser)session.getAttribute("userinfo");
	    //String DataBaseType = SysPreperty.getProperty().DataBaseType;//数据库类型
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String page=request.getParameter("page");
	    
	    //枚举值1
	    Hashtable<String, String> enumMap = new Hashtable<String, String>();
	    enumMap.put("1","重分配权限");
	    enumMap.put("2","个人委托");
	    enumMap.put("3","移交");
	    enumMap.put("4","权限委托");
	    
	    List<Object> newList = new ArrayList<Object>();
	    FLOW_RUNTIME_ENTRUSTLOG[] Daily = null;
	    //BPIP_USER[] puser1 = null;
	    Daily = flowMonitorService.dailyList(Integer.parseInt(page),20);

	    int count = 0;
	    if (Daily!=null && Daily.length>0){
	        count = Daily.length;
	        for(int i=0;i<Daily.length;i++){
	                 try {
                              Map<String, Object> map = new HashMap<String, Object>();
                              map.put("ID", Daily[i].getID());
                              map.put("SUSERNO", Daily[i].getSUSERNO());
                              map.put("IUSERNO", Daily[i].getIUSERNO());
                              map.put("SDATE", formatter.format(Daily[i].getSDATE()));
                              map.put("EDATE", formatter.format(Daily[i].getEDATE()));
                              map.put("TYPE", enumMap.get(Daily[i].getTYPE()));
                              map.put("FLOWNAME", Daily[i].getFLOWNAME());
                              map.put("FLOWID", Daily[i].getFLOWID());
                              map.put("FLOWNODE", Daily[i].getFLOWNODE());
                              map.put("LOGDATE", formatter.format(Daily[i].getLOGDATE()));
                              
                              newList.add(map);
                    } catch (Exception e) {}
	        }
	    }
        json.put("rows", newList);
        json.put("total",String.valueOf(count));

        newList=null;
        return json.toJSONString();
    }

	private String getGetProList1(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception {
        //response.setContentType("application/json");
        //request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        JSONObject json=new JSONObject();
        HttpSession session=request.getSession(true);
	    SessionUser user = (SessionUser)session.getAttribute("userinfo");
	    String DataBaseType = SysPreperty.getProperty().DataBaseType;//数据库类型
	    List<Object> newList = new ArrayList<Object>();

        FLOW_RUNTIME_PROCESS[] flow = null;
        String page=request.getParameter("page");

        String ID=request.getParameter("ID");
        String FDATE1=request.getParameter("FDATE1");
        String FDATE2=request.getParameter("FDATE2");
        String type=request.getParameter("type");
        String strw2 = " 1=1 ";

         if(request.getParameter("ID")!=null && !request.getParameter("ID").equals("")){
                 strw2 = strw2+" and B.ID LIKE '%"+ID+"%'";
         }
         if(request.getParameter("FDATE1")!=null && !request.getParameter("FDATE1").equals("")) {
                if (request.getParameter("FDATE2")!=null && !request.getParameter("FDATE2").equals("")) {
                     if (DataBaseType.equals("1")) {
                    	 strw2 = strw2 +" and B.CREATEDATE >= to_date('"+FDATE1+" 00:00:00','yyyy-mm-dd hh24:mi:ss') AND B.CREATEDATE <= to_date('"+FDATE2+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
                     }
                     else if (DataBaseType.equals("2")) {
                    	 strw2 = strw2 +" and B.CREATEDATE >= '"+FDATE1+" 00:00:00' AND B.CREATEDATE <= '"+FDATE2+" 23:59:59'";
                     }
                     else if (DataBaseType.equals("3")) {
                         strw2 = strw2 +" and B.CREATEDATE >= '"+FDATE1+" 00:00:00' AND B.CREATEDATE <= '"+FDATE2+" 23:59:59'";
                     }
                  }
         }
         Map<String, String> enums = new HashMap<String, String>();
         enums.put("0","否");
         enums.put("1","否");
         enums.put("2","否");
         enums.put("3","否");
         enums.put("4","是");
         System.out.println(strw2);
         //得到查询的记录集
         flow = flowMonitorService.getProList1(user.getUserID(),user.getUnitID().toString(),Integer.parseInt(page),20,type,strw2);

         String pageCount="0";
         try {
                 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 if (flow!=null && flow.length>0){
                         pageCount=FlowMonitorServiceImpl.GetProList1_pageCount;
                         for(int i=0;i<flow.length;i++){
                          try {
                                  Map<String, Object> map = new HashMap<String, Object>();
                                  map.put("ID", flow[i].getID());
                                  map.put("NAME", flow[i].getNAME());
                                  map.put("CURRACTIVITY", flow[i].getCURRACTIVITY());
                                  map.put("CREATEDATE", formatter.format(flow[i].getCREATEDATE()));
                                  map.put("ACCEPTDATE", formatter.format(flow[i].getACCEPTDATE()));
                                  map.put("FACCEPTPSN", flow[i].getFACCEPTPSN());
                                  map.put("ACCEPTPSN", flow[i].getACCEPTPSN());
                                  map.put("STATE", enums.get(flow[i].getSTATE()));
                                  
                                  newList.add(map);
                        } catch (Exception e) {}
                  }
                }
        }catch (Exception ex2) {}
        json.put("rows", newList);
        json.put("total",String.valueOf(pageCount));
        newList=null;
        return json.toJSONString();
    }

   
}