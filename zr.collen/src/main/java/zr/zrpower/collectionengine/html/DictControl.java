package zr.zrpower.collectionengine.html;

import com.alibaba.druid.support.json.JSONUtils;
import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;
import zr.zrpower.collectionengine.ItemField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictControl extends ControlBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected InputTag mThis;

	public DictControl(InputTag inputtag) {
		super();
		mThis = inputtag;
		mName = mThis.getAttribute("name");
		
	}

	public void setItemField(ItemField item) {
		
		mItem = item;
		if (item.isShow()) {
			if (item.isWrite()) {
				SelectBoxTag st = new SelectBoxTag(mName, mThis.getAttribute("style"));
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
				st.setAttribute("code", item.getCodeTable());
				if (item.getValue() != null && !item.getValue().equals("")) {
					st.addOption(item.getValue(), item.getCodeValue());
				} else if (item.getDefaultValue() != null && !item.getDefaultValue().equals("")) {
					st.addOption(item.getDefaultValue(), item.getCodeValue());
				} else {
					st.addOption("请选择", "请选择");
				}
				Node n = mThis.getParent();
				int ni = n.getChildren().indexOf(mThis);
				NodeList nlist = new NodeList();
				for (int ii = 0; ii < n.getChildren().size(); ii++) {
					if (ii == ni) {
						nlist.add(this.getElFormTagFist(item));//form验证 strat
						nlist.add(st);
						nlist.add(this.getElFormTagEnd(item));//form验证 end
					} else {
						nlist.add(n.getChildren().elementAt(ii));
					}
				}
				// -------------------------------------------------------
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
				this.mWrite = true;
			} else {
				InputTag it = new InputTag();
				it.setAttribute("name", "_" + mName);
				if (item.getShowValue() != null && !item.getShowValue().equals("")) {
					it.setAttribute("value", item.getShowValue());
					it.setAttribute("size", Integer.toString(getGBlen(item.getShowValue())));
				} else {
					it.setAttribute("size", Integer.toString(item.getLength()));
				}
				// ----------------
				String tmp1 = mThis.getAttribute("style");
				if (tmp1 != null) {
					it.setAttribute("style", tmp1);
				}
				// ---------------------
				it.setAttribute("class", "easyui-combobox");
				it.setAttribute("readonly", "readonly");

				Node n = mThis.getParent();
				int ni = n.getChildren().indexOf(mThis);
				NodeList nlist = new NodeList();
				for (int ii = 0; ii < n.getChildren().size(); ii++) {
					if (ii == ni) {
						String mValue = null;
						if (item.getValue() != null && !item.getValue().equals("")) {
							mValue = item.getValue();
						} else if (item.getDefaultValue() != null && !item.getDefaultValue().equals("")) {
							mValue = item.getDefaultValue();
						}
						if (mValue != null) {
							InputTag inputTag = new InputTag();
							inputTag.setAttribute("Type", "Hidden");
							inputTag.setAttribute("id", item.getName());
							inputTag.setAttribute("name", item.getName());
							inputTag.setAttribute("value", mValue);
							nlist.add(inputTag);
						}
						nlist.add(it);
					} else {
						nlist.add(n.getChildren().elementAt(ii));
					}
				}
				// -------------------------------------------------------
				n.getChildren().removeAll();
				n.getChildren().add(nlist);
			}
		} else {
			if (item.isWrite()) {
				SelectBoxTag st = new SelectBoxTag(mName, mThis.getAttribute("style"));
				st.setAttribute("code", item.getCodeTable());
				if (item.getValue() != null && !item.getValue().equals("")) {
					st.addOption(item.getValue(), item.getCodeValue());
				} else if (item.getDefaultValue() != null && !item.getDefaultValue().equals("")) {
					st.addOption(item.getDefaultValue(), item.getCodeValue());
				} else {
					st.addOption("请选择", "请选择");
				}
				this.mWrite = true;
			} else {
				Node n = mThis.getParent();
				int ni = n.getChildren().indexOf(mThis);
				NodeList nlist = new NodeList();
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
	}

	@SuppressWarnings("unused")
	private ImageTag getButton() {
		ImageTag imgBtn = new ImageTag();
		imgBtn.setImageURL("ZrCollEngine/Res/control.gif");
		imgBtn.setAttribute("onClick", "SetSelectValue('" + mName + "','" + mItem.getCodeTable() + "');");
		imgBtn.setAttribute("style", "margin:0px 2px -3px 0px;cursor:pointer;");
		imgBtn.setAttribute("alt", "国标代码");
		return imgBtn;
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
			validate.put("message", ""+this.mItem.getChineseName()+"不能为空!");
			validate.put("trigger", "blur");
			validateList.add(validate);
			sb.append(mName+":"+JSONUtils.toJSONString(validateList)+",\n");
			validateList = null;
			validate = null;
		}
		return sb.toString();
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
}