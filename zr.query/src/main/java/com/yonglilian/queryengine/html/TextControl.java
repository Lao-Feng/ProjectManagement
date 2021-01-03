package com.yonglilian.queryengine.html;

import com.yonglilian.queryengine.ItemField;

public class TextControl extends ControlBase {
    private StringBuffer resultHTML;
    public TextControl(ItemField itemField, String valueStr) {
        super();
        resultHTML = new StringBuffer();
        resultHTML.append("<input name=").append(itemField.getName()).append(" id=").append(
                          itemField.getName()).append(" type=text  style=\"width:180px;\"  maxlength=").append(
                          String.valueOf(itemField.getLength())).append(
                          " class=\"easyui-textbox\" value=\"").append(valueStr).append("\">");

    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }

}