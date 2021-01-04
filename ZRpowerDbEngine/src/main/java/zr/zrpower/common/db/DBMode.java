// 
// Decompiled by Procyon v0.5.36
// 

package zr.zrpower.common.db;

import java.io.Serializable;

public final class DBMode implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int value;
    public static final DBMode SOURCE;
    public static final DBMode EDITED;
    public static final DBMode NEW;
    public static final DBMode DELETE;
    
    protected DBMode(final int iValue) {
        this.value = iValue;
    }
    
    public int getValue() {
        return this.value;
    }
    
    static {
        SOURCE = new DBMode(0);
        EDITED = new DBMode(1);
        NEW = new DBMode(2);
        DELETE = new DBMode(3);
    }
}
