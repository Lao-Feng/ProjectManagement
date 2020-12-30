package zr.zrpower.collectionengine.html;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.util.NodeList;
import zr.zrpower.collectionengine.ItemField;

public class TextAreaControl_phone extends ControlBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected TextareaTag mThis;

	public TextAreaControl_phone(TextareaTag tag) {
		this.mThis = tag;
		this.mName = tag.getAttribute("name");
	}

	public void setItemField(ItemField item) {
		mItem = item;
		if (item.isShow()) {
			if (item.isWrite()) {
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					NodeList nt = new NodeList();
					nt.add(new TextNode(item.getShowValue()));
					this.mThis.setChildren(nt);
				}
				// System.out.println(this.mThis.getAttribute("rows")+"==="+this.mThis.getAttribute("cols"));//通过taxt获取行，列数
				double h = Double.valueOf(this.mThis.getAttribute("rows")) * 15;
				mThis.setAttribute("style", "height:" + h + "px");// style="width:100%;height:60px"
				// 判断是否必填System.out.println(mItem.isNull());
				if (!mItem.isNull()) {
					mThis.setAttribute("required", "true");// 编辑/必填
				} else {
					// mThis.setAttribute("class", "Border_Add_Light");//编辑 不必填
				}
				this.mWrite = true;
			} else {
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					NodeList nt = new NodeList();
					nt.add(new TextNode(item.getShowValue()));
					this.mThis.setChildren(nt);
				}
				double h = Double.valueOf(this.mThis.getAttribute("rows")) * 15;
				mThis.setAttribute("style", "height:" + h + "px");
				// mThis.setAttribute("class", "Border_re_Light");//不能编辑
				mThis.setAttribute("readonly", "readonly");
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
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					NodeList nt = new NodeList();
					nt.add(new TextNode(item.getShowValue()));
					this.mThis.setChildren(nt);
				}
				double h = Double.valueOf(this.mThis.getAttribute("rows")) * 15;
				mThis.setAttribute("style", "height:" + h + "px");
				// mThis.setAttribute("class", "Border_Add_Light");//科编辑 不必填
				this.mWrite = true;
			} else {
				for (int ii = 0; ii < n.getChildren().size(); ii++) {
					if (ii != ni) {
						nlist.add(n.getChildren().elementAt(ii));
					} else {
						nlist.add(new TextNode(HTMLEncode(item.getShowValue())));
					}
				}
			}
			n.getChildren().removeAll();
			n.getChildren().add(nlist);
		}
		// ------加入必填提示----------------
		Node n = mThis.getParent();
		int ni = n.getChildren().indexOf(mThis);
		NodeList nlist = new NodeList();
		for (int ii = 0; ii < n.getChildren().size(); ii++) {
			nlist.add(n.getChildren().elementAt(ii));
			if (ii == ni) {
				if (item.isNull() == false && item.isWrite()) {
					mThis.setAttribute("required", "true");// 编辑/必填
				}
			}
		}
		n.getChildren().removeAll();
		n.getChildren().add(nlist);
		// -----------------------------------------
	}

	public String getValidateJS() {
		StringBuffer sb = new StringBuffer();
		if (this.mWrite) {
			if (this.mItem.isNull() == false) {
				sb.append("if($(\"input[name='").append(mName).append("']\").val().length<1){\n");
				// sb.append("
				// $.messager.alert('提示','[").append(mItem.getChineseName()).append("]不能为空，请填写！','warning');\r\n");
				sb.append("   document.frmColl.").append(mName).append(".focus();\n");
				sb.append("   return false;\n");
				sb.append("}\n");
			}
		}
		return sb.toString();
	}
}
