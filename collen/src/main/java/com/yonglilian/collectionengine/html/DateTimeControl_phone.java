package com.yonglilian.collectionengine.html;

import com.yonglilian.collectionengine.ItemField;
import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;

public class DateTimeControl_phone extends TextControl_phone {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Onchange = "";

	public DateTimeControl_phone(InputTag inputtag) {
		super(inputtag);
	}

	public void setItemField(ItemField item) {
		mItem = item;
		if (item.isShow()) {
			ScriptTag st = new ScriptTag();
			st.setLanguage("Javascript");
			st.setType("text/javascript");
			LabelTag lt = new LabelTag();
			String SCRIPT = "";
			SCRIPT = "$(function() {";
			SCRIPT += "  $('#" + mName + "').datetimepicker({";
			SCRIPT += "     format: 'yyyy-mm-dd hh:ii:ss',";
			SCRIPT += "     autoclose:true,";
			SCRIPT += "     language:'zh-CN'";
			SCRIPT += "  });";
			SCRIPT += "});";
			st.setScriptCode(SCRIPT);
			lt.setText("</SCRIPT>");
			st.setEndTag(lt);
			if (item.isWrite()) {
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					mThis.setAttribute("value", item.getShowValue());
				}
				Onchange = this.setOnChange(mThis.getAttribute("onchange"));
				mThis.setAttribute("maxlength", "19");

//				int gsize = 20;
				// mThis.setAttribute("data-options","formatter:myDateTimeformatter,parser:myDateTimeparser");
				// mThis.setAttribute("class", "easyui-datetimebox");
				Node n = mThis.getParent();
				int ni = n.getChildren().indexOf(mThis);
				NodeList nlist = new NodeList();
				nlist.add(st);
				for (int ii = 0; ii < n.getChildren().size() + 1; ii++) {
					if (ii <= ni) {
						nlist.add(n.getChildren().elementAt(ii));
					} else if (ii == ni + 1) {
						// nlist.add(getDateTimeBtn(this.mName));
						// ------加入必填提示----------------
						if (item.isNull() == false) {
							mThis.setAttribute("required", "true");// 编辑/必填
						}
						// -----------------------------------------
					} else {
						nlist.add(n.getChildren().elementAt(ii - 1));
					}
				}
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
				this.mWrite = true;
			} else {
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					mThis.setAttribute("value", item.getShowValue());
				}
				mThis.setAttribute("maxlength", "19");
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
				}
				Onchange = this.setOnChange(mThis.getAttribute("onchange"));
				mThis.setAttribute("maxlength", "19");
				for (int ii = 0; ii < n.getChildren().size() + 1; ii++) {
					if (ii <= ni) {
						nlist.add(n.getChildren().elementAt(ii));
					} else if (ii == ni + 1) {
						// nlist.add(getDateTimeBtn(this.mName));
						// ------加入必填提示----------------
						if (item.isNull() == false) {
							nlist.add(this.getMustFillTag());
						}
						// -----------------------------------------
					} else {
						nlist.add(n.getChildren().elementAt(ii - 1));
					}
				}
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
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
	}

	public String getValidateJS() {
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}

	public String getOnchange() {
		return this.Onchange;
	}

	private String setOnChange(String onchange) {
		String sTmp = "";
		return sTmp;
	}

}