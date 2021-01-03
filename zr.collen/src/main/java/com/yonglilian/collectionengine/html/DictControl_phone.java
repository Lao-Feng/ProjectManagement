package com.yonglilian.collectionengine.html;

import com.yonglilian.collectionengine.ItemField;
import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;

public class DictControl_phone extends ControlBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected InputTag mThis;

	public DictControl_phone(InputTag inputtag) {
		super();
		mThis = inputtag;
		mName = mThis.getAttribute("name");
	}

	public void setItemField(ItemField item) {
		mItem = item;
		if (item.isShow()) {
			if (item.isWrite()) {
				SelectBoxTag st = new SelectBoxTag(mName, mThis.getAttribute("style"));
				st.setAttribute("code", item.getCodeTable());
				if (item.getValue() != null && !item.getValue().equals("")) {
					st.addOption(item.getValue(), item.getCodeValue());
				} else if (item.getDefaultValue() != null && !item.getDefaultValue().equals("")) {
					st.addOption(item.getDefaultValue(), item.getCodeValue());
				} else {
					st.addOption("请选择", "请选择");
				}
				Node n = mThis.getParent();
//				int ni = n.getChildren().indexOf(mThis);
				NodeList nlist = new NodeList();
//				NodeList tempNlist = new NodeList();
				// -------------------------------------------------------
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
				this.mWrite = true;
			} else {
				InputTag it = new InputTag();
				it.setAttribute("name", "_" + mName);
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					it.setAttribute("value", item.getShowValue());
					// it.setAttribute("size", Integer.toString(getGBlen(item.getShowValue())));
				} else {
					// it.setAttribute("size", Integer.toString(item.getLength()));
				}
				// it.setAttribute("class", "text");
				it.setAttribute("readonly", "readonly");
				Node n = mThis.getParent();
				int ni = n.getChildren().indexOf(mThis);
				NodeList nlist = new NodeList();
				for (int ii = 0; ii < n.getChildren().size(); ii++) {
					if (ii == ni) {
						String mValue = null;
						if (item.getValue() != null && !item.getValue().equals("")) {
							mValue = item.getValue();
						} else if (item.getDefaultValue() != null && !item.getDefaultValue().equals("")) {
							mValue = item.getDefaultValue();
						}
						if (mValue != null) {
							InputTag inputTag = new InputTag();
							inputTag.setAttribute("Type", "Hidden");
							inputTag.setAttribute("id", item.getName());
							inputTag.setAttribute("name", item.getName());
							inputTag.setAttribute("value", mValue);
							nlist.add(inputTag);
						}
						nlist.add(it);
					} else {
						nlist.add(n.getChildren().elementAt(ii));
					}
				}
				// -------------------------------------------------------
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
			}
		} else {// 隐藏
			ScriptTag st1 = new ScriptTag();
			st1.setLanguage("Javascript");
			st1.setType("text/javascript");
			LabelTag lt = new LabelTag();
			String SCRIPT = "";
			SCRIPT += "$('#" + mName + "_h').hide();";
			st1.setScriptCode(SCRIPT);
			lt.setText("</SCRIPT>");
			st1.setEndTag(lt);
			Node n = mThis.getParent();
			int ni = n.getChildren().indexOf(mThis);
			NodeList nlist = new NodeList();
			nlist.add(st1);
			if (item.isWrite()) {
				SelectBoxTag st = new SelectBoxTag(mName, mThis.getAttribute("style"));
				st.setAttribute("code", item.getCodeTable());
				if (item.getValue() != null && !item.getValue().equals("")) {
					st.addOption(item.getValue(), item.getCodeValue());
				} else if (item.getDefaultValue() != null && !item.getDefaultValue().equals("")) {
					st.addOption(item.getDefaultValue(), item.getCodeValue());
				} else {
					st.addOption("请选择", "请选择");
				}
//				NodeList tempNlist = new NodeList();
				// -------------------------------------------------------
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

	public int getGBlen(String inputstr) {
		if (inputstr == null || inputstr.equals("")) {
			return 0;
		}
		int strlen = 0;
		int strGBlen = 0;
		int i = 0;
		strlen = inputstr.length();
		for (i = 0; i < strlen; i++) {
			if (inputstr.charAt(i) > 255) {
				strGBlen += 2;
			} else {
				strGBlen++;
			}
		}
		return strGBlen;
	}
}
