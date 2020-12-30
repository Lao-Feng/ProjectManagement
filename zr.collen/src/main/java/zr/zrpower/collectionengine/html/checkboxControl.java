package zr.zrpower.collectionengine.html;

import com.alibaba.druid.support.json.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.util.NodeList;
import zr.zrpower.collectionengine.ItemField;

import java.util.*;

public class checkboxControl extends ControlBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected InputTag mThis;
	protected String[][] mDictList;
	protected String Stonchange;
	protected String Stonclick;

	public checkboxControl(InputTag inputtag, String[][] dictlist) {
		super();
		mThis = inputtag;
		mDictList = dictlist;
		mName = mThis.getAttribute("name");
		Stonchange = mThis.getAttribute("onchange");
		Stonclick = mThis.getAttribute("onclick");
		
		//判断是否有 / 结束标签 
		Tag end = inputtag.getEndTag();
		if(end == null) {
			inputtag.setEmptyXmlTag(false);
		}
	}

	public void setItemField(ItemField item) {
		mItem = item;
        //显示
		if (item.isShow()) {
			checkboxTag rts[] = null;
			if (mDictList != null && mDictList.length > 0) {
				rts = new checkboxTag[mDictList.length];
				for (int i = 0; i < mDictList.length; i++) {
					rts[i] = new checkboxTag(this.mItem.getName());// + "_checkbox" + i
					rts[i].setValue(mDictList[i][0]);
					rts[i].setHtmlText(mDictList[i][1]);
					if (Stonchange != null) {
						rts[i].setAttribute("@change", "\""+Stonchange+"\"");
					}
					if (Stonclick != null) {
						rts[i].setAttribute("@click", "\""+Stonclick+"\"");
					}
				}
			}
			setHtml(rts);
			this.mWrite = item.isWrite();
		} else {
			String value = mItem.getShowValue()!= null ? mItem.getShowValue().trim().toString() : "";
			List<String> nameLise = new ArrayList<>();
			if (mDictList != null && mDictList.length > 0) {
				String[] vlist = value.length()>0 ? value.split(",") : null;
				for (int i = 0; i < mDictList.length; i++) {
					if(vlist!=null && vlist.length>0) {
						for(int k = 0; k<vlist.length; k++) {
							if(vlist[k].equals(mDictList[i][0])) {
								nameLise.add(mDictList[i][1]);
								break;
							}
						}
					}
				}
			}
			Node n = mThis.getParent();
			int ni = n.getChildren().indexOf(mThis);
			NodeList nlist = new NodeList();
			for (int ii = 0; ii < n.getChildren().size(); ii++) {
				if (ii != ni) {
					nlist.add(n.getChildren().elementAt(ii));
				} else {
					if (item.isWrite()) {//显示值
						nlist.add(new TextNode(HTMLEncode(StringUtils.join(nameLise.toArray(), ","))));
					} else {//不显示值
						nlist.add(new TextNode(HTMLEncode("")));
					}
				}
			}
			n.getChildren().removeAll();
			n.getChildren().add(nlist);
		}
	}

	/**
	 * 验证vue+form
	 */
	public String getValidateJS() {
		StringBuffer sb = new StringBuffer();
		//必填
		if (this.mItem.isNull() == false) {
			List<Map<String,Object>> validateList = new ArrayList<Map<String,Object>>();
			Map<String,Object> validate = new HashMap<String, Object>();
			validate.put("required", true);
			validate.put("message", ""+this.mItem.getChineseName()+"至少选择一项!");
			validate.put("trigger", "change");
			validate.put("type", "array");
			validateList.add(validate);
			sb.append(this.mItem.getName()+":"+JSONUtils.toJSONString(validateList)+",\n");
			validateList = null;
			validate = null;
		}
		return sb.toString();
	}
	
	/**
	 * 获取控件值
	 * @return
	 */
	public Object getInitVal() {
		String value = mItem.getValue()!= null ? mItem.getValue().trim() : "";
		if(value.length()>0) {
			// 将字符串转换成集合
	        List<String> asList = Arrays.asList(value.split(","));
	        value = StringUtils.join(asList.toArray(), "','");
	        value = "['"+value+"']";
		}else {
			value = "[]";
		}
		return value;
	}
	
	/**
	 * 装载到模板
	 * @param rts
	 */
	public void setHtml(checkboxTag rts[]) {
		LabelTag lab = new LabelTag();
		lab.setText("</el-checkbox>");
		Node n = mThis.getParent();
		int ni = n.getChildren().indexOf(mThis);
		NodeList nlist = new NodeList();
		
		for (int ii = 0; ii < n.getChildren().size(); ii++) {
			if (ii != ni) {
				nlist.add(n.getChildren().elementAt(ii));
			} else if (ii == ni) {
				nlist.add(this.getElFormTagFist(this.mItem));//form验证 strat
				nlist.add(this.getElCheckboxTagFist(this.mItem));//多选框组 strat
				if (rts != null && rts.length > 0) {
					for (int i = 0; i < rts.length; i++) {
						nlist.add(rts[i]);
						nlist.add(rts[i].getHemlText());
						nlist.add(lab);
					}
				}
				nlist.add(this.getElCheckboxTagEnd(this.mItem));//多选框组 end
				nlist.add(this.getElFormTagEnd(this.mItem));//form验证 end
			}
		}
		n.getChildren().removeAll();
		n.getChildren().add(nlist);
	}

	/**
	 * 功能或作用：分析规则字符串，生成数组
	 * 
	 * @param strItems
	 *            字符串
	 * @param strItemMark
	 *            标识符
	 * @return 返回数组
	 */
//	private List<Object> GetArrayList(String strItems, String strItemMark) {
//		int intItemLen, i = 0, n = 0;
//		strItems = strItems + strItemMark;
//		String strItem;
//		List<Object> strList = new ArrayList<Object>();
//		intItemLen = strItems.length();
//		while (i < intItemLen) {
//			n = strItems.indexOf(strItemMark, i);
//			strItem = strItems.substring(i, n);
//			strList.add(strItem);
//			i = n + 1;
//		}
//		return strList;
//	}

}