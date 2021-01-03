package com.yonglilian.collectionengine.html;

import com.yonglilian.collectionengine.ItemField;
import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;

public class DateControl_phone extends TextControl_phone {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Onchange = "";

	public DateControl_phone(InputTag inputtag) {
		super(inputtag);
	}

	public void setItemField(ItemField item) {
		mItem = item;
		if (item.isShow()) {
			if (item.isWrite()) {
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					mThis.setAttribute("value", item.getShowValue());
				}
				Onchange = this.setOnChange(mThis.getAttribute("onchange"));
				mThis.setAttribute("maxlength", "10");
				
				//int gsize = 20;
				mThis.setAttribute("data-am-datepicker", "{format: 'yyyy-mm-dd'}");
				mThis.setAttribute("readonly", "true");
				Node n = mThis.getParent();
				int ni = n.getChildren().indexOf(mThis);
				NodeList nlist = new NodeList();
				/*
				 * for (int ii = 0; ii < n.getChildren().size() + 1; ii++) { if (ii <= ni) {
				 * nlist.add(n.getChildren().elementAt(ii)); } else if (ii == ni + 1) {
				 * nlist.add(getDateBtn(this.mName)); } else {
				 * nlist.add(n.getChildren().elementAt(ii - 1)); } }
				 */

				for (int ii = 0; ii < n.getChildren().size(); ii++) {
					nlist.add(n.getChildren().elementAt(ii));
					if (ii == ni) {
						// nlist.add(getDateBtn(this.mName));
						if (item.isNull() == false) {
							mThis.setAttribute("required", "true");// 编辑/必填
						}
						// -----------------------------------------
					}
				}
				// -------------------------------------------------------
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
				this.mWrite = true;
			} else {
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					mThis.setAttribute("value", item.getShowValue());
				}
				mThis.setAttribute("maxlength", "20");
				mThis.setAttribute("data-am-datepicker", "{format: 'yyyy-mm-dd'}");
				mThis.setAttribute("readonly", "true");
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
				mThis.setAttribute("maxlength", "20");
				mThis.setAttribute("data-am-datepicker", "{format: 'yyyy-mm-dd'}");
				mThis.setAttribute("readonly", "true");
				for (int ii = 0; ii < n.getChildren().size() + 1; ii++) {
					if (ii <= ni) {
						nlist.add(n.getChildren().elementAt(ii));
					} else if (ii == ni + 1) {
						// nlist.add(getDateBtn(this.mName));
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

	public String getOnchange() {
		return this.Onchange;
	}

	private String setOnChange(String onchange) {
		String sTmp = "";
		return sTmp;
	}
}