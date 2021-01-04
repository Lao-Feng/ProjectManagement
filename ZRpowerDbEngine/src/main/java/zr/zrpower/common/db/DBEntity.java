// 
// Decompiled by Procyon v0.5.36
// 

package zr.zrpower.common.db;

import com.yonglilian.common.util.StringWork;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class DBEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected DBRow dbrow;
    protected boolean bHTML;
    
    public DBEntity() {
        this.bHTML = false;
        this.dbrow = new DBRow();
    }
    
    protected void initialize() {
    }
    
    protected String getString(final String mValue) {
        if (!this.bHTML) {
            return mValue;
        }
        if (mValue == null) {
            return "&nbsp;";
        }
        if (!mValue.equals("")) {
            return this.replace(mValue);
        }
        return "&nbsp;";
    }
    
    public void fullData(final DBRow dr) {
        final StringWork sw = new StringWork();
        final String[] strColumnName = dr.getAllColumnName();
        final String[] saColumn = this.dbrow.getAllColumnName();
        if (!strColumnName.equals(null)) {
            for (int i = 0; i < strColumnName.length; ++i) {
                if (dr.Column(strColumnName[i]) != null && sw.IsInArrary(strColumnName[i], saColumn)) {
                    this.dbrow.Column(strColumnName[i]).setValue(dr.Column(strColumnName[i]).getValue());
                }
            }
        }
    }
    
    public void fullDataFromRequest(final HttpServletRequest request) {
        final Map<String, Object> map = new HashMap<String, Object>();
        final Enumeration<String> enu = (Enumeration<String>)request.getParameterNames();
        while (enu.hasMoreElements()) {
            final String name = enu.nextElement();
            final String[] values = request.getParameterValues(name);
            String value = "";
            if (values.length > 1) {
                final StringWork sw = new StringWork();
                value = sw.UnSplit(values);
            }
            else {
                value = values[0];
            }
            map.put(name.toUpperCase(), value);
        }
        this.fullDataFromHashtable(map);
    }
    
    public void fullDataFromHashtable(final Map<String, Object> map) {
        final String[] strColumnName = this.dbrow.getAllColumnName();
        String strDate = "";
        if (!strColumnName.equals(null)) {
            for (int i = 0; i < strColumnName.length; ++i) {
                if (map.get(strColumnName[i].toUpperCase()) != null) {
                    if (this.dbrow.Column(strColumnName[i]).getType().getValue() == DBType.DATE.getValue() || this.dbrow.Column(strColumnName[i]).getType().getValue() == DBType.DATETIME.getValue()) {
                        strDate = (String) map.get(strColumnName[i].toUpperCase());
                        if (strDate.length() > 0) {
                            this.dbrow.Column(strColumnName[i]).setValue(this.stringToDateTime((String) map.get(strColumnName[i].toUpperCase())));
                        }
                        else {
                            this.dbrow.Column(strColumnName[i]).setValue("");
                        }
                    }
                    else if (!map.get(strColumnName[i].toUpperCase()).equals("\u672a\u586b\u5199")) {
                        this.dbrow.Column(strColumnName[i]).setValue(map.get(strColumnName[i].toUpperCase()));
                    }
                }
            }
        }
    }
    
    public void fullData(final DBSet ds) {
        if (ds.RowCount() > 0) {
            this.fullData(ds.Row(0));
        }
    }
    
    private Date stringToDateTime(final String param) {
        Date result = null;
        if (param == null) {
            return null;
        }
        if (param.length() == 10) {
            Date date = null;
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(param);
                result = new Date(date.getTime());
            }
            catch (ParseException ex) {}
        }
        if (param.length() == 19) {
            Date date = null;
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = sdf.parse(param);
                result = new Date(date.getTime());
            }
            catch (ParseException ex2) {}
        }
        return result;
    }
    
    public String[] getAllColumnName() {
        return this.dbrow.getAllColumnName();
    }
    
    public int getColumnCount() {
        return this.dbrow.getColumnCount();
    }
    
    public DBRow getData() {
        return this.dbrow;
    }
    
    public DBColumn item(final String ItemName) {
        return this.dbrow.Column(ItemName);
    }
    
    public void setToHTML(final boolean IfTo) {
        this.bHTML = IfTo;
    }
    
    private String replace(String str) {
        final StringBuffer sb = new StringBuffer();
        str = str.trim();
        for (int i = 0; i < str.length(); ++i) {
            final char ch = str.charAt(i);
            switch (ch) {
                case '\r': {
                    sb.append("<br>");
                    break;
                }
                case ' ': {
                    sb.append("&nbsp;");
                    break;
                }
                case '\"': {
                    sb.append("&quot;");
                    break;
                }
                case '\'': {
                    sb.append("&quot;");
                    break;
                }
                case '&': {
                    sb.append("&amp;");
                    break;
                }
                case '<': {
                    sb.append("&lt;");
                    break;
                }
                case '>': {
                    sb.append("&gt;");
                    break;
                }
                default: {
                    sb.append(ch);
                    break;
                }
            }
        }
        return sb.toString();
    }
}
