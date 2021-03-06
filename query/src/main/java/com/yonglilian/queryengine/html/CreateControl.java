package com.yonglilian.queryengine.html;

import com.yonglilian.common.util.Log;
import com.yonglilian.model.SessionUser;
import com.yonglilian.queryengine.ItemField;
import zr.zrpower.common.db.DBType;

public class CreateControl {
	private ControlBase _control;
	private SessionUser _userInfo;
	private ItemField itemField;
	Log log;
	String _valueStr;

	public CreateControl(ItemField item, SessionUser userInfo, String valueStr) {
		itemField = item;
		_userInfo = userInfo;
		_valueStr = valueStr;
		ConstructorCtl();
	}

	public String GetOutPutHTML() {
		return _control.GetOutPutHTML();
	}

	private void ConstructorCtl() {

		if (itemField.getType().getValue() == DBType.STRING.getValue()) {
			if (itemField.isCode()) {
				if (itemField.getCodeInput() == 1 || itemField.getCodeInput() == 2 || itemField.getCodeInput() == 4) {
					_control = new SelectControl(itemField, _userInfo, _valueStr); // 下拉型字典元素
				} else {
					_control = new DictControl(itemField, _valueStr);
				}
			} else if (itemField.isUnit() || itemField.isUser()) {
				_control = new SelectControl(itemField, _userInfo, _valueStr); // 下拉型字典元素
			} else {
				_control = new TextControl(itemField, _valueStr); // 文本型
			}
		}
		// 日期型元素
		else if (itemField.getType().getValue() == DBType.DATE.getValue()) {
			_control = new DateControl(itemField, _valueStr);
		}
		// 日期时间型元素
		else if (itemField.getType().getValue() == DBType.DATETIME.getValue()) {
			if ("html5.css".equals(_userInfo.getUserCSS())) {
				_control = new DateTimeControl_html5(itemField, _valueStr);
			} else {
				_control = new DateTimeControl(itemField, _valueStr);
			}
		}
		// 数字型元素
		else if (itemField.getType().getValue() == DBType.LONG.getValue()) {
			_control = new NumberControl(itemField, _valueStr);
		}
		else if (itemField.getType().getValue() == DBType.FLOAT.getValue()) {
			_control = new FloatControl(itemField, _valueStr);
		}
		else {
			_control = new TextControl(itemField, _valueStr);
		}
	}
}
