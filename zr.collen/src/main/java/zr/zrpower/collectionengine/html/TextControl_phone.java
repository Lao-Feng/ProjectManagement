package zr.zrpower.collectionengine.html;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;
import zr.zrpower.collectionengine.ItemField;

public class TextControl_phone extends ControlBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected InputTag mThis;

	public TextControl_phone(InputTag inputtag) {
		super();
		mThis = inputtag;
		mName = mThis.getAttribute("name");
	}

	public void setItemField(ItemField item) {
		mItem = item;
		if (item.isShow()) {
			if (item.isWrite()) {
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					mThis.setAttribute("value", item.getShowValue());
				}
				mThis.setAttribute("maxlength", Integer.toString(item.getLength()));
				// 判断是否必填System.out.println(mItem.isNull());
				if (!mItem.isNull()) {
					mThis.setAttribute("required", "true");// 编辑/必填
				}
				this.mWrite = true;
			} else {
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					mThis.setAttribute("value", item.getShowValue());
				}
				// mThis.setAttribute("maxlength", Integer.toString(item.getLength()));
				mThis.setAttribute("disabled", "true");// 不可编辑
				mThis.setAttribute("readonly", "readonly");
			}
		} else {// 不显示/隐藏
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
					mThis.setAttribute("value", item.getShowValue());
				}
				mThis.setAttribute("maxlength", Integer.toString(item.getLength()));
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
		if (item.isShow() && item.isNull() == false) {
			Node n = mThis.getParent();
			int ni = n.getChildren().indexOf(mThis);
			NodeList nlist = new NodeList();
			for (int ii = 0; ii < n.getChildren().size(); ii++) {
				nlist.add(n.getChildren().elementAt(ii));
				if (ii == ni) {
					nlist.add(this.getMustFillTag());
				}
			}
			n.getChildren().removeAll();
			n.getChildren().add(nlist);
		}
	}

	public String getValidateJS() {
		StringBuffer sb = new StringBuffer();
		if (mItem.isShow() && mItem.isNull() == false) {
			sb.append("if($(\"input[name='").append(mName).append("']\").val().length<1){\n");
			sb.append("   document.frmColl.").append(mName).append(".focus();\n");
			// sb.append(" $('#"+mName+"').modal({\n");
			// sb.append(" content: 'Popover via JavaScript',\n");
			// sb.append(" trigger: 'focus'\n");
			// sb.append(" })\n");
			// sb.append("
			// $.messager.alert('提示','[").append(mItem.getChineseName()).append("]不能为空，请填写！','warning');\r\n");

			sb.append("   return false;\n");
			sb.append("}\n");
		}
		//
		// if (this.mWrite) {
		//
		// sb.append("if($(\"input[name='").append(mName).append("']\").val().replace(/[^\\x00-\\xff]/gi,'pi').length>"
		// +
		// Integer.toString(mItem.getLength())).append("){\n");
		// sb.append("
		// $.messager.alert('提示','[").append(mItem.getChineseName()).append("]不能大于").
		// append(Integer.toString(mItem.getLength())).append("个字符，请检查！','warning');\r\n");
		//// sb.append(" alert('[").append(mItem.getChineseName()).append("]不能大于").
		//// append(Integer.toString(mItem.getLength())).append("个字符，请检查！');\n");
		//// sb.append(" document.frmColl.").append(mName).append(".focus();\n");
		// sb.append(" return false;\n");
		// sb.append("}\n");
		// }
		return sb.toString();
	}
}
