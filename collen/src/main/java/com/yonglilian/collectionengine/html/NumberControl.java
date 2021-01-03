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

public class NumberControl extends TextControl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NumberControl(InputTag inputtag) {
		super(inputtag);
		inputtag.setTagName("el-input-number");
		//判断是否有 / 结束标签 
		Tag end = inputtag.getEndTag();
		if(end == null) {
			inputtag.setEmptyXmlTag(false);
		}
	}

	public void setItemField(ItemField item) {
		mItem = item;
		if (item.isShow()) {
			//可编辑
			if (item.isWrite()) {
				mThis.setAttribute("maxlength", Integer.toString(item.getLength()));
				this.mWrite = true;
			//只读	
			} else {
				mThis.setAttribute("maxlength", Integer.toString(item.getLength()));
				mThis.setAttribute("disabled", "true");
			}
		} else {//隐藏
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
			widh = widh <130 ? 130 :widh;
			mThis.setAttribute("style", "width:"+widh+"px;");
		}
		mThis.setAttribute("v-model", "form."+item.getName());
		mThis.setAttribute("placeholder", item.getChineseName());
		mThis.setAttribute("clearable", "true");
		mThis.setAttribute("size", "small");
		mThis.setAttribute(":step", "1");
		mThis.setAttribute(":precision", "0");
		mThis.removeAttribute("name");
		mThis.removeAttribute("type");

		// ------加入form验证和</>----------------
		Node n = mThis.getParent();
		int ni = n.getChildren().indexOf(mThis);
		NodeList nlist = new NodeList();
		Tag end =  mThis.getEndTag();
		for (int ii = 0; ii < n.getChildren().size(); ii++) {
			if (ii == ni) {
				nlist.add(this.getElFormTagFist(item));//form验证 strat
				nlist.add(n.getChildren().elementAt(ii));
				if(end!=null) {
					end.setTagName("el-input-number");
					mThis.setEndTag(end);
				}else {
					LabelTag lab = new LabelTag();
					lab.setTagName("/el-input-number");
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
		if(mItem.getShowValue()!=null) {
			if(mItem.getShowValue().trim().length()>0) {
				return Integer.valueOf(mItem.getShowValue());
			}
			return 0;
		}
		return 0;
	}
}