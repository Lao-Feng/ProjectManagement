// 
// Decompiled by Procyon v0.5.36
// 

package zr.zrpower.common.db;

import com.yonglilian.common.util.SysError;

import java.util.*;

public class DBEngine
{
    public SysError ObjSysError;
    private static int clients;
    private static Timer timerListener;
    static Map<String, Object> AutoHashMap;
    static Map<String, Object> isAutoHashMap;
    private DBServiceBean dbServiceObject;
    private String m_dsJNID;
    private boolean m_bConvert;
    static String ERROR_REMOTEEXEPTION;
    
    public DBEngine(final String dsJNID, final boolean Convert) {
        this.ObjSysError = new SysError();
        this.dbServiceObject = null;
        DBEngine.ERROR_REMOTEEXEPTION = "\u8c03\u7528\u6570\u636e\u5e93\u8fde\u63a5\u5931\u6548\uff0c\u91cd\u65b0\u8fde\u63a5\u3002";
        this.m_dsJNID = dsJNID;
        this.m_bConvert = Convert;
        if (DBEngine.clients < 1) {
            ++DBEngine.clients;
            final int intnum = 3600;
            (DBEngine.timerListener = new Timer()).schedule(new ScanTask(), 0L, intnum * 1000);
        }
    }
    
    public boolean initialize() {
        try {
            this.dbServiceObject = new DBServiceBean();
        }
        catch (Exception ex1) {
            this.dbServiceObject = null;
            System.out.println("\u521b\u5efa\u6570\u636e\u5e93\u5bf9\u8c61\u65f6\u8c03\u7528\u5f02\u5e38: " + ex1.toString());
            ex1.printStackTrace();
        }
        boolean result = false;
        try {
            result = this.dbServiceObject.init(this.m_dsJNID, this.m_bConvert);
        }
        catch (Exception ex2) {
            this.dbServiceObject = null;
            System.out.println("\u521d\u59cb\u5316\u6570\u636e\u5e93\u8bbf\u95ee\u65f6\u51fa\u9519\uff1a" + DBEngine.ERROR_REMOTEEXEPTION);
            ex2.printStackTrace();
            result = this.dbServiceObject.all_init(this.m_dsJNID, this.m_bConvert);
        }
        return result;
    }
    
