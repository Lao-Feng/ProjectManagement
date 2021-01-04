package com.yonglilian.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：BPIP_USER_PHOTO.java
 * </p>
 * <p>
 * 中文解释：用户照片数据实体类
 * </p>
 * <p>
 * 作用：将数据库表用户照片映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器
 */
public class BPIP_USER_PHOTO extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数
	 */
	public BPIP_USER_PHOTO() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_USER_PHOTO");
		this.dbrow.addColumn("USERID", null, DBType.STRING); // 用户编号
		this.dbrow.addColumn("USERPHOTO", null, DBType.BLOB); // 照片
		this.dbrow.addColumn("USERIMAGE", null, DBType.STRING); // 头像
		this.dbrow.addColumn("IDIOGRAPH", null, DBType.BLOB); // 签名

		this.dbrow.setPrimaryKey("USERID");
	}

	/**
	 * 获取[用户编号]
	 * 
	 * @return String
	 */
	public String getUSERID() {
		return this.getString(this.dbrow.Column("USERID").getString());
	}

	/**
	 * 设置[用户编号]
	 * 
	 * @param USERID
	 *            String
	 */
	public void setUSERID(String USERID) {
		this.dbrow.Column("USERID").setValue(USERID);
	}

	/**
	 * 获取[照片]
	 * 
	 * @return byte[]
	 */
	public byte[] getUSERPHOTO() {
		return this.dbrow.Column("USERPHOTO").getBlob();
	}

	/**
	 * 设置[照片]
	 * 
	 * @param USERPHOTO
	 *            byte[]
	 */
	public void setUSERPHOTO(byte[] USERPHOTO) {
		this.dbrow.Column("USERPHOTO").setValue(USERPHOTO);
	}

	/**
	 * 获取[头像]
	 * 
	 * @return String
	 */
	public String getUSERIMAGE() {
		return this.getString(this.dbrow.Column("USERIMAGE").getString());
	}

	/**
	 * 设置[头像]
	 * 
	 * @param USERIMAGE
	 *            String
	 */
	public void setUSERIMAGE(String USERIMAGE) {
		this.dbrow.Column("USERIMAGE").setValue(USERIMAGE);
	}

	/**
	 * 获取[签名]
	 * 
	 * @return byte[]
	 */
	public byte[] getIDIOGRAPH() {
		return this.dbrow.Column("IDIOGRAPH").getBlob();
	}

	/**
	 * 设置[签名]
	 * 
	 * @param IDIOGRAPH
	 *            byte[]
	 */
	public void setIDIOGRAPH(byte[] IDIOGRAPH) {
		this.dbrow.Column("IDIOGRAPH").setValue(IDIOGRAPH);
	}

}
