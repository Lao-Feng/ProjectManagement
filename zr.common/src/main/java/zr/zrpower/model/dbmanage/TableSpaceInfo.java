package zr.zrpower.model.dbmanage;

import java.io.Serializable;

/**
 * <p>Title: zrpower平台</p>
 * <p>
 * Description: 表空间信息实体
 * </p>
 * <p>
 * Copyright: Copyright
 * </p>
 * <p>
 * Company:
 * </p>
 * @author nfzr
 * 
 */
public class TableSpaceInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String TABLESPACE_NAME;
	public float TOTAL;
	public float USED;
	public float FREE;
	public float UsedPercent;
	public float FreePercent;

	public TableSpaceInfo() {
		TABLESPACE_NAME = "";
		TOTAL = 0;
		USED = 0;
		FREE = 0;
		UsedPercent = 0;
		FreePercent = 0;

	}

}