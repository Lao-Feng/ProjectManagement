package zr.zrpower.common.util;

public class StringWork {

	public StringWork() {
	}

	public String NewID(String oldID) {
		int id = 0;
		try {
			id = Integer.parseInt(oldID);
		} catch (NumberFormatException ex) {
		}
		id++;
		String str = "";
		int IDlen = oldID.length();
		int intLen = Integer.toString(id).length();
		for (int i = 0; i < IDlen - intLen; i++)
			str = str + "0";

		return str + Integer.toString(id);
	}

	public String UnSplit(String str[], String strChar) {
		String strTemp = "";
		if (str != null) {
			for (int i = 0; i < str.length; i++)
				strTemp = strTemp + str[i] + strChar;

			if (strTemp != null && strTemp.length() > 1)
				strTemp = strTemp.substring(0, strTemp.length() - strChar.length());
		}
		return strTemp;
	}

	public String UnSplit(String str[]) {
		String strTemp = "";
		if (str != null) {
			for (int i = 0; i < str.length; i++)
				strTemp = strTemp + "'" + str[i] + "',";

			if (strTemp != null && strTemp.length() > 1)
				strTemp = strTemp.substring(0, strTemp.length() - 1);
		}
		return strTemp;
	}

	public boolean IsInArrary(String str, String arrStr[]) {
		boolean returnValue = false;
		if (str != null && arrStr != null) {
			for (int i = 0; i < arrStr.length; i++)
				if (arrStr[i].equals(str))
					returnValue = true;

		}
		return returnValue;
	}

	public boolean IsArrary(Object obj) {
		boolean rv = false;
		try {
			String str[] = (String[]) (String[]) obj;
			if (str != null && str.length > 0)
				rv = true;
		} catch (Exception ex) {
			rv = false;
		}
		return rv;
	}

	public String ShowHTML(String str) {
		if (str != null) {
			StringBuffer sb = new StringBuffer();
			str = str.trim();
			for (int i = 0; i < str.length(); i++) {
				char ch = str.charAt(i);
				switch (ch) {
				case 13: // '\r'
					sb.append("<br>");
					break;

				case 32: // ' '
					sb.append("&nbsp;");
					break;

				case 34: // '"'
					sb.append("&quot;");
					break;

				case 39: // '\''
					sb.append("&quot;");
					break;

				case 38: // '&'
					sb.append("&amp;");
					break;

				case 60: // '<'
					sb.append("&lt;");
					break;

				case 62: // '>'
					sb.append("&gt;");
					break;

				default:
					sb.append(ch);
					break;
				}
			}
			return sb.toString();
		} else {
			return "&nbsp;";
		}
	}

	public String CutString(String str, int length) {
		StringBuffer sb = new StringBuffer();
		str = str.trim();
		int iLen = str.length();
		if (iLen < length)
			length = iLen;
		for (int i = 0; i < length; i++) {
			char ch = str.charAt(i);
			if (ch == '\r') {
				sb.append("...");
				return sb.toString();
			}
			sb.append(ch);
		}
		String strTmp = sb.toString();
		if (strTmp.indexOf("<br>") > 0)
			strTmp = strTmp.substring(0, strTmp.indexOf("<br>"));
		return strTmp + "...";
	}

	public String CutLastZero(String str, int len) {
		String strTmp = "";
		for (int i = 0; i < len; i++) {
			strTmp = strTmp + "0";
		}
		if (str != null) {
			for (; str.length() > len&& str.substring(str.length() - len).equals(strTmp); 
					str = str.substring(0, str.length() - len))
				;
		}
		return str;
	}

	public boolean EnableToInt(String str) {
		boolean result = true;
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			result = false;
		}
		return result;
	}

	/**
	 * 功能或作用：比较字符串大小是否相等
	 * 
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @param Flag 0:区分大小写,1:忽略大小写
	 * @return 0表示相等 -1表示str1小于str2 1表示str1大于str2
	 */
	public int CampareString(String str1, String str2, int Flag) {
		int Result = 0;
		switch (Flag) {
		case 0:
			if (str1.compareTo(str2) < 0) {
				Result = -1;
			} else if (str1.compareTo(str2) > 0) {
				Result = 1;
			}
			break;
		case 1:
			if (str1.compareToIgnoreCase(str2) < 0) {
				Result = -1;
			} else if (str1.compareToIgnoreCase(str2) > 0) {
				Result = 1;
			}
		}
		return Result;
	}
}