    public DBSet QuerySQL(String strSQL, final String TableName) {
        DBSet result = null;
        if (strSQL == null) {
            strSQL = "";
        }
        if (strSQL.length() == 0) {
            return result;
        }
        try {
            this.initialize();
            result = this.dbServiceObject.QuerySQL(this.m_dsJNID, strSQL, TableName);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("QuerySQL: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public DBSet QuerySQL(String strSQL) {
        DBSet result = null;
        if (strSQL == null) {
            strSQL = "";
        }
        if (strSQL.length() == 0) {
            return result;
        }
        try {
            this.initialize();
            result = this.dbServiceObject.QuerySQL(this.m_dsJNID, strSQL);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("QuerySQL: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public DBSet QuerySQL(String strSQL, final String[] value) {
        DBSet result = null;
        if (strSQL == null) {
            strSQL = "";
        }
        if (strSQL.length() == 0) {
            return result;
        }
        try {
            this.initialize();
            result = this.dbServiceObject.QuerySQL(this.m_dsJNID, strSQL, value);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("QuerySQL: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public DBSet QueryTable(final String TableName) {
        DBSet result = null;
        try {
            this.initialize();
            result = this.dbServiceObject.QueryTable(this.m_dsJNID, TableName);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("QueryTable: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public DBSet QueryColumns(final String TableName, final String[] ColumnName) {
        DBSet result = null;
        try {
            this.initialize();
            result = this.dbServiceObject.QueryColumns(this.m_dsJNID, TableName, ColumnName);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("QueryColumns: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public DBSet QueryCondition(final String TableName, final String[] ColumnName, final String Condition) {
        DBSet result = null;
        try {
            this.initialize();
            result = this.dbServiceObject.QueryCondition(this.m_dsJNID, TableName, ColumnName, Condition);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("QueryCondition: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public boolean ExecuteSQL(String SQL) {
        boolean result = false;
        if (SQL == null) {
            SQL = "";
        }
        if (SQL.length() == 0) {
            return result;
        }
        try {
            this.initialize();
            result = this.dbServiceObject.ExecuteSQL(this.m_dsJNID, SQL);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("ExecuteSQL: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public DBSet QuerySQL(String strSQL, final String name, final String id) {
        DBSet result = null;
        boolean is = true;
        if (strSQL == null) {
            strSQL = "";
        }
        if (strSQL.length() == 0) {
            return result;
        }
        final String isyes = (String) DBEngine.isAutoHashMap.get(name + id);
        is = (isyes == null);
        if (is) {
            try {
                this.initialize();
                result = this.dbServiceObject.QuerySQL(this.m_dsJNID, strSQL);
            }
            catch (Exception ex) {
                this.dbServiceObject = null;
                System.out.println("QuerySQL: " + DBEngine.ERROR_REMOTEEXEPTION);
                ex.printStackTrace();
                this.initialize();
            }
            DBEngine.isAutoHashMap.put(name + id, "1");
            if (result != null) {
                DBEngine.AutoHashMap.put(name + id, result);
            }
        }
        else {
            result = (DBSet) DBEngine.AutoHashMap.get(name + id);
        }
        return result;
    }
    
    public boolean ExecuteSQL(String SQL, final String[] value) {
        boolean result = false;
        if (SQL == null) {
            SQL = "";
        }
        if (SQL.length() == 0) {
            return result;
        }
        try {
            this.initialize();
            result = this.dbServiceObject.ExecuteSQL(this.m_dsJNID, SQL, value);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("ExecuteSQL: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public boolean ExecuteSQLs(final String[] SQLs) {
        boolean result = false;
        try {
            this.initialize();
            result = this.dbServiceObject.ExecuteSQLs(this.m_dsJNID, SQLs);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("ExecuteSQLs: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public boolean ExecuteSQLs(final String[] SQLs, final List<Object> list) {
        boolean result = false;
        try {
            this.initialize();
            result = this.dbServiceObject.ExecuteSQLs(this.m_dsJNID, SQLs, list);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("ExecuteSQLs: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public boolean ExecuteSQLs(final String SQLs, final String[][] list) {
        boolean result = false;
        try {
            this.initialize();
            result = this.dbServiceObject.ExecuteSQLs(this.m_dsJNID, SQLs, list);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("ExecuteSQLs: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public boolean ExecuteInsert(final DBRow dbrow) {
        boolean result = false;
        try {
            this.initialize();
            result = this.dbServiceObject.ExecuteInsert(this.m_dsJNID, dbrow);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("ExecuteInsert: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public boolean ExecuteEdit(final DBRow dbrow) {
        boolean result = false;
        try {
            this.initialize();
            result = this.dbServiceObject.ExecuteEdit(this.m_dsJNID, dbrow);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("ExecuteEdit: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public boolean ExecuteEdit(final DBRow dbrow, final String Condition) {
        boolean result = false;
        try {
            this.initialize();
            result = this.dbServiceObject.ExecuteEdit(this.m_dsJNID, dbrow, Condition);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("ExecuteEdit: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public boolean ExecuteDelete(final String TableName, final String Condition) {
        boolean result = false;
        try {
            this.initialize();
            result = this.dbServiceObject.ExecuteDelete(this.m_dsJNID, TableName, Condition);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("ExecuteDelete: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public boolean UpdateAll(final DBRow[] dArr, final String[] strBeforeSql, final String[] strAfterSql) {
        boolean result = false;
        try {
            this.initialize();
            result = this.dbServiceObject.UpdateAll(this.m_dsJNID, dArr, strBeforeSql, strAfterSql);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("UpdateAll: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public boolean UpdateAll(final DBRow[] dArr) {
        boolean result = false;
        try {
            this.initialize();
            result = this.dbServiceObject.UpdateAll(this.m_dsJNID, dArr, null, null);
        }
        catch (Exception ex) {
            this.dbServiceObject = null;
            System.out.println("UpdateAll: " + DBEngine.ERROR_REMOTEEXEPTION);
            ex.printStackTrace();
            this.initialize();
        }
        return result;
    }
    
    public void inithashtable() {
        DBEngine.AutoHashMap.clear();
        DBEngine.isAutoHashMap.clear();
    }
    
    private void Memory_Manage() {
        System.out.println("\u5185\u5b58\u6e05\u7406\u5f00\u59cb\u3002");
        DBEngine.AutoHashMap.clear();
        DBEngine.isAutoHashMap.clear();
        System.out.println("\u5185\u5b58\u6e05\u7406\u6e05\u7ed3\u675f\u3002");
    }
    
    static {
        DBEngine.clients = 0;
        DBEngine.AutoHashMap = new HashMap<String, Object>();
        DBEngine.isAutoHashMap = new HashMap<String, Object>();
    }
    
    class ScanTask extends TimerTask
    {
        @Override
        public void run() {
            System.out.println("\u626b\u63cf\u4efb\u52a1\u5f00\u59cb\uff01");
            DBEngine.this.Memory_Manage();
        }
    }
}
