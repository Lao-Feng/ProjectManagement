package zr.zrpower.collectionengine.html;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.*;
import org.htmlparser.util.NodeList;
import zr.zrpower.collectionengine.ItemField;

public class SelectControl_phone extends ControlBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected InputTag mThis;
	protected String[][] mDictList;
	protected SelectBoxTag_phone st;

	public SelectControl_phone(InputTag inputtag, String[][] dictlist) {
		super();
		mThis = inputtag;
		mDictList = dictlist;
//		int lwidth = 60;
		if (mThis.getAttribute("size") != null) {
			// lwidth = Integer.parseInt(mThis.getAttribute("size"))*7;
		}
		mName = mThis.getAttribute("name").toUpperCase();
		st = new SelectBoxTag_phone(mName);
		st.setAttribute("id", mName);
		st.setAttribute("data-am-selected", "{searchBox: 1}");
		String tmp = mThis.getAttribute("onchange");
		if (tmp != null) {
			st.setAttribute("onchange", tmp);
		}
		String tmp1 = mThis.getAttribute("onclick");
		if (tmp1 != null) {
			st.setAttribute("onclick", tmp1);
		}
		tmp1 = mThis.getAttribute("style");
		if (tmp1 != null) {
			// st.setAttribute("style", tmp1);
		} else {
			// st.setAttribute("style", "width:"+String.valueOf(lwidth));
		}
	}

	public void setArribute(String arrName, String arrValue) {
		st.setAttribute(arrName, arrValue);
	}

	public void setItemField(ItemField item) {
		mItem = item;
		// System.out.println(item.getChineseName());
		if (item.isShow()) {
			if (item.isWrite()) {
				if (item.isNull()) {
					st.addOption("", "");
				}
				if (mDictList != null && mDictList.length > 0) {
					for (int i = 0; i < mDictList.length; i++) {
						if (item.getValue() != null && item.getValue().equals(mDictList[i][0])) {
							st.addSelectedOption(mDictList[i][0], mDictList[i][1]);
						} else if (item.getDefaultValue() != null && item.getDefaultValue().equals(mDictList[i][0])
								&& item.getValue() == null) {
							st.addSelectedOption(mDictList[i][0], mDictList[i][1]);
						} else {
							st.addOption(mDictList[i][0], mDictList[i][1]);
						}
					}
				}
				Node n = mThis.getParent();
				int ni = n.getChildren().indexOf(mThis);
				NodeList nlist = new NodeList();
				NodeList tempNlist = new NodeList();
				for (int ii = 0; ii < n.getChildren().size(); ii++) {
					if (ii == ni) {
						Span span = new Span();
						LabelTag lt = new LabelTag();
						lt.setText("</SPAN>");
						span.setEndTag(lt);
						span.setAttribute("class", "");
						tempNlist.add(st);
						span.setChildren(tempNlist);
						nlist.add(span);
						if (item.isUnit()) {
							nlist.add(getUnitButton());
						}
						if (item.isUser()) {
							nlist.add(getPsnButton());
						}
						// -------------------------------------------------
						// ------加入必填提示----------------
						if (item.isNull() == false) {
							mThis.setAttribute("required", "true");// 编辑/必填
						}
						// -----------------------------------------
					} else {
						nlist.add(n.getChildren().elementAt(ii));
					}
				}
				// -------------------------------------------------------
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
				this.mWrite = true;
			} else {
				InputTag it = new InputTag();
				it.setAttribute("name", "_" + mName);
				// mThis.setAttribute("maxlength", Integer.toString(item.getLength()));
				it.setAttribute("disabled", "true");// 不可编辑
				it.setAttribute("readonly", "readonly");
				it.setAttribute("type", "text");
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					it.setAttribute("value", item.getShowValue());
					if (Integer.valueOf(getGBlen(item.getShowValue())) < 20) {
						// it.setAttribute("size", "20");
					} else {
						// it.setAttribute("size",
						// Integer.toString(getGBlen(item.getShowValue())));
					}
				} else {
					// it.setAttribute("size", mThis.getAttribute("size"));
				}
				// it.setAttribute("class", "easyui-combobox");
				it.setAttribute("readonly", "readonly");
				Node n = mThis.getParent();
				int ni = n.getChildren().indexOf(mThis);
				NodeList nlist = new NodeList();
				for (int ii = 0; ii < n.getChildren().size(); ii++) {
					if (ii != ni) {
						nlist.add(n.getChildren().elementAt(ii));
					} else if (ii == ni) {
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
						// --------------------------------
						nlist.add(it);
					}
				}
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
			}
		} else {// 隐藏
			ScriptTag st1 = new ScriptTag();
			st1.setLanguage("Javascript");
			st1.setType("text/javascript");
			LabelTag lt1 = new LabelTag();
			String SCRIPT = "";
			SCRIPT += "$('#" + mName + "_h').hide();";
			st1.setScriptCode(SCRIPT);
			lt1.setText("</SCRIPT>");
			st1.setEndTag(lt1);
			Node n = mThis.getParent();
			int ni = n.getChildren().indexOf(mThis);
			NodeList nlist = new NodeList();
			nlist.add(st1);
			if (item.isWrite()) {
				if (item.isNull()) {
					st.addOption("", "");
				}
				if (mDictList != null && mDictList.length > 0) {
					for (int i = 0; i < mDictList.length; i++) {
						if (item.getValue() != null && item.getValue().equals(mDictList[i][0])) {
							st.addSelectedOption(mDictList[i][0], mDictList[i][1]);
						} else if (item.getDefaultValue() != null && item.getDefaultValue().equals(mDictList[i][0])
								&& item.getValue() == null) {
							st.addSelectedOption(mDictList[i][0], mDictList[i][1]);
						} else {
							st.addOption(mDictList[i][0], mDictList[i][1]);
						}
					}
				}
				NodeList tempNlist = new NodeList();
				for (int ii = 0; ii < n.getChildren().size(); ii++) {
					if (ii == ni) {
						Span span = new Span();
						LabelTag lt = new LabelTag();
						lt.setText("</SPAN>");
						span.setEndTag(lt);
						// span.setAttribute("class", "Border_Add_Light");
						tempNlist.add(st);
						span.setChildren(tempNlist);
						nlist.add(span);
						// ----加入选择人员或单位----------------------
						if (item.isUnit()) {
							nlist.add(getUnitButton());
						}
						if (item.isUser()) {
							nlist.add(getPsnButton());
						}
						// -------------------------------------------------
						// ------加入必填提示----------------
						if (item.isNull() == false) {
							mThis.setAttribute("required", "true");// 编辑/必填
						}
						// -----------------------------------------
					} else {
						nlist.add(n.getChildren().elementAt(ii));
					}
				}
				// -------------------------------------------------------
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
				this.mWrite = true;
			} else {
				for (int ii = 0; ii < n.getChildren().size(); ii++) {
					if (ii != ni) {
						nlist.add(n.getChildren().elementAt(ii));
					} else {
						if (item.getShowValue() != null) {
							nlist.add(new TextNode(HTMLEncode(item.getShowValue())));
						} else {
							nlist.add(new TextNode(HTMLEncode("")));
						}
					}
				}
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
			}
		}
	}

	public String getValidateJS() {
		StringBuffer sb = new StringBuffer();
		// if (this.mWrite) {
		// if (this.mItem.isNull() == false) {
		// sb.append("if($(\"input[name='").append(mName).append("']\").val().length<1){\n");
		// sb.append(" document.frmColl.").append(mName).append(".focus();\n");
		// sb.append(" return false;\n");
		// sb.append("}\n");
		// }
		// }
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

	private ImageTag getUnitButton() {
		ImageTag imgBtn = new ImageTag();
		// imgBtn.setImageURL("/static/easyui/themes/default/images/searchbox_button.png");
		// imgBtn.setAttribute("onClick", "SelUnit('" + mName + "');");
		// imgBtn.setAttribute("style", "margin:0px 2px -5px 5px;cursor:pointer;");
		// imgBtn.setAttribute("alt", "选择单位");
		return imgBtn;
	}

	private ImageTag getPsnButton() {
		ImageTag imgBtn = new ImageTag();
		// imgBtn.setImageURL("/static/easyui/themes/default/images/searchbox_button.png");
		// imgBtn.setAttribute("onClick", "SelPsn('" + mName + "');");
		// imgBtn.setAttribute("style", "margin:0px 2px -5px 5px;cursor:pointer;");
		// imgBtn.setAttribute("alt", "选择人员");
		return imgBtn;
	}
}