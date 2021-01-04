// 
// Decompiled by Procyon v0.5.36
// 

package zr.zrpower.common.db;

import java.io.Serializable;

public final class DBType implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int value;
    public static final DBType STRING;
    public static final DBType FLOAT;
    public static final DBType BLOB;
    public static final DBType DATE;
    public static final DBType DATETIME;
    public static final DBType LONG;
    public static final DBType CLOB;
    
    protected DBType(final int iValue) {
        this.value = iValue;
    }
    
    public int getValue() {
        return this.value;
    }
    
    static {
        STRING = new DBType(1);
        FLOAT = new DBType(2);
        BLOB = new DBType(3);
        DATE = new DBType(4);
        DATETIME = new DBType(5);
        LONG = new DBType(6);
        CLOB = new DBType(7);
    }
}
