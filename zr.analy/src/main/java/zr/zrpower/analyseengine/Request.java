package zr.zrpower.analyseengine;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Request implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Object> mList;

	public Request() {
		mList = new HashMap<String, Object>();
	}

	public void addItem(String name, String value) {
		mList.put(name.toUpperCase(), value);
	}

	public void removeItem(String name) {
		mList.remove(name);
	}

	public int getCount() {
		return mList.size();
	}

	public String getItem(String name) {
		String result = "";
		String strTmp = "";
		try {
			if (mList.get(name.toUpperCase()) != null) {
				result = (String) mList.get(name.toUpperCase());
			}
			//-------------------解决多选的问题-------------------
			else {
				for (int i = 0; i < 5; i++) {
					if (mList.get((name + "_checkbox" + String.valueOf(i)).toUpperCase()) != null) {
						strTmp = (String) mList.get((name + "_checkbox" + String.valueOf(i)).toUpperCase());
						if (result.length() == 0) {
							result = strTmp;
						} else {
							result = result + "," + strTmp;
						}
					}
				}
			}
			// ------------------------------------------
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
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

	@SuppressWarnings("rawtypes")
	public void fullItem(HttpServletRequest request) {
		try {
			Enumeration enu = request.getParameterNames();
			while (enu.hasMoreElements()) {
				String name = (String) enu.nextElement();
				String values[] = request.getParameterValues(name);
				String value = "";
				if (values.length > 0) {
					value = values[0];
					if (value.trim().equals("请选择")) {
						value = "";
					}
				}
				mList.put(name.toUpperCase(), value);
			}
		} catch (Exception ex) {
		}
	}
}