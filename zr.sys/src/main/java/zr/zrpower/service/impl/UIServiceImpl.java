package zr.zrpower.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.UserMapper;
import zr.zrpower.dao.mapper.UntilMapper;
import zr.zrpower.model.BPIP_MENU;
import zr.zrpower.service.UIService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 界面公共函数库服务层实现
 * @author lwk
 *
 */
@Service
public class UIServiceImpl implements UIService {
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UntilMapper untilMapper;

	@Override
	public String[] loadMenuJqueryOne(String userID) throws Exception {
		String returnStr[] = new String[2];
		String returnStr1 = "";
		String strSql = "", MenId = "", SMenId = "", Name = "";
		
		// 查询第一级菜单
		strSql = "select MENUNO,MENUNAME,URL,ISOPEN From BPIP_MENU where FLAG='1' and MENUNO like '___' and "
				+ "MENUNO in (select distinct substr(MENUNO,1,3) from BPIP_ROLE_RERMISSISSON where ROLEID "
				+ "in (select ROLEID from BPIP_USER_ROLE where USERID='" + userID + "')) order by MNCODE,MENUNO";
		
		List<Map<String, Object>> retList1 = userMapper.selectListMapExecSQL(strSql);
		int length1 = retList1 != null ? retList1.size() : 0;
		if (length1 > 0) {
			for (int i = 0; i < length1; i++) {
				Map<String, Object> retMap1 = retList1.get(i);
				MenId = retMap1.get("MENUNO").toString();
				Name = retMap1.get("MENUNAME").toString();
				if (i == 0) {
					SMenId = MenId;
				}
				returnStr1 = returnStr1 + "<li class=\"layui-nav-item\">\r\n";
				returnStr1 = returnStr1 + "<a href=\"javascript:opensub('/user/actionuser?Act=main&ID=" + MenId
										+ "');\"><p style=\"color:#293964;\">" + Name + "</p></a>\r\n";
				returnStr1 = returnStr1 + "</li>";
			}
		}
		returnStr[0] = SMenId;
		returnStr[1] = returnStr1;

		return returnStr;
	}

	@Override
	public String loadMenuJquery(String userID, String ymenId) throws Exception {
		String strSql = "", MenId = "", Name = "", url = "javascript:;";
		String reList = "";
		reList = "var navs = [";
		// 查询第二级菜单
		strSql = "select URL,MENUNO,ISOPEN,MENUNAME from BPIP_MENU where FLAG='1' and " + "MENUNO like '" + ymenId
				+ "___' and MENUNO in (select distinct substr(MENUNO,1,6) from BPIP_ROLE_RERMISSISSON where ROLEID in "
				+ "(select ROLEID from BPIP_USER_ROLE where USERID='" + userID + "')) order by MNCODE,MENUNO";
		
		List<Map<String, Object>> retList2 = userMapper.selectListMapExecSQL(strSql);
		int length2 = retList2 != null ? retList2.size() : 0;
		if (length2 > 0) {
			for (int j = 0; j < length2; j++) {
				Map<String, Object> retMap2 = retList2.get(j);
				MenId = retMap2.get("MENUNO").toString();
				Name = retMap2.get("MENUNAME").toString();
				if (j == length2 - 1) {
					reList = reList + ",{\r\n";
				} else {
					reList = reList + "{\r\n";
				}
				reList = reList + "\"title\": \"" + Name + "\",\r\n";
				reList = reList + "\"icon\": \"fa-table\",\r\n";
				if (j < 3) {
					reList = reList + "\"spread\": true,\r\n";
				} else {
					reList = reList + "\"spread\": false,\r\n";
				}
				
				// 查询第三级菜单
				strSql = "select  URL,MENUNO,ISOPEN,MENUNAME from BPIP_MENU where FLAG='1' and ISPOWER='1' "
						+ "and MENUNO like '" + MenId
						+ "___'  and MENUNO in (select MENUNO from BPIP_ROLE_RERMISSISSON where ROLEID in "
						+ "(select ROLEID from BPIP_USER_ROLE where USERID='" + userID + "')) order by MNCODE,MENUNO";
				
				List<Map<String, Object>> retList3 = userMapper.selectListMapExecSQL(strSql);
				int length3 = retList3 != null ? retList3.size() : 0;
				if (length3 > 0) {// 存在第三级菜单
					reList = reList + "\"children\": [";
					for (int k = 0; k < length3; k++) {
						Map<String, Object> retMap3 = retList3.get(k);
						Name = retMap3.get("MENUNAME").toString();
						MenId = retMap3.get("MENUNO").toString();
						
						Object urlObj3 = retMap3.get("URL");
						if (urlObj3 != null && urlObj3.toString().length() > 1) {
							Object isOpen3 = retMap3.get("ISOPEN");
							if (isOpen3 != null && isOpen3.toString().equals("1")) {// 新窗口打开
								url = "/iframe?page="+retMap3.get("URL").toString() + ",newopen";
							} else {// 本地窗口打开
								url = "/iframe?page="+retMap3.get("URL").toString();
							}
						} else {
							url = "";
						}
						if (k == (length3 - 1)) {
							reList = reList + "{\r\n";
							reList = reList + "\"title\": \"" + Name + "\",\r\n";
							reList = reList + "\"icon\": \"fa-navicon\",\r\n";
							reList = reList + "\"href\": \"" + url + "\"\r\n";
							reList = reList + "}";
						} else {
							reList = reList + "{\r\n";
							reList = reList + "\"title\": \"" + Name + "\",\r\n";
							reList = reList + "\"icon\": \"fa-navicon\",\r\n";
							reList = reList + "\"href\": \"" + url + "\"\r\n";
							reList = reList + "},";
						}
					}
					reList = reList + "]\r\n";
				}
				if (j == (length2 - 1)) {
					reList = reList + "}";
				} else {
					reList = reList + "},";
				}
			}
		}
		reList = reList + "];\r\n";
		reList = reList.replaceAll(",,", ",");

		return reList;
	}

