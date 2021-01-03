package com.yonglilian.service.impl;

import com.yonglilian.bean.CollDocConfigBean;
import com.yonglilian.bean.CollDocPrintBean;
import com.yonglilian.bean.CollTempBean;
import com.yonglilian.dao.CollConfigOperateFieldDao;
import com.yonglilian.dao.CollDocConfigDao;
import com.yonglilian.dao.CollDocPrintDao;
import com.yonglilian.domain.CollDocConfig;
import com.yonglilian.service.CollConfigOperateFieldService;
import com.yonglilian.service.CollDocConfigService;
import com.yonglilian.service.CollDocPrintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.model.dbmanage.BPIP_FIELD;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("collDocConfigService")
public class CollDocConfigServiceImpl implements CollDocConfigService {
	/** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(CollDocConfigServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型

	@Autowired
	private CollDocConfigDao collDocConfigDao;
	@Autowired
	private CollDocPrintDao collDocPrintDao;
	@Autowired
	private CollConfigOperateFieldDao collConfigOperateFieldDao;

	@Autowired
	private CollConfigOperateFieldService collConfigOperateFieldService;
	@Autowired
	private CollDocPrintService collDocPrintService;

	/**
	 * 构造方法
	 */
	public CollDocConfigServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}

	@Override
	public CollDocConfigBean queryObject(String id) throws Exception {
		CollDocConfigBean collDocConfigBean = null;
		CollDocConfig collDocConfig = collDocConfigDao.queryObject(id);
		if (collDocConfig != null) {
			collDocConfigBean = new CollDocConfigBean();
			BeanUtils.copyProperties(collDocConfig, collDocConfigBean);
		}
		return collDocConfigBean;
	}

	@Override
	public List<CollDocConfigBean> queryList(Map<String, Object> map) throws Exception {
		List<CollDocConfigBean> collDocConfigBeans = new ArrayList<>();
		List<CollDocConfig> list = collDocConfigDao.queryList(map);
		for (CollDocConfig collDocConfig : list) {
			CollDocConfigBean collDocConfigBean = new CollDocConfigBean();
			BeanUtils.copyProperties(collDocConfig, collDocConfigBean);
			collDocConfigBeans.add(collDocConfigBean);
		}
		return collDocConfigBeans;
	}

	@Override
	public int queryTotal(Map<String, Object> map) throws Exception {
		return collDocConfigDao.queryTotal(map);
	}

	@Override
	public void save(CollDocConfigBean collDocConfigBean) throws Exception {
		CollDocConfig collDocConfig = new CollDocConfig();
		BeanUtils.copyProperties(collDocConfigBean, collDocConfig);
		if (collDocConfig.getId() != null && collDocConfig.getId().trim().length() == 8) {
			collDocConfigDao.update(collDocConfig);
		} else {
			collDocConfig.setId(getMaxNo());
			collDocConfigDao.save(collDocConfig);
		}
	}

	@Override
	public void update(CollDocConfigBean collDocConfigBean) throws Exception {
		CollDocConfig collDocConfig = new CollDocConfig();
		BeanUtils.copyProperties(collDocConfigBean, collDocConfig);
		collDocConfigDao.update(collDocConfig);
		// 删除以前的配置字段
		collConfigOperateFieldDao.delete(collDocConfig.getId());
		// 批量插入新的数据
		collConfigOperateFieldService.saveList(collDocConfigBean.getFieldlist());
	}

	@Override
	public void delete(String id) throws Exception {
		collDocConfigDao.delete(id);
	}

	@Override
	public void deleteBatch(String[] ids) throws Exception {
		collDocConfigDao.deleteBatch(ids);
		// 删除配置、打印表单表
		collDocPrintDao.delete(ids[0]);
		collConfigOperateFieldDao.delete(ids[0]);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId = collDocConfigDao.getMaxNo();
		if (maxId == null || maxId.equals("null")) {
			maxId = "00000001";
		} else {
			maxId = "00000000" + (Integer.valueOf(maxId) + 1);
			maxId = maxId.substring(maxId.length() - 8, maxId.length());
		}
		return maxId;
	}

	@Override
	public void tempform(CollTempBean collTempBean) throws Exception {
		StringBuffer strRes = new StringBuffer();// 自定义表单
		StringBuffer strPRes = new StringBuffer();// 打印表单
		
		boolean isblob = false;// 显示的字段中是否有blob类型的字段。
		CollDocConfig collDocConfig = collDocConfigDao.queryObject(collTempBean.getId());
		String strfields = String.join("','", collTempBean.getFields());
		Map<String,Object> map = new HashMap<>();
		String[] wheres = new String[]{"TABLEID='"+collDocConfig.getMaintable()+"'","FIELDNAME IN ('"+strfields+"')"};
		map.put("fields", "FIELDNAME,CHINESENAME,ISKEY,FIELDTYPE");
		map.put("wheres", wheres);
		List<BPIP_FIELD> fields = collDocConfigDao.getTableFields(map);

		// ---------------------------------------
		if (collTempBean.getDoctype()==1) {// PC端模板
			//字段模板
			StringBuffer strResFieldS = new StringBuffer();//
			strResFieldS.append("<table width=\"900\" border=\"0\"  bgcolor=\"#DFDCD7\" cellspacing=\"1\" cellpadding=\"5\"  id=\"table1\">\r\n");
			int len = fields.size(),jnum = 0;
			for (int i = 0; i < len; i++) {
				if (i % 2 == 0 && i == 0) {
					strResFieldS.append("<TR>\r\n");
				}
				if (i % 2 == 0 && i != 0) {
					strResFieldS.append("</TR><TR>\r\n");
				}
				if (fields.get(i).getFIELDTYPE()!=null&&fields.get(i).getFIELDTYPE().equals("3")) {
					isblob = true;
					strResFieldS.append("<TD  bgcolor=\"#F2F2F2\" width=\"220\">" + fields.get(i).getCHINESENAME() + "：</TD>\r\n");
					strResFieldS.append("<TD  bgcolor=\"#FFFFFF\" width=\"430\"><img name=\"" + fields.get(i).getFIELDNAME() + "\" id=\""+ fields.get(i).getFIELDNAME()
							+ "\" title=\"照片显示\" width=\"94\" height=\"120\"><br><div id='show2'><input name=\"PHOTO\" type=\"file\" size=\"35\"></div></TD>\r\n");
				} else {
					strResFieldS.append("<TD  bgcolor=\"#F2F2F2\" width=\"220\">" + fields.get(i).getCHINESENAME() + "：</TD>\r\n");
					strResFieldS.append("<TD  bgcolor=\"#FFFFFF\" width=\"430\"><input type=\"text\" name=\"" + fields.get(i).getFIELDNAME()
							+ "\" size=\"25\"></TD>\r\n");
				}
				jnum = i;
			}
			if (jnum % 2 == 0) {
				strResFieldS.append("<TD  bgcolor=\"#F2F2F2\" width=\"220\"></TD>\r\n");
				strResFieldS.append("<TD  bgcolor=\"#FFFFFF\" width=\"430\"></TD>\r\n");
				strResFieldS.append("</TR>\r\n");
			}
			strResFieldS.append("</table>\r\n");
			
			//表头及表尾
			strRes.append("<html>\r\n");
			strRes.append("<head>\r\n");
			strRes.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n");
			strRes.append("<title>" + collDocConfig.getDocname() + "</title>\r\n");
			strRes.append("<link href=\"/static/ZrCollEngine/Res/doc.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
			strRes.append("</head>\r\n");
			strRes.append("<body>\r\n");
			if (isblob) {
				strRes.append("<form name=\"form1\" method=\"post\" enctype=\"multipart/form-data\" action=\"\">\r\n");
			} else {
				strRes.append("<form name=\"form1\" method=\"post\" action=\"\">\r\n");
			}
			strRes.append("<div id=\"cc\" class=\"easyui-layout\" style=\"width:100%;height:96%; top:5px;\">\r\n");
			strRes.append("<div data-options=\"region:'center',title:'" + collDocConfig.getDocname() + "',iconCls:'icon-ok'\">\r\n");
			strRes.append("<table border=\"0\" cellpadding=\"3\" width=\"96%\">\r\n");
			strRes.append("<tr><td valign=\"top\" align=\"center\">\r\n");
			//组装字段
			strRes.append(strResFieldS.toString());
			strRes.append("<p></p></td></tr>\r\n");
			strRes.append("</table>\r\n");
			strRes.append("</div>\r\n");
			strRes.append("</div>\r\n");
			strRes.append("</form>\r\n");
			strRes.append("</body>\r\n");
			strRes.append("</html>\r\n");
		}
		if (collTempBean.getDoctype()==2) {// 手机模板
			//字段模板
			StringBuffer strResFieldS = new StringBuffer();//
			strResFieldS.append(
					"<ul data-am-widget=\"gallery\" class=\"am-gallery am-gallery-default am-no-layout\" data-am-gallery=\"{ pureview: true }\" >\r\n");
			strResFieldS.append("<li>\r\n");
			int len = fields.size();
			for (int i = 0; i < len; i++) {
				if (fields.get(i).getFIELDTYPE()!=null&&fields.get(i).getFIELDTYPE().equals("3")) {
					isblob = true;
					strResFieldS.append("<div class=\"am-form-group am-form-file\" id='PHOTO_h'>\r\n");
					strResFieldS.append("<label for=\"doc-vld-input1\">" + fields.get(i).getCHINESENAME() + "</label>\r\n");
					strResFieldS.append("<img name=\"" + fields.get(i).getFIELDNAME() + "\" id=\"" + fields.get(i).getFIELDNAME()
							+ "\" title=\"照片显示\" width=\"94\" height=\"120\">\r\n");
					strResFieldS.append("</div>\r\n");
					strResFieldS.append("<div class=\"am-form-group am-form-file\" id='PHOTO_f'>\r\n");
					strResFieldS.append("<label for=\"doc-vld-input1\">操作</label>\r\n");
					strResFieldS.append("<button type=\"button\" class=\"am-btn am-btn-danger am-btn-sm\">\r\n");
					strResFieldS.append("<i class=\"am-icon-cloud-upload\"></i> 选择图片</button>\r\n");
					strResFieldS.append(
							"<input name=\"PHOTO\" type=\"file\" multiple placeholder=\"" + fields.get(i).getCHINESENAME() + "\">\r\n");
					strResFieldS.append("</div>\r\n");
				} else {
					strResFieldS.append("<div class=\"am-form-group\" id=\"" + fields.get(i).getFIELDNAME() + "_h\">\r\n");
					strResFieldS.append("<label for=\"doc-vld-input1\">" + fields.get(i).getCHINESENAME() + "</label>\r\n");
					strResFieldS.append("<input type=\"text\" name=\"" + fields.get(i).getFIELDNAME() + "\" id=\"" + fields.get(i).getFIELDNAME()
							+ "\" placeholder=\"" + fields.get(i).getCHINESENAME() + "\">\r\n");
					strResFieldS.append("</div>\r\n");
				}
			}
			strResFieldS.append("</li>\r\n");
			strResFieldS.append("</ul>\r\n");
			//头部或尾部页面
			strRes.append("<html class=\"no-js\">\r\n");
			strRes.append("<head>\r\n");
			strRes.append("<meta charset=\"utf-8\">\r\n");
			strRes.append("<title>" + collDocConfig.getDocname() + "</title>\r\n");
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
			strRes.append(
					"<link rel=\"apple-touch-icon-precomposed\" href=\"/static/assets/i/app-icon72x72@2x.png\">\r\n");
			strRes.append("<link rel=\"stylesheet\" href=\"/static/assets/font_icon/css/font-awesome.css\">\r\n");
			strRes.append("<link rel=\"stylesheet\" href=\"/static/assets/css/amazeui.min.css\">\r\n");
			strRes.append("<link rel=\"stylesheet\" href=\"/static/assets/css/app.css\">\r\n");
			strRes.append("<link rel=\"stylesheet\" href=\"/static/assets/css/amazeui.datetimepicker.css\"/>\r\n");
			strRes.append("<link rel=\"stylesheet\" href=\"/static/assets/css/from.css\">\r\n");
			strRes.append("<link rel=\"stylesheet\" href=\"/static/assets/css/amazeui.datetimepicker.css\"/>\r\n");

			strRes.append("<script type=\"text/javascript\" src=\"/static/assets/js/jquery.min.js\"></script>\r\n");
			strRes.append(
					"<script type=\"text/javascript\" src=\"/static/assets/js/amazeui.min_coll.js\"></script>\r\n");
			strRes.append(
					"<script type=\"text/javascript\" src=\"/static/assets/js/amazeui.datetimepicker.min.js\"></script>\r\n");
			strRes.append(
					"<script type=\"text/javascript\" src=\"/static/assets/js/locales/amazeui.datetimepicker.zh-CN.js\"></script>\r\n");
			strRes.append(
					"<script type=\"text/javascript\" src=\"/static/ZrPhoneEngine/js/QueryEngine.js\"></script>\r\n");
			strRes.append(
					"<script type=\"text/javascript\" src=\"/static/Zrsysmanage/script/Public.js\"></script>\r\n");
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
				strRes.append(
						"<form name=\"form1\" method=\"post\" enctype=\"multipart/form-data\" action=\"\" class=\"am-form\" data-am-validator>\r\n");
			} else {
				strRes.append(
						"<form name=\"form1\" method=\"post\" action=\"\" class=\"am-form\" data-am-validator>\r\n");
			}
			//组装
			strRes.append(strResFieldS.toString());
			strRes.append("</form>\r\n");
			strRes.append("</div>\r\n");
			strRes.append("<!-- main  end -->\r\n");
			strRes.append("</body>\r\n");
			strRes.append("</html>\r\n");
		}
		
		// --------------------打印表单--------------------//
		//字段模板
		StringBuffer strResFieldS = new StringBuffer();//
		strResFieldS.append("<table width=\"760\" border=\"1\"  bgcolor=\"#C0C0C0\" cellspacing=\"0\" id=\"table1\" bordercolorlight=\"#000000\" cellpadding=\"0\">\r\n");
		int len = fields.size(),jnum = 0;
		for (int i = 0; i < len; i++) {
			if (i % 2 == 0 && i == 0) {
				strResFieldS.append("<TR>\r\n");
			}
			if (i % 2 == 0 && i != 0) {
				strResFieldS.append("</TR><TR>\r\n");
			}
			if (fields.get(i).getFIELDTYPE()!=null&&fields.get(i).getFIELDTYPE().equals("3")) {
				isblob = true;
				strResFieldS.append("<TD bgcolor=\"#FFFFFF\" width=\"110\">" + fields.get(i).getCHINESENAME() + "：</TD>\r\n");
				strResFieldS.append("<TD bgcolor=\"#FFFFFF\" width=\"270\"><img name=\"" + fields.get(i).getFIELDNAME() + "\" id=\""
						+ fields.get(i).getFIELDNAME() + "\" title=\"照片显示\" width=\"94\" height=\"120\"></TD>\r\n");
			} else {
				strResFieldS.append("<TD bgcolor=\"#FFFFFF\" width=\"110\">" + fields.get(i).getCHINESENAME() + "：</TD>\r\n");
				strResFieldS.append("<TD bgcolor=\"#FFFFFF\" width=\"270\"><input type=\"text\" name=\"" + fields.get(i).getFIELDNAME()
						+ "\" size=\"16\"></TD>\r\n");
			}
			jnum = i;
		}
		if (jnum % 2 == 0) {
			strResFieldS.append("<TD  bgcolor=\"#FFFFFF\" width=\"110\"></TD>\r\n");
			strResFieldS.append("<TD  bgcolor=\"#FFFFFF\" width=\"270\"></TD>\r\n");
			strResFieldS.append("</TR>\r\n");
		}
		strResFieldS.append("</table>\r\n");
		//表头部及尾部
		strPRes.append("<html>\r\n");
		strPRes.append("<head>\r\n");
		strPRes.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n");
		strPRes.append("<title>" + collDocConfig.getDocname() + "</title>\r\n");
		strPRes.append("</head>\r\n");
		strPRes.append("<body topmargin=\"2\" leftmargin=\"12\" rightmargin=\"2\" bottommargin=\"2\">\r\n");
		strPRes.append("<form name=\"form1\" method=\"post\" action=\"\">\r\n");
		strPRes.append("<br><br>\r\n");
		strPRes.append("<p align=\"center\"><b><font size=\"5\">" + collDocConfig.getDocname() + "</font></b> </p>\r\n");
		//组装
		strPRes.append(strResFieldS.toString());
		strPRes.append("</table>\r\n");
		strPRes.append("</body>\r\n");
		strPRes.append("</html>\r\n");
		// ----------------------------打印表单----------------------------//

		// printTempletPath
		// --------------生成模板文件-------------//
		boolean bFile1;
		File tempFile1 = new File(SysPreperty.getProperty().ShowTempletPath + "/" + collDocConfig.getId() + ".htm");
		tempFile1.delete();
		try {
			bFile1 = tempFile1.createNewFile();
			if (bFile1) {
				Writer fw1 = new OutputStreamWriter(new FileOutputStream(tempFile1), "UTF-8");
//				FileWriter fw1 = new FileWriter(SysPreperty.getProperty().ShowTempletPath + "/" + collDocConfig.getId() + ".htm");
				fw1.write(strRes.toString(), 0, strRes.toString().length());
				fw1.flush();
				fw1.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// --------------生成打印模板文件-------------//
		boolean bFile2;
		File tempFile2 = new File(SysPreperty.getProperty().PrintTempletPath + "/" + collDocConfig.getId() + "_01.htm");
		tempFile2.delete();
		try {
			bFile2 = tempFile2.createNewFile();
			if (bFile2) {
				Writer fw2 = new OutputStreamWriter(new FileOutputStream(tempFile1), "UTF-8");
//				FileWriter fw2 = new FileWriter(SysPreperty.getProperty().PrintTempletPath + "/" + collDocConfig.getId() + "_01.htm");
				fw2.write(strPRes.toString(), 0, strPRes.toString().length());
				fw2.flush();
				fw2.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		//生成表单完成后，修改表单
		CollDocConfig model = new CollDocConfig();
		model.setId(collDocConfig.getId());
		model.setTemplaet(collDocConfig.getId() + ".htm");
		collDocConfigDao.update(model);
		//插入打印表单数据
		CollDocPrintBean print = new CollDocPrintBean();
		print.setDocid(collDocConfig.getId());
		print.setTemplaet(collDocConfig.getId() + "_01.htm");
		print.setPage(1);
		collDocPrintService.save(print);
		
		//清空资源
		model = null;
		print = null;
		collDocConfig = null;
	}

}
