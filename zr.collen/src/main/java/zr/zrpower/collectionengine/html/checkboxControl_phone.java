package zr.zrpower.collectionengine.html;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;
import zr.zrpower.collectionengine.ItemField;

import java.util.ArrayList;
import java.util.List;

public class checkboxControl_phone extends ControlBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected InputTag mThis;
	protected String[][] mDictList;
	protected String Stonchange;
	protected String Stonclick;

	public checkboxControl_phone(InputTag inputtag, String[][] dictlist) {
		super();
		mThis = inputtag;
		mDictList = dictlist;
		mName = mThis.getAttribute("name");
		Stonchange = mThis.getAttribute("onchange");
		Stonclick = mThis.getAttribute("onclick");
	}

	public void setItemField(ItemField item) {
		mItem = item;
		List<Object> ValueList = new ArrayList<Object>();
		ValueList = GetArrayList(item.getValue(), ",");
		String cValue = "";
		String showValue = item.getShowValue();
		if (item.isShow()) {
			if (item.isWrite()) {
				checkboxTag rts[] = null;
				LabelTag mLabel[] = null;
				if (mDictList != null && mDictList.length > 0) {
					rts = new checkboxTag[mDictList.length];
					mLabel = new LabelTag[mDictList.length];
					for (int i = 0; i < mDictList.length; i++) {
						mLabel[i] = new LabelTag();
						mLabel[i].setAttribute("class", "am-checkbox");
						// rts[i] = new checkboxTag(mName);
						rts[i] = new checkboxTag(mName + "_checkbox" + i);
						// rts[i].setAttribute("class", "Border_Add_Light");
						rts[i].setValue(mDictList[i][0]);
						rts[i].setHtmlText(mDictList[i][1]);
						rts[i].setAttribute("data-am-ucheck", "true");
						if (Stonchange != null) {
							rts[i].setAttribute("onchange", Stonchange);
						}
						if (Stonclick != null) {
							rts[i].setAttribute("onclick", Stonclick);
						}
						for (int listlen = 0; listlen < ValueList.size(); listlen++) {
							cValue = ValueList.get(listlen).toString();
							if (cValue.equals(mDictList[i][0])) {
								rts[i].setCheck(true);
							}
						}
						if (item.getDefaultValue() != null && item.getDefaultValue().equals(mDictList[i][0])
								&& item.getValue() == null) {
							rts[i].setCheck(true);
						}
						//----------------加入必填提示----------------//
						if (item.isNull() == false) {
							rts[0].setAttribute("required", "true");// 编辑/必填
						}
						mLabel[i].setEndTag(rts[i]);
					}
				}
				Node n = mThis.getParent();
				int ni = n.getChildren().indexOf(mThis);
				NodeList nlist = new NodeList();
				LabelTag mLabe = new LabelTag();
				mLabe.setText("</label>");
				for (int ii = 0; ii < n.getChildren().size(); ii++) {
					if (ii != ni) {
						nlist.add(n.getChildren().elementAt(ii));
					} else if (ii == ni) {
						if (rts != null && rts.length > 0) {
							for (int i = 0; i < rts.length; i++) {
								nlist.add(mLabel[i]);
								nlist.add(rts[i].getHemlText());
								nlist.add(mLabe);
							}
						}
					}
				}
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
				this.mWrite = true;
			} else {
				checkboxTag rts[] = null;
				LabelTag mLabel[] = null;
				if (mDictList != null && mDictList.length > 0) {
					rts = new checkboxTag[mDictList.length];
					mLabel = new LabelTag[mDictList.length];
					for (int i = 0; i < mDictList.length; i++) {
						rts[i] = new checkboxTag(mName + "_checkbox" + i);
						mLabel[i] = new LabelTag();
						mLabel[i].setAttribute("class", "am-checkbox");
						rts[i].setValue(mDictList[i][0]);
						rts[i].setHtmlText(mDictList[i][1]);
						// rts[i].setAttribute("readonly","readonly");
						rts[i].setAttribute("disabled", "disabled");
						for (int listlen = 0; listlen < ValueList.size(); listlen++) {
							cValue = ValueList.get(listlen).toString();
							if (cValue.equals(mDictList[i][0])) {
								rts[i].setCheck(true);
							}
						}
						if (item.getDefaultValue() != null && item.getDefaultValue().equals(mDictList[i][0])
								&& item.getValue() == null) {
							rts[i].setCheck(true);
						}
						mLabel[i].setEndTag(rts[i]);
					}
				}
				Node n = mThis.getParent();
				int ni = n.getChildren().indexOf(mThis);
				NodeList nlist = new NodeList();
				LabelTag mLabe = new LabelTag();
				mLabe.setText("</label>");
				for (int ii = 0; ii < n.getChildren().size(); ii++) {
					if (ii != ni) {
						nlist.add(n.getChildren().elementAt(ii));
					} else if (ii == ni) {
						if (rts != null && rts.length > 0) {
							if (item.getValue() != null) {
								InputTag inputTag = new InputTag();
								inputTag.setAttribute("Type", "Hidden");
								inputTag.setAttribute("id", item.getName());
								inputTag.setAttribute("name", item.getName());
								inputTag.setAttribute("value", item.getValue());
								nlist.add(inputTag);
							} else if (item.getDefaultValue() != null) {
								InputTag inputTag = new InputTag();
								inputTag.setAttribute("Type", "Hidden");
								inputTag.setAttribute("id", item.getName());
								inputTag.setAttribute("name", item.getName());
								inputTag.setAttribute("value", item.getDefaultValue());
								nlist.add(inputTag);
							}
							// ----------------------------------
							for (int i = 0; i < rts.length; i++) {

								nlist.add(mLabel[i]);
								nlist.add(rts[i].getHemlText());
								nlist.add(mLabe);
							}
						}
					}
				}
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
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
				checkboxTag rts[] = null;
				if (mDictList != null && mDictList.length > 0) {
					rts = new checkboxTag[mDictList.length];
					for (int i = 0; i < mDictList.length; i++) {
						rts[i] = new checkboxTag(mName + "_checkbox" + i);
						// rts[i].setAttribute("class", "Border_Add_Light");
						rts[i].setValue(mDictList[i][0]);
						rts[i].setHtmlText(mDictList[i][1]);
						for (int listlen = 0; listlen < ValueList.size(); listlen++) {
							cValue = ValueList.get(listlen).toString();
							if (cValue.equals(mDictList[i][0])) {
								rts[i].setCheck(true);
							}
						}
						if (item.getDefaultValue() != null && item.getDefaultValue().equals(mDictList[i][0])
								&& item.getValue() == null) {
							rts[i].setCheck(true);
						}
					}
				}
				for (int ii = 0; ii < n.getChildren().size(); ii++) {
					if (ii != ni) {
						nlist.add(n.getChildren().elementAt(ii));
					} else if (ii == ni) {
						if (rts != null && rts.length > 0) {
							for (int i = 0; i < rts.length; i++) {
								nlist.add(rts[i]);
								nlist.add(rts[i].getHemlText());
							}
							//----------------加入必填提示----------------
							if (item.isNull() == false) {
							}
							// -----------------------------------------
						}
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
						// ----得到显示的值--------
						if (showValue == null) {
							showValue = "";
							if (mDictList != null && mDictList.length > 0) {
								for (int Dictlen = 0; Dictlen < mDictList.length; Dictlen++) {
									for (int listlen = 0; listlen < ValueList.size(); listlen++) {
										cValue = ValueList.get(listlen).toString();
										if (cValue.equals(mDictList[Dictlen][0])) {
											if (showValue.length() == 0) {
												showValue = mDictList[Dictlen][1];
											} else {
												showValue = showValue + "," + mDictList[Dictlen][1];
											}
										}
									}
								}
							}
						}
						// ------------
						nlist.add(new TextNode(HTMLEncode(showValue)));
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
					// nlist.add(this.getMustFillTag());
				}
			}
		}
		n.getChildren().removeAll();
		n.getChildren().add(nlist);
		// -----------------------------------------
	}

	public String getValidateJS() {
		String strRevalue = "";
		return strRevalue;
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
	private List<Object> GetArrayList(String strItems, String strItemMark) {
		int intItemLen, i = 0, n = 0;
		strItems = strItems + strItemMark;
		String strItem;
		List<Object> strList = new ArrayList<Object>();
		intItemLen = strItems.length();
		while (i < intItemLen) {
			n = strItems.indexOf(strItemMark, i);
			strItem = strItems.substring(i, n);
			strList.add(strItem);
			i = n + 1;
		}
		return strList;
	}
}