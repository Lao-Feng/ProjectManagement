package zr.zrpower.analyseengine.html;

import zr.zrpower.analyseengine.ItemField;

public class NumberControl extends ControlBase {
    private StringBuffer resultHTML;

    public NumberControl(ItemField itemField, String valueStr) {
        super();
        resultHTML = new StringBuffer();
        resultHTML.append("<input name=").append(itemField.getName()).append(" id=").append(
                          itemField.getName()).append(" type=text  style=\"width:").append(
                          String.valueOf(Integer.parseInt(size)/2-12)).append("px;\" maxlength=").append(
                          String.valueOf(itemField.getLength())).append(
                          " class=text  onkeypress=\"event.returnValue=CheckNumber();\" value=\"").append(
                          valueStr).append("\">");
    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }

}