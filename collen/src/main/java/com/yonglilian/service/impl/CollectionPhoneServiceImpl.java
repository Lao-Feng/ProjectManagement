package com.yonglilian.service.impl;

import com.yonglilian.collectionengine.*;
import com.yonglilian.collectionengine.html.*;
import com.yonglilian.service.CollectionPhoneService;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.*;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.*;
import zr.zrpower.common.util.*;
import zr.zrpower.dao.UserMapper;
import zr.zrpower.model.SessionUser;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CollectionPhoneServiceImpl implements CollectionPhoneService {
	/** The CollectionPhoneServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionPhoneServiceImpl.class);
	private DBEngine dbEngine;// 数据库引擎
	private static int clients = 0;
	static String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	private Timer timerListener; // 队列侦听器
	
	static Map<String, Object> CodeHashtable = new HashMap<String, Object>();// 根据表名得到列表
	static Map<String, Object> getFlowProprty = new HashMap<String, Object>();// 填充流程属性
	static Map<String, Object> getFlowProprty1 = new HashMap<String, Object>();// 填充流程属性
	static Map<String, Object> getTitleFieldHashtable = new HashMap<String, Object>();// 填充流程属性
	
	static Map<String, Object> getCollectionInfo = new HashMap<String, Object>();
	static Map<String, Object> MoreSelectHashtable = new HashMap<String, Object>();
	static Map<String, Object> DocIDHashtable = new HashMap<String, Object>();

	public CollectionPhoneServiceImpl() {
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

	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;

	@Override
	public String collectionData(Request request, SessionUser userInfo) throws Exception {
		LOGGER.info("调用采集开始...");
		String IfPho = "0";// 判断客户端为手机或者电脑 1--表示手机
		String mID = null; // 配置编号（电脑）
		String mIDPho = null; // 配置编号(手机)
		String mRID = null; // 关联流转ID
		String mPKID = null; // 主键编号
		String mFKID = null; // 外键编号
		String PAID1 = null; // 父编号1
		String PAID2 = null; // 父编号2
		String mAID = null; // 流程活动编号
		String mAction = null; // 执行动作
		String mMode = null;
		String templat = ""; // 模版文件
		String ISREVALUE = "1";
		boolean bReadOnly = false; // 调用是否只读模式 false可读写模式，true只读模式
		boolean bAllEdit = false; // 是否所有字段都可编辑 false否，true是
		
		boolean isBase = true; // 是否是基础数据
		StringBuffer strSQL = new StringBuffer();
		StringBuffer addBuf = new StringBuffer();
		StringBuffer addJs = new StringBuffer();
		try {
			LOGGER.info("调用采集初始化...");// 初始化信息
			mRID = request.getStringItem("RID");
			mPKID = request.getStringItem("COLL_PKID");
			mFKID = request.getStringItem("COLL_FKID");
			PAID1 = request.getStringItem("PAID1");
			PAID2 = request.getStringItem("PAID2");
			mAID = request.getStringItem("COLL_AID");
			mMode = request.getStringItem("COLL_MODE");
			mAction = request.getStringItem("COLL_Action");
			ISREVALUE = request.getStringItem("ISREVALUE");
			
			if (request.getStringItem("COLL_READ") != null && request.getStringItem("COLL_READ").equals("1")) {
				bReadOnly = true;
				LOGGER.info("只读调用");
			} else {
				LOGGER.info("读写调用");
			}
			if (request.getStringItem("COLL_READ") != null && request.getStringItem("COLL_READ").equals("2")) {
				bAllEdit = true;
				LOGGER.info("所有字段都可以编辑");
			}
			if (mAction == null) {
				mAction = "read";
			}
			if (mAID == null) {
				return ErrorHtml("非法调用错误！");
			} else {
				if (mAID.length() == 10) {// 表示流程活动
					strSQL.append("Select b.DocID,b.DocIDPHO "
							+ "From FLOW_CONFIG_ACTIVITY a Left Join FLOW_CONFIG_PROCESS b on b.ID=a.FID "
							+ "Where a.ID='" + mAID + "'");
					List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
					if (retlist != null && retlist.size() > 0) {
						Map<String, Object> retmap = retlist.get(0);
						if (retmap != null && retmap.get("DocID") != null) {
							mID = retmap.get("DocID").toString();
						}
						if (retmap != null && retmap.get("DocIDPHO") != null) {
							mIDPho = retmap.get("DocIDPHO").toString();
						}
					}
				} else{// 自定义表单
					mID = mAID;
					mIDPho = mAID;
				}
			}
			if (mID == null) {
				return ErrorHtml("自定义表单引擎发生异常：<br>流程配置与采集配置错误，未找到相关配置信息。");
			}
			LOGGER.info("调用采集，读取采集配置信息...");
			IfPho = request.getStringItem("IfPho");
			if ("1".equals(IfPho)) {
				mID = mIDPho;
			}
			CollectionInfo cInfo = getCollectionInfo(mID);
			if (cInfo == null) {
				return ErrorHtml("自定义表单引擎发生错误，采集配置信息未找到！");
			}
			else {
				if (cInfo.getDOCTYPE() != null && cInfo.getDOCTYPE().equals("1")) {
					isBase = false;
				}
			}
			// 分析操作类型
			String strID = "-1";
			if (mAction != null && !mAction.equals("read")) {
				if (mAction.equals("add") && mPKID != null && !mPKID.equals("")) {
					mAction = "edit";
				}
				strID = updateTable(mAction, cInfo, request, userInfo);
				if (!strID.equalsIgnoreCase("-1")) {
					mPKID = strID;
				} else {
					return ErrorHtml("自定义表单引擎发生错误，保存数据时系统发生异常，请与管理员联系！");
				}
			}
			addBuf.append(SysPreperty.getProperty().ShowTempletPath + "/").append(cInfo.getTEMPLAET());
			templat = addBuf.toString();
			addBuf.delete(0, addBuf.length()); // 清空
			// 读取模版
			LOGGER.info("调用采集，读取数据库信息...");
			// 装载主表
			DBRow dbMain = this.getTablePropery(cInfo.getMAINTABLE());
			if (mPKID != null && !mPKID.equals("")) {
				this.fullData(dbMain, mPKID, isBase);
			}
			ItemList itemList = new ItemList(bReadOnly, bAllEdit);
			itemList.fullFromDbRow(dbMain, null);
			
			// 读取流程配置信息
			FlowList flowList = new FlowList();
			this.getFlowProprty(flowList, mAID, cInfo.getMAINTABLE(), null, mPKID, mFKID, PAID1, PAID2, userInfo, mRID);
			
			itemList.fullFromFlowField(flowList);
			checkDefault(itemList);
			
			LOGGER.info("调用采集，读取数据库信息...");
			Parser parser = new Parser();
			Html html = null;
			try {
				parser.setURL(templat);
				parser.setEncoding("UTF-8");
				//parser.setEncoding("GBK");
				html = (Html) (parser.elements().nextNode());
				LOGGER.info("解析装载模版成功");
			} catch (ParserException ex) {
				LOGGER.error("调用采集", ex.toString());
				return ErrorHtml("自定义表单引擎发生错误，读取采集模版失败！<br>详细错误信息：<br>" + ex.toString());
			}
			// 解析模版
			if (html != null) {
				LOGGER.info("更替表单域");
				NodeList nlForm = html.searchFor(new FormTag().getClass(), true);
				FormTag ftForm = (FormTag) nlForm.elementAt(0);
				ftForm.setAttribute("name", "frmColl");
				ftForm.setAttribute("id", "frmColl");
				ftForm.setAttribute("method", "post");
				ftForm.setAttribute("action", "/collect/collenginephone");
				LOGGER.info("更替表单域成功");
				Vector<Object> vNotVisible = new Vector<Object>();
				Vector<Object> vUnit = new Vector<Object>();
				Hashtable<String, Object> vDate = new Hashtable<String, Object>();
				// 控件解析
				LOGGER.info("控件解析");
				ControlSet ctrlSet = new ControlSet();
				HiddenControlSet hiddSet = new HiddenControlSet();
				String[] saItem = itemList.getItemNameList();
				
				NodeList lt = html.searchFor(new InputTag().getClass(), true);
				String type = "";
				String name = "";
				for (int index = 0; index < lt.size(); index++) {
					type = ((InputTag) (lt.elementAt(index))).getAttribute("type");
					name = ((InputTag) (lt.elementAt(index))).getAttribute("name");
					if (type == null || type.equals("")) {
						type = "TEXT";
					}
					ItemField item;
					// 单选框
					if (type.equalsIgnoreCase("radio")) {
						for (int i = 0; i < saItem.length; i++) {
							if (saItem[i].equalsIgnoreCase(name)) {
								InputTag inputTag = (InputTag) (lt.elementAt(index));
								item = itemList.getItem(saItem[i]);
								if (!item.isWrite()) {
									inputTag.setAttribute("disabled", "true");
								}
								if (item.isWrite()) {
									inputTag.setAttribute("", "data-am-ucheck");
									// inputTag.s
								}
								if (inputTag.getAttribute("value").equals(item.getValue())) {
									inputTag.setAttribute("checked", "true");
								}
								break;
							}
						}
					}
					// --------------------------------------
					if (type.toUpperCase().equalsIgnoreCase("TEXT")) {
						for (int i = 0; i < saItem.length; i++) {
							if (saItem[i].equalsIgnoreCase(name)) {
								item = itemList.getItem(saItem[i]);
								// 数字型元素
								if (item.getType().getValue() == DBType.LONG.getValue()) {
									NumberControl_phone tc = new NumberControl_phone((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									ctrlSet.addControl(tc);
									// 文本型
								}
								else if (item.getType().getValue() == DBType.STRING.getValue()) {
									// 是字典表
									if (item.isCode()) {
										// Checkbox型字典元素
										if (item.getCodeInput() == 1) {
											RedioControl_phone rc = new RedioControl_phone(
													(InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
											rc.setItemField(item);
											ctrlSet.addControl(rc);
										}
										// 下拉型字典元素
										else if (item.getCodeInput() == 2) {
											SelectControl_phone dc = new SelectControl_phone(
													(InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
											dc.setItemField(item);
											ctrlSet.addControl(dc);
										}
										// 字典控件型字典元素--手机端用下拉型字典元素
										else if (item.getCodeInput() == 3) {
											SelectControl_phone dc = new SelectControl_phone(
													(InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
											dc.setItemField(item);
											ctrlSet.addControl(dc);
										}
										// 多选控件
										else {
											checkboxControl_phone rc = new checkboxControl_phone(
													(InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
											rc.setItemField(item);
											ctrlSet.addControl(rc);
										}
									}
									// 用户列表元素
									else if (item.isUser()) {
										SelectControl_phone sc = null;
										if (item.getValue() != null) {
											sc = new SelectControl_phone((InputTag) (lt.elementAt(index)),
													this.getUserArrary(getUserUnitID(item.getValue())));
										} else {
											sc = new SelectControl_phone((InputTag) (lt.elementAt(index)),
													getUserArrary(userInfo.getUnitID()));
										}
										item.setCodeValue(getUserName(item.getValue()));
										sc.setItemField(item);
										ctrlSet.addControl(sc);
									}
									// 单位列表元素
									else if (item.isUnit()) {
										SelectControl_phone sc = null;
										if (!item.isWrite()) {
											try {
												sc = new SelectControl_phone((InputTag) (lt.elementAt(index)),
														getDictList("BPIP_UNIT"));
											} catch (Exception ex2) {
												sc = new SelectControl_phone((InputTag) (lt.elementAt(index)),
														getDictList("BPIP_UNIT"));
												item.setDefaultValue(userInfo.getUnitID());
											}
										} else {
											sc = new SelectControl_phone((InputTag) (lt.elementAt(index)),
													getDictList("BPIP_UNIT"));
										}
										// if (item.getCodeValue()==null && item.getValue()!=null)
										// {
										item.setCodeValue(getUnitName(item.getValue()));
										// }
										sc.setItemField(item);
										ctrlSet.addControl(sc);
										vUnit.add(item);
									}
									// 普通文本型元素
									else {
										TextControl_phone tc = new TextControl_phone((InputTag) (lt.elementAt(index)));
										tc.setItemField(item);
										ctrlSet.addControl(tc);
									}
								}
								// 浮点型元素
								else if (item.getType().getValue() == DBType.FLOAT.getValue()) {
									FloatControl_phone tc = new FloatControl_phone((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									ctrlSet.addControl(tc);
								}
								// 日期型元素
								else if (item.getType().getValue() == DBType.DATE.getValue()) {
									DateControl_phone tc = new DateControl_phone((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									String tmpOnchange = tc.getOnchange();
									if (tmpOnchange != null && tmpOnchange.length() > 0) {
										vDate.put(name, tmpOnchange);
									}
									ctrlSet.addControl(tc);
								}
								// 日期时间型元素
								else if (item.getType().getValue() == DBType.DATETIME.getValue()) {
									DateTimeControl_phone tc = new DateTimeControl_phone(
											(InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									String tmpOnchange = tc.getOnchange();
									if (tmpOnchange != null && tmpOnchange.length() > 0) {
										vDate.put(name, tmpOnchange);
									}
									ctrlSet.addControl(tc);
								}
								if (item.isWrite() == false && item.isShow() == false) {
									if (item.getValue() != null) {
										hiddSet.addNewHidden(item.getName(), item.getValue());
									} else if (item.getDefaultValue() != null) {
										hiddSet.addNewHidden(item.getName(), item.getDefaultValue());
									}
									// -------------------------------------------------------
								}
							}
						}
					} else if (type.toUpperCase().equalsIgnoreCase("FILE")) {
						
					} else if (type.toUpperCase().equalsIgnoreCase("HIDDEN")) {
						boolean inItem = false;
						for (int i = 0; i < saItem.length; i++) {
							if (saItem[i].equalsIgnoreCase(name)) {
								inItem = true;
								hiddSet.addPageHidden((InputTag) (lt.elementAt(index)), itemList.getItem(saItem[i]));
							}
						}
						if (!inItem) {
							hiddSet.addPageHidden((InputTag) (lt.elementAt(index)), null);
						}
					}
					item = null;
				}
				// Textarea 标签
				NodeList ta = html.searchFor(new TextareaTag().getClass(), true);
				for (int index = 0; index < ta.size(); index++) {
					name = ((TextareaTag) ta.elementAt(index)).getAttribute("name");
					for (int i = 0; i < saItem.length; i++) {
						if (saItem[i].equalsIgnoreCase(name)) {
							TextAreaControl_phone tc = new TextAreaControl_phone((TextareaTag) (ta.elementAt(index)));
							tc.setItemField(itemList.getItem(saItem[i]));
							ctrlSet.addControl(tc);
							if (itemList.getItem(saItem[i]).isWrite() == false
									&& itemList.getItem(saItem[i]).isShow() == false) {
								hiddSet.addNewHidden(itemList.getItem(saItem[i]).getName(),
										itemList.getItem(saItem[i]).getValue());
							}
						}
					}
				}
				LOGGER.info("图片标记分析");
				NodeList nlImage = html.searchFor(new ImageTag().getClass(), true);
				for (int index = 0; index < nlImage.size(); index++) {
					name = ((ImageTag) nlImage.elementAt(index)).getAttribute("id");
					for (int i = 0; i < saItem.length; i++) {
						if (saItem[i].equalsIgnoreCase(name)) {
							ImageTag img = ((ImageTag) nlImage.elementAt(index));
							addBuf.delete(0, addBuf.length()); // 清空
							addBuf.append("/photoengine?coll_id=" + mID + "&coll_pkid=" + mPKID + "&coll_img=" + name);
							img.setImageURL(addBuf.toString());
						}
					}
				}
				addJavascriptSrc("/static/ZrCollEngine/Res/coll.js", html);
				int datCtrlCount = ctrlSet.getDateControlCount() + ctrlSet.getDateTimeControlCount();
				if (datCtrlCount > 0) {
					addJavascriptSrc("/static/ZrCollEngine/DatePicker/WdatePicker.js", html);
				}
				JavaScriptTag jst = new JavaScriptTag();
				addJs.append("$('#frmColl').validator({\r\n");
				addJs.append("  onValid: function(validity) {\r\n");
				addJs.append("    alert(); $(validity.field).closest('.am-form-group').find('.am-alert').hide();\r\n");
				addJs.append("  },\r\n");
				addJs.append("  onInValid: function(validity) {\r\n");
				addJs.append("  var $field = $(validity.field);\r\n");
				addJs.append("  var $group = $field.closest('.am-form-group');\r\n");
				addJs.append("  var $alert = $group.find('.am-alert');\r\n");
				addJs.append("  // 使用自定义的提示信息 或 插件内置的提示信息\r\n");
				addJs.append("  var msg = $field.data('validationMessage') || this.getValidationMessage(validity);\r\n");
				addJs.append("  if (!$alert.length) {\r\n");
				addJs.append("    $alert = $('<div class=\"am-alert am-alert-danger\"></div>').hide().\r\n");
				addJs.append("      appendTo($group);\r\n");
				addJs.append("  }\r\n");
				addJs.append("   $alert.html(msg).show();\r\n");
				addJs.append("  }\r\n");
				addJs.append("});\r\n");
				jst.addFunction(ctrlSet.getValidateJS(addJs.toString()));
				
				if (datCtrlCount > 0) {
					jst.addFunction(getDateChangeJs(vDate));
				}
				jst.addToHead(html);
				// ----------
				if (IfPho != null && !IfPho.equals("")) {
					hiddSet.addNewHidden("IfPho", IfPho);
				}
				// -----------
				if (mMode != null && !mMode.equals("")) {
					hiddSet.addNewHidden("COLL_MODE", mMode);
				}
				if (mPKID != null && !mPKID.equals("")) {
					hiddSet.addNewHidden("COLL_PKID", mPKID);
				}
				else {
					hiddSet.addNewHidden("COLL_PKID", "");
				}
				if (mFKID != null && !mFKID.equals("")) {
					hiddSet.addNewHidden("COLL_FKID", mFKID);
				}
				if (mAID != null && !mAID.equals("")) {
					hiddSet.addNewHidden("COLL_AID", mAID);
				}
				if (mAction != null && !mAction.equals("")) {
					if (mAction.equals("read")) {
						mAction = "add";
					} else if (mAction.equals("add")) {
						mAction = "edit";
					}
					hiddSet.addNewHidden("COLL_Action", mAction);
				}
				hiddSet.addNewHidden("ISREVALUE", "1");
				hiddSet.toForm(ftForm);
				
				NodeList nlTitle = html.searchFor(new TitleTag().getClass(), true);
				TitleTag tt = (TitleTag) nlTitle.elementAt(0);
				NodeList nTitle = new NodeList();
				nTitle.add(new TextNode(cInfo.getDOCNAME()));
				tt.setChildren(nTitle);
				NodeList ltt = html.searchFor(new BodyTag().getClass(), true);
				BodyTag n = (BodyTag) ltt.elementAt(0);
				n.setAttribute("class", "ContentBody");
				
				JavaScriptTag jBody = new JavaScriptTag();
				// 如果有折叠区域
				if (vNotVisible.size() > 0) {
					for (int k = 0; k < vNotVisible.size(); k++) {
						addBuf.delete(0, addBuf.length()); // 清空
						addBuf.append(" VisiableFalse('" + vNotVisible.get(k) + "');");
						jBody.addFunction(addBuf.toString());
					}
				}
				// 如果保存成功调用流程
				if (strID != null && !strID.equalsIgnoreCase("-1")) {
					String strTitle = null;
					if (dbMain.Column(getTitleField(cInfo.getMAINTABLE())) != null) {
						strTitle = dbMain.Column(getTitleField(cInfo.getMAINTABLE())).getString();
					}
					if (strTitle == null) {
						strTitle = "";
					}
					if (ISREVALUE.equals("1")) {
						addBuf.delete(0, addBuf.length()); // 清空
						addBuf.append(" parent.save_return(\"" + strID + "\",\"")
								.append(mFKID + "\",\"" + strTitle + "\");");
						jBody.addFunction(addBuf.toString());
					}
				}
				// 设置弹出窗口div
				NodeList formDiv = html.searchFor(new BodyTag().getClass(), true);
				BodyTag div = (BodyTag) formDiv.elementAt(0);
				BodyTag tg = new BodyTag();
				tg.setText("<div id=\"win\">");
				LabelTag endDiv = new LabelTag();
				endDiv.setText("</div>");
				tg.setEndTag(endDiv);
				div.getChildren().add(tg);
				
				// jBody.ad
				jBody.addToBoay(html);
			}
			String sReturn = html.toHtml(true);
			html = null;
			parser = null;
			
			return sReturn;
		} catch (Exception ex1) {
			return "自定义表单引擎发生异常：" + ex1.toString();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getDateChangeJs(Hashtable htDate) throws Exception {
		StringBuffer sb = new StringBuffer();
		Enumeration names;
		int i, j;
		i = htDate.size();
		String[] result = new String[i];
		names = htDate.keys();
		j = 0;
		while (names.hasMoreElements()) {
			result[j] = (String) names.nextElement();
			j++;
		}
		sb.append("function DoChange(ObjName){\n");
		if (result.length > 0) {
			for (int k = 0; k < result.length; k++) {
				sb.append("  if(ObjName=='" + result[k] + "'){\n");
				sb.append("       " + htDate.get(result[k]) + ";\n");
				sb.append("  }\n");
			}
		}
		sb.append("return;\n");
		sb.append("}\n");
		return sb.toString();
	}

	@Override
	public int getPrintTempletCount(String AID) throws Exception {
		int result = 1;
		String docID = "";
		if (AID.length() == 10) {// 表示流程活动
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("Select b.DocID,b.DocIDPHO "
					+ "From FLOW_CONFIG_ACTIVITY a Left Join FLOW_CONFIG_PROCESS b on b.ID=a.FID "
					+ "Where a.ID='" + AID + "'");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
			if (retlist != null && retlist.size() > 0) {
				Map<String, Object> retmap = retlist.get(0);
				if (retmap != null && retmap.get("DocID") != null) {
					docID = retmap.get("DocID").toString();
				}
			}
		} else {// 自定义表单
			docID = AID;
		}
		CollectionInfo cInfo = getCollectionInfo(docID);
		if (cInfo != null) {
			result = cInfo.getPrintTempletCount();
		}
		cInfo = null;
		return result;
	}

	@Override
	public byte[] getCollImage(String id, String pkid, String strImg) throws Exception {
		byte[] img = null;
		StringBuffer strSQL = new StringBuffer();
		if (id != null && !id.equals("") && pkid != null && !pkid.equals("") 
				&& strImg != null && !strImg.equals("") && strImg.length() > 0) {
			CollectionInfo cInfo = getCollectionInfo(id);
			if (cInfo != null) {
				strSQL.append("Select a.PRIMARYKEY From BPIP_TABLE a Where a.TABLENAME='" + cInfo.getMAINTABLE() + "'");
				DBSet dsTable = dbEngine.QuerySQL(strSQL.toString(), "form4", cInfo.getMAINTABLE());
				
				if (dsTable != null && dsTable.RowCount() > 0) {
					String PrimaryKey = dsTable.Row(0).Column("PRIMARYKEY").getString();
					strSQL.delete(0, strSQL.length()); // 清空
					strSQL.append("Select " + strImg + " From " + cInfo.getMAINTABLE() 
							   + " Where " + PrimaryKey + "='" + pkid + "'");
					DBSet dsImg = dbEngine.QuerySQL(strSQL.toString());
					if (dsImg != null && dsImg.RowCount() > 0) {
						img = dsImg.Row(0).Column(strImg).getBlob();
						dsImg = null;
					}
					dsTable = null;
				}
			}
			cInfo = null;
		}
		return img;
	}

	@Override
	public byte[] getImage(String TableName, String PrimaryKey, String PKID, String ImgField) throws Exception {
		byte[] img = null;
		if (ImgField == null) {
			ImgField = "";
		}
		if (ImgField.length() > 0) {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("Select "+ImgField+" From "+TableName+" Where "+PrimaryKey+"='"+PKID+"'");
			DBSet dsImg = dbEngine.QuerySQL(strSQL.toString());
			if (dsImg != null && dsImg.RowCount() > 0) {
				img = dsImg.Row(0).Column(ImgField).getBlob();
				dsImg = null;
			}
		}
		return img;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getUserList(String UnitID) throws Exception {
		List result = new ArrayList();
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("Select UserID,Name From BPIP_USER Where USERSTATE='0' and UnitID='" + UnitID + "'");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			String[] saTmp = new String[2];
			saTmp[0] = "0";
			saTmp[1] = "";
			result.add(saTmp);
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				String[] saTmp1 = new String[2];
				saTmp1[0] = retmap.get("UserID").toString();
				saTmp1[1] = retmap.get("Name").toString();
				result.add(saTmp1);
			}
		}
		return result;
	}

	@Override
	public String[][] getUserArrary(String UnitID) throws Exception {
		String[][] saUser = (String[][]) CodeHashtable.get("BPIP_USER" + UnitID);
		if (saUser == null) {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("Select UserID,Name From BPIP_USER Where  USERSTATE='0' and UnitID='" + UnitID + "'");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length > 0) {
				saUser = new String[length][2];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					saUser[i][0] = retmap.get("UserID").toString();
					saUser[i][1] = retmap.get("Name").toString();
				}
			}
			if (saUser != null) {
				CodeHashtable.put("BPIP_USER" + UnitID, saUser);
			}
		}
		return saUser;
	}

	@Override
	public String updateTable(String UpdateType, CollectionInfo cInfo, 
			Request request, SessionUser userinfo) throws Exception {
		LOGGER.info("UpdateTable开始....");
		String ID = "-1";
		if (UpdateType == null) {
			LOGGER.error("UpdateType空");
			return ID;
		}
		if (cInfo == null) {
			LOGGER.error("cInfo空");
			return ID;
		}
		if (request == null) {
			LOGGER.error("request空");
			return ID;
		}
		if (userinfo == null) {
			LOGGER.error("userinfo空");
			return ID;
		}
		if (UpdateType != null & cInfo != null & request != null && userinfo != null) {
			String mFKID = request.getStringItem("COLL_FKID");
			// 获取数据表属性
			DBRow dbrow = getTablePropery(cInfo.getMAINTABLE());
			if (dbrow != null) {
				DBRow[] drList = new DBRow[1];
				String[] saKey = dbrow.getPrimaryKeyName();
				if (saKey != null && saKey.length > 0 && saKey[0] != null && saKey[0].equals(cInfo.getOTHERFIELD())) {
					
				} else {
					dbrow.Column(cInfo.getOTHERFIELD()).setValue(mFKID);
				}
				// --------从上面移下的行------------------------
				fullDataFromRequest(request, dbrow, null);
				// --------------------------------------------
				String sID = initDbRow(dbrow, UpdateType, userinfo, mFKID);
				drList[0] = dbrow;
				if (dbEngine.UpdateAll(drList)) {
					ID = sID;
				} else {
					LOGGER.error("UpdateTable", "执行更新失败！");
				}
				drList = null;
			} else {
				LOGGER.error("UpdateTable", "没有找到表名为【" + cInfo.getMAINTABLE() + "】的数据表，请检查文档配置");
			}
		} else {
			LOGGER.error("UpdateTable", "传入的数据行为空");
		}
		LOGGER.info("保存完成ID：" + ID);
		LOGGER.info("UpdateTable结束.");
		
		return ID;
	}

	@Override
	public String getCollFkidByRid(String Rid) throws Exception {
		String docTableName = "", PrimaryKey = "", PkValue = "";
		String rtnValue = null;
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("Select * from FLOW_RUNTIME_PROCESS Where ID='" + Rid + "'");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sqlStr.toString());
		try {
			if (retlist != null && retlist.size() > 0) {
				Map<String, Object> retmap = retlist.get(0);
				if (retmap != null) {
					docTableName = retmap.get("FORMTABLE") != null ? retmap.get("FORMTABLE").toString() : "";
					PkValue = retmap.get("FORMID") != null ? retmap.get("FORMID").toString() : "";
				}
				PrimaryKey = getPrimaryFieldName(docTableName);
				sqlStr.delete(0, sqlStr.length()); // 清空
				sqlStr.append("Select * From " + docTableName + " Where " + PrimaryKey + "='" + PkValue + "'");
				List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(sqlStr.toString());
				if (retlist1 != null && retlist1.size() > 0) {
					Map<String, Object> retmap1 = retlist1.get(0);
					if (retmap1 != null && retmap1.get("COLL_FKID") != null) {
						rtnValue = retmap1.get("COLL_FKID").toString();
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("出现异常:" + ex.getMessage());
		}
		return rtnValue;
	}

	@Override
	public boolean saveCssFont(String FlowRunId, String CssFont) throws Exception {
		boolean result = false;
		String FORMTABLE = "";
		String FORMID = "";
		String PRIMARYKEY = "";
		StringBuffer strSQL = new StringBuffer();
		StringBuffer strRetrun = new StringBuffer();
		strRetrun.append("");
		strSQL.append("select FORMTABLE,FORMID from FLOW_RUNTIME_PROCESS where ID='" + FlowRunId + "'");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
		if (retlist != null && retlist.size() > 0) {
			Map<String, Object> retmap = retlist.get(0);
			if (retmap != null) {
				FORMTABLE = retmap.get("FORMTABLE") != null ? retmap.get("FORMTABLE").toString() : "";
				FORMID = retmap.get("FORMID") != null ? retmap.get("FORMID").toString() : "";
			}
		}
		// 得到业务表的主键
		strSQL.delete(0, strSQL.length());// 清空
		strSQL.append("select PRIMARYKEY from BPIP_TABLE where TABLENAME='" + FORMTABLE + "'");
		List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(strSQL.toString());
		if (retlist1 != null && retlist1.size() > 0) {
			Map<String, Object> retmap1 = retlist1.get(0);
			if (retmap1 != null && retmap1.get("PRIMARYKEY") != null) {
				PRIMARYKEY = retmap1.get("PRIMARYKEY").toString();
			}
		}
		// 更新字体设置字段的值
		strSQL.delete(0, strSQL.length());// 清空
		strSQL.append("update " + FORMTABLE + " set CSSFONT ='" + CssFont + "' where " + PRIMARYKEY + "='" + FORMID + "'");
		userMapper.updateExecSQL(strSQL.toString());
		
		return result;
	}

	@Override
	public String getFontCss1(String FlowRunId) throws Exception {
		String FORMTABLE = "";
		String FORMID = "";
		String PRIMARYKEY = "";
		String CSSFONT = "";
		StringBuffer strSQL = new StringBuffer();
		String strRetrun = "";
		strSQL.append("select FORMTABLE,FORMID from FLOW_RUNTIME_PROCESS where ID='" + FlowRunId + "'");
		List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(strSQL.toString());
		if (retlist1 != null && retlist1.size() > 0) {
			Map<String, Object> retmap1 = retlist1.get(0);
			if (retmap1 != null) {
				FORMTABLE = retmap1.get("FORMTABLE") != null ? retmap1.get("FORMTABLE").toString() : "";
				FORMID = retmap1.get("FORMID") != null ? retmap1.get("FORMID").toString() : "";
			}
		}
		// 得到业务表的主键
		strSQL.delete(0, strSQL.length());// 清空
		strSQL.append("select PRIMARYKEY from BPIP_TABLE where TABLENAME='" + FORMTABLE + "'");
		List<Map<String, Object>> retlist2 = userMapper.selectListMapExecSQL(strSQL.toString());
		if (retlist2 != null && retlist2.size() > 0) {
			Map<String, Object> retmap2 = retlist2.get(0);
			if (retmap2 != null && retmap2.get("PRIMARYKEY") != null) {
				PRIMARYKEY = retmap2.get("PRIMARYKEY").toString();
			}
		}
		// 得到字体设置字段的值
		strSQL.delete(0, strSQL.length());// 清空
		strSQL.append("select CSSFONT from " + FORMTABLE + " where " + PRIMARYKEY + "='" + FORMID + "'");
		List<Map<String, Object>> retlist3 = userMapper.selectListMapExecSQL(strSQL.toString());
		if (retlist3 != null && retlist3.size() > 0) {
			Map<String, Object> retmap3 = retlist3.get(0);
			if (retmap3 != null && retmap3.get("CSSFONT") != null) {
				CSSFONT = retmap3.get("CSSFONT").toString();
			}
		}
		strRetrun = CSSFONT;
		return strRetrun;
	}

	@Override
	public boolean datadelete(String docid, String id) throws Exception {
		boolean revalue = false;
		String TABLEID = "";
		String TABLENAME = "";
		String PRIMARYKEY = "";
		String FIELDTYPE = "";
		String strSQL = "select c.TABLEID,c.TABLENAME,c.PRIMARYKEY from COLL_DOC_CONFIG b join BPIP_TABLE c on b.maintable=c.TABLEID where b.ID='" + id + "'";
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
		if (retlist != null && retlist.size() > 0) {
			Map<String, Object> retmap = retlist.get(0);
			TABLEID = retmap.get("TABLEID").toString();
			TABLENAME = retmap.get("TABLENAME").toString();
			PRIMARYKEY = retmap.get("PRIMARYKEY").toString();
			// 得到主键的类型
			strSQL = "select FIELDTYPE from BPIP_FIELD where TABLEID='" + TABLEID + "' and FIELDNAME='" + PRIMARYKEY + "'";
			List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(strSQL);
			if (retlist1 != null && retlist1.size() > 0) {
				Map<String, Object> retmap1 = retlist1.get(0);
				if (retmap1 != null && retmap1.get("FIELDTYPE") != null) {
					FIELDTYPE = retmap1.get("FIELDTYPE").toString();
				}
				if (FIELDTYPE.equals("1")) {
					strSQL = "delete " + TABLENAME + " where " + PRIMARYKEY + "='" + docid + "'";
				} else {
					strSQL = "delete " + TABLENAME + " where " + PRIMARYKEY + "=" + docid;
				}
				Integer retInt = userMapper.deleteExecSQL(strSQL);
				if (retInt != null && retInt > 0) {
					revalue = true;
				}
			}
		}
		return revalue;
	}

	@Override
	public int getBlobSize(String tableName, String fieldName) throws Exception {
		int rvalue = 0;
		String strSql = "Select BLOBSIZE from bpip_field Where TableID = (Select TableID From Bpip_Table "
				+ "Where TableName='" + tableName + "' ) And FieldName='" + fieldName + "'";
		DBSet ds = dbEngine.QuerySQL(strSql, "form5", tableName + fieldName);
		if (ds != null && ds.RowCount() > 0) {
			try {
				rvalue = ds.Row(0).Column("BLOBSIZE").getInteger();
			} catch (Exception ex) {
				rvalue = 0;
			}
		}
		ds = null;
		return rvalue;
	}

	@Override
	public String GetDictList(String tableName) throws Exception {
		String strCODE = "";
		String strNAME = "";
		String strSPELL = "";
		StringBuffer resultStr = new StringBuffer();
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select * from " + tableName + " order by CODE");
		DBSet ds = dbEngine.QuerySQL(sqlStr.toString());
		if (ds != null && ds.RowCount() > 0) {
			for (int i = 0; i < ds.RowCount(); i++) {
				strCODE = "";
				strNAME = "";
				strSPELL = "";
				strCODE = ds.Row(i).Column("CODE").getString();
				strNAME = ds.Row(i).Column("NAME").getString();
				strSPELL = ds.Row(i).Column("SPELL").getString();
				resultStr.append("<option value='" + strCODE + "/" + strSPELL + "#" + strCODE + "'>" 
						+ strCODE + " " + strNAME + "</option>");
			}
			ds = null;
		}
		return resultStr.toString();
	}

	private String[][] getDictList(String codeTable) {
		String[][] result = null;
		result = (String[][]) CodeHashtable.get(codeTable);
		String sqlValue = "";
		int getnum = 0;
		if (result == null) {
			DBSet ds = null;
			// 用户表
			if (codeTable.equals("BPIP_USER")) {
				sqlValue = "Select count(USERID) as NUM From BPIP_USER Where USERSTATE='0'";
				ds = dbEngine.QuerySQL(sqlValue);
				getnum = ds.Row(0).Column("NUM").getInteger();
				if (getnum > 2000) {
					result = new String[1][2];
					result[0][0] = "000000000000";
					result[0][1] = "无";
					CodeHashtable.put(codeTable, result);
				} else {
					sqlValue = "Select USERID,NAME From BPIP_USER Where USERSTATE='0' order by USERID";
					ds = dbEngine.QuerySQL(sqlValue);
					if (ds != null && ds.RowCount() > 0) {
						result = new String[ds.RowCount()][2];
						for (int i = 0; i < ds.RowCount(); i++) {
							result[i][0] = ds.Row(i).Column("USERID").getString();
							result[i][1] = ds.Row(i).Column("NAME").getString();
						}
						if (result != null) {
							CodeHashtable.put(codeTable, result);
						}
					}
				}
			}
			// 单位表
			if (codeTable.equals("BPIP_UNIT")) {
				sqlValue = "Select count(UNITID) as NUM From BPIP_UNIT Where  STATE!='1'";
				ds = dbEngine.QuerySQL(sqlValue);
				getnum = ds.Row(0).Column("NUM").getInteger();
				if (getnum > 2000) {
					result = new String[1][2];
					result[0][0] = "000000000000";
					result[0][1] = "无";
					CodeHashtable.put(codeTable, result);
				} else {
					sqlValue = "Select UNITID,UNITNAME From BPIP_UNIT Where  STATE!='1' ORDER BY UNITID";
					ds = dbEngine.QuerySQL(sqlValue);
					if (ds != null && ds.RowCount() > 0) {
						result = new String[ds.RowCount()][2];
						for (int i = 0; i < ds.RowCount(); i++) {
							result[i][0] = ds.Row(i).Column("UNITID").getString();
							result[i][1] = ds.Row(i).Column("UNITNAME").getString();
						}
						if (result != null) {
							CodeHashtable.put(codeTable, result);
						}
					}
				}
			}
			// 字典表
			if (!codeTable.equals("BPIP_USER") && !codeTable.equals("BPIP_UNIT")) {
				sqlValue = "Select CODE,NAME From " + codeTable + " where ISSHOW!='1' or ISSHOW is null  order by CODE";
				ds = dbEngine.QuerySQL(sqlValue);
				if (ds == null) {
					sqlValue = "Select CODE,NAME From " + codeTable + " order by CODE";
					ds = dbEngine.QuerySQL(sqlValue);
				}
				if (ds != null && ds.RowCount() > 0) {
					result = new String[ds.RowCount()][2];
					for (int i = 0; i < ds.RowCount(); i++) {
						result[i][0] = ds.Row(i).Column("CODE").getString();
						result[i][1] = ds.Row(i).Column("NAME").getString();
					}
					if (result != null) {
						CodeHashtable.put(codeTable, result);
					}
				}
			}
		}
		return result;
	}

	private String initDbRow(DBRow dbrow, String UpdateType, SessionUser userinfo, String _mFKID) {
		String[] KeyName = dbrow.getPrimaryKeyName();
		String pkValue = dbrow.Column(KeyName[0]).getString();
		if (pkValue != null && pkValue.trim().length() > 0) {
			UpdateType = "edit";
		} else {
			UpdateType = "add";
		}
		// --------------------------------------------------------------------------

		// 如果是新增加数据设置自动编号
		if (UpdateType.equals("add")) {
			String[] strColumnName = dbrow.getAllColumnName();
			if (!strColumnName.equals(null)) {
				for (int i = 0; i < strColumnName.length; i++) {
					if (strColumnName[i].equalsIgnoreCase("COLL_FKID") && _mFKID != null) {
						dbrow.Column(strColumnName[i]).setValue(_mFKID);
					}
					int iTag = dbrow.Column(strColumnName[i]).getTag().getValue();

					switch (iTag) {
					case 5:
						set_CASE_NUMBER(dbrow.Column(strColumnName[i]));
						break;
					case 6:
						set_ID_INT(dbrow.getTableName(), dbrow.Column(strColumnName[i]));
						break;
					case 7:
						set_ID_INT_1(dbrow, dbrow.Column(strColumnName[i]));
						break;
					case 8:
						set_ID_INT_2(dbrow, dbrow.Column(strColumnName[i]));
						break;
					case 9:
						set_ID_INT_3(dbrow, dbrow.Column(strColumnName[i]));
						break;
					case 10:
						set_ID_STR(dbrow.getTableName(), dbrow.Column(strColumnName[i]));
						break;
					case 11:
						set_ID_STR_1(dbrow, dbrow.Column(strColumnName[i]));
						break;
					case 12:
						set_ID_STR_2(dbrow, dbrow.Column(strColumnName[i]));
						break;
					case 13:
						set_ID_STR_3(dbrow, dbrow.Column(strColumnName[i]));
						break;
					}
				}
			}
		}
		if (UpdateType.equals("add")) {
			dbrow.setDataMode(DBMode.NEW);
		} else {
			dbrow.setDataMode(DBMode.EDITED);
		}
		return dbrow.Column(KeyName[0]).getString();
	}

	/**
	 * 输出错误信息HTML页面
	 * @param Msg
	 * @return String
	 */
	private String ErrorHtml(String Msg) {
		StringBuffer sb = new StringBuffer();
		sb.append(Msg);
		return sb.toString();
	}

	/**
	 * 填充流程属性
	 * @param FID 流程活动编号
	 * @param drMain 配置数据项
	 */
	private void getFlowProprty(FlowList flowList, String FID, String TableName, 
			String AliasName, String _mPKID, String _mFKID, 
			String _PAID1, String _PAID2, SessionUser _user, String _RID) {
		StringBuffer strSQL = new StringBuffer();
		String strTmp = null;
		String strDEFAULT = "";
		try {
			DBSet ds = null;
			String strgetFlowProprty = null;
			strgetFlowProprty = (String) getFlowProprty.get(FID + TableName);
			if (strgetFlowProprty == null) {
				strSQL.append("Select * From COLL_CONFIG_OPERATE_FIELD Where FID='")
						.append(FID + "' And FIELD like '" + TableName + "%'");
				ds = dbEngine.QuerySQL(strSQL.toString());
				if (ds != null) {
					getFlowProprty1.put(FID + TableName, ds);
				}
				getFlowProprty.put(FID + TableName, "1");
			} else {
				ds = (DBSet) getFlowProprty1.get(FID + TableName);
			}
			String strName = "";
			if (ds != null && ds.RowCount() > 0) {
				for (int i = 0; i < ds.RowCount(); i++) {
					FlowField ff = new FlowField();
					strName = ds.Row(i).Column("FIELD").getString();
					strName = strName.substring(strName.indexOf(".") + 1);
					// ----------------------------------------------------------
					if (AliasName != null && !AliasName.equals("")) {
						strName = AliasName + "$" + strName;
					}
					ff.setName(strName);
					ff.setIsShow(ds.Row(i).Column("ISDISPLAY").getString());
					ff.setIsWrite(ds.Row(i).Column("ISEDIT").getString());
					ff.setIsNull(ds.Row(i).Column("ISMUSTFILL").getString());
					ff.setIsForce(ds.Row(i).Column("ISFORCE").getString());
					if (ds.Row(i).Column("DEFAULT1").getValue() != null
							&& !ds.Row(i).Column("DEFAULT1").getString().trim().equals("")) {
						strTmp = null;
						try {
							strDEFAULT = ds.Row(i).Column("DEFAULT1").getString();
							if (strDEFAULT.substring(0, 1).equals("?")) { // 多选一的情况
								strDEFAULT = strDEFAULT.substring(1);
								String ArrayValue[] = strDEFAULT.split(":");
								for (int k = 0; k < ArrayValue.length; k++) {
									strTmp = getValueByExpression(ArrayValue[k], _mPKID, _mFKID, _PAID1, _PAID2, _user, _RID);
									if (strTmp != null && strTmp.trim().length() > 0) {
										LOGGER.info("已经计算出其中一个时不再计算其它的值");
										break; // 已经计算出其中一个时不再计算其它的值
									}
								}
							} else {
								strTmp = getValueByExpression(strDEFAULT, _mPKID, _mFKID, _PAID1, _PAID2, _user, _RID);
							}
						} catch (Exception ex) {
							LOGGER.error("getValueByExpression", ex.toString());
						}
						if (strTmp != null) {
							ff.setDefault(strTmp);
						}
					}
					flowList.addItem(ff);
					ff = null;
				}
				ds = null;
			}
		} catch (Exception ex) {
			LOGGER.error("getFlowProprty", ex.toString());
		}
		flowList = null;
		_user = null;
	}

	/**
	 * 功能:根据算术表达式计算值
	 * @param expression 待计算的函数表达式
	 * @return String 函数返回的结果 表达式中的[表名.字段名]的值在表达式计算前须取出来后将表达式的对应部公置换掉
	 */
	private String getValueByExpression(String expression, String _mPKID, String _mFKID, 
			String _PAID1, String _PAID2, SessionUser _user, String _RID) {
		DBSet mdbset;
		String resultStr;
		StringBuffer sqlStr = new StringBuffer();
		// SQL语句解析
		if (expression.indexOf("@SQL:") > -1) {
			resultStr = getparseSqlValue(expression, _mFKID, _PAID1, _PAID2);
			return resultStr;
		}
		expression = prepareExpression(expression, _mPKID, _mFKID, _PAID1, _PAID2, _user, _RID);
		
		resultStr = expression;
		// 有运算符，需要将其交与数据库进行运算
		if (!(expression.indexOf("+") == -1 && expression.indexOf("*") == -1 && expression.indexOf("||") == -1
				&& expression.indexOf("/") == -1 && expression.indexOf("(") == -1 && expression.indexOf(")") == -1)) {
			if (expression.toLowerCase().indexOf("select") > -1 || expression.indexOf("[") > -1
					|| expression.indexOf("=") > -1) {
				// 表达式中有select语句，表达式不能参与数据库运算，故将表达式直接赋予结果串
				resultStr = expression;
			} else {
				if (DataBaseType.equals("1")) {// Oracle数据库
					sqlStr.append("Select " + expression + " as resultStr from dual");
				} else {// MSSQL or MySQL
					sqlStr.append("Select " + expression + " as resultStr");
				}
				try {
					mdbset = dbEngine.QuerySQL(sqlStr.toString());
					if (mdbset != null) {
						resultStr = mdbset.Row(0).Column("resultStr").getValue().toString();
						mdbset = null;
					} else {
						resultStr = expression; // 当mdbset为null时，则表达式有误
					}
				} catch (Exception ex) {
					LOGGER.error("出现异常", ex.getMessage());
				}
				if (resultStr.equals("???")) {
					resultStr = expression;
					LOGGER.info("2" + resultStr);
				}
			}
		}
		if (resultStr.startsWith("'")) {
			resultStr = resultStr.substring(1);
		}
		if (resultStr.endsWith("'")) {
			resultStr = resultStr.substring(0, resultStr.length() - 1);
		}
		
		return resultStr;
	}

	/**
	 * 功能:解析格式为@SQL:字段名@Select 字段名 From …..Where 字段名='PKVALUE'的语句
	 * @param expression 表达式
	 * @return String
	 */
	private String getparseSqlValue(String expression, String _mFKID, String _PAID1, String _PAID2) {
		String rtnValue = null;
		String fieldName;
		String sqlStr = null;
		String sqlStrTmp;
		DBSet ds = null;
		boolean boolMul = false; // 取多条记录组合值的标志(ROWS)
		int indexTmp;
		SimpleDateFormat fmt;
		// expression=@SQL:字段名@Select 字段名 From ... Where 字段名='PKVALUE'
		if (expression.indexOf("(ROWS)") > -1) {
			expression = expression.replaceFirst("\\(ROWS\\)", "");
			boolMul = true;
		}
		expression = expression.replaceFirst("@SQL:", "");
		indexTmp = expression.indexOf("@");
		fieldName = expression.substring(0, indexTmp);
		
		expression = expression.substring(indexTmp + 1);
		sqlStrTmp = expression;
		if (_mFKID != null && _mFKID.indexOf("A") == 0) {
			sqlStrTmp = sqlStrTmp.replaceAll("CASEIDVALUE", _mFKID);

		} else if (_PAID1 != null && _PAID1.indexOf("A") == 0) {
			sqlStrTmp = sqlStrTmp.replaceAll("CASEIDVALUE", _PAID1);

		} else if (_PAID2 != null && _PAID2.indexOf("A") == 0) {
			sqlStrTmp = sqlStrTmp.replaceAll("CASEIDVALUE", _PAID2);
		}
		
		if (_mFKID != null && _mFKID.indexOf("R") == 0) {
			sqlStrTmp = sqlStrTmp.replaceAll("PERSONIDVALUE", _mFKID);

		} else if (_PAID1 != null && _PAID1.indexOf("R") == 0) {
			sqlStrTmp = sqlStrTmp.replaceAll("PERSONIDVALUE", _PAID1);

		} else if (_PAID2 != null && _PAID2.indexOf("R") == 0) {
			sqlStrTmp = sqlStrTmp.replaceAll("PERSONIDVALUE", _PAID2);
		}
		ds = dbEngine.QuerySQL(sqlStrTmp);
		if (sqlStrTmp.indexOf("PKVALUE") > -1) {
			if (ds == null && _mFKID != null) {
				sqlStr = sqlStrTmp.replaceAll("PKVALUE", _mFKID);
				ds = dbEngine.QuerySQL(sqlStr);
			}
			if (ds == null && _PAID1 != null) {
				sqlStr = sqlStrTmp.replaceAll("PKVALUE", _PAID1);
				ds = dbEngine.QuerySQL(sqlStr);
			}
			if (ds == null && _PAID2 != null) {
				sqlStr = sqlStrTmp.replaceAll("PKVALUE", _PAID2);
				ds = dbEngine.QuerySQL(sqlStr);
			}
		}
		if (ds != null && ds.RowCount() > 0) {
			rtnValue = "";
			if (boolMul) {
				for (int i = 0; i < ds.RowCount(); i++) {
					if (ds.Row(i).Column(fieldName).getValue() != null) {
						rtnValue = rtnValue + String.valueOf(ds.Row(i).Column(fieldName).getValue()) + "、";
					}
				}
				if (rtnValue.endsWith("、")) {
					rtnValue = rtnValue.substring(0, rtnValue.length() - 1);
				}
			} else {
				if (ds.Row(0).Column(fieldName).getValue() != null) {
					if (ds.Row(0).Column(fieldName).getType().getValue() == DBType.DATETIME.getValue()) {
						fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						rtnValue = fmt.format(ds.Row(0).Column(fieldName).getDate());
					} else if (ds.Row(0).Column(fieldName).getType().getValue() == DBType.DATE.getValue()) {
						fmt = new SimpleDateFormat("yyyy-MM-dd");
						rtnValue = fmt.format(ds.Row(0).Column(fieldName).getDate());
					}
					else {
						rtnValue = String.valueOf(ds.Row(0).Column(fieldName).getValue());
					}
				}
			}
			ds = null;
		}
		return rtnValue;
	}

	/**
	 * 获取用户单位
	 * @param UserID
	 * @return String
	 * @throws Exception 
	 */
	private String getUserUnitID(String UserID) throws Exception {
		String result = null;
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("Select UnitID From BPIP_USER Where UserID='" + UserID + "'");
		Map<String, Object> retmap = userMapper.selectMapExecSQL(strSQL.toString());
		if (retmap != null && retmap.size() > 0) {
			result = retmap.get("UnitID").toString();
		}
		return result;
	}

	/**
	 * 获取表字段属性
	 * @param TableName
	 * @return dbRow
	 */
	private DBRow getTablePropery(String TableName) {
		DBRow dbPropery = null;
		StringBuffer strSQL = new StringBuffer();
		StringBuffer strSQL1 = new StringBuffer();
		StringBuffer strSQL2 = new StringBuffer();
		StringBuffer strSQL3 = new StringBuffer();

		DBSet dsTable = null;
		strSQL.append("Select a.TABLEID,a.TABLETYPE,a.PRIMARYKEY From BPIP_TABLE a Where a.TABLENAME='" + TableName + "'");
		dsTable = dbEngine.QuerySQL(strSQL.toString(), "form1", TableName);
		if (dsTable != null) {
			if (dsTable.RowCount() > 0) {
				String strTableType = dsTable.Row(0).Column("TABLETYPE").getString();
				String strTableID = dsTable.Row(0).Column("TABLEID").getString();
				String strKey = dsTable.Row(0).Column("PRIMARYKEY").getString();
				dbPropery = new DBRow();
				dbPropery.setTableName(TableName);
				dbPropery.setPrimaryKey(strKey);
				if (strTableType != null) {
					if (strTableType.equals("2")) {
						dbPropery.isBaseData = true;
					}
				}
				strSQL.delete(0, strSQL.length()); // 清空
				
				DBSet dsField1 = null;
				DBSet dsField2 = null;
				DBSet dsField3 = null;
				if (DataBaseType.equals("3")) {// mysql数据库
					strSQL.append("Select a.FIELDNAME,a.CHINESENAME,a.FIELDTYPE,a.FIELDLENGTH,a.DICTTABLE,a.ISNULL,a.FIELDTAG,a.TAGEXT,a.Auto1 "
							+ "From BPIP_FIELD a Left Join BPIP_FIELD b on a.Auto1=b.FIELDID Left Join BPIP_FIELD c on a.Auto2=c.FIELDID Left Join BPIP_FIELD d on a.Auto3=d.FIELDID "
							+ "Where a.TABLEID='" + strTableID + "'");
					strSQL1.append("Select b.FIELDNAME From BPIP_FIELD a Left Join BPIP_FIELD b on a.Auto1=b.FIELDID Left Join BPIP_FIELD c on a.Auto2=c.FIELDID Left Join BPIP_FIELD d on a.Auto3=d.FIELDID "
							+ "Where a.TABLEID='" + strTableID + "'");
					strSQL2.append("Select c.FIELDNAME From BPIP_FIELD a Left Join BPIP_FIELD b on a.Auto1=b.FIELDID Left Join BPIP_FIELD c on a.Auto2=c.FIELDID Left Join BPIP_FIELD d on a.Auto3=d.FIELDID "
							+ "Where a.TABLEID='" + strTableID + "'");
					strSQL3.append("Select d.FIELDNAME From BPIP_FIELD a Left Join BPIP_FIELD b on a.Auto1=b.FIELDID Left Join BPIP_FIELD c on a.Auto2=c.FIELDID Left Join BPIP_FIELD d on a.Auto3=d.FIELDID "
							+ "Where a.TABLEID='" + strTableID + "'");
					
					dsField1 = dbEngine.QuerySQL(strSQL1.toString(), "form6", strTableID);
					dsField2 = dbEngine.QuerySQL(strSQL2.toString(), "form7", strTableID);
					dsField3 = dbEngine.QuerySQL(strSQL3.toString(), "form8", strTableID);
				} else {
					strSQL.append("Select a.FIELDNAME,a.CHINESENAME,a.FIELDTYPE,a.FIELDLENGTH,a.DICTTABLE,a.ISNULL,a.FIELDTAG,a.TAGEXT,a.Auto1 as Auto1a,b.FIELDNAME as Auto1b,c.FIELDNAME as Auto2,d.FIELDNAME as Auto3 "
							+ "From BPIP_FIELD a Left Join BPIP_FIELD b on a.Auto1=b.FIELDID Left Join BPIP_FIELD c on a.Auto2=c.FIELDID Left Join BPIP_FIELD d on a.Auto3=d.FIELDID "
							+ "Where a.TABLEID='" + strTableID + "'");
				}
				DBSet dsField = dbEngine.QuerySQL(strSQL.toString(), "form9", strTableID);
				if (dsField != null) {
					if (dsField.RowCount() > 0) {
						for (int i = 0; i < dsField.RowCount(); i++) {
							DBColumn dc = new DBColumn(dsField.Row(i).Column("FIELDNAME").getString());
							dc.setChineseName(dsField.Row(i).Column("CHINESENAME").getString());
							dc.setType(switchDbType(dsField.Row(i).Column("FIELDTYPE").getString()));
							dc.setLength(dsField.Row(i).Column("FIELDLENGTH").getInteger());
							dc.setCodeTable(dsField.Row(i).Column("DICTTABLE").getString());
							dc.setNULL(true);
							dc.setTag(switchDbTag(dsField.Row(i).Column("FIELDTAG").getString()));
							dc.setTagExt(dsField.Row(i).Column("TAGEXT").getInteger());
							
							if (DataBaseType.equals("3")) {// mysql数据库
								dsField.Row(i).addColumn("Auto1a", "");
								dsField.Row(i).addColumn("Auto1b", "");
								dsField.Row(i).addColumn("Auto2", "");
								dsField.Row(i).addColumn("Auto3", "");
							}
							if (dsField.Row(i).Column("FIELDTAG").getString().trim().equals("5")) {
								if (DataBaseType.equals("3")) {// mysql数据库
									dc.setAuto1(dsField.Row(i).Column("Auto1").getString());
								} else {
									dc.setAuto1(dsField.Row(i).Column("Auto1a").getString());
								}
							} else {
								if (DataBaseType.equals("3")) {// mysql数据库
									dc.setAuto1(dsField1.Row(i).Column("FIELDNAME").getString());
								} else {
									dc.setAuto1(dsField.Row(i).Column("Auto1b").getString());
								}
							}
							if (DataBaseType.equals("3")) {// mysql数据库
								dc.setAuto2(dsField2.Row(i).Column("FIELDNAME").getString());
								dc.setAuto3(dsField3.Row(i).Column("FIELDNAME").getString());
							} else {
								dc.setAuto2(dsField.Row(i).Column("Auto2").getString());
								dc.setAuto3(dsField.Row(i).Column("Auto3").getString());
							}
							dbPropery.addColumn(dc);
						}
					}
					dsField = null;
				}
				dbPropery.setPrimaryKey(strKey);
			}
			dsTable = null;
		}
		return dbPropery;
	}

	/**
	 * 将从数据库中读取的文本数据类型转换为dbType型
	 * @param strType
	 * @return dbType
	 */
	private DBType switchDbType(String strType) {
		DBType dbtype = null;
		int iType = 0;
		if (strType != null) {
			iType = Integer.parseInt(strType);
		}
		switch (iType) {
		case 1:
			dbtype = DBType.STRING;
			break;
		case 2:
			dbtype = DBType.FLOAT;
			break;
		case 3:
			dbtype = DBType.BLOB;
			break;
		case 4:
			dbtype = DBType.DATE;
			break;
		case 5:
			dbtype = DBType.DATETIME;
			break;
		case 6:
			dbtype = DBType.LONG;
			break;
		case 7:
			dbtype = DBType.CLOB;
			break;
		}
		return dbtype;
	}

	/**
	 * 将从数据库中读取的文本数据标记转换为dbTag型
	 * @param strTag
	 * @return dbTag
	 */
	private DBTag switchDbTag(String strTag) {
		DBTag dbtag = null;
		int iTag = 0;
		if (strTag != null) {
			iTag = Integer.parseInt(strTag);
		}
		switch (iTag) {
		case 1:
			dbtag = DBTag.NONE;
			break;
		case 2:
			dbtag = DBTag.CODE;
			break;
		case 3:
			dbtag = DBTag.USER;
			break;
		case 4:
			dbtag = DBTag.UNIT;
			break;
		case 5:
			dbtag = DBTag.CASE_NUMBER;
			break;
		case 6:
			dbtag = DBTag.ID_INT;
			break;
		case 7:
			dbtag = DBTag.ID_INT_1;
			break;
		case 8:
			dbtag = DBTag.ID_INT_2;
			break;
		case 9:
			dbtag = DBTag.ID_INT_3;
			break;
		case 10:
			dbtag = DBTag.ID_STR;
			break;
		case 11:
			dbtag = DBTag.ID_STR_1;
			break;
		case 12:
			dbtag = DBTag.ID_STR_2;
			break;
		case 13:
			dbtag = DBTag.ID_STR_3;
			break;
		case 14:
			dbtag = DBTag.COMMUMNITY;
			break;
		}
		return dbtag;
	}

	/**
	 * 功能:处理表达式，使其能计算出来
	 * @param expression 待计算的函数表达式
	 * @return String 处理完后的表达式
	 */
	private String prepareExpression(String expression, String _mPKID, String _mFKID, 
			String _PAID1, String _PAID2, SessionUser _user, String _RID) {
		String relationField, tempValue, strPrimary;
		DBSet mdbset = null, dsTmp = null;
		String initExpression = expression;
		String[] TableField;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		String tmpStr;
		StringBuffer sqlStr = new StringBuffer();
		StringBuffer sqlStrTemp = new StringBuffer();
		StringBuffer addBuf = new StringBuffer();
		try {
			int beginIndex = -1, endIndex = -1, pointIndex = -1, tmpIndex1, tmpIndex2;
			// 处理表达式中的[表名.字段名]
			while (initExpression.indexOf("[", beginIndex + 1) > beginIndex) {
				if ((beginIndex >= initExpression.length() - 6)) {
					break;
				}
				beginIndex = initExpression.indexOf("[", beginIndex + 1);
				endIndex = initExpression.indexOf("]", beginIndex + 1);
				pointIndex = initExpression.indexOf(".", beginIndex + 1);
				
				if (!(pointIndex > beginIndex && pointIndex < endIndex)) {
					beginIndex = endIndex;
					continue;
				}
				TableField = initExpression.substring(beginIndex + 1, endIndex).split("\\.");
				sqlStr.delete(0, sqlStr.length()); // 清空
				
				if (TableField[1] == null) {
					TableField[1] = "";
				}
				if (TableField[0] == null) {
					TableField[0] = "";
				}
				mdbset = null;
				dsTmp = null;
				if (TableField[0].length() > 0 && TableField[1].length() > 0) {
					sqlStr.append("Select " + TableField[1] + " from " + TableField[0]);
					relationField = getRelationField(TableField[0]);
					strPrimary = getPrimaryFieldName(TableField[0]);
					if (strPrimary != null) {
						sqlStrTemp.delete(0, sqlStrTemp.length()); // 清空
						sqlStrTemp.append("Select " + TableField[1] + " from ")
								  .append(TableField[0] + " WHERE " + strPrimary + "='" + _mPKID + "'");
						mdbset = dbEngine.QuerySQL(sqlStrTemp.toString());
					}
					if (mdbset == null) {
						addBuf.delete(0, addBuf.length()); // 清空
						addBuf.append(sqlStr.toString() + " Where " + relationField + "='" + _mFKID + "'");
						mdbset = dbEngine.QuerySQL(addBuf.toString());
					}
					if (mdbset == null) {
						addBuf.delete(0, addBuf.length()); // 清空
						addBuf.append(sqlStr.toString() + " Where " + relationField + "='" + _PAID1 + "'");
						mdbset = dbEngine.QuerySQL(addBuf.toString());
					}
					if (mdbset == null) {
						addBuf.delete(0, addBuf.length()); // 清空
						addBuf.append(sqlStr.toString() + " Where " + relationField + "='" + _PAID2 + "'");
						mdbset = dbEngine.QuerySQL(addBuf.toString());
					}
					if (mdbset != null) {
						if (mdbset.RowCount() > 1 && _RID != null && _RID.length() > 0) {
							dsTmp = getParentDocDs(TableField[0], _RID);
						}
					}
				}
				if (dsTmp != null) {
					mdbset = dsTmp;
				}
				tempValue = "";
				if (mdbset != null) {
					try {
						/*
						 * 1 VARCHAR2 2 FLOAT 3 BLOB 4 DATE 5 DATETIME 6 NUMBER 7 CLOB
						 */
						String fieldType = getFieldType(TableField[0], TableField[1]);
						if (fieldType.equals("4")) {
							fmt = new SimpleDateFormat("yyyy-MM-dd");
							tempValue = fmt.format(mdbset.Row(0).Column(TableField[1]).getDate());
						} else if (fieldType.equals("5")) {
							fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							tempValue = fmt.format(mdbset.Row(0).Column(TableField[1]).getDate());
						}
						else {
							tempValue = mdbset.Row(0).Column(TableField[1]).getValue().toString();
						}
					} catch (Exception ex) {
						LOGGER.error("出现异常", ex.getMessage());
						tempValue = "";
					}
				}
				tmpStr = "[" + TableField[0] + "." + TableField[1] + "]";
				tmpIndex1 = initExpression.indexOf(tmpStr);
				tmpIndex2 = initExpression.indexOf("CN(" + tmpStr + ")");
				LOGGER.info("expression=" + expression);
				LOGGER.info("initExpression=" + initExpression);
				LOGGER.info("tmpStr=" + tmpStr);
				
				LOGGER.info("tmpIndex1=" + tmpIndex1);
				LOGGER.info("tmpIndex2=" + tmpIndex2);
				if (tmpIndex1 - tmpIndex2 == 3) { // 需作转换
					addBuf.delete(0, addBuf.length()); // 清空
					addBuf.append("CN\\(\\[" + TableField[0] + "\\." + TableField[1] + "\\]\\)");
					expression = expression.replaceAll(addBuf.toString(),
							convertFieldValueToCn(TableField[0], TableField[1], tempValue));
				} else {
					addBuf.delete(0, addBuf.length()); // 清空
					addBuf.append("\\[" + TableField[0] + "\\." + TableField[1] + "\\]");
					expression = expression.replaceAll(addBuf.toString(), tempValue);
				}
				beginIndex = endIndex;
			}
		} catch (Exception ex) {
			LOGGER.error("While出现异常：" + ex.getMessage());
		}
		// 处理当前日期
		if (DataBaseType.equals("1")) {// Oracle数据库
			expression = expression.replaceAll("sysdate", "to_char(sysdate,'yyyy-mm-dd')");
		}
		else if (DataBaseType.equals("2")) {// MSSQL数据库
			expression = expression.replaceAll("sysdate", "getdate()");
		}
		else if (DataBaseType.equals("3")){// MySQL数据库
			expression = expression.replaceAll("sysdate", "str_to_date(sysdate,'%Y-%m-%d')");
		}
		// 处理自定义的变量
		if (expression.lastIndexOf("{LoginID}") > -1) { // 登录名
			expression = expression.replaceAll("\\{LoginID\\}", _user.getLoginID());
		}
		if (expression.lastIndexOf("{Name}") > -1) { // 姓名
			expression = expression.replaceAll("\\{Name\\}", _user.getName());
		}
		if (expression.lastIndexOf("{UserID}") > -1) { // 用户编号
			expression = expression.replaceAll("\\{UserID\\}", _user.getUserID());
		}
		if (expression.lastIndexOf("{LCODE}") > -1) { // 用户内部编号
			expression = expression.replaceAll("\\{LCODE\\}", _user.getLCODE());
		}
		if (expression.lastIndexOf("{UnitName}") > -1) { // 所在单位名称
			expression = expression.replaceAll("\\{UnitName\\}", _user.getUnitName());
		}
		if (expression.lastIndexOf("{UnitID}") > -1) { // 所在单位编号
			expression = expression.replaceAll("\\{UnitID\\}", _user.getUnitID());
		}
		if (expression.lastIndexOf("{Custom1}") > -1) { // 自定义参数1
			expression = expression.replaceAll("\\{Custom1\\}", _user.getCustom1());
		}
		if (expression.lastIndexOf("{Custom2}") > -1) { // 自定义参数2
			expression = expression.replaceAll("\\{Custom2\\}", _user.getCustom2());
		}
		if (expression.lastIndexOf("{Custom3}") > -1) { // 自定义参数3
			expression = expression.replaceAll("\\{Custom3\\}", _user.getCustom3());
		}
		if (expression.lastIndexOf("{Custom4}") > -1) { // 自定义参数4
			expression = expression.replaceAll("\\{Custom4\\}", _user.getCustom4());
		}
		if (expression.lastIndexOf("{Custom5}") > -1) { // 自定义参数5
			expression = expression.replaceAll("\\{Custom5\\}", _user.getCustom5());
		}
		/******************************* 时间的处理 ************************************/
		Date date = new Date();
		if (expression.lastIndexOf("{YYYY年}") > -1) { // 当前年(中文式)
			fmt = new SimpleDateFormat("yyyy年");
			expression = expression.replaceAll("\\{YYYY年\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY年MM月}") > -1) { // 当前年月(中文式)
			fmt = new SimpleDateFormat("yyyy年MM月");
			expression = expression.replaceAll("\\{YYYY年\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY年MM月DD日}") > -1) { // 当前日期(中文式)
			fmt = new SimpleDateFormat("yyyy年MM月dd日");
			expression = expression.replaceAll("\\{YYYY年MM月DD日\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY年MM月DD日 HH:MM:SS}") > -1) { // 当前时间(中文式)
			fmt = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
			expression = expression.replaceAll("\\{YYYY年MM月DD日 HH:MM:SS\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY}") > -1) { // 当前年
			fmt = new SimpleDateFormat("yyyy");
			expression = expression.replaceAll("\\{YYYY\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY-MM}") > -1) { // 当前年月
			fmt = new SimpleDateFormat("yyyy-MM");
			expression = expression.replaceAll("\\{YYYY-MM\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY-MM-DD}") > -1) { // 当前日期
			fmt = new SimpleDateFormat("yyyy-MM-dd");
			expression = expression.replaceAll("\\{YYYY-MM-DD\\}", fmt.format(date));
		}
		if (expression.lastIndexOf("{YYYY-MM-DD HH:MM:SS}") > -1) { // 当前时间
			fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			expression = expression.replaceAll("\\{YYYY-MM-DD HH:MM:SS\\}", fmt.format(date));
		}
		return expression;
	}

	/**
	 * 给数据行赋值
	 * @param dr 要赋值的数据行
	 * @param Key 数据行的主键值
	 * @throws Exception 
	 */
	private void fullData(DBRow dr, String Key, boolean isBase) throws Exception {
		// 构造Where语句
		String PrimaryKey = dr.getPrimaryKeyName()[0];
		StringBuffer addBuf = new StringBuffer();
		addBuf.append(" Where " + PrimaryKey + " = " + Key);
		String strWhere = " Where " + PrimaryKey + " = " + Key;
		if (dr.Column(PrimaryKey).getType().getValue() == DBType.STRING.getValue()) {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(" Where " + PrimaryKey + " = '" + Key).append("'");
			strWhere = addBuf.toString();
		}
		boolean deleTag = false;
		String columns[] = dr.getAllColumnName();
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].equalsIgnoreCase("DELETAG")) {
				deleTag = true;
				break;
			}
		}
		if (isBase && deleTag) {
			addBuf.append(" And DeleTag='0' ");
			strWhere = addBuf.toString();
		}
		// --------------------------------------------------------------------------
		setData(dr, strWhere);
	}

	private void setData(DBRow dr, String where) throws Exception {
		// 查询数据库
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("Select * From " + dr.getTableName() + where);
		DBSet ds = dbEngine.QuerySQL(strSQL.toString());
		if (ds != null && ds.RowCount() > 0) {
			DBRow drTmp = ds.Row(0);
			String[] saSourceColumn = drTmp.getAllColumnName();
			String[] saTargetColumn = dr.getAllColumnName();
			// 赋值
			for (int i = 0; i < saSourceColumn.length; i++) {
				for (int j = 0; j < saTargetColumn.length; j++) {
					if (saSourceColumn[i].equals(saTargetColumn[j])) {
						dr.Column(saTargetColumn[j]).setValue(drTmp.Column(saSourceColumn[i]).getValue());
						if (dr.Column(saTargetColumn[j]).getTag().getValue() == DBTag.CODE.getValue()) {
							if (dr.Column(saTargetColumn[j]).getString() != null
									&& !dr.Column(saTargetColumn[j]).getString().equals("")) {
								dr.Column(saTargetColumn[j])
										.setCValue(getCodeValue(dr.Column(saTargetColumn[j]).getCodeTable(),
												dr.Column(saTargetColumn[j]).getString()));
							}
						}
						if (dr.Column(saTargetColumn[j]).getTag().getValue() == DBTag.UNIT.getValue()) {
							if (dr.Column(saTargetColumn[j]).getString() != null
									&& !dr.Column(saTargetColumn[j]).getString().equals("")) {
								dr.Column(saTargetColumn[j])
										.setCValue(getUnitName(dr.Column(saTargetColumn[j]).getString()));
							}
						}
						if (dr.Column(saTargetColumn[j]).getTag().getValue() == DBTag.USER.getValue()) {
							if (dr.Column(saTargetColumn[j]).getString() != null
									&& !dr.Column(saTargetColumn[j]).getString().equals("")) {
								dr.Column(saTargetColumn[j])
										.setCValue(getUserName(dr.Column(saTargetColumn[j]).getString()));
							}
						}
					}
				}
			}
			ds = null; drTmp = null;
		}
	}

	/**
	 * 根据字段名得到字类型
	 * @param tableName
	 * @param fieldName
	 * @return String
	 * @throws Exception 
	 */
	private String getFieldType(String tableName, String fieldName) throws Exception {
		/*
		 * 1 VARCHAR2 2 FLOAT 3 BLOB 4 DATE 5 DATETIME 6 NUMBER 7 CLOB
		 */
		String rtnStr = "";
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("Select FIELDTYPE From BPIP_FIELD Where UPPER(FIELDNAME) ='" + fieldName.toUpperCase()
					+ "' And TABLEID In (Select TABLEID FROM BPIP_TABLE Where UPPER(TABLENAME) = '")
			  .append(tableName.toUpperCase() + "')");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sqlStr.toString());
		if (retlist != null && retlist.size() > 0) {
			Map<String, Object> retmap = retlist.get(0);
			if (retmap != null && retmap.get("FIELDTYPE") != null) {
				rtnStr = retmap.get("FIELDTYPE").toString();
			}
		}
		return rtnStr;
	}

	/**
	 * 用哈西表填充数据
	 * @param has
	 * @param dbrow
	 * @throws Exception 
	 */
	private void fullDataFromRequest(Request request, DBRow dbrow, String Alias) throws Exception {
		String[] strColumnName = dbrow.getAllColumnName();
		String colName = "";
		String itemName = "";
		String strtmpcheckValue = "";
		String strcheckValue = "";
		String strDate = "";
		int isadd = 0; // 是否已经读取到值
		// 得到是多选控件的字段
		String strFIELDNAMEs = "";
		strFIELDNAMEs = (String) MoreSelectHashtable.get(dbrow.getTableName());
		if (strFIELDNAMEs == null) {
			strFIELDNAMEs = "";
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("Select FIELDNAME From BPIP_FIELD Where TABLEID in (select TABLEID from BPIP_TABLE where TABLENAME='")
				  .append(dbrow.getTableName()).append("') and FIELDTAG='2' and TAGEXT=4");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (strFIELDNAMEs != null) {
				strFIELDNAMEs = "";
			}
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					if (strFIELDNAMEs.length() == 0) {
						strFIELDNAMEs = retmap.get("FIELDNAME").toString();
					} else {
						strFIELDNAMEs = strFIELDNAMEs + "," + retmap.get("FIELDNAME").toString();
					}
				}
			}
			if (strFIELDNAMEs.length() == 0) {
				strFIELDNAMEs = "no";
			}
			MoreSelectHashtable.put(dbrow.getTableName(), strFIELDNAMEs);
		}
		if (strFIELDNAMEs.equals("no")) {
			strFIELDNAMEs = "";
		}
		// -------------------
		if (!strColumnName.equals(null)) {
			for (int i = 0; i < strColumnName.length; i++) {
				isadd = 0;
				colName = strColumnName[i].toUpperCase();
				itemName = strColumnName[i];
				if (Alias != null && !Alias.equals("")) {
					itemName = Alias + "$" + itemName;
				}
				itemName = itemName.toUpperCase();
				
				if (dbrow.Column(colName).getType().getValue() == DBType.BLOB.getValue()) {
					if (request.getBlobItem(itemName) != null) {
						isadd = 1;
						dbrow.Column(colName).setValue(request.getBlobItem(itemName));
					}
				} else
				// if (request.getStringItem(itemName) != null &&
				// !request.getStringItem(itemName).equals("")) {
				if (request.getStringItem(itemName) != null) {
					if (dbrow.Column(colName).getType().getValue() == DBType.DATE.getValue()
							|| dbrow.Column(colName).getType().getValue() == DBType.DATETIME.getValue()) {
						isadd = 1;
						strDate = request.getStringItem(itemName);
						if (strDate.length() > 0) {
							dbrow.Column(colName).setValue(DateWork.StringToDateTime(request.getStringItem(itemName)));
						} else {
							dbrow.Column(colName).setValue(null);
						}
					} else {
						if (!request.getStringItem(itemName).equals("请选择")) {
							isadd = 1;
							dbrow.Column(colName).setValue(request.getStringItem(itemName));
						}
					}
				}
				// 处理多选的情况-------
				if (isadd == 0) {
					if (strFIELDNAMEs.indexOf(itemName) > -1) {
						LOGGER.info("读取多选控件字段：" + itemName);
						strcheckValue = "";
						// 支持50个多选项目
						for (int itemnum = 0; itemnum < 50; itemnum++) {
							if (request.getStringItem(itemName + "_checkbox" + String.valueOf(itemnum)) != null) {
								strtmpcheckValue = request.getStringItem(itemName + "_checkbox" + String.valueOf(itemnum));
								if (strcheckValue.length() == 0) {
									strcheckValue = strtmpcheckValue;
								} else {
									strcheckValue = strcheckValue + "," + strtmpcheckValue;
								}
							}
						}
						if (strcheckValue.length() > 0) {
							dbrow.Column(colName).setValue(strcheckValue);
						}
					}
				}
				// -------------------
			}
		}
	}

	/**
	 * 若字段的值为代码，则将其转为代码所对应的值
	 * @param tableName
	 * @param fieldName
	 * @param fieldValue
	 * @return String
	 * @throws Exception 
	 */
	private String convertFieldValueToCn(String tableName, String fieldName, String fieldValue) throws Exception {
		String rtnValue = null;
		DBRow dbMain = this.getTablePropery(tableName);
		ItemList itemList = new ItemList(false);
		itemList.fullFromDbRow(dbMain, null);
		ItemField itemField = itemList.getItem(fieldName);
		if (itemField.isCode()) {
			rtnValue = getCodeValue(itemField.getCodeTable(), fieldValue);
		} else if (itemField.isUser()) {
			rtnValue = getUserName(fieldValue);
		} else if (itemField.isUnit()) {
			rtnValue = getUnitName(fieldValue);
		}
		if (rtnValue == null) {
			rtnValue = fieldValue;
		}
		return rtnValue;
	}

	/**
	 * 根据表单表名，关联流程的流转ID得到表单的缺省值
	 * @param docTableName
	 * @param _RID
	 * @return
	 */
	private DBSet getParentDocDs(String docTableName, String _RID) {
		StringBuffer strSQL = new StringBuffer();
		String ridTmp = _RID;
		DBSet ds, dsResult = null;
		int i = 0;
		strSQL.append("Select * from FLOW_RUNTIME_PROCESS Where ID='" + ridTmp + "' ");
		ds = dbEngine.QuerySQL(strSQL.toString());
		while (ds != null && !ds.Row(0).Column("FORMTABLE").getString().equalsIgnoreCase(docTableName) && i < 10) {
			i++;
			ridTmp = ds.Row(0).Column("RID").getString();
			strSQL.delete(0, strSQL.length()); // 清空
			strSQL.append("Select * from FLOW_RUNTIME_PROCESS Where ID='" + ridTmp + "' ");
			ds = dbEngine.QuerySQL(strSQL.toString());
		}
		if (ds != null && ds.RowCount() > 0) {
			strSQL.delete(0, strSQL.length()); // 清空
			strSQL.append(" Select * from " + docTableName + " Where " + getPrimaryFieldName(docTableName) + "='" + ds.Row(0).Column("FORMID").getString() + "'");
			dsResult = dbEngine.QuerySQL(strSQL.toString());
			ds = null;
		}
		return dsResult;
	}

	/**
	 * 功能:根据表名称获取关联字段的名称
	 * @param FORMTABLE 表名称
	 * @return String 关联字段名称
	 */
	private String getRelationField(String FORMTABLE) {
		StringBuffer sqlStr = new StringBuffer();
		DBSet mdbset;
		sqlStr.append("SELECT b.FIELDNAME FROM BPIP_TABLE a ,BPIP_FIELD b,COLL_DOC_CONFIG c  Where a.tablename='")
			  .append(FORMTABLE + "' and b.TABLEID=a.TABLEID and c.MAINTABLE=a.Tableid and b.FIELDID=c.OTHERFIELD");
		mdbset = dbEngine.QuerySQL(sqlStr.toString());
		if (mdbset != null) {
			String strReturn = mdbset.Row(0).Column("FIELDNAME").getString();
			mdbset = null;
			return strReturn;
		} else {
			return "";
		}
	}

	/**
	 * 功能:根据表名获取其主键字段名
	 * @param tableName
	 * @return String
	 */
	private String getPrimaryFieldName(String tableName) {
		String resultStr = null;
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT PRIMARYKEY FROM BPIP_TABLE WHERE TABLENAME='" + tableName + "'");
		DBSet ds = dbEngine.QuerySQL(sqlStr.toString());
		if (ds != null && ds.RowCount() > 0) {
			resultStr = ds.Row(0).Column("PRIMARYKEY").getString();
			ds = null;
		}
		return resultStr;
	}

	private String getTitleField(String TableName) throws Exception {
		String strTit = "";
		strTit = (String) getTitleFieldHashtable.get(TableName);
		if (strTit == null) {
			strTit = "";
			StringBuffer strSQL = new StringBuffer();
			if (TableName != null) {
				strSQL.append("Select TITLE From BPIP_TABLE Where TABLENAME='" + TableName + "'");
				List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
				if (retlist != null && retlist.size() > 0) {
					if (retlist.get(0) != null && retlist.get(0).get("TITLE") != null) {
						strTit = retlist.get(0).get("TITLE").toString();
					}
				}
			}
			if (strTit == null) {
				strTit = "";
			}
			strTit = strTit.trim();
			if (strTit.length() == 0) {
				strTit = "no";
			}
			getTitleFieldHashtable.put(TableName, strTit);
		}
		if (strTit.equals("no")) {
			strTit = "";
		}
		return strTit;
	}

	private void addJavascriptSrc(String stc, Html html) {
		ScriptTag st = new ScriptTag();
		st.setLanguage("Javascript");
		st.setType("text/javascript");
		st.setAttribute("src", stc);
		LabelTag lt = new LabelTag();
		lt.setText("</SCRIPT>");
		st.setEndTag(lt);
		NodeList nlHead = html.searchFor(new HeadTag().getClass(), true);
		HeadTag tagHead = (HeadTag) nlHead.elementAt(0);
		tagHead.getChildren().add(st);
	}

	/**
	 * 获取字典表的代码值
	 * @param TableName 字典表名称
	 * @param Code 代码
	 * @return String 代码值
	 * @throws Exception 
	 */
	private String getCodeValue(String TableName, String Code) throws Exception {
		String result = null;
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("Select NAME From " + TableName + " Where Code='").append(Code + "'");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
		if (retlist != null && retlist.size() > 0) {
			if (retlist.get(0) != null && retlist.get(0).get("NAME") != null) {
				result = retlist.get(0).get("NAME").toString();
			}
		}
		return result;
	}

	/**
	 * 获取单位名称
	 * @param UnitID
	 * @return String
	 * @throws Exception 
	 */
	private String getUnitName(String UnitID) throws Exception {
		String result = null;
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("Select UNITNAME From BPIP_UNIT Where UnitID='" + UnitID + "'");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
		if (retlist != null && retlist.size() > 0) {
			if (retlist.get(0) != null && retlist.get(0).get("UNITNAME") != null) {
				result = retlist.get(0).get("UNITNAME").toString();
			}
		}
		return result;
	}

	/**
	 * 获取用户名称
	 * @param UserID
	 * @return String
	 * @throws Exception 
	 */
	private String getUserName(String UserID) throws Exception {
		String result = null;
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("Select Name From BPIP_USER Where UserID='" + UserID).append("'");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
		if (retlist != null && retlist.size() > 0) {
			if (retlist.get(0) != null && retlist.get(0).get("Name") != null) {
				result = retlist.get(0).get("Name").toString();
			}
		}
		return result;
	}

	/**
	 * 获取采集文档配置信息
	 * @param id 采集文档配置编号
	 * @return 采集文档配置信息
	 */
	private CollectionInfo getCollectionInfo(String id) {
		CollectionInfo collectInfo = (CollectionInfo) getCollectionInfo.get(id);
		if (collectInfo == null) {
			StringBuffer strSQL = new StringBuffer();
			try {
				if (DataBaseType.equals("3")) {// mysql数据库
					strSQL.append("Select a.DOCTYPE,a.ID,a.DOCNAME,a.TEMPLAET,b.TABLENAME,c.FIELDNAME,c.FIELDNAME "
							+ "From COLL_DOC_CONFIG a Left Join BPIP_TABLE b On a.MAINTABLE=b.TABLEID Left Join BPIP_FIELD c On a.OTHERFIELD=c.FIELDID "
							+ "Where ID='" + id + "'");
				} else {
					strSQL.append("Select a.DOCTYPE,a.ID,a.DOCNAME,a.TEMPLAET,b.TABLENAME as MainTable,c.FIELDNAME as otherfield "
							+ "From COLL_DOC_CONFIG a Left Join BPIP_TABLE b On a.MAINTABLE=b.TABLEID Left Join BPIP_FIELD c On a.OTHERFIELD=c.FIELDID "
							+ "Where ID='" + id + "'");
				}
				List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
				if (retlist != null && retlist.size() > 0) {
					Map<String, Object> retmap = retlist.get(0);
					CollectionUtil.convertMapNullToStr(retmap);
					if (DataBaseType.equals("3")) {// mysql数据库
						retmap.put("MAINTABLE", retmap.get("TABLENAME").toString());
						retmap.put("OTHERFIELD", retmap.get("FIELDNAME").toString());
					}
					collectInfo = (CollectionInfo) ReflectionUtil.convertMapToBean(retmap, CollectionInfo.class);
					
					strSQL.delete(0, strSQL.length()); // 清空
					strSQL.append("Select PAGE,TEMPLAET From COLL_DOC_PRINT Where DOCID='").append(id + "'");
					List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(strSQL.toString());
					int length1 = retlist1 != null ? retlist1.size() : 0;
					if (length1 > 0) {
						for (int i = 0; i < length1; i++) {
							Map<String, Object> retmap1 = retlist1.get(i);
							collectInfo.addPrintTemplet(Integer.parseInt(retmap1.get("PAGE").toString()),
									retmap1.get("TEMPLAET").toString());
						}
					}
				}
			} catch (Exception ex) {
				LOGGER.error("读取采集配置信息", ex.toString());
			}
			if (collectInfo != null) {
				getCollectionInfo.put(id, collectInfo);
			}
		}
		return collectInfo;
	}

	private void checkDefault(ItemList itemlist) throws Exception {
		if (itemlist != null && itemlist.getItemCount() > 0) {
			String[] itemname = itemlist.getItemNameList();
			for (int i = 0; i < itemname.length; i++) {
				if (itemlist.getItem(itemname[i]).isCode()
						&& !(itemlist.getItem(itemname[i]).getValue() != null
								&& !itemlist.getItem(itemname[i]).getValue().equals(""))
						&& itemlist.getItem(itemname[i]).getDefaultValue() != null
						&& !itemlist.getItem(itemname[i]).getDefaultValue().equals("")) {
					itemlist.getItem(itemname[i])
							.setCodeValue(this.getCodeValue(itemlist.getItem(itemname[i]).getCodeTable(),
									itemlist.getItem(itemname[i]).getDefaultValue()));
				}
				if (itemlist.getItem(itemname[i]).isUnit()
						&& !(itemlist.getItem(itemname[i]).getValue() != null
								&& !itemlist.getItem(itemname[i]).getValue().equals(""))
						&& itemlist.getItem(itemname[i]).getDefaultValue() != null
						&& !itemlist.getItem(itemname[i]).getDefaultValue().equals("")) {
					itemlist.getItem(itemname[i])
							.setCodeValue(this.getUnitName(itemlist.getItem(itemname[i]).getDefaultValue()));
				}
				if (itemlist.getItem(itemname[i]).isUser()) {
					if (itemlist.getItem(itemname[i]).getValue() == null
							|| itemlist.getItem(itemname[i]).getValue().equals("")) {
						if (itemlist.getItem(itemname[i]).getDefaultValue() != null
								&& !itemlist.getItem(itemname[i]).getDefaultValue().equals("")) {
							itemlist.getItem(itemname[i])
									.setCodeValue(this.getUserName(itemlist.getItem(itemname[i]).getDefaultValue()));
						}
					}
				}
			}
		}
	}

	// ******************下列函数用于生成自动编号***********************//
	/**
	 * 设置自动编号（前缀字母＋系统自动编号）
	 * 
	 * @param dc
	 *            dbColumn 字段
	 */
	private void set_CASE_NUMBER(DBColumn dc) {
		String strValue = "";
		String strNum = "";
		try {
			strNum = myGetRandom(4);
		} catch (Exception ex) {
			strNum = "0";
		}
		strValue = dc.getAuto1().trim() + GlobalID() + strNum;

		if (strValue.length() > 18) {
			strValue = strValue.substring(0, 18);
		}
		dc.setValue(strValue);
	}

	private String myGetRandom(int i) {
		Random s = new Random();
		if (i == 0) {
			return "";
		}
		String revalue = "";
		for (int k = 0; k < i; k++) {
			revalue = revalue + s.nextInt(9);
		}
		return revalue;
	}

	private String GlobalID() {
		String strResult = "";
		java.util.Date dt = new java.util.Date();
		long lg = dt.getTime();
		Long ld = new Long(lg);
		strResult = ld.toString();
		return strResult;
	}

	/**
	 * 设置自动编号（数字型无条件）
	 * @param TableName
	 * @param dc dbColumn
	 */
	private void set_ID_INT(String TableName, DBColumn dc) {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("Select max(" + dc.getName() + ") as maxid From ").append(TableName);
		DBSet dsID = dbEngine.QuerySQL(strSQL.toString());
		int iID = 1;
		if (dsID != null) {
			if (dsID.RowCount() > 0) {
				iID = dsID.Row(0).Column("maxid").getInteger();
				iID++;
			}
			dsID = null;
		}
		dc.setValue(Integer.toString(iID));
	}

	/**
	 * 设置数字型自动编号（条件:行政区划）
	 * @param TableName 表名
	 * @param dc 字段
	 * @param userinfo 用户信息
	 */
	private void set_ID_INT_1(DBRow dr, DBColumn dc) {
		String mCondition = "";
		StringBuffer strSQL = new StringBuffer();
		StringBuffer addBuf = new StringBuffer();
		if (dr.Column(dc.getAuto1()).getType().getValue() == DBType.STRING.getValue()) {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto1() + "='" + dr.Column(dc.getAuto1()).getString() + "'");
			mCondition = addBuf.toString();
		} else {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto1() + "=" + Integer.toString(dr.Column(dc.getAuto1()).getInteger()));
			mCondition = addBuf.toString();
		}
		strSQL.append("Select max("+dc.getName()+") as maxid From "+dr.getTableName()+" Where "+mCondition);
		DBSet dsID = dbEngine.QuerySQL(strSQL.toString());
		int iID = 1;
		if (dsID != null) {
			if (dsID.RowCount() > 0) {
				iID = dsID.Row(0).Column("maxid").getInteger();
				iID++;
			}
			dsID = null;
		}
		dc.setValue(Integer.toString(iID));
	}

	/**
	 * 设置数字型自动编号（条件:单位编号）
	 * @param TableName 表名
	 * @param dc 字段
	 * @param userinfo 用户信息
	 */
	private void set_ID_INT_2(DBRow dr, DBColumn dc) {
		String mCondition1 = "";
		String mCondition2 = "";
		StringBuffer strSQL = new StringBuffer();
		StringBuffer addBuf = new StringBuffer();
		if (dr.Column(dc.getAuto1()).getType().getValue() == DBType.STRING.getValue()) {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto1() + "='" + dr.Column(dc.getAuto1()).getString() + "'");
			mCondition1 = addBuf.toString();
		} else {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto1() + "=" + Integer.toString(dr.Column(dc.getAuto1()).getInteger()));
			mCondition1 = addBuf.toString();
		}
		if (dr.Column(dc.getAuto2()).getType().getValue() == DBType.STRING.getValue()) {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto2() + "='" + dr.Column(dc.getAuto2()).getString() + "'");
			mCondition2 = addBuf.toString();
		} else {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto2() + "=" + Integer.toString(dr.Column(dc.getAuto2()).getInteger()));
			mCondition2 = addBuf.toString();
		}
		strSQL.append("Select max(" + dc.getName() + ") as maxid From ")
				.append(dr.getTableName() + " Where " + mCondition1);
		if (mCondition2.length() > 0) {
			strSQL.append(" And " + mCondition2);
		}
		DBSet dsID = dbEngine.QuerySQL(strSQL.toString());
		int iID = 1;
		if (dsID != null) {
			if (dsID.RowCount() > 0) {
				iID = dsID.Row(0).Column("maxid").getInteger();
				iID++;
			}
			dsID = null;
		}
		dc.setValue(Integer.toString(iID));
	}

	/**
	 * 设置数字型自动编号（条件:当前年份）
	 * @param TableName 表名
	 * @param dc 字段
	 */
	private void set_ID_INT_3(DBRow dr, DBColumn dc) {
		String mCondition1 = "";
		String mCondition2 = "";
		String mCondition3 = "";
		StringBuffer strSQL = new StringBuffer();
		StringBuffer addBuf = new StringBuffer();
		if (dr.Column(dc.getAuto1()).getType().getValue() == DBType.STRING.getValue()) {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto1() + "='" + dr.Column(dc.getAuto1()).getString() + "'");
			mCondition1 = addBuf.toString();
		} else {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto1() + "=" + Integer.toString(dr.Column(dc.getAuto1()).getInteger()));
			mCondition1 = addBuf.toString();
		}
		if (dr.Column(dc.getAuto2()).getType().getValue() == DBType.STRING.getValue()) {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto2() + "='" + dr.Column(dc.getAuto2()).getString() + "'");
			mCondition2 = addBuf.toString();
		} else {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto2() + "=" + Integer.toString(dr.Column(dc.getAuto2()).getInteger()));
			mCondition2 = addBuf.toString();
		}
		if (dr.Column(dc.getAuto3()).getType().getValue() == DBType.STRING.getValue()) {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto3() + "='" + dr.Column(dc.getAuto3()).getString() + "'");
			mCondition2 = addBuf.toString();
		} else {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto3() + "=" + Integer.toString(dr.Column(dc.getAuto3()).getInteger()));
			mCondition3 = addBuf.toString();
		}
		strSQL.append("Select max(" + dc.getName() + ") as maxid From ")
				.append(dr.getTableName() + " Where " + mCondition1);

		if (mCondition2.length() > 0) {
			strSQL.append(" And " + mCondition2);
		}
		if (mCondition3.length() > 0) {
			strSQL.append(" And " + mCondition3);
		}
		DBSet dsID = dbEngine.QuerySQL(strSQL.toString());
		int iID = 1;
		if (dsID != null) {
			if (dsID.RowCount() > 0) {
				iID = dsID.Row(0).Column("maxid").getInteger();
				iID++;
			}
			dsID = null;
		}
		dc.setValue(Integer.toString(iID));
	}

	private String getFristID(int length) {
		String result = "1";
		for (int i = 0; i < length - 1; i++) {
			result = "0" + result;
		}
		return result;
	}

	/**
	 * 设置文本型自动编号（无条件）
	 * @param TableName
	 * @param dc dbColumn
	 */
	private void set_ID_STR(String TableName, DBColumn dc) {
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("Select max(" + dc.getName() + ") as maxid From ").append(TableName);
		DBSet dsID = dbEngine.QuerySQL(strSQL.toString());
		String sID = getFristID(dc.getLength());
		if (dsID != null) {
			if (dsID.RowCount() > 0) {
				sID = dsID.Row(0).Column("maxid").getString();
				StringWork sw = new StringWork();
				// sw.NewID(sID); 原来的程序
				sID = sw.NewID(sID);
			}
			dsID = null;
		}
		if (sID.length() == 0 || sID.equals("null")) {
			sID = "1";
		}
		// ---------------------
		LOGGER.info("文本型自动编号（无条件）sID=" + sID);
		dc.setValue(sID);
	}

	/**
	 * 设置文本型自动编号（条件:行政区划）
	 * @param TableName
	 * @param dc dbColumn
	 */
	private void set_ID_STR_1(DBRow dr, DBColumn dc) {
		String mCondition = "";
		StringBuffer addBuf = new StringBuffer();
		StringBuffer strSQL = new StringBuffer();
		if (dr.Column(dc.getAuto1()).getType().getValue() == DBType.STRING.getValue()) {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto1() + "='" + dr.Column(dc.getAuto1()).getString() + "'");
			mCondition = addBuf.toString();
		} else {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto1() + "=" + Integer.toString(dr.Column(dc.getAuto1()).getInteger()));
			mCondition = addBuf.toString();
		}
		strSQL.append("Select max(" + dc.getName() + ") as maxid From ")
				.append(dr.getTableName() + " Where " + mCondition);
		DBSet dsID = dbEngine.QuerySQL(strSQL.toString());
		String sID = getFristID(dc.getLength());
		if (dsID != null) {
			if (dsID.RowCount() > 0) {
				sID = dsID.Row(0).Column("maxid").getString();
				StringWork sw = new StringWork();
				// sw.NewID(sID); 原来的程序
				sID = sw.NewID(sID);
			}
			dsID = null;
		}
		if (sID.length() == 0 || sID.equals("null")) {
			sID = "1";
		}
		// ---------------------
		dc.setValue(sID);
	}

	/**
	 * 设置文本型自动编号（条件:单位编号）
	 * @param TableName
	 * @param dc dbColumn
	 */
	private void set_ID_STR_2(DBRow dr, DBColumn dc) {
		String mCondition1 = "";
		String mCondition2 = "";
		StringBuffer addBuf = new StringBuffer();
		StringBuffer strSQL = new StringBuffer();
		if (dr.Column(dc.getAuto1()).getType().getValue() == DBType.STRING.getValue()) {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto1() + "='" + dr.Column(dc.getAuto1()).getString() + "'");
			mCondition1 = addBuf.toString();
		} else {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto1() + "=" + Integer.toString(dr.Column(dc.getAuto1()).getInteger()));
			mCondition1 = addBuf.toString();
		}
		if (dr.Column(dc.getAuto2()).getType().getValue() == DBType.STRING.getValue()) {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto2() + "='" + dr.Column(dc.getAuto2()).getString() + "'");
			mCondition2 = addBuf.toString();
		} else {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto2() + "=" + Integer.toString(dr.Column(dc.getAuto2()).getInteger()));
			mCondition2 = addBuf.toString();
		}
		strSQL.append("Select max(" + dc.getName() + ") as maxid From ")
				.append(dr.getTableName() + " Where " + mCondition1);
		if (mCondition2.length() > 0) {
			strSQL.append("  And " + mCondition2);
		}
		DBSet dsID = dbEngine.QuerySQL(strSQL.toString());
		String sID = getFristID(dc.getLength());
		if (dsID != null) {
			if (dsID.RowCount() > 0) {
				sID = dsID.Row(0).Column("maxid").getString();
				StringWork sw = new StringWork();
				// sw.NewID(sID); 原来的程序
				sID = sw.NewID(sID);
			}
			dsID = null;
		}
		if (sID.length() == 0 || sID.equals("null")) {
			sID = "1";
		}
		// ---------------------
		dc.setValue(sID);
	}

	/**
	 * 设置文本型自动编号（条件:当前年份）
	 * @param TableName
	 * @param dc dbColumn
	 */
	private void set_ID_STR_3(DBRow dr, DBColumn dc) {
		String mCondition1 = "";
		String mCondition2 = "";
		String mCondition3 = "";
		StringBuffer addBuf = new StringBuffer();
		StringBuffer strSQL = new StringBuffer();
		if (dr.Column(dc.getAuto1()).getType().getValue() == DBType.STRING.getValue()) {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto1() + "='" + dr.Column(dc.getAuto1()).getString() + "'");
			mCondition1 = addBuf.toString();
		} else {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto1() + "=" + Integer.toString(dr.Column(dc.getAuto1()).getInteger()));
			mCondition1 = addBuf.toString();
		}
		if (dr.Column(dc.getAuto2()).getType().getValue() == DBType.STRING.getValue()) {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto2() + "='" + dr.Column(dc.getAuto2()).getString() + "'");
			mCondition2 = addBuf.toString();
		} else {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto2() + "=" + Integer.toString(dr.Column(dc.getAuto2()).getInteger()));
			mCondition2 = addBuf.toString();
		}
		if (dr.Column(dc.getAuto3()).getType().getValue() == DBType.STRING.getValue()) {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto3() + "='" + dr.Column(dc.getAuto3()).getString() + "'");
			mCondition3 = addBuf.toString();
		} else {
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(dc.getAuto3() + "=" + Integer.toString(dr.Column(dc.getAuto3()).getInteger()));
			mCondition3 = addBuf.toString();
		}
		strSQL.append("Select max(" + dc.getName() + ") as maxid From ");
		strSQL.append(dr.getTableName() + " Where  " + mCondition1);
		if (mCondition2.length() > 0) {
			strSQL.append(" And " + mCondition2);
		}
		if (mCondition3.length() > 0) {
			strSQL.append(" And " + mCondition3);
		}
		// ---------------------------------------
		DBSet dsID = dbEngine.QuerySQL(strSQL.toString());
		String sID = getFristID(dc.getLength());
		if (dsID != null) {
			if (dsID.RowCount() > 0) {
				sID = dsID.Row(0).Column("maxid").getString();
				StringWork sw = new StringWork();
				// sw.NewID(sID); 原来的程序
				sID = sw.NewID(sID);
			}
			dsID = null;
		}
		if (sID.length() == 0 || sID.equals("null")) {
			sID = "1";
		}
		// ---------------------
		dc.setValue(sID);
	}
	// *********************自动编号生成函数结束*********************//
	
	class ScanTask extends TimerTask {
		public void run() {
			LOGGER.info("扫描任务开始！");
			doMemory_Manage();
		}
	}

	public void inithashtable() {
		CodeHashtable.clear();
		getFlowProprty.clear();
		getFlowProprty1.clear();
		getTitleFieldHashtable.clear();
		getCollectionInfo.clear();
		MoreSelectHashtable.clear();

		dbEngine.inithashtable();
	}

	private void doMemory_Manage() {
		LOGGER.info("内存清理开始。");
		inithashtable();
		LOGGER.info("内存清理清结束。");
	}

}