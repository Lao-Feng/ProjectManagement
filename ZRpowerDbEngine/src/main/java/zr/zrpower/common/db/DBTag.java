// 
// Decompiled by Procyon v0.5.36
// 

package zr.zrpower.common.db;

import java.io.Serializable;

public final class DBTag implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int value;
    public static final DBTag NONE;
    public static final DBTag CODE;
    public static final DBTag USER;
    public static final DBTag UNIT;
    public static final DBTag COMMUMNITY;
    public static final DBTag CASE_NUMBER;
    public static final DBTag ID_INT;
    public static final DBTag ID_INT_1;
    public static final DBTag ID_INT_2;
    public static final DBTag ID_INT_3;
    public static final DBTag ID_STR;
    public static final DBTag ID_STR_1;
    public static final DBTag ID_STR_2;
    public static final DBTag ID_STR_3;
    
    protected DBTag(final int iValue) {
        this.value = iValue;
    }
    
    public int getValue() {
        return this.value;
    }
    
    static {
        NONE = new DBTag(1);
        CODE = new DBTag(2);
        USER = new DBTag(3);
        UNIT = new DBTag(4);
        COMMUMNITY = new DBTag(14);
        CASE_NUMBER = new DBTag(5);
        ID_INT = new DBTag(6);
        ID_INT_1 = new DBTag(7);
        ID_INT_2 = new DBTag(8);
        ID_INT_3 = new DBTag(9);
        ID_STR = new DBTag(10);
        ID_STR_1 = new DBTag(11);
        ID_STR_2 = new DBTag(12);
        ID_STR_3 = new DBTag(13);
    }
}
