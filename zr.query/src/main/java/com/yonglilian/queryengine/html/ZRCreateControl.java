package com.yonglilian.queryengine.html;

import com.yonglilian.queryengine.ItemField;
import zr.zrpower.common.util.Log;
import zr.zrpower.model.SessionUser;

import java.util.List;
import java.util.Map;

public class ZRCreateControl {
	private ZRControlBase _control;
	private SessionUser _userInfo;
	private ItemField itemField;
	Log log;
	String _valueStr;

	public ZRCreateControl(ItemField item, SessionUser userInfo, String valueStr) {
		itemField = item;
		_userInfo = userInfo;
		_valueStr = valueStr;
		ConstructorCtl();
	}

	public List<Map<String, Object>> GetValuesList() {
		return _control.GetValuesList();
	}

	private void ConstructorCtl() {
        //代码标记
		if (itemField.isCode()) {
			//1、点选  2、下拉  3、字典  4、多选
			if (itemField.getCodeInput() == 1) {
				_control = new ZRRadioControl(itemField, _valueStr); // 点选
			} else if(itemField.getCodeInput() == 2){
				_control = new ZRSelectControl(itemField, _userInfo, _valueStr); // 下拉
			} else if(itemField.getCodeInput() == 3){
				_control = new ZRDictControl(itemField, _valueStr);//字典
			} else {
				_control = new ZRCheckboxControl(itemField, _valueStr);//多选
			}
		//单位或者用户表	
		} else if (itemField.isUnit() || itemField.isUser()) {
			_control = new ZRSelectControl(itemField, _userInfo, _valueStr); // 下拉型字典元素
		} else {
			_control = new ZRTextControl(itemField, _valueStr); // 文本型
		}
	}
}
