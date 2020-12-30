package zr.zrpower.queryengine;

import zr.zrpower.common.db.DBType;

/**
 * <p>
 * 数据自定义表单引擎核心组件包
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright
 * </p>
 * 
 * @author not attributable
 */
public class ItemField {
	private String Name; // 名称
	private String ChineseName; // 中文名称
	private DBType Type; // 数据类型
	private int Length = 0; // 长度
	private boolean mIsCode = false; // 是否代码表
	private boolean mIsUser = false; // 是否用户表
	private boolean mIsUnit = false; // 是否单位表
	private boolean mIsShow = true; // 是否显示
	private boolean mIsWrite = true; // 是否能写入
	private boolean mIsNull = false; // 是否必填项
	private String CodeTable; // 代码表
	private String CodeValue; // 代码值
	private String DefaultValue; // 缺省值
	private String Value; // 域值
	private int CodeInput; // 代码录入方式
	private String ColWidth = "120"; // 列宽
	private String QALIGN = "0"; // 对齐方式

	private String UserField1 = null;
	private String UserField2 = null;
	private String UserField3 = null;

	private String TableEnName;

	public void setName(String name) {
		this.Name = name;
	}

	public String getName() {
		return this.Name;
	}

	public void setType(DBType type) {
		this.Type = type;
	}

	public DBType getType() {
		return this.Type;
	}

	public void setChineseName(String chnName) {
		this.ChineseName = chnName;
	}

	public String getChineseName() {
		return this.ChineseName;
	}

	public void setLength(int length) {
		this.Length = length;
	}

	public int getLength() {
		return this.Length;
	}

	public void setIsCode(boolean iscode) {
		this.mIsCode = iscode;
	}

	public boolean isCode() {
		return this.mIsCode;
	}

	public void setIsUser(boolean isuser) {
		this.mIsUser = isuser;
	}

	public boolean isUser() {
		return this.mIsUser;
	}

	public void setIsUnit(boolean isunit) {
		this.mIsUnit = isunit;
	}

	public boolean isUnit() {
		return this.mIsUnit;
	}

	public void setCodeTable(String codetable) {
		this.CodeTable = codetable;
	}

	public String getCodeTable() {
		return this.CodeTable;
	}

	public void setIsNull(boolean isnull) {
		this.mIsNull = isnull;
	}

	public boolean isNull() {
		return this.mIsNull;
	}

	public void setIsShow(boolean isshow) {
		this.mIsShow = isshow;
	}

	public boolean isShow() {
		return this.mIsShow;
	}

	public void setIsWrite(boolean iswrite) {
		this.mIsWrite = iswrite;
	}

	public boolean isWrite() {
		return this.mIsWrite;
	}

	public void setCodeValue(String codevalue) {
		this.CodeValue = codevalue;
	}

	public String getCodeValue() {
		return this.CodeValue;
	}

	public void setValue(String value) {
		this.Value = value;
	}

	public String getValue() {
		return this.Value;
	}

	public void setDefaultValue(String defaultvalue) {
		this.DefaultValue = defaultvalue;
	}

	public String getDefaultValue() {
		return this.DefaultValue;
	}

	public void setCodeInput(int inputtype) {
		this.CodeInput = inputtype;
	}

	public int getCodeInput() {
		return this.CodeInput;
	}

	public void setTableEnName(String tableEnName) {
		this.TableEnName = tableEnName;
	}

	public String getTableEnName() {
		return this.TableEnName;
	}

	public String getShowValue() {
		String value = "";
		if (this.mIsCode) {
			value = this.CodeValue;
		} else if (this.Value != null && !this.Value.equals("")) {
			value = this.Value;
		} else if (this.DefaultValue != null && !this.DefaultValue.equals("")) {
			value = this.DefaultValue;
		}
		return value;
	}

	public void setColWidth(String colWidth) {
		this.ColWidth = colWidth;
	}

	public String getColWidth() {
		return this.ColWidth;
	}

	public void setQALIGN(String QALIGN) {
		this.QALIGN = QALIGN;
	}

	public String getQALIGN() {
		return this.QALIGN;
	}

	public void setUserField1(String userfield, String Alias) {
		if (userfield != null && !userfield.equals("")) {
			if (Alias != null && !Alias.equals("")) {
				userfield = Alias + "$" + userfield;
			}
			this.UserField1 = userfield.toUpperCase();
		}
	}

	public String getUserField1() {
		return this.UserField1;
	}

	public void setUserField2(String userfield, String Alias) {
		if (userfield != null && !userfield.equals("")) {
			if (Alias != null && !Alias.equals("")) {
				userfield = Alias + "$" + userfield;
			}
			this.UserField2 = userfield.toUpperCase();
		}
	}

	public String getUserField2() {
		return this.UserField2;
	}

	public void setUserField3(String userfield, String Alias) {
		if (userfield != null && !userfield.equals("")) {
			if (Alias != null && !Alias.equals("")) {
				userfield = Alias + "$" + userfield;
			}
			this.UserField3 = userfield.toUpperCase();
		}
	}

	public String getUserField3() {
		return this.UserField3;
	}

}