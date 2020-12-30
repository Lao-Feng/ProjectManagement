package zr.zrpower.generator.bean;

import java.util.List;


/**
 * 表数据对应实体类
 *
 *  
 */

public class TableBean {

	// 表的名称
	private String tableName;

	// 表的备注
	private String comments;

	// 表的主键
	private ColumnBean pk;

	// 表的列名(不包含主键)
	private List<ColumnBean> columns;

	// 类名(第一个字母大写)，如：sys_user => SysUser
	private String className;

	// 类名(第一个字母小写)，如：sys_user => sysUser
	private String classsname;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public ColumnBean getPk() {
		return pk;
	}

	public void setPk(ColumnBean pk) {
		this.pk = pk;
	}

	public List<ColumnBean> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnBean> columns) {
		this.columns = columns;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClasssname() {
		return classsname;
	}

	public void setClasssname(String classsname) {
		this.classsname = classsname;
	}
	
}
