package com.yonglilian.collectionengine.html;

import com.yonglilian.collectionengine.ItemField;
import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;

public class RedioControl_phone extends ControlBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected InputTag mThis;
	protected String[][] mDictList;
	protected String Stonchange;
	protected String Stonclick;

	public RedioControl_phone(InputTag inputtag, String[][] dictlist) {
		super();
		mThis = inputtag;
		mDictList = dictlist;
		mName = mThis.getAttribute("name");
		Stonchange = mThis.getAttribute("onchange");
		Stonclick = mThis.getAttribute("onclick");
	}

	@SuppressWarnings("null")
	public void setItemField(ItemField item) {
		mItem = item;
		if (item.isShow()) {
			if (item.isWrite()) {
				RedioTag rts[] = null;
				LabelTag mLabel[] = null;
				if (mDictList != null && mDictList.length > 0) {
					rts = new RedioTag[mDictList.length];
					mLabel = new LabelTag[mDictList.length];
					for (int i = 0; i < mDictList.length; i++) {
						rts[i] = new RedioTag(mName);
						mLabel[i] = new LabelTag();
						mLabel[i].setAttribute("class", "am-radio");
						rts[i].setValue(mDictList[i][0]);
						rts[i].setHtmlText(mDictList[i][1]);
						rts[i].setAttribute("data-am-ucheck", "true");
						if (Stonchange != null) {
							rts[i].setAttribute("onchange", Stonchange);
						}
						if (Stonclick != null) {
							rts[i].setAttribute("onclick", Stonclick);
						}
						if (item.getValue() != null && item.getValue().equals(mDictList[i][0])) {
							rts[i].setCheck(true);
						} else if (item.getDefaultValue() != null && item.getDefaultValue().equals(mDictList[i][0])
								&& item.getValue() == null) {
							rts[i].setCheck(true);
						}
						// ---加入必填提示----------------
						if (item.isNull() == false) {
							rts[0].setAttribute("required", "true");// 编辑/必填
						}
						// mLabel[i].setText("</label>");
						mLabel[i].setEndTag(rts[i]);
						// mLabel[i].setChildren(rts[i]);
						// rts[i].getHemlText();
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
							// ------加入必填提示----------------
							if (item.isNull() == false) {
								// nlist.add(this.getMustFillTag());
							}
							// -----------------------------------------
						}
					}
				}
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
				this.mWrite = true;
			} else {
				RedioTag rts[] = null;
				LabelTag mLabel[] = null;
				if (mDictList != null && mDictList.length > 0) {
					rts = new RedioTag[mDictList.length];
					mLabel = new LabelTag[mDictList.length];
					for (int i = 0; i < mDictList.length; i++) {
						rts[i] = new RedioTag("_" + mName);
						mLabel[i] = new LabelTag();
						mLabel[i].setAttribute("class", "am-radio");
						rts[i].setValue(mDictList[i][0]);
						rts[i].setHtmlText(mDictList[i][1]);
						// rts[i].setAttribute("readonly","readonly");
						rts[i].setAttribute("disabled", "disabled");
						rts[i].setAttribute("data-am-ucheck", "true");

						if (item.getValue() != null && item.getValue().equals(mDictList[i][0])) {
							rts[i].setCheck(true);
						} else if (item.getDefaultValue() != null && item.getDefaultValue().equals(mDictList[i][0])
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
							// --------------------------------
							for (int i = 0; i < rts.length; i++) {

								nlist.add(mLabel[i]);
								nlist.add(rts[i].getHemlText());
								nlist.add(mLabe);
								// nlist.add(mLabel[i].getHemlText());
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
				RedioTag rts[] = null;
				if (mDictList != null && mDictList.length > 0) {
					rts = new RedioTag[mDictList.length];
					for (int i = 0; i < mDictList.length; i++) {
						rts[i] = new RedioTag(mName);
						// rts[i].setAttribute("class", "Border_Add_Light");
						rts[i].setValue(mDictList[i][0]);
						rts[i].setHtmlText(mDictList[i][1]);
						if (item.getValue() != null && item.getValue().equals(mDictList[i][0])) {
							rts[i].setCheck(true);
						} else if (item.getDefaultValue() != null && item.getDefaultValue().equals(mDictList[i][0])
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
							// ------加入必填提示----------------
							if (item.isNull() == false) {
								nlist.add(this.getMustFillTag());
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
					nlist.add(this.getMustFillTag());
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
}
