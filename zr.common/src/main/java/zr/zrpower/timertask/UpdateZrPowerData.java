package zr.zrpower.timertask;

import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <p>Title:系统自动执行服务</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company:</p>
 */
public class UpdateZrPowerData {
	private DBEngine dbengine; // 数据库引擎

	public UpdateZrPowerData() {
		dbengine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbengine.initialize();
	}

	/**
	 * 更新业务系统的一些特殊情况
	 * @throws Exception
	 */
	public void updateSystemData() throws Exception {
		String strSQL = "";
		// -------------
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String mDateTime = formatter.format(cal.getTime());
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		int currenthour = gc.get(Calendar.HOUR_OF_DAY);
		int currentminute = gc.get(Calendar.MINUTE);
		int currentsecond = gc.get(Calendar.SECOND);

		String strcurrenthour = "00";
		String strcurrentminute = "00";
		String strcurrentsecond = "00";
		if (currenthour < 10) {
			strcurrenthour = "0" + String.valueOf(currenthour);
		} else {
			strcurrenthour = String.valueOf(currenthour);
		}
		if (currentminute < 10) {
			strcurrentminute = "0" + String.valueOf(currentminute);
		} else {
			strcurrentminute = String.valueOf(currentminute);
		}
		if (currentsecond < 10) {
			strcurrentsecond = "0" + String.valueOf(currentsecond);
		} else {
			strcurrentsecond = String.valueOf(currentsecond);
		}
		// 当天日期(年月日 时分秒)
		String strDateTime = df.format(gc.getTime()) + " " + strcurrenthour + ":" + strcurrentminute + ":"
				+ strcurrentsecond;
		// 得到自动运行服务的时间
		String strSdate = "04:01:01";
		String strEdate = "05:01:01";

		// 判断是否在有效的时间范围内
		strSdate = mDateTime + " " + strSdate;
		strEdate = mDateTime + " " + strEdate;

		String strSdate1 = "12:30:01";
		String strEdate1 = "13:30:01";

		strSdate1 = mDateTime + " " + strSdate1;
		strEdate1 = mDateTime + " " + strEdate1;
		// 在设置的时间范围内
		if ((strDateTime.compareTo(strSdate) > 0 && strDateTime.compareTo(strEdate) < 0)
				|| (strDateTime.compareTo(strSdate1) > 0 && strDateTime.compareTo(strEdate1) < 0)) {

			strSQL = "delete from BPIP_MENU;";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('003004', '数据库管理', '0', null, '0', '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('003001003', '数据字典管理', '1', 'Zrsysmanage/Dict/UdictIndex1.html', '0', '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('004002', '流程引擎', '0', null, '0', '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('004002001', '流程配置管理', '1', 'ZrWorkFlow/flowmain.html', '1', '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('004001007', '自定义表单配置', '1', 'ZrCollEngine/ConfigMsg/MainConfig/List.html', '1', '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('004', '引擎管理', '0', null, '0', '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('004001', '自定义表单引擎', '0', null, '0', '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('003', '系统管理', '0', null, '0', '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('003001', '字典管理', '0', null, null, '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('004003', '查询引擎', '0', null, '0', '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('004003001', '查询配置管理', '1', 'ZrQueryEngine/querymain.html', '1', '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('004004', '报表引擎', '0', null, '0', '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('004004001', '报表配置管理', '1', 'ZrAnalyseEngine/StatisticsConfig/StatisticsIndex.html', '1', '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('001003', '个人配置', '0', null, '0', '1');";

			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('003003', '系统设置', '0', null, null, '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('003003001', '单位管理', '1', 'Zrsysmanage/UnitIndex.html', null, '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('003003002', '用户管理', '1', 'Zrsysmanage/UserIndex.html', null, '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('003003003', '菜单管理', '1', 'Zrsysmanage/menu/ListMenuIndex.html', null, '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('003003004', '角色管理', '1', 'Zrsysmanage/role/List.html', null, '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('001001001', '首页显示', '1', 'sylist.html', '0', '0');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('003003006', '角色用户管理', '1', 'Zrsysmanage/role/ListRoleUser.html', '0', '1');";
			strSQL = strSQL
					+ "insert into BPIP_MENU (MENUNO, MENUNAME, ISPOWER, URL, ISOPEN, FLAG) values ('003004001', '数据库设置', '1', 'Zrsysmanage/DbManage/DbIndex.html', '0', '1');";

			dbengine.ExecuteSQLs(strSQL.split(";"));

			strSQL = "delete from BPIP_ROLE;";

			strSQL = strSQL + "insert into BPIP_ROLE (ROLEID, ROLENAME, DESCRIPTION) values (3, '主任', '主任');";
			strSQL = strSQL + "insert into BPIP_ROLE (ROLEID, ROLENAME, DESCRIPTION) values (1, '系统管理员', '系统管理员');";
			strSQL = strSQL + "insert into BPIP_ROLE (ROLEID, ROLENAME, DESCRIPTION) values (2, '系统设计员', '系统设计员');";

			strSQL = strSQL + "delete from BPIP_ROLE_RERMISSISSON;";

			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '001001001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '001001014');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '001003001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '001003002');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '001003003');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '003001003');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '003003001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '003003002');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '003003003');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '003003004');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '003003006');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '003003007');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '003004001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '004001007');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '004002001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '004003001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (2, '004004001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (3, '001001014');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (3, '001003001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (3, '001003002');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (3, '001003003');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '001001001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '001001014');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '001003001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '001003002');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '001003003');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '003001003');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '003003001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '003003002');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '003003003');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '003003004');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '003003006');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '003003007');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '003004001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '004001007');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '004002001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '004003001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (1, '004004001');";
			strSQL = strSQL + "insert into BPIP_ROLE_RERMISSISSON (ROLEID, MENUNO) values (3, '001001001');";

			dbengine.ExecuteSQLs(strSQL.split(";"));

			strSQL = "delete from BPIP_UNIT;";

			strSQL = strSQL
					+ "insert into BPIP_UNIT (UNITID, UNITNAME, STATE, ORDERCODE)  values ('040000000000', '信息中心', '0', '04');";
			strSQL = strSQL
					+ "insert into BPIP_UNIT (UNITID, UNITNAME, STATE, ORDERCODE)  values ('050000000000', '项目部', '0', '05');";
			strSQL = strSQL
					+ "insert into BPIP_UNIT (UNITID, UNITNAME, STATE, ORDERCODE)  values ('050100000000', '项目一部', '0', '01');";
			strSQL = strSQL
					+ "insert into BPIP_UNIT (UNITID, UNITNAME, STATE, ORDERCODE)  values ('050200000000', '项目二部', '0', '02');";
			strSQL = strSQL
					+ "insert into BPIP_UNIT (UNITID, UNITNAME, STATE, ORDERCODE)  values ('050300000000', '项目三部', '0', '03');";
			strSQL = strSQL
					+ "insert into BPIP_UNIT (UNITID, UNITNAME, STATE, ORDERCODE)  values ('050101000000', '项目一部A室', '0', '01');";
			strSQL = strSQL
					+ "insert into BPIP_UNIT (UNITID, UNITNAME, STATE, ORDERCODE)  values ('050102000000', '项目一部B室', '0', '02');";
			strSQL = strSQL
					+ "insert into BPIP_UNIT (UNITID, UNITNAME, STATE, ORDERCODE)  values ('010000000000', '局长室', '0', '01');";
			strSQL = strSQL
					+ "insert into BPIP_UNIT (UNITID, UNITNAME, STATE, ORDERCODE)  values ('020000000000', '办公室', '0', '02');";
			strSQL = strSQL
					+ "insert into BPIP_UNIT (UNITID, UNITNAME, STATE, ORDERCODE)  values ('030000000000', '财务室', '0', '03');";

			strSQL = strSQL + "delete from BPIP_USER;";
			strSQL = strSQL + "delete from BPIP_USER_PHOTO;";

			strSQL = strSQL
					+ "insert into BPIP_USER (USERID, LOGINID, LOGINPW, USERSTATE, NAME, LCODE, UNITID, SEX, BIRTHDAY, PHONE, MOBILE, EMAIL, USERIMAGE, LOGINTIME, PAGESIZE, WINDOWTYPE, DEPTINSIDE, ORBERCODE, ONLINEDATE)  values ('0100000000000001', 'admin', '21232F297A57A5A743894A0E4A801FC3', '0', '管理员', 'admin', '010000000000', '1', null, null, null, null, '101', 394, 18, '6', '0', null, '2012-07-30');";
			strSQL = strSQL
					+ "insert into BPIP_USER (USERID, LOGINID, LOGINPW, USERSTATE, NAME, LCODE, UNITID, SEX, BIRTHDAY, PHONE, MOBILE, EMAIL, USERIMAGE, LOGINTIME, PAGESIZE, WINDOWTYPE, DEPTINSIDE, ORBERCODE, ONLINEDATE)  values ('0100000000000003', '00001', '4C68CEA7E58591B579FD074BCDAFF740', '0', '李雪勇', '00001', '010000000000', '1', '1963-12-24', '3807799', '13908515005', null, '64', 424, 18, '6', '0', '01', '2012-03-07');";
			strSQL = strSQL
					+ "insert into BPIP_USER (USERID, LOGINID, LOGINPW, USERSTATE, NAME, LCODE, UNITID, SEX, BIRTHDAY, PHONE, MOBILE, EMAIL, USERIMAGE, LOGINTIME, PAGESIZE, WINDOWTYPE, DEPTINSIDE, ORBERCODE, ONLINEDATE)  values ('0100000000000002', 'designer', '230ACE927DA4BB74817FA22ADC663E0A', '0', '设计员', '01', '010000000000', '1', '1988-10-30', null, '15185182939', null, '99', 1662, 26, '6', '0', null, '2012-08-01');";

			dbengine.ExecuteSQLs(strSQL.split(";"));

			strSQL = "delete from BPIP_USER_ROLE;";

			strSQL = strSQL
					+ "insert into BPIP_USER_ROLE (USERID, ROLEID, UNITID) values ('0100000000000002', 2, '010000000000');";
			strSQL = strSQL
					+ "insert into BPIP_USER_ROLE (USERID, ROLEID, UNITID) values ('0100000000000003', 3, '010000000000');";
			strSQL = strSQL
					+ "insert into BPIP_USER_ROLE (USERID, ROLEID, UNITID) values ('0100000000000001', 1, '010000000000');";

			strSQL = strSQL + "delete from BPIP_TABLESPACE;";

			strSQL = strSQL
					+ "insert into BPIP_TABLESPACE (ID, CHINESENAME, DESCRIPTION, DTABLE) values ('04', '审批表', '审批表空间', null);";
			strSQL = strSQL
					+ "insert into BPIP_TABLESPACE (ID, CHINESENAME, DESCRIPTION, DTABLE) values ('01', '系统表', '系统表空间', 'QUERY_CONFIG_TABLE,FLOW_CONFIG_PROCESS');";
			strSQL = strSQL
					+ "insert into BPIP_TABLESPACE (ID, CHINESENAME, DESCRIPTION, DTABLE) values ('02', '字典表', '字典表空间', null);";
			strSQL = strSQL
					+ "insert into BPIP_TABLESPACE (ID, CHINESENAME, DESCRIPTION, DTABLE) values ('10', '其它表', '其它表表空间', null);";
			strSQL = strSQL
					+ "insert into BPIP_TABLESPACE (ID, CHINESENAME, DESCRIPTION, DTABLE) values ('03', '基础数据表', '基础数据表空间', null);";

			strSQL = strSQL + "delete from BPIP_TABLE;";

			strSQL = strSQL
					+ "insert into BPIP_TABLE (TABLEID, TABLENAME, CHINESENAME, TABLESPACE, DESCRIPTION, TABLETYPE, PRIMARYKEY, TITLE) values ('0003', 'YS_CODE_HYZK', '婚姻状况字典表', '02', null, '1', null, null);";
			strSQL = strSQL
					+ "insert into BPIP_TABLE (TABLEID, TABLENAME, CHINESENAME, TABLESPACE, DESCRIPTION, TABLETYPE, PRIMARYKEY, TITLE) values ('0004', 'YS_CODE_XL', '学历字典表', '02', null, '1', null, null);";
			strSQL = strSQL
					+ "insert into BPIP_TABLE (TABLEID, TABLENAME, CHINESENAME, TABLESPACE, DESCRIPTION, TABLETYPE, PRIMARYKEY, TITLE) values ('0005', 'YS_CODE_ZC', '职称字典表', '02', null, '1', null, null);";
			strSQL = strSQL
					+ "insert into BPIP_TABLE (TABLEID, TABLENAME, CHINESENAME, TABLESPACE, DESCRIPTION, TABLETYPE, PRIMARYKEY, TITLE) values ('0006', 'YS_CODE_GS', '归属字典表', '02', null, '1', null, null);";
			strSQL = strSQL
					+ "insert into BPIP_TABLE (TABLEID, TABLENAME, CHINESENAME, TABLESPACE, DESCRIPTION, TABLETYPE, PRIMARYKEY, TITLE) values ('0007', 'YS_CODE_ZW', '职务字典表', '02', null, '1', null, null);";
			strSQL = strSQL
					+ "insert into BPIP_TABLE (TABLEID, TABLENAME, CHINESENAME, TABLESPACE, DESCRIPTION, TABLETYPE, PRIMARYKEY, TITLE) values ('0008', 'YS_CODE_MZ', '民族字典表', '02', null, '1', null, null);";
			strSQL = strSQL
					+ "insert into BPIP_TABLE (TABLEID, TABLENAME, CHINESENAME, TABLESPACE, DESCRIPTION, TABLETYPE, PRIMARYKEY, TITLE) values ('0009', 'YS_CODE_ZZMM', '政治面貌字典表', '02', null, '1', null, null);";
			strSQL = strSQL
					+ "insert into BPIP_TABLE (TABLEID, TABLENAME, CHINESENAME, TABLESPACE, DESCRIPTION, TABLETYPE, PRIMARYKEY, TITLE) values ('0010', 'YS_CODE_ISZZJY', '是否在职教育字典表', '02', null, '1', null, null);";
			strSQL = strSQL
					+ "insert into BPIP_TABLE (TABLEID, TABLENAME, CHINESENAME, TABLESPACE, DESCRIPTION, TABLETYPE, PRIMARYKEY, TITLE) values ('0001', 'YS_HQ_PERSON', '人事档案表', '03', null, '2', 'ID', 'NAME');";
			strSQL = strSQL
					+ "insert into BPIP_TABLE (TABLEID, TABLENAME, CHINESENAME, TABLESPACE, DESCRIPTION, TABLETYPE, PRIMARYKEY, TITLE) values ('0002', 'YS_CODE_XB', '性别字典表', '02', null, '1', null, null);";
			strSQL = strSQL
					+ "insert into BPIP_TABLE (TABLEID, TABLENAME, CHINESENAME, TABLESPACE, DESCRIPTION, TABLETYPE, PRIMARYKEY, TITLE) values ('0011', 'YS_SP_ITEM_DLHT', '项目审批表', '04', null, '3', 'ID', 'ITEMNAME');";
			strSQL = strSQL
					+ "insert into BPIP_TABLE (TABLEID, TABLENAME, CHINESENAME, TABLESPACE, DESCRIPTION, TABLETYPE, PRIMARYKEY, TITLE) values ('0013', 'YS_CODE_BIDMODALITY', '招标方式字典表', '02', null, '1', null, null);";
			strSQL = strSQL
					+ "insert into BPIP_TABLE (TABLEID, TABLENAME, CHINESENAME, TABLESPACE, DESCRIPTION, TABLETYPE, PRIMARYKEY, TITLE) values ('0012', 'YS_CODE_MONEYUNIT', '委托金额字典表', '02', null, '1', null, null);";

			dbengine.ExecuteSQLs(strSQL.split(";"));

			strSQL = "delete from BPIP_FIELD;";

			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000054', '0003', 'CODE', '1', '编号', '1', 6, '0', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000055', '0003', 'NAME', '1', '名称', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000056', '0003', 'SPELL', '1', '拼音首字母', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000057', '0003', 'ISSHOW', '1', '是否禁用', '1', 1, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000058', '0004', 'CODE', '1', '编号', '1', 6, '0', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000059', '0004', 'NAME', '1', '名称', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000060', '0004', 'SPELL', '1', '拼音首字母', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000061', '0004', 'ISSHOW', '1', '是否禁用', '1', 1, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000062', '0005', 'CODE', '1', '编号', '1', 6, '0', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000063', '0005', 'NAME', '1', '名称', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000064', '0005', 'SPELL', '1', '拼音首字母', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000065', '0005', 'ISSHOW', '1', '是否禁用', '1', 1, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000066', '0006', 'CODE', '1', '编号', '1', 6, '0', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000067', '0006', 'NAME', '1', '名称', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000068', '0006', 'SPELL', '1', '拼音首字母', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000069', '0006', 'ISSHOW', '1', '是否禁用', '1', 1, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000070', '0007', 'CODE', '1', '编号', '1', 6, '0', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000071', '0007', 'NAME', '1', '名称', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000072', '0007', 'SPELL', '1', '拼音首字母', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000073', '0007', 'ISSHOW', '1', '是否禁用', '1', 1, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000074', '0008', 'CODE', '1', '编号', '1', 6, '0', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000075', '0008', 'NAME', '1', '名称', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000076', '0008', 'SPELL', '1', '拼音首字母', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000077', '0008', 'ISSHOW', '1', '是否禁用', '1', 1, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000078', '0009', 'CODE', '1', '编号', '1', 6, '0', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000079', '0009', 'NAME', '1', '名称', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000080', '0009', 'SPELL', '1', '拼音首字母', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000081', '0009', 'ISSHOW', '1', '是否禁用', '1', 1, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000082', '0010', 'CODE', '1', '编号', '1', 6, '0', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000083', '0010', 'NAME', '1', '名称', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000084', '0010', 'SPELL', '1', '拼音首字母', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000085', '0010', 'ISSHOW', '1', '是否禁用', '1', 1, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD,ISKEY) values ('00000086', '0011', 'ID', '5', '编号', '1', 25, '0', null, '          ', 0, 'D', null, null, 100, null,'1');";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000087', '0011', 'CONSIGNUNIT', '1', '委托单位', '1', 400, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000088', '0011', 'ITEMNAME', '1', '项目名称', '1', 400, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000089', '0011', 'ITEMBODY', '1', '项目内容', '1', 3000, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000090', '0011', 'ITEMBUDGET', '1', '委托金额', '2', 18, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000091', '0011', 'MONEYUNIT', '2', '委托金额(单位)', '1', 3, '1', 'YS_CODE_MONEYUNIT', '          ', 2, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000092', '0011', 'APPROVECODE', '1', '审批文号', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000093', '0011', 'BIDMODALITY', '2', '招标方式', '1', 3, '1', 'YS_CODE_BIDMODALITY', '          ', 2, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000094', '0011', 'SYFG', '1', '适用法规', '1', 2000, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000095', '0011', 'RLZYPB', '1', '人力资源配备', '1', 2000, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000096', '0011', 'WZZYBZ', '1', '物质资源保障', '1', 2000, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000097', '0011', 'XMMBJJDAP', '1', '项目目标及进度安排', '1', 2000, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000098', '0011', 'CZGC', '1', '适用的操作规程', '1', 2000, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000099', '0011', 'ZYSX', '1', '特别注意事项', '1', 2000, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000101', '0011', 'SPYJ1', '1', '部门经理审批意见', '1', 500, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000102', '0011', 'SPR1', '3', '部门经理', '1', 16, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000103', '0011', 'SPSJ1', '1', '部门经理审批时间', '4', 0, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000105', '0011', 'SPYJ2', '1', '总经理审批意见', '1', 500, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000106', '0011', 'SPR2', '3', '总经理', '1', 16, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000107', '0011', 'SPSJ2', '1', '总经理审批时间', '4', 0, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD,ISKEY) values ('00000001', '0001', 'ID', '5', 'ID', '1', 23, '0', null, '          ', 0, 'P', null, null, 100, null,'1');";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000002', '0001', 'PCODE', '1', '编号', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000003', '0001', 'NAME', '1', '姓名', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000004', '0001', 'DEPT', '4', '部门', '1', 12, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000005', '0001', 'XB', '2', '性别', '1', 6, '1', 'YS_CODE_XB', '          ', 2, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000006', '0001', 'SPELL', '1', '姓名简拼', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000007', '0001', 'NL', '1', '年龄', '6', 3, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000008', '0001', 'ZW', '2', '职务', '1', 3, '1', 'YS_CODE_ZW', '          ', 2, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000009', '0001', 'GS', '2', '归属', '1', 3, '1', 'YS_CODE_GS', '          ', 2, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000010', '0001', 'CODEORDER', '1', '排序号', '1', 3, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000011', '0001', 'PGH', '1', '工号', '1', 5, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000012', '0001', 'JG', '1', '籍贯', '1', 200, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000013', '0001', 'FKSZD', '1', '户口所在地', '1', 300, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000014', '0001', 'XL', '2', '第一学历', '1', 6, '1', 'YS_CODE_XL', '          ', 2, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000015', '0001', 'ZC', '2', '职称', '1', 50, '1', 'YS_CODE_ZC', '          ', 2, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000016', '0001', 'BLYS', '1', '毕业院校', '1', 200, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000017', '0001', 'ZL', '1', '专业', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000018', '0001', 'CJGZSJ', '1', '参加工作时间', '4', 0, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000019', '0001', 'JRBDWSJ', '1', '加入本单位时间', '4', 0, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000020', '0001', 'YGLX', '1', '用工类型', '1', 200, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000021', '0001', 'SJ', '1', '手机', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000022', '0001', 'JTDH', '1', '办公电话', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000023', '0001', 'DZYJ', '1', '电子邮件', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000024', '0001', 'JTZC', '1', '家庭住址', '1', 300, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000025', '0001', 'HTQS', '1', '开始合同期', '4', 0, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000026', '0001', 'HTQE', '1', '结束合同期', '4', 0, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000027', '0001', 'XL1', '2', '最高学历', '1', 6, '1', 'YS_CODE_XL', '          ', 2, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000028', '0001', 'BGDATE', '1', '本岗位时间', '4', 0, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000029', '0001', 'BLDATE', '1', '毕业时间', '4', 0, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000030', '0001', 'ISZZJY', '2', '是否在职教育', '1', 1, '1', 'YS_CODE_ISZZJY', '          ', 1, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000031', '0001', 'LXJ', '1', '年休假', '6', 2, '0', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000032', '0001', 'GWQK', '1', '岗位变动情况', '1', 2000, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000033', '0001', 'JYBJ', '1', '教育背景', '1', 2000, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000034', '0001', 'GZJL', '1', '工作简历', '1', 2000, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000035', '0001', 'SHGX', '1', '社会关系', '1', 2000, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000036', '0001', 'JCJL', '1', '奖惩记录', '1', 2000, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000037', '0001', 'ZWQK', '1', '职务情况', '1', 2000, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000038', '0001', 'PXJL', '1', '培训记录', '1', 2000, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000039', '0001', 'DBJL', '1', '担保记录', '1', 2000, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000040', '0001', 'HTQK', '1', '劳动合同签订情况', '1', 2000, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000041', '0001', 'SBQK', '1', '社保缴纳情况', '1', 2000, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000042', '0001', 'TJJL', '1', '体检记录', '1', 2000, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000043', '0001', 'BZ', '1', '备注', '1', 2000, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000044', '0001', 'ZZMM', '2', '政治面貌', '1', 10, '1', 'YS_CODE_ZZMM', '          ', 2, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000045', '0001', 'CSRQ', '1', '出生日期', '4', 0, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000046', '0001', 'PHOTO', '1', '照片', '3', 0, '1', null, '          ', 0, null, null, null, 0, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000047', '0001', 'MZ', '2', '民族', '1', 50, '1', 'YS_CODE_MZ', '          ', 2, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000048', '0001', 'SFZHM', '1', '身份证号码', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000049', '0001', 'HYZK', '2', '婚姻状况', '1', 6, '1', 'YS_CODE_HYZK', '          ', 2, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000050', '0002', 'CODE', '1', '编号', '1', 6, '0', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000051', '0002', 'NAME', '1', '名称', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000052', '0002', 'SPELL', '1', '拼音首字母', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000053', '0002', 'ISSHOW', '1', '是否禁用', '1', 1, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000113', '0012', 'CODE', '1', '编号', '1', 6, '0', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000114', '0012', 'NAME', '1', '名称', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000115', '0012', 'SPELL', '1', '拼音首字母', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000116', '0012', 'ISSHOW', '1', '是否禁用', '1', 1, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000117', '0013', 'CODE', '1', '编号', '1', 6, '0', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000118', '0013', 'NAME', '1', '名称', '1', 100, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000119', '0013', 'SPELL', '1', '拼音首字母', '1', 50, '1', null, '          ', 0, null, null, null, 100, null);";
			strSQL = strSQL
					+ "insert into BPIP_FIELD (FIELDID, TABLEID, FIELDNAME, FIELDTAG, CHINESENAME, FIELDTYPE, FIELDLENGTH, ISNULL, DICTTABLE, DESCRIPTION, TAGEXT, AUTO1, AUTO2, AUTO3, BLOBSIZE, QFIELD) values ('00000120', '0013', 'ISSHOW', '1', '是否禁用', '1', 1, '1', null, '          ', 0, null, null, null, 100, null);";

			dbengine.ExecuteSQLs(strSQL.split(";"));

			// 自定义表单--------------

			strSQL = "delete from YS_CODE_BIDMODALITY;";
			strSQL = strSQL
					+ "insert into YS_CODE_BIDMODALITY (CODE, NAME, SPELL, ISSHOW) values ('1', '公开招标', 'gkzb', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_BIDMODALITY (CODE, NAME, SPELL, ISSHOW) values ('2', '邀请招标', 'yqzb', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_BIDMODALITY (CODE, NAME, SPELL, ISSHOW) values ('3', '竞争性谈判采购', 'jzxtp', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_BIDMODALITY (CODE, NAME, SPELL, ISSHOW) values ('4', '询价采购', 'xj', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_BIDMODALITY (CODE, NAME, SPELL, ISSHOW) values ('5', '单一来源', 'dy', null);";

			strSQL = strSQL + "delete from YS_CODE_GS;";
			strSQL = strSQL + "insert into YS_CODE_GS (CODE, NAME, SPELL, ISSHOW) values ('01', '领导班子', 'LDBZ', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_GS (CODE, NAME, SPELL, ISSHOW) values ('02', '中层干部(分社)', 'ZCGBF', null);";
			strSQL = strSQL + "insert into YS_CODE_GS (CODE, NAME, SPELL, ISSHOW) values ('04', '副职', 'FZ', null);";
			strSQL = strSQL + "insert into YS_CODE_GS (CODE, NAME, SPELL, ISSHOW) values ('05', '主办会计', 'ZBKJ', null);";
			strSQL = strSQL + "insert into YS_CODE_GS (CODE, NAME, SPELL, ISSHOW) values ('06', '客户经理', 'KHJL', null);";
			strSQL = strSQL + "insert into YS_CODE_GS (CODE, NAME, SPELL, ISSHOW) values ('07', '综合柜员', 'ZHGY', null);";
			strSQL = strSQL + "insert into YS_CODE_GS (CODE, NAME, SPELL, ISSHOW) values ('08', '对公人员', 'DGRY', null);";
			strSQL = strSQL + "insert into YS_CODE_GS (CODE, NAME, SPELL, ISSHOW) values ('09', '工作人员', 'GZRY', null);";
			strSQL = strSQL + "insert into YS_CODE_GS (CODE, NAME, SPELL, ISSHOW) values ('10', '借用人员', 'JYRY', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_GS (CODE, NAME, SPELL, ISSHOW) values ('03', '中层干部(部门)', 'ZCGBB', null);";

			strSQL = strSQL + "delete from YS_CODE_HYZK;";
			strSQL = strSQL + "insert into YS_CODE_HYZK (CODE, NAME, SPELL, ISSHOW) values ('01', '未婚', 'wh', null);";
			strSQL = strSQL + "insert into YS_CODE_HYZK (CODE, NAME, SPELL, ISSHOW) values ('02', '已婚', 'yh', null);";
			strSQL = strSQL + "insert into YS_CODE_HYZK (CODE, NAME, SPELL, ISSHOW) values ('03', '离异', 'ly', null);";

			strSQL = strSQL + "delete from YS_CODE_ISZZJY;";
			strSQL = strSQL + "insert into YS_CODE_ISZZJY (CODE, NAME, SPELL, ISSHOW) values ('0', '否', null, null);";
			strSQL = strSQL + "insert into YS_CODE_ISZZJY (CODE, NAME, SPELL, ISSHOW) values ('1', '是', null, null);";

			strSQL = strSQL + "delete from YS_CODE_MONEYUNIT;";
			strSQL = strSQL
					+ "insert into YS_CODE_MONEYUNIT (CODE, NAME, SPELL, ISSHOW) values ('1', '万元', 'wy', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_MONEYUNIT (CODE, NAME, SPELL, ISSHOW) values ('2', '万美元', 'wmy', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_MONEYUNIT (CODE, NAME, SPELL, ISSHOW) values ('3', '万欧元', 'wwy', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_MONEYUNIT (CODE, NAME, SPELL, ISSHOW) values ('4', '万日元', 'wry', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_MONEYUNIT (CODE, NAME, SPELL, ISSHOW) values ('5', '万英磅', 'wyb', null);";

			strSQL = strSQL + "delete from YS_CODE_MZ;";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('21', '傈僳族', 'ssz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('22', '佤族', 'wz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('23', '畲族', 'sz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('26', '水族', 'sz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('27', '东乡族', 'dxz', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('30', '柯尔克孜族', 'kekzz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('32', '达斡尔族', 'dwez', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('33', '仫佬族', 'mlz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('34', '羌族', 'qz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('35', '布朗族', 'blz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('36', '撒拉族', 'slz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('37', '毛南族', 'mlz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('41', '普米族', 'pmz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('42', '塔吉克族', 'tjkz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('43', '怒族', 'nz', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('44', '乌孜别克族', 'mzbkz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('45', '俄罗斯族', 'elsz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('46', '鄂温克族', 'ewkz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('01', '汉族', 'hz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('02', '苗族', 'mz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('03', '布依族', 'byz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('04', '土家族', 'tjz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('05', '彝族', 'yz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('06', '侗族', 'dz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('07', '基诺族', 'jnz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('08', '蒙古族', 'mgz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('09', '回族', 'hz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('10', '藏族', 'zz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('11', '维吾尔族', 'wwez', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('12', '壮族', 'zz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('13', '朝鲜族', 'cxz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('14', '满族', 'mz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('15', '瑶族', 'yz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('16', '白族', 'bz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('17', '哈尼族', 'hnz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('18', '哈萨克族', 'hskz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('19', '傣族', 'dz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('20', '黎族', 'lz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('24', '高山族', 'gsz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('25', '拉祜族', 'lhz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('28', '纳西族', 'nxz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('29', '景颇族', 'jpz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('31', '土族', 'tz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('38', '仡佬族', 'ylz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('39', '锡伯族', 'xbz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('40', '阿昌族', 'acz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('47', '德昂族', 'daz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('48', '保安族', 'baz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('49', '裕固族', 'ygz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('50', '京族', 'jz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('51', '塔塔尔族', 'ttez', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('52', '独龙族', 'dlz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('53', '鄂伦春族', 'elcz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('54', '赫哲族', 'hzz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('55', '门巴族', 'mbz', null);";
			strSQL = strSQL + "insert into YS_CODE_MZ (CODE, NAME, SPELL, ISSHOW) values ('56', '珞巴族', 'lbz', null);";

			strSQL = strSQL + "delete from YS_CODE_XB;";
			strSQL = strSQL + "insert into YS_CODE_XB (CODE, NAME, SPELL, ISSHOW) values ('1', '男', null, '0');";
			strSQL = strSQL + "insert into YS_CODE_XB (CODE, NAME, SPELL, ISSHOW) values ('2', '女', null, '0');";

			strSQL = strSQL + "delete from YS_CODE_XL;";
			strSQL = strSQL + "insert into YS_CODE_XL (CODE, NAME, SPELL, ISSHOW) values ('11', '大学', 'dx', null);";
			strSQL = strSQL + "insert into YS_CODE_XL (CODE, NAME, SPELL, ISSHOW) values ('01', '小学', 'xx', null);";
			strSQL = strSQL + "insert into YS_CODE_XL (CODE, NAME, SPELL, ISSHOW) values ('02', '初中', 'cz', null);";
			strSQL = strSQL + "insert into YS_CODE_XL (CODE, NAME, SPELL, ISSHOW) values ('03', '高中', 'gz', null);";
			strSQL = strSQL + "insert into YS_CODE_XL (CODE, NAME, SPELL, ISSHOW) values ('04', '中专', 'zz', null);";
			strSQL = strSQL + "insert into YS_CODE_XL (CODE, NAME, SPELL, ISSHOW) values ('05', '大专', 'dz', null);";
			strSQL = strSQL + "insert into YS_CODE_XL (CODE, NAME, SPELL, ISSHOW) values ('06', '本科', 'bk', null);";
			strSQL = strSQL + "insert into YS_CODE_XL (CODE, NAME, SPELL, ISSHOW) values ('08', '硕士', 'ss', null);";
			strSQL = strSQL + "insert into YS_CODE_XL (CODE, NAME, SPELL, ISSHOW) values ('09', '博士', 'bs', null);";
			strSQL = strSQL + "insert into YS_CODE_XL (CODE, NAME, SPELL, ISSHOW) values ('10', '博士后', 'bsh', null);";
			strSQL = strSQL + "insert into YS_CODE_XL (CODE, NAME, SPELL, ISSHOW) values ('07', '研究生', 'yjs', null);";
			strSQL = strSQL + "insert into YS_CODE_XL (CODE, NAME, SPELL, ISSHOW) values ('12', '其它', null, null);";

			strSQL = strSQL + "delete from YS_CODE_ZC;";
			strSQL = strSQL + "insert into YS_CODE_ZC (CODE, NAME, SPELL, ISSHOW) values ('04', '会计员', 'kjy', null);";
			strSQL = strSQL + "insert into YS_CODE_ZC (CODE, NAME, SPELL, ISSHOW) values ('05', '经济师', 'jjs', null);";
			strSQL = strSQL + "insert into YS_CODE_ZC (CODE, NAME, SPELL, ISSHOW) values ('01', '会计师', 'kjs', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_ZC (CODE, NAME, SPELL, ISSHOW) values ('02', '助理政工师', 'zlzgs', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_ZC (CODE, NAME, SPELL, ISSHOW) values ('03', '助理会计师', 'zlkjs', null);";
			strSQL = strSQL + "insert into YS_CODE_ZC (CODE, NAME, SPELL, ISSHOW) values ('06', '经济员', 'jjy', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_ZC (CODE, NAME, SPELL, ISSHOW) values ('07', '助理经济师', 'zljjs', null);";

			strSQL = strSQL + "delete from YS_CODE_ZW;";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('21', '经理', 'jl', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('18', '分社主任', 'fszr', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('19', '试用期', 'syq', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('01', '理事长', 'LSZ', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('02', '监事长', 'JSZ', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('03', '主任', 'ZR', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('04', '副主任', 'FZR', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('05', '主任助理', 'ZRZL', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('06', '总经理', 'ZJL', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('07', '客户经理', 'KHJL', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('08', '副总经理', 'FZJL', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('09', '会计主管', 'KJZG', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('10', '主办会计', 'ZBKJ', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('11', '综合柜员', 'ZHGY', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('12', '对公人员', 'DGRY', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('13', '工作人员', 'GZRY', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('14', '解款员', 'jky', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('15', '对公柜', 'dgg', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('16', '信贷员', 'xdy', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('17', '内退', 'nt', null);";
			strSQL = strSQL + "insert into YS_CODE_ZW (CODE, NAME, SPELL, ISSHOW) values ('20', '借用', 'jy', null);";

			strSQL = strSQL + "delete from YS_CODE_ZZMM;";
			strSQL = strSQL + "insert into YS_CODE_ZZMM (CODE, NAME, SPELL, ISSHOW) values ('04', '群众', 'qz', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_ZZMM (CODE, NAME, SPELL, ISSHOW) values ('01', '中共党员', 'zgdy', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_ZZMM (CODE, NAME, SPELL, ISSHOW) values ('02', '预备党员', 'ybdy', null);";
			strSQL = strSQL
					+ "insert into YS_CODE_ZZMM (CODE, NAME, SPELL, ISSHOW) values ('03', '共青团员', 'gqty', null);";

			dbengine.ExecuteSQLs(strSQL.split(";"));

			strSQL = "delete from YS_HQ_PERSON ;insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010117', '5201210000000010', '张三', '020000000000', '1', '1988-08-08', '03', '520114199008110439', null, '贵州省贵阳市 ', '小河区王宽村', '05', null, '贵州财经学院', '金融保险', '1988-08-08', '1988-08-08', null, '15185111456', '3762055', null, '小河区王宽村', null, null, '5', '04', 'jrx', '11', '07', 21, null, '00194', '05', '1988-08-08', '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010127', '5201180000000011', '李四', '020000000000', '2', '1988-08-08', '01', '520103198801063267', null, null, null, '06', null, '贵州财经学院', null, null, '1988-08-08', null, '13765138786', null, null, null, null, null, '5', '04', 'lqq', null, '07', 24, null, '00188', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010130', '5201190000000002', '王五', '020000000000', '2', '1988-08-08', '01', '520103196907274020', null, '山东荷泽', '花溪贵筑派出所', '03', null, '贵州大学', '金融学专业', '1988-08-08', '1988-08-08', '合同工', '13885015844', null, null, '花溪徐家冲社区', '1988-08-08', '1988-08-08', '5', '04', 'zsm', '10', '05', 43, null, '00125', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010136', '5201200000000001', '赵六', '020000000000', '2', '1988-08-08', '01', '520103196311300021', null, '湖南祁阳', '云岩分局', '04', null, null, null, '1988-08-08', '1988-08-08', '合同工', '13595111350', '8690599', null, '云岩太平路15号', '1988-08-08', '1988-08-08', '15', '04', 'zj', '03', '02', 48, null, '00130', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010139', '5201200000000007', '孙七', '020000000000', '2', '1988-08-08', '01', '522101197404061625', null, '江西萍乡', '遵义红花岗分局', '05', null, '贵州省党校', '经济', '1988-08-08', '1988-08-08', '合同工', '13985199893', '8690399', null, '小河红河路丽景湾', '1988-08-08', '1988-08-08', '15', '04', 'hjt', '16', '06', 38, null, '00136', '05', null, '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010146', '5201210000000009', '李八', '020000000000', '2', '1988-08-08', '01', '520103198012213226', null, '贵州', '贵阳市云岩区', '05', null, '贵州工业大学', '工商管理', '1988-08-08', '1988-08-08', null, '13639144636', '3762055', null, '小河区', null, '1988-08-08', '5', '04', 'hk', '06', '06', 31, null, '00139', '03', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010150', '5201070000000009', '陈九', '020000000000', '2', '1988-08-08', '04', '522227196503310046', null, '贵州德江', '贵州省德江县', '05', null, '贵州省党校', '法律', '1988-08-08', '1988-08-08', '合同工', '15285180598', '3803713', null, '贵阳市白云区天林花园', '1988-08-08', '1988-08-08', '15', '04', 'yf', '13', '09', 47, null, '00154', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010153', '5201090000000002', '张小海', '020000000000', '1', '1988-08-08', '01', '520102197104092010', null, '山东商河', '南明河滨派出所', '06', null, '贵州大学', '金融学', '1988-08-08', '1988-08-08', '合同工', '13985116370', null, null, '小河明彩居', '1988-08-08', '1988-08-08', '15', '01', 'xh', '08', '03', 41, null, '00035', '06', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010157', '5201110000000014', '李一落', '020000000000', '2', '1988-08-08', '01', '520203198706291128', null, null, null, '06', null, '贵州财经学院', null, null, '1988-08-08', null, '13765844672', '3833147', null, null, null, null, '5', '04', 'xw', '11', '08', 25, null, '00170', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010158', '5201160000000011', '张二璟', '020000000000', '2', '1988-08-08', '01', '522101198710124449', null, '贵州遵义市', '遵义红花岗', '06', null, '贵州大学', '金融', '1988-08-08', '1988-08-08', null, '13765082354', null, null, '遵义红花岗', null, null, '5', '04', 'zj', '11', '07', 24, null, '00185', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010121', '5201140000000001', '蒋一华', '020000000000', '2', '1988-08-08', '01', '522501197009211226', null, '山东济南', '小河黄河路派出所', '06', null, '贵州财经学院', '金融', '1988-08-08', '1988-08-08', '合同工', '13639045578', '3896772', null, '小河中兴世家', '1988-08-08', '1988-08-08', '10', '04', 'jh', '03', '02', 41, null, '00081', '06', '1988-08-08', '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010003', '5201010000000001', '张林', '020000000000', '1', '1988-08-08', '01', '522125196312240016', null, '贵州正安', '南明分局', '07', '01', null, null, '1988-08-08', '1988-08-08', '合同工', '13908515005', '3807799', null, '南明红岩路34号', '1988-08-08', '1988-08-08', '15', '01', 'kcl', '01', '01', 48, null, '00001', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010069', '5201150000000009', '小微', '020000000000', '2', '1988-08-08', '38', '522501198802115840', null, null, null, '06', null, '中央民族大学', '国际经济与贸易', null, null, null, null, '5160969', null, null, null, '1988-08-08', '0', '04', 'yxw', null, '07', 24, null, '00161', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010178', '5201120000000011', '徐婕', '020000000000', '2', '1988-08-08', '01', '522101198702274025', null, '贵州省遵义市', null, '06', null, '贵州财经学院', null, null, null, null, '13765076274', null, null, null, null, null, '5', '04', 'gj', null, '07', 25, null, '00175', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010033', '5201130000000004', '陈不二', '020000000000', '1', '1988-08-08', '03', '520111197011260616', null, '贵州花溪', '金竹镇派出所', '06', null, '贵州大学', '金融', '1988-08-08', '1988-08-08', '合同工', '13984412518', '3909098', null, '金竹镇竹林村', '1988-08-08', '1988-08-08', '10', '01', 'cg', '16', '06', 41, null, '00141', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010064', '5201140000000003', '吴三琼', '020000000000', '2', '1988-08-08', '01', '522523197405190066', null, '贵州息烽', '花果园派出所', '06', null, '北京商务学院', '金融', '1988-08-08', '1988-08-08', '合同工', '13638513717', '3895777', null, '南明狮峰路2号', '1988-08-08', '1988-08-08', '10', '04', 'wq', '11', '07', 38, null, '00083', '06', null, '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010068', '5201150000000007', '石江二', '020000000000', '1', '1988-08-08', '01', '522523661015001', null, '贵州修文', '高新派出所', '04', null, '贵州省委党校', '法律专业', '1988-08-08', '1988-08-08', '合同工', '13985130552', '5160079', null, '乌当温泉路东新苑', '1988-08-08', '1988-08-08', '15', '04', 'sj', '16', '06', 45, null, '00023', '05', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010072', '5201160000000004', '吴国峰', '020000000000', '1', '1988-08-08', '03', '520111196912121512', null, '贵州贵阳', '花溪贵筑派出所', '06', '04', '贵州大学', '金融', '1988-08-08', '1988-08-08', '合同工', '13985422132', '3716280', null, '花溪徐家冲社区', '1988-08-08', '1988-08-08', '15', '01', 'wyf', '16', '06', 42, null, '00099', '06', null, '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010126', '5201180000000007', '董萍平', '020000000000', '2', '1988-08-08', '01', '520103196708194028', null, '山西平定', '市西派出所', '11', '05', '贵阳市金筑大学', null, '1988-08-08', '1988-08-08', '合同工', '13985110532', null, null, '云岩区新建路38号', '1988-08-08', '1988-08-08', '15', '04', 'dzp', '15', '08', 44, null, '00051', null, '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010138', '5201200000000006', '张琨', '020000000000', '2', '1988-08-08', '01', '520103198502023628', null, '天津', '环城北路派出所', '05', null, '贵阳学院', '英语教育', '1988-08-08', '1988-08-08', '合同工', '13765144733', '8690599', null, '贵惠路116号', '1988-08-08', '1988-08-08', '5', '04', 'ylk', '11', '07', 27, null, '00135', null, null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010162', '5201180000000004', '林娜', '020000000000', '2', '1988-08-08', null, '520103196311241287', null, null, '西湖路派出所', '05', null, '贵州工学院', '会计', '1988-08-08', '1988-08-08', null, '13985155956', null, null, '南明西湖路26号', '1988-08-08', '1988-08-08', '5', '04', 'wln', '11', '07', 48, null, '00116', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010172', '5201220000000004', '赵素', '020000000000', '2', '1988-08-08', '01', '52212319830705002X', null, '贵州贵阳', '百花山派出所', '06', null, null, null, '1988-08-08', '1988-08-08', '合同工', '13809435566', null, null, '小河三江小区', '1988-08-08', '1988-08-08', '5', '04', 'zy', '20', '10', 29, null, '00048', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010179', '5201140000000004', '田三三', '020000000000', '2', '1988-08-08', '01', '522123198207151106', null, '贵州绥阳', '新华路派出所', '06', null, '北京商务学院', '金融', '1988-08-08', '1988-08-08', '合同工', '18984855099', '3895777', null, '小河楠竹花园', '1988-08-08', '1988-08-08', '5', '04', 'trs', '11', '07', 30, null, '00085', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010183', '5201150000000006', '李红梅', '020000000000', '2', '1988-08-08', '03', '520102196909295021', null, '贵州贵阳', '小河平桥派出所', '05', null, '贵阳金筑大学', '金融专业', '1988-08-08', '1988-08-08', '合同工', '13688510365', '5160969', null, '小河三江小区', '1988-08-08', '1988-08-08', '5', '04', 'yhm', '11', '07', 42, null, '00094', '05', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010166', '5201130000000010', '张骏', '020000000000', '1', '1988-08-08', '01', '310107197909240458', null, null, null, '05', null, '上海市经济管理学院', null, null, null, null, '13885026424', null, null, null, null, null, '5', '04', 'lj', '11', '07', 32, null, '00177', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010044', '5201120000000009', '任四玲', '020000000000', '2', '1988-08-08', '04', '520201198506056075', null, '贵州省印江县', null, '06', null, '贵州大学', null, null, null, null, '13765024341', '8598720/8598721', null, null, null, null, '5', '04', 'rl', '07', '07', 27, null, '00165', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010038', '5201130000000008', '李娇', '020000000000', '2', '1988-08-08', '04', '522222198508280023', null, null, null, '06', null, '贵阳学院', '法学', null, null, null, '15180829607', null, null, null, null, '1988-08-08', '5', '01', 'tj', null, '07', 26, null, '00062', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010056', '5201010000000007', '杨五婷', '020000000000', '2', '1988-08-08', '02', '522601197402160029', null, '贵州天柱', '大营坡派出所', '06', null, null, null, '1988-08-08', '1988-08-08', '合同工', '13885123111', '3802358', null, '中天花园', '1988-08-08', '1988-08-08', '10', '01', 'ytt', '05', '01', 38, null, '00007', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010061', '5201010000000008', '李小炫', '020000000000', '1', '1988-08-08', '01', '520103197407091214', null, '内蒙通辽', '黔灵东路派出所', '05', null, null, null, '1988-08-08', '1988-08-08', '合同工', '13511986777', '3802378', null, '梭草路虹桥小区', '1988-08-08', '1988-08-08', '10', '04', 'zx', '05', '01', 38, null, '00008', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010104', '5201130000000007', '张和菊', '020000000000', '2', '1988-08-08', '01', '522724197309126029', null, '贵州福泉', '西湖路派出所', '06', null, '江西财经大学', '会计学', '1988-08-08', '1988-08-08', '合同工', '13984340234', '3909192', null, '官井路35号', '1988-08-08', '1988-08-08', '10', '04', 'cwj', '11', '07', 38, null, '00076', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010015', '5201070000000006', '李茜', '020000000000', '2', '1988-08-08', '01', '520102198204121629', null, '重庆', '神奇路派出所', '04', null, '贵州广播电视大学', '法律', '1988-08-08', '1988-08-08', '合同工', '18984844942', '3803713', null, '神奇路40号', '1988-08-08', '1988-08-08', '5', '04', 'dq', '13', '09', 30, null, '00049', '05', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010152', '5201080000000006', '李玉', '020000000000', '2', '1988-08-08', '01', '522101198006064825', null, '贵州遵义', '花溪分局', '06', null, '贵州大学法学院', '宪法学与行政法学', '1988-08-08', '1988-08-08', '合同工', '15885501216', '3802278', null, '小河中曹路', '1988-08-08', '1988-08-08', '5', '01', 'yfy', '13', '09', 32, null, '00061', '07', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010016', '5201080000000003', '王李爽', '020000000000', '1', '1988-08-08', '09', '522221198603051230', null, '贵州铜仁', '铜仁市凉水井派出所', '06', null, '青岛科技大学', '工业设计', '1988-08-08', '1988-08-08', '合同工', '15286006354', '3802278', null, '南明山水黔城', '1988-08-08', '1988-08-08', '5', '04', 'ws', '13', '09', 26, null, '00163', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010020', '5201100000000002', '吴李洁', '020000000000', '2', '1988-08-08', '01', '522101197903162420', null, '江苏镇江', '盐吉派出所', '06', null, '贵阳财经学院', '金融管理', '1988-08-08', '1988-08-08', '合同工', '13985446955', '3802350', null, '中华北路18号', '1988-08-08', '1988-08-08', '5', '04', 'wj', '13', '09', 33, null, '00066', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010050', '5201110000000002', '张李郁', '020000000000', '2', '1988-08-08', '01', '520102197411233047', null, '内蒙兴和', '朝阳派出所', '04', null, '贵州大学', '金融学', '1988-08-08', '1988-08-08', '合同工', '13985136992', '8598720', null, '南明飞行街105号', '1988-08-08', '1988-08-08', '10', '04', 'zy', '18', '02', 37, null, '00067', '05', '1988-08-08', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010098', '5201110000000007', '李小川', '020000000000', '1', '1988-08-08', '01', '520114198007280415', null, '贵州贵阳', '小河平桥派出所', '03', null, '郑州航空工业学院', '工商行政管理', '1988-08-08', '1988-08-08', '合同工', '13885172533', '3802197', null, '小河大寨村', '1988-08-08', '1988-08-08', '10', '04', 'lc', '16', '06', 32, null, '00071', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010022', '5201110000000006', '李盛', '020000000000', '1', '1988-08-08', '03', '52011319660705001X', null, '贵州贵阳', '市西派出所', '04', '03', '云南大学', '会计学', '1988-08-08', '1988-08-08', '合同工', '13312231123', '3802197', null, '瑞金中路80号', '1988-08-08', '1988-08-08', '15', '01', 'lds', '16', '06', 46, null, '00114', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010025', '5201110000000013', '李刘鑫', '020000000000', '1', '1988-08-08', '01', '371321198705022111', null, null, null, '06', null, '贵州财经学院', '金融学', null, '1988-08-08', null, '13639079652', '3802197', null, null, null, '1988-08-08', '5', '04', 'lx', '11', '08', 25, null, '00149', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010001', '5201010000000002', '李琴', '020000000000', '2', '1988-08-08', '03', '520103611026482', null, '贵州荔波', '环城东路派出所', '06', '02', null, null, '1988-08-08', '1988-08-08', '合同工', '13985044743', '3802397,3802377', null, '环城东路', '1988-08-08', '1988-08-08', '15', '01', 'myq', '02', '01', 50, null, '00003', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010099', '5201010000000003', '陈敏', '020000000000', '2', '1988-08-08', '03', '52011119630919002X', null, '贵州花溪', '花溪清溪路派出所', '06', '03', null, null, '1988-08-08', '1988-08-08', '合同工', '13985497399', '3802380', null, '小河蒲江路', '1988-08-08', '1988-08-08', '15', '01', 'cxj', '04', '01', 48, null, '00004', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010004', '5201010000000004', '许敏', '020000000000', '2', '1988-08-08', '01', '654001197707240722', null, '山东莱阳', '南明分局', '06', null, null, null, '1988-08-08', '1988-08-08', '合同工', '13809430198', '3805500', null, '白马井路', '1988-08-08', '1988-08-08', '10', '01', 'xm', '04', '01', 35, null, '00009', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010053', '5201020000000001', '敏晓洪', '020000000000', '1', '1988-08-08', '01', '52010219710603245X', null, '江西樟树', '南明区花溪大道北240号', null, null, '中共中央学校', '法律', '1988-08-08', '1988-08-08', '合同工', '13809425555', '3802280', null, '花溪大道北段240号', '1988-08-08', '1988-08-08', '15', '01', 'hxh', '03', '03', 41, null, '00011', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010024', '5201110000000012', '敏兰', '020000000000', '2', '1988-08-08', '01', '522131198408170821', null, null, null, '06', null, '贵州大学', '国际经济与贸易', null, '1988-08-08', null, '13511974748', '3833147', null, null, null, '1988-08-08', '5', '04', 'zl', '11', '08', 27, null, '00063', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010089', '5201110000000004', '敏吉', '020000000000', '2', '1988-08-08', '01', '520102198112192040', null, '贵州贵阳', '花溪溪北派出所', '05', null, '贵州大学', '法律', '1988-08-08', '1988-08-08', '合同工', '13985038379', '3833147', null, '花溪圆亭路36号', '1988-08-08', '1988-08-08', '5', '04', 'cj', '15', '08', 30, null, '00110', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010156', '5201110000000009', '敏曾黎', '020000000000', '2', '1988-08-08', '01', '520102197809282025', null, '贵州贵阳', '贵阳河滨派出所', '06', null, '贵州财经学院', '金融学', '1988-08-08', '1988-08-08', '合同工', '13985176161', '3833147', null, '安云路建工巷20号', '1988-08-08', '1988-08-08', '10', '04', 'zl', '15', '08', 33, null, '00115', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010037', '5201110000000011', '敏丽云', '020000000000', '2', '1988-08-08', '01', '520114198608190028', null, null, null, '06', null, '大连大学', '会计学', null, '1988-08-08', null, '13608521019', '3802197', null, null, null, '1988-08-08', '5', '04', 'rly', '11', '08', 25, null, '00158', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010026', '5201110000000015', '丽雅娟', '020000000000', '2', '1988-08-08', '01', '520103198801220445', null, null, null, '06', null, '贵州大学', null, null, '1988-08-08', null, '13984871367', null, null, null, null, null, '0', '04', 'wyj', '11', '08', 24, null, '00171', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010027', '5201110000000017', '丽睿婕', '020000000000', '2', '1988-08-08', '01', '522701198706260328', null, null, null, '06', null, '南京炮兵学院', null, null, '1988-08-08', null, '18798095149', '3833147', null, null, null, null, '5', '04', 'yrj', '11', '08', 25, null, '00173', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010177', '5201120000000001', '敏审环', '020000000000', '1', '1988-08-08', '14', '520111198203300015', null, '黑龙江绥化', '花溪桥南派出所', '06', null, '贵州民族大学', '法学', '1988-08-08', '1988-08-08', '合同工', '13688503335', '8598722', null, '花溪望哨坡巷9号', '1988-08-08', '1988-08-08', '5', '04', 'lsh', '21', '03', 30, null, '00041', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010174', '5201120000000004', '代红代', '020000000000', '1', '1988-08-08', '01', '520114197506220017', null, '贵州贵阳', '小河黄河路派出所', '06', null, '贵州大学', '金融学', '1988-08-08', '1988-08-08', '合同工', '13595025355', '8598720/8598721', null, '小河三江小区', '1988-08-08', '1988-08-08', '10', '04', 'dhx', '07', '06', 37, null, '00070', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010090', '5201120000000005', '刘代', '020000000000', '2', '1988-08-08', '02', '522723196510100225', null, '贵州贵定', '黔灵东路派出所', '06', null, '云南大学', '金融学', '1988-08-08', '1988-08-08', '合同工', '13985033900', null, null, '黔灵东路19号', '1988-08-08', '1988-08-08', '15', '01', 'lf', '15', '08', 46, null, '00077', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010028', '5201120000000003', '李代', '020000000000', '2', '1988-08-08', '01', '522227198107290046', null, null, '贵阳市月亮岩路182号', '06', null, '贵州大学', '法学', '1988-08-08', '1988-08-08', null, '15085911711', null, null, '贵阳市月亮岩路182号', '1988-08-08', '1988-08-08', '5', '01', 'lm', '07', '06', 31, null, '00120', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010009', '5201030000000001', '代敏', '020000000000', '2', '1988-08-08', '01', '520102197412210824', null, '贵州贵阳', '大南派出所', '04', null, '贵州广播电视大学', '会计', '1988-08-08', '1988-08-08', '合同工', '13639007007', '3802398', null, '小河瑞祥居', '1988-08-08', '1988-08-08', '10', '04', 'fm', '21', '03', 37, null, '00019', '03', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010010', '5201030000000004', '代先凤', '020000000000', '2', '1988-08-08', '01', '522701196606110323', null, '湖南', '贵阳南明', '03', '05', '贵州借据管理干部学院', '金融', '1988-08-08', '1988-08-08', '合同工', '15186980515', '3801927', null, '南明区山水黔城', null, null, '15', '04', 'zxf', '13', '09', 46, null, '00155', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010011', '5201040000000001', '代润英', '020000000000', '2', '1988-08-08', '01', '522524196606161247', null, '贵州修文', '云岩分局', '05', '05', '金筑大学', '财会电算化', '1988-08-08', '1988-08-08', '合同工', '13885171899', '3802368', null, '云岩半边街28号', '1988-08-08', '1988-08-08', '10', '04', 'lry', '21', '03', 46, null, '00025', '05', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010169', '5201040000000004', '代翔宇', '020000000000', '1', '1988-08-08', '01', '522124198406060035', null, '贵州正安', '正安县公安局', '06', null, '贵州大学', '计算机科学与技术', '1988-08-08', '1988-08-08', '合同工', '13765139328', null, null, '贵阳市小河区兴隆苑', null, null, '5', '01', 'wxy', '13', '09', 28, null, '00169', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010087', '5201050000000003', '代丽琨', '020000000000', '2', '1988-08-08', '01', '52210119720303360X', '02', '贵州遵义', '小河黄河路派出所', '04', null, '昆明陆军学院', '思想政治专业', '1988-08-08', '1988-08-08', '合同工', '18685157169', '3803790', null, '小河十里江南', '1988-08-08', '1988-08-08', '10', '04', 'zlk', '13', '09', 40, null, '00068', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010063', '5201060000000003', '刘信', '020000000000', '1', '1988-08-08', '01', '520103198109262016', null, '贵州兴仁', '延中派出所', '06', null, '贵州大学', '计算机科学与技术', '1988-08-08', '1988-08-08', '合同工', '13984338411', '3802328', null, '云岩交易西巷16号', '1988-08-08', '1988-08-08', '5', '04', 'lx', '13', '09', 30, null, '00031', '06', '1988-08-08', '1988-08-08', '0', 'AAA', null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010114', '5201060000000004', '刘梅', '020000000000', '2', '1988-08-08', '01', '522424198708041426', null, '贵州毕节', '贵州省金沙县', '06', null, '中国民航大学', '计算机科学与技术', '1988-08-08', '1988-08-08', null, '13595063134', '3802308', null, '贵州省金沙县', null, '1988-08-08', '5', '01', 'wlm', '13', '09', 25, null, '00160', '07', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010149', '5201070000000004', '刘娟', '020000000000', '2', '1988-08-08', '01', '520103198501144022', null, '河南宁陵', '市西派出所', '06', null, '贵州大学', '会计', '1988-08-08', '1988-08-08', '合同工', '13984074326', '3802508', null, '云岩桂月巷15号', '1988-08-08', '1988-08-08', '5', '04', 'hlj', '13', '09', 27, null, '00119', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010041', '5201070000000003', '刘蓉', '020000000000', '2', '1988-08-08', '01', '520102196701043044', null, '贵州贵阳', '贵乌派出所', '04', null, '贵州省银行学校', '农村金融', '1988-08-08', '1988-08-08', '合同工', '13985020200', '3802508', null, '贵开路银海嘉怡花园', '1988-08-08', '1988-08-08', '10', '04', 'zr', '13', '09', 45, null, '00123', '04', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010151', '5201080000000004', '王刘慧', '020000000000', '2', '1988-08-08', '01', '522424196105130025', null, '贵州金沙', '南明后巢乡派出所', '04', '03', '贵州广播电视大学遵义分校', '金融', '1988-08-08', '1988-08-08', '合同工', '13595119468', '3802278', null, '山水黔城', '1988-08-08', '1988-08-08', '15', '01', 'wch', '13', '09', 51, null, '00039', '06', '1988-08-08', '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010088', '5201080000000002', '熊刘艳', '020000000000', '2', '1988-08-08', '01', '512326198107126745', null, '重庆武隆', '重庆沙坪坝松林坡派出所', '08', null, '重庆大学发法学院', '民商法', '1988-08-08', '1988-08-08', '合同工', '18685129773', '3802272', null, '小河香江花园', '1988-08-08', '1988-08-08', '5', '01', 'xyy', '08', '04', 31, null, '00056', '08', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010018', '5201090000000001', '陈刘', '020000000000', '1', '1988-08-08', '01', '520111631230061', null, '贵州贵阳', '花溪溪北派出所', '06', '02', '贵州广播电视大学', '法学', '1988-08-08', '1988-08-08', '合同工', '13595143222', '3802218', null, '花溪清溪路110号', '1988-08-08', '1988-08-08', '15', '01', 'cg', '06', '03', 48, null, '00042', '06', null, '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010019', '5201090000000004', '罗刘龙', '020000000000', '1', '1988-08-08', '03', '520111196809060619', null, '贵州花溪', '金竹镇派出所', null, '04', '贵州工业机械职工大学', '金融管理', '1988-08-08', '1988-08-08', '合同工', '13985521132', '3802201', null, '花溪松涛路85号', '1988-08-08', '1988-08-08', '15', '04', 'lhl', '14', '09', 43, null, '00044', '05', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010154', '5201100000000003', '刘蓉', '020000000000', '2', '1988-08-08', '01', '520102197411286624', null, '贵州沿河', '小河黄河路派出所', null, null, '贵州职工大学', '金融会计', '1988-08-08', '1988-08-08', '合同工', '18908501128', '3802350', null, '小河麦兆小区', '1988-08-08', '1988-08-08', '10', '04', 'dr', '13', '09', 37, null, '00020', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010054', '5201100000000001', '刘红', '020000000000', '2', '1988-08-08', '01', '522522198308260024', null, '贵州开阳', '开阳县城关派出所', '06', null, '贵州省民族学院', '财会电算化', '1988-08-08', '1988-08-08', '合同工', '13984074233', '3802528', null, '小河兴隆花园', '1988-08-08', '1988-08-08', '5', '04', 'wh', '21', '03', 28, null, '00046', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010155', '5201110000000005', '刘鹏', '020000000000', '1', '1988-08-08', '01', '520102197703163810', null, '贵州湄潭', '沙冲路派出所', '05', null, '贵州财经学院', '银行学', '1988-08-08', '1988-08-08', '合同工', '13985116364', '3802197', null, '小河麦兆小区', '1988-08-08', '1988-08-08', '10', '04', 'lp', '16', '06', 35, null, '00021', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010005', '5201010000000005', '刘庆雷', '020000000000', '1', '1988-08-08', '01', '522725197309190019', null, '贵州瓮安', '西湖路派出所', '07', null, null, null, '1988-08-08', null, '合同工', '13985177916', '3802309', null, '观水路55号', '1988-08-08', '1988-08-08', '10', '04', 'yql', '05', '01', 38, null, '00006', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010006', '5201020000000002', '吴刘', '020000000000', '1', '1988-08-08', '01', '520111196412134819', null, '重庆綦江', '小河黄河路派出所', '04', null, '空军第一工程学校、贵州大学', '采暖通风、园林艺术与设计', '1988-08-08', '1988-08-08', '合同工', '13639086085', '3802373', null, '小河锦江路195号', '1988-08-08', '1988-08-08', '15', '04', 'wj', '11', '09', 47, null, '00012', '05', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010007', '5201020000000004', '刘玮', '020000000000', '1', '1988-08-08', '01', '520103198608244031', null, '贵州贵阳', '市西派出所', '06', null, '南昌大学', '汉语言文学', '1988-08-08', '1988-08-08', '合同工', '18785108824', '3802318', null, '贵阳市小河区香江路8号香江花园2-3-2', '1988-08-08', '1988-08-08', '5', '01', 'jyw', '13', '09', 25, null, '00016', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010100', '5201020000000005', '张兴', '020000000000', '1', '1988-08-08', '01', '520103197502250412', null, '贵州遵义', '中山东路派出所', '03', null, ' 金筑大学', '财务电算化', '1988-08-08', '1988-08-08', '合同工', '13608527388', '3802318', null, '贵阳市小河区江南苑C17-222', '1988-08-08', '1988-08-08', '10', '01', 'lx', '13', '09', 37, null, '00017', '05', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010164', '5201200000000003', '刘啟红', '020000000000', '2', '1988-08-08', '01', '520103196907063223', null, '贵州晴隆', '云岩头桥派出所', '05', null, '贵阳市金筑大学', '中文', '1988-08-08', '1988-08-08', '合同工', '13608525648', '8690399', null, '小河香江花园', '1988-08-08', '1988-08-08', '15', '01', 'mqh', '16', '06', 43, null, '00132', '05', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010173', '5201220000000005', '魏魏娇', '020000000000', '2', '1988-08-08', '01', '522501198505112846', null, '辽宁辽阳', '安顺西秀公安局', '06', null, null, null, '1988-08-08', '1988-08-08', '合同工', '13885074802', null, null, '小河黄河路97号', '1988-08-08', '1988-08-08', '5', '04', 'wj', '20', '10', 27, null, '00054', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010170', '5201220000000002', '魏明新', '020000000000', '1', '1988-08-08', '01', '520113197511130837', null, '湖北鄂城', '小河黄河路派出所', '06', null, null, null, '1988-08-08', '1988-08-08', '合同工', '13312261616', null, null, '小河香江路16号', '1988-08-08', '1988-08-08', '10', '04', 'pmx', '20', '10', 36, null, '00028', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010180', '5201140000000008', '穆魏娟', '020000000000', '2', '1988-08-08', '01', '522132198604024967', null, '贵州习水', '贵阳云岩', '06', null, '贵州大学', '金融学', null, '1988-08-08', null, '15985109711', '3896772', null, '贵州省遵义市习水县温水镇群英村', null, null, '5', '01', 'myj', '07', '06', 26, null, '00096', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010043', '5201110000000010', '钱魏', '020000000000', '2', '1988-08-08', '01', '520102198112192040', null, '贵州贵阳', null, '06', null, '贵州大学成人教育学院', '计算机科学与技术', null, null, null, '13885170117', '3833147', null, null, null, null, '5', '01', 'qh', '15', '08', 30, null, '00072', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010057', '5201130000000001', '魏鲲', '020000000000', '1', '1988-08-08', '04', '522228197811040018', null, '贵州德江', '花果园派出所', '05', null, '铜仁师专', '物理系', '1988-08-08', '1988-08-08', '合同工', '13595104792', '3909098', null, '花溪大道北段346号', '1988-08-08', '1988-08-08', '10', '01', 'tk', '03', '02', 33, null, '00121', '05', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010091', '5201130000000009', '南南', '020000000000', '1', '1988-08-08', '01', '130429198704263459', null, null, null, '06', null, '贵州大学', '新闻学', null, null, null, '13639079791', '3909098', null, null, null, null, '5', '04', 'znn', '11', '07', 25, null, '00157', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010035', '5201130000000011', '南君', '020000000000', '2', '1988-08-08', '01', '520103198508242821', null, null, null, '06', null, '贵州财经学院', null, null, null, null, '13511949910', null, null, null, null, null, '5', '04', 'sj', '11', '07', 26, null, '00178', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010065', '5201140000000005', '樊樊蓉', '020000000000', '2', '1988-08-08', '01', '520103197705286044', null, '四川安岳', '北京路派出所', '05', null, '贵州大学', '会计电算化', '1988-08-08', '1988-08-08', '合同工', '13511934088', '3895777', null, '北京路24号', '1988-08-08', '1988-08-08', '10', '01', 'fr', '15', '08', 35, null, '00109', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010052', '5201140000000006', '樊近', '020000000000', '2', '1988-08-08', '01', '520111197102030912', null, '贵州贵阳', '小河平桥派出所', '06', null, '贵阳电大', '法学', '1988-08-08', '1988-08-08', '合同工', '13308512589', '3896772', null, '小河漓江花园', '1988-08-08', '1988-08-08', '10', '01', 'xj', '07', '06', 41, null, '00128', '06', '1988-08-08', '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010111', '5201140000000009', '谢樊坤', '020000000000', '1', '1988-08-08', '01', '520114198508300015', null, '贵州省贵阳市 ', '贵阳小河', '06', null, '贵州财经学院', '工商管理', null, '1988-08-08', null, '18984889903', '3896772', null, '贵阳市小河区锦江路13号', null, null, '5', '04', 'xnk', null, '07', 26, null, '00179', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010181', '5201140000000011', '樊快乐', '020000000000', '2', '1988-08-08', '01', '520111198710213928', null, '贵州省贵阳市 ', '贵阳市南明区', '06', null, '贵阳学院', '汉语言文学', null, '1988-08-08', null, '15985194802', '3895777', null, '贵阳市花溪区马铃乡', null, null, '5', '04', 'qkl', '11', '07', 24, null, '00181', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010105', '5201140000000012', '凯凯', '020000000000', '2', '1988-08-08', '01', '520103198701120420', null, '贵州省贵阳市 ', '贵阳市云岩', '06', null, '贵州民族学院', '法学', null, '1988-08-08', null, '15185034040', '3895777', null, '贵阳市宝山北路', null, null, '5', '04', 'xk', '11', '07', 25, null, '00182', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010176', '5201150000000002', '凯慧萍', '020000000000', '2', '1988-08-08', '01', '52210119860730162X', null, '贵州遵义', '贵州省遵义市红花岗区', '05', null, '贵阳学院', '涉外事务管理', '1988-08-08', '1988-08-08', null, '13639000993', '5160969', null, '贵阳市小河区西工厂', null, null, '5', '02', 'whp', '10', '05', 26, null, '00089', '05', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010182', '5201150000000001', '吴明', '020000000000', '1', '1988-08-08', '01', '520111197109231215', null, '贵州贵阳', '小河平桥派出所', '06', '04', '云南大学', '会计学', '1988-08-08', '1988-08-08', '合同工', '13985015537', '5160078', null, '小河漓江花园 ', '1988-08-08', '1988-08-08', '10', '04', 'whm', '03', '02', 40, null, '00090', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010067', '5201150000000004', '洪静', '020000000000', '2', '1988-08-08', '38', '520114197412300024', null, '贵州贵阳', '水口寺派出所', '11', null, '贵州师范大学', '汉语言文学', '1988-08-08', '1988-08-08', '合同工', '13511957950', '5160079', null, '南明晒田巷18号', '1988-08-08', '1988-08-08', '10', '04', 'hj', '07', '06', 37, null, '00091', '06', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010046', '5201150000000005', '光红', '020000000000', '2', '1988-08-08', '01', '520111197605050923', null, '贵州贵阳', '小河三江派出所', '05', null, '贵州省财经学院', '金融专业', '1988-08-08', '1988-08-08', '合同工', '13608518118', '5160969', null, '小河毛寨村', '1988-08-08', '1988-08-08', '10', '04', 'ggh', '15', '08', 36, null, '00092', '05', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010116', '5201150000000003', '王苟玉', '020000000000', '2', '1988-08-08', '01', '520103198103215227', null, '贵州云岩', '贵乌派出所', '06', null, '贵州大学', '环境科学', '1988-08-08', '1988-08-08', '合同工', '13385147766', '5160969', null, '小河乐街小区', '1988-08-08', '1988-08-08', '5', '04', 'gy', '11', '07', 31, null, '00144', '06', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010184', '5201150000000008', '小伟', '020000000000', '1', '1988-08-08', '01', '520103198012213226', null, '贵州', '贵州省贵阳市', '05', null, '贵州商业高等专科学校', '计算机美术设计', '1988-08-08', '1988-08-08', null, '18985164141', '5160969', null, '花溪区清溪南路', null, '1988-08-08', '5', '04', 'cxw', '11', '07', 31, null, '00151', '05', null, '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010071', '5201160000000002', '李倩', '020000000000', '2', '1988-08-08', '01', '520102197109045862', null, '重庆', '市西派出所', null, '01', '云南大学', '会计学', '1988-08-08', '1988-08-08', '合同工', '13984356057', null, null, '小河三江小区', '1988-08-08', '1988-08-08', '15', '04', 'jq', '10', '05', 40, null, '00082', '06', null, '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010070', '5201160000000001', '洪英', '020000000000', '2', '1988-08-08', '01', '520111196303010948', null, '贵州贵阳', '花溪贵筑派出所', '06', null, '中央党校', '法律', '1988-08-08', '1988-08-08', '合同工', '13885170396', '3716280', null, '花溪清华路25号', '1988-08-08', '1988-08-08', '15', '01', 'wy', '03', '02', 49, null, '00097', '06', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010159', '5201160000000003', '洪刚', '020000000000', '1', '1988-08-08', '01', '520111196706091818', null, '贵州贵阳', '花溪桥南派出所', null, '04', '贵州大学', '金融', '1988-08-08', '1988-08-08', '合同工', '13608588510', '3716280', null, '花溪蟠龙巷15号', '1988-08-08', '1988-08-08', '15', '04', 'sg', '16', '08', 45, null, '00098', '06', null, '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010073', '5201160000000006', '洪爱敏', '020000000000', '2', '1988-08-08', '04', '520103197401156726', null, '贵州印江', '北京路派出所', null, null, '贵州师范', '汉语言文学', '1988-08-08', '1988-08-08', '合同工', '13984197779', '3812315', null, '瑞金北路麦禾街72号', '1988-08-08', '1988-08-08', '10', '04', 'yam', '15', '08', 38, null, '00102', '06', null, '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010106', '5201160000000009', '洪珣', '020000000000', '2', '1988-08-08', '01', '41132319861016012X', null, '河南', '云岩区永乐路', '06', null, '贵州大学', '金融学', '1988-08-08', '1988-08-08', null, '13511951210', null, null, '云岩区永乐路', null, null, '5', '01', 'jx', null, '06', 25, null, '00103', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010078', '5201160000000005', '洪如美', '020000000000', '2', '1988-08-08', '03', '520111196708261526', null, '贵州花溪', '花溪溪北派出所', '03', '04', null, null, '1988-08-08', '1988-08-08', '合同工', '13511975578', '3812315', null, '花溪上水村', '1988-08-08', '1988-08-08', '15', '04', 'grm', '11', '07', 44, null, '00142', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010080', '5201160000000010', '洪君芳', '020000000000', '1', '1988-08-08', '01', '522122198611096413', null, '贵州省贵阳市 ', '南明区贵工路', '07', null, '贵州大学', '矿井通风', '1988-08-08', '1988-08-08', null, '15519187011', null, null, '南明区贵工路', null, null, '5', '04', 'zjf', '11', '07', 25, null, '00184', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010008', '5201020000000006', '洪蕾玲', '020000000000', '2', '1988-08-08', '06', '520103197808104821', null, '贵州岑巩', null, '05', null, '贵州大学', '计算机应用', null, '1988-08-08', null, '18985407739', '3802373', null, null, null, '1988-08-08', '5', '04', 'hll', null, '09', 33, null, '00018', null, null, null, '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010086', '5201030000000002', '叶叶林', '020000000000', '1', '1988-08-08', '01', '52010219770817421X', null, '贵州贵阳', '油榨街派出所', '06', null, '江西财经大学', '会计', '1988-08-08', '1988-08-08', '合同工', '13308511778', '3801927', null, '小河乐街小区', '1988-08-08', '1988-08-08', '10', '01', 'yl', '13', '09', 34, null, '00095', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010059', '5201030000000003', '叶梅', '020000000000', '2', '1988-08-08', '01', '52250219770121442X', null, '贵州清镇', '贵乌派出所', '04', null, '贵州财经学院', '会计', '1988-08-08', '1988-08-08', '合同工', '13595107667', '3801927', null, '云岩半边街28号', '1988-08-08', '1988-08-08', '10', '04', 'fm', '13', '09', 35, null, '00117', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010012', '5201040000000003', '叶莉', '020000000000', '2', '1988-08-08', '01', '520102197210242422', null, '贵州贵阳', '中华中路派出所', '05', null, '贵州大学', '会计学专业', '1988-08-08', '1988-08-08', '合同工', '13985058496', '3802367', null, '省府北街1号', '1988-08-08', '1988-08-08', '10', '04', 'zl', '13', '09', 39, null, '00022', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010112', '5201180000000003', '叶宇', '020000000000', '1', '1988-08-08', '01', '520103198110060411', null, '上海', '上海金桥派出所', '06', null, '贵州省财经学院', '金融学', '1988-08-08', '1988-08-08', '合同工', '13885003695', null, null, '西湖路26号', '1988-08-08', '1988-08-08', '5', '04', 'cy', '16', '06', 30, null, '00058', '06', '1988-08-08', '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010163', '5201190000000005', '叶文艺', '020000000000', '2', '1988-08-08', '01', '522124197210070122', null, '贵州正安', '正安凤仪镇派出所', '03', null, '贵州党校', '法律专业', '1988-08-08', '1988-08-08', '合同工', '13595082206', null, null, '沙冲南路绿苑小区', '1988-08-08', '1988-08-08', '5', '04', 'ywy', '15', '08', 39, null, '00127', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010137', '5201200000000002', '叶家彬', '020000000000', '1', '1988-08-08', '01', '522501198306250437', null, '贵州安顺', '安顺南山派出所', '06', null, '贵州省财经学院', '金融学', '1988-08-08', '1988-08-08', '合同工', '18984875699', '8690599', null, '小河香港城', '1988-08-08', '1988-08-08', '5', '04', 'tjb', '10', '05', 29, null, '00134', '06', null, '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010141', '5201200000000009', '叶晓丽', '020000000000', '2', '1988-08-08', '01', '520114198511020022', null, '贵州', '贵州贵阳', '06', null, '西安工业大学', '机械设计制造及其自动化', '1988-08-08', '1988-08-08', null, '15685156611', '8690599', null, '小河黔江路', null, '1988-08-08', '5', '04', 'wxl', '11', '07', 26, null, '00138', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010142', '5201200000000010', '叶敏', '020000000000', '2', '1988-08-08', '01', '522227198107290046', null, '贵州', '广西桂林', '06', null, '贵州大学', '法学', '1988-08-08', '1988-08-08', null, '18798791927', null, null, '贵阳遵义路', '1988-08-08', null, '5', '04', 'lm', '11', '06', 31, null, '00192', '11', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010120', '5201170000000001', '叶馨', '020000000000', '2', '1988-08-08', '01', '520102197210081622', null, '上海', '小河黄河路派出所', '05', '07', '云南大学', '会计学', '1988-08-08', '1988-08-08', '合同工', '13595191891', '3835645', null, '小河清水江路1号', '1988-08-08', '1988-08-08', '10', '01', 'gx', '03', '02', 39, null, '00105', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010081', '5201170000000003', '黄一霞', '020000000000', '2', '1988-08-08', '01', '52210119730419202X', null, '山东济南', '小河黄河路派出所', '04', null, '遵义电大', '金融', '1988-08-08', '1988-08-08', '合同工', '13639074637', null, null, '小河兴隆花园', '1988-08-08', '1988-08-08', '10', '04', 'hx', '11', '07', 39, null, '00106', '05', '1988-08-08', '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010075', '5201170000000004', '叶翠萍', '020000000000', '2', '1988-08-08', '01', '520102196604254640', null, '浙江海宁', '小河黄河路派出所', '03', null, '解放军后勤工程学院', '质检', '1988-08-08', '1988-08-08', '合同工', '13765045585', null, null, '小河明彩居', '1988-08-08', '1988-08-08', '15', '04', 'zcp', '15', '08', 46, null, '00107', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010124', '5201180000000005', '叶壵', '020000000000', '1', '1988-08-08', '05', '522730198107170015', null, '贵州', '小河黄河路派出所', '06', null, '北京工商大学', '工商管理', '1988-08-08', '1988-08-08', '合同工', '13765824333', null, null, '小河锦江路', '1988-08-08', '1988-08-08', '5', '01', 'lz', '14', '06', 31, null, '00059', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010128', '5201180000000012', '叶雅琼', '020000000000', '2', '1988-08-08', '01', '522628198511200047', null, null, null, '06', null, '贵州财经学院', null, null, '1988-08-08', null, '13639009863', null, null, null, null, null, '5', '04', 'lyq', null, '07', 26, null, '00189', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010095', '5201190000000006', '叶莲', '020000000000', '2', '1988-08-08', '01', '522225197809188727', null, '贵州思南', '小河黄河路派出所', '04', null, '贵州财经学院', '金融专业', '1988-08-08', '1988-08-08', '合同工', '13984366733', null, null, '小河香港城', '1988-08-08', '1988-08-08', '10', '04', 'll', '11', '07', 33, null, '00075', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010134', '5201190000000009', '王斌', '020000000000', '1', '1988-08-08', '03', '520111198507170010', null, '贵州', '贵阳市小河区', '06', null, '安徽财经大学', '行政管理', '1988-08-08', '1988-08-08', null, '15885007376', null, null, '贵阳市小河区金竹镇', null, '1988-08-08', '5', '04', 'whb', '07', '07', 27, null, '00080', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010118', '5201190000000007', '王罗雨', '020000000000', '1', '1988-08-08', '01', '520112197508230013', null, '贵州贵阳', '威清派出所', '04', null, '贵州经济干部管理学院', '金融专业', '1988-08-08', '1988-08-08', '合同工', '13809437092', null, null, '威清路63号', '1988-08-08', '1988-08-08', '5', '04', 'ly', '11', '07', 36, null, '00100', '05', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010129', '5201190000000001', '王季波', '020000000000', '1', '1988-08-08', '01', '520102197710136618', null, '贵州贵阳', '小河黄河路派出所', '03', null, '贵州大学', '金融专业', '1988-08-08', '1988-08-08', '合同工', '13308511183', '3833477', null, '小河江南苑', '1988-08-08', '1988-08-08', '10', '04', 'jb', '03', '02', 34, null, '00112', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010133', '5201190000000008', '王忠琴', '020000000000', '2', '1988-08-08', '03', '520111196911140922', null, '贵州贵阳', '小河平桥派出所', null, '04', '贵州银行学校', '金融专业', '1988-08-08', '1988-08-08', '合同工', '13985443683', null, null, '小河漓江花园', '1988-08-08', '1988-08-08', '15', '04', 'jzq', '11', '07', 42, null, '00124', '04', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010021', '5201110000000003', '刘王冀', '020000000000', '1', '1988-08-08', '01', '520111197812060016', null, '贵州贵阳', '花溪清溪路派出所', '06', null, '贵州财经学院', '金融学', '1988-08-08', '1988-08-08', '合同工', '18608511167', '3802178', null, '花溪台子脚', '1988-08-08', '1988-08-08', '5', '04', 'ly', '10', '05', 33, null, '00052', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010023', '5201110000000008', '王红莲', '020000000000', '2', '1988-08-08', '04', '522227197812290020', null, '贵州德江', '拉萨金珠派出所', '06', null, '贵州民族学院', '数学', '1988-08-08', '1988-08-08', '合同工', '13595005146', '3833147', null, '相宝山宝山公寓', '1988-08-08', '1988-08-08', '5', '04', 'yhl', '15', '08', 33, null, '00069', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010029', '5201120000000007', '王王', '020000000000', '2', '1988-08-08', '01', '522424198703200029', null, null, null, '05', null, '贵州商业高等专科学校', '会计电算化', null, null, null, '15985118430', null, null, null, null, '1988-08-08', '5', '04', 'ws', '12', '07', 25, null, '00164', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010032', '5201130000000002', '王艳平', '020000000000', '2', '1988-08-08', '01', '520102197801084227', null, '湖南桃源', '小河黄河路派出所', '05', null, '重庆商学院', '电算化会计', '1988-08-08', '1988-08-08', '合同工', '13037833604', '3909192', null, '小河邮局宿舍', '1988-08-08', '1988-08-08', '5', '04', 'lyp', '10', '05', 34, null, '00053', '05', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010036', '5201080000000001', '曾王雷', '020000000000', '1', '1988-08-08', '01', '522223196804020418', null, '贵州铜仁', '平桥派出所', '04', '07', '中央广播电视大学', '法学', '1988-08-08', '1988-08-08', '合同工', '15085958916', '3802268', null, '宅吉小区', '1988-08-08', '1988-08-08', '15', '01', 'zcl', '06', '03', 44, null, '00036', '06', '1988-08-08', '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010039', '5201020000000003', '邓王美', '020000000000', '2', '1988-08-08', '01', '522526196810056025', null, '重庆铜梁', '小河黄河路派出所', '05', '04', ' 自学考试', '行政管理专业', '1988-08-08', '1988-08-08', '合同工', '15519057619', '3802338', null, '小河香港城', '1988-08-08', '1988-08-08', '15', '04', 'dm', '11', '09', 43, null, '00014', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010040', '5201040000000002', '邱王婷', '020000000000', '2', '1988-08-08', '01', '522501198006072026', null, '江苏徐州', '花溪溪北派出所', '07', null, '贵州大学', '经济学', '1988-08-08', '1988-08-08', '合同工', '18984571366', '3909098', null, '小河珠江湾畔', '1988-08-08', '1988-08-08', '5', '01', 'qt', '08', '04', 32, null, '00015', '07', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010042', '5201090000000003', '王王春', '020000000000', '1', '1988-08-08', '01', '522323197911100018', null, '贵州思南', '小河黄河路派出所', '05', null, '贵州工业机械职工大学', '金融管理', '1988-08-08', '1988-08-08', '合同工', '15180861357', '3802208', null, '小河黄河路163号', '1988-08-08', '1988-08-08', '10', '04', 'wc', '14', '09', 32, null, '00043', '03', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010045', '5201140000000002', '王晓涛', '020000000000', '2', '1988-08-08', '01', '520111198002210048', null, '贵州贵阳', '花溪清溪路派出所', '05', null, '贵州大学', '金融学', '1988-08-08', '1988-08-08', '合同工', '13595095627', '3895777', null, '花溪松涛苑', '1988-08-08', '1988-08-08', '5', '04', 'hxt', '10', '05', 32, null, '00146', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010060', '5201010000000006', '王国恒', '020000000000', '1', '1988-08-08', '03', '520111197206010617', null, '贵州小河', '小河黄河路派出所', '06', '04', null, null, '1988-08-08', '1988-08-08', '合同工', '13618506655', '3802207', null, '小河麦兆小区', '1988-08-08', '1988-08-08', '15', '01', 'jgh', '05', '01', 40, null, '00005', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010066', '5201140000000007', '康王艳', '020000000000', '2', '1988-08-08', '01', '522523198312090028', null, '贵州息烽', '息烽派出所', '05', null, '云南大学', '会计学', '1988-08-08', '1988-08-08', '合同工', '13984136493', null, null, '白云区云峰大道', null, null, '5', '04', 'ky', '11', '07', 28, null, '00168', '06', null, '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010077', '5201150000000010', '王方艺', '020000000000', '2', '1988-08-08', '01', '520103198810133220', null, '重庆', '贵州省贵阳市云岩区', '06', null, '贵州大学', '财务管理', '1988-08-08', '1988-08-08', null, '13984375148', null, null, '贵州省贵阳市云岩区枣山路鸿基景苑', null, null, '5', '04', 'lfy', null, '07', 23, null, '00183', '06', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010079', '5201160000000007', '代小琴', '020000000000', '2', '1988-08-08', '01', '522128197301262707', null, '归后遵义', '遵义县公安局', null, null, '遵义师专', '教师', '1988-08-08', '1988-08-08', '合同工', '15286000203', null, null, '小河红河路7号', '1988-08-08', '1988-08-08', '10', '04', 'dq', '16', '06', 39, null, '00152', '05', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010082', '5201170000000008', '刘晓王', '020000000000', '2', '1988-08-08', '01', '430223198104196223', null, '贵州', '三江派出所', '04', null, '北京国际商务学院', '金融', '1988-08-08', '1988-08-08', '合同工', '13809445575', null, null, '小河王武村', '1988-08-08', '1988-08-08', '5', '04', 'lxl', '11', '07', 31, null, '00086', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010096', '5201210000000002', '魏王苇', '020000000000', '2', '1988-08-08', '01', '522523197201200023', null, '贵州息烽', '息烽县教育局宿舍', '05', null, '贵州省委党校', '法律', '1988-08-08', '1988-08-08', '合同工', '13984070999', '3762055', null, '花溪区大将路', '1988-08-08', '1988-08-08', '10', '04', 'wyw', '10', '05', 40, null, '00088', '09', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010101', '5201070000000008', '柯王秀', '020000000000', '2', '1988-08-08', '01', '520111196901110922', null, '贵州贵阳', '小河平桥派出所', '04', '04', '贵州财经学院小河站', '金融', '1988-08-08', '1988-08-08', '合同工', '13885122854', '3802508', null, '贵阳市小河区漓江花园', '1988-08-08', '1988-08-08', '10', '04', 'kxx', '13', '09', 43, null, '00032', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010102', '5201110000000001', '张天王', '020000000000', '1', '1988-08-08', '06', '522601198110270036', null, '贵州天柱', '小河黄河路派出所', '04', null, '贵州财经学院', '金融管理', '1988-08-08', '1988-08-08', '合同工', '13985141513', '3808652', null, '小河中兴苑', '1988-08-08', '1988-08-08', '10', '04', 'ztp', '03', '02', 30, null, '00064', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010103', '5201120000000002', '王王', '020000000000', '2', '1988-08-08', '03', '520102198312131242', null, '贵州贵阳', '市南派出所', '06', null, '重庆工商大学', '会计学', '1988-08-08', '1988-08-08', '合同工', '13985514141', '8598783', null, '市南路51号', '1988-08-08', '1988-08-08', '5', '03', 'wx', '10', '05', 28, null, '00084', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010107', '5201170000000011', '宋王木', '020000000000', '1', '1988-08-08', '01', '522424198705242417', null, '贵州', '贵州金沙', '06', null, '西南民族大学', '财务管理', '1988-08-08', '1988-08-08', null, '13985491096', null, null, '南明区山水黔城', null, null, '5', '04', 'ssm', null, '07', 25, null, '00186', '06', null, '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010013', '5201050000000001', '李王燕', '020000000000', '2', '1988-08-08', '01', '52242719671113202X', null, '贵州毕节', '小河黄河路派出所', '05', null, '贵州省委党校', '经济管理', '1988-08-08', '1988-08-08', '合同工', '13985580700', '3807700', null, '小河三江小区', '1988-08-08', '1988-08-08', '15', '01', 'lhy', '21', '03', 44, null, '00026', '06', null, null, '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010167', '5201050000000002', '宋王娟', '020000000000', '2', '1988-08-08', '01', '440106197706100343', null, '贵州龙里', '小河平桥派出所', '05', '05', '贵州财经学院', '会计学', '1988-08-08', '1988-08-08', '合同工', '15985144194', '3803790', null, '小河榕筑鲜花广场', '1988-08-08', '1988-08-08', '10', '01', 'sj', '13', '09', 35, null, '00027', '06', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010148', '5201060000000001', '王川', '020000000000', '1', '1988-08-08', '01', '52011119761210001X', null, '贵州贵阳', '花溪分局', '06', null, '贵州财经学院', '计算机科学与技术', '1988-08-08', '1988-08-08', '合同工', '13985048899', '3802298', null, '小河榕筑鲜花广场', '1988-08-08', '1988-08-08', '10', '01', 'hdc', '21', '03', 35, null, '00030', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010110', '5201070000000010', '迟王明', '020000000000', '1', '1988-08-08', '01', '520111195802123010', null, '贵州贵阳', '花溪分局', '04', '06', '贵州省银行学校', '金融', '1988-08-08', '1988-08-08', '合同工', '13885048543', '3808672', null, '贵阳市花溪区清溪路', '1988-08-08', '1988-08-08', '15', '04', 'chm', '13', '09', 54, null, '00013', '04', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010062', '5201070000000005', '邵王萍', '020000000000', '2', '1988-08-08', '01', '520102197202121620', null, '山东藤县', '花果园派出所', '06', '03', '贵州大学', '会计', '1988-08-08', '1988-08-08', '合同工', '13985147158', '3802508', null, '南明玉田小区', '1988-08-08', '1988-08-08', '15', '04', 'sp', '13', '09', 40, null, '00029', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010014', '5201070000000001', '高王梅', '020000000000', '2', '1988-08-08', '05', '520102196911252047', null, '贵州织金', '宅吉派出所', '06', null, '贵州财经学院', '经济管理', '1988-08-08', '1988-08-08', '合同工', '13985409988', '3808702', null, '云岩天玺华庭', '1988-08-08', '1988-08-08', '10', '04', 'gm', '13', '03', 42, null, '00033', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010049', '5201070000000002', '王谢锦', '020000000000', '2', '1988-08-08', '01', '520103198203134846', null, '贵州小河', '小河黄河路派出所', '04', null, '贵州广播电视大学', '会计学', '1988-08-08', '1988-08-08', '合同工', '18685139711', '3802508', null, '贵阳市小河区中兴苑南苑5栋802号', '1988-08-08', '1988-08-08', '10', '04', 'xj', '13', '09', 30, null, '00050', '07', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010097', '5201070000000007', '王幼玲', '020000000000', '2', '1988-08-08', '01', '520111196506070422', null, '贵州花溪', '花溪清溪路派出所', '05', '03', '贵州大学', '金融', '1988-08-08', '1988-08-08', '合同工', '13885170882', '3803713', null, '花溪清溪路110号', '1988-08-08', '1988-08-08', '15', '04', 'jyl', '13', '09', 47, null, '00140', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010017', '5201080000000005', '王晓刚', '020000000000', '1', '1988-08-08', '01', '522501198408050612', null, '贵州贵阳', '花溪桥北派出所', '06', null, '贵阳民族学院', '汉语言文学', '1988-08-08', '1988-08-08', '合同工', '13511951253', '3802278', null, '小河中曹路46号', '1988-08-08', '1988-08-08', '5', '01', 'cxg', '13', '09', 28, null, '00040', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010051', '5201120000000006', '罗王丽', '020000000000', '2', '1988-08-08', '01', '520102197311155424', null, '贵州贵阳', '南明分局', '04', null, '贵州财经学院', '会计学', '1988-08-08', '1988-08-08', '合同工', '13885077679', '8598783', null, '南明区西湖路26号', null, null, '10', '04', 'lhl', '11', '07', 38, null, '00166', '05', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010030', '5201120000000010', '赵王洋', '020000000000', '1', '1988-08-08', '03', '520114199008110439', null, '贵州六盘水', null, '06', null, '中北大学', null, null, null, null, '18685120605', null, null, null, null, null, '5', '04', 'zy', null, '07', 21, null, '00174', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010031', '5201120000000012', '王念佳', '020000000000', '2', '1988-08-08', '01', '520102198803151643', null, '浙江省嘉善县', null, '06', null, '贵州大学', null, null, null, null, '13985001067', '8598783', null, null, null, null, '5', '04', 'ynj', null, '07', 24, null, '00176', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010034', '5201130000000005', '王王', '020000000000', '2', '1988-08-08', '03', '52011119740312092X', null, '贵州贵阳', '三江派出所', '06', null, '云南大学', '会计学', '1988-08-08', '1988-08-08', '合同工', '13985116390', '3909192', null, '小河中兴苑', '1988-08-08', '1988-08-08', '10', '01', 'wyc', '15', '08', 38, null, '00073', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010055', '5201130000000003', '田王涛', '020000000000', '1', '1988-08-08', '01', '520103198003010435', null, '贵州贵阳', '兴关路派出所', '05', null, '贵州商专', '电算化会计', '1988-08-08', '1988-08-08', '合同工', '13985057592', '3909098', null, '青云路260号', '1988-08-08', '1988-08-08', '10', '04', 'tt', '13', '09', 32, null, '00074', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010115', '5201130000000006', '周王师', '020000000000', '1', '1988-08-08', '01', '520103198403290413', null, '贵州贵阳', '新华路派出所', '04', null, '贵州电视广播大学', '金融微机', '1988-08-08', '1988-08-08', '合同工', '13312252099', '3909098', null, '箭道街145号', '1988-08-08', '1988-08-08', '5', '01', 'zs', '11', '07', 28, null, '00078', '05', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010076', '5201170000000006', '王张翼', '020000000000', '1', '1988-08-08', '01', '520102197708234235', null, '湖北武01', '兴关路派出所', '06', null, '中共省委党校', '经济管理', '1988-08-08', '1988-08-08', '合同工', '18908501237', null, null, '兴关北巷7号', '1988-08-08', '1988-08-08', '10', '04', 'zy', '07', '06', 34, null, '00060', '06', '1988-08-08', '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010161', '5201170000000007', '王小静', '020000000000', '2', '1988-08-08', '01', '52010219690912242X', null, '贵州贵阳', '新华路派出所', '05', '04', '贵州大学', '财务会计', '1988-08-08', '1988-08-08', '合同工', '13985496343', null, null, '山水黔城', '1988-08-08', '1988-08-08', '15', '04', 'wj', '07', '06', 42, null, '00111', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010175', '5201180000000001', '何王龙', '020000000000', '1', '1988-08-08', '01', '520112198105020016', null, '贵州贵阳', '乌当分局', '05', null, '云南大学', '会计学', '1988-08-08', '1988-08-08', '合同工', '13985135577', '3813313', null, '新天寨温泉路1号', '1988-08-08', '1988-08-08', '10', '04', 'hcl', '18', '02', 31, null, '00037', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010109', '5201210000000006', '王建雅', '020000000000', '2', '1988-08-08', '01', '520111196803240424', null, '贵州花溪', '花溪贵筑派出所', '05', null, '贵州省委党校', '经济管理', '1988-08-08', '1988-08-08', '合同工', '13885524867', '3762055', null, '花溪霞晖路84号', '1988-08-08', '1988-08-08', '5', '04', 'zyj', '11', '07', 44, null, '00145', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010143', '5201200000000011', '王王悦', '020000000000', '2', '1988-08-08', '01', '522225198506070021', null, '贵州', '贵州贵阳', '06', null, '贵州大学', '新闻学', '1988-08-08', '1988-08-08', null, '13765138893', null, null, '贵阳南明区', null, null, '5', '04', 'wxy', null, '07', 27, null, '00193', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010119', '5201060000000002', '王忠华', '020000000000', '1', '1988-08-08', '03', '52011419800516041X', null, '贵州贵阳', '小河三江派出所', '06', null, '江西财经大学', '会计', '1988-08-08', '1988-08-08', '合同工', '13765126655', '3802308', null, '小河王宽村', '1988-08-08', '1988-08-08', '10', '04', 'jzh', '13', '09', 32, null, '00034', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010048', '5201210000000001', '施王罡', '020000000000', '1', '1988-08-08', '01', '520102197710221636', null, '贵州贵阳', '中山东路派出所', '05', null, '贵州大学', '会计学', '1988-08-08', '1988-08-08', '合同工', '13308511633', '3762824', null, '小河三江小区', '1988-08-08', '1988-08-08', '10', '04', 'sg', '03', '02', 34, null, '00024', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010144', '5201210000000004', '邓王坤', '020000000000', '2', '1988-08-08', '01', '520202198504074049', null, '贵州柏果', '六盘水柏果镇派出所', '06', null, '贵州师范大学', '音乐', '1988-08-08', '1988-08-08', '合同工', '18785191180', '3762055', null, '小河中曹路', '1988-08-08', '1988-08-08', '5', '01', 'dk', '11', '07', 27, null, '00055', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010084', '5201210000000005', '李莉', '020000000000', '2', '1988-08-08', '01', '520111197404110029', null, '贵州花溪', '花溪清溪路派出所', '05', null, '贵州省委党校', '经济管理', '1988-08-08', '1988-08-08', '合同工', '18985595105', null, null, '花溪将军路从伟商住楼', '1988-08-08', '1988-08-08', '5', '04', 'wl', '15', '08', 38, null, '00143', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201050000002011030002', '5201160000000008', '徐钊宇', '020000000000', '2', '1988-08-08', '06', '522221198601021249', null, '贵州', '小河兴隆花园', '06', null, '贵州师范大学', '法学', '1988-08-08', '1988-08-08', null, '13639006004', null, null, '小河兴隆花园', null, null, '5', null, null, '11', null, 26, null, '00156', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201050000002011030005', '5201090000000002', '徐海', '020000000000', '1', '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, '13985116370', null, null, null, null, null, '0', null, null, null, null, 41, null, '00035', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201050000002011030014', null, '陈来江', '020000000000', '2', null, '01', '522723197912250466', null, '贵州贵阳', '贵阳市南明区', null, null, null, '会计电算化', '1988-08-08', '1988-08-08', '合同制', '18984968866', null, null, '贵阳市南明区四方河1号', null, null, '5', '04', null, null, null, 31, null, '00196', '06', null, '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010085', '5201210000000007', '代徐丹', '020000000000', '2', '1988-08-08', '01', '520111197704240626', null, '贵州贵阳', '花溪贵筑派出所', '05', null, '贵州大学', '法律', '1988-08-08', '1988-08-08', '合同工', '13985541115', '3762055', null, '花溪九龙花园', '1988-08-08', '1988-08-08', '5', '04', 'dd', '15', '08', 35, null, '00147', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010145', '5201210000000008', '徐龙娟', '020000000000', '2', '1988-08-08', '02', '522229198603130829', null, '贵州', '贵州铜仁', '06', null, '云南大学', '财务管理', '1988-08-08', '1988-08-08', null, '7910139', '3762924', null, '贵州铜仁', null, '1988-08-08', '5', '04', 'lj', '11', '07', 26, null, '00159', '08', null, '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010147', '5201210000000011', '徐王霞', '020000000000', '2', '1988-08-08', '01', '520102198806303024', null, '湖南省长沙市', '贵阳市南明区', '06', null, '贵州财经学院', '国际经济与贸易', '1988-08-08', '1988-08-08', null, '13765064909', '3762924', null, '贵阳市南明区遵义路', null, null, '5', '02', 'wx', '11', '07', 24, null, '00195', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010171', '5201220000000003', '徐显均', '020000000000', '1', '1988-08-08', '01', '51022119751213151X', null, '重庆长寿', '小河黄河路派出所', '06', null, null, null, '1988-08-08', '1988-08-08', '合同工', '13985056953', null, null, '小河香江路14号', '1988-08-08', '1988-08-08', '10', '01', 'cxj', '20', '10', 36, null, '00045', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010074', '5201170000000002', '徐万霞', '020000000000', '2', '1988-08-08', '03', '522522198110306066', null, '贵州开阳', '云岩龙岗派出所', '06', null, '贵州民族学院', '社会学', '1988-08-08', '1988-08-08', '合同工', '13984955465', null, null, '贵阳小石城', '1988-08-08', '1988-08-08', '5', '01', 'jwx', '10', '05', 30, null, '00057', '06', '1988-08-08', '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010160', '5201170000000005', '徐又红', '020000000000', '2', '1988-08-08', '01', '522130197209180022', null, '四川成都', '仁怀中枢镇派出所', '03', null, '财经学院', '金融', '1988-08-08', '1988-08-08', '合同工', '13511932568', null, null, '文昌北路100号', '1988-08-08', '1988-08-08', '10', '04', 'cyh', '07', '06', 39, null, '00108', '05', '1988-08-08', '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010093', '5201170000000010', '江徐玲', '020000000000', '2', '1988-08-08', '06', '522223198410061224', null, '贵州', '贵州玉屏', '06', null, '贵州大学', '金融学', '1988-08-08', '1988-08-08', null, '18785150700', null, null, '小河区珠江湾畔', null, '1988-08-08', '5', '02', 'jll', null, '07', 27, null, '00113', '06', null, '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010122', '5201170000000009', '王徐进', '020000000000', '1', '1988-08-08', '04', '522225198709182015', null, '贵州思南', '云岩区南垭路', '06', null, '贵州大学', '财务管理', '1988-08-08', '1988-08-08', '合同工', '15085835666', null, null, '小河区漓江花园', null, null, '5', '04', 'wrj', '19', '07', 24, null, '00167', '06', '1988-08-08', '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010123', '5201180000000002', '章徐芸', '020000000000', '2', '1988-08-08', '01', '520111691116006', null, '贵州贵阳', '小河黄河路派出所', '06', '04', '贵州省财经学院', '金融', '1988-08-08', '1988-08-08', '合同工', '13985110506', null, null, '小河蒲江路政府宿舍', '1988-08-08', '1988-08-08', '15', '01', 'zzy', '10', '05', 42, null, '00093', '06', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010125', '5201180000000006', '徐筱菲', '020000000000', '2', '1988-08-08', '05', '522529197704287026', null, '云南玉溪', '小河黄河路派出所', '05', null, '贵州商业高等专科学校', '会计电算化', '1988-08-08', '1988-08-08', '合同工', '13984040688', null, null, '小河乐街小区', '1988-08-08', '1988-08-08', '10', '04', 'lxf', '16', '06', 35, null, '00118', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010047', '5201180000000008', '徐薰贵', '020000000000', '1', '1988-08-08', null, '520103198606196718', null, null, null, null, null, null, null, null, null, null, '13595008088', null, null, null, null, null, '5', null, 'cxg', null, '07', 26, null, '00122', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010094', '5201180000000009', '徐左莎', '020000000000', '2', '1988-08-08', '01', '52272519850718006X', null, '贵州瓮安', null, '06', null, '贵州师范大学', '体育教育', null, null, null, '13885104819', null, null, null, null, null, '5', '04', 'zs', null, '07', 27, null, '00129', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010058', '5201180000000010', '徐谢琨', '020000000000', '1', '1988-08-08', '01', '520103198606150510', null, null, null, '06', null, '第二炮兵工程学院', null, null, null, null, '13595033000', null, null, null, null, null, '5', '04', 'xk', null, '07', 26, null, '00187', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010132', '5201190000000004', '张徐敏', '020000000000', '2', '1988-08-08', '01', '520103196310040440', null, '贵州贵阳', '朝阳派出所', '04', null, '贵州教育学院', '行政管理', '1988-08-08', '1988-08-08', '合同工', '13985590486', null, null, '贵阳大庆路利鑫圆', '1988-08-08', '1988-08-08', '15', '01', 'zm', '11', '07', 48, null, '00126', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010140', '5201200000000008', '马徐艺', '020000000000', '2', '1988-08-08', '01', '522101197410287663', null, '贵州贵阳', '中山东路派出所', '11', null, '遵义医学院', '临床医学', '1988-08-08', '1988-08-08', '合同工', '13985047773', null, null, '体育路6号', '1988-08-08', '1988-08-08', '10', '04', 'my', '15', '05', 37, null, '00104', '06', null, '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010108', '5201200000000005', '徐周波', '020000000000', '1', '1988-08-08', '01', '522502197902094831', null, '贵州清镇', '小河黄河路派出所', '05', null, null, null, '1988-08-08', '1988-08-08', '合同工', '13308510556', '8690399', null, '小河青山商住楼', '1988-08-08', '1988-08-08', '10', '01', 'zb', '16', '06', 33, null, '00131', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010165', '5201200000000004', '徐汝漫', '020000000000', '2', '1988-08-08', '03', '520114198501250449', null, '贵州小河', '小河三江派出所', '05', null, '贵州省财经学院', '会计电算化', '1988-08-08', '1988-08-08', '合同工', '13608511100', '8690599', null, '小河红河路航天圆', '1988-08-08', '1988-08-08', '5', '04', 'jrm', '11', '07', 27, null, '00133', null, null, '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010083', '5201210000000003', '梁徐励', '020000000000', '1', '1988-08-08', '01', '520103196308074019', null, '贵州毕节', '中山西路派出所', '03', null, '贵州大学', '经济管理', '1988-08-08', '1988-08-08', '合同工', '13985594387', '3762924', null, '小河明彩居', '1988-08-08', '1988-08-08', '15', '01', 'll', '07', '06', 49, null, '00079', '05', '1988-08-08', '1988-08-08', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010131', '5201190000000003', '徐苏俊', '020000000000', '1', '1988-08-08', '01', '520103197702054010', null, '贵州贵阳', '云岩分局', '04', null, '贵州财经学院', '金融专业', '1988-08-08', '1988-08-08', '合同工', '13885076717', null, null, '云岩香狮巷11号2-5', '1988-08-08', '1988-08-08', '10', '04', 'sj', '16', '06', 35, null, '00153', '05', '1988-08-08', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010135', '5201190000000010', '徐张苏', '020000000000', '2', '1988-08-08', '01', '520102198601282020', null, '江苏省扬中市', '南明区瑞金南路', '06', null, '贵州大学', '法学', '1988-08-08', '1988-08-08', null, '13639069056', null, null, '南明区瑞金南路', null, null, '5', '01', 'zs', '11', '07', 26, null, '00190', '06', '1988-08-08', '1988-08-08', '0', null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010113', '5201190000000011', '罗徐柳', '020000000000', '2', '1988-08-08', '01', '522132198709232228', null, '贵州省贵阳市 ', '贵阳省人才市场', '06', null, '贵州大学明德学院', '会计学', '1988-08-08', '1988-08-08', null, '13765046378', null, null, '习水县', null, null, '5', '04', 'ln', '07', '07', 24, null, '00191', null, '1988-08-08', '1988-08-08', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into YS_HQ_PERSON (ID, PCODE, NAME, DEPT, XB, CSRQ, MZ, SFZHM, HYZK, JG, FKSZD, XL, ZC, BLYS, ZL, CJGZSJ, JRBDWSJ, YGLX, SJ, JTDH, DZYJ, JTZC, HTQS, HTQE, LXJ, ZZMM, SPELL, ZW, GS, NL, CODEORDER, PGH, XL1, BGDATE, BLDATE, ISZZJY, QT, GWQK, JYBJ, GZJL, SHGX, JCJL, ZWQK, PXJL, DBJL, HTQK, SBQK, TJJL, PHOTO, BZ) values ('P5201000000002011010168', '5201120000000008', '徐婷婷', '020000000000', '2', '1988-08-08', '01', '520103198611160445', null, null, null, '06', null, '贵州财经学院', '财务管理', null, null, null, '13984330655', '8598783', null, null, null, '1988-08-08', '5', '02', 'ztt', '11', '07', 25, null, '00150', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";

			strSQL = strSQL
					+ "update YS_HQ_PERSON set SJ='12345678901',JTDH='0851-1234567',SFZHM='52010119885523111x';";

			dbengine.ExecuteSQLs(strSQL.split(";"));

			strSQL = "delete from COLL_CONFIG_OPERATE_FIELD;insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000003', '0000000004', 'YS_SP_ITEM_DLHT.ITEMNAME', '1', '1', '1', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000004', '0000000004', 'YS_SP_ITEM_DLHT.ITEMBODY', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000005', '0000000004', 'YS_SP_ITEM_DLHT.ITEMBUDGET', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000006', '0000000004', 'YS_SP_ITEM_DLHT.MONEYUNIT', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000007', '0000000004', 'YS_SP_ITEM_DLHT.APPROVECODE', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000008', '0000000004', 'YS_SP_ITEM_DLHT.BIDMODALITY', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000009', '0000000004', 'YS_SP_ITEM_DLHT.SYFG', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000010', '0000000004', 'YS_SP_ITEM_DLHT.RLZYPB', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000011', '0000000004', 'YS_SP_ITEM_DLHT.WZZYBZ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000012', '0000000004', 'YS_SP_ITEM_DLHT.XMMBJJDAP', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000013', '0000000004', 'YS_SP_ITEM_DLHT.CZGC', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000014', '0000000004', 'YS_SP_ITEM_DLHT.ZYSX', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000015', '0000000004', 'YS_SP_ITEM_DLHT.SPYJ1', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000016', '0000000004', 'YS_SP_ITEM_DLHT.SPR1', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000017', '0000000004', 'YS_SP_ITEM_DLHT.SPSJ1', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000018', '0000000004', 'YS_SP_ITEM_DLHT.SPYJ2', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000019', '0000000004', 'YS_SP_ITEM_DLHT.SPR2', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000020', '0000000004', 'YS_SP_ITEM_DLHT.SPSJ2', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000021', '0000000004', 'YS_SP_ITEM_DLHT.ETIME', '1', '0', '0', '{YYYY-MM-DD}', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000022', '0000000005', 'YS_SP_ITEM_DLHT.ID', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000023', '0000000005', 'YS_SP_ITEM_DLHT.CONSIGNUNIT', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000024', '0000000005', 'YS_SP_ITEM_DLHT.ITEMNAME', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000025', '0000000005', 'YS_SP_ITEM_DLHT.ITEMBODY', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000026', '0000000005', 'YS_SP_ITEM_DLHT.ITEMBUDGET', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000027', '0000000005', 'YS_SP_ITEM_DLHT.MONEYUNIT', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000028', '0000000005', 'YS_SP_ITEM_DLHT.APPROVECODE', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000029', '0000000005', 'YS_SP_ITEM_DLHT.BIDMODALITY', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000030', '0000000005', 'YS_SP_ITEM_DLHT.SYFG', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000031', '0000000005', 'YS_SP_ITEM_DLHT.RLZYPB', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000032', '0000000005', 'YS_SP_ITEM_DLHT.WZZYBZ', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000033', '0000000005', 'YS_SP_ITEM_DLHT.XMMBJJDAP', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000034', '0000000005', 'YS_SP_ITEM_DLHT.CZGC', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000035', '0000000005', 'YS_SP_ITEM_DLHT.ZYSX', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000036', '0000000005', 'YS_SP_ITEM_DLHT.SPYJ1', '1', '1', '1', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000037', '0000000005', 'YS_SP_ITEM_DLHT.SPR1', '1', '0', '0', '{UserID}', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000038', '0000000005', 'YS_SP_ITEM_DLHT.SPSJ1', '1', '0', '0', '{YYYY-MM-DD}', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000039', '0000000005', 'YS_SP_ITEM_DLHT.SPYJ2', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000040', '0000000005', 'YS_SP_ITEM_DLHT.SPR2', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000041', '0000000005', 'YS_SP_ITEM_DLHT.SPSJ2', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000042', '0000000005', 'YS_SP_ITEM_DLHT.ETIME', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000043', '0000000006', 'YS_SP_ITEM_DLHT.ID', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000044', '0000000006', 'YS_SP_ITEM_DLHT.CONSIGNUNIT', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000045', '0000000006', 'YS_SP_ITEM_DLHT.ITEMNAME', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000046', '0000000006', 'YS_SP_ITEM_DLHT.ITEMBODY', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000047', '0000000006', 'YS_SP_ITEM_DLHT.ITEMBUDGET', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000048', '0000000006', 'YS_SP_ITEM_DLHT.MONEYUNIT', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000049', '0000000006', 'YS_SP_ITEM_DLHT.APPROVECODE', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000050', '0000000006', 'YS_SP_ITEM_DLHT.BIDMODALITY', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000051', '0000000006', 'YS_SP_ITEM_DLHT.SYFG', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000001', '0000000004', 'YS_SP_ITEM_DLHT.ID', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000002', '0000000004', 'YS_SP_ITEM_DLHT.CONSIGNUNIT', '1', '1', '1', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000052', '0000000006', 'YS_SP_ITEM_DLHT.RLZYPB', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000053', '0000000006', 'YS_SP_ITEM_DLHT.WZZYBZ', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000054', '0000000006', 'YS_SP_ITEM_DLHT.XMMBJJDAP', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000055', '0000000006', 'YS_SP_ITEM_DLHT.CZGC', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000056', '0000000006', 'YS_SP_ITEM_DLHT.ZYSX', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000057', '0000000006', 'YS_SP_ITEM_DLHT.SPYJ1', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000058', '0000000006', 'YS_SP_ITEM_DLHT.SPR1', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000059', '0000000006', 'YS_SP_ITEM_DLHT.SPSJ1', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000060', '0000000006', 'YS_SP_ITEM_DLHT.SPYJ2', '1', '1', '1', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000061', '0000000006', 'YS_SP_ITEM_DLHT.SPR2', '1', '0', '0', '{UserID}', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000062', '0000000006', 'YS_SP_ITEM_DLHT.SPSJ2', '1', '0', '0', '{YYYY-MM-DD}', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000063', '0000000006', 'YS_SP_ITEM_DLHT.ETIME', '1', '0', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000064', '00000001', 'YS_HQ_PERSON.PCODE', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000065', '00000001', 'YS_HQ_PERSON.NAME', '1', '1', '1', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000066', '00000001', 'YS_HQ_PERSON.DEPT', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000067', '00000001', 'YS_HQ_PERSON.XB', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000068', '00000001', 'YS_HQ_PERSON.SPELL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000069', '00000001', 'YS_HQ_PERSON.NL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000070', '00000001', 'YS_HQ_PERSON.ZW', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000071', '00000001', 'YS_HQ_PERSON.GS', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000072', '00000001', 'YS_HQ_PERSON.CODEORDER', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000073', '00000001', 'YS_HQ_PERSON.PGH', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000074', '00000001', 'YS_HQ_PERSON.JG', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000075', '00000001', 'YS_HQ_PERSON.FKSZD', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000076', '00000001', 'YS_HQ_PERSON.XL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000077', '00000001', 'YS_HQ_PERSON.ZC', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000078', '00000001', 'YS_HQ_PERSON.BLYS', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000079', '00000001', 'YS_HQ_PERSON.ZL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000080', '00000001', 'YS_HQ_PERSON.CJGZSJ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000081', '00000001', 'YS_HQ_PERSON.JRBDWSJ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000082', '00000001', 'YS_HQ_PERSON.YGLX', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000083', '00000001', 'YS_HQ_PERSON.SJ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000084', '00000001', 'YS_HQ_PERSON.JTDH', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000085', '00000001', 'YS_HQ_PERSON.DZYJ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000086', '00000001', 'YS_HQ_PERSON.JTZC', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000087', '00000001', 'YS_HQ_PERSON.HTQS', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000088', '00000001', 'YS_HQ_PERSON.HTQE', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000089', '00000001', 'YS_HQ_PERSON.XL1', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000090', '00000001', 'YS_HQ_PERSON.BGDATE', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000091', '00000001', 'YS_HQ_PERSON.BLDATE', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000092', '00000001', 'YS_HQ_PERSON.ISZZJY', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000093', '00000001', 'YS_HQ_PERSON.LXJ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000094', '00000001', 'YS_HQ_PERSON.GWQK', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000095', '00000001', 'YS_HQ_PERSON.JYBJ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000096', '00000001', 'YS_HQ_PERSON.GZJL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000097', '00000001', 'YS_HQ_PERSON.SHGX', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000098', '00000001', 'YS_HQ_PERSON.JCJL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000099', '00000001', 'YS_HQ_PERSON.ZWQK', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000100', '00000001', 'YS_HQ_PERSON.PXJL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000101', '00000001', 'YS_HQ_PERSON.DBJL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000102', '00000001', 'YS_HQ_PERSON.HTQK', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000103', '00000001', 'YS_HQ_PERSON.SBQK', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000104', '00000001', 'YS_HQ_PERSON.TJJL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000105', '00000001', 'YS_HQ_PERSON.BZ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000106', '00000001', 'YS_HQ_PERSON.ZZMM', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000107', '00000001', 'YS_HQ_PERSON.CSRQ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000108', '00000001', 'YS_HQ_PERSON.PHOTO', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000109', '00000001', 'YS_HQ_PERSON.MZ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000110', '00000001', 'YS_HQ_PERSON.SFZHM', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000111', '00000001', 'YS_HQ_PERSON.HYZK', '1', '1', '0', ' ', '0');";

			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000112', '00000005', 'YS_HQ_PERSON.PCODE', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000113', '00000005', 'YS_HQ_PERSON.NAME', '1', '1', '1', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000114', '00000005', 'YS_HQ_PERSON.DEPT', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000115', '00000005', 'YS_HQ_PERSON.XB', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000116', '00000005', 'YS_HQ_PERSON.SPELL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000117', '00000005', 'YS_HQ_PERSON.NL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000118', '00000005', 'YS_HQ_PERSON.ZW', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000119', '00000005', 'YS_HQ_PERSON.GS', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000120', '00000005', 'YS_HQ_PERSON.CODEORDER', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000121', '00000005', 'YS_HQ_PERSON.PGH', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000122', '00000005', 'YS_HQ_PERSON.JG', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000123', '00000005', 'YS_HQ_PERSON.FKSZD', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000124', '00000005', 'YS_HQ_PERSON.XL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000125', '00000005', 'YS_HQ_PERSON.ZC', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000126', '00000005', 'YS_HQ_PERSON.BLYS', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000127', '00000005', 'YS_HQ_PERSON.ZL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000128', '00000005', 'YS_HQ_PERSON.CJGZSJ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000129', '00000005', 'YS_HQ_PERSON.JRBDWSJ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000130', '00000005', 'YS_HQ_PERSON.YGLX', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000131', '00000005', 'YS_HQ_PERSON.SJ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000132', '00000005', 'YS_HQ_PERSON.JTDH', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000133', '00000005', 'YS_HQ_PERSON.DZYJ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000134', '00000005', 'YS_HQ_PERSON.JTZC', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000135', '00000005', 'YS_HQ_PERSON.HTQS', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000136', '00000005', 'YS_HQ_PERSON.HTQE', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000137', '00000005', 'YS_HQ_PERSON.XL1', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000138', '00000005', 'YS_HQ_PERSON.BGDATE', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000139', '00000005', 'YS_HQ_PERSON.BLDATE', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000140', '00000005', 'YS_HQ_PERSON.ISZZJY', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000141', '00000005', 'YS_HQ_PERSON.LXJ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000142', '00000005', 'YS_HQ_PERSON.GWQK', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000143', '00000005', 'YS_HQ_PERSON.JYBJ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000144', '00000005', 'YS_HQ_PERSON.GZJL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000145', '00000005', 'YS_HQ_PERSON.SHGX', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000146', '00000005', 'YS_HQ_PERSON.JCJL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000147', '00000005', 'YS_HQ_PERSON.ZWQK', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000148', '00000005', 'YS_HQ_PERSON.PXJL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000149', '00000005', 'YS_HQ_PERSON.DBJL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000150', '00000005', 'YS_HQ_PERSON.HTQK', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000151', '00000005', 'YS_HQ_PERSON.SBQK', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000152', '00000005', 'YS_HQ_PERSON.TJJL', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000153', '00000005', 'YS_HQ_PERSON.BZ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000154', '00000005', 'YS_HQ_PERSON.ZZMM', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000155', '00000005', 'YS_HQ_PERSON.CSRQ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000156', '00000005', 'YS_HQ_PERSON.PHOTO', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000157', '00000005', 'YS_HQ_PERSON.MZ', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000158', '00000005', 'YS_HQ_PERSON.SFZHM', '1', '1', '0', ' ', '0');";
			strSQL = strSQL
					+ "insert into COLL_CONFIG_OPERATE_FIELD (ID, FID, FIELD, ISDISPLAY, ISEDIT, ISMUSTFILL, DEFAULT1, ISFORCE) values ('00000159', '00000005', 'YS_HQ_PERSON.HYZK', '1', '1', '0', ' ', '0');";

			strSQL = strSQL
					+ "delete from COLL_DOC_CONFIG;insert into COLL_DOC_CONFIG (ID, DOCNAME, TEMPLAET, OTHERFIELD, MAINTABLE, DOCTYPE, DCODE) values ('00000002', '项目审批表单', 'bid_dlht_sp.htm', '00000086', '0011', '1', '6bf50f953299f82564d1538eb395cf3f');";
			strSQL = strSQL
					+ "insert into COLL_DOC_CONFIG (ID, DOCNAME, TEMPLAET, OTHERFIELD, MAINTABLE, DOCTYPE, DCODE) values ('00000001', '人事档案管理', 'oa_hq_person.htm', '00000001', '0001', '0', '4698ad491f2a55374eec16a955aca10d');";

			strSQL = strSQL
					+ "insert into COLL_DOC_CONFIG (ID, DOCNAME, TEMPLAET, OTHERFIELD, MAINTABLE, DOCTYPE, DCODE) values ('00000003', '人事档案管理-手机端', 'oa_hq_person_phone.htm', '00000001', '0001', '0', '');";
			strSQL = strSQL
					+ "insert into COLL_DOC_CONFIG (ID, DOCNAME, TEMPLAET, OTHERFIELD, MAINTABLE, DOCTYPE, DCODE) values ('00000004', '项目审批表单-手机端', 'bid_dlht_sp_phone.htm', '00000086', '0011', '1', '');";
			strSQL = strSQL
					+ "insert into COLL_DOC_CONFIG (ID, DOCNAME, TEMPLAET, OTHERFIELD, MAINTABLE, DOCTYPE, DCODE) values ('00000005', '人事档案管理-选项卡式模板', 'oa_hq_person_tabs.htm', '00000001', '0001', '0', '');";

			strSQL = strSQL + "delete from COLL_DOC_PRINT;";
			strSQL = strSQL
					+ "insert into COLL_DOC_PRINT (ID, DOCID, TEMPLAET, PAGE) values ('00000001', '00000001', 'oa_hq_person.htm', 1);";
			strSQL = strSQL
					+ "insert into COLL_DOC_PRINT (ID, DOCID, TEMPLAET, PAGE) values ('00000002', '00000002', 'bid_dlht_sp.htm', 1);";
			strSQL = strSQL
					+ "insert into COLL_DOC_PRINT (ID, DOCID, TEMPLAET, PAGE) values ('00000003', '00000005', 'oa_hq_person.htm', 1);";

			dbengine.ExecuteSQLs(strSQL.split(";"));

			// 流程----------

			strSQL = "delete from FLOW_CONFIG_ACTIVITY;insert into FLOW_CONFIG_ACTIVITY (ID, FID, IDENTIFICATION, NAME, DESC1, TYPE, ORDER1, ISSIGN, ASTRATEGY, CSTRATEGY, CNUM, OPENHELP, XYCSS, ATTTYPE, ATTNUM, ISNOTE, FORMPATH, ISMESSAGE, MESSAGE, ISSAVE1, ISSAVE2, ISLEAVE1, ISLEAVE2, ADDFORMPATH, ADDFORMWIDTH, ADDFORMHEIGHT, ADDFORMMESSAGE, ISBRANCH) values ('0000000001', '0000000001', 'FA0000000001', '开始', null, '1', 0, null, null, null, null, null, 'n1/0000000001/开始活动/0000000004/3/49px/254px', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY (ID, FID, IDENTIFICATION, NAME, DESC1, TYPE, ORDER1, ISSIGN, ASTRATEGY, CSTRATEGY, CNUM, OPENHELP, XYCSS, ATTTYPE, ATTNUM, ISNOTE, FORMPATH, ISMESSAGE, MESSAGE, ISSAVE1, ISSAVE2, ISLEAVE1, ISLEAVE2, ADDFORMPATH, ADDFORMWIDTH, ADDFORMHEIGHT, ADDFORMMESSAGE, ISBRANCH) values ('0000000003', '0000000001', 'FA0000000003', '结束', null, '2', 10000, null, null, null, null, null, 'n1/0000000003/结束活动//3/625px/162px', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY (ID, FID, IDENTIFICATION, NAME, DESC1, TYPE, ORDER1, ISSIGN, ASTRATEGY, CSTRATEGY, CNUM, OPENHELP, XYCSS, ATTTYPE, ATTNUM, ISNOTE, FORMPATH, ISMESSAGE, MESSAGE, ISSAVE1, ISSAVE2, ISLEAVE1, ISLEAVE2, ADDFORMPATH, ADDFORMWIDTH, ADDFORMHEIGHT, ADDFORMMESSAGE, ISBRANCH) values ('0000000004', '0000000001', 'FA0000000004', '填表', null, '3', 1, '0', '1', '1', 0, '1', 'n1/0000000004/填表/0000000005,0000000006/3/226px/254px', '2', 0, null, null, null, null, '1', '1', '0', '0', null, null, null, null, '0');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY (ID, FID, IDENTIFICATION, NAME, DESC1, TYPE, ORDER1, ISSIGN, ASTRATEGY, CSTRATEGY, CNUM, OPENHELP, XYCSS, ATTTYPE, ATTNUM, ISNOTE, FORMPATH, ISMESSAGE, MESSAGE, ISSAVE1, ISSAVE2, ISLEAVE1, ISLEAVE2, ADDFORMPATH, ADDFORMWIDTH, ADDFORMHEIGHT, ADDFORMMESSAGE, ISBRANCH) values ('0000000005', '0000000001', 'FA0000000005', '部门经理审批', null, '3', 2, '1', '1', '1', 0, '1', 'n1/0000000005/部门经理审批/0000000006,0000000004/3/426px/54px', '4', 0, null, null, null, null, null, null, null, null, null, null, null, null, '1');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY (ID, FID, IDENTIFICATION, NAME, DESC1, TYPE, ORDER1, ISSIGN, ASTRATEGY, CSTRATEGY, CNUM, OPENHELP, XYCSS, ATTTYPE, ATTNUM, ISNOTE, FORMPATH, ISMESSAGE, MESSAGE, ISSAVE1, ISSAVE2, ISLEAVE1, ISLEAVE2, ADDFORMPATH, ADDFORMWIDTH, ADDFORMHEIGHT, ADDFORMMESSAGE, ISBRANCH) values ('0000000006', '0000000001', 'FA0000000006', '总经理审批', null, '3', 3, '1', '1', '1', 0, '1', 'n1/0000000006/总经理审批/0000000003/3/426px/338px', '4', 0, null, null, null, null, null, null, null, null, null, null, null, null, '1');";

			strSQL = strSQL
					+ "delete from FLOW_CONFIG_ACTIVITY_BUTTON;insert into FLOW_CONFIG_ACTIVITY_BUTTON (ID, FID, BUTTONID, BWHERE) values ('000000000014', '0000000004', '00000008', null);";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_BUTTON (ID, FID, BUTTONID, BWHERE) values ('000000000017', '0000000006', '00000007', null);";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_BUTTON (ID, FID, BUTTONID, BWHERE) values ('000000000018', '0000000006', '00000003', null);";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_BUTTON (ID, FID, BUTTONID, BWHERE) values ('000000000013', '0000000004', '00000007', null);";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_BUTTON (ID, FID, BUTTONID, BWHERE) values ('000000000015', '0000000005', '00000007', null);";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_BUTTON (ID, FID, BUTTONID, BWHERE) values ('000000000016', '0000000005', '00000008', null);";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_BUTTON (ID, FID, BUTTONID, BWHERE) values ('000000000019', '0000000005', '00000005', null);";

			strSQL = strSQL
					+ "delete from FLOW_CONFIG_ACTIVITY_CONNE;insert into FLOW_CONFIG_ACTIVITY_CONNE (ID, FID, CID, SID, EID, NAME, DESC1, WHERE1, TYPE, ISNEED, ISATT) values ('0000000001', '0000000001', '0000000004', '0000000001', '', null, null, null, '6', '0', '0');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_CONNE (ID, FID, CID, SID, EID, NAME, DESC1, WHERE1, TYPE, ISNEED, ISATT) values ('0000000002', '0000000001', '0000000005', '0000000004', '', null, null, null, '6', '0', '0');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_CONNE (ID, FID, CID, SID, EID, NAME, DESC1, WHERE1, TYPE, ISNEED, ISATT) values ('0000000003', '0000000001', '0000000006', '0000000004', '', null, null, null, '6', '0', '0');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_CONNE (ID, FID, CID, SID, EID, NAME, DESC1, WHERE1, TYPE, ISNEED, ISATT) values ('0000000004', '0000000001', '0000000006', '0000000005', '', null, null, null, '6', '0', '0');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_CONNE (ID, FID, CID, SID, EID, NAME, DESC1, WHERE1, TYPE, ISNEED, ISATT) values ('0000000005', '0000000001', '0000000004', '', '0000000005', null, null, null, '6', '0', '0');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_CONNE (ID, FID, CID, SID, EID, NAME, DESC1, WHERE1, TYPE, ISNEED, ISATT) values ('0000000006', '0000000001', '0000000003', '0000000006', '', null, null, null, '6', '0', '0');";

			strSQL = strSQL
					+ "delete from FLOW_CONFIG_ACTIVITY_GROUP;insert into FLOW_CONFIG_ACTIVITY_GROUP (ID, ACTIVITYID, GROUPID) values ('0000000031', '0000000005', '2');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_GROUP (ID, ACTIVITYID, GROUPID) values ('0000000034', '0000000006', '2');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_GROUP (ID, ACTIVITYID, GROUPID) values ('0000000027', '0000000004', '1');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_GROUP (ID, ACTIVITYID, GROUPID) values ('0000000028', '0000000004', '2');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_GROUP (ID, ACTIVITYID, GROUPID) values ('0000000032', '0000000005', '3');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_GROUP (ID, ACTIVITYID, GROUPID) values ('0000000033', '0000000006', '1');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_GROUP (ID, ACTIVITYID, GROUPID) values ('0000000035', '0000000006', '3');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_GROUP (ID, ACTIVITYID, GROUPID) values ('0000000029', '0000000004', '3');";
			strSQL = strSQL
					+ "insert into FLOW_CONFIG_ACTIVITY_GROUP (ID, ACTIVITYID, GROUPID) values ('0000000030', '0000000005', '1');";

			strSQL = strSQL
					+ "delete from FLOW_CONFIG_PACKAGE;insert into FLOW_CONFIG_PACKAGE (ID, IDENTIFICATION, NAME, DESC1, STATUS, ICO, CREATEPSN, CREATEDATE, FID) values ('001', 'FP520000001', '演示流程', null, '1', '4', '5200000000000001', '2012-07-30', '000');";

			strSQL = strSQL
					+ "delete from FLOW_CONFIG_PROCESS;insert into FLOW_CONFIG_PROCESS (ID, IDENTIFICATION, NAME, DESC1, FLOWPACKAGE, DOCID,DOCIDPHO, STATUS, CREATEPSN, CREATEDATE, CODE, ICO, TYPE, CNUM, FORMTYPE, ISDELETEFORM, DCODE) values ('0000000001', 'WF00000001', '项目评审审批', null, '001', '00000002','00000004', '1', '0100000000000002', '2012-08-01', null, '1', '1', 0, '1', '1', '1af6cf46d363b0aa5da0d14236aaf8e4');";

			dbengine.ExecuteSQLs(strSQL.split(";"));

			// 数据查询引擎

			strSQL = "delete from QUERY_CONFIG_BRELATION;insert into QUERY_CONFIG_BRELATION (ID, FID, BID) values ('00000007', '00000002', '00000003');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_BRELATION (ID, FID, BID) values ('00000005', '00000001', '00000001');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_BRELATION (ID, FID, BID) values ('00000006', '00000001', '00000002');";

			strSQL = strSQL
					+ "insert into QUERY_CONFIG_BRELATION (ID, FID, BID) values ('00000008', '00000003', '00000003');";

			strSQL = strSQL
					+ "delete from QUERY_CONFIG_ORDER;insert into QUERY_CONFIG_ORDER (ID, FID, FIELD, TYPE) values ('00000004', '00000002', 'YS_HQ_PERSON.PCODE', '1');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_ORDER (ID, FID, FIELD, TYPE) values ('00000003', '00000001', 'YS_HQ_PERSON.PCODE', '1');";

			strSQL = strSQL
					+ "insert into QUERY_CONFIG_ORDER (ID, FID, FIELD, TYPE) values ('00000005', '00000003', 'YS_HQ_PERSON.PCODE', '1');";

			strSQL = strSQL
					+ "delete from QUERY_CONFIG_PARAMETER;insert into QUERY_CONFIG_PARAMETER (ID, FID, BID, NAME, FIELD) values ('00000002', '00000002', '00000003', 'ID', 'YS_HQ_PERSON.ID');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_PARAMETER (ID, FID, BID, NAME, FIELD) values ('00000001', '00000001', '00000002', 'ID', 'YS_HQ_PERSON.ID');";

			strSQL = strSQL
					+ "insert into QUERY_CONFIG_PARAMETER (ID, FID, BID, NAME, FIELD) values ('00000003', '00000003', '00000003', 'ID', 'YS_HQ_PERSON.ID');";

			strSQL = strSQL
					+ "delete from QUERY_CONFIG_QUERYFIELD;insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000048', '00000002', 'YS_HQ_PERSON.SFZHM', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000049', '00000002', 'YS_HQ_PERSON.XB', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000050', '00000002', 'YS_HQ_PERSON.NL', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000051', '00000002', 'YS_HQ_PERSON.ZW', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000052', '00000002', 'YS_HQ_PERSON.GS', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000053', '00000002', 'YS_HQ_PERSON.PGH', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000054', '00000002', 'YS_HQ_PERSON.ZC', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000055', '00000002', 'YS_HQ_PERSON.BLYS', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000056', '00000002', 'YS_HQ_PERSON.CJGZSJ', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000057', '00000002', 'YS_HQ_PERSON.ZL', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000058', '00000002', 'YS_HQ_PERSON.YGLX', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000059', '00000002', 'YS_HQ_PERSON.SJ', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000060', '00000002', 'YS_HQ_PERSON.ISZZJY', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000046', '00000002', 'YS_HQ_PERSON.DEPT', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000047', '00000002', 'YS_HQ_PERSON.NAME', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000031', '00000001', 'YS_HQ_PERSON.DEPT', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000032', '00000001', 'YS_HQ_PERSON.NAME', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000033', '00000001', 'YS_HQ_PERSON.SFZHM', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000034', '00000001', 'YS_HQ_PERSON.XB', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000035', '00000001', 'YS_HQ_PERSON.NL', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000036', '00000001', 'YS_HQ_PERSON.ZW', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000037', '00000001', 'YS_HQ_PERSON.GS', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000038', '00000001', 'YS_HQ_PERSON.PGH', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000039', '00000001', 'YS_HQ_PERSON.ZC', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000040', '00000001', 'YS_HQ_PERSON.BLYS', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000041', '00000001', 'YS_HQ_PERSON.CJGZSJ', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000042', '00000001', 'YS_HQ_PERSON.ZL', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000043', '00000001', 'YS_HQ_PERSON.YGLX', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000044', '00000001', 'YS_HQ_PERSON.SJ', '0', '0', '0', null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000045', '00000001', 'YS_HQ_PERSON.ISZZJY', '0', '0', '0', null);";

			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000061', '00000003', 'YS_HQ_PERSON.DEPT', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000062', '00000003', 'YS_HQ_PERSON.NAME', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000063', '00000003', 'YS_HQ_PERSON.SFZHM', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000064', '00000003', 'YS_HQ_PERSON.XB', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000065', '00000003', 'YS_HQ_PERSON.NL', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000066', '00000003', 'YS_HQ_PERSON.ZW', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000067', '00000003', 'YS_HQ_PERSON.GS', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000068', '00000003', 'YS_HQ_PERSON.PGH', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000069', '00000003', 'YS_HQ_PERSON.ZC', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000070', '00000003', 'YS_HQ_PERSON.BLYS', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000071', '00000003', 'YS_HQ_PERSON.CJGZSJ', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000072', '00000003', 'YS_HQ_PERSON.ZL', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000073', '00000003', 'YS_HQ_PERSON.YGLX', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000074', '00000003', 'YS_HQ_PERSON.SJ', null, null, null, null);";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_QUERYFIELD (ID, FID, FIELD, ISPRECISION, ISMUST, ISDAY, DVALUE) values ('00000075', '00000003', 'YS_HQ_PERSON.ISZZJY', null, null, null, null);";

			strSQL = strSQL
					+ "delete from QUERY_CONFIG_SHOWFIELD;insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000099', '00000002', 'YS_HQ_PERSON.DEPT', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000100', '00000002', 'YS_HQ_PERSON.XB', '1', '70', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000101', '00000002', 'YS_HQ_PERSON.NL', '1', '70', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000102', '00000002', 'YS_HQ_PERSON.ZW', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000103', '00000002', 'YS_HQ_PERSON.GS', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000104', '00000002', 'YS_HQ_PERSON.XL', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000105', '00000002', 'YS_HQ_PERSON.ZC', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000106', '00000002', 'YS_HQ_PERSON.BLYS', '1', '160', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000107', '00000002', 'YS_HQ_PERSON.ZL', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000108', '00000002', 'YS_HQ_PERSON.CJGZSJ', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000109', '00000002', 'YS_HQ_PERSON.JRBDWSJ', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000110', '00000002', 'YS_HQ_PERSON.YGLX', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000111', '00000002', 'YS_HQ_PERSON.SJ', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000112', '00000002', 'YS_HQ_PERSON.JTDH', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000113', '00000002', 'YS_HQ_PERSON.DZYJ', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000114', '00000002', 'YS_HQ_PERSON.JTZC', '1', '250', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000115', '00000002', 'YS_HQ_PERSON.HTQS', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000116', '00000002', 'YS_HQ_PERSON.HTQE', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000117', '00000002', 'YS_HQ_PERSON.XL1', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000118', '00000002', 'YS_HQ_PERSON.BGDATE', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000119', '00000002', 'YS_HQ_PERSON.BLDATE', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000120', '00000002', 'YS_HQ_PERSON.ISZZJY', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000121', '00000002', 'YS_HQ_PERSON.LXJ', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000122', '00000002', 'YS_HQ_PERSON.GWQK', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000123', '00000002', 'YS_HQ_PERSON.ZZMM', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000124', '00000002', 'YS_HQ_PERSON.CSRQ', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000125', '00000002', 'YS_HQ_PERSON.MZ', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000126', '00000002', 'YS_HQ_PERSON.SFZHM', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000127', '00000002', 'YS_HQ_PERSON.HYZK', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000128', '00000002', 'YS_HQ_PERSON.ID', '0', null, '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000097', '00000002', 'YS_HQ_PERSON.PGH', '1', '70', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000098', '00000002', 'YS_HQ_PERSON.NAME', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000065', '00000001', 'YS_HQ_PERSON.PGH', '1', '70', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000066', '00000001', 'YS_HQ_PERSON.NAME', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000067', '00000001', 'YS_HQ_PERSON.DEPT', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000068', '00000001', 'YS_HQ_PERSON.XB', '1', '70', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000069', '00000001', 'YS_HQ_PERSON.NL', '1', '70', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000070', '00000001', 'YS_HQ_PERSON.ZW', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000071', '00000001', 'YS_HQ_PERSON.GS', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000072', '00000001', 'YS_HQ_PERSON.XL', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000073', '00000001', 'YS_HQ_PERSON.ZC', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000074', '00000001', 'YS_HQ_PERSON.BLYS', '1', '160', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000075', '00000001', 'YS_HQ_PERSON.ZL', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000076', '00000001', 'YS_HQ_PERSON.CJGZSJ', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000077', '00000001', 'YS_HQ_PERSON.JRBDWSJ', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000078', '00000001', 'YS_HQ_PERSON.YGLX', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000079', '00000001', 'YS_HQ_PERSON.SJ', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000080', '00000001', 'YS_HQ_PERSON.JTDH', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000081', '00000001', 'YS_HQ_PERSON.DZYJ', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000082', '00000001', 'YS_HQ_PERSON.JTZC', '1', '250', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000083', '00000001', 'YS_HQ_PERSON.HTQS', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000084', '00000001', 'YS_HQ_PERSON.HTQE', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000085', '00000001', 'YS_HQ_PERSON.XL1', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000086', '00000001', 'YS_HQ_PERSON.BGDATE', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000087', '00000001', 'YS_HQ_PERSON.BLDATE', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000088', '00000001', 'YS_HQ_PERSON.ISZZJY', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000089', '00000001', 'YS_HQ_PERSON.LXJ', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000090', '00000001', 'YS_HQ_PERSON.GWQK', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000091', '00000001', 'YS_HQ_PERSON.ZZMM', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000092', '00000001', 'YS_HQ_PERSON.CSRQ', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000093', '00000001', 'YS_HQ_PERSON.MZ', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000094', '00000001', 'YS_HQ_PERSON.SFZHM', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000095', '00000001', 'YS_HQ_PERSON.HYZK', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000096', '00000001', 'YS_HQ_PERSON.ID', '0', null, '0');";

			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000129', '00000003', 'YS_HQ_PERSON.PGH', '1', '70', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000130', '00000003', 'YS_HQ_PERSON.NAME', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000131', '00000003', 'YS_HQ_PERSON.DEPT', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000132', '00000003', 'YS_HQ_PERSON.XB', '1', '70', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000133', '00000003', 'YS_HQ_PERSON.NL', '1', '70', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000134', '00000003', 'YS_HQ_PERSON.ZW', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000135', '00000003', 'YS_HQ_PERSON.GS', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000136', '00000003', 'YS_HQ_PERSON.XL', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000137', '00000003', 'YS_HQ_PERSON.ZC', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000138', '00000003', 'YS_HQ_PERSON.BLYS', '1', '160', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000139', '00000003', 'YS_HQ_PERSON.ZL', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000140', '00000003', 'YS_HQ_PERSON.CJGZSJ', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000141', '00000003', 'YS_HQ_PERSON.JRBDWSJ', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000142', '00000003', 'YS_HQ_PERSON.YGLX', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000143', '00000003', 'YS_HQ_PERSON.SJ', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000144', '00000003', 'YS_HQ_PERSON.JTDH', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000145', '00000003', 'YS_HQ_PERSON.DZYJ', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000146', '00000003', 'YS_HQ_PERSON.JTZC', '1', '250', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000147', '00000003', 'YS_HQ_PERSON.HTQS', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000148', '00000003', 'YS_HQ_PERSON.HTQE', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000149', '00000003', 'YS_HQ_PERSON.XL1', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000150', '00000003', 'YS_HQ_PERSON.BGDATE', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000151', '00000003', 'YS_HQ_PERSON.BLDATE', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000152', '00000003', 'YS_HQ_PERSON.ISZZJY', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000153', '00000003', 'YS_HQ_PERSON.LXJ', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000154', '00000003', 'YS_HQ_PERSON.GWQK', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000155', '00000003', 'YS_HQ_PERSON.ZZMM', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000156', '00000003', 'YS_HQ_PERSON.CSRQ', '1', '80', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000157', '00000003', 'YS_HQ_PERSON.MZ', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000158', '00000003', 'YS_HQ_PERSON.SFZHM', '1', '120', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000159', '00000003', 'YS_HQ_PERSON.HYZK', '1', '100', '0');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_SHOWFIELD (ID, FID, FIELD, ISSHOW, COLWIDTH, ISNUMBER) values ('00000160', '00000003', 'YS_HQ_PERSON.ID', '0', null, '0');";

			strSQL = strSQL
					+ "delete from QUERY_CONFIG_TABLE;insert into QUERY_CONFIG_TABLE (ID, NAME, MAINTABLE, TYPE, BID, IFFIRSTDATA, IFMULSEL, IFCOMBTN, CID, DCODE) values ('00000001', '人事档案管理(数据操作式的查询)', 'YS_HQ_PERSON', '2', '00000002', '1', '0', '1', ' ', '244f249066f73495e44cb45d988b5c23');";
			strSQL = strSQL
					+ "insert into QUERY_CONFIG_TABLE (ID, NAME, MAINTABLE, TYPE, BID, IFFIRSTDATA, IFMULSEL, IFCOMBTN, CID, DCODE) values ('00000002', '人事档案查询(一般查询)', 'YS_HQ_PERSON', '1', '00000003', '1', '0', '1', ' ', 'dafdaad448fa308e8b5e7c9736f5ab6c');";

			strSQL = strSQL
					+ "insert into QUERY_CONFIG_TABLE (ID, NAME, MAINTABLE, TYPE, BID, IFFIRSTDATA, IFMULSEL, IFCOMBTN, CID, DCODE) values ('00000003', '人事档案查询(查询条件与查询结果页面分离)', 'YS_HQ_PERSON', '5', '00000003', '1', '0', '1', ' ', 'dafdaad448fa308e8b5e7c9736f5ab6c');";

			strSQL = strSQL + "delete from ANALYSE_STATISTICS_MAIN;";
			strSQL = strSQL + "delete from ANALYSE_STATISTICS_CFIELD;";
			strSQL = strSQL + "delete from ANALYSE_STATISTICS_WHERE;";
			strSQL = strSQL + "delete from ANALYSE_STATISTICS_CWHERE;";
			strSQL = strSQL + "delete from ANALYSE_STATISTICS_CUSTOM;";
			strSQL = strSQL + "delete from ANALYSE_STATISTICS_CCONNECTION;";

			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_MAIN (ID, STATISTICSNAME, SDESC, TABLEID, TIMESTYPE, PLANARTABLE, PLANARFIELD, CPLANARFIELD, WHEREVALUE, EXCELTEMPLATE, PLANARFIELDNAME, WISMATCH, CJOIN, ISUNIT, SINPUTTYPE, SINPUTPAGE, ISAGV, ISSUM, ISSHOWTYPE, SHOWLINK, TIMETEMPLATE, CODETABLE, TBUTTON, ISNUMBER, ADDFIELD, DCODE) values ('00000001', '按学历和年龄情况统计表', '按学历和年龄情况统计表', 'YS_HQ_PERSON', '1', 'YS_CODE_XL', 'CODE', 'XL', null, null, 'NAME', '0', '1', '0', '1', null, '0', '1', '1', null, '00000001.htm', null, null, ' ', null, null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_MAIN (ID, STATISTICSNAME, SDESC, TABLEID, TIMESTYPE, PLANARTABLE, PLANARFIELD, CPLANARFIELD, WHEREVALUE, EXCELTEMPLATE, PLANARFIELDNAME, WISMATCH, CJOIN, ISUNIT, SINPUTTYPE, SINPUTPAGE, ISAGV, ISSUM, ISSHOWTYPE, SHOWLINK, TIMETEMPLATE, CODETABLE, TBUTTON, ISNUMBER, ADDFIELD, DCODE) values ('00000002', '按部门及年龄情况统计表', '按部门及年龄情况统计表', 'YS_HQ_PERSON', '1', 'BPIP_UNIT', 'UNITID', 'DEPT', null, null, 'UNITNAME', '0', '1', '0', '1', null, '0', '1', '1', null, '00000002.htm', null, null, '1', null, null);";

			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000001', '00000001', 'C01', '人数', 'COUNT(ID)', 1, '0', 'FIELD2', null, null, 0, '01', 'YS_HQ_PERSON', 'XL', '1', null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000002', '00000001', 'C02', '22岁以下', 'COUNT(YS_HQ_PERSON.ID)', 1, '0', 'FIELD3', '[YS_HQ_PERSON.NL]<22', null, 0, '02', 'YS_HQ_PERSON', 'XL', '1', null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000003', '00000001', 'C03', '22-25', 'count(ID)', 1, '0', 'FIELD4', '[YS_HQ_PERSON.NL]>=22 AND [YS_HQ_PERSON.NL]<=25', null, 0, '03', 'YS_HQ_PERSON', 'XL', '1', null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000004', '00000001', 'C04', '26-30', 'count(ID)', 1, '0', 'FIELD5', '[YS_HQ_PERSON.NL]>=26 AND [YS_HQ_PERSON.NL]<=30', null, 0, '04', 'YS_HQ_PERSON', 'XL', '1', null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000005', '00000001', 'C05', '31-40', 'count(ID)', 1, '0', 'FIELD6', '[YS_HQ_PERSON.NL]>=31 AND [YS_HQ_PERSON.NL]<=40', null, 0, '05', 'YS_HQ_PERSON', 'XL', '1', null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000006', '00000001', 'C06', '41-50', 'count(ID)', 1, '0', 'FIELD7', '[YS_HQ_PERSON.NL]>=41 AND [YS_HQ_PERSON.NL]<=50', null, 0, '06', 'YS_HQ_PERSON', 'XL', '1', null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000007', '00000001', 'C07', '50以上', 'count(ID)', 1, '0', 'FIELD8', '[YS_HQ_PERSON.NL]>=50', null, 0, '07', 'YS_HQ_PERSON', 'XL', '1', null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000008', '00000002', 'C01', '人数', 'COUNT(ID)', 1, '0', 'FIELD2', null, null, 0, '01', 'YS_HQ_PERSON', 'DEPT', '1', null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000009', '00000002', 'C02', '22岁以下', 'COUNT(YS_HQ_PERSON.ID)', 1, '0', 'FIELD3', '[YS_HQ_PERSON.NL]<22', null, 0, '02', 'YS_HQ_PERSON', 'DEPT', '1', null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000010', '00000002', 'C03', '22-25', 'count(ID)', 1, '0', 'FIELD4', '[YS_HQ_PERSON.NL]>=22 AND [YS_HQ_PERSON.NL]<=25', null, 0, '03', 'YS_HQ_PERSON', 'DEPT', '1', null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000011', '00000002', 'C04', '26-30', 'count(ID)', 1, '0', 'FIELD5', '[YS_HQ_PERSON.NL]>=26 AND [YS_HQ_PERSON.NL]<=30', null, 0, '04', 'YS_HQ_PERSON', 'DEPT', '1', null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000012', '00000002', 'C05', '31-40', 'count(ID)', 1, '0', 'FIELD6', '[YS_HQ_PERSON.NL]>=31 AND [YS_HQ_PERSON.NL]<=40', null, 0, '05', 'YS_HQ_PERSON', 'DEPT', '1', null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000013', '00000002', 'C06', '41-50', 'count(ID)', 1, '0', 'FIELD7', '[YS_HQ_PERSON.NL]>=41 AND [YS_HQ_PERSON.NL]<=50', null, 0, '06', 'YS_HQ_PERSON', 'DEPT', '1', null);";
			strSQL = strSQL
					+ "insert into ANALYSE_STATISTICS_CFIELD (ID, FID, SFIELDNAME, SFIELDSHOWNAME, EXPRESSIONS, DISTINCTION, ISSHOW, SAVEFIELD, EXPRESSIONSWHERE, ADDSIGN, RADIXPOINT, SHOWCODE, TABLEID, CPLANARFIELD, CJOIN, CPLANARESPECIAL) values ('000000000014', '00000002', 'C07', '50以上', 'count(ID)', 1, '0', 'FIELD8', '[YS_HQ_PERSON.NL]>=50', null, 0, '07', 'YS_HQ_PERSON', 'DEPT', '1', null);";

			dbengine.ExecuteSQLs(strSQL.split(";"));
		}
	}
}