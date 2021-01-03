package com.yonglilian.collectionengine.html;

import com.alibaba.druid.support.json.JSONUtils;
import com.yonglilian.collectionengine.ItemField;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.util.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateControl extends TextControl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Onchange = "";

	public DateControl(InputTag inputtag) {
		super(inputtag);
		inputtag.setTagName("el-date-picker");
		//判断是否有 / 结束标签 
		Tag end = inputtag.getEndTag();
		if(end == null) {
			inputtag.setEmptyXmlTag(false);
		}
	}

	public void setItemField(ItemField item) {
		mItem = item;
		Tag end = mThis.getEndTag();
		if (item.isShow()) {
			if (item.isWrite()) {
				Onchange = this.setOnChange(mThis.getAttribute("onchange"));
				mThis.setAttribute("maxlength", "20");
				this.mWrite = true;
			} else {
				mThis.setAttribute("maxlength", "20");
				mThis.setAttribute("disabled", "true");
			}
		} else {
			String value = mItem.getShowValue()!= null ? mItem.getShowValue() : "";
			Node n = mThis.getParent();
			int ni = n.getChildren().indexOf(mThis);
			NodeList nlist = new NodeList();
			for (int ii = 0; ii < n.getChildren().size(); ii++) {
				if (ii != ni) {
					nlist.add(n.getChildren().elementAt(ii));
				} else {
					if (item.isWrite()) {//显示值
						nlist.add(new TextNode(HTMLEncode(value)));
					} else {//不显示值
						nlist.add(new TextNode(HTMLEncode("")));
					}
				}
			}
			n.getChildren().removeAll();
			n.getChildren().add(nlist);
		}
		//公共模块
		String tmp = mThis.getAttribute("size");
		if(tmp!=null) {
			int widh=Integer.valueOf(tmp)*8;
			mThis.setAttribute("style", "width:"+widh+"px;");
		}	
		mThis.setAttribute("v-model", "form."+item.getName());
		mThis.setAttribute("placeholder", item.getChineseName());
		mThis.setAttribute("clearable", "true");
		mThis.setAttribute("size", "small");
		mThis.setAttribute("value-format", "yyyy-MM-dd");
		mThis.removeAttribute("name");
		mThis.removeAttribute("type");
		

		// ------加入form验证和</>----------------
		Node n = mThis.getParent();
		int ni = n.getChildren().indexOf(mThis);
		NodeList nlist = new NodeList();
		end =  mThis.getEndTag();
		for (int ii = 0; ii < n.getChildren().size(); ii++) {
			if (ii == ni) {
				nlist.add(this.getElFormTagFist(item));//form验证 strat
				nlist.add(n.getChildren().elementAt(ii));
				if(end!=null) {
					end.setTagName("el-date-picker");
					mThis.setEndTag(end);
				}else {
					LabelTag lab = new LabelTag();
					lab.setTagName("/el-date-picker");
					nlist.add(lab);
				}
				nlist.add(this.getElFormTagEnd(item));//form验证 end
			}else {
				nlist.add(n.getChildren().elementAt(ii));
			}
		}
		n.getChildren().removeAll();
		n.getChildren().add(nlist);
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
			validate.put("message", ""+this.mItem.getChineseName()+"不能为空!");
			validate.put("trigger", "blur");
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
		String value = mItem.getShowValue()!= null ? "'"+mItem.getShowValue()+"'" : "''";
		return value;
	}

	public String getOnchange() {
		return this.Onchange;
	}

	private String setOnChange(String onchange) {
		String sTmp = "";
		StringBuffer sTmp1 = new StringBuffer();
		if (onchange != null && !onchange.equals("")) {
			String[] saTmp = onchange.split("=");
			if (saTmp != null && saTmp.length > 0) {
				for (int i = 0; i < saTmp.length; i++) {
					if (saTmp[i].equalsIgnoreCase("equstr(this)")) {
						sTmp1.append("equstr(document.all." + mName + ");");
					} else {
						sTmp1.append("document.all.").append(saTmp[i]).append("=");
					}
				}
				sTmp = sTmp1.toString();
				sTmp = sTmp.substring(0, sTmp.length() - 1);
			}
		}
		return sTmp;
	}
}