// 
// Decompiled by Procyon v0.5.36
// 

package zr.zrpower.common.db;

import java.io.Serializable;
import java.util.Vector;

public class DBSet implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Vector<DBRow> RowList;
    protected String dbSetName;
    
    public DBSet() {
        this.RowList = new Vector<DBRow>();
    }
    
    public DBSet(final String TableName) {
        this.RowList = new Vector<DBRow>();
        this.dbSetName = TableName;
    }
    
    public String getTableName() {
        return this.dbSetName;
    }
    
    public void SetTableName(final String name) {
        this.dbSetName = name;
    }
    
    public void AddRow(final DBRow row) {
        this.RowList.add(row);
    }
    
    public void AddRows(final DBRow[] rows) {
        if (rows.length > 0) {
            for (int i = 0; i < rows.length - 1; ++i) {
                this.RowList.add(rows[i]);
            }
        }
    }
    
    public void DeleteRow(final int index) {
        this.RowList.remove(index);
    }
    
    public void DeleteRow(final DBRow row) {
        this.RowList.remove(row);
    }
    
    public DBRow Row(final int index) {
        return this.RowList.get(index);
    }
    
    public int RowCount() {
        return this.RowList.size();
    }
}
