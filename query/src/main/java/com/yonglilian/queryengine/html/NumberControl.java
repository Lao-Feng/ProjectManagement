package com.yonglilian.queryengine.html;

import com.yonglilian.queryengine.ItemField;

public class NumberControl extends ControlBase {
    private StringBuffer resultHTML;
    public NumberControl(ItemField itemField, String valueStr) {
        super();

        resultHTML = new StringBuffer();
        resultHTML.append("<input name=").append(itemField.getName()).append(" id=").append(
                          itemField.getName()).append(" type=text  style=\"width:78px;\" maxlength=").append(
                          String.valueOf(itemField.getLength())).append(
                          " class=\"easyui-numberbox\"  onkeypress=\"event.returnValue=CheckNumber();\" value=\"").append(
                          valueStr).append("\">");
    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }
}
