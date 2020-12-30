package zr.zrpower.analyseengine.html;

import zr.zrpower.analyseengine.ItemField;

public class DateTimeControl extends ControlBase {
    private StringBuffer resultHTML;

    public DateTimeControl(ItemField itemField, String valueStr) {
        super();
        resultHTML = new StringBuffer();
        resultHTML.append("<input name=").append(itemField.getName()).append(" id=").append(
                          itemField.getName()).append(" type=text style=\"width:").append(
                          size).append(
                "px;\" maxlength=19 class=text onblur=\"checkdatetime1(this)\" value=\"").append(
                          valueStr).append("\">");
        resultHTML.append(
                "<IMG SRC=\"/static/ZrCollEngine/Res/datecal.gif\" onClick=\"return showCalendar('").append(
                itemField.getName()).append(
                "', 'Y-MM-DD h:mm:ss');\" style=\"margin:0px 2px -5px 0px;cursor:pointer;\" alt=选择时间>");
    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }
}