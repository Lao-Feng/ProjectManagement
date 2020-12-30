package zr.zrpower.queryengine.mode;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：QUERY_CONFIG_QUERYFIELD.java
 * </p>
 * <p>
 * 中文解释：查询字段配置表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表查询字段配置表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class QUERY_CONFIG_QUERYFIELD {
	private String ID;
	private String FID;
	private String FIELD;
	private String ISPRECISION;
	private String ISMUST;
	private String ISDAY;
	private String DVALUE;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getFID() {
		return FID;
	}

	public void setFID(String fID) {
		FID = fID;
	}

	public String getFIELD() {
		return FIELD;
	}

	public void setFIELD(String fIELD) {
		FIELD = fIELD;
	}

	public String getISPRECISION() {
		return ISPRECISION;
	}

	public void setISPRECISION(String iSPRECISION) {
		ISPRECISION = iSPRECISION;
	}

	public String getISMUST() {
		return ISMUST;
	}

	public void setISMUST(String iSMUST) {
		ISMUST = iSMUST;
	}

	public String getISDAY() {
		return ISDAY;
	}

	public void setISDAY(String iSDAY) {
		ISDAY = iSDAY;
	}

	public String getDVALUE() {
		return DVALUE;
	}

	public void setDVALUE(String dVALUE) {
		DVALUE = dVALUE;
	}
}