package com.yonglilian.queryengine.html;

import com.yonglilian.queryengine.ItemField;

public class DateControl_html5 extends ControlBase {
    private StringBuffer resultHTML;
    public DateControl_html5(ItemField item, String valueStr) {
        super();
        resultHTML = new StringBuffer();
        resultHTML.append("<input name=").append(item.getName()).append(" id=").append(
                          item.getName()).append(" type=text style=\"width:").append("180").append(
                "px;\" maxlength=10 class=\"easyui-datebox\" data-options=\"formatter:myformatter,parser:myparser\" value=\"").append(
                          valueStr).append("\">");

    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }

}