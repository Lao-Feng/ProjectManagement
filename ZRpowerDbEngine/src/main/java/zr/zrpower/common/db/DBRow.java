// 
// Decompiled by Procyon v0.5.36
// 

package zr.zrpower.common.db;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DBRow implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Map<String, Object> columnList;
    protected Map<String, Object> primaryCol;
    protected DBMode mDataMode;
    protected String mTableName;
    public boolean isBaseData;
    
    public DBRow() {
        this.mDataMode = DBMode.SOURCE;
        this.isBaseData = false;
        this.columnList = new HashMap<String, Object>();
        this.primaryCol = new HashMap<String, Object>();
    }
    
    public void addColumn(final String ColumnName, final Object Value, final DBType ColumnType) {
        if (ColumnName.equals(null)) {
            return;
        }
        final DBColumn column = new DBColumn(ColumnName, Value, ColumnType);
        this.columnList.put(ColumnName.toUpperCase(), column);
    }
    
    public void addColumn(final String ColumnName, final Object Value) {
        if (ColumnName.equals(null) || ColumnName.equals("")) {
            return;
        }
        final DBColumn column = new DBColumn(ColumnName, Value, DBType.STRING);
        this.columnList.put(ColumnName.toUpperCase(), column);
    }
    
    public void addColumn(final DBColumn column) {
        if (column == null) {
            return;
        }
        this.columnList.put(column.getName().toUpperCase(), column);
    }
    
    public void clear() {
        this.columnList.clear();
    }
    
    public int getColumnCount() {
        return this.columnList.size();
    }
    
    public DBColumn Column(final String ColumnName) {
        return (DBColumn) this.columnList.get(ColumnName.toUpperCase());
    }
    
    public String[] getAllColumnName() {
        final int length = this.columnList.size();
        if (length == 0) {
            return null;
        }
        final String[] result = new String[length];
        final Set<String> columnNames = this.columnList.keySet();
        int j = 0;
        for (final String column : columnNames) {
            result[j] = column;
            ++j;
        }
        return result;
    }
    
    public DBMode getDataMode() {
        return this.mDataMode;
    }
    
    public void setDataMode(final DBMode DataMode) {
        this.mDataMode = DataMode;
    }
    
    public String getTableName() {
        return this.mTableName;
    }
    
    public void setTableName(final String TableName) {
        this.mTableName = TableName;
    }
    
    public boolean setPrimaryKey(final String strPriColName) {
        boolean result = false;
        if (this.primaryCol.get(strPriColName) == null && this.columnList.get(strPriColName.toUpperCase()) != null) {
            this.primaryCol.put(strPriColName, strPriColName);
            result = true;
        }
        return result;
    }
    
    public int getPrimaryKeyCount() {
        return this.primaryCol.size();
    }
    
    public String[] getPrimaryKeyName() {
        final int length = this.primaryCol.size();
        if (length == 0) {
            return null;
        }
        final String[] result = new String[length];
        final Set<String> pryColumns = this.primaryCol.keySet();
        int i = 0;
        for (final String pryColumn : pryColumns) {
            result[i] = pryColumn;
            ++i;
        }
        return result;
    }
}
