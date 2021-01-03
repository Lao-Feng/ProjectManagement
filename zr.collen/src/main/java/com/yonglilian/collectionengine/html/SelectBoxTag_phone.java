package com.yonglilian.collectionengine.html;

import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.util.NodeList;

public class SelectBoxTag_phone extends SelectTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SelectBoxTag_phone(String name) {
		super();
		this.setAttribute("name", name);
		this.setAttribute("id", name);
		this.setAttribute("data-am-selected", " ");
		// this.setAttribute("class","easyui-combobox");
		LabelTag lt1 = new LabelTag();
		lt1.setText("</Select>");
		this.setEndTag(lt1);
	}

	public void addOption(String value, String text) {
		OptionTag ot = new OptionTag();
		ot.setAttribute("value", value);
		NodeList nt = new NodeList();
		nt.add(new TextNode(text));
		ot.setChildren(nt);
		LabelTag lt = new LabelTag();
		lt.setText("</Option>");
		ot.setEndTag(lt);
		NodeList nl = this.getChildren();
		if (nl == null) {
			nl = new NodeList(ot);
			this.setChildren(nl);
		} else {
			nl.add(ot);
		}
	}

	public void addSelectedOption(String value, String text) {
		OptionTag ot = new OptionTag();
		ot.setAttribute("value", value);
		ot.setAttribute("Selected", "true");
		NodeList nt = new NodeList();
		nt.add(new TextNode(text));
		ot.setChildren(nt);
		LabelTag lt = new LabelTag();
		lt.setText("</Option>");
		ot.setEndTag(lt);
		NodeList nl = this.getChildren();
		if (nl == null) {
			nl = new NodeList(ot);
			this.setChildren(nl);
		} else {
			nl.add(ot);
		}
	}

	public void clearOption() {
		this.setChildren(null);
	}

}