package com.yonglilian.anqueryflow.html;

import com.yonglilian.common.util.Log;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.queryengine.ItemField;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBSet;

public class CheckBoxControl extends ControlBase {
    private StringBuffer resultHTML;
    DBEngine dbengine;
    private Log log;

	public CheckBoxControl(ItemField itemField, String valueStr) {
        dbengine = new DBEngine(SysPreperty.getProperty().MainDataSource,
                              SysPreperty.getProperty().IsConvert);
        log = new Log();
        log.SetLogForClass("CheckBoxControl");
        log.SetLogFile("CheckBoxControl.log");
        dbengine.initialize();
        resultHTML = new StringBuffer();
        log.WriteLog("valueStr="+valueStr);
        log.WriteLog(itemField.getName());
          String[][] optionsStr = getDictList(itemField.getCodeTable());
          for(int i=0;i<optionsStr.length;i++)
          {
              resultHTML.append("<input type=\"checkbox\" name=\""+itemField.getName()+"_checkbox"+String.valueOf(i)+"\" value=\""+optionsStr[i][0]+"\"");
              if(valueStr.indexOf(optionsStr[i][0])>-1)
              {
                resultHTML.append(" checked=true ");
              }
              resultHTML.append(">"+optionsStr[i][1]+"&nbsp;&nbsp;");
          }
          optionsStr = null;
    }

    public String GetOutPutHTML() {
     return resultHTML.toString();
 }

 private String[][] getDictList(String codeTable) {
       String[][] result = null;
       String strSQL = "";
       strSQL ="Select Code,Name From "+codeTable+" where ISSHOW!='1' or ISSHOW is null order by CODE";
       DBSet ds = dbengine.QuerySQL(strSQL.toString());
       if (ds==null)
       {

           strSQL ="Select Code,Name From "+codeTable+" order by CODE";
           ds = dbengine.QuerySQL(strSQL.toString());
       }

       int length = 1;
       if (ds != null && ds.RowCount() > 0) {
           if (ds.RowCount() > 5) {
               length = 5;
           } else {
               length = ds.RowCount() + 1;
           }
       }

           result = new String[length][2];
           result[0][0] = "ALL";
           result[0][1] = "全部";

           if(ds != null && ds.RowCount()>0)
           {
               for (int i = 0; i < ds.RowCount() && i < 5; i++) {
                   result[i + 1][0] = ds.Row(i).Column("Code").getString();
                   result[i + 1][1] = ds.Row(i).Column("Name").getString();
               }
           }
       return result;
   }
}