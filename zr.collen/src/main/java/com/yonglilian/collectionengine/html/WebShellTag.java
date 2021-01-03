package com.yonglilian.collectionengine.html;

import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.ObjectTag;

public class WebShellTag extends ObjectTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WebShellTag(String name) {
		this.setObjectClassId("CLSID:8856F961-340A-11D0-A96B-00C04FD705A2");
		this.setObjectWidth("0");
		this.setObjectHeight("0");
		this.setAttribute("name", name);
		this.setAttribute("id", name);
		LabelTag ltObj = new LabelTag();
		ltObj.setText("</OBJECT>");
		this.setEndTag(ltObj);
	}

}