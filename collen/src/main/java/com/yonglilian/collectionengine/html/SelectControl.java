package com.yonglilian.collectionengine.html;

import com.alibaba.druid.support.json.JSONUtils;
import com.yonglilian.collectionengine.ItemField;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SelectControl extends ControlBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected InputTag mThis;
	protected String[][] mDictList;
	protected SelectBoxTag st;

	public SelectControl(InputTag inputtag, String[][] dictlist) {
		super();
		inputtag.setTagName("el-select");
		//判断是否有 / 结束标签 
		Tag end = inputtag.getEndTag();
		if(end == null) {
			inputtag.setEmptyXmlTag(false);
		}
		mThis = inputtag;
		mDictList = dictlist;
		mName = mThis.getAttribute("name").toUpperCase();
		st = new SelectBoxTag(mName, mThis.getAttribute("style"));
		String tmp = mThis.getAttribute("change");
		if (tmp != null) {
			st.setAttribute("@change", ""+tmp);
		}
		tmp = mThis.getAttribute("onclick");
		if (tmp != null) {
			st.setAttribute("@click", ""+tmp);
		}
		tmp = mThis.getAttribute("style");
		if (tmp != null) {
			st.setAttribute("style", tmp);
		} else {
			tmp = mThis.getAttribute("size");
			if(tmp!=null) {
				int widh=Integer.valueOf(tmp)*8;
				st.setAttribute("style", "width:"+widh+"px;");
			}else {
				st.setAttribute("style", "width:100%;");
			}
		}  
		if(mThis.getAttribute("onchange")!=null) {
			st.setAttribute("@change", ""+mThis.getAttribute("onchange"));
		}
	}

	public void setArribute(String arrName, String arrValue) {
		st.setAttribute(arrName, arrValue);
	}

	public void setItemField(ItemField item) {
		mItem = item;
		if (item.isShow()) {
			//填充下拉值
			if (mDictList != null && mDictList.length > 0) {
				for (int i = 0; i < mDictList.length; i++) {
					st.addOption(mDictList[i][0], mDictList[i][1]);
				}
			}
			this.mWrite = item.isWrite();
			setHtml(item);
		} else {
			//获得值
			String value = "";
			if (mDictList != null && mDictList.length > 0) {
				for (int i = 0; i < mDictList.length; i++) {
					if(item.getShowValue() == mDictList[i][0]) {
						value = mDictList[i][1];
						break;
					}
				}
			}
			Node n = mThis.getParent();
			int ni = n.getChildren().indexOf(mThis);
			NodeList nlist = new NodeList();
			for (int ii = 0; ii < n.getChildren().size(); ii++) {
				if (ii != ni) {
					nlist.add(n.getChildren().elementAt(ii));
				} else {
					if (item.isWrite()) {//显示值
						nlist.add(new TextNode(HTMLEncode(value)));
					} else {//不显示值
						nlist.add(new TextNode(HTMLEncode("")));
					}
				}
			}
			n.getChildren().removeAll();
			n.getChildren().add(nlist);
			
			this.mWrite = item.isWrite();
			setHtml(item);
		}
		
	}
	
	/**
	 * 装载到html
	 * @param item
	 */
	public void setHtml(ItemField item) {
		Node n = mThis.getParent();
		int ni = n.getChildren().indexOf(mThis);
		NodeList nlist = new NodeList();
		for (int ii = 0; ii < n.getChildren().size(); ii++) {
			if (ii == ni) {
				nlist.add(this.getElFormTagFist(item));//form验证 strat
				if(!item.isWrite()) {
					st.setAttribute("disabled", "true");
				}
				nlist.add(st);
				if(item.isWrite()) {
					if (item.isUnit()) {
						nlist.add(getUnitButton(item.getName()));
					}
					if (item.isUser()) {
						nlist.add(getPsnButton(item.getName()));
					}
				}				
				nlist.add(this.getElFormTagEnd(item));//form验证 end
			} else {
				nlist.add(n.getChildren().elementAt(ii));
			}
		}
		n.getChildren().removeAll();
		n.getChildren().add(nlist);
	}

	/**
	 * 验证vue+form
	 */
	public String getValidateJS() {
		StringBuffer sb = new StringBuffer();
		//必填
		if (this.mItem.isNull() == false) {
			List<Map<String,Object>> validateList = new ArrayList<Map<String,Object>>();
			Map<String,Object> validate = new HashMap<String, Object>();
			validate.put("required", true);
			validate.put("message", ""+this.mItem.getChineseName()+"必须选择一项!");
			validate.put("trigger", "change");
			validateList.add(validate);
			sb.append(this.mItem.getName()+":"+JSONUtils.toJSONString(validateList)+",\n");
			validateList = null;
			validate = null;
		}
		return sb.toString();
	}
	
	/**
	 * 获取控件值
	 * @return
	 */
	public Object getInitVal() {
		String value = mItem.getValue()!= null ? "\""+mItem.getValue()+"\"" : "\"\"";
		return value;
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

	private NodeList getUnitButton(String field) {
		NodeList nlist = new NodeList();
		NodeList tempNlist = new NodeList();
		Span li = new Span();
		li.setTagName("li");
		li.setAttribute("class", "el-icon-search");
		LabelTag lab = new LabelTag();
		lab.setText("</li>");
		li.setEndTag(lab);
		tempNlist.add(li);
		
		Span a = new Span();
		a.setTagName("a");
		a.setAttribute("href", "javascript:void(0);");
		a.setAttribute("@click", "on_query_unit('"+field+"')");
		a.setAttribute("title", "选择单位");
		a.setAttribute("style","font-size:20px;margin:5px;vertical-align:middle;");
		lab = new LabelTag();
		lab.setText("</a>");
		a.setEndTag(lab);
		a.setChildren(tempNlist);
		
		nlist.add(a);
		return nlist;
	}

	private NodeList getPsnButton(String field) {
		NodeList nlist = new NodeList();
		NodeList tempNlist = new NodeList();
		Span li = new Span();
		li.setTagName("li");
		li.setAttribute("class", "el-icon-search");
		LabelTag lab = new LabelTag();
		lab.setText("</li>");
		li.setEndTag(lab);
		tempNlist.add(li);
		
		Span a = new Span();
		a.setTagName("a");
		a.setAttribute("href", "javascript:void(0);");
		a.setAttribute("@click", "on_query_user('"+field+"')");
		a.setAttribute("title", "选择用户");
		a.setAttribute("style","font-size:20px;margin:5px;vertical-align:middle;");
		lab = new LabelTag();
		lab.setText("</a>");
		a.setEndTag(lab);
		a.setChildren(tempNlist);
		
		nlist.add(a);
		return nlist;
	}
}