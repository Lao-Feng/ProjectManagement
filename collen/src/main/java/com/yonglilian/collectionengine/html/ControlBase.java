package com.yonglilian.collectionengine.html;

import com.yonglilian.collectionengine.ItemField;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.Span;

import java.io.Serializable;

public abstract class ControlBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String mName;
	protected ItemField mItem;
	protected boolean mWrite;
	protected boolean isMustFill;

	public ControlBase() {
		mWrite = false;
		isMustFill = false;
	}

	abstract String getValidateJS();

	abstract void setItemField(ItemField item);

	public void setName(String name) {
		this.mName = name;
	}

	public String getName() {
		return this.mName;
	}

	public boolean IsWrite() {
		return this.mWrite;
	}

	// 得到必填*提示
	protected Span getMustFillTag() {
		Span span = new Span();
		return span;
	}

	// 转换字符串为HTML文本
	protected String HTMLEncode(String s) {
		if (s != null && !s.equals("")) {
			s = Replace(s, "<", "&lt;");
			s = Replace(s, ">", "&gt;");
			s = Replace(s, " ", "&nbsp;");
			s = Replace(s, "\"", "&quot;");
			s = s.replace('\r', '^');
			s = Replace(s, "^", "<br>");
		} else {
			s = "&nbsp;";
		}
		return s;
	}

	// 字符串替换函数
	private String Replace(String strSource, String strFrom, String strTo) {
		java.lang.String strDest = "";
		try {
			int intFromLen = strFrom.length();
			int intPos;
			while ((intPos = strSource.indexOf(strFrom)) != -1) {
				strDest = strDest + strSource.substring(0, intPos);
				strDest = strDest + strTo;
				strSource = strSource.substring(intPos + intFromLen);
			}
			strDest = strDest + strSource;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDest;
	}
	
	// ******************** ftl add strat *****************/
	/**
	 * 加载form验证  <el-form-item>
	 * @param item
	 * @return
	 */
	protected LabelTag getElFormTagFist(ItemField item) {
		LabelTag label = new LabelTag();
		label.setTagName("el-form-item");
		if(!item.isNull()) {
			//label.setAttribute("label", "*");
		}
		label.setAttribute("prop", "\""+item.getName()+"\"");
		return label;
	}
	/**
	 * 加载form验证  </el-form-item>
	 * @param item
	 * @return
	 */
	protected LabelTag getElFormTagEnd(ItemField item) {
		LabelTag label = new LabelTag();
		label.setTagName("/el-form-item");
		return label;
	}
	
	/**
	 * 加载单选组验证  <el-radio-group>
	 * @param item
	 * @return
	 */
	protected LabelTag getElRaioTagFist(ItemField item) {
		LabelTag label = new LabelTag();
		label.setTagName("el-radio-group");
		label.setAttribute("v-model", "\"form."+item.getName()+"\"");
		if(!item.isWrite()) {
			label.setAttribute("disabled", "true");
		}
		return label;
	}
	/**
	 * 加载单选组验证  </el-radio-group>
	 * @param item
	 * @return
	 */
	protected LabelTag getElRaioTagEnd(ItemField item) {
		LabelTag label = new LabelTag();
		label.setTagName("/el-radio-group");
		return label;
	}
	
	/**
	 * 加载多选组验证  <el-checkbox-group>
	 * @param item
	 * @return
	 */
	protected LabelTag getElCheckboxTagFist(ItemField item) {
		LabelTag label = new LabelTag();
		label.setTagName("el-checkbox-group");
		label.setAttribute("v-model", "\"form."+item.getName()+"\"");
		if(!item.isWrite()) {
			label.setAttribute("disabled", "true");
		}
		return label;
	}
	/**
	 * 加载多选组验证  </el-checkbox-group>
	 * @param item
	 * @return
	 */
	protected LabelTag getElCheckboxTagEnd(ItemField item) {
		LabelTag label = new LabelTag();
		label.setTagName("/el-checkbox-group");
		return label;
	}
		
}