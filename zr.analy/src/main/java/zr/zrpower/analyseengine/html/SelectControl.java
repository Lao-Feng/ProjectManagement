package zr.zrpower.analyseengine.html;

import zr.zrpower.analyseengine.ItemField;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBSet;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.model.SessionUser;

public class SelectControl extends ControlBase {
    StringBuffer resultHTML;
    DBEngine dbengine;
    String _valueStr;
    SessionUser _userInfo;

    public SelectControl(ItemField itemField, SessionUser userInfo, String valueStr) {
        super();
        _userInfo = userInfo;
        String[][] optionsStr = null;
        _valueStr = valueStr;
        dbengine = new DBEngine(SysPreperty.getProperty().MainDataSource,
                                SysPreperty.getProperty().IsConvert);
        dbengine.initialize();
        resultHTML = new StringBuffer();
        resultHTML.append("<SELECT name=").append(itemField.getName()).append(" id=").append(
                          itemField.getName()).append(" style=\"width:").append(size).append(
                          "px;\" >");
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
        if(itemField.isUser()) {
            resultHTML.append(
              "<IMG SRC=\"/static/ZrCollEngine/Res/control.gif\" onClick=\"SelPsn('").append(
              itemField.getName()).append("');\" style=\"margin:0px 2px -3px 0px;cursor:pointer;\" alt=选择人员>");
        }
        if(itemField.isUnit()) {
           resultHTML.append(
             "<IMG SRC=\"/static/ZrCollEngine/Res/control.gif\" onClick=\"SelUnit('").append(
             itemField.getName()).append("');\" style=\"margin:0px 2px -3px 0px;cursor:pointer;\" alt=选择单位>");
       }
    }

    public String GetOutPutHTML() {
        return resultHTML.toString();
    }

    protected String AddOption(String value, String text) {
        StringBuffer  strResult = new   StringBuffer();
        if (_valueStr.equals(value)) {
            strResult.append("<Option value=").append(value).append(" selected=true>")
            		 .append(text).append("</Option>");
        } else {
            strResult.append("<Option value=").append(value).append(" >").append(text)
            		 .append("</Option>");
        }
        return strResult.toString();
    }

    private String[][] getDictList(String codeTable) {
        String[][] result = null;
        StringBuffer strSQL = new StringBuffer();
        strSQL.append("Select Code,Name From ").append(codeTable);
        DBSet ds = dbengine.QuerySQL(strSQL.toString());
        if (ds != null && ds.RowCount() > 0) {
            result = new String[ds.RowCount()][2];
            for (int i = 0; i < ds.RowCount(); i++) {
                result[i][0] = ds.Row(i).Column("Code").getString();
                result[i][1] = ds.Row(i).Column("Name").getString();
            }
        }
        return result;
    }

    private String[][] getUserList(String UnitID) {
        String[][] saUser = null;
        StringBuffer strSQL = new StringBuffer();
        strSQL.append("Select UserID,Name From BPIP_USER Where UnitID='").append(UnitID).append("'");
        if(_valueStr != null && _valueStr.length()>0) {
            strSQL.append(" Or UserID='"+_valueStr+"'");
        }
        DBSet ds = dbengine.QuerySQL(strSQL.toString());
        if (ds != null && ds.RowCount() > 0) {
            saUser = new String[ds.RowCount()][2];
            for (int i = 0; i < ds.RowCount(); i++) {
                saUser[i][0] = ds.Row(i).Column("UserID").getString();
                saUser[i][1] = ds.Row(i).Column("Name").getString();
            }
        }
        return saUser;
    }

    private String[][] getUnitList() {
        String[][] saUnit = null;
        StringBuffer strSQL = new StringBuffer();
        strSQL.append("Select UnitID,UNITNAME From BPIP_UNIT Where UnitID Like ");
        strSQL.append("'%'");
        if(_valueStr != null && _valueStr.length()>0) {
        	strSQL.append(" Or UnitID='"+_valueStr+"'");
        }
        strSQL.append(" Order by UnitId");
        DBSet ds = dbengine.QuerySQL(strSQL.toString());
        if (ds != null && ds.RowCount() > 0) {
            saUnit = new String[ds.RowCount()][2];
            for (int i = 0; i < ds.RowCount(); i++) {
                saUnit[i][0] = ds.Row(i).Column("UnitID").getString();
                saUnit[i][1] = ds.Row(i).Column("UNITNAME").getString();
            }
        }
        return saUnit;
    }

}