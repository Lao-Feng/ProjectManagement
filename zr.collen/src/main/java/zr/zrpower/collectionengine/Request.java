package zr.zrpower.collectionengine;

import zr.zrpower.common.web.FileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

public class Request implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Object> mList;

	public Request(HttpServletRequest request) {
		mList = new HashMap<String, Object>();
		this.fullItem(request);
	}

	public int getCount() {
		return mList.size();
	}

	public void appendItem(String name, String value) {
		if (mList.get(name.toUpperCase()) == null) {
			mList.put(name.toUpperCase(), value);
		}
	}

	public String getStringItem(String name) {
		String result = null;
		try {
			if (mList.get(name.toUpperCase()) != null) {
				result = (String) mList.get(name.toUpperCase());
			}
		} catch (Exception ex) {
		}
		return result;
	}

	public byte[] getBlobItem(String name) {
		byte[] result = null;
		try {
			if (mList.get(name.toUpperCase()) != null) {
				result = (byte[]) mList.get(name.toUpperCase());
			}
		} catch (Exception ex) {
		}
		return result;
	}

	public String[] getItemNameList() {
		int i, j;
		i = mList.size();
		String[] result = new String[i];
		Set<String> keys = mList.keySet();
		j = 0;
		for (String key : keys) {
			result[j] = key;
			j++;
		}
		return result;
	}

	private void fullItem(HttpServletRequest request) {
		try {
			if (request != null) {
				String contentType = request.getHeader("content-type");
				if (contentType != null && contentType.indexOf("multipart/form-data") > -1) {
					FileUpload fileUpload = new FileUpload();
					fileUpload.setSize(1024 * 1024 * 10);
					fileUpload.setSourceFile(request);
					Map<String, Object> htField = fileUpload.getRtFields();
					if (htField != null && htField.size() > 0) {
						Iterator<Entry<String, Object>> enu = htField.entrySet().iterator();
						while (enu.hasNext()) {
							Entry<String, Object> entry = enu.next();
							String name = entry.getKey();
							if (htField.get(name) != null) {
								mList.put(name.toUpperCase(), htField.get(name));
							}
						}
					}
				} else {
					Enumeration<String> enu = request.getParameterNames();
					while (enu.hasMoreElements()) {
						String name = (String) enu.nextElement();
						String values[] = request.getParameterValues(name);
						String value = "";
						if (values.length > 0) {
							value = values[0];
						}
						mList.put(name.toUpperCase(), value);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}