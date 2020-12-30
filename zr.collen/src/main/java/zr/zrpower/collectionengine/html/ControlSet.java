package zr.zrpower.collectionengine.html;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>Title:自定义表单引擎</p>
 * <p>
 * Description: 页面元素控件集
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author NFZR
 */
public class ControlSet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map<String, Object> mControls = null;
	Map<String, Object> mTxtCtrl = null;
	Map<String, Object> mNumCtrl = null;
	Map<String, Object> mFltCtrl = null;
	Map<String, Object> mDatCtrl = null;
	Map<String, Object> mDteCtrl = null;
	Map<String, Object> mSelCtrl = null;
	Map<String, Object> mRdiCtrl = null;
	Map<String, Object> mDitCtrl = null;
	Map<String, Object> mAraCtrl = null;

	/**
	 * 构造函数
	 */
	public ControlSet() {
		mControls = new HashMap<String, Object>();
		mTxtCtrl = new HashMap<String, Object>();
		mNumCtrl = new HashMap<String, Object>();
		mFltCtrl = new HashMap<String, Object>();
		mDatCtrl = new HashMap<String, Object>();
		mDteCtrl = new HashMap<String, Object>();
		mSelCtrl = new HashMap<String, Object>();
		mRdiCtrl = new HashMap<String, Object>();
		mDitCtrl = new HashMap<String, Object>();
		mAraCtrl = new HashMap<String, Object>();
	}

	/**
	 * 给元素控件集添加控件
	 * 
	 * @param ctrl
	 *            ControlBase 控件
	 */
	public void addControl(ControlBase ctrl) {
		String packagename = "zr.zrpower.collectionengine.html.";
		mControls.put(ctrl.getName(), ctrl);
		if (ctrl.getClass().getName().equals(packagename + "TextControl")) {
			mTxtCtrl.put(ctrl.getName(), ctrl);
		}
		if (ctrl.getClass().getName().equals(packagename + "NubmerControl")) {
			mNumCtrl.put(ctrl.getName(), ctrl);
		}
		if (ctrl.getClass().getName().equals(packagename + "FloatControl")) {
			mFltCtrl.put(ctrl.getName(), ctrl);
		}
		if (ctrl.getClass().getName().equals(packagename + "DateControl")) {
			mDatCtrl.put(ctrl.getName(), ctrl);
		}
		if (ctrl.getClass().getName().equals(packagename + "DateTimeControl")) {
			mDteCtrl.put(ctrl.getName(), ctrl);
		}
		if (ctrl.getClass().getName().equals(packagename + "SelectControl")) {
			mSelCtrl.put(ctrl.getName(), ctrl);
		}
		if (ctrl.getClass().getName().equals(packagename + "RedioControl")) {
			mRdiCtrl.put(ctrl.getName(), ctrl);
		}
		if (ctrl.getClass().getName().equals(packagename + "DictControl")) {
			mDitCtrl.put(ctrl.getName(), ctrl);
		}
		if (ctrl.getClass().getName().equals(packagename + "TextAreaControl")) {
			mAraCtrl.put(ctrl.getName(), ctrl);
		}
	}

	/**
	 * 获取总控件数
	 * 
	 * @return int
	 */
	public int getTotalCount() {
		return mControls.size();
	}

	/**
	 * 获取文本控件数
	 * 
	 * @return int
	 */
	public int getTextControlCount() {
		return mTxtCtrl.size();
	}

	/**
	 * 获取整型控件数
	 * 
	 * @return int
	 */
	public int getNumberControlCount() {
		return mNumCtrl.size();
	}

	/**
	 * 获取浮点型控件数
	 * 
	 * @return int
	 */
	public int getFloatControlCount() {
		return mFltCtrl.size();
	}

	/**
	 * 获取日期型控件数
	 * 
	 * @return int
	 */
	public int getDateControlCount() {
		return mDatCtrl.size();
	}

	/**
	 * 获取日期时间型控件数
	 * 
	 * @return int
	 */
	public int getDateTimeControlCount() {
		return mDteCtrl.size();
	}

	/**
	 * 获取点选型控件数
	 * 
	 * @return int
	 */
	public int getRedioCotrolCount() {
		return mRdiCtrl.size();
	}

	/**
	 * 获取下拉选择型控件数
	 * 
	 * @return int
	 */
	public int getSelectControlCount() {
		return mSelCtrl.size();
	}

	/**
	 * 获取字典型控件数
	 * 
	 * @return int
	 */
	public int getDictControlCount() {
		return mDitCtrl.size();
	}

	/**
	 * 获取多行文本域控件数
	 * 
	 * @return int
	 */
	public int getAreaControlCount() {
		return mAraCtrl.size();
	}

	/**
	 * 获取验证表单输入验证JavaScript函数
	 * 
	 * @return String
	 */
	public String getValidateJS(String addJs) {
		StringBuffer sbScript = new StringBuffer();
		sbScript.append("function saveColl(){\n").append(addJs).append("\n");
		String saCtrl[] = getNameList(mControls);
		if (saCtrl != null && saCtrl.length > 0) {
			for (int i = 0; i < saCtrl.length; i++) {
				sbScript.append(((ControlBase) mControls.get(saCtrl[i])).getValidateJS());
			}
		}
		sbScript.append("document.frmColl.ISREVALUE.value=\"1\";\n");
		sbScript.append("document.frmColl.submit();return true;\n");
		sbScript.append("}\n");

		sbScript.append("function notre_saveColl(){\n").append(addJs).append("\n");
		if (saCtrl != null && saCtrl.length > 0) {
			for (int i = 0; i < saCtrl.length; i++) {
				sbScript.append(((ControlBase) mControls.get(saCtrl[i])).getValidateJS());
			}
		}
		sbScript.append("document.frmColl.ISREVALUE.value=\"0\";\n");
		sbScript.append("document.frmColl.submit();return true;\n");
		sbScript.append("}\n");

		return sbScript.toString();
	}

	private String[] getNameList(Map<String, Object> hash) {
		String[] result = null;
		if (hash != null && hash.size() > 0) {
			Iterator<Entry<String, Object>> names;
			int i, j;
			i = hash.size();
			result = new String[i];
			names = hash.entrySet().iterator();
			j = 0;
			while (names.hasNext()) {
				Entry<String, Object> entry = names.next();
				result[j] = entry.getKey();
				j++;
			}
		}
		return result;
	}
}