	@Override
	public List<BPIP_MENU> loadUserMenu(String userID) throws Exception {
		//加载用户菜单权限  userId,flag,menuNo,len
		Map<String,Object> map = new HashMap<>();
		map.put("userId", userID);
		map.put("flag", "1");
		map.put("menuNo", "___");
		map.put("len", 3);
		List<BPIP_MENU> list = null;
		List<BPIP_MENU> list1 = untilMapper.userMenuList(map);
		if(list1!=null&&list1.size()>0) {
			list = new ArrayList<>();
			list.addAll(list1);
			for(int i=0;i<list1.size();i++) {
				map.put("menuNo", ""+list1.get(i).getMENUNO()+"___");
				map.put("len", 6);
				List<BPIP_MENU> list2 = untilMapper.userMenuList(map);
				if(list2!=null&&list2.size()>0) {
					list.addAll(list2);
					for(int k=0;k<list2.size();k++) {
						map.put("menuNo", ""+list2.get(k).getMENUNO()+"___");
						map.put("len", 9);
						List<BPIP_MENU> list3 = untilMapper.userMenuList(map);
						if(list3!=null&&list3.size()>0) {
							list.addAll(list3);
							for(int m=0;m<list3.size();m++) {
								map.put("menuNo", ""+list3.get(m).getMENUNO()+"___");
								map.put("len", 12);
								List<BPIP_MENU> list4 = untilMapper.userMenuList(map);
								if(list4!=null&&list4.size()>0) {
									list.addAll(list4);
								}
							}
						}
					}
				}
			}
		}
        return list;
	}

