package zr.zrpower.collectionengine.html;

import org.htmlparser.Node;
import org.htmlparser.tags.FormTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;
import zr.zrpower.collectionengine.ItemField;

import java.util.Vector;

/**
 * <p>
 * Title: 数据采集引擎－隐藏控件集类
 * </p>
 * <p>
 * Description: 定义采集模版中解析的隐藏控件集
 * </p>
 * <p>
 * Copyright: Copyright (c)
 * </p>
 * <p>
 * Company:
 * </p>
 */
public class HiddenControlSet {
	private Vector<Object> ctrlList; // 控件集

	/**
	 * 构造函数
	 */
	public HiddenControlSet() {
		ctrlList = new Vector<Object>();
	}

	/**
	 * 将模版上现有的隐藏控件加入集合
	 * @param inputtag 模版上的隐藏控件
	 * @param item 字段项信息
	 */
	public void addPageHidden(InputTag inputtag, ItemField item) {
		if (item != null) {
			inputtag.setAttribute("name", item.getName());
			inputtag.setAttribute("id", item.getName());
			inputtag.setAttribute("type", "hidden");
			if (item.getValue() != null && !item.getValue().equals("")) {
				inputtag.setAttribute("value", item.getValue());
			} else if (item.getDefaultValue() != null && !item.getDefaultValue().equals("")) {
				inputtag.setAttribute("value", item.getDefaultValue());
			}
		} else {
			inputtag.setAttribute("id", inputtag.getAttribute("name"));
		}
		ctrlList.add(inputtag);

		// 从界面上移出控件
		Node n = inputtag.getParent();
		int ni = n.getChildren().indexOf(inputtag);
		NodeList nlist = new NodeList();
		for (int ii = 0; ii < n.getChildren().size(); ii++) {
			if (ii != ni) {
				nlist.add(n.getChildren().elementAt(ii));
			}
		}
		n.getChildren().removeAll();
		n.getChildren().add(nlist);
	}
	
	/**
	 * 将模版上现有的隐藏控件移除
	 * @param inputtag 模版上的隐藏控件
	 * @param item 字段项信息
	 */
	public void removePageHidden(InputTag inputtag) {
		// 从界面上移出控件
		Node n = inputtag.getParent();
		int ni = n.getChildren().indexOf(inputtag);
		NodeList nlist = new NodeList();
		for (int ii = 0; ii < n.getChildren().size(); ii++) {
			if (ii != ni) {
				nlist.add(n.getChildren().elementAt(ii));
			}
		}
		n.getChildren().removeAll();
		n.getChildren().add(nlist);
	}

	/**
	 * 创建一个新隐藏控件加入集合
	 * @param name 新控件名称
	 * @param value 新控件值
	 */
	public void addNewHidden(String name, String value) {
		InputTag inputtag = new InputTag();
		inputtag.setAttribute("type", "hidden");
		inputtag.setAttribute("name", name);
		inputtag.setAttribute("id", name);
		inputtag.setAttribute("value", "\"" + value + "\"");
		// ctrlList.add(inputtag);
		// ---------加入判断，解决当isWrite() == false && isShow() == false时存入字符串"null"的问题 ----
		if (!(value == null)) {
			ctrlList.add(inputtag);
		}
		// ----------------------------------
	}

	/**
	 * 将隐藏控件集填充到表单中
	 * @param frm 模版表单
	 */
	public void toForm(FormTag frm) {
		for (int i = 0; i < ctrlList.size(); i++) {
			frm.getChildren().add((InputTag) ctrlList.get(i));
		}
	}
}