package com.yonglilian.anqueryflow.html;

import com.yonglilian.queryengine.ItemField;

public class DateControl extends ControlBase {
    private StringBuffer resultHTML;
    public DateControl(ItemField item, String valueStr) {
        super();
        resultHTML = new StringBuffer();
//        resultHTML.append("<input name=").append(item.getName()).append(" id=").append(
//                          item.getName()).append(" type=text   style=\"width:").append(size).append(
//                "px;\" maxlength=10 class=text onblur=\"checkdate1(this)\" value=\"").append(
//                          valueStr).append("\">");
        String t_name=item.getChineseName();//显示名称
        if(item.getName().indexOf("_Begin")>-1){
        	t_name+="_起";
        }else if(item.getName().indexOf("_End")>-1){
        	t_name+="_止";
        }else{
//        	t_name=t_name;
        }

        resultHTML.append("<input type=\"text\" class=\"am-form-field\" name=").append(item.getName()).append(" id=").append(
        		item.getName()).append("  placeholder=\""+t_name+"\" value=\"").append(valueStr).append("\" data-am-datepicker readonly />\r\n");
    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }

}