// 
// Decompiled by Procyon v0.5.36
// 

package zr.zrpower.common.db;

import java.io.Serializable;
import java.util.Date;

public class DBColumn implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Object mValue;
    protected String mName;
    protected DBType mType;
    protected DBTag mTag;
    protected boolean mNull;
    protected int mLength;
    protected String mCodeTable;
    protected String mCValue;
    protected String mChineseName;
    protected int mTagExt;
    protected String mAuto1;
    protected String mAuto2;
    protected String mAuto3;
    
    public DBColumn(final String Name) {
        this.mType = DBType.STRING;
        this.mTag = DBTag.NONE;
        this.mNull = true;
        this.mTagExt = 0;
        this.mName = Name.toUpperCase();
    }
    
    public DBColumn(final String Name, final Object setValue) {
        this.mType = DBType.STRING;
        this.mTag = DBTag.NONE;
        this.mNull = true;
        this.mTagExt = 0;
        this.mValue = setValue;
        this.mName = Name.toUpperCase();
    }
    
    public DBColumn(final String Name, final Object setValue, final DBType Type) {
        this.mType = DBType.STRING;
        this.mTag = DBTag.NONE;
        this.mNull = true;
        this.mTagExt = 0;
        this.mValue = setValue;
        this.mName = Name.toUpperCase();
        this.mType = Type;
    }
    
    public void setName(final String mName) {
        this.mName = mName.toUpperCase();
    }
    
    public void setValue(final Object mValue) {
        this.mValue = mValue;
    }
    
    public void setLength(final int mValue) {
        this.mLength = mValue;
    }
    
    public void setTag(final DBTag Tag) {
        this.mTag = Tag;
    }
    
    public void setCodeTable(final String strTableName) {
        this.mCodeTable = strTableName;
        this.mTag = DBTag.CODE;
    }
    
    public void setCValue(final String strValue) {
        this.mCValue = strValue;
    }
    
    public void setType(final DBType mType) {
        this.mType = mType;
    }
    
    public void setNULL(final boolean isNULL) {
        this.mNull = isNULL;
    }
    
    public void setChineseName(final String ChineseName) {
        this.mChineseName = ChineseName;
    }
    
    public DBTag getTag() {
        return this.mTag;
    }
    
    public String getCodeTable() {
        return this.mCodeTable;
    }
    
    public String getCValue() {
        return this.mCValue;
    }
    
    public String getChineseName() {
        return this.mChineseName;
    }
    
    public String getName() {
        return this.mName;
    }
    
    public Object getValue() {
        return this.mValue;
    }
    
    public int getLength() {
        return this.mLength;
    }
    
    public DBType getType() {
        return this.mType;
    }
    
    public boolean isNull() {
        return this.mNull;
    }
    
    public String getString() {
        try {
            if (this.mValue == null) {
                return "";
            }
            return (String)this.mValue;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
    
    public int getInteger() {
        try {
            if (this.mValue == null) {
                return 0;
            }
            final int retInt = (int)Float.parseFloat(this.mValue.toString());
            return retInt;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    public float getFloat() {
        try {
            if (this.mValue == null) {
                return 0.0f;
            }
            return Float.parseFloat(this.mValue.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return 0.0f;
        }
    }
    
    public double getDouble() {
        try {
            if (this.mValue == null) {
                return 0.0;
            }
            return Double.parseDouble(this.mValue.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }
    
    public Date getDate() {
        try {
            if (this.mValue == null) {
                return null;
            }
            return (Date)this.mValue;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public byte[] getBlob() {
        if (this.mValue != null) {
            return (byte[])this.mValue;
        }
        return null;
    }
    
    public String getClob() {
        if (this.mValue != null) {
            return (String)this.mValue;
        }
        return null;
    }
    
    public int getTagExt() {
        return this.mTagExt;
    }
    
    public void setTagExt(final int tagext) {
        this.mTagExt = tagext;
    }
    
    public String getAuto1() {
        return this.mAuto1;
    }
    
    public void setAuto1(final String auto) {
        this.mAuto1 = auto;
    }
    
    public String getAuto2() {
        return this.mAuto2;
    }
    
    public void setAuto2(final String auto) {
        this.mAuto2 = auto;
    }
    
    public String getAuto3() {
        return this.mAuto3;
    }
    
    public void setAuto3(final String auto) {
        this.mAuto3 = auto;
    }
}
