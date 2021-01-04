package com.yonglilian.anqueryflow.html;

import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.queryengine.ItemField;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBSet;

public class DictControl extends ControlBase {
    private StringBuffer resultHTML;
    DBEngine dbengine;

    public DictControl(ItemField itemField, String valueStr) {
        super();
        dbengine = new DBEngine(SysPreperty.getProperty().MainDataSource,
                                SysPreperty.getProperty().IsConvert);
        dbengine.initialize();

        resultHTML = new StringBuffer();
        resultHTML.append("<SELECT name=").append(itemField.getName()).append(" id=").append(
                          itemField.getName()).append(
                          " sdata-am-selected code=").append(
                          itemField.getCodeTable()).append(">");

        if (valueStr.length() > 0 && valueStr.equals("请选择")) {
            resultHTML.append(
                    "<OPTION value=\"请选择\" selected=true>请选择</Option>");
        } else if (valueStr.length() > 0) {
            resultHTML.append(GetOptionStr(valueStr, itemField.getCodeTable()));
        } else {
            resultHTML.append(
                    "<OPTION value=\"请选择\" selected=true>请选择</Option>");
        }
        resultHTML.append("</SELECT>");

//        resultHTML.append(
//                "<IMG SRC=\"ZrCollEngine/Res/control.gif\" onClick=\"SetSelectValue('").append(
//                itemField.getName()).append("', '").append(itemField.getCodeTable()).append(
//                "');\" style=\"margin:0px 2px -3px 0px;cursor:pointer;\" alt=国标代码>");

    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }

    private String GetOptionStr(String code, String codeTable) {
        String result = "";
        StringBuffer  strSQL = new   StringBuffer();
        StringBuffer  addBuf = new   StringBuffer();
        strSQL.append("Select Code,Name From " ).append(codeTable).append(" Where Code='").append(
                        code).append("'");
        DBSet ds = dbengine.QuerySQL(strSQL.toString());
        if (ds != null && ds.RowCount() > 0) {
            addBuf.append("<Option value=").append(code).append(">").append(ds.Row(0).Column("Name").getString()).append("</Option>");
            result = addBuf.toString();
        }
        return result;
    }

}
