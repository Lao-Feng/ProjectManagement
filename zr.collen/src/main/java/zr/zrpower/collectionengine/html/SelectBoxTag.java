package zr.zrpower.collectionengine.html;

import org.htmlparser.Tag;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.util.NodeList;

public class SelectBoxTag extends SelectTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SelectBoxTag(String name, String css) {
		super();
		this.setTagName("el-select");
		if (css != null) {
			this.setAttribute("style", css);
		}
		//判断是否有 / 结束标签 
		Tag end = this.getEndTag();
		if(end == null) {
			this.setEmptyXmlTag(false);
		}
		if(end!=null) {
			end.setTagName("el-select");
			this.setEndTag(end);
		}else {
			LabelTag lt1 = new LabelTag();
			lt1.setText("</el-select>");
			this.setEndTag(lt1);
		}
		
		//公共模块
		this.setAttribute("v-model", "form."+name);
		this.setAttribute("clearable", "true");
		this.setAttribute("size", "small");
		this.removeAttribute("name");
		this.removeAttribute("type");
		this.removeAttribute("id");
	}

	public void addOption(String value, String text) {
		OptionTag ot = new OptionTag();
		ot.setTagName("el-option");
		ot.setAttribute("label", "\""+text+"\"");
		ot.setAttribute("value", "\""+value+"\"");
//		NodeList nt = new NodeList();
//		nt.add(new TextNode(text));
//		ot.setChildren(nt);
		LabelTag lt = new LabelTag();
		lt.setText("</el-option>");
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
		ot.setTagName("el-option");
		ot.setAttribute("label", "\""+text+"\"");
		ot.setAttribute("value", "\""+value+"\"");
		ot.setAttribute("Selected", "true");
		NodeList nt = new NodeList();
		nt.add(new TextNode(text));
		ot.setChildren(nt);
		LabelTag lt = new LabelTag();
		lt.setText("</el-option>");
		ot.setEndTag(lt);
		NodeList nl = this.getChildren();
		if (nl == null) {
			nl = new NodeList(ot);
			this.setChildren(nl);
		} else {
			nl.add(ot);
		}
	}
	
	public void addSelectedOptionVue(String value, String text) {
		OptionTag ot = new OptionTag();
		ot.setTagName("el-option");
		ot.setAttribute("label", "\""+text+"\"");
		ot.setAttribute("value", "\""+value+"\"");
		ot.setAttribute("Selected", "true");
		NodeList nt = new NodeList();
		nt.add(new TextNode(text));
		ot.setChildren(nt);
		LabelTag lt = new LabelTag();
		lt.setText("</el-option>");
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