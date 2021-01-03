package zr.zrpower.dao;


import zr.zrpower.model.dbmanage.BPIP_FIELD;
import zr.zrpower.model.dbmanage.BPIP_TABLE;

/**
 * 数据库表操作接口
 * @author ftl
 *  
 */
public interface BaseDbaDao {
	
	/**
	 * 通用sql查询数据库操作语句（）
	 * @param Sql
	 * @return
	 */
	int dbaSelectSql(String sql);
	
	/**
	 * 通用sql执行数据库操作语句（）
	 * @param Sql
	 * @return
	 */
	int dbaSql( String Sql );
	
    /**
     * Mysql 操作数据库表（）
     * @param model
     */
	int MysqlCreatTable( BPIP_TABLE model );
	//修改表key（）
	int MysqlAlterTableKey( BPIP_TABLE model );
	int MysqlCreatFiled( BPIP_FIELD model );
	int MysqlAlterFiled( BPIP_FIELD model );
	//执行字典表创建（）
	int MysqlCreatCodeTable( BPIP_TABLE model );
	//删除字段（）
	int MysqlDropFiled(BPIP_FIELD model);
    
	
	/**
     * Sqlserver 操作数据库表（）
     * @param model
     */
	int SqlserverCreatTable( BPIP_TABLE model );
	//修改表key（）
	int SqlserverAlterTableKey( BPIP_TABLE model );
	int SqlserverCreatFiled( BPIP_FIELD model );
	int SqlserverAlterFiled( BPIP_FIELD model );
	//执行字典表创建（）
	int SqlserverCreatCodeTable( BPIP_TABLE model );
	//删除字段（）
	int SqlserverDropFiled(BPIP_FIELD model);
	
	/**
     * Oracle 操作数据库表
     * @param model
     */
	int OracleCreatTable( BPIP_TABLE model );
	//修改表key
	int OracleAlterTableKey( BPIP_TABLE model );
	int OracleCreatFiled( BPIP_FIELD model );
	int OracleAlterFiled( BPIP_FIELD model );
	
	int OracleCreatCodeTable( BPIP_TABLE model );
	//删除字段（）
	int OracleDropFiled(BPIP_FIELD model);
	//增加主键
	int OracleKeyTable( BPIP_TABLE model );
	//增加表备注
	int OracleCommentTable( BPIP_TABLE model );
	//增加备注
	int OracleCommentField( BPIP_FIELD model );
	
	/**
	 * 通用删除表、删除字段、执行（）
	 * @param tableName
	 */
	int DropTable( String tableName );
	
}
