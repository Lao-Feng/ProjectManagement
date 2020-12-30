package zr.zrpower.anqueryflow.html;

import zr.zrpower.queryengine.ItemField;

public class NumberControl extends ControlBase {
    private StringBuffer resultHTML;

    public NumberControl(ItemField itemField, String valueStr) {
        super();
        resultHTML = new StringBuffer();
//        resultHTML.append("<input name=").append(itemField.getName()).append(" id=").append(
//                          itemField.getName()).append(" type=text  style=\"width:").append(
//                          String.valueOf(Integer.parseInt(size)/2-12)).append("px;\" maxlength=").append(
//                          String.valueOf(itemField.getLength())).append(
//                          " class=text  onkeypress=\"event.returnValue=CheckNumber();\" value=\"").append(
//                          valueStr).append("\">");
        String t_name=itemField.getChineseName();//显示名称
        if(itemField.getName().indexOf("_Begin")>-1){
        	t_name+="_起";
        }else if(itemField.getName().indexOf("_End")>-1){
        	t_name+="_止";
        }else{
//        	t_name=t_name;
        }

        resultHTML.append("<input type=\"text\" class=\"am-form-field\" name=").append(itemField.getName()).append(" id=").append(
                itemField.getName()).append(" minlength=\"3\" placeholder=\""+t_name+"\" value=\"").append(valueStr).append("\" onkeypress=\"event.returnValue=CheckNumber();\"/>\r\n");
    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }

}
