package com.yonglilian.collectionengine.mode;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * Title: 已完成活动
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c)
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author NFZR
 */
public class EActivityList {
	private class EAcitivity {
		@SuppressWarnings("unused")
		public String ActID = null;
		public String DoPsn = null;
		public Date DoDate = null;

		public EAcitivity(String actID, String doPsn, Date doDate) {
			ActID = actID;
			DoPsn = doPsn;
			DoDate = doDate;
		}
	}

	private Map<String, Object> ActList = new HashMap<String, Object>();

	public EActivityList() {
	}

	public void addActivity(String ActID, String DoPsn, Date DoDate) {
		if (ActList.get(ActID) != null) {
			EAcitivity eAct = (EAcitivity) ActList.get(ActID);
			if (eAct.DoDate.getTime() < DoDate.getTime()) {
				ActList.remove(ActID);
				ActList.put(ActID, new EAcitivity(ActID, DoPsn, DoDate));
			}
		} else {
			ActList.put(ActID, new EAcitivity(ActID, DoPsn, DoDate));
		}
	}

	public int getCount() {
		return ActList.size();
	}

	public String[] getIdList() {
		Iterator<Entry<String, Object>> names;
		int i, j;
		i = ActList.size();
		String[] result = new String[i];
		names = ActList.entrySet().iterator();
		j = 0;
		while (names.hasNext()) {
			Entry<String, Object> entry = names.next();
			result[j] = entry.getKey();
			j++;
		}
		return result;
	}

	/**
	 * 得到处理人
	 * @param ActID
	 * @return
	 */
	public String getPsn(String ActID) {
		String result = null;
		if (ActList.get(ActID) != null) {
			EAcitivity ea = (EAcitivity) ActList.get(ActID);
			result = ea.DoPsn;
		}
		return result;
	}

	/**
	 * 得到处理时间
	 * @param ActID
	 * @return
	 */
	public Date getDotime(String ActID) {
		Date result = null;
		if (ActList.get(ActID) != null) {
			EAcitivity ea = (EAcitivity) ActList.get(ActID);
			result = ea.DoDate;
		}
		return result;
	}
}