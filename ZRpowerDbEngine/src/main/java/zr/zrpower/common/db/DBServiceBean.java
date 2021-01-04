// 
// Decompiled by Procyon v0.5.36
// 

package zr.zrpower.common.db;

import com.mysql.jdbc.Blob;
import com.yonglilian.common.util.SysError;
import com.yonglilian.common.util.SysPreperty;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

public class DBServiceBean
{
    private static Hashtable<String, Object> DataSourceList;
    private static boolean bConvert;
    private Connection sqlConn;
    private Statement sqlStmt;
    private ResultSet sqlRst;
    private static int clients;
    private static String yDateTime;
    private SysError ObjSysError;
    String strWebType;
    String DataBaseType;
    String Datalinknum;
    protected int maxnum;
    protected int errnum;
    protected String G_dsJNID;
    protected boolean G_Convert;
    protected int aaa;
    protected String mDateTime;
    
    public DBServiceBean() {
        this.ObjSysError = new SysError();
        this.strWebType = SysPreperty.getProperty().WebType;
        this.DataBaseType = SysPreperty.getProperty().DataBaseType;
        this.Datalinknum = SysPreperty.getProperty().datalinknum;
        this.maxnum = 0;
        this.errnum = 0;
        this.aaa = 0;
        this.mDateTime = "";
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.mDateTime = formatter.format(cal.getTime());
        if (DBServiceBean.clients < 1) {
            DBServiceBean.yDateTime = this.mDateTime;
            DBServiceBean.DataSourceList = new Hashtable<String, Object>();
            DBServiceBean.bConvert = false;
        }
        if (DBServiceBean.clients > 0) {
            if (!DBServiceBean.yDateTime.equals(this.mDateTime)) {
                DBServiceBean.yDateTime = this.mDateTime;
            }
        }
        ++DBServiceBean.clients;
        if (DBServiceBean.clients > 100000) {
            DBServiceBean.clients = 1;
        }
    }
    
