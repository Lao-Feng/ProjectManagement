package zr.zrpower.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.*;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.collectionengine.*;
import zr.zrpower.collectionengine.html.*;
import zr.zrpower.common.db.*;
import zr.zrpower.common.remark.PrintJs;
import zr.zrpower.common.remark.RemarkVb;
import zr.zrpower.common.util.*;
import zr.zrpower.dao.UserMapper;
import zr.zrpower.model.SessionUser;
import zr.zrpower.service.CollectionService;
import zr.zrpower.service.ZRCollenHtmlUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CollectionServiceImpl implements CollectionService {
	/** The ConfigServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionServiceImpl.class);
	private DBEngine dbEngine;// 数据库引擎
	private static int clients = 0;
	static String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	// 系统日志路径
	String logPath = SysPreperty.getProperty().LogFilePath;
	// 文件上传路径
	String upload_path = logPath.substring(0, logPath.length() - 3);
	
	static Map<String, Object> CodeHashtable = new HashMap<String, Object>();// 根据表名得到列表
	static Map<String, Object> getFlowProprty = new HashMap<String, Object>();// 填充流程属性
	static Map<String, Object> getFlowProprty1 = new HashMap<String, Object>();// 填充流程属性
	static Map<String, Object> getTitleFieldHashtable = new HashMap<String, Object>();// 填充流程属性
	
	static Map<String, Object> getCollectionInfo = new HashMap<String, Object>();
	static Map<String, Object> MoreSelectHashtable = new HashMap<String, Object>();
	static Map<String, Object> DocIDHashtable = new HashMap<String, Object>();
	
	private Timer timerListener;// 队列侦听器

	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;

	/**
	 * 构造方法
	 */
	public CollectionServiceImpl() {
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

	class ScanTask extends TimerTask {
		public void run() {
			LOGGER.info("扫描任务开始！");
			doMemory_Manage();
		}
	}
	
	/**
	 * ftl  调整（表单提交）
	 */
	@Override
	public String saveOrUpdate(Request request, SessionUser userInfo) throws Exception {
		LOGGER.info("调用表单引擎保存数据开始...");
		String strID ="-1";
		String IfPho = "0";// 判断客户端为手机或者电脑 1--表示手机
		String mID = null; // 配置编号（电脑）
		String mIDPho = null; // 配置编号(手机)
		String mPKID = null; // 主键编号
		String mAID = null; // 流程活动编号
		String mAction = null; // 执行动作
		StringBuffer strSQL = new StringBuffer();
		try {
			mPKID = request.getStringItem("COLL_PKID");
			mAID = request.getStringItem("COLL_AID");
			mAction = request.getStringItem("COLL_Action");
			if (mAction == null) {
				mAction = "read";
			}
			if (mAID == null) {
				return ErrorHtml("非法调用错误！");
			} else {
				if (mAID.length() == 10) {// 表示流程活动
					strSQL.append("Select b.DocID,b.DocIDPHO From FLOW_CONFIG_ACTIVITY a Left Join FLOW_CONFIG_PROCESS b on b.ID=a.FID "
							+ "Where a.ID='" + mAID + "'");
					List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
					if (retlist != null && retlist.size() > 0) {
						if (retlist.get(0) != null) {
							mID = retlist.get(0).get("DocID") != null ? retlist.get(0).get("DocID").toString() : "";
							mIDPho = retlist.get(0).get("DocIDPHO") != null ? retlist.get(0).get("DocIDPHO").toString() : "";
						}
					}
				} else {// 自定义表单
					mID = mAID;
					mIDPho = mAID;
				}
			}
			if (mID == null) {
				return strID;
			}
			IfPho = request.getStringItem("IfPho");
			if ("1".equals(IfPho)) {
				mID = mIDPho;
			}
			CollectionInfo cInfo = getCollectionInfo(mID);
			if (cInfo == null) {
				return strID;
			}
			if (mAction != null && !mAction.equals("read")) {
				if (mAction.equals("add") && mPKID != null && !mPKID.equals("")) {
					mAction = "edit";
				}
				strID = updateTable(mAction, cInfo, request, userInfo);
				if (!strID.equalsIgnoreCase("-1")) {
					mPKID = strID;
				} else {
					return strID;
				}
			}
		}catch (Exception ex1) {
			return strID;
		}			
		LOGGER.info("调用表单引擎保存数据结束...");
		return strID;
	}

	/**
	 * ftl  调整（表单新增或编辑展示）
	 */
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
		boolean bReadOnly = false; // 调用是否只读模式 false可读写模式，true只读模式
		boolean bAllEdit = false;  // 是否所有字段都可编辑 false否，true是
		
		boolean isBase = true; // 是否是基础数据
		StringBuffer strSQL = new StringBuffer();
		StringBuffer addBuf = new StringBuffer();
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
					strSQL.append("Select b.DocID,b.DocIDPHO From FLOW_CONFIG_ACTIVITY a Left Join FLOW_CONFIG_PROCESS b on b.ID=a.FID "
							+ "Where a.ID='" + mAID + "'");
					List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
					if (retlist != null && retlist.size() > 0) {
						if (retlist.get(0) != null) {
							mID = retlist.get(0).get("DocID") != null ? retlist.get(0).get("DocID").toString() : "";
							mIDPho = retlist.get(0).get("DocIDPHO") != null ? retlist.get(0).get("DocIDPHO").toString() : "";
						}
					}
				} else {// 自定义表单
					mID = mAID;
					mIDPho = mAID;
				}
			}
			if (mID == null) {
				return ErrorHtml("自定义表单引擎发生异常：<br>流程配置与表单配置错误，未找到相关配置信息。");
			}
			IfPho = request.getStringItem("IfPho");
			if ("1".equals(IfPho)) {
				mID = mIDPho;
			}
			CollectionInfo cInfo = getCollectionInfo(mID);
			if (cInfo == null) {
				return ErrorHtml("自定义表单引擎发生错误，表单配置信息未找到！");
			} else {
				if (cInfo.getDOCTYPE() != null && cInfo.getDOCTYPE().equals("1")) {
					isBase = false;
				}
			}
			if (mAction != null && !mAction.equals("read")) {
				if (mAction.equals("add") && mPKID != null && !mPKID.equals("")) {
					mAction = "edit";
				}
			}
			addBuf.append(SysPreperty.getProperty().ShowTempletPath + "/").append(cInfo.getTEMPLAET());
			templat = addBuf.toString();
			addBuf.delete(0, addBuf.length()); // 清空
			// 读取模版,装载主表
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
			
			Parser parser = new Parser();
			Html html = null;
			try {
				parser.setURL(templat);
				parser.setEncoding("UTF-8");
//				parser.setEncoding("GBK");
				html = (Html) (parser.elements().nextNode());
			} catch (ParserException ex) {
				return ErrorHtml("自定义表单引擎发生错误，读取采集模版失败！<br>详细错误信息：<br>" + ex.toString());
			}
			// 解析模版
			if (html != null) {
				NodeList nlForm = html.searchFor(new FormTag().getClass(), true);
				FormTag ftForm = (FormTag) nlForm.elementAt(0);
				ftForm.setTagName("el-form");
				ftForm.setAttribute("name", "frmColl");
				ftForm.setAttribute("id", "frmColl");
				ftForm.setAttribute(":model", "\"form\"");
				ftForm.setAttribute(":rules", "\"rules\"");
				ftForm.setAttribute("ref", "\"form\"");
				ftForm.setAttribute(":inline", "\"true\"");
				ftForm.setAttribute("style", "\"padding:0px;margin: 0px;\"");
				ftForm.removeAttribute("enctype");
				Tag end = ftForm.getEndTag();
				if(end == null) {
					ftForm.setEmptyXmlTag(false);
				}else if(end!=null) {
					end.setTagName("/el-form");
					ftForm.setEndTag(end);
				}
				Vector<Object> vNotVisible = new Vector<Object>();
				Vector<Object> vUnit = new Vector<Object>();
				Hashtable<String, Object> vDate = new Hashtable<String, Object>();
				// 控件解析
				ControlSet ctrlSet = new ControlSet();
				HiddenControlSet hiddSet = new HiddenControlSet();
				String[] saItem = itemList.getItemNameList();
				
				NodeList lt = html.searchFor(new InputTag().getClass(), true);
				String type = "";
				String name = "";
				//form 验证vue
				List<String> validateList = new ArrayList<>();
				//form 控件初始化
				List<String> formList = new ArrayList<>();
				//form 是数组的字段
				List<String> arrayList = new ArrayList<>();
				//form 图片字段
				List<String> imageList = new ArrayList<>();
				//form 图片字段byte转换为base64字符串
				List<Map<String,Object>> imageUrl = new ArrayList<Map<String,Object>>();
				for (int index = 0; index < lt.size(); index++) {
					type = ((InputTag) (lt.elementAt(index))).getAttribute("type");
					name = ((InputTag) (lt.elementAt(index))).getAttribute("name");
					if (type == null || type.equals("")) {
						type = "TEXT";
					}
					ItemField item;
					//单选框
					if (type.equalsIgnoreCase("radio")) {
						for (int i = 0; i < saItem.length; i++) {
							if (saItem[i].equalsIgnoreCase(name)) {
								InputTag inputTag = (InputTag) (lt.elementAt(index));
								item = itemList.getItem(saItem[i]);
								if (!item.isWrite()) {
									inputTag.setAttribute("disabled", "true");
								}
								if (inputTag.getAttribute("value").equals(item.getValue())) {
									inputTag.setAttribute("checked", "true");
								}
//								validateList.add(tc.getValidateJS());
								break;
							}
						}
					}
					// 普通文本
					if (type.toUpperCase().equalsIgnoreCase("TEXT")) {
						for (int i = 0; i < saItem.length; i++) {
							if (saItem[i].equalsIgnoreCase(name)) {
								item = itemList.getItem(saItem[i]);
								// 整数
								if (item.getType().getValue() == DBType.LONG.getValue()) {
									InputTag inputTag = (InputTag) (lt.elementAt(index));
									NumberControl tc = new NumberControl(inputTag);
									tc.setItemField(item);
									ctrlSet.addControl(tc);
									validateList.add(tc.getValidateJS());
									formList.add(item.getName()+":"+tc.getInitVal()+",");
								// 文本型
								}else if (item.getType().getValue() == DBType.STRING.getValue()) {// 是字典表
									if (item.isCode()) {// Redio型字典元素
										if (item.getCodeInput() == 1) {
											RedioControl rc = new RedioControl((InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
											rc.setItemField(item);
											ctrlSet.addControl(rc);
											validateList.add(rc.getValidateJS());
											formList.add(item.getName()+":"+rc.getInitVal()+",");
										}else if (item.getCodeInput() == 2) {// 下拉型字典元素
											SelectControl dc = new SelectControl((InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
											dc.setItemField(item);
											ctrlSet.addControl(dc);
											validateList.add(dc.getValidateJS());
											formList.add(item.getName()+":"+dc.getInitVal()+",");
										}else if (item.getCodeInput() == 3) {// 字典控件型字典元素 == 转换下拉框
											SelectControl dc = new SelectControl((InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
											dc.setItemField(item);
											ctrlSet.addControl(dc);
											validateList.add(dc.getValidateJS());
											formList.add(item.getName()+":"+dc.getInitVal()+",");
										}else {// 多选控件
											checkboxControl rc = new checkboxControl((InputTag) (lt.elementAt(index)),getDictList(item.getCodeTable()));
											rc.setItemField(item);
											ctrlSet.addControl(rc);
											validateList.add(rc.getValidateJS());
											formList.add(item.getName()+":"+rc.getInitVal()+",");
											arrayList.add(item.getName());
										}
									}else if (item.isUser()) {// 用户列表元素
										SelectControl sc = null;
										if (item.getValue() != null) {
											sc = new SelectControl((InputTag) (lt.elementAt(index)),
													this.getUserArrary(getUserUnitID(item.getValue())));
										} else {
											sc = new SelectControl((InputTag) (lt.elementAt(index)),
													getUserArrary(userInfo.getUnitID()));
										}
										item.setCodeValue(getUserName(item.getValue()));
										sc.setItemField(item);
										ctrlSet.addControl(sc);
										validateList.add(sc.getValidateJS());
										formList.add(item.getName()+":"+sc.getInitVal()+",");
									}else if (item.isUnit()) {// 单位列表元素
										SelectControl sc = null;
										if (!item.isWrite()) {
											try {
												sc = new SelectControl((InputTag) (lt.elementAt(index)),getDictList("BPIP_UNIT"));
											} catch (Exception ex2) {
												sc = new SelectControl((InputTag) (lt.elementAt(index)),getDictList("BPIP_UNIT"));
												item.setDefaultValue(userInfo.getUnitID());
											}
										} else {
											sc = new SelectControl((InputTag) (lt.elementAt(index)),getDictList("BPIP_UNIT"));
										}
										item.setCodeValue(getUnitName(item.getValue()));
										sc.setItemField(item);
										ctrlSet.addControl(sc);
										validateList.add(sc.getValidateJS());
										formList.add(item.getName()+":"+sc.getInitVal()+",");
										vUnit.add(item);
									}else {// 普通文本型元素
										InputTag inputTag = (InputTag) (lt.elementAt(index));
										TextControl tc = new TextControl(inputTag);
										tc.setItemField(item);
										ctrlSet.addControl(tc);
										validateList.add(tc.getValidateJS());
										formList.add(item.getName()+":"+tc.getInitVal()+",");
									}
								}else if (item.getType().getValue() == DBType.FLOAT.getValue()) {// 浮点型元素
									FloatControl tc = new FloatControl((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									ctrlSet.addControl(tc);
									validateList.add(tc.getValidateJS());
									formList.add(item.getName()+":"+tc.getInitVal()+",");
								}else if (item.getType().getValue() == DBType.DATE.getValue()) {// 日期型元素
									DateControl tc = new DateControl((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									String tmpOnchange = tc.getOnchange();
									if (tmpOnchange != null && tmpOnchange.length() > 0) {
										vDate.put(name, tmpOnchange);
									}
									ctrlSet.addControl(tc);
									validateList.add(tc.getValidateJS());
									formList.add(item.getName()+":"+tc.getInitVal()+",");
								}else if (item.getType().getValue() == DBType.DATETIME.getValue()) {// 日期时间型元素
									DateTimeControl tc = new DateTimeControl((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									String tmpOnchange = tc.getOnchange();
									if (tmpOnchange != null && tmpOnchange.length() > 0) {
										vDate.put(name, tmpOnchange);
									}
									ctrlSet.addControl(tc);
									validateList.add(tc.getValidateJS());
									formList.add(item.getName()+":"+tc.getInitVal()+",");
								}
								//隐藏字段
								if (item.isWrite() == false && item.isShow() == false) {
									String value = "";
									if (item.getValue() != null) {
										value = item.getValue()!= null ? item.getValue().trim() : "";
										formList.add(item.getName()+":\""+value+"\",");
										hiddSet.removePageHidden((InputTag) (lt.elementAt(index)));
									} else if (item.getDefaultValue() != null) {
										value = item.getDefaultValue()!= null ? item.getDefaultValue().trim() : "";
										formList.add(item.getName()+":\""+value+"\",");
										hiddSet.removePageHidden((InputTag) (lt.elementAt(index)));
									}
								}
							}
						}
					//上传控件	
					} else if (type.toUpperCase().equalsIgnoreCase("FILE")) {
					//隐藏字段	
					} else if (type.toUpperCase().equalsIgnoreCase("HIDDEN")) {
						boolean inItem = false;
						for (int i = 0; i < saItem.length; i++) {
							if (saItem[i].equalsIgnoreCase(name)) {//html和表对应的隐藏字段
								inItem = true;
								ItemField hiddin = itemList.getItem(saItem[i]);
								String value = hiddin.getShowValue()!= null ? hiddin.getShowValue().trim() : "";
								formList.add(hiddin.getName()+":\""+value+"\",");
								hiddSet.removePageHidden((InputTag) (lt.elementAt(index)));
							}
						}
						if (!inItem) {//html存在，数据库不存在
							InputTag hiddin =(InputTag) (lt.elementAt(index));
							String value =hiddin.getAttribute("value");
							formList.add(name+":\""+value+"\",");
							hiddSet.removePageHidden((InputTag) (lt.elementAt(index)));
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
							TextAreaControl tc = new TextAreaControl((TextareaTag) (ta.elementAt(index)));
							tc.setItemField(itemList.getItem(saItem[i]));
							ctrlSet.addControl(tc);
							validateList.add(tc.getValidateJS());
							formList.add(itemList.getItem(saItem[i]).getName()+":"+tc.getInitVal()+",");
//							hiddSet.removePageHidden((InputTag) (lt.elementAt(index)));
						}
					}
				}
				// 图片标签
				NodeList nlImage = html.searchFor(new ImageTag().getClass(), true);
				for (int index = 0; index < nlImage.size(); index++) {
					name = ((ImageTag) nlImage.elementAt(index)).getAttribute("name");
					for (int i = 0; i < saItem.length; i++) {
						if (saItem[i].equalsIgnoreCase(name)) {
							ImageTag imgTag = ((ImageTag) nlImage.elementAt(index));
							Node n = imgTag.getParent();
							NodeList tag = new NodeList();
//							addBuf.append("/photoengine?coll_id=" + mID + "&coll_pkid=" + mPKID + "&coll_img=" + name);
							imgTag.setTagName("el-upload");
							imgTag.setAttribute("class", "avatar-uploader");
							imgTag.setAttribute("action", "\"\"");//
							imgTag.setAttribute(":http-request", "\"ZRcollenRequest\"");
							imgTag.setAttribute(":show-file-list", "\"false\"");
							tag.add(imgTag);
														
							ImageTag img = new ImageTag();
							img.setTagName("img");
							img.setAttribute("v-if", "\"image."+name+"\"");
							img.setAttribute(":src", "\"image."+name+"\"");
							img.setAttribute("class", "avatar");
							tag.add(img);
							
							LabelTag lti = new LabelTag();
							lti.setTagName("i");
							lti.setAttribute("class", "el-icon-plus avatar-uploader-icon");
							lti.setAttribute("v-else",null);
							tag.add(lti);
							
							LabelTag lei = new LabelTag();
							lei.setText("</i>");
							tag.add(lei);
							
							LabelTag imgEnd= new LabelTag();
							imgEnd.setText("</el-upload>");
							tag.add(imgEnd);
							
							n.getChildren().removeAll();
							n.getChildren().add(tag);
							
							//设置图片控件及显示 
							imageList.add(name);
							byte[] mImage = getCollImage(mID,mPKID,name);
							Map<String,Object> image = new HashMap<String, Object>();
							if(mImage!=null) {
								image.put("field", name);
								image.put("url", Base64.encodeBase64String(mImage));
							}else {
								//读取默认图片
								
								String webapp = upload_path+"/Templet/NoImage.jpg";
								try {
									File file = new File(webapp);
									if (file.exists()) {
										byte[] fileByte = Files.readAllBytes(file.toPath());
										image.put("field", name);
										image.put("url", Base64.encodeBase64String(fileByte));
						            }
						        } catch (IOException e) {
						            e.printStackTrace();
						        }							
							}
							imageUrl.add(image);
							break;
						}
					}
				}
				// vue引用--------------------------------开始--------
				addCssLinkSrc("/static/vue/element-ui/lib/theme-chalk/index.css", html);
				addCssLinkSrc("/static/vue/element-ui/lib/theme-chalk/html.css", html);
				addJavascriptSrc("/static/vue/jquery-1_15.9.1.min.js", html);
				addJavascriptSrc("/static/vue/vue.min.js", html);
				addJavascriptSrc("/static/vue/element-ui/lib/index.js", html);
				addJavascriptSrc("/static/vue/ZRcollen.js", html);
				addJavascriptSrc("/static/vue/ZRvalidate.js", html);
				
				addJavascriptSrc("/static/ZrCollEngine/Res/coll.js", html);
				// vue引用--------------------------------结束--------
				

				if (IfPho != null && !IfPho.equals("")) {
					formList.add(IfPho+":\""+IfPho+"\",");
				}
				if (mMode != null && !mMode.equals("")) {
					formList.add("COLL_MODE:\""+mMode+"\",");
				}
				if (mPKID != null && !mPKID.equals("")) {
					formList.add("COLL_PKID:\""+mPKID+"\",");
				}else {
					formList.add("COLL_PKID:\"\",");
				}
				if (mFKID != null && !mFKID.equals("")) {
					formList.add("COLL_FKID:\""+mFKID+"\",");
				}
				if (mAID != null && !mAID.equals("")) {
					formList.add("COLL_AID:\""+mAID+"\",");
				}
				if (mAction != null && !mAction.equals("")) {
					if (mAction.equals("read")) {
						mAction = "add";
					} else if (mAction.equals("add")) {
						mAction = "edit";
					}
					formList.add("COLL_Action:\""+mAction+"\",");
				}
				formList.add("ISREVALUE:\"1\",");
				
				NodeList nlTitle = html.searchFor(new TitleTag().getClass(), true);
				TitleTag tt = (TitleTag) nlTitle.elementAt(0);
				NodeList nTitle = new NodeList();
				nTitle.add(new TextNode(cInfo.getDOCNAME()));
				tt.setChildren(nTitle);
				NodeList ltt = html.searchFor(new BodyTag().getClass(), true);
				BodyTag n = (BodyTag) ltt.elementAt(0);
				n.setAttribute("class", "ContentBody");
				n.getChild(0).setText("\n<div id =\"zrcollen\" v-loading=\"htmlLoading\">\n");
				
				JavaScriptTag jBody = new JavaScriptTag();
				// 如果有折叠区域
				if (vNotVisible.size() > 0) {
					for (int k = 0; k < vNotVisible.size(); k++) {
						addBuf.delete(0, addBuf.length()); // 清空
						addBuf.append(" VisiableFalse('" + vNotVisible.get(k) + "');");
						jBody.addFunction(addBuf.toString());
					}
				}
				// 添加主要公共vue注册=============
				
				// 判断是否增加多级联动-----
//				String NDJS = getNDJS(cInfo.getMAINTABLE());
				JavaScriptTag jst1 = new JavaScriptTag();
//				jst1.addFunction(NDJS);
//				jst1.addToBoay(html);
				// --------------------------
				// 组装 （弹窗、div）
				NodeList formDiv = html.searchFor(new BodyTag().getClass(), true);
				BodyTag div = (BodyTag) formDiv.elementAt(0);
				BodyTag tg = new BodyTag();
				tg.setText("<div id=\"win\">");
				
				TextNode dilogDiv = new TextNode("");
				Map<String,Object> dialog = new HashMap<>();
				String divUserOrUnit = ZRCollenHtmlUtils.dialogCode(dialog);
				dilogDiv.setText(divUserOrUnit);
				
				LabelTag endDiv = new LabelTag();
				endDiv.setText("</div> \n\n\n");
				tg.setEndTag(endDiv);
				
				div.getChildren().add(tg);
				div.getChildren().add(dilogDiv);
				div.getChildren().add(endDiv);
				jBody.addToBoay(html);

				// 添加主要公共vue注册=============调用模板生成html
				Map<String,Object> script = new HashMap<>();
				script.put("rules", validateList);
				script.put("forms", formList);
				script.put("lists", arrayList);//数组字段
				script.put("images", imageList);//图片控件字段
				script.put("imagesurl", imageUrl);//图片控件显示值list
				String temscripts = ZRCollenHtmlUtils.scriptCode(script)+"\n";
				jst1.addFunction(temscripts);
				jst1.addToBoay(html);

			}
			String sReturn = html.toHtml(true);
			html = null;
			parser = null;
			
			return sReturn;
		} catch (Exception ex1) {
			ex1.printStackTrace();
			return "自定义表单引擎发生异常：" + ex1.toString();
		}
	}

	/***
	 * ftl 调整（采集预览）
	 */
	@Override
	public String collectionPriview(Request request, SessionUser userInfo) throws Exception {
		LOGGER.info("采集预览开始...");
		String mID = null; // 配置编号
		String mAction = null; // 执行动作
		String templat = ""; // 模版文件
		try {
			LOGGER.info("采集预览初始化...");// 初始化信息
			mID = request.getStringItem("COLL_ID");
			mAction = request.getStringItem("COLL_Action");
			if (mID == null) {
				return ErrorHtml("非法调用错误！");
			}
			if (mAction == null) {
				mAction = "read";
			}
			LOGGER.info("采集预览读取采集配置信息...");
			CollectionInfo cInfo = getCollectionInfo(mID);
			if (cInfo == null) {
				return ErrorHtml("自定义表单引擎发生错误，采集配置信息未找到！");
			}
			templat = SysPreperty.getProperty().ShowTempletPath + "/" + cInfo.getTEMPLAET();
			
			// 读取数据库信息
			LOGGER.info("采集预览读取数据库信息...");
			// 装载主表
			DBRow dbMain = this.getTablePropery(cInfo.getMAINTABLE());
			
			ItemList itemList = new ItemList(false);
			itemList.fullFromDbRow(dbMain, null);
			// 读取流程配置信息
			String mAID = request.getStringItem("COLL_AID");
			if (mAID != null) {
				FlowList flowList = new FlowList();
				this.getFlowProprty(flowList, mAID, cInfo.getMAINTABLE(), 
									null, null, null, null, null, null, null);
				itemList.fullFromFlowField(flowList);
				flowList = null;
			}
			// --------------------------------------------------------------------------
			LOGGER.info("调用采集", "读取数据库信息....5");
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
				ftForm.setAttribute("action", "/collect/collengine");
				LOGGER.info("更替表单域成功");
				Vector<Object> vNotVisible = new Vector<Object>();
				
				// 样式解析
				LOGGER.info("加载样式解析");
				NodeList nlTable = html.searchFor(new TableTag().getClass(), true);
				for (int i = 0; i < nlTable.size(); i++) {
					TableTag tt = (TableTag) nlTable.elementAt(i);
					String tag = tt.getAttribute("coll_tag");
					if (tag != null && tag.equals("Title")) {
						tt.setAttribute("style", "border-collapse: collapse;margin-left:5px;");
						NodeList nl = tt.searchFor(new TableRow().getClass(), true);
						for (int j = 0; j < nl.size(); j++) {
							TableRow tr = (TableRow) nl.elementAt(j);
							String trTag = tr.getAttribute("coll_tag");
							if (trTag != null && trTag.equals("PuckerOpen")) {
								String trPid = tr.getAttribute("coll_pid");
								if (trPid != null) {
									NodeList col = tr.searchFor(new TableColumn().getClass(), true);
									NodeList tmp = new NodeList();
									tmp.add(getPuckerBtn(trPid, true));
									for (int k = 0; k < col.elementAt(0).getChildren().size(); k++) {
										tmp.add(col.elementAt(0).getChildren().elementAt(k));
									}
									col.elementAt(0).getChildren().removeAll();
									col.elementAt(0).getChildren().add(tmp);
								}
								tr.setAttribute("onMouseOver", "onRowOver(this,'+rowNo+')");
								tr.setAttribute("onMouseOut", "onRowOut(this,'+rowNo+')");
							} else if (trTag != null && trTag.equals("PuckerClose")) {
								String trPid = tr.getAttribute("coll_pid");
								if (trPid != null) {
									NodeList col = tr.searchFor(new TableColumn().getClass(), true);
									NodeList tmp = new NodeList();
									tmp.add(getPuckerBtn(trPid, false));
									for (int k = 0; k < col.elementAt(0).getChildren().size(); k++) {
										tmp.add(col.elementAt(0).getChildren().elementAt(k));
									}
									col.elementAt(0).getChildren().removeAll();
									col.elementAt(0).getChildren().add(tmp);
									vNotVisible.add(trPid);
								}
								tr.setAttribute("onMouseOver", "onRowOver(this,'+rowNo+')");
								tr.setAttribute("onMouseOut", "onRowOut(this,'+rowNo+')");
							} else if (trTag != null && trTag.equals("PuckerSubOpen")) {
								String trPid = tr.getAttribute("coll_pid");
								tr.setAttribute("class", "PuckerSub");
								if (trPid != null) {
									NodeList col = tr.searchFor(new TableColumn().getClass(), true);
									NodeList tmp = new NodeList();
									tmp.add(getPuckerBtn(trPid, true));
									for (int k = 0; k < col.elementAt(0).getChildren().size(); k++) {
										tmp.add(col.elementAt(0).getChildren().elementAt(k));
									}
									tmp.add(new TextNode(
											" [<a href=\"javascript:OpenWin('/collect/collengine',800,625)\">点击本栏新增或修改数据</a>]"));
									col.elementAt(0).getChildren().removeAll();
									col.elementAt(0).getChildren().add(tmp);
								}
							} else if (trTag != null && trTag.equals("PuckerSubClose")) {
								String trPid = tr.getAttribute("coll_pid");
								tr.setAttribute("class", "PuckerSub");
								if (trPid != null) {
									NodeList col = tr.searchFor(new TableColumn().getClass(), true);
									NodeList tmp = new NodeList();
									tmp.add(getPuckerBtn(trPid, false));
									for (int k = 0; k < col.elementAt(0).getChildren().size(); k++) {
										tmp.add(col.elementAt(0).getChildren().elementAt(k));
									}
									tmp.add(new TextNode(
											" [<a href=\"javascript:OpenWin('/collect/collengine',800,625)\">点击本栏新增或修改数据</a>]"));
									col.elementAt(0).getChildren().removeAll();
									col.elementAt(0).getChildren().add(tmp);
									vNotVisible.add(trPid);
								}
							}
							tr = null;
						}
						nl = null;
					} else if (tag != null && tag.equals("Content")) {
						tt.setAttribute("style", "border-collapse: collapse;margin-left:5px;");
						NodeList nl = tt.searchFor(new TableRow().getClass(), true);
						for (int j = 0; j < nl.size(); j++) {
							TableRow tr = (TableRow) nl.elementAt(j);
							String trTag = tr.getAttribute("coll_tag");
							if (trTag != null && trTag.equals("ControlHit")) {
								NodeList col = tr.searchFor(new TableColumn().getClass(), true);
								col.elementAt(0).getChildren().removeAll();
								col.elementAt(0).getChildren().add(getHit());
							}
							tr.setAttribute("onMouseOver", "onRowOver(this,'+rowNo+')");
							tr.setAttribute("onMouseOut", "onRowOut(this,'+rowNo+')");
						}
						nl = null;
					}
				}
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
					if (type.toUpperCase().equalsIgnoreCase("TEXT")) {
						for (int i = 0; i < saItem.length; i++) {
							if (saItem[i].equalsIgnoreCase(name.toUpperCase())) {
								ItemField item = itemList.getItem(saItem[i]);
								// 数字型元素
								if (item.getType().getValue() == DBType.LONG.getValue()) {
									NumberControl tc = new NumberControl((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									ctrlSet.addControl(tc);
									// 文本型
								} else if (item.getType().getValue() == DBType.STRING.getValue()) {
									// 是字典表
									if (item.isCode()) {
										// checkbox型字典元素
										if (item.getCodeInput() == 1) {
											RedioControl rc = new RedioControl((InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
											rc.setItemField(item);
											ctrlSet.addControl(rc);
										}
										// 下拉型字典元素
										else if (item.getCodeInput() == 2) {
											SelectControl dc = new SelectControl((InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
											dc.setItemField(item);
											ctrlSet.addControl(dc);
										}
										// 字典控件型字典元素
										else if (item.getCodeInput() == 3) {
											DictControl dc = new DictControl((InputTag) (lt.elementAt(index)));
											dc.setItemField(item);
											ctrlSet.addControl(dc);
										}
										// 多选控件
										else {
											checkboxControl rc = new checkboxControl((InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
											rc.setItemField(item);
											ctrlSet.addControl(rc);
										}
									}
									// 用户列表元素
									else if (item.isUser()) {
										SelectControl sc = null;
										if (item.getValue() != null) {
											sc = new SelectControl((InputTag) (lt.elementAt(index)),
													this.getUserArrary(getUserUnitID(item.getValue())));
										} else {
											sc = new SelectControl((InputTag) (lt.elementAt(index)),
													getUserArrary(userInfo.getUnitID()));
										}
										// if (item.getCodeValue()==null && item.getValue()!=null)
										// {
										item.setCodeValue(getUserName(item.getValue()));
										// }
										sc.setItemField(item);
										ctrlSet.addControl(sc);
									}
									// 单位列表元素
									else if (item.isUnit()) {
										SelectControl sc = null;
										if (!item.isWrite()) {
											try {
												sc = new SelectControl((InputTag) (lt.elementAt(index)), getDictList("BPIP_UNIT"));
												item.setDefaultValue(item.getValue());
											} catch (Exception ex2) {
												sc = new SelectControl((InputTag) (lt.elementAt(index)), getDictList("BPIP_UNIT"));
												item.setDefaultValue(userInfo.getUnitID());
											}
										} else {
											sc = new SelectControl((InputTag) (lt.elementAt(index)), getDictList("BPIP_UNIT"));
											item.setDefaultValue(userInfo.getUnitID());
										}
										// ---------------------------------------------
										// if (item.getCodeValue()==null && item.getValue()!=null)
										// {
										item.setCodeValue(getUnitName(item.getValue()));
										// }
										sc.setItemField(item);
										ctrlSet.addControl(sc);
									}
									// 普通文本型元素
									else {
										TextControl tc = new TextControl((InputTag) (lt.elementAt(index)));
										tc.setItemField(item);
										ctrlSet.addControl(tc);
									}
								}
								// 浮点型元素
								else if (item.getType().getValue() == DBType.FLOAT.getValue()) {
									FloatControl tc = new FloatControl((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									ctrlSet.addControl(tc);
								}
								// 日期型元素
								else if (item.getType().getValue() == DBType.DATE.getValue()) {
									DateControl tc = new DateControl((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									ctrlSet.addControl(tc);
								}
								// 日期时间型元素
								else if (item.getType().getValue() == DBType.DATETIME.getValue()) {
									DateTimeControl tc = new DateTimeControl((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									ctrlSet.addControl(tc);
								}
								item = null;
							}
						}
					} else if (type.toUpperCase().equalsIgnoreCase("FILE")) {
						((InputTag) (lt.elementAt(index))).setAttribute("class", "text");
					} else if (type.toUpperCase().equalsIgnoreCase("HIDDEN")) {
						boolean inItem = false;
						for (int i = 0; i < saItem.length; i++) {
							if (saItem[i].equalsIgnoreCase(name.toUpperCase())) {
								inItem = true;
								hiddSet.addPageHidden((InputTag) (lt.elementAt(index)), itemList.getItem(saItem[i]));
							}
						}
						if (!inItem) {
							hiddSet.addPageHidden((InputTag) (lt.elementAt(index)), null);
						}
					}
				}
				// Textarea 标签
				NodeList ta = html.searchFor(new TextareaTag().getClass(), true);
				for (int index = 0; index < ta.size(); index++) {
					name = ((TextareaTag) ta.elementAt(index)).getAttribute("name");
					for (int i = 0; i < saItem.length; i++) {
						if (saItem[i].equalsIgnoreCase(name.toUpperCase())) {
							TextAreaControl tc = new TextAreaControl((TextareaTag) (ta.elementAt(index)));
							tc.setItemField(itemList.getItem(saItem[i]));
							ctrlSet.addControl(tc);
						}
					}
				}
				LOGGER.info("图片标记分析");
				NodeList nlImage = html.searchFor(new ImageTag().getClass(), true);
				if (nlImage != null && nlImage.size() > 0) {
					for (int index = 0; index < nlImage.size(); index++) {
						name = ((ImageTag) nlImage.elementAt(index)).getAttribute("id");
						if (name != null && !name.equals("")) {
							for (int i = 0; i < saItem.length; i++) {
								if (saItem[i].equalsIgnoreCase(name.toUpperCase())) {
									ImageTag img = ((ImageTag) nlImage.elementAt(index));
									img.setImageURL("/photoengine?coll_id=" + mID + "&coll_pkid=" + "&coll_img=" + name);
								}
							}
						}
					}
				}
				// 添加页面头尾信息
				addJavascriptSrc("/static/ZrCollEngine/Res/coll.js", html);
				int datCtrlCount = ctrlSet.getDateControlCount() + ctrlSet.getDateTimeControlCount();
				if (datCtrlCount > 0) {
					addJavascriptSrc("/static/ZrCollEngine/DatePicker/WdatePicker.js", html);
				}
				if (mID != null && !mID.equals("")) {
					hiddSet.addNewHidden("COLL_ID", mID);
				}
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
						jBody.addFunction(" VisiableFalse('" + vNotVisible.get(k) + "');");
					}
				}
				jBody.addToBoay(html);
			}
			String sReturn = html.toHtml(true);
			html = null;
			parser = null;
			itemList = null;
			cInfo = null;
			dbMain = null;
			
			return sReturn;
		} catch (Exception ex1) {
			return "自定义表单引擎发生异常：" + ex1.toString();
		}
	}

	@Override
	public String printPriview(Request request, SessionUser userInfo) throws Exception {
		LOGGER.info("打印预览开始...");
		String mID = null; // 配置编号
		String mPKID = null; // 主键编号
		String mFKID = null; // 外键编号
		String mMode = null;
		String mAction = null; // 执行动作
		String templat = ""; // 模版文件
		StringBuffer addBuf = new StringBuffer();
		try {
			LOGGER.info("打印预览初始化...");// 初始化信息
			mID = request.getStringItem("COLL_ID");
			mPKID = request.getStringItem("COLL_PKID");
			mFKID = request.getStringItem("COLL_FKID");
			mMode = request.getStringItem("COLL_MODE");
			mAction = request.getStringItem("COLL_Action");
			if (mID == null) {
				return ErrorHtml("非法调用错误！");
			}
			if (mAction == null) {
				mAction = "read";
			}
			CollectionInfo cInfo = getCollectionInfo(mID);
			if (cInfo == null) {
				return ErrorHtml("自定义表单引擎发生错误，采集配置信息未找到！");
			}
			// 分析操作类型
			String strID = "-1";
			if (!mAction.equals("read")) {
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
			addBuf.delete(0, addBuf.length()); // 清空
			addBuf.append(SysPreperty.getProperty().ShowTempletPath + "/").append(cInfo.getTEMPLAET());
			templat = addBuf.toString();
			addBuf.delete(0, addBuf.length()); // 清空
			// 读取数据库信息，装载主表
			DBRow dbMain = this.getTablePropery(cInfo.getMAINTABLE());
			if (mPKID != null && !mPKID.equals("")) {
				this.fullData(dbMain, mPKID);
			}
			ItemList itemList = new ItemList(false);
			itemList.fullFromDbRow(dbMain, null);
			Parser parser = new Parser();
			Html html = null;
			try {
				parser.setURL(templat);
				parser.setEncoding("UTF-8");
				//parser.setEncoding("GBK");
				html = (Html) (parser.elements().nextNode());
			} catch (ParserException ex) {
				LOGGER.error("打印预览", ex.toString());
				return ErrorHtml("自定义表单引擎发生错误，读取采集模版失败！<br>详细错误信息：<br>" + ex.toString());
			}
			// 解析模版
			if (html != null) {
				NodeList nlForm = html.searchFor(new FormTag().getClass(), true);
				FormTag ftForm = (FormTag) nlForm.elementAt(0);
				ftForm.setAttribute("name", "frmColl");
				ftForm.setAttribute("id", "frmColl");
				ftForm.setAttribute("method", "post");
				ftForm.setAttribute("action", "/collect/collengine");
				Vector<Object> vNotVisible = new Vector<Object>();
				
				// 样式解析
				NodeList nlTable = html.searchFor(new TableTag().getClass(), true);
				for (int i = 0; i < nlTable.size(); i++) {
					TableTag tt = (TableTag) nlTable.elementAt(i);
					String tag = tt.getAttribute("coll_tag");
					if (tag != null && tag.equals("Title")) {
						tt.setAttribute("style", "border-collapse: collapse;margin-left:5px;");
						NodeList nl = tt.searchFor(new TableRow().getClass(), true);
						for (int j = 0; j < nl.size(); j++) {
							TableRow tr = (TableRow) nl.elementAt(j);
							String trTag = tr.getAttribute("coll_tag");
							if (trTag != null && trTag.equals("PuckerOpen")) {
								String trPid = tr.getAttribute("coll_pid");
								if (trPid != null) {
									NodeList col = tr.searchFor(new TableColumn().getClass(), true);
									NodeList tmp = new NodeList();
									tmp.add(getPuckerBtn(trPid, true));
									for (int k = 0; k < col.elementAt(0).getChildren().size(); k++) {
										tmp.add(col.elementAt(0).getChildren().elementAt(k));
									}
									col.elementAt(0).getChildren().removeAll();
									col.elementAt(0).getChildren().add(tmp);
								}
								tr.setAttribute("onMouseOver", "onRowOver(this,'+rowNo+')");
								tr.setAttribute("onMouseOut", "onRowOut(this,'+rowNo+')");
							} else if (trTag != null && trTag.equals("PuckerClose")) {
								String trPid = tr.getAttribute("coll_pid");
								if (trPid != null) {
									NodeList col = tr.searchFor(new TableColumn().getClass(), true);
									NodeList tmp = new NodeList();
									tmp.add(getPuckerBtn(trPid, false));
									for (int k = 0; k < col.elementAt(0).getChildren().size(); k++) {
										tmp.add(col.elementAt(0).getChildren().elementAt(k));
									}
									col.elementAt(0).getChildren().removeAll();
									col.elementAt(0).getChildren().add(tmp);
									vNotVisible.add(trPid);
								}
								tr.setAttribute("onMouseOver", "onRowOver(this,'+rowNo+')");
								tr.setAttribute("onMouseOut", "onRowOut(this,'+rowNo+')");
							} else if (trTag != null && trTag.equals("PuckerSubOpen")) {
								String trPid = tr.getAttribute("coll_pid");
								tr.setAttribute("class", "PuckerSub");
								if (trPid != null) {
									NodeList col = tr.searchFor(new TableColumn().getClass(), true);
									NodeList tmp = new NodeList();
									tmp.add(getPuckerBtn(trPid, true));
									for (int k = 0; k < col.elementAt(0).getChildren().size(); k++) {
										tmp.add(col.elementAt(0).getChildren().elementAt(k));
									}
									tmp.add(new TextNode(
											" [<a href=\"javascript:OpenWin('/collect/collengine',800,625)\">点击本栏新增或修改数据</a>]"));
									col.elementAt(0).getChildren().removeAll();
									col.elementAt(0).getChildren().add(tmp);
								}
							} else if (trTag != null && trTag.equals("PuckerSubClose")) {
								String trPid = tr.getAttribute("coll_pid");
								tr.setAttribute("class", "PuckerSub");
								if (trPid != null) {
									NodeList col = tr.searchFor(new TableColumn().getClass(), true);
									NodeList tmp = new NodeList();
									tmp.add(getPuckerBtn(trPid, false));
									for (int k = 0; k < col.elementAt(0).getChildren().size(); k++) {
										tmp.add(col.elementAt(0).getChildren().elementAt(k));
									}
									tmp.add(new TextNode(
											" [<a href=\"javascript:OpenWin('/collect/collengine',800,625)\">点击本栏新增或修改数据</a>]"));
									col.elementAt(0).getChildren().removeAll();
									col.elementAt(0).getChildren().add(tmp);
									vNotVisible.add(trPid);
								}
							}
						}
					} else if (tag != null && tag.equals("Content")) {
						tt.setAttribute("style", "border-collapse: collapse;margin-left:5px;");
						NodeList nl = tt.searchFor(new TableRow().getClass(), true);
						for (int j = 0; j < nl.size(); j++) {
							TableRow tr = (TableRow) nl.elementAt(j);
							String trTag = tr.getAttribute("coll_tag");
							if (trTag != null && trTag.equals("ControlHit")) {
								NodeList col = tr.searchFor(new TableColumn().getClass(), true);
								col.elementAt(0).getChildren().removeAll();
								col.elementAt(0).getChildren().add(getHit());
							}
							tr.setAttribute("onMouseOver", "onRowOver(this,'+rowNo+')");
							tr.setAttribute("onMouseOut", "onRowOut(this,'+rowNo+')");
						}
					}
				}
				// 控件解析
				ControlSet ctrlSet = new ControlSet();
				HiddenControlSet hiddSet = new HiddenControlSet();
				String[] saItem = itemList.getItemNameList();
				NodeList lt = html.searchFor(new InputTag().getClass(), true);
				for (int index = 0; index < lt.size(); index++) {
					String type = ((InputTag) (lt.elementAt(index))).getAttribute("type");
					String name = ((InputTag) (lt.elementAt(index))).getAttribute("name");
					if (type.toUpperCase().equalsIgnoreCase("TEXT")) {
						for (int i = 0; i < saItem.length; i++) {
							if (saItem[i].equalsIgnoreCase(name.toUpperCase())) {
								ItemField item = itemList.getItem(saItem[i]);
								// 数字型元素
								if (item.getType().getValue() == DBType.LONG.getValue()) {
									NumberControl tc = new NumberControl((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									ctrlSet.addControl(tc);
									// 文本型
								} else if (item.getType().getValue() == DBType.STRING.getValue()) {
									// 是字典表
									if (item.isCode()) {
										// Checkbox型字典元素
										if (item.getCodeInput() == 1) {
											RedioControl rc = new RedioControl((InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
											rc.setItemField(item);
											ctrlSet.addControl(rc);
										}
										// 下拉型字典元素
										else if (item.getCodeInput() == 2) {
											SelectControl dc = new SelectControl((InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
											dc.setItemField(item);
											ctrlSet.addControl(dc);
										}
										// 字典控件型字典元素
										else if (item.getCodeInput() == 3) {
											DictControl dc = new DictControl((InputTag) (lt.elementAt(index)));
											dc.setItemField(item);
											ctrlSet.addControl(dc);
										}
										// 多选控件
										else {
											checkboxControl rc = new checkboxControl((InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
											rc.setItemField(item);
											ctrlSet.addControl(rc);
										}
									}
									// 用户列表元素
									else if (item.isUser()) {
										SelectControl sc = null;
										if (item.getValue() != null) {
											sc = new SelectControl((InputTag) (lt.elementAt(index)),
													this.getUserArrary(getUserUnitID(item.getValue())));
										} else {
											sc = new SelectControl((InputTag) (lt.elementAt(index)),
													getUserArrary(userInfo.getUnitID()));
										}
										// if (item.getCodeValue()==null && item.getValue()!=null)
										// {
										item.setCodeValue(getUserName(item.getValue()));
										// }
										sc.setItemField(item);
										ctrlSet.addControl(sc);
									}
									// 单位列表元素
									else if (item.isUnit()) {
										SelectControl sc = null;
										if (!item.isWrite()) {
											try {
												sc = new SelectControl((InputTag) (lt.elementAt(index)), getDictList("BPIP_UNIT"));
											} catch (Exception ex2) {
												sc = new SelectControl((InputTag) (lt.elementAt(index)), getDictList("BPIP_UNIT"));
												item.setDefaultValue(userInfo.getUnitID());
											}
										} else {
											sc = new SelectControl((InputTag) (lt.elementAt(index)), getDictList("BPIP_UNIT"));
										}
										// if (item.getCodeValue()==null && item.getValue()!=null)
										// {
										item.setCodeValue(getUnitName(item.getValue()));
										// }
										sc.setItemField(item);
										addBuf.delete(0, addBuf.length()); // 清空
										addBuf.append("\"" + item.getName() + "loadFieldList(this.value)\"");
										sc.setArribute("onchange", addBuf.toString());
										ctrlSet.addControl(sc);
									}
									// 普通文本型元素
									else {
										TextControl tc = new TextControl((InputTag) (lt.elementAt(index)));
										tc.setItemField(item);
										ctrlSet.addControl(tc);
									}
								}
								// 浮点型元素
								else if (item.getType().getValue() == DBType.FLOAT.getValue()) {
									FloatControl tc = new FloatControl((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									ctrlSet.addControl(tc);
								}
								// 日期型元素
								else if (item.getType().getValue() == DBType.DATE.getValue()) {
									DateControl tc = new DateControl((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									ctrlSet.addControl(tc);
								}
								// 日期时间型元素
								else if (item.getType().getValue() == DBType.DATETIME.getValue()) {
									DateTimeControl tc = new DateTimeControl((InputTag) (lt.elementAt(index)));
									tc.setItemField(item);
									ctrlSet.addControl(tc);
								}
								item = null;
							}
						}
					} else if (type.toUpperCase().equalsIgnoreCase("FILE")) {
						((InputTag) (lt.elementAt(index))).setAttribute("class", "text");
					} else if (type.toUpperCase().equalsIgnoreCase("HIDDEN")) {
						boolean inItem = false;
						for (int i = 0; i < saItem.length; i++) {
							if (saItem[i].equalsIgnoreCase(name.toUpperCase())) {
								inItem = true;
								hiddSet.addPageHidden((InputTag) (lt.elementAt(index)), itemList.getItem(saItem[i]));
							}
						}
						if (!inItem) {
							hiddSet.addPageHidden((InputTag) (lt.elementAt(index)), null);
						}
					}
				}
				// Textarea 标签
				NodeList ta = html.searchFor(new TextareaTag().getClass(), true);
				for (int index = 0; index < ta.size(); index++) {
					String name = ((TextareaTag) ta.elementAt(index)).getAttribute("name");
					for (int i = 0; i < saItem.length; i++) {
						if (saItem[i].equalsIgnoreCase(name.toUpperCase())) {
							TextAreaControl tc = new TextAreaControl((TextareaTag) (ta.elementAt(index)));
							tc.setItemField(itemList.getItem(saItem[i]));
							ctrlSet.addControl(tc);
						}
					}
				}
				// 图片标记分析
				NodeList nlImage = html.searchFor(new ImageTag().getClass(), true);
				if (nlImage != null && nlImage.size() > 0) {
					for (int index = 0; index < nlImage.size(); index++) {
						String name = ((ImageTag) nlImage.elementAt(index)).getAttribute("id");
						if (name != null && !name.equals("")) {
							for (int i = 0; i < saItem.length; i++) {
								if (saItem[i].equalsIgnoreCase(name.toUpperCase())) {
									ImageTag img = ((ImageTag) nlImage.elementAt(index));
									addBuf.delete(0, addBuf.length()); // 清空
									addBuf.append("/photoengine?coll_id=" + mID + "&coll_pkid=" + mPKID + "&coll_img=" + name);
									img.setImageURL(addBuf.toString());
								}
							}
						}
					}
				}
				// 添加页面头尾信息
				addJavascriptSrc(addBuf.toString(), html);
				addJavascriptSrc("/static/ZrCollEngine/Res/coll.js", html);
				int datCtrlCount = ctrlSet.getDateControlCount() + ctrlSet.getDateTimeControlCount();
				if (datCtrlCount > 0) {
					addJavascriptSrc("/static/ZrCollEngine/DatePicker/WdatePicker.js", html);
				}
				JavaScriptTag jst = new JavaScriptTag();
				jst.addFunction(ctrlSet.getValidateJS(""));
				jst.addToHead(html);
				if (mMode != null && !mMode.equals("")) {
					hiddSet.addNewHidden("COLL_MODE", mMode);
				}
				if (mID != null && !mID.equals("")) {
					hiddSet.addNewHidden("COLL_ID", mID);
				}
				if (mPKID != null && !mPKID.equals("")) {
					hiddSet.addNewHidden("COLL_PKID", mPKID);
				}
				if (mAction != null && !mAction.equals("")) {
					if (mAction.equals("read")) {
						mAction = "add";
					} else if (mAction.equals("add")) {
						mAction = "edit";
					}
					hiddSet.addNewHidden("COLL_Action", mAction);
				}
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
					addBuf.delete(0, addBuf.length()); // 清空
					addBuf.append(" parent.save_return(\"" + strID + "\",\"").append(mFKID + "\",\"\")");
					jBody.addFunction(addBuf.toString());
				}
				jBody.addToBoay(html);
			}
			String sReturn = html.toHtml(true);
			html = null;
			parser = null;
			itemList = null;
			cInfo = null;
			dbMain = null;
			
			return sReturn;
		} catch (Exception ex1) {
			return "自定义表单引擎发生异常：" + ex1.toString();
		}
	}

	/**
	 * ftl  调整(待打印头部打印)
	 */
	@Override
	public String printDocument(Request request) throws Exception {
		LOGGER.info("打印文档开始...");
		String mID = null; // 配置编号（电脑）
		String mPKID = null; // 主键编号
		String mAID = null; // 流程活动编号
		int iPrintPage = 1;
		String templat = ""; // 模版文件
		StringBuffer strSQL = new StringBuffer();
		StringBuffer addBuf = new StringBuffer();
		try {
			LOGGER.info("打印文档", "初始化...");// 初始化信息
			mPKID = request.getStringItem("COLL_PKID");
			mAID = request.getStringItem("COLL_AID");
			String mPage = request.getStringItem("COLL_PAGE");
			if (mPage != null) {
				try {
					iPrintPage = Integer.parseInt(mPage);
				} catch (Exception exPage) {
					LOGGER.error(exPage.toString());
				}
			}
			if (mAID == null) {
				return ErrorHtml("非法调用错误！");
			} else {
				if (mAID.length() == 10) {// 表示流程活动
					strSQL.append("Select b.DocID,b.DocIDPHO From FLOW_CONFIG_ACTIVITY a Left Join FLOW_CONFIG_PROCESS b on b.ID=a.FID "
							+ "Where a.ID='" + mAID + "'");
					List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
					if (retlist != null && retlist.size() > 0) {
						if (retlist.get(0) != null && retlist.get(0).get("DocID") != null) {
							mID = retlist.get(0).get("DocID").toString();
						}
					}
				} else {// 自定义表单
					mID = mAID;
				}
			}
			if (mID == null) {
				return ErrorHtml("自定义表单引擎发生异常：<br>流程配置与采集配置错误，未找到相关配置信息。");
			}
			// 读取采集配置信息
			CollectionInfo cInfo = getCollectionInfo(mID);
			if (cInfo == null) {
				return ErrorHtml("自定义表单引擎发生错误，采集配置信息未找到！");
			} else {
				addBuf.append(SysPreperty.getProperty().PrintTempletPath + "/" + cInfo.getPrintTemplet(iPrintPage));
				templat = addBuf.toString();
				addBuf.delete(0, addBuf.length()); // 清空
			}
			// 装载主表
			DBRow dbMain = this.getTablePropery(cInfo.getMAINTABLE());
			if (mPKID != null && !mPKID.equals("")) {
				this.fullData(dbMain, mPKID);
			}
			ItemList itemList = new ItemList(false);
			itemList.fullFromDbRow(dbMain, null);
			
			Parser parser = new Parser();
			Html html = null;
			try {
				parser.setURL(templat);
				parser.setEncoding("UTF-8");
				//parser.setEncoding("GBK");
				html = (Html) (parser.elements().nextNode());
			} catch (ParserException ex) {
				return ErrorHtml("自定义表单引擎发生错误，读取采集模版失败！<br>详细错误信息：<br>" + ex.toString());
			}
			// 解析模版
			if (html != null) {
				// 控件解析
				String[] saItem = itemList.getItemNameList();
				NodeList nlImage = html.searchFor(new ImageTag().getClass(), true);
				for (int index = 0; index < nlImage.size(); index++) {
					String name = ((ImageTag) nlImage.elementAt(index)).getAttribute("id");
					if (name != null) {
						for (int i = 0; i < saItem.length; i++) {
							if (saItem[i].equalsIgnoreCase(name)) {
								addBuf.delete(0, addBuf.length()); // 清空
								addBuf.append("/photoengine?coll_id=" + mID + "&coll_pkid=" + mPKID + "&coll_img=" + name);
								String URL = addBuf.toString();
								ImageTag img = ((ImageTag) nlImage.elementAt(index));
								img.setImageURL(URL);
							}
						}
					} else {
						String tag = ((ImageTag) nlImage.elementAt(index)).getAttribute("barcode");
						String URL = "";
						String Name = "";
						if (tag != null && tag.equals("cafis")) {
							Name = ((ImageTag) nlImage.elementAt(index)).getAttribute("name");
							ItemField item = itemList.getItem(Name);
							if (item != null) {
								addBuf.delete(0, addBuf.length()); // 清空
								if (mPKID == null || mPKID.equals("null")) {
									addBuf.append("docbarcode.do?type=1&id=000000000000");
								} else {
									addBuf.append("docbarcode.do?type=1&id=" + mPKID);
								}
								URL = addBuf.toString();
							}
							item = null;
						}
						if (tag != null && tag.equals("doc")) {
							addBuf.delete(0, addBuf.length()); // 清空
							if (mPKID == null || mPKID.equals("null")) {
								addBuf.append("docbarcode.do?type=0&id=000000000000");
							} else {
								addBuf.append("docbarcode.do?type=0&id=" + mPKID);
							}
							URL = addBuf.toString();
						}
						ImageTag img = ((ImageTag) nlImage.elementAt(index));
						img.setImageURL(URL);
					}
				}
				// ------从上面移下解决签名图片的问题------------------------------------
				NodeList lt = html.searchFor(new InputTag().getClass(), true);
				for (int index = 0; index < lt.size(); index++) {
					String name = ((InputTag) (lt.elementAt(index))).getAttribute("name");
					for (int i = 0; i < saItem.length; i++) {
						if (saItem[i].equalsIgnoreCase(name)) {
							ItemField item = itemList.getItem(saItem[i]);
							if (item.getCodeInput() == 4) {// 多选控件
								checkboxControl rc = new checkboxControl((InputTag) (lt.elementAt(index)), getDictList(item.getCodeTable()));
								item.setIsShow(false);
								item.setIsWrite(false);
								rc.setItemField(item);
							} else {
								PrintControl tc = new PrintControl((InputTag) (lt.elementAt(index)));
								tc.setItemField(item);
							}
						}
					}
				}
				NodeList nlHead = html.searchFor(new BodyTag().getClass(), true);
				BodyTag tagHead = (BodyTag) nlHead.elementAt(0);
				tagHead.getChildren().add(new WebShellTag("webshell"));
				
				String cachetXML = "";
				InputTag hidden = new InputTag();
				hidden.setAttribute("type", "hidden");
				hidden.setAttribute("name", "PmXML");
				hidden.setAttribute("id", "PmXML");
				hidden.setAttribute("value", cachetXML);
				FormTag ft = new FormTag();
				ft.setAttribute("name", "frmCachat");
				ft.setAttribute("id", "frmCachat");
				ft.setAttribute("action", "");
				ft.setAttribute("method", "post");
				LabelTag ltFt = new LabelTag();
				ltFt.setText("</FORM>");
				ft.setChildren(new NodeList(hidden));
				ft.setEndTag(ltFt);
				tagHead.getChildren().add(ft);
				
				VBScriptTag vBody = new VBScriptTag();
				vBody.addFunction(RemarkVb.get());
				vBody.addToBoay(html);
				
				JavaScriptTag jBody = new JavaScriptTag();
				jBody.addFunction(PrintJs.get(mPage, ""));
				jBody.addToBoay(html);
				
				addJavascriptSrc("/static/ZrCollEngine/Res/docPrint.js", html);
			}
			String sReturn = html.toHtml(true);
			html = null;
			parser = null;
			dbMain = null;
			
			return sReturn;
		} catch (Exception ex1) {
			return "自定义表单引擎发生异常：" + ex1.toString();
		}
	}

	@Override
	public String getDateChangeJs(Hashtable<String, Object> htDate) throws Exception {
		StringBuffer sb = new StringBuffer();
		Enumeration<String> names;
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
			strSQL.append("Select b.DocID,b.DocIDPHO From FLOW_CONFIG_ACTIVITY a Left Join FLOW_CONFIG_PROCESS b on b.ID=a.FID "
						+ "Where a.ID='" + AID + "'");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
			if (retlist != null && retlist.size() > 0) {
				if (retlist.get(0) != null && retlist.get(0).get("DocID") != null) {
					docID = retlist.get(0).get("DocID").toString();
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

	@Override
	public List<Map<String, Object>> getUserList(String UnitID) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("Select UserID,Name From BPIP_USER Where USERSTATE='0' and UnitID='" + UnitID + "'");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			String[] saTmp = new String[2];
			saTmp[0] = "0";
			saTmp[1] = "";
			result.add(new HashMap<String, Object>(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				{
					put("UserID", saTmp[0]);
					put("Name", saTmp[1]);
				}
			});
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				String[] saTmp1 = new String[2];
				saTmp1[0] = retmap.get("UserID").toString();
				saTmp1[1] = retmap.get("Name").toString();
				result.add(new HashMap<String, Object>(){
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					{
						put("UserID", saTmp1[0]);
						put("Name", saTmp1[1]);
					}
				});
			}
		}
		return result;
	}

	@Override
	public String[][] getUserArrary(String UnitID) throws Exception {
		String[][] saUser = null;
		saUser = (String[][]) CodeHashtable.get("BPIP_USER" + UnitID);
		if (saUser == null) {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("Select UserID,Name From BPIP_USER Where USERSTATE='0' and UnitID='" + UnitID + "'");
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
	public String updateTable(String updateType, CollectionInfo cInfo, 
			Request request, SessionUser userinfo) throws Exception {
		LOGGER.info("updateTable开始...");
		String ID = "-1";
		if (updateType == null) {
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
		if (updateType != null & cInfo != null & request != null && userinfo != null) {
			String mFKID = request.getStringItem("COLL_FKID");
			// 获取数据表属性
			DBRow dbrow = getTablePropery(cInfo.getMAINTABLE());
			if (dbrow != null) {
				DBRow[] drList = new DBRow[1];
				String[] saKey = dbrow.getPrimaryKeyName();
				if (saKey != null && saKey.length > 0 && saKey[0] != null&& saKey[0].equals(cInfo.getOTHERFIELD())) {
					// code
				} else {
					dbrow.Column(cInfo.getOTHERFIELD()).setValue(mFKID);
				}
				// --------从上面移下的行------------------------
				fullDataFromRequest(request, dbrow, null);
				// --------------------------------------------
				String sID = initDbRow(dbrow, updateType, userinfo, mFKID);
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
		return ID;
	}

	@Override
	public String doGetDictList(String tableName) throws Exception {
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

	@Override
	public String getCollFkidByRid(String Rid) throws Exception {
		String docTableName, PrimaryKey, PkValue;
		String rtnValue = null;
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("Select * from FLOW_RUNTIME_PROCESS Where ID='" + Rid + "'");
		Map<String, Object> retmap = userMapper.selectMapExecSQL(sqlStr.toString());
		try {
			if (retmap != null && retmap.size() > 0) {
				docTableName = retmap.get("FORMTABLE").toString();
				PkValue = retmap.get("FORMID").toString();
				PrimaryKey = getPrimaryFieldName(docTableName);
				sqlStr.delete(0, sqlStr.length()); // 清空
				sqlStr.append("Select * From " + docTableName + " Where ").append(PrimaryKey + "='" + PkValue + "'");
				List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sqlStr.toString());
				if (retlist != null && retlist.size() > 0) {
					if (retlist.get(0) != null && retlist.get(0).get("COLL_FKID") != null) {
						rtnValue = retlist.get(0).get("COLL_FKID").toString();
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
		Map<String, Object> retmap = userMapper.selectMapExecSQL(strSQL.toString());
		if (retmap != null && retmap.size() > 0) {
			FORMTABLE = retmap.get("FORMTABLE").toString();
			FORMID = retmap.get("FORMID").toString();
		}
		// 得到业务表的主键
		strSQL.delete(0, strSQL.length());// 清空
		strSQL.append("select PRIMARYKEY from BPIP_TABLE where TABLENAME='" + FORMTABLE + "'");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
		if (retlist != null && retlist.size() > 0) {
			if (retlist.get(0) != null && retlist.get(0).get("PRIMARYKEY") != null) {
				PRIMARYKEY = retlist.get(0).get("PRIMARYKEY").toString();
			}
		}
		// 更新字体设置字段的值
		strSQL.delete(0, strSQL.length());// 清空
		strSQL.append("update "+FORMTABLE+" set CSSFONT ='"+CssFont+"' where "+PRIMARYKEY+"='"+FORMID+"'");
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
		Map<String, Object> retmap = userMapper.selectMapExecSQL(strSQL.toString());
		if (retmap != null && retmap.size() > 0) {
			FORMTABLE = retmap.get("FORMTABLE").toString();
			FORMID = retmap.get("FORMID").toString();
		}
		// 得到业务表的主键
		strSQL.delete(0, strSQL.length());// 清空
		strSQL.append("select PRIMARYKEY from BPIP_TABLE where TABLENAME='" + FORMTABLE + "'");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
		if (retlist != null && retlist.size() > 0) {
			if (retlist.get(0) != null && retlist.get(0).get("PRIMARYKEY") != null) {
				PRIMARYKEY = retlist.get(0).get("PRIMARYKEY").toString();
			}
		}
		// 得到字体设置字段的值
		strSQL.delete(0, strSQL.length());// 清空
		strSQL.append("select CSSFONT from " + FORMTABLE + " where " + PRIMARYKEY + "='" + FORMID + "'");
		List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(strSQL.toString());
		if (retlist1 != null && retlist1.size() > 0) {
			if (retlist1.get(0) != null && retlist1.get(0).get("CSSFONT") != null) {
				CSSFONT = retlist1.get(0).get("CSSFONT").toString();
			}
		}
		strRetrun = CSSFONT;
		return strRetrun;
	}

	@Override
	public String getCollName(String ID) throws Exception {
		String strSQL = "select DOCNAME from COLL_DOC_CONFIG where ID='" + ID + "'";
		String strDOCNAME = "";
		Map<String, Object> retmap = userMapper.selectMapExecSQL(strSQL);
		if (retmap != null && retmap.size() > 0) {
			strDOCNAME = retmap.get("DOCNAME").toString();
		}
		return strDOCNAME;
	}

	/**
	 * ftl  调整（删除表单对应的数据）
	 */
	@Override
	public boolean datadelete(String docid, String id) throws Exception {
		boolean revalue = false;
		String TABLEID = "";
		String TABLENAME = "";
		String PRIMARYKEY = "";
		String FIELDTYPE = "";
		String strSQL = "select c.TABLEID,c.TABLENAME,c.PRIMARYKEY "
					  + "from COLL_DOC_CONFIG b join BPIP_TABLE c on b.maintable=c.TABLEID "
					  + "where b.ID='" + id + "'";
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
		if (retlist != null && retlist.size() > 0) {
			Map<String, Object> retmap = retlist.get(0);
			TABLEID = retmap.get("TABLEID").toString();
			TABLENAME = retmap.get("TABLENAME").toString();
			PRIMARYKEY = retmap.get("PRIMARYKEY").toString();
			// 得到主键的类型
			strSQL = "select FIELDTYPE from BPIP_FIELD where TABLEID='" + TABLEID 
					+ "' and FIELDNAME='" + PRIMARYKEY + "'";
			List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(strSQL);
			if (retlist1 != null && retlist1.size() > 0) {
				if (retlist1.get(0) != null && retlist1.get(0).get("FIELDTYPE") != null) {
					FIELDTYPE = retlist1.get(0).get("FIELDTYPE").toString();
				}
				if (FIELDTYPE.equals("1")) {
					strSQL = "delete from " + TABLENAME + " where " + PRIMARYKEY + "='" + docid + "'";
				} else {
					strSQL = "delete from " + TABLENAME + " where " + PRIMARYKEY + "=" + docid;
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
		String strSql = "Select BLOBSIZE from bpip_field Where TableID = "
				+ "(Select TableID From Bpip_Table Where TableName='"
				+ tableName + "' ) And FieldName='" + fieldName + "'";
		DBSet dbset = dbEngine.QuerySQL(strSql, "form5", tableName + fieldName);
		if (dbset != null && dbset.RowCount() > 0) {
			try {
				rvalue = dbset.Row(0).Column("BLOBSIZE").getInteger();
			} catch (Exception ex) {
				rvalue = 0;
				ex.printStackTrace();
			}
		}
		dbset = null;
		return rvalue;
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
		strSQL.append("Select UnitID From BPIP_USER Where UserID='" + UserID).append("'");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
		if (retlist != null && retlist.size() > 0) {
			if (retlist.get(0) != null && retlist.get(0).get("UnitID") != null) {
				result = retlist.get(0).get("UnitID").toString();
			}
		}
		return result;
	}

	private void checkDefault(ItemList itemlist) throws Exception {
		if (itemlist != null && itemlist.getItemCount() > 0) {
			String[] itemNames = itemlist.getItemNameList();
			for (int i = 0; i < itemNames.length; i++) {
				String itemname = itemNames[i];
				if (itemlist.getItem(itemname).isCode()
						&& !(itemlist.getItem(itemname).getValue() != null
						&& !itemlist.getItem(itemname).getValue().equals(""))
						&& itemlist.getItem(itemname).getDefaultValue() != null
						&& !itemlist.getItem(itemname).getDefaultValue().equals("")) {
					itemlist.getItem(itemname).setCodeValue(
							this.getCodeValue(itemlist.getItem(itemname).getCodeTable(), 
							itemlist.getItem(itemname).getDefaultValue()));
				}
				if (itemlist.getItem(itemname).isUnit()
						&& !(itemlist.getItem(itemname).getValue() != null
						&& !itemlist.getItem(itemname).getValue().equals(""))
						&& itemlist.getItem(itemname).getDefaultValue() != null
						&& !itemlist.getItem(itemname).getDefaultValue().equals("")) {
					itemlist.getItem(itemname).setCodeValue(
							this.getUnitName(itemlist.getItem(itemname).getDefaultValue()));
				}
				if (itemlist.getItem(itemname).isUser()) {
					if (itemlist.getItem(itemname).getValue() == null || itemlist.getItem(itemname).getValue().equals("")) {
						if (itemlist.getItem(itemname).getDefaultValue() != null
								&& !itemlist.getItem(itemname).getDefaultValue().equals("")) {
							itemlist.getItem(itemname).setCodeValue(
									this.getUserName(itemlist.getItem(itemname).getDefaultValue()));
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
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

	private NodeList getHit() {
		NodeList hit = new NodeList();
		ImageTag imgDate = new ImageTag();
		imgDate.setImageURL("ZrCollEngine/Res/datecal.gif");
		ImageTag imgDict = new ImageTag();
		imgDict.setImageURL("ZrCollEngine/Res/control.gif");
		hit.add(new TextNode("※图示说明※ "));
		hit.add(imgDate);
		hit.add(new TextNode("日历选择 "));
		hit.add(imgDict);
		hit.add(new TextNode("代码选择"));

		imgDate = null;
		return hit;
	}

	private ImageTag getPuckerBtn(String id, boolean expend) {
		String imgUrl = "ZrCollEngine/Res/expand.gif";
		String alt = "展开内容";
		if (expend) {
			imgUrl = "ZrCollEngine/Res/shrink.gif";
			alt = "隐藏内容";
		}
		ImageTag imgBtn = new ImageTag();
		imgBtn.setImageURL(imgUrl);
		imgBtn.setAttribute("onClick", "javascript:Pucker(this,'" + id + "')");
		imgBtn.setAttribute("alt", alt);
		return imgBtn;
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

	private void addCssLinkSrc(String stc, Html html) {
		StyleLinkTag st = new StyleLinkTag();
		st.setUrl(stc);
		NodeList nlHead = html.searchFor(new HeadTag().getClass(), true);
		HeadTag tagHead = (HeadTag) nlHead.elementAt(0);
		tagHead.getChildren().add(st);
	}

	private String[][] getDictList(String codeTable) throws Exception {
		String[][] result = null;
		result = (String[][]) CodeHashtable.get(codeTable);
		String sqlValue = "";
		int getnum = 0;
		if (result == null) {
			// 用户表
			if (codeTable.equals("BPIP_USER")) {
				sqlValue = "Select count(USERID) as NUM From BPIP_USER Where USERSTATE='0'";
				Map<String, Object> retmap = userMapper.selectMapExecSQL(sqlValue);
				getnum = Integer.parseInt(retmap.get("NUM").toString());
				if (getnum > 2000) {
					result = new String[1][2];
					result[0][0] = "000000000000";
					result[0][1] = "无";
					CodeHashtable.put(codeTable, result);
				} else {
					sqlValue = "Select USERID,NAME From BPIP_USER Where USERSTATE='0' order by USERID";
					List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(sqlValue);
					int length1 = retlist1 != null ? retlist1.size() : 0;
					if (length1 > 0) {
						result = new String[length1][2];
						for (int i = 0; i < length1; i++) {
							Map<String, Object> retmap1 = retlist1.get(i);
							result[i][0] = retmap1.get("USERID").toString();
							result[i][1] = retmap1.get("NAME").toString();
						}
						if (result != null) {
							CodeHashtable.put(codeTable, result);
						}
					}
				}
			}
			// 单位表
			if (codeTable.equals("BPIP_UNIT")) {
				sqlValue = "Select count(UNITID) as NUM From BPIP_UNIT Where STATE!='1'";
				Map<String, Object> retmap2 = userMapper.selectMapExecSQL(sqlValue);
				getnum = Integer.parseInt(retmap2.get("NUM").toString());
				if (getnum > 2000) {
					result = new String[1][2];
					result[0][0] = "000000000000";
					result[0][1] = "无";
					CodeHashtable.put(codeTable, result);
				} else {
					sqlValue = "Select UNITID,UNITNAME From BPIP_UNIT Where STATE!='1' ORDER BY UNITID";
					List<Map<String, Object>> retlist2 = userMapper.selectListMapExecSQL(sqlValue);
					int length2 = retlist2 != null ? retlist2.size() : 0;
					if (length2 > 0) {
						result = new String[length2][2];
						for (int i = 0; i < length2; i++) {
							Map<String, Object> retmap = retlist2.get(i);
							result[i][0] = retmap.get("UNITID").toString();
							result[i][1] = retmap.get("UNITNAME").toString();
						}
						if (result != null) {
							CodeHashtable.put(codeTable, result);
						}
					}
				}
			}
			// 字典表
			if (!codeTable.equals("BPIP_USER") && !codeTable.equals("BPIP_UNIT")) {
				sqlValue = "Select CODE,NAME From " + codeTable + " where ISSHOW!='1' or ISSHOW is null order by CODE";
				List<Map<String, Object>> retlist3 = userMapper.selectListMapExecSQL(sqlValue);
				if (retlist3 == null || retlist3.size() == 0) {
					sqlValue = "Select CODE,NAME From " + codeTable + " order by CODE";
					retlist3 = userMapper.selectListMapExecSQL(sqlValue);
				}
				if (retlist3 != null && retlist3.size() > 0) {
					int length3 = retlist3.size();
					result = new String[length3][2];
					for (int i = 0; i < length3; i++) {
						Map<String, Object> retmap3 = retlist3.get(i);
						result[i][0] = retmap3.get("CODE").toString();
						result[i][1] = retmap3.get("NAME").toString();
					}
					if (result != null) {
						CodeHashtable.put(codeTable, result);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 获取采集文档配置信息
	 * @param id 采集文档配置编号
	 * @return CollectionInfo 采集文档配置信息
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
					
					strSQL.delete(0, strSQL.length());// 清空
					strSQL.append("Select PAGE,TEMPLAET From COLL_DOC_PRINT Where DOCID='" + id + "'");
					List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(strSQL.toString());;
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
		strSQL.append("Select a.TABLEID,a.TABLETYPE,a.PRIMARYKEY From BPIP_TABLE a "
					+ "Where a.TABLENAME='" + TableName + "'");
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
							dc.setType(SwitchDbType(dsField.Row(i).Column("FIELDTYPE").getString()));
							dc.setLength(dsField.Row(i).Column("FIELDLENGTH").getInteger());
							dc.setCodeTable(dsField.Row(i).Column("DICTTABLE").getString());
							dc.setNULL(true);
							dc.setTag(SwitchDbTag(dsField.Row(i).Column("FIELDTAG").getString()));
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
							if (strDEFAULT.substring(0, 1).equals("?")) {// 多选一的情况
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
							LOGGER.error("GetValueByExpression", ex.toString());
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
			ds = null;
			drTmp = null;
		}
	}

	/**
	 * 给数据行赋值
	 * @param dr 要赋值的数据行
	 * @param Key 数据行的主键值
	 */
	private void fullData(DBRow dr, String Key, boolean isBase) {
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
		try {
			setData(dr, strWhere);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fullData(DBRow dr, String Key) {
		fullData(dr, Key, false);
	}

	/**
	 * 获取字典表的代码值
	 * @param TableName 字典表名称
	 * @param Code 代码
	 * @return String 代码值
	 */
	private String getCodeValue(String TableName, String Code) {
		String result = null;
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("Select NAME From " + TableName + " Where Code='" + Code + "'");
		DBSet ds = dbEngine.QuerySQL(strSQL.toString(), "form2", TableName + Code);
		if (ds != null && ds.RowCount() > 0) {
			result = ds.Row(0).Column("NAME").getString();
			ds = null;
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
		Map<String, Object> retmap = userMapper.selectMapExecSQL(strSQL.toString());
		if (retmap != null && retmap.size() > 0) {
			result = retmap.get("UNITNAME").toString();
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
		strSQL.append("Select Name From BPIP_USER Where UserID='" + UserID + "'");
		Map<String, Object> retmap = userMapper.selectMapExecSQL(strSQL.toString());
		if (retmap != null && retmap.size() > 0) {
			result = retmap.get("Name").toString();
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
	 * 将从数据库中读取的文本数据类型转换为dbType型
	 * @param strType
	 * @return dbType
	 */
	private DBType SwitchDbType(String strType) {
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
	private DBTag SwitchDbTag(String strTag) {
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
	 * ftl 调整（用哈西表填充数据）
	 * @param has
	 * @param dbrow
	 */
	private void fullDataFromRequest(Request request, DBRow dbrow, String Alias) {
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
			strSQL.append("Select FIELDNAME From BPIP_FIELD Where TABLEID in (select TABLEID from BPIP_TABLE "
						+ "where TABLENAME='" + dbrow.getTableName() + "') and FIELDTAG='2' and TAGEXT=4");
			DBSet ds = dbEngine.QuerySQL(strSQL.toString());
			if (strFIELDNAMEs != null) {
				strFIELDNAMEs = "";
			}
			if (ds != null && ds.RowCount() > 0) {
				for (int i = 0; i < ds.RowCount(); i++) {
					if (strFIELDNAMEs.length() == 0) {
						strFIELDNAMEs = ds.Row(i).Column("FIELDNAME").getString();
					} else {
						strFIELDNAMEs = strFIELDNAMEs + "," + ds.Row(i).Column("FIELDNAME").getString();
					}
				}
				ds = null;
			}
			if (strFIELDNAMEs.length() == 0) {
				strFIELDNAMEs = "no";
			}
			MoreSelectHashtable.put(dbrow.getTableName(), strFIELDNAMEs);
		}
		if (strFIELDNAMEs.equals("no")) {
			strFIELDNAMEs = "";
		}
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
					}else {
						//通过base64转图片
						if(request.getStringItem(itemName) != null) {
							String base64 = String.valueOf(request.getStringItem(itemName));
							byte[] image = Base64.decodeBase64(base64);
							isadd = 1;
							dbrow.Column(colName).setValue(image);
						}
					}
				} else
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
			}
		}
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

	// ******************下列函数用于生成自动编号***********************//
	/**
	 * 设置自动编号（前缀字母＋系统自动编号）
	 * @param dc dbColumn 字段
	 */
	private void set_CASE_NUMBER(DBColumn dc) {
		String strValue = "";
		String strNum = "";
		try {
			strNum = myGetRandom(4);
		} catch (Exception ex) {
			strNum = "0";
		}
		strValue = dc.getAuto1().trim() + globalID() + strNum;
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

	private String globalID() {
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
		strSQL.append("Select max(" + dc.getName() + ") as maxid From " + dr.getTableName() + " Where " + mCondition);
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
		strSQL.append("Select max(" + dc.getName() + ") as maxid From " + dr.getTableName() + " Where " + mCondition1);
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
	 * @param dc dbColumn 字段
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
		strSQL.append("Select max(" + dc.getName() + ") as maxid From " + dr.getTableName() + " Where " + mCondition1);
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
	 * @param dc
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
	 * @param dc
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
		strSQL.append("Select max(" + dc.getName() + ") as maxid From " + dr.getTableName() + " Where " + mCondition);
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
	 * @param dc
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
		strSQL.append("Select max(" + dc.getName() + ") as maxid From " + dr.getTableName() + " Where " + mCondition1);
		if (mCondition2.length() > 0) {
			strSQL.append("  And " + mCondition2);
		}
		DBSet dsID = dbEngine.QuerySQL(strSQL.toString());
		String sID = getFristID(dc.getLength());
		if (dsID != null) {
			if (dsID.RowCount() > 0) {
				sID = dsID.Row(0).Column("maxid").getString();
				StringWork sw = new StringWork();
				//sw.NewID(sID);// 原来的程序
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
		//-----------------------------------------------
		DBSet dsID = dbEngine.QuerySQL(strSQL.toString());
		String sID = getFristID(dc.getLength());
		if (dsID != null) {
			if (dsID.RowCount() > 0) {
				sID = dsID.Row(0).Column("maxid").getString();
				StringWork sw = new StringWork();
				//sw.NewID(sID);// 原来的程序
				sID = sw.NewID(sID);
			}
			dsID = null;
		}
		if (sID.length() == 0 || sID.equals("null")) {
			sID = "1";
		}
		//-----------------------------------------------
		dc.setValue(sID);
	}

	// *********************自动编号生成函数结束*********************//
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
			strSQL.append("Select * from " + docTableName + " Where  ")
				  .append(getPrimaryFieldName(docTableName) + "='" + ds.Row(0).Column("FORMID").getString() + "'");
			dsResult = dbEngine.QuerySQL(strSQL.toString());
			ds = null;
		}
		return dsResult;
	}

	//-----------------------保存结果处理及设置默认值相关-----------------------//
	//-----------------------设置保存后的处理结果相关函数-----------------------//
	/**
	 * 功能:处理表达式，使其能计算出来
	 * @param expression 待计算的函数表达式
	 * @return String 处理完后的表达式
	 */
	private String prepareExpression(String expression, String _mPKID, String _mFKID, 
			String _PAID1, String _PAID2, SessionUser _user, String _RID) {
		DBSet mdbset = null, dsTmp = null;
		String relationField, tempValue, strPrimary;
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
						addBuf.append(sqlStr.toString() + "  Where " + relationField + "='" + _PAID2 + "'");
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
						} else {
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

	//**********************以下函数用于表达式计算*******************//
	//************************计算结果或计算条件********************//
	/**
	 * 功能:根据算术表达式计算值
	 * @param expression 待计算的函数表达式
	 * @return String 函数返回的结果 表达式中的[表名.字段名]的值在表达式计算前须取出来后将表达式的对应部公置换掉
	 */
	private String getValueByExpression(String expression, String _mPKID, String _mFKID, 
			String _PAID1, String _PAID2, SessionUser _user, String _RID) {
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
					List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sqlStr.toString());
					if (retlist != null && retlist.size() > 0) {
						if (retlist.get(0) != null && retlist.get(0).get("resultStr") != null) {
							resultStr = retlist.get(0).get("resultStr").toString();
						}
					} else {
						resultStr = expression; // 当retlist为null时，则表达式有误
					}
				} catch (Exception ex) {
					LOGGER.error("出现异常", ex.getMessage());
				}
				if (resultStr.equals("???")) {
					resultStr = expression;
					LOGGER.info("2", resultStr);
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
		//expression=@SQL:字段名@Select 字段名 From ... Where 字段名='PKVALUE'
		
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
					} else {
						rtnValue = String.valueOf(ds.Row(0).Column(fieldName).getValue());
					}
				}
			}
			ds = null;
		}
		return rtnValue;
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

	/**
	 * 功能:根据表名称获取关联字段的名称
	 * @param FORMTABLE 表名称
	 * @return String 关联字段名称
	 * @throws Exception 
	 */
	private String getRelationField(String FORMTABLE) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT b.FIELDNAME FROM BPIP_TABLE a,BPIP_FIELD b,COLL_DOC_CONFIG c "
				+ "Where a.tablename='" + FORMTABLE + "' and b.TABLEID=a.TABLEID and "
				+ "c.MAINTABLE=a.Tableid and b.FIELDID=c.OTHERFIELD");
		
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sqlStr.toString());
		if (retlist != null && retlist.size() > 0) {
			String strReturn = "";
			if (retlist.get(0) != null && retlist.get(0).get("FIELDNAME") != null) {
				strReturn = retlist.get(0).get("FIELDNAME").toString();
			}
			return strReturn;
		} else {
			return "";
		}
	}
	///**************************计算结果或计算条件结束**************************///
	//--------------------------设置保存后的处理结果相关函数结束--------------------------///

	/**
	 * 若字段的值为代码，则将其转为代码所对应的值。
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
		sqlStr.append("Select FIELDTYPE From BPIP_FIELD Where UPPER(FIELDNAME) ='"
			  		+ fieldName.toUpperCase() + "' And  TABLEID In ("
			  		+ "Select TABLEID FROM BPIP_TABLE Where UPPER(TABLENAME) = '"
					+ tableName.toUpperCase() + "')");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sqlStr.toString());
		if (retlist != null && retlist.size() > 0) {
			if (retlist.get(0) != null && retlist.get(0).get("FIELDTYPE") != null) {
				rtnStr = retlist.get(0).get("FIELDTYPE").toString();
			}
		}
		return rtnStr;
	}

	/**
	 * 生成联动字段相关js
	 * @param TABLENAME
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	private String getNDJS(String TABLENAME) throws Exception {
		String FIELDNAME = "";
		String FIELDNAMEY = "";
		String FIELDNAMEA = "";
		String DESCRIPTION = "";
		String JS1 = "";
		String JS2 = "";
		String JS31 = "";
		String JS32 = "";
		String JS4 = "";
		String JS5 = "";
		String ZJS = "";
		String strZSQL = "SELECT b.FIELDNAME, b.DESCRIPTION FROM BPIP_TABLE a, BPIP_FIELD b WHERE a.TABLEID = b.TABLEID "
				+ "AND a.TABLENAME = '" + TABLENAME + "' AND b.DESCRIPTION > '00000000' order by b.DESCRIPTION,b.FIELDID";
		
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strZSQL);
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				FIELDNAME = retmap.get("FIELDNAME").toString();
				DESCRIPTION = retmap.get("DESCRIPTION").toString();
				if (!DESCRIPTION.equals(FIELDNAMEY)) {
					strZSQL = "SELECT FIELDNAME FROM  BPIP_FIELD WHERE FIELDID='" + DESCRIPTION + "'";
					List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(strZSQL);
					if (retlist1 != null && retlist1.size() > 0) {
						if (retlist1.get(0) != null && retlist1.get(0).get("FIELDNAME") != null) {
							FIELDNAMEA = retlist1.get(0).get("FIELDNAME").toString();
						}
					}
					JS1 = JS1 + "var " + FIELDNAMEA + "id=$(\"#" + FIELDNAMEA + "\").combobox(\"getValue\");\r\n";
					JS1 = JS1 + "var " + FIELDNAMEA + "name=$(\"#" + FIELDNAMEA + "\").combobox(\"getText\");\r\n";
					
					JS2 = JS2 + "$(\"#" + FIELDNAMEA + "\").combobox(\"setValue\"," + FIELDNAMEA + "id);\r\n";
					JS2 = JS2 + "$(\"#" + FIELDNAMEA + "\").combobox(\"setText\"," + FIELDNAMEA + "name);\r\n";
					
					JS31 = "$(\"#" + FIELDNAMEA + "\").combobox({\r\n";
					JS31 = JS31 + "onChange: function () {\r\n";
					JS31 = JS31 + "var url = \"\";\r\n";
					JS31 = JS31 + "var sid = \"\";\r\n";
					JS32 = "}});\r\n";
					
					JS4 = "sid = $(\"#" + FIELDNAMEA + "\").combobox(\"getValue\");\r\n";
					JS4 = JS4 + "url = \"/dictdata/actionJson?method=dtselect&sid=\"+sid+\"&tablename=" + TABLENAME
							+ "&fieldname=" + FIELDNAME + "\";\r\n";
					
					JS4 = JS4 + "$.getJSON(url, function(json){\r\n";
					JS4 = JS4 + "$('#" + FIELDNAME + "').combobox({\r\n";
					JS4 = JS4 + "data:json,\r\n";
					JS4 = JS4 + "valueField:'CODE',\r\n";
					JS4 = JS4 + "textField:'NAME',\r\n";
					JS4 = JS4 + "});\r\n";
					JS4 = JS4 + "});\r\n\r\n";
					
					FIELDNAMEY = DESCRIPTION;
				} else {
					JS4 = JS4 + "sid = $(\"#" + FIELDNAMEA + "\").combobox(\"getValue\");\r\n";
					JS4 = JS4 + "url = \"/dictdata/actionJson?method=dtselect&sid=\"+sid+\"&tablename=" 
							+ TABLENAME + "&fieldname=" + FIELDNAME + "\";\r\n";
					
					JS4 = JS4 + "\r\n$.getJSON(url,function(json){\r\n";
					JS4 = JS4 + "$('#" + FIELDNAME + "').combobox({\r\n";
					JS4 = JS4 + "data:json,\r\n";
					JS4 = JS4 + "valueField:'CODE',\r\n";
					JS4 = JS4 + "textField:'NAME',\r\n";
					JS4 = JS4 + "});\r\n";
					JS4 = JS4 + "});\r\n\r\n";
				}
			}
			JS5 = JS5 + JS31 + JS4 + JS32;
			ZJS = JS5 + JS1;
			ZJS = ZJS + "function sresetup(){\r\n";
			ZJS = ZJS + JS2;
			ZJS = ZJS + "}\r\n";
			ZJS = ZJS + "setTimeout(sresetup,1000);\r\n";
		}
		return ZJS;
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
