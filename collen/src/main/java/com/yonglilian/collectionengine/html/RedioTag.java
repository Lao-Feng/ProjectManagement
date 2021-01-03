package com.yonglilian.collectionengine.html;

import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.InputTag;

public class RedioTag extends InputTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String mName;
	private TextNode td;

	public RedioTag(String name) {
		super();
		this.mName = name;
		this.setTagName("el-radio");
		this.setAttribute("v-model", "form."+mName);
		this.setAttribute("size", "small");
		this.removeAttribute("name");
		this.removeAttribute("type");
	}

	public void setValue(String value) {
		this.setAttribute("label", "\""+value+"\"");
		this.setAttribute("value", "\""+value+"\"");
	}

	public void setCheck(boolean check) {
		this.removeAttribute("checked");
		if (check) {
			this.setAttribute("checked", "true");
		}
	}

	public void setHtmlText(String str) {
		td = new TextNode(str + " ");
	}

	public TextNode getHemlText() {
		return td;
	}

}