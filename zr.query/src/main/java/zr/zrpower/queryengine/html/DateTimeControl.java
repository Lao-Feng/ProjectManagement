package zr.zrpower.queryengine.html;

import zr.zrpower.queryengine.ItemField;

public class DateTimeControl extends ControlBase {
    private StringBuffer resultHTML;
    public DateTimeControl(ItemField itemField, String valueStr) {
        super();
        resultHTML = new StringBuffer();
        resultHTML.append("<input name=").append(itemField.getName()).append(" id=").append(
                          itemField.getName()).append(" type=text style=\"width:").append(
                          "180").append(
                "px;\" maxlength=19  class=\"easyui-datetimebox\" data-options=\"formatter:myDateTimeformatter,parser:myDateTimeparser\" value=\"").append(
                          valueStr).append("\">");
    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }

}