	@Override
	public String showHeadHtml(String strTitle, String userImagePath) throws Exception {
		StringBuffer strResult = new StringBuffer();
		if (userImagePath.equals("6")) {
			strResult.append("<TABLE border='0' width='100%' height='100%' cellSpacing='0' cellPadding='0'>"+"\r\n");
			//strResult.append("  <TR>"+"\r\n");
		} else {
			strResult.append("<TABLE border='0' width='100%' height='100%' cellSpacing='0' cellPadding='0'>"+"\r\n");
			strResult.append("  <TR>"+"\r\n");
	        strResult.append("     <TD width='100%' height='100%' valign='center' align='center'>"+"\r\n");
	        strResult.append("       <TABLE width='100%' height='100%' class='Window' cellSpacing='0' cellPadding='0' align='center' border='0'>"+"\r\n");
	        strResult.append("         <TR>"+"\r\n");
	        strResult.append("           <TD height='22' width='100%'>"+"\r\n");
	        strResult.append("             <TABLE border='0' width='100%' height='100%' cellspacing='0' cellpadding='0'>"+"\r\n");
	        strResult.append("               <TR>"+"\r\n");
	        strResult.append("                 <TD align='left' background='"+SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/"+userImagePath+"/WindowTitle.gif' align=center>&nbsp;<img align='absmiddle' src='"+SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/"+userImagePath+"/IcoTitle.gif'>&nbsp;<font class='titleFont'>"+strTitle+"</font></TD>"+"\r\n");
	        strResult.append("               </TR>"+"\r\n");
	        strResult.append("             </TABLE>"+"\r\n");
	        strResult.append("           </TD>"+"\r\n");
	        strResult.append("         </TR>"+"\r\n");
		}
		return strResult.toString();
	}

	@Override
	public String showHeadHtml(String strTitle, String titleImage, String userImagePath) throws Exception {
		StringBuffer strResult = new StringBuffer();
    	if(userImagePath.equals("6")){
    		strResult.append("<TABLE border='0' width='100%' height='100%' cellSpacing='0' cellPadding='0'>"+"\r\n");
            //strResult.append("  <TR>"+"\r\n");
    	}else{
    		strResult.append("<TABLE border='0' width='100%' height='100%' cellSpacing='0' cellPadding='0'>"+"\r\n");
            strResult.append("  <TR>"+"\r\n");
            strResult.append("     <TD width='100%' height='100%' valign='center' align='center'>"+"\r\n");
            strResult.append("       <TABLE width='100%' height='100%'  cellSpacing='0' cellPadding='0' align='center' border='0'>"+"\r\n");//class='Window'
            strResult.append("         <TR>"+"\r\n");
            strResult.append("           <TD height='22' width='100%'>"+"\r\n");
            strResult.append("             <TABLE border='0' width='100%' height='100%' cellspacing='0' cellpadding='0'>"+"\r\n");
            strResult.append("               <TR>"+"\r\n");
            strResult.append("                 <TD align='left' background='"+SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/"+userImagePath+"/WindowTitle.gif' align=center>&nbsp;<img align='absmiddle' src='"+SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/"+userImagePath+"/"+titleImage+"'>&nbsp;<font class='titleFont'>"+strTitle+"</font></TD>"+"\r\n");
            strResult.append("               </TR>"+"\r\n");
            strResult.append("             </TABLE>"+"\r\n");
            strResult.append("           </TD>"+"\r\n");
            strResult.append("         </TR>"+"\r\n");
    	}
    	return strResult.toString();
	}

	@Override
	public String clickButton(String strName, String strScript, String strImage, String userImagePath) throws Exception {
		StringBuffer strResult = new StringBuffer();
        if(strScript.indexOf("javascript")<0&&strScript.indexOf("(")>-1&&strScript.indexOf("layer")<0){
		    strScript="javascript:"+strScript;
	    }else if(strScript.indexOf("javascript")<0&&strScript.indexOf("layer")>-1){
		    strScript="javascript:"+strScript;
	    }
	    strResult.append("<td><a class=\"easyui-linkbutton\" data-options=\"iconCls:'"+strImage+"'\" href=\"javascript:void(0)\" onclick=\""+strScript+"\" style=\"width:100px\">"+strName+"</a>\r\n");
	    
        return strResult.toString();
	}

	@Override
	public String clickButton1(String strName, String strScript, String strImage, String userImagePath) throws Exception {
        StringBuffer strResult = new StringBuffer();
        if(strScript.indexOf("javascript")<0&&strScript.indexOf("(")>-1&&strScript.indexOf("layer")<0){
		    strScript="javascript:"+strScript;
	    }else if(strScript.indexOf("javascript")<0&&strScript.indexOf("layer")>-1){
		    strScript="javascript:"+strScript;
	    }
	    strResult.append("<td><a class=\"easyui-linkbutton\" data-options=\"iconCls:'"+strImage+"'\" href=\"javascript:void(0)\" onclick=\""+strScript+"\" style=\"width:100px\">"+strName+"</a>\r\n");
	    
        return strResult.toString();
	}

