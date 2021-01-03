package com.yonglilian.collectionengine.html;

import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.InputTag;

public class checkboxTag extends InputTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String mName;
	private TextNode td;
    //<el-checkbox v-model="checked">备选项</el-checkbox>
	public checkboxTag(String name) {
		super();
		this.mName = name;
		this.setTagName("el-checkbox");
		this.setAttribute("name", "\""+name+"\"");
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