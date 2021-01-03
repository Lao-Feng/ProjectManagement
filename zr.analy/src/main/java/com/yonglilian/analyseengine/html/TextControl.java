package com.yonglilian.analyseengine.html;

import com.yonglilian.analyseengine.ItemField;

public class TextControl extends ControlBase {
    private StringBuffer resultHTML;

    public TextControl(ItemField itemField, String valueStr) {
        super();
        resultHTML = new StringBuffer();
        resultHTML.append("<input name=").append(itemField.getName()).append(" id=").append(
                          itemField.getName()).append(" type=text  style=\"width:").append(size)
        		  .append("px;\"  maxlength=").append(String.valueOf(itemField.getLength()))
        		  .append(" class=text value=\"").append(valueStr).append("\">");
    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }

}