package com.yonglilian.queryengine.html;

import com.yonglilian.queryengine.ItemField;

public class FloatControl extends ControlBase {
    private StringBuffer resultHTML;
    public FloatControl(ItemField itemField, String valueStr) {
        super();
        resultHTML = new StringBuffer();
        resultHTML.append("<input name=").append(itemField.getName()).append(" id=").append(
                          itemField.getName()).append(" type=text  style=\"width:70px;\"  maxlength=").append(
                          String.valueOf(itemField.getLength())).append(
                          " class=\"easyui-numberbox\" precision=\"2\" onkeypress=\"event.returnValue=CheckNumberPoint();\" value=\"").append(
                          valueStr).append("\">");
    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }
}
