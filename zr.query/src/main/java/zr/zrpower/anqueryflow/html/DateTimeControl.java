package zr.zrpower.anqueryflow.html;

import zr.zrpower.queryengine.ItemField;

public class DateTimeControl extends ControlBase {
    private StringBuffer resultHTML;

    public DateTimeControl(ItemField itemField, String valueStr) {
        super();
        resultHTML = new StringBuffer();
//        resultHTML.append("<input name=").append(itemField.getName()).append(" id=").append(
//                          itemField.getName()).append(" type=text style=\"width:").append(
//                          "200").append(
//                "px;\" maxlength=19 class=text value=\"").append(
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
        		itemField.getName()).append(" placeholder=\""+t_name+"\" value=\"").append(valueStr).append("\" readonly />\r\n");

        resultHTML.append("<script>\r\n");
        resultHTML.append("$(function() {\r\n");
        resultHTML.append("   $('#"+itemField.getName()+"').datetimepicker({\r\n");
        resultHTML.append("	  format: 'yyyy-mm-dd hh:ii:ss',\r\n");
        resultHTML.append("      autoclose:true,\r\n");
        resultHTML.append("      language:'zh-CN'\r\n");
        resultHTML.append("   });\r\n");
        resultHTML.append("});\r\n");
        resultHTML.append("</script>\r\n");
    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }
}