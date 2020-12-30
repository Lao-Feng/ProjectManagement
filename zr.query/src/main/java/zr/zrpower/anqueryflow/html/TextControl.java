package zr.zrpower.anqueryflow.html;

import zr.zrpower.queryengine.ItemField;

public class TextControl extends ControlBase {
    private StringBuffer resultHTML;
    public TextControl(ItemField itemField,String valueStr) {
        super();
        resultHTML = new StringBuffer();
//        resultHTML.append("<input name=").append(itemField.getName()).append(" id=").append(
//                          itemField.getName()).append(" type=text  style=\"width:").append(size).append("px;\"  maxlength=").append(
//                          String.valueOf(itemField.getLength())).append(
//                          " class=text value=\"").append(valueStr).append("\">");
        resultHTML.append("<input type=\"text\" class=\"am-form-field\" name=").append(itemField.getName()).append(" id=").append(
                          itemField.getName()).append(" minlength=\"3\" placeholder=\""+itemField.getChineseName()+"\" value=\"").append(valueStr).append("\"/>\r\n");
    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }
}