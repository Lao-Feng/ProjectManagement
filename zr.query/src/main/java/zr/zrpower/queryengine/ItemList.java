package zr.zrpower.queryengine;

import zr.zrpower.common.db.DBRow;
import zr.zrpower.common.db.DBTag;
import zr.zrpower.common.db.DBType;

import java.util.ArrayList;
import java.util.List;

public class ItemList {
	private List<Object> arrayList;

	public ItemList() {
		arrayList = new ArrayList<Object>();
	}

	public void addItem(ItemField item) {
		arrayList.add(item);
	}

	public int getItemCount() {
		return arrayList.size();
	}

	public ItemField getItemField(int i) {
		return (ItemField) arrayList.get(i);
	}

	public int fullFromDbRow(DBRow dr) {
		ItemField item = new ItemField();
		item.setTableEnName(dr.getTableName());
		item.setName(dr.getTableName() + "." + dr.Column("FIELDNAME").getString());
		item.setChineseName(dr.Column("CHINESENAME").getString());
		item.setCodeTable(dr.Column("DICTTABLE").getString());

		if (item.getCodeTable() != null && !item.getCodeTable().equals("")) {
			item.setIsCode(true);
		}
		item.setLength(dr.Column("FIELDLENGTH").getInteger());
		item.setType(SwitchDbType(dr.Column("FIELDTYPE").getString()));
		item.setIsNull(SwitchIsNull(dr.Column("ISNULL").getString()));
		if (dr.Column("FIELDTAG").getInteger() == DBTag.USER.getValue()) {
			item.setIsUser(true);
		}
		if (dr.Column("FIELDTAG").getInteger() == DBTag.UNIT.getValue()) {
			item.setIsUnit(true);

			item.setUserField1(dr.Column("AUTO1").getString(), "");
			item.setUserField2(dr.Column("AUTO2").getString(), "");
			item.setUserField3(dr.Column("AUTO3").getString(), "");
			// -----------------------------------
		}
		item.setCodeInput(dr.Column("TAGEXT").getInteger());
		arrayList.add(item);

		return 0;
	}

	/**
	 * 将从数据库中读取的文本数据类型转换为dbType型
	 *
	 * @param strType
	 *            String
	 * @return dbType
	 */
	private DBType SwitchDbType(String strType) {
		DBType dbtype = null;
		int iType = 0;
		if (strType != null) {
			iType = Integer.parseInt(strType);
		}
		switch (iType) {
		case 1:
			dbtype = DBType.STRING;
			break;
		case 2:
			dbtype = DBType.FLOAT;
			break;
		case 3:
			dbtype = DBType.BLOB;
			break;
		case 4:
			dbtype = DBType.DATE;
			break;
		case 5:
			dbtype = DBType.DATETIME;
			break;
		case 6:
			dbtype = DBType.LONG;
			break;
		case 7:
			dbtype = DBType.CLOB;
			break;
		}
		return dbtype;
	}

	/**
	 * 将从数据库中读取的文本是否为空转换为boolean型
	 *
	 * @param strNULL
	 *            String
	 * @return boolean
	 */
	private boolean SwitchIsNull(String strNULL) {
		boolean result = true;
		if (strNULL != null) {
			if (strNULL.equals("1")) {
				result = false;
			}
		}
		return result;
	}

}