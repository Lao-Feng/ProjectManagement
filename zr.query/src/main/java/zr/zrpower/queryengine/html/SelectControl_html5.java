package zr.zrpower.queryengine.html;

import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.model.SessionUser;
import zr.zrpower.queryengine.ItemField;
import zr.zrpower.service.impl.QueryControlImpl;

public class SelectControl_html5 extends ControlBase {
    StringBuffer resultHTML;
    DBEngine dbengine;
    String _valueStr;
    SessionUser _userInfo;
    QueryControlImpl queryControl =  new QueryControlImpl();
    
    public SelectControl_html5(ItemField itemField, SessionUser userInfo, String valueStr) {
        super();
        _userInfo = userInfo;
        String[][] optionsStr = null;
        _valueStr = valueStr;
        dbengine = new DBEngine(SysPreperty.getProperty().MainDataSource,
                                SysPreperty.getProperty().IsConvert);
        dbengine.initialize();

        resultHTML = new StringBuffer();
        if(itemField.isUser() || itemField.isUnit())
        {
          resultHTML.append("<SELECT name=").append(itemField.getName()).append(
              " id=").append(
                  itemField.getName()).append(" style=\"width:180px;\" class=\"easyui-combobox\">");
        }else
        {
           resultHTML.append("<SELECT name=").append(itemField.getName()).append(
                " id=").append(
                    itemField.getName()).append(" style=\"width:180px;\" class=\"easyui-combobox\">");

        }
        resultHTML.append("<OPTION value=\"\">---全部---</Option>");
        if (itemField.isCode()) {
            optionsStr = getDictList(itemField.getCodeTable());
        }

        if (itemField.isUser()) {
            optionsStr = getUserList(userInfo.getUnitID());
        }
        if (itemField.isUnit()) {
            optionsStr = getUnitList();
        }

        if (optionsStr != null && optionsStr.length > 0) {
            for (int i = 0; i < optionsStr.length; i++) {
                resultHTML.append(AddOption(optionsStr[i][0], optionsStr[i][1]));
            }
        }

        resultHTML.append("</SELECT>");

        if(itemField.isUser())
        {
            resultHTML.append(
              "<IMG SRC=\"/static/easyui/themes/default/images/searchbox_button.png\"  onClick=\"SelPsn('").append(
              itemField.getName()).append("');\" " +
              "icon-index=\"0\" style=\"margin:0px 2px -5px 5px;cursor:pointer;\" alt=选择人员></a>");

        }

        if(itemField.isUnit())
       {
           resultHTML.append(
             "<IMG SRC=\"/static/easyui/themes/default/images/searchbox_button.png\" onClick=\"SelUnit('").append(
             itemField.getName()).append("');\" style=\"margin:0px 2px -5px 5px;cursor:pointer;\" alt=选择单位>");

       }

    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }

    protected String AddOption(String value, String text) {
        StringBuffer  strResult = new   StringBuffer();
        if (_valueStr.equals(value)) {
            strResult.append("<Option value=").append(value).append(" selected=true>").append(text).append(
                        "</Option>");
        } else {
            strResult.append("<Option value=").append(value).append(" >").append(text).append("</Option>");
        }
        return strResult.toString();
    }

    private String[][] getDictList(String codeTable) {
      String[][] result = null;
       try {
         result = queryControl.getCodeTable(codeTable);
       }
       catch (Exception ex) {}
       return result;

    }

    private String[][] getUserList(String UnitID) {
      String[][] saUser = null;
      try {
         saUser = queryControl.getCodeTable("BPIP_USER");
       }
       catch (Exception ex){}
       return saUser;

    }

    private String[][] getUnitList() {
      String[][] saUnit = null;
      try {
         saUnit = queryControl.getCodeTable("BPIP_UNIT");
       }
       catch (Exception ex){}
       return saUnit;
    }
}
