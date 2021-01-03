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

public class RedioControl extends ControlBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected InputTag mThis;
	protected String[][] mDictList;
	protected String Stonchange;
	protected String Stonclick;

	public RedioControl(InputTag inputtag, String[][] dictlist) {
		super();
		//判断是否有 / 结束标签 
		Tag end = inputtag.getEndTag();
		if(end == null) {
			inputtag.setEmptyXmlTag(false);
		}
		mThis = inputtag;
		mDictList = dictlist;
		mName = mThis.getAttribute("name");
		Stonchange = mThis.getAttribute("onchange");
		Stonclick = mThis.getAttribute("onclick");
	}

	public void setItemField(ItemField item) {
		mItem = item;
		//显示
		if (item.isShow()) {
			RedioTag rts[] = null;
			if (mDictList != null && mDictList.length > 0) {
				rts = new RedioTag[mDictList.length];
				for (int i = 0; i < mDictList.length; i++) {
					rts[i] = new RedioTag(mName);
					rts[i].setValue(mDictList[i][0]);
					rts[i].setHtmlText(mDictList[i][1]);
					if (Stonchange != null) {
						rts[i].setAttribute("@change", Stonchange);
					}
					if (Stonclick != null) {
						rts[i].setAttribute("@click", Stonclick);
					}
				}
			}
			setHtml(rts);
			this.mWrite = item.isWrite();
		//不显示	
		} else {
			String value = mItem.getShowValue()!= null ? mItem.getShowValue() : "";
			if (mDictList != null && mDictList.length > 0) {
				for (int i = 0; i < mDictList.length; i++) {
					if(value.equals(mDictList[i][0])) {
						value = mDictList[i][1];
						break;
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
						nlist.add(new TextNode(HTMLEncode(value)));
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
			validate.put("message", ""+this.mItem.getChineseName()+"必须选择一项!");
			validate.put("trigger", "change");
			validateList.add(validate);
			sb.append(this.mItem.getName()+":"+JSONUtils.toJSONString(validateList)+",\n");
			validateList = null;
			validate = null;
		}
		return sb.toString();
	}
	
	/**
	 * 装载到模板
	 * @param rts
	 */
	public void setHtml(RedioTag[] rts) {
		LabelTag lab = new LabelTag();
		lab.setText("</el-radio>");
		Node n = mThis.getParent();
		int ni = n.getChildren().indexOf(mThis);
		NodeList nlist = new NodeList();
		for (int ii = 0; ii < n.getChildren().size(); ii++) {
			if (ii != ni) {
				nlist.add(n.getChildren().elementAt(ii));
			} else if (ii == ni) {
				nlist.add(this.getElFormTagFist(this.mItem));//form验证 strat
				nlist.add(this.getElRaioTagFist(this.mItem));//单选框组 strat
				if (rts != null && rts.length > 0) {
					for (int i = 0; i < rts.length; i++) {
						nlist.add(rts[i]);
						nlist.add(rts[i].getHemlText());
						nlist.add(lab);
					}
				}
				nlist.add(this.getElRaioTagEnd(this.mItem));//单选框组 end
				nlist.add(this.getElFormTagEnd(this.mItem));//form验证 end
			}
		}
		n.getChildren().removeAll();
		n.getChildren().add(nlist);
	}
	
	/**
	 * 获取控件值
	 * @return
	 */
	public Object getInitVal() {
		String value = mItem.getValue()!= null ? "\""+mItem.getValue()+"\"" : "\"\"";
		return value;
	}
}