	@Override
	public String showBodyStartHtml() throws Exception {
		StringBuffer strResult = new StringBuffer();
        strResult.append("<TR>\r\n");
        strResult.append("  <TD width='100%' height='100%' class='Window_Body' valign='top'>\r\n");
        return strResult.toString();
	}

	@Override
	public String showFootHtml(String strTitle, String userImagePath) throws Exception {
		StringBuffer strResult = new StringBuffer();
        strResult.append("      </TABLE>\r\n");
        strResult.append("    </TD>\r\n");
        strResult.append("  </TR>\r\n");
        strResult.append("</TABLE>\r\n");
        return strResult.toString();
	}

	@Override
	public String showFootHtml() throws Exception {
        StringBuffer strResult = new StringBuffer();
        strResult.append("  </TD>\r\n");
        strResult.append("  </TR>\r\n");
        strResult.append("      </TABLE>\r\n");
        strResult.append("    </TD>\r\n");
        strResult.append("  </TR>\r\n");
        strResult.append("</TABLE>\r\n");
        return strResult.toString();
	}

	@Override
	public String createPageMenu(int pageSize, String fileUrl, int rowCount, int page, String strAll) throws Exception {
        String PageMenu1 = "";
        StringBuffer PageMenu = new StringBuffer();
        PageMenu.append("&nbsp;");
        int pageCount = 0, i, sum1 = 0, sum2 = 0, sum3 = rowCount;
        int curRowCount = rowCount;
        while (((rowCount-pageSize)>0) || (rowCount>0)){
        	pageCount++;
        	rowCount = rowCount-pageSize;
        }
        if (page > pageCount) {
        	page = pageCount;
        }
        if (pageCount > 1) {
        	sum1 = page - 1;
        	if (sum1 < 1) {
        		sum1 = 1;
        	}
        	sum2 = page + 1;
        	if (sum2 > pageCount) {
        		sum2 = pageCount;
        	}
        	PageMenu.append("<a href='"+fileUrl+"page=1'>首页</a>");
        	PageMenu.append("&nbsp;&nbsp;<a href='"+fileUrl+"page="+sum1+"'>上一页</a>");
        	PageMenu.append("&nbsp;&nbsp;<a href='"+fileUrl+"page="+sum2+"'>下一页</a>");
        	PageMenu.append("&nbsp;&nbsp;<a href='"+fileUrl+"page="+pageCount+"'>末页</a>&nbsp;&nbsp;");
        	PageMenu.append("<select size='1' class='PageStyle'  Onchange='DoMenu(this.value)'>");
        	for (i = 1; i <= pageCount; i++) {
        		if (page == i) {
        			PageMenu.append("<option value='"+fileUrl+"page="+i+"' selected >第"+i+"页</option>");
        		} else{
        			PageMenu.append("<option value='"+fileUrl+"page="+i+"' >第"+i+"页</option>");
        		}
        	}
        	PageMenu.append("</select>");
        	PageMenu.append("&nbsp;&nbsp;页次："+page+"/"+pageCount+"&nbsp;&nbsp;共"+sum3+"条");
        } else {
        	PageMenu.append("&nbsp;&nbsp;共"+sum3+"条");
        }
        if (strAll.equals("yes")) {
        	PageMenu1 =PageMenu.toString();
        	PageMenu.delete(0,PageMenu.length());//清空
        	PageMenu.append("<a href='"+fileUrl+"all=no&page=1'>分页显示</a>&nbsp;&nbsp;"+PageMenu1);
        } else {
        	if (pageSize < curRowCount) {
        		PageMenu1 =PageMenu.toString();
        		PageMenu.delete(0,PageMenu.length());//清空
        		PageMenu.append("<a href='"+fileUrl+"all=yes&page=1'>单页显示</a>&nbsp;&nbsp;"+PageMenu1);
        	} else {
        		PageMenu1 =PageMenu.toString();
        		PageMenu.delete(0,PageMenu.length());//清空
        		PageMenu.append("<a href='"+fileUrl+"all=no&page=1'></a>&nbsp;&nbsp;"+PageMenu1);
        	}
        }
        return PageMenu.toString();
	}
}
