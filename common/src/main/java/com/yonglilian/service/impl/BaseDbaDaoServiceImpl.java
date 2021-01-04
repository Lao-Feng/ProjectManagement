package com.yonglilian.service.impl;

import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.mapper.BaseDbaDaoMapper;
import com.yonglilian.model.dbmanage.BPIP_FIELD;
import com.yonglilian.model.dbmanage.BPIP_TABLE;
import com.yonglilian.service.BaseDbaDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;

/**
 * 接口实现
 * 
 * @author nfzr
 *
 */
@Service
public class BaseDbaDaoServiceImpl implements BaseDbaDaoService {
	/** The UserServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseDbaDaoServiceImpl.class);
	/**
	 * 数据库类型，1：Oracle，2：MSsql，3：MySQL
	 */
	String dataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	/** 数据库引擎. */
	private DBEngine dbEngine;
	private static int clients = 0;
	/** 数据库数据层. */
	@Autowired
	private BaseDbaDaoMapper baseDbaDaoMapper;

	/**
	 * 构造方法
	 */
	public BaseDbaDaoServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
		}
		clients++;
	}

	@Override
	public boolean CreatTable(BPIP_TABLE model) {
		int count=-1;
		if(dataBaseType.equals("1")) {//1：Oracle
			//oracle 创建表
			if(model.getTABLETYPE().equals("1")) {//字典表
				count=baseDbaDaoMapper.OracleCreatCodeTable(model);
			}else {
				count=baseDbaDaoMapper.OracleCreatTable(model);
			}
			//增加表备注
			baseDbaDaoMapper.OracleCommentTable(model);
			//增加字段备注
			if(model.getTABLETYPE().equals("1")) {
				addComantField(model.getTABLENAME(),"CODE","编码");
				addComantField(model.getTABLENAME(),"NAME","名称");
				addComantField(model.getTABLENAME(),"SPELL","拼音首字母");
				addComantField(model.getTABLENAME(),"ISSHOW","是否禁用，1=禁止，0或者空=启用");
			}else {
				addComantField(model.getTABLENAME(),model.getPRIMARYKEY(),"主键id");
			}
			//增加表主键 
			baseDbaDaoMapper.OracleKeyTable(model);
		}else if(dataBaseType.equals("2")) {//2：MSsql
			if(model.getTABLETYPE().equals("1")) {//字典表
				count=baseDbaDaoMapper.SqlserverCreatCodeTable(model);
			}else {
				count=baseDbaDaoMapper.SqlserverCreatTable(model);
			}
		}else if(dataBaseType.equals("3")) {//3：MySQL
			if(model.getTABLETYPE().equals("1")) {//字典表
				count=baseDbaDaoMapper.MysqlCreatCodeTable(model);
			}else {
				//表、key、备注一键创建
				count=baseDbaDaoMapper.MysqlCreatTable(model);
			}
		}
		if(count>=0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean DropTable(String tableName) {
		baseDbaDaoMapper.DropTable(tableName);
		return true;
	}

	@Override
	public boolean AlterTableKey(BPIP_TABLE model) {
		String Sql="";
		if(dataBaseType.equals("1")) {//1：Oracle
			//删除主键约束
			Sql="ALTER TABLE "+model.getTABLENAME()+" DROP CONSTRAINT PK_"+model.getTABLENAME()+"";
			baseDbaDaoMapper.dbaSql(Sql);
			//创建新的主键
			baseDbaDaoMapper.OracleAlterTableKey(model);
		}else if(dataBaseType.equals("2")) {//2：MSsql
			//删除主键约束 
			Sql="ALTER TABLE [dbo].["+model.getTABLENAME()+"] DROP CONSTRAINT PK_"+model.getTABLENAME()+"";
			baseDbaDaoMapper.dbaSql(Sql);
			//创建新的主键
			baseDbaDaoMapper.SqlserverAlterTableKey(model);
		}else if(dataBaseType.equals("3")) {//3：MySQL
			//删除主键约束
			Sql="ALTER TABLE `"+model.getTABLENAME()+"` DROP PRIMARY KEY";
			baseDbaDaoMapper.dbaSql(Sql);
			//创建新的主键
			baseDbaDaoMapper.MysqlAlterTableKey(model);
		}
		return true;
	}

	@Override
	public boolean CreatField(BPIP_FIELD model) {
		String Sql="";
		if(dataBaseType.equals("1")) {//1：Oracle
			//创建表字段
			baseDbaDaoMapper.OracleCreatFiled(model);
			//创建表字段备注
			baseDbaDaoMapper.OracleCommentField(model);
			//判断是否为主键
			if(model.getISKEY()!=null&&model.getISKEY().trim().equals("1")) {
				//删除主键
				Sql="alter table "+model.getTABLENAME()+" drop constraint PK_"+model.getTABLENAME()+" cascade";
				baseDbaDaoMapper.dbaSql(Sql);
				//新增主键
				Sql="alter table "+model.getTABLENAME()+" add constraint PK_"+model.getTABLENAME()+" primary key ("+model.getFIELDNAME()+")";
				baseDbaDaoMapper.dbaSql(Sql);
			}
		}else if(dataBaseType.equals("2")) {//2：MSsql
			//创建字段
			baseDbaDaoMapper.SqlserverCreatFiled(model);
			//如果是主键，还有执行
			if(model.getISKEY()!=null&&model.getISKEY().trim().equals("1")) {
				//删除主键
				Sql="ALTER TABLE [dbo].["+model.getTABLENAME()+"] DROP CONSTRAINT PK_"+model.getTABLENAME()+"";
				baseDbaDaoMapper.dbaSql(Sql);
				//增加主键
				Sql="ALTER TABLE [dbo].["+model.getTABLENAME()+"] ADD CONSTRAINT PK_"+model.getTABLENAME()+" PRIMARY KEY CLUSTERED  ("+model.getFIELDNAME()+") ";
				baseDbaDaoMapper.dbaSql(Sql);
			}
		}else if(dataBaseType.equals("3")) {//3：MySQL
			//一句sql执行完成
			//baseDbaDaoMapper.MysqlCreatFiled(model);
		}
		return true;
	}

	@Override
	public boolean DropField(BPIP_FIELD model) {
		String sql="";
		if(dataBaseType.equals("1")) {//1：Oracle  
			sql="alter table "+model.getTABLENAME()+" drop column "+model.getFIELDNAME()+"";
		}else if(dataBaseType.equals("2")) {//2：MSsql
			sql="ALTER TABLE [dbo].["+model.getTABLENAME()+"] DROP COLUMN ["+model.getFIELDNAME()+"]";
		}else if(dataBaseType.equals("3")) {//3：MySQL
			sql="ALTER TABLE `"+model.getTABLENAME()+"` DROP COLUMN `"+model.getFIELDNAME()+"`";
		}
		baseDbaDaoMapper.dbaSql(sql);
		return true;
	}

	@Override
	public boolean AlterField(BPIP_FIELD model) {
		String Sql="";
		if(dataBaseType.equals("1")) {//1：Oracle
			//只是修改表名称
			baseDbaDaoMapper.OracleAlterFiled(model);
			//创建表字段备注
			baseDbaDaoMapper.OracleCommentField(model);
			//修改字段类型及长度
			Sql="alter table "+model.getTABLENAME()+" modify "+model.getFIELDNAME()+" "+model.getFIELDTYPENAME()+"";
			if(model.getFIELDLENGTH()>0) {
				Sql=Sql+"("+model.getFIELDLENGTH()+")";
			}
			baseDbaDaoMapper.dbaSql(Sql);
			//判断是否为主键
			if(model.getISKEY()!=null&&model.getISKEY().trim().equals("1")) {
				//删除主键
				Sql="alter table "+model.getTABLENAME()+" drop constraint PK_"+model.getTABLENAME()+" cascade";
				baseDbaDaoMapper.dbaSql(Sql);
				//新增主键
				Sql="alter table "+model.getTABLENAME()+" add constraint PK_"+model.getTABLENAME()+" primary key ("+model.getFIELDNAME()+")";
				baseDbaDaoMapper.dbaSql(Sql);
			}
		}else if(dataBaseType.equals("2")) {//2：MSsql
			//只是修改字段名称
			baseDbaDaoMapper.SqlserverAlterFiled(model);
			//修改字段类型及长度
			Sql="ALTER TABLE [dbo].["+model.getTABLENAME()+"] "+model.getFIELDTYPENAME()+"";
			if(model.getFIELDLENGTH()>0) {
				Sql=Sql+"("+model.getFIELDLENGTH()+")";
			}
			if(model.getISNULL()!=null&&model.getISNULL().trim().equals("0")) {
				Sql=Sql+" NOT";
			}
			Sql=Sql+" NULL ";
			baseDbaDaoMapper.dbaSql(Sql);
			//如果是主键，还有执行
			if(model.getISKEY()!=null&&model.getISKEY().trim().equals("1")) {
				//删除主键
				Sql="ALTER TABLE [dbo].["+model.getTABLENAME()+"] DROP CONSTRAINT PK_"+model.getTABLENAME()+"";
				baseDbaDaoMapper.dbaSql(Sql);
				//增加主键
				Sql="ALTER TABLE [dbo].["+model.getTABLENAME()+"] ADD CONSTRAINT PK_"+model.getTABLENAME()+" PRIMARY KEY CLUSTERED  ("+model.getFIELDNAME()+") ";
				baseDbaDaoMapper.dbaSql(Sql);
			}
		}else if(dataBaseType.equals("3")) {//3：MySQL
			//一句搞定，修改、不为空、主键
			baseDbaDaoMapper.MysqlAlterFiled(model);
		}
		return true;
	}

	@Override
	public boolean dbaSql(String sql) {
		baseDbaDaoMapper.dbaSql(sql);
		return true;
	}

	/**
	 * 给字段添加属性描述
	 * @param TABLENAME
	 * @param FIELDNAME
	 * @param CHINESENAME
	 */
	public void addComantField(String TABLENAME,String FIELDNAME,String CHINESENAME) {
		BPIP_FIELD field = new BPIP_FIELD();
		field.setTABLENAME(TABLENAME);
		field.setFIELDNAME(FIELDNAME);
		field.setCHINESENAME(CHINESENAME);
		baseDbaDaoMapper.OracleCommentField(field);
		field=null;
	}

	@Override
	public boolean booleanField(BPIP_FIELD model) {
		String sql="";
		if(dataBaseType.equals("1")) {//1：Oracle
			sql = "select count(*) from user_tab_columns where table_name = '"+model.getTABLENAME()+"' and column_name = '"+model.getFIELDNAMEOLD()+"'";
		}else if(dataBaseType.equals("2")) {//2：MSsql 
			sql = "select  count(*)  from syscolumns where id=object_id('"+model.getTABLENAME()+"')  and name=  '"+model.getFIELDNAMEOLD()+"'";
		}else if(dataBaseType.equals("3")) {//3：MySQL
			sql = "select count(*) from information_schema.columns where table_name = '"+model.getTABLENAME()+"' and column_name = '"+model.getFIELDNAMEOLD()+"'";
		}
		int rows = baseDbaDaoMapper.dbaSelectSql(sql);
		if(rows>0) {
			return true;
		}
		return false;
	}

}