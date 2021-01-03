package com.yonglilian.analyseengine.html;

public abstract class ControlBase {
    protected String size;

    public ControlBase() {
        size = "230";
    }

    public abstract String GetOutPutHTML();

    //转换字符串为HTML文本
    protected String HTMLEncode(String s) {
        if (s != null && !s.equals("")) {
            s = Replace(s, "<", "&lt;");
            s = Replace(s, ">", "&gt;");
            s = Replace(s, " ", "&nbsp;");
            s = Replace(s, "\"", "&quot;");
            s = s.replace('\r', '^');
            s = Replace(s, "^", "<br>");
        } else {
            s = "&nbsp;";
        }
        return s;
    }

    //字符串替换函数
    private String Replace(String strSource, String strFrom, String strTo) {
        java.lang.String strDest = "";
        try {
            int intFromLen = strFrom.length();
            int intPos;
            while ((intPos = strSource.indexOf(strFrom)) != -1) {
                strDest = strDest + strSource.substring(0, intPos);
                strDest = strDest + strTo;
                strSource = strSource.substring(intPos + intFromLen);
            }
            strDest = strDest + strSource;
        } catch (Exception e) {

        }
        return strDest;
    }

}