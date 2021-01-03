package com.yonglilian.collectionengine;

import com.yonglilian.service.impl.CollectionServiceImpl;
import zr.zrpower.common.db.*;
import zr.zrpower.common.util.DateWork;
import zr.zrpower.common.util.SysPreperty;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ItemList {
	private Map<String, Object> mList;
	private boolean ReadOnlyMode = false;
	private boolean bAllEditMode = false;
	private DBEngine dbengine;// 数据库引擎

	public ItemList(boolean readonly) {
		mList = new HashMap<String, Object>();
		ReadOnlyMode = readonly;
		dbengine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbengine.initialize();

	}

	/**
	 * 区分所有字段都可以编辑的情况
	 * @param readonly
	 * @param bAllEdit
	 */
	public ItemList(boolean readonly, boolean bAllEdit) {
		mList = new HashMap<String, Object>();
		ReadOnlyMode = readonly;
		bAllEditMode = bAllEdit;
		dbengine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbengine.initialize();
	}

	public void addItem(ItemField item) {
		if (ReadOnlyMode) {
			item.setIsShow(true);
			item.setIsWrite(false);
			// item.setIsNull(true);
		}
		// 所有字段可编辑模式
		if (bAllEditMode) {
			item.setIsShow(true);
			item.setIsWrite(true);
		}

		mList.put(item.getName().toUpperCase(), item);
	}

	public void removeItem(String itemname) {
		mList.remove(itemname.toUpperCase());
	}

	public int getItemCount() {
		return mList.size();
	}

	public ItemField getItem(String itemname) {
		return (ItemField) mList.get(itemname.toUpperCase());
	}

	public String[] getItemNameList() {
		Iterator<Entry<String, Object>> names;
		int i, j;
		i = mList.size();
		String[] result = new String[i];
		names = mList.entrySet().iterator();
		j = 0;
		while (names.hasNext()) {
			Entry<String, Object> entry = names.next();
			result[j] = entry.getKey();
			j++;
		}
		return result;
	}

	/**
	 * 此函数需要修改，带表名的字段情况
	 * @param flowList
	 */
	public void fullFromFlowField(FlowList flowList) {
		if (flowList != null && flowList.getItemCount() > 0) {
			String saFlowName[] = flowList.getItemNameList();
			String saFieldName[] = getItemNameList();
			for (int i = 0; i < saFlowName.length; i++) {
				for (int j = 0; j < saFieldName.length; j++) {
					if (saFlowName[i].toUpperCase().equals(saFieldName[j].toUpperCase())) {
						// -----根据设置是否允许为空，解决姓名在只读状态下，必须选择填入的情况
						((ItemField) mList.get(saFieldName[j])).setIsNull(flowList.getItem(saFlowName[i]).isNull());
						// ------------------------------------------------------------------
						if (ReadOnlyMode) {
							// ((ItemField)mList.get(saFieldName[j])).setIsNull(true);
							((ItemField) mList.get(saFieldName[j])).setIsWrite(false);
							((ItemField) mList.get(saFieldName[j])).setIsShow(true);
							((ItemField) mList.get(saFieldName[j])).setIsForce(false);
						} else {
							if (bAllEditMode) {
								((ItemField) mList.get(saFieldName[j])).setIsWrite(true);
								((ItemField) mList.get(saFieldName[j])).setIsShow(true);
							} else {
								// ((ItemField)mList.get(saFieldName[j])).setIsNull(flowList.getItem(saFlowName[i]).isNull());
								((ItemField) mList.get(saFieldName[j]))
										.setIsWrite(flowList.getItem(saFlowName[i]).isWrite());
								((ItemField) mList.get(saFieldName[j]))
										.setIsShow(flowList.getItem(saFlowName[i]).isShow());
								((ItemField) mList.get(saFieldName[j]))
										.setIsForce(flowList.getItem(saFlowName[i]).isForce());
							}
						}
						((ItemField) mList.get(saFieldName[j]))
								.setDefaultValue(flowList.getItem(saFlowName[i]).getDefault());

						if (flowList.getItem(saFlowName[i]).isForce()) {
							((ItemField) mList.get(saFieldName[j]))
									.setValue(flowList.getItem(saFlowName[i]).getDefault());
						}
					}
				}
			}
		}
	}

	public int fullFromDbRow(DBRow dr, String Alias) {
		String[] columnName = dr.getAllColumnName();
		int i = 0;
		for (i = 0; i < columnName.length; i++) {
			String colName = columnName[i];
			if (Alias != null && !Alias.equals("")) {
				colName = (Alias + "$" + colName).toUpperCase();
			}
			if (mList.get(colName) != null) {
				mList.remove(colName);
			}
			ItemField item = new ItemField();
			DBColumn dc = dr.Column(columnName[i]);
			item.setName(colName);
			item.setChineseName(dc.getChineseName());
			item.setCodeTable(dc.getCodeTable());
			item.setCodeValue(dc.getCValue());
			item.setLength(dc.getLength());
			item.setType(dc.getType());
			item.setIsNull(dc.isNull());
			item.setCodeInput(dc.getTagExt());
			if (item.getType().getValue() == DBType.DATE.getValue()) {
				item.setValue(DateWork.DateToString(dc.getDate()));
			}
			if (item.getType().getValue() == DBType.DATETIME.getValue()) {
				item.setValue(DateWork.DateTimeToString(dc.getDate()));
			}
			if (item.getType().getValue() == DBType.FLOAT.getValue() && dc.getFloat() != 0) {
				item.setValue(Float.toString(dc.getFloat()));
			}
			if (item.getType().getValue() == DBType.LONG.getValue() && dc.getInteger() != 0) {
				item.setValue(Integer.toString(dc.getInteger()));
			}
			if (item.getType().getValue() == DBType.STRING.getValue()) {
				item.setValue(dc.getString());
			}
			if (item.getType().getValue() == DBType.CLOB.getValue()) {
				item.setValue(dc.getClob());
			}
			if (item.getCodeTable() != null && !item.getCodeTable().equals("")) {
				item.setIsCode(true);
			}
			if (dc.getTag().getValue() == DBTag.USER.getValue()) {
				item.setIsUser(true);
			}
			if (dc.getTag().getValue() == DBTag.UNIT.getValue()) {
				item.setIsUnit(true);
				item.setUserField1(dc.getAuto1(), Alias);
				item.setUserField2(dc.getAuto2(), Alias);
				item.setUserField3(dc.getAuto3(), Alias);
			}
			if (ReadOnlyMode) {
				item.setIsShow(true);
				item.setIsWrite(false);
				// item.setIsNull(false);
				item.setIsForce(false);
			}
			// 设置上传图片大小
			if (item.getType().getValue() == DBType.BLOB.getValue()) {
				item.setBlobSize(getBlobSize(dr.getTableName(), columnName[i]));
			} else {
				item.setBlobSize(0);
			}

			mList.put(colName, item);
		}
		return i;
	}

	// 得到上传照片的大小
	private int getBlobSize(String tableName, String fieldName) {
		int rtnValue = 0;
		try {
			CollectionServiceImpl collectService = new CollectionServiceImpl();
			rtnValue = collectService.getBlobSize(tableName, fieldName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rtnValue;
	}
}