package com.yonglilian.collectionengine.html;

import com.alibaba.druid.support.json.JSONUtils;
import com.yonglilian.collectionengine.ItemField;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.util.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextAreaControl extends ControlBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected TextareaTag mThis;

	public TextAreaControl(TextareaTag tag) {
		this.mThis = tag;
		mThis.setTagName("el-input");
		mThis.setEmptyXmlTag(false);
		this.mName = tag.getAttribute("name");
		//判断是否有 / 结束标签 
		Tag end = mThis.getEndTag();
		if(end == null) {
			mThis.setEmptyXmlTag(false);
		}				
	}

	public void setItemField(ItemField item) {
		mItem = item;
		mThis.setAttribute("maxlength", Integer.toString(item.getLength()));
		if (item.isShow()) {
			if (item.isWrite()) {
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					NodeList nt = new NodeList();
					nt.add(new TextNode(item.getShowValue()));
					this.mThis.setChildren(nt);
				}
				this.mWrite = true;
			} else {
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					NodeList nt = new NodeList();
					nt.add(new TextNode(item.getShowValue()));
					this.mThis.setChildren(nt);
				}
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
		String tmp = mThis.getAttribute("cols");
		if(tmp!=null) {
			int widh=Integer.valueOf(tmp)*8;
			mThis.setAttribute("style", "width:"+widh+"px;");
		}	
		mThis.setAttribute("v-model", "form."+item.getName());
		mThis.setAttribute("placeholder", item.getChineseName());
		mThis.setAttribute("show-word-limit", "true");
		mThis.setAttribute("clearable", "true");
		mThis.setAttribute("size", "small");
		mThis.setAttribute("type", "textarea");
		mThis.removeAttribute("name");

		
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
					end.setTagName("el-input");
					mThis.setEndTag(end);
				}else {
					LabelTag lab = new LabelTag();
					lab.setTagName("/el-input");
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
			validate = new HashMap<String, Object>();
			validate.put("min", 1);
			validate.put("max", Integer.toString(this.mItem.getLength()));
			validate.put("trigger", "blur");
			validate.put("message", "长度在 1 到 "+Integer.toString(this.mItem.getLength())+" 个字符");
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
		String value = mItem.getShowValue()!= null ? "\""+mItem.getShowValue()+"\"" : "\"\"";
		return value;
	}
}