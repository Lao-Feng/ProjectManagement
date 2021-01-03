package com.yonglilian.collectionengine.html;

import com.yonglilian.collectionengine.ItemField;
import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;

public class NumberControl_phone extends TextControl_phone {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NumberControl_phone(InputTag inputtag) {
		super(inputtag);
	}

	public void setItemField(ItemField item) {
		mItem = item;
		if (item.isShow()) {
			if (item.isWrite()) {
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					mThis.setAttribute("value", item.getShowValue());
				} else {
					mThis.setAttribute("value", "0");
				}
				mThis.setAttribute("maxlength", Integer.toString(item.getLength()));
				mThis.setAttribute("onKeyPress", "numberInput()");
				this.mWrite = true;
			} else {
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					mThis.setAttribute("value", item.getShowValue());
				} else {
					mThis.setAttribute("value", "0");
				}
				mThis.setAttribute("maxlength", Integer.toString(item.getLength()));
				mThis.setAttribute("onKeyPress", "numberInput()");
				mThis.setAttribute("readonly", "readonly");
			}
		} else {// 隐藏
			ScriptTag st = new ScriptTag();
			st.setLanguage("Javascript");
			st.setType("text/javascript");
			LabelTag lt = new LabelTag();
			String SCRIPT = "";
			SCRIPT += "$('#" + mName + "_h').hide();";
			st.setScriptCode(SCRIPT);
			lt.setText("</SCRIPT>");
			st.setEndTag(lt);
			Node n = mThis.getParent();
			int ni = n.getChildren().indexOf(mThis);
			NodeList nlist = new NodeList();
			nlist.add(st);
			if (item.isWrite()) {
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					mThis.setAttribute("value", item.getShowValue());
				} else {
					mThis.setAttribute("value", "0");
				}
				mThis.setAttribute("maxlength", Integer.toString(item.getLength()));
				mThis.setAttribute("onKeyPress", "numberInput()");
				this.mWrite = true;
			} else {
				for (int ii = 0; ii < n.getChildren().size(); ii++) {
					if (ii != ni) {
						nlist.add(n.getChildren().elementAt(ii));
					} else {
						nlist.add(new TextNode(HTMLEncode(item.getShowValue())));
					}
				}
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
			}
		}
		// ------加入必填提示----------------
		Node n = mThis.getParent();
		int ni = n.getChildren().indexOf(mThis);
		NodeList nlist = new NodeList();
		for (int ii = 0; ii < n.getChildren().size(); ii++) {
			nlist.add(n.getChildren().elementAt(ii));
			if (ii == ni) {
				if (item.isNull() == false && item.isWrite()) {
					mThis.setAttribute("required", "true");// 编辑/必填
				}
			}
		}
		n.getChildren().removeAll();
		n.getChildren().add(nlist);
		// -----------------------------------------
	}

	public String getValidateJS() {
		StringBuffer sb = new StringBuffer();
		if (this.mWrite) {
			if (this.mItem.isNull() == false) {
				sb.append("if (!checkintNull($(\"input[name='").append(mName).append("']\").val(),'")
						.append(mItem.getChineseName()).append("')){\n");
				sb.append("   return false;\n");
				sb.append("}\n");
			} else {
				sb.append("if (!checkint($(\"input[name='").append(mName).append("']\").val(),'")
						.append(mItem.getChineseName()).append("')){\n");
				sb.append("   return false;\n");
				sb.append("}\n");
			}
		}
		return sb.toString();
	}

}