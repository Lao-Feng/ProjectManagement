package zr.zrpower.analyseengine.html;

import zr.zrpower.analyseengine.ItemField;

public class DateControl extends ControlBase {
    private StringBuffer resultHTML;

    public DateControl(ItemField item, String valueStr) {
        super();
        resultHTML = new StringBuffer();
        resultHTML.append("<input name=").append(item.getName()).append(" id=").append(
                          item.getName()).append(" type=text   style=\"width:").append(size).append(
                "px;\" maxlength=10 class=text onblur=\"checkdate1(this)\" value=\"").append(
                          valueStr).append("\">");
        resultHTML.append(
                "<IMG SRC=\"/static/ZrCollEngine/Res/datecal.gif\" onClick=\"return showCalendar('").append(
                item.getName()).append(
                "', 'Y-MM-DD');\" style=\"margin:0px 2px -5px 0px;cursor:pointer;\" alt=选择日期>");
    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }

}