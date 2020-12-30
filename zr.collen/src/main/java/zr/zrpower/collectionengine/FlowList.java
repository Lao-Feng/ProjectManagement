package zr.zrpower.collectionengine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class FlowList {
	private Map<String, Object> mList;

	public FlowList() {
		mList = new HashMap<String, Object>();
	}

	public void addItem(FlowField item) {
		mList.put(item.getName().toUpperCase(), item);
	}

	public void removeItem(String itemname) {
		mList.remove(itemname.toUpperCase());
	}

	public int getItemCount() {
		return mList.size();
	}

	public FlowField getItem(String itemname) {
		return (FlowField) mList.get(itemname.toUpperCase());
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
}