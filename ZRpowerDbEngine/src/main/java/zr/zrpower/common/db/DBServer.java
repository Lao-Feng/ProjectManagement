// 
// Decompiled by Procyon v0.5.36
// 

package zr.zrpower.common.db;

import com.yonglilian.analyseengine.mode.ANALYSE_STATISTICS_MAIN;
import com.yonglilian.collectionengine.CollectionInfo;
import com.yonglilian.common.util.DateWork;
import com.yonglilian.common.util.FunctionMessage;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.flowengine.mode.config.FLOW_CONFIG_ACTIVITY;
import com.yonglilian.queryengine.mode.QUERY_CONFIG_TABLE;
import com.yonglilian.service.impl.CollectionServiceImpl;
import com.yonglilian.service.impl.FlowControlServiceImpl;
import com.yonglilian.service.impl.StatisticsControlServiceImpl;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

public class DBServer
{
    private DBEngine dbengine;
    private static int clients;
    String DataBaseType;
    
    public DBServer() {
        this.DataBaseType = SysPreperty.getProperty().DataBaseType;
        (this.dbengine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert)).initialize();
        ++DBServer.clients;
        if (DBServer.clients > 100000) {
            DBServer.clients = 1;
        }
    }
    
    public void doflow() {
        String strSQL = "";
        String ID = "";
        String CREATEPSN = "";
        String CDATE = "";
        String DCODE = "";
        final boolean isflow = true;
        DBSet dbset1 = null;
        try {
            if (isflow) {
                strSQL = "Select ID,CREATEPSN,CREATEDATE From FLOW_CONFIG_PROCESS";
                dbset1 = this.dbengine.QuerySQL(strSQL);
                if (dbset1 != null && dbset1.RowCount() > 0) {
                    for (int i = 0; i < dbset1.RowCount(); ++i) {
                        ID = dbset1.Row(i).Column("ID").getString();
                        CREATEPSN = dbset1.Row(i).Column("CREATEPSN").getString();
                        CDATE = DateWork.DateTimeToString(dbset1.Row(i).Column("CREATEDATE").getDate());
                        if (CDATE.length() > 10) {
                            CDATE = CDATE.substring(0, 10);
                        }
                        DCODE = "NFZR" + ID + CREATEPSN + CDATE;
                        DCODE = this.MD5(DCODE);
                        strSQL = "update FLOW_CONFIG_PROCESS set DCODE='" + DCODE + "' where ID='" + ID + "'";
                        this.dbengine.ExecuteSQL(strSQL);
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        dbset1 = null;
    }
    
    public void doform() {
        String strSQL = "";
        String ID = "";
        String MAINTABLE = "";
        String DCODE = "";
        final boolean isform = true;
        DBSet dbset1 = null;
        try {
            if (isform) {
                strSQL = "Select ID,MAINTABLE From COLL_DOC_CONFIG";
                dbset1 = this.dbengine.QuerySQL(strSQL);
                if (dbset1 != null && dbset1.RowCount() > 0) {
                    for (int i = 0; i < dbset1.RowCount(); ++i) {
                        ID = dbset1.Row(i).Column("ID").getString();
                        MAINTABLE = dbset1.Row(i).Column("MAINTABLE").getString();
                        DCODE = "NFZR" + ID + MAINTABLE;
                        DCODE = this.MD5(DCODE);
                        strSQL = "update COLL_DOC_CONFIG set DCODE='" + DCODE + "' where ID='" + ID + "'";
                        this.dbengine.ExecuteSQL(strSQL);
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        dbset1 = null;
    }
    
    public void doquery() {
        String strSQL = "";
        String ID = "";
        String MAINTABLE = "";
        String DCODE = "";
        final boolean isquery = true;
        DBSet dbset1 = null;
        try {
            if (isquery) {
                strSQL = "Select ID,MAINTABLE From QUERY_CONFIG_TABLE";
                dbset1 = this.dbengine.QuerySQL(strSQL);
                if (dbset1 != null && dbset1.RowCount() > 0) {
                    for (int i = 0; i < dbset1.RowCount(); ++i) {
                        ID = dbset1.Row(i).Column("ID").getString();
                        MAINTABLE = dbset1.Row(i).Column("MAINTABLE").getString();
                        DCODE = "NFZR" + ID + MAINTABLE;
                        DCODE = this.MD5(DCODE);
                        strSQL = "update QUERY_CONFIG_TABLE set DCODE='" + DCODE + "' where ID='" + ID + "'";
                        this.dbengine.ExecuteSQL(strSQL);
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        dbset1 = null;
    }
    
    public void doAnalyse() {
        String strSQL = "";
        String ID = "";
        String DCODE = "";
        final boolean isquery = true;
        DBSet dbset = null;
        try {
            if (isquery) {
                strSQL = "select ID from ANALYSE_STATISTICS_MAIN";
                dbset = this.dbengine.QuerySQL(strSQL);
                if (dbset != null && dbset.RowCount() > 0) {
                    for (int i = 0; i < dbset.RowCount(); ++i) {
                        ID = dbset.Row(i).Column("ID").getString();
                        DCODE = "NFZR" + ID;
                        DCODE = this.MD5(DCODE);
                        strSQL = "update ANALYSE_STATISTICS_MAIN set DCODE='" + DCODE + "' where ID='" + ID + "'";
                        this.dbengine.ExecuteSQL(strSQL);
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        dbset = null;
    }
    
    private String MD5(final String s) {
        final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            final byte[] strTemp = s.getBytes();
            final MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            final byte[] md = mdTemp.digest();
            final int j = md.length;
            final char[] str = new char[j * 2];
            int k = 0;
            for (final byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xF];
                str[k++] = hexDigits[byte0 & 0xF];
            }
            String revalue = new String(str);
            revalue = "nx" + revalue.substring(2);
            return revalue;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public boolean isDog() {
        return true;
    }
    
    public void isTable(final String strDtable) {
        String strsql = "";
        String strFieldName = "";
        String strFieldNames = "";
        String ID = "";
        String DTABLE = "";
        DBSet dbset = null;
        boolean isedit = false;
        try {
            if (this.DataBaseType.equals("1")) {
                strsql = "select COLUMN_NAME from USER_TAB_COLUMNS where TABLE_NAME = '" + strDtable + "'";
            }
            else if (this.DataBaseType.equals("2")) {
                strsql = "select name from syscolumns where id=object_id('" + strDtable + "')";
            }
            if (this.DataBaseType.equals("1") || this.DataBaseType.equals("2")) {
                dbset = this.dbengine.QuerySQL(strsql);
                if (dbset != null && dbset.RowCount() > 0) {
                    for (int i = 0; i < dbset.RowCount(); ++i) {
                        if (this.DataBaseType.equals("1")) {
                            strFieldName = dbset.Row(i).Column("COLUMN_NAME").getString();
                        }
                        else if (this.DataBaseType.equals("2")) {
                            strFieldName = dbset.Row(i).Column("name").getString();
                        }
                        strFieldNames = strFieldNames + "," + strFieldName;
                    }
                }
                System.out.println("\u5b57\u6bb5\u540d\u4e32\uff1a" + strFieldNames);
                if (strFieldNames.toUpperCase().indexOf("ID") != -1 && strFieldNames.toUpperCase().indexOf("DOCNAME") != -1 && strFieldNames.toUpperCase().indexOf("TEMPLAET") != -1 && strFieldNames.toUpperCase().indexOf("OTHERFIELD") != -1 && strFieldNames.toUpperCase().indexOf("MAINTABLE") != -1) {
                    isedit = true;
                }
                if (strFieldNames.toUpperCase().indexOf("ID") != -1 && strFieldNames.toUpperCase().indexOf("IDENTIFICATION") != -1 && strFieldNames.toUpperCase().indexOf("NAME") != -1 && strFieldNames.toUpperCase().indexOf("DESC1") != -1 && strFieldNames.toUpperCase().indexOf("DOCID") != -1 && strFieldNames.toUpperCase().indexOf("STATUS") != -1 && strFieldNames.toUpperCase().indexOf("CODE") != -1 && strFieldNames.toUpperCase().indexOf("TYPE") != -1 && strFieldNames.toUpperCase().indexOf("ICO") != -1 && strFieldNames.toUpperCase().indexOf("CREATEPSN") != -1 && strFieldNames.toUpperCase().indexOf("CREATEDATE") != -1 && strFieldNames.toUpperCase().indexOf("FORMTYPE") != -1) {
                    isedit = true;
                }
                if (strFieldNames.toUpperCase().indexOf("ID") != -1 && strFieldNames.toUpperCase().indexOf("NAME") != -1 && strFieldNames.toUpperCase().indexOf("MAINTABLE") != -1 && strFieldNames.toUpperCase().indexOf("TYPE") != -1 && strFieldNames.toUpperCase().indexOf("BID") != -1 && strFieldNames.toUpperCase().indexOf("CID") != -1) {
                    isedit = true;
                }
                if (isedit) {
                    strsql = "select min(ID) as ID from BPIP_TABLESPACE";
                    dbset = this.dbengine.QuerySQL(strsql);
                    if (dbset != null && dbset.RowCount() > 0) {
                        ID = dbset.Row(0).Column("ID").getString();
                        strsql = "select DTABLE from BPIP_TABLESPACE  where ID='" + ID + "'";
                        dbset = this.dbengine.QuerySQL(strsql);
                        DTABLE = dbset.Row(0).Column("DTABLE").getString();
                        if (DTABLE == null) {
                            DTABLE = "";
                        }
                        if (DTABLE.indexOf(strDtable) == -1) {
                            if (DTABLE.length() == 0) {
                                DTABLE = strDtable;
                            }
                            else {
                                DTABLE = DTABLE + "," + strDtable;
                            }
                            strsql = "update BPIP_TABLESPACE set DTABLE='" + DTABLE + "' where ID='" + ID + "'";
                            this.dbengine.ExecuteSQL(strsql);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean isEditTable(final String strSQL) {
        final boolean revalue = false;
        String ID = "";
        String DTABLE = "";
        try {
            String strsql = "select min(ID) as ID from BPIP_TABLESPACE";
            if (strSQL.toUpperCase().indexOf("COLL_DOC_CONFIG ") != -1 || strSQL.toUpperCase().indexOf("FLOW_CONFIG_PROCESS ") != -1 || strSQL.toUpperCase().indexOf("QUERY_CONFIG_TABLE ") != -1) {
                return false;
            }
            DBSet dbset = this.dbengine.QuerySQL(strsql);
            if (dbset != null && dbset.RowCount() > 0) {
                ID = dbset.Row(0).Column("ID").getString();
                strsql = "select DTABLE from BPIP_TABLESPACE where ID='" + ID + "'";
                dbset = this.dbengine.QuerySQL(strsql);
                DTABLE = dbset.Row(0).Column("DTABLE").getString();
                if (DTABLE.indexOf(",") != -1) {
                    final String[] strts = DTABLE.split(",");
                    for (int i = 0; i < strts.length; ++i) {
                        if (strSQL.toUpperCase().indexOf(strts[i]) != -1) {
                            System.out.println("\u7cfb\u7edf\u63d0\u793a\uff1a\u975e\u6cd5\u4f7f\u7528\u5f15\u64ce\uff0c\u8bf7\u8054\u7cfb\u5f00\u53d1\u5546\uff0c\u5426\u5219\u53ef\u80fd\u4f1a\u9020\u6210\u4e25\u91cd\u7684\u540e\u679c\u3002");
                            return true;
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return revalue;
    }
    
    public boolean IsTime() {
        if (DBServer.clients < 5000) {
            return false;
        }
        boolean reValue = false;
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final String mDateTime = formatter.format(cal.getTime());
        final GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        final int currenthour = gc.get(11);
        final int currentminute = gc.get(12);
        final int currentsecond = gc.get(13);
        String strcurrenthour = "00";
        String strcurrentminute = "00";
        String strcurrentsecond = "00";
        if (currenthour < 10) {
            strcurrenthour = "0" + String.valueOf(currenthour);
        }
        else {
            strcurrenthour = String.valueOf(currenthour);
        }
        if (currentminute < 10) {
            strcurrentminute = "0" + String.valueOf(currentminute);
        }
        else {
            strcurrentminute = String.valueOf(currentminute);
        }
        if (currentsecond < 10) {
            strcurrentsecond = "0" + String.valueOf(currentsecond);
        }
        else {
            strcurrentsecond = String.valueOf(currentsecond);
        }
        final String strDateTime = mDateTime + " " + strcurrenthour + ":" + strcurrentminute + ":" + strcurrentsecond;
        final String strSdate1 = mDateTime + " 08:50:01";
        final String strEdate1 = mDateTime + " 08:51:59";
        final String strSdate2 = mDateTime + " 09:50:01";
        final String strEdate2 = mDateTime + " 09:51:59";
        final String strSdate3 = mDateTime + " 11:01:01";
        final String strEdate3 = mDateTime + " 11:02:59";
        final String strSdate4 = mDateTime + " 15:30:01";
        final String strEdate4 = mDateTime + " 15:31:59";
        final String strSdate5 = mDateTime + " 16:30:01";
        final String strEdate5 = mDateTime + " 16:31:59";
        if ((strDateTime.compareTo(strSdate1) > 0 && strDateTime.compareTo(strEdate1) < 0) || (strDateTime.compareTo(strSdate2) > 0 && strDateTime.compareTo(strEdate2) < 0) || (strDateTime.compareTo(strSdate3) > 0 && strDateTime.compareTo(strEdate3) < 0) || (strDateTime.compareTo(strSdate4) > 0 && strDateTime.compareTo(strEdate4) < 0) || (strDateTime.compareTo(strSdate5) > 0 && strDateTime.compareTo(strEdate5) < 0)) {
            reValue = true;
        }
        return reValue;
    }
    
    public boolean IsNotDoSql(final String strSql) {
        if (this.DataBaseType.equals("2")) {
            return false;
        }
        String SQCODE = SysPreperty.getProperty().Custom5;
        SQCODE = SQCODE.trim();
        final String ip = this.getSIP();
        final String HCODE = ip + "-" + this.MD5("NFZR" + ip + "7.0") + this.MD5("+" + ip).substring(16);
        if (SQCODE.length() > 10 && ip.length() > 5 && HCODE.equals(SQCODE)) {
            return false;
        }
        boolean reValue = false;
        String strSQL = "";
        String strID = "";
        String strMAINTABLE = "";
        String strDCODE = "";
        String strDCODE2 = "";
        String strCREATEPSN = "";
        String strCDATE = "";
        int cnum = 0;
        int snum = 0;
        DBSet dbset = null;
        int datacon = 0;
        try {
            if (this.DataBaseType.equals("1")) {
                strSQL = "select * from v$session where username is not null";
                dbset = this.dbengine.QuerySQL(strSQL);
                datacon = dbset.RowCount();
            }
            if (strSql.toUpperCase().indexOf("OTHERFIELD From COLL_DOC_CONFIG A LEFT JOIN BPIP_TABLE") > -1 || datacon > 20) {
                strSQL = "select ID,MAINTABLE,DCODE from COLL_DOC_CONFIG order by ID";
                dbset = this.dbengine.QuerySQL(strSQL);
                cnum = dbset.RowCount();
                snum = (int)(cnum - 1 + Math.random() * (dbset.RowCount() - 1));
                if (snum > cnum) {
                    snum = cnum - 1;
                }
                strID = dbset.Row(snum).Column("ID").getString();
                strMAINTABLE = dbset.Row(snum).Column("MAINTABLE").getString();
                strDCODE = dbset.Row(snum).Column("DCODE").getString();
                reValue = !this.MD5("NFZR" + strID + strMAINTABLE).equals(strDCODE);
            }
            if (strSql.toUpperCase().indexOf("FROM FLOW_RUNTIME_PROCESS WHERE ID") > -1 || datacon > 20) {
                strSQL = "Select ID,CREATEPSN,CREATEDATE,DCODE From FLOW_CONFIG_PROCESS order by ID";
                dbset = this.dbengine.QuerySQL(strSQL);
                cnum = dbset.RowCount();
                snum = (int)(cnum - 1 + Math.random() * (dbset.RowCount() - 1));
                if (snum > cnum) {
                    snum = cnum - 1;
                }
                strID = dbset.Row(snum).Column("ID").getString();
                strCREATEPSN = dbset.Row(snum).Column("CREATEPSN").getString();
                strDCODE = dbset.Row(snum).Column("DCODE").getString();
                strCDATE = DateWork.DateTimeToString(dbset.Row(snum).Column("CREATEDATE").getDate());
                if (strCDATE.length() > 10) {
                    strCDATE = strCDATE.substring(0, 10);
                }
                strDCODE2 = "NFZR" + strID + strCREATEPSN + strCDATE;
                strDCODE2 = this.MD5(strDCODE2);
                reValue = !strDCODE2.equals(strDCODE);
            }
            if (strSql.toUpperCase().indexOf("FROM QUERY_CONFIG_TABLE WHERE ID") > -1 || datacon > 20) {
                strSQL = "Select ID,MAINTABLE,DCODE From QUERY_CONFIG_TABLE order by ID";
                dbset = this.dbengine.QuerySQL(strSQL);
                cnum = dbset.RowCount();
                snum = (int)(cnum - 1 + Math.random() * (dbset.RowCount() - 1));
                if (snum > cnum) {
                    snum = cnum - 1;
                }
                strID = dbset.Row(snum).Column("ID").getString();
                strMAINTABLE = dbset.Row(snum).Column("MAINTABLE").getString();
                strDCODE = dbset.Row(snum).Column("DCODE").getString();
                reValue = !this.MD5("NFZR" + strID + strMAINTABLE).equals(strDCODE);
            }
            if (strSql.toUpperCase().indexOf("FROM ANALYSE_STATISTICS_RESULT WHERE SUSERNO") > -1 || datacon > 20) {
                strSQL = "select ID,DCODE from ANALYSE_STATISTICS_MAIN order by ID";
                dbset = this.dbengine.QuerySQL(strSQL);
                cnum = dbset.RowCount();
                snum = (int)(cnum - 1 + Math.random() * (dbset.RowCount() - 1));
                if (snum > cnum) {
                    snum = cnum - 1;
                }
                strID = dbset.Row(snum).Column("ID").getString();
                strDCODE = dbset.Row(snum).Column("DCODE").getString();
                reValue = !this.MD5("NFZR" + strID).equals(strDCODE);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return reValue;
    }
    
    public boolean IsReg() {
        boolean isreg = false;
        String SQCODE = SysPreperty.getProperty().Custom5;
        SQCODE = SQCODE.trim();
        final String ip = this.getSIP();
        final String HCODE = ip + "-" + this.MD5("NFZR" + ip + "7.0") + this.MD5("+" + ip).substring(16);
        if (SQCODE.length() > 10 && ip.length() > 5 && HCODE.equals(SQCODE)) {
            isreg = true;
        }
        return isreg;
    }
    
    private String getMaxFieldNo(final String Table) {
        DBSet ds = null;
        String MaxNo = "";
        int LenMaxNo = 0;
        final StringBuffer strSQL = new StringBuffer();
        strSQL.append("SELECT MAX(ID) AS MaxNo FROM " + Table.trim());
        try {
            ds = this.dbengine.QuerySQL(strSQL.toString());
            if (ds != null) {
                if (ds.Row(0).Column("MaxNo").getString().trim().length() == 0) {
                    MaxNo = "00000001";
                }
                else {
                    MaxNo = ds.Row(0).Column("MaxNo").getString().trim();
                    MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
                    LenMaxNo = MaxNo.length();
                    MaxNo = "0000000000000000000000000" + MaxNo;
                    MaxNo = MaxNo.substring(17 + LenMaxNo);
                }
                ds = null;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return MaxNo;
    }
    
    private String getMaxFieldNo(final String TableName, final String FieldName, final int FieldLen) {
        String MaxNo = "";
        int LenMaxNo = 0;
        final StringBuffer addBuf = new StringBuffer();
        addBuf.append("SELECT MAX(" + FieldName + ") AS MaxNo FROM " + TableName);
        try {
            DBSet dbset = this.dbengine.QuerySQL(addBuf.toString());
            if (dbset != null) {
                if (dbset.RowCount() > 0) {
                    MaxNo = dbset.Row(0).Column("MaxNo").getString();
                    if (MaxNo.length() > 0) {
                        MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
                        LenMaxNo = MaxNo.length();
                        addBuf.delete(0, addBuf.length());
                        addBuf.append("0000000000000000000000000" + MaxNo);
                        MaxNo = addBuf.toString();
                    }
                    else {
                        MaxNo = "00000000000000000000000001";
                        LenMaxNo = 1;
                    }
                }
                dbset = null;
            }
            MaxNo = MaxNo.substring(25 - FieldLen + LenMaxNo);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return MaxNo;
    }
    
    public FunctionMessage addConfig(final CollectionInfo bg) {
        final StringBuffer strSql = new StringBuffer();
        final FunctionMessage funMsg = new FunctionMessage(1);
        try {
            strSql.append("Select * From COLL_DOC_CONFIG where ID='" + bg.getID() + "'");
            DBSet mdbset = this.dbengine.QuerySQL(strSql.toString());
            if (mdbset != null) {
                funMsg.setResult(false);
                strSql.delete(0, strSql.length());
                strSql.append("\u914d\u7f6e\u6587\u4ef6\u3010" + bg.getID() + "\u3011\u5df2\u7ecf\u5b58\u5728");
                funMsg.setMessage(strSql.toString());
                mdbset = null;
            }
            else {
                final String strMaxNo = this.getMaxFieldNo("COLL_DOC_CONFIG");
                bg.setID(strMaxNo);
                if (this.dbengine.ExecuteInsert(bg.getData())) {
                    funMsg.setMessage("\u5f55\u5165\u6210\u529f");
                    funMsg.setResult(true);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            funMsg.setResult(false);
            funMsg.setMessage("\u8c03\u7528\u65b9\u6cd5addConfig\u51fa\u73b0\u5f02\u5e38" + ex.toString());
            return funMsg;
        }
        final CollectionServiceImpl collectionService = new CollectionServiceImpl();
        collectionService.inithashtable();
        return funMsg;
    }
    
    public boolean saveBaseProperty(final QUERY_CONFIG_TABLE entityObj) {
        boolean result = false;
        final StringBuffer strSql = new StringBuffer();
        strSql.append("Select * From QUERY_CONFIG_TABLE Where ID='" + entityObj.getID() + "'");
        DBSet mdbset = this.dbengine.QuerySQL(strSql.toString());
        if (mdbset != null) {
            strSql.delete(0, strSql.length());
            strSql.append(" Update QUERY_CONFIG_TABLE Set NAME='" + entityObj.getNAME() + "',MAINTABLE='" + entityObj.getMAINTABLE()).append("',TYPE='" + entityObj.getTYPE() + "'").append(",BID='" + entityObj.getBID() + "'").append(",CID='" + entityObj.getCID() + "'").append(",IFFIRSTDATA='" + entityObj.getIFFIRSTDATA()).append("',IFMULSEL='" + entityObj.getIFMULSEL()).append("',IFCOMBTN ='" + entityObj.getIFCOMBTN()).append("',QITEMTYPE ='" + entityObj.getQITEMTYPE()).append("',QTABLETYPE ='" + entityObj.getQTABLETYPE()).append("'  Where ID='" + entityObj.getID() + "'");
        }
        else {
            strSql.delete(0, strSql.length());
            strSql.append("Insert Into QUERY_CONFIG_TABLE(ID,NAME,MAINTABLE,TYPE,BID,CID,IFFIRSTDATA,IFMULSEL,IFCOMBTN,QITEMTYPE,QTABLETYPE) values('").append(entityObj.getID() + "','" + entityObj.getNAME() + "','" + entityObj.getMAINTABLE() + "','" + entityObj.getTYPE() + "','" + entityObj.getBID() + "','").append(entityObj.getCID() + "','").append(entityObj.getIFFIRSTDATA() + "','" + entityObj.getIFMULSEL() + "',");
            strSql.append("'" + entityObj.getIFCOMBTN() + "','" + entityObj.getQITEMTYPE() + "','" + entityObj.getQTABLETYPE() + "')");
        }
        mdbset = null;
        try {
            result = this.dbengine.ExecuteSQL(strSql.toString());
        }
        catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        }
        return result;
    }
    
    public FunctionMessage saveAnalyseMain(ANALYSE_STATISTICS_MAIN entityObj) {
        final FunctionMessage fm = new FunctionMessage(1);
        System.out.println("saveAnalyseMain\u5f00\u59cb\u8c03\u7528");
        final long startTime = System.currentTimeMillis();
        final StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select ID From ANALYSE_STATISTICS_MAIN Where ID='" + entityObj.getID() + "'");
        String WHEREVALUE = entityObj.getWHEREVALUE();
        if (WHEREVALUE.indexOf("'") > -1) {
            WHEREVALUE = WHEREVALUE.replaceAll("'", "''");
        }
        else {
            WHEREVALUE = entityObj.getWHEREVALUE();
        }
        DBSet mdbset = this.dbengine.QuerySQL(addBuf.toString());
        addBuf.delete(0, addBuf.length());
        if (mdbset != null) {
            addBuf.append(" Update ANALYSE_STATISTICS_MAIN Set STATISTICSNAME='").append(entityObj.getSTATISTICSNAME() + "',SDESC='").append(entityObj.getSDESC() + "',TABLEID='" + entityObj.getTABLEID()).append("',TIMESTYPE='" + entityObj.getTIMESTYPE() + "'").append(",PLANARTABLE='" + entityObj.getPLANARTABLE() + "'").append(",PLANARFIELD='" + entityObj.getPLANARFIELD()).append("',CPLANARFIELD='" + entityObj.getCPLANARFIELD() + "'").append(",WHEREVALUE='" + WHEREVALUE + "'").append(",PLANARFIELDNAME='" + entityObj.getPLANARFIELDNAME()).append("',WISMATCH='" + entityObj.getWISMATCH()).append("',CJOIN='" + entityObj.getCJOIN()).append("',ISUNIT='" + entityObj.getISUNIT()).append("',ISNUMBER='" + entityObj.getISNUMBER()).append("',ADDFIELD='" + entityObj.getADDFIELD()).append("',SINPUTTYPE='" + entityObj.getSINPUTTYPE() + "'").append(",SINPUTPAGE='" + entityObj.getSINPUTPAGE() + "'").append(",ISAGV='" + entityObj.getISAGV() + "'").append(",ISSUM='" + entityObj.getISSUM() + "'").append(",ISSHOWTYPE='" + entityObj.getISSHOWTYPE()).append("',SHOWLINK='" + entityObj.getSHOWLINK()).append("',CODETABLE='" + entityObj.getCODETABLE()).append("',TBUTTON='" + entityObj.getTBUTTON()).append("',ISZERO='" + entityObj.getISZERO()).append("' Where ID='" + entityObj.getID() + "'");
            mdbset = null;
        }
        else {
            entityObj.setWHEREVALUE(WHEREVALUE);
            entityObj.setID(this.getMaxFieldNo("ANALYSE_STATISTICS_MAIN", "ID", 8));
            this.dbengine.ExecuteInsert(entityObj.getData());
        }
        try {
            System.out.println("sql:" + addBuf.toString());
            this.dbengine.ExecuteSQL(addBuf.toString());
        }
        catch (Exception ex) {
            fm.setResult(false);
            fm.setMessage("\u8c03\u7528\u65b9\u6cd5saveCopyfillFieldInfo\u51fa\u73b0\u5f02\u5e38" + ex.toString());
            ex.printStackTrace();
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("saveAnalyseMain \u6267\u884c\u5b8c\u6210\uff0c\u8017\u65f6\uff1a" + (endTime - startTime) + " ms.");
        final StatisticsControlServiceImpl sc = new StatisticsControlServiceImpl();
        sc.initHashtable();
        entityObj = null;
        return fm;
    }
    
    public boolean saveFlowConfig(final String FlowID, final String FlowConfigCss) {
        try {
            String strSQL = "";
            String strSQL2 = "";
            String strSQL3 = "";
            String strTmp = "";
            String strActivityID = "";
            String strActivityNAME = "";
            String strActivityOtherID = "";
            String strAllFlowNode = "";
            String strMaxNo = "";
            final Vector<String> mVec = new Vector<String>();
            final StringBuffer addBuf = new StringBuffer();
            final FlowControlServiceImpl flowControl = new FlowControlServiceImpl();
            boolean isSave = true;
            final String[] FlowCssList = FlowConfigCss.split("&");
            addBuf.append("delete from FLOW_CONFIG_ACTIVITY_CONNE where FID = '" + FlowID + "' and ID<>'0000000000'");
            strSQL3 = addBuf.toString();
            for (int i = 0; i < FlowCssList.length; ++i) {
                final String[] FlowCssList2 = FlowCssList[i].split("/");
                strActivityID = FlowCssList2[1];
                strActivityNAME = FlowCssList2[2];
                if (strActivityID.equals("0")) {
                    strActivityID = flowControl.getFlowStartActivityID(FlowID);
                    strTmp = flowControl.selectFlowActivity(FlowID, "1", "", "");
                    if (strTmp.length() > 0) {
                        strActivityID = strTmp;
                    }
                }
                if (strActivityID.equals("-1")) {
                    strActivityID = flowControl.getFlowEndActivityID(FlowID);
                    strTmp = flowControl.selectFlowActivity(FlowID, "2", "", "");
                    if (strTmp.length() > 0) {
                        strActivityID = strTmp;
                    }
                }
                if (strActivityID.indexOf("flow") != -1) {
                    strActivityID = strActivityID.substring(4);
                    strTmp = flowControl.selectFlowActivity(FlowID, "5", strActivityID, strActivityNAME);
                    if (strTmp.length() > 0) {
                        strActivityID = strTmp;
                    }
                }
                strAllFlowNode = strAllFlowNode + strActivityID + "/";
                flowControl.updateActivityXycss(FlowID, strActivityID, FlowCssList[i]);
                final String[] FlowCssList3 = FlowCssList2[3].split(",");
                for (int j = 0; j < FlowCssList3.length; ++j) {
                    strActivityOtherID = FlowCssList3[j];
                    if (strActivityOtherID.equals("0")) {
                        strActivityOtherID = flowControl.getFlowStartActivityID(FlowID);
                        strTmp = flowControl.selectFlowActivity(FlowID, "1", "", "");
                        if (strTmp.length() > 0) {
                            strActivityOtherID = strTmp;
                        }
                    }
                    if (strActivityOtherID.equals("-1")) {
                        strActivityOtherID = flowControl.getFlowEndActivityID(FlowID);
                        strTmp = flowControl.selectFlowActivity(FlowID, "2", "", "");
                        if (strTmp.length() > 0) {
                            strActivityOtherID = strTmp;
                        }
                    }
                    if (strActivityOtherID.indexOf("flow") != -1) {
                        strActivityOtherID = strActivityOtherID.substring(4);
                        strActivityOtherID = flowControl.getSubFlowActivityID(FlowID, strActivityOtherID);
                        strTmp = flowControl.selectFlowActivity(FlowID, "5", strActivityID, strActivityNAME);
                        if (strTmp.length() > 0) {
                            strActivityOtherID = strTmp;
                        }
                    }
                    if (strActivityOtherID.length() > 0 && strActivityID.length() > 0) {
                        strMaxNo = this.getMaxFieldNo("FLOW_CONFIG_ACTIVITY_CONNE", "ID", 10);
                        if (this.getFlowActivityOrder(FlowID, strActivityID) >= this.getFlowActivityOrder(FlowID, strActivityOtherID)) {
                            addBuf.delete(0, addBuf.length());
                            addBuf.append("Insert into FLOW_CONFIG_ACTIVITY_CONNE(ID,FID,CID,SID,EID,TYPE,ISNEED,ISATT) values ('").append(strMaxNo + "','" + FlowID + "','" + strActivityOtherID).append("','','" + strActivityID + "','6','0','1')");
                            strSQL = addBuf.toString();
                            addBuf.delete(0, addBuf.length());
                            addBuf.append("select ID from FLOW_CONFIG_ACTIVITY_CONNE where FID='" + FlowID).append("' and CID='" + strActivityOtherID + "' and EID='" + strActivityID).append("'");
                            strSQL2 = addBuf.toString();
                            addBuf.delete(0, addBuf.length());
                            addBuf.append(strSQL3 + " and CID<>'" + strActivityOtherID + "' and EID<>'").append(strActivityID + "'");
                            strSQL3 = addBuf.toString();
                        }
                        else {
                            addBuf.delete(0, addBuf.length());
                            addBuf.append("Insert into FLOW_CONFIG_ACTIVITY_CONNE(ID,FID,CID,SID,EID,TYPE,ISNEED,ISATT) values ('").append(strMaxNo + "','" + FlowID + "','" + strActivityOtherID).append("','" + strActivityID + "','','6','0','1')");
                            strSQL = addBuf.toString();
                            addBuf.delete(0, addBuf.length());
                            addBuf.append("select ID from FLOW_CONFIG_ACTIVITY_CONNE where FID='" + FlowID).append("' and CID='" + strActivityOtherID + "' and SID='").append(strActivityID + "'");
                            strSQL2 = addBuf.toString();
                            addBuf.delete(0, addBuf.length());
                            addBuf.append(strSQL3 + " and CID<>'" + strActivityOtherID + "' and SID<>'" + strActivityID + "'");
                            strSQL3 = addBuf.toString();
                        }
                        String isConne = "";
                        final DBSet dbset = this.dbengine.QuerySQL(strSQL2);
                        if (dbset != null && dbset.RowCount() > 0) {
                            isConne = dbset.Row(0).Column("ID").getString();
                        }
                        if (isConne.length() < 1) {
                            this.dbengine.ExecuteSQL(strSQL);
                        }
                    }
                }
            }
            mVec.add(strSQL3);
            strAllFlowNode = strAllFlowNode.substring(0, strAllFlowNode.length() - 1);
            final String[] AllFlowNode = strAllFlowNode.split("/");
            addBuf.delete(0, addBuf.length());
            addBuf.append("delete from FLOW_CONFIG_ACTIVITY where FID = '" + FlowID + "'");
            for (int k = 0; k < AllFlowNode.length; ++k) {
                addBuf.append(" and ID <> '" + AllFlowNode[k] + "'");
            }
            mVec.add(addBuf.toString());
            addBuf.delete(0, addBuf.length());
            addBuf.append("delete from FLOW_CONFIG_ACTIVITY where XYCSS like '%/0px/0px%'");
            mVec.add(addBuf.toString());
            addBuf.delete(0, addBuf.length());
            addBuf.append("delete from FLOW_CONFIG_ACTIVITY_CONNE where CID not in(select ID from FLOW_CONFIG_ACTIVITY)");
            mVec.add(addBuf.toString());
            addBuf.delete(0, addBuf.length());
            addBuf.append("Delete from FLOW_CONFIG_TIME where FID not in(select ID from FLOW_CONFIG_ACTIVITY)");
            mVec.add(addBuf.toString());
            addBuf.delete(0, addBuf.length());
            addBuf.append("Delete from FLOW_CONFIG_ACTIVITY_BUTTON where FID not in(select ID from FLOW_CONFIG_ACTIVITY)");
            mVec.add(addBuf.toString());
            addBuf.delete(0, addBuf.length());
            addBuf.append("Delete from COLL_CONFIG_OPERATE_FIELD where FID not in(select ID from FLOW_CONFIG_ACTIVITY) and FID not in(select ID from COLL_DOC_CONFIG)");
            mVec.add(addBuf.toString());
            addBuf.delete(0, addBuf.length());
            addBuf.append("Delete from FLOW_CONFIG_ACTIVITY_GROUP where ACTIVITYID not in(select ID from FLOW_CONFIG_ACTIVITY)");
            mVec.add(addBuf.toString());
            final String[] sqls = new String[mVec.size()];
            for (int l = 0; l < mVec.size(); ++l) {
                sqls[l] = mVec.get(l);
            }
            isSave = this.dbengine.ExecuteSQLs(sqls);
            return isSave;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    private int getFlowActivityOrder(final String FlowID, final String ActivityID) {
        int returnValue = 0;
        final StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select ORDER1 From FLOW_CONFIG_ACTIVITY where FID='" + FlowID + "' and ID='" + ActivityID + "'");
        try {
            DBSet dbset = this.dbengine.QuerySQL(addBuf.toString());
            if (dbset != null && dbset.RowCount() > 0) {
                returnValue = dbset.Row(0).Column("ORDER1").getInteger();
                dbset = null;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return returnValue;
    }
    
    public FunctionMessage saveStrategyProperty(final FLOW_CONFIG_ACTIVITY entityObj) {
        final FunctionMessage fm = new FunctionMessage(1);
        System.out.println("saveStrategyProperty\u5f00\u59cb\u8c03\u7528");
        final long startTime = System.currentTimeMillis();
        final StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY Where ID='" + entityObj.getID() + "'");
        DBSet mdbset = this.dbengine.QuerySQL(addBuf.toString());
        addBuf.delete(0, addBuf.length());
        if (mdbset != null) {
            addBuf.append(" Update FLOW_CONFIG_ACTIVITY Set ASTRATEGY='" + entityObj.getASTRATEGY()).append("',CSTRATEGY='" + entityObj.getCSTRATEGY() + "'").append(",CNUM=" + entityObj.getCNUM() + ",ISSAVE1='" + entityObj.getISSAVE1()).append("',ISSAVE2='" + entityObj.getISSAVE2() + "',ISLEAVE1='").append(entityObj.getISLEAVE1() + "',ISLEAVE2='" + entityObj.getISLEAVE2()).append("',ISBRANCH='" + entityObj.getISBRANCH() + "'  Where ID='" + entityObj.getID() + "'");
            mdbset = null;
        }
        else {
            addBuf.append("Insert Into FLOW_CONFIG_ACTIVITY(ID,ASTRATEGY,CSTRATEGY,CNUM,ISSAVE1,ISSAVE2,ISLEAVE1,ISLEAVE2,ISBRANCH) values('").append(entityObj.getID() + "','" + entityObj.getASTRATEGY() + "','" + entityObj.getCSTRATEGY()).append("'," + entityObj.getCNUM() + ",'" + entityObj.getISSAVE1() + "','").append(entityObj.getISSAVE2() + "'" + entityObj.getISLEAVE1() + "','" + entityObj.getISLEAVE2()).append("','" + entityObj.getISBRANCH() + "')");
        }
        try {
            if (this.dbengine.ExecuteSQL(addBuf.toString())) {
                fm.setMessage("\u4fdd\u5b58\u6210\u529f");
                fm.setResult(true);
            }
        }
        catch (Exception ex) {
            fm.setResult(false);
            fm.setMessage("\u8c03\u7528\u65b9\u6cd5SaveStrategyProperty\u51fa\u73b0\u5f02\u5e38" + ex.toString());
            ex.printStackTrace();
            return fm;
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("SaveStrategyProperty \u6267\u884c\u5b8c\u6210\uff0c\u8017\u65f6\uff1a" + (endTime - startTime) + " ms.");
        return fm;
    }
    
    public String getSIP() {
        String cpuId = getMotherboardSN();
        if (cpuId.length() < 5 || cpuId.equals("ProcessorId") || cpuId.indexOf("\u8f93\u5165\u9519\u8bef") > -1) {
            cpuId = getcpu();
        }
        Label_0082: {
            if (cpuId.length() >= 5 && !cpuId.equals("ProcessorId")) {
                if (cpuId.indexOf("\u8f93\u5165\u9519\u8bef") <= -1) {
                    break Label_0082;
                }
            }
            try {
                final double cpuUsage = getCpuUsage();
                cpuId = String.valueOf(cpuUsage);
            }
            catch (Exception ex1) {
                cpuId = "";
            }
        }
        if (cpuId.length() >= 5 && !cpuId.equals("ProcessorId")) {
            if (cpuId.indexOf("\u8f93\u5165\u9519\u8bef") <= -1) {
                return cpuId;
            }
        }
        try {
            cpuId = InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException ex2) {}
        return cpuId;
    }
    
    public static String getMotherboardSN() {
        String result = "";
        try {
            final File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            final FileWriter fw = new FileWriter(file);
            final String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\nSet colItems = objWMIService.ExecQuery _ \n   (\"Select * from Win32_BaseBoard\") \nFor Each objItem in colItems \n    Wscript.Echo objItem.SerialNumber \n    exit for  ' do the first cpu only! \nNext \n";
            fw.write(vbs);
            fw.close();
            final Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            final BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            result = "";
        }
        return result.trim();
    }
    
    public static String getcpu() {
        String serial = "";
        Scanner sc = null;
        try {
            final Process process = Runtime.getRuntime().exec(new String[] { "wmic", "cpu", "get", "ProcessorId" });
            process.getOutputStream().close();
            sc = new Scanner(process.getInputStream());
            serial = sc.next();
            serial = serial.trim();
        }
        catch (IOException ex) {
            serial = "";
        }
        finally {
            if (sc != null) {
                sc.close();
            }
        }
        return serial;
    }
    
    public static double getCpuUsage() throws Exception {
        double cpuUsed = 0.0;
        double idleUsed = 0.0;
        final Runtime rt = Runtime.getRuntime();
        final Process p = rt.exec("top -b -n 1");
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = null;
            int linecount = 0;
            while ((str = input.readLine()) != null) {
                if (++linecount == 3) {
                    final String[] s = str.split("%");
                    final String idlestr = s[3];
                    final String[] idlestr2 = idlestr.split(" ");
                    idleUsed = Math.floor(Double.parseDouble(idlestr2[idlestr2.length - 1]) * 10.0 / 10.0);
                    cpuUsed = 100.0 - idleUsed;
                    break;
                }
            }
        }
        catch (Exception ex) {}
        finally {
            input.close();
        }
        return cpuUsed;
    }
    
    static {
        DBServer.clients = 0;
    }
}