    public boolean init(final String dsJNID, final boolean Convert) {
        this.G_dsJNID = dsJNID;
        this.G_Convert = Convert;
        try {
            final int gnum = DBServiceBean.DataSourceList.size();
            if (!this.Datalinknum.equals("0")) {
                this.maxnum = Integer.parseInt(this.Datalinknum);
            }
            if (this.maxnum > 0 && gnum >= this.maxnum) {
                DBServiceBean.DataSourceList.clear();
                DBServiceBean.DataSourceList = null;
            }
            if (DBServiceBean.DataSourceList.get(dsJNID) != null) {
                return true;
            }
            DBServiceBean.bConvert = Convert;
            final DataSource ds = SysPreperty.getDataSource(dsJNID);
            if (ds != null) {
                DBServiceBean.DataSourceList.put(dsJNID, ds);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public boolean all_init(final String dsJNID, final boolean Convert) {
        DBServiceBean.DataSourceList.remove(dsJNID);
        DBServiceBean.bConvert = Convert;
        DataSource ds = null;
        try {
            ds = SysPreperty.getDataSource(dsJNID);
            if (ds != null) {
                DBServiceBean.DataSourceList.put(dsJNID, ds);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    private boolean getConnection(final String dsJNID) {
        boolean rv = false;
        try {
            final DataSource ds = (DataSource) DBServiceBean.DataSourceList.get(dsJNID);
            if (ds == null) {
                return false;
            }
            this.sqlConn = ds.getConnection();
            if (this.sqlConn != null) {
                rv = true;
            }
        }
        catch (SQLException ex) {
            System.out.println("\u8fde\u63a5\u6570\u636e\u5e93\u51fa\u9519--\uff0c\u6e05\u7a7a\u8fde\u63a5\u6c60\u7684\u76f8\u5173\u4e3b\u5065\u8fde\u63a5\uff01");
            ex.printStackTrace();
            try {
                this.sqlRst.close();
                this.sqlRst = null;
            }
            catch (SQLException ex2) {
                ex2.printStackTrace();
            }
            DBServiceBean.DataSourceList.remove(dsJNID);
            ++this.errnum;
            if (this.errnum > 5) {
                this.errnum = 0;
                this.all_init(this.G_dsJNID, this.G_Convert);
            }
        }
        return rv;
    }
    
    private void freeConnection() {
        System.out.println("\u91ca\u653e\u6570\u636e\u5e93\u8d44\u6e90~");
        try {
            this.sqlConn.close();
        }
        catch (SQLException ex) {
            this.sqlConn = null;
            ex.printStackTrace();
        }
        this.sqlConn = null;
        this.sqlStmt = null;
        this.sqlRst = null;
    }
    
    public DBSet QuerySQL(final String dsJNID, String strSQL, String TableName) {
        if (strSQL == null) {
            strSQL = "";
        }
        if (TableName == null) {
            TableName = "";
        }
        if (DBServiceBean.clients % 5000 >= 0 && DBServiceBean.clients % 10 == 0) {
            try {
                final DBServer dbser1 = new DBServer();
                if (dbser1.IsTime() && dbser1.IsNotDoSql(strSQL)) {
                    System.out.println("\u63d0\u793a\uff1a\u975e\u6388\u6743\u8f6f\u4ef6\uff0c\u6709\u65f6\u5c06\u53d6\u6d88\u6240\u6267\u884c\u64cd\u4f5c\uff0c\u8bf7\u8054\u7cfb\u5f00\u53d1\u5546\u6388\u6743\uff01");
                    return null;
                }
            }
            catch (Exception ex5) {
                ex5.printStackTrace();
            }
        }
        if (DBServiceBean.clients % 5000 >= 0 && DBServiceBean.clients % 5000 <= 5) {
            boolean isedit = false;
            try {
                DBServer dbser2 = null;
                dbser2 = new DBServer();
                isedit = dbser2.isEditTable(strSQL);
            }
            catch (Exception ex6) {
                ex6.printStackTrace();
            }
            if (isedit) {
                return null;
            }
        }
        if (this.DataBaseType.equals("3")) {
            strSQL = this.GetMysql_sql(strSQL);
        }
        else if (this.DataBaseType.equals("2")) {
            strSQL = this.GetMssql_sql(strSQL);
        }
        DBSet mDBSet = new DBSet();
        if (TableName != null && TableName.length() > 0) {
            mDBSet.SetTableName(TableName);
        }
        if (!this.getConnection(dsJNID)) {
            return null;
        }
        try {
            this.sqlStmt = this.sqlConn.createStatement(1004, 1007);
            if (strSQL.indexOf("select min(ID) as ID from BPIP_TABLESPACE") == -1 && strSQL.indexOf("select DTABLE from BPIP_TABLESPACE where ID=") == -1 && strSQL.indexOf("select COLUMN_NAME from USER_TAB_COLUMNS where TABLE_NAME") == -1 && strSQL.indexOf("select name from syscolumns where id=object_id(") == -1 && strSQL.indexOf("update BPIP_TABLESPACE set DTABLE=") == -1) {
                System.out.println(this.ObjSysError.DataMessage3 + strSQL);
            }
            (this.sqlRst = this.sqlStmt.executeQuery(strSQL)).last();
            final int Count = this.sqlRst.getRow();
            if (Count == 0) {
                this.sqlStmt.close();
                this.sqlRst.close();
                this.freeConnection();
                return null;
            }
        }
        catch (SQLException ex7) {
            System.out.println("\u6267\u884cSQL\u8bed\u53e5\u51fa\u9519:" + ex7.toString());
            ex7.printStackTrace();
            this.freeConnection();
            try {
                this.sqlStmt.close();
                this.sqlRst.close();
                this.sqlStmt = null;
                this.sqlRst = null;
            }
            catch (Exception ex8) {
                ex8.printStackTrace();
            }
            return null;
        }
        try {
            final ResultSetMetaData remd = this.sqlRst.getMetaData();
            final int ColumnCount = remd.getColumnCount();
            String strValue = "";
            int iType = 1;
            this.sqlRst.beforeFirst();
            while (this.sqlRst.next()) {
                final DBRow row = new DBRow();
                row.setTableName(TableName);
                row.setDataMode(DBMode.SOURCE);
                for (int j = 1; j <= ColumnCount; ++j) {
                    final DBColumn column = new DBColumn(remd.getColumnName(j));
                    final String sColumnType = remd.getColumnTypeName(j).toUpperCase();
                    iType = this.convertType(sColumnType);
                    switch (iType) {
                        case 1: {
                            if (DBServiceBean.bConvert) {
                                try {
                                    if (this.sqlRst.getString(remd.getColumnName(j)) != null) {
                                        strValue = new String(this.sqlRst.getString(remd.getColumnName(j)).getBytes("ISO8859_1"), "GBK");
                                    }
                                }
                                catch (SQLException ex9) {
                                    System.out.println(this.ObjSysError.DataMessage9 + ex9.toString());
                                    System.out.println("\u9519\u8befsql:" + strSQL);
                                    ex9.printStackTrace();
                                    this.freeConnection();
                                    try {
                                        this.sqlRst.close();
                                    }
                                    catch (SQLException ex10) {
                                        ex10.printStackTrace();
                                    }
                                    this.sqlRst = null;
                                    mDBSet = null;
                                }
                                catch (UnsupportedEncodingException ex11) {
                                    System.out.println(this.ObjSysError.DataMessage10 + ex11.toString());
                                    ex11.printStackTrace();
                                }
                            }
                            else {
                                strValue = this.sqlRst.getString(remd.getColumnName(j));
                            }
                            column.setValue(strValue);
                            column.setType(DBType.STRING);
                            break;
                        }
                        case 2: {
                            column.setValue(this.sqlRst.getObject(remd.getColumnName(j)));
                            column.setType(DBType.FLOAT);
                            break;
                        }
                        case 3: {
                            if (this.sqlRst.getObject(remd.getColumnName(j)) != null) {
                                if (this.strWebType.equals("1") && this.DataBaseType.equals("1")) {
                                    final java.sql.Blob blob = this.sqlRst.getBlob(remd.getColumnName(j));
                                    final byte[] ab = blob.getBytes(1L, (int)blob.length());
                                    column.setValue(ab);
                                }
                                if (this.strWebType.equals("1") && this.DataBaseType.equals("2")) {
                                    final java.sql.Blob blob = this.sqlRst.getBlob(remd.getColumnName(j));
                                    final byte[] ab = blob.getBytes(1L, (int)blob.length());
                                    column.setValue(ab);
                                }
                                if (this.strWebType.equals("1") && this.DataBaseType.equals("3")) {
                                    final Blob blob2 = (Blob)this.sqlRst.getBlob(remd.getColumnName(j));
                                    final byte[] ab = blob2.getBytes(1L, (int)blob2.length());
                                    column.setValue(ab);
                                }
                                if (this.strWebType.equals("2") && this.DataBaseType.equals("1")) {
                                    final java.sql.Blob blob = this.sqlRst.getBlob(remd.getColumnName(j));
                                    final byte[] ab = blob.getBytes(1L, (int)blob.length());
                                    column.setValue(ab);
                                }
                                if (this.strWebType.equals("2") && this.DataBaseType.equals("2")) {
                                    final java.sql.Blob blob = this.sqlRst.getBlob(remd.getColumnName(j));
                                    final byte[] ab = blob.getBytes(1L, (int)blob.length());
                                    column.setValue(ab);
                                }
                                if (this.strWebType.equals("2") && this.DataBaseType.equals("3")) {
                                    final Blob blob2 = (Blob)this.sqlRst.getBlob(remd.getColumnName(j));
                                    final byte[] ab = blob2.getBytes(1L, (int)blob2.length());
                                    column.setValue(ab);
                                }
                                if (this.strWebType.equals("3") && this.DataBaseType.equals("1")) {
                                    final java.sql.Blob blob = this.sqlRst.getBlob(remd.getColumnName(j));
                                    final byte[] ab = blob.getBytes(1L, (int)blob.length());
                                    column.setValue(ab);
                                }
                                if (this.strWebType.equals("3") && this.DataBaseType.equals("2")) {
                                    final java.sql.Blob blob = this.sqlRst.getBlob(remd.getColumnName(j));
                                    final byte[] ab = blob.getBytes(1L, (int)blob.length());
                                    column.setValue(ab);
                                }
                                if (this.strWebType.equals("3") && this.DataBaseType.equals("3")) {
                                    final Blob blob2 = (Blob)this.sqlRst.getBlob(remd.getColumnName(j));
                                    final byte[] ab = blob2.getBytes(1L, (int)blob2.length());
                                    column.setValue(ab);
                                }
                            }
                            else {
                                column.setValue(null);
                            }
                            column.setType(DBType.BLOB);
                            break;
                        }
                        case 4: {
                            column.setValue(this.sqlRst.getTimestamp(remd.getColumnName(j)));
                            column.setType(DBType.DATE);
                            break;
                        }
                        case 5: {
                            column.setValue(this.sqlRst.getTimestamp(remd.getColumnName(j)));
                            column.setType(DBType.DATETIME);
                            break;
                        }
                        case 6: {
                            column.setValue(this.sqlRst.getObject(remd.getColumnName(j)));
                            column.setType(DBType.LONG);
                            break;
                        }
                        case 7: {
                            try {
                                if (this.sqlRst.getClob(remd.getColumnName(j)) != null) {
                                    final char[] ac = new char[299];
                                    String content = "";
                                    Reader reader = null;
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("1")) {
                                        final CLOB clob = (CLOB)this.sqlRst.getObject(remd.getColumnName(j));
                                        reader = clob.getCharacterStream();
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("2")) {
                                        final Clob clob2 = (Clob)this.sqlRst.getObject(remd.getColumnName(j));
                                        reader = clob2.getCharacterStream();
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("3")) {
                                        final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getObject(remd.getColumnName(j));
                                        reader = clob3.getCharacterStream();
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("1")) {
                                        final Clob clob2 = (Clob)this.sqlRst.getObject(remd.getColumnName(j));
                                        reader = clob2.getCharacterStream();
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("2")) {
                                        final Clob clob2 = (Clob)this.sqlRst.getObject(remd.getColumnName(j));
                                        reader = clob2.getCharacterStream();
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("3")) {
                                        final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getObject(remd.getColumnName(j));
                                        reader = clob3.getCharacterStream();
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("1")) {
                                        final CLOB clob = (CLOB)this.sqlRst.getObject(remd.getColumnName(j));
                                        reader = clob.getCharacterStream();
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("2")) {
                                        final Clob clob2 = (Clob)this.sqlRst.getObject(remd.getColumnName(j));
                                        reader = clob2.getCharacterStream();
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("3")) {
                                        final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getObject(remd.getColumnName(j));
                                        reader = clob3.getCharacterStream();
                                    }
                                    int y;
                                    while ((y = reader.read(ac, 0, 299)) != -1) {
                                        content += new String(ac, 0, y);
                                    }
                                    column.setValue(content);
                                }
                                else {
                                    column.setValue(null);
                                }
                                column.setType(DBType.CLOB);
                            }
                            catch (IOException ex12) {
                                System.out.println("CLOB" + ex12.toString());
                            }
                            catch (SQLException ex13) {
                                System.out.println("CLOB" + ex13.toString());
                            }
                            break;
                        }
                    }
                    row.addColumn(column);
                }
                mDBSet.AddRow(row);
            }
            this.sqlRst.close();
            this.sqlRst = null;
        }
        catch (Exception ex14) {
            System.out.println(this.ObjSysError.DataMessage9 + ex14.toString());
            System.out.println("\u9519\u8befsql:" + strSQL);
            ex14.printStackTrace();
            this.freeConnection();
            try {
                this.sqlRst.close();
            }
            catch (SQLException ex15) {
                ex15.printStackTrace();
            }
            this.sqlRst = null;
            mDBSet = null;
        }
        this.freeConnection();
        return mDBSet;
    }
    
    public DBSet QuerySQL(final String dsJNID, final String strSQL) {
        return this.QuerySQL(dsJNID, strSQL, "");
    }
    
    public DBSet QuerySQL(final String dsJNID, final String strSQL, final String[] value) {
        String strC = "";
        String strSql = "";
        int k = 0;
        int num = 0;
        final String type = value[value.length - 1];
        for (int i = 0; i < strSQL.length(); ++i) {
            try {
                if (i < strSQL.length()) {
                    strC = "";
                    strC = strSQL.substring(i, i + 1);
                    if (strC.equals("?")) {
                        if (type.substring(k, k + 1).equals("1")) {
                            strSql = strSql + strSQL.substring(num, i) + "'" + value[k] + "'";
                        }
                        if (type.substring(k, k + 1).equals("2")) {
                            strSql = strSql + strSQL.substring(num, i) + value[k];
                        }
                        if (type.substring(k, k + 1).equals("3")) {
                            if (value[k].length() == 10) {
                                if (this.DataBaseType.equals("2")) {
                                    strSql = strSql + strSQL.substring(num, i) + "'" + value[k] + "'";
                                }
                                else {
                                    strSql = strSql + strSQL.substring(num, i) + "to_date('" + value[k] + "','YYYY-MM-DD')";
                                }
                            }
                            if (value[k].length() > 10) {
                                if (this.DataBaseType.equals("2")) {
                                    strSql = strSql + strSQL.substring(num, i) + "'" + value[k] + "'";
                                }
                                else {
                                    strSql = strSql + strSQL.substring(num, i) + "to_date('" + value[k] + "','YYYY-MM-DD hh24:mi:ss')";
                                }
                            }
                            if (value[k].length() < 10) {
                                strSql = strSql + strSQL.substring(num, i) + value[k];
                            }
                        }
                        ++k;
                        num = i + 1;
                    }
                }
            }
            catch (Exception ex) {
                System.out.println("\u7ec4\u6210sql\u51fa\u9519\uff1a" + ex.toString());
                ex.printStackTrace();
            }
        }
        strSql += strSQL.substring(num);
        return this.QuerySQL(dsJNID, strSql, "");
    }
    
    public DBSet QueryTable(final String dsJNID, final String TableName) {
        final String strSQL = "Select * From " + TableName;
        return this.QuerySQL(dsJNID, strSQL, TableName);
    }
    
    public DBSet QueryColumns(final String dsJNID, final String TableName, final String[] ColumnName) {
        if (ColumnName.length == 0) {
            return this.QueryTable(dsJNID, TableName);
        }
        String strSQL = "Select ";
        for (int iLoop = 0; iLoop < ColumnName.length; ++iLoop) {
            strSQL = strSQL + ColumnName[iLoop] + ",";
        }
        strSQL = strSQL.substring(0, strSQL.length() - 1) + " From " + TableName;
        return this.QuerySQL(strSQL, TableName);
    }
    
    public DBSet QueryCondition(final String dsJNID, final String TableName, final String[] ColumnName, final String Condition) {
        if (ColumnName.length == 0) {
            return this.QueryTable(dsJNID, TableName);
        }
        String strSQL = "Select ";
        for (int iLoop = 0; iLoop < ColumnName.length; ++iLoop) {
            strSQL = strSQL + ColumnName[iLoop] + ",";
        }
        strSQL = strSQL.substring(0, strSQL.length() - 1) + " From " + TableName;
        if (Condition.length() == 0) {
            return this.QueryColumns(dsJNID, TableName, ColumnName);
        }
        strSQL = strSQL + " Where " + Condition;
        return this.QuerySQL(dsJNID, strSQL, TableName);
    }
    
    public boolean ExecuteSQL(final String dsJNID, String SQL) {
        boolean result = false;
        PreparedStatement pst = null;
        if (!this.getConnection(dsJNID)) {
            return false;
        }
        if (this.DataBaseType.equals("3")) {
            SQL = this.GetMysql_sql(SQL);
        }
        if (this.DataBaseType.equals("2")) {
            SQL = this.GetMssql_sql(SQL);
        }
        try {
            System.out.println(this.ObjSysError.DataMessage3 + SQL);
            String Dtable = "";
            final int wnum = SQL.toUpperCase().indexOf("INSERT INTO");
            if (wnum != -1 && DBServiceBean.clients % 5000 >= 0 && DBServiceBean.clients % 5000 <= 6) {
                try {
                    Dtable = SQL.toUpperCase().substring(12, SQL.toUpperCase().indexOf("(")).trim();
                }
                catch (Exception ex4) {
                    Dtable = "";
                }
                if (Dtable.length() > 0) {
                    try {
                        final DBServer dbser = new DBServer();
                        dbser.isTable(Dtable);
                    }
                    catch (Exception ex1) {
                        ex1.printStackTrace();
                    }
                }
            }
            this.sqlConn.setAutoCommit(false);
            pst = this.sqlConn.prepareStatement(SQL);
            pst.execute();
            this.sqlConn.commit();
            result = true;
        }
        catch (Exception ex2) {
            System.out.println(this.ObjSysError.DataMessage9 + ex2.toString());
            System.out.println("\u9519\u8befsql: " + SQL);
            ex2.printStackTrace();
            this.freeConnection();
            try {
                this.sqlRst.close();
            }
            catch (SQLException ex3) {
                ex3.printStackTrace();
            }
            this.sqlRst = null;
            result = false;
        }
        if (pst != null) {
            try {
                pst.close();
                pst = null;
            }
            catch (Exception ex5) {}
        }
        this.freeConnection();
        return result;
    }
    
    public boolean ExecuteSQL(final String dsJNID, final String SQL, final String[] value) {
        boolean result = false;
        String strC = "";
        String strSql = "";
        int k = 0;
        int num = 0;
        final String type = value[value.length - 1];
        for (int i = 0; i < SQL.length(); ++i) {
            try {
                if (i < SQL.length()) {
                    strC = "";
                    strC = SQL.substring(i, i + 1);
                    if (strC.equals("?")) {
                        if (type.substring(k, k + 1).equals("1")) {
                            strSql = strSql + SQL.substring(num, i) + "'" + value[k] + "'";
                        }
                        if (type.substring(k, k + 1).equals("2")) {
                            strSql = strSql + SQL.substring(num, i) + value[k];
                        }
                        if (type.substring(k, k + 1).equals("3")) {
                            if (value[k].length() == 10) {
                                if (this.DataBaseType.equals("2")) {
                                    strSql = strSql + SQL.substring(num, i) + "'" + value[k] + "'";
                                }
                                else {
                                    strSql = strSql + SQL.substring(num, i) + "to_date('" + value[k] + "','YYYY-MM-DD')";
                                }
                            }
                            if (value[k].length() > 10) {
                                if (this.DataBaseType.equals("2")) {
                                    strSql = strSql + SQL.substring(num, i) + "'" + value[k] + "'";
                                }
                                else {
                                    strSql = strSql + SQL.substring(num, i) + "to_date('" + value[k] + "','YYYY-MM-DD hh24:mi:ss')";
                                }
                            }
                            if (value[k].length() < 10) {
                                strSql = strSql + SQL.substring(num, i) + value[k];
                            }
                        }
                        ++k;
                        num = i + 1;
                    }
                }
            }
            catch (Exception ex) {
                System.out.println("\u7ec4\u6210sql\u51fa\u9519\uff1a" + ex.toString());
                ex.printStackTrace();
            }
        }
        strSql += SQL.substring(num);
        result = this.ExecuteSQL(dsJNID, strSql);
        return result;
    }
    
    public boolean ExecuteSQLs(final String dsJNID, final String[] SQLs) {
        if (SQLs.length == 0) {
            return false;
        }
        boolean result = false;
        PreparedStatement pst = null;
        if (!this.getConnection(dsJNID)) {
            return false;
        }
        int i = 0;
        try {
            this.sqlConn.setAutoCommit(false);
            for (i = 0; i < SQLs.length; ++i) {
                if (this.DataBaseType.equals("3")) {
                    SQLs[i] = this.GetMysql_sql(SQLs[i]);
                }
                else if (this.DataBaseType.equals("2")) {
                    SQLs[i] = this.GetMssql_sql(SQLs[i]);
                }
                System.out.println("\u51c6\u5907\u6267\u884c\u7684SQL\u8bed\u53e5\uff1a" + SQLs[i]);
                pst = this.sqlConn.prepareStatement(SQLs[i]);
                pst.execute();
            }
            this.sqlConn.commit();
            result = true;
        }
        catch (Exception ex) {
            System.out.println(this.ObjSysError.DataMessage9 + ex.toString());
            System.out.println("number:" + Integer.toString(i) + " \u9519\u8befSQL\uff1a" + SQLs[i]);
            ex.printStackTrace();
            this.freeConnection();
            try {
                this.sqlConn.commit();
                this.sqlRst.close();
            }
            catch (SQLException ex2) {
                ex2.printStackTrace();
            }
            this.sqlRst = null;
            result = false;
        }
        if (pst != null) {
            try {
                pst.close();
                pst = null;
            }
            catch (Exception ex3) {}
        }
        this.freeConnection();
        return result;
    }
    
    public boolean ExecuteSQLs(final String dsJNID, final String[] SQLs, final List<Object> list) {
        final String[] NSQL = new String[SQLs.length];
        String SQL = "";
        String strC = "";
        String strSql = "";
        String type = "";
        int k = 0;
        int num = 0;
        for (int sn = 0; sn < SQLs.length; ++sn) {
            SQL = SQLs[sn];
            final String[] value = (String[]) list.get(sn);
            strC = "";
            strSql = "";
            k = 0;
            num = 0;
            type = value[value.length - 1];
            for (int i = 0; i < SQL.length(); ++i) {
                try {
                    if (i < SQL.length()) {
                        strC = "";
                        strC = SQL.substring(i, i + 1);
                        if (strC.equals("?")) {
                            if (type.substring(k, k + 1).equals("1")) {
                                strSql = strSql + SQL.substring(num, i) + "'" + value[k] + "'";
                            }
                            if (type.substring(k, k + 1).equals("2")) {
                                strSql = strSql + SQL.substring(num, i) + value[k];
                            }
                            if (type.substring(k, k + 1).equals("3")) {
                                if (value[k].length() == 10) {
                                    if (this.DataBaseType.equals("2")) {
                                        strSql = strSql + SQL.substring(num, i) + "'" + value[k] + "'";
                                    }
                                    else {
                                        strSql = strSql + SQL.substring(num, i) + "to_date('" + value[k] + "','YYYY-MM-DD')";
                                    }
                                }
                                if (value[k].length() > 10) {
                                    if (this.DataBaseType.equals("2")) {
                                        strSql = strSql + SQL.substring(num, i) + "'" + value[k] + "'";
                                    }
                                    else {
                                        strSql = strSql + SQL.substring(num, i) + "to_date('" + value[k] + "','YYYY-MM-DD hh24:mi:ss')";
                                    }
                                }
                                if (value[k].length() < 10) {
                                    strSql = strSql + SQL.substring(num, i) + value[k];
                                }
                            }
                            ++k;
                            num = i + 1;
                        }
                    }
                }
                catch (Exception ex) {
                    System.out.println("\u7ec4\u6210sql\u51fa\u9519\uff1a" + ex.toString());
                    ex.printStackTrace();
                }
            }
            strSql += SQL.substring(num);
            NSQL[sn] = strSql;
        }
        return this.ExecuteSQLs(dsJNID, NSQL);
    }
    
    public boolean ExecuteSQLs(final String dsJNID, final String SQLs, final String[][] list) {
        final String[] NSQL = new String[list.length - 1];
        String SQL = "";
        String strC = "";
        String strSql = "";
        String type = "";
        int k = 0;
        int num = 0;
        for (int sn = 0; sn < list.length - 1; ++sn) {
            SQL = SQLs;
            strC = "";
            strSql = "";
            k = 0;
            num = 0;
            for (int i = 0; i < SQL.length(); ++i) {
                try {
                    if (i < SQL.length()) {
                        strC = "";
                        strC = SQL.substring(i, i + 1);
                        if (strC.equals("?")) {
                            type = list[list.length - 1][k];
                            if (type.equals("1")) {
                                strSql = strSql + SQL.substring(num, i) + "'" + list[sn][k] + "'";
                            }
                            if (type.equals("2")) {
                                strSql = strSql + SQL.substring(num, i) + list[sn][k];
                            }
                            if (type.equals("3")) {
                                if (list[sn][k].length() == 10) {
                                    if (this.DataBaseType.equals("2")) {
                                        strSql = strSql + SQL.substring(num, i) + "'" + list[sn][k] + "'";
                                    }
                                    else {
                                        strSql = strSql + SQL.substring(num, i) + "to_date('" + list[sn][k] + "','YYYY-MM-DD')";
                                    }
                                }
                                if (list[sn][k].length() > 10) {
                                    if (this.DataBaseType.equals("2")) {
                                        strSql = strSql + SQL.substring(num, i) + "'" + list[sn][k] + "'";
                                    }
                                    else {
                                        strSql = strSql + SQL.substring(num, i) + "to_date('" + list[sn][k] + "','YYYY-MM-DD hh24:mi:ss')";
                                    }
                                }
                                if (list[sn][k].length() < 10) {
                                    strSql = strSql + SQL.substring(num, i) + list[sn][k];
                                }
                            }
                            ++k;
                            num = i + 1;
                        }
                    }
                }
                catch (Exception ex) {
                    System.out.println("\u7ec4\u6210sql\u51fa\u9519\uff1a" + ex.toString());
                    ex.printStackTrace();
                }
            }
            strSql += SQL.substring(num);
            NSQL[sn] = strSql;
        }
        return this.ExecuteSQLs(dsJNID, NSQL);
    }
    
    public boolean ExecuteInsert(final String dsJNID, final DBRow DBRow) {
        System.out.println("\u6267\u884cExecuteInsert:");
        boolean result = false;
        String strSql = "";
        String strMysqlSql = "";
        PreparedStatement ps = null;
        if (!this.getConnection(dsJNID)) {
            return false;
        }
        if (this.sqlConn == null) {
            this.freeConnection();
            return result;
        }
        strSql = this.MakeInsertSql(DBRow);
        if (strSql == "") {
            System.out.println(this.ObjSysError.DataMessage11);
            this.freeConnection();
            return result;
        }
        try {
            this.sqlConn.setAutoCommit(false);
            ps = this.sqlConn.prepareStatement(strSql);
            if (this.setParamValue(ps, DBRow)) {
                if (this.DataBaseType.equals("3") || this.DataBaseType.equals("2")) {
                    strMysqlSql = this.MakeMysqlUpdateSql(DBRow);
                    ps.execute();
                    ps = this.sqlConn.prepareStatement(strMysqlSql);
                    System.out.println("strMysqlSql--:" + strMysqlSql);
                    int num = 0;
                    final String[] saBlobFields1 = this.getBlobFields(DBRow);
                    if (saBlobFields1 != null) {
                        for (int j = 0; j < saBlobFields1.length; ++j) {
                            ++num;
                            final byte[] bBlob1 = DBRow.Column(saBlobFields1[j]).getBlob();
                            ps.setBytes(num, bBlob1);
                        }
                    }
                    final String[] saClobFields1 = this.getClobFields(DBRow);
                    if (saClobFields1 != null) {
                        for (int i = 0; i < saClobFields1.length; ++i) {
                            ++num;
                            ps.setBytes(num, DBRow.Column(saClobFields1[i]).getClob().getBytes());
                        }
                    }
                    if (num > 0) {
                        ps.execute();
                    }
                }
                else {
                    System.out.println(this.ObjSysError.DataMessage3 + strSql);
                    ps.executeUpdate();
                    if (this.haveBlobFields(DBRow)) {
                        ps.close();
                        final String strSQL = this.MakeSelectBlobSql(DBRow);
                        ps = this.sqlConn.prepareStatement(strSQL);
                        System.out.println(this.ObjSysError.DataMessage3 + strSQL);
                        this.sqlRst = ps.executeQuery();
                        if (this.sqlRst.next()) {
                            final String[] saBlobFields2 = this.getBlobFields(DBRow);
                            if (saBlobFields2 != null) {
                                for (int k = 0; k < saBlobFields2.length; ++k) {
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("1")) {
                                        final java.sql.Blob blob = this.sqlRst.getBlob(saBlobFields2[k]);
                                        final OutputStream bout = ((BLOB)blob).getBinaryOutputStream();
                                        final byte[] bBlob2 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout.write(bBlob2, 0, bBlob2.length);
                                        bout.close();
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("2")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("3")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("1")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("2")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("3")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("1")) {
                                        final java.sql.Blob blob = this.sqlRst.getBlob(saBlobFields2[k]);
                                        final OutputStream bout = ((BLOB)blob).getBinaryOutputStream();
                                        final byte[] bBlob2 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout.write(bBlob2, 0, bBlob2.length);
                                        bout.close();
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("2")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("3")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                }
                                this.sqlConn.commit();
                            }
                        }
                    }
                    if (this.haveClobFields(DBRow)) {
                        final String strSQL = this.MakeSelectClobSql(DBRow);
                        ps = this.sqlConn.prepareStatement(strSQL);
                        System.out.println(this.ObjSysError.DataMessage3 + strSQL);
                        this.sqlRst = ps.executeQuery();
                        if (this.sqlRst.next()) {
                            final String[] saClobFields2 = this.getClobFields(DBRow);
                            if (saClobFields2 != null) {
                                for (int k = 0; k < saClobFields2.length; ++k) {
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("1")) {
                                        final CLOB clob = (CLOB)this.sqlRst.getClob(saClobFields2[k]);
                                        clob.putString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("2")) {
                                        final Clob clob2 = this.sqlRst.getClob(saClobFields2[k]);
                                        clob2.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("3")) {
                                        final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getClob(saClobFields2[k]);
                                        clob3.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("1")) {
                                        final Clob clob2 = this.sqlRst.getClob(saClobFields2[k]);
                                        clob2.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("2")) {
                                        final Clob clob2 = this.sqlRst.getClob(saClobFields2[k]);
                                        clob2.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("3")) {
                                        final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getClob(saClobFields2[k]);
                                        clob3.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("1")) {
                                        final CLOB clob = (CLOB)this.sqlRst.getClob(saClobFields2[k]);
                                        clob.putString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("2")) {
                                        final Clob clob2 = this.sqlRst.getClob(saClobFields2[k]);
                                        clob2.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("3")) {
                                        final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getClob(saClobFields2[k]);
                                        clob3.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                }
                            }
                        }
                    }
                }
                this.sqlConn.commit();
            }
            else {
                System.out.println("\u51c6\u5907\u6267\u884c\u7684sql: " + strSql);
                String Dtable = "";
                final int wnum = strSql.toUpperCase().indexOf("INSERT INTO");
                if (wnum != -1 && DBServiceBean.clients % 5000 >= 0 && DBServiceBean.clients % 5000 <= 6) {
                    try {
                        Dtable = strSql.toUpperCase().substring(12, strSql.toUpperCase().indexOf("(")).trim();
                    }
                    catch (Exception ex) {
                        Dtable = "";
                    }
                    if (Dtable.length() > 0) {
                        try {
                            final DBServer dbser = new DBServer();
                            dbser.isTable(Dtable);
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                ps.execute();
                this.sqlConn.commit();
            }
            result = true;
        }
        catch (IOException ex2) {
            System.out.println(this.ObjSysError.DataMessage12 + ex2.toString());
            ex2.printStackTrace();
            this.freeConnection();
        }
        catch (Exception ex3) {
            System.out.println(this.ObjSysError.DataMessage11 + ex3.toString());
            ex3.printStackTrace();
            this.freeConnection();
        }
        if (ps != null) {
            try {
                ps.close();
                ps = null;
            }
            catch (SQLException ex4) {
                System.out.println(this.ObjSysError.DataMessage11 + ex4.toString());
                ex4.printStackTrace();
            }
        }
        this.freeConnection();
        ps = null;
        this.sqlRst = null;
        return result;
    }
    
    private String MakeSelectBlobSql(final DBRow DBRow) {
        final String[] saPrimaryCol = DBRow.getPrimaryKeyName();
        final String[] saBlobColumn = this.getBlobFields(DBRow);
        String strWhere = "";
        String strBlob = "";
        for (int i = 0; i < saPrimaryCol.length; ++i) {
            if (DBRow.Column(saPrimaryCol[i]).getType().getValue() == DBType.STRING.getValue()) {
                strWhere = strWhere + saPrimaryCol[i] + "='" + DBRow.Column(saPrimaryCol[i]).getString() + "',";
            }
            else {
                strWhere = strWhere + saPrimaryCol[i] + "=" + Integer.toString(DBRow.Column(saPrimaryCol[i]).getInteger()) + ",";
            }
        }
        for (int i = 0; i < saBlobColumn.length; ++i) {
            strBlob = strBlob + saBlobColumn[i] + ",";
        }
        if (strBlob.length() > 0) {
            strBlob = strBlob.substring(0, strBlob.length() - 1);
        }
        if (strWhere.length() > 0) {
            strWhere = strWhere.substring(0, strWhere.length() - 1);
        }
        return "Select " + strBlob + " From " + DBRow.getTableName() + " Where " + strWhere + " for update";
    }
    
    private String MakeSelectClobSql(final DBRow DBRow) {
        final String[] saPrimaryCol = DBRow.getPrimaryKeyName();
        final String[] saClobColumn = this.getClobFields(DBRow);
        String strWhere = "";
        String strClob = "";
        for (int i = 0; i < saPrimaryCol.length; ++i) {
            if (DBRow.Column(saPrimaryCol[i]).getType().getValue() == DBType.STRING.getValue()) {
                strWhere = strWhere + saPrimaryCol[i] + "='" + DBRow.Column(saPrimaryCol[i]).getString() + "',";
            }
            else {
                strWhere = strWhere + saPrimaryCol[i] + "=" + Integer.toString(DBRow.Column(saPrimaryCol[i]).getInteger()) + ",";
            }
        }
        for (int i = 0; i < saClobColumn.length; ++i) {
            strClob = strClob + saClobColumn[i] + ",";
        }
        if (strClob.length() > 0) {
            strClob = strClob.substring(0, strClob.length() - 1);
        }
        if (strWhere.length() > 0) {
            strWhere = strWhere.substring(0, strWhere.length() - 1);
        }
        return "Select " + strClob + " From " + DBRow.getTableName() + " Where " + strWhere + " for update";
    }
    
    private String MakeSelectBlobSql(final DBRow DBRow, final String Condition) {
        final String[] saBlobColumn = this.getBlobFields(DBRow);
        String strBlob = "";
        for (int i = 0; i < saBlobColumn.length; ++i) {
            strBlob = strBlob + saBlobColumn[i] + ",";
        }
        if (strBlob.length() > 0) {
            strBlob = strBlob.substring(0, strBlob.length() - 1);
        }
        return "Select " + strBlob + " From " + DBRow.getTableName() + " Where " + Condition + " for update";
    }
    
    private String MakeSelectClobSql(final DBRow DBRow, final String Condition) {
        final String[] saClobColumn = this.getClobFields(DBRow);
        String strClob = "";
        for (int i = 0; i < saClobColumn.length; ++i) {
            strClob = strClob + saClobColumn[i] + ",";
        }
        if (strClob.length() > 0) {
            strClob = strClob.substring(0, strClob.length() - 1);
        }
        return "Select " + strClob + " From " + DBRow.getTableName() + " Where " + Condition + " for update";
    }
    
    private String[] getBlobFields(final DBRow DBRow) {
        final String[] saColumnName = DBRow.getAllColumnName();
        String[] returnValue = null;
        final Vector<String> vTmp = new Vector<String>();
        for (int i = 0; i < DBRow.getColumnCount(); ++i) {
            if (DBRow.Column(saColumnName[i]).getType().getValue() == DBType.BLOB.getValue() && DBRow.Column(saColumnName[i]).getValue() != null) {
                vTmp.add(saColumnName[i]);
            }
        }
        if (vTmp.size() > 0) {
            returnValue = new String[vTmp.size()];
            for (int i = 0; i < vTmp.size(); ++i) {
                returnValue[i] = vTmp.elementAt(i);
            }
        }
        return returnValue;
    }
    
    private String[] getClobFields(final DBRow DBRow) {
        final String[] saColumnName = DBRow.getAllColumnName();
        String[] returnValue = null;
        final Vector<String> vTmp = new Vector<String>();
        for (int i = 0; i < DBRow.getColumnCount(); ++i) {
            if (DBRow.Column(saColumnName[i]).getType().getValue() == DBType.CLOB.getValue() && DBRow.Column(saColumnName[i]).getValue() != null) {
                vTmp.add(saColumnName[i]);
            }
        }
        if (vTmp.size() > 0) {
            returnValue = new String[vTmp.size()];
            for (int i = 0; i < vTmp.size(); ++i) {
                returnValue[i] = vTmp.elementAt(i);
            }
        }
        return returnValue;
    }
    
    private boolean haveBlobFields(final DBRow DBRow) {
        final String[] saColumnName = DBRow.getAllColumnName();
        for (int i = 0; i < DBRow.getColumnCount(); ++i) {
            if (DBRow.Column(saColumnName[i]).getType().getValue() == DBType.BLOB.getValue() && DBRow.Column(saColumnName[i]).getValue() != null) {
                return true;
            }
        }
        return false;
    }
    
    private boolean haveClobFields(final DBRow DBRow) {
        final String[] saColumnName = DBRow.getAllColumnName();
        try {
            for (int i = 0; i < DBRow.getColumnCount(); ++i) {
                if (DBRow.Column(saColumnName[i]).getType().getValue() == DBType.CLOB.getValue() && DBRow.Column(saColumnName[i]).getValue() != null) {
                    return true;
                }
            }
        }
        catch (Exception ex) {
            System.out.println("haveClobFields" + ex.toString());
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean ExecuteEdit(final String dsJNID, final DBRow DBRow) {
        System.out.println("\u6267\u884c ExecuteEdit:");
        boolean result = false;
        final String[] keyname = DBRow.getPrimaryKeyName();
        if (keyname == null) {
            System.out.println(this.ObjSysError.DataMessage13);
            return false;
        }
        if (DBRow.getTableName() == "") {
            System.out.println(this.ObjSysError.DataMessage14);
            return false;
        }
        String strSql = "";
        String strMysqlSql = "";
        PreparedStatement ps = null;
        if (!this.getConnection(dsJNID)) {
            return false;
        }
        if (this.sqlConn == null) {
            System.out.println(this.ObjSysError.DataMessage15);
            this.freeConnection();
            return result;
        }
        strSql = this.MakeUpdateSql(DBRow);
        try {
            this.sqlConn.setAutoCommit(false);
            ps = this.sqlConn.prepareStatement(strSql);
            if (this.setParamValue(ps, DBRow)) {
                if (this.DataBaseType.equals("3") || this.DataBaseType.equals("2")) {
                    strMysqlSql = this.MakeMysqlUpdateSql(DBRow);
                    ps.execute();
                    ps = this.sqlConn.prepareStatement(strMysqlSql);
                    System.out.println("strMysqlSql--:" + strMysqlSql);
                    int num = 0;
                    final String[] saBlobFields1 = this.getBlobFields(DBRow);
                    if (saBlobFields1 != null) {
                        for (int j = 0; j < saBlobFields1.length; ++j) {
                            ++num;
                            final byte[] bBlob1 = DBRow.Column(saBlobFields1[j]).getBlob();
                            ps.setBytes(num, bBlob1);
                        }
                    }
                    final String[] saClobFields1 = this.getClobFields(DBRow);
                    if (saClobFields1 != null) {
                        for (int i = 0; i < saClobFields1.length; ++i) {
                            ++num;
                            ps.setBytes(num, DBRow.Column(saClobFields1[i]).getClob().getBytes());
                        }
                    }
                    if (num > 0) {
                        ps.execute();
                    }
                }
                else {
                    System.out.println(this.ObjSysError.DataMessage3 + strSql);
                    ps.executeUpdate();
                    if (this.haveBlobFields(DBRow)) {
                        ps.close();
                        final String strSQL = this.MakeSelectBlobSql(DBRow);
                        ps = this.sqlConn.prepareStatement(strSQL);
                        System.out.println(this.ObjSysError.DataMessage3 + strSQL);
                        this.sqlRst = ps.executeQuery();
                        System.out.println("SQL\uff1a" + strSQL + " Execute");
                        if (this.sqlRst.next()) {
                            final String[] saBlobFields2 = this.getBlobFields(DBRow);
                            if (saBlobFields2 != null) {
                                for (int k = 0; k < saBlobFields2.length; ++k) {
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("1")) {
                                        final java.sql.Blob blob = this.sqlRst.getBlob(saBlobFields2[k]);
                                        final OutputStream bout = ((BLOB)blob).getBinaryOutputStream();
                                        final byte[] bBlob2 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout.write(bBlob2, 0, bBlob2.length);
                                        bout.close();
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("2")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("3")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("1")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("2")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("3")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("1")) {
                                        final java.sql.Blob blob = this.sqlRst.getBlob(saBlobFields2[k]);
                                        final OutputStream bout = ((BLOB)blob).getBinaryOutputStream();
                                        final byte[] bBlob2 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout.write(bBlob2, 0, bBlob2.length);
                                        bout.close();
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("2")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("3")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[k]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[k]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                }
                            }
                        }
                    }
                    if (this.haveClobFields(DBRow)) {
                        final String strSQL = this.MakeSelectClobSql(DBRow);
                        ps = this.sqlConn.prepareStatement(strSQL);
                        System.out.println(this.ObjSysError.DataMessage3 + strSQL);
                        this.sqlRst = ps.executeQuery();
                        if (this.sqlRst.next()) {
                            final String[] saClobFields2 = this.getClobFields(DBRow);
                            if (saClobFields2 != null) {
                                for (int k = 0; k < saClobFields2.length; ++k) {
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("1")) {
                                        final CLOB clob = (CLOB)this.sqlRst.getClob(saClobFields2[k]);
                                        clob.putString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("2")) {
                                        final Clob clob2 = this.sqlRst.getClob(saClobFields2[k]);
                                        clob2.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("3")) {
                                        final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getClob(saClobFields2[k]);
                                        clob3.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("1")) {
                                        final Clob clob2 = this.sqlRst.getClob(saClobFields2[k]);
                                        clob2.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("2")) {
                                        final Clob clob2 = this.sqlRst.getClob(saClobFields2[k]);
                                        clob2.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("3")) {
                                        final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getClob(saClobFields2[k]);
                                        clob3.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("1")) {
                                        final CLOB clob = (CLOB)this.sqlRst.getClob(saClobFields2[k]);
                                        clob.putString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("2")) {
                                        final Clob clob2 = this.sqlRst.getClob(saClobFields2[k]);
                                        clob2.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("3")) {
                                        final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getClob(saClobFields2[k]);
                                        clob3.setString(1L, DBRow.Column(saClobFields2[k]).getClob());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                ps.execute();
            }
            this.sqlConn.commit();
            result = true;
        }
        catch (Exception ex) {
            System.out.println(this.ObjSysError.DataMessage16 + ex.toString());
            ex.printStackTrace();
            this.freeConnection();
            result = false;
        }
        if (ps != null) {
            try {
                ps.close();
                ps = null;
            }
            catch (SQLException ex2) {
                ex2.printStackTrace();
            }
        }
        this.freeConnection();
        ps = null;
        this.sqlRst = null;
        return result;
    }
    
    public boolean ExecuteEdit(final String dsJNID, final DBRow DBRow, final String Condition) {
        System.out.println("\u6267\u884c ExecuteEdit:");
        boolean result = false;
        PreparedStatement ps = null;
        if (!this.getConnection(dsJNID)) {
            return false;
        }
        String strSql = "";
        String strMysqlSql = "";
        final String[] columnname = DBRow.getAllColumnName();
        strSql = "Update " + DBRow.getTableName() + " Set ";
        for (int i = 0; i < DBRow.getColumnCount(); ++i) {
            if (DBRow.Column(columnname[i]).getValue() != null) {
                if (DBRow.Column(columnname[i]).getType().getValue() == DBType.BLOB.getValue()) {
                    if (this.DataBaseType.equals("2") || this.DataBaseType.equals("3")) {
                        strSql = strSql + columnname[i] + "=null,";
                    }
                    else {
                        strSql = strSql + columnname[i] + "=empty_blob(),";
                    }
                }
                else if (DBRow.Column(columnname[i]).getType().getValue() == DBType.CLOB.getValue()) {
                    if (this.DataBaseType.equals("2") || this.DataBaseType.equals("3")) {
                        strSql = strSql + columnname[i] + "=null,";
                    }
                    else {
                        strSql = strSql + columnname[i] + "=empty_clob(),";
                    }
                }
                else {
                    strSql = strSql + columnname[i] + "=?,";
                }
            }
        }
        strSql = strSql.substring(0, strSql.length() - 1);
        strSql = strSql + " Where " + Condition;
        try {
            this.sqlConn.setAutoCommit(false);
            ps = this.sqlConn.prepareStatement(strSql);
            if (this.setParamValue(ps, DBRow)) {
                if (this.DataBaseType.equals("3") || this.DataBaseType.equals("2")) {
                    strMysqlSql = this.MakeMysqlUpdateSql(DBRow);
                    ps.execute();
                    ps = this.sqlConn.prepareStatement(strMysqlSql);
                    System.out.println("ExecuteEdit strMysqlSql:" + strMysqlSql);
                    int num = 0;
                    final String[] saBlobFields1 = this.getBlobFields(DBRow);
                    if (saBlobFields1 != null) {
                        for (int j = 0; j < saBlobFields1.length; ++j) {
                            ++num;
                            final byte[] bBlob1 = DBRow.Column(saBlobFields1[j]).getBlob();
                            ps.setBytes(num, bBlob1);
                        }
                    }
                    final String[] saClobFields1 = this.getClobFields(DBRow);
                    if (saClobFields1 != null) {
                        for (int k = 0; k < saClobFields1.length; ++k) {
                            ++num;
                            ps.setBytes(num, DBRow.Column(saClobFields1[k]).getClob().getBytes());
                        }
                    }
                    if (num > 0) {
                        ps.execute();
                    }
                }
                else {
                    ps.executeUpdate();
                    if (this.haveBlobFields(DBRow)) {
                        ps.close();
                        final String strSQL = this.MakeSelectBlobSql(DBRow, Condition);
                        ps = this.sqlConn.prepareStatement(strSQL);
                        System.out.println(this.ObjSysError.DataMessage3 + strSQL);
                        this.sqlRst = ps.executeQuery();
                        if (this.sqlRst.next()) {
                            final String[] saBlobFields2 = this.getBlobFields(DBRow);
                            if (saBlobFields2 != null) {
                                for (int l = 0; l < saBlobFields2.length; ++l) {
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("1")) {
                                        final java.sql.Blob blob = this.sqlRst.getBlob(saBlobFields2[l]);
                                        final OutputStream bout = ((BLOB)blob).getBinaryOutputStream();
                                        final byte[] bBlob2 = DBRow.Column(saBlobFields2[l]).getBlob();
                                        bout.write(bBlob2, 0, bBlob2.length);
                                        bout.close();
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("2")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[l]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[l]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("3")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[l]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[l]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("1")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[l]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[l]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("2")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[l]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[l]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("3")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[l]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[l]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("1")) {
                                        final java.sql.Blob blob = this.sqlRst.getBlob(saBlobFields2[l]);
                                        final OutputStream bout = ((BLOB)blob).getBinaryOutputStream();
                                        final byte[] bBlob2 = DBRow.Column(saBlobFields2[l]).getBlob();
                                        bout.write(bBlob2, 0, bBlob2.length);
                                        bout.close();
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("2")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[l]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[l]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("3")) {
                                        final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[l]).setBinaryStream(1L);
                                        final byte[] bBlob3 = DBRow.Column(saBlobFields2[l]).getBlob();
                                        bout2.write(bBlob3, 0, bBlob3.length);
                                        bout2.close();
                                    }
                                }
                            }
                        }
                    }
                    if (this.haveClobFields(DBRow)) {
                        final String strSQL = this.MakeSelectClobSql(DBRow, Condition);
                        ps = this.sqlConn.prepareStatement(strSQL);
                        System.out.println(this.ObjSysError.DataMessage3 + strSQL);
                        this.sqlRst = ps.executeQuery();
                        if (this.sqlRst.next()) {
                            final String[] saClobFields2 = this.getClobFields(DBRow);
                            if (saClobFields2 != null) {
                                for (int l = 0; l < saClobFields2.length; ++l) {
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("1")) {
                                        final CLOB clob = (CLOB)this.sqlRst.getClob(saClobFields2[l]);
                                        clob.putString(1L, DBRow.Column(saClobFields2[l]).getClob());
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("2")) {
                                        final Clob clob2 = this.sqlRst.getClob(saClobFields2[l]);
                                        clob2.setString(1L, DBRow.Column(saClobFields2[l]).getClob());
                                    }
                                    if (this.strWebType.equals("1") && this.DataBaseType.equals("3")) {
                                        final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getClob(saClobFields2[l]);
                                        clob3.setString(1L, DBRow.Column(saClobFields2[l]).getClob());
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("1")) {
                                        final Clob clob2 = this.sqlRst.getClob(saClobFields2[l]);
                                        clob2.setString(1L, DBRow.Column(saClobFields2[l]).getClob());
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("2")) {
                                        final Clob clob2 = this.sqlRst.getClob(saClobFields2[l]);
                                        clob2.setString(1L, DBRow.Column(saClobFields2[l]).getClob());
                                    }
                                    if (this.strWebType.equals("2") && this.DataBaseType.equals("3")) {
                                        final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getClob(saClobFields2[l]);
                                        clob3.setString(1L, DBRow.Column(saClobFields2[l]).getClob());
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("1")) {
                                        final CLOB clob = (CLOB)this.sqlRst.getClob(saClobFields2[l]);
                                        clob.putString(1L, DBRow.Column(saClobFields2[l]).getClob());
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("2")) {
                                        final Clob clob2 = this.sqlRst.getClob(saClobFields2[l]);
                                        clob2.setString(1L, DBRow.Column(saClobFields2[l]).getClob());
                                    }
                                    if (this.strWebType.equals("3") && this.DataBaseType.equals("3")) {
                                        final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getClob(saClobFields2[l]);
                                        clob3.setString(1L, DBRow.Column(saClobFields2[l]).getClob());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                System.out.println(this.ObjSysError.DataMessage3 + strSql);
                ps.execute();
            }
            this.sqlConn.commit();
            result = true;
        }
        catch (Exception ex) {
            System.out.println(this.ObjSysError.DataMessage17 + ex.toString());
            ex.printStackTrace();
            this.freeConnection();
            result = false;
        }
        if (ps != null) {
            try {
                ps.close();
                ps = null;
            }
            catch (SQLException ex2) {
                ex2.printStackTrace();
            }
        }
        this.freeConnection();
        ps = null;
        this.sqlRst = null;
        return result;
    }
    
    public boolean ExecuteDelete(final String dsJNID, final String TableName, final String Condition) {
        boolean result = false;
        String strSQL = "";
        strSQL = "Delete " + TableName + " Where " + Condition;
        result = this.ExecuteSQL(dsJNID, strSQL);
        return result;
    }
    
    public boolean UpdateAll(final String dsJNID, DBRow[] dArr, final String[] strBeforeSql, final String[] strAfterSql) {
        System.out.println("\u6267\u884c UpdateAll:");
        boolean bRtn = false;
        String strSql = "";
        String strMysqlSql = "";
        PreparedStatement ps = null;
        try {
            if (!this.getConnection(dsJNID)) {
                return false;
            }
            int iRowCount = 0;
            if (dArr == null) {
                bRtn = true;
                return bRtn;
            }
            iRowCount = dArr.length;
            this.sqlConn.setAutoCommit(false);
            if (strBeforeSql != null && strBeforeSql.length > 0) {
                for (int i = 0; i <= strBeforeSql.length - 1; ++i) {
                    System.out.println("UpdateAll" + this.ObjSysError.DataMessage3 + strBeforeSql[i]);
                    ps = this.sqlConn.prepareStatement(strBeforeSql[i]);
                    ps.execute();
                }
            }
            for (int iRowCounter = 0; iRowCounter <= iRowCount - 1; ++iRowCounter) {
                final DBRow d = dArr[iRowCounter];
                if (d.getDataMode().getValue() == DBMode.EDITED.getValue()) {
                    strSql = this.MakeUpdateSql(d);
                    strMysqlSql = this.MakeMysqlUpdateSql(d);
                }
                else if (d.getDataMode().getValue() == DBMode.NEW.getValue()) {
                    strSql = this.MakeInsertSql(d);
                    strMysqlSql = this.MakeMysqlUpdateSql(d);
                }
                System.out.println("UpdateAll" + this.ObjSysError.DataMessage3 + strSql);
                ps = this.sqlConn.prepareStatement(strSql);
                if (this.setParamValue(ps, d)) {
                    System.out.println(this.ObjSysError.DataMessage3 + strSql);
                    if (this.DataBaseType.equals("3") || this.DataBaseType.equals("2")) {
                        ps.execute();
                        ps = this.sqlConn.prepareStatement(strMysqlSql);
                        System.out.println("strMysqlSql:" + strMysqlSql);
                        int num = 0;
                        final String[] saBlobFields1 = this.getBlobFields(d);
                        if (saBlobFields1 != null) {
                            for (int j = 0; j < saBlobFields1.length; ++j) {
                                ++num;
                                final byte[] bBlob1 = d.Column(saBlobFields1[j]).getBlob();
                                ps.setBytes(num, bBlob1);
                            }
                        }
                        final String[] saClobFields1 = this.getClobFields(d);
                        if (saClobFields1 != null) {
                            for (int k = 0; k < saClobFields1.length; ++k) {
                                ++num;
                                ps.setBytes(num, d.Column(saClobFields1[k]).getClob().getBytes());
                            }
                        }
                        if (num > 0) {
                            ps.execute();
                        }
                    }
                    else {
                        ps.execute();
                        if (this.haveBlobFields(d)) {
                            ps.close();
                            final String strSQL = this.MakeSelectBlobSql(d);
                            ps = this.sqlConn.prepareStatement(strSQL);
                            System.out.println(this.ObjSysError.DataMessage3 + strSQL);
                            this.sqlRst = ps.executeQuery();
                            if (this.sqlRst.next()) {
                                final String[] saBlobFields2 = this.getBlobFields(d);
                                if (saBlobFields2 != null) {
                                    for (int j = 0; j < saBlobFields2.length; ++j) {
                                        if (this.strWebType.equals("1") && this.DataBaseType.equals("1")) {
                                            final java.sql.Blob blob = this.sqlRst.getBlob(saBlobFields2[j]);
                                            final OutputStream bout = ((BLOB)blob).getBinaryOutputStream();
                                            final byte[] bBlob2 = d.Column(saBlobFields2[j]).getBlob();
                                            bout.write(bBlob2, 0, bBlob2.length);
                                            bout.close();
                                        }
                                        if (this.strWebType.equals("1") && this.DataBaseType.equals("2")) {
                                            final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[j]).setBinaryStream(1L);
                                            final byte[] bBlob3 = d.Column(saBlobFields2[j]).getBlob();
                                            bout2.write(bBlob3, 0, bBlob3.length);
                                            bout2.close();
                                        }
                                        if (this.strWebType.equals("1") && this.DataBaseType.equals("3")) {
                                            final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[j]).setBinaryStream(1L);
                                            final byte[] bBlob3 = d.Column(saBlobFields2[j]).getBlob();
                                            bout2.write(bBlob3, 0, bBlob3.length);
                                            bout2.close();
                                        }
                                        if (this.strWebType.equals("2") && this.DataBaseType.equals("1")) {
                                            final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[j]).setBinaryStream(1L);
                                            final byte[] bBlob3 = d.Column(saBlobFields2[j]).getBlob();
                                            bout2.write(bBlob3, 0, bBlob3.length);
                                            bout2.close();
                                        }
                                        if (this.strWebType.equals("2") && this.DataBaseType.equals("2")) {
                                            final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[j]).setBinaryStream(1L);
                                            final byte[] bBlob3 = d.Column(saBlobFields2[j]).getBlob();
                                            bout2.write(bBlob3, 0, bBlob3.length);
                                            bout2.close();
                                        }
                                        if (this.strWebType.equals("2") && this.DataBaseType.equals("3")) {
                                            final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[j]).setBinaryStream(1L);
                                            final byte[] bBlob3 = d.Column(saBlobFields2[j]).getBlob();
                                            bout2.write(bBlob3, 0, bBlob3.length);
                                            bout2.close();
                                        }
                                        if (this.strWebType.equals("3") && this.DataBaseType.equals("1")) {
                                            final java.sql.Blob blob = this.sqlRst.getBlob(saBlobFields2[j]);
                                            final OutputStream bout = ((BLOB)blob).getBinaryOutputStream();
                                            final byte[] bBlob2 = d.Column(saBlobFields2[j]).getBlob();
                                            bout.write(bBlob2, 0, bBlob2.length);
                                            bout.close();
                                        }
                                        if (this.strWebType.equals("3") && this.DataBaseType.equals("2")) {
                                            final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[j]).setBinaryStream(1L);
                                            final byte[] bBlob3 = d.Column(saBlobFields2[j]).getBlob();
                                            bout2.write(bBlob3, 0, bBlob3.length);
                                            bout2.close();
                                        }
                                        if (this.strWebType.equals("3") && this.DataBaseType.equals("3")) {
                                            final OutputStream bout2 = this.sqlRst.getBlob(saBlobFields2[j]).setBinaryStream(1L);
                                            final byte[] bBlob3 = d.Column(saBlobFields2[j]).getBlob();
                                            bout2.write(bBlob3, 0, bBlob3.length);
                                            bout2.close();
                                        }
                                    }
                                }
                            }
                        }
                        if (this.haveClobFields(d)) {
                            final String strSQL = this.MakeSelectClobSql(d);
                            ps = this.sqlConn.prepareStatement(strSQL);
                            System.out.println(this.ObjSysError.DataMessage3 + strSQL);
                            this.sqlRst = ps.executeQuery();
                            if (this.sqlRst.next()) {
                                final String[] saClobFields2 = this.getClobFields(d);
                                if (saClobFields2 != null) {
                                    for (int l = 0; l < saClobFields2.length; ++l) {
                                        if (this.strWebType.equals("1") && this.DataBaseType.equals("1")) {
                                            final CLOB clob = (CLOB)this.sqlRst.getClob(saClobFields2[l]);
                                            clob.putString(1L, d.Column(saClobFields2[l]).getClob());
                                        }
                                        if (this.strWebType.equals("1") && this.DataBaseType.equals("2")) {
                                            final Clob clob2 = this.sqlRst.getClob(saClobFields2[l]);
                                            clob2.setString(1L, d.Column(saClobFields2[l]).getClob());
                                        }
                                        if (this.strWebType.equals("1") && this.DataBaseType.equals("3")) {
                                            final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getClob(saClobFields2[l]);
                                            clob3.setString(1L, d.Column(saClobFields2[l]).getClob());
                                        }
                                        if (this.strWebType.equals("2") && this.DataBaseType.equals("1")) {
                                            final Clob clob2 = this.sqlRst.getClob(saClobFields2[l]);
                                            clob2.setString(1L, d.Column(saClobFields2[l]).getClob());
                                        }
                                        if (this.strWebType.equals("2") && this.DataBaseType.equals("2")) {
                                            final Clob clob2 = this.sqlRst.getClob(saClobFields2[l]);
                                            clob2.setString(1L, d.Column(saClobFields2[l]).getClob());
                                        }
                                        if (this.strWebType.equals("2") && this.DataBaseType.equals("3")) {
                                            final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getClob(saClobFields2[l]);
                                            clob3.setString(1L, d.Column(saClobFields2[l]).getClob());
                                        }
                                        if (this.strWebType.equals("3") && this.DataBaseType.equals("1")) {
                                            final CLOB clob = (CLOB)this.sqlRst.getClob(saClobFields2[l]);
                                            clob.putString(1L, d.Column(saClobFields2[l]).getClob());
                                        }
                                        if (this.strWebType.equals("3") && this.DataBaseType.equals("2")) {
                                            final Clob clob2 = this.sqlRst.getClob(saClobFields2[l]);
                                            clob2.setString(1L, d.Column(saClobFields2[l]).getClob());
                                        }
                                        if (this.strWebType.equals("3") && this.DataBaseType.equals("3")) {
                                            final com.mysql.jdbc.Clob clob3 = (com.mysql.jdbc.Clob)this.sqlRst.getClob(saClobFields2[l]);
                                            clob3.setString(1L, d.Column(saClobFields2[l]).getClob());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    System.out.println(this.ObjSysError.DataMessage3 + strSql);
                    ps.execute();
                }
            }
            if (strAfterSql != null && strAfterSql.length > 0) {
                for (int i = 0; i <= strAfterSql.length - 1; ++i) {
                    System.out.println("UpdateAll: " + this.ObjSysError.DataMessage3 + strAfterSql[i]);
                    ps = this.sqlConn.prepareStatement(strAfterSql[i]);
                    ps.execute();
                }
            }
            this.sqlConn.commit();
            bRtn = true;
            dArr = null;
            if (ps != null) {
                try {
                    ps.close();
                }
                catch (SQLException ex) {
                    System.out.println(this.ObjSysError.DataMessage19 + ex.toString());
                    ex.printStackTrace();
                }
            }
            this.freeConnection();
            ps = null;
            this.sqlRst = null;
        }
        catch (Exception ex2) {
            System.out.println("updateAll: " + ex2.toString());
            ex2.printStackTrace();
            bRtn = false;
            ps = null;
            this.sqlRst = null;
            this.freeConnection();
        }
        return bRtn;
    }
    
    private boolean setParamValue(final PreparedStatement ps, final DBRow d) {
        String strValue = null;
        boolean IsBlob = false;
        final String[] strColName = d.getAllColumnName();
        final int iColCount = strColName.length;
        int jjj = 0;
        for (int iColCounter = 0; iColCounter <= iColCount - 1; ++iColCounter) {
            final DBColumn col = d.Column(strColName[iColCounter]);
            if (col != null) {
                if (col.getValue() != null) {
                    ++jjj;
                    final int iType = col.getType().getValue();
                    try {
                        switch (iType) {
                            case 1: {
                                if (DBServiceBean.bConvert) {
                                    try {
                                        strValue = new String(col.getString().getBytes("ISO8859_1"), "GBK");
                                    }
                                    catch (UnsupportedEncodingException ex) {
                                        System.out.println(this.ObjSysError.DataMessage20 + ex.getMessage());
                                    }
                                }
                                else {
                                    strValue = col.getString();
                                }
                                System.out.println("PAR\uff1a" + col.getName() + " = " + col.getString());
                                ps.setString(jjj, strValue);
                                break;
                            }
                            case 2: {
                                ps.setFloat(jjj, col.getFloat());
                                break;
                            }
                            case 3: {
                                IsBlob = true;
                                --jjj;
                                System.out.println("Blob\uff1a" + Integer.toString(jjj));
                                this.aaa = jjj;
                                break;
                            }
                            case 4: {
                                ps.setTimestamp(jjj, this.UtiltdateToSqldate(col.getDate()));
                                break;
                            }
                            case 5: {
                                ps.setTimestamp(jjj, this.UtiltdateToSqldate(col.getDate()));
                                break;
                            }
                            case 6: {
                                ps.setInt(jjj, col.getInteger());
                                break;
                            }
                            case 7: {
                                IsBlob = true;
                                --jjj;
                                System.out.println("Clob\uff1a" + Integer.toString(jjj));
                                break;
                            }
                        }
                    }
                    catch (SQLException ex2) {
                        System.out.println(this.ObjSysError.DataMessage21 + ex2.toString());
                        ex2.printStackTrace();
                    }
                }
            }
        }
        return IsBlob;
    }
    
    public Timestamp UtiltdateToSqldate(final Date date) {
        Timestamp sqlDate = null;
        if (date != null) {
            try {
                sqlDate = new Timestamp(date.getTime());
            }
            catch (Exception ex) {
                System.out.println(this.ObjSysError.DataMessage22 + ex.toString());
                ex.printStackTrace();
            }
        }
        return sqlDate;
    }
    
    private String MakeInsertSql(final DBRow row) {
        final StringBuffer strInsertBuffer = new StringBuffer();
        int iCounter = 0;
        try {
            final String[] strColName = row.getAllColumnName();
            if (strColName.length > 0) {
                strInsertBuffer.append("Insert into " + row.getTableName() + " (");
                int iCount;
                for (iCount = strColName.length, iCounter = 0; iCounter < iCount; ++iCounter) {
                    if (row.Column(strColName[iCounter]).getValue() != null) {
                        strInsertBuffer.append(strColName[iCounter] + ",");
                    }
                }
                strInsertBuffer.deleteCharAt(strInsertBuffer.length() - 1);
                strInsertBuffer.append(") ");
                strInsertBuffer.append(" values (");
                for (iCounter = 0; iCounter < iCount; ++iCounter) {
                    if (row.Column(strColName[iCounter]).getValue() != null) {
                        if (row.Column(strColName[iCounter]).getType().getValue() == DBType.BLOB.getValue()) {
                            if (this.DataBaseType.equals("2") || this.DataBaseType.equals("3")) {
                                strInsertBuffer.append("null,");
                            }
                            else {
                                strInsertBuffer.append("empty_blob(),");
                            }
                        }
                        else if (row.Column(strColName[iCounter]).getType().getValue() == DBType.CLOB.getValue()) {
                            if (this.DataBaseType.equals("2") || this.DataBaseType.equals("3")) {
                                strInsertBuffer.append("null,");
                            }
                            else {
                                strInsertBuffer.append("empty_clob(),");
                            }
                        }
                        else {
                            strInsertBuffer.append("?,");
                        }
                    }
                }
                strInsertBuffer.deleteCharAt(strInsertBuffer.length() - 1);
                strInsertBuffer.append(")");
            }
        }
        catch (Exception ex) {
            System.out.println(this.ObjSysError.DataMessage23 + ex.toString());
            ex.printStackTrace();
        }
        return strInsertBuffer.toString();
    }
    
    private String MakeUpdateSql(final DBRow row) {
        final StringBuffer strUpdateBuffer = new StringBuffer();
        int iCounter=0 ;
        try {
            String[] strColName = row.getAllColumnName();
            if (strColName.length > 0) {
                strUpdateBuffer.append("update " + row.getTableName() + " set ");
                for (final int iCount = strColName.length; iCounter < iCount; ++iCounter) {
                    if (row.Column(strColName[iCounter]).getValue() != null) {
                        if (row.Column(strColName[iCounter]).getType().getValue() == DBType.BLOB.getValue()) {
                            if (this.DataBaseType.equals("2") || this.DataBaseType.equals("3")) {
                                strUpdateBuffer.append(strColName[iCounter] + "=null,");
                            }
                            else {
                                strUpdateBuffer.append(strColName[iCounter] + "=empty_blob(),");
                            }
                        }
                        else if (row.Column(strColName[iCounter]).getType().getValue() == DBType.CLOB.getValue()) {
                            if (this.DataBaseType.equals("2") || this.DataBaseType.equals("3")) {
                                strUpdateBuffer.append(strColName[iCounter] + "=null,");
                            }
                            else {
                                strUpdateBuffer.append(strColName[iCounter] + "=empty_clob(),");
                            }
                        }
                        else {
                            strUpdateBuffer.append(strColName[iCounter] + "=?,");
                        }
                    }
                }
                strUpdateBuffer.deleteCharAt(strUpdateBuffer.length() - 1);
                strColName = row.getPrimaryKeyName();
                if (strColName != null && strColName.length > 0) {
                    strUpdateBuffer.append(" where ");
                    final int iCount = strColName.length;
                    String strValue = null;
                    for (iCounter = 0; iCounter <= iCount - 1; ++iCounter) {
                        switch (row.Column(strColName[iCounter]).getType().getValue()) {
                            case 1: {
                                strUpdateBuffer.append(" " + strColName[iCounter] + "='");
                                if (DBServiceBean.bConvert) {
                                    strValue = new String(row.Column(strColName[iCounter]).getString().getBytes("ISO8859_1"), "GBK");
                                }
                                else {
                                    strValue = row.Column(strColName[iCounter]).getString();
                                }
                                strUpdateBuffer.append(strValue + "' and");
                                break;
                            }
                            case 2: {
                                strUpdateBuffer.append(" " + strColName[iCounter] + "=");
                                strUpdateBuffer.append(Integer.toString(row.Column(strColName[iCounter]).getInteger()) + " and");
                                break;
                            }
                            case 6: {
                                strUpdateBuffer.append(" " + strColName[iCounter] + "=");
                                strUpdateBuffer.append(Integer.toString(row.Column(strColName[iCounter]).getInteger()) + " and");
                                break;
                            }
                        }
                    }
                }
                strUpdateBuffer.replace(strUpdateBuffer.length() - 3, strUpdateBuffer.length(), "");
            }
        }
        catch (Exception ex) {
            System.out.println(this.ObjSysError.DataMessage24 + ex.toString());
            ex.printStackTrace();
        }
        return strUpdateBuffer.toString();
    }
    
    private String MakeMysqlUpdateSql(final DBRow row) {
        final StringBuffer strUpdateBuffer = new StringBuffer();
        int iCounter = 0;
        try {
            String[] strColName = row.getAllColumnName();
            if (strColName.length > 0) {
                strUpdateBuffer.append("update " + row.getTableName() + " set ");
                int iCount;
                for (iCount = strColName.length, iCounter = 0; iCounter < iCount; ++iCounter) {
                    if (row.Column(strColName[iCounter]).getValue() != null) {
                        if (row.Column(strColName[iCounter]).getType().getValue() == DBType.BLOB.getValue()) {
                            strUpdateBuffer.append(strColName[iCounter] + "=?,");
                        }
                    }
                }
                for (iCounter = 0; iCounter < iCount; ++iCounter) {
                    if (row.Column(strColName[iCounter]).getValue() != null) {
                        if (row.Column(strColName[iCounter]).getType().getValue() == DBType.CLOB.getValue()) {
                            strUpdateBuffer.append(strColName[iCounter] + "=?,");
                        }
                    }
                }
                strUpdateBuffer.deleteCharAt(strUpdateBuffer.length() - 1);
                strColName = row.getPrimaryKeyName();
                if (strColName != null && strColName.length > 0) {
                    strUpdateBuffer.append(" where ");
                    iCount = strColName.length;
                    String strValue = null;
                    for (iCounter = 0; iCounter <= iCount - 1; ++iCounter) {
                        switch (row.Column(strColName[iCounter]).getType().getValue()) {
                            case 1: {
                                strUpdateBuffer.append(" " + strColName[iCounter] + "='");
                                if (DBServiceBean.bConvert) {
                                    strValue = new String(row.Column(strColName[iCounter]).getString().getBytes("ISO8859_1"), "GBK");
                                }
                                else {
                                    strValue = row.Column(strColName[iCounter]).getString();
                                }
                                strUpdateBuffer.append(strValue + "' and");
                                break;
                            }
                            case 2: {
                                strUpdateBuffer.append(" " + strColName[iCounter] + "=");
                                strUpdateBuffer.append(Integer.toString(row.Column(strColName[iCounter]).getInteger()) + " and");
                                break;
                            }
                            case 6: {
                                strUpdateBuffer.append(" " + strColName[iCounter] + "=");
                                strUpdateBuffer.append(Integer.toString(row.Column(strColName[iCounter]).getInteger()) + " and");
                                break;
                            }
                        }
                    }
                }
                strUpdateBuffer.replace(strUpdateBuffer.length() - 3, strUpdateBuffer.length(), "");
            }
        }
        catch (Exception ex) {
            System.out.println(this.ObjSysError.DataMessage24 + ex.toString());
            ex.printStackTrace();
        }
        return strUpdateBuffer.toString();
    }
    
    private int convertType(String strType) {
        int iRtn = 0;
        final int iComp = -1;
        final String strStr = "VARCHAR2STRINGCHAR";
        final String strNum = "NUMBERFLOATDOUBLEDECIMALNUMERICREAL";
        final String strDate = "DATEJAVA.UTIL.DATE";
        final String strDateTime = "DATETIMETIMESTAMP";
        final String strInt = "LONGINTTINYSHORTLONGLONGBIGINTINTEGERTINYINTSMALLINTMEDIUMINT";
        final String strBlob = "BLOBLONGBLOBTINYBLOBMEDIUMBLOBTEXTBYTE[]IMAGE";
        final String strClob = "CLOBLONGTEXTTINYTEXTMEDIUMTEXT";
        if (strStr == null || strType.trim() == "") {
            return 1;
        }
        strType = strType.toUpperCase();
        if (strStr.indexOf(strType) > iComp) {
            iRtn = 1;
        }
        else if (strNum.indexOf(strType) > iComp) {
            iRtn = 2;
        }
        else if (strInt.indexOf(strType) > iComp) {
            iRtn = 6;
        }
        else if (strDate.indexOf(strType) > iComp) {
            iRtn = 4;
        }
        else if (strDateTime.indexOf(strType) > iComp) {
            iRtn = 5;
        }
        else if (strBlob.indexOf(strType) > iComp) {
            iRtn = 3;
        }
        else if (strClob.indexOf(strType) > iComp) {
            iRtn = 7;
        }
        return iRtn;
    }
    
    private String GetMysql_sql(String sql) {
        String reSql = "";
        if (sql.length() > 0) {
            if (sql.toUpperCase().indexOf("DELETE") != -1 && sql.toUpperCase().indexOf("DELETE FROM") == -1 && sql.toUpperCase().indexOf("DELETE  FROM") == -1 && sql.toUpperCase().indexOf("DELETE   FROM") == -1) {
                sql = sql.replaceAll("DELETE", "DELETE FROM ");
                sql = sql.replaceAll("delete", "DELETE FROM ");
                sql = sql.replaceAll("Delete", "DELETE FROM ");
            }
            if (sql.toUpperCase().indexOf("TRUNC") != -1) {
                sql = sql.replaceAll("TRUNC", "TO_DAYS");
                sql = sql.replaceAll("trunc", "TO_DAYS");
            }
            sql = sql.replaceAll("sysdate", "now()");
            sql = sql.replaceAll("SYSDATE", "now()");
            sql = sql.replaceAll("\\*1440", "\\/60");
            if (sql.lastIndexOf("RN >") > -1 || sql.lastIndexOf("RN>") > -1 || sql.lastIndexOf("rn >") > -1 || sql.lastIndexOf("rn>") > -1) {
                String sentence = sql;
                String character = "(";
                int index = 0;
                int index2 = 0;
                int index3 = 0;
                int x = 0;
                int startnum = -1;
                int endnum = -1;
                int chr = 0;
                while (index < sentence.length() - character.length()) {
                    final String newCharacter = sentence.substring(index, index + character.length());
                    if (newCharacter.equalsIgnoreCase(character) && ++x == 2) {
                        index2 = index;
                        break;
                    }
                    ++index;
                }
                character = ")";
                index = 0;
                x = 0;
                while (index < sentence.length() - character.length()) {
                    final String newCharacter = sentence.substring(index, index + character.length());
                    if (newCharacter.equalsIgnoreCase(character)) {
                        index3 = x;
                        x = index;
                    }
                    ++index;
                }
                sentence = sql.substring(index3 + 1) + "--";
                sql = sql.substring(index2 + 1, index3);
                index = 0;
                x = 0;
                character = "";
                while (index < sentence.length() - 1) {
                    chr = sentence.substring(index, index + 1).charAt(0);
                    if (chr >= 48 && chr <= 57) {
                        character += sentence.substring(index, index + 1);
                    }
                    else if (character.length() > 0) {
                        if (startnum == -1) {
                            startnum = Integer.parseInt(character);
                        }
                        else {
                            endnum = Integer.parseInt(character);
                        }
                        character = "";
                    }
                    ++index;
                }
                if (startnum > endnum) {
                    x = startnum;
                    startnum = endnum;
                    endnum = x;
                }
                if (startnum != -1 && endnum != -1) {
                    sql = sql + " LIMIT " + String.valueOf(startnum) + "," + String.valueOf(endnum - startnum) + "";
                }
            }
            sql = sql.replaceAll("to_date", "str_to_date");
            sql = sql.replaceAll("TO_DATE", "str_to_date");
            sql = sql.replaceAll("to_char", "str_to_date");
            sql = sql.replaceAll("TO_CHAR", "str_to_date");
            sql = sql.replaceAll("str_str_to_date", "str_to_date");
            if (sql.toUpperCase().indexOf("SELECT") != -1) {
                sql = sql.replaceAll("yyyy-mm-dd", "%Y-%m-%d");
                sql = sql.replaceAll("YYYY-MM-DD", "%Y-%m-%d");
                sql = sql.replaceAll("hh24:mi:ss", "%H:%i:%s");
                sql = sql.replaceAll("HH24:MI:SS", "%%H:%i:%s");
            }
            if (sql.lastIndexOf("and rownum<20") > -1) {
                sql = sql.replaceAll("and rownum<20", " ");
                sql += "  LIMIT 20 ";
            }
            if (sql.lastIndexOf("and rownum<30") > -1) {
                sql = sql.replaceAll("and rownum<30", " ");
                sql += "  LIMIT 30 ";
            }
        }
        reSql = sql;
        return reSql;
    }
    
    private String GetMssql_sql(String sql) {
        String reSql = "";
        String strorder = "";
        String strTmp = "";
        int intorder = 0;
        if (sql.length() > 0) {
            if (sql.toUpperCase().indexOf("SUBSTR") != -1) {
                sql = sql.replaceAll("substr\\(", "substring(");
                sql = sql.replaceAll("SUBSTR\\(", "substring(");
            }
            sql = sql.replaceAll("sysdate", "getdate()");
            sql = sql.replaceAll("SYSDATE", "getdate()");
            sql = sql.replaceAll("length\\(", "len(");
            sql = sql.replaceAll("LENGTH\\(", "len(");
            sql = sql.replaceAll("nvl\\(", "isnull(");
            sql = sql.replaceAll("NVL\\(", "isnull(");
            sql = sql.replaceAll("\\|\\|", "+");
            if (sql.lastIndexOf("RN >") > -1 || sql.lastIndexOf("RN>") > -1 || sql.lastIndexOf("rn >") > -1 || sql.lastIndexOf("rn>") > -1) {
                String sentence = sql;
                String character = "(";
                int index = 0;
                int index2 = 0;
                int index3 = 0;
                int x = 0;
                int startnum = -1;
                int endnum = -1;
                int chr = 0;
                while (index < sentence.length() - character.length()) {
                    final String newCharacter = sentence.substring(index, index + character.length());
                    if (newCharacter.equalsIgnoreCase(character) && ++x == 2) {
                        index2 = index;
                        break;
                    }
                    ++index;
                }
                character = ")";
                index = 0;
                x = 0;
                while (index < sentence.length() - character.length()) {
                    final String newCharacter = sentence.substring(index, index + character.length());
                    if (newCharacter.equalsIgnoreCase(character)) {
                        index3 = x;
                        x = index;
                    }
                    ++index;
                }
                sentence = sql.substring(index3 + 1) + "--";
                sql = sql.substring(index2 + 1, index3);
                index = 0;
                x = 0;
                character = "";
                while (index < sentence.length() - 1) {
                    chr = sentence.substring(index, index + 1).charAt(0);
                    if (chr >= 48 && chr <= 57) {
                        character += sentence.substring(index, index + 1);
                    }
                    else if (character.length() > 0) {
                        if (startnum == -1) {
                            startnum = Integer.parseInt(character);
                        }
                        else {
                            endnum = Integer.parseInt(character);
                        }
                        character = "";
                    }
                    ++index;
                }
                if (startnum > endnum) {
                    x = startnum;
                    startnum = endnum;
                    endnum = x;
                }
                strorder = "";
                if (sql.toUpperCase().indexOf("ORDER") != -1) {
                    intorder = sql.toUpperCase().indexOf("ORDER");
                    strorder = sql.substring(intorder);
                    sql = sql.substring(0, intorder);
                    if (sql.trim().substring(6).toUpperCase().indexOf("DISTINCT") != -1) {
                        strTmp = sql.trim().substring(6);
                        strTmp = strTmp.replaceAll("Distinct", "");
                        strTmp = strTmp.replaceAll("distinct", "");
                        strTmp = strTmp.replaceAll("DISTINCT", "");
                        sql = "select * from (select Distinct ROW_NUMBER() Over(" + strorder + ") as rownum," + strTmp + ") as querytable where rownum>" + String.valueOf(startnum) + " and rownum<=" + String.valueOf(endnum);
                    }
                    else {
                        sql = "select * from (select ROW_NUMBER() Over(" + strorder + ") as rownum," + sql.trim().substring(6) + ") as querytable where rownum>" + String.valueOf(startnum) + " and rownum<=" + String.valueOf(endnum);
                    }
                }
                else {
                    sql = sql.replaceAll("select", "select top 100 ");
                    sql = sql.replaceAll("Select", "select top 100 ");
                    sql = sql.replaceAll("SELECT", "select top 100 ");
                    sql = sql.replaceAll("select top 100  distinct", "select top 100 ");
                    sql = sql.replaceAll("select top 100  Distinct", "select top 100 ");
                    sql = sql.replaceAll("select top 100  DISTINCT", "select top 100 ");
                }
            }
        }
        reSql = sql;
        return reSql;
    }
    
    public int getdatalinknum() {
        final int gnum = DBServiceBean.DataSourceList.size();
        return gnum;
    }
    
    static {
        DBServiceBean.clients = 0;
        DBServiceBean.yDateTime = "";
    }
}
